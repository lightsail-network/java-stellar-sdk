package org.stellar.sdk.responses;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;

public class ClaimableBalanceResponseTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/asset.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AssetResponse assetResponse = GsonSingleton.getInstance().fromJson(json, AssetResponse.class);
  }
}
