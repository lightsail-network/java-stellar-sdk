/**
 * Core classes for the Java Stellar SDK.
 *
 * <p>This package contains the primary entry points and foundational types for interacting with the
 * Stellar network:
 *
 * <ul>
 *   <li>{@link org.stellar.sdk.Server} — client for the Horizon API (accounts, transactions,
 *       payments, effects, offers, order books, etc.)
 *   <li>{@link org.stellar.sdk.SorobanServer} — client for Stellar RPC (formerly Soroban-RPC), used
 *       for smart contract interaction, transaction simulation, and submission
 *   <li>{@link org.stellar.sdk.Transaction} and {@link org.stellar.sdk.TransactionBuilder} —
 *       building, signing, and encoding Stellar transactions
 *   <li>{@link org.stellar.sdk.FeeBumpTransaction} — wrapping an inner transaction with a higher
 *       fee
 *   <li>{@link org.stellar.sdk.KeyPair} — Ed25519 key management (signing and verification)
 *   <li>{@link org.stellar.sdk.Asset}, {@link org.stellar.sdk.AssetTypeNative}, {@link
 *       org.stellar.sdk.AssetTypeCreditAlphaNum4}, {@link
 *       org.stellar.sdk.AssetTypeCreditAlphaNum12} — Stellar asset representations
 *   <li>{@link org.stellar.sdk.Memo} — transaction memo types (none, text, id, hash, return hash)
 *   <li>{@link org.stellar.sdk.Network} — network passphrase constants (public, testnet)
 *   <li>{@link org.stellar.sdk.Address} — Stellar account or contract address abstraction
 * </ul>
 *
 * <p>Related packages:
 *
 * <ul>
 *   <li>{@link org.stellar.sdk.operations} — Stellar operation types
 *   <li>{@link org.stellar.sdk.requests} — Horizon request builders
 *   <li>{@link org.stellar.sdk.responses} — Horizon response models
 *   <li>{@link org.stellar.sdk.contract} — smart contract interaction
 *   <li>{@link org.stellar.sdk.exception} — SDK exception hierarchy
 * </ul>
 *
 * @see <a href="https://developers.stellar.org/docs/data/apis">Stellar APIs overview</a>
 */
package org.stellar.sdk;
