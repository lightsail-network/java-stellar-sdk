package org.stellar.sdk.operations;

import java.nio.charset.StandardCharsets;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.StrKey;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.AllowTrustOp;
import org.stellar.sdk.xdr.AssetCode;
import org.stellar.sdk.xdr.AssetCode12;
import org.stellar.sdk.xdr.AssetCode4;
import org.stellar.sdk.xdr.AssetType;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.TrustLineFlags;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

/**
 * @deprecated As of release 0.24.0, replaced by {@link SetTrustlineFlagsOperation}
 *     <p>Represents <a
 *     href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#allow-trust"
 *     target="_blank">AllowTrust</a> operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
@Deprecated
public class AllowTrustOperation extends Operation {

  /** The account of the recipient of the trustline. */
  @NonNull private final String trustor;

  /**
   * The asset of the trustline the source account is authorizing. For example, if a gateway wants
   * to allow another account to hold its USD credit, the type is USD.
   */
  @NonNull private final String assetCode;

  /** Indicates which flags to set for a trust line entry */
  @NonNull private final TrustLineEntryFlag authorize;

  /**
   * Construct a new {@link AllowTrustOperation} object from a {@link AllowTrustOp} XDR object.
   *
   * @param op {@link AllowTrustOp} XDR object
   * @return {@link AllowTrustOperation} object
   */
  public static AllowTrustOperation fromXdr(AllowTrustOp op) {
    String trustor =
        StrKey.encodeEd25519PublicKey(op.getTrustor().getAccountID().getEd25519().getUint256());
    String assetCode;
    switch (op.getAsset().getDiscriminant()) {
      case ASSET_TYPE_CREDIT_ALPHANUM4:
        assetCode =
            new String(op.getAsset().getAssetCode4().getAssetCode4(), StandardCharsets.UTF_8)
                .trim();
        break;
      case ASSET_TYPE_CREDIT_ALPHANUM12:
        assetCode =
            new String(op.getAsset().getAssetCode12().getAssetCode12(), StandardCharsets.UTF_8)
                .trim();
        break;
      default:
        throw new IllegalArgumentException("Unknown asset code");
    }
    int flag = op.getAuthorize().getUint32().getNumber().intValue();
    TrustLineEntryFlag authorize = TrustLineEntryFlag.fromValue(flag);
    return new AllowTrustOperation(trustor, assetCode, authorize);
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
    AllowTrustOp op = new AllowTrustOp();

    // trustor
    op.setTrustor(KeyPair.fromAccountId(this.trustor).getXdrAccountId());
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
    Uint32 flag = new Uint32(new XdrUnsignedInteger(authorize.value));
    op.setAuthorize(flag);

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.ALLOW_TRUST);
    body.setAllowTrustOp(op);
    return body;
  }

  /**
   * Indicates which flags to set for a trust line entry. For details about the flags, please refer
   * to the <a
   * href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0018.md">CAP-0018</a>.
   */
  @Getter
  public enum TrustLineEntryFlag {
    /**
     * The account can hold a balance but cannot receive payments, send payments, maintain offers or
     * manage offers.
     */
    UNAUTHORIZED_FLAG(0),
    /**
     * The account can hold a balance, receive payments, send payments, maintain offers or manage
     * offers.
     */
    AUTHORIZED_FLAG(TrustLineFlags.AUTHORIZED_FLAG.getValue()),
    /**
     * The account can hold a balance and maintain offers but cannot receive payments, send payments
     * or manage offers.
     */
    AUTHORIZED_TO_MAINTAIN_LIABILITIES_FLAG(
        TrustLineFlags.AUTHORIZED_TO_MAINTAIN_LIABILITIES_FLAG.getValue());

    private final int value;

    TrustLineEntryFlag(int value) {
      this.value = value;
    }

    static TrustLineEntryFlag fromValue(int value) {
      for (TrustLineEntryFlag flag : TrustLineEntryFlag.values()) {
        if (flag.getValue() == value) {
          return flag;
        }
      }
      throw new IllegalArgumentException("Invalid TrustLineEntryFlag value: " + value);
    }
  }
}
