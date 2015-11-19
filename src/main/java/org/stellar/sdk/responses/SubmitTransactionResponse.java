package org.stellar.sdk.responses;

public class SubmitTransactionResponse {
    private final String hash;
    private final Long ledger;
    private final Extras extras;

    public SubmitTransactionResponse(Extras extras, Long ledger, String hash) {
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

    public static class Extras {
        private final String envelopeXdr;
        private final String resultXdr;

        public Extras(String envelopeXdr, String resultXdr) {
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
