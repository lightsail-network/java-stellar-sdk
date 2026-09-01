package org.stellar.sdk.contract

import com.google.gson.JsonParser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf
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
import org.stellar.sdk.contract.exception.ExternalRefNotFoundException
import org.stellar.sdk.contract.exception.StellarAssetContractHasNoWasmException
import org.stellar.sdk.scval.Scv
import org.stellar.sdk.xdr.ContractCodeEntry
import org.stellar.sdk.xdr.ContractDataDurability
import org.stellar.sdk.xdr.ContractDataEntry
import org.stellar.sdk.xdr.ContractExecutable
import org.stellar.sdk.xdr.ContractExecutableExternalRef
import org.stellar.sdk.xdr.ContractExecutableType
import org.stellar.sdk.xdr.ExtensionPoint
import org.stellar.sdk.xdr.Hash
import org.stellar.sdk.xdr.LedgerEntry
import org.stellar.sdk.xdr.LedgerEntryType
import org.stellar.sdk.xdr.LedgerKey
import org.stellar.sdk.xdr.SCAddress
import org.stellar.sdk.xdr.SCAddressType
import org.stellar.sdk.xdr.SCContractInstance
import org.stellar.sdk.xdr.SCMap
import org.stellar.sdk.xdr.SCMapEntry
import org.stellar.sdk.xdr.SCMetaEntry
import org.stellar.sdk.xdr.SCMetaKind
import org.stellar.sdk.xdr.SCMetaV0
import org.stellar.sdk.xdr.SCString
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

private const val OWNER_ID = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA"

private const val ACCOUNT_ID = "GAHJJJKMOKYE4RVPZEWZTKH5FVI4PA3VL7GK2LFNUBSGBV6OJP7TQSLX"

private fun externalRef(owner: String, tag: ByteArray): ContractExecutableExternalRef =
  ContractExecutableExternalRef.builder()
    .executable_owner(Address(owner).toSCAddress())
    .tag(SCString(XdrString(tag)))
    .build()

private fun externalRefExecutable(owner: String, tag: ByteArray): ContractExecutable =
  ContractExecutable.builder()
    .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_EXTERNAL_REF)
    .external_ref(externalRef(owner, tag))
    .build()

/** The persistent entry on the owner contract that names the Wasm, keyed by the raw tag bytes. */
private fun tagLedgerKeyXdr(owner: String, tag: ByteArray): String =
  LedgerKey.builder()
    .discriminant(LedgerEntryType.CONTRACT_DATA)
    .contractData(
      LedgerKey.LedgerKeyContractData.builder()
        .contract(Address(owner).toSCAddress())
        .key(Scv.toExecutableTag(tag))
        .durability(ContractDataDurability.PERSISTENT)
        .build()
    )
    .build()
    .toXdrBase64()

private fun tagLedgerEntryXdr(owner: String, tag: ByteArray, value: SCVal): String {
  val contractData =
    ContractDataEntry.builder()
      .ext(ExtensionPoint.builder().discriminant(0).build())
      .contract(Address(owner).toSCAddress())
      .key(Scv.toExecutableTag(tag))
      .durability(ContractDataDurability.PERSISTENT)
      .`val`(value)
      .build()
  return LedgerEntry.LedgerEntryData.builder()
    .discriminant(LedgerEntryType.CONTRACT_DATA)
    .contractData(contractData)
    .build()
    .toXdrBase64()
}

private fun requestedKeys(request: RecordedRequest): List<String> =
  JsonParser.parseString(request.body.readUtf8())
    .asJsonObject
    .getAsJsonObject("params")
    .getAsJsonArray("keys")
    .map { it.asString }

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

    test("getExternalRefWasmHash resolves the tag entry on the owner contract") {
      val tag = "my-executable".toByteArray(Charsets.UTF_8)
      val wasmHash = sha256("wasm".toByteArray())
      mockWebServer.enqueue(
        MockResponse()
          .setBody(singleEntryJson(tagLedgerEntryXdr(OWNER_ID, tag, Scv.toBytes(wasmHash))))
      )
      mockWebServer.start()

      newServer().use { server ->
        server.getExternalRefWasmHash(externalRef(OWNER_ID, tag)) shouldBe wasmHash
      }

      // The owner contract is not invoked; a single getLedgerEntries call reads its tag entry.
      mockWebServer.requestCount shouldBe 1
      requestedKeys(mockWebServer.takeRequest()) shouldBe listOf(tagLedgerKeyXdr(OWNER_ID, tag))
    }

    test("getExternalRefWasmHash rejects a null reference") {
      mockWebServer.start()
      newServer().use { server ->
        shouldThrow<IllegalArgumentException> { server.getExternalRefWasmHash(null) }
      }
    }

    test("getContractWasm follows an external reference: instance, then tag entry, then code") {
      val tag = "my-executable".toByteArray(Charsets.UTF_8)
      val wasm = buildMinimalWasmWithMeta("rsver", "1.78.0")
      val wasmHash = sha256(wasm)
      mockWebServer.enqueue(
        MockResponse()
          .setBody(
            singleEntryJson(contractInstanceLedgerEntryXdr(externalRefExecutable(OWNER_ID, tag)))
          )
      )
      mockWebServer.enqueue(
        MockResponse()
          .setBody(singleEntryJson(tagLedgerEntryXdr(OWNER_ID, tag, Scv.toBytes(wasmHash))))
      )
      mockWebServer.enqueue(
        MockResponse().setBody(singleEntryJson(contractCodeLedgerEntryXdr(wasm)))
      )
      mockWebServer.start()

      newServer().use { server -> server.getContractWasm(CONTRACT_ID) shouldBe wasm }

      mockWebServer.requestCount shouldBe 3
      mockWebServer.takeRequest() // the contract instance
      requestedKeys(mockWebServer.takeRequest()) shouldBe listOf(tagLedgerKeyXdr(OWNER_ID, tag))
    }

    test("getContractSpec follows an external reference") {
      val tag = "my-executable".toByteArray(Charsets.UTF_8)
      val wasm = buildMinimalWasmWithMeta("sep", "41,40")
      mockWebServer.enqueue(
        MockResponse()
          .setBody(
            singleEntryJson(contractInstanceLedgerEntryXdr(externalRefExecutable(OWNER_ID, tag)))
          )
      )
      mockWebServer.enqueue(
        MockResponse()
          .setBody(singleEntryJson(tagLedgerEntryXdr(OWNER_ID, tag, Scv.toBytes(sha256(wasm)))))
      )
      mockWebServer.enqueue(
        MockResponse().setBody(singleEntryJson(contractCodeLedgerEntryXdr(wasm)))
      )
      mockWebServer.start()

      newServer().use { server ->
        server.getContractInfo(CONTRACT_ID).meta.supportedSeps() shouldBe setOf(41, 40)
      }
    }

    test("keys the lookup on a binary tag without decoding it") {
      // A tag is an unbounded SCString and need not be UTF-8. A lenient decode would build the key
      // of a different entry.
      val binaryTag = byteArrayOf(0xff.toByte(), 0xfe.toByte(), 0x00, 0x41)
      mockWebServer.enqueue(
        MockResponse()
          .setBody(
            singleEntryJson(
              contractInstanceLedgerEntryXdr(externalRefExecutable(OWNER_ID, binaryTag))
            )
          )
      )
      mockWebServer.enqueue(MockResponse().setBody(emptyEntriesJson()))
      mockWebServer.start()

      newServer().use { server ->
        shouldThrow<ExternalRefNotFoundException> { server.getContractWasm(CONTRACT_ID) }
      }

      mockWebServer.takeRequest() // the contract instance
      requestedKeys(mockWebServer.takeRequest()) shouldBe
        listOf(tagLedgerKeyXdr(OWNER_ID, binaryTag))
    }

    test("missing tag entry throws ExternalRefNotFoundException naming the owner and tag") {
      val tag = "my-executable".toByteArray(Charsets.UTF_8)
      mockWebServer.enqueue(MockResponse().setBody(emptyEntriesJson()))
      mockWebServer.start()

      newServer().use { server ->
        val e =
          shouldThrow<ExternalRefNotFoundException> {
            server.getExternalRefWasmHash(externalRef(OWNER_ID, tag))
          }
        e.owner shouldBe OWNER_ID
        e.tag shouldBe tag
        e.message shouldContain OWNER_ID
        e.message shouldContain "my-executable"
      }
    }

    test("a binary tag is rendered as hex rather than lenient-decoded in the error message") {
      val binaryTag = byteArrayOf(0xff.toByte(), 0xfe.toByte())
      mockWebServer.enqueue(MockResponse().setBody(emptyEntriesJson()))
      mockWebServer.start()

      newServer().use { server ->
        val e =
          shouldThrow<ExternalRefNotFoundException> {
            server.getExternalRefWasmHash(externalRef(OWNER_ID, binaryTag))
          }
        e.tag shouldBe binaryTag
        e.message shouldContain "0xFFFE"
      }
    }

    test("a non-contract owner is rejected before any lookup") {
      // Only a contract can hold the persistent tag entry that names the Wasm.
      mockWebServer.enqueue(
        MockResponse()
          .setBody(
            singleEntryJson(
              contractInstanceLedgerEntryXdr(
                externalRefExecutable(ACCOUNT_ID, "v1".toByteArray(Charsets.UTF_8))
              )
            )
          )
      )
      mockWebServer.start()

      newServer().use { server ->
        // The reference came off the ledger, not from the caller, so it is reported as unexpected
        // response data rather than as a bad argument -- callers catching
        // ContractIntrospectionException must not have this escape as IllegalArgumentException.
        val e = shouldThrow<ContractWasmRetrievalException> { server.getContractWasm(CONTRACT_ID) }
        e.message shouldContain "unusable external executable reference"
        e.cause.shouldBeInstanceOf<IllegalArgumentException>()
        e.cause!!.message shouldContain "is not a contract"
        e.cause!!.message shouldContain ACCOUNT_ID
      }
      mockWebServer.requestCount shouldBe 1
    }

    test("a structurally broken reference is an invalid argument, not a NullPointerException") {
      mockWebServer.start()
      newServer().use { server ->
        // Every arm of a hand-built reference may be missing. None of these may surface as an NPE.
        val ownerlessRef =
          ContractExecutableExternalRef.builder().tag(SCString(XdrString("v1"))).build()
        shouldThrow<IllegalArgumentException> { server.getExternalRefWasmHash(ownerlessRef) }
          .message shouldContain "missing its owner"

        val ownerWithoutType =
          ContractExecutableExternalRef.builder()
            .executable_owner(SCAddress())
            .tag(SCString(XdrString("v1")))
            .build()
        shouldThrow<IllegalArgumentException> { server.getExternalRefWasmHash(ownerWithoutType) }
          .message shouldContain "missing its address type"

        val contractOwnerWithoutId =
          ContractExecutableExternalRef.builder()
            .executable_owner(
              SCAddress.builder().discriminant(SCAddressType.SC_ADDRESS_TYPE_CONTRACT).build()
            )
            .tag(SCString(XdrString("v1")))
            .build()
        shouldThrow<IllegalArgumentException> {
            server.getExternalRefWasmHash(contractOwnerWithoutId)
          }
          .message shouldContain "missing its contract ID"

        val tagless =
          ContractExecutableExternalRef.builder()
            .executable_owner(Address(OWNER_ID).toSCAddress())
            .build()
        shouldThrow<IllegalArgumentException> { server.getExternalRefWasmHash(tagless) }

        val nullTagBytes =
          ContractExecutableExternalRef.builder()
            .executable_owner(Address(OWNER_ID).toSCAddress())
            .tag(SCString(XdrString(null as ByteArray?)))
            .build()
        shouldThrow<IllegalArgumentException> { server.getExternalRefWasmHash(nullTagBytes) }
      }
      mockWebServer.requestCount shouldBe 0
    }

    test("tag entry that does not hold a 32-byte hash throws") {
      val tag = "v1".toByteArray(Charsets.UTF_8)
      mockWebServer.enqueue(
        MockResponse().setBody(singleEntryJson(tagLedgerEntryXdr(OWNER_ID, tag, Scv.toUint32(7))))
      )
      mockWebServer.enqueue(
        MockResponse()
          .setBody(singleEntryJson(tagLedgerEntryXdr(OWNER_ID, tag, Scv.toBytes(ByteArray(31)))))
      )
      mockWebServer.start()

      newServer().use { server ->
        val ref = externalRef(OWNER_ID, tag)
        val wrongType =
          shouldThrow<ContractWasmRetrievalException> { server.getExternalRefWasmHash(ref) }
        wrongType.message shouldContain "32-byte Wasm hash"
        // Bytes of the wrong length are rejected too.
        shouldThrow<ContractWasmRetrievalException> { server.getExternalRefWasmHash(ref) }
      }
    }

    test("tag entry that is not contract data throws") {
      mockWebServer.enqueue(
        MockResponse().setBody(singleEntryJson(contractCodeLedgerEntryXdr(byteArrayOf(0, 1, 2))))
      )
      mockWebServer.start()

      newServer().use { server ->
        shouldThrow<ContractWasmRetrievalException> {
          server.getExternalRefWasmHash(externalRef(OWNER_ID, "v1".toByteArray(Charsets.UTF_8)))
        }
      }
    }

    test("malformed tag entry XDR throws ContractWasmRetrievalException") {
      mockWebServer.enqueue(MockResponse().setBody(singleEntryJson("not-valid-base64!@#")))
      mockWebServer.start()

      newServer().use { server ->
        shouldThrow<ContractWasmRetrievalException> {
          server.getExternalRefWasmHash(externalRef(OWNER_ID, "v1".toByteArray(Charsets.UTF_8)))
        }
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
