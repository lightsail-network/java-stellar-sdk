# Troubleshooting

## Exception hierarchy

All SDK exceptions derive from `org.stellar.sdk.exception.SdkException` (a
`RuntimeException`). Network/RPC exceptions live in `org.stellar.sdk.exception`; the
high-level contract-client exceptions thrown by `ContractClient`/`AssembledTransaction`
live in `org.stellar.sdk.contract.exception` (and also extend `SdkException`).

Network / Horizon / RPC (subclasses of `NetworkException`):
- `ConnectionErrorException` — could not reach the server.
- `BadRequestException` — 4xx (except 429); e.g. account/transaction not found (404) or a
  rejected transaction (400). Inspect the problem details for `result_codes`.
- `BadResponseException` — 5xx / unexpected response.
- `TooManyRequestsException` — 429 rate limited.
- `RequestTimeoutException` — the request timed out.

Soroban / contract (RPC-level, in `org.stellar.sdk.exception`):
- `SorobanRpcException` — the RPC returned an error.
- `PrepareTransactionException` — simulation/preparation failed; inspect
  `e.getSimulateTransactionResponse()`. (Extends `SdkException` directly, not
  `NetworkException`.)
- `AccountNotFoundException` — account does not exist on the network.

Contract client lifecycle (in `org.stellar.sdk.contract.exception`, all extend
`AssembledTransactionException` → `SdkException`):
- `SimulationFailedException` — the simulation step failed.
- `ExpiredStateException` — ledger entries are archived/expired and need restoring.
- `NotYetSimulatedException`, `RestorationFailureException`,
  `SendTransactionFailedException`, `TransactionStillPendingException`,
  `TransactionFailedException`, `NeedsMoreSignaturesException`, `NoSignatureNeededException`.

Other:
- `AccountRequiresMemoException` — destination requires a memo (SEP-29).
- `InvalidSep10ChallengeException`, `InvalidSep45ChallengeException` — challenge validation.
- `UnexpectedException`, `UnknownResponseException`.

```java
import org.stellar.sdk.exception.*;

try {
    server.submitTransaction(tx);
} catch (BadRequestException e) {
    System.out.println(e.getMessage()); // includes result codes for rejected submissions
} catch (NetworkException e) {
    System.out.println(e.getMessage());
}
```

## Reading submission failures

A failed Horizon submission throws `BadRequestException`; the useful detail is the
transaction/operation result codes in the error's problem extras. Common transaction-level
codes:

| Code | Meaning | Fix |
| --- | --- | --- |
| `tx_bad_seq` | stale sequence number | reload the source account before building (see `transactions.md`) |
| `tx_bad_auth` | missing/insufficient signatures | sign with all required keys; check thresholds |
| `tx_insufficient_fee` | fee too low | raise `setBaseFee` |
| `tx_too_late` / `tx_too_early` | outside time bounds | rebuild with a valid timeout |
| `tx_failed` | an operation failed | inspect per-operation codes |

Operation-level codes you'll hit often:
- `op_no_trust` / `op_no_issuer` — recipient lacks a trustline for the asset (add a
  `ChangeTrustOperation`).
- `op_underfunded` — source lacks the balance.
- `op_no_destination` — destination account does not exist (create it first).
- `op_low_reserve` — would drop below the minimum XLM reserve.

## Soroban issues

- **Simulation failed** → `PrepareTransactionException`; read
  `e.getSimulateTransactionResponse().getError()` for the diagnostic.
- **State expired/archived** → restore it: `ContractClient.invoke` restores automatically when
  a `signer` is present. For a no-signer/read-only call it cannot auto-restore — retry the
  invoke with a `signer`, or build a `RestoreFootprintOperation` transaction yourself.
  (`assembled.restoreFootprint()` requires the signer supplied at construction and throws if it
  is null.) See `soroban.md`.
- **Authorization missing** → a non-invoker must sign auth entries; use
  `assembled.signAuthEntries(theirKeyPair)` before `signAndSubmit(...)`.
- **Transaction still pending** → `getTransaction` returns status `NOT_FOUND` until it is in a
  ledger; poll with `pollTransaction` or a loop. See `soroban.md`.
