package org.stellar.sdk.effects;

import org.stellar.base.Keypair;

public class TrustlineDeauthorizedEffect extends TrustlineAuthorization {
  TrustlineDeauthorizedEffect(Keypair trustor, String assetType, String assetCode) {
    super(trustor, assetType, assetCode);
  }
}
