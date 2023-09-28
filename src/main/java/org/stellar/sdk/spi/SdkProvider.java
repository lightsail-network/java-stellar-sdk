package org.stellar.sdk.spi;

import org.stellar.sdk.Base32;
import org.stellar.sdk.Base64;

/**
 * An abstract class for service providers that provide implementations of the SDK.
 *
 * <p>Note: we offer an Android specific implementation of this class, see <a
 * href="https://github.com/stellar/java-stellar-sdk-android-spi">Java Stellar SDK Android SPI</a>.
 *
 * @see <a href="https://docs.oracle.com/javase/tutorial/sound/SPI-intro.html">Service Provider
 *     Interfaces</a>
 */
public interface SdkProvider {

  /**
   * Creates a {@link Base64} implementation that will be used by the SDK.
   *
   * <p>Note: Default implementation will use the JDK's {@link java.util.Base64}, and it only works
   * with Android API 26 and above, if you need to support older versions of Android you need to
   * provide your own implementation, <a
   * href="https://github.com/stellar/java-stellar-sdk/blob/master/android_test/app/src/main/java/org/stellar/javastellarsdkdemoapp/AndroidSdkProvider.kt">this</a>
   * is an example for reference.
   *
   * @return a {@link Base64} implementation, if returns null the SDK will use the default
   *     implementation.
   */
  default Base64 createBase64() {
    return null;
  }

  /**
   * Creates a {@link Base32} implementation that will be used by the SDK.
   *
   * <p>Note: Default implementation will use the Apache Commons Codec's {@link
   * org.apache.commons.codec.binary.Base32}, and it only works with Android API 28 and above, if
   * you need to support older versions of Android you need to provide your own implementation, <a
   * href="https://github.com/stellar/java-stellar-sdk/blob/master/android_test/app/src/main/java/org/stellar/javastellarsdkdemoapp/AndroidSdkProvider.kt">this</a>
   * is an example for reference.
   *
   * @return a {@link Base32} implementation, if returns null the SDK will use the default
   *     implementation.
   */
  default Base32 createBase32() {
    return null;
  }
}
