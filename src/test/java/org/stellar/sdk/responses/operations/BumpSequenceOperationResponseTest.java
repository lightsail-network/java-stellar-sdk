package org.stellar.sdk.responses.operations;

import org.junit.Test;
import org.stellar.sdk.responses.GsonSingleton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BumpSequenceOperationResponseTest {

    @Test
    public void testDeserialize() throws IOException {
        String filePath = "src/test/resources/responses/operations/bump_sequence.json";
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        BumpSequenceOperationResponse response = GsonSingleton.getInstance().fromJson(json, BumpSequenceOperationResponse.class);

        assertNotNull(response);
        assertEquals(150675003296243713L, response.getId().longValue());
        assertEquals("150675003296243713", response.getPagingToken());
        assertTrue(response.getTransactionSuccessful());
        assertEquals("GC37FPMWRUEMIEIMMR7L32NMIJLZJ6W3UYKEAN2M5YKX4TPZMQ4HZWWQ", response.getSourceAccount());
        assertEquals("bump_sequence", response.getType());
        assertEquals("2021-04-24T10:54:19Z", response.getCreatedAt());
        assertEquals("7cf9775c27cf2f386a25fd871cd951fe715eca16e28ff45495cb7c3add389f6d", response.getTransactionHash());
        assertEquals(136025045943191412L, response.getBumpTo().longValue());
    }
}
