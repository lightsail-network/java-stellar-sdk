package org.stellar.sdk;

import java.io.IOException;
import java.util.Arrays;
import lombok.EqualsAndHashCode;
import org.stellar.sdk.xdr.ClaimableBalanceID;
import org.stellar.sdk.xdr.ClaimableBalanceIDType;
import org.stellar.sdk.xdr.ContractID;
import org.stellar.sdk.xdr.Hash;
import org.stellar.sdk.xdr.MuxedEd25519Account;
import org.stellar.sdk.xdr.PoolID;
import org.stellar.sdk.xdr.SCAddress;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/**
 * Represents a single address in the Stellar network. An address can represent an account,
 * contract, muxed account, claimable balance, or liquidity pool.
 */
@EqualsAndHashCode
public class Address {
  private static final SCValType TYPE = SCValType.SCV_ADDRESS;
  private final byte[] key;

  private final AddressType type;

  /**
   * Creates a new {@link Address} from a Stellar public key (G...), contract ID (C...), med25519
   * public key (M...), liquidity pool ID (L...), or claimable balance ID (B...).
   *
   * @param address the StrKey encoded format of Stellar address.
   */
  public Address(String address) {
    if (StrKey.isValidEd25519PublicKey(address)) {
      this.type = AddressType.ACCOUNT;
      this.key = StrKey.decodeEd25519PublicKey(address);
    } else if (StrKey.isValidContract(address)) {
      this.type = AddressType.CONTRACT;
      this.key = StrKey.decodeContract(address);
    } else if (StrKey.isValidMed25519PublicKey(address)) {
      this.type = AddressType.MUXED_ACCOUNT;
      this.key = StrKey.decodeMed25519PublicKey(address);
    } else if (StrKey.isValidClaimableBalance(address)) {
      this.type = AddressType.CLAIMABLE_BALANCE;
      this.key = StrKey.decodeClaimableBalance(address);
    } else if (StrKey.isValidLiquidityPool(address)) {
      this.type = AddressType.LIQUIDITY_POOL;
      this.key = StrKey.decodeLiquidityPool(address);
    } else {
      throw new IllegalArgumentException("Unsupported address type");
    }
  }

  /**
   * Creates a new {@link Address} from a Stellar public key.
   *
   * @param accountId the byte array of the Stellar public key (G...).
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
   * Creates a new {@link Address} from a Stellar med25519 public key.
   *
   * @param muxedAccountId the byte array of the Stellar med25519 public key (M...).
   * @return a new {@link Address} object from the given Stellar med25519 public key.
   */
  public static Address fromMuxedAccount(byte[] muxedAccountId) {
    return new Address(StrKey.encodeMed25519PublicKey(muxedAccountId));
  }

  /**
   * Creates a new {@link Address} from a Stellar Claimable Balance ID.
   *
   * @param claimableBalanceId the byte array of the Stellar Claimable Balance ID (B...).
   * @return a new {@link Address} object from the given Stellar Claimable Balance ID.
   */
  public static Address fromClaimableBalance(byte[] claimableBalanceId) {
    return new Address(StrKey.encodeClaimableBalance(claimableBalanceId));
  }

  /**
   * Creates a new {@link Address} from a Stellar Liquidity Pool ID.
   *
   * @param liquidityPoolId the byte array of the Stellar Liquidity Pool ID (L...).
   * @return a new {@link Address} object from the given Stellar Liquidity Pool ID.
   */
  public static Address fromLiquidityPool(byte[] liquidityPoolId) {
    return new Address(StrKey.encodeLiquidityPool(liquidityPoolId));
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
        return fromAccount(scAddress.getAccountId().getAccountID().getEd25519().getUint256());
      case SC_ADDRESS_TYPE_CONTRACT:
        return fromContract(scAddress.getContractId().getContractID().getHash());
      case SC_ADDRESS_TYPE_MUXED_ACCOUNT:
        return fromMuxedAccount(
            StrKey.toRawMuxedAccountStrKey(
                new StrKey.RawMuxedAccountStrKey(
                    scAddress.getMuxedAccount().getEd25519(),
                    scAddress.getMuxedAccount().getId())));
      case SC_ADDRESS_TYPE_CLAIMABLE_BALANCE:
        if (scAddress.getClaimableBalanceId().getDiscriminant()
            != ClaimableBalanceIDType.CLAIMABLE_BALANCE_ID_TYPE_V0) {
          throw new IllegalArgumentException(
              "The claimable balance ID type is not supported, it must be `CLAIMABLE_BALANCE_ID_TYPE_V0`.");
        }
        byte[] v0Bytes = scAddress.getClaimableBalanceId().getV0().getHash();
        byte[] withZeroPrefix = new byte[v0Bytes.length + 1];
        withZeroPrefix[0] = 0x00;
        System.arraycopy(v0Bytes, 0, withZeroPrefix, 1, v0Bytes.length);
        return fromClaimableBalance(withZeroPrefix);
      case SC_ADDRESS_TYPE_LIQUIDITY_POOL:
        return fromLiquidityPool(scAddress.getLiquidityPoolId().getPoolID().getHash());
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
      case MUXED_ACCOUNT:
        scAddress.setDiscriminant(org.stellar.sdk.xdr.SCAddressType.SC_ADDRESS_TYPE_MUXED_ACCOUNT);
        StrKey.RawMuxedAccountStrKey rawMuxedAccountStrKey =
            StrKey.fromRawMuxedAccountStrKey(this.key);
        MuxedEd25519Account muxedEd25519Account =
            MuxedEd25519Account.builder()
                .id(rawMuxedAccountStrKey.getId())
                .ed25519(rawMuxedAccountStrKey.getEd25519())
                .build();
        scAddress.setMuxedAccount(muxedEd25519Account);
        break;
      case CLAIMABLE_BALANCE:
        if (this.key[0] != 0x00) {
          throw new IllegalArgumentException(
              "The claimable balance ID type is not supported, it must be `CLAIMABLE_BALANCE_ID_TYPE_V0`.");
        }
        byte[] hashBytes = Arrays.copyOfRange(this.key, 1, this.key.length);
        Hash hash = new Hash(hashBytes);
        ClaimableBalanceID claimableBalanceID =
            ClaimableBalanceID.builder()
                .discriminant(ClaimableBalanceIDType.CLAIMABLE_BALANCE_ID_TYPE_V0)
                .v0(hash)
                .build();
        scAddress.setDiscriminant(
            org.stellar.sdk.xdr.SCAddressType.SC_ADDRESS_TYPE_CLAIMABLE_BALANCE);
        scAddress.setClaimableBalanceId(claimableBalanceID);
        break;
      case LIQUIDITY_POOL:
        scAddress.setDiscriminant(org.stellar.sdk.xdr.SCAddressType.SC_ADDRESS_TYPE_LIQUIDITY_POOL);
        try {
          scAddress.setLiquidityPoolId(PoolID.fromXdrByteArray(this.key));
        } catch (IOException e) {
          throw new IllegalArgumentException("Invalid liquidity pool ID", e);
        }
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

  /**
   * Gets the encoded string representation of this address.
   *
   * @return The StrKey-encoded representation of this address
   * @throws IllegalArgumentException if the address type is unknown
   */
  public String getEncodedAddress() {
    switch (this.type) {
      case ACCOUNT:
        return StrKey.encodeEd25519PublicKey(this.key);
      case CONTRACT:
        return StrKey.encodeContract(this.key);
      case MUXED_ACCOUNT:
        return StrKey.encodeMed25519PublicKey(this.key);
      case CLAIMABLE_BALANCE:
        return StrKey.encodeClaimableBalance(this.key);
      case LIQUIDITY_POOL:
        return StrKey.encodeLiquidityPool(this.key);
      default:
        throw new IllegalArgumentException("Unsupported address type");
    }
  }

  @Override
  public String toString() {
    return getEncodedAddress();
  }

  /** Represents the type of the address. */
  public enum AddressType {
    ACCOUNT,
    CONTRACT,
    MUXED_ACCOUNT,
    CLAIMABLE_BALANCE,
    LIQUIDITY_POOL
  }
}
