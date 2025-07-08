package org.stellar.sdk.responses.operations;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;
import org.stellar.sdk.xdr.HostFunctionType;

/**
 * Represents InvokeHostFunction operation response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/horizon/api-reference/resources/operations/object/invoke-host-function"
 *     target="_blank">Operation documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class InvokeHostFunctionOperationResponse extends OperationResponse {
  /**
   * the function type ({@link HostFunctionType}), which can have one of the following values:
   *
   * <ul>
   *   <li>"HostFunctionTypeHostFunctionTypeInvokeContract"
   *   <li>"HostFunctionTypeHostFunctionTypeCreateContract"
   *   <li>"HostFunctionTypeHostFunctionTypeUploadContractWasm"
   * </ul>
   */
  @SerializedName("function")
  String function;

  @SerializedName("parameters")
  List<HostFunctionParameter> parameters;

  @SerializedName("address")
  String address;

  @SerializedName("salt")
  String salt;

  @SerializedName("asset_balance_changes")
  List<AssetContractBalanceChange> assetBalanceChanges;

  @Value
  public static class HostFunctionParameter {
    @SerializedName("type")
    String type;

    @SerializedName("value")
    String value;
  }

  @Value
  public static class AssetContractBalanceChange {
    @SerializedName("asset_type")
    String assetType;

    @SerializedName("asset_code")
    String assetCode;

    @SerializedName("asset_issuer")
    String assetIssuer;

    @SerializedName("type")
    String type;

    @SerializedName("from")
    String from;

    @SerializedName("to")
    String to;

    @SerializedName("amount")
    String amount;

    @SerializedName("destination_muxed_id")
    BigInteger destinationMuxedId;

    public Asset getAsset() {
      return create(assetType, assetCode, assetIssuer);
    }
  }
}
