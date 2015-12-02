package org.stellar.sdk.effects;

import org.stellar.base.Keypair;

/**
 * Represents trustline_deauthorized effect response.
 */
public class TrustlineDeauthorizedEffect extends TrustlineAuthorization {
  TrustlineDeauthorizedEffect(Keypair trustor, String assetType, String assetCode) {
    super(trustor, assetType, assetCode);
  }
}
