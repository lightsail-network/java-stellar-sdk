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

  public SorobanServer(String serverURI, OkHttpClient httpClient) {
    this.serverURI = HttpUrl.parse(serverURI);
    this.httpClient = httpClient;
  }

  public TransactionBuilderAccount getAccount(String accountId)
      throws IOException, LedgerEntryNotFoundException {
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
      throw new LedgerEntryNotFoundException(ledgerKeyToXdrBase64(ledgerKey));
    }
    LedgerEntry.LedgerEntryData ledgerEntryData =
        ledgerEntryDataFromXdrBase64(entries.get(0).getXdr());
    long sequence = ledgerEntryData.getAccount().getSeqNum().getSequenceNumber().getInt64();
    return new Account(accountId, sequence);
  }

  public GetHealthResponse getHealth() throws IOException {
    return this.<Void, GetHealthResponse>sendRequest(
        "getHealth", null, new TypeToken<SorobanRpcResponse<GetHealthResponse>>() {});
  }

  public GetLedgerEntriesResponse.LedgerEntryResult getContractData(
      String contractId, SCVal key, Durability durability)
      throws IOException, LedgerEntryNotFoundException {

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
      throw new LedgerEntryNotFoundException(ledgerKeyToXdrBase64(ledgerKey));
    }

    return entries.get(0);
  }

  public GetLedgerEntriesResponse getLedgerEntries(Collection<LedgerKey> keys) throws IOException {
    List<String> xdrKeys =
        keys.stream().map(SorobanServer::ledgerKeyToXdrBase64).collect(Collectors.toList());
    GetLedgerEntriesRequest params = new GetLedgerEntriesRequest(xdrKeys);
    return this.sendRequest(
        "getLedgerEntries",
        params,
        new TypeToken<SorobanRpcResponse<GetLedgerEntriesResponse>>() {});
  }

  public GetTransactionResponse getTransaction(String hash) throws IOException {
    GetTransactionRequest params = new GetTransactionRequest(hash);
    return this.sendRequest(
        "getTransaction", params, new TypeToken<SorobanRpcResponse<GetTransactionResponse>>() {});
  }

  public GetEventsResponse getEvents(GetEventsRequest getEventsRequest) throws IOException {
    return this.sendRequest(
        "getEvents", getEventsRequest, new TypeToken<SorobanRpcResponse<GetEventsResponse>>() {});
  }

  public GetNetworkResponse getNetwork() throws IOException {
    return this.<Void, GetNetworkResponse>sendRequest(
        "getNetwork", null, new TypeToken<SorobanRpcResponse<GetNetworkResponse>>() {});
  }

  public GetLatestLedgerResponse getLatestLedger() throws IOException {
    return this.<Void, GetLatestLedgerResponse>sendRequest(
        "getLatestLedger", null, new TypeToken<SorobanRpcResponse<GetLatestLedgerResponse>>() {});
  }

  public SimulateTransactionResponse simulateTransaction(Transaction transaction)
      throws IOException {
    // TODO: In the future, it may be necessary to consider FeeBumpTransaction.
    SimulateTransactionRequest params =
        new SimulateTransactionRequest(transaction.toEnvelopeXdrBase64());
    return this.sendRequest(
        "simulateTransaction",
        params,
        new TypeToken<SorobanRpcResponse<SimulateTransactionResponse>>() {});
  }

  public Transaction prepareTransaction(Transaction transaction) throws IOException {
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

  public SendTransactionResponse sendTransaction(Transaction transaction) throws IOException {
    // TODO: In the future, it may be necessary to consider FeeBumpTransaction.
    SendTransactionRequest params = new SendTransactionRequest(transaction.toEnvelopeXdrBase64());
    return this.sendRequest(
        "sendTransaction", params, new TypeToken<SorobanRpcResponse<SendTransactionResponse>>() {});
  }

  private <T, R> R sendRequest(
      String method, @Nullable T params, TypeToken<SorobanRpcResponse<R>> responseType)
      throws IOException {
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

  public enum Durability {
    TEMPORARY,
    PERSISTENT
  }
}
