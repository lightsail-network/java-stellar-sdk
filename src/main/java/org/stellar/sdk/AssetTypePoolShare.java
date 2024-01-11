package org.stellar.sdk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents Stellar liquidity pool share asset - <a
 * href="https://developers.stellar.org/docs/encyclopedia/liquidity-on-stellar-sdex-liquidity-pools#trustlines"
 * target="_blank">lumens (XLM)</a>
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/encyclopedia/liquidity-on-stellar-sdex-liquidity-pools#trustlines"
 *     target="_blank">Assets</a>
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
public final class AssetTypePoolShare extends Asset {

  /** The pool ID of the liquidity pool share asset */
  @NonNull private final String poolId;

  @Override
  public String toString() {
    return "liquidity_pool_shares";
  }

  @Override
  public String getType() {
    return "liquidity_pool_shares";
  }

  @Override
  public org.stellar.sdk.xdr.Asset toXdr() {
    throw new UnsupportedOperationException(
        "liquidity_pool_shares are not defined as Asset in XDR");
  }

  @Override
  public int compareTo(@NonNull Asset other) {
    if (!this.getClass().equals(other.getClass())) {
      return -1;
    }
    AssetTypePoolShare otherPoolShare = (AssetTypePoolShare) other;
    return poolId.compareTo(otherPoolShare.getPoolId());
  }
}
