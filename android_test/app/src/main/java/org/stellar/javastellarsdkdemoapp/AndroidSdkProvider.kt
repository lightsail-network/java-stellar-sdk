package org.stellar.javastellarsdkdemoapp

import org.stellar.sdk.Base64
import org.stellar.sdk.spi.SdkProvider

class AndroidSdkProvider : SdkProvider {
    override fun createBase64(): Base64 {
        return AndroidBase64()
    }

    class AndroidBase64 : Base64 {
        override fun encodeToString(data: ByteArray?): String {
            return android.util.Base64.encodeToString(data, android.util.Base64.NO_WRAP)
        }

        override fun encode(data: ByteArray?): ByteArray {
            return android.util.Base64.encode(data, android.util.Base64.NO_WRAP)
        }

        override fun decode(data: String?): ByteArray {
            return android.util.Base64.decode(data, android.util.Base64.NO_WRAP)
        }
    }
}