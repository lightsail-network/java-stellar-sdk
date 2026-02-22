package org.stellar.sdk

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class UtilTest :
  FunSpec({
    context("bytesToHex") {
      test("empty array") { Util.bytesToHex(byteArrayOf()) shouldBe "" }

      test("single byte") {
        Util.bytesToHex(byteArrayOf(0x00)) shouldBe "00"
        Util.bytesToHex(byteArrayOf(0x0A)) shouldBe "0A"
        Util.bytesToHex(byteArrayOf(0x7F)) shouldBe "7F"
        Util.bytesToHex(byteArrayOf(0x80.toByte())) shouldBe "80"
        Util.bytesToHex(byteArrayOf(0xFF.toByte())) shouldBe "FF"
      }

      test("multiple bytes") {
        Util.bytesToHex(byteArrayOf(0x48, 0x65, 0x6C, 0x6C, 0x6F)) shouldBe "48656C6C6F"
        Util.bytesToHex(
          byteArrayOf(0xDE.toByte(), 0xAD.toByte(), 0xBE.toByte(), 0xEF.toByte())
        ) shouldBe "DEADBEEF"
        Util.bytesToHex(byteArrayOf(0x00, 0x01, 0x02, 0x03, 0x04)) shouldBe "0001020304"
      }

      test("output is uppercase") {
        Util.bytesToHex(byteArrayOf(0xAB.toByte(), 0xCD.toByte(), 0xEF.toByte())) shouldBe "ABCDEF"
      }

      test("all 256 byte values") {
        val allBytes = ByteArray(256) { it.toByte() }
        val hex = Util.bytesToHex(allBytes)
        hex.length shouldBe 512
        // spot check a few values
        hex.substring(0, 2) shouldBe "00"
        hex.substring(2, 4) shouldBe "01"
        hex.substring(30, 32) shouldBe "0F"
        hex.substring(32, 34) shouldBe "10"
        hex.substring(510, 512) shouldBe "FF"
      }
    }

    context("hexToBytes") {
      test("empty string") { Util.hexToBytes("") shouldBe byteArrayOf() }

      test("single byte") {
        Util.hexToBytes("00") shouldBe byteArrayOf(0x00)
        Util.hexToBytes("FF") shouldBe byteArrayOf(0xFF.toByte())
        Util.hexToBytes("ff") shouldBe byteArrayOf(0xFF.toByte())
        Util.hexToBytes("0a") shouldBe byteArrayOf(0x0A)
        Util.hexToBytes("0A") shouldBe byteArrayOf(0x0A)
      }

      test("multiple bytes") {
        Util.hexToBytes("48656C6C6F") shouldBe byteArrayOf(0x48, 0x65, 0x6C, 0x6C, 0x6F)
        Util.hexToBytes("DEADBEEF") shouldBe
          byteArrayOf(0xDE.toByte(), 0xAD.toByte(), 0xBE.toByte(), 0xEF.toByte())
        Util.hexToBytes("deadbeef") shouldBe
          byteArrayOf(0xDE.toByte(), 0xAD.toByte(), 0xBE.toByte(), 0xEF.toByte())
      }

      test("mixed case") {
        Util.hexToBytes("AbCdEf") shouldBe byteArrayOf(0xAB.toByte(), 0xCD.toByte(), 0xEF.toByte())
      }

      test("odd length throws IllegalArgumentException") {
        shouldThrow<IllegalArgumentException> { Util.hexToBytes("ABC") }
        shouldThrow<IllegalArgumentException> { Util.hexToBytes("A") }
      }

      test("invalid characters throw IllegalArgumentException") {
        shouldThrow<IllegalArgumentException> { Util.hexToBytes("ZZZZ") }
        shouldThrow<IllegalArgumentException> { Util.hexToBytes("GH") }
        shouldThrow<IllegalArgumentException> { Util.hexToBytes("0G") }
      }
    }

    context("round trip") {
      test("bytesToHex then hexToBytes") {
        val original = byteArrayOf(0x00, 0x11, 0x22, 0xAA.toByte(), 0xBB.toByte(), 0xFF.toByte())
        val hex = Util.bytesToHex(original)
        Util.hexToBytes(hex) shouldBe original
      }

      test("all 256 byte values round trip") {
        val allBytes = ByteArray(256) { it.toByte() }
        val hex = Util.bytesToHex(allBytes)
        Util.hexToBytes(hex) shouldBe allBytes
      }

      test("lowercase hex input round trips") {
        val hex = "deadbeef0123456789abcdef"
        val bytes = Util.hexToBytes(hex)
        Util.bytesToHex(bytes).lowercase() shouldBe hex
      }
    }
  })
