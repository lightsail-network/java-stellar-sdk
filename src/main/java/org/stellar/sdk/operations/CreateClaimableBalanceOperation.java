package org.stellar.sdk.operations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.Asset;
import org.stellar.sdk.Claimant;
import org.stellar.sdk.Predicate;
import org.stellar.sdk.StrKey;
import org.stellar.sdk.xdr.ClaimantType;
import org.stellar.sdk.xdr.CreateClaimableBalanceOp;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#create-claimable-balance"
 * target="_blank">CreateClaimableBalance</a> operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class CreateClaimableBalanceOperation extends Operation {
  /* The amount of the asset (max of 7 decimal places). */
  @NonNull private final BigDecimal amount;
  /* The asset for the claimable balance. */
  @NonNull private final Asset asset;
  /* The list of claimants for the claimable balance. */
  @NonNull private final List<Claimant> claimants;

  /**
   * Construct a new {@link CreateClaimableBalanceOperation} object from a {@link
   * CreateClaimableBalanceOp} XDR object.
   *
   * @param op {@link CreateClaimableBalanceOp} XDR object
   * @return {@link CreateClaimableBalanceOperation} object
   */
  public static CreateClaimableBalanceOperation fromXdr(CreateClaimableBalanceOp op) {
    Asset asset = Asset.fromXdr(op.getAsset());
    BigDecimal amount = Operation.fromXdrAmount(op.getAmount().getInt64());
    List<Claimant> claimants = new ArrayList<>();
    for (org.stellar.sdk.xdr.Claimant c : op.getClaimants()) {
      claimants.add(
          new Claimant(
              StrKey.encodeEd25519PublicKey(c.getV0().getDestination()),
              Predicate.fromXdr(c.getV0().getPredicate())));
    }
    return new CreateClaimableBalanceOperation(amount, asset, claimants);
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
    CreateClaimableBalanceOp op = new CreateClaimableBalanceOp();

    // asset
    op.setAsset(asset.toXdr());
    // amount
    Int64 amount = new Int64();
    amount.setInt64(Operation.toXdrAmount(this.amount));
    op.setAmount(amount);

    org.stellar.sdk.xdr.Claimant[] xdrClaimants =
        new org.stellar.sdk.xdr.Claimant[claimants.size()];
    for (int i = 0; i < claimants.size(); i++) {

      org.stellar.sdk.xdr.Claimant.ClaimantV0 v0 = new org.stellar.sdk.xdr.Claimant.ClaimantV0();
      v0.setDestination(StrKey.encodeToXDRAccountId(claimants.get(i).getDestination()));
      v0.setPredicate(claimants.get(i).getPredicate().toXdr());

      xdrClaimants[i] = new org.stellar.sdk.xdr.Claimant();
      xdrClaimants[i].setDiscriminant(ClaimantType.CLAIMANT_TYPE_V0);
      xdrClaimants[i].setV0(v0);
    }
    op.setClaimants(xdrClaimants);

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.CREATE_CLAIMABLE_BALANCE);
    body.setCreateClaimableBalanceOp(op);
    return body;
  }

  private static final class CreateClaimableBalanceOperationBuilderImpl
      extends CreateClaimableBalanceOperationBuilder<
          CreateClaimableBalanceOperation, CreateClaimableBalanceOperationBuilderImpl> {

    public CreateClaimableBalanceOperation build() {
      CreateClaimableBalanceOperation op = new CreateClaimableBalanceOperation(this);
      if (op.claimants.isEmpty()) {
        throw new IllegalArgumentException("claimants cannot be empty");
      }
      return op;
    }
  }
}
