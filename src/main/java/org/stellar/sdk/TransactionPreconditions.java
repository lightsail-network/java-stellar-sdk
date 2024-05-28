package org.stellar.sdk;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import org.stellar.sdk.xdr.Duration;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.PreconditionType;
import org.stellar.sdk.xdr.Preconditions;
import org.stellar.sdk.xdr.PreconditionsV2;
import org.stellar.sdk.xdr.SequenceNumber;
import org.stellar.sdk.xdr.SignerKey;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

/**
 * Preconditions of a transaction per <a
 * href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0021.md#specification">CAP-21</a>
 */
@Value
@Builder(toBuilder = true)
public class TransactionPreconditions {
  public static final long MAX_EXTRA_SIGNERS_COUNT = 2;
  public static final BigInteger TIMEOUT_INFINITE = BigInteger.ZERO;

  /** The ledger bounds for the transaction. */
  LedgerBounds ledgerBounds;

  /**
   * The minimum source account sequence number this transaction is valid for. if <code>null</code>,
   * the transaction is valid when **source account's sequence number == tx.sequence - 1**.
   */
  Long minSeqNumber; // int64

  /**
   * The minimum amount of time between source account sequence time and the ledger time when this
   * transaction will become valid. If the value is <code>0</code>, the transaction is unrestricted
   * by the account sequence age. Cannot be negative.
   */
  @Builder.Default BigInteger minSeqAge = BigInteger.ZERO; // uint64

  /**
   * The minimum number of ledgers between source account sequence and the ledger number when this
   * transaction will become valid. If the value is <code>0</code>, the transaction is unrestricted
   * by the account sequence ledger. Cannot be negative.
   */
  long minSeqLedgerGap; // uint32

  /** Required extra signers. */
  @Singular @NonNull List<SignerKey> extraSigners;

  /** The time bounds for the transaction. */
  TimeBounds timeBounds;

  public void isValid() {
    if (timeBounds == null) {
      throw new FormatException("Invalid preconditions, must define timebounds");
    }

    if (extraSigners.size() > MAX_EXTRA_SIGNERS_COUNT) {
      throw new FormatException(
          "Invalid preconditions, too many extra signers, can only have up to "
              + MAX_EXTRA_SIGNERS_COUNT);
    }
  }

  public boolean hasV2() {
    return (ledgerBounds != null
        || (minSeqLedgerGap > 0)
        || (minSeqAge.compareTo(BigInteger.ZERO) > 0)
        || minSeqNumber != null
        || !extraSigners.isEmpty());
  }

  public static TransactionPreconditions fromXdr(Preconditions preconditions) {
    TransactionPreconditionsBuilder builder = new TransactionPreconditionsBuilder();

    if (preconditions.getDiscriminant().equals(PreconditionType.PRECOND_V2)) {
      if (preconditions.getV2().getTimeBounds() != null) {
        builder.timeBounds(
            new TimeBounds(
                preconditions
                    .getV2()
                    .getTimeBounds()
                    .getMinTime()
                    .getTimePoint()
                    .getUint64()
                    .getNumber(),
                preconditions
                    .getV2()
                    .getTimeBounds()
                    .getMaxTime()
                    .getTimePoint()
                    .getUint64()
                    .getNumber()));
      }
      if (preconditions.getV2().getExtraSigners() != null
          && preconditions.getV2().getExtraSigners().length > 0) {
        builder.extraSigners(Arrays.asList(preconditions.getV2().getExtraSigners()));
      }
      if (preconditions.getV2().getMinSeqAge() != null) {
        builder.minSeqAge(
            preconditions.getV2().getMinSeqAge().getDuration().getUint64().getNumber());
      }
      if (preconditions.getV2().getLedgerBounds() != null) {
        builder.ledgerBounds(LedgerBounds.fromXdr(preconditions.getV2().getLedgerBounds()));
      }
      if (preconditions.getV2().getMinSeqNum() != null) {
        builder.minSeqNumber(preconditions.getV2().getMinSeqNum().getSequenceNumber().getInt64());
      }
      if (preconditions.getV2().getMinSeqLedgerGap() != null) {
        builder.minSeqLedgerGap(
            preconditions.getV2().getMinSeqLedgerGap().getUint32().getNumber().intValue());
      }
    } else {
      if (preconditions.getTimeBounds() != null) {
        builder.timeBounds(
            new TimeBounds(
                preconditions.getTimeBounds().getMinTime().getTimePoint().getUint64().getNumber(),
                preconditions.getTimeBounds().getMaxTime().getTimePoint().getUint64().getNumber()));
      }
    }

    return builder.build();
  }

  public Preconditions toXdr() {
    Preconditions.PreconditionsBuilder preconditionsBuilder = Preconditions.builder();

    if (hasV2()) {
      preconditionsBuilder.discriminant(PreconditionType.PRECOND_V2);
      PreconditionsV2.PreconditionsV2Builder v2Builder = PreconditionsV2.builder();
      v2Builder.extraSigners(extraSigners.toArray(new SignerKey[] {}));
      v2Builder.minSeqAge(new Duration(new Uint64(new XdrUnsignedHyperInteger(minSeqAge))));

      if (ledgerBounds != null) {
        v2Builder.ledgerBounds(
            org.stellar.sdk.xdr.LedgerBounds.builder()
                .minLedger(new Uint32(new XdrUnsignedInteger(ledgerBounds.getMinLedger())))
                .maxLedger(new Uint32(new XdrUnsignedInteger(ledgerBounds.getMaxLedger())))
                .build());
      }
      if (minSeqNumber != null) {
        v2Builder.minSeqNum(new SequenceNumber(new Int64(minSeqNumber)));
      }

      v2Builder.minSeqLedgerGap(new Uint32(new XdrUnsignedInteger(minSeqLedgerGap)));

      if (timeBounds != null) {
        v2Builder.timeBounds(timeBounds.toXdr());
      }
      preconditionsBuilder.v2(v2Builder.build());
    } else {
      if (timeBounds == null) {
        preconditionsBuilder.discriminant(PreconditionType.PRECOND_NONE);
      } else {
        preconditionsBuilder.discriminant(PreconditionType.PRECOND_TIME);
        preconditionsBuilder.timeBounds(timeBounds.toXdr());
      }
    }

    return preconditionsBuilder.build();
  }
}
