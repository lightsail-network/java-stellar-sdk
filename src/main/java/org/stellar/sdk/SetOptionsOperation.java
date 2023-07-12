package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import org.stellar.sdk.xdr.*;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/start/list-of-operations/#set-options">SetOptions</a>
 * operation.
 *
 * @see <a href="https://developers.stellar.org/docs/start/list-of-operations/">List of
 *     Operations</a>
 */
public class SetOptionsOperation extends Operation {

  private final String inflationDestination;
  private final Integer clearFlags;
  private final Integer setFlags;
  private final Integer masterKeyWeight;
  private final Integer lowThreshold;
  private final Integer mediumThreshold;
  private final Integer highThreshold;
  private final String homeDomain;
  private final SignerKey signer;
  private final Integer signerWeight;

  private SetOptionsOperation(
      String inflationDestination,
      Integer clearFlags,
      Integer setFlags,
      Integer masterKeyWeight,
      Integer lowThreshold,
      Integer mediumThreshold,
      Integer highThreshold,
      String homeDomain,
      SignerKey signer,
      Integer signerWeight) {
    this.inflationDestination = inflationDestination;
    this.clearFlags = clearFlags;
    this.setFlags = setFlags;
    this.masterKeyWeight = masterKeyWeight;
    this.lowThreshold = lowThreshold;
    this.mediumThreshold = mediumThreshold;
    this.highThreshold = highThreshold;
    this.homeDomain = homeDomain;
    this.signer = signer;
    this.signerWeight = signerWeight;

    if (this.homeDomain != null && new XdrString(this.homeDomain).getBytes().length > 32) {
      throw new IllegalArgumentException("home domain cannot exceed 32 bytes");
    }
  }

  /** Account of the inflation destination. */
  public String getInflationDestination() {
    return inflationDestination;
  }

  /**
   * Indicates which flags to clear. For details about the flags, please refer to the <a
   * href="https://developers.stellar.org/docs/glossary/accounts/" target="_blank">accounts doc</a>.
   * You can also use {@link AccountFlag} enum.
   */
  public Integer getClearFlags() {
    return clearFlags;
  }

  /**
   * Indicates which flags to set. For details about the flags, please refer to the <a
   * href="https://developers.stellar.org/docs/glossary/accounts/" target="_blank">accounts doc</a>.
   * You can also use {@link AccountFlag} enum.
   */
  public Integer getSetFlags() {
    return setFlags;
  }

  /** Weight of the master key. */
  public Integer getMasterKeyWeight() {
    return masterKeyWeight;
  }

  /**
   * A number from 0-255 representing the threshold this account sets on all operations it performs
   * that have <a href="https://developers.stellar.org/docs/glossary/multisig/" target="_blank">a
   * low threshold</a>.
   */
  public Integer getLowThreshold() {
    return lowThreshold;
  }

  /**
   * A number from 0-255 representing the threshold this account sets on all operations it performs
   * that have <a href="https://developers.stellar.org/docs/glossary/multisig/" target="_blank">a
   * medium threshold</a>.
   */
  public Integer getMediumThreshold() {
    return mediumThreshold;
  }

  /**
   * A number from 0-255 representing the threshold this account sets on all operations it performs
   * that have <a href="https://developers.stellar.org/docs/glossary/multisig/" target="_blank">a
   * high threshold</a>.
   */
  public Integer getHighThreshold() {
    return highThreshold;
  }

  /** The home domain of an account. */
  public String getHomeDomain() {
    return homeDomain;
  }

  /** Additional signer added/removed in this operation. */
  public SignerKey getSigner() {
    return signer;
  }

  /** Additional signer weight. The signer is deleted if the weight is 0. */
  public Integer getSignerWeight() {
    return signerWeight;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    SetOptionsOp op = new SetOptionsOp();
    if (inflationDestination != null) {
      op.setInflationDest(StrKey.encodeToXDRAccountId(this.inflationDestination));
    }
    if (clearFlags != null) {
      Uint32 clearFlags = new Uint32();
      clearFlags.setUint32(this.clearFlags);
      op.setClearFlags(clearFlags);
    }
    if (setFlags != null) {
      Uint32 setFlags = new Uint32();
      setFlags.setUint32(this.setFlags);
      op.setSetFlags(setFlags);
    }
    if (masterKeyWeight != null) {
      Uint32 uint32 = new Uint32();
      uint32.setUint32(masterKeyWeight);
      op.setMasterWeight(uint32);
    }
    if (lowThreshold != null) {
      Uint32 uint32 = new Uint32();
      uint32.setUint32(lowThreshold);
      op.setLowThreshold(uint32);
    }
    if (mediumThreshold != null) {
      Uint32 uint32 = new Uint32();
      uint32.setUint32(mediumThreshold);
      op.setMedThreshold(uint32);
    }
    if (highThreshold != null) {
      Uint32 uint32 = new Uint32();
      uint32.setUint32(highThreshold);
      op.setHighThreshold(uint32);
    }
    if (homeDomain != null) {
      String32 homeDomain = new String32();
      homeDomain.setString32(new XdrString(this.homeDomain));
      op.setHomeDomain(homeDomain);
    }
    if (signer != null) {
      org.stellar.sdk.xdr.Signer signer = new org.stellar.sdk.xdr.Signer();
      Uint32 weight = new Uint32();
      weight.setUint32(signerWeight & 0xFF);
      signer.setKey(this.signer);
      signer.setWeight(weight);
      op.setSigner(signer);
    }

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.SET_OPTIONS);
    body.setSetOptionsOp(op);
    return body;
  }

  /**
   * Builds SetOptions operation.
   *
   * @see SetOptionsOperation
   */
  public static class Builder {
    private String inflationDestination;
    private Integer clearFlags;
    private Integer setFlags;
    private Integer masterKeyWeight;
    private Integer lowThreshold;
    private Integer mediumThreshold;
    private Integer highThreshold;
    private String homeDomain;
    private SignerKey signer;
    private Integer signerWeight;
    private String sourceAccount;

    Builder(SetOptionsOp op) {
      if (op.getInflationDest() != null) {
        inflationDestination = StrKey.encodeStellarAccountId(op.getInflationDest());
      }
      if (op.getClearFlags() != null) {
        clearFlags = op.getClearFlags().getUint32();
      }
      if (op.getSetFlags() != null) {
        setFlags = op.getSetFlags().getUint32();
      }
      if (op.getMasterWeight() != null) {
        masterKeyWeight = op.getMasterWeight().getUint32();
      }
      if (op.getLowThreshold() != null) {
        lowThreshold = op.getLowThreshold().getUint32();
      }
      if (op.getMedThreshold() != null) {
        mediumThreshold = op.getMedThreshold().getUint32();
      }
      if (op.getHighThreshold() != null) {
        highThreshold = op.getHighThreshold().getUint32();
      }
      if (op.getHomeDomain() != null) {
        homeDomain = op.getHomeDomain().getString32().toString();
      }
      if (op.getSigner() != null) {
        signer = op.getSigner().getKey();
        signerWeight = op.getSigner().getWeight().getUint32() & 0xFF;
      }
    }

    /** Creates a new SetOptionsOperation builder. */
    public Builder() {}

    /**
     * Sets the inflation destination for the account.
     *
     * @param inflationDestination The inflation destination account.
     * @return Builder object so you can chain methods.
     */
    public Builder setInflationDestination(String inflationDestination) {
      this.inflationDestination = inflationDestination;
      return this;
    }

    /**
     * Clears the given flags from the account.
     *
     * @param clearFlags For details about the flags, please refer to the <a
     *     href="https://developers.stellar.org/docs/glossary/accounts/" target="_blank">accounts
     *     doc</a>.
     * @return Builder object so you can chain methods.
     */
    public Builder setClearFlags(int clearFlags) {
      this.clearFlags = clearFlags;
      return this;
    }

    /**
     * Sets the given flags on the account.
     *
     * @param setFlags For details about the flags, please refer to the <a
     *     href="https://developers.stellar.org/docs/glossary/accounts/" target="_blank">accounts
     *     doc</a>.
     * @return Builder object so you can chain methods.
     */
    public Builder setSetFlags(int setFlags) {
      this.setFlags = setFlags;
      return this;
    }

    /**
     * Weight of the master key.
     *
     * @param masterKeyWeight Number between 0 and 255
     * @return Builder object so you can chain methods.
     */
    public Builder setMasterKeyWeight(int masterKeyWeight) {
      this.masterKeyWeight = masterKeyWeight;
      return this;
    }

    /**
     * A number from 0-255 representing the threshold this account sets on all operations it
     * performs that have a low threshold.
     *
     * @param lowThreshold Number between 0 and 255
     * @return Builder object so you can chain methods.
     */
    public Builder setLowThreshold(int lowThreshold) {
      this.lowThreshold = lowThreshold;
      return this;
    }

    /**
     * A number from 0-255 representing the threshold this account sets on all operations it
     * performs that have a medium threshold.
     *
     * @param mediumThreshold Number between 0 and 255
     * @return Builder object so you can chain methods.
     */
    public Builder setMediumThreshold(int mediumThreshold) {
      this.mediumThreshold = mediumThreshold;
      return this;
    }

    /**
     * A number from 0-255 representing the threshold this account sets on all operations it
     * performs that have a high threshold.
     *
     * @param highThreshold Number between 0 and 255
     * @return Builder object so you can chain methods.
     */
    public Builder setHighThreshold(int highThreshold) {
      this.highThreshold = highThreshold;
      return this;
    }

    /**
     * Sets the account's home domain address used in <a
     * href="https://developers.stellar.org/docs/glossary/federation/"
     * target="_blank">Federation</a>.
     *
     * @param homeDomain A string of the address which can be up to 32 characters.
     * @return Builder object so you can chain methods.
     */
    public Builder setHomeDomain(String homeDomain) {
      if (homeDomain.length() > 32) {
        throw new IllegalArgumentException("Home domain must be <= 32 characters");
      }
      this.homeDomain = homeDomain;
      return this;
    }

    /**
     * Add, update, or remove a signer from the account. Signer is deleted if the weight = 0;
     *
     * @param signer The signer key. Use {@link org.stellar.sdk.Signer} helper to create this
     *     object.
     * @param weight The weight to attach to the signer (0-255).
     * @return Builder object so you can chain methods.
     */
    public Builder setSigner(SignerKey signer, Integer weight) {
      checkNotNull(signer, "signer cannot be null");
      checkNotNull(weight, "weight cannot be null");
      this.signer = signer;
      signerWeight = weight & 0xFF;
      return this;
    }

    /**
     * Sets the source account for this operation.
     *
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public Builder setSourceAccount(String sourceAccount) {
      this.sourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public SetOptionsOperation build() {
      SetOptionsOperation operation =
          new SetOptionsOperation(
              inflationDestination,
              clearFlags,
              setFlags,
              masterKeyWeight,
              lowThreshold,
              mediumThreshold,
              highThreshold,
              homeDomain,
              signer,
              signerWeight);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(
        this.getSourceAccount(),
        this.inflationDestination,
        this.clearFlags,
        this.setFlags,
        this.masterKeyWeight,
        this.lowThreshold,
        this.mediumThreshold,
        this.highThreshold,
        this.homeDomain,
        this.signer,
        this.signerWeight);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SetOptionsOperation)) {
      return false;
    }

    SetOptionsOperation other = (SetOptionsOperation) object;
    return Objects.equal(this.getSourceAccount(), other.getSourceAccount())
        && Objects.equal(this.inflationDestination, other.inflationDestination)
        && Objects.equal(this.clearFlags, other.clearFlags)
        && Objects.equal(this.setFlags, other.setFlags)
        && Objects.equal(this.masterKeyWeight, other.masterKeyWeight)
        && Objects.equal(this.lowThreshold, other.lowThreshold)
        && Objects.equal(this.mediumThreshold, other.mediumThreshold)
        && Objects.equal(this.highThreshold, other.highThreshold)
        && Objects.equal(this.homeDomain, other.homeDomain)
        && Objects.equal(this.signer, other.signer)
        && Objects.equal(this.signerWeight, other.signerWeight);
  }
}
