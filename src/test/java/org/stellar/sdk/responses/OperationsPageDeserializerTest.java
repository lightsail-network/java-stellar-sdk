package org.stellar.sdk.responses;

import com.google.gson.reflect.TypeToken;

import junit.framework.TestCase;

import org.junit.Test;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.responses.operations.CreateAccountOperationResponse;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;

public class OperationsPageDeserializerTest extends TestCase {
  @Test
  public void testDeserialize() {
    Page<OperationResponse> operationsPage = GsonSingleton.getInstance().fromJson(json, new TypeToken<Page<OperationResponse>>() {}.getType());

    CreateAccountOperationResponse createAccountOperation = (CreateAccountOperationResponse) operationsPage.getRecords().get(0);
    assertEquals(createAccountOperation.getStartingBalance(), "10000.0");
    assertEquals(createAccountOperation.getPagingToken(), "3717508943056897");
    assertEquals(createAccountOperation.getAccount().getAccountId(), "GDFH4NIYMIIAKRVEJJZOIGWKXGQUF3XHJG6ZM6CEA64AMTVDN44LHOQE");
    assertEquals(createAccountOperation.getFunder().getAccountId(), "GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K");
    assertEquals(createAccountOperation.getCreatedAt(), "2018-01-22T21:30:53Z");
    assertEquals(createAccountOperation.getTransactionHash(), "dd9d10c80a344f4464df3ecaa63705a5ef4a0533ff2f2099d5ef371ab5e1c046");

    PaymentOperationResponse paymentOperation = (PaymentOperationResponse) operationsPage.getRecords().get(4);
    assertEquals(paymentOperation.getAmount(), "10.123");
    TestCase.assertEquals(paymentOperation.getAsset(), new AssetTypeNative());
    assertEquals(paymentOperation.getFrom().getAccountId(), "GCYK67DDGBOANS6UODJ62QWGLEB2A7JQ3XUV25HCMLT7CI23PMMK3W6R");
    assertEquals(paymentOperation.getTo().getAccountId(), "GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H");
  }

  String json = "{\n" +
          "  \"_links\": {\n" +
          "    \"self\": {\n" +
          "      \"href\": \"http://horizon-testnet.stellar.org/operations?order=desc\\u0026limit=10\\u0026cursor=\"\n" +
          "    },\n" +
          "    \"next\": {\n" +
          "      \"href\": \"http://horizon-testnet.stellar.org/operations?order=desc\\u0026limit=10\\u0026cursor=3695540185337857\"\n" +
          "    },\n" +
          "    \"prev\": {\n" +
          "      \"href\": \"http://horizon-testnet.stellar.org/operations?order=asc\\u0026limit=10\\u0026cursor=3717508943056897\"\n" +
          "    }\n" +
          "  },\n" +
          "  \"_embedded\": {\n" +
          "    \"records\": [\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"self\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3717508943056897\"\n" +
          "          },\n" +
          "          \"transaction\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/transactions/ce81d957352501a46d9b938462cbef76283dcba8108d2649e0d79279a8f36488\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3717508943056897/effects\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3717508943056897\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3717508943056897\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"3717508943056897\",\n" +
          "        \"paging_token\": \"3717508943056897\",\n" +
          "        \"source_account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
          "        \"type\": \"create_account\",\n" +
          "        \"type_i\": 0,\n" +
          "        \"created_at\": \"2018-01-22T21:30:53Z\",\n" +
          "        \"transaction_hash\": \"dd9d10c80a344f4464df3ecaa63705a5ef4a0533ff2f2099d5ef371ab5e1c046\","+
          "        \"starting_balance\": \"10000.0\",\n" +
          "        \"funder\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
          "        \"account\": \"GDFH4NIYMIIAKRVEJJZOIGWKXGQUF3XHJG6ZM6CEA64AMTVDN44LHOQE\"\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"self\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3715417293983745\"\n" +
          "          },\n" +
          "          \"transaction\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/transactions/57d3ff20b53a21bd2a5c24838401e01fc13abc0193437d050dbdb8b7784cd674\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3715417293983745/effects\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3715417293983745\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3715417293983745\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"3715417293983745\",\n" +
          "        \"paging_token\": \"3715417293983745\",\n" +
          "        \"source_account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
          "        \"type\": \"create_account\",\n" +
          "        \"type_i\": 0,\n" +
          "        \"starting_balance\": \"10000.0\",\n" +
          "        \"funder\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
          "        \"account\": \"GA4GQOVE7SQCPGDCXOKIUWGZYJCMMA3TCJUB54ZYYCNMZD7MVILGEADL\"\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"self\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3711620542894081\"\n" +
          "          },\n" +
          "          \"transaction\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/transactions/e37b9dd7f36397e7a06ef121fb5446431585d30f8f3cf1d63a6d283e8f7b5a8c\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3711620542894081/effects\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3711620542894081\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3711620542894081\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"3711620542894081\",\n" +
          "        \"paging_token\": \"3711620542894081\",\n" +
          "        \"source_account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
          "        \"type\": \"create_account\",\n" +
          "        \"type_i\": 0,\n" +
          "        \"starting_balance\": \"10000.0\",\n" +
          "        \"funder\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
          "        \"account\": \"GDD2PUGSEGWN6TSHQNE6EDHVUYH6I37Y7727V3WFXUS2GGO4ZJ72EPEZ\"\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"self\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3709305555521537\"\n" +
          "          },\n" +
          "          \"transaction\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/transactions/af6ab37cfbeefc62a215ab7c4f64b007b666eed0c12dd92abbe0af08461d7b7f\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3709305555521537/effects\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3709305555521537\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3709305555521537\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"3709305555521537\",\n" +
          "        \"paging_token\": \"3709305555521537\",\n" +
          "        \"source_account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
          "        \"type\": \"create_account\",\n" +
          "        \"type_i\": 0,\n" +
          "        \"starting_balance\": \"10000.0\",\n" +
          "        \"funder\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
          "        \"account\": \"GDPMWVUTK7T5NG2OHDAZLGLT7QS5GCL23CFVNUR3BNKVPDLW2RULWE7Z\"\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"self\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3704821609664513\"\n" +
          "          },\n" +
          "          \"transaction\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/transactions/d4cb66d51cf773a4126ef8d535f03ba08cdc2389dc067e05c5d2ba1b335f19f2\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3704821609664513/effects\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3704821609664513\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3704821609664513\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"3704821609664513\",\n" +
          "        \"paging_token\": \"3704821609664513\",\n" +
          "        \"source_account\": \"GCYK67DDGBOANS6UODJ62QWGLEB2A7JQ3XUV25HCMLT7CI23PMMK3W6R\",\n" +
          "        \"type\": \"payment\",\n" +
          "        \"type_i\": 1,\n" +
          "        \"asset_type\": \"native\",\n" +
          "        \"from\": \"GCYK67DDGBOANS6UODJ62QWGLEB2A7JQ3XUV25HCMLT7CI23PMMK3W6R\",\n" +
          "        \"to\": \"GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H\",\n" +
          "        \"amount\": \"10.123\"\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"self\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3704778659991553\"\n" +
          "          },\n" +
          "          \"transaction\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/transactions/741a66dee5bafdefa1803bd80108fb86b075bbca80165bc4137c8f8ad1959efa\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3704778659991553/effects\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3704778659991553\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3704778659991553\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"3704778659991553\",\n" +
          "        \"paging_token\": \"3704778659991553\",\n" +
          "        \"source_account\": \"GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H\",\n" +
          "        \"type\": \"payment\",\n" +
          "        \"type_i\": 1,\n" +
          "        \"asset_type\": \"native\",\n" +
          "        \"from\": \"GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H\",\n" +
          "        \"to\": \"GCYK67DDGBOANS6UODJ62QWGLEB2A7JQ3XUV25HCMLT7CI23PMMK3W6R\",\n" +
          "        \"amount\": \"10.123\"\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"self\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3704435062607873\"\n" +
          "          },\n" +
          "          \"transaction\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/transactions/9bf15f7003c098c935d03bd178eda02b39cecb7a6eb53b4dd278aa9d4620c45b\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3704435062607873/effects\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3704435062607873\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3704435062607873\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"3704435062607873\",\n" +
          "        \"paging_token\": \"3704435062607873\",\n" +
          "        \"source_account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
          "        \"type\": \"create_account\",\n" +
          "        \"type_i\": 0,\n" +
          "        \"starting_balance\": \"10000.0\",\n" +
          "        \"funder\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
          "        \"account\": \"GCYK67DDGBOANS6UODJ62QWGLEB2A7JQ3XUV25HCMLT7CI23PMMK3W6R\"\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"self\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3700453627924481\"\n" +
          "          },\n" +
          "          \"transaction\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/transactions/aae8986da322e9405fd27f0e284817cb2e86618151ac54ef8734f629d8cf9446\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3700453627924481/effects\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3700453627924481\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3700453627924481\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"3700453627924481\",\n" +
          "        \"paging_token\": \"3700453627924481\",\n" +
          "        \"source_account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
          "        \"type\": \"create_account\",\n" +
          "        \"type_i\": 0,\n" +
          "        \"starting_balance\": \"10000.0\",\n" +
          "        \"funder\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
          "        \"account\": \"GA3J26UB775RW6M5INM75MJDPG72PMSUNZYBYU7IJL75UKMOIMJSHVEY\"\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"self\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3696369114025985\"\n" +
          "          },\n" +
          "          \"transaction\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/transactions/e852c069fbd0f3eafa691c276b0b57a4d0fad833e979fa192ad6b7a741892083\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3696369114025985/effects\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3696369114025985\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3696369114025985\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"3696369114025985\",\n" +
          "        \"paging_token\": \"3696369114025985\",\n" +
          "        \"source_account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
          "        \"type\": \"create_account\",\n" +
          "        \"type_i\": 0,\n" +
          "        \"starting_balance\": \"10000.0\",\n" +
          "        \"funder\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
          "        \"account\": \"GDIEI4DXAV57TDMNJSYWU7WDNVVS4GYU65YFMP7KQRGOU4TQALFYZUIJ\"\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"self\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3695540185337857\"\n" +
          "          },\n" +
          "          \"transaction\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/transactions/0028e0d640a74b372c4195575c785da61605b6a7da95998cbb56553850e97963\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/operations/3695540185337857/effects\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3695540185337857\"\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3695540185337857\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"3695540185337857\",\n" +
          "        \"paging_token\": \"3695540185337857\",\n" +
          "        \"source_account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
          "        \"type\": \"create_account\",\n" +
          "        \"type_i\": 0,\n" +
          "        \"starting_balance\": \"10000.0\",\n" +
          "        \"funder\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
          "        \"account\": \"GB3ZROOPBSDASLJUYCV7FJVGRWDJRB3MZAEY5CUZTRHLADEE5WW4AOIK\"\n" +
          "      }\n" +
          "    ]\n" +
          "  }\n" +
          "}";
}
