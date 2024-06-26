package org.stellar.sdk.exception;

public class SubmitTransactionTimeoutResponseException extends SdkException {
  @Override
  public String getMessage() {
    return "Timeout. Please resubmit your transaction to receive submission status. More info: https://developers.stellar.org/api/errors/http-status-codes/horizon-specific/timeout/";
  }
}
