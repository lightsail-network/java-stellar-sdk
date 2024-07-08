package org.stellar.sdk.responses.effects;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Base64Factory;

/**
 * Represents data_created effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class DataCreatedEffectResponse extends EffectResponse {
  String name;
  String value;

  /**
   * Gets decoded value for the base64 encoded data value.
   *
   * @return decoded value
   */
  public byte[] getDecodedValue() {
    return Base64Factory.getInstance().decode(value);
  }
}
