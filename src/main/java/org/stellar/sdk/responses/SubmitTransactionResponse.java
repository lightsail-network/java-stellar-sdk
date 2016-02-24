package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;

import org.stellar.sdk.Server;

import java.util.ArrayList;

/**
 * Represents server response after submitting transaction.
 * @see Server#submitTransaction(org.stellar.sdk.Transaction)
 */
public class SubmitTransactionResponse extends Response {
    @SerializedName("hash")
    private final String hash;
    @SerializedName("ledger")
    private final Long ledger;
    @SerializedName("extras")
    private final Extras extras;

    SubmitTransactionResponse(Extras extras, Long ledger, String hash) {
        this.extras = extras;
        this.ledger = ledger;
        this.hash = hash;
    }

    public boolean isSuccess() {
        return ledger != null;
    }

    public String getHash() {
        return hash;
    }

    public Long getLedger() {
        return ledger;
    }

    /**
     * Additional information returned by a server. This will be <code>null</code> if transaction succeeded.
     */
    public Extras getExtras() {
        return extras;
    }

    /**
     * Additional information returned by a server.
     */
    public static class Extras {
        @SerializedName("envelope_xdr")
        private final String envelopeXdr;
        @SerializedName("result_xdr")
        private final String resultXdr;
        @SerializedName("result_codes")
        private final ResultCodes resultCodes;

        Extras(String envelopeXdr, String resultXdr, ResultCodes resultCodes) {
            this.envelopeXdr = envelopeXdr;
            this.resultXdr = resultXdr;
            this.resultCodes = resultCodes;
        }

        /**
         * Returns XDR TransactionEnvelope base64-encoded string.
         * Use <a href="http://stellar.github.io/xdr-viewer/">xdr-viewer</a> to debug.
         */
        public String getEnvelopeXdr() {
            return envelopeXdr;
        }

        /**
         * Returns XDR TransactionResult base64-encoded string
         * Use <a href="http://stellar.github.io/xdr-viewer/">xdr-viewer</a> to debug.
         */
        public String getResultXdr() {
            return resultXdr;
        }

        /**
         * Returns ResultCodes object that contains result codes for transaction.
         */
        public ResultCodes getResultCodes() {
            return resultCodes;
        }

        /**
         * Contains result codes for this transaction.
         * @see <a href="https://github.com/stellar/horizon/blob/master/src/github.com/stellar/horizon/codes/main.go" target="_blank">Possible values</a>
         */
        public static class ResultCodes {
            @SerializedName("transaction")
            private final String transactionResultCode;
            @SerializedName("operations")
            private final ArrayList<String> operationsResultCodes;

            public ResultCodes(String transactionResultCode, ArrayList<String> operationsResultCodes) {
                this.transactionResultCode = transactionResultCode;
                this.operationsResultCodes = operationsResultCodes;
            }

            public String getTransactionResultCode() {
                return transactionResultCode;
            }

            public ArrayList<String> getOperationsResultCodes() {
                return operationsResultCodes;
            }
        }
    }
}
