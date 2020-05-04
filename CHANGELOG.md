# Changelog

As this project is pre 1.0, breaking changes may happen for minor version bumps. A breaking change will get clearly notified in this log.

## 0.16.0

* Update XDR definitions and auto-generated classes to support upcoming protocol 13 release. (https://github.com/stellar/java-stellar-sdk/pull/276)
* Extend StrKey implementation to handle [CAP 27 Muxed Accounts](https://github.com/stellar/stellar-protocol/blob/master/core/cap-0027.md) ([#276](https://github.com/stellar/java-stellar-sdk/pull/276)).
* Update `TransactionResponse` to include new fields which are relevant to [CAP 15 Fee-Bump Transactions](https://github.com/stellar/stellar-protocol/blob/master/core/cap-0015.md). (https://github.com/stellar/java-stellar-sdk/pull/275)
* Update `AccountResponse.Balance`, `AllowTrustOperationResponse`, and create `TrustlineAuthorizedToMaintainLiabilitiesEffectResponse` to support [CAP 18 Fine-Grained Control of Authorization](https://github.com/stellar/stellar-protocol/blob/master/core/cap-0018.md). (https://github.com/stellar/java-stellar-sdk/pull/274)
* Add `FeeBumpTransaction` and `FeeBumpTransaction.Builder` for parsing and creating [CAP 15 Fee-Bump Transactions](https://github.com/stellar/stellar-protocol/blob/master/core/cap-0015.md) ([#278](https://github.com/stellar/java-stellar-sdk/pull/278)).
* Add methods to `Server` for submitting [CAP 15 Fee-Bump Transactions](https://github.com/stellar/stellar-protocol/blob/master/core/cap-0015.md) ([#278](https://github.com/stellar/java-stellar-sdk/pull/278)).
* Update SEP 10 implementation to reject fee-bump transactions and transactions with multiplexed addresses ([#278] (https://github.com/stellar/java-stellar-sdk/pull/278)).
* Update SEP 29 implementation to handle bump transactions ([#278](https://github.com/stellar/java-stellar-sdk/pull/278)).


## 0.15.0

- Add SEP0029 (memo required) support. (https://github.com/stellar/java-stellar-sdk/issues/272)

  Extends `Server.submitTransaction` to always run a memo required check before
  sending the transaction.  If any of the destinations require a memo and the
  transaction doesn't include one, then an `AccountRequiresMemoError` will be thrown.

  You can skip this check by passing a true `skipMemoRequiredCheck` value to `Server.submitTransaction`:

  ```
  server.submitTransaction(tx, true)
  ```

  The check runs for each operation of type:
   - `payment`
   - `pathPaymentStrictReceive`
   - `pathPaymentStrictSend`
   - `mergeAccount`

  If the transaction includes a memo, then memo required checking is skipped.

  See [SEP0029](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0029.md) for more information about memo required check.

## 0.14.0

* Update challenge transaction helpers for SEP-10 v1.3.0 (https://github.com/stellar/java-stellar-sdk/issues/263).
* Add support for /accounts end-point with ?signer and ?asset filters (https://github.com/stellar/java-stellar-sdk/issues/261).
* Add support for /offers end-point with query parameters (https://github.com/stellar/java-stellar-sdk/issues/261).
* Regenerate the XDR definitions to include MetaV2 support (https://github.com/stellar/java-stellar-sdk/issues/261).

## 0.13.0

* Horizon v0.24.0 added a `fee_charged` and `max_fee` object with information about max bid and actual fee paid for each transaction.
* We are removing ``*_all_accepted_fee` fields in favor of the new keys, making it easier for people to understand the meaning the fields.

## 0.12.0

* Represent memo text contents as bytes because a memo text may not be valid UTF-8 string (https://github.com/stellar/java-stellar-sdk/issues/257).
* Validate name length when constructing org.stellar.sdk.ManageDataOperation instances.
* Validate home domain length when constructing org.stellar.sdk.SetOptionsOperation instances.

## 0.11.0

* Fix bug in `org.stellar.sdk.requests.OperationsRequestBuilder.operation(long operationId)`. The method submitted an HTTP request to Horizon with the following path, /operation/<id> , but the correct path is /operations/<id>
* Rename `org.stellar.sdk.requests.PathsRequestBuilder` to `org.stellar.sdk.requests.StrictReceivePathsRequestBuilder`
* Add `sourceAssets()` to `org.stellar.sdk.requests.StrictReceivePathsRequestBuilder` which allows a list of assets to be provided instead of a source account
* Add `org.stellar.sdk.requests.StrictSendPathsRequestBuilder` which is the request builder for the /paths/strict-send endpoint
* Removed deprecated classes: `org.stellar.sdk.PathPaymentOperation` and `org.stellar.sdk.responses.operations.PathPaymentOperationResponse`
* The `fee_paid` field in the Horizon transaction response will be removed when Horizon 0.25 is released. The `fee_paid` field has been replaced by `max_fee`, which defines the maximum fee the source account is willing to pay, and `fee_charged`, which defines the fee that was actually paid for a transaction. Consequently, `getFeePaid()` has been removed from `org.stellar.sdk.responses.Transaction` and has been replaced with `getMaxFee()` and `getFeeCharged()`.

## 0.10.0

### Deprecations

The following methods are deprecated and will be removed in 0.11.0. Please switch to new methods and classes.

Deprecated | New method/class
-|-
`org.stellar.sdk.PathPaymentOperation`                              | `org.stellar.sdk.PathPaymentStrictReceiveOperation`
`org.stellar.sdk.responses.operations.PathPaymentOperationResponse` | `org.stellar.sdk.responses.operations.PathPaymentStrictReceiveOperationResponse`

### Changes

* Add helper method to generate SEP 10 challenge
* Stellar Protocol 12 compatibility.
* Include `path` property in path payment operation responses.
* Provide `includeTransactions()` method for constructing operations requests which include transaction data in the operations response.
* Provide `includeTransactions()` method for constructing payments requests which include transaction data in the payments response.

## 0.9.0
* Use strings to represent account ids instead of KeyPair instances because account ids will not necessarily be valid
  public keys. If you try to parse an invalid public key into a KeyPair you will encounter an exception. To prevent
  exceptions when parsing horizon responses it is better to represent account ids as strings

## 0.8.0

* Removed deprecated methods and classes listed in the 0.7.0 changelog entry
* Configure network at the transaction and server level rather than using a singleton to determine
  which network to use globally.
* Implement hashCode() and equals() on Stellar classes and XDR generated classes
* Add streaming for account Offers
* Add callback to handle SSE failures


## 0.7.0

### Deprecations

The following methods are deprecated and will be removed in 0.8.0. Please switch to new methods and classes.

Deprecated | New method/class
-|-
`org.stellar.sdk.Server#operationFeeStats` | `org.stellar.sdk.Server#feeStats`
`org.stellar.sdk.requests.OperationFeeStatsRequestBuilder` | `org.stellar.sdk.requests.FeeStatsRequestBuilder`
`org.stellar.sdk.responses.OperationFeeStatsResponse` | `org.stellar.sdk.responses.FeeStatsResponse`
`org.stellar.sdk.responses.operations.CreatePassiveOfferOperationResponse` | `org.stellar.sdk.responses.operations.CreatePassiveSellOfferOperationResponse`
`org.stellar.sdk.responses.operations.ManageOfferOperationResponse` | `org.stellar.sdk.responses.operations.ManageOfferSellOperationResponse`
`org.stellar.sdk.CreatePassiveOfferOperation` | `org.stellar.sdk.CreatePassiveSellOfferOperation`
`org.stellar.sdk.ManageOfferOperation` | `org.stellar.sdk.ManageSellOfferOperation`

### Changes

* Stellar Protocol 11 compatibility (#199).
* Compatibility with Horizon API updates (#205).
* Add Support for `InflationOperation` in `Operation.fromXdr` (#194).
* Fixed exception thrown from `ManageOfferOperation.fromXDR` for some offers (#188).
* Send Horizon client fingerprint (#190).
* `Server` now implements `Closeable` interface (#182).
* Fixed `/order_book` endpoint streaming.

## 0.6.0

* Horizon 0.17.0 features (#180)
* Enable setting custom base fee (#177)

## 0.5.0

* Horizon 0.16.0 features (#172)
* Allow no signatures in Transaction.toEnvelopeXdr (#164)
* Fix dependencies shadowing (#173)

## 0.4.1

* Fixed streaming issues.

## 0.4.0

* **Breaking change** `Transaction.Builder` requires `setTimeout` method to be called.
* Added [Horizon 0.15.0](https://github.com/stellar/go/releases/tag/horizon-v0.15.0) features.
* Improved streaming code and dependencies (thanks @jillesvangurp!).
* SEP-0005 derivation (thanks @westonal!).

## 0.3.2

* Non `ed25519` keys are now supported in all responses (fixes #126):
  * `SetOptionsOperationResponse.getSigner` is deprecated. Please use `SetOptionsOperationResponse.getSignerKey`.
  * `AccountResponse.Signer.getAccountId` is deprecated. Please use `AccountResponse.Signer.getKey`.
* Fixed `PathPaymentOperationResponse.getSourceAsset` method (#125).

## 0.3.1

* Fixed condition check in `TimeBounds` when `maxTime` is equal `0`.

## 0.3.0

* Protocol V10 updates:
    * `BumpSequence` operation support ([CAP-0001](https://github.com/stellar/stellar-protocol/blob/master/core/cap-0001.md),
    * Asset liabilities ([CAP-0003](https://github.com/stellar/stellar-protocol/blob/master/core/cap-0003.md),
    * New Horizon fields and effects.
    * Updated XDR classes.
* `Transaction` now has methods to create it from previously built `TransactionEnvelope`.
* Improved `Server.submitTransaction` method, now throws exception for timeouts.

## 0.2.2

* Fixed `MemoReturnHash#toXdr()` method.
* Patch for Horizon [Timeout](https://www.stellar.org/developers/horizon/reference/errors/timeout.html) responses (`SubmitTransactionResponse.getEnvelopeXdr()` and `SubmitTransactionResponse.getResultXdr()`).

## 0.2.1

* Full compatibility with Horizon 0.13.0 [API](https://github.com/stellar/go/blob/master/protocols/horizon/README.md).
* Fixed `Page.getNextPage` #66.

## 0.2.0

* Java SDK now works in Android!
* `Price` constructor is now public.
* Added support for new endpoints and fields added in Horizon v0.12.0.

## 0.1.14

* Fixed `pad()` method in `XdrInputStream`.
* Added utf8 string support for `XdrDataInputStream` and `XdrDataOutputStream`.
* Fixed `AllowTrustOperation.Builder` for `ASSET_TYPE_CREDIT_ALPHANUM12` assets.

## 0.1.13

* `KeyPair.sign` now throws `RuntimeException` when `KeyPair` object does not contain a secret key.
* `SubmitTransactionResponse.getOfferIdFromResult` if offer was taken and `tx_result` does not contain offer ID.

## 0.1.12

- Regenerated XDR classes:
  - `XdrDataOutputStream` class is now padding opaque data.
  - `XdrDataInputStream` class is now throwing an IOException when padding
  bytes are not zeros.
  - Made methods that shouldn't be used outside of `XdrDataOutputStream`
  and `XdrDataInputStream` classes private.
  - Removed unused imports and variables.

## 0.1.11

* Added ability to set TimeBounds using Transaction.Builder
* Implemented `/order_book` and `/order_book/trades` requests.

## 0.1.10

* Fixed a bug in `AssetDeserializer`.
* Fixed a bug in `TransactionResponse`.

## 0.1.9

* Support for new signer types: `sha256Hash`, `preAuthTx`.
* **Breaking change** `Network` must be explicitly selected. Previously testnet was a default network.

## 0.1.8

* New location of `stellar.toml` file

## 0.1.7

* Support for `ManageData` operation ([c5faa15](https://github.com/stellar/java-stellar-sdk/commit/c5faa1578defb6f74513f8f96521c64dbfa6f6f2)),
* `AccountFlag` enum ([4b67e2d](https://github.com/stellar/java-stellar-sdk/commit/4b67e2dd6492c14f833d10532de211382dc04efc)).

## 0.1.6

* Fixed a bug in `OffersRequestBuilder.execute(URI uri)` 
([799f0df](https://github.com/stellar/java-stellar-sdk/commit/799f0dfc7496849e3426706cfc2163379be80918)).

## 0.1.5

* Offers for Accounts ([d5225bc](https://github.com/stellar/java-stellar-sdk/commit/d5225bc4f8613bf1f1fd2a45893f0b5ca4933e76)),
* Add Helper methods for `SubmitTransactionResponse` ([6d14284](https://github.com/stellar/java-stellar-sdk/commit/6d14284fb69f834d6ef1fe3205b267ed63a0eeb4)).

## 0.1.4

* Update and fix XDR ([0b404d9](https://github.com/stellar/java-stellar-sdk/commit/0b404d9)).

## 0.1.3

* Added ResultCodes to SubmitTransactionResponse ([79e2260](https://github.com/stellar/java-stellar-sdk/commit/79e2260)).

## 0.1.2

* Rate limiting data in responses ([e0ee7f1](https://github.com/stellar/java-stellar-sdk/commit/e0ee7f15e3b21fe156e89991629d019c511c4676)).
* Fixed a bug in `Transaction.Builder.build()` ([657c720](https://github.com/stellar/java-stellar-sdk/commit/657c720)).

## 0.1.1

* `Transaction.Builder.addOperation` is now thread safe ([248a4a1](https://github.com/stellar/java-stellar-sdk/commit/248a4a16eab6e0a3d22416a003ab79e077dcc9ba)).
* Added `hashCode` method for `Asset` classes ([ab822e5](https://github.com/stellar/java-stellar-sdk/commit/ab822e563df1e955ca3d9a6af923128eb2ba0de6)).
* `FederationResponse` constructor is now public. ([c4c5a4d](https://github.com/stellar/java-stellar-sdk/commit/c4c5a4de6167bdcc1ce2a3e0f5e3acf5df06eed4)).
* Updates to javadoc.

## 0.1.0

* **Breaking change** Merged [java-stellar-base](https://github.com/stellar/java-stellar-base) and [java-stellar-sdk](https://github.com/stellar/java-stellar-sdk). More info in [#19](https://github.com/stellar/java-stellar-sdk/pull/19).
