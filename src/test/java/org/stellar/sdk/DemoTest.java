package org.stellar.sdk;

import com.google.common.io.BaseEncoding;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;
import org.stellar.sdk.xdr.XdrDataInputStream;
import org.stellar.sdk.xdr.XdrDataOutputStream;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

public class DemoTest {
  public static String xdrToSorobanTransactionData(XdrUnsignedInteger sorobanData) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    try {
      sorobanData.encode(xdrDataOutputStream);
    } catch (IOException e) {
      throw new IllegalArgumentException("invalid sorobanData.", e);
    }
    BaseEncoding base64Encoding = BaseEncoding.base64();
    return base64Encoding.encode(byteArrayOutputStream.toByteArray());
  }

  public static XdrUnsignedInteger sorobanTransactionDataToXDR(String sorobanData) {
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] bytes = base64Encoding.decode(sorobanData);
    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
    XdrDataInputStream xdrInputStream = new XdrDataInputStream(inputStream);
    try {
      return XdrUnsignedInteger.decode(xdrInputStream);
    } catch (IOException e) {
      throw new IllegalArgumentException("invalid sorobanData: " + sorobanData, e);
    }
  }

  @Test
  public void test() {
    Long x = 4294967295L;
    System.out.println(x);
    XdrUnsignedInteger xdrUnsignedInteger = new XdrUnsignedInteger(x);
    String xdr = (xdrToSorobanTransactionData(xdrUnsignedInteger));
    System.out.println(sorobanTransactionDataToXDR(xdr).equals(xdrUnsignedInteger));
  }
}
