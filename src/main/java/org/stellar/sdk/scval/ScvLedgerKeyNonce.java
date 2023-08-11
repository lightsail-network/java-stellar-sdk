package org.stellar.sdk.scval;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.SCNonceKey;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_LEDGER_KEY_NONCE}. */
@Value
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
class ScvLedgerKeyNonce extends Scv {
  private static final SCValType TYPE = SCValType.SCV_LEDGER_KEY_NONCE;

  long value;

  @Override
  public SCVal toSCVal() {
    return new SCVal.Builder()
        .discriminant(TYPE)
        .nonce_key(new SCNonceKey.Builder().nonce(new Int64(value)).build())
        .build();
  }

  public static ScvLedgerKeyNonce fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }
    return new ScvLedgerKeyNonce(scVal.getNonce_key().getNonce().getInt64());
  }
}
