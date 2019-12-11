package org.stellar.sdk.xdr;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

public class XdrString implements XdrElement {
    private byte[] bytes;
    private String text;

    public XdrString(byte[] bytes) {
        this.bytes = bytes;
        this.text = new String(bytes, Charset.forName("UTF-8"));
    }

    public XdrString(String text) {
        this.bytes = text.getBytes(Charset.forName("UTF-8"));
        this.text = text;
    }

    @Override
    public void encode(XdrDataOutputStream stream) throws IOException {
        stream.writeInt(this.bytes.length);
        stream.write(this.bytes, 0, this.bytes.length);
    }

    public static XdrString decode(XdrDataInputStream stream) throws IOException {
        int size = stream.readInt();
        byte[] bytes = new byte[size];
        stream.read(bytes);
        return new XdrString(bytes);
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.bytes);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof XdrString)) {
          return false;
        }

        XdrString other = (XdrString) object;
        return Arrays.equals(this.bytes, other.bytes);
    }

    @Override
    public String toString() {
        return this.text;
    }
}
