package org.stellar.sdk;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

// TODO: move to resp?
/** Represents an amount of asset. */
@Value
public class AssetAmount {
  /*
   * The asset.
   */
  @SerializedName("asset")
  Asset asset;

  /*
   * The amount.
   */
  @SerializedName("amount")
  String amount;
}
