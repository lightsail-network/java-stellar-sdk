package org.stellar.sdk.responses.operations;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;

/**
 * @deprecated As of release 0.24.0, replaced by {@link SetTrustLineFlagsOperationResponse}
 *     <p>Represents AllowTrust operation response.
 * @see <a href="https://developers.stellar.org/api/resources/operations/" target="_blank">Operation
 *     documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
@Deprecated
public class AllowTrustOperationResponse extends OperationResponse {
  @SerializedName("trustor")
  String trustor;

  @SerializedName("trustee")
  String trustee;

  @SerializedName("trustee_muxed")
  String trusteeMuxed;

  @SerializedName("trustee_muxed_id")
  BigInteger trusteeMuxedId;

  @SerializedName("asset_type")
  String assetType;

  @SerializedName("asset_code")
  String assetCode;

  @SerializedName("asset_issuer")
  String assetIssuer;

  @SerializedName("authorize")
  Boolean authorize;

  @SerializedName("authorize_to_maintain_liabilities")
  Boolean authorizeToMaintainLiabilities;

  public Asset getAsset() {
    return create(assetType, assetCode, assetIssuer);
  }
}
