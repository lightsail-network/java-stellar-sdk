# Changelog

As this project is pre 1.0, breaking changes may happen for minor version bumps. A breaking change will get clearly notified in this log.

## Pending

## 0.40.0
* Add strkey support for contract ids ([#471](https://github.com/stellar/java-stellar-sdk/pull/471))
* Fix NPE in `KeyPair.equals()` method ([#474](https://github.com/stellar/java-stellar-sdk/pull/474))
* Avoid to have unexpected exception on status code 429 TooManyRequests ([#433](https://github.com/stellar/java-stellar-sdk/pull/433))

## 0.39.0

* Add SubmitTransactionResponse.ResultCodes.innerTransactionResultCode ([#466](https://github.com/stellar/java-stellar-sdk/pull/466)) 

## 0.38.0

* Fix android crashing related to okhttp3 ([#457](https://github.com/stellar/java-stellar-sdk/pull/457))

## 0.37.2

* Fix the deployment CI that uploads the jar to the GitHub release [2]. ([#453](https://github.com/stellar/java-stellar-sdk/pull/453))

## 0.37.1

* Fix the deployment CI that uploads the jar to the GitHub release. ([#452](https://github.com/stellar/java-stellar-sdk/pull/452))

## 0.37.0

* Fix missing `auth_clawback_enabled` field in AccountResponse class. ([#449](https://github.com/stellar/java-stellar-sdk/pull/449))

## 0.36.0

* Fix bug in `KeyPair.fromSecretSeed(char[] seed)`. ([#447](https://github.com/stellar/java-stellar-sdk/pull/447))
* Shade kotlin dependencies to prevent 'Duplicate class' errors. ([#448](https://github.com/stellar/java-stellar-sdk/pull/448))

## 0.35.0

* Update JDK compatibility version from Java 1.6 to Java 1.8 and bump the version of few libraries ([#444](https://github.com/stellar/java-stellar-sdk/pull/444)):
  * com.squareup.okhttp3 from `v3.11.0` to `v4.10.0`. 
  * commons-io:commons-io from `v2.6` to `v2.11.0`.
  * junit:junit from `v4.12` to `v4.13.2`.
  * org.threeten:threetenbp from `v1.4.4` to `v1.6.0`.
  * org.mockito:mockito-core from `v2.21.0` to `v4.6.1`.
  * javax.xml.bind:jaxb-api from `v2.3.0` to `v2.3.1`.

## 0.34.2

* Bump gson version from `v2.8.5` to `v2.9.0`. ([#443](https://github.com/stellar/java-stellar-sdk/pull/443))

## 0.34.1

* Fix the `Sep10Challenge.verifyTransactionSignatures` method to handle/ignore signers that are not ed25519 compliant. ([#440](https://github.com/stellar/java-stellar-sdk/pull/440))

## 0.34.0
* Add memo to `Sep10Challenge.newChallenge()` and `Sep10Challenge.readChallengeTransaction`. ([#435](https://github.com/stellar/java-stellar-sdk/pull/435))

## 0.33.0 
* Update TransactionResponse to include new Protocol 19 Preconditions ([#428](https://github.com/stellar/java-stellar-sdk/pull/428)).
* Fix asset compare to when asset code are equals ([#424](https://github.com/stellar/java-stellar-sdk/pull/424)).
* LiquidityPoolIDDeserializer is missing from the PageDeserializer. ([#422](https://github.com/stellar/java-stellar-sdk/pull/422))

## 0.32.0

* Update XDR definitions and auto-generated classes to support upcoming protocol 19 release ([#416](https://github.com/stellar/java-stellar-sdk/pull/416)).
* Extend StrKey implementation to handle [CAP 40 Payload Signer](https://github.com/stellar/stellar-protocol/blob/master/core/cap-0040.md).
* Extended Transaction submission settings, additional new Preconditions can be added now, refer to [CAP 21 Transaction Preconditions](https://github.com/stellar/stellar-protocol/blob/master/core/cap-0021.md).

### Breaking changes

* org.stellar.sdk.Transaction.Builder
  * deprecated `addTimeBounds()` use `addPreconditions()` instead
  * deprecated `setTimeout()` use `addPreconditions()` instead
  * deprecated `Transaction.Builder` use TransactionBuilder instead
* org.stellar.sdk.Transaction
  * `getSignatures()` returns an ImmutableList of signatures, do NOT add signatures to the collection returned.
    use `addSignature(DecoratedSignature signature)` instead.

## 0.31.0

* Fixed NPE on TrustlineCreatedEffectResponse.getAsset() for liquidity pool asset type.
  Consolidated Asset factory creation pattern, made consistent for all asset types including native, alpha4, alpha12, liquidity pool shares.
  ([#398](https://github.com/stellar/java-stellar-sdk/pull/398)).

### Breaking changes
* org.stellar.sdk.Asset.createNonNativeAsset() is now private. ([#398](https://github.com/stellar/java-stellar-sdk/pull/398)).
* org.stellar.sdk.responses.effects.TrustlineCUDResponse, removed non-default public constructor, it wasn't needed. ([#398](https://github.com/stellar/java-stellar-sdk/pull/398)).
* Muxed accounts are now supported by default. Previously we added opt in support for muxed accounts. But now we are changing the default behavior so that muxed accounts are rendered using their 'M' address encoding ([#399](https://github.com/stellar/java-stellar-sdk/pull/399)).

## 0.30.0

* Fix missing Liquidity Pool ID in AccountResponse Balance ([#379](https://github.com/stellar/java-stellar-sdk/pull/379)).
* Fix null pointer when calling ChangeTrustOperationResponse.getAsset() for LiquidityPool trust line ([#378](https://github.com/stellar/java-stellar-sdk/pull/378)).
* Changed the access modifiers of the inner static classes of `AccountResponse` to public ([#390](https://github.com/stellar/java-stellar-sdk/pull/390)).
* Use the new ClaimableBalance Predicate AbsBeforeEpoch field to avoid parsing errors on potential large dates in AbsBefore ([#394](https://github.com/stellar/java-stellar-sdk/pull/394)).

### Breaking changes
* Changed offer ids to be represented in requests and response models as long data type. ([#386](https://github.com/stellar/java-stellar-sdk/pull/386)).
* Changed all *MuxedId* attributes to be of data type `java.math.BigInteger` in request and response models ([#388](https://github.com/stellar/java-stellar-sdk/pull/388)).

## 0.29.0

* Fixed bug in parsing liquidity pool operation and effect responses ([#373](https://github.com/stellar/java-stellar-sdk/pull/373)).

## 0.28.0

* Added support for 'client_domain' ManageData operations in SEP 10 challenges ([#368](https://github.com/stellar/java-stellar-sdk/pull/368)).

## 0.27.1-beta

* Add 5 minute grace period to SEP-10 challenge parsing function ([#366](https://github.com/stellar/java-stellar-sdk/pull/366)).

## 0.27.0-beta

* Add AMM support for [CAP38](https://github.com/stellar/stellar-protocol/blob/master/core/cap-0038.md) ([#359](https://github.com/stellar/java-stellar-sdk/pull/359)).

## 0.26.0

* Add opt-in support for [SEP23](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0023.md) M-strkeys for `MuxedAccount`s
* Add `getClaimableBalanceId()` method to `Transaction` class which returns the claimable balance id for a given operation.
* Add support for additional _muxed and _muxed_id optional fields in Horizon's JSON responses (available since Horizon 2.4, following what's described in SEP 23).

## 0.25.0

* Added TransactionsRequestBuilder.forClaimableBalance(), and OperationsRequestBuilder.forClaimableBalance().
* Added support for new `accounts`, `balances`, `claimable_balances_amount`, and `num_claimable_balances` fields on Assets.

## 0.24.0

### Deprecations

* Operation `allow_trust` is deprecated in favor of `set_trust_line_flags` (although it will still be supported by the network)

* Effects `trustline_authorized`, `trustline_authorized_to_maintain_liabilities` and `trustline_deauthorized` are deprecated in favor of `trustline_flags_updated`. Note how we intentionally didn't add a new `trustline_authorized_clawback_enabled` effect.

For uniformity, the `allow_trust` operation will start producing `trustline_flags_updated` from this release.

For now `trustline_authorized`, `trustline_authorized_to_maintain_liabilities` and `trustline_deauthorized` will continue to be emitted as a result of the `allow_trust` operation but in the future we may stop doing so.


Deprecated | New class
-|-
`org.stellar.sdk.AllowTrustOperation`                                                      | `org.stellar.sdk.SetTrustlineFlagsOperation`
`org.stellar.sdk.responses.operations.AllowTrustOperationResponse`                         | `org.stellar.sdk.responses.operations.SetTrustLineFlagsOperationResponse`
`org.stellar.sdk.responses.effects.TrustlineAuthorizedEffectResponse`                      | `org.stellar.sdk.responses.effects.TrustlineFlagsUpdatedEffectResponse`
`org.stellar.sdk.responses.effects.TrustlineAuthorizedToMaintainLiabilitiesEffectResponse` | `org.stellar.sdk.responses.effects.TrustlineFlagsUpdatedEffectResponse`
`org.stellar.sdk.responses.effects.TrustlineDeauthorizedEffectResponse`                    | `org.stellar.sdk.responses.effects.TrustlineFlagsUpdatedEffectResponse`


### Changes

## New Operations

* `clawback` implemented in `org.stellar.sdk.ClawbackOperation` claws back a trustline from a given asset holder.

* `clawback_claimable_balance` implemented in `org.stellar.sdk.ClawbackClaimableBalanceOperation` claws back a claimable balance.

* `set_trust_line_flags` implemented in `org.stellar.sdk.SetTrustlineFlagsOperation` modifies a trustline's flags. This operation should be used instead of `org.stellar.sdk.AllowTrustOperation`.

## New effects

* `trustline_flags_updated` implemented in `org.stellar.sdk.responses.effects.TrustlineFlagsUpdatedEffectResponse`, with the following fields:
  * Asset fields (like explained in the operations above):
    * `asset_type`
    * `asset_code`
    * `asset_issuer`
  * `trustor` - account whose trustline the effect refers to
  * `authorized_flag` - true to indicate the flag is set, field ommited if not set
  * `authorized_to_maintain_liabilites` - true to indicate the flag is set, field ommited if not set
  * `clawback_enabled_flag` - true to indicate that the flag is set, field ommitted if not set


* `claimable_balance_clawed_back` implemented in `org.stellar.sdk.responses.effects.ClaimableBalanceClawedBackEffectResponse`, with the following fields:
  * `balance_id` - claimable balance identifer of the claimable balance clawed back

## 0.23.0
### Breaking change
- Updates the SEP-10 utility function parameters to support [SEP-10 v3.1](https://github.com/stellar/stellar-protocol/commit/6c8c9cf6685c85509835188a136ffb8cd6b9c11c) [(#319)](https://github.com/stellar/java-stellar-sdk/pull/319)
  - A new required `webAuthDomain` parameter was added to the following functions
    - `Sep10Challenge#newChallenge(KeyPair, Network, String, String, String, TimeBounds)`
    - `Sep10Challenge#readChallengeTransaction(String, String, Network, String, String)`
    - `Sep10Challenge#readChallengeTransaction(String, String, Network, String[], String)`
    - `Sep10Challenge#verifyChallengeTransactionSigners(String, String, Network, String, String, Set)`
    - `Sep10Challenge#verifyChallengeTransactionSigners(String, String, Network, String[], String, Set)`
    - `Sep10Challenge#verifyChallengeTransactionThreshold(String, String, Network, String[], String, int, Set)`
    - `Sep10Challenge#verifyChallengeTransactionThreshold(String, String, Network, String, String, int, Set)`
  - The `webAuthDomain` parameter is expected to match the value of the Manage Data operation with the 'web_auth_domain' key, if present.

## 0.22.1
- Fix several bugs in revoke operations. ([#317](https://github.com/stellar/java-stellar-sdk/pull/317))
    
## 0.22.0
- The XDR classes have been regenerated using the latest version of xdrgen which provides the following two features:

    - Builder static inner classes to unions and structs
    - Constructors for typedefs

## 0.21.2
- Update challenge transaction helpers for SEP-0010 v3.0.0. ([#308](https://github.com/stellar/java-stellar-sdk/pull/308))
- Fix the decoding of `balanceId` in `org.stellar.sdk.ClaimClaimableBalanceOperation`. ([#310](https://github.com/stellar/java-stellar-sdk/pull/310))

## 0.21.1
- Fix NullPointerException in `org.stellar.sdk.responses.operations.RevokeSponsorshipOperationResponse` accessor methods.

## 0.21.0

### Breaking change
- Change the type of `offerId` from **java.lang.Integer** to **java.lang.Long** to align with the 64-bit size of offer IDs. ([#303](https://github.com/stellar/java-stellar-sdk/pull/303)).

    This change affects the following fields:
    - org.stellar.sdk.responses.operations.CreatePassiveSellOfferOperationResponse.offerId
    - org.stellar.sdk.responses.operations.ManageBuyOfferOperationResponse.offerId
    - org.stellar.sdk.responses.operations.ManageSellOfferOperationResponse.offerId

## 0.20.0
* Update challenge transaction helpers for SEP-0010 v2.1.0. ([#300](https://github.com/stellar/java-stellar-sdk/pull/300)).
    - Remove verification of domain name.
    - Allow additional manage data operations that have the source account set as the server key.

## 0.19.0

### Add

- Add support for claimable balances ([#295](https://github.com/stellar/java-stellar-sdk/pull/295)).
Extend server class to allow loading claimable balances from Horizon. The following functions are available:

```
server.claimableBalances();
server.claimableBalances().forClaimant(claimant);
server.claimableBalances().forSponsor(sponsorId);
server.claimableBalances().forAsset(asset);
server.claimableBalances().claimableBalance(balanceId);
```
-  Add the following attributes to `AccountResponse` ([#295](https://github.com/stellar/java-stellar-sdk/pull/295)):
    * `Optional<String> getSponsor()`
    * `Integer getNumSponsoring()`
    * `Integer getNumSponsored()`

- Add the optional attribute `Optional<String> getSponsor()` to `AccountResponse.Signer`, `AccountResponse.Balance`, `ClaimableBalanceResponse`, and `OfferResponse` ([#295](https://github.com/stellar/java-stellar-sdk/pull/295)).

- Add `sponsor` filtering support for `offers` and `accounts` ([#295](https://github.com/stellar/java-stellar-sdk/pull/295)).
    * `server.offers().forSponsor(accountID)`
    * `server.accounts().forSponsor(accountID)`

- Extend operation responses to support new operations ([#295](https://github.com/stellar/java-stellar-sdk/pull/295)).
    * `create_claimable_balance` with the following fields:
        * `asset` - asset available to be claimed (in canonical form),
        * `amount` - amount available to be claimed,
        * `claimants` - list of claimants with predicates (see below):
            * `destination` - destination account ID,
            * `predicate` - predicate required to claim a balance (see below).
    * `claim_claimable_balance` with the following fields:
        * `balance_id` - unique ID of balance to be claimed,
        * `claimant` - account ID of a claimant.
    * `begin_sponsoring_future_reserves` with the following fields:
        * `sponsored_id` - account ID for which future reserves will be sponsored.
    * `end_sponsoring_future_reserves` with the following fields:
        * `begin_sponsor` - account sponsoring reserves.
    * `revoke_sponsorship` with the following fields:
        * `account_id` - if account sponsorship was revoked,
        * `claimable_balance_id` - if claimable balance sponsorship was revoked,
        * `data_account_id` - if account data sponsorship was revoked,
        * `data_name` - if account data sponsorship was revoked,
        * `offer_id` - if offer sponsorship was revoked,
        * `trustline_account_id` - if trustline sponsorship was revoked,
        * `trustline_asset` - if trustline sponsorship was revoked,
        * `signer_account_id` - if signer sponsorship was revoked,
        * `signer_key` - if signer sponsorship was revoked.

- Extend effect responses to support new effects ([#295](https://github.com/stellar/java-stellar-sdk/pull/295)).
    * `claimable_balance_created` with the following fields:
        * `balance_id` - unique ID of claimable balance,
        * `asset` - asset available to be claimed (in canonical form),
        * `amount` - amount available to be claimed.
    * `claimable_balance_claimant_created` with the following fields:
        * `balance_id` - unique ID of a claimable balance,
        * `asset` - asset available to be claimed (in canonical form),
        * `amount` - amount available to be claimed,
        * `predicate` - predicate required to claim a balance (see below).
    * `claimable_balance_claimed` with the following fields:
        * `balance_id` - unique ID of a claimable balance,
        * `asset` - asset available to be claimed (in canonical form),
        * `amount` - amount available to be claimed,
    * `account_sponsorship_created` with the following fields:
        * `sponsor` - sponsor of an account.
    * `account_sponsorship_updated` with the following fields:
        * `new_sponsor` - new sponsor of an account,
        * `former_sponsor` - former sponsor of an account.
    * `account_sponsorship_removed` with the following fields:
        * `former_sponsor` - former sponsor of an account.
    * `trustline_sponsorship_created` with the following fields:
        * `sponsor` - sponsor of a trustline.
    * `trustline_sponsorship_updated` with the following fields:
        * `new_sponsor` - new sponsor of a trustline,
        * `former_sponsor` - former sponsor of a trustline.
    * `trustline_sponsorship_removed` with the following fields:
        * `former_sponsor` - former sponsor of a trustline.
    * `claimable_balance_sponsorship_created` with the following fields:
        * `sponsor` - sponsor of a claimable balance.
    * `claimable_balance_sponsorship_updated` with the following fields:
        * `new_sponsor` - new sponsor of a claimable balance,
        * `former_sponsor` - former sponsor of a claimable balance.
    * `claimable_balance_sponsorship_removed` with the following fields:
        * `former_sponsor` - former sponsor of a claimable balance.
    * `signer_sponsorship_created` with the following fields:
        * `signer` - signer being sponsored.
        * `sponsor` - signer sponsor.
    * `signer_sponsorship_updated` with the following fields:
        * `signer` - signer being sponsored.
        * `former_sponsor` - the former sponsor of the signer.
        * `new_sponsor` - the new sponsor of the signer.
    * `signer_sponsorship_removed` with the following fields:
        * `former_sponsor` - former sponsor of a signer.

### Breaking changes

* Replace `Sep10Challenge.newChallenge()`'s `String anchorName` parameter with `String domainName` 
* Add `String domainName` parameter to `Sep10Challenge.readChallengeTransaction()`, `Sep10Challenge.verifyChallengeTransactionSigners()`, and `Sep10Challenge.verifyChallengeTransactionThreshold()`

SEP-10 now requires clients to verify the `SIGNING_KEY` included in the TOML file of the service requiring authentication is used to sign the challenge and that the challenge's Manage Data operation key includes the requested service's home domain. These checks ensure the challenge cannot be used in a relay attack.

The breaking changes described above support the added SEP-10 2.0 requirements for both servers and clients.

## 0.18.0

* Generate V1 transaction envelopes when constructing new Transaction instances ([#285](https://github.com/stellar/java-stellar-sdk/pull/285/files)).
* Allow FeeBumpTransaction instances to wrap V0 transactions ([#285](https://github.com/stellar/java-stellar-sdk/pull/285/files)).

## 0.17.0

* Rollback support for SEP23 (Muxed Account StrKey) ([#282](https://github.com/stellar/java-stellar-sdk/pull/282)).

## 0.16.0

* Update XDR definitions and auto-generated classes to support upcoming protocol 13 release ([#276](https://github.com/stellar/java-stellar-sdk/pull/276)).
* Extend StrKey implementation to handle [CAP 27 Muxed Accounts](https://github.com/stellar/stellar-protocol/blob/master/core/cap-0027.md) ([#276](https://github.com/stellar/java-stellar-sdk/pull/276)).
* Update `TransactionResponse` to include new fields which are relevant to [CAP 15 Fee-Bump Transactions](https://github.com/stellar/stellar-protocol/blob/master/core/cap-0015.md) ([#275](https://github.com/stellar/java-stellar-sdk/pull/275)).
* Update `AccountResponse.Balance`, `AllowTrustOperationResponse`, and create `TrustlineAuthorizedToMaintainLiabilitiesEffectResponse` to support [CAP 18 Fine-Grained Control of Authorization](https://github.com/stellar/stellar-protocol/blob/master/core/cap-0018.md) ([#274](https://github.com/stellar/java-stellar-sdk/pull/274)).
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
* Patch for Horizon [Timeout](https://developers.stellar.org/api/errors/http-status-codes/horizon-specific/timeout/) responses (`SubmitTransactionResponse.getEnvelopeXdr()` and `SubmitTransactionResponse.getResultXdr()`).

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
