package org.stellar.sdk.effects;

public class TrustlineCreatedEffect extends TrustlineCUD {
  public TrustlineCreatedEffect(String limit, String assetType, String assetCode, String assetIssuer) {
    super(limit, assetType, assetCode, assetIssuer);
  }
}
