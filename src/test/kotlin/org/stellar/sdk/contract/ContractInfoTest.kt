package org.stellar.sdk.contract

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.io.ByteArrayOutputStream
import java.util.Optional
import org.stellar.sdk.contract.exception.InvalidWasmException
import org.stellar.sdk.xdr.SCEnvMetaEntry
import org.stellar.sdk.xdr.SCEnvMetaKind
import org.stellar.sdk.xdr.SCMetaEntry
import org.stellar.sdk.xdr.SCMetaKind
import org.stellar.sdk.xdr.SCMetaV0
import org.stellar.sdk.xdr.SCSpecEntry
import org.stellar.sdk.xdr.SCSpecEntryKind
import org.stellar.sdk.xdr.SCSpecFunctionInputV0
import org.stellar.sdk.xdr.SCSpecFunctionV0
import org.stellar.sdk.xdr.SCSpecTypeDef
import org.stellar.sdk.xdr.SCSymbol
import org.stellar.sdk.xdr.Uint32
import org.stellar.sdk.xdr.XdrString
import org.stellar.sdk.xdr.XdrUnsignedInteger

private fun meta(key: String, value: String): SCMetaEntry {
  val v0 =
    SCMetaV0().apply {
      this.key = XdrString(key.toByteArray(Charsets.UTF_8))
      this.`val` = XdrString(value.toByteArray(Charsets.UTF_8))
    }
  return SCMetaEntry().apply {
    discriminant = SCMetaKind.SC_META_V0
    this.v0 = v0
  }
}

private fun functionEntry(name: String): SCSpecEntry {
  val fn =
    SCSpecFunctionV0().apply {
      doc = XdrString(ByteArray(0))
      this.name = SCSymbol().apply { scSymbol = XdrString(name.toByteArray(Charsets.UTF_8)) }
      inputs = arrayOf<SCSpecFunctionInputV0>()
      outputs = arrayOf<SCSpecTypeDef>()
    }
  return SCSpecEntry().apply {
    discriminant = SCSpecEntryKind.SC_SPEC_ENTRY_FUNCTION_V0
    functionV0 = fn
  }
}

private fun envMetaEntry(protocol: Int, preRelease: Int): SCEnvMetaEntry {
  val version =
    SCEnvMetaEntry.SCEnvMetaEntryInterfaceVersion().apply {
      this.protocol = Uint32().apply { uint32 = XdrUnsignedInteger(protocol.toLong()) }
      this.preRelease = Uint32().apply { uint32 = XdrUnsignedInteger(preRelease.toLong()) }
    }
  return SCEnvMetaEntry().apply {
    discriminant = SCEnvMetaKind.SC_ENV_META_KIND_INTERFACE_VERSION
    interfaceVersion = version
  }
}

class ContractInfoTest :
  FunSpec({
    test("single-pass parses all sections") {
      val metaXdr = XdrStreams.serializeScMetaEntries(listOf(meta("k", "v")))
      val specXdr = XdrStreams.serializeScSpecEntries(listOf(functionEntry("fn")))
      val envXdr = XdrStreams.serializeScEnvMetaEntries(listOf(envMetaEntry(22, 0)))

      val wasm =
        ByteArrayOutputStream()
          .apply {
            write(WasmTestSupport.WASM_HEADER)
            write(WasmTestSupport.buildCustomSection("contractmetav0", metaXdr))
            write(WasmTestSupport.buildCustomSection("contractenvmetav0", envXdr))
            write(WasmTestSupport.buildCustomSection("contractspecv0", specXdr))
          }
          .toByteArray()

      val info = ContractInfo.fromWasm(wasm)
      info.meta.get("k") shouldBe Optional.of("v")
      info.spec.functions.size shouldBe 1
      info.envMeta.size shouldBe 1
      info.envMeta[0].interfaceVersion shouldNotBe null
    }

    test("empty envMeta when absent") {
      val metaXdr = XdrStreams.serializeScMetaEntries(listOf(meta("k", "v")))
      val wasm =
        ByteArrayOutputStream()
          .apply {
            write(WasmTestSupport.WASM_HEADER)
            write(WasmTestSupport.buildCustomSection("contractmetav0", metaXdr))
          }
          .toByteArray()
      ContractInfo.fromWasm(wasm).envMeta.isEmpty() shouldBe true
    }

    test("multiple envMeta sections merged in order") {
      val env1 = XdrStreams.serializeScEnvMetaEntries(listOf(envMetaEntry(22, 0)))
      val env2 = XdrStreams.serializeScEnvMetaEntries(listOf(envMetaEntry(23, 1)))
      val wasm =
        ByteArrayOutputStream()
          .apply {
            write(WasmTestSupport.WASM_HEADER)
            write(WasmTestSupport.buildCustomSection("contractenvmetav0", env1))
            write(WasmTestSupport.buildCustomSection("contractenvmetav0", env2))
          }
          .toByteArray()
      val info = ContractInfo.fromWasm(wasm)
      info.envMeta.size shouldBe 2
      info.envMeta[0].interfaceVersion.protocol.uint32.number shouldBe 22L
      info.envMeta[1].interfaceVersion.protocol.uint32.number shouldBe 23L
    }

    test("constructor rejects null envMeta elements") {
      shouldThrow<IllegalArgumentException> {
        ContractInfo(ContractMeta(), ContractSpec(), listOf(envMetaEntry(22, 0), null))
      }
    }

    test("multiple spec sections are rejected") {
      val specXdr = XdrStreams.serializeScSpecEntries(listOf(functionEntry("fn")))
      val wasm =
        ByteArrayOutputStream()
          .apply {
            write(WasmTestSupport.WASM_HEADER)
            write(WasmTestSupport.buildCustomSection("contractspecv0", specXdr))
            write(WasmTestSupport.buildCustomSection("contractspecv0", specXdr))
          }
          .toByteArray()
      shouldThrow<InvalidWasmException> { ContractInfo.fromWasm(wasm) }
    }
  })
