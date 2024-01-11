package org.stellar.sdk;

import lombok.Builder;
import lombok.Value;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

/**
 * LedgerBounds are Preconditions of a transaction per <a
 * href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0021.md#specification">CAP-21</a>
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/fundamentals-and-concepts/stellar-data-structures/operations-and-transactions#ledger-bounds"
 *     target="_blank">LedgerBounds</a>
 */
@Value
@Builder
public class LedgerBounds {
  /** Minimum ledger sequence number of the transaction. */
  long minLedger;

  /** Maximum ledger sequence number of the transaction. */
  long maxLedger;

  /**
   * Creates a new LedgerBounds object.
   *
   * @param xdrLedgerBounds XDR LedgerBounds object to convert.
   * @return LedgerBounds
   */
  public static LedgerBounds fromXdr(org.stellar.sdk.xdr.LedgerBounds xdrLedgerBounds) {
    return new LedgerBoundsBuilder()
        .minLedger(xdrLedgerBounds.getMinLedger().getUint32().getNumber())
        .maxLedger(xdrLedgerBounds.getMaxLedger().getUint32().getNumber())
        .build();
  }

  /**
   * Returns the XDR LedgerBounds object.
   *
   * @return the XDR LedgerBounds object.
   */
  public org.stellar.sdk.xdr.LedgerBounds toXdr() {
    return new org.stellar.sdk.xdr.LedgerBounds.Builder()
        .maxLedger(new Uint32(new XdrUnsignedInteger(maxLedger)))
        .minLedger(new Uint32(new XdrUnsignedInteger(minLedger)))
        .build();
  }
}
