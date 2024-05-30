// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * AccountMergeResultCode's original definition in the XDR file is:
 *
 * <pre>
 * enum AccountMergeResultCode
 * {
 *     // codes considered as "success" for the operation
 *     ACCOUNT_MERGE_SUCCESS = 0,
 *     // codes considered as "failure" for the operation
 *     ACCOUNT_MERGE_MALFORMED = -1,       // can't merge onto itself
 *     ACCOUNT_MERGE_NO_ACCOUNT = -2,      // destination does not exist
 *     ACCOUNT_MERGE_IMMUTABLE_SET = -3,   // source account has AUTH_IMMUTABLE set
 *     ACCOUNT_MERGE_HAS_SUB_ENTRIES = -4, // account has trust lines/offers
 *     ACCOUNT_MERGE_SEQNUM_TOO_FAR = -5,  // sequence number is over max allowed
 *     ACCOUNT_MERGE_DEST_FULL = -6,       // can't add source balance to
 *                                         // destination balance
 *     ACCOUNT_MERGE_IS_SPONSOR = -7       // can't merge account that is a sponsor
 * };
 * </pre>
 */
public enum AccountMergeResultCode implements XdrElement {
  ACCOUNT_MERGE_SUCCESS(0),
  ACCOUNT_MERGE_MALFORMED(-1),
  ACCOUNT_MERGE_NO_ACCOUNT(-2),
  ACCOUNT_MERGE_IMMUTABLE_SET(-3),
  ACCOUNT_MERGE_HAS_SUB_ENTRIES(-4),
  ACCOUNT_MERGE_SEQNUM_TOO_FAR(-5),
  ACCOUNT_MERGE_DEST_FULL(-6),
  ACCOUNT_MERGE_IS_SPONSOR(-7);

  private final int value;

  AccountMergeResultCode(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static AccountMergeResultCode decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return ACCOUNT_MERGE_SUCCESS;
      case -1:
        return ACCOUNT_MERGE_MALFORMED;
      case -2:
        return ACCOUNT_MERGE_NO_ACCOUNT;
      case -3:
        return ACCOUNT_MERGE_IMMUTABLE_SET;
      case -4:
        return ACCOUNT_MERGE_HAS_SUB_ENTRIES;
      case -5:
        return ACCOUNT_MERGE_SEQNUM_TOO_FAR;
      case -6:
        return ACCOUNT_MERGE_DEST_FULL;
      case -7:
        return ACCOUNT_MERGE_IS_SPONSOR;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(value);
  }

  public static AccountMergeResultCode fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static AccountMergeResultCode fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
