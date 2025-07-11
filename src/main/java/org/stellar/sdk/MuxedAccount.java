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
      // first 32 bytes are the ed25519 public key
      byte[] ed25519PublicKey = new byte[32];
      System.arraycopy(rawMed25519, 0, ed25519PublicKey, 0, 32);
      // the next 8 bytes are the multiplexing ID, it's an unsigned 64-bit integer
      byte[] muxedIdBytes = new byte[8];
      System.arraycopy(rawMed25519, 32, muxedIdBytes, 0, 8);
      this.accountId = StrKey.encodeEd25519PublicKey(ed25519PublicKey);
      this.muxedId = new BigInteger(1, muxedIdBytes);
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
    return StrKey.encodeMed25519PublicKey(getMuxedEd25519AccountBytes(toXdr().getMed25519()));
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

  private static byte[] getMuxedEd25519AccountBytes(
      org.stellar.sdk.xdr.MuxedAccount.MuxedAccountMed25519 muxedAccountMed25519) {
    byte[] accountBytes = muxedAccountMed25519.getEd25519().getUint256();
    byte[] idBytes = muxedAccountMed25519.getId().getUint64().getNumber().toByteArray();
    byte[] idPaddedBytes = new byte[8];
    int idNumBytesToCopy = Math.min(idBytes.length, 8);
    int idCopyStartIndex = idBytes.length - idNumBytesToCopy;
    System.arraycopy(
        idBytes, idCopyStartIndex, idPaddedBytes, 8 - idNumBytesToCopy, idNumBytesToCopy);
    byte[] result = new byte[accountBytes.length + idPaddedBytes.length];
    System.arraycopy(accountBytes, 0, result, 0, accountBytes.length);
    System.arraycopy(idPaddedBytes, 0, result, accountBytes.length, idPaddedBytes.length);
    return result;
  }
}
