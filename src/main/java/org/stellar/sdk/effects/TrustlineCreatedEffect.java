package org.stellar.sdk.effects;

/**
 * Represents trustline_created effect response.
 */
public class TrustlineCreatedEffect extends TrustlineCUD {
  TrustlineCreatedEffect(String limit, String assetType, String assetCode, String assetIssuer) {
    super(limit, assetType, assetCode, assetIssuer);
  }
}
