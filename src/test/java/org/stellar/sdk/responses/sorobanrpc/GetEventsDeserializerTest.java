package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.requests.sorobanrpc.EventFilterType;
import org.stellar.sdk.responses.gson.GsonSingleton;

public class GetEventsDeserializerTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/sorobanrpc/get_events.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));

    SorobanRpcResponse<GetEventsResponse> getEventsResponse =
        GsonSingleton.getInstance()
            .fromJson(json, new TypeToken<SorobanRpcResponse<GetEventsResponse>>() {}.getType());
    assertEquals(getEventsResponse.getResult().getLatestLedger().longValue(), 169L);
    assertEquals(getEventsResponse.getResult().getEvents().size(), 3);
    assertEquals(getEventsResponse.getResult().getCursor(), "0000000463856472064-0000000000");

    GetEventsResponse.EventInfo eventInfo0 = getEventsResponse.getResult().getEvents().get(0);
    assertEquals(eventInfo0.getType(), EventFilterType.CONTRACT);
    assertEquals(eventInfo0.getLedger().intValue(), 108);
    assertEquals(eventInfo0.getLedgerClosedAt(), "2023-07-23T14:47:01Z");
    assertEquals(
        eventInfo0.getContractId(), "CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K");
    assertEquals(eventInfo0.getId(), "0000000463856472064-0000000000");
    assertEquals(eventInfo0.getTopic().size(), 2);
    assertEquals(eventInfo0.getTopic().get(0), "AAAADwAAAAdDT1VOVEVSAA==");
    assertEquals(eventInfo0.getTopic().get(1), "AAAADwAAAAlpbmNyZW1lbnQAAAA=");
    assertEquals(eventInfo0.getValue(), "AAAAAwAAAAE=");
    assertEquals(eventInfo0.getInSuccessfulContractCall(), true);
    assertEquals(
        eventInfo0.getTransactionHash(),
        "db86e94aa98b7d38213c041ebbb727fbaabf0b7c435de594f36c2d51fc61926d");
    assertEquals(eventInfo0.parseValue().toXdrBase64(), eventInfo0.getValue());
    assertNotNull(eventInfo0.parseTopic());
    assertEquals(eventInfo0.parseTopic().size(), 2);
    assertEquals(eventInfo0.parseTopic().get(0).toXdrBase64(), eventInfo0.getTopic().get(0));
    assertEquals(eventInfo0.parseTopic().get(1).toXdrBase64(), eventInfo0.getTopic().get(1));

    GetEventsResponse.EventInfo eventInfo1 = getEventsResponse.getResult().getEvents().get(1);
    assertEquals(eventInfo1.getType(), EventFilterType.SYSTEM);

    GetEventsResponse.EventInfo eventInfo2 = getEventsResponse.getResult().getEvents().get(2);
    assertEquals(eventInfo2.getType(), EventFilterType.DIAGNOSTIC);
  }
}
