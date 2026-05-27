package org.stellar.sdk.contract

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.io.ByteArrayOutputStream
import org.stellar.sdk.contract.exception.InvalidWasmException

class WasmCustomSectionsTest :
  FunSpec({
    test("empty valid Wasm returns no sections") {
      val sections = WasmCustomSections.getCustomSections(WasmTestSupport.WASM_HEADER)
      sections.isEmpty() shouldBe true
    }

    test("multiple same-name sections returned in order") {
      val wasm =
        WasmTestSupport.wasmWithSections(
          "contractmetav0" to byteArrayOf(0x01),
          "contractmetav0" to byteArrayOf(0x02),
        )
      val payloads = WasmCustomSections.getCustomSections(wasm, "contractmetav0")
      payloads.size shouldBe 2
      payloads[0] shouldBe byteArrayOf(0x01)
      payloads[1] shouldBe byteArrayOf(0x02)
    }

    test("unrelated sections are ignored") {
      val wasm =
        ByteArrayOutputStream()
          .apply {
            write(WasmTestSupport.WASM_HEADER)
            write(WasmTestSupport.buildOtherSection(1, byteArrayOf(0x00, 0x00)))
            write(WasmTestSupport.buildCustomSection("contractmetav0", byteArrayOf(0x42)))
            write(WasmTestSupport.buildOtherSection(3, byteArrayOf(0x00, 0x00)))
          }
          .toByteArray()
      val payloads = WasmCustomSections.getCustomSections(wasm, "contractmetav0")
      payloads.size shouldBe 1
      payloads[0] shouldBe byteArrayOf(0x42)
    }

    test("multi-byte LEB128 section length is supported") {
      val payload = ByteArray(200) { (it and 0xff).toByte() }
      val wasm = WasmTestSupport.wasmWithSections("contractmetav0" to payload)
      val payloads = WasmCustomSections.getCustomSections(wasm, "contractmetav0")
      payloads.size shouldBe 1
      payloads[0] shouldBe payload
    }

    test("invalid header throws") {
      val wasm = byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00)
      shouldThrow<InvalidWasmException> { WasmCustomSections.getCustomSections(wasm) }
    }

    test("short header throws") {
      val wasm = byteArrayOf(0x00, 0x61, 0x73, 0x6d)
      shouldThrow<InvalidWasmException> { WasmCustomSections.getCustomSections(wasm) }
    }

    test("invalid version throws") {
      val wasm = byteArrayOf(0x00, 0x61, 0x73, 0x6d, 0x02, 0x00, 0x00, 0x00)
      shouldThrow<InvalidWasmException> { WasmCustomSections.getCustomSections(wasm) }
    }

    test("section extending past EOF throws") {
      val wasm =
        ByteArrayOutputStream()
          .apply {
            write(WasmTestSupport.WASM_HEADER)
            write(0x00) // custom section id
            write(WasmTestSupport.encodeUnsignedLeb128(100)) // claims 100 bytes
            write(byteArrayOf(0x01)) // but only 1 byte follows
          }
          .toByteArray()
      shouldThrow<InvalidWasmException> { WasmCustomSections.getCustomSections(wasm) }
    }

    test("truncated LEB128 throws") {
      val wasm =
        ByteArrayOutputStream()
          .apply {
            write(WasmTestSupport.WASM_HEADER)
            write(0x00)
            write(0x80) // continuation bit set, no following byte
          }
          .toByteArray()
      shouldThrow<InvalidWasmException> { WasmCustomSections.getCustomSections(wasm) }
    }

    test("too-long LEB128 throws") {
      val wasm =
        ByteArrayOutputStream()
          .apply {
            write(WasmTestSupport.WASM_HEADER)
            write(0x00)
            // 6 bytes with continuation bits — exceeds 5-byte limit for u32
            repeat(5) { write(0x80) }
            write(0x01)
          }
          .toByteArray()
      shouldThrow<InvalidWasmException> { WasmCustomSections.getCustomSections(wasm) }
    }

    test("non-UTF-8 section name throws") {
      val sectionBody =
        ByteArrayOutputStream()
          .apply {
            write(0x02) // name length = 2
            write(0xff)
            write(0xff)
          }
          .toByteArray()
      val wasm =
        ByteArrayOutputStream()
          .apply {
            write(WasmTestSupport.WASM_HEADER)
            write(0x00)
            write(WasmTestSupport.encodeUnsignedLeb128(sectionBody.size.toLong()))
            write(sectionBody)
          }
          .toByteArray()
      shouldThrow<InvalidWasmException> { WasmCustomSections.getCustomSections(wasm) }
    }
  })
