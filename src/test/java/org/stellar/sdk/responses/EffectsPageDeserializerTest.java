package org.stellar.sdk.responses;

import com.google.gson.reflect.TypeToken;

import junit.framework.TestCase;

import org.junit.Test;
import org.stellar.sdk.responses.effects.AccountCreatedEffectResponse;
import org.stellar.sdk.responses.effects.EffectResponse;
import org.stellar.sdk.responses.effects.SignerCreatedEffectResponse;

public class EffectsPageDeserializerTest extends TestCase {
  @Test
  public void testDeserialize() {
    Page<EffectResponse> effectsPage = GsonSingleton.getInstance().fromJson(json, new TypeToken<Page<EffectResponse>>() {}.getType());

    SignerCreatedEffectResponse signerCreatedEffect = (SignerCreatedEffectResponse) effectsPage.getRecords().get(0);
    assertEquals(signerCreatedEffect.getPublicKey(), "GAZHVTAM3NRJ6W643LOVA76T2W3TUKPF34ED5VNE4ZKJ2B5T2EUQHIQI");
    assertEquals(signerCreatedEffect.getPagingToken(), "3964757325385729-3");

    AccountCreatedEffectResponse accountCreatedEffect = (AccountCreatedEffectResponse) effectsPage.getRecords().get(8);
    assertEquals(accountCreatedEffect.getStartingBalance(), "10000.0");
    assertEquals(accountCreatedEffect.getAccount(), "GDIQJ6G5AWSBRMHIZYWVWCFN64Q4BZ4TYEAQRO5GVR4EWR23RKBJ2A4R");

    assertEquals(effectsPage.getLinks().getNext().getHref(), "http://horizon-testnet.stellar.org/effects?order=desc&limit=10&cursor=3962163165138945-3");
  }

  String json = "{\n" +
          "  \"_links\": {\n" +
          "    \"self\": {\n" +
          "      \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026limit=10\\u0026cursor=\"\n" +
          "    },\n" +
          "    \"next\": {\n" +
          "      \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026limit=10\\u0026cursor=3962163165138945-3\"\n" +
          "    },\n" +
          "    \"prev\": {\n" +
          "      \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026limit=10\\u0026cursor=3964757325385729-3\"\n" +
          "    }\n" +
          "  },\n" +
          "  \"_embedded\": {\n" +
          "    \"records\": [\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"operation\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3964757325385729\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3964757325385729-3\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3964757325385729-3\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"0003964757325385729-0000000003\",\n" +
          "        \"paging_token\": \"3964757325385729-3\",\n" +
          "        \"account\": \"GAZHVTAM3NRJ6W643LOVA76T2W3TUKPF34ED5VNE4ZKJ2B5T2EUQHIQI\",\n" +
          "        \"type\": \"signer_created\",\n" +
          "        \"type_i\": 10,\n" +
          "        \"weight\": 1,\n" +
          "        \"public_key\": \"GAZHVTAM3NRJ6W643LOVA76T2W3TUKPF34ED5VNE4ZKJ2B5T2EUQHIQI\"\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"operation\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3964757325385729\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3964757325385729-2\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3964757325385729-2\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"0003964757325385729-0000000002\",\n" +
          "        \"paging_token\": \"3964757325385729-2\",\n" +
          "        \"account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
          "        \"type\": \"account_debited\",\n" +
          "        \"type_i\": 3,\n" +
          "        \"asset_type\": \"native\",\n" +
          "        \"amount\": \"10000.0\"\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"operation\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3964757325385729\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3964757325385729-1\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3964757325385729-1\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"0003964757325385729-0000000001\",\n" +
          "        \"paging_token\": \"3964757325385729-1\",\n" +
          "        \"account\": \"GAZHVTAM3NRJ6W643LOVA76T2W3TUKPF34ED5VNE4ZKJ2B5T2EUQHIQI\",\n" +
          "        \"type\": \"account_created\",\n" +
          "        \"type_i\": 0,\n" +
          "        \"starting_balance\": \"10000.0\"\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"operation\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3964645656236033\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3964645656236033-3\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3964645656236033-3\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"0003964645656236033-0000000003\",\n" +
          "        \"paging_token\": \"3964645656236033-3\",\n" +
          "        \"account\": \"GA43X7ERPZQUVNDVHBGN42AK3SYJRVJ7TUWQGNFVY2O74YVDQFW2KP7C\",\n" +
          "        \"type\": \"signer_created\",\n" +
          "        \"type_i\": 10,\n" +
          "        \"weight\": 1,\n" +
          "        \"public_key\": \"GA43X7ERPZQUVNDVHBGN42AK3SYJRVJ7TUWQGNFVY2O74YVDQFW2KP7C\"\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"operation\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3964645656236033\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3964645656236033-2\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3964645656236033-2\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"0003964645656236033-0000000002\",\n" +
          "        \"paging_token\": \"3964645656236033-2\",\n" +
          "        \"account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
          "        \"type\": \"account_debited\",\n" +
          "        \"type_i\": 3,\n" +
          "        \"asset_type\": \"native\",\n" +
          "        \"amount\": \"10000.0\"\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"operation\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3964645656236033\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3964645656236033-1\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3964645656236033-1\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"0003964645656236033-0000000001\",\n" +
          "        \"paging_token\": \"3964645656236033-1\",\n" +
          "        \"account\": \"GA43X7ERPZQUVNDVHBGN42AK3SYJRVJ7TUWQGNFVY2O74YVDQFW2KP7C\",\n" +
          "        \"type\": \"account_created\",\n" +
          "        \"type_i\": 0,\n" +
          "        \"starting_balance\": \"10000.0\"\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"operation\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3963559029510145\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3963559029510145-3\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3963559029510145-3\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"0003963559029510145-0000000003\",\n" +
          "        \"paging_token\": \"3963559029510145-3\",\n" +
          "        \"account\": \"GDIQJ6G5AWSBRMHIZYWVWCFN64Q4BZ4TYEAQRO5GVR4EWR23RKBJ2A4R\",\n" +
          "        \"type\": \"signer_created\",\n" +
          "        \"type_i\": 10,\n" +
          "        \"weight\": 1,\n" +
          "        \"public_key\": \"GDIQJ6G5AWSBRMHIZYWVWCFN64Q4BZ4TYEAQRO5GVR4EWR23RKBJ2A4R\"\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"operation\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3963559029510145\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3963559029510145-2\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3963559029510145-2\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"0003963559029510145-0000000002\",\n" +
          "        \"paging_token\": \"3963559029510145-2\",\n" +
          "        \"account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
          "        \"type\": \"account_debited\",\n" +
          "        \"type_i\": 3,\n" +
          "        \"asset_type\": \"native\",\n" +
          "        \"amount\": \"10000.0\"\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"operation\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3963559029510145\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3963559029510145-1\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3963559029510145-1\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"0003963559029510145-0000000001\",\n" +
          "        \"paging_token\": \"3963559029510145-1\",\n" +
          "        \"account\": \"GDIQJ6G5AWSBRMHIZYWVWCFN64Q4BZ4TYEAQRO5GVR4EWR23RKBJ2A4R\",\n" +
          "        \"type\": \"account_created\",\n" +
          "        \"type_i\": 0,\n" +
          "        \"starting_balance\": \"10000.0\"\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"operation\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3962163165138945\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3962163165138945-3\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3962163165138945-3\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"0003962163165138945-0000000003\",\n" +
          "        \"paging_token\": \"3962163165138945-3\",\n" +
          "        \"account\": \"GAXHEECODLEAGGSC4ERUH3YW7G47IS7TS32NZQT4YLP5FK7HQZZWLLFC\",\n" +
          "        \"type\": \"signer_created\",\n" +
          "        \"type_i\": 10,\n" +
          "        \"weight\": 1,\n" +
          "        \"public_key\": \"GAXHEECODLEAGGSC4ERUH3YW7G47IS7TS32NZQT4YLP5FK7HQZZWLLFC\"\n" +
          "      }\n" +
          "    ]\n" +
          "  }\n" +
          "}";
}
