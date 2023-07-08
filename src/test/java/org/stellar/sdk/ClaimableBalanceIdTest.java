package org.stellar.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.google.common.collect.Lists;
import java.io.IOException;
import org.junit.Test;
import org.stellar.sdk.xdr.CryptoKeyType;
import org.stellar.sdk.xdr.MuxedAccount;
import org.stellar.sdk.xdr.Uint64;

public class ClaimableBalanceIdTest {

  @Test
  public void testClaimableBalanceIds() throws IOException {
    String sourceAccount =
        KeyPair.fromSecretSeed("SCZANGBA5YHTNYVVV4C3U252E2B6P6F5T3U6MM63WBSBZATAQI3EBTQ4")
            .getAccountId();
    CreateClaimableBalanceOperation op0 =
        new CreateClaimableBalanceOperation.Builder(
                "420",
                new AssetTypeNative(),
                Lists.newArrayList(
                    new Claimant(
                        "GCACCFMIWJAHUUASSE2WC7V6VVDLYRLSJYZ3DJEXCG523FSHTNII6KOG",
                        new Predicate.Unconditional())))
            .build();
    Transaction transaction =
        new TransactionBuilder(
                AccountConverter.enableMuxed(), new Account(sourceAccount, 123l), Network.TESTNET)
            .addOperation(op0)
            .addOperation(new BumpSequenceOperation.Builder(2l).build())
            .addOperation(op0)
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();

    String expectedIdIndex0 =
        "0000000095001252ab3b4d16adbfa5364ce526dfcda03cb2258b827edbb2e0450087be51";
    String expectedIdIndex2 =
        "000000009ce17215cc7280e4f4f31fdff4084c9da90ae8236e157acf09d2ce4bf20ac9fb";
    String expectedIdSeq124 =
        "000000004cadb397acbcd75f56ff7a623470ecd52d8cf7d1e7bae26271405a1b6233bdfa";

    assertEquals(expectedIdIndex0, transaction.getClaimableBalanceId(0));
    // different index changes the claimable balance id
    assertEquals(expectedIdIndex2, transaction.getClaimableBalanceId(2));

    CreateClaimableBalanceOperation opWithSourceAccount =
        new CreateClaimableBalanceOperation.Builder(
                "420",
                new AssetTypeNative(),
                Lists.newArrayList(
                    new Claimant(
                        "GCACCFMIWJAHUUASSE2WC7V6VVDLYRLSJYZ3DJEXCG523FSHTNII6KOG",
                        new Predicate.Unconditional())))
            .setSourceAccount("GABXJTV7ELEB2TQZKJYEGXBUIG6QODJULKJDI65KZMIZZG2EACJU5EA7")
            .build();
    transaction =
        new TransactionBuilder(
                AccountConverter.enableMuxed(), new Account(sourceAccount, 123l), Network.TESTNET)
            .addOperation(opWithSourceAccount)
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();

    // operation source account does not affect claimable balance id
    assertEquals(expectedIdIndex0, transaction.getClaimableBalanceId(0));

    transaction =
        new TransactionBuilder(
                AccountConverter.enableMuxed(), new Account(sourceAccount, 124l), Network.TESTNET)
            .addOperation(opWithSourceAccount)
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
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

    transaction =
        new TransactionBuilder(
                AccountConverter.enableMuxed(),
                new Account(StrKey.encodeStellarMuxedAccount(muxedAccount), 123l),
                Network.TESTNET)
            .addOperation(op0)
            .addOperation(new BumpSequenceOperation.Builder(2l).build())
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();

    // muxed id does not affect claimable balance id
    assertEquals(expectedIdIndex0, transaction.getClaimableBalanceId(0));

    try {
      transaction.getClaimableBalanceId(4);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(
          "index: 4 is outside the bounds of the operations within this transaction",
          e.getMessage());
    }

    try {
      transaction.getClaimableBalanceId(1);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(
          "operation at index 1 is not of type CreateClaimableBalanceOperation: class org.stellar.sdk.BumpSequenceOperation",
          e.getMessage());
    }
  }
}
