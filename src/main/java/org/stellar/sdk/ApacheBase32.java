package org.stellar.sdk;

/** Default implementation of {@link Base32} using the Apache Commons Codec. */
class ApacheBase32 implements Base32 {
  // org.apache.commons.codec.binary.Base32 is thread-safe.
  private final org.apache.commons.codec.binary.Base32 base32 =
      new org.apache.commons.codec.binary.Base32();

  @Override
  public byte[] encode(byte[] data) {
    return base32.encode(data);
  }

  @Override
  public byte[] decode(String data) {
    return base32.decode(data);
  }

  @Override
  public byte[] decode(byte[] data) {
    return base32.decode(data);
  }
}
