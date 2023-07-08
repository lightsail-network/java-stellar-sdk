package org.stellar.sdk.responses;

import static java.lang.Long.valueOf;

import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;
import org.junit.Test;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.Memo;
import org.stellar.sdk.responses.operations.CreateAccountOperationResponse;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;
import org.stellar.sdk.responses.operations.RevokeSponsorshipOperationResponse;

public class OperationsPageDeserializerTest extends TestCase {
  @Test
  public void testDeserialize() {
    Page<OperationResponse> operationsPage =
        GsonSingleton.getInstance()
            .fromJson(json, new TypeToken<Page<OperationResponse>>() {}.getType());

    CreateAccountOperationResponse createAccountOperation =
        (CreateAccountOperationResponse) operationsPage.getRecords().get(0);
    assertEquals(createAccountOperation.getStartingBalance(), "10000.0");
    assertEquals(createAccountOperation.getPagingToken(), "3717508943056897");
    assertEquals(
        createAccountOperation.getAccount(),
        "GDFH4NIYMIIAKRVEJJZOIGWKXGQUF3XHJG6ZM6CEA64AMTVDN44LHOQE");
    assertEquals(
        createAccountOperation.getFunder(),
        "GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K");
    assertEquals(createAccountOperation.getCreatedAt(), "2018-01-22T21:30:53Z");
    assertEquals(
        createAccountOperation.getTransactionHash(),
        "dd9d10c80a344f4464df3ecaa63705a5ef4a0533ff2f2099d5ef371ab5e1c046");
    assertFalse(createAccountOperation.getTransaction().isPresent());

    PaymentOperationResponse paymentOperation =
        (PaymentOperationResponse) operationsPage.getRecords().get(4);
    assertEquals(paymentOperation.getAmount(), "10.123");
    TestCase.assertEquals(paymentOperation.getAsset(), new AssetTypeNative());
    assertEquals(
        paymentOperation.getFrom(), "GCYK67DDGBOANS6UODJ62QWGLEB2A7JQ3XUV25HCMLT7CI23PMMK3W6R");
    assertEquals(
        paymentOperation.getTo(), "GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H");
    assertFalse(paymentOperation.getTransaction().isPresent());
  }

  @Test
  public void testDeserializeWithTransactions() {
    Page<OperationResponse> operationsPage =
        GsonSingleton.getInstance()
            .fromJson(
                includeTransactionsJSON, new TypeToken<Page<OperationResponse>>() {}.getType());

    assertEquals(operationsPage.getRecords().size(), 1);
    CreateAccountOperationResponse createAccountOperation =
        (CreateAccountOperationResponse) operationsPage.getRecords().get(0);
    assertEquals(createAccountOperation.getStartingBalance(), "10000000000.0000000");
    assertEquals(createAccountOperation.getPagingToken(), "828928692225");
    assertEquals(
        createAccountOperation.getAccount(),
        "GAIH3ULLFQ4DGSECF2AR555KZ4KNDGEKN4AFI4SU2M7B43MGK3QJZNSR");
    assertEquals(
        createAccountOperation.getFunder(),
        "GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H");
    assertEquals(createAccountOperation.getCreatedAt(), "2019-07-31T09:30:47Z");
    assertEquals(
        createAccountOperation.getTransactionHash(),
        "749e4f8933221b9942ef38a02856803f379789ec8d971f1f60535db70135673e");
    assertTrue(createAccountOperation.getTransaction().isPresent());

    TransactionResponse transaction = createAccountOperation.getTransaction().get();
    assertEquals(
        transaction.getHash(), "749e4f8933221b9942ef38a02856803f379789ec8d971f1f60535db70135673e");
    assertEquals(transaction.getLedger(), valueOf(193));
    assertEquals(transaction.getMemo(), Memo.none());
  }

  @Test
  public void testDeserializeRevokeSponsorship() {
    Page<OperationResponse> operationsPage =
        GsonSingleton.getInstance()
            .fromJson(revokeSponsorshipJSON, new TypeToken<Page<OperationResponse>>() {}.getType());
    RevokeSponsorshipOperationResponse revokeOp =
        ((RevokeSponsorshipOperationResponse) operationsPage.getRecords().get(0));

    assertFalse(revokeOp.getAccountId().isPresent());
    assertFalse(revokeOp.getClaimableBalanceId().isPresent());
    assertFalse(revokeOp.getDataAccountId().isPresent());
    assertFalse(revokeOp.getDataName().isPresent());
    assertFalse(revokeOp.getSignerAccountId().isPresent());
    assertFalse(revokeOp.getSignerKey().isPresent());
    assertFalse(revokeOp.getTrustlineAccountId().isPresent());
    assertFalse(revokeOp.getTrustlineAsset().isPresent());
    assertEquals(revokeOp.getOfferId().get(), valueOf(8822470));
    assertEquals(
        revokeOp.getSourceAccount(), "GB6QDNU47MYBR4NDTRP7M3FW27DAFOEADN5KDQI2DAVWW6YVKKG4QJS7");
  }

  String revokeSponsorshipJSON =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"self\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/transactions/02a69bb0dc83d004ae918aab2d10c9dbc2cbf4451ab12927a691b87e5c6c5079/operations?cursor=\\u0026limit=10\\u0026order=asc\"\n"
          + "    },\n"
          + "    \"next\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/transactions/02a69bb0dc83d004ae918aab2d10c9dbc2cbf4451ab12927a691b87e5c6c5079/operations?cursor=4458463816060929\\u0026limit=10\\u0026order=asc\"\n"
          + "    },\n"
          + "    \"prev\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/transactions/02a69bb0dc83d004ae918aab2d10c9dbc2cbf4451ab12927a691b87e5c6c5079/operations?cursor=4458463816060929\\u0026limit=10\\u0026order=desc\"\n"
          + "    }\n"
          + "  },\n"
          + "  \"_embedded\": {\n"
          + "    \"records\": [\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"self\": {\n"
          + "            \"href\": \"https://horizon-testnet.stellar.org/operations/4458463816060929\"\n"
          + "          },\n"
          + "          \"transaction\": {\n"
          + "            \"href\": \"https://horizon-testnet.stellar.org/transactions/02a69bb0dc83d004ae918aab2d10c9dbc2cbf4451ab12927a691b87e5c6c5079\"\n"
          + "          },\n"
          + "          \"effects\": {\n"
          + "            \"href\": \"https://horizon-testnet.stellar.org/operations/4458463816060929/effects\"\n"
          + "          },\n"
          + "          \"succeeds\": {\n"
          + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=4458463816060929\"\n"
          + "          },\n"
          + "          \"precedes\": {\n"
          + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=4458463816060929\"\n"
          + "          }\n"
          + "        },\n"
          + "        \"id\": \"4458463816060929\",\n"
          + "        \"paging_token\": \"4458463816060929\",\n"
          + "        \"transaction_successful\": true,\n"
          + "        \"source_account\": \"GB6QDNU47MYBR4NDTRP7M3FW27DAFOEADN5KDQI2DAVWW6YVKKG4QJS7\",\n"
          + "        \"type\": \"revoke_sponsorship\",\n"
          + "        \"type_i\": 18,\n"
          + "        \"created_at\": \"2020-10-02T20:35:22Z\",\n"
          + "        \"transaction_hash\": \"02a69bb0dc83d004ae918aab2d10c9dbc2cbf4451ab12927a691b87e5c6c5079\",\n"
          + "        \"offer_id\": \"8822470\"\n"
          + "      }\n"
          + "    ]\n"
          + "  }\n"
          + "}";

  String json =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"self\": {\n"
          + "      \"href\": \"http://horizon-testnet.stellar.org/operations?order=desc\\u0026limit=10\\u0026cursor=\"\n"
          + "    },\n"
          + "    \"next\": {\n"
          + "      \"href\": \"http://horizon-testnet.stellar.org/operations?order=desc\\u0026limit=10\\u0026cursor=3695540185337857\"\n"
          + "    },\n"
          + "    \"prev\": {\n"
          + "      \"href\": \"http://horizon-testnet.stellar.org/operations?order=asc\\u0026limit=10\\u0026cursor=3717508943056897\"\n"
          + "    }\n"
          + "  },\n"
          + "  \"_embedded\": {\n"
          + "    \"records\": [\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"self\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3717508943056897\"\n"
          + "          },\n"
          + "          \"transaction\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/transactions/ce81d957352501a46d9b938462cbef76283dcba8108d2649e0d79279a8f36488\"\n"
          + "          },\n"
          + "          \"effects\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3717508943056897/effects\"\n"
          + "          },\n"
          + "          \"succeeds\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3717508943056897\"\n"
          + "          },\n"
          + "          \"precedes\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3717508943056897\"\n"
          + "          }\n"
          + "        },\n"
          + "        \"id\": \"3717508943056897\",\n"
          + "        \"paging_token\": \"3717508943056897\",\n"
          + "        \"source_account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n"
          + "        \"type\": \"create_account\",\n"
          + "        \"type_i\": 0,\n"
          + "        \"created_at\": \"2018-01-22T21:30:53Z\",\n"
          + "        \"transaction_hash\": \"dd9d10c80a344f4464df3ecaa63705a5ef4a0533ff2f2099d5ef371ab5e1c046\","
          + "        \"starting_balance\": \"10000.0\",\n"
          + "        \"funder\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n"
          + "        \"account\": \"GDFH4NIYMIIAKRVEJJZOIGWKXGQUF3XHJG6ZM6CEA64AMTVDN44LHOQE\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"self\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3715417293983745\"\n"
          + "          },\n"
          + "          \"transaction\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/transactions/57d3ff20b53a21bd2a5c24838401e01fc13abc0193437d050dbdb8b7784cd674\"\n"
          + "          },\n"
          + "          \"effects\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3715417293983745/effects\"\n"
          + "          },\n"
          + "          \"succeeds\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3715417293983745\"\n"
          + "          },\n"
          + "          \"precedes\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3715417293983745\"\n"
          + "          }\n"
          + "        },\n"
          + "        \"id\": \"3715417293983745\",\n"
          + "        \"paging_token\": \"3715417293983745\",\n"
          + "        \"source_account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n"
          + "        \"type\": \"create_account\",\n"
          + "        \"type_i\": 0,\n"
          + "        \"starting_balance\": \"10000.0\",\n"
          + "        \"funder\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n"
          + "        \"account\": \"GA4GQOVE7SQCPGDCXOKIUWGZYJCMMA3TCJUB54ZYYCNMZD7MVILGEADL\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"self\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3711620542894081\"\n"
          + "          },\n"
          + "          \"transaction\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/transactions/e37b9dd7f36397e7a06ef121fb5446431585d30f8f3cf1d63a6d283e8f7b5a8c\"\n"
          + "          },\n"
          + "          \"effects\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3711620542894081/effects\"\n"
          + "          },\n"
          + "          \"succeeds\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3711620542894081\"\n"
          + "          },\n"
          + "          \"precedes\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3711620542894081\"\n"
          + "          }\n"
          + "        },\n"
          + "        \"id\": \"3711620542894081\",\n"
          + "        \"paging_token\": \"3711620542894081\",\n"
          + "        \"source_account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n"
          + "        \"type\": \"create_account\",\n"
          + "        \"type_i\": 0,\n"
          + "        \"starting_balance\": \"10000.0\",\n"
          + "        \"funder\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n"
          + "        \"account\": \"GDD2PUGSEGWN6TSHQNE6EDHVUYH6I37Y7727V3WFXUS2GGO4ZJ72EPEZ\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"self\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3709305555521537\"\n"
          + "          },\n"
          + "          \"transaction\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/transactions/af6ab37cfbeefc62a215ab7c4f64b007b666eed0c12dd92abbe0af08461d7b7f\"\n"
          + "          },\n"
          + "          \"effects\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3709305555521537/effects\"\n"
          + "          },\n"
          + "          \"succeeds\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3709305555521537\"\n"
          + "          },\n"
          + "          \"precedes\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3709305555521537\"\n"
          + "          }\n"
          + "        },\n"
          + "        \"id\": \"3709305555521537\",\n"
          + "        \"paging_token\": \"3709305555521537\",\n"
          + "        \"source_account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n"
          + "        \"type\": \"create_account\",\n"
          + "        \"type_i\": 0,\n"
          + "        \"starting_balance\": \"10000.0\",\n"
          + "        \"funder\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n"
          + "        \"account\": \"GDPMWVUTK7T5NG2OHDAZLGLT7QS5GCL23CFVNUR3BNKVPDLW2RULWE7Z\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"self\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3704821609664513\"\n"
          + "          },\n"
          + "          \"transaction\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/transactions/d4cb66d51cf773a4126ef8d535f03ba08cdc2389dc067e05c5d2ba1b335f19f2\"\n"
          + "          },\n"
          + "          \"effects\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3704821609664513/effects\"\n"
          + "          },\n"
          + "          \"succeeds\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3704821609664513\"\n"
          + "          },\n"
          + "          \"precedes\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3704821609664513\"\n"
          + "          }\n"
          + "        },\n"
          + "        \"id\": \"3704821609664513\",\n"
          + "        \"paging_token\": \"3704821609664513\",\n"
          + "        \"source_account\": \"GCYK67DDGBOANS6UODJ62QWGLEB2A7JQ3XUV25HCMLT7CI23PMMK3W6R\",\n"
          + "        \"type\": \"payment\",\n"
          + "        \"type_i\": 1,\n"
          + "        \"asset_type\": \"native\",\n"
          + "        \"from\": \"GCYK67DDGBOANS6UODJ62QWGLEB2A7JQ3XUV25HCMLT7CI23PMMK3W6R\",\n"
          + "        \"to\": \"GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H\",\n"
          + "        \"amount\": \"10.123\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"self\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3704778659991553\"\n"
          + "          },\n"
          + "          \"transaction\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/transactions/741a66dee5bafdefa1803bd80108fb86b075bbca80165bc4137c8f8ad1959efa\"\n"
          + "          },\n"
          + "          \"effects\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3704778659991553/effects\"\n"
          + "          },\n"
          + "          \"succeeds\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3704778659991553\"\n"
          + "          },\n"
          + "          \"precedes\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3704778659991553\"\n"
          + "          }\n"
          + "        },\n"
          + "        \"id\": \"3704778659991553\",\n"
          + "        \"paging_token\": \"3704778659991553\",\n"
          + "        \"source_account\": \"GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H\",\n"
          + "        \"type\": \"payment\",\n"
          + "        \"type_i\": 1,\n"
          + "        \"asset_type\": \"native\",\n"
          + "        \"from\": \"GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H\",\n"
          + "        \"to\": \"GCYK67DDGBOANS6UODJ62QWGLEB2A7JQ3XUV25HCMLT7CI23PMMK3W6R\",\n"
          + "        \"amount\": \"10.123\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"self\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3704435062607873\"\n"
          + "          },\n"
          + "          \"transaction\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/transactions/9bf15f7003c098c935d03bd178eda02b39cecb7a6eb53b4dd278aa9d4620c45b\"\n"
          + "          },\n"
          + "          \"effects\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3704435062607873/effects\"\n"
          + "          },\n"
          + "          \"succeeds\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3704435062607873\"\n"
          + "          },\n"
          + "          \"precedes\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3704435062607873\"\n"
          + "          }\n"
          + "        },\n"
          + "        \"id\": \"3704435062607873\",\n"
          + "        \"paging_token\": \"3704435062607873\",\n"
          + "        \"source_account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n"
          + "        \"type\": \"create_account\",\n"
          + "        \"type_i\": 0,\n"
          + "        \"starting_balance\": \"10000.0\",\n"
          + "        \"funder\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n"
          + "        \"account\": \"GCYK67DDGBOANS6UODJ62QWGLEB2A7JQ3XUV25HCMLT7CI23PMMK3W6R\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"self\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3700453627924481\"\n"
          + "          },\n"
          + "          \"transaction\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/transactions/aae8986da322e9405fd27f0e284817cb2e86618151ac54ef8734f629d8cf9446\"\n"
          + "          },\n"
          + "          \"effects\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3700453627924481/effects\"\n"
          + "          },\n"
          + "          \"succeeds\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3700453627924481\"\n"
          + "          },\n"
          + "          \"precedes\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3700453627924481\"\n"
          + "          }\n"
          + "        },\n"
          + "        \"id\": \"3700453627924481\",\n"
          + "        \"paging_token\": \"3700453627924481\",\n"
          + "        \"source_account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n"
          + "        \"type\": \"create_account\",\n"
          + "        \"type_i\": 0,\n"
          + "        \"starting_balance\": \"10000.0\",\n"
          + "        \"funder\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n"
          + "        \"account\": \"GA3J26UB775RW6M5INM75MJDPG72PMSUNZYBYU7IJL75UKMOIMJSHVEY\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"self\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3696369114025985\"\n"
          + "          },\n"
          + "          \"transaction\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/transactions/e852c069fbd0f3eafa691c276b0b57a4d0fad833e979fa192ad6b7a741892083\"\n"
          + "          },\n"
          + "          \"effects\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3696369114025985/effects\"\n"
          + "          },\n"
          + "          \"succeeds\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3696369114025985\"\n"
          + "          },\n"
          + "          \"precedes\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3696369114025985\"\n"
          + "          }\n"
          + "        },\n"
          + "        \"id\": \"3696369114025985\",\n"
          + "        \"paging_token\": \"3696369114025985\",\n"
          + "        \"source_account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n"
          + "        \"type\": \"create_account\",\n"
          + "        \"type_i\": 0,\n"
          + "        \"starting_balance\": \"10000.0\",\n"
          + "        \"funder\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n"
          + "        \"account\": \"GDIEI4DXAV57TDMNJSYWU7WDNVVS4GYU65YFMP7KQRGOU4TQALFYZUIJ\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"self\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3695540185337857\"\n"
          + "          },\n"
          + "          \"transaction\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/transactions/0028e0d640a74b372c4195575c785da61605b6a7da95998cbb56553850e97963\"\n"
          + "          },\n"
          + "          \"effects\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3695540185337857/effects\"\n"
          + "          },\n"
          + "          \"succeeds\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3695540185337857\"\n"
          + "          },\n"
          + "          \"precedes\": {\n"
          + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3695540185337857\"\n"
          + "          }\n"
          + "        },\n"
          + "        \"id\": \"3695540185337857\",\n"
          + "        \"paging_token\": \"3695540185337857\",\n"
          + "        \"source_account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n"
          + "        \"type\": \"create_account\",\n"
          + "        \"type_i\": 0,\n"
          + "        \"starting_balance\": \"10000.0\",\n"
          + "        \"funder\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n"
          + "        \"account\": \"GB3ZROOPBSDASLJUYCV7FJVGRWDJRB3MZAEY5CUZTRHLADEE5WW4AOIK\"\n"
          + "      }\n"
          + "    ]\n"
          + "  }\n"
          + "}";

  String includeTransactionsJSON =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"self\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/operations?cursor=\\u0026join=transactions\\u0026limit=1\\u0026order=asc\"\n"
          + "    },\n"
          + "    \"next\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/operations?cursor=828928692225\\u0026join=transactions\\u0026limit=1\\u0026order=asc\"\n"
          + "    },\n"
          + "    \"prev\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/operations?cursor=828928692225\\u0026join=transactions\\u0026limit=1\\u0026order=desc\"\n"
          + "    }\n"
          + "  },\n"
          + "  \"_embedded\": {\n"
          + "    \"records\": [\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"self\": {\n"
          + "            \"href\": \"https://horizon-testnet.stellar.org/operations/828928692225\"\n"
          + "          },\n"
          + "          \"transaction\": {\n"
          + "            \"href\": \"https://horizon-testnet.stellar.org/transactions/749e4f8933221b9942ef38a02856803f379789ec8d971f1f60535db70135673e\"\n"
          + "          },\n"
          + "          \"effects\": {\n"
          + "            \"href\": \"https://horizon-testnet.stellar.org/operations/828928692225/effects\"\n"
          + "          },\n"
          + "          \"succeeds\": {\n"
          + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=828928692225\"\n"
          + "          },\n"
          + "          \"precedes\": {\n"
          + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=828928692225\"\n"
          + "          }\n"
          + "        },\n"
          + "        \"id\": \"828928692225\",\n"
          + "        \"paging_token\": \"828928692225\",\n"
          + "        \"transaction_successful\": true,\n"
          + "        \"source_account\": \"GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H\",\n"
          + "        \"type\": \"create_account\",\n"
          + "        \"type_i\": 0,\n"
          + "        \"created_at\": \"2019-07-31T09:30:47Z\",\n"
          + "        \"transaction_hash\": \"749e4f8933221b9942ef38a02856803f379789ec8d971f1f60535db70135673e\",\n"
          + "        \"transaction\": {\n"
          + "          \"_links\": {\n"
          + "            \"self\": {\n"
          + "              \"href\": \"https://horizon-testnet.stellar.org/transactions/749e4f8933221b9942ef38a02856803f379789ec8d971f1f60535db70135673e\"\n"
          + "            },\n"
          + "            \"account\": {\n"
          + "              \"href\": \"https://horizon-testnet.stellar.org/accounts/GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H\"\n"
          + "            },\n"
          + "            \"ledger\": {\n"
          + "              \"href\": \"https://horizon-testnet.stellar.org/ledgers/193\"\n"
          + "            },\n"
          + "            \"operations\": {\n"
          + "              \"href\": \"https://horizon-testnet.stellar.org/transactions/749e4f8933221b9942ef38a02856803f379789ec8d971f1f60535db70135673e/operations{?cursor,limit,order}\",\n"
          + "              \"templated\": true\n"
          + "            },\n"
          + "            \"effects\": {\n"
          + "              \"href\": \"https://horizon-testnet.stellar.org/transactions/749e4f8933221b9942ef38a02856803f379789ec8d971f1f60535db70135673e/effects{?cursor,limit,order}\",\n"
          + "              \"templated\": true\n"
          + "            },\n"
          + "            \"precedes\": {\n"
          + "              \"href\": \"https://horizon-testnet.stellar.org/transactions?order=asc\\u0026cursor=828928692224\"\n"
          + "            },\n"
          + "            \"succeeds\": {\n"
          + "              \"href\": \"https://horizon-testnet.stellar.org/transactions?order=desc\\u0026cursor=828928692224\"\n"
          + "            }\n"
          + "          },\n"
          + "          \"id\": \"749e4f8933221b9942ef38a02856803f379789ec8d971f1f60535db70135673e\",\n"
          + "          \"paging_token\": \"828928692224\",\n"
          + "          \"successful\": true,\n"
          + "          \"hash\": \"749e4f8933221b9942ef38a02856803f379789ec8d971f1f60535db70135673e\",\n"
          + "          \"ledger\": 193,\n"
          + "          \"created_at\": \"2019-07-31T09:30:47Z\",\n"
          + "          \"source_account\": \"GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H\",\n"
          + "          \"source_account_sequence\": \"1\",\n"
          + "          \"fee_paid\": 1000,\n"
          + "          \"fee_charged\": 1000,\n"
          + "          \"max_fee\": 1000,\n"
          + "          \"operation_count\": 10,\n"
          + "          \"envelope_xdr\": \"AAAAAGL8HQvQkbK2HA3WVjRrKmjX00fG8sLI7m0ERwJW/AX3AAAD6AAAAAAAAAABAAAAAAAAAAAAAAAKAAAAAAAAAAAAAAAAEH3Rayw4M0iCLoEe96rPFNGYim8AVHJU0z4ebYZW4JwBY0V4XYoAAAAAAAAAAAAAAAAAAN2+SherpNcNX0imC680fIBdpQfgBwIuqFOgmlobpwLJAAAAF0h26AAAAAAAAAAAAAAAAAAAlRt2go9sp7E1a5ZWvr7vin4UPrFQThpQax1lOFm33AAAABdIdugAAAAAAAAAAAAAAAAAmv+knlR6JR2VqWeU0k/4FgvZ/tSV5DEY4gu0iOTKgpUAAAAXSHboAAAAAAAAAAAAAAAAANpaWLojuOtfC0cmMh+DvQTfPDrkfXhblQTdFXrGYc0bAAAAF0h26AAAAAABAAAAAACVG3aCj2ynsTVrlla+vu+KfhQ+sVBOGlBrHWU4WbfcAAAABgAAAAFURVNUAAAAANpaWLojuOtfC0cmMh+DvQTfPDrkfXhblQTdFXrGYc0bf/////////8AAAABAAAAAJr/pJ5UeiUdlalnlNJP+BYL2f7UleQxGOILtIjkyoKVAAAABgAAAAFURVNUAAAAANpaWLojuOtfC0cmMh+DvQTfPDrkfXhblQTdFXrGYc0bf/////////8AAAABAAAAANpaWLojuOtfC0cmMh+DvQTfPDrkfXhblQTdFXrGYc0bAAAAAQAAAAAAlRt2go9sp7E1a5ZWvr7vin4UPrFQThpQax1lOFm33AAAAAFURVNUAAAAANpaWLojuOtfC0cmMh+DvQTfPDrkfXhblQTdFXrGYc0bAAAJGE5yoAAAAAABAAAAANpaWLojuOtfC0cmMh+DvQTfPDrkfXhblQTdFXrGYc0bAAAAAQAAAACa/6SeVHolHZWpZ5TST/gWC9n+1JXkMRjiC7SI5MqClQAAAAFURVNUAAAAANpaWLojuOtfC0cmMh+DvQTfPDrkfXhblQTdFXrGYc0bAAAJGE5yoAAAAAAAAAAAAAAAAABKBB+2UBMP/abwcm/M1TXO+/JQWhPwkalgqizKmXyRIQx9cN3sY5YAAAAAAAAAAARW/AX3AAAAQDX5vrTLWyUxzrvpeEghwlfZYjb8PhnV+vjXAQE+iCNotx2S0qDtnNppNy9p0qlsXtKKyZqn036kHMFGQ7RxBQ3GYc0bAAAAQOquRvJeUiQ8uDcNGUnIxXT0xaHe91JZHCVjPEm6j9Biii954p9o7Muer9B9ipn6O4Y+4oiF9NbUxyeqh1VJnQw4WbfcAAAAQG/GEctb+uefyEvdeP8V61fCvvdGCW7KoH7iLXxtvanGk9CyydtRGEIxu66hPdUKKbXpXPEKWvnAAp5V+XQqjQnkyoKVAAAAQNTyKwB94kyBjjczpFwMFVtbhHtugo+DYeQKN13jQUjWQDSgistLE+TDrxlxW0qiIhl/GkOdVLMtG6YhfZeVOQU=\",\n"
          + "          \"result_xdr\": \"AAAAAAAAA+gAAAAAAAAACgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGAAAAAAAAAAAAAAAGAAAAAAAAAAAAAAABAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAA=\",\n"
          + "          \"result_meta_xdr\": \"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\",\n"
          + "          \"fee_meta_xdr\": \"AAAAAgAAAAMAAAABAAAAAAAAAABi/B0L0JGythwN1lY0aypo19NHxvLCyO5tBEcCVvwF9w3gtrOnZAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAEAAADBAAAAAAAAAABi/B0L0JGythwN1lY0aypo19NHxvLCyO5tBEcCVvwF9w3gtrOnY/wYAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAA==\",\n"
          + "          \"memo_type\": \"none\",\n"
          + "          \"signatures\": [\n"
          + "            \"Nfm+tMtbJTHOu+l4SCHCV9liNvw+GdX6+NcBAT6II2i3HZLSoO2c2mk3L2nSqWxe0orJmqfTfqQcwUZDtHEFDQ==\",\n"
          + "            \"6q5G8l5SJDy4Nw0ZScjFdPTFod73UlkcJWM8SbqP0GKKL3nin2jsy56v0H2Kmfo7hj7iiIX01tTHJ6qHVUmdDA==\",\n"
          + "            \"b8YRy1v655/IS914/xXrV8K+90YJbsqgfuItfG29qcaT0LLJ21EYQjG7rqE91Qoptelc8Qpa+cACnlX5dCqNCQ==\",\n"
          + "            \"1PIrAH3iTIGONzOkXAwVW1uEe26Cj4Nh5Ao3XeNBSNZANKCKy0sT5MOvGXFbSqIiGX8aQ51Usy0bpiF9l5U5BQ==\"\n"
          + "          ]\n"
          + "        },\n"
          + "        \"starting_balance\": \"10000000000.0000000\",\n"
          + "        \"funder\": \"GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H\",\n"
          + "        \"account\": \"GAIH3ULLFQ4DGSECF2AR555KZ4KNDGEKN4AFI4SU2M7B43MGK3QJZNSR\"\n"
          + "      }\n"
          + "    ]\n"
          + "  }\n"
          + "}";
}
