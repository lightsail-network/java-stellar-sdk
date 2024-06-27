package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.StrKey;
import org.stellar.sdk.xdr.*;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#revoke-sponsorship"
 * target="_blank">Revoke sponsorship</a> operation.
 *
 * <p>Revokes the sponsorship of a signer entry.
 *
 * @see <a href="https://developers.stellar.org/docs/encyclopedia/sponsored-reserves"
 *     target="_blank">Sponsored Reserves</a>
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class RevokeSignerSponsorshipOperation extends Operation {
  /** The account whose signer will be revoked. */
  @NonNull private final String accountId;

  /** The signer whose sponsorship which will be revoked. */
  @NonNull private final SignerKey signer;

  /**
   * Construct a new {@link RevokeSignerSponsorshipOperation} object from a {@link
   * RevokeSponsorshipOp} XDR object.
   *
   * @param op {@link RevokeSponsorshipOp} XDR object
   * @return {@link RevokeSignerSponsorshipOperation} object
   */
  public static RevokeSignerSponsorshipOperation fromXdr(RevokeSponsorshipOp op) {
    String accountId = StrKey.encodeEd25519PublicKey(op.getSigner().getAccountID());
    SignerKey signer = op.getSigner().getSignerKey();
    return new RevokeSignerSponsorshipOperation(accountId, signer);
  }

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
}
