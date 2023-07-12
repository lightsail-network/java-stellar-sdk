package org.stellar.sdk.responses.operations;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.stellar.sdk.Asset;

/**
 * Represents a Set Trustine Flags operation response.
 *
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class SetTrustLineFlagsOperationResponse extends OperationResponse {
  @SerializedName("asset_type")
  protected final String assetType;

  @SerializedName("asset_code")
  protected final String assetCode;

  @SerializedName("asset_issuer")
  protected final String assetIssuer;

  @SerializedName("clear_flags")
  protected final List<Integer> clearFlags;

  @SerializedName("clear_flags_s")
  protected final List<String> clearFlagStings;

  @SerializedName("set_flags")
  protected final List<Integer> setFlags;

  @SerializedName("set_flags_s")
  protected final List<String> setFlagStrings;

  @SerializedName("trustor")
  private final String trustor;

  public SetTrustLineFlagsOperationResponse(
      String assetType,
      String assetCode,
      String assetIssuer,
      List<Integer> clearFlags,
      List<String> clearFlagStings,
      List<Integer> setFlags,
      List<String> setFlagStrings,
      String trustor) {
    this.assetType = assetType;
    this.assetCode = assetCode;
    this.assetIssuer = assetIssuer;
    this.clearFlags = clearFlags;
    this.clearFlagStings = clearFlagStings;
    this.setFlags = setFlags;
    this.setFlagStrings = setFlagStrings;
    this.trustor = trustor;
  }

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
    return create(assetType, assetCode, assetIssuer);
  }

  public String getTrustor() {
    return trustor;
  }

  public List<Integer> getClearFlags() {
    return clearFlags;
  }

  public List<String> getClearFlagStrings() {
    return clearFlagStings;
  }

  public List<Integer> getSetFlags() {
    return setFlags;
  }

  public List<String> getSetFlagStrings() {
    return setFlagStrings;
  }
}
