package org.stellar.sdk;

import com.google.gson.annotations.SerializedName;

import org.stellar.base.Keypair;

/**
 * Represents transaction response.
 */
public class Transaction {
  @SerializedName("hash")
  private final String hash;
  @SerializedName("ledger")
  private final Long ledger;
  @SerializedName("created_at")
  private final String createdAt;
  @SerializedName("source_account")
  private final Keypair sourceAccount;
  @SerializedName("source_account_sequence")
  private final Long sourceAccountSequence;
  @SerializedName("fee_paid")
  private final Long feePaid;
  @SerializedName("operation_count")
  private final Integer operationCount;
  @SerializedName("envelope_xdr")
  private final String envelopeXdr;
  @SerializedName("result_xdr")
  private final String resultXdr;
  @SerializedName("result_meta_xdr")
  private final String resultMetaXdr;
  // TODO memo!
  @SerializedName("_links")
  private final Links links;

  Transaction(String hash, Long ledger, String createdAt, Keypair sourceAccount, Long sourceAccountSequence, Long feePaid, Integer operationCount, String envelopeXdr, String resultXdr, String resultMetaXdr, Links links) {
    this.hash = hash;
    this.ledger = ledger;
    this.createdAt = createdAt;
    this.sourceAccount = sourceAccount;
    this.sourceAccountSequence = sourceAccountSequence;
    this.feePaid = feePaid;
    this.operationCount = operationCount;
    this.envelopeXdr = envelopeXdr;
    this.resultXdr = resultXdr;
    this.resultMetaXdr = resultMetaXdr;
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

  public Keypair getSourceAccount() {
    return sourceAccount;
  }

  public Long getSourceAccountSequence() {
    return sourceAccountSequence;
  }

  public Long getFeePaid() {
    return feePaid;
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

  public Links getLinks() {
    return links;
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
