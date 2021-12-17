package org.stellar.sdk.responses.operations;

import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.Asset;
import org.stellar.sdk.responses.MuxedAccount;

import java.math.BigInteger;

/**
 * Represents a Clawback operation response.
 *
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class ClawbackOperationResponse extends OperationResponse {
  @SerializedName("asset_type")
  private String assetType;
  @SerializedName("asset_code")
  private String assetCode;
  @SerializedName("asset_issuer")
  private String assetIssuer;
  @SerializedName("amount")
  private String amount;
  @SerializedName("from")
  private String from;
  @SerializedName("from_muxed")
  private String fromMuxed;
  @SerializedName("from_muxed_id")
  private BigInteger fromMuxedId;

  public String getAssetType() {
    return assetType;
  }

  public String getAssetIssuer() {
    return assetIssuer;
  }

  public String getAssetCode() {
    return assetCode;
  }

  public Asset getAsset() {
    return Asset.createNonNativeAsset(assetCode, assetIssuer);
  }

  public String getAmount() {
    return amount;
  }

  public String getFrom() {
    return from;
  }

  public Optional<MuxedAccount> getFromMuxed() {
    if (this.fromMuxed == null || this.fromMuxed.isEmpty()) {
      return Optional.absent();
    }
    return Optional.of(new MuxedAccount(this.fromMuxed, this.from, this.fromMuxedId));
  }

}
