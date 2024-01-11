package org.stellar.sdk.responses.operations;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;

/**
 * Represents a Set Trustine Flags operation response.
 *
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class SetTrustLineFlagsOperationResponse extends OperationResponse {
  @SerializedName("asset_type")
  String assetType;

  @SerializedName("asset_code")
  String assetCode;

  @SerializedName("asset_issuer")
  String assetIssuer;

  @SerializedName("clear_flags")
  List<Integer> clearFlags;

  @SerializedName("clear_flags_s")
  List<String> clearFlagStrings;

  @SerializedName("set_flags")
  List<Integer> setFlags;

  @SerializedName("set_flags_s")
  List<String> setFlagStrings;

  @SerializedName("trustor")
  String trustor;

  public Asset getAsset() {
    return create(assetType, assetCode, assetIssuer);
  }
}
