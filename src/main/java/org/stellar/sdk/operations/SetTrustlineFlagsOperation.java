package org.stellar.sdk.operations;

import java.util.EnumSet;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
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
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#set-trustline-flags"
 * target="_blank">SetTrustlineFlags</a> operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class SetTrustlineFlagsOperation extends Operation {
  /** The account owning of the trustline. */
  @NonNull private final String trustor;

  /** The asset trustline whose flags are being modified. */
  @NonNull private final Asset asset;

  /** One or more flags (combined via bitwise-OR) indicating which flags to set. */
  @NonNull private final EnumSet<TrustLineFlags> setFlags;

  /** One or more flags (combined via bitwise OR) indicating which flags to clear. */
  @NonNull private final EnumSet<TrustLineFlags> clearFlags;

  /**
   * Construct a new {@link SetTrustlineFlagsOperation} object from a {@link SetTrustLineFlagsOp}
   * XDR object.
   *
   * @param op {@link SetTrustLineFlagsOp} XDR object
   * @return {@link SetTrustlineFlagsOperation} object
   */
  public static SetTrustlineFlagsOperation fromXdr(SetTrustLineFlagsOp op) {
    String trustor = StrKey.encodeEd25519PublicKey(op.getTrustor());
    AssetTypeCreditAlphaNum asset = Util.assertNonNativeAsset(Asset.fromXdr(op.getAsset()));
    EnumSet<TrustLineFlags> clearFlags =
        flagSetFromInt(op.getClearFlags().getUint32().getNumber().intValue());
    EnumSet<TrustLineFlags> setFlags =
        flagSetFromInt(op.getSetFlags().getUint32().getNumber().intValue());
    return new SetTrustlineFlagsOperation(trustor, asset, setFlags, clearFlags);
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

  private static EnumSet<TrustLineFlags> flagSetFromInt(int x) {
    EnumSet<TrustLineFlags> set = EnumSet.noneOf(TrustLineFlags.class);
    for (TrustLineFlags flag : TrustLineFlags.values()) {
      if ((flag.getValue() & x) != 0) {
        set.add(flag);
      }
    }
    return set;
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

  private static final class SetTrustlineFlagsOperationBuilderImpl
      extends SetTrustlineFlagsOperationBuilder<
          SetTrustlineFlagsOperation, SetTrustlineFlagsOperationBuilderImpl> {
    public SetTrustlineFlagsOperation build() {
      SetTrustlineFlagsOperation op = new SetTrustlineFlagsOperation(this);
      if (!(op.asset instanceof AssetTypeCreditAlphaNum)) {
        throw new IllegalArgumentException("native assets are not supported");
      }
      return op;
    }
  }
}
