package org.stellar.sdk.responses;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClaimableBalanceResponseTest {
    @Test
    public void testDeserialize() throws IOException {
        String filePath = "src/test/resources/responses/asset.json";
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        AssetResponse assetResponse = GsonSingleton.getInstance().fromJson(json, AssetResponse.class);
    }
}
