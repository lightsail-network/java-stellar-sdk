package org.stellar.sdk;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Closeable;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.Nullable;
import org.stellar.sdk.exception.AccountNotFoundException;
import org.stellar.sdk.exception.ConnectionErrorException;
import org.stellar.sdk.exception.PrepareTransactionException;
import org.stellar.sdk.exception.RequestTimeoutException;
import org.stellar.sdk.exception.SorobanRpcException;
import org.stellar.sdk.operations.InvokeHostFunctionOperation;
import org.stellar.sdk.operations.Operation;
import org.stellar.sdk.requests.ClientIdentificationInterceptor;
import org.stellar.sdk.requests.ResponseHandler;
import org.stellar.sdk.requests.sorobanrpc.GetEventsRequest;
import org.stellar.sdk.requests.sorobanrpc.GetLedgerEntriesRequest;
import org.stellar.sdk.requests.sorobanrpc.GetTransactionRequest;
import org.stellar.sdk.requests.sorobanrpc.SendTransactionRequest;
import org.stellar.sdk.requests.sorobanrpc.SimulateTransactionRequest;
import org.stellar.sdk.requests.sorobanrpc.SorobanRpcRequest;
import org.stellar.sdk.responses.sorobanrpc.GetEventsResponse;
import org.stellar.sdk.responses.sorobanrpc.GetHealthResponse;
import org.stellar.sdk.responses.sorobanrpc.GetLatestLedgerResponse;
import org.stellar.sdk.responses.sorobanrpc.GetLedgerEntriesResponse;
import org.stellar.sdk.responses.sorobanrpc.GetNetworkResponse;
import org.stellar.sdk.responses.sorobanrpc.GetTransactionResponse;
import org.stellar.sdk.responses.sorobanrpc.SendTransactionResponse;
import org.stellar.sdk.responses.sorobanrpc.SimulateTransactionResponse;
import org.stellar.sdk.responses.sorobanrpc.SorobanRpcResponse;
import org.stellar.sdk.xdr.ContractDataDurability;
import org.stellar.sdk.xdr.LedgerEntry;
import org.stellar.sdk.xdr.LedgerEntryType;
import org.stellar.sdk.xdr.LedgerKey;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SorobanAuthorizationEntry;
import org.stellar.sdk.xdr.SorobanTransactionData;

/**
 * Main class used to connect to the Soroban-RPC instance and exposes an interface for requests to
 * that instance.
 */
public class SorobanServer implements Closeable {
  private static final int SUBMIT_TRANSACTION_TIMEOUT = 60; // seconds
  private static final int CONNECT_TIMEOUT = 10; // seconds
  private final HttpUrl serverURI;
  private final OkHttpClient httpClient;
  private final Gson gson = new Gson();

  /**
   * Creates a new SorobanServer instance.
   *
   * @param serverURI The URI of the Soroban-RPC instance to connect to.
   */
  public SorobanServer(String serverURI) {
    this(
        serverURI,
        new OkHttpClient.Builder()
            .addInterceptor(new ClientIdentificationInterceptor())
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(SUBMIT_TRANSACTION_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build());
  }

  /**
   * Creates a new SorobanServer instance.
   *
   * @param serverURI The URI of the Soroban-RPC instance to connect to.
   * @param httpClient The {@link OkHttpClient} instance to use for requests.
   */
  public SorobanServer(String serverURI, OkHttpClient httpClient) {
    this.serverURI = HttpUrl.parse(serverURI);
    this.httpClient = httpClient;
  }

  /**
   * Fetch a minimal set of current info about a Stellar account. Needed to get the current sequence
   * number for the account, so you can build a successful transaction with {@link
   * TransactionBuilder}.
   *
   * @param accountId The public address of the account to load.
   * @return An {@link Account} object containing the sequence number and current state of the
   *     account.
   * @throws AccountNotFoundException If the account does not exist on the network. You may need to
   *     fund it first.
   * @throws SorobanRpcException If the Soroban-RPC instance returns an error response.
   * @throws RequestTimeoutException If the request timed out.
   * @throws ConnectionErrorException If the request could not be executed due to cancellation or
   *     connectivity problem. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   */
  public TransactionBuilderAccount getAccount(String accountId) {
    LedgerKey.LedgerKeyAccount ledgerKeyAccount =
        LedgerKey.LedgerKeyAccount.builder()
            .accountID(KeyPair.fromAccountId(accountId).getXdrAccountId())
            .build();
    LedgerKey ledgerKey =
        LedgerKey.builder().account(ledgerKeyAccount).discriminant(LedgerEntryType.ACCOUNT).build();
    GetLedgerEntriesResponse getLedgerEntriesResponse =
        this.getLedgerEntries(Collections.singleton(ledgerKey));
    List<GetLedgerEntriesResponse.LedgerEntryResult> entries =
        getLedgerEntriesResponse.getEntries();
    if (entries == null || entries.isEmpty()) {
      throw new AccountNotFoundException(accountId);
    }
    LedgerEntry.LedgerEntryData ledgerEntryData;
    try {
      ledgerEntryData = LedgerEntry.LedgerEntryData.fromXdrBase64(entries.get(0).getXdr());
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid ledgerEntryData: " + entries.get(0).getXdr(), e);
    }
    long sequence = ledgerEntryData.getAccount().getSeqNum().getSequenceNumber().getInt64();
    return new Account(accountId, sequence);
  }

  /**
   * General node health check.
   *
   * @return A {@link GetHealthResponse} object containing the health check result.
   * @throws SorobanRpcException If the Soroban-RPC instance returns an error response.
   * @throws RequestTimeoutException If the request timed out.
   * @throws ConnectionErrorException If the request could not be executed due to cancellation or
   *     connectivity problem. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   * @see <a href="https://soroban.stellar.org/api/methods/getHealth" target="_blank">getHealth
   *     documentation</a>
   */
  public GetHealthResponse getHealth() {
    return this.<Void, GetHealthResponse>sendRequest(
        "getHealth", null, new TypeToken<SorobanRpcResponse<GetHealthResponse>>() {});
  }

  /**
   * Reads the current value of contract data ledger entries directly.
   *
   * @param contractId The contract ID containing the data to load. Encoded as Stellar Contract
   *     Address. e.g. "CCJZ5DGASBWQXR5MPFCJXMBI333XE5U3FSJTNQU7RIKE3P5GN2K2WYD5"
   * @param key The key of the contract data to load.
   * @param durability The "durability keyspace" that this ledger key belongs to, which is either
   *     {@link Durability#TEMPORARY} or {@link Durability#PERSISTENT}.
   * @return A {@link GetLedgerEntriesResponse.LedgerEntryResult} object containing the ledger entry
   *     result.
   * @throws SorobanRpcException If the Soroban-RPC instance returns an error response.
   * @throws RequestTimeoutException If the request timed out.
   * @throws ConnectionErrorException If the request could not be executed due to cancellation or
   *     connectivity problem. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   */
  public Optional<GetLedgerEntriesResponse.LedgerEntryResult> getContractData(
      String contractId, SCVal key, Durability durability) {

    ContractDataDurability contractDataDurability;
    switch (durability) {
      case TEMPORARY:
        contractDataDurability = ContractDataDurability.TEMPORARY;
        break;
      case PERSISTENT:
        contractDataDurability = ContractDataDurability.PERSISTENT;
        break;
      default:
        throw new IllegalArgumentException("Invalid durability: " + durability);
    }

    Address address = new Address(contractId);
    LedgerKey.LedgerKeyContractData ledgerKeyContractData =
        LedgerKey.LedgerKeyContractData.builder()
            .contract(address.toSCAddress())
            .key(key)
            .durability(contractDataDurability)
            .build();
    LedgerKey ledgerKey =
        LedgerKey.builder()
            .discriminant(LedgerEntryType.CONTRACT_DATA)
            .contractData(ledgerKeyContractData)
            .build();
    GetLedgerEntriesResponse getLedgerEntriesResponse =
        this.getLedgerEntries(Collections.singleton(ledgerKey));
    List<GetLedgerEntriesResponse.LedgerEntryResult> entries =
        getLedgerEntriesResponse.getEntries();
    if (entries == null || entries.isEmpty()) {
      return Optional.empty();
    }
    GetLedgerEntriesResponse.LedgerEntryResult result = entries.get(0);
    return Optional.of(result);
  }

  /**
   * Reads the current value of ledger entries directly.
   *
   * <p>Allows you to directly inspect the current state of contracts, contract's code, or any other
   * ledger entries.
   *
   * @param keys The key of the contract data to load, at least one key must be provided.
   * @return A {@link GetLedgerEntriesResponse} object containing the current values.
   * @throws IllegalArgumentException If the keys collection is empty or invalid.
   * @throws SorobanRpcException If the Soroban-RPC instance returns an error response.
   * @throws RequestTimeoutException If the request timed out.
   * @throws ConnectionErrorException If the request could not be executed due to cancellation or
   *     connectivity problem. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   * @see <a href="https://soroban.stellar.org/api/methods/getLedgerEntries"
   *     target="_blank">getLedgerEntries documentation</a>
   */
  public GetLedgerEntriesResponse getLedgerEntries(Collection<LedgerKey> keys) {
    if (keys.isEmpty()) {
      throw new IllegalArgumentException("At least one key must be provided.");
    }

    List<String> xdrKeys = new ArrayList<>(keys.size());
    for (LedgerKey key : keys) {
      String xdrBase64;
      try {
        xdrBase64 = key.toXdrBase64();
      } catch (IOException e) {
        throw new IllegalArgumentException("Invalid ledgerKey: " + key, e);
      }
      xdrKeys.add(xdrBase64);
    }
    GetLedgerEntriesRequest params = new GetLedgerEntriesRequest(xdrKeys);
    return this.sendRequest(
        "getLedgerEntries",
        params,
        new TypeToken<SorobanRpcResponse<GetLedgerEntriesResponse>>() {});
  }

  /**
   * Fetch the details of a submitted transaction.
   *
   * <p>When submitting a transaction, client should poll this to tell when the transaction has
   * completed.
   *
   * @param hash The hash of the transaction to check. Encoded as a hex string.
   * @return A {@link GetTransactionResponse} object containing the transaction status, result, and
   *     other details.
   * @throws SorobanRpcException If the Soroban-RPC instance returns an error response.
   * @throws RequestTimeoutException If the request timed out.
   * @throws ConnectionErrorException If the request could not be executed due to cancellation or
   *     connectivity problem. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   * @see <a href="https://soroban.stellar.org/api/methods/getTransaction"
   *     target="_blank">getTransaction documentation</a>
   */
  public GetTransactionResponse getTransaction(String hash) {
    GetTransactionRequest params = new GetTransactionRequest(hash);
    return this.sendRequest(
        "getTransaction", params, new TypeToken<SorobanRpcResponse<GetTransactionResponse>>() {});
  }

  /**
   * Fetches all events that match the given {@link GetEventsRequest}.
   *
   * @param getEventsRequest The {@link GetEventsRequest} to use for the request.
   * @return A {@link GetEventsResponse} object containing the events that match the request.
   * @throws SorobanRpcException If the Soroban-RPC instance returns an error response.
   * @throws RequestTimeoutException If the request timed out.
   * @throws ConnectionErrorException If the request could not be executed due to cancellation or
   *     connectivity problem. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   * @see <a href="https://soroban.stellar.org/api/methods/getEvents" target="_blank">getEvents
   *     documentation</a>
   */
  public GetEventsResponse getEvents(GetEventsRequest getEventsRequest) {
    return this.sendRequest(
        "getEvents", getEventsRequest, new TypeToken<SorobanRpcResponse<GetEventsResponse>>() {});
  }

  /**
   * Fetches metadata about the network which Soroban-RPC is connected to.
   *
   * @return A {@link GetNetworkResponse} object containing the network metadata.
   * @throws ConnectionErrorException If the request could not be executed due to cancellation or
   *     connectivity problem. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   * @throws SorobanRpcException If the Soroban-RPC instance returns an error response.
   * @throws RequestTimeoutException If the request timed out.
   * @see <a href="https://soroban.stellar.org/api/methods/getNetwork" target="_blank">getNetwork
   *     documentation</a>
   */
  public GetNetworkResponse getNetwork() {
    return this.<Void, GetNetworkResponse>sendRequest(
        "getNetwork", null, new TypeToken<SorobanRpcResponse<GetNetworkResponse>>() {});
  }

  /**
   * Fetches the latest ledger meta info from network which Soroban-RPC is connected to.
   *
   * @return A {@link GetLatestLedgerResponse} object containing the latest ledger meta info.
   * @throws SorobanRpcException If the Soroban-RPC instance returns an error response.
   * @throws RequestTimeoutException If the request timed out.
   * @throws ConnectionErrorException If the request could not be executed due to cancellation or
   *     connectivity problem. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   * @see <a href="https://soroban.stellar.org/api/methods/getLatestLedger"
   *     target="_blank">getLatestLedger documentation</a>
   */
  public GetLatestLedgerResponse getLatestLedger() {
    return this.<Void, GetLatestLedgerResponse>sendRequest(
        "getLatestLedger", null, new TypeToken<SorobanRpcResponse<GetLatestLedgerResponse>>() {});
  }

  /**
   * Submit a trial contract invocation to get back return values, expected ledger footprint,
   * expected authorizations, and expected costs.
   *
   * @param transaction The transaction to simulate. It should include exactly one operation, which
   *     must be one of {@link InvokeHostFunctionOperation}, {@link
   *     org.stellar.sdk.operations.ExtendFootprintTTLOperation}, or {@link
   *     org.stellar.sdk.operations.RestoreFootprintOperation}. Any provided footprint will be
   *     ignored.
   * @param resourceConfig Additional resource include in the simulation.
   * @return A {@link SimulateTransactionResponse} object containing the cost, footprint,
   *     result/auth requirements (if applicable), and error of the transaction.
   * @throws SorobanRpcException If the Soroban-RPC instance returns an error response.
   * @throws RequestTimeoutException If the request timed out.
   * @throws ConnectionErrorException If the request could not be executed due to cancellation or
   *     connectivity problem. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   * @see <a href="https://soroban.stellar.org/api/methods/simulateTransaction"
   *     target="_blank">simulateTransaction documentation</a>
   */
  public SimulateTransactionResponse simulateTransaction(
      Transaction transaction, @Nullable SimulateTransactionRequest.ResourceConfig resourceConfig) {
    // TODO: In the future, it may be necessary to consider FeeBumpTransaction.
    SimulateTransactionRequest params =
        new SimulateTransactionRequest(transaction.toEnvelopeXdrBase64(), resourceConfig);
    return this.sendRequest(
        "simulateTransaction",
        params,
        new TypeToken<SorobanRpcResponse<SimulateTransactionResponse>>() {});
  }

  /**
   * An alias for {@link #simulateTransaction(Transaction,
   * SimulateTransactionRequest.ResourceConfig)} with no resource leeway.
   */
  public SimulateTransactionResponse simulateTransaction(Transaction transaction) {
    return simulateTransaction(transaction, null);
  }

  /**
   * Submit a trial contract invocation, first run a simulation of the contract invocation as
   * defined on the incoming transaction, and apply the results to a new copy of the transaction
   * which is then returned. Setting the ledger footprint and authorization, so the resulting
   * transaction is ready for signing and sending.
   *
   * <p>The returned transaction will also have an updated fee that is the sum of fee set on
   * incoming transaction with the contract resource fees estimated from simulation. It is advisable
   * to check the fee on returned transaction and validate or take appropriate measures for
   * interaction with user to confirm it is acceptable.
   *
   * <p>You can call the {@link SorobanServer#simulateTransaction} method directly first if you want
   * to inspect estimated fees for a given transaction in detail first, if that is of importance.
   *
   * @param transaction The transaction to prepare. It should include exactly one operation, which
   *     must be one of {@link InvokeHostFunctionOperation}, {@link
   *     org.stellar.sdk.operations.ExtendFootprintTTLOperation}, or {@link
   *     org.stellar.sdk.operations.RestoreFootprintOperation}. Any provided footprint will be
   *     ignored. You can use {@link Transaction#isSorobanTransaction()} to check if a transaction
   *     is a Soroban transaction. Any provided footprint will be overwritten. However, if your
   *     operation has existing auth entries, they will be preferred over ALL auth entries from the
   *     simulation. In other words, if you include auth entries, you don't care about the auth
   *     returned from the simulation. Other fields (footprint, etc.) will be filled as normal.
   * @return Returns a copy of the {@link Transaction}, with the expected authorizations (in the
   *     case of invocation) and ledger footprint added. The transaction fee will also automatically
   *     be padded with the contract's minimum resource fees discovered from the simulation.
   * @throws PrepareTransactionException If preparing the transaction fails.
   * @throws SorobanRpcException If the Soroban-RPC instance returns an error response.
   * @throws RequestTimeoutException If the request timed out.
   * @throws ConnectionErrorException If the request could not be executed due to cancellation or
   *     connectivity problem. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   */
  public Transaction prepareTransaction(Transaction transaction) {
    SimulateTransactionResponse simulateTransactionResponse = simulateTransaction(transaction);
    return prepareTransaction(transaction, simulateTransactionResponse);
  }

  /**
   * Prepare the transaction using the simulateTransaction obtained by the user in advance, apply
   * the simulateTransaction results to a new copy of the transaction which is then returned.
   * Setting the ledger footprint and authorization, so the resulting transaction is ready for
   * signing and sending.
   *
   * <p>The returned transaction will also have an updated fee that is the sum of fee set on
   * incoming transaction with the contract resource fees estimated from simulation. It is advisable
   * to check the fee on returned transaction and validate or take appropriate measures for
   * interaction with user to confirm it is acceptable.
   *
   * @param transaction The transaction to prepare. It should include exactly one operation, which
   *     must be one of {@link InvokeHostFunctionOperation}, {@link
   *     org.stellar.sdk.operations.ExtendFootprintTTLOperation}, or {@link
   *     org.stellar.sdk.operations.RestoreFootprintOperation}. Any provided footprint will be
   *     ignored. You can use {@link Transaction#isSorobanTransaction()} to check if a transaction
   *     is a Soroban transaction. Any provided footprint will be overwritten. However, if your
   *     operation has existing auth entries, they will be preferred over ALL auth entries from the
   *     simulation. In other words, if you include auth entries, you don't care about the auth
   *     returned from the simulation. Other fields (footprint, etc.) will be filled as normal.
   * @param simulateTransactionResponse The {@link SimulateTransactionResponse} to use for preparing
   *     the transaction.
   * @return Returns a copy of the {@link Transaction}, with the expected authorizations (in the
   *     case of invocation) and ledger footprint added. The transaction fee will also automatically
   *     be padded with the contract's minimum resource fees discovered from the simulation.
   * @throws PrepareTransactionException If preparing the transaction fails.
   */
  public Transaction prepareTransaction(
      Transaction transaction, SimulateTransactionResponse simulateTransactionResponse) {
    if (simulateTransactionResponse.getError() != null) {
      throw new PrepareTransactionException(
          "simulation transaction failed, the response contains error information.",
          simulateTransactionResponse);
    }
    return assembleTransaction(transaction, simulateTransactionResponse);
  }

  /**
   * Submit a real transaction to the Stellar network. This is the only way to make changes
   * "on-chain". Unlike Horizon, Soroban-RPC does not wait for transaction completion. It simply
   * validates the transaction and enqueues it. Clients should call {@link
   * SorobanServer#getTransaction} to learn about transaction's status.
   *
   * @param transaction The transaction to submit.
   * @return A {@link SendTransactionResponse} object containing some details about the transaction
   *     that was submitted.
   * @throws ConnectionErrorException If the request could not be executed due to cancellation or
   *     connectivity problem. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   * @throws SorobanRpcException If the Soroban-RPC instance returns an error response.
   * @throws RequestTimeoutException If the request timed out.
   * @see <a href="https://soroban.stellar.org/api/methods/sendTransaction"
   *     target="_blank">sendTransaction documentation</a>
   */
  public SendTransactionResponse sendTransaction(Transaction transaction) {
    // TODO: In the future, it may be necessary to consider FeeBumpTransaction.
    SendTransactionRequest params = new SendTransactionRequest(transaction.toEnvelopeXdrBase64());
    return this.sendRequest(
        "sendTransaction", params, new TypeToken<SorobanRpcResponse<SendTransactionResponse>>() {});
  }

  private Transaction assembleTransaction(
      Transaction transaction, SimulateTransactionResponse simulateTransactionResponse) {
    if (!transaction.isSorobanTransaction()) {
      throw new IllegalArgumentException(
          "unsupported transaction: must contain exactly one InvokeHostFunctionOperation, BumpSequenceOperation, or RestoreFootprintOperation");
    }

    long classicFeeNum = transaction.getFee();
    long minResourceFeeNum =
        Optional.ofNullable(simulateTransactionResponse.getMinResourceFee()).orElse(0L);
    long fee = classicFeeNum + minResourceFeeNum;
    Operation operation = transaction.getOperations()[0];

    if (operation instanceof InvokeHostFunctionOperation) {
      // If the operation is an InvokeHostFunctionOperation, we need to update the auth entries if
      // existing entries are empty and the simulation result contains auth entries.
      if (simulateTransactionResponse.getResults() == null
          || simulateTransactionResponse.getResults().size() != 1) {
        throw new IllegalArgumentException(
            "invalid simulateTransactionResponse: results must contain exactly one element if the operation is an InvokeHostFunctionOperation");
      }

      SimulateTransactionResponse.SimulateHostFunctionResult simulateHostFunctionResult =
          simulateTransactionResponse.getResults().get(0);

      Collection<SorobanAuthorizationEntry> existingEntries =
          ((InvokeHostFunctionOperation) operation).getAuth();
      if (existingEntries.isEmpty()
          && simulateHostFunctionResult.getAuth() != null
          && !simulateHostFunctionResult.getAuth().isEmpty()) {
        List<SorobanAuthorizationEntry> authorizationEntries =
            new ArrayList<>(simulateHostFunctionResult.getAuth().size());
        for (String auth : simulateHostFunctionResult.getAuth()) {
          try {
            authorizationEntries.add(SorobanAuthorizationEntry.fromXdrBase64(auth));
          } catch (IOException e) {
            throw new IllegalArgumentException("Invalid auth: " + auth, e);
          }
        }

        operation =
            InvokeHostFunctionOperation.builder()
                .hostFunction(((InvokeHostFunctionOperation) operation).getHostFunction())
                .sourceAccount(operation.getSourceAccount())
                .auth(authorizationEntries)
                .build();
      }
    }

    SorobanTransactionData sorobanData;
    try {
      sorobanData =
          SorobanTransactionData.fromXdrBase64(simulateTransactionResponse.getTransactionData());
    } catch (IOException e) {
      throw new IllegalArgumentException(
          "Invalid transactionData: " + simulateTransactionResponse.getTransactionData(), e);
    }

    return new Transaction(
        transaction.getAccountConverter(),
        transaction.getSourceAccount(),
        fee,
        transaction.getSequenceNumber(),
        new Operation[] {operation},
        transaction.getMemo(),
        transaction.getPreconditions(),
        sorobanData,
        transaction.getNetwork());
  }

  /**
   * Sends a request to the Soroban-RPC instance.
   *
   * @param method The method to call.
   * @param params The parameters to send.
   * @param responseType The response type to expect.
   * @param <T> The type of the request parameters.
   * @param <R> The type of the response result.
   * @return The response from the Soroban-RPC instance.
   * @throws SorobanRpcException If the Soroban-RPC instance returns an error response.
   * @throws RequestTimeoutException If the request timed out.
   * @throws ConnectionErrorException If the request could not be executed due to cancellation or
   *     connectivity problem. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   */
  private <T, R> R sendRequest(
      String method, @Nullable T params, TypeToken<SorobanRpcResponse<R>> responseType) {
    String requestId = generateRequestId();
    ResponseHandler<SorobanRpcResponse<R>> responseHandler = new ResponseHandler<>(responseType);
    SorobanRpcRequest<T> sorobanRpcRequest = new SorobanRpcRequest<>(requestId, method, params);
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody requestBody =
        RequestBody.create(gson.toJson(sorobanRpcRequest).getBytes(), mediaType);

    Request request = new Request.Builder().url(this.serverURI).post(requestBody).build();
    try (Response response = this.httpClient.newCall(request).execute()) {
      SorobanRpcResponse<R> sorobanRpcResponse = responseHandler.handleResponse(response);
      if (sorobanRpcResponse.getError() != null) {
        SorobanRpcResponse.Error error = sorobanRpcResponse.getError();
        throw new SorobanRpcException(error.getCode(), error.getMessage(), error.getData());
      }
      return sorobanRpcResponse.getResult();
    } catch (SocketTimeoutException e) {
      throw new RequestTimeoutException(e);
    } catch (IOException e) {
      throw new ConnectionErrorException(e);
    }
  }

  @Override
  public void close() throws IOException {
    this.httpClient.connectionPool().evictAll();
  }

  private static String generateRequestId() {
    return UUID.randomUUID().toString();
  }

  /**
   * Represents the "durability keyspace" that this ledger key belongs to, check {@link
   * SorobanServer#getContractData} for more details.
   */
  public enum Durability {
    TEMPORARY,
    PERSISTENT
  }
}
