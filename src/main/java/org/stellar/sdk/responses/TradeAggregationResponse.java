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

  @SerializedName("low")
  String low;

  @SerializedName("open")
  String open;

  @SerializedName("close")
  String close;

  public Date getDate() {
    return new Date(this.timestamp);
  }
}
