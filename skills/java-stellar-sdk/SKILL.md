---
name: java-stellar-sdk
description: Build Stellar blockchain applications in Java using stellar-sdk (network.lightsail:stellar-sdk). Use for transaction building, signing, Horizon queries, Soroban RPC, smart contract deployment/invocation, XDR/SCVal conversion, and SEP integrations.
license: Apache-2.0
compatibility: Published artifacts target Java 8 bytecode (require Java 8+). Building from source requires JDK 21.
metadata:
  sdk_package: network.lightsail:stellar-sdk
  repository: lightsail-network/java-stellar-sdk
  docs: https://javadoc.io/doc/network.lightsail/stellar-sdk
---

# Stellar Java SDK (`network.lightsail:stellar-sdk`)

Write correct Java for the Stellar network using the `stellar-sdk` package. This file is
self-contained; load a file from `references/` only when the task needs that topic.

## Install

Maven:

```xml
<dependency>
    <groupId>network.lightsail</groupId>
    <artifactId>stellar-sdk</artifactId>
    <version>4.0.0-beta0</version>
</dependency>
```

Gradle:

```groovy
implementation 'network.lightsail:stellar-sdk:4.0.0-beta0'
```

## Import style

Public APIs live under `org.stellar.sdk`; operations under `org.stellar.sdk.operations`;
the contract helpers under `org.stellar.sdk.contract`; SCVal helpers under
`org.stellar.sdk.scval`; exceptions under `org.stellar.sdk.exception`, with the high-level
contract-client exceptions under `org.stellar.sdk.contract.exception`.

```java
import org.stellar.sdk.Asset;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Network;
import org.stellar.sdk.Server;
import org.stellar.sdk.Transaction;
import org.stellar.sdk.TransactionBuilder;
import org.stellar.sdk.operations.PaymentOperation;
import org.stellar.sdk.scval.Scv;
```

## Clients

- `Server` — Horizon client. See `references/horizon.md`.
- `SorobanServer` — Soroban RPC client (`Closeable`). See `references/soroban.md`.
- `ContractClient` — high-level Soroban contract client (`Closeable`) that wraps the
  simulate → prepare → sign → submit flow. See `references/soroban.md`.

The SDK is **synchronous** (built on OkHttp); there is no separate async client.

## Java conventions (read this first)

These are the things that most often trip people up with *this* SDK:

- **Operations use Lombok builders.** Every operation is built with `XxxOperation.builder()...
  .build()`, e.g. `PaymentOperation.builder().destination(d).asset(a).amount(amt).build()`.
  `InvokeHostFunctionOperation` instead exposes static factories (see `references/operations.md`).
- **There are three asset types — don't mix them up.** `Asset` (payments, offers, paths),
  `ChangeTrustAsset` (the `ChangeTrustOperation` line, wraps an `Asset` or `LiquidityPool`), and
  `TrustLineAsset` (balances/trustline entries). See `references/assets.md`.
- **Amounts and balances are `BigDecimal`.** `new BigDecimal("10.5")`, never `double`/`float`.
- **`G...` and `M...` both work as a destination.** Address strings accept plain ed25519
  accounts and SEP-23 muxed accounts; use `MuxedAccount` to build/parse `M...` (see
  `references/sep.md`).
- **Clients are `Closeable`.** `Server.close()`; wrap `SorobanServer`/`ContractClient` in
  try-with-resources. `SSEStream` from `.stream(...)` must also be closed to stop it.
- **SDK exceptions are unchecked.** Everything extends `RuntimeException`
  (`org.stellar.sdk.exception.SdkException`), so the compiler will not force you to catch them —
  you still should. The common supertype for network/RPC errors is `NetworkException`.
- **`KeyPair.getSecretSeed()` returns `char[]`,** not `String`, so it can be zeroed after use.
- **Android:** works out of the box on API level 28+. For lower levels add the
  [Java Stellar SDK Android SPI](https://github.com/lightsail-network/java-stellar-sdk-android-spi).
- **Example syntax.** The SDK runs on **Java 8+**, but the snippets in this skill use some
  Java 9–16 conveniences for brevity. On Java 8, substitute equivalents: `List.of(...)` →
  `Arrays.asList(...)`, `Map.of(...)` → a populated `LinkedHashMap`, `"x".repeat(n)` → a literal,
  pattern-matching `instanceof` → a classic cast, and `java.net.http` → `HttpURLConnection` or
  any HTTP client.

## Critical rules (do not violate)

1. **`BigDecimal` amounts, never `double`/`float`.** Use `new BigDecimal("350.1234567")`,
   not `350.1234567`. Lumens have 7 decimal places; binary floats lose precision.
2. **Prefer loading secret seeds at runtime** (env / a secret manager) over embedding
   them in source: `KeyPair.fromSecretSeed(System.getenv("STELLAR_SECRET_KEY"))`.
3. **Always set a timeout** with `.setTimeout(...)` (or explicit time bounds). A
   transaction without one can hang in the pending pool indefinitely.
4. **Use the correct network.** `Network.TESTNET` vs `Network.PUBLIC`. Signatures are
   network-specific; a testnet-signed transaction is invalid on mainnet.
5. **`TransactionBuilder.build()` increments the source account's sequence number.** If you
   build but do not submit (or submission fails with `tx_bad_seq`), reload the account with
   `server.loadAccount(...)` before building again.
6. **For Soroban, prepare before signing.** `sorobanServer.prepareTransaction(tx)` (or
   `ContractClient.invoke(...)` which simulates by default) fills in footprint, auth, and
   resource fees. Signing before this produces an invalid transaction.
7. **Convert contract args with `Scv.to*`** and read results with `Scv.from*`. Never pass
   raw Java values to a contract.
8. **Restore archived state** when simulation reports archived entries (see
   `references/soroban.md`).
9. **Close clients.** Call `server.close()` for `Server`; use try-with-resources for
   `SorobanServer` and `ContractClient` (both implement `Closeable`).
10. **Catch specific SDK exceptions** (`BadRequestException`, `BadResponseException`,
    `PrepareTransactionException`, …), not a broad `Exception`. They all extend
    `SdkException`; network/HTTP errors additionally share the `NetworkException` supertype,
    but `PrepareTransactionException` and the contract-lifecycle exceptions extend
    `SdkException` directly. Network/RPC exceptions live in `org.stellar.sdk.exception`;
    `ContractClient`/`AssembledTransaction` errors live in `org.stellar.sdk.contract.exception`.

## Minimal end-to-end examples

### 1. Create / load a keypair

```java
import org.stellar.sdk.KeyPair;

// New random account (fund it before use — see example 2).
KeyPair kp = KeyPair.random();
System.out.println(kp.getAccountId());      // G... (safe to share)
System.out.println(kp.getSecretSeed());     // S... (secret — never log or commit in real code)

// Load an existing account from the environment.
KeyPair signer = KeyPair.fromSecretSeed(System.getenv("STELLAR_SECRET_KEY"));

// Verify-only keypair (cannot sign).
KeyPair publicOnly = KeyPair.fromAccountId("GD2JXEFGEO53CNQ22KN2ICOQ2LOASCABQHAIOMLZV265C246PFKKHPYU");
```

### 2. Build, sign, and submit a payment

```java
import java.math.BigDecimal;
import org.stellar.sdk.*;
import org.stellar.sdk.operations.PaymentOperation;
import org.stellar.sdk.responses.TransactionResponse;

KeyPair source = KeyPair.fromSecretSeed(System.getenv("STELLAR_SECRET_KEY"));
String destination = "GD2JXEFGEO53CNQ22KN2ICOQ2LOASCABQHAIOMLZV265C246PFKKHPYU";

Server server = new Server("https://horizon-testnet.stellar.org");

// Reload right before building so the sequence number is current.
TransactionBuilderAccount account = server.loadAccount(source.getAccountId());

PaymentOperation payment =
    PaymentOperation.builder()
        .destination(destination)
        .asset(Asset.createNativeAsset())
        .amount(new BigDecimal("350.1234567")) // BigDecimal amount
        .build();

Transaction transaction =
    new TransactionBuilder(account, Network.TESTNET)
        .setBaseFee(Transaction.MIN_BASE_FEE)
        .addMemo(Memo.text("Hello, Stellar!"))
        .addOperation(payment)
        .setTimeout(30)
        .build();

transaction.sign(source);
TransactionResponse response = server.submitTransaction(transaction);
System.out.println(response.getHash());
server.close();
```

New testnet accounts can be funded by Friendbot:
`GET https://friendbot.stellar.org?addr=<G...>`.

### 3. Query Horizon

```java
import org.stellar.sdk.Server;
import org.stellar.sdk.requests.RequestBuilder;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.operations.OperationResponse;

Server server = new Server("https://horizon.stellar.org");

// Request builders are chainable; .execute() runs the HTTP request.
Page<OperationResponse> payments =
    server.payments()
        .forAccount("GB6NVEN5HSUBKMYCE5ZOWSK5K23TBWRUQLZY3KNMXUZ3AQ2ESC4MY4AQ")
        .order(RequestBuilder.Order.DESC)
        .limit(10)
        .execute();
for (OperationResponse op : payments.getRecords()) {
    System.out.println(op.getType());
}
```

### 4. Invoke a Soroban contract (read-only)

For contracts you call repeatedly, prefer **generated typed bindings** (see
`references/bindings.md`). The hand-written form with `ContractClient` is:

```java
import java.util.List;
import org.stellar.sdk.Network;
import org.stellar.sdk.contract.ContractClient;
import org.stellar.sdk.scval.Scv;
import org.stellar.sdk.xdr.SCVal;

// A read-only call needs no signer, but `source` is required and must be an account
// that already exists on the target network — simulate() loads its sequence via
// server.getAccount(), which throws AccountNotFoundException if the account is missing.
String source = "GD2JXEFGEO53CNQ22KN2ICOQ2LOASCABQHAIOMLZV265C246PFKKHPYU";

try (ContractClient client =
    new ContractClient(
        "CACZTW72246RA2MOCNKUBRRRRPT26UZ7LXE5ZHH44OGKIMCTQJ74O4D5",
        "https://soroban-testnet.stellar.org:443",
        Network.TESTNET)) {
    SCVal result =
        client
            .invoke("hello", List.of(Scv.toSymbol("world")), source, null, null,
                (int) org.stellar.sdk.Transaction.MIN_BASE_FEE)
            .result(); // read-only: no signer needed
    System.out.println(result);
}
```

For a state-changing call, pass `source` and `signer`, then `signAndSubmit(...)`. See
`references/soroban.md`.

## Reference index

Load on demand:

- `references/quickstart.md` — install → keypair → fund → payment → query → contract.
- `references/transactions.md` — transaction lifecycle, memos, preconditions, fee bump,
  multisig, XDR round-trips, sequence-number pitfalls.
- `references/assets.md` — `Asset` vs `ChangeTrustAsset` vs `TrustLineAsset`, native vs
  alphanum4/12, canonical form.
- `references/operations.md` — catalog of every operation and its builder.
- `references/horizon.md` — Horizon client, request builders, pagination, streaming, errors.
- `references/soroban.md` — Soroban RPC client + `ContractClient`/`AssembledTransaction`.
- `references/bindings.md` — generate typed contract clients with `stellar-contract-bindings`.
- `references/xdr_scval.md` — SCVal conversion, `Address`, XDR encode/decode.
- `references/sep.md` — SEP support matrix and common flows (SEP-02/05/10/23/35/45/...).
- `references/troubleshooting.md` — exception hierarchy and common failures.

Full API docs (Javadoc): https://javadoc.io/doc/network.lightsail/stellar-sdk
