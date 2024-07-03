package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.Nullable;
import org.stellar.sdk.AccountFlag;
import org.stellar.sdk.StrKey;
import org.stellar.sdk.xdr.*;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#set-options">SetOptions</a>
 * operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class SetOptionsOperation extends Operation {

  /** Account of the inflation destination. */
  @Nullable private final String inflationDestination;

  /**
   * Indicates which flags to clear. For details about the flags, please refer to the <a
   * href="https://developers.stellar.org/docs/glossary/accounts/" target="_blank">accounts doc</a>.
   * You can also use {@link AccountFlag} enum.
   */
  @Nullable private final Integer clearFlags;

  /**
   * Indicates which flags to set. For details about the flags, please refer to the <a
   * href="https://developers.stellar.org/docs/glossary/accounts/" target="_blank">accounts doc</a>.
   * You can also use {@link AccountFlag} enum.
   */
  @Nullable private final Integer setFlags;

  /** Weight of the master key. */
  @Nullable private final Integer masterKeyWeight;

  /**
   * A number from 0-255 representing the threshold this account sets on all operations it performs
   * that have <a href="https://developers.stellar.org/docs/glossary/multisig/" target="_blank">a
   * low threshold</a>.
   */
  @Nullable private final Integer lowThreshold;

  /**
   * A number from 0-255 representing the threshold this account sets on all operations it performs
   * that have <a href="https://developers.stellar.org/docs/glossary/multisig/" target="_blank">a
   * medium threshold</a>.
   */
  @Nullable private final Integer mediumThreshold;

  /**
   * A number from 0-255 representing the threshold this account sets on all operations it performs
   * that have <a href="https://developers.stellar.org/docs/glossary/multisig/" target="_blank">a
   * high threshold</a>.
   */
  @Nullable private final Integer highThreshold;

  /** The home domain of an account. */
  @Nullable private final String homeDomain;

  /** Additional signer added/removed in this operation. */
  @Nullable private final SignerKey signer;

  /** Additional signer weight. The signer is deleted if the weight is 0. */
  @Nullable private final Integer signerWeight;

  /**
   * Construct a new {@link SetOptionsOperation} object from a {@link SetOptionsOp} XDR object.
   *
   * @param op {@link SetOptionsOp} XDR object
   * @return {@link SetOptionsOperation} object
   */
  public static SetOptionsOperation fromXdr(SetOptionsOp op) {
    SetOptionsOperationBuilder<?, ?> builder = SetOptionsOperation.builder();
    if (op.getInflationDest() != null) {
      builder.inflationDestination(StrKey.encodeEd25519PublicKey(op.getInflationDest()));
    }
    if (op.getClearFlags() != null) {
      builder.clearFlags(op.getClearFlags().getUint32().getNumber().intValue());
    }
    if (op.getSetFlags() != null) {
      builder.setFlags(op.getSetFlags().getUint32().getNumber().intValue());
    }
    if (op.getMasterWeight() != null) {
      builder.masterKeyWeight(op.getMasterWeight().getUint32().getNumber().intValue());
    }
    if (op.getLowThreshold() != null) {
      builder.lowThreshold(op.getLowThreshold().getUint32().getNumber().intValue());
    }
    if (op.getMedThreshold() != null) {
      builder.mediumThreshold(op.getMedThreshold().getUint32().getNumber().intValue());
    }
    if (op.getHighThreshold() != null) {
      builder.highThreshold(op.getHighThreshold().getUint32().getNumber().intValue());
    }
    if (op.getHomeDomain() != null) {
      builder.homeDomain(op.getHomeDomain().getString32().toString());
    }
    if (op.getSigner() != null) {
      builder.signer(op.getSigner().getKey());
      builder.signerWeight(op.getSigner().getWeight().getUint32().getNumber().intValue());
    }
    return builder.build();
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
    SetOptionsOp op = new SetOptionsOp();
    if (inflationDestination != null) {
      op.setInflationDest(StrKey.encodeToXDRAccountId(this.inflationDestination));
    }
    if (clearFlags != null) {
      Uint32 clearFlags = new Uint32();
      clearFlags.setUint32(new XdrUnsignedInteger(this.clearFlags));
      op.setClearFlags(clearFlags);
    }
    if (setFlags != null) {
      Uint32 setFlags = new Uint32();
      setFlags.setUint32(new XdrUnsignedInteger(this.setFlags));
      op.setSetFlags(setFlags);
    }
    if (masterKeyWeight != null) {
      Uint32 uint32 = new Uint32();
      uint32.setUint32(new XdrUnsignedInteger(masterKeyWeight));
      op.setMasterWeight(uint32);
    }
    if (lowThreshold != null) {
      Uint32 uint32 = new Uint32();
      uint32.setUint32(new XdrUnsignedInteger(lowThreshold));
      op.setLowThreshold(uint32);
    }
    if (mediumThreshold != null) {
      Uint32 uint32 = new Uint32();
      uint32.setUint32(new XdrUnsignedInteger(mediumThreshold));
      op.setMedThreshold(uint32);
    }
    if (highThreshold != null) {
      Uint32 uint32 = new Uint32();
      uint32.setUint32(new XdrUnsignedInteger(highThreshold));
      op.setHighThreshold(uint32);
    }
    if (homeDomain != null) {
      String32 homeDomain = new String32();
      homeDomain.setString32(new XdrString(this.homeDomain));
      op.setHomeDomain(homeDomain);
    }
    if (signer != null && signerWeight != null) {
      org.stellar.sdk.xdr.Signer signer = new org.stellar.sdk.xdr.Signer();
      Uint32 weight = new Uint32();
      weight.setUint32(new XdrUnsignedInteger(signerWeight));
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

  private static final class SetOptionsOperationBuilderImpl
      extends SetOptionsOperationBuilder<SetOptionsOperation, SetOptionsOperationBuilderImpl> {
    public SetOptionsOperation build() {
      SetOptionsOperation op = new SetOptionsOperation(this);
      if (op.homeDomain != null && new XdrString(op.homeDomain).getBytes().length > 32) {
        throw new IllegalArgumentException("home domain cannot exceed 32 bytes");
      }
      if (op.signer != null && op.signerWeight == null) {
        throw new IllegalArgumentException("signer weight cannot be null if signer is not null");
      }
      if (op.signer == null && op.signerWeight != null) {
        throw new IllegalArgumentException("signer cannot be null if signer weight is not null");
      }
      if (op.signerWeight != null && (op.signerWeight < 0 || op.signerWeight > 255)) {
        throw new IllegalArgumentException("signer weight must be between 0 and 255");
      }
      return op;
    }
  }
}
