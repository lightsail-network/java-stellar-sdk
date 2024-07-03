package org.stellar.sdk.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collections;
import org.junit.Test;
import org.stellar.sdk.Account;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.Claimant;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Network;
import org.stellar.sdk.Predicate;
import org.stellar.sdk.StrKey;
import org.stellar.sdk.Transaction;
import org.stellar.sdk.TransactionBuilder;
import org.stellar.sdk.TransactionPreconditions;
import org.stellar.sdk.xdr.CryptoKeyType;
import org.stellar.sdk.xdr.MuxedAccount;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

// TODO: refactor the test
public class CreateClaimableBalanceOperationTest {

  @Test
  public void testClaimableBalanceIds() throws IOException {
    String sourceAccount =
        KeyPair.fromSecretSeed("SCZANGBA5YHTNYVVV4C3U252E2B6P6F5T3U6MM63WBSBZATAQI3EBTQ4")
            .getAccountId();
    CreateClaimableBalanceOperation op0 =
        CreateClaimableBalanceOperation.builder()
            .asset(new AssetTypeNative())
            .amount("420")
            .claimants(
                Collections.singletonList(
                    new Claimant(
                        "GCACCFMIWJAHUUASSE2WC7V6VVDLYRLSJYZ3DJEXCG523FSHTNII6KOG",
                        new Predicate.Unconditional())))
            .build();
    Transaction transaction =
        new TransactionBuilder(new Account(sourceAccount, 123l), Network.TESTNET)
            .addOperation(op0)
            .addOperation(BumpSequenceOperation.builder().bumpTo(2).build())
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
        CreateClaimableBalanceOperation.builder()
            .asset(new AssetTypeNative())
            .amount("420")
            .claimants(
                Collections.singletonList(
                    new Claimant(
                        "GCACCFMIWJAHUUASSE2WC7V6VVDLYRLSJYZ3DJEXCG523FSHTNII6KOG",
                        new Predicate.Unconditional())))
            .sourceAccount("GABXJTV7ELEB2TQZKJYEGXBUIG6QODJULKJDI65KZMIZZG2EACJU5EA7")
            .build();

    transaction =
        new TransactionBuilder(new Account(sourceAccount, 123l), Network.TESTNET)
            .addOperation(opWithSourceAccount)
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();

    // operation source account does not affect claimable balance id
    assertEquals(expectedIdIndex0, transaction.getClaimableBalanceId(0));

    transaction =
        new TransactionBuilder(new Account(sourceAccount, 124l), Network.TESTNET)
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
    med.setId(new Uint64(new XdrUnsignedHyperInteger(41l)));
    muxedAccount.setMed25519(med);

    transaction =
        new TransactionBuilder(
                new Account(StrKey.encodeMuxedAccount(muxedAccount), 123l), Network.TESTNET)
            .addOperation(op0)
            .addOperation(BumpSequenceOperation.builder().bumpTo(2).build())
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
          "operation at index 1 is not of type CreateClaimableBalanceOperation: class org.stellar.sdk.operations.BumpSequenceOperation",
          e.getMessage());
    }
  }
}
