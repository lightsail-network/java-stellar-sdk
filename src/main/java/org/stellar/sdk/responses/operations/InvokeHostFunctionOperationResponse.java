package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.xdr.HostFunctionType;

/**
 * Represents InvokeHostFunction operation response.
 *
 * @see <a
 *     href="https://github.com/stellar/go/blob/7ff6ffae29d278f979fcd6c6bed8cd0d4b4d2e08/protocols/horizon/operations/main.go#L350-L367">Horizon
 *     Protocol</a>
 * @see <a href="https://developers.stellar.org/api/horizon/resources/operations"
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
  }
}
