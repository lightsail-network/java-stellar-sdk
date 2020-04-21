package org.stellar.sdk.responses;

import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;

import org.stellar.sdk.Memo;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents transaction response.
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/transaction.html" target="_blank">Transaction documentation</a>
 * @see org.stellar.sdk.requests.TransactionsRequestBuilder
 * @see org.stellar.sdk.Server#transactions()
 */
public class TransactionResponse extends Response implements Pageable {
  @SerializedName("hash")
  private final String hash;
  @SerializedName("ledger")
  private final Long ledger;
  @SerializedName("created_at")
  private final String createdAt;
  @SerializedName("source_account")
  private final String sourceAccount;
  @SerializedName("fee_account")
  private final String feeAccount;
  @SerializedName("successful")
  private final Boolean successful;
  @SerializedName("paging_token")
  private final String pagingToken;
  @SerializedName("source_account_sequence")
  private final Long sourceAccountSequence;
  @SerializedName("max_fee")
  private final Long maxFee;
  @SerializedName("fee_charged")
  private final Long feeCharged;
  @SerializedName("operation_count")
  private final Integer operationCount;
  @SerializedName("envelope_xdr")
  private final String envelopeXdr;
  @SerializedName("result_xdr")
  private final String resultXdr;
  @SerializedName("result_meta_xdr")
  private final String resultMetaXdr;
  @SerializedName("signatures")
  private final List<String> signatures;
  @SerializedName("fee_bump_transaction")
  private final FeeBumpTransaction feeBumpTransaction;
  @SerializedName("inner_transaction")
  private final InnerTransaction innerTransaction;
  @SerializedName("_links")
  private final Links links;

  // GSON won't serialize `transient` variables automatically. We need this behaviour
  // because Memo is an abstract class and GSON tries to instantiate it.
  private transient Memo memo;

  TransactionResponse(
      String hash,
      Long ledger,
      String createdAt,
      String sourceAccount,
      String feeAccount,
      Boolean successful,
      String pagingToken,
      Long sourceAccountSequence,
      Long maxFee,
      Long feeCharged,
      Integer operationCount,
      String envelopeXdr,
      String resultXdr,
      String resultMetaXdr,
      Memo memo,
      List<String> signatures,
      FeeBumpTransaction feeBumpTransaction,
      InnerTransaction innerTransaction,
      Links links
  ) {
    this.hash = hash;
    this.ledger = ledger;
    this.createdAt = createdAt;
    this.sourceAccount = sourceAccount;
    this.feeAccount = feeAccount;
    this.successful = successful;
    this.pagingToken = pagingToken;
    this.sourceAccountSequence = sourceAccountSequence;
    this.maxFee = maxFee;
    this.feeCharged = feeCharged;
    this.operationCount = operationCount;
    this.envelopeXdr = envelopeXdr;
    this.resultXdr = resultXdr;
    this.resultMetaXdr = resultMetaXdr;
    this.memo = memo;
    this.signatures = signatures;
    this.feeBumpTransaction = feeBumpTransaction;
    this.innerTransaction = innerTransaction;
    this.links = links;
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
   * FeeBumpTransaction is only present in a TransactionResponse if the transaction is a fee bump transaction or is
   * wrapped by a fee bump transaction. The object has two fields: the hash of the fee bump transaction and the
   * signatures present in the fee bump transaction envelope.
   */
  public static class FeeBumpTransaction {
    @SerializedName("hash")
    private final String hash;
    @SerializedName("signatures")
    private final List<String> signatures;

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
   * InnerTransaction is only present in a TransactionResponse if the transaction is a fee bump transaction or is
   * wrapped by a fee bump transaction. The object has three fields: the hash of the inner transaction wrapped by the
   * fee bump transaction, the max fee set in the inner transaction, and the signatures present in the inner
   * transaction envelope.
   */
  public static class InnerTransaction {
    @SerializedName("hash")
    private final String hash;
    @SerializedName("signatures")
    private final List<String> signatures;
    @SerializedName("max_fee")
    private final Long maxFee;


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

  /**
   * Links connected to transaction.
   */
  public static class Links {
    @SerializedName("account")
    private final Link account;
    @SerializedName("effects")
    private final Link effects;
    @SerializedName("ledger")
    private final Link ledger;
    @SerializedName("operations")
    private final Link operations;
    @SerializedName("precedes")
    private final Link precedes;
    @SerializedName("self")
    private final Link self;
    @SerializedName("succeeds")
    private final Link succeeds;

    Links(Link account, Link effects, Link ledger, Link operations, Link self, Link precedes, Link succeeds) {
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
