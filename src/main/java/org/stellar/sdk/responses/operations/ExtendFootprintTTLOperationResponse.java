package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;

/**
 * Represents ExtendFootprintTTL operation response.
 *
 * <p>TODO: update link
 *
 * @see <a
 *     href="https://github.com/stellar/go/blob/7ff6ffae29d278f979fcd6c6bed8cd0d4b4d2e08/protocols/horizon/operations/main.go#L376-L381">Horizon
 *     Protocol</a>
 * @see <a href="https://developers.stellar.org/api/horizon/resources/operations"
 *     target="_blank">Operation documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class ExtendFootprintTTLOperationResponse extends OperationResponse {
  @SerializedName("extend_to")
  private final Long extendTo;

  public ExtendFootprintTTLOperationResponse(Long extendTo) {
    this.extendTo = extendTo;
  }

  public Long getExtendTo() {
    return extendTo;
  }
}
