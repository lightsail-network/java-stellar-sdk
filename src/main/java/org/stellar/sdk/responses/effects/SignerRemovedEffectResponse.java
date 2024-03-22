package org.stellar.sdk.responses.effects;

import lombok.EqualsAndHashCode;
import lombok.Value;

/** Represents signer_removed effect response. */
@Value
@EqualsAndHashCode(callSuper = true)
public class SignerRemovedEffectResponse extends SignerEffectResponse {
  SignerRemovedEffectResponse(Integer weight, String publicKey) {
    super(weight, publicKey);
  }
}
