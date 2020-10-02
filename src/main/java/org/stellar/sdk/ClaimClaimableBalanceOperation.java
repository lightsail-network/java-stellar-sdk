package org.stellar.sdk;

import com.google.common.base.Objects;
import com.google.common.io.BaseEncoding;
import org.stellar.sdk.xdr.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClaimClaimableBalanceOperation extends Operation {
  private final String balanceId;


  private ClaimClaimableBalanceOperation(String balanceId) {
    this.balanceId = checkNotNull(balanceId, "balanceId cannot be null");
  }

  public String getBalanceId() {
    return balanceId;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
    ClaimClaimableBalanceOp op = new ClaimClaimableBalanceOp();

    ClaimableBalanceID id = new ClaimableBalanceID();
    id.setDiscriminant(ClaimableBalanceIDType.CLAIMABLE_BALANCE_ID_TYPE_V0);
    Hash hash = new Hash();
    hash.setHash(BaseEncoding.base16().lowerCase().decode(balanceId.toLowerCase()));
    id.setV0(hash);

    op.setBalanceID(id);

    org.stellar.sdk.xdr.Operation.OperationBody body = new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.CLAIM_CLAIMABLE_BALANCE);
    body.setClaimClaimableBalanceOp(op);
    return body;
  }

  public static class Builder {
    private final String balanceId;

    private String mSourceAccount;

    /**
     * Construct a new ClaimClaimableBalance builder from a ClaimClaimableBalance XDR.
     * @param op {@link ClaimClaimableBalanceOp}
     */
    Builder(ClaimClaimableBalanceOp op) {
      balanceId = BaseEncoding.base16().lowerCase().encode(op.getBalanceID().getV0().getHash());
    }

    /**
     * Creates a new ClaimClaimableBalance builder.
     * @param balanceId The id of the claimable balance.
     */
    public Builder(String balanceId) {
      this.balanceId = balanceId;
    }

    /**
     * Sets the source account for this operation.
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public ClaimClaimableBalanceOperation.Builder setSourceAccount(String sourceAccount) {
      mSourceAccount = checkNotNull(sourceAccount, "sourceAccount cannot be null");
      return this;
    }

    /**
     * Builds an operation
     */
    public ClaimClaimableBalanceOperation build() {
      ClaimClaimableBalanceOperation operation = new ClaimClaimableBalanceOperation(balanceId);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.balanceId, this.getSourceAccount());
  }

  @Override
  public boolean equals(Object object) {
    if (object == null || !(object instanceof ClaimClaimableBalanceOperation)) {
      return false;
    }

    ClaimClaimableBalanceOperation other = (ClaimClaimableBalanceOperation) object;
    return Objects.equal(this.balanceId, other.balanceId) &&
        Objects.equal(this.getSourceAccount(), other.getSourceAccount());
  }

}
