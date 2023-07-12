package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.KeyPair;

/**
 * Represents SetOptions operation response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/operations/" target="_blank">Operation
 *     documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class SetOptionsOperationResponse extends OperationResponse {
  @SerializedName("low_threshold")
  protected final Integer lowThreshold;

  @SerializedName("med_threshold")
  protected final Integer medThreshold;

  @SerializedName("high_threshold")
  protected final Integer highThreshold;

  @SerializedName("inflation_dest")
  protected final String inflationDestination;

  @SerializedName("home_domain")
  protected final String homeDomain;

  @SerializedName("signer_key")
  protected final String signerKey;

  @SerializedName("signer_weight")
  protected final Integer signerWeight;

  @SerializedName("master_key_weight")
  protected final Integer masterKeyWeight;

  @SerializedName("clear_flags_s")
  protected final String[] clearFlags;

  @SerializedName("set_flags_s")
  protected final String[] setFlags;

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

  public Integer getLowThreshold() {
    return lowThreshold;
  }

  public Integer getMedThreshold() {
    return medThreshold;
  }

  public Integer getHighThreshold() {
    return highThreshold;
  }

  public String getInflationDestination() {
    return inflationDestination;
  }

  public String getHomeDomain() {
    return homeDomain;
  }

  public String getSignerKey() {
    return signerKey;
  }

  /**
   * @deprecated Use {@link SetOptionsOperationResponse#getSignerKey()}
   * @return
   */
  public KeyPair getSigner() {
    return KeyPair.fromAccountId(signerKey);
  }

  public Integer getSignerWeight() {
    return signerWeight;
  }

  public Integer getMasterKeyWeight() {
    return masterKeyWeight;
  }

  public String[] getClearFlags() {
    return clearFlags;
  }

  public String[] getSetFlags() {
    return setFlags;
  }
}
