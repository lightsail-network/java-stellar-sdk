package org.stellar.sdk;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.stellar.sdk.requests.ClientIdentificationInterceptor;
import org.stellar.sdk.requests.ResponseHandler;
import org.stellar.sdk.requests.sorobanrpc.GetEventsRequest;
import org.stellar.sdk.requests.sorobanrpc.GetLedgerEntriesRequest;
import org.stellar.sdk.requests.sorobanrpc.GetTransactionRequest;
import org.stellar.sdk.requests.sorobanrpc.SendTransactionRequest;
import org.stellar.sdk.requests.sorobanrpc.SimulateTransactionRequest;
import org.stellar.sdk.requests.sorobanrpc.SorobanRpcErrorResponse;
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
import org.stellar.sdk.xdr.ContractEntryBodyType;
import org.stellar.sdk.xdr.LedgerEntry;
import org.stellar.sdk.xdr.LedgerEntryType;
import org.stellar.sdk.xdr.LedgerKey;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SorobanAuthorizationEntry;
import org.stellar.sdk.xdr.XdrDataInputStream;
import org.stellar.sdk.xdr.XdrDataOutputStream;

/**
 * Main class used to connect to the Soroban-RPC instance and exposes an interface for requests to
 * that instance.
 */
@SuppressWarnings("KotlinInternalInJava")
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
   * @throws IOException If the request could not be executed due to cancellation, a connectivity
   *     problem or timeout. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   * @throws AccountNotFoundException If the account does not exist on the network. You may need to
   *     fund it first.
   * @throws SorobanRpcErrorResponse If the Soroban-RPC instance returns an error response.
   */
  public TransactionBuilderAccount getAccount(String accountId)
      throws IOException, AccountNotFoundException, SorobanRpcErrorResponse {
    LedgerKey.LedgerKeyAccount ledgerKeyAccount =
        new LedgerKey.LedgerKeyAccount.Builder()
            .accountID(KeyPair.fromAccountId(accountId).getXdrAccountId())
            .build();
    LedgerKey ledgerKey =
        new LedgerKey.Builder()
            .account(ledgerKeyAccount)
            .discriminant(LedgerEntryType.ACCOUNT)
            .build();
    GetLedgerEntriesResponse getLedgerEntriesResponse =
        this.getLedgerEntries(Collections.singleton(ledgerKey));
    List<GetLedgerEntriesResponse.LedgerEntryResult> entries =
        getLedgerEntriesResponse.getEntries();
    if (entries == null || entries.isEmpty()) {
      throw new AccountNotFoundException(accountId);
    }
    LedgerEntry.LedgerEntryData ledgerEntryData =
        ledgerEntryDataFromXdrBase64(entries.get(0).getXdr());
    long sequence = ledgerEntryData.getAccount().getSeqNum().getSequenceNumber().getInt64();
    return new Account(accountId, sequence);
  }

  /**
   * General node health check.
   *
   * @return A {@link GetHealthResponse} object containing the health check result.
   * @throws IOException If the request could not be executed due to cancellation, a connectivity
   *     problem or timeout. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   * @throws SorobanRpcErrorResponse If the Soroban-RPC instance returns an error response.
   */
  public GetHealthResponse getHealth() throws IOException, SorobanRpcErrorResponse {
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
   *     {@link Durability#TEMPORARY} or '{@link Durability#PERSISTENT}'.
   * @return A {@link GetLedgerEntriesResponse.LedgerEntryResult} object containing the ledger entry
   *     result.
   * @throws IOException If the request could not be executed due to cancellation, a connectivity
   *     problem or timeout. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   * @throws SorobanRpcErrorResponse If the Soroban-RPC instance returns an error response.
   */
  public Optional<GetLedgerEntriesResponse.LedgerEntryResult> getContractData(
      String contractId, SCVal key, Durability durability)
      throws IOException, SorobanRpcErrorResponse {

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
        new LedgerKey.LedgerKeyContractData.Builder()
            .contract(address.toSCAddress())
            .key(key)
            .durability(contractDataDurability)
            .bodyType(ContractEntryBodyType.DATA_ENTRY)
            .build();
    LedgerKey ledgerKey =
        new LedgerKey.Builder()
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
   * @param keys The key of the contract data to load.
   * @return A {@link GetLedgerEntriesResponse} object containing the current values.
   * @throws IOException If the request could not be executed due to cancellation, a connectivity
   *     problem or timeout. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   * @throws SorobanRpcErrorResponse If the Soroban-RPC instance returns an error response.
   */
  public GetLedgerEntriesResponse getLedgerEntries(Collection<LedgerKey> keys)
      throws IOException, SorobanRpcErrorResponse {
    List<String> xdrKeys =
        keys.stream().map(SorobanServer::ledgerKeyToXdrBase64).collect(Collectors.toList());
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
   * @throws IOException If the request could not be executed due to cancellation, a connectivity
   *     problem or timeout. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   * @throws SorobanRpcErrorResponse If the Soroban-RPC instance returns an error response.
   */
  public GetTransactionResponse getTransaction(String hash)
      throws IOException, SorobanRpcErrorResponse {
    GetTransactionRequest params = new GetTransactionRequest(hash);
    return this.sendRequest(
        "getTransaction", params, new TypeToken<SorobanRpcResponse<GetTransactionResponse>>() {});
  }

  /**
   * Fetches all events that match the given {@link GetEventsRequest}.
   *
   * @param getEventsRequest The {@link GetEventsRequest} to use for the request.
   * @return A {@link GetEventsResponse} object containing the events that match the request.
   * @throws IOException If the request could not be executed due to cancellation, a connectivity
   *     problem or timeout. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   * @throws SorobanRpcErrorResponse If the Soroban-RPC instance returns an error response.
   */
  public GetEventsResponse getEvents(GetEventsRequest getEventsRequest)
      throws IOException, SorobanRpcErrorResponse {
    return this.sendRequest(
        "getEvents", getEventsRequest, new TypeToken<SorobanRpcResponse<GetEventsResponse>>() {});
  }

  /**
   * Fetches metadata about the network which Soroban-RPC is connected to.
   *
   * @return A {@link GetNetworkResponse} object containing the network metadata.
   * @throws IOException If the request could not be executed due to cancellation, a connectivity
   *     problem or timeout. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   * @throws SorobanRpcErrorResponse If the Soroban-RPC instance returns an error response.
   */
  public GetNetworkResponse getNetwork() throws IOException, SorobanRpcErrorResponse {
    return this.<Void, GetNetworkResponse>sendRequest(
        "getNetwork", null, new TypeToken<SorobanRpcResponse<GetNetworkResponse>>() {});
  }

  /**
   * Fetches the latest ledger meta info from network which Soroban-RPC is connected to.
   *
   * @return A {@link GetLatestLedgerResponse} object containing the latest ledger meta info.
   * @throws IOException If the request could not be executed due to cancellation, a connectivity
   *     problem or timeout. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   * @throws SorobanRpcErrorResponse If the Soroban-RPC instance returns an error response.
   */
  public GetLatestLedgerResponse getLatestLedger() throws IOException, SorobanRpcErrorResponse {
    return this.<Void, GetLatestLedgerResponse>sendRequest(
        "getLatestLedger", null, new TypeToken<SorobanRpcResponse<GetLatestLedgerResponse>>() {});
  }

  /**
   * Submit a trial contract invocation to get back return values, expected ledger footprint,
   * expected authorizations, and expected costs.
   *
   * @param transaction The transaction to simulate. It should include exactly one operation, which
   *     must be one of {@link InvokeHostFunctionOperation}, {@link
   *     BumpFootprintExpirationOperation}, or {@link RestoreFootprintOperation}. Any provided
   *     footprint will be ignored.
   * @return A {@link SimulateTransactionResponse} object containing the cost, footprint,
   *     result/auth requirements (if applicable), and error of the transaction.
   * @throws IOException If the request could not be executed due to cancellation, a connectivity
   *     problem or timeout. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   * @throws SorobanRpcErrorResponse If the Soroban-RPC instance returns an error response.
   */
  public SimulateTransactionResponse simulateTransaction(Transaction transaction)
      throws IOException, SorobanRpcErrorResponse {
    // TODO: In the future, it may be necessary to consider FeeBumpTransaction.
    SimulateTransactionRequest params =
        new SimulateTransactionRequest(transaction.toEnvelopeXdrBase64());
    return this.sendRequest(
        "simulateTransaction",
        params,
        new TypeToken<SorobanRpcResponse<SimulateTransactionResponse>>() {});
  }

  /**
   * Submit a trial contract invocation, first run a simulation of the contract invocation as
   * defined on the incoming transaction, and apply the results to a new copy of the transaction
   * which is then returned. Setting the ledger footprint and authorization, so the resulting
   * transaction is ready for signing & sending.
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
   *     BumpFootprintExpirationOperation}, or {@link RestoreFootprintOperation}. Any provided
   *     footprint will be ignored.
   * @return Returns a copy of the {@link Transaction}, with the expected authorizations (in the
   *     case of invocation) and ledger footprint added. The transaction fee will also automatically
   *     be padded with the contract's minimum resource fees discovered from the simulation.
   * @throws IOException If the request could not be executed due to cancellation, a connectivity
   *     problem or timeout. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   * @throws SorobanRpcErrorResponse If the Soroban-RPC instance returns an error response.
   */
  public Transaction prepareTransaction(Transaction transaction)
      throws IOException, SorobanRpcErrorResponse {
    SimulateTransactionResponse simulateTransactionResponse = this.simulateTransaction(transaction);
    if (simulateTransactionResponse.getError() != null) {
      throw new PrepareTransactionException(simulateTransactionResponse.getError());
    }
    if (simulateTransactionResponse.getResults() == null
        || simulateTransactionResponse.getResults().size() != 1) {
      throw new PrepareTransactionException(
          "unexpected response: " + simulateTransactionResponse.getResults());
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
   * @throws IOException If the request could not be executed due to cancellation, a connectivity
   *     problem or timeout. Because networks can fail during an exchange, it is possible that the
   *     remote server accepted the request before the failure.
   * @throws SorobanRpcErrorResponse If the Soroban-RPC instance returns an error response.
   */
  public SendTransactionResponse sendTransaction(Transaction transaction)
      throws IOException, SorobanRpcErrorResponse {
    // TODO: In the future, it may be necessary to consider FeeBumpTransaction.
    SendTransactionRequest params = new SendTransactionRequest(transaction.toEnvelopeXdrBase64());
    return this.sendRequest(
        "sendTransaction", params, new TypeToken<SorobanRpcResponse<SendTransactionResponse>>() {});
  }

  private Transaction assembleTransaction(
      Transaction transaction, SimulateTransactionResponse simulateTransactionResponse) {
    if (!isSorobanTransaction(transaction)) {
      throw new IllegalArgumentException(
          "unsupported transaction: must contain exactly one InvokeHostFunctionOperation, BumpSequenceOperation, or RestoreFootprintOperation");
    } // TODO: Throw the appropriate exception.

    SimulateTransactionResponse.SimulateHostFunctionResult simulateHostFunctionResult =
        simulateTransactionResponse.getResults().get(0);

    long classicFeeNum = transaction.getFee();
    long minResourceFeeNum =
        Optional.ofNullable(simulateTransactionResponse.getMinResourceFee()).orElse(0L);
    long fee = classicFeeNum + minResourceFeeNum;
    Operation operation = transaction.getOperations()[0];

    if (operation instanceof InvokeHostFunctionOperation) {
      Collection<SorobanAuthorizationEntry> originalEntries =
          ((InvokeHostFunctionOperation) operation).getAuth();
      List<SorobanAuthorizationEntry> newEntries = new ArrayList<>(originalEntries);
      if (simulateHostFunctionResult.getAuth() != null) {
        for (String auth : simulateHostFunctionResult.getAuth()) {
          newEntries.add(sorobanAuthorizationEntryFromXdrBase64(auth));
        }
      }
      operation =
          InvokeHostFunctionOperation.builder()
              .hostFunction(((InvokeHostFunctionOperation) operation).getHostFunction())
              .sourceAccount(operation.getSourceAccount())
              .auth(newEntries)
              .build();
    }

    return new Transaction(
        transaction.getAccountConverter(),
        transaction.getSourceAccount(),
        fee,
        transaction.getSequenceNumber(),
        new Operation[] {operation},
        transaction.getMemo(),
        transaction.getPreconditions(),
        transaction.getSorobanData(),
        transaction.getNetwork());
  }

  private <T, R> R sendRequest(
      String method, @Nullable T params, TypeToken<SorobanRpcResponse<R>> responseType)
      throws IOException, SorobanRpcErrorResponse {
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
        throw new SorobanRpcErrorResponse(error.getCode(), error.getMessage(), error.getData());
      }
      return sorobanRpcResponse.getResult();
    }
  }

  @Override
  public void close() throws IOException {
    this.httpClient.connectionPool().evictAll();
  }

  private static String generateRequestId() {
    return UUID.randomUUID().toString();
  }

  private static String ledgerKeyToXdrBase64(LedgerKey ledgerKey) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    try {
      ledgerKey.encode(xdrDataOutputStream);
    } catch (IOException e) {
      throw new IllegalArgumentException("invalid ledgerKey.", e);
    }
    BaseEncoding base64Encoding = BaseEncoding.base64();
    return base64Encoding.encode(byteArrayOutputStream.toByteArray());
  }

  private static LedgerEntry.LedgerEntryData ledgerEntryDataFromXdrBase64(String ledgerEntryData) {
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] bytes = base64Encoding.decode(ledgerEntryData);
    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
    XdrDataInputStream xdrInputStream = new XdrDataInputStream(inputStream);
    try {
      return LedgerEntry.LedgerEntryData.decode(xdrInputStream);
    } catch (IOException e) {
      throw new IllegalArgumentException("invalid ledgerEntryData: " + ledgerEntryData, e);
    }
  }

  private static SorobanAuthorizationEntry sorobanAuthorizationEntryFromXdrBase64(
      String sorobanAuthorizationEntry) {
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] bytes = base64Encoding.decode(sorobanAuthorizationEntry);
    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
    XdrDataInputStream xdrInputStream = new XdrDataInputStream(inputStream);
    try {
      return SorobanAuthorizationEntry.decode(xdrInputStream);
    } catch (IOException e) {
      throw new IllegalArgumentException(
          "invalid ledgerEntryData: " + sorobanAuthorizationEntry, e);
    }
  }

  private static boolean isSorobanTransaction(Transaction transaction) {
    if (transaction.getOperations().length != 1) {
      return false;
    }

    Operation op = transaction.getOperations()[0];
    return op instanceof InvokeHostFunctionOperation
        || op instanceof BumpSequenceOperation
        || op instanceof RestoreFootprintOperation;
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
