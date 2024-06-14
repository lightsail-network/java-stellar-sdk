package org.stellar.sdk.operations;

import java.util.EnumSet;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.StrKey;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.SetTrustLineFlagsOp;
import org.stellar.sdk.xdr.TrustLineFlags;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#set-trustline-flags"
 * target="_blank">SetTrustlineFlags</a> operation.
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class SetTrustlineFlagsOperation extends Operation {
  /** The account owning of the trustline. */
  @Getter @NonNull private final String trustor;

  @NonNull private final AssetTypeCreditAlphaNum asset;
  @Getter @NonNull private final EnumSet<TrustLineFlags> clearFlags;
  @Getter @NonNull private final EnumSet<TrustLineFlags> setFlags;

  /** The asset of the trustline. */
  public Asset getAsset() {
    return asset;
  }

  private static Uint32 bitwiseOr(EnumSet<TrustLineFlags> set) {
    int v = 0;
    for (TrustLineFlags f : set) {
      v |= f.getValue();
    }
    Uint32 combined = new Uint32();
    combined.setUint32(new XdrUnsignedInteger(v));
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

    private String sourceAccount;

    Builder(SetTrustLineFlagsOp op) {
      trustor = StrKey.encodeEd25519PublicKey(op.getTrustor());
      asset = Util.assertNonNativeAsset(Asset.fromXdr(op.getAsset()));
      clearFlags = flagSetFromInt(op.getClearFlags().getUint32().getNumber().intValue());
      setFlags = flagSetFromInt(op.getSetFlags().getUint32().getNumber().intValue());
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
      this.sourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public SetTrustlineFlagsOperation build() {
      SetTrustlineFlagsOperation operation =
          new SetTrustlineFlagsOperation(trustor, asset, clearFlags, setFlags);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
