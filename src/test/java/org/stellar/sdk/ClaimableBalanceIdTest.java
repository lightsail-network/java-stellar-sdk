package org.stellar.sdk;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.stellar.sdk.xdr.CryptoKeyType;
import org.stellar.sdk.xdr.MuxedAccount;
import org.stellar.sdk.xdr.Uint64;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ClaimableBalanceIdTest {


  @Test
  public void testClaimableBalanceIds() throws IOException {
    String sourceAccount = KeyPair.fromSecretSeed("SCZANGBA5YHTNYVVV4C3U252E2B6P6F5T3U6MM63WBSBZATAQI3EBTQ4").getAccountId();
    CreateClaimableBalanceOperation op0 = new CreateClaimableBalanceOperation.Builder(
        "420",
        new AssetTypeNative(),
        Lists.newArrayList(
            new Claimant(
                "GCACCFMIWJAHUUASSE2WC7V6VVDLYRLSJYZ3DJEXCG523FSHTNII6KOG",
                new Predicate.Unconditional()
            )
        )).build();
    Transaction transaction = new Transaction.Builder(AccountConverter.enableMuxed(), new Account(sourceAccount, 123l), Network.TESTNET)
        .addOperation(op0)
        .addOperation(new BumpSequenceOperation.Builder(2l).build())
        .addOperation(op0)
        .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
        .setBaseFee(Transaction.MIN_BASE_FEE)
        .build();

    String expectedIdIndex0 = "0000000095001252AB3B4D16ADBFA5364CE526DFCDA03CB2258B827EDBB2E0450087BE51";
    String expectedIdIndex2 = "000000009CE17215CC7280E4F4F31FDFF4084C9DA90AE8236E157ACF09D2CE4BF20AC9FB";
    String expectedIdSeq124 = "000000004CADB397ACBCD75F56FF7A623470ECD52D8CF7D1E7BAE26271405A1B6233BDFA";

    assertEquals(expectedIdIndex0, transaction.getClaimableBalanceId(0));
    // different index changes the claimable balance id
    assertEquals(expectedIdIndex2, transaction.getClaimableBalanceId(2));

    CreateClaimableBalanceOperation opWithSourceAccount = new CreateClaimableBalanceOperation.Builder(
        "420",
        new AssetTypeNative(),
        Lists.newArrayList(
            new Claimant(
                "GCACCFMIWJAHUUASSE2WC7V6VVDLYRLSJYZ3DJEXCG523FSHTNII6KOG",
                new Predicate.Unconditional()
            )
        )).setSourceAccount("GABXJTV7ELEB2TQZKJYEGXBUIG6QODJULKJDI65KZMIZZG2EACJU5EA7").build();
    transaction = new Transaction.Builder(AccountConverter.enableMuxed(), new Account(sourceAccount, 123l), Network.TESTNET)
        .addOperation(opWithSourceAccount)
        .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
        .setBaseFee(Transaction.MIN_BASE_FEE)
        .build();

    // operation source account does not affect claimable balance id
    assertEquals(expectedIdIndex0, transaction.getClaimableBalanceId(0));

    transaction = new Transaction.Builder(AccountConverter.enableMuxed(), new Account(sourceAccount, 124l), Network.TESTNET)
        .addOperation(opWithSourceAccount)
        .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
        .setBaseFee(Transaction.MIN_BASE_FEE)
        .build();

    // different sequence number changes the claimable balance id
    assertEquals(expectedIdSeq124, transaction.getClaimableBalanceId(0));

    // MuxedAccount muxedAccount = ;
    MuxedAccount muxedAccount = new MuxedAccount();
    muxedAccount.setDiscriminant(CryptoKeyType.KEY_TYPE_MUXED_ED25519);
    MuxedAccount.MuxedAccountMed25519 med = new MuxedAccount.MuxedAccountMed25519();
    med.setEd25519(StrKey.encodeToXDRMuxedAccount(sourceAccount).getEd25519());
    med.setId(new Uint64(41l));
    muxedAccount.setMed25519(med);


    transaction = new Transaction.Builder(AccountConverter.enableMuxed(), new Account(StrKey.encodeStellarMuxedAccount(muxedAccount), 123l), Network.TESTNET)
        .addOperation(op0)
        .addOperation(new BumpSequenceOperation.Builder(2l).build())
        .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
        .setBaseFee(Transaction.MIN_BASE_FEE)
        .build();

    // muxed id does not affect claimable balance id
    assertEquals(expectedIdIndex0, transaction.getClaimableBalanceId(0));

    try {
      transaction.getClaimableBalanceId(4);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("index: 4 is outside the bounds of the operations within this transaction", e.getMessage());
    }

    try {
      transaction.getClaimableBalanceId(1);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("operation at index 1 is not of type CreateClaimableBalanceOperation: class org.stellar.sdk.BumpSequenceOperation", e.getMessage());
    }
  }
}
