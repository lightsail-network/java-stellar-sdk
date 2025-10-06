package org.stellar.sdk.responses.operations;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;

/**
 * Represents Payment operation response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/apis/horizon/api-reference/resources/operations/object/payment"
 *     target="_blank">Operation documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class PaymentOperationResponse extends OperationResponse {
  @SerializedName("amount")
  String amount;

  @SerializedName("asset_type")
  String assetType;

  @SerializedName("asset_code")
  String assetCode;

  @SerializedName("asset_issuer")
  String assetIssuer;

  @SerializedName("from")
  String from;

  @SerializedName("from_muxed")
  String fromMuxed;

  @SerializedName("from_muxed_id")
  BigInteger fromMuxedId;

  @SerializedName("to")
  String to;

  @SerializedName("to_muxed")
  String toMuxed;

  @SerializedName("to_muxed_id")
  BigInteger toMuxedId;

  public Asset getAsset() {
    return create(assetType, assetCode, assetIssuer);
  }
}
