package org.stellar.sdk;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.io.BaseEncoding;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.stellar.sdk.scval.Scv;
import org.stellar.sdk.xdr.ContractDataDurability;
import org.stellar.sdk.xdr.ContractExecutable;
import org.stellar.sdk.xdr.ContractExecutableType;
import org.stellar.sdk.xdr.ContractIDPreimage;
import org.stellar.sdk.xdr.ContractIDPreimageType;
import org.stellar.sdk.xdr.CreateContractArgs;
import org.stellar.sdk.xdr.DecoratedSignature;
import org.stellar.sdk.xdr.EnvelopeType;
import org.stellar.sdk.xdr.ExtensionPoint;
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
import org.stellar.sdk.xdr.XdrDataInputStream;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

public class TransactionTest {

  @Test
  public void testParseV0Transaction() throws FormatException, IOException {

    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    Account account = new Account(source.getAccountId(), 2908908335136768L);

    Transaction transaction =
        new Transaction(
            AccountConverter.enableMuxed(),
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.Operation[] {
              new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build()
            },
            null,
            new TransactionPreconditions(
                null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>(), null),
            null,
            Network.PUBLIC);

    transaction.setEnvelopeType(EnvelopeType.ENVELOPE_TYPE_TX_V0);
    transaction.sign(source);

    XdrDataInputStream is =
        new XdrDataInputStream(
            new ByteArrayInputStream(
                BaseEncoding.base64().decode(transaction.toEnvelopeXdrBase64())));
    org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction =
        org.stellar.sdk.xdr.TransactionEnvelope.decode(is);
    assertEquals(EnvelopeType.ENVELOPE_TYPE_TX_V0, decodedTransaction.getDiscriminant());

    Transaction parsed =
        (Transaction)
            Transaction.fromEnvelopeXdr(
                AccountConverter.enableMuxed(), transaction.toEnvelopeXdrBase64(), Network.PUBLIC);
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
            AccountConverter.disableMuxed(),
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.Operation[] {
              new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build()
            },
            null,
            new TransactionPreconditions(
                null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>(), null),
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
  public void testSha256HashSigning() throws FormatException {
    KeyPair source =
        KeyPair.fromAccountId("GBBM6BKZPEHWYO3E3YKREDPQXMS4VK35YLNU7NFBRI26RAN7GI5POFBB");
    KeyPair destination =
        KeyPair.fromAccountId("GDJJRRMBK4IWLEPJGIE6SXD2LP7REGZODU7WDC3I2D6MR37F4XSHBKX2");

    Account account = new Account(source.getAccountId(), 0L);

    Transaction transaction =
        new Transaction(
            AccountConverter.disableMuxed(),
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.Operation[] {
              new PaymentOperation.Builder(
                      destination.getAccountId(), new AssetTypeNative(), "2000")
                  .build()
            },
            null,
            new TransactionPreconditions(
                null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>(), null),
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
  public void testToBase64EnvelopeXdrBuilderNoSignatures() throws FormatException, IOException {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    Account account = new Account(source.getAccountId(), 2908908335136768L);

    Transaction transaction =
        new Transaction(
            AccountConverter.enableMuxed(),
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.Operation[] {
              new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build()
            },
            null,
            new TransactionPreconditions(
                null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>(), null),
            null,
            Network.TESTNET);

    Transaction parsed =
        (Transaction)
            Transaction.fromEnvelopeXdr(
                AccountConverter.enableMuxed(), transaction.toEnvelopeXdrBase64(), Network.TESTNET);
    assertEquals(parsed, transaction);
    assertEquals(
        "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAO3gUmG83C+VCqO6FztuMtXJF/l7grZA7MjRzqdZ9W8QAAAABKgXyAAAAAAAAAAAAA==",
        transaction.toEnvelopeXdrBase64());
  }

  @Test
  public void testConstructorWithSorobanData() throws IOException {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    LedgerKey ledgerKey =
        new LedgerKey.Builder()
            .discriminant(LedgerEntryType.ACCOUNT)
            .account(
                new LedgerKey.LedgerKeyAccount.Builder()
                    .accountID(
                        KeyPair.fromAccountId(
                                "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO")
                            .getXdrAccountId())
                    .build())
            .build();
    SorobanTransactionData sorobanData =
        new SorobanTransactionData.Builder()
            .resources(
                new SorobanResources.Builder()
                    .footprint(
                        new LedgerFootprint.Builder()
                            .readOnly(new LedgerKey[] {ledgerKey})
                            .readWrite(new LedgerKey[] {})
                            .build())
                    .readBytes(new Uint32(new XdrUnsignedInteger(699)))
                    .writeBytes(new Uint32(new XdrUnsignedInteger(0)))
                    .instructions(new Uint32(new XdrUnsignedInteger(34567)))
                    .build())
            .refundableFee(new Int64(100L))
            .ext(new ExtensionPoint.Builder().discriminant(0).build())
            .build();

    CreateContractArgs createContractArgs =
        new CreateContractArgs.Builder()
            .contractIDPreimage(
                new ContractIDPreimage.Builder()
                    .discriminant(ContractIDPreimageType.CONTRACT_ID_PREIMAGE_FROM_ADDRESS)
                    .fromAddress(
                        new ContractIDPreimage.ContractIDPreimageFromAddress.Builder()
                            .address(
                                new Address(
                                        "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO")
                                    .toSCAddress())
                            .salt(new Uint256(new byte[32]))
                            .build())
                    .build())
            .executable(
                new ContractExecutable.Builder()
                    .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_TOKEN)
                    .build())
            .build();
    HostFunction hostFunction =
        new HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs)
            .build();
    InvokeHostFunctionOperation invokeHostFunctionOperation =
        InvokeHostFunctionOperation.builder().hostFunction(hostFunction).build();
    Transaction transaction =
        new Transaction(
            AccountConverter.enableMuxed(),
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.Operation[] {invokeHostFunctionOperation},
            null,
            new TransactionPreconditions(
                null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>(), null),
            sorobanData,
            Network.TESTNET);

    Transaction parsed =
        (Transaction)
            Transaction.fromEnvelopeXdr(
                AccountConverter.enableMuxed(), transaction.toEnvelopeXdrBase64(), Network.TESTNET);
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
            AccountConverter.enableMuxed(),
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.Operation[] {operation},
            null,
            new TransactionPreconditions(
                null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>(), null),
            null,
            Network.TESTNET);
    assertTrue(transaction.isSorobanTransaction());
  }

  @Test
  public void testIsSorobanTransactionBumpFootprintExpirationOperation() {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    String contractId = "CDYUOBUVMZPWIU4GB4XNBAYL6NIHIMYLZFNEXOCDIO33MBJMNPSFBYKU";
    SorobanTransactionData sorobanData =
        new SorobanDataBuilder()
            .setReadOnly(
                Collections.singletonList(
                    new LedgerKey.Builder()
                        .discriminant(LedgerEntryType.CONTRACT_DATA)
                        .contractData(
                            new LedgerKey.LedgerKeyContractData.Builder()
                                .contract(new Address(contractId).toSCAddress())
                                .key(Scv.toLedgerKeyContractInstance())
                                .durability(ContractDataDurability.PERSISTENT)
                                .build())
                        .build()))
            .build();
    BumpFootprintExpirationOperation operation =
        BumpFootprintExpirationOperation.builder().ledgersToExpire(4096L).build();
    Transaction transaction =
        new Transaction(
            AccountConverter.enableMuxed(),
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.Operation[] {operation},
            null,
            new TransactionPreconditions(
                null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>(), null),
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
                    new LedgerKey.Builder()
                        .discriminant(LedgerEntryType.CONTRACT_DATA)
                        .contractData(
                            new LedgerKey.LedgerKeyContractData.Builder()
                                .contract(new Address(contractId).toSCAddress())
                                .key(Scv.toLedgerKeyContractInstance())
                                .durability(ContractDataDurability.PERSISTENT)
                                .build())
                        .build()))
            .build();
    RestoreFootprintOperation operation = RestoreFootprintOperation.builder().build();
    Transaction transaction =
        new Transaction(
            AccountConverter.enableMuxed(),
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.Operation[] {operation},
            null,
            new TransactionPreconditions(
                null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>(), null),
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
            AccountConverter.enableMuxed(),
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.Operation[] {operation, operation},
            null,
            new TransactionPreconditions(
                null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>(), null),
            null,
            Network.TESTNET);
    assertFalse(transaction.isSorobanTransaction());
  }

  @Test
  public void testIsSorobanTransactionBumpSequenceOperation() {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    BumpSequenceOperation operation = new BumpSequenceOperation.Builder(0L).build();

    Transaction transaction =
        new Transaction(
            AccountConverter.enableMuxed(),
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.Operation[] {operation},
            null,
            new TransactionPreconditions(
                null, null, BigInteger.ZERO, 0, new ArrayList<SignerKey>(), null),
            null,
            Network.TESTNET);
    assertFalse(transaction.isSorobanTransaction());
  }
}
