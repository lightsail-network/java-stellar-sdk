package org.stellar.sdk;

/**
 * Data model for the <a href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0040.md#xdr-changes">signed payload signer </a>
 */
public class SignedPayloadSigner {
    private String accountId;
    private byte[] payload;

    /**
     * constructor
     *
     * @param accountId - the StrKey format of a stellar AccountId
     * @param payload - the raw payload for a signed payload
     */
    public SignedPayloadSigner(String accountId, byte[] payload) {
        this.accountId = accountId;
        this.payload = payload;
    }

    /**
     * get the StrKey encoded representation of a signed payload signer
     * @return stellar account id in StrKey encoding
     */
    public String getEncodedAccountId() {
        return accountId;
    }

    /**
     * get the binary format of a stellar account id, a Ed25519 public key
     * @return stellar account id in binary format
     */
    public byte[] getDecodedAccountId() {
        return StrKey.decodeStellarAccountId(accountId);
    }

    /**
     * get the payload that signatures are produced from.
     * @see <a href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0040.md#semantics"/>
     * @return
     */
    public byte[] getPayload() {
        return payload;
    }
}
