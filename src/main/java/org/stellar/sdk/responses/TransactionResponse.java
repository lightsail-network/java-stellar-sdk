package org.stellar.sdk.responses;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;
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
public class TransactionResponse extends Response implements Pageable {
  @SerializedName("hash")
  private String hash;

  @SerializedName("ledger")
  private Long ledger;

  @SerializedName("created_at")
  private String createdAt;

  @SerializedName("source_account")
  private String sourceAccount;

  @SerializedName("fee_account")
  private String feeAccount;

  @SerializedName("successful")
  private Boolean successful;

  @SerializedName("paging_token")
  private String pagingToken;

  @SerializedName("source_account_sequence")
  private Long sourceAccountSequence;

  @SerializedName("max_fee")
  private Long maxFee;

  @SerializedName("fee_charged")
  private Long feeCharged;

  @SerializedName("operation_count")
  private Integer operationCount;

  @SerializedName("envelope_xdr")
  private String envelopeXdr;

  @SerializedName("result_xdr")
  private String resultXdr;

  @SerializedName("result_meta_xdr")
  private String resultMetaXdr;

  @SerializedName("signatures")
  private List<String> signatures;

  @SerializedName("fee_bump_transaction")
  private FeeBumpTransaction feeBumpTransaction;

  @SerializedName("preconditions")
  private Preconditions preconditions;

  @SerializedName("inner_transaction")
  private InnerTransaction innerTransaction;

  @SerializedName("account_muxed")
  private String accountMuxed;

  @SerializedName("account_muxed_id")
  private BigInteger accountMuxedId;

  @SerializedName("fee_account_muxed")
  private String feeAccountMuxed;

  @SerializedName("fee_account_muxed_id")
  private BigInteger feeAccountMuxedId;

  @SerializedName("_links")
  private Links links;

  // GSON won't serialize `transient` variables automatically. We need this behaviour
  // because Memo is an abstract class and GSON tries to instantiate it.
  private transient Memo memo;

  public Optional<MuxedAccount> getSourceAccountMuxed() {
    if (this.accountMuxed == null || this.accountMuxed.isEmpty()) {
      return Optional.absent();
    }
    return Optional.of(
        new MuxedAccount(this.accountMuxed, this.sourceAccount, this.accountMuxedId));
  }

  public Optional<MuxedAccount> getFeeAccountMuxed() {
    if (this.feeAccountMuxed == null || this.feeAccountMuxed.isEmpty()) {
      return Optional.absent();
    }
    return Optional.of(
        new MuxedAccount(this.feeAccountMuxed, this.feeAccount, this.feeAccountMuxedId));
  }

  public String getHash() {
    return hash;
  }

  public Long getLedger() {
    return ledger;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public String getSourceAccount() {
    return sourceAccount;
  }

  public String getFeeAccount() {
    return feeAccount;
  }

  public List<String> getSignatures() {
    return signatures;
  }

  public Optional<FeeBumpTransaction> getFeeBump() {
    return Optional.fromNullable(this.feeBumpTransaction);
  }

  public Optional<InnerTransaction> getInner() {
    return Optional.fromNullable(this.innerTransaction);
  }

  public Optional<Preconditions> getPreconditions() {
    return Optional.fromNullable(this.preconditions);
  }

  public String getPagingToken() {
    return pagingToken;
  }

  public Boolean isSuccessful() {
    return successful;
  }

  public Long getSourceAccountSequence() {
    return sourceAccountSequence;
  }

  public Long getMaxFee() {
    return maxFee;
  }

  public Long getFeeCharged() {
    return feeCharged;
  }

  public Integer getOperationCount() {
    return operationCount;
  }

  public String getEnvelopeXdr() {
    return envelopeXdr;
  }

  public String getResultXdr() {
    return resultXdr;
  }

  public String getResultMetaXdr() {
    return resultMetaXdr;
  }

  public Memo getMemo() {
    return memo;
  }

  public void setMemo(Memo memo) {
    memo = checkNotNull(memo, "memo cannot be null");
    if (this.memo != null) {
      throw new RuntimeException("Memo has been already set.");
    }
    this.memo = memo;
  }

  public Links getLinks() {
    return links;
  }

  /**
   * Preconditions of a transaction per <a
   * href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0021.md#specification">CAP-21<a/>
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
  public static class FeeBumpTransaction {
    @SerializedName("hash")
    private String hash;

    @SerializedName("signatures")
    private List<String> signatures;

    FeeBumpTransaction(String hash, List<String> signatures) {
      this.hash = hash;
      this.signatures = signatures;
    }

    public String getHash() {
      return hash;
    }

    public List<String> getSignatures() {
      return signatures;
    }
  }

  /**
   * InnerTransaction is only present in a TransactionResponse if the transaction is a fee bump
   * transaction or is wrapped by a fee bump transaction. The object has three fields: the hash of
   * the inner transaction wrapped by the fee bump transaction, the max fee set in the inner
   * transaction, and the signatures present in the inner transaction envelope.
   */
  public static class InnerTransaction {
    @SerializedName("hash")
    private String hash;

    @SerializedName("signatures")
    private List<String> signatures;

    @SerializedName("max_fee")
    private Long maxFee;

    InnerTransaction(String hash, List<String> signatures, Long maxFee) {
      this.hash = hash;
      this.signatures = signatures;
      this.maxFee = maxFee;
    }

    public String getHash() {
      return hash;
    }

    public List<String> getSignatures() {
      return signatures;
    }

    public Long getMaxFee() {
      return maxFee;
    }
  }

  /** Links connected to transaction. */
  public static class Links {
    @SerializedName("account")
    private Link account;

    @SerializedName("effects")
    private Link effects;

    @SerializedName("ledger")
    private Link ledger;

    @SerializedName("operations")
    private Link operations;

    @SerializedName("precedes")
    private Link precedes;

    @SerializedName("self")
    private Link self;

    @SerializedName("succeeds")
    private Link succeeds;

    Links(
        Link account,
        Link effects,
        Link ledger,
        Link operations,
        Link self,
        Link precedes,
        Link succeeds) {
      this.account = account;
      this.effects = effects;
      this.ledger = ledger;
      this.operations = operations;
      this.self = self;
      this.precedes = precedes;
      this.succeeds = succeeds;
    }

    public Link getAccount() {
      return account;
    }

    public Link getEffects() {
      return effects;
    }

    public Link getLedger() {
      return ledger;
    }

    public Link getOperations() {
      return operations;
    }

    public Link getPrecedes() {
      return precedes;
    }

    public Link getSelf() {
      return self;
    }

    public Link getSucceeds() {
      return succeeds;
    }
  }
}
