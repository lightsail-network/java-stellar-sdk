package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.xdr.LiquidityPoolType;

/**
 * Represents liquidity pool response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/liquiditypools/"
 *     target="_blank">Liquidity Pool documentation</a>
 * @see org.stellar.sdk.requests.LiquidityPoolsRequestBuilder
 * @see org.stellar.sdk.Server#liquidityPools()
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class LiquidityPoolResponse extends Response {
  @SerializedName("id")
  LiquidityPoolID id;

  @SerializedName("paging_token")
  String pagingToken;

  @SerializedName("fee_bp")
  Integer feeBP;

  // TODO: String?
  @SerializedName("type")
  LiquidityPoolType type;

  @SerializedName("total_trustlines")
  Long totalTrustlines;

  @SerializedName("total_shares")
  String totalShares;

  @SerializedName("reserves")
  List<Reserve> reserves;

  @SerializedName("last_modified_ledger")
  Long lastModifiedLedger;

  @SerializedName("last_modified_time")
  String lastModifiedTime;

  @SerializedName("_links")
  Links links;

  /** Represents liquidity pool reserves. */
  @Value
  public static class Reserve {
    @SerializedName("amount")
    String amount;

    @SerializedName("asset")
    Asset asset;
  }

  /** Links connected to account. */
  @Value
  public static class Links {
    @SerializedName("self")
    Link self;

    @SerializedName("operations")
    Link operations;

    @SerializedName("transactions")
    Link transactions;
  }
}
