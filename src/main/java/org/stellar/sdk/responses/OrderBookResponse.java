package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;
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
@Value
@EqualsAndHashCode(callSuper = false)
public class OrderBookResponse extends Response {
  @SerializedName("base")
  Asset base;

  @SerializedName("counter")
  Asset counter;

  @SerializedName("asks")
  Row[] asks;

  @SerializedName("bids")
  Row[] bids;

  /** Represents order book row. */
  @Value
  public static class Row {
    @SerializedName("amount")
    String amount;

    @SerializedName("price")
    String price;

    @SerializedName("price_r")
    Price priceR;
  }
}
