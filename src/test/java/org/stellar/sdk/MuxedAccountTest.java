package org.stellar.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.math.BigInteger;
import org.junit.Test;
import org.stellar.sdk.exception.StrKeyException;
import org.stellar.sdk.xdr.CryptoKeyType;
import org.stellar.sdk.xdr.Uint256;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

public class MuxedAccountTest {
  @Test
  public void testInitWithMuxedId() {
    String accountId = "GAQAA5L65LSYH7CQ3VTJ7F3HHLGCL3DSLAR2Y47263D56MNNGHSQSTVY";
    BigInteger muxexId = BigInteger.valueOf(1234);
    String muxedAddress = "MAQAA5L65LSYH7CQ3VTJ7F3HHLGCL3DSLAR2Y47263D56MNNGHSQSAAAAAAAAAAE2LP26";

    org.stellar.sdk.xdr.MuxedAccount.MuxedAccountMed25519 med25519 =
        new org.stellar.sdk.xdr.MuxedAccount.MuxedAccountMed25519(
            new Uint64(new XdrUnsignedHyperInteger(muxexId)),
            new Uint256(StrKey.decodeEd25519PublicKey(accountId)));
    org.stellar.sdk.xdr.MuxedAccount muxedAccountXdr =
        new org.stellar.sdk.xdr.MuxedAccount(CryptoKeyType.KEY_TYPE_MUXED_ED25519, null, med25519);

    MuxedAccount muxedAccount = new MuxedAccount(accountId, muxexId);
    assertEquals(muxedAddress, muxedAccount.getAddress());
    assertEquals(muxedAccountXdr, muxedAccount.toXdr());
    assertEquals(muxedAccount, MuxedAccount.fromXdr(muxedAccountXdr));
  }

  @Test
  public void testInitWithoutMuxedId() {
    String accountId = "GAQAA5L65LSYH7CQ3VTJ7F3HHLGCL3DSLAR2Y47263D56MNNGHSQSTVY";
    BigInteger muxedId = null;

    org.stellar.sdk.xdr.MuxedAccount muxedAccountXdr =
        new org.stellar.sdk.xdr.MuxedAccount(
            CryptoKeyType.KEY_TYPE_ED25519,
            new Uint256(StrKey.decodeEd25519PublicKey(accountId)),
            null);

    MuxedAccount muxedAccount = new MuxedAccount(accountId, muxedId);
    assertEquals(muxedAccountXdr, muxedAccount.toXdr());
    assertEquals(muxedAccount, MuxedAccount.fromXdr(muxedAccountXdr));
    assertNull(muxedAccount.getMuxedId());
    assertEquals(accountId, muxedAccount.getAccountId());
  }

  @Test
  public void testFromMuxedAddress() {
    String accountId = "GAQAA5L65LSYH7CQ3VTJ7F3HHLGCL3DSLAR2Y47263D56MNNGHSQSTVY";
    BigInteger muxedId = BigInteger.valueOf(1234);
    String muxedAddress = "MAQAA5L65LSYH7CQ3VTJ7F3HHLGCL3DSLAR2Y47263D56MNNGHSQSAAAAAAAAAAE2LP26";

    MuxedAccount muxedAccount = new MuxedAccount(muxedAddress);
    assertEquals(accountId, muxedAccount.getAccountId());
    assertEquals(muxedId, muxedAccount.getMuxedId());
  }

  @Test
  public void testFromAccountEd25519Account() {
    String accountId = "GAQAA5L65LSYH7CQ3VTJ7F3HHLGCL3DSLAR2Y47263D56MNNGHSQSTVY";
    BigInteger muxedId = null;

    MuxedAccount muxedAccount = new MuxedAccount(accountId);
    assertEquals(accountId, muxedAccount.getAccountId());
    assertEquals(muxedId, muxedAccount.getMuxedId());
  }

  @Test
  public void testFromAccountInvalidAccountRaise() {
    String invalidAccount =
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    assertThrows(StrKeyException.class, () -> new MuxedAccount(invalidAccount));
  }
}
