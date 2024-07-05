package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;
import org.stellar.sdk.Claimant;

/**
 * Represents a claimable balance response.
 *
 * @see org.stellar.sdk.requests.ClaimableBalancesRequestBuilder
 * @see org.stellar.sdk.Server#claimableBalances()
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class ClaimableBalanceResponse extends Response implements Pageable {
  @SerializedName("id")
  String id;

  @SerializedName("asset")
  String assetString;

  @SerializedName("amount")
  String amount;

  @SerializedName("sponsor")
  String sponsor;

  @SerializedName("last_modified_ledger")
  Integer lastModifiedLedger;

  @SerializedName("last_modified_time")
  String lastModifiedTime;

  @SerializedName("claimants")
  List<Claimant> claimants;

  @SerializedName("flags")
  Flags flags;

  @SerializedName("paging_token")
  String pagingToken;

  @SerializedName("_links")
  Links links;

  public Asset getAsset() {
    return Asset.create(assetString);
  }

  @Value
  public static class Flags {
    @SerializedName("clawback_enabled")
    Boolean clawbackEnabled;
  }

  /** Links connected to claimable balance. */
  @Value
  public static class Links {
    @SerializedName("self")
    Link self;

    @SerializedName("transactions")
    Link transactions;

    @SerializedName("operations")
    Link operations;
  }
}
