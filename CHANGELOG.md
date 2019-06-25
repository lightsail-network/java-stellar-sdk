# Changelog

As this project is pre 1.0, breaking changes may happen for minor version bumps. A breaking change will get clearly notified in this log.

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
