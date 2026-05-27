package org.stellar.sdk.contract

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.stellar.sdk.contract.exception.InvalidWasmException
import org.stellar.sdk.xdr.SCMetaEntry
import org.stellar.sdk.xdr.SCMetaKind
import org.stellar.sdk.xdr.SCMetaV0
import org.stellar.sdk.xdr.XdrString

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

class XdrStreamsTest :
  FunSpec({
    test("round-trip multiple SCMetaEntry values") {
      val entries =
        listOf(meta("rsver", "1.78.0"), meta("rssdkver", "21.0.0"), meta("sep", "41,40"))
      val bytes = XdrStreams.serializeScMetaEntries(entries)
      XdrStreams.parseScMetaEntries(bytes) shouldBe entries
    }

    test("empty stream produces empty list") {
      XdrStreams.parseScMetaEntries(ByteArray(0)).isEmpty() shouldBe true
    }

    test("serialize empty iterable produces empty byte array") {
      XdrStreams.serializeScMetaEntries(emptyList()) shouldBe ByteArray(0)
    }

    test("truncated XDR throws") {
      val bytes = XdrStreams.serializeScMetaEntries(listOf(meta("k", "v")))
      val truncated = bytes.copyOfRange(0, bytes.size - 1)
      shouldThrow<InvalidWasmException> { XdrStreams.parseScMetaEntries(truncated) }
    }

    test("trailing bytes are rejected") {
      val bytes = XdrStreams.serializeScMetaEntries(listOf(meta("k", "v")))
      val withGarbage = bytes + byteArrayOf(0x01)
      shouldThrow<InvalidWasmException> { XdrStreams.parseScMetaEntries(withGarbage) }
    }

    test("invalid discriminant throws") {
      // SCMetaKind only defines SC_META_V0 = 0; value 1 should fail decode.
      shouldThrow<InvalidWasmException> {
        XdrStreams.parseScMetaEntries(byteArrayOf(0x00, 0x00, 0x00, 0x01))
      }
    }
  })
