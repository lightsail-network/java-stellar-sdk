package org.stellar.sdk.responses;

import junit.framework.TestCase;

import org.junit.Test;

public class LedgerDeserializerTest extends TestCase {
  @Test
  public void testDeserialize() {
    String json = "{\n" +
            "  \"_links\": {\n" +
            "    \"effects\": {\n" +
            "      \"href\": \"/ledgers/898826/effects{?cursor,limit,order}\",\n" +
            "      \"templated\": true\n" +
            "    },\n" +
            "    \"operations\": {\n" +
            "      \"href\": \"/ledgers/898826/operations{?cursor,limit,order}\",\n" +
            "      \"templated\": true\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"/ledgers/898826\"\n" +
            "    },\n" +
            "    \"transactions\": {\n" +
            "      \"href\": \"/ledgers/898826/transactions{?cursor,limit,order}\",\n" +
            "      \"templated\": true\n" +
            "    }\n" +
            "  },\n" +
            "  \"id\": \"686bb246db89b099cd3963a4633eb5e4315d89dfd3c00594c80b41a483847bfa\",\n" +
            "  \"paging_token\": \"3860428274794496\",\n" +
            "  \"hash\": \"686bb246db89b099cd3963a4633eb5e4315d89dfd3c00594c80b41a483847bfa\",\n" +
            "  \"prev_hash\": \"50c8695eb32171a19858413e397cc50b504ceacc819010bdf8ff873aff7858d7\",\n" +
            "  \"sequence\": 898826,\n" +
            "  \"transaction_count\": 1,\n" +
            "  \"operation_count\": 2,\n" +
            "  \"closed_at\": \"2015-11-19T21:35:59Z\",\n" +
            "  \"total_coins\": \"101343867604.8975480\",\n" +
            "  \"fee_pool\": \"1908.2248818\",\n" +
            "  \"base_fee\": 100,\n" +
            "  \"base_reserve\": \"10.0000000\",\n" +
            "  \"max_tx_set_size\": 50\n" +
            "}";

    Ledger ledger = GsonSingleton.getInstance().fromJson(json, Ledger.class);
    assertEquals(ledger.getHash(), "686bb246db89b099cd3963a4633eb5e4315d89dfd3c00594c80b41a483847bfa");
    assertEquals(ledger.getPagingToken(), "3860428274794496");
    assertEquals(ledger.getPrevHash(), "50c8695eb32171a19858413e397cc50b504ceacc819010bdf8ff873aff7858d7");
    assertEquals(ledger.getSequence(), new Long(898826));
    assertEquals(ledger.getTransactionCount(), new Integer(1));
    assertEquals(ledger.getOperationCount(), new Integer(2));
    assertEquals(ledger.getClosedAt(), "2015-11-19T21:35:59Z");
    assertEquals(ledger.getTotalCoins(), "101343867604.8975480");
    assertEquals(ledger.getFeePool(), "1908.2248818");
    assertEquals(ledger.getBaseFee(), new Long(100));
    assertEquals(ledger.getBaseReserve(), "10.0000000");
    assertEquals(ledger.getMaxTxSetSize(), new Integer(50));
    assertEquals(ledger.getLinks().getEffects().getHref(), "/ledgers/898826/effects{?cursor,limit,order}");
    assertEquals(ledger.getLinks().getOperations().getHref(), "/ledgers/898826/operations{?cursor,limit,order}");
    assertEquals(ledger.getLinks().getSelf().getHref(), "/ledgers/898826");
    assertEquals(ledger.getLinks().getTransactions().getHref(), "/ledgers/898826/transactions{?cursor,limit,order}");
  }
}
