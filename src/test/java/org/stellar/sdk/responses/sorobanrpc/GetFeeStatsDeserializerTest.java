package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.responses.gson.GsonSingleton;

public class GetFeeStatsDeserializerTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/sorobanrpc/get_fee_stats.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));

    SorobanRpcResponse<GetFeeStatsResponse> getFeeStatsResponseSorobanRpcResponse =
        GsonSingleton.getInstance()
            .fromJson(json, new TypeToken<SorobanRpcResponse<GetFeeStatsResponse>>() {}.getType());
    GetFeeStatsResponse getFeeStatsResponse = getFeeStatsResponseSorobanRpcResponse.getResult();
    assertEquals(getFeeStatsResponse.getLatestLedger().longValue(), 4519945L);
    assertEquals(
        getFeeStatsResponse.getInclusionFee(),
        new GetFeeStatsResponse.FeeDistribution(
            200L, 100L, 125L, 100L, 100L, 100L, 100L, 100L, 100L, 100L, 100L, 100L, 100L, 100L, 7L,
            10L));
    assertEquals(
        getFeeStatsResponse.getSorobanInclusionFee(),
        new GetFeeStatsResponse.FeeDistribution(
            210L, 100L, 100L, 100L, 100L, 100L, 100L, 100L, 100L, 100L, 100L, 120L, 190L, 200L, 10L,
            50L));
  }
}
