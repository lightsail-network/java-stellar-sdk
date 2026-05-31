# Soroban (RPC + contracts)

Two layers: the low-level `SorobanServer` RPC client, and the high-level `ContractClient` /
`AssembledTransaction` which wrap the simulate → prepare → sign → submit dance. Both implement
`Closeable` — use try-with-resources.

**Prefer generated bindings.** For most contract work, generate a typed Java client with
[`stellar-contract-bindings`](https://github.com/lightsail-network/stellar-contract-bindings)
instead of hand-writing `Scv` conversions — see `bindings.md`. The hand-written `ContractClient`
flow below is the fallback.

## RPC client (`SorobanServer`)

```java
import org.stellar.sdk.SorobanServer;

try (SorobanServer server = new SorobanServer("https://soroban-testnet.stellar.org:443")) {
    server.getHealth();
    server.getNetwork();
    server.getLatestLedger();
    server.getLedgerEntries(keys);            // Collection<LedgerKey>
    server.getEvents(request);
    server.simulateTransaction(tx);
    server.prepareTransaction(tx);            // simulate + assemble (footprint, auth, resource fee)
    server.sendTransaction(tx);               // submit; returns immediately with a hash + status
    server.getTransaction(hash);              // poll for the result
    server.pollTransaction(hash);             // poll with retries until final
    server.getAccount("G...");                // -> TransactionBuilderAccount
    server.getContractData(contractId, key, durability);
    server.getContractInfo(contractId);       // SEP-48 (interface spec + meta)
    server.getContractMeta(contractId);       // SEP-46
    server.getContractSpec(contractId);       // SEP-48
}
```

### Manual submit loop

```java
import org.stellar.sdk.*;
import org.stellar.sdk.exception.*;
import org.stellar.sdk.operations.InvokeHostFunctionOperation;
import org.stellar.sdk.responses.sorobanrpc.GetTransactionResponse;
import org.stellar.sdk.responses.sorobanrpc.SendTransactionResponse;
import org.stellar.sdk.scval.Scv;
import org.stellar.sdk.xdr.SCVal;
import java.util.List;

KeyPair kp = KeyPair.fromSecretSeed(System.getenv("STELLAR_SECRET_KEY"));
try (SorobanServer server = new SorobanServer("https://soroban-testnet.stellar.org:443")) {
    TransactionBuilderAccount source = server.getAccount(kp.getAccountId());

    Transaction tx =
        new TransactionBuilder(source, Network.TESTNET)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .addOperation(
                InvokeHostFunctionOperation.invokeContractFunctionOperationBuilder(
                        "C...", "hello", List.of(Scv.toSymbol("world")))
                    .build())
            .setTimeout(300)
            .build();

    // MUST happen before signing.
    Transaction prepared;
    try {
        prepared = server.prepareTransaction(tx);
    } catch (PrepareTransactionException e) {
        System.out.println(e.getSimulateTransactionResponse());
        throw e;
    }

    prepared.sign(kp);
    SendTransactionResponse sent = server.sendTransaction(prepared);
    if (sent.getStatus() != SendTransactionResponse.SendTransactionStatus.PENDING) {
        throw new RuntimeException("send failed: " + sent);
    }

    GetTransactionResponse got;
    while (true) {
        got = server.getTransaction(sent.getHash());
        if (got.getStatus() != GetTransactionResponse.GetTransactionStatus.NOT_FOUND) break;
        Thread.sleep(3000);
    }
}
```

Read the return value from the transaction meta. Soroban meta lives under `V3` or `V4`
depending on the protocol version, so check both (this is exactly what
`AssembledTransaction.submit()` does internally):

```java
import org.stellar.sdk.xdr.TransactionMeta;
import org.stellar.sdk.xdr.SCVal;

TransactionMeta meta = got.parseResultMetaXdr();
SCVal returnValue =
    meta.getV3() != null
        ? meta.getV3().getSorobanMeta().getReturnValue()
        : meta.getV4().getSorobanMeta().getReturnValue();
```

## High-level client (`ContractClient` / `AssembledTransaction`)

```java
import org.stellar.sdk.contract.ContractClient;

ContractClient client =
    new ContractClient("C...", "https://soroban-testnet.stellar.org:443", Network.TESTNET);
```

`invoke(functionName, parameters, source, signer, parseResultXdrFn, baseFee)` returns an
`AssembledTransaction<T>` (already simulated by default). `parseResultXdrFn` is a
`Function<SCVal, T>`; pass `null` to get the raw `SCVal`.

### Read-only call

No signer needed, but you must still pass a `source` account id that already exists on
the target network — `simulate()` loads its sequence via `server.getAccount()`, which
throws `AccountNotFoundException` if the account does not exist. Read the simulated result:

```java
import java.util.List;
import org.stellar.sdk.scval.Scv;
import org.stellar.sdk.xdr.SCVal;

String source = "GD2JXEFGEO53CNQ22KN2ICOQ2LOASCABQHAIOMLZV265C246PFKKHPYU";
try (ContractClient client =
    new ContractClient("C...", "https://soroban-testnet.stellar.org:443", Network.TESTNET)) {
    SCVal value =
        client
            .invoke("hello", List.of(Scv.toSymbol("world")), source, null, null,
                (int) Transaction.MIN_BASE_FEE)
            .result();
}
```

### State-changing call

Pass `source` and `signer`, then submit. Use a `parseResultXdrFn` to decode the result:

```java
import java.util.Collections;
import java.util.function.Function;
import org.stellar.sdk.contract.AssembledTransaction;

KeyPair kp = KeyPair.fromSecretSeed(System.getenv("STELLAR_SECRET_KEY"));
Function<SCVal, Long> parse = scVal -> Scv.fromUint32(scVal);

try (ContractClient client =
    new ContractClient("C...", "https://soroban-testnet.stellar.org:443", Network.TESTNET)) {
    AssembledTransaction<Long> assembled =
        client.invoke("increment", Collections.emptyList(), kp.getAccountId(), kp, parse,
            (int) Transaction.MIN_BASE_FEE);
    Long result = assembled.signAndSubmit(kp, false);
}
```

### `AssembledTransaction` lifecycle

1. construct (via `client.invoke(...)`) — simulated by default
2. `simulate(restore)` — re-run if you changed the transaction
3. `signAuthEntries(kp)` — for each non-invoker that must authorize
4. `sign(signer, force)` / `signAndSubmit(signer, force)`
5. `result()` (read) or `submit()` then parse

Helpers: `isReadCall()`, `needsNonInvokerSigningBy(includeAlreadySigned)`,
`restoreFootprint()`, `toEnvelopeXdrBase64()`. Pass `force = true` to `signAndSubmit` to submit
a call that simulated as read-only.

### Multi-party authorization

```java
AssembledTransaction<Object> assembled =
    client.invoke("swap", args, submitter.getAccountId(), submitter, null, (int) Transaction.MIN_BASE_FEE);
assembled.signAuthEntries(alice);
assembled.signAuthEntries(bob);
assembled.signAndSubmit(submitter, false);
```

## Uploading / creating contracts

`ContractClient` only invokes functions. To upload Wasm or create a contract, build an
`InvokeHostFunctionOperation` with `SorobanServer` directly (see `operations.md`):
`uploadContractWasmOperationBuilder(wasmBytes)`, `createContractOperationBuilder(...)`,
`createStellarAssetContractOperationBuilder(asset)`. Prepare, sign, send, then read the Wasm ID
/ contract ID from the transaction meta.

## Archived state

If simulation reports archived (expired) ledger entries, restore them. `ContractClient.invoke`
uses `restore = true` by default, which restores automatically when a `signer` is present.
For a no-signer/read-only call it cannot auto-restore: retry the invoke with a `signer`, or
build a `RestoreFootprintOperation` transaction yourself. (`assembled.restoreFootprint()`
requires the signer supplied at construction and throws if it is null.) Extend TTL ahead of
expiry with `ExtendFootprintTTLOperation`.
