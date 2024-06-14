package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.StrKey;
import org.stellar.sdk.xdr.*;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#revoke-sponsorship"
 * target="_blank">Revoke sponsorship</a> operation.
 *
 * <p>Revokes the sponsorship of a signer entry.
 *
 * @see <a href="https://developers.stellar.org/docs/encyclopedia/sponsored-reserves"
 *     target="_blank">Sponsored Reserves</a>
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RevokeSignerSponsorshipOperation extends Operation {
  /** The account whose signer will be revoked. */
  @NonNull private final String accountId;

  /** The signer whose sponsorship which will be revoked. */
  @NonNull private final SignerKey signer;

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    RevokeSponsorshipOp op = new RevokeSponsorshipOp();

    RevokeSponsorshipOp.RevokeSponsorshipOpSigner xdrSigner =
        new RevokeSponsorshipOp.RevokeSponsorshipOpSigner();
    xdrSigner.setAccountID(StrKey.encodeToXDRAccountId(accountId));
    xdrSigner.setSignerKey(signer);
    op.setSigner(xdrSigner);
    op.setDiscriminant(RevokeSponsorshipType.REVOKE_SPONSORSHIP_SIGNER);

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.REVOKE_SPONSORSHIP);
    body.setRevokeSponsorshipOp(op);

    return body;
  }

  public static class Builder {
    private final String accountId;
    private final SignerKey signer;

    private String sourceAccount;

    /**
     * Construct a new RevokeSignerSponsorshipOperation builder from a RevokeSponsorship XDR.
     *
     * @param op {@link RevokeSponsorshipOp}
     */
    Builder(RevokeSponsorshipOp op) {
      accountId = StrKey.encodeEd25519PublicKey(op.getSigner().getAccountID());
      signer = op.getSigner().getSignerKey();
    }

    /**
     * Creates a new RevokeSignerSponsorshipOperation builder.
     *
     * @param accountId The id of the account whose signer will be revoked.
     * @param signer The signer whose sponsorship which will be revoked.
     */
    public Builder(String accountId, SignerKey signer) {
      this.accountId = accountId;
      this.signer = signer;
    }

    /**
     * Sets the source account for this operation.
     *
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public RevokeSignerSponsorshipOperation.Builder setSourceAccount(
        @NonNull String sourceAccount) {
      this.sourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public RevokeSignerSponsorshipOperation build() {
      RevokeSignerSponsorshipOperation operation =
          new RevokeSignerSponsorshipOperation(accountId, signer);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
