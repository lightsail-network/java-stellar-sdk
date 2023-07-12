package org.stellar.sdk;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;
import org.stellar.sdk.xdr.XdrDataInputStream;
import org.stellar.sdk.xdr.XdrDataOutputStream;

public class XdrDataStreamTest {
  // helper for tests below.
  public static String backAndForthXdrStreaming(String inputString) throws IOException {

    // String to XDR
    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrOutputStream = new XdrDataOutputStream(byteOutputStream);

    org.stellar.sdk.xdr.Memo xdrMemo = Memo.text(inputString).toXdr();
    xdrMemo.encode(xdrOutputStream);

    byte[] xdrByteOutput = byteOutputStream.toByteArray();

    // XDR back to String
    XdrDataInputStream xdrInputStream =
        new XdrDataInputStream(new ByteArrayInputStream(xdrByteOutput));
    xdrMemo = org.stellar.sdk.xdr.Memo.decode(xdrInputStream);

    return xdrMemo.getText().toString();
  }

  @Test
  public void backAndForthXdrStreamingWithStandardAscii() throws IOException {
    String memo = "Dollar Sign $";
    assertEquals(memo, backAndForthXdrStreaming(memo));
  }

  @Test
  public void backAndForthXdrStreamingWithNonStandardAscii() throws IOException {
    String memo = "Euro Sign €";
    assertEquals(memo, backAndForthXdrStreaming(memo));
  }

  @Test
  public void backAndForthXdrStreamingWithAllNonStandardAscii() throws IOException {
    String memo = "øûý™€♠♣♥†‡";
    assertEquals(memo, backAndForthXdrStreaming(memo));
  }
}
