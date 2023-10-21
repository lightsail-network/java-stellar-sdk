package org.stellar.sdk.responses.operations;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.Optional;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.responses.MuxedAccount;

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

  @SerializedName("from_muxed")
  private String fromMuxed;

  @SerializedName("from_muxed_id")
  private BigInteger fromMuxedId;

  @SerializedName("to")
  protected final String to;

  @SerializedName("to_muxed")
  private String toMuxed;

  @SerializedName("to_muxed_id")
  private BigInteger toMuxedId;

  PaymentOperationResponse(
      String amount,
      String assetType,
      String assetCode,
      String assetIssuer,
      String from,
      String fromMuxed,
      BigInteger fromMuxedId,
      String to,
      String toMuxed,
      BigInteger toMuxedId) {
    this.amount = amount;
    this.assetType = assetType;
    this.assetCode = assetCode;
    this.assetIssuer = assetIssuer;
    this.from = from;
    this.fromMuxed = fromMuxed;
    this.fromMuxedId = fromMuxedId;
    this.to = to;
    this.toMuxed = toMuxed;
    this.toMuxedId = toMuxedId;
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

  public Optional<MuxedAccount> getFromMuxed() {
    if (this.fromMuxed == null || this.fromMuxed.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new MuxedAccount(this.fromMuxed, this.from, this.fromMuxedId));
  }

  public String getTo() {
    return to;
  }

  public Optional<MuxedAccount> getToMuxed() {
    if (this.toMuxed == null || this.toMuxed.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new MuxedAccount(this.toMuxed, this.to, this.toMuxedId));
  }
}
