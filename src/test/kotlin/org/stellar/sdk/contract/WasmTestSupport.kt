package org.stellar.sdk.contract

import java.io.ByteArrayOutputStream

internal object WasmTestSupport {
  val WASM_HEADER: ByteArray = byteArrayOf(0x00, 0x61, 0x73, 0x6d, 0x01, 0x00, 0x00, 0x00)

  fun buildCustomSection(name: String, payload: ByteArray): ByteArray {
    val nameBytes = name.toByteArray(Charsets.UTF_8)
    val body =
      ByteArrayOutputStream().apply {
        write(encodeUnsignedLeb128(nameBytes.size.toLong()))
        write(nameBytes)
        write(payload)
      }
    return ByteArrayOutputStream()
      .apply {
        write(0x00)
        write(encodeUnsignedLeb128(body.size().toLong()))
        write(body.toByteArray())
      }
      .toByteArray()
  }

  fun buildOtherSection(id: Int, payload: ByteArray): ByteArray =
    ByteArrayOutputStream()
      .apply {
        write(id)
        write(encodeUnsignedLeb128(payload.size.toLong()))
        write(payload)
      }
      .toByteArray()

  fun encodeUnsignedLeb128(value: Long): ByteArray {
    require(value >= 0) { "value must be non-negative" }
    val out = ByteArrayOutputStream()
    var remaining = value
    do {
      var b = (remaining and 0x7f).toInt()
      remaining = remaining ushr 7
      if (remaining != 0L) {
        b = b or 0x80
      }
      out.write(b)
    } while (remaining != 0L)
    return out.toByteArray()
  }

  fun wasmWithSections(vararg sections: Pair<String, ByteArray>): ByteArray =
    ByteArrayOutputStream()
      .apply {
        write(WASM_HEADER)
        for ((name, payload) in sections) {
          write(buildCustomSection(name, payload))
        }
      }
      .toByteArray()
}
