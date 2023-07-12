package org.stellar.sdk.responses;

public class SubmitTransactionTimeoutResponseException extends RuntimeException {
  @Override
  public String getMessage() {
    return "Timeout. Please resubmit your transaction to receive submission status. More info: https://developers.stellar.org/api/errors/http-status-codes/horizon-specific/timeout/";
  }
}
