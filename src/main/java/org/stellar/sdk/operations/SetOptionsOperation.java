package org.stellar.sdk.operations;

import com.google.gson.annotations.SerializedName;

import org.stellar.base.Keypair;

/**
 * Represents SetOptions operation response.
 */
public class SetOptionsOperation extends Operation {
  @SerializedName("low_threshold")
  protected final Integer lowThreshold;
  @SerializedName("med_threshold")
  protected final Integer medThreshold;
  @SerializedName("high_threshold")
  protected final Integer highThreshold;
  @SerializedName("inflation_dest")
  protected final Keypair inflationDestination;
  @SerializedName("home_domain")
  protected final String homeDomain;
  @SerializedName("signer_key")
  protected final Keypair signerKey;
  @SerializedName("signer_weight")
  protected final Integer signerWeight;
  @SerializedName("master_key_weight")
  protected final Integer masterKeyWeight;
  @SerializedName("clear_flags_s")
  protected final String[] clearFlags;
  @SerializedName("set_flags_s")
  protected final String[] setFlags;

  SetOptionsOperation(Integer lowThreshold, Integer medThreshold, Integer highThreshold, Keypair inflationDestination, String homeDomain, Keypair signerKey, Integer signerWeight, Integer masterKeyWeight, String[] clearFlags, String[] setFlags) {
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

  public Keypair getInflationDestination() {
    return inflationDestination;
  }

  public String getHomeDomain() {
    return homeDomain;
  }

  public Keypair getSigner() {
    return signerKey;
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
