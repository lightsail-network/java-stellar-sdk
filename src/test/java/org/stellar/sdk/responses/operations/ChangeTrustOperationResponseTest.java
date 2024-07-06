package org.stellar.sdk.responses.operations;

import org.junit.Test;
import org.stellar.sdk.AssetTypeCreditAlphaNum4;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.TrustLineAsset;
import org.stellar.sdk.responses.GsonSingleton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ChangeTrustOperationResponseTest {
    @Test
    public void testDeserializeWithAsset() throws IOException {
        String filePath = "src/test/resources/responses/operations/change_trust_asset.json";
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        ChangeTrustOperationResponse response = GsonSingleton.getInstance().fromJson(json, ChangeTrustOperationResponse.class);
        assertNotNull(response);
        assertEquals(150661512804089860L, response.getId().longValue());
        assertEquals("150661512804089860", response.getPagingToken());
        assertTrue(response.getTransactionSuccessful());
        assertEquals("GA26UJZUXR5Q2VMTJHAFS2DV6DKFRWBIN7JKDALGYFEXTRNGX5K6DEAZ", response.getSourceAccount());
        assertEquals("change_trust", response.getType());
        assertEquals("2021-04-24T06:10:44Z", response.getCreatedAt());
        assertEquals("c96337fca2de58731dbfd7a70e90e1078af796e8c5b8f621584c4565311be4c1", response.getTransactionHash());
        assertEquals("credit_alphanum4", response.getAssetType());
        assertEquals("USDC", response.getAssetCode());
        assertEquals("GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN", response.getAssetIssuer());
        assertEquals(new TrustLineAsset(new AssetTypeCreditAlphaNum4("USDC", "GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN")), response.getAsset());
        assertEquals("922337203685.4775807", response.getLimit());
        assertEquals("GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN", response.getTrustee());
        assertEquals("GA26UJZUXR5Q2VMTJHAFS2DV6DKFRWBIN7JKDALGYFEXTRNGX5K6DEAZ", response.getTrustor());
    }

    @Test
    public void testDeserializeWithLiquidityPoolId() throws IOException {
        String filePath = "src/test/resources/responses/operations/change_trust_liquidity_pool_id.json";
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        ChangeTrustOperationResponse response = GsonSingleton.getInstance().fromJson(json, ChangeTrustOperationResponse.class);

        assertNotNull(response);
        assertEquals(1578868632723457L, response.getId().longValue());
        assertEquals("1578868632723457", response.getPagingToken());
        assertTrue(response.getTransactionSuccessful());
        assertEquals("GAQXAWHCM4A7SQCT3BOSVEGRI2OOB7LO2CMFOYFF6YRXU4VQSB5V2V2K", response.getSourceAccount());
        assertEquals("MAQXAWHCM4A7SQCT3BOSVEGRI2OOB7LO2CMFOYFF6YRXU4VQSB5V2AAAAAAAAE4DUGF2O", response.getSourceAccountMuxed());
        assertEquals(1278881L, response.getSourceAccountMuxedId().longValue());
        assertEquals("change_trust", response.getType());
        assertEquals("2021-10-07T18:02:57Z", response.getCreatedAt());
        assertEquals("4ebd36d6ae43622d606a0b7c59fd28d1c0b07d96bae7deb5c86e7b93a9c1c5c3", response.getTransactionHash());
        assertEquals("liquidity_pool_shares", response.getAssetType());
        assertEquals("2c0bfa623845dd101cbf074a1ca1ae4b2458cc8d0104ad65939ebe2cd9054355", response.getLiquidityPoolId());
        assertEquals(new TrustLineAsset(new LiquidityPoolID("2c0bfa623845dd101cbf074a1ca1ae4b2458cc8d0104ad65939ebe2cd9054355")), response.getAsset());
        assertEquals("922337203685.4775807", response.getLimit());
        assertEquals("GAQXAWHCM4A7SQCT3BOSVEGRI2OOB7LO2CMFOYFF6YRXU4VQSB5V2V2K", response.getTrustor());
        assertEquals("MAQXAWHCM4A7SQCT3BOSVEGRI2OOB7LO2CMFOYFF6YRXU4VQSB5V2AAAAAAAAE4DUGF2O", response.getTrustorMuxed());
        assertEquals(1278881L, response.getTrustorMuxedId().longValue());
    }
}
