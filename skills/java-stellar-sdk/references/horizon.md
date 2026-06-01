# Horizon API

`Server` is the Horizon client (synchronous, built on OkHttp). Remember to `close()` it.

```java
import org.stellar.sdk.Server;

Server testnet = new Server("https://horizon-testnet.stellar.org");
Server public_ = new Server("https://horizon.stellar.org");
```

## Top-level methods

```java
server.loadAccount("G...");          // -> TransactionBuilderAccount (with current sequence)
server.submitTransaction(tx);        // submit a built+signed Transaction (or FeeBumpTransaction)
server.submitTransactionAsync(tx);   // submit and return immediately (async-pending)
server.close();                      // release the HTTP client
```

## Request builders

Each returns a chainable builder; `.execute()` runs the HTTP request.

```java
server.root();
server.accounts();
server.assets();
server.claimableBalances();
server.effects();
server.feeStats();
server.ledgers();
server.liquidityPools();
server.offers();
server.operations();
server.orderBook();
server.strictReceivePaths();
server.strictSendPaths();
server.payments();
server.trades();
server.tradeAggregations(...);
server.transactions();
```

## Chaining / filters

```java
import org.stellar.sdk.requests.RequestBuilder;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.operations.OperationResponse;

Page<OperationResponse> records =
    server.payments()
        .forAccount("G...")                 // endpoint-specific filter
        .order(RequestBuilder.Order.DESC)   // ASC (default) or DESC
        .limit(50)                          // max 200
        .cursor("now")                      // paging token; "now" for live tail (streaming)
        .execute();
```

Other filters by endpoint: `forLedger`, `forTransaction`, `forOperation`, `forAsset`,
`forClaimant`, `forSigner`, `forLiquidityPool`, etc. Single-resource lookups return the
response directly: `server.accounts().account("G...")`,
`server.transactions().transaction("<hash>")`.

## Pagination

Page through results with the paging token of the last record:

```java
import org.stellar.sdk.requests.PaymentsRequestBuilder;

PaymentsRequestBuilder builder = server.payments().forAccount("G...").limit(100);
List<OperationResponse> all = new ArrayList<>();
String cursor = null;
while (true) {
    if (cursor != null) builder.cursor(cursor);
    List<OperationResponse> records = builder.execute().getRecords();
    if (records.isEmpty()) break;
    all.addAll(records);
    cursor = records.get(records.size() - 1).getPagingToken();
}
```

`Page` also exposes `getNextPage(okHttpClient)` to follow the HAL "next" link directly.

## Streaming (Server-Sent Events)

```java
import org.stellar.sdk.requests.EventListener;
import org.stellar.sdk.requests.SSEStream;
import org.stellar.sdk.responses.operations.OperationResponse;

SSEStream<OperationResponse> stream =
    server.payments()
        .forAccount("G...")
        .cursor("now")
        .stream(new EventListener<OperationResponse>() {
            @Override public void onEvent(OperationResponse payment) {
                System.out.println(payment.getId());
            }
            @Override public void onFailure(java.util.Optional<Throwable> error, java.util.Optional<Integer> code) {}
        });
// ... later ...
stream.close(); // stop streaming
```

`cursor("now")` starts at the present. On disconnect the SDK reconnects from the last seen
cursor. Always `close()` the stream when done.

## Errors

```java
import org.stellar.sdk.exception.*;

try {
    server.accounts().account("G...");
} catch (BadRequestException e) {       // 4xx (e.g. 400/404), includes problem details
    System.out.println(e.getMessage());
} catch (BadResponseException e) {      // 5xx
    System.out.println(e.getMessage());
} catch (NetworkException e) {          // supertype: transport/other network errors
    System.out.println(e.getMessage());
}
```

`NetworkException` is the common supertype; `BadRequestException` and `BadResponseException`
are subclasses. See `troubleshooting.md` for result codes on failed submissions.
