package org.stellar.sdk.responses.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.responses.GsonSingleton;

public class ClawbackClaimableBalanceOperationResponseTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/operations/clawback_claimable_balance.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ClaimClaimableBalanceOperationResponse response =
        GsonSingleton.getInstance().fromJson(json, ClaimClaimableBalanceOperationResponse.class);

    assertNotNull(response);
    assertEquals(3513936083165185L, response.getId().longValue());
    assertEquals("3513936083165185", response.getPagingToken());
    assertTrue(response.getTransactionSuccessful());
    assertEquals(
        "GD5YHBKE7FSUUZIOSL4ED6UKMM2HZAYBYGZI7KRCTMFDTOO6SGZCQB4Z", response.getSourceAccount());
    assertEquals("clawback_claimable_balance", response.getType());
    assertEquals("2021-05-06T03:48:05Z", response.getCreatedAt());
    assertEquals(
        "8820306ee424f47fd1c16b28ab034a3bdab0147fc16c65b145ba1df5f338c8a2",
        response.getTransactionHash());
    assertEquals(
        "000000001fe36f3ce6ab6a6423b18b5947ce8890157ae77bb17faeb765814ad040b74ce1",
        response.getBalanceId());
  }
}
