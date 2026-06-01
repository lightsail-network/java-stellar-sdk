# Generated contract bindings

For any non-trivial Soroban contract work, **generate a typed Java client** instead of
hand-writing `Scv` conversions and `ContractClient.invoke(...)` calls. The generated client is
type-safe, mirrors the contract's functions and custom types, and removes a whole class of
encoding bugs.

Tool: [`stellar-contract-bindings`](https://github.com/lightsail-network/stellar-contract-bindings)
(supports Java, Python, Flutter/Dart, PHP, Swift). There is also a web generator at
https://stellar-contract-bindings.fly.dev/.

## Generate

```bash
pip install stellar-contract-bindings

stellar-contract-bindings java \
  --contract-id CDOAW6D7NXAPOCO7TFAWZNJHK62E3IYRGNRVX3VOXNKNVOXCLLPJXQCF \
  --rpc-url https://mainnet.sorobanrpc.com \
  --output ./bindings \
  --package com.example
```

This writes a `Client` class (plus any custom struct/enum/error types the contract defines)
into the given package. The generated `Client extends ContractClient`, so it is `Closeable` and
shares the same lifecycle as the hand-written client.

## Use

Each contract function becomes a typed method that returns an
`AssembledTransaction<ReturnType>`. The trailing arguments are the same as
`ContractClient.invoke`: `source` account id, `signer` (nullable for read-only), and `baseFee`.

```java
import java.util.List;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Network;
import org.stellar.sdk.contract.AssembledTransaction;
import com.example.Client; // generated

KeyPair kp = KeyPair.fromSecretSeed(System.getenv("STELLAR_SECRET_KEY"));

try (Client client =
    new Client(
        "CDOAW6D7NXAPOCO7TFAWZNJHK62E3IYRGNRVX3VOXNKNVOXCLLPJXQCF",
        "https://mainnet.sorobanrpc.com",
        Network.PUBLIC)) {

    // Read-only: signer can be null; read the simulated result.
    AssembledTransaction<List<byte[]>> readTx =
        client.hello("World".getBytes(), kp.getAccountId(), null, 100);
    List<byte[]> value = readTx.result();

    // State-changing: pass a signer, then submit. The exact method name, argument
    // types, and AssembledTransaction<T> result type come from the contract's spec.
    client.increment(kp.getAccountId(), kp, 100).signAndSubmit(kp, false);
}
```

The returned `AssembledTransaction<T>` is exactly the one documented in `soroban.md`, so all of
its helpers apply: `result()`, `sign(...)`, `signAuthEntries(...)`,
`needsNonInvokerSigningBy(...)`, `restoreFootprint()`, `signAndSubmit(...)`, `submit()`.

## When to hand-write instead

Use `ContractClient.invoke(...)` directly (see `soroban.md`) only when you cannot run the code
generator — e.g. a one-off call, or a contract whose spec you do not have locally. For anything
you maintain, regenerate the bindings when the contract interface changes.
