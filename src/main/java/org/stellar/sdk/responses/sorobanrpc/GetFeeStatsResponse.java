package org.stellar.sdk.responses.sorobanrpc;

import lombok.Value;

/**
 * Response for JSON-RPC method getFeeStats.
 *
 * @see <a href="https://developers.stellar.org/docs/data/rpc/api-reference/methods/getFeeStats"
 *     target="_blank">getFeeStats documentation</a>
 */
@Value
public class GetFeeStatsResponse {
  FeeDistribution sorobanInclusionFee;
  FeeDistribution inclusionFee;
  Long latestLedger;

  @Value
  public static class FeeDistribution {
    Long max;
    Long min;
    Long mode;
    Long p10;
    Long p20;
    Long p30;
    Long p40;
    Long p50;
    Long p60;
    Long p70;
    Long p80;
    Long p90;
    Long p95;
    Long p99;
    Long transactionCount;
    Long ledgerCount;
  }
}
