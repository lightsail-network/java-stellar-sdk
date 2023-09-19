package org.stellar.sdk;

import com.google.common.base.Objects;

/**
 * Represents Stellar liquidity pool share asset - <a
 * href="https://developers.stellar.org/docs/glossary/liquidity-pool/#trustlines"
 * target="_blank">lumens (XLM)</a>
 *
 * @see <a href="https://developers.stellar.org/docs/glossary/liquidity-pool/#trustlines"
 *     target="_blank">Assets</a>
 */
public final class AssetTypePoolShare extends Asset {

  private final String poolId;

  public AssetTypePoolShare(String poolId) {
    this.poolId = poolId;
  }

  @Override
  public String toString() {
    return "liquidity_pool_shares";
  }

  @Override
  public String getType() {
    return "liquidity_pool_shares";
  }

  @Override
  public boolean equals(Object object) {
    if (object == null || !this.getClass().equals(object.getClass())) {
      return false;
    }

    return (Objects.equal(((AssetTypePoolShare) object).getPoolId(), poolId));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(poolId);
  }

  @Override
  public org.stellar.sdk.xdr.Asset toXdr() {
    throw new UnsupportedOperationException(
        "liquidity_pool_shares are not defined as Asset in XDR");
  }

  @Override
  public int compareTo(Asset other) {
    if (other == null || !this.getClass().equals(other.getClass())) {
      return -1;
    }

    AssetTypePoolShare otherPoolShare = (AssetTypePoolShare) other;

    if (poolId == null && otherPoolShare.getPoolId() == null) {
      return 0;
    }

    if (poolId == null) {
      return -1;
    }

    if (otherPoolShare.getPoolId() == null) {
      return 1;
    }

    return poolId.compareTo(otherPoolShare.getPoolId());
  }

  public String getPoolId() {
    return poolId;
  }
}
