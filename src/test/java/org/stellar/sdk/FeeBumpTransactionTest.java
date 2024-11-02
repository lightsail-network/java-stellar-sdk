package org.stellar.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import org.junit.Test;
import org.stellar.sdk.operations.InvokeHostFunctionOperation;
import org.stellar.sdk.operations.PaymentOperation;
import org.stellar.sdk.xdr.ContractExecutable;
import org.stellar.sdk.xdr.ContractExecutableType;
import org.stellar.sdk.xdr.ContractIDPreimage;
import org.stellar.sdk.xdr.ContractIDPreimageType;
import org.stellar.sdk.xdr.CreateContractArgs;
import org.stellar.sdk.xdr.EnvelopeType;
import org.stellar.sdk.xdr.ExtensionPoint;
import org.stellar.sdk.xdr.HostFunction;
import org.stellar.sdk.xdr.HostFunctionType;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.LedgerEntryType;
import org.stellar.sdk.xdr.LedgerFootprint;
import org.stellar.sdk.xdr.LedgerKey;
import org.stellar.sdk.xdr.SorobanResources;
import org.stellar.sdk.xdr.SorobanTransactionData;
import org.stellar.sdk.xdr.Uint256;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

public class FeeBumpTransactionTest {

  private Transaction createInnerTransaction(long baseFee, Network network) {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    Transaction inner =
        new TransactionBuilder(account, network)
            .addOperation(
                PaymentOperation.builder()
                    .destination("GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ")
                    .asset(new AssetTypeNative())
                    .amount(BigDecimal.valueOf(200))
                    .build())
            .setBaseFee(baseFee)
            .addPreconditions(
                TransactionPreconditions.builder().timeBounds(new TimeBounds(10, 11)).build())
            .build();

    inner.sign(source);
    return inner;
  }

  private Transaction createInnerTransaction() {
    return createInnerTransaction(Transaction.MIN_BASE_FEE, Network.TESTNET);
  }

  private Transaction createInnerTransaction(long baseFee) {
    return createInnerTransaction(baseFee, Network.TESTNET);
  }

  private Transaction createInnerTransaction(Network network) {
    return createInnerTransaction(Transaction.MIN_BASE_FEE, network);
  }

  @Test
  public void testSetBaseFeeBelowNetworkMinimum() {
    Transaction inner = createInnerTransaction();

    try {
      FeeBumpTransaction.createWithBaseFee(
          "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3",
          Transaction.MIN_BASE_FEE - 1,
          inner);
      fail();
    } catch (RuntimeException e) {
      assertEquals("baseFee cannot be smaller than the BASE_FEE (100): 99", e.getMessage());
    }
  }

  @Test
  public void testSetBaseFeeBelowInner() {
    Transaction inner = createInnerTransaction(Transaction.MIN_BASE_FEE + 1);

    try {
      FeeBumpTransaction.createWithBaseFee(
          "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3",
          Transaction.MIN_BASE_FEE,
          inner);
      fail();
    } catch (RuntimeException e) {
      assertEquals(
          "base fee cannot be lower than provided inner transaction base fee", e.getMessage());
    }
  }

  @Test
  public void testSetBaseFeeOverflowsLong() {
    Transaction inner = createInnerTransaction(Transaction.MIN_BASE_FEE + 1);

    try {
      FeeBumpTransaction.createWithBaseFee(
          "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3", Long.MAX_VALUE, inner);
      fail();
    } catch (RuntimeException e) {
      assertEquals("fee overflows 64 bit int", e.getMessage());
    }
  }

  @Test
  public void testSetBaseFeeEqualToInner() {
    Transaction inner = createInnerTransaction();

    FeeBumpTransaction feeBump =
        FeeBumpTransaction.createWithBaseFee(
            "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3",
            Transaction.MIN_BASE_FEE,
            inner);

    assertEquals(Transaction.MIN_BASE_FEE * 2, feeBump.getFee());
  }

  @Test
  public void testHash() {
    Transaction inner = createInnerTransaction();
    assertEquals(
        "2a8ead3351faa7797b284f59027355ddd69c21adb8e4da0b9bb95531f7f32681", inner.hashHex());

    FeeBumpTransaction feeBump =
        FeeBumpTransaction.createWithBaseFee(
            "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3",
            Transaction.MIN_BASE_FEE * 2,
            inner);
    assertEquals(
        "58266712c0c1d1cd98faa0e0159605a361cf2a5ca44ad69650eeb1d27ee62334", feeBump.hashHex());
  }

  @Test
  public void testRoundTripXdr() {
    Transaction inner = createInnerTransaction();

    FeeBumpTransaction feeBump =
        FeeBumpTransaction.createWithBaseFee(
            "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3",
            Transaction.MIN_BASE_FEE * 2,
            inner);

    assertEquals(Transaction.MIN_BASE_FEE * 4, feeBump.getFee());
    assertEquals(
        "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3", feeBump.getFeeSource());
    assertEquals(inner, feeBump.getInnerTransaction());
    assertEquals(0, feeBump.getSignatures().size());

    FeeBumpTransaction fromXdr =
        (FeeBumpTransaction)
            AbstractTransaction.fromEnvelopeXdr(feeBump.toEnvelopeXdrBase64(), Network.TESTNET);

    assertEquals(feeBump, fromXdr);

    KeyPair signer = KeyPair.random();
    feeBump.sign(signer);
    fromXdr =
        (FeeBumpTransaction)
            AbstractTransaction.fromEnvelopeXdr(feeBump.toEnvelopeXdrBase64(), Network.TESTNET);
    assertEquals(feeBump, fromXdr);
    assertEquals(inner, fromXdr.getInnerTransaction());

    assertEquals(1, feeBump.getSignatures().size());
    signer.verify(feeBump.hash(), feeBump.getSignatures().get(0).getSignature().getSignature());
  }

  @Test
  public void testFeeBumpUpgradesInnerToV1() {
    Transaction innerV0 = createInnerTransaction();
    innerV0.setEnvelopeType(EnvelopeType.ENVELOPE_TYPE_TX_V0);

    FeeBumpTransaction feeBump =
        FeeBumpTransaction.createWithBaseFee(
            "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3",
            Transaction.MIN_BASE_FEE * 2,
            innerV0);

    assertEquals(Transaction.MIN_BASE_FEE * 4, feeBump.getFee());
    assertEquals(
        "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3", feeBump.getFeeSource());
    assertEquals(0, feeBump.getSignatures().size());

    assertEquals(EnvelopeType.ENVELOPE_TYPE_TX_V0, innerV0.toEnvelopeXdr().getDiscriminant());
    innerV0.setEnvelopeType(EnvelopeType.ENVELOPE_TYPE_TX);
    assertEquals(innerV0, feeBump.getInnerTransaction());

    FeeBumpTransaction fromXdr =
        (FeeBumpTransaction)
            AbstractTransaction.fromEnvelopeXdr(feeBump.toEnvelopeXdrBase64(), Network.TESTNET);

    assertEquals(feeBump, fromXdr);

    KeyPair signer = KeyPair.random();
    feeBump.sign(signer);
    fromXdr =
        (FeeBumpTransaction)
            AbstractTransaction.fromEnvelopeXdr(feeBump.toEnvelopeXdrBase64(), Network.TESTNET);
    assertEquals(feeBump, fromXdr);
    assertEquals(feeBump.getInnerTransaction(), fromXdr.getInnerTransaction());

    assertEquals(1, feeBump.getSignatures().size());
    signer.verify(feeBump.hash(), feeBump.getSignatures().get(0).getSignature().getSignature());
  }

  @Test
  public void testHashCodeAndEquals() {
    Transaction inner = createInnerTransaction();
    FeeBumpTransaction feeBump0 =
        FeeBumpTransaction.createWithBaseFee(
            "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3",
            Transaction.MIN_BASE_FEE * 2,
            inner);

    // they get different base fee
    FeeBumpTransaction feeBump2 =
        FeeBumpTransaction.createWithBaseFee(
            "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3",
            Transaction.MIN_BASE_FEE * 3,
            createInnerTransaction(Network.PUBLIC));
    System.out.println(feeBump2.toEnvelopeXdr());
    assertNotEquals(feeBump0, feeBump2);

    // they get different network
    FeeBumpTransaction feeBump3 =
        FeeBumpTransaction.createWithBaseFee(
            "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3",
            Transaction.MIN_BASE_FEE * 2,
            createInnerTransaction(Network.PUBLIC));

    assertNotEquals(feeBump0, feeBump3);
  }

  @Test
  public void testCreateWithBaseFee() {
    Transaction inner = createInnerTransaction(300);
    FeeBumpTransaction feeBumpTx =
        FeeBumpTransaction.createWithBaseFee(
            "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3", 400, inner);

    assertEquals(
        feeBumpTx.getFeeSource(), "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3");
    assertEquals(feeBumpTx.getFee(), 800);
    assertEquals(feeBumpTx.getInnerTransaction(), inner);
  }

  @Test
  public void testCreateWithFee() {
    Transaction inner = createInnerTransaction(300);
    FeeBumpTransaction feeBumpTx =
        FeeBumpTransaction.createWithFee(
            "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3", 1100, inner);

    assertEquals(
        feeBumpTx.getFeeSource(), "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3");
    assertEquals(feeBumpTx.getFee(), 1100);
    assertEquals(feeBumpTx.getInnerTransaction(), inner);
  }

  @Test
  public void testCreateWithBaseFeeWithSorobanOp() {
    long sorobanResourceFee = 346546L;
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    LedgerKey ledgerKey =
        LedgerKey.builder()
            .discriminant(LedgerEntryType.ACCOUNT)
            .account(
                LedgerKey.LedgerKeyAccount.builder()
                    .accountID(
                        KeyPair.fromAccountId(
                                "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO")
                            .getXdrAccountId())
                    .build())
            .build();
    SorobanTransactionData sorobanData =
        SorobanTransactionData.builder()
            .resources(
                SorobanResources.builder()
                    .footprint(
                        LedgerFootprint.builder()
                            .readOnly(new LedgerKey[] {ledgerKey})
                            .readWrite(new LedgerKey[] {})
                            .build())
                    .readBytes(new Uint32(new XdrUnsignedInteger(699)))
                    .writeBytes(new Uint32(new XdrUnsignedInteger(0)))
                    .instructions(new Uint32(new XdrUnsignedInteger(34567)))
                    .build())
            .resourceFee(new Int64(sorobanResourceFee))
            .ext(ExtensionPoint.builder().discriminant(0).build())
            .build();

    CreateContractArgs createContractArgs =
        CreateContractArgs.builder()
            .contractIDPreimage(
                ContractIDPreimage.builder()
                    .discriminant(ContractIDPreimageType.CONTRACT_ID_PREIMAGE_FROM_ADDRESS)
                    .fromAddress(
                        ContractIDPreimage.ContractIDPreimageFromAddress.builder()
                            .address(
                                new Address(
                                        "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO")
                                    .toSCAddress())
                            .salt(new Uint256(new byte[32]))
                            .build())
                    .build())
            .executable(
                ContractExecutable.builder()
                    .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_STELLAR_ASSET)
                    .build())
            .build();
    HostFunction hostFunction =
        HostFunction.builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs)
            .build();
    InvokeHostFunctionOperation invokeHostFunctionOperation =
        InvokeHostFunctionOperation.builder().hostFunction(hostFunction).build();
    Transaction transaction =
        new Transaction(
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.operations.Operation[] {invokeHostFunctionOperation},
            null,
            new TransactionPreconditions(null, null, null, BigInteger.ZERO, 0, new ArrayList<>()),
            sorobanData,
            Network.TESTNET);

    FeeBumpTransaction feeBumpTransaction =
        FeeBumpTransaction.createWithBaseFee(
            "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3", 200, transaction);
    assertEquals(
        feeBumpTransaction.getFeeSource(),
        "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3");
    assertEquals(feeBumpTransaction.getFee(), 200 * (1 + 1) + sorobanResourceFee);
    assertEquals(feeBumpTransaction.getInnerTransaction(), transaction);
  }
}
