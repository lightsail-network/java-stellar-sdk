package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.LedgerHeader;

/**
 * Represents ledger response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/fundamentals-and-concepts/stellar-data-structures/ledgers"
 *     target="_blank">Ledger documentation</a>
 * @see org.stellar.sdk.requests.LedgersRequestBuilder
 * @see org.stellar.sdk.Server#ledgers()
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class LedgerResponse extends Response implements Pageable {
  @SerializedName("id")
  String id;

  @SerializedName("paging_token")
  String pagingToken;

  @SerializedName("hash")
  String hash;

  @SerializedName("prev_hash")
  String prevHash;

  @SerializedName("sequence")
  Long sequence;

  @SerializedName("successful_transaction_count")
  Integer successfulTransactionCount;

  @SerializedName("failed_transaction_count")
  Integer failedTransactionCount;

  @SerializedName("operation_count")
  Integer operationCount;

  @SerializedName("tx_set_operation_count")
  Integer txSetOperationCount;

  @SerializedName("closed_at")
  String closedAt;

  @SerializedName("total_coins")
  String totalCoins;

  @SerializedName("fee_pool")
  String feePool;

  @SerializedName("base_fee_in_stroops")
  BigInteger baseFeeInStroops;

  @SerializedName("base_reserve_in_stroops")
  BigInteger baseReserveInStroops;

  @SerializedName("max_tx_set_size")
  Integer maxTxSetSize;

  @SerializedName("protocol_version")
  Integer protocolVersion;

  @SerializedName("header_xdr")
  String headerXdr;

  @SerializedName("_links")
  Links links;

  /** Links connected to ledger. */
  @Value
  public static class Links {
    @SerializedName("self")
    Link self;

    @SerializedName("transactions")
    Link transactions;

    @SerializedName("operations")
    Link operations;

    @SerializedName("payments")
    Link payments;

    @SerializedName("effects")
    Link effects;
  }

  /**
   * Parses the {@code headerXdr} field from a string to an {@link org.stellar.sdk.xdr.LedgerHeader}
   * object.
   *
   * @return the parsed {@link org.stellar.sdk.xdr.LedgerHeader} object
   */
  public LedgerHeader parseHeaderXdr() {
    return Util.parseXdr(headerXdr, LedgerHeader::fromXdrBase64);
  }
}
