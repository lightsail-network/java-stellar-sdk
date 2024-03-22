package org.stellar.sdk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.stellar.sdk.xdr.CryptoKeyType;
import org.stellar.sdk.xdr.MuxedAccount;

/**
 * AccountConverter is a helper class that can be used to encode and decode muxed accounts.
 *
 * @see <a href="https://stellar.org/blog/developers/muxed-accounts-faq">Muxed Accounts FAQ</a>
 */
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@EqualsAndHashCode
public class AccountConverter {
  private final boolean enableMuxed;

  /** Returns an AccountConverter which supports muxed accounts. */
  public static AccountConverter enableMuxed() {
    return new AccountConverter(true);
  }

  /**
   * Returns an AccountConverter which does not support muxed accounts. When trying to encode or
   * decode a muxed account the AccountConverter will first convert the muxed account into an
   * ED25519 account id.
   */
  public static AccountConverter disableMuxed() {
    return new AccountConverter(false);
  }

  /**
   * Encodes an account string into its XDR MuxedAccount representation.
   *
   * @param account the string representation of an account
   * @return the XDR MuxedAccount representation of an account
   */
  public MuxedAccount encode(String account) {
    MuxedAccount muxed = StrKey.encodeToXDRMuxedAccount(account);

    if (this.enableMuxed || muxed.getDiscriminant().equals(CryptoKeyType.KEY_TYPE_ED25519)) {
      return muxed;
    }

    MuxedAccount.MuxedAccountMed25519 med25519 = muxed.getMed25519();
    muxed.setDiscriminant(CryptoKeyType.KEY_TYPE_ED25519);
    muxed.setEd25519(med25519.getEd25519());
    return muxed;
  }

  /**
   * Decodes an XDR MuxedAccount into its string representation.
   *
   * @param account the XDR MuxedAccount representation of an account
   * @return the string representation of an account
   */
  public String decode(MuxedAccount account) {
    if (this.enableMuxed || account.getDiscriminant().equals(CryptoKeyType.KEY_TYPE_ED25519)) {
      return StrKey.encodeStellarMuxedAccount(account);
    }

    return StrKey.encodeEd25519PublicKey(StrKey.muxedAccountToAccountId(account));
  }
}
