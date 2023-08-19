package org.stellar.sdk;

import lombok.Value;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

@Value
@lombok.Builder
/**
 * LedgerBounds are Preconditions of a transaction per <a
 * href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0021.md#specification">CAP-21</a>
 */
public class LedgerBounds {
  long minLedger;
  long maxLedger;

  public static LedgerBounds fromXdr(org.stellar.sdk.xdr.LedgerBounds xdrLedgerBounds) {
    return new LedgerBoundsBuilder()
        .minLedger(xdrLedgerBounds.getMinLedger().getUint32().getNumber())
        .maxLedger(xdrLedgerBounds.getMaxLedger().getUint32().getNumber())
        .build();
  }

  public org.stellar.sdk.xdr.LedgerBounds toXdr() {
    return new org.stellar.sdk.xdr.LedgerBounds.Builder()
        .maxLedger(new Uint32(new XdrUnsignedInteger(maxLedger)))
        .minLedger(new Uint32(new XdrUnsignedInteger(minLedger)))
        .build();
  }
}
