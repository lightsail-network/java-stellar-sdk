package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Represents ledger response.
 *
 * @see <a href="https://developers.stellar.org/docs/glossary/ledger/" target="_blank">Ledger
 *     documentation</a>
 * @see org.stellar.sdk.requests.LedgersRequestBuilder
 * @see org.stellar.sdk.Server#ledgers()
 */
public class LedgerResponse extends Response implements Pageable {
  @SerializedName("sequence")
  private final Long sequence;

  @SerializedName("hash")
  private final String hash;

  @SerializedName("paging_token")
  private final String pagingToken;

  @SerializedName("prev_hash")
  private final String prevHash;

  @SerializedName("transaction_count")
  private final Integer transactionCount;

  @SerializedName("successful_transaction_count")
  private final Integer successfulTransactionCount;

  @SerializedName("failed_transaction_count")
  private final Integer failedTransactionCount;

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

  @SerializedName("base_fee_in_stroops")
  private final String baseFeeInStroops;

  @SerializedName("base_reserve_in_stroops")
  private final String baseReserveInStroops;

  @SerializedName("max_tx_set_size")
  private final Integer maxTxSetSize;

  @SerializedName("protocol_version")
  private final Integer protocolVersion;

  @SerializedName("header_xdr")
  private final String headerXdr;

  @SerializedName("_links")
  private final Links links;

  LedgerResponse(
      Long sequence,
      String hash,
      String pagingToken,
      String prevHash,
      Integer transactionCount,
      Integer successfulTransactionCount,
      Integer failedTransactionCount,
      Integer operationCount,
      String closedAt,
      String totalCoins,
      String feePool,
      Long baseFee,
      String baseReserve,
      String baseFeeInStroops,
      String baseReserveInStroops,
      Integer maxTxSetSize,
      Integer protocolVersion,
      String headerXdr,
      Links links) {
    this.sequence = sequence;
    this.hash = hash;
    this.pagingToken = pagingToken;
    this.prevHash = prevHash;
    this.transactionCount = transactionCount;
    this.successfulTransactionCount = successfulTransactionCount;
    this.failedTransactionCount = failedTransactionCount;
    this.operationCount = operationCount;
    this.closedAt = closedAt;
    this.totalCoins = totalCoins;
    this.feePool = feePool;
    this.baseFee = baseFee;
    this.baseFeeInStroops = baseFeeInStroops;
    this.baseReserve = baseReserve;
    this.baseReserveInStroops = baseReserveInStroops;
    this.maxTxSetSize = maxTxSetSize;
    this.protocolVersion = protocolVersion;
    this.headerXdr = headerXdr;
    this.links = links;
  }

  public Long getSequence() {
    return sequence;
  }

  public String getHash() {
    return hash;
  }

  public String getPagingToken() {
    return pagingToken;
  }

  public String getPrevHash() {
    return prevHash;
  }

  /**
   * @deprecated Will be removed in Horizon 0.17.0
   */
  public Integer getTransactionCount() {
    return transactionCount;
  }

  public Integer getSuccessfulTransactionCount() {
    return successfulTransactionCount;
  }

  public Integer getFailedTransactionCount() {
    return failedTransactionCount;
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

  public String getBaseFeeInStroops() {
    return baseFeeInStroops;
  }

  public String getBaseReserveInStroops() {
    return baseReserveInStroops;
  }

  public Integer getMaxTxSetSize() {
    return maxTxSetSize;
  }

  public Integer getProtocolVersion() {
    return protocolVersion;
  }

  public String getHeaderXdr() {
    return headerXdr;
  }

  public Links getLinks() {
    return links;
  }

  /** Links connected to ledger. */
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
