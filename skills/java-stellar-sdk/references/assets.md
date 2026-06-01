# Assets: `Asset` vs `ChangeTrustAsset` vs `TrustLineAsset`

The Java SDK models assets with **three distinct types**. Using the wrong one is the most
common compile error when writing transactions. They are not interchangeable, but the latter
two wrap an `Asset`.

| Type | Use it for | Build it with |
| --- | --- | --- |
| `Asset` | Payments, path payments, offers, clawback — anything that moves an asset | `Asset.create(...)`, `Asset.createNativeAsset()` |
| `ChangeTrustAsset` | The `line` of a `ChangeTrustOperation` (a trustline target, which may be a classic asset **or** a liquidity pool) | `new ChangeTrustAsset(asset)` / `new ChangeTrustAsset(liquidityPool)` |
| `TrustLineAsset` | Trustline / balance entries you read back, and `RevokeTrustlineSponsorshipOperation` | `new TrustLineAsset(asset)` / `new TrustLineAsset(liquidityPoolId)` |

## `Asset`

`Asset` is abstract with three concrete subtypes, picked automatically by code length:

- `AssetTypeNative` — native XLM.
- `AssetTypeCreditAlphaNum4` — codes of 1–4 characters.
- `AssetTypeCreditAlphaNum12` — codes of 5–12 characters.

```java
import org.stellar.sdk.Asset;

Asset xlm  = Asset.createNativeAsset();
Asset usdc = Asset.create("USDC", "GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN");
Asset alt  = Asset.create("native");                 // canonical form: "native"
Asset alt2 = Asset.create("USDC:GA5Z...KZVN");       // canonical form: "CODE:ISSUER"
Asset alt3 = Asset.create("credit_alphanum4", "USDC", "GA5Z...KZVN");
```

`Asset.create(String canonicalForm)` accepts `"native"` or `"CODE:ISSUER"`. You generally do
**not** instantiate the subtypes directly.

## `ChangeTrustAsset` (for `ChangeTrustOperation`)

```java
import org.stellar.sdk.ChangeTrustAsset;
import org.stellar.sdk.operations.ChangeTrustOperation;
import java.math.BigDecimal;

ChangeTrustOperation trust =
    ChangeTrustOperation.builder()
        .asset(new ChangeTrustAsset(Asset.create("USDC", "GA5Z...KZVN")))
        .limit(new BigDecimal("1000")) // set limit to "0" to remove the trustline
        .build();
```

A `ChangeTrustAsset` can also wrap a `LiquidityPool` to add a trustline to a pool share.

## `TrustLineAsset` (balances & revoke-trustline-sponsorship)

```java
import org.stellar.sdk.TrustLineAsset;
import org.stellar.sdk.operations.RevokeTrustlineSponsorshipOperation;

TrustLineAsset line = new TrustLineAsset(Asset.create("USDC", "GA5Z...KZVN"));
```

> Note: `SetTrustlineFlagsOperation` and `AllowTrustOperation` take a plain `Asset`, **not** a
> `TrustLineAsset`.

Account balances returned by Horizon expose `getTrustLineAsset()` on each `Balance`, so you can
recover a `TrustLineAsset` directly from a queried account.

## Amounts

Asset amounts and trustline limits are **`BigDecimal`** with up to 7 decimal places. Never use
`double`/`float` — they lose precision. Construct from a string: `new BigDecimal("350.1234567")`.
