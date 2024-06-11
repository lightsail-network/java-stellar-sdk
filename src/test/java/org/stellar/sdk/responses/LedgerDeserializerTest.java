package org.stellar.sdk.responses;

import java.io.IOException;
import junit.framework.TestCase;
import org.junit.Test;

public class LedgerDeserializerTest extends TestCase {
  @Test
  public void testDeserialize() throws IOException {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"self\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/ledgers/48637678\"\n"
            + "    },\n"
            + "    \"transactions\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/ledgers/48637678/transactions{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"operations\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/ledgers/48637678/operations{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"payments\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/ledgers/48637678/payments{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/ledgers/48637678/effects{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    }\n"
            + "  },\n"
            + "  \"id\": \"41cfd330183c18bc8b380a46e71769e496199b420356bffc9f03287bfb9ce7b6\",\n"
            + "  \"paging_token\": \"208897236363378688\",\n"
            + "  \"hash\": \"41cfd330183c18bc8b380a46e71769e496199b420356bffc9f03287bfb9ce7b6\",\n"
            + "  \"prev_hash\": \"fa6f2b3fc10f441a5dfdcd973c73a78cc99f3257271f8da2d9b0bc4e2cf31bcf\",\n"
            + "  \"sequence\": 48637678,\n"
            + "  \"successful_transaction_count\": 98,\n"
            + "  \"failed_transaction_count\": 505,\n"
            + "  \"operation_count\": 443,\n"
            + "  \"tx_set_operation_count\": 964,\n"
            + "  \"closed_at\": \"2023-10-20T04:07:21Z\",\n"
            + "  \"total_coins\": \"105443902087.3472865\",\n"
            + "  \"fee_pool\": \"4230801.4837990\",\n"
            + "  \"base_fee_in_stroops\": 100,\n"
            + "  \"base_reserve_in_stroops\": 5000000,\n"
            + "  \"max_tx_set_size\": 1000,\n"
            + "  \"protocol_version\": 19,\n"
            + "  \"header_xdr\": \"AAAAE/pvKz/BD0QaXf3Nlzxzp4zJnzJXJx+NotmwvE4s8xvP7qfvEc2qNS7fkzYsE0nuWzTntzk51lygSX3R4OohQqEAAAAAZTH8+QAAAAAAAAABAAAAAAaweClXqq3sjNIHBm/r6o1RY6yR5HqkHJCaZtEEdMUfAAAAQC8qa6+gFLyfrj2BLi7B6TJUF0AXAg5Tao+wfa46XK1Ap9MdCtNekddz5+yIuJfEXpZbDL026omNsGZAXApWWQWaqZFwC16SgweXoVDEoRR5DxOpX1KTw8mYFB1LQ5hXDnP0Q24AyMXFNS5889SWfmKJ23vRjsw2+dJ6d4ldcKKYAuYm7g6iHrPseVthAAAmepoyEOYAAAEWAAAAAFJPSV0AAABkAExLQAAAA+hqOATyc5yeP5Rbm04iZNfUsSSL5gjTrl9qiRrMyVBAmUnSjbHKF9M4otFOBK8dTBPBlwTILohIRKCKLrfzZZ3yAnnNpR07DmDiuC0VFFYqF4mp7+54u0NkorebGPfT+Oyzx+5JqY5nE8H+vi/tc2ytQwnV2PZQhsxBoaVJrJluHgAAAAA=\"\n"
            + "}";

    LedgerResponse ledger = GsonSingleton.getInstance().fromJson(json, LedgerResponse.class);
    assertEquals(
        ledger.getId(), "41cfd330183c18bc8b380a46e71769e496199b420356bffc9f03287bfb9ce7b6");
    assertEquals(ledger.getPagingToken(), "208897236363378688");
    assertEquals(
        ledger.getHash(), "41cfd330183c18bc8b380a46e71769e496199b420356bffc9f03287bfb9ce7b6");
    assertEquals(
        ledger.getPrevHash(), "fa6f2b3fc10f441a5dfdcd973c73a78cc99f3257271f8da2d9b0bc4e2cf31bcf");
    assertEquals(ledger.getSequence(), Long.valueOf(48637678));
    assertEquals(ledger.getSuccessfulTransactionCount(), Integer.valueOf(98));
    assertEquals(ledger.getFailedTransactionCount(), Integer.valueOf(505));
    assertEquals(ledger.getOperationCount(), Integer.valueOf(443));
    assertEquals(ledger.getTxSetOperationCount(), Integer.valueOf(964));
    assertEquals(ledger.getClosedAt(), "2023-10-20T04:07:21Z");
    assertEquals(ledger.getTotalCoins(), "105443902087.3472865");
    assertEquals(ledger.getFeePool(), "4230801.4837990");
    assertEquals(ledger.getBaseFeeInStroops(), "100");
    assertEquals(ledger.getBaseReserveInStroops(), "5000000");
    assertEquals(ledger.getMaxTxSetSize(), Integer.valueOf(1000));
    assertEquals(ledger.getProtocolVersion(), Integer.valueOf(19));
    assertEquals(
        ledger.getHeaderXdr(),
        "AAAAE/pvKz/BD0QaXf3Nlzxzp4zJnzJXJx+NotmwvE4s8xvP7qfvEc2qNS7fkzYsE0nuWzTntzk51lygSX3R4OohQqEAAAAAZTH8+QAAAAAAAAABAAAAAAaweClXqq3sjNIHBm/r6o1RY6yR5HqkHJCaZtEEdMUfAAAAQC8qa6+gFLyfrj2BLi7B6TJUF0AXAg5Tao+wfa46XK1Ap9MdCtNekddz5+yIuJfEXpZbDL026omNsGZAXApWWQWaqZFwC16SgweXoVDEoRR5DxOpX1KTw8mYFB1LQ5hXDnP0Q24AyMXFNS5889SWfmKJ23vRjsw2+dJ6d4ldcKKYAuYm7g6iHrPseVthAAAmepoyEOYAAAEWAAAAAFJPSV0AAABkAExLQAAAA+hqOATyc5yeP5Rbm04iZNfUsSSL5gjTrl9qiRrMyVBAmUnSjbHKF9M4otFOBK8dTBPBlwTILohIRKCKLrfzZZ3yAnnNpR07DmDiuC0VFFYqF4mp7+54u0NkorebGPfT+Oyzx+5JqY5nE8H+vi/tc2ytQwnV2PZQhsxBoaVJrJluHgAAAAA=");
    assertEquals(
        ledger.getLinks().getSelf().getHref(), "https://horizon.stellar.org/ledgers/48637678");
    assertEquals(
        ledger.getLinks().getTransactions().getHref(),
        "https://horizon.stellar.org/ledgers/48637678/transactions{?cursor,limit,order}");
    assertEquals(
        ledger.getLinks().getOperations().getHref(),
        "https://horizon.stellar.org/ledgers/48637678/operations{?cursor,limit,order}");
    assertEquals(
        ledger.getLinks().getPayments().getHref(),
        "https://horizon.stellar.org/ledgers/48637678/payments{?cursor,limit,order}");
    assertEquals(
        ledger.getLinks().getEffects().getHref(),
        "https://horizon.stellar.org/ledgers/48637678/effects{?cursor,limit,order}");
    assertNotNull(ledger.parseHeaderXdr());
    assertEquals(ledger.parseHeaderXdr().toXdrBase64(), ledger.getHeaderXdr());
  }
}
