# Transactions

## Lifecycle

```java
import org.stellar.sdk.*;
import org.stellar.sdk.operations.PaymentOperation;
import org.stellar.sdk.responses.TransactionResponse;
import java.math.BigDecimal;

Server server = new Server("https://horizon-testnet.stellar.org");
KeyPair kp = KeyPair.fromSecretSeed(System.getenv("STELLAR_SECRET_KEY"));

// 1. Load the source account — this fetches its current sequence number.
TransactionBuilderAccount source = server.loadAccount(kp.getAccountId());

// 2. Build. Each addOperation adds one operation; set a timeout.
Transaction tx =
    new TransactionBuilder(source, Network.TESTNET)
        .setBaseFee(Transaction.MIN_BASE_FEE)
        .addOperation(
            PaymentOperation.builder()
                .destination("GD2J...HPYU")
                .asset(Asset.createNativeAsset())
                .amount(new BigDecimal("10"))
                .build())
        .setTimeout(180)
        .build(); // <-- increments source sequence

// 3. Sign with every required key.
tx.sign(kp);

// 4. Submit.
TransactionResponse response = server.submitTransaction(tx);
```

`setBaseFee` is the fee **per operation**, in stroops (1 XLM = 10,000,000 stroops).
`Transaction.MIN_BASE_FEE` is 100 stroops. The total fee is `baseFee * number_of_operations`.
Soroban transactions add a separate resource fee computed during simulation/preparation (see
`soroban.md`).

### Sequence-number pitfall

`build()` increments the in-memory sequence number of the `source` account object. If you
build a transaction but do not submit it (or submission fails), do **not** reuse the same
account object to build another transaction — reload it:

```java
source = server.loadAccount(kp.getAccountId()); // reload before re-building
```

Reusing a stale sequence causes a `tx_bad_seq` error on submission.

## Memos

```java
import org.stellar.sdk.Memo;

builder.addMemo(Memo.text("note"));            // <= 28 bytes UTF-8
builder.addMemo(Memo.id(123456789L));          // unsigned 64-bit int
// 32-byte hash as a 64-char hex string (or pass a byte[32]):
builder.addMemo(Memo.hash("0000000000000000000000000000000000000000000000000000000000000000"));
builder.addMemo(Memo.returnHash("0000000000000000000000000000000000000000000000000000000000000000"));
```

Only one memo per transaction. Some exchanges require a memo — see SEP-29 in `sep.md`.

## Preconditions

`setTimeout` is the convenience path. For finer control build a `TransactionPreconditions`
and pass it with `addPreconditions(...)`:

```java
import org.stellar.sdk.TimeBounds;
import org.stellar.sdk.LedgerBounds;
import org.stellar.sdk.TransactionPreconditions;

TransactionPreconditions preconditions =
    TransactionPreconditions.builder()
        .timeBounds(new TimeBounds(0, 1700000000L)) // unix seconds; 0 = no bound
        .ledgerBounds(new LedgerBounds(0, 0))
        .minSeqAge(java.math.BigInteger.ZERO)
        .minSeqLedgerGap(0L)
        .build();
builder.addPreconditions(preconditions);
```

`setTimeout` and an explicit `timeBounds` are alternative ways to bound validity time; do not
set both on the same builder.

## XDR round-trips

```java
import org.stellar.sdk.Transaction;
import org.stellar.sdk.Network;

String xdr = tx.toEnvelopeXdrBase64();           // base64 string, safe to store/transmit
Transaction restored = (Transaction) Transaction.fromEnvelopeXdr(xdr, Network.TESTNET);
restored.sign(kp);                               // e.g. a second signer adds their signature
```

## Fee-bump transaction

Wrap an existing (inner) transaction to pay a higher fee on someone else's behalf:

```java
import org.stellar.sdk.FeeBumpTransaction;

FeeBumpTransaction feeBump =
    FeeBumpTransaction.createWithBaseFee(
        feePayer.getAccountId(), // fee source
        200,                     // base fee per op, in stroops
        innerSignedTx);          // the signed inner Transaction
feeBump.sign(feePayer);
server.submitTransaction(feeBump);
```

## Multisig

Add multiple signers to one transaction; collect signatures by passing XDR between parties:

```java
tx.sign(signerA);
String xdr = tx.toEnvelopeXdrBase64();
// ... send xdr to the second holder ...
Transaction tx2 = (Transaction) Transaction.fromEnvelopeXdr(xdr, Network.TESTNET);
tx2.sign(signerB);
server.submitTransaction(tx2);
```

Configure thresholds and signer weights with `SetOptionsOperation`; see `operations.md`.

## Don't

- Don't use `double`/`float` for amounts — use `new BigDecimal("10.5")`.
- Don't omit a timeout / time bound.
- Don't mix `Network.TESTNET` and `Network.PUBLIC`.
- Don't reuse a stale source account after `build()`.
