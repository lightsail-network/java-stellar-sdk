package org.stellar.sdk;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.stellar.sdk.operations.BumpSequenceOperation;
import org.stellar.sdk.operations.CreateAccountOperation;
import org.stellar.sdk.operations.ExtendFootprintTTLOperation;
import org.stellar.sdk.operations.InvokeHostFunctionOperation;
import org.stellar.sdk.operations.PaymentOperation;
import org.stellar.sdk.operations.RestoreFootprintOperation;
import org.stellar.sdk.scval.Scv;
import org.stellar.sdk.xdr.ContractDataDurability;
import org.stellar.sdk.xdr.ContractExecutable;
import org.stellar.sdk.xdr.ContractExecutableType;
import org.stellar.sdk.xdr.ContractIDPreimage;
import org.stellar.sdk.xdr.ContractIDPreimageType;
import org.stellar.sdk.xdr.CreateContractArgs;
import org.stellar.sdk.xdr.DecoratedSignature;
import org.stellar.sdk.xdr.EnvelopeType;
import org.stellar.sdk.xdr.HostFunction;
import org.stellar.sdk.xdr.HostFunctionType;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.LedgerEntryType;
import org.stellar.sdk.xdr.LedgerFootprint;
import org.stellar.sdk.xdr.LedgerKey;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SignerKey;
import org.stellar.sdk.xdr.SorobanResources;
import org.stellar.sdk.xdr.SorobanTransactionData;
import org.stellar.sdk.xdr.Uint256;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

public class TransactionTest {

  @Test
  public void testParseV0Transaction() throws IOException {

    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    Account account = new Account(source.getAccountId(), 2908908335136768L);

    Transaction transaction =
        new Transaction(
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.operations.Operation[] {
              CreateAccountOperation.builder()
                  .destination(destination.getAccountId())
                  .startingBalance(BigDecimal.valueOf(2000))
                  .build()
            },
            null,
            new TransactionPreconditions(
                null, null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>()),
            null,
            Network.PUBLIC);

    transaction.setEnvelopeType(EnvelopeType.ENVELOPE_TYPE_TX_V0);
    transaction.sign(source);

    org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction =
        org.stellar.sdk.xdr.TransactionEnvelope.fromXdrBase64(transaction.toEnvelopeXdrBase64());
    assertEquals(EnvelopeType.ENVELOPE_TYPE_TX_V0, decodedTransaction.getDiscriminant());

    Transaction parsed =
        (Transaction)
            Transaction.fromEnvelopeXdr(transaction.toEnvelopeXdrBase64(), Network.PUBLIC);
    assertEquals(parsed, transaction);
    assertEquals(EnvelopeType.ENVELOPE_TYPE_TX_V0, parsed.toEnvelopeXdr().getDiscriminant());
    assertEquals(transaction.toEnvelopeXdrBase64(), parsed.toEnvelopeXdrBase64());

    assertEquals(
        "AAAAAF7FIiDToW1fOYUFBC0dmyufJbFTOa2GQESGz+S2h5ViAAAAZAAKVaMAAAABAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAAEqBfIAAAAAAAAAAABtoeVYgAAAEDzfR5PgRFim5Wdvq9ImdZNWGBxBWwYkQPa9l5iiBdtPLzAZv6qj+iOfSrqinsoF0XrLkwdIcZQVtp3VRHhRoUE",
        transaction.toEnvelopeXdrBase64());
  }

  @Test
  public void testAddingSignaturesDirectly() {
    KeyPair source =
        KeyPair.fromAccountId("GBBM6BKZPEHWYO3E3YKREDPQXMS4VK35YLNU7NFBRI26RAN7GI5POFBB");
    KeyPair destination =
        KeyPair.fromAccountId("GDJJRRMBK4IWLEPJGIE6SXD2LP7REGZODU7WDC3I2D6MR37F4XSHBKX2");

    Account account = new Account(source.getAccountId(), 0L);

    Transaction transaction =
        new Transaction(
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.operations.Operation[] {
              CreateAccountOperation.builder()
                  .destination(destination.getAccountId())
                  .startingBalance(BigDecimal.valueOf(2000))
                  .build()
            },
            null,
            new TransactionPreconditions(
                null, null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>()),
            null,
            Network.PUBLIC);

    assertEquals(0, transaction.getSignatures().size());
    try {
      // should not be able to change the list of signatures directly
      transaction.getSignatures().add(new DecoratedSignature());
      fail();
    } catch (UnsupportedOperationException ignored) {
    }

    // should only be able to add signatures through interface
    transaction.addSignature(new DecoratedSignature());
    assertEquals(1, transaction.getSignatures().size());
  }

  @Test
  public void testSha256HashSigning() {
    KeyPair source =
        KeyPair.fromAccountId("GBBM6BKZPEHWYO3E3YKREDPQXMS4VK35YLNU7NFBRI26RAN7GI5POFBB");
    KeyPair destination =
        KeyPair.fromAccountId("GDJJRRMBK4IWLEPJGIE6SXD2LP7REGZODU7WDC3I2D6MR37F4XSHBKX2");

    Account account = new Account(source.getAccountId(), 0L);

    Transaction transaction =
        new Transaction(
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.operations.Operation[] {
              PaymentOperation.builder()
                  .destination(destination.getAccountId())
                  .asset(new AssetTypeNative())
                  .amount(BigDecimal.valueOf(2000))
                  .build()
            },
            null,
            new TransactionPreconditions(
                null, null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>()),
            null,
            Network.PUBLIC);

    byte[] preimage = new byte[64];
    new SecureRandom().nextBytes(preimage);
    byte[] hash = Util.hash(preimage);

    transaction.sign(preimage);

    assertArrayEquals(transaction.getSignatures().get(0).getSignature().getSignature(), preimage);
    assertArrayEquals(
        transaction.getSignatures().get(0).getHint().getSignatureHint(),
        Arrays.copyOfRange(hash, hash.length - 4, hash.length));
  }

  @Test
  public void testToBase64EnvelopeXdrBuilderNoSignatures() {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    Account account = new Account(source.getAccountId(), 2908908335136768L);

    Transaction transaction =
        new Transaction(
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.operations.Operation[] {
              CreateAccountOperation.builder()
                  .destination(destination.getAccountId())
                  .startingBalance(BigDecimal.valueOf(2000))
                  .build()
            },
            null,
            new TransactionPreconditions(
                null, null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>()),
            null,
            Network.TESTNET);

    Transaction parsed =
        (Transaction)
            Transaction.fromEnvelopeXdr(transaction.toEnvelopeXdrBase64(), Network.TESTNET);
    assertEquals(parsed, transaction);
    assertEquals(
        "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAO3gUmG83C+VCqO6FztuMtXJF/l7grZA7MjRzqdZ9W8QAAAABKgXyAAAAAAAAAAAAA==",
        transaction.toEnvelopeXdrBase64());
  }

  @Test
  public void testConstructorWithSorobanData() {
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
                    .diskReadBytes(new Uint32(new XdrUnsignedInteger(699)))
                    .writeBytes(new Uint32(new XdrUnsignedInteger(0)))
                    .instructions(new Uint32(new XdrUnsignedInteger(34567)))
                    .build())
            .resourceFee(new Int64(100L))
            .ext(SorobanTransactionData.SorobanTransactionDataExt.builder().discriminant(0).build())
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
            new TransactionPreconditions(
                null, null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>()),
            sorobanData,
            Network.TESTNET);

    Transaction parsed =
        (Transaction)
            Transaction.fromEnvelopeXdr(transaction.toEnvelopeXdrBase64(), Network.TESTNET);
    assertEquals(parsed, transaction);
    String expectedXdr =
        "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAAAAAAAAAAAAQAAAAAAAAAYAAAAAQAAAAAAAAAAAAAAAH8wYjTJienWf2nf2TEZi2APPWzmtkwiQHAftisIgyuHAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAAAfzBiNMmJ6dZ/ad/ZMRmLYA89bOa2TCJAcB+2KwiDK4cAAAAAAACHBwAAArsAAAAAAAAAAAAAAGQAAAAA";
    assertEquals(expectedXdr, transaction.toEnvelopeXdrBase64());
  }

  @Test
  public void testIsSorobanTransactionInvokeHostFunctionOperation() {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

    Account account = new Account(source.getAccountId(), 2908908335136768L);

    String contractId = "CDYUOBUVMZPWIU4GB4XNBAYL6NIHIMYLZFNEXOCDIO33MBJMNPSFBYKU";
    String functionName = "hello";
    List<SCVal> params = Collections.singletonList(Scv.toSymbol("Soroban"));
    InvokeHostFunctionOperation operation =
        InvokeHostFunctionOperation.invokeContractFunctionOperationBuilder(
                contractId, functionName, params)
            .build();

    Transaction transaction =
        new Transaction(
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.operations.Operation[] {operation},
            null,
            new TransactionPreconditions(
                null, null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>()),
            null,
            Network.TESTNET);
    assertTrue(transaction.isSorobanTransaction());
  }

  @Test
  public void testIsSorobanTransactionExtendFootprintTTLOperation() {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    String contractId = "CDYUOBUVMZPWIU4GB4XNBAYL6NIHIMYLZFNEXOCDIO33MBJMNPSFBYKU";
    SorobanTransactionData sorobanData =
        new SorobanDataBuilder()
            .setReadOnly(
                Collections.singletonList(
                    LedgerKey.builder()
                        .discriminant(LedgerEntryType.CONTRACT_DATA)
                        .contractData(
                            LedgerKey.LedgerKeyContractData.builder()
                                .contract(new Address(contractId).toSCAddress())
                                .key(Scv.toLedgerKeyContractInstance())
                                .durability(ContractDataDurability.PERSISTENT)
                                .build())
                        .build()))
            .build();
    ExtendFootprintTTLOperation operation =
        ExtendFootprintTTLOperation.builder().extendTo(4096L).build();
    Transaction transaction =
        new Transaction(
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.operations.Operation[] {operation},
            null,
            new TransactionPreconditions(
                null, null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>()),
            sorobanData,
            Network.TESTNET);
    assertTrue(transaction.isSorobanTransaction());
  }

  @Test
  public void testIsSorobanTransactionRestoreFootprintOperation() {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    String contractId = "CDYUOBUVMZPWIU4GB4XNBAYL6NIHIMYLZFNEXOCDIO33MBJMNPSFBYKU";
    SorobanTransactionData sorobanData =
        new SorobanDataBuilder()
            .setReadOnly(
                Collections.singletonList(
                    LedgerKey.builder()
                        .discriminant(LedgerEntryType.CONTRACT_DATA)
                        .contractData(
                            LedgerKey.LedgerKeyContractData.builder()
                                .contract(new Address(contractId).toSCAddress())
                                .key(Scv.toLedgerKeyContractInstance())
                                .durability(ContractDataDurability.PERSISTENT)
                                .build())
                        .build()))
            .build();
    RestoreFootprintOperation operation = RestoreFootprintOperation.builder().build();
    Transaction transaction =
        new Transaction(
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.operations.Operation[] {operation},
            null,
            new TransactionPreconditions(
                null, null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>()),
            sorobanData,
            Network.TESTNET);
    assertTrue(transaction.isSorobanTransaction());
  }

  @Test
  public void testIsSorobanTransactionMultiOperations() {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

    Account account = new Account(source.getAccountId(), 2908908335136768L);

    String contractId = "CDYUOBUVMZPWIU4GB4XNBAYL6NIHIMYLZFNEXOCDIO33MBJMNPSFBYKU";
    String functionName = "hello";
    List<SCVal> params = Collections.singletonList(Scv.toSymbol("Soroban"));
    InvokeHostFunctionOperation operation =
        InvokeHostFunctionOperation.invokeContractFunctionOperationBuilder(
                contractId, functionName, params)
            .build();

    Transaction transaction =
        new Transaction(
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.operations.Operation[] {operation, operation},
            null,
            new TransactionPreconditions(
                null, null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>()),
            null,
            Network.TESTNET);
    assertFalse(transaction.isSorobanTransaction());
  }

  @Test
  public void testIsSorobanTransactionBumpSequenceOperation() {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    BumpSequenceOperation operation = BumpSequenceOperation.builder().bumpTo(0).build();

    Transaction transaction =
        new Transaction(
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.operations.Operation[] {operation},
            null,
            new TransactionPreconditions(
                null, null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>()),
            null,
            Network.TESTNET);
    assertFalse(transaction.isSorobanTransaction());
  }

  @Test
  public void testHashCodeAndEquals() {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    BumpSequenceOperation operation = BumpSequenceOperation.builder().bumpTo(0).build();

    Transaction transaction1 =
        new Transaction(
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.operations.Operation[] {operation},
            null,
            new TransactionPreconditions(
                null, null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>()),
            null,
            Network.TESTNET);

    // they get different account converters
    Transaction transaction2 =
        new Transaction(
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.operations.Operation[] {operation},
            null,
            new TransactionPreconditions(
                null, null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>()),
            null,
            Network.TESTNET);
    assertEquals(transaction1.hashCode(), transaction2.hashCode());
    assertEquals(transaction1, transaction2);

    // they get different memo
    Transaction transaction3 =
        new Transaction(
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.operations.Operation[] {operation},
            Memo.text("not equal tx"),
            new TransactionPreconditions(
                null, null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>()),
            null,
            Network.TESTNET);
    assertNotEquals(transaction1, transaction3);

    // they get different network
    Transaction transaction4 =
        new Transaction(
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.operations.Operation[] {operation},
            null,
            new TransactionPreconditions(
                null, null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>()),
            null,
            Network.PUBLIC);
    assertNotEquals(transaction1, transaction4);
  }

  @Test
  public void testIntegrationPaymentToContractTransactionWithNativeAsset() throws IOException {
    Asset asset = Asset.createNativeAsset();
    BigDecimal amount1 = new BigDecimal("100.125");
    BigDecimal amount2 = new BigDecimal("120.7891");

    KeyPair kp = KeyPair.random();
    IntegrationUtils.fundAccount(kp.getAccountId());
    IntegrationUtils.createAssetContract(asset, kp);
    String destination = IntegrationUtils.getRandomContractId(kp);
    Server server = new Server(IntegrationUtils.HORIZON_URL);
    TransactionBuilderAccount sourceAccount = server.loadAccount(kp.getAccountId());

    Transaction transaction1 =
        new TransactionBuilder(sourceAccount, IntegrationUtils.NETWORK)
            .setBaseFee(100)
            .setTimeout(300)
            .buildPaymentToContractTransaction(destination, asset, amount1, null);
    transaction1.sign(kp);
    server.submitTransaction(transaction1);
    BigInteger balance1 = IntegrationUtils.getBalanceForContract(destination, asset, kp);
    assertEquals(1001250000L, balance1.longValue());

    Transaction transaction2 =
        new TransactionBuilder(sourceAccount, IntegrationUtils.NETWORK)
            .setBaseFee(100)
            .setTimeout(300)
            .buildPaymentToContractTransaction(destination, asset, amount2, null);
    transaction2.sign(kp);
    server.submitTransaction(transaction2);
    BigInteger balance2 = IntegrationUtils.getBalanceForContract(destination, asset, kp);
    assertEquals(2209141000L, balance2.longValue());

    Transaction transaction3 =
        new TransactionBuilder(sourceAccount, IntegrationUtils.NETWORK)
            .setBaseFee(100)
            .setTimeout(300)
            .buildRestoreAssetBalanceEntryTransaction(destination, asset, null);
    transaction3.sign(kp);
    server.submitTransaction(transaction3);

    server.close();
  }

  @Test
  public void testIntegrationPaymentToContractTransactionWithAlphanum4Asset() throws IOException {
    BigDecimal amount1 = new BigDecimal("100.125");
    BigDecimal amount2 = new BigDecimal("120.7891");

    KeyPair issuerKp = KeyPair.random();
    KeyPair kp = KeyPair.random();
    IntegrationUtils.fundAccount(issuerKp.getAccountId());
    IntegrationUtils.fundAccount(kp.getAccountId());
    Asset asset = Asset.createNonNativeAsset("CAT", issuerKp.getAccountId());
    IntegrationUtils.issueAsset("CAT", issuerKp, kp, new BigDecimal("1000"));
    IntegrationUtils.createAssetContract(asset, kp);
    String destination = IntegrationUtils.getRandomContractId(kp);
    Server server = new Server(IntegrationUtils.HORIZON_URL);
    TransactionBuilderAccount sourceAccount = server.loadAccount(kp.getAccountId());

    Transaction transaction1 =
        new TransactionBuilder(sourceAccount, IntegrationUtils.NETWORK)
            .setBaseFee(100)
            .setTimeout(300)
            .buildPaymentToContractTransaction(destination, asset, amount1, null);
    transaction1.sign(kp);
    server.submitTransaction(transaction1);
    BigInteger balance1 = IntegrationUtils.getBalanceForContract(destination, asset, kp);
    assertEquals(1001250000L, balance1.longValue());

    Transaction transaction2 =
        new TransactionBuilder(sourceAccount, IntegrationUtils.NETWORK)
            .setBaseFee(100)
            .setTimeout(300)
            .buildPaymentToContractTransaction(destination, asset, amount2, null);
    transaction2.sign(kp);
    server.submitTransaction(transaction2);
    BigInteger balance2 = IntegrationUtils.getBalanceForContract(destination, asset, kp);
    assertEquals(2209141000L, balance2.longValue());

    Transaction transaction3 =
        new TransactionBuilder(sourceAccount, IntegrationUtils.NETWORK)
            .setBaseFee(100)
            .setTimeout(300)
            .buildRestoreAssetBalanceEntryTransaction(destination, asset, null);
    transaction3.sign(kp);
    server.submitTransaction(transaction3);

    server.close();
  }

  @Test
  public void testIntegrationPaymentToContractTransactionWithAlphanum12Asset() throws IOException {
    BigDecimal amount1 = new BigDecimal("100.125");
    BigDecimal amount2 = new BigDecimal("120.7891");

    KeyPair issuerKp = KeyPair.random();
    KeyPair kp = KeyPair.random();
    IntegrationUtils.fundAccount(issuerKp.getAccountId());
    IntegrationUtils.fundAccount(kp.getAccountId());
    Asset asset = Asset.createNonNativeAsset("BANANA", issuerKp.getAccountId());
    IntegrationUtils.issueAsset("BANANA", issuerKp, kp, new BigDecimal("1000"));
    IntegrationUtils.createAssetContract(asset, kp);
    String destination = IntegrationUtils.getRandomContractId(kp);
    Server server = new Server(IntegrationUtils.HORIZON_URL);
    TransactionBuilderAccount sourceAccount = server.loadAccount(kp.getAccountId());

    Transaction transaction1 =
        new TransactionBuilder(sourceAccount, IntegrationUtils.NETWORK)
            .setBaseFee(100)
            .setTimeout(300)
            .buildPaymentToContractTransaction(destination, asset, amount1, null);
    transaction1.sign(kp);
    server.submitTransaction(transaction1);
    BigInteger balance1 = IntegrationUtils.getBalanceForContract(destination, asset, kp);
    assertEquals(1001250000L, balance1.longValue());

    Transaction transaction2 =
        new TransactionBuilder(sourceAccount, IntegrationUtils.NETWORK)
            .setBaseFee(100)
            .setTimeout(300)
            .buildPaymentToContractTransaction(destination, asset, amount2, null);
    transaction2.sign(kp);
    server.submitTransaction(transaction2);
    BigInteger balance2 = IntegrationUtils.getBalanceForContract(destination, asset, kp);
    assertEquals(2209141000L, balance2.longValue());

    Transaction transaction3 =
        new TransactionBuilder(sourceAccount, IntegrationUtils.NETWORK)
            .setBaseFee(100)
            .setTimeout(300)
            .buildRestoreAssetBalanceEntryTransaction(destination, asset, null);
    transaction3.sign(kp);
    server.submitTransaction(transaction3);

    server.close();
  }

  @Test
  public void testIntegrationPaymentToContractTransactionWithAssetIssuerAsSender()
      throws IOException {
    BigDecimal amount1 = new BigDecimal("100.125");
    BigDecimal amount2 = new BigDecimal("120.7891");

    KeyPair kp = KeyPair.random();
    IntegrationUtils.fundAccount(kp.getAccountId());
    Asset asset = Asset.createNonNativeAsset("CAT", kp.getAccountId());
    IntegrationUtils.createAssetContract(asset, kp);
    String destination = IntegrationUtils.getRandomContractId(kp);
    Server server = new Server(IntegrationUtils.HORIZON_URL);
    TransactionBuilderAccount sourceAccount = server.loadAccount(kp.getAccountId());

    Transaction transaction1 =
        new TransactionBuilder(sourceAccount, IntegrationUtils.NETWORK)
            .setBaseFee(100)
            .setTimeout(300)
            .buildPaymentToContractTransaction(destination, asset, amount1, null);
    transaction1.sign(kp);
    server.submitTransaction(transaction1);
    BigInteger balance1 = IntegrationUtils.getBalanceForContract(destination, asset, kp);
    assertEquals(1001250000L, balance1.longValue());

    Transaction transaction2 =
        new TransactionBuilder(sourceAccount, IntegrationUtils.NETWORK)
            .setBaseFee(100)
            .setTimeout(300)
            .buildPaymentToContractTransaction(destination, asset, amount2, null);
    transaction2.sign(kp);
    server.submitTransaction(transaction2);
    BigInteger balance2 = IntegrationUtils.getBalanceForContract(destination, asset, kp);
    assertEquals(2209141000L, balance2.longValue());

    Transaction transaction3 =
        new TransactionBuilder(sourceAccount, IntegrationUtils.NETWORK)
            .setBaseFee(100)
            .setTimeout(300)
            .buildRestoreAssetBalanceEntryTransaction(destination, asset, null);
    transaction3.sign(kp);
    server.submitTransaction(transaction3);

    server.close();
  }

  @Test
  public void testIntegrationPaymentToContractTransactionWithDifferentSource() throws IOException {
    Asset asset = Asset.createNativeAsset();
    BigDecimal amount1 = new BigDecimal("100.125");
    BigDecimal amount2 = new BigDecimal("120.7891");

    KeyPair kp = KeyPair.random();
    KeyPair opSource = KeyPair.random();
    IntegrationUtils.fundAccount(kp.getAccountId());
    IntegrationUtils.fundAccount(opSource.getAccountId());
    IntegrationUtils.createAssetContract(asset, kp);
    String destination = IntegrationUtils.getRandomContractId(kp);
    Server server = new Server(IntegrationUtils.HORIZON_URL);
    TransactionBuilderAccount sourceAccount = server.loadAccount(kp.getAccountId());

    Transaction transaction1 =
        new TransactionBuilder(sourceAccount, IntegrationUtils.NETWORK)
            .setBaseFee(100)
            .setTimeout(300)
            .buildPaymentToContractTransaction(
                destination, asset, amount1, opSource.getAccountId());
    transaction1.sign(kp);
    transaction1.sign(opSource);
    server.submitTransaction(transaction1);
    BigInteger balance1 = IntegrationUtils.getBalanceForContract(destination, asset, kp);
    assertEquals(1001250000L, balance1.longValue());

    Transaction transaction2 =
        new TransactionBuilder(sourceAccount, IntegrationUtils.NETWORK)
            .setBaseFee(100)
            .setTimeout(300)
            .buildPaymentToContractTransaction(
                destination, asset, amount2, opSource.getAccountId());
    transaction2.sign(kp);
    transaction2.sign(opSource);
    server.submitTransaction(transaction2);
    BigInteger balance2 = IntegrationUtils.getBalanceForContract(destination, asset, kp);
    assertEquals(2209141000L, balance2.longValue());

    Transaction transaction3 =
        new TransactionBuilder(sourceAccount, IntegrationUtils.NETWORK)
            .setBaseFee(100)
            .setTimeout(300)
            .buildRestoreAssetBalanceEntryTransaction(destination, asset, opSource.getAccountId());
    transaction3.sign(kp);
    transaction3.sign(opSource);
    server.submitTransaction(transaction3);

    server.close();
  }
}
