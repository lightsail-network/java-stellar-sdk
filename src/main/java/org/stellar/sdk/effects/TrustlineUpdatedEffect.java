package org.stellar.sdk.effects;

public class TrustlineUpdatedEffect extends TrustlineCUD {
  public TrustlineUpdatedEffect(String limit, String assetType, String assetCode, String assetIssuer) {
    super(limit, assetType, assetCode, assetIssuer);
  }
}
