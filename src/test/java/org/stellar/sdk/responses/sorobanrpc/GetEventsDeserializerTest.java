package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;

import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.stellar.sdk.requests.sorobanrpc.EventFilterType;
import org.stellar.sdk.responses.GsonSingleton;

public class GetEventsDeserializerTest {
  @Test
  public void testDeserialize() {
    SorobanRpcResponse<GetEventsResponse> getEventsResponse =
        GsonSingleton.getInstance()
            .fromJson(json, new TypeToken<SorobanRpcResponse<GetEventsResponse>>() {}.getType());
    assertEquals(getEventsResponse.getResult().getLatestLedger().longValue(), 169L);
    assertEquals(getEventsResponse.getResult().getEvents().size(), 3);
    GetEventsResponse.EventInfo eventInfo0 = getEventsResponse.getResult().getEvents().get(0);
    assertEquals(eventInfo0.getType(), EventFilterType.CONTRACT);
    assertEquals(eventInfo0.getLedger().intValue(), 108);
    assertEquals(eventInfo0.getLedgerClosedAt(), "2023-07-23T14:47:01Z");
    assertEquals(
        eventInfo0.getContractId(), "CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K");
    assertEquals(eventInfo0.getId(), "0000000463856472064-0000000000");
    assertEquals(eventInfo0.getPagingToken(), "0000000463856472064-0000000000");
    assertEquals(eventInfo0.getTopic().size(), 2);
    assertEquals(eventInfo0.getTopic().get(0), "AAAADwAAAAdDT1VOVEVSAA==");
    assertEquals(eventInfo0.getTopic().get(1), "AAAADwAAAAlpbmNyZW1lbnQAAAA=");
    assertEquals(eventInfo0.getValue(), "AAAAAwAAAAE=");
    assertEquals(eventInfo0.getInSuccessfulContractCall(), true);

    GetEventsResponse.EventInfo eventInfo1 = getEventsResponse.getResult().getEvents().get(1);
    assertEquals(eventInfo1.getType(), EventFilterType.SYSTEM);

    GetEventsResponse.EventInfo eventInfo2 = getEventsResponse.getResult().getEvents().get(2);
    assertEquals(eventInfo2.getType(), EventFilterType.DIAGNOSTIC);
  }

  String json =
      "{\n"
          + "    \"jsonrpc\": \"2.0\",\n"
          + "    \"id\": \"198cb1a8-9104-4446-a269-88bf000c2721\",\n"
          + "    \"result\": {\n"
          + "        \"events\": [\n"
          + "            {\n"
          + "                \"type\": \"contract\",\n"
          + "                \"ledger\": \"108\",\n"
          + "                \"ledgerClosedAt\": \"2023-07-23T14:47:01Z\",\n"
          + "                \"contractId\": \"CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K\",\n"
          + "                \"id\": \"0000000463856472064-0000000000\",\n"
          + "                \"pagingToken\": \"0000000463856472064-0000000000\",\n"
          + "                \"topic\": [\n"
          + "                    \"AAAADwAAAAdDT1VOVEVSAA==\",\n"
          + "                    \"AAAADwAAAAlpbmNyZW1lbnQAAAA=\"\n"
          + "                ],\n"
          + "                \"value\": \"AAAAAwAAAAE=\",\n"
          + "                \"inSuccessfulContractCall\": true\n"
          + "            },\n"
          + "            {\n"
          + "                \"type\": \"system\",\n"
          + "                \"ledger\": \"111\",\n"
          + "                \"ledgerClosedAt\": \"2023-07-23T14:47:04Z\",\n"
          + "                \"contractId\": \"CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K\",\n"
          + "                \"id\": \"0000000476741373952-0000000000\",\n"
          + "                \"pagingToken\": \"0000000476741373952-0000000000\",\n"
          + "                \"topic\": [\n"
          + "                    \"AAAADwAAAAdDT1VOVEVSAA==\",\n"
          + "                    \"AAAADwAAAAlpbmNyZW1lbnQAAAA=\"\n"
          + "                ],\n"
          + "                \"value\": \"AAAAAwAAAAI=\",\n"
          + "                \"inSuccessfulContractCall\": true\n"
          + "            },\n"
          + "            {\n"
          + "                \"type\": \"diagnostic\",\n"
          + "                \"ledger\": \"114\",\n"
          + "                \"ledgerClosedAt\": \"2023-07-23T14:47:07Z\",\n"
          + "                \"contractId\": \"CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K\",\n"
          + "                \"id\": \"0000000489626275840-0000000000\",\n"
          + "                \"pagingToken\": \"0000000489626275840-0000000000\",\n"
          + "                \"topic\": [\n"
          + "                    \"AAAADwAAAAdDT1VOVEVSAA==\",\n"
          + "                    \"AAAADwAAAAlpbmNyZW1lbnQAAAA=\"\n"
          + "                ],\n"
          + "                \"value\": \"AAAAAwAAAAM=\",\n"
          + "                \"inSuccessfulContractCall\": true\n"
          + "            }\n"
          + "        ],\n"
          + "        \"latestLedger\": \"169\"\n"
          + "    }\n"
          + "}";
}
