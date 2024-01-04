package org.stellar.sdk;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.stellar.sdk.xdr.AssetType;

/**
 * Class for LiquidityPoolShareChangeTrustAsset
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/encyclopedia/liquidity-on-stellar-sdex-liquidity-pools#liquidity-pools"
 *     target="_blank">Liquidity Pool</a>
 */
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class LiquidityPoolShareChangeTrustAsset extends ChangeTrustAsset {
  @NonNull private final LiquidityPoolParameters params;

  /**
   * Get the liquidity pool id
   *
   * @return LiquidityPoolID
   */
  public LiquidityPoolID getLiquidityPoolID() {
    return params.getId();
  }

  /**
   * Get the liquidity pool parameters
   *
   * @return LiquidityPoolParameters
   */
  public LiquidityPoolParameters getLiquidityPoolParams() {
    return params;
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
  public int compareTo(@NonNull ChangeTrustAsset other) {
    if (!Objects.equals(other.getType(), "pool_share")) {
      return 1;
    }
    return this.toString().compareTo(((LiquidityPoolShareChangeTrustAsset) other).toString());
  }

  @Override
  public org.stellar.sdk.xdr.ChangeTrustAsset toXdr() {
    org.stellar.sdk.xdr.ChangeTrustAsset xdr = new org.stellar.sdk.xdr.ChangeTrustAsset();
    xdr.setDiscriminant(AssetType.ASSET_TYPE_POOL_SHARE);
    org.stellar.sdk.xdr.LiquidityPoolParameters params = this.params.toXdr();
    xdr.setLiquidityPool(params);
    return xdr;
  }
}
