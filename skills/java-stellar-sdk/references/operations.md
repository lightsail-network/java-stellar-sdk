# Operations

Each operation lives in `org.stellar.sdk.operations` and is built with a Lombok builder, then
added to a transaction via `TransactionBuilder.addOperation(op)`. Every operation builder
optionally accepts `.sourceAccount(accountId)` to set a per-operation source account (defaults
to the transaction source). Amounts and balances are **`BigDecimal`**.

```java
import org.stellar.sdk.operations.PaymentOperation;
import org.stellar.sdk.Asset;
import java.math.BigDecimal;

PaymentOperation op =
    PaymentOperation.builder()
        .destination("GD2J...HPYU")
        .asset(Asset.createNativeAsset())
        .amount(new BigDecimal("10"))
        .build();
```

## Payments & accounts

| Operation class | Purpose |
| --- | --- |
| `CreateAccountOperation` | Create & fund a new account (`startingBalance`) |
| `PaymentOperation` | Send an asset |
| `PathPaymentStrictSendOperation` | Send exact amount, receive ≥ min |
| `PathPaymentStrictReceiveOperation` | Receive exact amount, send ≤ max |
| `AccountMergeOperation` | Merge account into destination |
| `BumpSequenceOperation` | Bump the account sequence number |
| `ManageDataOperation` | Set/remove an account data entry |

## Trustlines & flags

| Operation class | Purpose |
| --- | --- |
| `ChangeTrustOperation` | Create/update/remove a trustline (`limit` `"0"` removes) |
| `SetTrustlineFlagsOperation` | Authorize/freeze a trustline (`trustor`, `asset`, clear/set flags) |
| `AllowTrustOperation` | Legacy authorize (prefer `SetTrustlineFlagsOperation`) |
| `SetOptionsOperation` | Signers, thresholds, home domain, flags, inflation dest |

`ChangeTrustOperation` takes a `ChangeTrustAsset` (not a plain `Asset`). Note that
`SetTrustlineFlagsOperation` and `AllowTrustOperation` take a plain `Asset`, while
`TrustLineAsset` is used for balances and `RevokeTrustlineSponsorshipOperation`. See
`assets.md` for the three asset types and when to use each.

```java
import org.stellar.sdk.ChangeTrustAsset;
import org.stellar.sdk.operations.ChangeTrustOperation;

ChangeTrustOperation trust =
    ChangeTrustOperation.builder()
        .asset(new ChangeTrustAsset(Asset.create("USDC", "GA5Z...ZVN")))
        .limit(new BigDecimal("1000"))
        .build();
```

## Offers & liquidity pools

| Operation class | Purpose |
| --- | --- |
| `ManageSellOfferOperation` | Create/update/delete a sell offer |
| `ManageBuyOfferOperation` | Create/update/delete a buy offer |
| `CreatePassiveSellOfferOperation` | Passive sell offer |
| `LiquidityPoolDepositOperation` | Deposit into an AMM pool |
| `LiquidityPoolWithdrawOperation` | Withdraw from an AMM pool |

## Claimable balances

| Operation class | Purpose |
| --- | --- |
| `CreateClaimableBalanceOperation` | Create a claimable balance (`asset`, `amount`, `claimants`) |
| `ClaimClaimableBalanceOperation` | Claim one (`balanceId`) |

## Sponsorship (wrap the sponsored ops between begin/end)

`BeginSponsoringFutureReservesOperation` … sponsored ops …
`EndSponsoringFutureReservesOperation`.

Revoke existing sponsorships with the matching class:
`RevokeAccountSponsorshipOperation`, `RevokeTrustlineSponsorshipOperation`,
`RevokeOfferSponsorshipOperation`, `RevokeDataSponsorshipOperation`,
`RevokeClaimableBalanceSponsorshipOperation`, `RevokeSignerSponsorshipOperation`.

## Clawback

`ClawbackOperation` (`asset`, `from`, `amount`), `ClawbackClaimableBalanceOperation`
(`balanceId`). Requires the asset's clawback flag.

## Soroban operations

See `soroban.md` for the full lifecycle; `InvokeHostFunctionOperation` exposes static builder
factories for every host function:

| Static builder | Purpose |
| --- | --- |
| `InvokeHostFunctionOperation.invokeContractFunctionOperationBuilder(contractId, functionName, parameters)` | Call a contract function |
| `InvokeHostFunctionOperation.uploadContractWasmOperationBuilder(wasmBytes)` | Upload Wasm bytecode |
| `InvokeHostFunctionOperation.createContractOperationBuilder(wasmId, address, constructorArgs, salt)` | Instantiate a contract |
| `InvokeHostFunctionOperation.createStellarAssetContractOperationBuilder(asset)` | Deploy the SAC for a classic asset |
| `ExtendFootprintTTLOperation` | Extend ledger entry TTL |
| `RestoreFootprintOperation` | Restore archived state |

Soroban operations must be **simulated/prepared** before signing so the footprint, auth, and
resource fee are populated.
