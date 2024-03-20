package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;

/**
 * Represents offer response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/offers/" target="_blank">Offer
 *     documentation</a>
 * @see org.stellar.sdk.requests.OffersRequestBuilder
 * @see org.stellar.sdk.Server#offers()
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class OfferResponse extends Response implements Pageable {
  @SerializedName("id")
  Long id;

  @SerializedName("paging_token")
  String pagingToken;

  @SerializedName("seller")
  String seller;

  @SerializedName("selling")
  Asset selling;

  @SerializedName("buying")
  Asset buying;

  @SerializedName("amount")
  String amount;

  @SerializedName("price_r")
  TradePrice priceR;

  @SerializedName("price")
  String price;

  /** Can be null if ledger adding an offer has not been ingested yet. * */
  @SerializedName("last_modified_ledger")
  Integer lastModifiedLedger;

  @SerializedName("last_modified_time")
  String lastModifiedTime;

  @SerializedName("sponsor")
  String sponsor;

  @SerializedName("_links")
  Links links;

  public Optional<String> getSponsor() {
    // For backwards compatibility
    return Optional.ofNullable(this.sponsor);
  }

  /** Links connected to ledger. */
  @Value
  public static class Links {
    @SerializedName("self")
    Link self;

    @SerializedName("offer_maker")
    Link offerMaker;
  }
}
