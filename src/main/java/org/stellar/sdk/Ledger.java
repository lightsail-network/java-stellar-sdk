package org.stellar.sdk;

import com.google.gson.annotations.SerializedName;

/**
 * Represents ledger response.
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/ledger.html" target="_blank">Ledger documentation</a>
 * @see org.stellar.sdk.requests.LedgersRequestBuilder
 * @see org.stellar.sdk.Server#ledgers()
 */
public class Ledger {
  @SerializedName("sequence")
  private final Long sequence;
  @SerializedName("hash")
  private final String hash;
  @SerializedName("prev_hash")
  private final String prevHash;
  @SerializedName("transaction_count")
  private final Integer transactionCount;
  @SerializedName("operation_count")
  private final Integer operationCount;
  @SerializedName("closed_at")
  private final String closedAt;
  @SerializedName("total_coins")
  private final String totalCoins;
  @SerializedName("fee_pool")
  private final String feePool;
  @SerializedName("base_fee")
  private final Long baseFee;
  @SerializedName("base_reserve")
  private final String baseReserve;
  @SerializedName("max_tx_set_size")
  private final Integer maxTxSetSize;
  @SerializedName("_links")
  private final Links links;

  Ledger(Long sequence, String hash, String prevHash, Integer transactionCount, Integer operationCount, String closedAt, String totalCoins, String feePool, Long baseFee, String baseReserve, Integer maxTxSetSize, Links links) {
    this.sequence = sequence;
    this.hash = hash;
    this.prevHash = prevHash;
    this.transactionCount = transactionCount;
    this.operationCount = operationCount;
    this.closedAt = closedAt;
    this.totalCoins = totalCoins;
    this.feePool = feePool;
    this.baseFee = baseFee;
    this.baseReserve = baseReserve;
    this.maxTxSetSize = maxTxSetSize;
    this.links = links;
  }

  public Long getSequence() {
    return sequence;
  }

  public String getHash() {
    return hash;
  }

  public String getPrevHash() {
    return prevHash;
  }

  public Integer getTransactionCount() {
    return transactionCount;
  }

  public Integer getOperationCount() {
    return operationCount;
  }

  public String getClosedAt() {
    return closedAt;
  }

  public String getTotalCoins() {
    return totalCoins;
  }

  public String getFeePool() {
    return feePool;
  }

  public Long getBaseFee() {
    return baseFee;
  }

  public String getBaseReserve() {
    return baseReserve;
  }

  public Integer getMaxTxSetSize() {
    return maxTxSetSize;
  }

  public Links getLinks() {
    return links;
  }

  /**
   * Links connected to ledger.
   */
  public static class Links {
    @SerializedName("effects")
    private final Link effects;
    @SerializedName("operations")
    private final Link operations;
    @SerializedName("self")
    private final Link self;
    @SerializedName("transactions")
    private final Link transactions;

    Links(Link effects, Link operations, Link self, Link transactions) {
      this.effects = effects;
      this.operations = operations;
      this.self = self;
      this.transactions = transactions;
    }

    public Link getEffects() {
      return effects;
    }

    public Link getOperations() {
      return operations;
    }

    public Link getSelf() {
      return self;
    }

    public Link getTransactions() {
      return transactions;
    }
  }
}
