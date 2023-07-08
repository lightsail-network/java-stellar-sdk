package org.stellar.sdk.responses.operations;

import static org.stellar.sdk.Asset.create;

import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.responses.MuxedAccount;

/**
 * @deprecated As of release 0.24.0, replaced by {@link SetTrustLineFlagsOperationResponse}
 *     <p>Represents AllowTrust operation response.
 * @see <a href="https://developers.stellar.org/api/resources/operations/" target="_blank">Operation
 *     documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class AllowTrustOperationResponse extends OperationResponse {
  @SerializedName("trustor")
  private String trustor;

  @SerializedName("trustee")
  private String trustee;

  @SerializedName("trustee_muxed")
  private String trusteeMuxed;

  @SerializedName("trustee_muxed_id")
  private BigInteger trusteeMuxedId;

  @SerializedName("asset_type")
  private String assetType;

  @SerializedName("asset_code")
  private String assetCode;

  @SerializedName("asset_issuer")
  private String assetIssuer;

  @SerializedName("authorize")
  private boolean authorize;

  @SerializedName("authorize_to_maintain_liabilities")
  private boolean authorizeToMaintainLiabilities;

  public String getTrustor() {
    return trustor;
  }

  public Optional<MuxedAccount> getTrusteeMuxed() {
    if (this.trusteeMuxed == null || this.trusteeMuxed.isEmpty()) {
      return Optional.absent();
    }
    return Optional.of(new MuxedAccount(this.trusteeMuxed, this.trustee, this.trusteeMuxedId));
  }

  public String getTrustee() {
    return trustee;
  }

  public boolean isAuthorize() {
    return authorize;
  }

  public boolean isAuthorizedToMaintainLiabilities() {
    return authorizeToMaintainLiabilities;
  }

  public Asset getAsset() {
    if (assetType.equals("native")) {
      return new AssetTypeNative();
    } else {
      return create(assetType, assetCode, assetIssuer);
    }
  }
}
