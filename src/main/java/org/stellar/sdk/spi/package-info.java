/**
 * Service Provider Interface (SPI) for platform-specific SDK implementations.
 *
 * <p>{@link org.stellar.sdk.spi.SdkProvider} allows overriding default Base64 and Base32
 * implementations. This is primarily used to provide Android-compatible implementations for API
 * levels below 26 (Base64) and 28 (Base32).
 *
 * @see org.stellar.sdk.spi.SdkProvider
 * @see <a href="https://github.com/stellar/java-stellar-sdk-android-spi">Java Stellar SDK Android
 *     SPI</a>
 */
package org.stellar.sdk.spi;
