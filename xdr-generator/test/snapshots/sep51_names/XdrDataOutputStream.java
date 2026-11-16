package org.stellar.sdk.xdr;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/** XDR-aware output stream that writes variable-length values with the required padding. */
public class XdrDataOutputStream extends DataOutputStream {

    private final XdrOutputStream mOut;

    /**
     * Creates an XdrDataOutputStream that uses the specified underlying OutputStream.
     *
     * @param out the specified output stream
     */
    public XdrDataOutputStream(OutputStream out) {
        super(new XdrOutputStream(out));
        mOut = (XdrOutputStream) super.out;
    }

    /**
     * Writes an XDR variable-length array of integers.
     *
     * @param a the array to write
     * @throws IOException if an I/O error occurs while writing the array
     */
    public void writeIntArray(int[] a) throws IOException {
        writeInt(a.length);
        writeIntArray(a, a.length);
    }

    private void writeIntArray(int[] a, int l) throws IOException {
        for (int i = 0; i < l; i++) {
            writeInt(a[i]);
        }
    }

    /**
     * Writes an XDR variable-length array of floats.
     *
     * @param a the array to write
     * @throws IOException if an I/O error occurs while writing the array
     */
    public void writeFloatArray(float[] a) throws IOException {
        writeInt(a.length);
        writeFloatArray(a, a.length);
    }

    private void writeFloatArray(float[] a, int l) throws IOException {
        for (int i = 0; i < l; i++) {
            writeFloat(a[i]);
        }
    }

    /**
     * Writes an XDR variable-length array of doubles.
     *
     * @param a the array to write
     * @throws IOException if an I/O error occurs while writing the array
     */
    public void writeDoubleArray(double[] a) throws IOException {
        writeInt(a.length);
        writeDoubleArray(a, a.length);
    }

    private void writeDoubleArray(double[] a, int l) throws IOException {
        for (int i = 0; i < l; i++) {
            writeDouble(a[i]);
        }
    }

    private static final class XdrOutputStream extends OutputStream {

        private final OutputStream mOut;

        // Number of bytes written
        private int mCount;

        public XdrOutputStream(OutputStream out) {
            mOut = out;
            mCount = 0;
        }

        @Override
        public void write(int b) throws IOException {
            mOut.write(b);
            // https://docs.oracle.com/javase/7/docs/api/java/io/OutputStream.html#write(int):
            // > The byte to be written is the eight low-order bits of the argument b.
            // > The 24 high-order bits of b are ignored.
            mCount++;
        }

        @Override
        public void write(byte[] b) throws IOException {
            // https://docs.oracle.com/javase/7/docs/api/java/io/OutputStream.html#write(byte[]):
            // > The general contract for write(b) is that it should have exactly the same effect
            // > as the call write(b, 0, b.length).
            write(b, 0, b.length);
        }

        public void write(byte[] b, int offset, int length) throws IOException {
            mOut.write(b, offset, length);
            mCount += length;
            pad();
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
