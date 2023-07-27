package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;

/**
 * Represents BumpFootprintExpiration operation response.
 *
 * @see <a
 *     href="https://github.com/stellar/go/blob/7ff6ffae29d278f979fcd6c6bed8cd0d4b4d2e08/protocols/horizon/operations/main.go#L376-L381">Horizon
 *     Protocol</a>
 * @see <a href="https://developers.stellar.org/api/horizon/resources/operations"
 *     target="_blank">Operation documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class BumpFootprintExpirationOperationResponse extends OperationResponse {
  @SerializedName("ledgers_to_expire")
  private final Long ledgers_to_expire;

  public BumpFootprintExpirationOperationResponse(Long ledgersToExpire) {
    ledgers_to_expire = ledgersToExpire;
  }

  public Long getLedgers_to_expire() {
    return ledgers_to_expire;
  }
}
