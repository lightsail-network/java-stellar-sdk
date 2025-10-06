package org.stellar.sdk;

import java.math.BigInteger;

/**
 * The memo contains optional extra information. It is the responsibility of the client to interpret
 * this value. Memos can be one of the following types:
 *
 * <ul>
 *   <li><code>MEMO_NONE</code>: Empty memo.
 *   <li><code>MEMO_TEXT</code>: A string up to 28-bytes long.
 *   <li><code>MEMO_ID</code>: A 64 bit unsigned integer.
 *   <li><code>MEMO_HASH</code>: A 32 byte hash.
 *   <li><code>MEMO_RETURN</code>: A 32 byte hash intended to be interpreted as the hash of the
 *       transaction the sender is refunding.
 * </ul>
 *
 * <p>Use static methods to generate any of above types.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/learn/fundamentals/transactions/operations-and-transactions#memo"
 *     target="_blank">Memos</a>
 * @see Transaction
 */
public abstract class Memo {
  /** Creates new MemoNone instance. */
  public static MemoNone none() {
    return new MemoNone();
  }

  /**
   * Creates new {@link MemoText} instance.
   *
   * @param text Memo text.
   */
  public static MemoText text(String text) {
    return new MemoText(text);
  }

  /**
   * Creates new {@link MemoText} instance.
   *
   * @param text Memo text as bytes.
   */
  public static MemoText text(byte[] text) {
    return new MemoText(text);
  }

  /**
   * Creates new {@link MemoId} instance.
   *
   * @param id Memo id.
   */
  public static MemoId id(BigInteger id) {
    return new MemoId(id);
  }

  /**
   * Creates new {@link MemoId} instance.
   *
   * @param id Memo id.
   */
  public static MemoId id(Long id) {
    return new MemoId(id);
  }

  /**
   * Creates new {@link MemoHash} instance from byte array.
   *
   * @param bytes Memo hash bytes.
   */
  public static MemoHash hash(byte[] bytes) {
    return new MemoHash(bytes);
  }

  /**
   * Creates new {@link MemoHash} instance from hex-encoded string
   *
   * @param hexString Memo hash hex-encoded string
   */
  public static MemoHash hash(String hexString) {
    return new MemoHash(hexString);
  }

  /**
   * Creates new {@link MemoReturnHash} instance from byte array.
   *
   * @param bytes Memo return hash bytes.
   */
  public static MemoReturnHash returnHash(byte[] bytes) {
    return new MemoReturnHash(bytes);
  }

  /**
   * Creates new {@link MemoReturnHash} instance from hex-encoded string.
   *
   * @param hexString Memo return hash hex-encoded string.
   */
  public static MemoReturnHash returnHash(String hexString) {
    return new MemoReturnHash(hexString);
  }

  public static Memo fromXdr(org.stellar.sdk.xdr.Memo memo) {
    switch (memo.getDiscriminant()) {
      case MEMO_NONE:
        return none();
      case MEMO_ID:
        return id(memo.getId().getUint64().getNumber());
      case MEMO_TEXT:
        return text(memo.getText().getBytes());
      case MEMO_HASH:
        return hash(memo.getHash().getHash());
      case MEMO_RETURN:
        return returnHash(memo.getRetHash().getHash());
      default:
        throw new IllegalArgumentException("Unknown memo type");
    }
  }

  abstract org.stellar.sdk.xdr.Memo toXdr();

  public abstract boolean equals(Object o);
}
