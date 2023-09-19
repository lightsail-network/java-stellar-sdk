package org.stellar.sdk.responses;

import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.stellar.sdk.Asset;
import org.stellar.sdk.Claimant;

/**
 * Represents a claimable balance response.
 *
 * @see org.stellar.sdk.requests.ClaimableBalancesRequestBuilder
 * @see org.stellar.sdk.Server#claimableBalances()
 */
public class ClaimableBalanceResponse extends Response implements Pageable {

  @SerializedName("id")
  private final String id;

  @SerializedName("asset")
  private final String assetString;

  @SerializedName("amount")
  private final String amount;

  @SerializedName("sponsor")
  private final String sponsor;

  @SerializedName("last_modified_ledger")
  private final Integer lastModifiedLedger;

  @SerializedName("last_modified_time")
  private final String lastModifiedTime;

  @SerializedName("paging_token")
  private final String pagingToken;

  @SerializedName("_links")
  private final Links links;

  @SerializedName("claimants")
  private final List<Claimant> claimants;

  public ClaimableBalanceResponse(
      String id,
      String assetString,
      String amount,
      String sponsor,
      Integer lastModifiedLedger,
      String lastModifiedTime,
      String pagingToken,
      List<Claimant> claimants,
      Links links) {
    this.id = id;
    this.assetString = assetString;
    this.amount = amount;
    this.sponsor = sponsor;
    this.lastModifiedLedger = lastModifiedLedger;
    this.lastModifiedTime = lastModifiedTime;
    this.pagingToken = pagingToken;
    this.claimants = claimants;
    this.links = links;
  }

  public String getId() {
    return id;
  }

  public String getAssetString() {
    return assetString;
  }

  public Asset getAsset() {
    return Asset.create(assetString);
  }

  public String getAmount() {
    return amount;
  }

  public Integer getLastModifiedLedger() {
    return lastModifiedLedger;
  }

  public String getLastModifiedTime() {
    return lastModifiedTime;
  }

  public Optional<String> getSponsor() {
    return Optional.fromNullable(this.sponsor);
  }

  public String getPagingToken() {
    return pagingToken;
  }

  public Links getLinks() {
    return links;
  }

  public List<Claimant> getClaimants() {
    return claimants;
  }

  /** Links connected to claimable balance. */
  public static class Links {
    @SerializedName("self")
    private final Link self;

    public Links(Link self) {
      this.self = self;
    }

    public Link getSelf() {
      return self;
    }
  }
}
