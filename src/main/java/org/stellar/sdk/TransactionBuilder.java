package org.stellar.sdk;

import static org.stellar.sdk.TransactionPreconditions.TIMEOUT_INFINITE;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import org.stellar.sdk.operations.InvokeHostFunctionOperation;
import org.stellar.sdk.operations.Operation;
import org.stellar.sdk.operations.RestoreFootprintOperation;
import org.stellar.sdk.scval.Scv;
import org.stellar.sdk.xdr.*;

/** Builds a new Transaction object. */
public class TransactionBuilder {
  @Getter private final TransactionBuilderAccount sourceAccount;
  private Memo memo;
  private final List<Operation> operations;
  @Getter private Long baseFee;
  @Getter private final Network network;
  @NonNull private TransactionPreconditions preconditions;
  private SorobanTransactionData sorobanData;
  private BigInteger txTimeout;

  /**
   * Construct a new transaction builder.
   *
   * @param sourceAccount the source account for this transaction. This account is the account who
   *     will use a sequence number. When build() is called, the account object's sequence number
   *     will be incremented.
   * @param network the testnet or pubnet network to use
   */
  public TransactionBuilder(
      @NonNull TransactionBuilderAccount sourceAccount, @NonNull Network network) {
    this.sourceAccount = sourceAccount;
    this.network = network;
    operations = new ArrayList<>();
    preconditions = TransactionPreconditions.builder().build();
  }

  public int getOperationsCount() {
    return operations.size();
  }

  /**
   * Adds a new <a
   * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations"
   * target="_blank">operation</a> to this transaction.
   *
   * @param operation the operation to add
   * @return Builder object so you can chain methods.
   * @see Operation
   */
  public TransactionBuilder addOperation(@NonNull Operation operation) {
    operations.add(operation);
    return this;
  }

  /**
   * Adds <a
   * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations"
   * target="_blank">operation</a> to this transaction.
   *
   * @param operations list of operations
   * @return Builder object so you can chain methods.
   * @see Operation
   */
  public TransactionBuilder addOperations(@NonNull Collection<Operation> operations) {
    this.operations.addAll(operations);
    return this;
  }

  /**
   * Adds preconditions. For details of all preconditions on transaction refer to <a
   * href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0021.md#specification">CAP-21</a>
   *
   * @param preconditions the tx PreConditions
   * @return updated Builder object
   */
  public TransactionBuilder addPreconditions(@NonNull TransactionPreconditions preconditions) {
    this.preconditions = preconditions;
    return this;
  }

  /**
   * Adds a <a href="https://developers.stellar.org/docs/glossary/transactions/#memo"
   * target="_blank">memo</a> to this transaction.
   *
   * @param memo memo to add
   * @return Builder object so you can chain methods.
   * @see Memo
   */
  public TransactionBuilder addMemo(@NonNull Memo memo) {
    if (this.memo != null) {
      throw new IllegalStateException("Memo has been already added.");
    }
    this.memo = memo;
    return this;
  }

  /**
   * Because of the distributed nature of the Stellar network it is possible that the status of your
   * transaction will be determined after a long time if the network is highly congested. If you
   * want to be sure to receive the status of the transaction within a given period you should set
   * the {@link TimeBounds} with <code>maxTime</code> on the transaction (this is what <code>
   * setTimeout</code> does internally; if there's <code>minTime</code> set but no <code>maxTime
   * </code> it will be added). Call to <code>Builder.setTimeout</code> is required if Transaction
   * does not have <code>max_time</code> set. If you don't want to set timeout, use <code>
   * TIMEOUT_INFINITE</code>. In general, you should set <code>TIMEOUT_INFINITE</code> only in smart
   * contracts. Please note that Horizon may still return <code>504 Gateway Timeout</code> error,
   * even for short timeouts. In such case you need to resubmit the same transaction again without
   * making any changes to receive a status. This method is using the machine system time (UTC),
   * make sure it is set correctly.
   *
   * @param timeout Timeout in seconds.
   * @return updated Builder
   * @see TimeBounds
   */
  public TransactionBuilder setTimeout(BigInteger timeout) {
    if (timeout.compareTo(BigInteger.ZERO) < 0) {
      throw new IllegalArgumentException("timeout cannot be negative");
    }

    txTimeout = timeout;
    return this;
  }

  /**
   * An alias for {@link #setTimeout(BigInteger)} with <code>timeout</code> in seconds.
   *
   * @param timeout Timeout in seconds.
   * @return updated Builder
   */
  public TransactionBuilder setTimeout(long timeout) {
    return setTimeout(BigInteger.valueOf(timeout));
  }

  public TransactionBuilder setBaseFee(long baseFee) {
    if (baseFee < AbstractTransaction.MIN_BASE_FEE) {
      throw new IllegalArgumentException(
          "baseFee cannot be smaller than the BASE_FEE ("
              + AbstractTransaction.MIN_BASE_FEE
              + "): "
              + baseFee);
    }

    this.baseFee = baseFee;
    return this;
  }

  /**
   * Builds a transaction and increments the sequence number on the source account after transaction
   * is constructed.
   */
  public Transaction build() {
    // ensure that the preconditions are valid
    if (preconditions.getTimeBounds() != null && txTimeout != null) {
      throw new IllegalStateException(
          "Can not set both TransactionPreconditions.timeBounds and timeout.");
    }

    TransactionPreconditions txPreconditions;

    if (txTimeout != null) {
      BigInteger maxTime =
          !TIMEOUT_INFINITE.equals(txTimeout)
              ? txTimeout.add(BigInteger.valueOf(System.currentTimeMillis() / 1000L))
              : TIMEOUT_INFINITE;
      txPreconditions =
          preconditions.toBuilder().timeBounds(new TimeBounds(BigInteger.ZERO, maxTime)).build();
    } else {
      txPreconditions = preconditions;
    }

    txPreconditions.validate();

    if (baseFee == null) {
      throw new IllegalStateException("baseFee has to be set. you must call setBaseFee().");
    }

    if (network == null) {
      throw new IllegalStateException("network has to be set. you must call setNetwork().");
    }

    long sequenceNumber = sourceAccount.getIncrementedSequenceNumber();
    Operation[] operations = new Operation[this.operations.size()];
    operations = this.operations.toArray(operations);

    long fee = operations.length * baseFee;
    if (sorobanData != null) {
      fee += sorobanData.getResourceFee().getInt64();
    }

    Transaction transaction =
        new Transaction(
            sourceAccount.getAccountId(),
            fee,
            sequenceNumber,
            operations,
            memo,
            txPreconditions,
            sorobanData,
            network);
    sourceAccount.setSequenceNumber(sequenceNumber);
    return transaction;
  }

  /**
   * Sets the transaction's internal Soroban transaction data (resources, footprint, etc.).
   *
   * <p>For non-contract(non-Soroban) transactions, this setting has no effect. In the case of
   * Soroban transactions, this is either an instance of {@link SorobanTransactionData} or a
   * base64-encoded string of said structure. This is usually obtained from the simulation response
   * based on a transaction with a Soroban operation (e.g. {@link
   * org.stellar.sdk.operations.InvokeHostFunctionOperation}, providing necessary resource and
   * storage footprint estimations for contract invocation.
   *
   * @param sorobanData Soroban data to set
   * @return Builder object so you can chain methods.
   */
  public TransactionBuilder setSorobanData(SorobanTransactionData sorobanData) {
    this.sorobanData = new SorobanDataBuilder(sorobanData).build();
    return this;
  }

  /**
   * Sets the transaction's internal Soroban transaction data (resources, footprint, etc.).
   *
   * <p>For non-contract(non-Soroban) transactions, this setting has no effect. In the case of
   * Soroban transactions, this is either an instance of {@link SorobanTransactionData} or a
   * base64-encoded string of said structure. This is usually obtained from the simulation response
   * based on a transaction with a Soroban operation (e.g. {@link
   * org.stellar.sdk.operations.InvokeHostFunctionOperation}, providing necessary resource and
   * storage footprint estimations for contract invocation.
   *
   * @param sorobanData Soroban data to set
   * @return Builder object so you can chain methods.
   */
  public TransactionBuilder setSorobanData(String sorobanData) {
    this.sorobanData = new SorobanDataBuilder(sorobanData).build();
    return this;
  }

  /**
   * An alias for {@link #buildRestoreAssetBalanceEntryTransaction(String, Asset,
   * SorobanDataBuilder.Resources, Long, String)} with {@code resources} set to {@code new
   * SorobanDataBuilder.Resources(400_000L, 1_000L, 1_000L)} and {@code resourceFee} set to {@code
   * 5_000_000L}.
   *
   * @param destination The contract to send the assets to. (starting with 'C')
   * @param asset The asset to send.
   * @param amount The amount of the asset to send.
   * @param source The source account for the transaction.
   * @return The transaction.
   */
  public Transaction buildPaymentToContractTransaction(
      String destination, Asset asset, BigDecimal amount, @Nullable String source) {
    SorobanDataBuilder.Resources resources =
        new SorobanDataBuilder.Resources(400_000L, 1_000L, 1_000L);
    return buildPaymentToContractTransaction(
        destination, asset, amount, resources, 5_000_000L, source);
  }

  /**
   * Builds a transaction to send asset to a contract.
   *
   * <p>The original intention of this interface design is to send assets to the contract account
   * when the Stellar RPC server is inaccessible. Without Stellar RPC, we cannot accurately estimate
   * the required resources, so we have preset some values that may be slightly higher than the
   * actual resource consumption.
   *
   * <p>If you encounter the {@code entry_archived} error when submitting this transaction, you
   * should consider calling the {@link #buildRestoreAssetBalanceEntryTransaction(String, Asset,
   * SorobanDataBuilder.Resources, Long, String)} method to restore the entry, and then use the
   * method to send assets again.
   *
   * <p><b>Note:</b>
   *
   * <ol>
   *   <li>This method should only be used to send assets to a contract (starting with 'C'). For
   *       sending assets to regular account addresses (starting with 'G'), please use the {@link
   *       org.stellar.sdk.operations.PaymentOperation}.
   *   <li>This method is suitable for sending assets to a contract account when you don't have
   *       access to a Stellar RPC server. If you have access to a Stellar RPC server, it is
   *       recommended to use the {@link org.stellar.sdk.contract.ContractClient} to build
   *       transactions for sending tokens to contracts.
   *   <li>This method may consume slightly more transaction fee than actually required. Under
   *       Protocol 22, if the destination is receiving the asset for the first time, it will
   *       actually consume about 0.25 XLM in resource fees; if it has received the asset before, it
   *       will actually consume about 0.006 XLM in resource fees.
   * </ol>
   *
   * @param destination The contract to send the assets to. (starting with 'C')
   * @param asset The asset to send.
   * @param amount The amount of the asset to send.
   * @param resources The resources required for the transaction.
   * @param resourceFee The maximum fee (in stroops) that can be paid for the transaction.
   * @param source The source account for the transaction.
   * @return The transaction.
   */
  public Transaction buildPaymentToContractTransaction(
      String destination,
      Asset asset,
      BigDecimal amount,
      SorobanDataBuilder.Resources resources,
      Long resourceFee,
      @Nullable String source) {
    if (!StrKey.isValidContract(destination)) {
      throw new IllegalArgumentException("destination is not a valid contract address");
    }

    String fromAddress;
    if (source != null) {
      fromAddress = new MuxedAccount(source).getAccountId();
    } else {
      fromAddress = new MuxedAccount(this.sourceAccount.getAccountId()).getAccountId();
    }

    String contractId = asset.getContractId(this.network);
    String functionName = "transfer";
    BigInteger tokenAmount =
        BigInteger.valueOf(Operation.toXdrAmount(Operation.formatAmountScale(amount)));
    List<SCVal> parameters =
        Arrays.asList(
            Scv.toAddress(fromAddress), Scv.toAddress(destination), Scv.toInt128(tokenAmount));

    SorobanCredentials credentials =
        SorobanCredentials.builder()
            .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_SOURCE_ACCOUNT)
            .build();
    SorobanAuthorizedInvocation rootInvocation =
        SorobanAuthorizedInvocation.builder()
            .function(
                SorobanAuthorizedFunction.builder()
                    .discriminant(
                        SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
                    .contractFn(
                        InvokeContractArgs.builder()
                            .contractAddress(new Address(contractId).toSCAddress())
                            .functionName(Scv.toSymbol(functionName).getSym())
                            .args(parameters.toArray(new SCVal[0]))
                            .build())
                    .build())
            .subInvocations(new SorobanAuthorizedInvocation[0])
            .build();

    SorobanAuthorizationEntry auth =
        SorobanAuthorizationEntry.builder()
            .credentials(credentials)
            .rootInvocation(rootInvocation)
            .build();
    InvokeHostFunctionOperation op =
        InvokeHostFunctionOperation.invokeContractFunctionOperationBuilder(
                contractId, functionName, parameters)
            .auth(Collections.singletonList(auth))
            .sourceAccount(source)
            .build();

    LedgerKey contractInstanceLedgerKey =
        LedgerKey.builder()
            .discriminant(LedgerEntryType.CONTRACT_DATA)
            .contractData(
                LedgerKey.LedgerKeyContractData.builder()
                    .contract(new Address(contractId).toSCAddress())
                    .key(
                        SCVal.builder()
                            .discriminant(SCValType.SCV_LEDGER_KEY_CONTRACT_INSTANCE)
                            .build())
                    .durability(ContractDataDurability.PERSISTENT)
                    .build())
            .build();

    LedgerKey balanceLedgerKey =
        LedgerKey.builder()
            .discriminant(LedgerEntryType.CONTRACT_DATA)
            .contractData(
                LedgerKey.LedgerKeyContractData.builder()
                    .contract(new Address(contractId).toSCAddress())
                    .key(
                        Scv.toVec(
                            Arrays.asList(Scv.toSymbol("Balance"), Scv.toAddress(destination))))
                    .durability(ContractDataDurability.PERSISTENT)
                    .build())
            .build();

    ArrayList<LedgerKey> readOnly = new ArrayList<>();
    ArrayList<LedgerKey> readWrite = new ArrayList<>();
    if (asset instanceof AssetTypeNative) {
      readOnly.add(contractInstanceLedgerKey);
      readWrite.add(
          LedgerKey.builder()
              .discriminant(LedgerEntryType.ACCOUNT)
              .account(
                  LedgerKey.LedgerKeyAccount.builder()
                      .accountID(KeyPair.fromAccountId(fromAddress).getXdrAccountId())
                      .build())
              .build());
      readWrite.add(balanceLedgerKey);
    } else {
      String assetIssuer = ((AssetTypeCreditAlphaNum) asset).getIssuer();
      readOnly.add(
          LedgerKey.builder()
              .discriminant(LedgerEntryType.ACCOUNT)
              .account(
                  LedgerKey.LedgerKeyAccount.builder()
                      .accountID(KeyPair.fromAccountId(assetIssuer).getXdrAccountId())
                      .build())
              .build());
      readOnly.add(contractInstanceLedgerKey);
      if (!assetIssuer.equals(fromAddress)) {
        readWrite.add(
            LedgerKey.builder()
                .discriminant(LedgerEntryType.TRUSTLINE)
                .trustLine(
                    LedgerKey.LedgerKeyTrustLine.builder()
                        .accountID(KeyPair.fromAccountId(fromAddress).getXdrAccountId())
                        .asset(new TrustLineAsset(asset).toXdr())
                        .build())
                .build());
      }
      readWrite.add(balanceLedgerKey);
    }

    SorobanTransactionData sorobanData =
        new SorobanDataBuilder()
            .setReadOnly(readOnly)
            .setReadWrite(readWrite)
            .setResourceFee(resourceFee)
            .setResources(resources)
            .build();

    this.addOperation(op);
    this.setSorobanData(sorobanData);
    return this.build();
  }

  /**
   * An alias for {@link #buildRestoreAssetBalanceEntryTransaction(String, Asset,
   * SorobanDataBuilder.Resources, Long, String)} with {@code resources} set to {@code new
   * SorobanDataBuilder.Resources(0L, 500L, 500L)} and {@code resourceFee} set to {@code
   * 4_000_000L}.
   *
   * @param balanceOwner The owner of the asset, it should be the same as the `destination` address
   *     in the {@link #buildPaymentToContractTransaction(String, Asset, BigDecimal,
   *     SorobanDataBuilder.Resources, Long, String)} method.
   * @param asset The asset
   * @param source The source account for the transaction.
   * @return The transaction.
   */
  public Transaction buildRestoreAssetBalanceEntryTransaction(
      String balanceOwner, Asset asset, @Nullable String source) {
    SorobanDataBuilder.Resources resources = new SorobanDataBuilder.Resources(0L, 500L, 500L);
    return buildRestoreAssetBalanceEntryTransaction(
        balanceOwner, asset, resources, 4_000_000L, source);
  }

  /**
   * Builds a transaction to restore the asset balance entry.
   *
   * <p>This method is designed to be used in conjunction with the {@link
   * #buildPaymentToContractTransaction(String, Asset, BigDecimal, SorobanDataBuilder.Resources,
   * Long, String)} method.
   *
   * <p>Under Protocol 22, if the entry really needs to be restored, then this method will consume
   * about 0.25 XLM in resource fees.
   *
   * @param balanceOwner The owner of the asset, it should be the same as the `destination` address
   *     in the {@link #buildPaymentToContractTransaction(String, Asset, BigDecimal,
   *     SorobanDataBuilder.Resources, Long, String)} method.
   * @param asset The asset
   * @param resources The resources required for the transaction.
   * @param resourceFee The maximum fee (in stroops) that can be paid for the transaction.
   * @param source The source account for the transaction.
   * @return The transaction.
   */
  public Transaction buildRestoreAssetBalanceEntryTransaction(
      String balanceOwner,
      Asset asset,
      SorobanDataBuilder.Resources resources,
      Long resourceFee,
      @Nullable String source) {
    String contractId = asset.getContractId(this.network);
    LedgerKey balanceLedgerKey =
        LedgerKey.builder()
            .discriminant(LedgerEntryType.CONTRACT_DATA)
            .contractData(
                LedgerKey.LedgerKeyContractData.builder()
                    .contract(new Address(contractId).toSCAddress())
                    .key(
                        Scv.toVec(
                            Arrays.asList(Scv.toSymbol("Balance"), Scv.toAddress(balanceOwner))))
                    .durability(ContractDataDurability.PERSISTENT)
                    .build())
            .build();

    RestoreFootprintOperation op =
        RestoreFootprintOperation.builder().sourceAccount(source).build();
    SorobanTransactionData sorobanData =
        new SorobanDataBuilder()
            .setReadOnly(new ArrayList<>())
            .setReadWrite(Collections.singletonList(balanceLedgerKey))
            .setResourceFee(resourceFee)
            .setResources(resources)
            .build();

    this.addOperation(op);
    this.setSorobanData(sorobanData);
    return this.build();
  }
}
