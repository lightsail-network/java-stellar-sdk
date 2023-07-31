package org.stellar.sdk.scval;

import com.google.common.base.Objects;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.StrKey;
import org.stellar.sdk.xdr.Hash;
import org.stellar.sdk.xdr.SCAddress;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/**
 * Represents a single address in the Stellar network. An address can represent an account or a
 * contract.
 */
public class ScvAddress extends Scv {
  private static final SCValType TYPE = SCValType.SCV_ADDRESS;

  private final byte[] key;

  private final AddressType type;

  /**
   * Creates a new {@link ScvAddress} from a Stellar public key or contract ID.
   *
   * @param address the StrKey encoded format of Stellar public key or contract ID.
   */
  public ScvAddress(String address) {
    if (StrKey.isValidStellarAccountId(address)) {
      this.type = AddressType.ACCOUNT;
      this.key = StrKey.decodeStellarAccountId(address);
    } else if (StrKey.isValidContractId(address)) {
      this.type = AddressType.CONTRACT;
      this.key = StrKey.decodeContractId(address);
    } else {
      throw new IllegalArgumentException("Unsupported address type");
    }
  }

  /**
   * Creates a new {@link ScvAddress} from a Stellar public key.
   *
   * @param accountId the byte array of the Stellar public key.
   * @return a new {@link ScvAddress} object from the given Stellar public key.
   */
  public static ScvAddress fromAccount(byte[] accountId) {
    return new ScvAddress(StrKey.encodeStellarAccountId(accountId));
  }

  /**
   * Creates a new {@link ScvAddress} from a Stellar Contract ID.
   *
   * @param contractId the byte array of the Stellar Contract ID.
   * @return a new {@link ScvAddress} object from the given Stellar Contract ID.
   */
  public static ScvAddress fromContract(byte[] contractId) {
    return new ScvAddress(StrKey.encodeContractId(contractId));
  }

  /**
   * Creates a new {@link ScvAddress} from a {@link SCAddress} XDR object.
   *
   * @param scAddress the {@link SCAddress} object to convert
   * @return a new {@link ScvAddress} object from the given XDR object
   */
  public static ScvAddress fromSCAddress(SCAddress scAddress) {
    switch (scAddress.getDiscriminant()) {
      case SC_ADDRESS_TYPE_ACCOUNT:
        return new ScvAddress(StrKey.encodeStellarAccountId(scAddress.getAccountId()));
      case SC_ADDRESS_TYPE_CONTRACT:
        return new ScvAddress(StrKey.encodeContractId(scAddress.getContractId().getHash()));
      default:
        throw new IllegalArgumentException("Unsupported address type");
    }
  }

  /**
   * Creates a new {@link ScvAddress} from a {@link SCVal} XDR object.
   *
   * @param scVal the {@link SCVal} object to convert
   * @return a new {@link ScvAddress} object from the given XDR object
   */
  public static ScvAddress fromSCVal(SCVal scVal) {
    if (!TYPE.equals(scVal.getDiscriminant())) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }
    return ScvAddress.fromSCAddress(scVal.getAddress());
  }

  /**
   * Converts this object to its {@link SCAddress} XDR object representation.
   *
   * @return a new {@link SCAddress} object from this object
   */
  public SCAddress toSCAddress() {
    SCAddress scAddress = new SCAddress();
    switch (this.type) {
      case ACCOUNT:
        scAddress.setDiscriminant(org.stellar.sdk.xdr.SCAddressType.SC_ADDRESS_TYPE_ACCOUNT);
        scAddress.setAccountId(KeyPair.fromPublicKey(this.key).getXdrAccountId());
        break;
      case CONTRACT:
        scAddress.setDiscriminant(org.stellar.sdk.xdr.SCAddressType.SC_ADDRESS_TYPE_CONTRACT);
        scAddress.setContractId(new Hash(this.key));
        break;
      default:
        throw new IllegalArgumentException("Unsupported address type");
    }
    return scAddress;
  }

  /**
   * Converts this object to its {@link SCVal} XDR object representation.
   *
   * @return a new {@link SCVal} object from this object
   */
  public SCVal toSCVal() {
    SCVal scVal = new SCVal();
    scVal.setDiscriminant(TYPE);
    scVal.setAddress(this.toSCAddress());
    return scVal;
  }

  @Override
  public SCValType getSCValType() {
    return TYPE;
  }

  /**
   * Returns the byte array of the Stellar public key or contract ID.
   *
   * @return the byte array of the Stellar public key or contract ID.
   */
  public byte[] getBytes() {
    return key;
  }

  /**
   * Returns the type of this address.
   *
   * @return the type of this address.
   */
  public AddressType getAddressType() {
    return type;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.key, this.type);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ScvAddress)) {
      return false;
    }

    ScvAddress other = (ScvAddress) object;
    return Objects.equal(this.key, other.key) && Objects.equal(this.type, other.type);
  }

  @Override
  public String toString() {
    switch (this.type) {
      case ACCOUNT:
        return StrKey.encodeStellarAccountId(this.key);
      case CONTRACT:
        return StrKey.encodeContractId(this.key);
      default:
        throw new IllegalArgumentException("Unsupported address type");
    }
  }

  /** Represents the type of the address. */
  public enum AddressType {
    ACCOUNT,
    CONTRACT
  }
}
