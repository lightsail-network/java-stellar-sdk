package org.stellar.sdk.scval;

import java.math.BigInteger;
import java.util.Arrays;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Abstract class for all {@link Scv} types. */
public abstract class Scv {

  /**
   * Convert this object to {@link SCVal}.
   *
   * @return {@link SCVal}
   */
  public abstract SCVal toSCVal();

  /**
   * Get the {@link SCValType} of this object.
   *
   * @return {@link SCValType}
   */
  public abstract SCValType getSCValType();

  /**
   * Convert from {@link SCVal} to {@link Scv}.
   *
   * @param scVal {@link SCVal} to convert
   * @return {@link Scv}
   */
  public static Scv fromSCVal(SCVal scVal) {
    switch (scVal.getDiscriminant()) {
      case SCV_BOOL:
        return ScvBoolean.fromSCVal(scVal);
      case SCV_VOID:
        return ScvVoid.fromSCVal(scVal);
      case SCV_ERROR:
        return ScvError.fromSCVal(scVal);
      case SCV_U32:
        return ScvUint32.fromSCVal(scVal);
      case SCV_I32:
        return ScvInt32.fromSCVal(scVal);
      case SCV_U64:
        return ScvUint64.fromSCVal(scVal);
      case SCV_I64:
        return ScvInt64.fromSCVal(scVal);
      case SCV_TIMEPOINT:
        return ScvTimePoint.fromSCVal(scVal);
      case SCV_DURATION:
        return ScvDuration.fromSCVal(scVal);
      case SCV_U128:
        return ScvUint128.fromSCVal(scVal);
      case SCV_I128:
        return ScvInt128.fromSCVal(scVal);
      case SCV_U256:
        return ScvUint256.fromSCVal(scVal);
      case SCV_I256:
        return ScvInt256.fromSCVal(scVal);
      case SCV_BYTES:
        return ScvBytes.fromSCVal(scVal);
      case SCV_STRING:
        return ScvString.fromSCVal(scVal);
      case SCV_SYMBOL:
        return ScvSymbol.fromSCVal(scVal);
      case SCV_VEC:
        return ScvVec.fromSCVal(scVal);
      case SCV_MAP:
        return ScvMap.fromSCVal(scVal);
      case SCV_ADDRESS:
        return ScvAddress.fromSCVal(scVal);
      case SCV_CONTRACT_INSTANCE:
        return ScvContractInstance.fromSCVal(scVal);
      case SCV_LEDGER_KEY_CONTRACT_INSTANCE:
        return ScvLedgerKeyContractInstance.fromSCVal(scVal);
      case SCV_LEDGER_KEY_NONCE:
        return ScvLedgerKeyNonce.fromSCVal(scVal);
      default:
        throw new IllegalArgumentException(
            String.format("unsupported scVal type: %s", scVal.getDiscriminant()));
    }
  }

  static byte[] getBytes(BigInteger value) {
    byte[] bytes = value.toByteArray();
    if (bytes.length < 8) {
      byte[] temp = new byte[8];
      System.arraycopy(bytes, 0, temp, 8 - bytes.length, bytes.length);
      bytes = temp;
    } else if (bytes.length > 8) {
      bytes = Arrays.copyOfRange(bytes, bytes.length - 8, bytes.length);
    }
    return bytes;
  }
}
