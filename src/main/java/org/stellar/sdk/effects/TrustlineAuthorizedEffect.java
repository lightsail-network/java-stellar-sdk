package org.stellar.sdk.effects;

import org.stellar.base.Keypair;

/**
 * Represents trustline_authorized effect response.
 */
public class TrustlineAuthorizedEffect extends TrustlineAuthorization {
  TrustlineAuthorizedEffect(Keypair trustor, String assetType, String assetCode) {
    super(trustor, assetType, assetCode);
  }
}
