package org.stellar.sdk;

import com.google.common.io.BaseEncoding;
import org.stellar.sdk.xdr.MemoType;

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

    abstract org.stellar.sdk.xdr.Memo toXdr();

    /**
     * Creates new Memo from XDR-encoded memo.
     * @param xdrMemo
     * @return the new Memo instance
     */
    public static Memo fromXdr(org.stellar.sdk.xdr.Memo xdrMemo) {
        MemoType memoType = xdrMemo.getDiscriminant();
        switch (memoType) {
            case MEMO_NONE:
                return none();
            case MEMO_TEXT:
                return text(xdrMemo.getText());
            case MEMO_ID:
                return id(xdrMemo.getId().getUint64());
            case MEMO_HASH:
                return hash(xdrMemo.getHash().getHash());
            case MEMO_RETURN:
                return returnHash(xdrMemo.getRetHash().getHash());
            default:
                // all possible cases were covered above,
                // so this code should never be reached
                throw new RuntimeException("Unknown enum value:" + memoType);
        }
    }

}
