package org.stellar.sdk;

import com.google.common.io.BaseEncoding;

/**
 * <p>The memo contains optional extra information. It is the responsibility of the client to interpret this value. Memos can be one of the following types:</p>
 * <ul>
 *  <li><code>MEMO_NONE</code>: Empty memo.</li>
 *  <li><code>MEMO_TEXT</code>: A string up to 28-bytes long.</li>
 *  <li><code>MEMO_ID</code>: A 64 bit unsigned integer.</li>
 *  <li><code>MEMO_HASH</code>: A 32 byte hash.</li>
 *  <li><code>MEMO_RETURN</code>: A 32 byte hash intended to be interpreted as the hash of the transaction the sender is refunding.</li>
 * </ul>
 * <p>Use static methods to generate any of above types.</p>
 * @see Transaction
 */
public abstract class Memo {
    /**
     * Creates new MemoNone instance.
     */
    public static MemoNone none() {
        return new MemoNone();
    }

    /**
     * Creates new {@link MemoText} instance.
     * @param text
     */
    public static MemoText text(String text) {
        return new MemoText(text);
    }

    /**
     * Creates new {@link MemoId} instance.
     * @param id
     */
    public static MemoId id(long id) {
        return new MemoId(id);
    }

    /**
     * Creates new {@link MemoHash} instance from byte array.
     * @param bytes
     */
    public static MemoHash hash(byte[] bytes) {
        return new MemoHash(bytes);
    }

    /**
     * Creates new {@link MemoHash} instance from hex-encoded string
     * @param hexString
     */
    public static MemoHash hash(String hexString) {
        return new MemoHash(hexString);
    }

    /**
     * Creates new {@link MemoReturnHash} instance from byte array.
     * @param bytes
     */
    public static MemoReturnHash returnHash(byte[] bytes) {
        return new MemoReturnHash(bytes);
    }

    /**
     * Creates new {@link MemoReturnHash} instance from hex-encoded string.
     * @param hexString
     */
    public static MemoReturnHash returnHash(String hexString) {
        // We change to lowercase because we want to decode both: upper cased and lower cased alphabets.
        return new MemoReturnHash(BaseEncoding.base16().lowerCase().decode(hexString.toLowerCase()));
    }

    /**
     * Creates a Memo from an XDR Memo instance.
     */
    public static Memo fromXdr(org.stellar.sdk.xdr.Memo memo) {
        if (memo == null) {
            return Memo.none();
        }
        switch (memo.getDiscriminant()) {
            case MEMO_NONE:
                return Memo.none();
            case MEMO_TEXT:
                return new MemoText(memo.getText());
            case MEMO_ID:
                return new MemoId(memo.getId().getUint64());
            case MEMO_HASH:
                return new MemoHash(memo.getHash().getHash());
            case MEMO_RETURN:
                return new MemoReturnHash(memo.getHash().getHash());
            default:
                throw new AssertionError("Unrecognized memo discriminant: " + memo.getDiscriminant());
        }
    }

    abstract org.stellar.sdk.xdr.Memo toXdr();
}
