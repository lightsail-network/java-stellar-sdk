package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.TrustLineAsset;
import org.stellar.sdk.responses.MuxedAccount;

/**
 * Represents ChangeTrust operation response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/operations/" target="_blank">Operation
 *     documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ChangeTrustOperationResponse extends OperationResponse {
  @SerializedName("trustor")
  String trustor;

  @SerializedName("trustor_muxed")
  String trustorMuxed;

  @SerializedName("trustor_muxed_id")
  BigInteger trustorMuxedId;

  @SerializedName("trustee")
  String trustee;

  @SerializedName("asset_type")
  String assetType;

  @SerializedName("asset_code")
  String assetCode;

  @SerializedName("asset_issuer")
  String assetIssuer;

  @SerializedName("limit")
  String limit;

  @SerializedName("liquidity_pool_id")
  String liquidityPoolId;

  public Optional<MuxedAccount> getTrustorMuxed() {
    if (this.trustorMuxed == null || this.trustorMuxed.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new MuxedAccount(this.trustorMuxed, this.trustor, this.trustorMuxedId));
  }

  public TrustLineAsset getTrustLineAsset() {
    return getTrustLineAsset(assetType, assetCode, assetIssuer, liquidityPoolId);
  }
}
