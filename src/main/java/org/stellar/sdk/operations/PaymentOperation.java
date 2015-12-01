package org.stellar.sdk.operations;

import com.google.gson.annotations.SerializedName;

import org.stellar.base.Asset;
import org.stellar.base.AssetTypeNative;
import org.stellar.base.Keypair;

/**
 * Represents Payment operation response.
 */
public class PaymentOperation extends Operation {
  @SerializedName("amount")
  protected final String amount;
  @SerializedName("asset_type")
  protected final String assetType;
  @SerializedName("asset_code")
  protected final String assetCode;
  @SerializedName("asset_issuer")
  protected final String assetIssuer;
  @SerializedName("from")
  protected final Keypair from;
  @SerializedName("to")
  protected final Keypair to;

  PaymentOperation(String amount, String assetType, String assetCode, String assetIssuer, Keypair from, Keypair to) {
    this.amount = amount;
    this.assetType = assetType;
    this.assetCode = assetCode;
    this.assetIssuer = assetIssuer;
    this.from = from;
    this.to = to;
  }

  public String getAmount() {
    return amount;
  }

  public Asset getAsset() {
    if (assetType.equals("native")) {
      return new AssetTypeNative();
    } else {
      Keypair issuer = Keypair.fromAddress(assetIssuer);
      return Asset.createNonNativeAsset(assetCode, issuer);
    }
  }

  public Keypair getFrom() {
    return from;
  }

  public Keypair getTo() {
    return to;
  }
}
