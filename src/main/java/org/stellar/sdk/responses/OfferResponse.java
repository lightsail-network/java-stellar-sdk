package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;

import org.stellar.sdk.Asset;
import org.stellar.sdk.KeyPair;

/**
 * Represents offer response.
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/offer.html" target="_blank">Offer documentation</a>
 * @see org.stellar.sdk.requests.OffersRequestBuilder
 * @see org.stellar.sdk.Server#offers()
 */
public class OfferResponse extends Response {
  @SerializedName("id")
  private final Long id;
  @SerializedName("paging_token")
  private final String pagingToken;
  @SerializedName("seller")
  private final KeyPair seller;
  @SerializedName("selling")
  private final Asset selling;
  @SerializedName("buying")
  private final Asset buying;
  @SerializedName("amount")
  private final String amount;
  @SerializedName("price")
  private final String price;
  @SerializedName("_links")
  private final Links links;

  OfferResponse(Long id, String pagingToken, KeyPair seller, Asset selling, Asset buying, String amount, String price, Links links) {
    this.id = id;
    this.pagingToken = pagingToken;
    this.seller = seller;
    this.selling = selling;
    this.buying = buying;
    this.amount = amount;
    this.price = price;
    this.links = links;
  }

  public Long getId() {
    return id;
  }

  public String getPagingToken() {
    return pagingToken;
  }

  public KeyPair getSeller() {
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

  public Links getLinks() {
    return links;
  }

  /**
   * Links connected to ledger.
   */
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
