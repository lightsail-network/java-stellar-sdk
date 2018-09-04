package org.stellar.sdk;

import com.google.common.base.Objects;
import org.stellar.sdk.xdr.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents <a href="https://www.stellar.org/developers/learn/concepts/list-of-operations.html#allow-trust" target="_blank">AllowTrust</a> operation.
 * @see <a href="https://www.stellar.org/developers/learn/concepts/list-of-operations.html" target="_blank">List of Operations</a>
 */
public class AllowTrustOperation extends Operation {

  private final String trustor;
  private final String assetCode;
  private final boolean authorize;

  private AllowTrustOperation(String trustor, String assetCode, boolean authorize) {
    this.trustor = checkNotNull(trustor, "trustor cannot be null");
    this.assetCode = checkNotNull(assetCode, "assetCode cannot be null");
    this.authorize = authorize;
  }

  /**
   * The account of the recipient of the trustline.
   */
  public String getTrustor() {
    return trustor;
  }

  /**
   * The asset of the trustline the source account is authorizing. For example, if a gateway wants to allow another account to hold its USD credit, the type is USD.
   */
  public String getAssetCode() {
    return assetCode;
  }

  /**
   * Flag indicating whether the trustline is authorized.
   */
  public boolean getAuthorize() {
    return authorize;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
    AllowTrustOp op = new AllowTrustOp();

    // trustor
    op.setTrustor(StrKey.encodeToXDRAccountId(this.trustor));
    // asset
    AllowTrustOp.AllowTrustOpAsset asset = new AllowTrustOp.AllowTrustOpAsset();
    if (assetCode.length() <= 4) {
      AssetCode4 assetCode4 = new AssetCode4();
      assetCode4.setAssetCode4(Util.paddedByteArray(assetCode, 4));
      asset.setDiscriminant(AssetType.ASSET_TYPE_CREDIT_ALPHANUM4);
      asset.setAssetCode4(assetCode4);
    } else {
      AssetCode12 assetCode12 = new AssetCode12();
      assetCode12.setAssetCode12(Util.paddedByteArray(assetCode, 12));
      asset.setDiscriminant(AssetType.ASSET_TYPE_CREDIT_ALPHANUM12);
      asset.setAssetCode12(assetCode12);
    }
    op.setAsset(asset);
    // authorize
    op.setAuthorize(authorize);

    org.stellar.sdk.xdr.Operation.OperationBody body = new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.ALLOW_TRUST);
    body.setAllowTrustOp(op);
    return body;
  }

  /**
   * Builds AllowTrust operation.
   * @see AllowTrustOperation
   */
  public static class Builder {
    private final String trustor;
    private final String assetCode;
    private final boolean authorize;

    private String mSourceAccount;

    Builder(AllowTrustOp op) {
      trustor = StrKey.encodeStellarAccountId(op.getTrustor().getAccountID().getEd25519().getUint256());
      switch (op.getAsset().getDiscriminant()) {
        case ASSET_TYPE_CREDIT_ALPHANUM4:
          assetCode = new String(op.getAsset().getAssetCode4().getAssetCode4()).trim();
          break;
        case ASSET_TYPE_CREDIT_ALPHANUM12:
          assetCode = new String(op.getAsset().getAssetCode12().getAssetCode12()).trim();
          break;
        default:
          throw new RuntimeException("Unknown asset code");
      }
      authorize = op.getAuthorize();
    }

    /**
     * Creates a new AllowTrust builder.
     * @param trustor The account of the recipient of the trustline.
     * @param assetCode The asset of the trustline the source account is authorizing. For example, if a gateway wants to allow another account to hold its USD credit, the type is USD.
     * @param authorize Flag indicating whether the trustline is authorized.
     */
    public Builder(String trustor, String assetCode, boolean authorize) {
      this.trustor = trustor;
      this.assetCode = assetCode;
      this.authorize = authorize;
    }

    /**
     * Set source account of this operation
     * @param sourceAccount Source account
     * @return Builder object so you can chain methods.
     */
    public Builder setSourceAccount(String sourceAccount) {
      mSourceAccount = sourceAccount;
      return this;
    }

    /**
     * Builds an operation
     */
    public AllowTrustOperation build() {
      AllowTrustOperation operation = new AllowTrustOperation(trustor, assetCode, authorize);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.getSourceAccount(), this.assetCode, this.authorize, this.trustor);
  }

  @Override
  public boolean equals(Object object) {
    if (object == null || !(object instanceof AllowTrustOperation)) {
      return false;
    }

    AllowTrustOperation other = (AllowTrustOperation) object;
    return Objects.equal(this.assetCode, other.assetCode) &&
            Objects.equal(this.authorize, other.authorize) &&
            Objects.equal(this.trustor, other.trustor) &&
            Objects.equal(this.getSourceAccount(), other.getSourceAccount());
  }
}
