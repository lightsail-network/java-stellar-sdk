package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class TradeAggregationResponse extends Response {
  @SerializedName("timestamp")
  long timestamp;

  @SerializedName("trade_count")
  int tradeCount;

  @SerializedName("base_volume")
  String baseVolume;

  @SerializedName("counter_volume")
  String counterVolume;

  @SerializedName("avg")
  String avg;

  @SerializedName("high")
  String high;

  @SerializedName("high_r")
  TradePrice highR;

  @SerializedName("low")
  String low;

  @SerializedName("low_r")
  TradePrice lowR;

  @SerializedName("open")
  String open;

  @SerializedName("open_r")
  TradePrice openR;

  @SerializedName("close")
  String close;

  @SerializedName("close_r")
  TradePrice closeR;

  public Date getDate() {
    return new Date(this.timestamp);
  }
}
