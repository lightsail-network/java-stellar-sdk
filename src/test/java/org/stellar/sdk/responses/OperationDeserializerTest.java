package org.stellar.sdk.responses;

import junit.framework.TestCase;

import org.stellar.sdk.*;

import org.junit.Test;
import org.stellar.sdk.responses.operations.*;

public class OperationDeserializerTest extends TestCase {
  @Test
  public void testDeserializeCreateAccountOperation() {
    String json = "{\n" +
            "  \"_links\": {\n" +
            "    \"effects\": {\n" +
            "      \"href\": \"/operations/3936840037961729/effects{?cursor,limit,order}\",\n" +
            "      \"templated\": true\n" +
            "    },\n" +
            "    \"precedes\": {\n" +
            "      \"href\": \"/operations?cursor=3936840037961729\\u0026order=asc\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"/operations/3936840037961729\"\n" +
            "    },\n" +
            "    \"succeeds\": {\n" +
            "      \"href\": \"/operations?cursor=3936840037961729\\u0026order=desc\"\n" +
            "    },\n" +
            "    \"transaction\": {\n" +
            "      \"href\": \"/transactions/75608563ae63757ffc0650d84d1d13c0f3cd4970a294a2a6b43e3f454e0f9e6d\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"account\": \"GAR4DDXYNSN2CORG3XQFLAPWYKTUMLZYHYWV4Y2YJJ4JO6ZJFXMJD7PT\",\n" +
            "  \"funder\": \"GD6WU64OEP5C4LRBH6NK3MHYIA2ADN6K6II6EXPNVUR3ERBXT4AN4ACD\",\n" +
            "  \"id\": 3936840037961729,\n" +
            "  \"paging_token\": \"3936840037961729\",\n" +
            "  \"source_account\": \"GD6WU64OEP5C4LRBH6NK3MHYIA2ADN6K6II6EXPNVUR3ERBXT4AN4ACD\",\n" +
            "  \"starting_balance\": \"299454.904954\",\n" +
            "  \"type\": \"create_account\",\n" +
            "  \"type_i\": 0\n" +
            "}";
    CreateAccountOperationResponse operation = (CreateAccountOperationResponse) GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getSourceAccount(), "GD6WU64OEP5C4LRBH6NK3MHYIA2ADN6K6II6EXPNVUR3ERBXT4AN4ACD");
    assertEquals(operation.getPagingToken(), "3936840037961729");
    assertEquals(operation.getId(), new Long(3936840037961729L));
    assertNull(operation.isTransactionSuccessful());

    assertEquals(operation.getAccount(), "GAR4DDXYNSN2CORG3XQFLAPWYKTUMLZYHYWV4Y2YJJ4JO6ZJFXMJD7PT");
    assertEquals(operation.getStartingBalance(), "299454.904954");
    assertEquals(operation.getFunder(), "GD6WU64OEP5C4LRBH6NK3MHYIA2ADN6K6II6EXPNVUR3ERBXT4AN4ACD");

    assertEquals(operation.getLinks().getEffects().getHref(), "/operations/3936840037961729/effects{?cursor,limit,order}");
    assertEquals(operation.getLinks().getPrecedes().getHref(), "/operations?cursor=3936840037961729&order=asc");
    assertEquals(operation.getLinks().getSelf().getHref(), "/operations/3936840037961729");
    assertEquals(operation.getLinks().getSucceeds().getHref(), "/operations?cursor=3936840037961729&order=desc");
    assertEquals(operation.getLinks().getTransaction().getHref(), "/transactions/75608563ae63757ffc0650d84d1d13c0f3cd4970a294a2a6b43e3f454e0f9e6d");
  }

  @Test
  public void testDeserializePaymentOperation() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"effects\": {\n" +
            "            \"href\": \"/operations/3940808587743233/effects{?cursor,limit,order}\",\n" +
            "            \"templated\": true\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"/operations?cursor=3940808587743233\\u0026order=asc\"\n" +
            "          },\n" +
            "          \"self\": {\n" +
            "            \"href\": \"/operations/3940808587743233\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"/operations?cursor=3940808587743233\\u0026order=desc\"\n" +
            "          },\n" +
            "          \"transaction\": {\n" +
            "            \"href\": \"/transactions/9a140b8942da440c27b65658061c2d7fafe4d0de8424fa832568f3282793fa33\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"amount\": \"100.0\",\n" +
            "        \"asset_type\": \"native\",\n" +
            "        \"transaction_successful\": false,\n" +
            "        \"from\": \"GB6NVEN5HSUBKMYCE5ZOWSK5K23TBWRUQLZY3KNMXUZ3AQ2ESC4MY4AQ\",\n" +
            "        \"id\": 3940808587743233,\n" +
            "        \"paging_token\": \"3940808587743233\",\n" +
            "        \"source_account\": \"GB6NVEN5HSUBKMYCE5ZOWSK5K23TBWRUQLZY3KNMXUZ3AQ2ESC4MY4AQ\",\n" +
            "        \"to\": \"GDWNY2POLGK65VVKIH5KQSH7VWLKRTQ5M6ADLJAYC2UEHEBEARCZJWWI\",\n" +
            "        \"type\": \"payment\",\n" +
            "        \"type_i\": 1\n" +
            "      }";
    PaymentOperationResponse operation = (PaymentOperationResponse) GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getSourceAccount(), "GB6NVEN5HSUBKMYCE5ZOWSK5K23TBWRUQLZY3KNMXUZ3AQ2ESC4MY4AQ");
    assertEquals(operation.getId(), new Long(3940808587743233L));
    assertEquals(operation.isTransactionSuccessful(), new Boolean(false));

    assertEquals(operation.getFrom(), "GB6NVEN5HSUBKMYCE5ZOWSK5K23TBWRUQLZY3KNMXUZ3AQ2ESC4MY4AQ");
    assertEquals(operation.getTo(), "GDWNY2POLGK65VVKIH5KQSH7VWLKRTQ5M6ADLJAYC2UEHEBEARCZJWWI");
    assertEquals(operation.getAmount(), "100.0");
    assertEquals(operation.getAsset(), new AssetTypeNative());
  }

  @Test
  public void testDeserializeNonNativePaymentOperation() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"//horizon-testnet.stellar.org/operations/3603043769651201\"\n" +
            "          },\n" +
            "          \"transaction\": {\n" +
            "            \"href\": \"//horizon-testnet.stellar.org/transactions/b59dee0f85bc7efdefa1a6eec7a0b6f602d567cca6e7f587056d41d42ca48879\"\n" +
            "          },\n" +
            "          \"effects\": {\n" +
            "            \"href\": \"//horizon-testnet.stellar.org/operations/3603043769651201/effects\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"//horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3603043769651201\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"//horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3603043769651201\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"3603043769651201\",\n" +
            "        \"paging_token\": \"3603043769651201\",\n" +
            "        \"transaction_successful\": true,\n" +
            "        \"source_account\": \"GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA\",\n" +
            "        \"type\": \"payment\",\n" +
            "        \"type_i\": 1,\n" +
            "        \"asset_type\": \"credit_alphanum4\",\n" +
            "        \"asset_code\": \"EUR\",\n" +
            "        \"asset_issuer\": \"GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA\",\n" +
            "        \"from\": \"GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA\",\n" +
            "        \"to\": \"GBHUSIZZ7FS2OMLZVZ4HLWJMXQ336NFSXHYERD7GG54NRITDTEWWBBI6\",\n" +
            "        \"amount\": \"1000000000.0\"\n" +
            "      }";

    PaymentOperationResponse operation = (PaymentOperationResponse) GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.isTransactionSuccessful(), new Boolean(true));

    assertEquals(operation.getFrom(), "GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA");
    assertEquals(operation.getTo(), "GBHUSIZZ7FS2OMLZVZ4HLWJMXQ336NFSXHYERD7GG54NRITDTEWWBBI6");
    assertEquals(operation.getAmount(), "1000000000.0");
    assertEquals(operation.getAsset(), Asset.createNonNativeAsset("EUR", "GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA"));
  }

  @Test
  public void testDeserializeAllowTrustOperation() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"//horizon-testnet.stellar.org/operations/3602979345141761\"\n" +
            "          },\n" +
            "          \"transaction\": {\n" +
            "            \"href\": \"//horizon-testnet.stellar.org/transactions/1f265c075e8559ee4c21a32ae53337658e52d7504841adad4144c37143ea01a2\"\n" +
            "          },\n" +
            "          \"effects\": {\n" +
            "            \"href\": \"//horizon-testnet.stellar.org/operations/3602979345141761/effects\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"//horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3602979345141761\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"//horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3602979345141761\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"3602979345141761\",\n" +
            "        \"paging_token\": \"3602979345141761\",\n" +
            "        \"source_account\": \"GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM\",\n" +
            "        \"type\": \"allow_trust\",\n" +
            "        \"type_i\": 7,\n" +
            "        \"asset_type\": \"credit_alphanum4\",\n" +
            "        \"asset_code\": \"EUR\",\n" +
            "        \"asset_issuer\": \"GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM\",\n" +
            "        \"trustee\": \"GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM\",\n" +
            "        \"trustor\": \"GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F\",\n" +
            "        \"authorize\": true\n" +
            "      }";

    AllowTrustOperationResponse operation = (AllowTrustOperationResponse) GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getTrustee(), "GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM");
    assertEquals(operation.getTrustor(), "GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F");
    assertEquals(operation.isAuthorize(), true);
    assertEquals(operation.getAsset(), Asset.createNonNativeAsset("EUR", "GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM"));
  }

  @Test
  public void testDeserializeChangeTrustOperation() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"//horizon-testnet.stellar.org/operations/3602970755207169\"\n" +
            "          },\n" +
            "          \"transaction\": {\n" +
            "            \"href\": \"//horizon-testnet.stellar.org/transactions/8d409a788543895843d269c3f97a2d6a2ebca6e9f8f9a7ae593457b5c0ba6644\"\n" +
            "          },\n" +
            "          \"effects\": {\n" +
            "            \"href\": \"//horizon-testnet.stellar.org/operations/3602970755207169/effects\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"//horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3602970755207169\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"//horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3602970755207169\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"3602970755207169\",\n" +
            "        \"paging_token\": \"3602970755207169\",\n" +
            "        \"source_account\": \"GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F\",\n" +
            "        \"type\": \"change_trust\",\n" +
            "        \"type_i\": 6,\n" +
            "        \"asset_type\": \"credit_alphanum4\",\n" +
            "        \"asset_code\": \"EUR\",\n" +
            "        \"asset_issuer\": \"GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM\",\n" +
            "        \"limit\": \"922337203685.4775807\",\n" +
            "        \"trustee\": \"GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM\",\n" +
            "        \"trustor\": \"GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F\"\n" +
            "      }";

    ChangeTrustOperationResponse operation = (ChangeTrustOperationResponse) GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getTrustee(), "GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM");
    assertEquals(operation.getTrustor(), "GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F");
    assertEquals(operation.getLimit(), "922337203685.4775807");
    assertEquals(operation.getAsset(), Asset.createNonNativeAsset("EUR", "GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM"));
  }

  @Test
  public void testDeserializeSetOptionsOperation() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/1253868457431041\"\n" +
            "          },\n" +
            "          \"transaction\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/transactions/cc392abf8a4e649f16eeba4f40c43a8d50001b14a98ccfc639783125950e99d8\"\n" +
            "          },\n" +
            "          \"effects\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/1253868457431041/effects\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=1253868457431041\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=1253868457431041\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"1253868457431041\",\n" +
            "        \"paging_token\": \"1253868457431041\",\n" +
            "        \"source_account\": \"GB6FDI5Q46BJVPNVWJU527NH463N3EF3S6TXRA2YSCZHXANY3YLXB7MC\",\n" +
            "        \"type\": \"set_options\",\n" +
            "        \"type_i\": 5,\n" +
            "        \"signer_key\": \"GD3ZYXVC7C3ECD5I4E5NGPBFJJSULJ6HJI2FBHGKYFV34DSIWB4YEKJZ\",\n" +
            "        \"signer_weight\": 1,\n"+
            "        \"home_domain\": \"stellar.org\","+
            "        \"inflation_dest\": \"GBYWSY4NPLLPTP22QYANGTT7PEHND64P4D4B6LFEUHGUZRVYJK2H4TBE\","+
            "        \"low_threshold\": 1,\n" +
            "        \"med_threshold\": 2,\n" +
            "        \"high_threshold\": 3,\n" +
            "        \"master_key_weight\": 4,\n" +
            "        \"set_flags_s\": [\n" +
            "          \"auth_required_flag\"\n" +
            "        ],"+
            "        \"clear_flags_s\": [\n" +
            "          \"auth_revocable_flag\"\n" +
            "        ]"+
            "      }";

    SetOptionsOperationResponse operation = (SetOptionsOperationResponse) GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getSigner().getAccountId(), "GD3ZYXVC7C3ECD5I4E5NGPBFJJSULJ6HJI2FBHGKYFV34DSIWB4YEKJZ");
    assertEquals(operation.getSignerKey(), "GD3ZYXVC7C3ECD5I4E5NGPBFJJSULJ6HJI2FBHGKYFV34DSIWB4YEKJZ");
    assertEquals(operation.getSignerWeight(), new Integer(1));
    assertEquals(operation.getHomeDomain(), "stellar.org");
    assertEquals(operation.getInflationDestination(), "GBYWSY4NPLLPTP22QYANGTT7PEHND64P4D4B6LFEUHGUZRVYJK2H4TBE");
    assertEquals(operation.getLowThreshold(), new Integer(1));
    assertEquals(operation.getMedThreshold(), new Integer(2));
    assertEquals(operation.getHighThreshold(), new Integer(3));
    assertEquals(operation.getMasterKeyWeight(), new Integer(4));
    assertEquals(operation.getSetFlags()[0], "auth_required_flag");
    assertEquals(operation.getClearFlags()[0], "auth_revocable_flag");
  }

  @Test
  public void testDeserializeSetOptionsOperationWithNonEd25519Key() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"https://horizon-testnet.stellar.org/operations/44921793093312513\"\n" +
            "          },\n" +
            "          \"transaction\": {\n" +
            "            \"href\": \"https://horizon-testnet.stellar.org/transactions/d991075183f7740e1aa43700b824f2f404082632f1db9d8a54db00574f83393b\"\n" +
            "          },\n" +
            "          \"effects\": {\n" +
            "            \"href\": \"https://horizon-testnet.stellar.org/operations/44921793093312513/effects\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=44921793093312513\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=44921793093312513\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"44921793093312513\",\n" +
            "        \"paging_token\": \"44921793093312513\",\n" +
            "        \"source_account\": \"GCWYUHCMWC2AATGAXXYZX7T45QZLTRCYNJDD3PC73NEMUXBOCO5F6T6Z\",\n" +
            "        \"type\": \"set_options\",\n" +
            "        \"type_i\": 5,\n" +
            "        \"created_at\": \"2018-08-09T15:36:24Z\",\n" +
            "        \"transaction_hash\": \"d991075183f7740e1aa43700b824f2f404082632f1db9d8a54db00574f83393b\",\n" +
            "        \"signer_key\": \"TBGFYVCU76LJ7GZOCGR4X7DG2NV42JPG5CKRL42LA5FZOFI3U2WU7ZAL\",\n" +
            "        \"signer_weight\": 1\n" +
            "      }";

    SetOptionsOperationResponse operation = (SetOptionsOperationResponse) GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    try {
      operation.getSigner();
      fail();
    } catch (FormatException e) {
      assertEquals("Version byte is invalid", e.getMessage());
    }
    assertEquals(operation.getSignerKey(), "TBGFYVCU76LJ7GZOCGR4X7DG2NV42JPG5CKRL42LA5FZOFI3U2WU7ZAL");
  }

  @Test
  public void testDeserializeAccountMergeOperation() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/3424497684189185\"\n" +
            "          },\n" +
            "          \"transaction\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/transactions/68f4d37780e2a85f5698b73977126a595dff99aed852f14a7eb39221ce1f96d5\"\n" +
            "          },\n" +
            "          \"effects\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/3424497684189185/effects\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3424497684189185\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3424497684189185\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"3424497684189185\",\n" +
            "        \"paging_token\": \"3424497684189185\",\n" +
            "        \"source_account\": \"GD6GKRABNDVYDETEZJQEPS7IBQMERCN44R5RCI4LJNX6BMYQM2KPGGZ2\",\n" +
            "        \"type\": \"account_merge\",\n" +
            "        \"type_i\": 8,\n" +
            "        \"account\": \"GD6GKRABNDVYDETEZJQEPS7IBQMERCN44R5RCI4LJNX6BMYQM2KPGGZ2\",\n" +
            "        \"into\": \"GAZWSWPDQTBHFIPBY4FEDFW2J6E2LE7SZHJWGDZO6Q63W7DBSRICO2KN\"\n" +
            "      }";

    AccountMergeOperationResponse operation = (AccountMergeOperationResponse) GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getAccount(), "GD6GKRABNDVYDETEZJQEPS7IBQMERCN44R5RCI4LJNX6BMYQM2KPGGZ2");
    assertEquals(operation.getInto(), "GAZWSWPDQTBHFIPBY4FEDFW2J6E2LE7SZHJWGDZO6Q63W7DBSRICO2KN");
  }

  @Test
  public void testDeserializeManageBuyOfferOperation() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/3320426331639809\"\n" +
            "          },\n" +
            "          \"transaction\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/transactions/1f8fc03b26110e917d124381645d7dcf85927f17e46d8390d254a0bd99cfb0ad\"\n" +
            "          },\n" +
            "          \"effects\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/3320426331639809/effects\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3320426331639809\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3320426331639809\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"3320426331639809\",\n" +
            "        \"paging_token\": \"3320426331639809\",\n" +
            "        \"source_account\": \"GCR6QXX7IRIJVIM5WA5ASQ6MWDOEJNBW3V6RTC5NJXEMOLVTUVKZ725X\",\n" +
            "        \"type\": \"manage_buy_offer\",\n" +
            "        \"type_i\": 12,\n" +
            "        \"offer_id\": 0,\n" +
            "        \"amount\": \"100.0\",\n" +
            "        \"buying_asset_type\": \"credit_alphanum4\",\n" +
            "        \"buying_asset_code\": \"CNY\",\n" +
            "        \"buying_asset_issuer\": \"GAZWSWPDQTBHFIPBY4FEDFW2J6E2LE7SZHJWGDZO6Q63W7DBSRICO2KN\",\n" +
            "        \"selling_asset_type\": \"native\"\n" +
            "      }";

    ManageBuyOfferOperationResponse operation = (ManageBuyOfferOperationResponse) GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getOfferId(), new Integer(0));
    assertEquals(operation.getAmount(), "100.0");
    assertEquals(operation.getBuyingAsset(), Asset.createNonNativeAsset("CNY", "GAZWSWPDQTBHFIPBY4FEDFW2J6E2LE7SZHJWGDZO6Q63W7DBSRICO2KN"));
    assertEquals(operation.getSellingAsset(), new AssetTypeNative());
  }

  @Test
  public void testDeserializePathPaymentOperation() {
    String json = "{\n" +
            "  \"_links\": {\n" +
            "    \"self\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/operations/75252830662840321\"\n" +
            "    },\n" +
            "    \"transaction\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/transactions/fb2f5655c70a459220ac09eb3d6870422b58dcf5c5ffb5e5b21817b4d248826e\"\n" +
            "    },\n" +
            "    \"effects\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/operations/75252830662840321/effects\"\n" +
            "    },\n" +
            "    \"succeeds\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/effects?order=desc\\u0026cursor=75252830662840321\"\n" +
            "    },\n" +
            "    \"precedes\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/effects?order=asc\\u0026cursor=75252830662840321\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"id\": \"75252830662840321\",\n" +
            "  \"paging_token\": \"75252830662840321\",\n" +
            "  \"source_account\": \"GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD\",\n" +
            "  \"type\": \"path_payment\",\n" +
            "  \"type_i\": 2,\n" +
            "  \"created_at\": \"2018-04-24T12:58:12Z\",\n" +
            "  \"transaction_hash\": \"fb2f5655c70a459220ac09eb3d6870422b58dcf5c5ffb5e5b21817b4d248826e\",\n" +
            "  \"asset_type\": \"native\",\n" +
            "  \"from\": \"GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD\",\n" +
            "  \"to\": \"GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD\",\n" +
            "  \"amount\": \"2.5000000\",\n" +
            "  \"path\": [],\n" +
            "  \"source_amount\": \"1.1777000\",\n"+
            "  \"source_max\": \"1.1779523\",\n" +
            "  \"source_asset_type\": \"credit_alphanum4\",\n" +
            "  \"source_asset_code\": \"XRP\",\n" +
            "  \"source_asset_issuer\": \"GBVOL67TMUQBGL4TZYNMY3ZQ5WGQYFPFD5VJRWXR72VA33VFNL225PL5\"\n" +
            "}";

    PathPaymentOperationResponse operation = (PathPaymentOperationResponse) GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getFrom(), "GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD");
    assertEquals(operation.getTo(), "GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD");
    assertEquals(operation.getAmount(), "2.5000000");
    assertEquals(operation.getSourceAmount(), "1.1777000");
    assertEquals(operation.getSourceMax(), "1.1779523");
    assertEquals(operation.getAsset(), new AssetTypeNative());
    assertEquals(operation.getSourceAsset(), Asset.createNonNativeAsset("XRP", "GBVOL67TMUQBGL4TZYNMY3ZQ5WGQYFPFD5VJRWXR72VA33VFNL225PL5"));
  }

  @Test
  public void testDeserializePathPaymentOperationSourceAssetNative() {
    String json = "{\n" +
            "  \"_links\": {\n" +
            "    \"self\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/operations/75252830662840321\"\n" +
            "    },\n" +
            "    \"transaction\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/transactions/fb2f5655c70a459220ac09eb3d6870422b58dcf5c5ffb5e5b21817b4d248826e\"\n" +
            "    },\n" +
            "    \"effects\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/operations/75252830662840321/effects\"\n" +
            "    },\n" +
            "    \"succeeds\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/effects?order=desc\\u0026cursor=75252830662840321\"\n" +
            "    },\n" +
            "    \"precedes\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/effects?order=asc\\u0026cursor=75252830662840321\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"id\": \"75252830662840321\",\n" +
            "  \"paging_token\": \"75252830662840321\",\n" +
            "  \"source_account\": \"GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD\",\n" +
            "  \"type\": \"path_payment\",\n" +
            "  \"type_i\": 2,\n" +
            "  \"created_at\": \"2018-04-24T12:58:12Z\",\n" +
            "  \"transaction_hash\": \"fb2f5655c70a459220ac09eb3d6870422b58dcf5c5ffb5e5b21817b4d248826e\",\n" +
            "  \"asset_type\": \"credit_alphanum4\",\n" +
            "  \"asset_code\": \"XRP\",\n" +
            "  \"asset_issuer\": \"GBVOL67TMUQBGL4TZYNMY3ZQ5WGQYFPFD5VJRWXR72VA33VFNL225PL5\",\n" +
            "  \"from\": \"GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD\",\n" +
            "  \"to\": \"GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD\",\n" +
            "  \"amount\": \"2.5000000\",\n" +
            "  \"path\": [],\n" +
            "  \"source_max\": \"1.1779523\",\n" +
            "  \"source_asset_type\": \"native\"\n" +
            "}";

    PathPaymentOperationResponse operation = (PathPaymentOperationResponse) GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getFrom(), "GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD");
    assertEquals(operation.getTo(), "GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD");
    assertEquals(operation.getAmount(), "2.5000000");
    assertEquals(operation.getSourceMax(), "1.1779523");
    assertEquals(operation.getSourceAsset(), new AssetTypeNative());
    assertEquals(operation.getAsset(), Asset.createNonNativeAsset("XRP", "GBVOL67TMUQBGL4TZYNMY3ZQ5WGQYFPFD5VJRWXR72VA33VFNL225PL5"));
  }

  @Test
  public void testDeserializeInflationOperation() {
    String json = "{\n" +
            "  \"_links\": {\n" +
            "    \"effects\": {\n" +
            "      \"href\": \"/operations/12884914177/effects/{?cursor,limit,order}\",\n" +
            "      \"templated\": true\n" +
            "    },\n" +
            "    \"precedes\": {\n" +
            "      \"href\": \"/operations?cursor=12884914177\\u0026order=asc\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"/operations/12884914177\"\n" +
            "    },\n" +
            "    \"succeeds\": {\n" +
            "      \"href\": \"/operations?cursor=12884914177\\u0026order=desc\"\n" +
            "    },\n" +
            "    \"transaction\": {\n" +
            "      \"href\": \"/transactions/12884914176\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"id\": 12884914177,\n" +
            "  \"paging_token\": \"12884914177\",\n" +
            "  \"type_i\": 9,\n" +
            "  \"type\": \"inflation\"\n" +
            "}";

    InflationOperationResponse operation = (InflationOperationResponse) GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getId(), new Long(12884914177L));
  }

  @Test
  public void testDeserializeManageDataOperation() {
    String json = "{\n" +
            "  \"_links\": {\n" +
            "    \"self\": {\n" +
            "      \"href\": \"https://horizon-testnet.stellar.org/operations/14336188517191688\"\n" +
            "    },\n" +
            "    \"transaction\": {\n" +
            "      \"href\": \"https://horizon-testnet.stellar.org/transactions/ad99999615f653528e67d8c8783e14044519c3e5233b73633aa88050c5856103\"\n" +
            "    },\n" +
            "    \"effects\": {\n" +
            "      \"href\": \"https://horizon-testnet.stellar.org/operations/14336188517191688/effects\"\n" +
            "    },\n" +
            "    \"succeeds\": {\n" +
            "      \"href\": \"https://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=14336188517191688\"\n" +
            "    },\n" +
            "    \"precedes\": {\n" +
            "      \"href\": \"https://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=14336188517191688\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"id\": \"14336188517191688\",\n" +
            "  \"paging_token\": \"14336188517191688\",\n" +
            "  \"source_account\": \"GAC3U2W4KPZJX7GX5AZPOEWBJVUTJQ4TZNO74YN24ETTFIJVY7EMMANP\",\n" +
            "  \"type\": \"manage_data\",\n" +
            "  \"type_i\": 10,\n" +
            "  \"name\": \"CollateralValue\",\n" +
            "  \"value\": \"MjAwMA==\"\n" +
            "}";

    ManageDataOperationResponse operation = (ManageDataOperationResponse) GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getId(), new Long(14336188517191688L));
    assertEquals(operation.getName(), "CollateralValue");
    assertEquals(operation.getValue(), "MjAwMA==");
  }

  @Test
  public void testDeserializeManageDataOperationValueEmpty() {
    String json = "{\n" +
            "  \"_links\": {\n" +
            "    \"self\": {\n" +
            "      \"href\": \"https://horizon-testnet.stellar.org/operations/14336188517191688\"\n" +
            "    },\n" +
            "    \"transaction\": {\n" +
            "      \"href\": \"https://horizon-testnet.stellar.org/transactions/ad99999615f653528e67d8c8783e14044519c3e5233b73633aa88050c5856103\"\n" +
            "    },\n" +
            "    \"effects\": {\n" +
            "      \"href\": \"https://horizon-testnet.stellar.org/operations/14336188517191688/effects\"\n" +
            "    },\n" +
            "    \"succeeds\": {\n" +
            "      \"href\": \"https://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=14336188517191688\"\n" +
            "    },\n" +
            "    \"precedes\": {\n" +
            "      \"href\": \"https://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=14336188517191688\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"id\": \"14336188517191688\",\n" +
            "  \"paging_token\": \"14336188517191688\",\n" +
            "  \"source_account\": \"GAC3U2W4KPZJX7GX5AZPOEWBJVUTJQ4TZNO74YN24ETTFIJVY7EMMANP\",\n" +
            "  \"type\": \"manage_data\",\n" +
            "  \"type_i\": 10,\n" +
            "  \"name\": \"CollateralValue\",\n" +
            "  \"value\": null\n" +
            "}";

    ManageDataOperationResponse operation = (ManageDataOperationResponse) GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getValue(), null);
  }

  @Test
  public void testDeserializeBumpSequenceOperation() {
    String json = "{\n" +
            "  \"_links\": {\n" +
            "    \"effects\": {\n" +
            "      \"href\": \"/operations/12884914177/effects/{?cursor,limit,order}\",\n" +
            "      \"templated\": true\n" +
            "    },\n" +
            "    \"precedes\": {\n" +
            "      \"href\": \"/operations?cursor=12884914177\\u0026order=asc\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"/operations/12884914177\"\n" +
            "    },\n" +
            "    \"succeeds\": {\n" +
            "      \"href\": \"/operations?cursor=12884914177\\u0026order=desc\"\n" +
            "    },\n" +
            "    \"transaction\": {\n" +
            "      \"href\": \"/transactions/12884914176\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"id\": 12884914177,\n" +
            "  \"paging_token\": \"12884914177\",\n" +
            "  \"type_i\": 11,\n" +
            "  \"type\": \"bump_sequence\",\n" +
            "  \"bump_to\": \"79473726952833048\"\n" +
            "}";

    BumpSequenceOperationResponse operation = (BumpSequenceOperationResponse) GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getId(), new Long(12884914177L));
    assertEquals(operation.getBumpTo(), new Long(79473726952833048L));
  }
}
