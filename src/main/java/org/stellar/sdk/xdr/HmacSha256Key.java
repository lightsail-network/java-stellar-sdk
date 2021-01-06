// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;


import java.io.IOException;

import java.util.Arrays;

// === xdr source ============================================================

//  struct HmacSha256Key
//  {
//      opaque key[32];
//  };

//  ===========================================================================
public class HmacSha256Key implements XdrElement {
  public HmacSha256Key () {}
  private byte[] key;
  public byte[] getKey() {
    return this.key;
  }
  public void setKey(byte[] value) {
    this.key = value;
  }
  public static void encode(XdrDataOutputStream stream, HmacSha256Key encodedHmacSha256Key) throws IOException{
    int keysize = encodedHmacSha256Key.key.length;
    stream.write(encodedHmacSha256Key.getKey(), 0, keysize);
  }
  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }
  public static HmacSha256Key decode(XdrDataInputStream stream) throws IOException {
    HmacSha256Key decodedHmacSha256Key = new HmacSha256Key();
    int keysize = 32;
    decodedHmacSha256Key.key = new byte[keysize];
    stream.read(decodedHmacSha256Key.key, 0, keysize);
    return decodedHmacSha256Key;
  }
  @Override
  public int hashCode() {
    return Arrays.hashCode(this.key);
  }
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof HmacSha256Key)) {
      return false;
    }

    HmacSha256Key other = (HmacSha256Key) object;
    return Arrays.equals(this.key, other.key);
  }

  public static final class Builder {
    private byte[] key;

    public Builder key(byte[] key) {
      this.key = key;
      return this;
    }

    public HmacSha256Key build() {
      HmacSha256Key val = new HmacSha256Key();
      val.setKey(key);
      return val;
    }
  }
}
