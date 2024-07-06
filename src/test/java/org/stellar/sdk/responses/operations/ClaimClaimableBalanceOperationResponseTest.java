package org.stellar.sdk.responses.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.responses.GsonSingleton;

public class ClaimClaimableBalanceOperationResponseTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/operations/claim_claimable_balance.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ClaimClaimableBalanceOperationResponse response =
        GsonSingleton.getInstance().fromJson(json, ClaimClaimableBalanceOperationResponse.class);

    assertNotNull(response);
    assertEquals(2730894825627649L, response.getId().longValue());
    assertEquals("2730894825627649", response.getPagingToken());
    assertTrue(response.getTransactionSuccessful());
    assertEquals(
        "GAEY7JFLBBDD6PAUPVRVKMBNSL5W6GYMUOGJKNGHGFSFGJU6CT2IUARS", response.getSourceAccount());
    assertEquals("claim_claimable_balance", response.getType());
    assertEquals("2021-04-25T01:37:30Z", response.getCreatedAt());
    assertEquals(
        "ebf78969b439f9b6c0777695c76a668e5baabe747696fbef7fc7d40b5cbe9a6d",
        response.getTransactionHash());
    assertEquals(
        "00000000a5c8c85c12a32ec1b30fc1792a542ca38702afd78eb4fe524d028887cf6b6952",
        response.getBalanceId());
    assertEquals(
        "GAEY7JFLBBDD6PAUPVRVKMBNSL5W6GYMUOGJKNGHGFSFGJU6CT2IUARS", response.getClaimant());
  }
}
