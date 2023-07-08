package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class TradeAggregationResponse extends Response {
  @SerializedName("timestamp")
  private final long timestamp;

  @SerializedName("trade_count")
  private final int tradeCount;

  @SerializedName("base_volume")
  private final String baseVolume;

  @SerializedName("counter_volume")
  private final String counterVolume;

  @SerializedName("avg")
  private final String avg;

  @SerializedName("high")
  private final String high;

  @SerializedName("low")
  private final String low;

  @SerializedName("open")
  private final String open;

  @SerializedName("close")
  private final String close;

  public TradeAggregationResponse(
      long timestamp,
      int tradeCount,
      String baseVolume,
      String counterVolume,
      String avg,
      String high,
      String low,
      String open,
      String close) {
    this.timestamp = timestamp;
    this.tradeCount = tradeCount;
    this.baseVolume = baseVolume;
    this.counterVolume = counterVolume;
    this.avg = avg;
    this.high = high;
    this.low = low;
    this.open = open;
    this.close = close;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public Date getDate() {
    return new Date(Long.valueOf(this.timestamp));
  }

  public int getTradeCount() {
    return tradeCount;
  }

  public String getBaseVolume() {
    return baseVolume;
  }

  public String getCounterVolume() {
    return counterVolume;
  }

  public String getAvg() {
    return avg;
  }

  public String getHigh() {
    return high;
  }

  public String getLow() {
    return low;
  }

  public String getOpen() {
    return open;
  }

  public String getClose() {
    return close;
  }
}
