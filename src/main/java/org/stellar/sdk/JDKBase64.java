package org.stellar.sdk;

/** Default implementation of {@link Base64} using the JDK's {@link java.util.Base64}. */
class JDKBase64 implements Base64 {
  @Override
  public String encodeToString(byte[] data) {
    return java.util.Base64.getEncoder().encodeToString(data);
  }

  @Override
  public byte[] encode(byte[] data) {
    return java.util.Base64.getEncoder().encode(data);
  }

  @Override
  public byte[] decode(String data) {
    return java.util.Base64.getDecoder().decode(data);
  }
}
