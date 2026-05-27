package org.stellar.sdk.contract

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.io.ByteArrayOutputStream
import java.security.MessageDigest
import java.util.Optional
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.stellar.sdk.Address
import org.stellar.sdk.SorobanServer
import org.stellar.sdk.contract.exception.ContractCodeNotFoundException
import org.stellar.sdk.contract.exception.ContractInstanceNotFoundException
import org.stellar.sdk.contract.exception.ContractWasmRetrievalException
import org.stellar.sdk.contract.exception.StellarAssetContractHasNoWasmException
import org.stellar.sdk.scval.Scv
import org.stellar.sdk.xdr.ContractCodeEntry
import org.stellar.sdk.xdr.ContractDataDurability
import org.stellar.sdk.xdr.ContractDataEntry
import org.stellar.sdk.xdr.ContractExecutable
import org.stellar.sdk.xdr.ContractExecutableType
import org.stellar.sdk.xdr.ExtensionPoint
import org.stellar.sdk.xdr.Hash
import org.stellar.sdk.xdr.LedgerEntry
import org.stellar.sdk.xdr.LedgerEntryType
import org.stellar.sdk.xdr.SCContractInstance
import org.stellar.sdk.xdr.SCMap
import org.stellar.sdk.xdr.SCMapEntry
import org.stellar.sdk.xdr.SCMetaEntry
import org.stellar.sdk.xdr.SCMetaKind
import org.stellar.sdk.xdr.SCMetaV0
import org.stellar.sdk.xdr.SCVal
import org.stellar.sdk.xdr.SCValType
import org.stellar.sdk.xdr.XdrString

private const val CONTRACT_ID = "CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K"

private fun sha256(data: ByteArray): ByteArray = MessageDigest.getInstance("SHA-256").digest(data)

private fun buildMinimalWasmWithMeta(key: String, value: String): ByteArray {
  val v0 =
    SCMetaV0().apply {
      this.key = XdrString(key.toByteArray(Charsets.UTF_8))
      this.`val` = XdrString(value.toByteArray(Charsets.UTF_8))
    }
  val entry =
    SCMetaEntry().apply {
      discriminant = SCMetaKind.SC_META_V0
      this.v0 = v0
    }
  val xdr = XdrStreams.serializeScMetaEntries(listOf(entry))
  return ByteArrayOutputStream()
    .apply {
      write(WasmTestSupport.WASM_HEADER)
      write(WasmTestSupport.buildCustomSection("contractmetav0", xdr))
    }
    .toByteArray()
}

private fun contractInstanceLedgerEntryXdr(executable: ContractExecutable): String {
  val instance =
    SCContractInstance().apply {
      this.executable = executable
      storage = SCMap(arrayOf<SCMapEntry>())
    }
  val value =
    SCVal().apply {
      discriminant = SCValType.SCV_CONTRACT_INSTANCE
      this.instance = instance
    }
  val contractData =
    ContractDataEntry.builder()
      .ext(ExtensionPoint.builder().discriminant(0).build())
      .contract(Address(CONTRACT_ID).toSCAddress())
      .key(Scv.toLedgerKeyContractInstance())
      .durability(ContractDataDurability.PERSISTENT)
      .`val`(value)
      .build()
  val ledgerEntryData =
    LedgerEntry.LedgerEntryData.builder()
      .discriminant(LedgerEntryType.CONTRACT_DATA)
      .contractData(contractData)
      .build()
  return ledgerEntryData.toXdrBase64()
}

private fun contractCodeLedgerEntryXdr(code: ByteArray): String {
  val codeEntry =
    ContractCodeEntry.builder()
      .ext(ContractCodeEntry.ContractCodeEntryExt.builder().discriminant(0).build())
      .hash(Hash(sha256(code)))
      .code(code)
      .build()
  val ledgerEntryData =
    LedgerEntry.LedgerEntryData.builder()
      .discriminant(LedgerEntryType.CONTRACT_CODE)
      .contractCode(codeEntry)
      .build()
  return ledgerEntryData.toXdrBase64()
}

private fun singleEntryJson(xdr: String): String =
  """
    {
      "jsonrpc": "2.0",
      "id": "id",
      "result": {
        "entries": [
          {
            "key": "key",
            "xdr": "$xdr",
            "lastModifiedLedgerSeq": "100",
            "liveUntilLedgerSeq": "500"
          }
        ],
        "latestLedger": "100"
      }
    }
  """
    .trimIndent()

private fun emptyEntriesJson(): String =
  """
    {
      "jsonrpc": "2.0",
      "id": "id",
      "result": {
        "entries": [],
        "latestLedger": "100"
      }
    }
  """
    .trimIndent()

private fun sequentialDispatcher(instanceJson: String, codeJson: String): Dispatcher =
  object : Dispatcher() {
    var call = 0

    override fun dispatch(request: RecordedRequest): MockResponse {
      call += 1
      return if (call == 1) {
        MockResponse().setResponseCode(200).setBody(instanceJson)
      } else {
        MockResponse().setResponseCode(200).setBody(codeJson)
      }
    }
  }

class SorobanServerContractIntrospectionTest :
  FunSpec({
    lateinit var mockWebServer: MockWebServer

    beforeTest { mockWebServer = MockWebServer() }

    afterTest { mockWebServer.shutdown() }

    fun newServer(): SorobanServer = SorobanServer(mockWebServer.url("").toString())

    test("getContractWasmByHash returns code") {
      val wasm = buildMinimalWasmWithMeta("k", "v")
      val hash = sha256(wasm)
      mockWebServer.enqueue(
        MockResponse().setBody(singleEntryJson(contractCodeLedgerEntryXdr(wasm)))
      )
      mockWebServer.start()

      newServer().use { server -> server.getContractWasmByHash(hash) shouldBe wasm }
    }

    test("getContractWasmByHash missing code throws") {
      mockWebServer.enqueue(MockResponse().setBody(emptyEntriesJson()))
      mockWebServer.start()

      newServer().use { server ->
        val hash = ByteArray(32).apply { this[0] = 1 }
        shouldThrow<ContractCodeNotFoundException> { server.getContractWasmByHash(hash) }
      }
    }

    test("getContractWasmByHash non-code entry throws") {
      val executable =
        ContractExecutable.builder()
          .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_STELLAR_ASSET)
          .build()
      mockWebServer.enqueue(
        MockResponse().setBody(singleEntryJson(contractInstanceLedgerEntryXdr(executable)))
      )
      mockWebServer.start()

      newServer().use { server ->
        val hash = ByteArray(32).apply { this[0] = 1 }
        shouldThrow<ContractWasmRetrievalException> { server.getContractWasmByHash(hash) }
      }
    }

    test("getContractWasmByHash validates hash length") {
      mockWebServer.start()
      newServer().use { server ->
        shouldThrow<IllegalArgumentException> { server.getContractWasmByHash(ByteArray(16)) }
        shouldThrow<IllegalArgumentException> { server.getContractWasmByHash(null) }
      }
    }

    test("getContractWasm fetches instance then code") {
      val wasm = buildMinimalWasmWithMeta("rsver", "1.78.0")
      val executable =
        ContractExecutable.builder()
          .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_WASM)
          .wasm_hash(Hash(sha256(wasm)))
          .build()
      mockWebServer.dispatcher =
        sequentialDispatcher(
          singleEntryJson(contractInstanceLedgerEntryXdr(executable)),
          singleEntryJson(contractCodeLedgerEntryXdr(wasm)),
        )
      mockWebServer.start()

      newServer().use { server -> server.getContractWasm(CONTRACT_ID) shouldBe wasm }
    }

    test("getContractWasm stellar asset throws") {
      val executable =
        ContractExecutable.builder()
          .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_STELLAR_ASSET)
          .build()
      mockWebServer.enqueue(
        MockResponse().setBody(singleEntryJson(contractInstanceLedgerEntryXdr(executable)))
      )
      mockWebServer.start()

      newServer().use { server ->
        shouldThrow<StellarAssetContractHasNoWasmException> { server.getContractWasm(CONTRACT_ID) }
      }
    }

    test("getContractWasm missing instance throws") {
      mockWebServer.enqueue(MockResponse().setBody(emptyEntriesJson()))
      mockWebServer.start()

      newServer().use { server ->
        shouldThrow<ContractInstanceNotFoundException> { server.getContractWasm(CONTRACT_ID) }
      }
    }

    test("getContractWasm invalid contract id throws") {
      mockWebServer.start()
      newServer().use { server ->
        shouldThrow<IllegalArgumentException> { server.getContractWasm("not-a-contract") }
      }
    }

    test("getContractMeta parses fetched Wasm") {
      val wasm = buildMinimalWasmWithMeta("rsver", "1.78.0")
      val executable =
        ContractExecutable.builder()
          .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_WASM)
          .wasm_hash(Hash(sha256(wasm)))
          .build()
      mockWebServer.dispatcher =
        sequentialDispatcher(
          singleEntryJson(contractInstanceLedgerEntryXdr(executable)),
          singleEntryJson(contractCodeLedgerEntryXdr(wasm)),
        )
      mockWebServer.start()

      newServer().use { server ->
        server.getContractMeta(CONTRACT_ID).get("rsver") shouldBe Optional.of("1.78.0")
      }
    }

    test(
      "getContractWasm unknown executable kind in raw XDR throws ContractWasmRetrievalException"
    ) {
      // Encode a valid STELLAR_ASSET LedgerEntryData, then mutate the executable discriminant to
      // an unknown value. The LedgerEntryData layout places the executable discriminant at a
      // fixed offset:
      //   4 (LedgerEntryType=CONTRACT_DATA) + 4 (ext v0) + 36 (SCAddress contract) +
      //   4 (SCVal key) + 4 (durability) + 4 (SCVal val discriminant) = 56
      val executable =
        ContractExecutable.builder()
          .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_STELLAR_ASSET)
          .build()
      val xdrBase64 = contractInstanceLedgerEntryXdr(executable)
      val bytes = java.util.Base64.getDecoder().decode(xdrBase64)
      // Patch the 4 bytes at offset 56 to 99 (unknown discriminant).
      bytes[56] = 0x00
      bytes[57] = 0x00
      bytes[58] = 0x00
      bytes[59] = 0x63
      val mutated = java.util.Base64.getEncoder().encodeToString(bytes)

      mockWebServer.enqueue(MockResponse().setBody(singleEntryJson(mutated)))
      mockWebServer.start()

      newServer().use { server ->
        shouldThrow<ContractWasmRetrievalException> { server.getContractWasm(CONTRACT_ID) }
      }
    }

    test("getContractWasmByHash malformed XDR throws ContractWasmRetrievalException") {
      mockWebServer.enqueue(MockResponse().setBody(singleEntryJson("not-valid-base64!@#")))
      mockWebServer.start()

      newServer().use { server ->
        val hash = ByteArray(32).apply { this[0] = 1 }
        shouldThrow<ContractWasmRetrievalException> { server.getContractWasmByHash(hash) }
      }
    }

    test("getContractInfo parses fetched Wasm") {
      val wasm = buildMinimalWasmWithMeta("sep", "41,40")
      val executable =
        ContractExecutable.builder()
          .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_WASM)
          .wasm_hash(Hash(sha256(wasm)))
          .build()
      mockWebServer.dispatcher =
        sequentialDispatcher(
          singleEntryJson(contractInstanceLedgerEntryXdr(executable)),
          singleEntryJson(contractCodeLedgerEntryXdr(wasm)),
        )
      mockWebServer.start()

      newServer().use { server ->
        val info = server.getContractInfo(CONTRACT_ID)
        info.meta.supportedSeps() shouldBe setOf(41, 40)
        info.spec.entries.isEmpty() shouldBe true
      }
    }
  })
