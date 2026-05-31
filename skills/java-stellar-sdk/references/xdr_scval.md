# XDR, SCVal & Addresses

## SCVal conversion (`org.stellar.sdk.scval.Scv`)

Soroban contract arguments and return values are `SCVal` XDR objects. Build them with
`Scv.to*` and read them with `Scv.from*`.

```java
import org.stellar.sdk.scval.Scv;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

// To SCVal (for contract arguments)
Scv.toBoolean(true);
Scv.toVoid();
Scv.toUint32(7L);       Scv.toInt32(-7);
Scv.toUint64(BigInteger.valueOf(7));   Scv.toInt64(-7L);
Scv.toUint128(BigInteger.valueOf(7));  Scv.toInt128(BigInteger.valueOf(-7));
Scv.toUint256(BigInteger.valueOf(7));  Scv.toInt256(BigInteger.valueOf(-7));
Scv.toTimePoint(BigInteger.valueOf(1700000000)); Scv.toDuration(BigInteger.valueOf(3600));
Scv.toBytes(new byte[] {1, 2});
Scv.toString("hello");  Scv.toSymbol("increment");
Scv.toAddress("G...");                 // account or contract ("C...") address
Scv.toVec(List.of(Scv.toUint32(1L), Scv.toUint32(2L)));
Scv.toMap(Map.of(Scv.toSymbol("k"), Scv.toUint32(1L)));
```

```java
// From SCVal (parsing results)
Scv.fromUint32(v);      // -> long
Scv.fromInt128(v);      // -> BigInteger
Scv.fromString(v);      // -> byte[]; new String(bytes, StandardCharsets.UTF_8) for text
Scv.fromSymbol(v);      // -> String
Scv.fromAddress(v);     // -> Address
Scv.fromVec(v);         // -> Collection<SCVal>
Scv.fromMap(v);         // -> LinkedHashMap<SCVal, SCVal>
```

`Scv.toMap` sorts entries by key per Soroban's runtime ordering rules (keys must ascend).

## Addresses

```java
import org.stellar.sdk.Address;
import org.stellar.sdk.xdr.SCVal;

Address addr = new Address("G...");      // account or "C..." contract
SCVal scVal = addr.toSCVal();            // SCVal for contract args
Address back = Address.fromSCVal(scVal);
```

## Transaction XDR

```java
import org.stellar.sdk.Transaction;
import org.stellar.sdk.Network;

String xdr = tx.toEnvelopeXdrBase64();
Transaction te = (Transaction) Transaction.fromEnvelopeXdr(xdr, Network.PUBLIC);
```

## Lower-level XDR types

Generated XDR types live in `org.stellar.sdk.xdr` (e.g. `org.stellar.sdk.xdr.TransactionMeta`,
`org.stellar.sdk.xdr.SCVal`). Most carry `fromXdrBase64(...)` / `toXdrBase64()` for base64 and
`fromXdrByteArray(...)` / `toXdrByteArray()` for raw bytes. Use these to debug result/meta XDR,
e.g. decoding a `resultMetaXdr` returned by Soroban RPC:

```java
import org.stellar.sdk.xdr.TransactionMeta;

TransactionMeta meta = TransactionMeta.fromXdrBase64(resultMetaXdrBase64);
```

## SEP-51 JSON

XDR base classes support JSON encoding/decoding (SEP-51) in addition to base64/bytes, useful
for inspecting structures in a readable form.
