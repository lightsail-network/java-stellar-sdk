package org.stellar.sdk.responses.operations;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
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
public class InvokeHostFunctionOperationResponse extends OperationResponse {
  @SerializedName("function")
  private final String function;

  @SerializedName("parameters")
  private final ImmutableList<HostFunctionParameter> parameters;

  @SerializedName("address")
  private final String address;

  @SerializedName("salt")
  private final String salt;

  @SerializedName("asset_balance_changes")
  private final ImmutableList<AssetContractBalanceChange> assetBalanceChanges;

  public InvokeHostFunctionOperationResponse(
      String function,
      ImmutableList<HostFunctionParameter> parameters,
      String address,
      String salt,
      ImmutableList<AssetContractBalanceChange> assetBalanceChanges) {
    this.function = function;
    this.parameters = parameters;
    this.address = address;
    this.salt = salt;
    this.assetBalanceChanges = assetBalanceChanges;
  }

  /**
   * Returns the function type ({@link HostFunctionType}), which can have one of the following
   * values:
   *
   * <ul>
   *   <li>"HostFunctionTypeHostFunctionTypeInvokeContract"
   *   <li>"HostFunctionTypeHostFunctionTypeCreateContract"
   *   <li>"HostFunctionTypeHostFunctionTypeUploadContractWasm"
   * </ul>
   *
   * @return the function type of the host function
   */
  public String getFunction() {
    return function;
  }

  public ImmutableList<HostFunctionParameter> getParameters() {
    return parameters;
  }

  public String getAddress() {
    return address;
  }

  public String getSalt() {
    return salt;
  }

  public ImmutableList<AssetContractBalanceChange> getAssetBalanceChanges() {
    return assetBalanceChanges;
  }

  public static class HostFunctionParameter {

    @SerializedName("type")
    private final String type;

    @SerializedName("value")
    private final String value;

    HostFunctionParameter(String type, String value) {
      this.type = type;
      this.value = value;
    }

    public String getType() {
      return type;
    }

    public String getValue() {
      return value;
    }
  }

  public static class AssetContractBalanceChange {
    @SerializedName("asset_type")
    private final String assetType;

    @SerializedName("asset_code")
    private final String assetCode;

    @SerializedName("asset_issuer")
    private final String assetIssuer;

    @SerializedName("type")
    private final String type;

    @SerializedName("from")
    private final String from;

    @SerializedName("to")
    private final String to;

    @SerializedName("amount")
    private final String amount;

    AssetContractBalanceChange(
        String assetType,
        String assetCode,
        String assetIssuer,
        String type,
        String from,
        String to,
        String amount) {
      this.assetType = assetType;
      this.assetCode = assetCode;
      this.assetIssuer = assetIssuer;
      this.type = type;
      this.from = from;
      this.to = to;
      this.amount = amount;
    }

    public String getAssetType() {
      return assetType;
    }

    public String getAssetCode() {
      return assetCode;
    }

    public String getAssetIssuer() {
      return assetIssuer;
    }

    public String getType() {
      return type;
    }

    public String getFrom() {
      return from;
    }

    public String getTo() {
      return to;
    }

    public String getAmount() {
      return amount;
    }
  }
}
