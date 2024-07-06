package org.stellar.sdk.responses.operations;

import org.junit.Test;
import org.stellar.sdk.AssetTypeCreditAlphaNum4;
import org.stellar.sdk.responses.GsonSingleton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AllowTrustOperationResponseTest {
    @Test
    public void testDeserialize() throws IOException {
        String filePath = "src/test/resources/responses/operations/allow_trust.json";
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        AllowTrustOperationResponse response = GsonSingleton.getInstance().fromJson(json, AllowTrustOperationResponse.class);

        assertNotNull(response);
        assertEquals(150662449110290439L, response.getId().longValue());
        assertEquals("150662449110290439", response.getPagingToken());
        assertTrue(response.getTransactionSuccessful());
        assertEquals("GBRDHSZL4ZKOI2PTUMM53N3NICZXC5OX3KPCD4WD4NG4XGCBC2ZA3KAG", response.getSourceAccount());
        assertEquals("allow_trust", response.getType());
        assertEquals("2021-04-24T06:30:21Z", response.getCreatedAt());
        assertEquals("918370699c6c59c2ab2b59657f6a9d968197a5b90fa87734f805b0c3954d6010", response.getTransactionHash());
        assertEquals("credit_alphanum4", response.getAssetType());
        assertEquals("4GLD", response.getAssetCode());
        assertEquals("GBRDHSZL4ZKOI2PTUMM53N3NICZXC5OX3KPCD4WD4NG4XGCBC2ZA3KAG", response.getAssetIssuer());
        assertEquals(new AssetTypeCreditAlphaNum4("4GLD", "GBRDHSZL4ZKOI2PTUMM53N3NICZXC5OX3KPCD4WD4NG4XGCBC2ZA3KAG"), response.getAsset());
        assertEquals("GBRDHSZL4ZKOI2PTUMM53N3NICZXC5OX3KPCD4WD4NG4XGCBC2ZA3KAG", response.getTrustee());
        assertEquals("GDJ4X5NVRMIB3ZYO2PQ7U2QKZ5C42YKV7ZH3LJYXZWQOP4K5CUBSNGVS", response.getTrustor());
        // TODO: recheck it
        assertTrue(response.getAuthorize());
        assertTrue(response.getAuthorizeToMaintainLiabilities());
    }
}
