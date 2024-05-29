package org.stellar.sdk.xdr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/** Common parent interface for all generated classes. */
interface XdrElement {
  void encode(XdrDataOutputStream stream) throws IOException;

  default String toXdrBase64() throws IOException {
    return Base64Factory.getInstance().encodeToString(toXdrByteArray());
  }

  default byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }
}
