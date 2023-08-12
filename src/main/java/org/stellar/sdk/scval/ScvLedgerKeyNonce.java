package org.stellar.sdk.scval;

import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.SCNonceKey;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_LEDGER_KEY_NONCE}. */
class ScvLedgerKeyNonce {
  private static final SCValType TYPE = SCValType.SCV_LEDGER_KEY_NONCE;

  static SCVal toSCVal(long value) {
    return new SCVal.Builder()
        .discriminant(TYPE)
        .nonce_key(new SCNonceKey.Builder().nonce(new Int64(value)).build())
        .build();
  }

  static long fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }
    return scVal.getNonce_key().getNonce().getInt64();
  }
}
