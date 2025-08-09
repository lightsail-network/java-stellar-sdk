package org.stellar.sdk;

import java.math.BigInteger;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;
import org.stellar.sdk.xdr.CryptoKeyType;
import org.stellar.sdk.xdr.Uint256;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

/**
 * Represents a multiplexed account on Stellar's network.
 *
 * <p>A muxed account is an extension of the regular account that allows multiple entities to share
 * the same ed25519 key pair as their account ID while providing a unique identifier for each
 * entity.
 *
 * <p>A muxed account consists of two parts:
 *
 * <ul>
 *   <li>The ed25519 account ID, which starts with the letter "G".
 *   <li>An optional account multiplexing ID, which is a 64-bit unsigned integer.
 * </ul>
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/learn/encyclopedia/transactions-specialized/pooled-accounts-muxed-accounts-memos#muxed-accounts"
 *     target="_blank">Muxed Accounts</a>
 */
@Getter
@ToString
@EqualsAndHashCode
public class MuxedAccount {

  /** The ed25519 account ID. It starts with the letter "G". */
  @NonNull private final String accountId;

  /** The optional account multiplexing ID. It is a 64-bit unsigned integer. */
  @Nullable private final BigInteger muxedId;

  /**
   * Creates a new muxed account from the given ed25519 account ID and optional multiplexing ID.
   *
   * @param accountId The ed25519 account ID. It must be a valid account ID starting with "G".
   * @param muxedId The optional account multiplexing ID. It can be null if not set.
   * @throws IllegalArgumentException If the provided account ID is invalid.
   */
  public MuxedAccount(@NonNull String accountId, @Nullable BigInteger muxedId) {
    if (!StrKey.isValidEd25519PublicKey(accountId)) {
      throw new IllegalArgumentException("accountId is invalid");
    }
    this.accountId = accountId;
    this.muxedId = muxedId;
  }

  /**
   * Creates a new muxed account from the given muxed account address.
   *
   * @param address The muxed account address. It can be either a regular account ID (starting with
   *     "G") or a muxed account address (starting with "M").
   * @throws IllegalArgumentException If the provided address is invalid.
   */
  public MuxedAccount(@NonNull String address) {
    if (StrKey.isValidEd25519PublicKey(address)) {
      this.accountId = address;
      this.muxedId = null;
    } else if (StrKey.isValidMed25519PublicKey(address)) {
      byte[] rawMed25519 = StrKey.decodeMed25519PublicKey(address);
      StrKey.RawMuxedAccountStrKeyParameter parameter =
          StrKey.fromRawMuxedAccountStrKey(rawMed25519);
      this.accountId = StrKey.encodeEd25519PublicKey(parameter.getEd25519().getUint256());
      this.muxedId = parameter.getId().getUint64().getNumber();
    } else {
      throw new IllegalArgumentException("Invalid address");
    }
  }

  /**
   * Returns the account address representation of this muxed account.
   *
   * @return The account address. It starts with "M" if the multiplexing ID is set, or with "G" if
   *     the multiplexing ID is not set.
   */
  public String getAddress() {
    if (muxedId == null) {
      return accountId;
    }
    org.stellar.sdk.xdr.MuxedAccount.MuxedAccountMed25519 med25519 = toXdr().getMed25519();
    return StrKey.encodeMed25519PublicKey(
        StrKey.toRawMuxedAccountStrKey(
            new StrKey.RawMuxedAccountStrKeyParameter(med25519.getEd25519(), med25519.getId())));
  }

  /**
   * Creates a new muxed account from the given XDR representation.
   *
   * @param xdr The XDR representation of the muxed account.
   * @return A new muxed account instance.
   * @throws IllegalArgumentException If the provided XDR is invalid.
   */
  public static MuxedAccount fromXdr(org.stellar.sdk.xdr.MuxedAccount xdr) {
    switch (xdr.getDiscriminant()) {
      case KEY_TYPE_ED25519:
        return new MuxedAccount(StrKey.encodeEd25519PublicKey(xdr.getEd25519().getUint256()), null);
      case KEY_TYPE_MUXED_ED25519:
        return new MuxedAccount(
            StrKey.encodeEd25519PublicKey(xdr.getMed25519().getEd25519().getUint256()),
            xdr.getMed25519().getId().getUint64().getNumber());
      default:
        throw new IllegalArgumentException("Invalid address");
    }
  }

  /**
   * Returns the XDR representation of this muxed account.
   *
   * @return The XDR representation of the muxed account.
   */
  public org.stellar.sdk.xdr.MuxedAccount toXdr() {
    if (muxedId == null) {
      return new org.stellar.sdk.xdr.MuxedAccount(
          CryptoKeyType.KEY_TYPE_ED25519,
          new Uint256(StrKey.decodeEd25519PublicKey(this.accountId)),
          null);
    } else {
      return new org.stellar.sdk.xdr.MuxedAccount(
          CryptoKeyType.KEY_TYPE_MUXED_ED25519,
          null,
          new org.stellar.sdk.xdr.MuxedAccount.MuxedAccountMed25519(
              new Uint64(new XdrUnsignedHyperInteger(this.muxedId)),
              new Uint256(StrKey.decodeEd25519PublicKey(this.accountId))));
    }
  }
}
