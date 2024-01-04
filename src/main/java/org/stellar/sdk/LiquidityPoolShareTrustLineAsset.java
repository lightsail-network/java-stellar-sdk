package org.stellar.sdk;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.stellar.sdk.xdr.AssetType;

/**
 * Class for LiquidityPoolShareTrustLineAsset
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/encyclopedia/liquidity-on-stellar-sdex-liquidity-pools#liquidity-pools"
 *     target="_blank">Liquidity Pool</a>
 */
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class LiquidityPoolShareTrustLineAsset extends TrustLineAsset {
  @NonNull private final LiquidityPoolID id;

  /**
   * Constructor
   *
   * @param params the LiquidityPoolParameters
   */
  public LiquidityPoolShareTrustLineAsset(@NonNull LiquidityPoolParameters params) {
    id = params.getId();
  }

  /**
   * Get the liquidity pool id
   *
   * @return LiquidityPoolID
   */
  public LiquidityPoolID getLiquidityPoolID() {
    return id;
  }

  @Override
  public String getType() {
    return "pool_share";
  }

  @Override
  public String toString() {
    return this.getLiquidityPoolID().toString();
  }

  @Override
  public int compareTo(@NonNull TrustLineAsset other) {
    if (!Objects.equals(other.getType(), "pool_share")) {
      return 1;
    }
    return this.toString().compareTo(((LiquidityPoolShareTrustLineAsset) other).toString());
  }

  @Override
  public org.stellar.sdk.xdr.TrustLineAsset toXdr() {
    org.stellar.sdk.xdr.TrustLineAsset xdr = new org.stellar.sdk.xdr.TrustLineAsset();
    xdr.setDiscriminant(AssetType.ASSET_TYPE_POOL_SHARE);
    xdr.setLiquidityPoolID(id.toXdr());
    return xdr;
  }
}
