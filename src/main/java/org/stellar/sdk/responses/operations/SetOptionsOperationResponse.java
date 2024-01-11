package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.KeyPair;

/**
 * Represents SetOptions operation response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/operations/" target="_blank">Operation
 *     documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class SetOptionsOperationResponse extends OperationResponse {
  @SerializedName("low_threshold")
  Integer lowThreshold;

  @SerializedName("med_threshold")
  Integer medThreshold;

  @SerializedName("high_threshold")
  Integer highThreshold;

  @SerializedName("inflation_dest")
  String inflationDestination;

  @SerializedName("home_domain")
  String homeDomain;

  @SerializedName("signer_key")
  String signerKey;

  @SerializedName("signer_weight")
  Integer signerWeight;

  @SerializedName("master_key_weight")
  Integer masterKeyWeight;

  @SerializedName("clear_flags_s")
  String[] clearFlags;

  @SerializedName("set_flags_s")
  String[] setFlags;

  SetOptionsOperationResponse(
      Integer lowThreshold,
      Integer medThreshold,
      Integer highThreshold,
      String inflationDestination,
      String homeDomain,
      String signerKey,
      Integer signerWeight,
      Integer masterKeyWeight,
      String[] clearFlags,
      String[] setFlags) {
    this.lowThreshold = lowThreshold;
    this.medThreshold = medThreshold;
    this.highThreshold = highThreshold;
    this.inflationDestination = inflationDestination;
    this.homeDomain = homeDomain;
    this.signerKey = signerKey;
    this.signerWeight = signerWeight;
    this.masterKeyWeight = masterKeyWeight;
    this.clearFlags = clearFlags;
    this.setFlags = setFlags;
  }

  /**
   * @deprecated Use {@link SetOptionsOperationResponse#getSignerKey()}
   * @return
   */
  public KeyPair getSigner() {
    return KeyPair.fromAccountId(signerKey);
  }
}
