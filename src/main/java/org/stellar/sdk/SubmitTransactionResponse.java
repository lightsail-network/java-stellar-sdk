package org.stellar.sdk;

import com.google.gson.annotations.SerializedName;

/**
 * Represents server response after submitting transaction.
 * @see Server#submitTransaction(org.stellar.base.Transaction)
 */
public class SubmitTransactionResponse {
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

        Extras(String envelopeXdr, String resultXdr) {
            this.envelopeXdr = envelopeXdr;
            this.resultXdr = resultXdr;
        }

        public String getEnvelopeXdr() {
            return envelopeXdr;
        }

        public String getResultXdr() {
            return resultXdr;
        }
    }
}
