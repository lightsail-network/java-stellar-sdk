# Changelog

As this project is pre 1.0, breaking changes may happen for minor version bumps. A breaking change will get clearly notified in this log.

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
