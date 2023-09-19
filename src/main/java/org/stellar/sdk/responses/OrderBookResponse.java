package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import lombok.NonNull;
import org.stellar.sdk.Asset;
import org.stellar.sdk.Price;

/**
 * Represents order book response.
 *
 * @see <a href="https://developers.stellar.org/api/aggregations/order-books/" target="_blank">Order
 *     book documentation</a>
 * @see org.stellar.sdk.requests.OrderBookRequestBuilder
 * @see org.stellar.sdk.Server#orderBook()
 */
public class OrderBookResponse extends Response {
  @SerializedName("base")
  private final Asset base;

  @SerializedName("counter")
  private final Asset counter;

  @SerializedName("asks")
  private final Row[] asks;

  @SerializedName("bids")
  private final Row[] bids;

  public OrderBookResponse(Asset base, Asset counter, Row[] asks, Row[] bids) {
    this.base = base;
    this.counter = counter;
    this.asks = asks;
    this.bids = bids;
  }

  public Asset getBase() {
    return base;
  }

  public Asset getCounter() {
    return counter;
  }

  public Row[] getAsks() {
    return asks;
  }

  public Row[] getBids() {
    return bids;
  }

  /** Represents order book row. */
  public static class Row {
    @SerializedName("amount")
    private final String amount;

    @SerializedName("price")
    private final String price;

    @SerializedName("price_r")
    private final Price priceR;

    Row(@NonNull String amount, @NonNull String price, @NonNull Price priceR) {
      this.amount = amount;
      this.price = price;
      this.priceR = priceR;
    }

    public String getAmount() {
      return amount;
    }

    public String getPrice() {
      return price;
    }

    public Price getPriceR() {
      return priceR;
    }
  }
}
