package org.stellar.sdk.responses.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.responses.GsonSingleton;

public class AccountMergeOperationResponseTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/operations/account_merge.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountMergeOperationResponse response =
        GsonSingleton.getInstance().fromJson(json, AccountMergeOperationResponse.class);

    assertNotNull(response);
    assertEquals(150661877876133889L, response.getId().longValue());
    assertEquals("150661877876133889", response.getPagingToken());
    assertTrue(response.getTransactionSuccessful());
    assertEquals(
        "GBMZ7GUHNHT6TG4ITOBG46TKA5YMNH7ZKHHJSQRU6PNYBSAELLXNOFDE", response.getSourceAccount());
    assertEquals("account_merge", response.getType());
    assertEquals("2021-04-24T06:18:26Z", response.getCreatedAt());
    assertEquals(
        "f86aaf456dc000e565ac89bd0b21abcedad4d3ab497bdb9fefa782c9e6ce8c98",
        response.getTransactionHash());
    assertEquals("GBMZ7GUHNHT6TG4ITOBG46TKA5YMNH7ZKHHJSQRU6PNYBSAELLXNOFDE", response.getAccount());
    assertEquals("GDYG6NVTFCY6HPBRQ3SQNKTDZUR7SS6WAWNEAKAFJW5EMKDGQLPG523C", response.getInto());
  }
}
