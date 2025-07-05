package org.stellar.sdk;

import lombok.EqualsAndHashCode;
import org.stellar.sdk.xdr.ContractID;
import org.stellar.sdk.xdr.Hash;
import org.stellar.sdk.xdr.SCAddress;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/**
 * Represents a single address in the Stellar network. An address can represent an account or a
 * contract.
 */
@EqualsAndHashCode
public class Address {
  private static final SCValType TYPE = SCValType.SCV_ADDRESS;
  private final byte[] key;

  private final AddressType type;

  /**
   * Creates a new {@link Address} from a Stellar public key or contract ID.
   *
   * @param address the StrKey encoded format of Stellar public key or contract ID.
   */
  public Address(String address) {
    if (StrKey.isValidEd25519PublicKey(address)) {
      this.type = AddressType.ACCOUNT;
      this.key = StrKey.decodeEd25519PublicKey(address);
    } else if (StrKey.isValidContract(address)) {
      this.type = AddressType.CONTRACT;
      this.key = StrKey.decodeContract(address);
    } else {
      throw new IllegalArgumentException("Unsupported address type");
    }
  }

  /**
   * Creates a new {@link Address} from a Stellar public key.
   *
   * @param accountId the byte array of the Stellar public key.
   * @return a new {@link Address} object from the given Stellar public key.
   */
  public static Address fromAccount(byte[] accountId) {
    return new Address(StrKey.encodeEd25519PublicKey(accountId));
  }

  /**
   * Creates a new {@link Address} from a Stellar Contract ID.
   *
   * @param contractId the byte array of the Stellar Contract ID.
   * @return a new {@link Address} object from the given Stellar Contract ID.
   */
  public static Address fromContract(byte[] contractId) {
    return new Address(StrKey.encodeContract(contractId));
  }

  /**
   * Creates a new {@link Address} from a {@link SCAddress} XDR object.
   *
   * @param scAddress the {@link SCAddress} object to convert
   * @return a new {@link Address} object from the given XDR object
   */
  public static Address fromSCAddress(SCAddress scAddress) {
    switch (scAddress.getDiscriminant()) {
      case SC_ADDRESS_TYPE_ACCOUNT:
        return new Address(
            StrKey.encodeEd25519PublicKey(
                scAddress.getAccountId().getAccountID().getEd25519().getUint256()));
      case SC_ADDRESS_TYPE_CONTRACT:
        return new Address(
            StrKey.encodeContract(scAddress.getContractId().getContractID().getHash()));
      default:
        throw new IllegalArgumentException("Unsupported address type");
    }
  }

  /**
   * Creates a new {@link Address} from a {@link SCVal} XDR object.
   *
   * @param scVal the {@link SCVal} object to convert
   * @return a new {@link Address} object from the given XDR object
   */
  public static Address fromSCVal(SCVal scVal) {
    if (!TYPE.equals(scVal.getDiscriminant())) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }
    return Address.fromSCAddress(scVal.getAddress());
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
        scAddress.setContractId(new ContractID(new Hash(this.key)));
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
  public String toString() {
    switch (this.type) {
      case ACCOUNT:
        return StrKey.encodeEd25519PublicKey(this.key);
      case CONTRACT:
        return StrKey.encodeContract(this.key);
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
