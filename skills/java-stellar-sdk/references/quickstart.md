# Quickstart

The fast path from zero to a working Stellar app. All amounts are `BigDecimal`; secrets come
from the environment. See `transactions.md` for the full lifecycle.

## Install (Gradle)

```groovy
implementation 'network.lightsail:stellar-sdk:4.0.0-beta0'
```

## Generate or load a keypair

```java
import org.stellar.sdk.KeyPair;

KeyPair kp = KeyPair.random();                                           // new account
KeyPair signer = KeyPair.fromSecretSeed(System.getenv("STELLAR_SECRET_KEY")); // existing
KeyPair publicOnly = KeyPair.fromAccountId("GD2J...HPYU");               // verify-only, cannot sign
```

## Fund a testnet account (Friendbot)

Hit `https://friendbot.stellar.org?addr=<G...>` with any HTTP client. With `HttpURLConnection`
(Java 8):

```java
import java.net.HttpURLConnection;
import java.net.URL;

KeyPair kp = KeyPair.random();
URL url = new URL("https://friendbot.stellar.org?addr=" + kp.getAccountId());
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
conn.getResponseCode(); // triggers the request
```

## Native XLM payment

```java
import java.math.BigDecimal;
import org.stellar.sdk.*;
import org.stellar.sdk.operations.PaymentOperation;

Server server = new Server("https://horizon-testnet.stellar.org");
KeyPair source = KeyPair.fromSecretSeed(System.getenv("STELLAR_SECRET_KEY"));
TransactionBuilderAccount account = server.loadAccount(source.getAccountId());

Transaction tx =
    new TransactionBuilder(account, Network.TESTNET)
        .setBaseFee(Transaction.MIN_BASE_FEE)
        .addOperation(
            PaymentOperation.builder()
                .destination("GD2JXEFGEO53CNQ22KN2ICOQ2LOASCABQHAIOMLZV265C246PFKKHPYU")
                .asset(Asset.createNativeAsset())
                .amount(new BigDecimal("100.5"))
                .build())
        .setTimeout(30)
        .build();
tx.sign(source);
System.out.println(server.submitTransaction(tx).getHash());
server.close();
```

## Custom (non-native) asset payment

The recipient must already hold a trustline to the asset (see `ChangeTrustOperation` in
`operations.md`).

```java
import org.stellar.sdk.Asset;

Asset usdc = Asset.create("USDC", "GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN");
// PaymentOperation.builder().destination(dest).asset(usdc).amount(new BigDecimal("25.0")).build();
```

## Basic Horizon query

```java
import org.stellar.sdk.Server;
import org.stellar.sdk.responses.AccountResponse;

Server server = new Server("https://horizon.stellar.org");
AccountResponse account = server.accounts().account("GB6N...Y4AQ");
System.out.println(account.getBalances());
```

## Basic Soroban contract call (read-only)

```java
import java.util.List;
import org.stellar.sdk.Network;
import org.stellar.sdk.Transaction;
import org.stellar.sdk.contract.ContractClient;
import org.stellar.sdk.scval.Scv;

try (ContractClient client =
    new ContractClient(
        "CACZTW72246RA2MOCNKUBRRRRPT26UZ7LXE5ZHH44OGKIMCTQJ74O4D5",
        "https://soroban-testnet.stellar.org:443",
        Network.TESTNET)) {
    // A source account id is required (no signer needed). It must already exist on the
    // network — simulate() loads its sequence and 404s with AccountNotFoundException.
    String source = "GD2JXEFGEO53CNQ22KN2ICOQ2LOASCABQHAIOMLZV265C246PFKKHPYU";
    Object result =
        client
            .invoke("hello", List.of(Scv.toSymbol("world")), source, null, null,
                (int) Transaction.MIN_BASE_FEE)
            .result();
}
```

Learn more: https://javadoc.io/doc/network.lightsail/stellar-sdk
