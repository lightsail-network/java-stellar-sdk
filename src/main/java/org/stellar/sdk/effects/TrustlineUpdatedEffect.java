package org.stellar.sdk.effects;

/**
 * Represents trustline_updated effect response.
 */
public class TrustlineUpdatedEffect extends TrustlineCUD {
  TrustlineUpdatedEffect(String limit, String assetType, String assetCode, String assetIssuer) {
    super(limit, assetType, assetCode, assetIssuer);
  }
}
