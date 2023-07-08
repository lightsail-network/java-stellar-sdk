package org.stellar.sdk.responses.operations;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;

/**
 * Represents Payment operation response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/operations/" target="_blank">Operation
 *     documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class PaymentOperationResponse extends OperationResponse {
  @SerializedName("amount")
  protected final String amount;

  @SerializedName("asset_type")
  protected final String assetType;

  @SerializedName("asset_code")
  protected final String assetCode;

  @SerializedName("asset_issuer")
  protected final String assetIssuer;

  @SerializedName("from")
  protected final String from;

  @SerializedName("to")
  protected final String to;

  PaymentOperationResponse(
      String amount,
      String assetType,
      String assetCode,
      String assetIssuer,
      String from,
      String to) {
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
      return create(assetType, assetCode, assetIssuer);
    }
  }

  public String getFrom() {
    return from;
  }

  public String getTo() {
    return to;
  }
}
