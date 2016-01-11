package org.stellar.sdk;

import junit.framework.TestCase;

import org.junit.Test;
import org.stellar.base.Asset;
import org.stellar.base.AssetTypeNative;
import org.stellar.base.KeyPair;
import org.stellar.sdk.operations.*;

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
    CreateAccountOperation operation = (CreateAccountOperation) GsonSingleton.getInstance().fromJson(json, Operation.class);

    assertEquals(operation.getSourceAccount().getAddress(), "GD6WU64OEP5C4LRBH6NK3MHYIA2ADN6K6II6EXPNVUR3ERBXT4AN4ACD");
    assertEquals(operation.getPagingToken(), "3936840037961729");
    assertEquals(operation.getId(), new Long(3936840037961729L));

    assertEquals(operation.getAccount().getAddress(), "GAR4DDXYNSN2CORG3XQFLAPWYKTUMLZYHYWV4Y2YJJ4JO6ZJFXMJD7PT");
    assertEquals(operation.getStartingBalance(), "299454.904954");
    assertEquals(operation.getFunder().getAddress(), "GD6WU64OEP5C4LRBH6NK3MHYIA2ADN6K6II6EXPNVUR3ERBXT4AN4ACD");

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
            "        \"from\": \"GB6NVEN5HSUBKMYCE5ZOWSK5K23TBWRUQLZY3KNMXUZ3AQ2ESC4MY4AQ\",\n" +
            "        \"id\": 3940808587743233,\n" +
            "        \"paging_token\": \"3940808587743233\",\n" +
            "        \"source_account\": \"GB6NVEN5HSUBKMYCE5ZOWSK5K23TBWRUQLZY3KNMXUZ3AQ2ESC4MY4AQ\",\n" +
            "        \"to\": \"GDWNY2POLGK65VVKIH5KQSH7VWLKRTQ5M6ADLJAYC2UEHEBEARCZJWWI\",\n" +
            "        \"type\": \"payment\",\n" +
            "        \"type_i\": 1\n" +
            "      }";
    PaymentOperation operation = (PaymentOperation) GsonSingleton.getInstance().fromJson(json, Operation.class);

    assertEquals(operation.getSourceAccount().getAddress(), "GB6NVEN5HSUBKMYCE5ZOWSK5K23TBWRUQLZY3KNMXUZ3AQ2ESC4MY4AQ");
    assertEquals(operation.getId(), new Long(3940808587743233L));

    assertEquals(operation.getFrom().getAddress(), "GB6NVEN5HSUBKMYCE5ZOWSK5K23TBWRUQLZY3KNMXUZ3AQ2ESC4MY4AQ");
    assertEquals(operation.getTo().getAddress(), "GDWNY2POLGK65VVKIH5KQSH7VWLKRTQ5M6ADLJAYC2UEHEBEARCZJWWI");
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

    PaymentOperation operation = (PaymentOperation) GsonSingleton.getInstance().fromJson(json, Operation.class);

    assertEquals(operation.getFrom().getAddress(), "GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA");
    assertEquals(operation.getTo().getAddress(), "GBHUSIZZ7FS2OMLZVZ4HLWJMXQ336NFSXHYERD7GG54NRITDTEWWBBI6");
    assertEquals(operation.getAmount(), "1000000000.0");
    assertEquals(operation.getAsset(), Asset.createNonNativeAsset("EUR", KeyPair.fromAddress("GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA")));
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

    AllowTrustOperation operation = (AllowTrustOperation) GsonSingleton.getInstance().fromJson(json, Operation.class);

    assertEquals(operation.getTrustee().getAddress(), "GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM");
    assertEquals(operation.getTrustor().getAddress(), "GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F");
    assertEquals(operation.isAuthorize(), true);
    assertEquals(operation.getAsset(), Asset.createNonNativeAsset("EUR", KeyPair.fromAddress("GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM")));
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

    ChangeTrustOperation operation = (ChangeTrustOperation) GsonSingleton.getInstance().fromJson(json, Operation.class);

    assertEquals(operation.getTrustee().getAddress(), "GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM");
    assertEquals(operation.getTrustor().getAddress(), "GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F");
    assertEquals(operation.getLimit(), "922337203685.4775807");
    assertEquals(operation.getAsset(), Asset.createNonNativeAsset("EUR", KeyPair.fromAddress("GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM")));
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

    SetOptionsOperation operation = (SetOptionsOperation) GsonSingleton.getInstance().fromJson(json, Operation.class);

    assertEquals(operation.getSigner().getAddress(), "GD3ZYXVC7C3ECD5I4E5NGPBFJJSULJ6HJI2FBHGKYFV34DSIWB4YEKJZ");
    assertEquals(operation.getSignerWeight(), new Integer(1));
    assertEquals(operation.getHomeDomain(), "stellar.org");
    assertEquals(operation.getInflationDestination().getAddress(), "GBYWSY4NPLLPTP22QYANGTT7PEHND64P4D4B6LFEUHGUZRVYJK2H4TBE");
    assertEquals(operation.getLowThreshold(), new Integer(1));
    assertEquals(operation.getMedThreshold(), new Integer(2));
    assertEquals(operation.getHighThreshold(), new Integer(3));
    assertEquals(operation.getMasterKeyWeight(), new Integer(4));
    assertEquals(operation.getSetFlags()[0], "auth_required_flag");
    assertEquals(operation.getClearFlags()[0], "auth_revocable_flag");
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

    AccountMergeOperation operation = (AccountMergeOperation) GsonSingleton.getInstance().fromJson(json, Operation.class);

    assertEquals(operation.getAccount().getAddress(), "GD6GKRABNDVYDETEZJQEPS7IBQMERCN44R5RCI4LJNX6BMYQM2KPGGZ2");
    assertEquals(operation.getInto().getAddress(), "GAZWSWPDQTBHFIPBY4FEDFW2J6E2LE7SZHJWGDZO6Q63W7DBSRICO2KN");
  }

  @Test
  public void testDeserializeManageOfferOperation() {
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
            "        \"type\": \"manage_offer\",\n" +
            "        \"type_i\": 3,\n" +
            "        \"offer_id\": 0,\n" +
            "        \"amount\": \"100.0\",\n" +
            "        \"buying_asset_type\": \"credit_alphanum4\",\n" +
            "        \"buying_asset_code\": \"CNY\",\n" +
            "        \"buying_asset_issuer\": \"GAZWSWPDQTBHFIPBY4FEDFW2J6E2LE7SZHJWGDZO6Q63W7DBSRICO2KN\",\n" +
            "        \"selling_asset_type\": \"native\"\n" +
            "      }";

    ManageOfferOperation operation = (ManageOfferOperation) GsonSingleton.getInstance().fromJson(json, Operation.class);

    assertEquals(operation.getOfferId(), new Integer(0));
    assertEquals(operation.getAmount(), "100.0");
    assertEquals(operation.getBuyingAsset(), Asset.createNonNativeAsset("CNY", KeyPair.fromAddress("GAZWSWPDQTBHFIPBY4FEDFW2J6E2LE7SZHJWGDZO6Q63W7DBSRICO2KN")));
    assertEquals(operation.getSellingAsset(), new AssetTypeNative());
  }

  @Test
  public void testDeserializePathPaymentOperation() {
    String json = "{\n" +
            "  \"_links\": {\n" +
            "    \"effects\": {\n" +
            "      \"href\": \"/operations/25769807873/effects/{?cursor,limit,order}\",\n" +
            "      \"templated\": true\n" +
            "    },\n" +
            "    \"precedes\": {\n" +
            "      \"href\": \"/operations?cursor=25769807873\\u0026order=asc\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"/operations/25769807873\"\n" +
            "    },\n" +
            "    \"succeeds\": {\n" +
            "      \"href\": \"/operations?cursor=25769807873\\u0026order=desc\"\n" +
            "    },\n" +
            "    \"transaction\": {\n" +
            "      \"href\": \"/transactions/25769807872\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"amount\": \"10.0\",\n" +
            "  \"asset_code\": \"EUR\",\n" +
            "  \"asset_issuer\": \"GCQPYGH4K57XBDENKKX55KDTWOTK5WDWRQOH2LHEDX3EKVIQRLMESGBG\",\n" +
            "  \"asset_type\": \"credit_alphanum4\",\n" +
            "  \"from\": \"GCXKG6RN4ONIEPCMNFB732A436Z5PNDSRLGWK7GBLCMQLIFO4S7EYWVU\",\n" +
            "  \"id\": 25769807873,\n" +
            "  \"paging_token\": \"25769807873\",\n" +
            "  \"send_asset_code\": \"USD\",\n" +
            "  \"send_asset_issuer\": \"GC23QF2HUE52AMXUFUH3AYJAXXGXXV2VHXYYR6EYXETPKDXZSAW67XO4\",\n" +
            "  \"send_asset_type\": \"credit_alphanum4\",\n" +
            "  \"source_amount\": \"100.0\",\n" +
            "  \"to\": \"GA5WBPYA5Y4WAEHXWR2UKO2UO4BUGHUQ74EUPKON2QHV4WRHOIRNKKH2\",\n" +
            "  \"type_i\": 2,\n" +
            "  \"type\": \"path_payment\"\n" +
            "}";

    PathPaymentOperation operation = (PathPaymentOperation) GsonSingleton.getInstance().fromJson(json, Operation.class);

    assertEquals(operation.getFrom().getAddress(), "GCXKG6RN4ONIEPCMNFB732A436Z5PNDSRLGWK7GBLCMQLIFO4S7EYWVU");
    assertEquals(operation.getTo().getAddress(), "GA5WBPYA5Y4WAEHXWR2UKO2UO4BUGHUQ74EUPKON2QHV4WRHOIRNKKH2");
    assertEquals(operation.getAmount(), "10.0");
    assertEquals(operation.getSourceAmount(), "100.0");
    assertEquals(operation.getAsset(), Asset.createNonNativeAsset("EUR", KeyPair.fromAddress("GCQPYGH4K57XBDENKKX55KDTWOTK5WDWRQOH2LHEDX3EKVIQRLMESGBG")));
    assertEquals(operation.getSendAsset(), Asset.createNonNativeAsset("USD", KeyPair.fromAddress("GC23QF2HUE52AMXUFUH3AYJAXXGXXV2VHXYYR6EYXETPKDXZSAW67XO4")));
  }

  @Test
  public void testDeserializeCreatePassiveOfferOperation() {
    String json = "{\n" +
            "  \"_links\": {\n" +
            "    \"effects\": {\n" +
            "      \"href\": \"/operations/1127729562914817/effects{?cursor,limit,order}\",\n" +
            "      \"templated\": true\n" +
            "    },\n" +
            "    \"precedes\": {\n" +
            "      \"href\": \"/operations?cursor=1127729562914817\\u0026order=asc\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"/operations/1127729562914817\"\n" +
            "    },\n" +
            "    \"succeeds\": {\n" +
            "      \"href\": \"/operations?cursor=1127729562914817\\u0026order=desc\"\n" +
            "    },\n" +
            "    \"transaction\": {\n" +
            "      \"href\": \"/transactions/1127729562914816\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"amount\": \"11.27827\",\n" +
            "  \"buying_asset_code\": \"USD\",\n" +
            "  \"buying_asset_issuer\": \"GDS5JW5E6DRSSN5XK4LW7E6VUMFKKE2HU5WCOVFTO7P2RP7OXVCBLJ3Y\",\n" +
            "  \"buying_asset_type\": \"credit_alphanum4\",\n" +
            "  \"id\": 1127729562914817,\n" +
            "  \"offer_id\": 9,\n" +
            "  \"paging_token\": \"1127729562914817\",\n" +
            "  \"price\": \"1.0\",\n" +
            "  \"price_r\": {\n" +
            "    \"d\": 1,\n" +
            "    \"n\": 1\n" +
            "  },\n" +
            "  \"selling_asset_type\": \"native\",\n" +
            "  \"type_i\": 4,\n" +
            "  \"type\": \"create_passive_offer\"\n" +
            "}";

    CreatePassiveOfferOperation operation = (CreatePassiveOfferOperation) GsonSingleton.getInstance().fromJson(json, Operation.class);

    assertEquals(operation.getAmount(), "11.27827");
    assertEquals(operation.getBuyingAsset(), Asset.createNonNativeAsset("USD", KeyPair.fromAddress("GDS5JW5E6DRSSN5XK4LW7E6VUMFKKE2HU5WCOVFTO7P2RP7OXVCBLJ3Y")));
    assertEquals(operation.getSellingAsset(), new AssetTypeNative());
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

    InflationOperation operation = (InflationOperation) GsonSingleton.getInstance().fromJson(json, Operation.class);

    assertEquals(operation.getId(), new Long(12884914177L));
  }
}
