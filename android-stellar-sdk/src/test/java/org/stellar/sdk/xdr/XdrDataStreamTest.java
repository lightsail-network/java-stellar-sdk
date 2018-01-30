
package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.assertEquals;
import org.junit.Test;


public class XdrDataStreamTest {
    //helper for tests below.
     public static String backAndForthXdrStreaming(String inputString) throws IOException {

        //String to XDR
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        XdrDataOutputStream xdrOutputStream = new XdrDataOutputStream(byteOutputStream);
        xdrOutputStream.writeString(inputString);

        byte[] xdrByteOutput = byteOutputStream.toByteArray();

        //XDR back to String
        XdrDataInputStream xdrInputStream = new XdrDataInputStream(new ByteArrayInputStream(xdrByteOutput));
        String outputString = xdrInputStream.readString();

        return outputString;

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
        String memo = "øûý™€♠♣♥†‡µ¢£€";
        assertEquals(memo, backAndForthXdrStreaming(memo));
    }
}
