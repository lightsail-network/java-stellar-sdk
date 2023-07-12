package org.stellar.sdk;

import com.google.common.base.Objects;
import java.util.EnumSet;
import org.stellar.sdk.xdr.*;

/**
 * Represents a Set Trustline Flags operation.
 *
 * @see <a href="https://developers.stellar.org/docs/start/list-of-operations/" target="_blank">List
 *     of Operations</a>
 */
public class SetTrustlineFlagsOperation extends Operation {
  private final String trustor;
  private final AssetTypeCreditAlphaNum asset;
  private final EnumSet<TrustLineFlags> clearFlags;
  private final EnumSet<TrustLineFlags> setFlags;

  private SetTrustlineFlagsOperation(
      String trustor,
      AssetTypeCreditAlphaNum asset,
      EnumSet<TrustLineFlags> clearFlags,
      EnumSet<TrustLineFlags> setFlags) {
    this.trustor = trustor;
    this.asset = asset;
    this.clearFlags = clearFlags;
    this.setFlags = setFlags;
  }

  /** The account owning of the trustline. */
  public String getTrustor() {
    return trustor;
  }

  /** The asset of the trustline. */
  public Asset getAsset() {
    return asset;
  }

  /** The flags to be set. */
  public EnumSet<TrustLineFlags> getSetFlags() {
    return setFlags;
  }

  /** The flags to be cleared. */
  public EnumSet<TrustLineFlags> getClearFlags() {
    return clearFlags;
  }

  private static Uint32 bitwiseOr(EnumSet<TrustLineFlags> set) {
    int v = 0;
    for (TrustLineFlags f : set) {
      v |= f.getValue();
    }
    Uint32 combined = new Uint32();
    combined.setUint32(v);
    return combined;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    SetTrustLineFlagsOp op = new SetTrustLineFlagsOp();

    op.setTrustor(StrKey.encodeToXDRAccountId(this.trustor));
    op.setAsset(asset.toXdr());
    op.setClearFlags(bitwiseOr(clearFlags));
    op.setSetFlags(bitwiseOr(setFlags));

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.SET_TRUST_LINE_FLAGS);
    body.setSetTrustLineFlagsOp(op);
    return body;
  }

  /**
   * Builds SetTrustlineFlagsOperation operation.
   *
   * @see SetTrustlineFlagsOperation
   */
  public static class Builder {
    private final String trustor;
    private final AssetTypeCreditAlphaNum asset;
    private final EnumSet<TrustLineFlags> clearFlags;
    private final EnumSet<TrustLineFlags> setFlags;

    private String mSourceAccount;

    Builder(SetTrustLineFlagsOp op) {
      trustor = StrKey.encodeStellarAccountId(op.getTrustor());
      asset = Util.assertNonNativeAsset(Asset.fromXdr(op.getAsset()));
      clearFlags = flagSetFromInt(op.getClearFlags().getUint32());
      setFlags = flagSetFromInt(op.getSetFlags().getUint32());
    }

    private static EnumSet<TrustLineFlags> flagSetFromInt(int x) {
      EnumSet<TrustLineFlags> set = EnumSet.noneOf(TrustLineFlags.class);
      for (TrustLineFlags flag : TrustLineFlags.values()) {
        if ((flag.getValue() & x) != 0) {
          set.add(flag);
        }
      }
      return set;
    }

    /**
     * Creates a new SetTrustlineFlagsOperation builder.
     *
     * @param trustor The account holding the trustline.
     * @param asset The asset held in the trustline.
     * @param clearFlags The flags to be cleared on the trustline.
     * @param setFlags The flags to be set on the trustline.
     */
    public Builder(
        String trustor,
        Asset asset,
        EnumSet<TrustLineFlags> clearFlags,
        EnumSet<TrustLineFlags> setFlags) {
      this.trustor = trustor;
      this.asset = Util.assertNonNativeAsset(asset);
      this.clearFlags = clearFlags;
      this.setFlags = setFlags;
    }

    /**
     * Set source account of this operation
     *
     * @param sourceAccount Source account
     * @return Builder object so you can chain methods.
     */
    public Builder setSourceAccount(String sourceAccount) {
      mSourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public SetTrustlineFlagsOperation build() {
      SetTrustlineFlagsOperation operation =
          new SetTrustlineFlagsOperation(trustor, asset, clearFlags, setFlags);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(
        this.getSourceAccount(), this.trustor, this.asset, this.clearFlags, this.setFlags);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SetTrustlineFlagsOperation)) {
      return false;
    }

    SetTrustlineFlagsOperation other = (SetTrustlineFlagsOperation) object;
    return Objects.equal(this.trustor, other.trustor)
        && Objects.equal(this.asset, other.asset)
        && Objects.equal(this.clearFlags, other.clearFlags)
        && Objects.equal(this.setFlags, other.setFlags)
        && Objects.equal(this.getSourceAccount(), other.getSourceAccount());
  }
}
