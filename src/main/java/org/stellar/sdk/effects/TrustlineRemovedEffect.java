package org.stellar.sdk.effects;

/**
 * Represents trustline_removed effect response.
 */
public class TrustlineRemovedEffect extends TrustlineCUD {
  TrustlineRemovedEffect(String limit, String assetType, String assetCode, String assetIssuer) {
    super(limit, assetType, assetCode, assetIssuer);
  }
}
