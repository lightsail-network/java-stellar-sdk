package org.stellar.sdk;

import org.stellar.sdk.xdr.CryptoKeyType;
import org.stellar.sdk.xdr.MuxedAccount;

public class AccountConverter {
  private final boolean enableMuxed;
  private AccountConverter(boolean enabled) {
    this.enableMuxed = enabled;
  }

  public static AccountConverter enableMuxed() {
    return new AccountConverter(true);
  }

  public static AccountConverter disableMuxed() {
    return new AccountConverter(false);
  }


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

  public String decode(MuxedAccount account) {
    if (this.enableMuxed || account.getDiscriminant().equals(CryptoKeyType.KEY_TYPE_ED25519)) {
      return StrKey.encodeStellarMuxedAccount(account);
    }

    return StrKey.encodeStellarAccountId(StrKey.muxedAccountToAccountId(account));
  }
}
