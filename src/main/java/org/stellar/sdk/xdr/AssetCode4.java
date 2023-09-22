// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import org.stellar.sdk.Base64;

// === xdr source ============================================================

//  typedef opaque AssetCode4[4];

//  ===========================================================================
public class AssetCode4 implements XdrElement {
  private byte[] AssetCode4;

  public AssetCode4() {}

  public AssetCode4(byte[] AssetCode4) {
    this.AssetCode4 = AssetCode4;
  }

  public byte[] getAssetCode4() {
    return this.AssetCode4;
  }

  public void setAssetCode4(byte[] value) {
    this.AssetCode4 = value;
  }

  public static void encode(XdrDataOutputStream stream, AssetCode4 encodedAssetCode4)
      throws IOException {
    int AssetCode4size = encodedAssetCode4.AssetCode4.length;
    stream.write(encodedAssetCode4.getAssetCode4(), 0, AssetCode4size);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static AssetCode4 decode(XdrDataInputStream stream) throws IOException {
    AssetCode4 decodedAssetCode4 = new AssetCode4();
    int AssetCode4size = 4;
    decodedAssetCode4.AssetCode4 = new byte[AssetCode4size];
    stream.read(decodedAssetCode4.AssetCode4, 0, AssetCode4size);
    return decodedAssetCode4;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.AssetCode4);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof AssetCode4)) {
      return false;
    }

    AssetCode4 other = (AssetCode4) object;
    return Arrays.equals(this.AssetCode4, other.AssetCode4);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64.encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static AssetCode4 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static AssetCode4 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
