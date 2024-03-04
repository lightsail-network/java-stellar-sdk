package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import org.stellar.sdk.Memo;

/**
 * Represents transaction response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/transactions/"
 *     target="_blank">Transaction documentation</a>
 * @see org.stellar.sdk.requests.TransactionsRequestBuilder
 * @see org.stellar.sdk.Server#transactions()
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TransactionResponse extends Response implements Pageable {
  @SerializedName("id")
  private final String id;

  @SerializedName("paging_token")
  private final String pagingToken;

  @SerializedName("successful")
  private final Boolean successful;

  @SerializedName("hash")
  private final String hash;

  @SerializedName("ledger")
  private final Long ledger;

  @SerializedName("created_at")
  private final String createdAt;

  @SerializedName("source_account")
  private final String sourceAccount;

  @SerializedName("account_muxed")
  private final String accountMuxed;

  @SerializedName("account_muxed_id")
  private final BigInteger accountMuxedId;

  @SerializedName("source_account_sequence")
  private final Long sourceAccountSequence;

  @SerializedName("fee_account")
  private final String feeAccount;

  @SerializedName("fee_account_muxed")
  private final String feeAccountMuxed;

  @SerializedName("fee_account_muxed_id")
  private final BigInteger feeAccountMuxedId;

  @SerializedName("fee_charged")
  private final Long feeCharged;

  @SerializedName("max_fee")
  private final Long maxFee;

  @SerializedName("operation_count")
  private final Integer operationCount;

  @SerializedName("envelope_xdr")
  private final String envelopeXdr;

  @SerializedName("result_xdr")
  private final String resultXdr;

  @SerializedName("result_meta_xdr")
  private final String resultMetaXdr;

  @SerializedName("fee_meta_xdr")
  private final String feeMetaXdr;

  // GSON won't serialize `transient` variables automatically. We need this behaviour
  // because Memo is an abstract class and GSON tries to instantiate it.
  private transient Memo memo;

  @SerializedName("signatures")
  private final List<String> signatures;

  @SerializedName("preconditions")
  private final Preconditions preconditions;

  @SerializedName("fee_bump_transaction")
  private final FeeBumpTransaction feeBumpTransaction;

  @SerializedName("inner_transaction")
  private final InnerTransaction innerTransaction;

  @SerializedName("_links")
  private final Links links;

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

  public void setMemo(@NonNull Memo memo) {
    if (this.memo != null) {
      throw new RuntimeException("Memo has been already set.");
    }
    this.memo = memo;
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
