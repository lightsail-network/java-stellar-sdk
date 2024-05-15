// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * TransactionResultCode's original definition in the XDR file is:
 *
 * <pre>
 * enum TransactionResultCode
 * {
 *     txFEE_BUMP_INNER_SUCCESS = 1, // fee bump inner transaction succeeded
 *     txSUCCESS = 0,                // all operations succeeded
 *
 *     txFAILED = -1, // one of the operations failed (none were applied)
 *
 *     txTOO_EARLY = -2,         // ledger closeTime before minTime
 *     txTOO_LATE = -3,          // ledger closeTime after maxTime
 *     txMISSING_OPERATION = -4, // no operation was specified
 *     txBAD_SEQ = -5,           // sequence number does not match source account
 *
 *     txBAD_AUTH = -6,             // too few valid signatures / wrong network
 *     txINSUFFICIENT_BALANCE = -7, // fee would bring account below reserve
 *     txNO_ACCOUNT = -8,           // source account not found
 *     txINSUFFICIENT_FEE = -9,     // fee is too small
 *     txBAD_AUTH_EXTRA = -10,      // unused signatures attached to transaction
 *     txINTERNAL_ERROR = -11,      // an unknown error occurred
 *
 *     txNOT_SUPPORTED = -12,          // transaction type not supported
 *     txFEE_BUMP_INNER_FAILED = -13,  // fee bump inner transaction failed
 *     txBAD_SPONSORSHIP = -14,        // sponsorship not confirmed
 *     txBAD_MIN_SEQ_AGE_OR_GAP = -15, // minSeqAge or minSeqLedgerGap conditions not met
 *     txMALFORMED = -16,              // precondition is invalid
 *     txSOROBAN_INVALID = -17         // soroban-specific preconditions were not met
 * };
 * </pre>
 */
public enum TransactionResultCode implements XdrElement {
  txFEE_BUMP_INNER_SUCCESS(1),
  txSUCCESS(0),
  txFAILED(-1),
  txTOO_EARLY(-2),
  txTOO_LATE(-3),
  txMISSING_OPERATION(-4),
  txBAD_SEQ(-5),
  txBAD_AUTH(-6),
  txINSUFFICIENT_BALANCE(-7),
  txNO_ACCOUNT(-8),
  txINSUFFICIENT_FEE(-9),
  txBAD_AUTH_EXTRA(-10),
  txINTERNAL_ERROR(-11),
  txNOT_SUPPORTED(-12),
  txFEE_BUMP_INNER_FAILED(-13),
  txBAD_SPONSORSHIP(-14),
  txBAD_MIN_SEQ_AGE_OR_GAP(-15),
  txMALFORMED(-16),
  txSOROBAN_INVALID(-17),
  ;
  private int mValue;

  TransactionResultCode(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  public static TransactionResultCode decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 1:
        return txFEE_BUMP_INNER_SUCCESS;
      case 0:
        return txSUCCESS;
      case -1:
        return txFAILED;
      case -2:
        return txTOO_EARLY;
      case -3:
        return txTOO_LATE;
      case -4:
        return txMISSING_OPERATION;
      case -5:
        return txBAD_SEQ;
      case -6:
        return txBAD_AUTH;
      case -7:
        return txINSUFFICIENT_BALANCE;
      case -8:
        return txNO_ACCOUNT;
      case -9:
        return txINSUFFICIENT_FEE;
      case -10:
        return txBAD_AUTH_EXTRA;
      case -11:
        return txINTERNAL_ERROR;
      case -12:
        return txNOT_SUPPORTED;
      case -13:
        return txFEE_BUMP_INNER_FAILED;
      case -14:
        return txBAD_SPONSORSHIP;
      case -15:
        return txBAD_MIN_SEQ_AGE_OR_GAP;
      case -16:
        return txMALFORMED;
      case -17:
        return txSOROBAN_INVALID;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, TransactionResultCode value)
      throws IOException {
    stream.writeInt(value.getValue());
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64Factory.getInstance().encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static TransactionResultCode fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TransactionResultCode fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
