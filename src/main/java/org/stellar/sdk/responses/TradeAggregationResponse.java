package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents the response from the Horizon {@code /trade_aggregations} endpoint.
 *
 * <p>Contains aggregated trade statistics for an asset pair over a given time period, including
 * trade count, base and counter volumes, average price, and high/low/open/close prices. Prices are
 * available both as decimal strings and as rational {@link Price} values.
 *
 * @see org.stellar.sdk.requests.TradeAggregationsRequestBuilder
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class TradeAggregationResponse extends Response {
  @SerializedName("timestamp")
  Long timestamp;

  @SerializedName("trade_count")
  Integer tradeCount;

  @SerializedName("base_volume")
  String baseVolume;

  @SerializedName("counter_volume")
  String counterVolume;

  @SerializedName("avg")
  String avg;

  @SerializedName("high")
  String high;

  @SerializedName("high_r")
  Price highR;

  @SerializedName("low")
  String low;

  @SerializedName("low_r")
  Price lowR;

  @SerializedName("open")
  String open;

  @SerializedName("open_r")
  Price openR;

  @SerializedName("close")
  String close;

  @SerializedName("close_r")
  Price closeR;
}
