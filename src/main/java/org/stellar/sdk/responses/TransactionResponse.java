package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import org.stellar.sdk.Base64Factory;
import org.stellar.sdk.Memo;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.OperationMeta;
import org.stellar.sdk.xdr.TransactionEnvelope;
import org.stellar.sdk.xdr.TransactionMeta;
import org.stellar.sdk.xdr.TransactionResult;

/**
 * Represents transaction response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/transactions/"
 *     target="_blank">Transaction documentation</a>
 * @see org.stellar.sdk.requests.TransactionsRequestBuilder
 * @see org.stellar.sdk.Server#transactions()
 */
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TransactionResponse extends Response implements Pageable {
  @Getter
  @SerializedName("id")
  private final String id;

  @Getter
  @SerializedName("paging_token")
  private final String pagingToken;

  @Getter
  @SerializedName("successful")
  private final Boolean successful;

  @Getter
  @SerializedName("hash")
  private final String hash;

  @Getter
  @SerializedName("ledger")
  private final Long ledger;

  @Getter
  @SerializedName("created_at")
  private final String createdAt;

  @Getter
  @SerializedName("source_account")
  private final String sourceAccount;

  @Getter
  @SerializedName("account_muxed")
  private final String accountMuxed;

  @Getter
  @SerializedName("account_muxed_id")
  private final BigInteger accountMuxedId;

  @Getter
  @SerializedName("source_account_sequence")
  private final Long sourceAccountSequence;

  @Getter
  @SerializedName("fee_account")
  private final String feeAccount;

  @SerializedName("fee_account_muxed")
  private final String feeAccountMuxed;

  @Getter
  @SerializedName("fee_account_muxed_id")
  private final BigInteger feeAccountMuxedId;

  @Getter
  @SerializedName("fee_charged")
  private final Long feeCharged;

  @Getter
  @SerializedName("max_fee")
  private final Long maxFee;

  @Getter
  @SerializedName("operation_count")
  private final Integer operationCount;

  @Getter
  @SerializedName("envelope_xdr")
  private final String envelopeXdr;

  @Getter
  @SerializedName("result_xdr")
  private final String resultXdr;

  @Getter
  @SerializedName("result_meta_xdr")
  private final String resultMetaXdr;

  @Getter
  @SerializedName("fee_meta_xdr")
  private final String feeMetaXdr;

  @Getter
  @SerializedName("signatures")
  private final List<String> signatures;

  @SerializedName("preconditions")
  private final Preconditions preconditions;

  @Getter
  @SerializedName("fee_bump_transaction")
  private final FeeBumpTransaction feeBumpTransaction;

  @Getter
  @SerializedName("inner_transaction")
  private final InnerTransaction innerTransaction;

  @SerializedName("memo_type")
  private final String memoType;

  @SerializedName("memo_bytes")
  private final String memoBytes;

  @SerializedName("memo")
  private final String memoValue;

  @Getter
  @SerializedName("_links")
  private final Links links;

  /**
   * Parses the {@code envelopeXdr} field from a string to an {@link
   * org.stellar.sdk.xdr.TransactionEnvelope} object.
   *
   * @return the parsed {@link org.stellar.sdk.xdr.TransactionEnvelope} object
   */
  public TransactionEnvelope parseEnvelopeXdr() {
    return Util.parseXdr(envelopeXdr, TransactionEnvelope::fromXdrBase64);
  }

  /**
   * Parses the {@code resultXdr} field from a string to an {@link
   * org.stellar.sdk.xdr.TransactionResult} object.
   *
   * @return the parsed {@link org.stellar.sdk.xdr.TransactionResult} object
   */
  public TransactionResult parseResultXdr() {
    return Util.parseXdr(resultXdr, TransactionResult::fromXdrBase64);
  }

  /**
   * Parses the {@code resultMetaXdr} field from a string to an {@link
   * org.stellar.sdk.xdr.TransactionMeta} object.
   *
   * @return the parsed {@link org.stellar.sdk.xdr.TransactionMeta} object
   */
  public TransactionMeta parseResultMetaXdr() {
    return Util.parseXdr(resultMetaXdr, TransactionMeta::fromXdrBase64);
  }

  /**
   * Parses the {@code feeMetaXdr} field from a string to an {@link
   * org.stellar.sdk.xdr.OperationMeta} object.
   *
   * @return the parsed {@link org.stellar.sdk.xdr.OperationMeta} object
   */
  public OperationMeta parseFeeMetaXdr() {
    return Util.parseXdr(feeMetaXdr, OperationMeta::fromXdrBase64);
  }

  /**
   * @return {@link Memo} object from this transaction.
   */
  public Memo getMemo() {
    if ("none".equals(memoType)) {
      return Memo.none();
    } else if ("text".equals(memoType)) {
      return Memo.text(Base64Factory.getInstance().decode(memoBytes));
    } else if ("id".equals(memoType)) {
      return Memo.id(new BigInteger(memoValue));
    } else if ("hash".equals(memoType)) {
      return Memo.hash(Base64Factory.getInstance().decode(memoValue));
    } else if ("return".equals(memoType)) {
      return Memo.returnHash(Base64Factory.getInstance().decode(memoValue));
    } else {
      throw new IllegalArgumentException("Invalid memo type: " + memoType);
    }
  }

  public Optional<MuxedAccount> getSourceAccountMuxed() {
    if (this.accountMuxed == null || this.accountMuxed.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(
        new MuxedAccount(this.accountMuxed, this.sourceAccount, this.accountMuxedId));
  }

  public Optional<MuxedAccount> getFeeAccountMuxed() {
    if (this.feeAccountMuxed == null || this.feeAccountMuxed.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(
        new MuxedAccount(this.feeAccountMuxed, this.feeAccount, this.feeAccountMuxedId));
  }

  public Optional<FeeBumpTransaction> getFeeBump() {
    return Optional.ofNullable(this.feeBumpTransaction);
  }

  public Optional<InnerTransaction> getInner() {
    return Optional.ofNullable(this.innerTransaction);
  }

  public Optional<Preconditions> getPreconditions() {
    return Optional.ofNullable(this.preconditions);
  }

  public Boolean isSuccessful() {
    return successful;
  }

  /**
   * Preconditions of a transaction per <a
   * href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0021.md#specification">CAP-21</a>
   */
  @Value
  public static class Preconditions {
    @SerializedName("timebounds")
    TimeBounds timeBounds;

    @SerializedName("ledgerbounds")
    LedgerBounds ledgerBounds;

    @SerializedName("min_account_sequence")
    Long minAccountSequence;

    @SerializedName("min_account_sequence_age")
    long minAccountSequenceAge;

    @SerializedName("min_account_sequence_ledger_gap")
    long minAccountSequenceLedgerGap;

    @SerializedName("extra_signers")
    List<String> signatures;

    @Value
    public static class TimeBounds {
      @SerializedName("min_time")
      long minTime;

      @SerializedName("max_time")
      long maxTime;
    }

    @Value
    public static class LedgerBounds {
      @SerializedName("min_ledger")
      long minTime;

      @SerializedName("max_ledger")
      long maxTime;
    }
  }

  /**
   * FeeBumpTransaction is only present in a TransactionResponse if the transaction is a fee bump
   * transaction or is wrapped by a fee bump transaction. The object has two fields: the hash of the
   * fee bump transaction and the signatures present in the fee bump transaction envelope.
   */
  @Value
  public static class FeeBumpTransaction {
    @SerializedName("hash")
    String hash;

    @SerializedName("signatures")
    List<String> signatures;
  }

  /**
   * InnerTransaction is only present in a TransactionResponse if the transaction is a fee bump
   * transaction or is wrapped by a fee bump transaction. The object has three fields: the hash of
   * the inner transaction wrapped by the fee bump transaction, the max fee set in the inner
   * transaction, and the signatures present in the inner transaction envelope.
   */
  @Value
  public static class InnerTransaction {
    @SerializedName("hash")
    String hash;

    @SerializedName("signatures")
    List<String> signatures;

    @SerializedName("max_fee")
    Long maxFee;
  }

  /** Links connected to transaction. */
  @Value
  public static class Links {
    @SerializedName("self")
    Link self;

    @SerializedName("account")
    Link account;

    @SerializedName("ledger")
    Link ledger;

    @SerializedName("operations")
    Link operations;

    @SerializedName("effects")
    Link effects;

    @SerializedName("precedes")
    Link precedes;

    @SerializedName("succeeds")
    Link succeeds;
  }
}
