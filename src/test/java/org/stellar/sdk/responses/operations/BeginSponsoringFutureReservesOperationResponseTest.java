package org.stellar.sdk.responses.operations;

import org.junit.Test;
import org.stellar.sdk.responses.GsonSingleton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BeginSponsoringFutureReservesOperationResponseTest {
    @Test
    public void testDeserialize() throws IOException {
        String filePath = "src/test/resources/responses/operations/begin_sponsoring_future_reserves.json";
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        BeginSponsoringFutureReservesOperationResponse response = GsonSingleton.getInstance().fromJson(json, BeginSponsoringFutureReservesOperationResponse.class);

        assertNotNull(response);
        assertEquals(150660619450806273L, response.getId().longValue());
        assertEquals("150660619450806273", response.getPagingToken());
        assertTrue(response.getTransactionSuccessful());
        assertEquals("GDDUVW72OCNRAVL752HXYJQTXEJQRYHDWU76YKGDEWZ5HCATZBSKJM7Y", response.getSourceAccount());
        assertEquals("begin_sponsoring_future_reserves", response.getType());
        assertEquals("2021-04-24T05:52:06Z", response.getCreatedAt());
        assertEquals("41d80b1509a355d45271e0058c7ceac8ee616c2b5215bdb664ec55e50d2bad6c", response.getTransactionHash());
        assertEquals("GDS6ULV46WVSO2USGIVTIUDYBL3ROBPQEEANE3AY6XZION25DCNFIE2R", response.getSponsoredId());
    }
}
