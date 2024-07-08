package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;

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
