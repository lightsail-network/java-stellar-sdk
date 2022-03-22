package org.stellar.sdk;

import org.stellar.sdk.xdr.AccountID;
import org.stellar.sdk.xdr.PublicKey;
import org.stellar.sdk.xdr.PublicKeyType;
import org.stellar.sdk.xdr.Uint256;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Data model for the <a href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0040.md#xdr-changes">signed payload signer </a>
 */
public class SignedPayloadSigner {
    private AccountID accountId;
    private byte[] payload;

    /**
     * constructor
     *
     * @param accountId - the xdr AccountID
     * @param payload - the raw payload for a signed payload
     */
    public SignedPayloadSigner(AccountID accountId, byte[] payload) {
        this.accountId = checkNotNull(accountId);
        this.payload = checkNotNull(payload);
    }

    /**
     * construcxtor
     *
     * @param ed25519PublicKey raw bytes of a ED25519 public key
     * @param payload the raw payload for a signed payload
     */
    public SignedPayloadSigner(byte[] ed25519PublicKey, byte[] payload ) {
        this(new AccountID(
                new PublicKey.Builder()
                        .discriminant(PublicKeyType.PUBLIC_KEY_TYPE_ED25519)
                        .ed25519(new Uint256(ed25519PublicKey)).build()), payload);
    }

    /**
     * get the account that represents the signed payload signer
     * @return stellar account
     */
    public AccountID getAccountId() {
        return accountId;
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
