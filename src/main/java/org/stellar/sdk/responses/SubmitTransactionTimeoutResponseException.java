package org.stellar.sdk.responses;

public class SubmitTransactionTimeoutResponseException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Timeout. Please resubmit your transaction to receive submission status. More info: https://www.stellar.org/developers/horizon/reference/errors/timeout.html";
    }
}
