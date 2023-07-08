package org.stellar.sdk.responses;

import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.Asset;

/**
 * Represents offer response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/offers/" target="_blank">Offer
 *     documentation</a>
 * @see org.stellar.sdk.requests.OffersRequestBuilder
 * @see org.stellar.sdk.Server#offers()
 */
public class OfferResponse extends Response implements Pageable {
  @SerializedName("id")
  private final Long id;

  @SerializedName("paging_token")
  private final String pagingToken;

  @SerializedName("seller")
  private final String seller;

  @SerializedName("selling")
  private final Asset selling;

  @SerializedName("buying")
  private final Asset buying;

  @SerializedName("amount")
  private final String amount;

  @SerializedName("price")
  private final String price;

  @SerializedName("last_modified_ledger")
  private final Integer lastModifiedLedger;

  @SerializedName("last_modified_time")
  private final String lastModifiedTime;

  @SerializedName("_links")
  private final Links links;

  @SerializedName("sponsor")
  private String sponsor;

  public OfferResponse(
      Long id,
      String pagingToken,
      String seller,
      Asset selling,
      Asset buying,
      String amount,
      String price,
      Integer lastModifiedLedger,
      String lastModifiedTime,
      String sponsor,
      Links links) {
    this.id = id;
    this.pagingToken = pagingToken;
    this.seller = seller;
    this.selling = selling;
    this.buying = buying;
    this.amount = amount;
    this.price = price;
    this.lastModifiedLedger = lastModifiedLedger;
    this.lastModifiedTime = lastModifiedTime;
    this.sponsor = sponsor;
    this.links = links;
  }

  public Long getId() {
    return id;
  }

  public String getPagingToken() {
    return pagingToken;
  }

  public String getSeller() {
    return seller;
  }

  public Asset getSelling() {
    return selling;
  }

  public Asset getBuying() {
    return buying;
  }

  public String getAmount() {
    return amount;
  }

  public String getPrice() {
    return price;
  }

  public Integer getLastModifiedLedger() {
    return lastModifiedLedger;
  }

  // Can be null if ledger adding an offer has not been ingested yet.
  public String getLastModifiedTime() {
    return lastModifiedTime;
  }

  public Optional<String> getSponsor() {
    return Optional.fromNullable(this.sponsor);
  }

  public Links getLinks() {
    return links;
  }

  /** Links connected to ledger. */
  public static class Links {
    @SerializedName("self")
    private final Link self;

    @SerializedName("offer_maker")
    private final Link offerMaker;

    public Links(Link self, Link offerMaker) {
      this.self = self;
      this.offerMaker = offerMaker;
    }

    public Link getSelf() {
      return self;
    }

    public Link getOfferMaker() {
      return offerMaker;
    }
  }
}
