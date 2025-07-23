package org.stellar.sdk

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import org.stellar.sdk.xdr.MemoType

class MemoTest :
  FunSpec({
    context("MEMO_NONE") {
      test("should create empty memo") {
        val memo = Memo.none()
        memo.toXdr().discriminant shouldBe MemoType.MEMO_NONE
        memo.toString() shouldBe ""
        Memo.fromXdr(memo.toXdr()) shouldBe memo
      }
    }

    context("MEMO_TEXT") {
      test("should create text memo from string") {
        val memo = Memo.text("test")
        memo.toXdr().discriminant shouldBe MemoType.MEMO_TEXT
        memo.text shouldBe "test"
        memo.bytes shouldBe "test".toByteArray(StandardCharsets.UTF_8)
        memo.toString() shouldBe "test"
        Memo.fromXdr(memo.toXdr()) shouldBe memo
      }

      test("should create text memo from empty string") {
        val memo = Memo.text("")
        memo.toXdr().discriminant shouldBe MemoType.MEMO_TEXT
        memo.text shouldBe ""
        memo.bytes shouldBe "".toByteArray(StandardCharsets.UTF_8)
        memo.toString() shouldBe ""
        Memo.fromXdr(memo.toXdr()) shouldBe memo

        // https://discord.com/channels/897514728459468821/1082043640140017664/1397339401713094778
        org.stellar.sdk.xdr.Memo.fromXdrBase64(memo.toXdr().toXdrBase64())
      }

      test("should create text memo with UTF-8 content") {
        val memo = Memo.text("三")
        memo.toXdr().discriminant shouldBe MemoType.MEMO_TEXT
        memo.text shouldBe "三"
        memo.bytes shouldBe "三".toByteArray(StandardCharsets.UTF_8)
        memo.toString() shouldBe "三"
        Memo.fromXdr(memo.toXdr()) shouldBe memo
      }

      test("should create text memo with exactly 28 bytes") {
        val text = "1234567890123456789012345678"
        text.toByteArray(StandardCharsets.UTF_8).size shouldBe 28
        val memo = Memo.text(text)
        memo.toXdr().discriminant shouldBe MemoType.MEMO_TEXT
        memo.text shouldBe text
        memo.bytes shouldBe text.toByteArray(StandardCharsets.UTF_8)
        memo.toString() shouldBe text
        Memo.fromXdr(memo.toXdr()) shouldBe memo
      }

      test("should throw exception when text is too long") {
        val longText = "12345678901234567890123456789"
        longText.toByteArray(StandardCharsets.UTF_8).size shouldBe 29
        val exception = shouldThrow<IllegalArgumentException> { Memo.text(longText) }
        exception.message shouldBe "text cannot be more than 28-bytes long."
      }

      test("should throw exception when UTF-8 text is too long") {
        val longText = "价值交易的开源协议!!" // 30 bytes
        longText.toByteArray(StandardCharsets.UTF_8).size shouldBe 29
        val exception = shouldThrow<IllegalArgumentException> { Memo.text(longText) }
        exception.message shouldBe "text cannot be more than 28-bytes long."
      }
    }

    context("MEMO_ID") {
      test("should create id memo") {
        val memo = Memo.id(9223372036854775807L)
        memo.id shouldBe BigInteger.valueOf(9223372036854775807L)
        memo.toXdr().discriminant shouldBe MemoType.MEMO_ID
        memo.toXdr().id.uint64.number shouldBe BigInteger.valueOf(9223372036854775807L)
        memo.toString() shouldBe "9223372036854775807"
        Memo.fromXdr(memo.toXdr()) shouldBe memo
      }

      test("should create id memo with 0") {
        val memo = Memo.id(0)
        memo.id shouldBe BigInteger.ZERO
        memo.toXdr().discriminant shouldBe MemoType.MEMO_ID
        memo.toXdr().id.uint64.number shouldBe BigInteger.ZERO
        memo.toString() shouldBe "0"
        Memo.fromXdr(memo.toXdr()) shouldBe memo
      }

      test("should throw exception when id is negative") {
        val exception = shouldThrow<IllegalArgumentException> { Memo.id(-1) }
        exception.message shouldBe "MEMO_ID must be between 0 and 2^64-1"
      }
    }

    context("MEMO_HASH") {
      test("should create hash memo from byte array") {
        val bytes = ByteArray(32) { 'A'.code.toByte() }
        val memo = Memo.hash(bytes)
        memo.toXdr().discriminant shouldBe MemoType.MEMO_HASH
        memo.bytes shouldBe bytes
        memo.hexValue shouldBe "4141414141414141414141414141414141414141414141414141414141414141"
        memo.toString() shouldBe "4141414141414141414141414141414141414141414141414141414141414141"
        Memo.fromXdr(memo.toXdr()) shouldBe memo
      }

      test("should create hash memo from hex string") {
        val hashHex = "4142434445464748494a4b4c0000000000000000000000000000000000000000"
        val memo = Memo.hash(hashHex)
        memo.toXdr().discriminant shouldBe MemoType.MEMO_HASH
        memo.hexValue shouldBe hashHex.lowercase()
        memo.toString() shouldBe hashHex.lowercase()
      }

      test("should create hash memo from uppercase hex string") {
        val hashHex = "4142434445464748494a4b4c0000000000000000000000000000000000000000"
        val memo = Memo.hash(hashHex.uppercase())
        memo.toXdr().discriminant shouldBe MemoType.MEMO_HASH
        memo.hexValue shouldBe hashHex.lowercase()
        memo.toString() shouldBe hashHex.lowercase()
        Memo.fromXdr(memo.toXdr()) shouldBe memo
      }

      test("should throw exception when bytes are not 32-bytes long") {
        val bytes31 = ByteArray(31)
        val exception31 = shouldThrow<IllegalArgumentException> { Memo.hash(bytes31) }
        exception31.message shouldBe "bytes must be 32-bytes long."

        val bytes33 = ByteArray(33)
        val exception33 = shouldThrow<IllegalArgumentException> { Memo.hash(bytes33) }
        exception33.message shouldBe "bytes must be 32-bytes long."
      }

      test("should throw exception when hex string is invalid") {
        shouldThrow<IllegalArgumentException> { Memo.hash("test") }
        shouldThrow<IllegalArgumentException> { Memo.hash("") }
        shouldThrow<IllegalArgumentException> {
          Memo.hash("00000000000000000000000000000000000000000000000000000000000000")
        } // 63 chars
      }
    }

    context("MEMO_RETURN_HASH") {
      test("should create return hash memo from byte array") {
        val bytes = ByteArray(32) { 'A'.code.toByte() }
        val memo = Memo.returnHash(bytes)
        memo.toXdr().discriminant shouldBe MemoType.MEMO_RETURN
        memo.bytes shouldBe bytes
        memo.hexValue shouldBe "4141414141414141414141414141414141414141414141414141414141414141"
        memo.toString() shouldBe "4141414141414141414141414141414141414141414141414141414141414141"
        Memo.fromXdr(memo.toXdr()) shouldBe memo
      }

      test("should create return hash memo from hex string") {
        val hashHex = "4142434445464748494a4b4c0000000000000000000000000000000000000000"
        val memo = Memo.returnHash(hashHex)
        memo.toXdr().discriminant shouldBe MemoType.MEMO_RETURN
        memo.hexValue shouldBe hashHex.lowercase()
        memo.toString() shouldBe hashHex.lowercase()
        Memo.fromXdr(memo.toXdr()) shouldBe memo
      }

      test("should create return hash memo from uppercase hex string") {
        val hashHex = "4142434445464748494a4b4c0000000000000000000000000000000000000000"
        val memo = Memo.returnHash(hashHex.uppercase())
        memo.toXdr().discriminant shouldBe MemoType.MEMO_RETURN
        memo.hexValue shouldBe hashHex.lowercase()
        memo.toString() shouldBe hashHex.lowercase()
        Memo.fromXdr(memo.toXdr()) shouldBe memo
      }

      test("should throw exception when bytes are not 32-bytes long") {
        val bytes31 = ByteArray(31)
        val exception31 = shouldThrow<IllegalArgumentException> { Memo.returnHash(bytes31) }
        exception31.message shouldBe "bytes must be 32-bytes long."

        val bytes33 = ByteArray(33)
        val exception33 = shouldThrow<IllegalArgumentException> { Memo.returnHash(bytes33) }
        exception33.message shouldBe "bytes must be 32-bytes long."
      }

      test("should throw exception when hex string is invalid") {
        shouldThrow<IllegalArgumentException> { Memo.returnHash("test") }
        shouldThrow<IllegalArgumentException> { Memo.returnHash("") }
        shouldThrow<IllegalArgumentException> {
          Memo.returnHash("00000000000000000000000000000000000000000000000000000000000000")
        } // 63 chars
      }
    }
  })
