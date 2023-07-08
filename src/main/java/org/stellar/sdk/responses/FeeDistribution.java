package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;

public class FeeDistribution {
  @SerializedName("min")
  private final Long min;

  @SerializedName("max")
  private final Long max;

  @SerializedName("mode")
  private final Long mode;

  @SerializedName("p10")
  private final Long p10;

  @SerializedName("p20")
  private final Long p20;

  @SerializedName("p30")
  private final Long p30;

  @SerializedName("p40")
  private final Long p40;

  @SerializedName("p50")
  private final Long p50;

  @SerializedName("p60")
  private final Long p60;

  @SerializedName("p70")
  private final Long p70;

  @SerializedName("p80")
  private final Long p80;

  @SerializedName("p90")
  private final Long p90;

  @SerializedName("p95")
  private final Long p95;

  @SerializedName("p99")
  private final Long p99;

  public FeeDistribution(
      Long min,
      Long max,
      Long mode,
      Long p10,
      Long p20,
      Long p30,
      Long p40,
      Long p50,
      Long p60,
      Long p70,
      Long p80,
      Long p90,
      Long p95,
      Long p99) {
    this.min = min;
    this.max = max;
    this.mode = mode;
    this.p10 = p10;
    this.p20 = p20;
    this.p30 = p30;
    this.p40 = p40;
    this.p50 = p50;
    this.p60 = p60;
    this.p70 = p70;
    this.p80 = p80;
    this.p90 = p90;
    this.p95 = p95;
    this.p99 = p99;
  }

  public Long getMin() {
    return min;
  }

  public Long getMax() {
    return max;
  }

  public Long getMode() {
    return mode;
  }

  public Long getP10() {
    return p10;
  }

  public Long getP20() {
    return p20;
  }

  public Long getP30() {
    return p30;
  }

  public Long getP40() {
    return p40;
  }

  public Long getP50() {
    return p50;
  }

  public Long getP60() {
    return p60;
  }

  public Long getP70() {
    return p70;
  }

  public Long getP80() {
    return p80;
  }

  public Long getP90() {
    return p90;
  }

  public Long getP95() {
    return p95;
  }

  public Long getP99() {
    return p99;
  }
}
