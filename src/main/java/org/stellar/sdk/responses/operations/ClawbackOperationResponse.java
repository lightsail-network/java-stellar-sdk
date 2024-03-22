package org.stellar.sdk.responses.operations;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;
import org.stellar.sdk.responses.MuxedAccount;

/**
 * Represents a Clawback operation response.
 *
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ClawbackOperationResponse extends OperationResponse {
  @SerializedName("asset_type")
  String assetType;

  @SerializedName("asset_code")
  String assetCode;

  @SerializedName("asset_issuer")
  String assetIssuer;

  @SerializedName("amount")
  String amount;

  @SerializedName("from")
  String from;

  @SerializedName("from_muxed")
  String fromMuxed;

  @SerializedName("from_muxed_id")
  BigInteger fromMuxedId;

  public Asset getAsset() {
    return create(assetType, assetCode, assetIssuer);
  }

  public Optional<MuxedAccount> getFromMuxed() {
    if (this.fromMuxed == null || this.fromMuxed.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new MuxedAccount(this.fromMuxed, this.from, this.fromMuxedId));
  }
}
