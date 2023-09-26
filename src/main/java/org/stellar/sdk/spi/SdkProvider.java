package org.stellar.sdk.spi;

import org.stellar.sdk.Base64;

/**
 * An abstract class for service providers that provide implementations of the SDK.
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
   * @see org.stellar.sdk.Base64Factory
   */
  default Base64 createBase64() {
    return null;
  }
}
