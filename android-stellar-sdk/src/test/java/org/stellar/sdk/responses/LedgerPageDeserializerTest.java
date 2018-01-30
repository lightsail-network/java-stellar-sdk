package org.stellar.sdk.responses;

import com.google.gson.reflect.TypeToken;

import junit.framework.TestCase;

import org.junit.Test;

public class LedgerPageDeserializerTest extends TestCase {
  @Test
  public void testDeserialize() {
    Page<LedgerResponse> ledgersPage = GsonSingleton.getInstance().fromJson(json, new TypeToken<Page<LedgerResponse>>() {}.getType());
    assertEquals(ledgersPage.getRecords().get(0).getHash(), "f6fc9a29b5ecec90348e17a10a60e019c5cb8ea24f66a1063e92c13391b09b8d");
    assertEquals(ledgersPage.getRecords().get(0).getPagingToken(), "3862202096287744");
    assertEquals(ledgersPage.getRecords().get(9).getHash(), "8552b7d130e602ed068bcfec729b3056d0c8b94d77f06d91cd1ec8323c2d6177");
    assertEquals(ledgersPage.getLinks().getNext().getHref(), "/ledgers?order=desc&limit=10&cursor=3862163441582080");
    assertEquals(ledgersPage.getLinks().getPrev().getHref(), "/ledgers?order=asc&limit=10&cursor=3862202096287744");
    assertEquals(ledgersPage.getLinks().getSelf().getHref(), "/ledgers?order=desc&limit=10&cursor=");
  }

  String json = "{\n" +
          "  \"_embedded\": {\n" +
          "    \"records\": [\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/ledgers/899239/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/ledgers/899239/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/ledgers/899239\"\n" +
          "          },\n" +
          "          \"transactions\": {\n" +
          "            \"href\": \"/ledgers/899239/transactions{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"f6fc9a29b5ecec90348e17a10a60e019c5cb8ea24f66a1063e92c13391b09b8d\",\n" +
          "        \"paging_token\": \"3862202096287744\",\n" +
          "        \"hash\": \"f6fc9a29b5ecec90348e17a10a60e019c5cb8ea24f66a1063e92c13391b09b8d\",\n" +
          "        \"prev_hash\": \"0bc807d3be86e8a9f7c913bc0e408f67402f50fbf4c0ea6583bc0f788075a567\",\n" +
          "        \"sequence\": 899239,\n" +
          "        \"transaction_count\": 0,\n" +
          "        \"operation_count\": 0,\n" +
          "        \"closed_at\": \"2015-11-19T22:01:34Z\",\n" +
          "        \"total_coins\": \"101343867604.8975480\",\n" +
          "        \"fee_pool\": \"1908.2251218\",\n" +
          "        \"base_fee\": 100,\n" +
          "        \"base_reserve\": \"10.0000000\",\n" +
          "        \"max_tx_set_size\": 50\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/ledgers/899238/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/ledgers/899238/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/ledgers/899238\"\n" +
          "          },\n" +
          "          \"transactions\": {\n" +
          "            \"href\": \"/ledgers/899238/transactions{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"0bc807d3be86e8a9f7c913bc0e408f67402f50fbf4c0ea6583bc0f788075a567\",\n" +
          "        \"paging_token\": \"3862197801320448\",\n" +
          "        \"hash\": \"0bc807d3be86e8a9f7c913bc0e408f67402f50fbf4c0ea6583bc0f788075a567\",\n" +
          "        \"prev_hash\": \"4a88c758baf11ff4265df0d7b304b737db7c7fad4b605deb887abaa1994c9be7\",\n" +
          "        \"sequence\": 899238,\n" +
          "        \"transaction_count\": 0,\n" +
          "        \"operation_count\": 0,\n" +
          "        \"closed_at\": \"2015-11-19T22:01:27Z\",\n" +
          "        \"total_coins\": \"101343867604.8975480\",\n" +
          "        \"fee_pool\": \"1908.2251218\",\n" +
          "        \"base_fee\": 100,\n" +
          "        \"base_reserve\": \"10.0000000\",\n" +
          "        \"max_tx_set_size\": 50\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/ledgers/899237/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/ledgers/899237/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/ledgers/899237\"\n" +
          "          },\n" +
          "          \"transactions\": {\n" +
          "            \"href\": \"/ledgers/899237/transactions{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"4a88c758baf11ff4265df0d7b304b737db7c7fad4b605deb887abaa1994c9be7\",\n" +
          "        \"paging_token\": \"3862193506353152\",\n" +
          "        \"hash\": \"4a88c758baf11ff4265df0d7b304b737db7c7fad4b605deb887abaa1994c9be7\",\n" +
          "        \"prev_hash\": \"6c8c24c0f4668e8f67d868bbfc851863f309da50f70341607a442f201da17190\",\n" +
          "        \"sequence\": 899237,\n" +
          "        \"transaction_count\": 0,\n" +
          "        \"operation_count\": 0,\n" +
          "        \"closed_at\": \"2015-11-19T22:01:25Z\",\n" +
          "        \"total_coins\": \"101343867604.8975480\",\n" +
          "        \"fee_pool\": \"1908.2251218\",\n" +
          "        \"base_fee\": 100,\n" +
          "        \"base_reserve\": \"10.0000000\",\n" +
          "        \"max_tx_set_size\": 50\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/ledgers/899236/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/ledgers/899236/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/ledgers/899236\"\n" +
          "          },\n" +
          "          \"transactions\": {\n" +
          "            \"href\": \"/ledgers/899236/transactions{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"6c8c24c0f4668e8f67d868bbfc851863f309da50f70341607a442f201da17190\",\n" +
          "        \"paging_token\": \"3862189211385856\",\n" +
          "        \"hash\": \"6c8c24c0f4668e8f67d868bbfc851863f309da50f70341607a442f201da17190\",\n" +
          "        \"prev_hash\": \"32efbdd295310a379621bda41397258708e5a4e606ad3c33c5a343289979ee7f\",\n" +
          "        \"sequence\": 899236,\n" +
          "        \"transaction_count\": 0,\n" +
          "        \"operation_count\": 0,\n" +
          "        \"closed_at\": \"2015-11-19T22:01:20Z\",\n" +
          "        \"total_coins\": \"101343867604.8975480\",\n" +
          "        \"fee_pool\": \"1908.2251218\",\n" +
          "        \"base_fee\": 100,\n" +
          "        \"base_reserve\": \"10.0000000\",\n" +
          "        \"max_tx_set_size\": 50\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/ledgers/899235/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/ledgers/899235/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/ledgers/899235\"\n" +
          "          },\n" +
          "          \"transactions\": {\n" +
          "            \"href\": \"/ledgers/899235/transactions{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"32efbdd295310a379621bda41397258708e5a4e606ad3c33c5a343289979ee7f\",\n" +
          "        \"paging_token\": \"3862184916418560\",\n" +
          "        \"hash\": \"32efbdd295310a379621bda41397258708e5a4e606ad3c33c5a343289979ee7f\",\n" +
          "        \"prev_hash\": \"65349e138f295aff14f30d1bcce6f9fb76055ca561004416194572e90cb2d05f\",\n" +
          "        \"sequence\": 899235,\n" +
          "        \"transaction_count\": 0,\n" +
          "        \"operation_count\": 0,\n" +
          "        \"closed_at\": \"2015-11-19T22:01:15Z\",\n" +
          "        \"total_coins\": \"101343867604.8975480\",\n" +
          "        \"fee_pool\": \"1908.2251218\",\n" +
          "        \"base_fee\": 100,\n" +
          "        \"base_reserve\": \"10.0000000\",\n" +
          "        \"max_tx_set_size\": 50\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/ledgers/899234/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/ledgers/899234/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/ledgers/899234\"\n" +
          "          },\n" +
          "          \"transactions\": {\n" +
          "            \"href\": \"/ledgers/899234/transactions{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"65349e138f295aff14f30d1bcce6f9fb76055ca561004416194572e90cb2d05f\",\n" +
          "        \"paging_token\": \"3862180621451264\",\n" +
          "        \"hash\": \"65349e138f295aff14f30d1bcce6f9fb76055ca561004416194572e90cb2d05f\",\n" +
          "        \"prev_hash\": \"ec548930241d677c712381cfc72b0c57bd4c0ce10ef7e7c3b7c693387f932aa5\",\n" +
          "        \"sequence\": 899234,\n" +
          "        \"transaction_count\": 0,\n" +
          "        \"operation_count\": 0,\n" +
          "        \"closed_at\": \"2015-11-19T22:01:12Z\",\n" +
          "        \"total_coins\": \"101343867604.8975480\",\n" +
          "        \"fee_pool\": \"1908.2251218\",\n" +
          "        \"base_fee\": 100,\n" +
          "        \"base_reserve\": \"10.0000000\",\n" +
          "        \"max_tx_set_size\": 50\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/ledgers/899233/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/ledgers/899233/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/ledgers/899233\"\n" +
          "          },\n" +
          "          \"transactions\": {\n" +
          "            \"href\": \"/ledgers/899233/transactions{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"ec548930241d677c712381cfc72b0c57bd4c0ce10ef7e7c3b7c693387f932aa5\",\n" +
          "        \"paging_token\": \"3862176326483968\",\n" +
          "        \"hash\": \"ec548930241d677c712381cfc72b0c57bd4c0ce10ef7e7c3b7c693387f932aa5\",\n" +
          "        \"prev_hash\": \"03dfe28b30dad13a95eaf20681613ecad1795f527182103524e1f5b106fefd53\",\n" +
          "        \"sequence\": 899233,\n" +
          "        \"transaction_count\": 0,\n" +
          "        \"operation_count\": 0,\n" +
          "        \"closed_at\": \"2015-11-19T22:01:09Z\",\n" +
          "        \"total_coins\": \"101343867604.8975480\",\n" +
          "        \"fee_pool\": \"1908.2251218\",\n" +
          "        \"base_fee\": 100,\n" +
          "        \"base_reserve\": \"10.0000000\",\n" +
          "        \"max_tx_set_size\": 50\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/ledgers/899232/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/ledgers/899232/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/ledgers/899232\"\n" +
          "          },\n" +
          "          \"transactions\": {\n" +
          "            \"href\": \"/ledgers/899232/transactions{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"03dfe28b30dad13a95eaf20681613ecad1795f527182103524e1f5b106fefd53\",\n" +
          "        \"paging_token\": \"3862172031516672\",\n" +
          "        \"hash\": \"03dfe28b30dad13a95eaf20681613ecad1795f527182103524e1f5b106fefd53\",\n" +
          "        \"prev_hash\": \"03abed895270fd6a534c7d8813778e932dd6b5894aa350b085ce8d9e46ce32fa\",\n" +
          "        \"sequence\": 899232,\n" +
          "        \"transaction_count\": 0,\n" +
          "        \"operation_count\": 0,\n" +
          "        \"closed_at\": \"2015-11-19T22:01:05Z\",\n" +
          "        \"total_coins\": \"101343867604.8975480\",\n" +
          "        \"fee_pool\": \"1908.2251218\",\n" +
          "        \"base_fee\": 100,\n" +
          "        \"base_reserve\": \"10.0000000\",\n" +
          "        \"max_tx_set_size\": 50\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/ledgers/899231/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/ledgers/899231/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/ledgers/899231\"\n" +
          "          },\n" +
          "          \"transactions\": {\n" +
          "            \"href\": \"/ledgers/899231/transactions{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"03abed895270fd6a534c7d8813778e932dd6b5894aa350b085ce8d9e46ce32fa\",\n" +
          "        \"paging_token\": \"3862167736549376\",\n" +
          "        \"hash\": \"03abed895270fd6a534c7d8813778e932dd6b5894aa350b085ce8d9e46ce32fa\",\n" +
          "        \"prev_hash\": \"8552b7d130e602ed068bcfec729b3056d0c8b94d77f06d91cd1ec8323c2d6177\",\n" +
          "        \"sequence\": 899231,\n" +
          "        \"transaction_count\": 0,\n" +
          "        \"operation_count\": 0,\n" +
          "        \"closed_at\": \"2015-11-19T22:01:02Z\",\n" +
          "        \"total_coins\": \"101343867604.8975480\",\n" +
          "        \"fee_pool\": \"1908.2251218\",\n" +
          "        \"base_fee\": 100,\n" +
          "        \"base_reserve\": \"10.0000000\",\n" +
          "        \"max_tx_set_size\": 50\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/ledgers/899230/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/ledgers/899230/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/ledgers/899230\"\n" +
          "          },\n" +
          "          \"transactions\": {\n" +
          "            \"href\": \"/ledgers/899230/transactions{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"8552b7d130e602ed068bcfec729b3056d0c8b94d77f06d91cd1ec8323c2d6177\",\n" +
          "        \"paging_token\": \"3862163441582080\",\n" +
          "        \"hash\": \"8552b7d130e602ed068bcfec729b3056d0c8b94d77f06d91cd1ec8323c2d6177\",\n" +
          "        \"prev_hash\": \"62efa4d5059f438aef2e3c059162b8fca9c59317bdbdd1b04dc92924302a455e\",\n" +
          "        \"sequence\": 899230,\n" +
          "        \"transaction_count\": 0,\n" +
          "        \"operation_count\": 0,\n" +
          "        \"closed_at\": \"2015-11-19T22:00:59Z\",\n" +
          "        \"total_coins\": \"101343867604.8975480\",\n" +
          "        \"fee_pool\": \"1908.2251218\",\n" +
          "        \"base_fee\": 100,\n" +
          "        \"base_reserve\": \"10.0000000\",\n" +
          "        \"max_tx_set_size\": 50\n" +
          "      }\n" +
          "    ]\n" +
          "  },\n" +
          "  \"_links\": {\n" +
          "    \"next\": {\n" +
          "      \"href\": \"/ledgers?order=desc\\u0026limit=10\\u0026cursor=3862163441582080\"\n" +
          "    },\n" +
          "    \"prev\": {\n" +
          "      \"href\": \"/ledgers?order=asc\\u0026limit=10\\u0026cursor=3862202096287744\"\n" +
          "    },\n" +
          "    \"self\": {\n" +
          "      \"href\": \"/ledgers?order=desc\\u0026limit=10\\u0026cursor=\"\n" +
          "    }\n" +
          "  }\n" +
          "}";
}
