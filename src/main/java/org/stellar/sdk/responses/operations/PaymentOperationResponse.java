package org.stellar.sdk.responses.operations;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.responses.MuxedAccount;

/**
 * Represents Payment operation response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/horizon/api-reference/resources/operations/object/payment"
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
    if (assetType.equals("native")) {
      return new AssetTypeNative();
    } else {
      return create(assetType, assetCode, assetIssuer);
    }
  }

  public Optional<MuxedAccount> getFromMuxed() {
    if (this.fromMuxed == null || this.fromMuxed.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new MuxedAccount(this.fromMuxed, this.from, this.fromMuxedId));
  }

  public Optional<MuxedAccount> getToMuxed() {
    if (this.toMuxed == null || this.toMuxed.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new MuxedAccount(this.toMuxed, this.to, this.toMuxedId));
  }
}
