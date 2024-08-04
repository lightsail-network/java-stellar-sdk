package org.stellar.sdk;

import lombok.Value;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

/**
 * LedgerBounds are Preconditions of a transaction per <a
 * href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0021.md#specification">CAP-21</a>
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/learn/fundamentals/transactions/operations-and-transactions#ledger-bounds"
 *     target="_blank">LedgerBounds</a>
 */
@Value
public class LedgerBounds {
  /** Minimum ledger sequence number of the transaction. */
  long minLedger;

  /** Maximum ledger sequence number of the transaction. */
  long maxLedger;

  public LedgerBounds(long minLedger, long maxLedger) {
    if (minLedger < XdrUnsignedInteger.MIN_VALUE || minLedger > XdrUnsignedInteger.MAX_VALUE) {
      throw new IllegalArgumentException("minLedger must be between 0 and 2^32-1");
    }
    if (maxLedger < XdrUnsignedInteger.MIN_VALUE || maxLedger > XdrUnsignedInteger.MAX_VALUE) {
      throw new IllegalArgumentException("maxLedger must be between 0 and 2^32-1");
    }
    if (maxLedger > 0 && minLedger > maxLedger) {
      throw new IllegalArgumentException("minLedger can not be greater than maxLedger");
    }
    this.minLedger = minLedger;
    this.maxLedger = maxLedger;
  }

  /**
   * Creates a new LedgerBounds object.
   *
   * @param xdrLedgerBounds XDR LedgerBounds object to convert.
   * @return LedgerBounds
   */
  public static LedgerBounds fromXdr(org.stellar.sdk.xdr.LedgerBounds xdrLedgerBounds) {
    return new LedgerBounds(
        xdrLedgerBounds.getMinLedger().getUint32().getNumber(),
        xdrLedgerBounds.getMaxLedger().getUint32().getNumber());
  }

  /**
   * Returns the XDR LedgerBounds object.
   *
   * @return the XDR LedgerBounds object.
   */
  public org.stellar.sdk.xdr.LedgerBounds toXdr() {
    return org.stellar.sdk.xdr.LedgerBounds.builder()
        .maxLedger(new Uint32(new XdrUnsignedInteger(maxLedger)))
        .minLedger(new Uint32(new XdrUnsignedInteger(minLedger)))
        .build();
  }
}
