package org.stellar.sdk.contract

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.io.ByteArrayOutputStream
import java.util.Optional
import org.stellar.sdk.contract.exception.InvalidWasmException
import org.stellar.sdk.xdr.SCMetaEntry
import org.stellar.sdk.xdr.SCMetaKind
import org.stellar.sdk.xdr.SCMetaV0
import org.stellar.sdk.xdr.XdrString

private fun metaBytes(key: ByteArray, value: ByteArray): SCMetaEntry {
  val v0 =
    SCMetaV0().apply {
      this.key = XdrString(key)
      this.`val` = XdrString(value)
    }
  return SCMetaEntry().apply {
    discriminant = SCMetaKind.SC_META_V0
    this.v0 = v0
  }
}

private fun meta(key: String, value: String): SCMetaEntry =
  metaBytes(key.toByteArray(Charsets.UTF_8), value.toByteArray(Charsets.UTF_8))

private fun wasmWith(vararg entries: SCMetaEntry): ByteArray {
  val xdr = XdrStreams.serializeScMetaEntries(entries.toList())
  return ByteArrayOutputStream()
    .apply {
      write(WasmTestSupport.WASM_HEADER)
      write(WasmTestSupport.buildCustomSection("contractmetav0", xdr))
    }
    .toByteArray()
}

class ContractMetaTest :
  FunSpec({
    test("merges multiple sections in order") {
      val xdr1 = XdrStreams.serializeScMetaEntries(listOf(meta("a", "1")))
      val xdr2 = XdrStreams.serializeScMetaEntries(listOf(meta("b", "2")))
      val wasm =
        ByteArrayOutputStream()
          .apply {
            write(WasmTestSupport.WASM_HEADER)
            write(WasmTestSupport.buildCustomSection("contractmetav0", xdr1))
            write(WasmTestSupport.buildCustomSection("contractmetav0", xdr2))
          }
          .toByteArray()
      val meta = ContractMeta.fromWasm(wasm)
      meta.entries.size shouldBe 2
      meta.get("a") shouldBe Optional.of("1")
      meta.get("b") shouldBe Optional.of("2")
    }

    test("items, get, getAll") {
      val meta = ContractMeta.fromWasm(wasmWith(meta("a", "1"), meta("a", "2"), meta("b", "3")))
      meta.items().size shouldBe 3
      meta.items()[0].key shouldBe "a"
      meta.items()[0].value shouldBe "1"
      meta.get("a") shouldBe Optional.of("1")
      meta.getAll("a") shouldBe listOf("1", "2")
      meta.get("b") shouldBe Optional.of("3")
      meta.get("missing") shouldBe Optional.empty()
    }

    test("supportedSeps parses comma-separated") {
      val meta = ContractMeta.fromWasm(wasmWith(meta("sep", "41,40")))
      meta.supportedSeps() shouldBe listOf(41, 40)
      meta.implementsSep(40) shouldBe true
      meta.implementsSep(42) shouldBe false
    }

    test("supportedSeps merges multiple entries") {
      val meta = ContractMeta.fromWasm(wasmWith(meta("sep", "41"), meta("sep", "40,46")))
      meta.supportedSeps() shouldBe listOf(41, 40, 46)
    }

    test("supportedSeps accepts leading zeros and deduplicates") {
      val meta = ContractMeta.fromWasm(wasmWith(meta("sep", "041"), meta("sep", "41,41,40")))
      meta.supportedSeps() shouldBe listOf(41, 40)
    }

    test("supportedSeps skips invalid by default") {
      val meta = ContractMeta.fromWasm(wasmWith(meta("sep", "41, ,abc,40")))
      meta.supportedSeps() shouldBe listOf(41, 40)
    }

    test("supportedSeps rejects invalid in strict mode") {
      val meta = ContractMeta.fromWasm(wasmWith(meta("sep", "41,abc")))
      shouldThrow<IllegalArgumentException> { meta.supportedSeps(true) }
    }

    test("supportedSeps rejects empty in strict mode") {
      val meta = ContractMeta.fromWasm(wasmWith(meta("sep", "41,")))
      shouldThrow<IllegalArgumentException> { meta.supportedSeps(true) }
    }

    test("non-UTF-8 value rejects on access, not on construction") {
      val entry =
        metaBytes("k".toByteArray(Charsets.UTF_8), byteArrayOf(0xff.toByte(), 0xff.toByte()))
      val meta = ContractMeta.fromWasm(wasmWith(entry))
      shouldThrow<InvalidWasmException> { meta.items() }
      shouldThrow<InvalidWasmException> { meta.get("k") }
    }

    test("no-arg constructor produces empty entries") {
      val meta = ContractMeta()
      meta.entries.isEmpty() shouldBe true
      meta.supportedSeps().isEmpty() shouldBe true
      meta.get("anything") shouldBe Optional.empty()
    }

    test("XDR bytes round trip") {
      val original = ContractMeta(listOf(meta("a", "1"), meta("sep", "41")))
      val restored = ContractMeta.fromXdrBytes(original.toXdrBytes())
      restored shouldBe original
    }

    test("entries list is unmodifiable") {
      val meta = ContractMeta(listOf(meta("k", "v")))
      shouldThrow<UnsupportedOperationException> { meta.entries.clear() }
    }

    test("constructor rejects null elements") {
      shouldThrow<IllegalArgumentException> { ContractMeta(listOf(meta("k", "v"), null)) }
    }
  })
