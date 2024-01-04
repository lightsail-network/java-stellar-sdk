package org.stellar.sdk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.xdr.*;

/**
 * @deprecated As of release 0.24.0, replaced by {@link SetTrustlineFlagsOperation}
 *     <p>Represents <a
 *     href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#allow-trust"
 *     target="_blank">AllowTrust</a> operation.
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class AllowTrustOperation extends Operation {

  /** The account of the recipient of the trustline. */
  @Getter @NonNull private final String trustor;

  /**
   * The asset of the trustline the source account is authorizing. For example, if a gateway wants
   * to allow another account to hold its USD credit, the type is USD.
   */
  @Getter @NonNull private final String assetCode;

  /** Flag indicating whether the trustline is authorized. */
  private final boolean authorize;

  /** Flag indicating whether the trustline is authorized to maintain liabilities. */
  @Getter private final boolean authorizeToMaintainLiabilities;

  public boolean getAuthorize() {
    // lombok will generate a getter for authorize, but it will be `isAuthorize` instead of.
    // For keep the same with the old version, we need to override the getter.
    return authorize;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    AllowTrustOp op = new AllowTrustOp();

    // trustor
    op.setTrustor(StrKey.encodeToXDRAccountId(this.trustor));
    // asset
    AssetCode asset = new AssetCode();
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
    Uint32 flag = new Uint32();
    // authorize
    if (authorize) {
      flag.setUint32(new XdrUnsignedInteger(TrustLineFlags.AUTHORIZED_FLAG.getValue()));
    } else if (authorizeToMaintainLiabilities) {
      flag.setUint32(
          new XdrUnsignedInteger(
              TrustLineFlags.AUTHORIZED_TO_MAINTAIN_LIABILITIES_FLAG.getValue()));
    } else {
      flag.setUint32(new XdrUnsignedInteger(0));
    }
    op.setAuthorize(flag);

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.ALLOW_TRUST);
    body.setAllowTrustOp(op);
    return body;
  }

  /**
   * Builds AllowTrust operation.
   *
   * @see AllowTrustOperation
   */
  public static class Builder {

    private final String trustor;
    private final String assetCode;
    private final boolean authorize;
    private boolean authorizeToMaintainLiabilities;

    private String sourceAccount;

    Builder(AllowTrustOp op) {
      trustor = StrKey.encodeEd25519PublicKey(op.getTrustor());
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

      int flag = op.getAuthorize().getUint32().getNumber().intValue();
      if (flag == TrustLineFlags.AUTHORIZED_FLAG.getValue()) {
        authorize = true;
        authorizeToMaintainLiabilities = false;
      } else if (flag == TrustLineFlags.AUTHORIZED_TO_MAINTAIN_LIABILITIES_FLAG.getValue()) {
        authorize = false;
        authorizeToMaintainLiabilities = true;
      } else if (flag != 0) {
        throw new IllegalArgumentException("invalid authorize flag " + flag);
      } else {
        authorize = false;
        authorizeToMaintainLiabilities = false;
      }
    }

    /**
     * Creates a new AllowTrust builder.
     *
     * @param trustor The account of the recipient of the trustline.
     * @param assetCode The asset of the trustline the source account is authorizing. For example,
     *     if a gateway wants to allow another account to hold its USD credit, the type is USD.
     * @param authorize Flag indicating whether the trustline is authorized.
     */
    public Builder(String trustor, String assetCode, boolean authorize) {
      this.trustor = trustor;
      this.assetCode = assetCode;
      this.authorize = authorize;
    }

    /**
     * Set source account of this operation
     *
     * @param sourceAccount Source account
     * @return Builder object so you can chain methods.
     */
    public Builder setSourceAccount(String sourceAccount) {
      this.sourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public AllowTrustOperation build() {
      AllowTrustOperation operation =
          new AllowTrustOperation(trustor, assetCode, authorize, authorizeToMaintainLiabilities);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
