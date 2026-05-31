# SEP support

Stellar Ecosystem Proposals supported by the Java SDK. Unlike some SDKs, support is spread
across top-level classes rather than a single `sep` package.

| SEP | Topic | Where |
| --- | --- | --- |
| SEP-02 | Federation | `org.stellar.sdk.federation.Federation` |
| SEP-05 | Key derivation (BIP-39 seed) | `KeyPair.fromBip39Seed(byte[], int)` |
| SEP-10 | Web authentication | `org.stellar.sdk.Sep10Challenge` |
| SEP-23 | Muxed accounts | `org.stellar.sdk.MuxedAccount` |
| SEP-29 | Memo-required check | submission helpers / `AccountRequiresMemoException` |
| SEP-35 | Operation IDs (TOID) | `org.stellar.sdk.TOID` |
| SEP-45 | Soroban web auth | `org.stellar.sdk.Sep45Challenge` |
| SEP-46 | Contract meta | `SorobanServer.getContractMeta` |
| SEP-48 | Contract spec | `SorobanServer.getContractSpec` / `getContractInfo` |
| SEP-51 | XDR JSON encoding | XDR base classes (see `xdr_scval.md`) |
| SEP-53 | Message signing | `KeyPair.signMessage` / `verifyMessage` |

## SEP-02: federation lookup

```java
import org.stellar.sdk.federation.Federation;
import org.stellar.sdk.federation.FederationResponse;

Federation federation = new Federation();
FederationResponse record = federation.resolveAddress("alice*example.com");
System.out.println(record.getAccountId());
// federation.resolveAccountId("G...", "example.com");  // reverse lookup
```

## SEP-05: key derivation from a BIP-39 seed

```java
import org.stellar.sdk.KeyPair;

// bip39Seed is the 64-byte seed derived from a mnemonic (e.g. via a BIP-39 library).
KeyPair kp = KeyPair.fromBip39Seed(bip39Seed, 0); // account index 0
```

## SEP-10: web authentication (server side)

```java
import org.stellar.sdk.Sep10Challenge;
import org.stellar.sdk.Network;
import org.stellar.sdk.TimeBounds;

long now = System.currentTimeMillis() / 1000L;
org.stellar.sdk.Transaction challenge =
    Sep10Challenge.newChallenge(
        serverKeyPair,                  // KeyPair of the server's signing key
        Network.TESTNET,
        "G...",                         // client account id
        "example.com",                  // home domain
        "auth.example.com",             // web auth domain
        new TimeBounds(now, now + 300)); // challenge lifetime

// The client signs the challenge XDR and returns it; then verify (note the argument
// order: serverAccountId, network, homeDomain, webAuthDomain, ...):
Sep10Challenge.ChallengeTransaction read =
    Sep10Challenge.readChallengeTransaction(
        signedChallengeXdr, serverAccountId, Network.TESTNET, "example.com", "auth.example.com");

java.util.Set<String> signers =
    Sep10Challenge.verifyChallengeTransactionThreshold(
        signedChallengeXdr, serverAccountId, Network.TESTNET, "example.com", "auth.example.com",
        threshold, signerSet); // signerSet is a Set<Sep10Challenge.Signer>
```

(Several overloads exist, including ones that add a client domain and memo; check the Javadoc
for the exact argument list you need.)

## SEP-23: muxed accounts

```java
import org.stellar.sdk.MuxedAccount;
import java.math.BigInteger;

MuxedAccount muxed = new MuxedAccount("G...", BigInteger.valueOf(12345));
String mAddress = muxed.getAddress();           // M...
MuxedAccount parsed = new MuxedAccount("M...");  // parse an existing M-address
```

## SEP-35: TOID (total order ID)

```java
import org.stellar.sdk.TOID;

long id = new TOID(ledgerSequence, transactionOrder, operationIndex).toInt64();
TOID toid = TOID.fromInt64(id);
```

## SEP-45: Soroban web authentication

Same idea as SEP-10 but with contract authorization entries, in `org.stellar.sdk.Sep45Challenge`:
`buildChallengeAuthorizationEntries`, `readChallengeAuthorizationEntries`,
`verifyChallengeAuthorizationEntries`.

## SEP-46 / SEP-48: contract introspection

```java
import org.stellar.sdk.SorobanServer;
import org.stellar.sdk.contract.ContractMeta;
import org.stellar.sdk.contract.ContractSpec;
import org.stellar.sdk.contract.ContractInfo;

try (SorobanServer server = new SorobanServer("https://soroban-testnet.stellar.org:443")) {
    ContractMeta meta = server.getContractMeta("C...");  // SEP-46
    ContractSpec spec = server.getContractSpec("C...");  // SEP-48
    ContractInfo info = server.getContractInfo("C...");  // spec + meta
}
```
