package org.stellar.sdk.responses.operations;

    import com.google.gson.annotations.SerializedName;

    import org.stellar.sdk.Asset;
    import org.stellar.sdk.AssetTypeNative;

    import java.util.List;

/**
 * @deprecated Will be removed in version 0.11.0, use {@link PathPaymentStrictReceiveOperationResponse}
 */
public class PathPaymentOperationResponse extends PathPaymentStrictReceiveOperationResponse {

  public PathPaymentOperationResponse(String amount, String sourceAmount, String sourceMax, String from, String to, String assetType, String assetCode, String assetIssuer, String sourceAssetType, String sourceAssetCode, String sourceAssetIssuer, List<Asset> path) {
    super(amount, sourceAmount, sourceMax, from, to, assetType, assetCode, assetIssuer, sourceAssetType, sourceAssetCode, sourceAssetIssuer, path);
  }
}
