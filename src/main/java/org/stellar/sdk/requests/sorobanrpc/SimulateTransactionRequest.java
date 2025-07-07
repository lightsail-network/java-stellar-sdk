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
 *     href="https://developers.stellar.org/docs/data/rpc/api-reference/methods/simulateTransaction"
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
