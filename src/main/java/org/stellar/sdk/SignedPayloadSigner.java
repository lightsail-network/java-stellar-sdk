package org.stellar.sdk;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.xdr.AccountID;
import org.stellar.sdk.xdr.PublicKey;
import org.stellar.sdk.xdr.PublicKeyType;
import org.stellar.sdk.xdr.Uint256;

/**
 * Data model for the <a
 * href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0040.md#xdr-changes"
 * target="_blank">signed payload signer</a>
 */
@Getter
@EqualsAndHashCode
public class SignedPayloadSigner {
  public static final int SIGNED_PAYLOAD_MAX_PAYLOAD_LENGTH = 64;

  /** The account that represents the signed payload signer */
  private final AccountID signerAccountId;

  /** The payload that signatures are produced from */
  private final byte[] payload;

  /**
   * constructor
   *
   * @param signerAccountId - the xdr AccountID
   * @param payload - the raw payload for a signed payload
   */
  public SignedPayloadSigner(@NonNull AccountID signerAccountId, byte @NonNull [] payload) {
    if (payload.length > SIGNED_PAYLOAD_MAX_PAYLOAD_LENGTH) {
      throw new IllegalArgumentException(
          "invalid payload length, must be less than " + SIGNED_PAYLOAD_MAX_PAYLOAD_LENGTH);
    }
    if (signerAccountId.getAccountID().getDiscriminant() == null
        || !signerAccountId
            .getAccountID()
            .getDiscriminant()
            .equals(PublicKeyType.PUBLIC_KEY_TYPE_ED25519)) {
      throw new IllegalArgumentException(
          "invalid payload signer, only ED25519 public key accounts are supported currently");
    }
    this.signerAccountId = signerAccountId;
    this.payload = payload;
  }

  /**
   * constructor
   *
   * @param signerED25519PublicKey raw bytes of a ED25519 public key for the signer account
   * @param payload the raw payload for a signed payload
   */
  public SignedPayloadSigner(byte[] signerED25519PublicKey, byte[] payload) {
    this(
        new AccountID(
            new PublicKey.Builder()
                .discriminant(PublicKeyType.PUBLIC_KEY_TYPE_ED25519)
                .ed25519(new Uint256(signerED25519PublicKey))
                .build()),
        payload);
  }
}
