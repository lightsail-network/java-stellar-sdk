package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import lombok.Value;
import org.stellar.sdk.Asset;

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
