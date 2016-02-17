package org.stellar.sdk.xdr;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class XdrDataOutputStream extends DataOutputStream {

    private final XdrOutputStream mOut;

    public XdrDataOutputStream(OutputStream out) {
        super(new XdrOutputStream(out));
        mOut = (XdrOutputStream) super.out;
    }

    public void writeString(String s) throws IOException {
        byte[] chars = s.getBytes();
        writeInt(chars.length);
        write(chars);
        pad();
    }

    public void writeIntArray(int[] a) throws IOException {
        writeInt(a.length);
        writeIntArray(a, a.length);
    }

    public void writeIntArray(int[] a, int l) throws IOException {
        for (int i = 0; i < l; i++) {
            writeInt(a[i]);
        }
    }

    public void writeFloatArray(float[] a) throws IOException {
        writeInt(a.length);
        writeFloatArray(a, a.length);
    }

    public void writeFloatArray(float[] a, int l) throws IOException {
        for (int i = 0; i < l; i++) {
            writeFloat(a[i]);
        }
    }

    public void writeDoubleArray(double[] a) throws IOException {
        writeInt(a.length);
        writeDoubleArray(a, a.length);
    }

    public void writeDoubleArray(double[] a, int l) throws IOException {
        for (int i = 0; i < l; i++) {
            writeDouble(a[i]);
        }
    }

    public void pad() throws IOException {
        mOut.pad();
    }

    private static final class XdrOutputStream extends OutputStream {

        private final OutputStream mOut;

        private int mCount;

        public XdrOutputStream(OutputStream out) {
            mOut = out;
            mCount = 0;
        }

        @Override
        public void write(int b) throws IOException {
            mOut.write(b);
            mCount++;
        }

        @Override
        public void write(byte[] b) throws IOException {
            mOut.write(b);
            mCount += b.length;
        }

        public void write(byte[] b, int offset, int length) throws IOException {
            mOut.write(b, offset, length);
            mCount += length;
        }

        public void pad() throws IOException {
            int pad = 0;
            int mod = mCount % 4;
            if (mod > 0) {
                pad = 4-mod;
            }
            while (pad-- > 0) {
                write(0);
            }
        }
    }
}
