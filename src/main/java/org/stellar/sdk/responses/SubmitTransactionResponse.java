package org.stellar.sdk.responses;

import com.google.common.io.BaseEncoding;
import com.google.gson.annotations.SerializedName;

import org.stellar.sdk.Server;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.TransactionResult;
import org.stellar.sdk.xdr.XdrDataInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
    @SerializedName("envelope_xdr")
    private final String envelopeXdr;
    @SerializedName("result_xdr")
    private final String resultXdr;
    @SerializedName("extras")
    private final Extras extras;

    SubmitTransactionResponse(Extras extras, Long ledger, String hash, String envelopeXdr, String resultXdr) {
        this.extras = extras;
        this.ledger = ledger;
        this.hash = hash;
        this.envelopeXdr = envelopeXdr;
        this.resultXdr = resultXdr;
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

    public String getEnvelopeXdr() {
        if (this.isSuccess()) {
            return this.envelopeXdr;
        } else {
            if (this.getExtras() != null) {
                return this.getExtras().getEnvelopeXdr();
            }
            return null;
        }
    }

    public String getResultXdr() {
        if (this.isSuccess()) {
            return this.resultXdr;
        } else {
            if (this.getExtras() != null) {
                return this.getExtras().getResultXdr();
            }
            return null;
        }
    }

    /**
     * Helper method that returns Offer ID for ManageOffer from TransactionResult Xdr.
     * This is helpful when you need ID of an offer to update it later.
     * @param position Position of ManageOffer operation. If ManageOffer is second operation in this transaction this should be equal <code>1</code>.
     * @return Offer ID or <code>null</code> when operation at <code>position</code> is not a ManageOffer operation or error has occurred.
     */
    public Long getOfferIdFromResult(int position) {
        if (!this.isSuccess()) {
            return null;
        }

        BaseEncoding base64Encoding = BaseEncoding.base64();
        byte[] bytes = base64Encoding.decode(this.getResultXdr());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        XdrDataInputStream xdrInputStream = new XdrDataInputStream(inputStream);
        TransactionResult result;

        try {
            result = TransactionResult.decode(xdrInputStream);
        } catch (IOException e) {
            return null;
        }

        if (result.getResult().getResults()[position] == null) {
            return null;
        }

        if (result.getResult().getResults()[position].getTr().getDiscriminant() != OperationType.MANAGE_OFFER) {
            return null;
        }

        if (result.getResult().getResults()[0].getTr().getManageOfferResult().getSuccess().getOffer().getOffer() == null) {
            return null;
        }

        return result.getResult().getResults()[0].getTr().getManageOfferResult().getSuccess().getOffer().getOffer().getOfferID().getUint64();
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
