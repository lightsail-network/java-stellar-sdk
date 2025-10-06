package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;

/**
 * Represents order book response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/apis/horizon/api-reference/aggregations/order-books"
 *     target="_blank">Order book documentation</a>
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
  List<Row> asks;

  @SerializedName("bids")
  List<Row> bids;

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
