package org.stellar.sdk.responses;

import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;
import org.junit.Test;

public class LedgerPageDeserializerTest extends TestCase {
  @Test
  public void testDeserialize() {
    Page<LedgerResponse> ledgersPage =
        GsonSingleton.getInstance()
            .fromJson(json, new TypeToken<Page<LedgerResponse>>() {}.getType());
    assertEquals(
        ledgersPage.getRecords().get(0).getHash(),
        "1c47f90760bc6761a75922658093c1302ab8c1275a6dffca0c2f612afcfd05c0");
    assertEquals(ledgersPage.getRecords().get(0).getPagingToken(), "208902149805965312");
    assertEquals(
        ledgersPage.getRecords().get(2).getHash(),
        "f3a99918b9e3be4b2d2d19a49a1058ee3e14c691b2de0c0ab345cf7371b19f08");
    assertEquals(
        ledgersPage.getLinks().getNext().getHref(),
        "https://horizon.stellar.org/ledgers?cursor=208902141216030720&limit=3&order=desc");
    assertEquals(
        ledgersPage.getLinks().getPrev().getHref(),
        "https://horizon.stellar.org/ledgers?cursor=208902149805965312&limit=3&order=asc");
    assertEquals(
        ledgersPage.getLinks().getSelf().getHref(),
        "https://horizon.stellar.org/ledgers?cursor=&limit=3&order=desc");
  }

  String json =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"self\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/ledgers?cursor=&limit=3&order=desc\"\n"
          + "    },\n"
          + "    \"next\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/ledgers?cursor=208902141216030720&limit=3&order=desc\"\n"
          + "    },\n"
          + "    \"prev\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/ledgers?cursor=208902149805965312&limit=3&order=asc\"\n"
          + "    }\n"
          + "  },\n"
          + "  \"_embedded\": {\n"
          + "    \"records\": [\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"self\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/ledgers/48638822\"\n"
          + "          },\n"
          + "          \"transactions\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/ledgers/48638822/transactions{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          },\n"
          + "          \"operations\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/ledgers/48638822/operations{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          },\n"
          + "          \"payments\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/ledgers/48638822/payments{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          },\n"
          + "          \"effects\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/ledgers/48638822/effects{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          }\n"
          + "        },\n"
          + "        \"id\": \"1c47f90760bc6761a75922658093c1302ab8c1275a6dffca0c2f612afcfd05c0\",\n"
          + "        \"paging_token\": \"208902149805965312\",\n"
          + "        \"hash\": \"1c47f90760bc6761a75922658093c1302ab8c1275a6dffca0c2f612afcfd05c0\",\n"
          + "        \"prev_hash\": \"c1f60e0626a21e7d6375300b22a2cd0c1a18b07b78853f883b8836e827df475a\",\n"
          + "        \"sequence\": 48638822,\n"
          + "        \"successful_transaction_count\": 118,\n"
          + "        \"failed_transaction_count\": 64,\n"
          + "        \"operation_count\": 395,\n"
          + "        \"tx_set_operation_count\": 471,\n"
          + "        \"closed_at\": \"2023-10-20T06:00:47Z\",\n"
          + "        \"total_coins\": \"105443902087.3472865\",\n"
          + "        \"fee_pool\": \"4230864.3905680\",\n"
          + "        \"base_fee_in_stroops\": 100,\n"
          + "        \"base_reserve_in_stroops\": 5000000,\n"
          + "        \"max_tx_set_size\": 1000,\n"
          + "        \"protocol_version\": 19,\n"
          + "        \"header_xdr\": \"AAAAE8H2DgYmoh59Y3UwCyKizQwaGLB7eIU/iDuINugn30daLWY7+UaK4yKpdCFQAWcJA4l1Rq7rDNFAq4r1QH6bimkAAAAAZTIXjwAAAAAAAAABAAAAAFaWt2CucvNouAYniCiCbacMYpTGlVK3TqKyoNF3RcOeAAAAQEAYeHnLlZs5XT5NcDpfeNwY6Dll7aKicGYJeIW/doF/sxZY8whV3hePJ1ARx58qKeO0sr0K8oVEmfc5a+3qOAIarXr3MA9nr4vrda61LC0Zki97fz6hGeY2R2kaHvZOD3SCQC9g5lZIwKuNE9Ielxy1Rq/0hj+SOUe0u7HRwrWxAuYrZg6iHrPseVthAAAmer+w4JAAAAEWAAAAAFJQkowAAABkAExLQAAAA+iQpli0ZPueCHp7QYx758RcXpCE58SPU3HPXfuDGFXHGEnSjbHKF9M4otFOBK8dTBPBlwTILohIRKCKLrfzZZ3yAnnNpR07DmDiuC0VFFYqF4mp7+54u0NkorebGPfT+Oyzx+5JqY5nE8H+vi/tc2ytQwnV2PZQhsxBoaVJrJluHgAAAAA=\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"self\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/ledgers/48638821\"\n"
          + "          },\n"
          + "          \"transactions\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/ledgers/48638821/transactions{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          },\n"
          + "          \"operations\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/ledgers/48638821/operations{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          },\n"
          + "          \"payments\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/ledgers/48638821/payments{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          },\n"
          + "          \"effects\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/ledgers/48638821/effects{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          }\n"
          + "        },\n"
          + "        \"id\": \"c1f60e0626a21e7d6375300b22a2cd0c1a18b07b78853f883b8836e827df475a\",\n"
          + "        \"paging_token\": \"208902145510998016\",\n"
          + "        \"hash\": \"c1f60e0626a21e7d6375300b22a2cd0c1a18b07b78853f883b8836e827df475a\",\n"
          + "        \"prev_hash\": \"f3a99918b9e3be4b2d2d19a49a1058ee3e14c691b2de0c0ab345cf7371b19f08\",\n"
          + "        \"sequence\": 48638821,\n"
          + "        \"successful_transaction_count\": 107,\n"
          + "        \"failed_transaction_count\": 171,\n"
          + "        \"operation_count\": 321,\n"
          + "        \"tx_set_operation_count\": 493,\n"
          + "        \"closed_at\": \"2023-10-20T06:00:40Z\",\n"
          + "        \"total_coins\": \"105443902087.3472865\",\n"
          + "        \"fee_pool\": \"4230864.3858480\",\n"
          + "        \"base_fee_in_stroops\": 100,\n"
          + "        \"base_reserve_in_stroops\": 5000000,\n"
          + "        \"max_tx_set_size\": 1000,\n"
          + "        \"protocol_version\": 19,\n"
          + "        \"header_xdr\": \"AAAAE/OpmRi5475LLS0ZpJoQWO4+FMaRst4MCrNFz3NxsZ8IKazZrKli28IjrqWNyZhzpZd/CXg3aMKnQVq83NvDJd8AAAAAZTIXiAAAAAAAAAABAAAAAFMKIvR3Lya8FJ+cNeaJjXTX3wCSqBxw3WYftnPvFVHeAAAAQMhQ82GdXyFDjpCzBC/4ZSW5SYDw5VjL5fyv/YQ4IltiSqKgUqaK0XNm2Pma5nKByEVvTgOqpeIUBnAxwCXlpAThqEzjvZ0BPLeGnA5YIODGbJWr7E8sq59l2JmSkWmT6uJAEKjmiDZ7qbQICtqGsO2d8dVSAE0Gs9ZmJZCvpVQkAuYrZQ6iHrPseVthAAAmer+wKDAAAAEWAAAAAFJQkk4AAABkAExLQAAAA+iQpli0ZPueCHp7QYx758RcXpCE58SPU3HPXfuDGFXHGEnSjbHKF9M4otFOBK8dTBPBlwTILohIRKCKLrfzZZ3yAnnNpR07DmDiuC0VFFYqF4mp7+54u0NkorebGPfT+Oyzx+5JqY5nE8H+vi/tc2ytQwnV2PZQhsxBoaVJrJluHgAAAAA=\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"self\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/ledgers/48638820\"\n"
          + "          },\n"
          + "          \"transactions\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/ledgers/48638820/transactions{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          },\n"
          + "          \"operations\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/ledgers/48638820/operations{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          },\n"
          + "          \"payments\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/ledgers/48638820/payments{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          },\n"
          + "          \"effects\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/ledgers/48638820/effects{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          }\n"
          + "        },\n"
          + "        \"id\": \"f3a99918b9e3be4b2d2d19a49a1058ee3e14c691b2de0c0ab345cf7371b19f08\",\n"
          + "        \"paging_token\": \"208902141216030720\",\n"
          + "        \"hash\": \"f3a99918b9e3be4b2d2d19a49a1058ee3e14c691b2de0c0ab345cf7371b19f08\",\n"
          + "        \"prev_hash\": \"1cf89c5a76f3a0ed4b60077a6f17399e83a38d2d2eb81ecb66893930a4f2f4ca\",\n"
          + "        \"sequence\": 48638820,\n"
          + "        \"successful_transaction_count\": 100,\n"
          + "        \"failed_transaction_count\": 144,\n"
          + "        \"operation_count\": 373,\n"
          + "        \"tx_set_operation_count\": 523,\n"
          + "        \"closed_at\": \"2023-10-20T06:00:35Z\",\n"
          + "        \"total_coins\": \"105443902087.3472865\",\n"
          + "        \"fee_pool\": \"4230864.3809180\",\n"
          + "        \"base_fee_in_stroops\": 100,\n"
          + "        \"base_reserve_in_stroops\": 5000000,\n"
          + "        \"max_tx_set_size\": 1000,\n"
          + "        \"protocol_version\": 19,\n"
          + "        \"header_xdr\": \"AAAAExz4nFp286DtS2AHem8XOZ6Do40tLrgey2aJOTCk8vTKxo0OIVaAQ9y6YvR4wW88JNf6pXS364wqZFe5rBZLqogAAAAAZTIXgwAAAAAAAAABAAAAADPN/Tz0hFfkg11MNisvWV3/3TyFQUjJNY5VqfZfvQp7AAAAQF6QJM4hGy0NYD0Q60KnRj39I2+Jr9uiY4obqLLDaT+az5X6HAaD9GeEX+7iPKD3eE7aylrfANYBIjuRbcpHxAo9Sfqi/qTCAr0Y5vQw3JqxViZthEzefMMjrzEh8cp/FqMQsqL2tmhp542WBRxt6rfv+wM11/aH5TpVgXngme32AuYrZA6iHrPseVthAAAmer+vZ5wAAAEWAAAAAFJQki0AAABkAExLQAAAA+iQpli0ZPueCHp7QYx758RcXpCE58SPU3HPXfuDGFXHGEnSjbHKF9M4otFOBK8dTBPBlwTILohIRKCKLrfzZZ3yAnnNpR07DmDiuC0VFFYqF4mp7+54u0NkorebGPfT+Oyzx+5JqY5nE8H+vi/tc2ytQwnV2PZQhsxBoaVJrJluHgAAAAA=\"\n"
          + "      }\n"
          + "    ]\n"
          + "  }\n"
          + "}\n";
}
