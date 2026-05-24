package org.stellar.sdk.contract

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.io.ByteArrayOutputStream
import org.stellar.sdk.contract.exception.InvalidWasmException
import org.stellar.sdk.xdr.SCSpecEntry
import org.stellar.sdk.xdr.SCSpecEntryKind
import org.stellar.sdk.xdr.SCSpecEventDataFormat
import org.stellar.sdk.xdr.SCSpecEventParamV0
import org.stellar.sdk.xdr.SCSpecEventV0
import org.stellar.sdk.xdr.SCSpecFunctionInputV0
import org.stellar.sdk.xdr.SCSpecFunctionV0
import org.stellar.sdk.xdr.SCSpecTypeDef
import org.stellar.sdk.xdr.SCSpecUDTEnumCaseV0
import org.stellar.sdk.xdr.SCSpecUDTEnumV0
import org.stellar.sdk.xdr.SCSpecUDTErrorEnumCaseV0
import org.stellar.sdk.xdr.SCSpecUDTErrorEnumV0
import org.stellar.sdk.xdr.SCSpecUDTStructFieldV0
import org.stellar.sdk.xdr.SCSpecUDTStructV0
import org.stellar.sdk.xdr.SCSpecUDTUnionCaseV0
import org.stellar.sdk.xdr.SCSpecUDTUnionV0
import org.stellar.sdk.xdr.SCSymbol
import org.stellar.sdk.xdr.XdrString

private fun symbol(name: String): SCSymbol =
  SCSymbol().apply { scSymbol = XdrString(name.toByteArray(Charsets.UTF_8)) }

private fun symbolBytes(bytes: ByteArray): SCSymbol =
  SCSymbol().apply { scSymbol = XdrString(bytes) }

private fun functionEntry(name: String): SCSpecEntry {
  val fn =
    SCSpecFunctionV0().apply {
      doc = XdrString(ByteArray(0))
      this.name = symbol(name)
      inputs = arrayOf<SCSpecFunctionInputV0>()
      outputs = arrayOf<SCSpecTypeDef>()
    }
  return SCSpecEntry().apply {
    discriminant = SCSpecEntryKind.SC_SPEC_ENTRY_FUNCTION_V0
    functionV0 = fn
  }
}

private fun eventEntry(name: String): SCSpecEntry {
  val event =
    SCSpecEventV0().apply {
      doc = XdrString(ByteArray(0))
      lib = XdrString(ByteArray(0))
      this.name = symbol(name)
      prefixTopics = arrayOf<SCSymbol>()
      params = arrayOf<SCSpecEventParamV0>()
      dataFormat = SCSpecEventDataFormat.values().first()
    }
  return SCSpecEntry().apply {
    discriminant = SCSpecEntryKind.SC_SPEC_ENTRY_EVENT_V0
    eventV0 = event
  }
}

private fun structEntry(name: String): SCSpecEntry {
  val udt =
    SCSpecUDTStructV0().apply {
      doc = XdrString(ByteArray(0))
      lib = XdrString(ByteArray(0))
      this.name = XdrString(name.toByteArray(Charsets.UTF_8))
      fields = arrayOf<SCSpecUDTStructFieldV0>()
    }
  return SCSpecEntry().apply {
    discriminant = SCSpecEntryKind.SC_SPEC_ENTRY_UDT_STRUCT_V0
    udtStructV0 = udt
  }
}

private fun unionEntry(name: String): SCSpecEntry {
  val udt =
    SCSpecUDTUnionV0().apply {
      doc = XdrString(ByteArray(0))
      lib = XdrString(ByteArray(0))
      this.name = XdrString(name.toByteArray(Charsets.UTF_8))
      cases = arrayOf<SCSpecUDTUnionCaseV0>()
    }
  return SCSpecEntry().apply {
    discriminant = SCSpecEntryKind.SC_SPEC_ENTRY_UDT_UNION_V0
    udtUnionV0 = udt
  }
}

private fun enumEntry(name: String): SCSpecEntry {
  val udt =
    SCSpecUDTEnumV0().apply {
      doc = XdrString(ByteArray(0))
      lib = XdrString(ByteArray(0))
      this.name = XdrString(name.toByteArray(Charsets.UTF_8))
      cases = arrayOf<SCSpecUDTEnumCaseV0>()
    }
  return SCSpecEntry().apply {
    discriminant = SCSpecEntryKind.SC_SPEC_ENTRY_UDT_ENUM_V0
    udtEnumV0 = udt
  }
}

private fun errorEnumEntry(name: String): SCSpecEntry {
  val udt =
    SCSpecUDTErrorEnumV0().apply {
      doc = XdrString(ByteArray(0))
      lib = XdrString(ByteArray(0))
      this.name = XdrString(name.toByteArray(Charsets.UTF_8))
      cases = arrayOf<SCSpecUDTErrorEnumCaseV0>()
    }
  return SCSpecEntry().apply {
    discriminant = SCSpecEntryKind.SC_SPEC_ENTRY_UDT_ERROR_ENUM_V0
    udtErrorEnumV0 = udt
  }
}

private fun wasmWith(vararg entries: SCSpecEntry): ByteArray {
  val xdr = XdrStreams.serializeScSpecEntries(entries.toList())
  return ByteArrayOutputStream()
    .apply {
      write(WasmTestSupport.WASM_HEADER)
      write(WasmTestSupport.buildCustomSection("contractspecv0", xdr))
    }
    .toByteArray()
}

class ContractSpecTest :
  FunSpec({
    test("empty when section absent") {
      ContractSpec.fromWasm(WasmTestSupport.WASM_HEADER).entries.isEmpty() shouldBe true
    }

    test("rejects multiple spec sections") {
      val xdr = XdrStreams.serializeScSpecEntries(listOf(functionEntry("a")))
      val wasm =
        ByteArrayOutputStream()
          .apply {
            write(WasmTestSupport.WASM_HEADER)
            write(WasmTestSupport.buildCustomSection("contractspecv0", xdr))
            write(WasmTestSupport.buildCustomSection("contractspecv0", xdr))
          }
          .toByteArray()
      shouldThrow<InvalidWasmException> { ContractSpec.fromWasm(wasm) }
    }

    test("classifies all entry kinds") {
      val spec =
        ContractSpec.fromWasm(
          wasmWith(
            functionEntry("fn"),
            eventEntry("evt"),
            structEntry("S"),
            unionEntry("U"),
            enumEntry("E"),
            errorEnumEntry("Err"),
          )
        )
      spec.entries.size shouldBe 6
      spec.functions.size shouldBe 1
      spec.events.size shouldBe 1
      spec.structs.size shouldBe 1
      spec.unions.size shouldBe 1
      spec.enums.size shouldBe 1
      spec.errorEnums.size shouldBe 1
    }

    test("get function by name") {
      val spec =
        ContractSpec.fromWasm(wasmWith(functionEntry("transfer"), functionEntry("balance")))
      val fn = spec.getFunction("transfer")
      fn.isPresent shouldBe true
      String(fn.get().name.scSymbol.bytes, Charsets.UTF_8) shouldBe "transfer"
      spec.getFunction("unknown").isPresent shouldBe false
    }

    test("get event by name") {
      val spec = ContractSpec.fromWasm(wasmWith(eventEntry("Transfer")))
      spec.getEvent("Transfer").isPresent shouldBe true
      spec.getEvent("missing").isPresent shouldBe false
    }

    test("get UDT by name") {
      val spec =
        ContractSpec.fromWasm(
          wasmWith(structEntry("S1"), unionEntry("U1"), enumEntry("E1"), errorEnumEntry("Err1"))
        )
      spec.getUdt("S1").isPresent shouldBe true
      spec.getUdt("U1").isPresent shouldBe true
      spec.getUdt("E1").isPresent shouldBe true
      spec.getUdt("Err1").isPresent shouldBe true
      spec.getUdt("missing").isPresent shouldBe false
    }

    test("non-UTF-8 function name rejected by lookup") {
      val fn =
        SCSpecFunctionV0().apply {
          doc = XdrString(ByteArray(0))
          name = symbolBytes(byteArrayOf(0xff.toByte()))
          inputs = arrayOf<SCSpecFunctionInputV0>()
          outputs = arrayOf<SCSpecTypeDef>()
        }
      val entry =
        SCSpecEntry().apply {
          discriminant = SCSpecEntryKind.SC_SPEC_ENTRY_FUNCTION_V0
          functionV0 = fn
        }
      val spec = ContractSpec.fromWasm(wasmWith(entry))
      shouldThrow<InvalidWasmException> { spec.getFunction("anything") }
    }

    test("XDR bytes round trip") {
      val original = ContractSpec(listOf(functionEntry("a"), eventEntry("b")))
      val restored = ContractSpec.fromXdrBytes(original.toXdrBytes())
      restored shouldBe original
    }

    test("constructor rejects null elements") {
      shouldThrow<IllegalArgumentException> { ContractSpec(listOf(functionEntry("a"), null)) }
    }
  })
