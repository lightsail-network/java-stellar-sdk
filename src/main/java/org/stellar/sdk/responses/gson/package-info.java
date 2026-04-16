/**
 * Provides Gson serialization and deserialization adapters for Horizon response types.
 *
 * <p>This package contains custom {@link com.google.gson.JsonDeserializer} implementations that
 * handle polymorphic deserialization of Horizon responses (e.g., effects, operations, assets,
 * predicates). The {@link org.stellar.sdk.responses.gson.GsonSingleton} class provides the
 * pre-configured {@link com.google.gson.Gson} instance used throughout the SDK.
 */
package org.stellar.sdk.responses.gson;
