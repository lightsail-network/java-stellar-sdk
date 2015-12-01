package org.stellar.sdk.effects;

public class TrustlineRemovedEffect extends TrustlineCUD {
  TrustlineRemovedEffect(String limit, String assetType, String assetCode, String assetIssuer) {
    super(limit, assetType, assetCode, assetIssuer);
  }
}
