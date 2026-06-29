package org.stellar.sdk.requests.sorobanrpc;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * Request for JSON-RPC method simulateTransaction.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/apis/rpc/api-reference/methods/simulateTransaction"
 *     target="_blank">simulateTransaction documentation</a>
 */
@Value
public class SimulateTransactionRequest {
  String transaction;

  ResourceConfig resourceConfig;

  /**
   * Explicitly allows users to opt-in to non-root authorization in recording mode.
   *
   * <p>Leaving this field unset will default to {@link AuthMode#ENFORCE} if auth entries are
   * present, {@link AuthMode#RECORD} otherwise.
   */
  AuthMode authMode;

  /**
   * Opt simulation into recording {@code ADDRESS_V2} ("upgraded") authorization credentials
   * (CAP-71) instead of the legacy {@code ADDRESS} credentials. This maps to the {@code
   * useUpgradedAuth} flag introduced in Stellar RPC v27.1.0. It is best-effort and transitional: it
   * only affects the recording auth modes and is silently ignored by RPC servers (or protocol
   * versions) that cannot emit {@code ADDRESS_V2}.
   */
  boolean useUpgradedAuth;

  public enum AuthMode {
    /** Always enforce mode, even with an empty list. */
    @SerializedName("enforce")
    ENFORCE,
    /** Always recording mode, failing if any auth exists. */
    @SerializedName("record")
    RECORD,
    /** Like {@code RECORD}, but allowing non-root authorization. */
    @SerializedName("record_allow_nonroot")
    RECORD_ALLOW_NONROOT;
  }

  @Value
  @AllArgsConstructor
  @Builder
  public static class ResourceConfig {
    BigInteger instructionLeeway;
  }
}
