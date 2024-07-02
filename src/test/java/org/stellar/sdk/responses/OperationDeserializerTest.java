package org.stellar.sdk.responses;

import static java.math.BigInteger.valueOf;
import static org.stellar.sdk.Asset.create;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import junit.framework.TestCase;
import org.junit.Test;
import org.stellar.sdk.AssetAmount;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.Predicate;
import org.stellar.sdk.Price;
import org.stellar.sdk.TrustLineAsset;
import org.stellar.sdk.responses.operations.AccountMergeOperationResponse;
import org.stellar.sdk.responses.operations.AllowTrustOperationResponse;
import org.stellar.sdk.responses.operations.BumpSequenceOperationResponse;
import org.stellar.sdk.responses.operations.ChangeTrustOperationResponse;
import org.stellar.sdk.responses.operations.ClaimClaimableBalanceOperationResponse;
import org.stellar.sdk.responses.operations.ClawbackClaimableBalanceOperationResponse;
import org.stellar.sdk.responses.operations.ClawbackOperationResponse;
import org.stellar.sdk.responses.operations.CreateAccountOperationResponse;
import org.stellar.sdk.responses.operations.CreateClaimableBalanceOperationResponse;
import org.stellar.sdk.responses.operations.EndSponsoringFutureReservesOperationResponse;
import org.stellar.sdk.responses.operations.ExtendFootprintTTLOperationResponse;
import org.stellar.sdk.responses.operations.InflationOperationResponse;
import org.stellar.sdk.responses.operations.InvokeHostFunctionOperationResponse;
import org.stellar.sdk.responses.operations.LiquidityPoolDepositOperationResponse;
import org.stellar.sdk.responses.operations.LiquidityPoolWithdrawOperationResponse;
import org.stellar.sdk.responses.operations.ManageBuyOfferOperationResponse;
import org.stellar.sdk.responses.operations.ManageDataOperationResponse;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.responses.operations.PathPaymentStrictReceiveOperationResponse;
import org.stellar.sdk.responses.operations.PathPaymentStrictSendOperationResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;
import org.stellar.sdk.responses.operations.RestoreFootprintOperationResponse;
import org.stellar.sdk.responses.operations.SetOptionsOperationResponse;
import org.stellar.sdk.responses.operations.SetTrustLineFlagsOperationResponse;

public class OperationDeserializerTest extends TestCase {
  @Test
  public void testDeserializeCreateAccountOperation() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"/operations/3936840037961729/effects{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"precedes\": {\n"
            + "      \"href\": \"/operations?cursor=3936840037961729\\u0026order=asc\"\n"
            + "    },\n"
            + "    \"self\": {\n"
            + "      \"href\": \"/operations/3936840037961729\"\n"
            + "    },\n"
            + "    \"succeeds\": {\n"
            + "      \"href\": \"/operations?cursor=3936840037961729\\u0026order=desc\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"/transactions/75608563ae63757ffc0650d84d1d13c0f3cd4970a294a2a6b43e3f454e0f9e6d\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"account\": \"GAR4DDXYNSN2CORG3XQFLAPWYKTUMLZYHYWV4Y2YJJ4JO6ZJFXMJD7PT\",\n"
            + "  \"funder\": \"GD6WU64OEP5C4LRBH6NK3MHYIA2ADN6K6II6EXPNVUR3ERBXT4AN4ACD\",\n"
            + "  \"id\": 3936840037961729,\n"
            + "  \"paging_token\": \"3936840037961729\",\n"
            + "  \"source_account\": \"GD6WU64OEP5C4LRBH6NK3MHYIA2ADN6K6II6EXPNVUR3ERBXT4AN4ACD\",\n"
            + "  \"starting_balance\": \"299454.904954\",\n"
            + "  \"type\": \"create_account\",\n"
            + "  \"type_i\": 0\n"
            + "}";
    CreateAccountOperationResponse operation =
        (CreateAccountOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(
        operation.getSourceAccount(), "GD6WU64OEP5C4LRBH6NK3MHYIA2ADN6K6II6EXPNVUR3ERBXT4AN4ACD");
    assertEquals(operation.getPagingToken(), "3936840037961729");
    assertEquals(operation.getId().longValue(), 3936840037961729L);
    assertNull(operation.isTransactionSuccessful());
    assertFalse(operation.getFunderMuxed().isPresent());

    assertEquals(
        operation.getAccount(), "GAR4DDXYNSN2CORG3XQFLAPWYKTUMLZYHYWV4Y2YJJ4JO6ZJFXMJD7PT");
    assertEquals(operation.getStartingBalance(), "299454.904954");
    assertEquals(operation.getFunder(), "GD6WU64OEP5C4LRBH6NK3MHYIA2ADN6K6II6EXPNVUR3ERBXT4AN4ACD");

    assertEquals(
        operation.getLinks().getEffects().getHref(),
        "/operations/3936840037961729/effects{?cursor,limit,order}");
    assertEquals(
        operation.getLinks().getPrecedes().getHref(),
        "/operations?cursor=3936840037961729&order=asc");
    assertEquals(operation.getLinks().getSelf().getHref(), "/operations/3936840037961729");
    assertEquals(
        operation.getLinks().getSucceeds().getHref(),
        "/operations?cursor=3936840037961729&order=desc");
    assertEquals(
        operation.getLinks().getTransaction().getHref(),
        "/transactions/75608563ae63757ffc0650d84d1d13c0f3cd4970a294a2a6b43e3f454e0f9e6d");
  }

  @Test
  public void testDeserializeMuxedCreateAccountOperation() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"/operations/3936840037961729/effects{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"precedes\": {\n"
            + "      \"href\": \"/operations?cursor=3936840037961729\\u0026order=asc\"\n"
            + "    },\n"
            + "    \"self\": {\n"
            + "      \"href\": \"/operations/3936840037961729\"\n"
            + "    },\n"
            + "    \"succeeds\": {\n"
            + "      \"href\": \"/operations?cursor=3936840037961729\\u0026order=desc\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"/transactions/75608563ae63757ffc0650d84d1d13c0f3cd4970a294a2a6b43e3f454e0f9e6d\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"account\": \"GAR4DDXYNSN2CORG3XQFLAPWYKTUMLZYHYWV4Y2YJJ4JO6ZJFXMJD7PT\",\n"
            + "  \"funder\": \"GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\",\n"
            + "  \"funder_muxed\": \"MAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472YAAAAAAAAAABUSON4\",\n"
            + "  \"funder_muxed_id\": \"18446744073709551615\",\n"
            + "  \"id\": 3936840037961729,\n"
            + "  \"paging_token\": \"3936840037961729\",\n"
            + "  \"source_account\": \"GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\",\n"
            + "  \"source_account_muxed\": \"MBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA6AAAAAAAAAAAPN7BA\",\n"
            + "  \"source_account_muxed_id\": \"123\",\n"
            + "  \"starting_balance\": \"299454.904954\",\n"
            + "  \"type\": \"create_account\",\n"
            + "  \"type_i\": 0\n"
            + "}";
    CreateAccountOperationResponse operation =
        (CreateAccountOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(
        operation.getAccount(), "GAR4DDXYNSN2CORG3XQFLAPWYKTUMLZYHYWV4Y2YJJ4JO6ZJFXMJD7PT");

    assertEquals(
        operation.getSourceAccountMuxed().get(),
        new MuxedAccount(
            "MBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA6AAAAAAAAAAAPN7BA",
            "GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L",
            valueOf(123l)));

    assertEquals(operation.getFunder(), "GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G");
    assertEquals(
        operation.getFunderMuxed().get().getUnmuxedAddress(),
        "GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G");
    assertEquals(
        operation.getFunderMuxed().get().toString(),
        "MAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472YAAAAAAAAAABUSON4");
    assertEquals(operation.getFunderMuxed().get().getId(), new BigInteger("18446744073709551615"));
  }

  @Test
  public void testDeserializePaymentOperation() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"effects\": {\n"
            + "            \"href\": \"/operations/3940808587743233/effects{?cursor,limit,order}\",\n"
            + "            \"templated\": true\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"/operations?cursor=3940808587743233\\u0026order=asc\"\n"
            + "          },\n"
            + "          \"self\": {\n"
            + "            \"href\": \"/operations/3940808587743233\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"/operations?cursor=3940808587743233\\u0026order=desc\"\n"
            + "          },\n"
            + "          \"transaction\": {\n"
            + "            \"href\": \"/transactions/9a140b8942da440c27b65658061c2d7fafe4d0de8424fa832568f3282793fa33\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"amount\": \"100.0\",\n"
            + "        \"asset_type\": \"native\",\n"
            + "        \"transaction_successful\": false,\n"
            + "        \"from\": \"GB6NVEN5HSUBKMYCE5ZOWSK5K23TBWRUQLZY3KNMXUZ3AQ2ESC4MY4AQ\",\n"
            + "        \"id\": 3940808587743233,\n"
            + "        \"paging_token\": \"3940808587743233\",\n"
            + "        \"source_account\": \"GB6NVEN5HSUBKMYCE5ZOWSK5K23TBWRUQLZY3KNMXUZ3AQ2ESC4MY4AQ\",\n"
            + "        \"to\": \"GDWNY2POLGK65VVKIH5KQSH7VWLKRTQ5M6ADLJAYC2UEHEBEARCZJWWI\",\n"
            + "        \"type\": \"payment\",\n"
            + "        \"type_i\": 1\n"
            + "      }";
    PaymentOperationResponse operation =
        (PaymentOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(
        operation.getSourceAccount(), "GB6NVEN5HSUBKMYCE5ZOWSK5K23TBWRUQLZY3KNMXUZ3AQ2ESC4MY4AQ");
    assertEquals(operation.getId().longValue(), 3940808587743233L);
    assertEquals(operation.isTransactionSuccessful().booleanValue(), false);

    assertEquals(operation.getFrom(), "GB6NVEN5HSUBKMYCE5ZOWSK5K23TBWRUQLZY3KNMXUZ3AQ2ESC4MY4AQ");
    assertEquals(operation.getTo(), "GDWNY2POLGK65VVKIH5KQSH7VWLKRTQ5M6ADLJAYC2UEHEBEARCZJWWI");
    assertFalse(operation.getFromMuxed().isPresent());
    assertFalse(operation.getToMuxed().isPresent());
    assertEquals(operation.getAmount(), "100.0");
    assertEquals(operation.getAsset(), new AssetTypeNative());
  }

  @Test
  public void testDeserializeNonNativePaymentOperation() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"self\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/operations/3603043769651201\"\n"
            + "          },\n"
            + "          \"transaction\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/transactions/b59dee0f85bc7efdefa1a6eec7a0b6f602d567cca6e7f587056d41d42ca48879\"\n"
            + "          },\n"
            + "          \"effects\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/operations/3603043769651201/effects\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3603043769651201\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3603043769651201\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"3603043769651201\",\n"
            + "        \"paging_token\": \"3603043769651201\",\n"
            + "        \"transaction_successful\": true,\n"
            + "        \"source_account\": \"GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA\",\n"
            + "        \"type\": \"payment\",\n"
            + "        \"type_i\": 1,\n"
            + "        \"asset_type\": \"credit_alphanum4\",\n"
            + "        \"asset_code\": \"EUR\",\n"
            + "        \"asset_issuer\": \"GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA\",\n"
            + "        \"from\": \"GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA\",\n"
            + "        \"to\": \"GBHUSIZZ7FS2OMLZVZ4HLWJMXQ336NFSXHYERD7GG54NRITDTEWWBBI6\",\n"
            + "        \"amount\": \"1000000000.0\"\n"
            + "      }";

    PaymentOperationResponse operation =
        (PaymentOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.isTransactionSuccessful().booleanValue(), true);

    assertEquals(operation.getFrom(), "GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA");
    assertEquals(operation.getTo(), "GBHUSIZZ7FS2OMLZVZ4HLWJMXQ336NFSXHYERD7GG54NRITDTEWWBBI6");
    assertFalse(operation.getFromMuxed().isPresent());
    assertFalse(operation.getToMuxed().isPresent());
    assertEquals(operation.getAmount(), "1000000000.0");
    assertEquals(
        operation.getAsset(),
        create(null, "EUR", "GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA"));
  }

  @Test
  public void testDeserializeMuxedPaymentOperation() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"self\": {\n"
            + "      \"href\": \"https://horizon-testnet.stellar.org/operations/9108208295616513\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"https://horizon-testnet.stellar.org/transactions/015097334b0d70476831d819db2a07c003da336cdcd90e810885e062c3e908b2\"\n"
            + "    },\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"https://horizon-testnet.stellar.org/operations/9108208295616513/effects\"\n"
            + "    },\n"
            + "    \"succeeds\": {\n"
            + "      \"href\": \"https://horizon-testnet.stellar.org/effects?order=desc&cursor=9108208295616513\"\n"
            + "    },\n"
            + "    \"precedes\": {\n"
            + "      \"href\": \"https://horizon-testnet.stellar.org/effects?order=asc&cursor=9108208295616513\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"id\": \"9108208295616513\",\n"
            + "  \"paging_token\": \"9108208295616513\",\n"
            + "  \"transaction_successful\": true,\n"
            + "  \"source_account\": \"GDRNOJXOSJNN6XYO5WTFTOSDPJ2YHPWGTK76XIFWW4MITYVHXP2QMB6Y\",\n"
            + "  \"source_account_muxed\": \"MDRNOJXOSJNN6XYO5WTFTOSDPJ2YHPWGTK76XIFWW4MITYVHXP2QMAAAAAAAOW6NCUZC4\",\n"
            + "  \"source_account_muxed_id\": \"123456789\",\n"
            + "  \"type\": \"payment\",\n"
            + "  \"type_i\": 1,\n"
            + "  \"created_at\": \"2023-10-21T09:49:13Z\",\n"
            + "  \"transaction_hash\": \"015097334b0d70476831d819db2a07c003da336cdcd90e810885e062c3e908b2\",\n"
            + "  \"asset_type\": \"native\",\n"
            + "  \"from\": \"GDRNOJXOSJNN6XYO5WTFTOSDPJ2YHPWGTK76XIFWW4MITYVHXP2QMB6Y\",\n"
            + "  \"from_muxed\": \"MDRNOJXOSJNN6XYO5WTFTOSDPJ2YHPWGTK76XIFWW4MITYVHXP2QMAAAAAAAOW6NCUZC4\",\n"
            + "  \"from_muxed_id\": \"123456789\",\n"
            + "  \"to\": \"GDQJVHV2EGSV2P7EETRKKY6VRFGIEP5MICKDWIR3ZWSJSYM7LAEXUIN6\",\n"
            + "  \"to_muxed\": \"MDQJVHV2EGSV2P7EETRKKY6VRFGIEP5MICKDWIR3ZWSJSYM7LAEXUAAAAAADVXTIWGZD4\",\n"
            + "  \"to_muxed_id\": \"987654321\",\n"
            + "  \"amount\": \"350.1234567\"\n"
            + "}\n";
    PaymentOperationResponse operation =
        (PaymentOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getId(), Long.valueOf(9108208295616513L));
    assertEquals(operation.getPagingToken(), "9108208295616513");
    assertTrue(operation.isTransactionSuccessful());
    assertEquals(
        operation.getSourceAccount(), "GDRNOJXOSJNN6XYO5WTFTOSDPJ2YHPWGTK76XIFWW4MITYVHXP2QMB6Y");
    assertTrue(operation.getSourceAccountMuxed().isPresent());
    assertEquals(
        operation.getSourceAccountMuxed().get().getUnmuxedAddress(),
        "GDRNOJXOSJNN6XYO5WTFTOSDPJ2YHPWGTK76XIFWW4MITYVHXP2QMB6Y");
    assertEquals(operation.getSourceAccountMuxed().get().getId(), BigInteger.valueOf(123456789L));

    assertEquals(operation.getType(), "payment");
    assertEquals(operation.getCreatedAt(), "2023-10-21T09:49:13Z");
    assertEquals(
        operation.getTransactionHash(),
        "015097334b0d70476831d819db2a07c003da336cdcd90e810885e062c3e908b2");
    assertEquals(operation.getAsset(), new AssetTypeNative());
    assertEquals(operation.getFrom(), "GDRNOJXOSJNN6XYO5WTFTOSDPJ2YHPWGTK76XIFWW4MITYVHXP2QMB6Y");
    assertTrue(operation.getFromMuxed().isPresent());
    assertEquals(
        operation.getFromMuxed().get().getUnmuxedAddress(),
        "GDRNOJXOSJNN6XYO5WTFTOSDPJ2YHPWGTK76XIFWW4MITYVHXP2QMB6Y");
    assertEquals(operation.getFromMuxed().get().getId(), BigInteger.valueOf(123456789L));
    assertEquals(operation.getTo(), "GDQJVHV2EGSV2P7EETRKKY6VRFGIEP5MICKDWIR3ZWSJSYM7LAEXUIN6");
    assertTrue(operation.getToMuxed().isPresent());
    assertEquals(
        operation.getToMuxed().get().getUnmuxedAddress(),
        "GDQJVHV2EGSV2P7EETRKKY6VRFGIEP5MICKDWIR3ZWSJSYM7LAEXUIN6");
    assertEquals(operation.getToMuxed().get().getId(), BigInteger.valueOf(987654321L));
    assertEquals(operation.getAmount(), "350.1234567");
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testDeserializeAllowTrustOperation() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"self\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/operations/3602979345141761\"\n"
            + "          },\n"
            + "          \"transaction\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/transactions/1f265c075e8559ee4c21a32ae53337658e52d7504841adad4144c37143ea01a2\"\n"
            + "          },\n"
            + "          \"effects\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/operations/3602979345141761/effects\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3602979345141761\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3602979345141761\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"3602979345141761\",\n"
            + "        \"paging_token\": \"3602979345141761\",\n"
            + "        \"source_account\": \"GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM\",\n"
            + "        \"type\": \"allow_trust\",\n"
            + "        \"type_i\": 7,\n"
            + "        \"asset_type\": \"credit_alphanum4\",\n"
            + "        \"asset_code\": \"EUR\",\n"
            + "        \"asset_issuer\": \"GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM\",\n"
            + "        \"trustee\": \"GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM\",\n"
            + "        \"trustor\": \"GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F\",\n"
            + "        \"authorize\": true\n,"
            + "        \"authorize_to_maintain_liabilities\": false\n"
            + "      }";

    AllowTrustOperationResponse operation =
        (AllowTrustOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(
        operation.getTrustee(), "GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM");
    assertEquals(
        operation.getTrustor(), "GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F");
    assertEquals(operation.isAuthorize(), true);
    assertEquals(operation.isAuthorizedToMaintainLiabilities(), false);
    assertEquals(
        operation.getAsset(),
        create(null, "EUR", "GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM"));
    assertFalse(operation.getTrusteeMuxed().isPresent());
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testDeserializeMuxedAllowTrustOperation() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"self\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/operations/3602979345141761\"\n"
            + "          },\n"
            + "          \"transaction\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/transactions/1f265c075e8559ee4c21a32ae53337658e52d7504841adad4144c37143ea01a2\"\n"
            + "          },\n"
            + "          \"effects\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/operations/3602979345141761/effects\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3602979345141761\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3602979345141761\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"3602979345141761\",\n"
            + "        \"paging_token\": \"3602979345141761\",\n"
            + "        \"source_account\": \"GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM\",\n"
            + "        \"type\": \"allow_trust\",\n"
            + "        \"type_i\": 7,\n"
            + "        \"asset_type\": \"credit_alphanum4\",\n"
            + "        \"asset_code\": \"EUR\",\n"
            + "        \"asset_issuer\": \"GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM\",\n"
            + "        \"trustee\": \"GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\",\n"
            + "        \"trustee_muxed\": \"MAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472YAAAAAAAAAABUSON4\",\n"
            + "        \"trustee_muxed_id\": \"420\",\n"
            + "        \"trustor\": \"GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F\",\n"
            + "        \"authorize\": true\n,"
            + "        \"authorize_to_maintain_liabilities\": false\n"
            + "      }";

    AllowTrustOperationResponse operation =
        (AllowTrustOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(
        operation.getTrustor(), "GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F");
    assertEquals(
        operation.getTrustee(), "GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G");
    assertEquals(
        operation.getTrusteeMuxed().get().getUnmuxedAddress(),
        "GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G");
    assertEquals(
        operation.getTrusteeMuxed().get().toString(),
        "MAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472YAAAAAAAAAABUSON4");
    assertEquals(operation.getTrusteeMuxed().get().getId().longValue(), 420l);
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testDeserializeAllowTrustOperationAuthorizeToMaintainLiabilities() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"self\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/operations/3602979345141761\"\n"
            + "          },\n"
            + "          \"transaction\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/transactions/1f265c075e8559ee4c21a32ae53337658e52d7504841adad4144c37143ea01a2\"\n"
            + "          },\n"
            + "          \"effects\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/operations/3602979345141761/effects\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3602979345141761\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3602979345141761\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"3602979345141761\",\n"
            + "        \"paging_token\": \"3602979345141761\",\n"
            + "        \"source_account\": \"GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM\",\n"
            + "        \"type\": \"allow_trust\",\n"
            + "        \"type_i\": 7,\n"
            + "        \"asset_type\": \"credit_alphanum4\",\n"
            + "        \"asset_code\": \"EUR\",\n"
            + "        \"asset_issuer\": \"GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM\",\n"
            + "        \"trustee\": \"GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM\",\n"
            + "        \"trustor\": \"GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F\",\n"
            + "        \"authorize\": false\n,"
            + "        \"authorize_to_maintain_liabilities\": true\n"
            + "      }";

    AllowTrustOperationResponse operation =
        (AllowTrustOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(
        operation.getTrustee(), "GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM");
    assertEquals(
        operation.getTrustor(), "GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F");
    assertEquals(operation.isAuthorize(), false);
    assertEquals(operation.isAuthorizedToMaintainLiabilities(), true);
    assertEquals(
        operation.getAsset(),
        create(null, "EUR", "GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM"));
  }

  @Test
  public void testDeserializeChangeTrustOperation() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"self\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/operations/3602970755207169\"\n"
            + "          },\n"
            + "          \"transaction\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/transactions/8d409a788543895843d269c3f97a2d6a2ebca6e9f8f9a7ae593457b5c0ba6644\"\n"
            + "          },\n"
            + "          \"effects\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/operations/3602970755207169/effects\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3602970755207169\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3602970755207169\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"3602970755207169\",\n"
            + "        \"paging_token\": \"3602970755207169\",\n"
            + "        \"source_account\": \"GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F\",\n"
            + "        \"type\": \"change_trust\",\n"
            + "        \"type_i\": 6,\n"
            + "        \"asset_type\": \"credit_alphanum4\",\n"
            + "        \"asset_code\": \"EUR\",\n"
            + "        \"asset_issuer\": \"GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM\",\n"
            + "        \"limit\": \"922337203685.4775807\",\n"
            + "        \"trustee\": \"GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM\",\n"
            + "        \"trustor\": \"GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F\"\n"
            + "      }";

    ChangeTrustOperationResponse operation =
        (ChangeTrustOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(
        operation.getTrustee(), "GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM");
    assertEquals(
        operation.getTrustor(), "GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F");
    assertEquals(operation.getLimit(), "922337203685.4775807");
    assertEquals(
        operation.getTrustLineAsset(),
        new TrustLineAsset(
            create(null, "EUR", "GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM")));
    assertFalse(operation.getTrustorMuxed().isPresent());
  }

  @Test
  public void testDeserializeChangeTrustOperationLiquidityPoolShares() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"self\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/operations/3602970755207169\"\n"
            + "          },\n"
            + "          \"transaction\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/transactions/8d409a788543895843d269c3f97a2d6a2ebca6e9f8f9a7ae593457b5c0ba6644\"\n"
            + "          },\n"
            + "          \"effects\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/operations/3602970755207169/effects\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3602970755207169\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3602970755207169\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"3602970755207169\",\n"
            + "        \"paging_token\": \"3602970755207169\",\n"
            + "        \"source_account\": \"GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F\",\n"
            + "        \"type\": \"change_trust\",\n"
            + "        \"type_i\": 6,\n"
            + "        \"asset_type\": \"liquidity_pool_shares\",\n"
            + "        \"liquidity_pool_id\": \"02449937ed825805b7a945bb6c027b53dfaf140983c1a1a64c42a81edd89b5e0\",\n"
            + "        \"limit\": \"5.0000000\",\n"
            + "        \"trustee\": \"GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM\",\n"
            + "        \"trustor\": \"GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F\"\n"
            + "      }";

    ChangeTrustOperationResponse operation =
        (ChangeTrustOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(
        operation.getTrustee(), "GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM");
    assertEquals(
        operation.getTrustor(), "GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F");
    assertEquals(operation.getLimit(), "5.0000000");
    // TODO: fix
    //    assertEquals(
    //        ((AssetTypePoolShare) operation.getAsset()).getPoolId(),
    //        "02449937ed825805b7a945bb6c027b53dfaf140983c1a1a64c42a81edd89b5e0");
  }

  @Test
  public void testDeserializeMuxedChangeTrustOperation() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"self\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/operations/3602970755207169\"\n"
            + "          },\n"
            + "          \"transaction\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/transactions/8d409a788543895843d269c3f97a2d6a2ebca6e9f8f9a7ae593457b5c0ba6644\"\n"
            + "          },\n"
            + "          \"effects\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/operations/3602970755207169/effects\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3602970755207169\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"//horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3602970755207169\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"3602970755207169\",\n"
            + "        \"paging_token\": \"3602970755207169\",\n"
            + "        \"source_account\": \"GDZ55LVXECRTW4G36EZPTHI4XIYS5JUC33TUS22UOETVFVOQ77JXWY4F\",\n"
            + "        \"type\": \"change_trust\",\n"
            + "        \"type_i\": 6,\n"
            + "        \"asset_type\": \"credit_alphanum4\",\n"
            + "        \"asset_code\": \"EUR\",\n"
            + "        \"asset_issuer\": \"GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM\",\n"
            + "        \"limit\": \"922337203685.4775807\",\n"
            + "        \"trustee\": \"GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM\",\n"
            + "        \"trustor\": \"GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\",\n"
            + "        \"trustor_muxed\": \"MAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472YAAAAAAAAAABUSON4\",\n"
            + "        \"trustor_muxed_id\": \"420\"\n"
            + "      }";

    ChangeTrustOperationResponse operation =
        (ChangeTrustOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(
        operation.getTrustee(), "GDIROJW2YHMSFZJJ4R5XWWNUVND5I45YEWS5DSFKXCHMADZ5V374U2LM");
    assertEquals(
        operation.getTrustor(), "GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G");
    assertEquals(
        operation.getTrustorMuxed().get().getUnmuxedAddress(),
        "GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G");
    assertEquals(
        operation.getTrustorMuxed().get().toString(),
        "MAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472YAAAAAAAAAABUSON4");
    assertEquals(operation.getTrustorMuxed().get().getId().longValue(), 420l);
  }

  @Test
  public void testDeserializeSetOptionsOperation() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"self\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/1253868457431041\"\n"
            + "          },\n"
            + "          \"transaction\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/transactions/cc392abf8a4e649f16eeba4f40c43a8d50001b14a98ccfc639783125950e99d8\"\n"
            + "          },\n"
            + "          \"effects\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/1253868457431041/effects\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=1253868457431041\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=1253868457431041\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"1253868457431041\",\n"
            + "        \"paging_token\": \"1253868457431041\",\n"
            + "        \"source_account\": \"GB6FDI5Q46BJVPNVWJU527NH463N3EF3S6TXRA2YSCZHXANY3YLXB7MC\",\n"
            + "        \"type\": \"set_options\",\n"
            + "        \"type_i\": 5,\n"
            + "        \"signer_key\": \"GD3ZYXVC7C3ECD5I4E5NGPBFJJSULJ6HJI2FBHGKYFV34DSIWB4YEKJZ\",\n"
            + "        \"signer_weight\": 1,\n"
            + "        \"home_domain\": \"stellar.org\","
            + "        \"inflation_dest\": \"GBYWSY4NPLLPTP22QYANGTT7PEHND64P4D4B6LFEUHGUZRVYJK2H4TBE\","
            + "        \"low_threshold\": 1,\n"
            + "        \"med_threshold\": 2,\n"
            + "        \"high_threshold\": 3,\n"
            + "        \"master_key_weight\": 4,\n"
            + "        \"set_flags_s\": [\n"
            + "          \"auth_required_flag\"\n"
            + "        ],"
            + "        \"clear_flags_s\": [\n"
            + "          \"auth_revocable_flag\"\n"
            + "        ]"
            + "      }";

    SetOptionsOperationResponse operation =
        (SetOptionsOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(
        operation.getSignerKey(), "GD3ZYXVC7C3ECD5I4E5NGPBFJJSULJ6HJI2FBHGKYFV34DSIWB4YEKJZ");
    assertEquals(
        operation.getSignerKey(), "GD3ZYXVC7C3ECD5I4E5NGPBFJJSULJ6HJI2FBHGKYFV34DSIWB4YEKJZ");
    assertEquals(operation.getSignerWeight().intValue(), 1);
    assertEquals(operation.getHomeDomain(), "stellar.org");
    assertEquals(
        operation.getInflationDestination(),
        "GBYWSY4NPLLPTP22QYANGTT7PEHND64P4D4B6LFEUHGUZRVYJK2H4TBE");
    assertEquals(operation.getLowThreshold().intValue(), 1);
    assertEquals(operation.getMedThreshold().intValue(), 2);
    assertEquals(operation.getHighThreshold().intValue(), 3);
    assertEquals(operation.getMasterKeyWeight().intValue(), 4);
    assertEquals(operation.getSetFlags()[0], "auth_required_flag");
    assertEquals(operation.getClearFlags()[0], "auth_revocable_flag");
  }

  @Test
  public void testDeserializeAccountMergeOperation() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"self\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3424497684189185\"\n"
            + "          },\n"
            + "          \"transaction\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/transactions/68f4d37780e2a85f5698b73977126a595dff99aed852f14a7eb39221ce1f96d5\"\n"
            + "          },\n"
            + "          \"effects\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3424497684189185/effects\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3424497684189185\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3424497684189185\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"3424497684189185\",\n"
            + "        \"paging_token\": \"3424497684189185\",\n"
            + "        \"source_account\": \"GD6GKRABNDVYDETEZJQEPS7IBQMERCN44R5RCI4LJNX6BMYQM2KPGGZ2\",\n"
            + "        \"type\": \"account_merge\",\n"
            + "        \"type_i\": 8,\n"
            + "        \"account\": \"GD6GKRABNDVYDETEZJQEPS7IBQMERCN44R5RCI4LJNX6BMYQM2KPGGZ2\",\n"
            + "        \"into\": \"GAZWSWPDQTBHFIPBY4FEDFW2J6E2LE7SZHJWGDZO6Q63W7DBSRICO2KN\"\n"
            + "      }";

    AccountMergeOperationResponse operation =
        (AccountMergeOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(
        operation.getAccount(), "GD6GKRABNDVYDETEZJQEPS7IBQMERCN44R5RCI4LJNX6BMYQM2KPGGZ2");
    assertEquals(operation.getInto(), "GAZWSWPDQTBHFIPBY4FEDFW2J6E2LE7SZHJWGDZO6Q63W7DBSRICO2KN");
    assertFalse(operation.getIntoMuxed().isPresent());
    assertFalse(operation.getAccountMuxed().isPresent());
  }

  @Test
  public void testDeserializeMuxedAccountMergeOperation() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"self\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3424497684189185\"\n"
            + "          },\n"
            + "          \"transaction\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/transactions/68f4d37780e2a85f5698b73977126a595dff99aed852f14a7eb39221ce1f96d5\"\n"
            + "          },\n"
            + "          \"effects\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3424497684189185/effects\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3424497684189185\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3424497684189185\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"3424497684189185\",\n"
            + "        \"paging_token\": \"3424497684189185\",\n"
            + "        \"source_account\": \"GD6GKRABNDVYDETEZJQEPS7IBQMERCN44R5RCI4LJNX6BMYQM2KPGGZ2\",\n"
            + "        \"type\": \"account_merge\",\n"
            + "        \"type_i\": 8,\n"
            + "        \"account\": \"GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\",\n"
            + "        \"account_muxed\": \"MBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA6AAAAAAAAAAAPN7BA\",\n"
            + "        \"account_muxed_id\": \"123\",\n"
            + "        \"into\": \"GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\",\n"
            + "        \"into_muxed\": \"MAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472YAAAAAAAAAABUSON4\",\n"
            + "        \"into_muxed_id\": \"420\"\n"
            + "      }";

    AccountMergeOperationResponse operation =
        (AccountMergeOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(
        operation.getAccount(), "GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L");
    assertEquals(operation.getInto(), "GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G");

    assertEquals(
        operation.getAccountMuxed().get(),
        new MuxedAccount(
            "MBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA6AAAAAAAAAAAPN7BA",
            "GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L",
            valueOf(123l)));
    assertEquals(
        operation.getIntoMuxed().get().getUnmuxedAddress(),
        "GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G");
    assertEquals(
        operation.getIntoMuxed().get().toString(),
        "MAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472YAAAAAAAAAABUSON4");
    assertEquals(operation.getIntoMuxed().get().getId(), valueOf(420l));
  }

  @Test
  public void testDeserializeManageBuyOfferOperation() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"self\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3320426331639809\"\n"
            + "          },\n"
            + "          \"transaction\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/transactions/1f8fc03b26110e917d124381645d7dcf85927f17e46d8390d254a0bd99cfb0ad\"\n"
            + "          },\n"
            + "          \"effects\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/3320426331639809/effects\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3320426331639809\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3320426331639809\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"3320426331639809\",\n"
            + "        \"paging_token\": \"3320426331639809\",\n"
            + "        \"source_account\": \"GCR6QXX7IRIJVIM5WA5ASQ6MWDOEJNBW3V6RTC5NJXEMOLVTUVKZ725X\",\n"
            + "        \"type\": \"manage_buy_offer\",\n"
            + "        \"type_i\": 12,\n"
            + "        \"offer_id\": 0,\n"
            + "        \"amount\": \"100.0\",\n"
            + "        \"buying_asset_type\": \"credit_alphanum4\",\n"
            + "        \"buying_asset_code\": \"CNY\",\n"
            + "        \"buying_asset_issuer\": \"GAZWSWPDQTBHFIPBY4FEDFW2J6E2LE7SZHJWGDZO6Q63W7DBSRICO2KN\",\n"
            + "        \"selling_asset_type\": \"native\"\n"
            + "      }";

    ManageBuyOfferOperationResponse operation =
        (ManageBuyOfferOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getOfferId(), Long.valueOf(0));
    assertEquals(operation.getAmount(), "100.0");
    assertEquals(
        operation.getBuyingAsset(),
        create(null, "CNY", "GAZWSWPDQTBHFIPBY4FEDFW2J6E2LE7SZHJWGDZO6Q63W7DBSRICO2KN"));
    assertEquals(operation.getSellingAsset(), new AssetTypeNative());
  }

  @Test
  public void testDeserializePathPaymentOperation() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"self\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/operations/75252830662840321\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/transactions/fb2f5655c70a459220ac09eb3d6870422b58dcf5c5ffb5e5b21817b4d248826e\"\n"
            + "    },\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/operations/75252830662840321/effects\"\n"
            + "    },\n"
            + "    \"succeeds\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/effects?order=desc\\u0026cursor=75252830662840321\"\n"
            + "    },\n"
            + "    \"precedes\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/effects?order=asc\\u0026cursor=75252830662840321\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"id\": \"75252830662840321\",\n"
            + "  \"paging_token\": \"75252830662840321\",\n"
            + "  \"source_account\": \"GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD\",\n"
            + "  \"type\": \"path_payment\",\n"
            + "  \"type_i\": 2,\n"
            + "  \"created_at\": \"2018-04-24T12:58:12Z\",\n"
            + "  \"transaction_hash\": \"fb2f5655c70a459220ac09eb3d6870422b58dcf5c5ffb5e5b21817b4d248826e\",\n"
            + "  \"asset_type\": \"native\",\n"
            + "  \"from\": \"GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD\",\n"
            + "  \"to\": \"GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD\",\n"
            + "  \"amount\": \"2.5000000\",\n"
            + "  \"path\": [{\"asset_type\": \"native\"}, {\"asset_type\": \"credit_alphanum4\", \"asset_code\": \"CNY\", \"asset_issuer\": \"GAREELUB43IRHWEASCFBLKHURCGMHE5IF6XSE7EXDLACYHGRHM43RFOX\"}, {\"asset_type\": \"credit_alphanum12\", \"asset_code\": \"CNYMNL\", \"asset_issuer\": \"GAREELUB43IRHWEASCFBLKHURCGMHE5IF6XSE7EXDLACYHGRHM43RFOX\"}],\n"
            + "  \"source_amount\": \"1.1777000\",\n"
            + "  \"source_max\": \"1.1779523\",\n"
            + "  \"source_asset_type\": \"credit_alphanum4\",\n"
            + "  \"source_asset_code\": \"XRP\",\n"
            + "  \"source_asset_issuer\": \"GBVOL67TMUQBGL4TZYNMY3ZQ5WGQYFPFD5VJRWXR72VA33VFNL225PL5\"\n"
            + "}";

    PathPaymentStrictReceiveOperationResponse operation =
        (PathPaymentStrictReceiveOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getFrom(), "GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD");
    assertEquals(operation.getTo(), "GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD");
    assertEquals(operation.getAmount(), "2.5000000");
    assertEquals(operation.getSourceAmount(), "1.1777000");
    assertEquals(operation.getSourceMax(), "1.1779523");
    assertEquals(operation.getAsset(), new AssetTypeNative());
    assertEquals(
        operation.getPath(),
        Arrays.asList(
            new AssetTypeNative(),
            create(null, "CNY", "GAREELUB43IRHWEASCFBLKHURCGMHE5IF6XSE7EXDLACYHGRHM43RFOX"),
            create(null, "CNYMNL", "GAREELUB43IRHWEASCFBLKHURCGMHE5IF6XSE7EXDLACYHGRHM43RFOX")));
    assertEquals(
        operation.getSourceAsset(),
        create(null, "XRP", "GBVOL67TMUQBGL4TZYNMY3ZQ5WGQYFPFD5VJRWXR72VA33VFNL225PL5"));
  }

  @Test
  public void testDeserializePathPaymentStrictSendOperation() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"self\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/operations/75252830662840321\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/transactions/fb2f5655c70a459220ac09eb3d6870422b58dcf5c5ffb5e5b21817b4d248826e\"\n"
            + "    },\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/operations/75252830662840321/effects\"\n"
            + "    },\n"
            + "    \"succeeds\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/effects?order=desc\\u0026cursor=75252830662840321\"\n"
            + "    },\n"
            + "    \"precedes\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/effects?order=asc\\u0026cursor=75252830662840321\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"id\": \"75252830662840321\",\n"
            + "  \"paging_token\": \"75252830662840321\",\n"
            + "  \"source_account\": \"GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD\",\n"
            + "  \"type\": \"path_payment_strict_send\",\n"
            + "  \"type_i\": 13,\n"
            + "  \"created_at\": \"2018-04-24T12:58:12Z\",\n"
            + "  \"transaction_hash\": \"fb2f5655c70a459220ac09eb3d6870422b58dcf5c5ffb5e5b21817b4d248826e\",\n"
            + "  \"asset_type\": \"native\",\n"
            + "  \"from\": \"GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD\",\n"
            + "  \"to\": \"GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD\",\n"
            + "  \"amount\": \"2.5000000\",\n"
            + "  \"path\": [{\"asset_type\": \"native\"}, {\"asset_type\": \"credit_alphanum4\", \"asset_code\": \"CNY\", \"asset_issuer\": \"GAREELUB43IRHWEASCFBLKHURCGMHE5IF6XSE7EXDLACYHGRHM43RFOX\"}, {\"asset_type\": \"credit_alphanum12\", \"asset_code\": \"CNYMNL\", \"asset_issuer\": \"GAREELUB43IRHWEASCFBLKHURCGMHE5IF6XSE7EXDLACYHGRHM43RFOX\"}],\n"
            + "  \"source_amount\": \"1.1777000\",\n"
            + "  \"destination_min\": \"1.1779523\",\n"
            + "  \"source_asset_type\": \"credit_alphanum4\",\n"
            + "  \"source_asset_code\": \"XRP\",\n"
            + "  \"source_asset_issuer\": \"GBVOL67TMUQBGL4TZYNMY3ZQ5WGQYFPFD5VJRWXR72VA33VFNL225PL5\"\n"
            + "}";

    PathPaymentStrictSendOperationResponse operation =
        (PathPaymentStrictSendOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertFalse(operation.getSourceAccountMuxed().isPresent());
    assertEquals(operation.getFrom(), "GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD");
    assertFalse(operation.getFromMuxed().isPresent());
    assertEquals(operation.getTo(), "GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD");
    assertFalse(operation.getToMuxed().isPresent());
    assertEquals(operation.getAmount(), "2.5000000");
    assertEquals(operation.getSourceAmount(), "1.1777000");
    assertEquals(operation.getDestinationMin(), "1.1779523");
    assertEquals(operation.getAsset(), new AssetTypeNative());
    assertEquals(
        operation.getPath(),
        Arrays.asList(
            new AssetTypeNative(),
            create(null, "CNY", "GAREELUB43IRHWEASCFBLKHURCGMHE5IF6XSE7EXDLACYHGRHM43RFOX"),
            create(null, "CNYMNL", "GAREELUB43IRHWEASCFBLKHURCGMHE5IF6XSE7EXDLACYHGRHM43RFOX")));
    assertEquals(
        operation.getSourceAsset(),
        create(null, "XRP", "GBVOL67TMUQBGL4TZYNMY3ZQ5WGQYFPFD5VJRWXR72VA33VFNL225PL5"));
  }

  @Test
  public void testDeserializeMuxedPathPaymentStrictSendOperation() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"self\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/operations/75252830662840321\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/transactions/fb2f5655c70a459220ac09eb3d6870422b58dcf5c5ffb5e5b21817b4d248826e\"\n"
            + "    },\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/operations/75252830662840321/effects\"\n"
            + "    },\n"
            + "    \"succeeds\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/effects?order=desc\\u0026cursor=75252830662840321\"\n"
            + "    },\n"
            + "    \"precedes\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/effects?order=asc\\u0026cursor=75252830662840321\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"id\": \"75252830662840321\",\n"
            + "  \"paging_token\": \"75252830662840321\",\n"
            + "  \"source_account\": \"GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD\",\n"
            + "  \"type\": \"path_payment_strict_send\",\n"
            + "  \"type_i\": 13,\n"
            + "  \"created_at\": \"2018-04-24T12:58:12Z\",\n"
            + "  \"transaction_hash\": \"fb2f5655c70a459220ac09eb3d6870422b58dcf5c5ffb5e5b21817b4d248826e\",\n"
            + "  \"asset_type\": \"native\",\n"
            + "  \"from\": \"GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\",\n"
            + "  \"from_muxed\": \"MBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA6AAAAAAAAAAAPN7BA\",\n"
            + "  \"from_muxed_id\": \"123\",\n"
            + "  \"to\": \"GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\",\n"
            + "  \"to_muxed\": \"MAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472YAAAAAAAAAABUSON4\",\n"
            + "  \"to_muxed_id\": \"420\",\n"
            + "  \"amount\": \"2.5000000\",\n"
            + "  \"path\": [{\"asset_type\": \"native\"}, {\"asset_type\": \"credit_alphanum4\", \"asset_code\": \"CNY\", \"asset_issuer\": \"GAREELUB43IRHWEASCFBLKHURCGMHE5IF6XSE7EXDLACYHGRHM43RFOX\"}, {\"asset_type\": \"credit_alphanum12\", \"asset_code\": \"CNYMNL\", \"asset_issuer\": \"GAREELUB43IRHWEASCFBLKHURCGMHE5IF6XSE7EXDLACYHGRHM43RFOX\"}],\n"
            + "  \"source_amount\": \"1.1777000\",\n"
            + "  \"destination_min\": \"1.1779523\",\n"
            + "  \"source_asset_type\": \"credit_alphanum4\",\n"
            + "  \"source_asset_code\": \"XRP\",\n"
            + "  \"source_asset_issuer\": \"GBVOL67TMUQBGL4TZYNMY3ZQ5WGQYFPFD5VJRWXR72VA33VFNL225PL5\"\n"
            + "}";

    PathPaymentStrictSendOperationResponse operation =
        (PathPaymentStrictSendOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getFrom(), "GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L");
    assertEquals(
        operation.getFromMuxed().get(),
        new MuxedAccount(
            "MBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA6AAAAAAAAAAAPN7BA",
            "GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L",
            valueOf(123l)));

    assertEquals(operation.getTo(), "GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G");
    assertEquals(
        operation.getToMuxed().get().getUnmuxedAddress(),
        "GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G");
    assertEquals(
        operation.getToMuxed().get().toString(),
        "MAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472YAAAAAAAAAABUSON4");
    assertEquals(operation.getToMuxed().get().getId(), valueOf(420l));
  }

  @Test
  public void testDeserializePathPaymentOperationSourceAssetNative() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"self\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/operations/75252830662840321\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/transactions/fb2f5655c70a459220ac09eb3d6870422b58dcf5c5ffb5e5b21817b4d248826e\"\n"
            + "    },\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/operations/75252830662840321/effects\"\n"
            + "    },\n"
            + "    \"succeeds\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/effects?order=desc\\u0026cursor=75252830662840321\"\n"
            + "    },\n"
            + "    \"precedes\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/effects?order=asc\\u0026cursor=75252830662840321\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"id\": \"75252830662840321\",\n"
            + "  \"paging_token\": \"75252830662840321\",\n"
            + "  \"source_account\": \"GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD\",\n"
            + "  \"type\": \"path_payment\",\n"
            + "  \"type_i\": 2,\n"
            + "  \"created_at\": \"2018-04-24T12:58:12Z\",\n"
            + "  \"transaction_hash\": \"fb2f5655c70a459220ac09eb3d6870422b58dcf5c5ffb5e5b21817b4d248826e\",\n"
            + "  \"asset_type\": \"credit_alphanum4\",\n"
            + "  \"asset_code\": \"XRP\",\n"
            + "  \"asset_issuer\": \"GBVOL67TMUQBGL4TZYNMY3ZQ5WGQYFPFD5VJRWXR72VA33VFNL225PL5\",\n"
            + "  \"from\": \"GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD\",\n"
            + "  \"to\": \"GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD\",\n"
            + "  \"amount\": \"2.5000000\",\n"
            + "  \"path\": [],\n"
            + "  \"source_max\": \"1.1779523\",\n"
            + "  \"source_asset_type\": \"native\"\n"
            + "}";

    PathPaymentStrictReceiveOperationResponse operation =
        (PathPaymentStrictReceiveOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getFrom(), "GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD");
    assertEquals(operation.getTo(), "GC45JH537XZD4DY4WTV5PCUJL4KPOIE4WMGX5OP5KSPS2OLGRUOVVIGD");
    assertEquals(operation.getAmount(), "2.5000000");
    assertEquals(operation.getSourceMax(), "1.1779523");
    assertEquals(operation.getSourceAsset(), new AssetTypeNative());
    assertEquals(operation.getPath(), Collections.emptyList());
    assertEquals(
        operation.getAsset(),
        create(null, "XRP", "GBVOL67TMUQBGL4TZYNMY3ZQ5WGQYFPFD5VJRWXR72VA33VFNL225PL5"));
  }

  @Test
  public void testDeserializeInflationOperation() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"/operations/12884914177/effects/{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"precedes\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=asc\"\n"
            + "    },\n"
            + "    \"self\": {\n"
            + "      \"href\": \"/operations/12884914177\"\n"
            + "    },\n"
            + "    \"succeeds\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=desc\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"/transactions/12884914176\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"id\": 12884914177,\n"
            + "  \"paging_token\": \"12884914177\",\n"
            + "  \"type_i\": 9,\n"
            + "  \"type\": \"inflation\"\n"
            + "}";

    InflationOperationResponse operation =
        (InflationOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getId().longValue(), 12884914177L);
  }

  @Test
  public void testDeserializeManageDataOperation() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"self\": {\n"
            + "      \"href\": \"https://horizon-testnet.stellar.org/operations/14336188517191688\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"https://horizon-testnet.stellar.org/transactions/ad99999615f653528e67d8c8783e14044519c3e5233b73633aa88050c5856103\"\n"
            + "    },\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"https://horizon-testnet.stellar.org/operations/14336188517191688/effects\"\n"
            + "    },\n"
            + "    \"succeeds\": {\n"
            + "      \"href\": \"https://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=14336188517191688\"\n"
            + "    },\n"
            + "    \"precedes\": {\n"
            + "      \"href\": \"https://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=14336188517191688\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"id\": \"14336188517191688\",\n"
            + "  \"paging_token\": \"14336188517191688\",\n"
            + "  \"source_account\": \"GAC3U2W4KPZJX7GX5AZPOEWBJVUTJQ4TZNO74YN24ETTFIJVY7EMMANP\",\n"
            + "  \"type\": \"manage_data\",\n"
            + "  \"type_i\": 10,\n"
            + "  \"name\": \"CollateralValue\",\n"
            + "  \"value\": \"MjAwMA==\"\n"
            + "}";

    ManageDataOperationResponse operation =
        (ManageDataOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getId().longValue(), 14336188517191688L);
    assertEquals(operation.getName(), "CollateralValue");
    assertEquals(operation.getValue(), "MjAwMA==");
  }

  @Test
  public void testDeserializeManageDataOperationValueEmpty() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"self\": {\n"
            + "      \"href\": \"https://horizon-testnet.stellar.org/operations/14336188517191688\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"https://horizon-testnet.stellar.org/transactions/ad99999615f653528e67d8c8783e14044519c3e5233b73633aa88050c5856103\"\n"
            + "    },\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"https://horizon-testnet.stellar.org/operations/14336188517191688/effects\"\n"
            + "    },\n"
            + "    \"succeeds\": {\n"
            + "      \"href\": \"https://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=14336188517191688\"\n"
            + "    },\n"
            + "    \"precedes\": {\n"
            + "      \"href\": \"https://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=14336188517191688\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"id\": \"14336188517191688\",\n"
            + "  \"paging_token\": \"14336188517191688\",\n"
            + "  \"source_account\": \"GAC3U2W4KPZJX7GX5AZPOEWBJVUTJQ4TZNO74YN24ETTFIJVY7EMMANP\",\n"
            + "  \"type\": \"manage_data\",\n"
            + "  \"type_i\": 10,\n"
            + "  \"name\": \"CollateralValue\",\n"
            + "  \"value\": null\n"
            + "}";

    ManageDataOperationResponse operation =
        (ManageDataOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getValue(), null);
  }

  @Test
  public void testDeserializeBumpSequenceOperation() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"/operations/12884914177/effects/{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"precedes\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=asc\"\n"
            + "    },\n"
            + "    \"self\": {\n"
            + "      \"href\": \"/operations/12884914177\"\n"
            + "    },\n"
            + "    \"succeeds\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=desc\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"/transactions/12884914176\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"id\": 12884914177,\n"
            + "  \"paging_token\": \"12884914177\",\n"
            + "  \"type_i\": 11,\n"
            + "  \"type\": \"bump_sequence\",\n"
            + "  \"bump_to\": \"79473726952833048\"\n"
            + "}";

    BumpSequenceOperationResponse operation =
        (BumpSequenceOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getId().longValue(), 12884914177L);
    assertEquals(operation.getBumpTo().longValue(), 79473726952833048L);
  }

  @Test
  public void testDeserializeEndSponsoringFutureReservesOperation() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"/operations/12884914177/effects/{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"precedes\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=asc\"\n"
            + "    },\n"
            + "    \"self\": {\n"
            + "      \"href\": \"/operations/12884914177\"\n"
            + "    },\n"
            + "    \"succeeds\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=desc\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"/transactions/12884914176\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"id\": 12884914177,\n"
            + "  \"paging_token\": \"12884914177\",\n"
            + "  \"type_i\": 17,\n"
            + "  \"type\": \"end_sponsoring_future_reserves\",\n"
            + "  \"begin_sponsor\": \"GDRW375MAYR46ODGF2WGANQC2RRZL7O246DYHHCGWTV2RE7IHE2QUQLD\"\n"
            + "}";

    EndSponsoringFutureReservesOperationResponse operation =
        (EndSponsoringFutureReservesOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getId().longValue(), 12884914177L);
    assertEquals(
        operation.getBeginSponsor(), "GDRW375MAYR46ODGF2WGANQC2RRZL7O246DYHHCGWTV2RE7IHE2QUQLD");
    assertFalse(operation.getBeginSponsorMuxed().isPresent());
    assertEquals(operation.getType(), "end_sponsoring_future_reserves");
  }

  @Test
  public void testDeserializeMuxedEndSponsoringFutureReservesOperation() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"/operations/12884914177/effects/{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"precedes\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=asc\"\n"
            + "    },\n"
            + "    \"self\": {\n"
            + "      \"href\": \"/operations/12884914177\"\n"
            + "    },\n"
            + "    \"succeeds\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=desc\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"/transactions/12884914176\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"id\": 12884914177,\n"
            + "  \"paging_token\": \"12884914177\",\n"
            + "  \"type_i\": 17,\n"
            + "  \"type\": \"end_sponsoring_future_reserves\",\n"
            + "  \"begin_sponsor\": \"GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\",\n"
            + "  \"begin_sponsor_muxed\": \"MAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472YAAAAAAAAAABUSON4\",\n"
            + "  \"begin_sponsor_muxed_id\": \"420\"\n"
            + "}";

    EndSponsoringFutureReservesOperationResponse operation =
        (EndSponsoringFutureReservesOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getId().longValue(), 12884914177L);
    assertEquals(
        operation.getBeginSponsor(), "GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G");
    assertEquals(
        operation.getBeginSponsorMuxed().get().toString(),
        "MAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472YAAAAAAAAAABUSON4");
    assertEquals(operation.getBeginSponsorMuxed().get().getId().longValue(), 420l);
    assertEquals(operation.getType(), "end_sponsoring_future_reserves");
  }

  @Test
  public void testDeserializeClaimClaimableBalanceOperation() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"/operations/12884914177/effects/{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"precedes\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=asc\"\n"
            + "    },\n"
            + "    \"self\": {\n"
            + "      \"href\": \"/operations/12884914177\"\n"
            + "    },\n"
            + "    \"succeeds\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=desc\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"/transactions/12884914176\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"id\": 12884914177,\n"
            + "  \"paging_token\": \"12884914177\",\n"
            + "  \"type_i\": 15,\n"
            + "  \"type\": \"claim_claimable_balance\",\n"
            + "  \"claimant\": \"GDRW375MAYR46ODGF2WGANQC2RRZL7O246DYHHCGWTV2RE7IHE2QUQLD\",\n"
            + "  \"balance_id\": \"00000000178826fbfe339e1f5c53417c6fedfe2c05e8bec14303143ec46b38981b09c3f9\"\n"
            + "}";

    ClaimClaimableBalanceOperationResponse operation =
        (ClaimClaimableBalanceOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getId().longValue(), 12884914177L);
    assertEquals(
        operation.getBalanceId(),
        "00000000178826fbfe339e1f5c53417c6fedfe2c05e8bec14303143ec46b38981b09c3f9");
    assertEquals(
        operation.getClaimant(), "GDRW375MAYR46ODGF2WGANQC2RRZL7O246DYHHCGWTV2RE7IHE2QUQLD");
    assertFalse(operation.getClaimantMuxed().isPresent());
    assertEquals(operation.getType(), "claim_claimable_balance");
  }

  @Test
  public void testDeserializeMuxedClaimClaimableBalanceOperation() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"/operations/12884914177/effects/{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"precedes\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=asc\"\n"
            + "    },\n"
            + "    \"self\": {\n"
            + "      \"href\": \"/operations/12884914177\"\n"
            + "    },\n"
            + "    \"succeeds\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=desc\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"/transactions/12884914176\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"id\": 12884914177,\n"
            + "  \"paging_token\": \"12884914177\",\n"
            + "  \"type_i\": 15,\n"
            + "  \"type\": \"claim_claimable_balance\",\n"
            + "  \"claimant\": \"GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\",\n"
            + "  \"claimant_muxed\": \"MBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA6AAAAAAAAAAAPN7BA\",\n"
            + "  \"claimant_muxed_id\": \"123\",\n"
            + "  \"balance_id\": \"00000000178826fbfe339e1f5c53417c6fedfe2c05e8bec14303143ec46b38981b09c3f9\"\n"
            + "}";

    ClaimClaimableBalanceOperationResponse operation =
        (ClaimClaimableBalanceOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getId().longValue(), 12884914177L);
    assertEquals(
        operation.getBalanceId(),
        "00000000178826fbfe339e1f5c53417c6fedfe2c05e8bec14303143ec46b38981b09c3f9");
    assertEquals(
        operation.getClaimant(), "GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L");
    assertEquals(
        operation.getClaimantMuxed().get(),
        new MuxedAccount(
            "MBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA6AAAAAAAAAAAPN7BA",
            "GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L",
            valueOf(123l)));
    assertEquals(operation.getType(), "claim_claimable_balance");
  }

  @Test
  public void testDeserializeClawbackClaimableBalanceOperation() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"/operations/12884914177/effects/{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"precedes\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=asc\"\n"
            + "    },\n"
            + "    \"self\": {\n"
            + "      \"href\": \"/operations/12884914177\"\n"
            + "    },\n"
            + "    \"succeeds\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=desc\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"/transactions/12884914176\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"id\": 12884914177,\n"
            + "  \"paging_token\": \"12884914177\",\n"
            + "  \"type_i\": 20,\n"
            + "  \"type\": \"clawback_claimable_balance\",\n"
            + "  \"balance_id\": \"00000000178826fbfe339e1f5c53417c6fedfe2c05e8bec14303143ec46b38981b09c3f9\"\n"
            + "}";

    ClawbackClaimableBalanceOperationResponse operation =
        (ClawbackClaimableBalanceOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getId().longValue(), 12884914177L);
    assertEquals(
        operation.getBalanceId(),
        "00000000178826fbfe339e1f5c53417c6fedfe2c05e8bec14303143ec46b38981b09c3f9");
    assertEquals(operation.getType(), "clawback_claimable_balance");
  }

  @Test
  public void testDeserializeClawbackOperation() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"/operations/12884914177/effects/{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"precedes\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=asc\"\n"
            + "    },\n"
            + "    \"self\": {\n"
            + "      \"href\": \"/operations/12884914177\"\n"
            + "    },\n"
            + "    \"succeeds\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=desc\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"/transactions/12884914176\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"id\": 12884914177,\n"
            + "  \"paging_token\": \"12884914177\",\n"
            + "  \"type_i\": 19,\n"
            + "  \"type\": \"clawback\",\n"
            + "  \"asset_type\": \"credit_alphanum4\",\n"
            + "  \"asset_code\": \"EUR\",\n"
            + "  \"asset_issuer\": \"GCWVFBJ24754I5GXG4JOEB72GJCL3MKWC7VAEYWKGQHPVH3ENPNBSKWS\",\n"
            + "  \"from\": \"GDPFGP4IPE5DXG6XRXC4ZBUI43PAGRQ5VVNJ3LJTBXDBZ4ITO6HBHNSF\"\n"
            + "}";

    ClawbackOperationResponse operation =
        (ClawbackOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getId().longValue(), 12884914177L);
    assertEquals(operation.getFrom(), "GDPFGP4IPE5DXG6XRXC4ZBUI43PAGRQ5VVNJ3LJTBXDBZ4ITO6HBHNSF");
    assertEquals(operation.getType(), "clawback");
    assertEquals(
        operation.getAsset(),
        create(null, "EUR", "GCWVFBJ24754I5GXG4JOEB72GJCL3MKWC7VAEYWKGQHPVH3ENPNBSKWS"));
    assertEquals(
        operation.getAssetIssuer(), "GCWVFBJ24754I5GXG4JOEB72GJCL3MKWC7VAEYWKGQHPVH3ENPNBSKWS");
    assertEquals(operation.getAssetCode(), "EUR");
    assertEquals(operation.getAssetType(), "credit_alphanum4");
    assertFalse(operation.getFromMuxed().isPresent());
  }

  @Test
  public void testDeserializeMuxedClawbackOperation() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"/operations/12884914177/effects/{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"precedes\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=asc\"\n"
            + "    },\n"
            + "    \"self\": {\n"
            + "      \"href\": \"/operations/12884914177\"\n"
            + "    },\n"
            + "    \"succeeds\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=desc\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"/transactions/12884914176\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"id\": 12884914177,\n"
            + "  \"paging_token\": \"12884914177\",\n"
            + "  \"type_i\": 19,\n"
            + "  \"type\": \"clawback\",\n"
            + "  \"asset_type\": \"credit_alphanum4\",\n"
            + "  \"asset_code\": \"EUR\",\n"
            + "  \"asset_issuer\": \"GCWVFBJ24754I5GXG4JOEB72GJCL3MKWC7VAEYWKGQHPVH3ENPNBSKWS\",\n"
            + "  \"from\": \"GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\",\n"
            + "  \"from_muxed\": \"MBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA6AAAAAAAAAAAPN7BA\",\n"
            + "  \"from_muxed_id\": \"123\"\n"
            + "}";

    ClawbackOperationResponse operation =
        (ClawbackOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getFrom(), "GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L");
    assertEquals(
        operation.getFromMuxed().get(),
        new MuxedAccount(
            "MBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA6AAAAAAAAAAAPN7BA",
            "GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L",
            valueOf(123l)));
  }

  @Test
  public void testDeserializeSetTrustlineFlagsOperation() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"/operations/12884914177/effects/{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"precedes\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=asc\"\n"
            + "    },\n"
            + "    \"self\": {\n"
            + "      \"href\": \"/operations/12884914177\"\n"
            + "    },\n"
            + "    \"succeeds\": {\n"
            + "      \"href\": \"/operations?cursor=12884914177\\u0026order=desc\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"/transactions/12884914176\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"id\": 12884914177,\n"
            + "  \"paging_token\": \"12884914177\",\n"
            + "  \"type_i\": 21,\n"
            + "  \"type\": \"set_trust_line_flags\",\n"
            + "  \"asset_type\": \"credit_alphanum4\",\n"
            + "  \"asset_code\": \"EUR\",\n"
            + "  \"asset_issuer\": \"GCWVFBJ24754I5GXG4JOEB72GJCL3MKWC7VAEYWKGQHPVH3ENPNBSKWS\",\n"
            + "  \"set_flags\": [\n"
            + "     4\n"
            + "  ],"
            + "  \"clear_flags\": [\n"
            + "     2\n"
            + "  ],"
            + "  \"set_flags_s\": [\n"
            + "     \"clawback_enabled\"\n"
            + "  ],"
            + "  \"clear_flags_s\": [\n"
            + "     \"authorized_to_maintain_liabilites\"\n"
            + "  ],"
            + "  \"trustor\": \"GDPFGP4IPE5DXG6XRXC4ZBUI43PAGRQ5VVNJ3LJTBXDBZ4ITO6HBHNSF\"\n"
            + "}";

    SetTrustLineFlagsOperationResponse operation =
        (SetTrustLineFlagsOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getId().longValue(), 12884914177L);
    assertEquals(
        operation.getTrustor(), "GDPFGP4IPE5DXG6XRXC4ZBUI43PAGRQ5VVNJ3LJTBXDBZ4ITO6HBHNSF");
    assertEquals(operation.getType(), "set_trust_line_flags");
    assertEquals(
        operation.getAsset(),
        create(null, "EUR", "GCWVFBJ24754I5GXG4JOEB72GJCL3MKWC7VAEYWKGQHPVH3ENPNBSKWS"));
    assertEquals(
        operation.getAssetIssuer(), "GCWVFBJ24754I5GXG4JOEB72GJCL3MKWC7VAEYWKGQHPVH3ENPNBSKWS");
    assertEquals(operation.getAssetCode(), "EUR");
    assertEquals(operation.getAssetType(), "credit_alphanum4");
    assertEquals(operation.getSetFlags(), Collections.singletonList(4));
    assertEquals(operation.getClearFlags(), Collections.singletonList(2));
    assertEquals(operation.getSetFlagStrings(), Collections.singletonList("clawback_enabled"));
    assertEquals(
        operation.getClearFlagStrings(),
        Collections.singletonList("authorized_to_maintain_liabilites"));
  }

  @Test
  public void testDeserializeLiquidityPoolDepositOperation() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"self\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/operations/2278153733029889\"\n"
            + "          },\n"
            + "          \"transaction\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/transactions/0a3037071e226d981ad4114d268ad88693ee432a7b267b6007bae48f63e5e84d\"\n"
            + "          },\n"
            + "          \"effects\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/operations/2278153733029889/effects\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=2278153733029889\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=2278153733029889\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"2278153733029889\",\n"
            + "        \"paging_token\": \"2278153733029889\",\n"
            + "        \"transaction_successful\": true,\n"
            + "        \"source_account\": \"GC5NCMHE56RCV44WULN6BU3THEJWWXH6PYNLCM5LGWGEGIVLTP3355V3\",\n"
            + "        \"type\": \"liquidity_pool_deposit\",\n"
            + "        \"type_i\": 22,\n"
            + "        \"created_at\": \"2021-10-17T15:45:20Z\",\n"
            + "        \"transaction_hash\": \"0a3037071e226d981ad4114d268ad88693ee432a7b267b6007bae48f63e5e84d\",\n"
            + "        \"liquidity_pool_id\": \"1df1380108ca32e96650074db1f3e1e10541ab8768c9eba7ec3b6f9315f9faee\",\n"
            + "        \"reserves_max\": [\n"
            + "          {\n"
            + "            \"asset\": \"ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO\",\n"
            + "            \"amount\": \"10000.0000000\"\n"
            + "          },\n"
            + "          {\n"
            + "            \"asset\": \"USDC:GC5W3BH2MQRQK2H4A6LP3SXDSAAY2W2W64OWKKVNQIAOVWSAHFDEUSDC\",\n"
            + "            \"amount\": \"187.0000000\"\n"
            + "          }\n"
            + "        ],\n"
            + "        \"min_price\": \"53.4759358\",\n"
            + "        \"min_price_r\": {\n"
            + "          \"n\": 10000,\n"
            + "          \"d\": 187\n"
            + "        },\n"
            + "        \"max_price\": \"53.4759358\",\n"
            + "        \"max_price_r\": {\n"
            + "          \"n\": 10000,\n"
            + "          \"d\": 187\n"
            + "        },\n"
            + "        \"reserves_deposited\": [\n"
            + "          {\n"
            + "            \"asset\": \"ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO\",\n"
            + "            \"amount\": \"10000.0000000\"\n"
            + "          },\n"
            + "          {\n"
            + "            \"asset\": \"USDC:GC5W3BH2MQRQK2H4A6LP3SXDSAAY2W2W64OWKKVNQIAOVWSAHFDEUSDC\",\n"
            + "            \"amount\": \"187.0000000\"\n"
            + "          }\n"
            + "        ],\n"
            + "        \"shares_received\": \"1367.4794331\"\n"
            + "      }";

    LiquidityPoolDepositOperationResponse operation =
        (LiquidityPoolDepositOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getId().longValue(), 2278153733029889L);
    assertEquals(operation.getType(), "liquidity_pool_deposit");
    assertEquals(operation.getSharesReceived(), "1367.4794331");
    assertEquals(
        operation.getLiquidityPoolId().toString(),
        "1df1380108ca32e96650074db1f3e1e10541ab8768c9eba7ec3b6f9315f9faee");
    assertEquals(operation.getMaxPrice(), "53.4759358");
    assertEquals(operation.getMaxPriceR(), new Price(10000, 187));
    assertEquals(operation.getMinPrice(), "53.4759358");
    assertEquals(operation.getMinPriceR(), new Price(10000, 187));
    assertTrue(
        Arrays.equals(
            operation.getReservesDeposited(),
            new AssetAmount[] {
              new AssetAmount(
                  create("ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO"),
                  "10000.0000000"),
              new AssetAmount(
                  create("USDC:GC5W3BH2MQRQK2H4A6LP3SXDSAAY2W2W64OWKKVNQIAOVWSAHFDEUSDC"),
                  "187.0000000")
            }));
    assertTrue(
        Arrays.equals(
            operation.getReservesMax(),
            new AssetAmount[] {
              new AssetAmount(
                  create("ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO"),
                  "10000.0000000"),
              new AssetAmount(
                  create("USDC:GC5W3BH2MQRQK2H4A6LP3SXDSAAY2W2W64OWKKVNQIAOVWSAHFDEUSDC"),
                  "187.0000000")
            }));
  }

  @Test
  public void testDeserializeLiquidityPoolWithdrawOperation() {
    String json =
        "      {\n"
            + "        \"_links\": {\n"
            + "          \"self\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/operations/2313539968573441\"\n"
            + "          },\n"
            + "          \"transaction\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/transactions/691487d46d6e8bf37bf54afae9a99cd23e1e609fd1b0071cdd5011e68099306a\"\n"
            + "          },\n"
            + "          \"effects\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/operations/2313539968573441/effects\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=2313539968573441\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=2313539968573441\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"2313539968573441\",\n"
            + "        \"paging_token\": \"2313539968573441\",\n"
            + "        \"transaction_successful\": true,\n"
            + "        \"source_account\": \"GC5NCMHE56RCV44WULN6BU3THEJWWXH6PYNLCM5LGWGEGIVLTP3355V3\",\n"
            + "        \"type\": \"liquidity_pool_withdraw\",\n"
            + "        \"type_i\": 23,\n"
            + "        \"created_at\": \"2021-10-18T03:47:46Z\",\n"
            + "        \"transaction_hash\": \"691487d46d6e8bf37bf54afae9a99cd23e1e609fd1b0071cdd5011e68099306a\",\n"
            + "        \"liquidity_pool_id\": \"1df1380108ca32e96650074db1f3e1e10541ab8768c9eba7ec3b6f9315f9faee\",\n"
            + "        \"reserves_min\": [\n"
            + "          {\n"
            + "            \"asset\": \"ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO\",\n"
            + "            \"amount\": \"10000.0000000\"\n"
            + "          },\n"
            + "          {\n"
            + "            \"asset\": \"USDC:GC5W3BH2MQRQK2H4A6LP3SXDSAAY2W2W64OWKKVNQIAOVWSAHFDEUSDC\",\n"
            + "            \"amount\": \"187.0000000\"\n"
            + "          }\n"
            + "        ],\n"
            + "        \"shares\": \"1367.4794331\",\n"
            + "        \"reserves_received\": [\n"
            + "          {\n"
            + "            \"asset\": \"ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO\",\n"
            + "            \"amount\": \"10000.0000000\"\n"
            + "          },\n"
            + "          {\n"
            + "            \"asset\": \"USDC:GC5W3BH2MQRQK2H4A6LP3SXDSAAY2W2W64OWKKVNQIAOVWSAHFDEUSDC\",\n"
            + "            \"amount\": \"187.0000000\"\n"
            + "          }\n"
            + "        ]\n"
            + "      }";

    LiquidityPoolWithdrawOperationResponse operation =
        (LiquidityPoolWithdrawOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getId().longValue(), 2313539968573441L);
    assertEquals(operation.getType(), "liquidity_pool_withdraw");
    assertEquals(operation.getShares(), "1367.4794331");
    assertEquals(
        operation.getLiquidityPoolId().toString(),
        "1df1380108ca32e96650074db1f3e1e10541ab8768c9eba7ec3b6f9315f9faee");
    assertTrue(
        Arrays.equals(
            operation.getReservesMin(),
            new AssetAmount[] {
              new AssetAmount(
                  create("ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO"),
                  "10000.0000000"),
              new AssetAmount(
                  create("USDC:GC5W3BH2MQRQK2H4A6LP3SXDSAAY2W2W64OWKKVNQIAOVWSAHFDEUSDC"),
                  "187.0000000")
            }));
    assertTrue(
        Arrays.equals(
            operation.getReservesReceived(),
            new AssetAmount[] {
              new AssetAmount(
                  create("ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO"),
                  "10000.0000000"),
              new AssetAmount(
                  create("USDC:GC5W3BH2MQRQK2H4A6LP3SXDSAAY2W2W64OWKKVNQIAOVWSAHFDEUSDC"),
                  "187.0000000")
            }));
  }

  @Test
  public void testDeserializeCreateClaimableBalanceOperation() {
    String json =
        "{\n"
            + "  \n"
            + "  \"id\": \"158104892991700993\",\n"
            + "  \"paging_token\": \"158104892991700993\",\n"
            + "  \"transaction_successful\": true,\n"
            + "  \"source_account\": \"GAKFBRS24U3PEN6XDMEX4JEV5NGBARS2ZFF5O4CF3JL464SQSD4MDCPF\",\n"
            + "  \"type\": \"create_claimable_balance\",\n"
            + "  \"type_i\": 14,\n"
            + "  \"created_at\": \"2021-08-11T16:16:32Z\",\n"
            + "  \"transaction_hash\": \"c78e86007a0ee0bb0c717b02f5e306e524b4913b892e9983e7db4664a0c29841\",\n"
            + "  \"sponsor\": \"GAKFBRS24U3PEN6XDMEX4JEV5NGBARS2ZFF5O4CF3JL464SQSD4MDCPF\",\n"
            + "  \"asset\": \"KES:GA2MSSZKJOU6RNL3EJKH3S5TB5CDYTFQFWRYFGUJVIN5I6AOIRTLUHTO\",\n"
            + "  \"amount\": \"0.0012200\",\n"
            + "  \"claimants\": [\n"
            + "    {\n"
            + "      \"destination\": \"GBQECQVAS2FJ7DLCUXDASZAJQLWPXNTCR2DGBPKQDO3QS66TJXLRHFIK\",\n"
            + "      \"predicate\": {\n"
            + "        \"abs_before\": \"+39121901036-03-29T15:30:22Z\",\n"
            + "        \"abs_before_epoch\": \"1234567890982222222\"\n"
            + "      }\n"
            + "    },\n"
            + "    {\n"
            + "      \"destination\": \"GAKFBRS24U3PEN6XDMEX4JEV5NGBARS2ZFF5O4CF3JL464SQSD4MDCPF\",\n"
            + "      \"predicate\": {\n"
            + "        \"unconditional\": true\n"
            + "      }\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    CreateClaimableBalanceOperationResponse operation =
        (CreateClaimableBalanceOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals(operation.getId().longValue(), 158104892991700993L);
    assertEquals(operation.getClaimants().size(), 2);
    assertEquals(
        operation.getClaimants().get(0).getDestination(),
        "GBQECQVAS2FJ7DLCUXDASZAJQLWPXNTCR2DGBPKQDO3QS66TJXLRHFIK");
    assertSame(
        operation.getClaimants().get(0).getPredicate().getClass(), Predicate.AbsBefore.class);
    assertEquals(
        ((Predicate.AbsBefore) operation.getClaimants().get(0).getPredicate())
            .getTimestampSeconds(),
        BigInteger.valueOf(1234567890982222222L));
    assertEquals(
        operation.getClaimants().get(1).getDestination(),
        "GAKFBRS24U3PEN6XDMEX4JEV5NGBARS2ZFF5O4CF3JL464SQSD4MDCPF");
    assertSame(
        operation.getClaimants().get(1).getPredicate().getClass(), Predicate.Unconditional.class);
    assertEquals(operation.getType(), "create_claimable_balance");
  }

  @Test
  public void testDeserializeInvokeHostFunctionOperation() {
    String json =
        "{\n"
            + "    \"_links\": {\n"
            + "        \"self\": {\n"
            + "            \"href\": \"http://127.0.0.1:8000/operations/2224793063425\"\n"
            + "        },\n"
            + "        \"transaction\": {\n"
            + "            \"href\": \"http://127.0.0.1:8000/transactions/4ef3d81fba4b7db959080e4894cb8b2575418b8da9aa484f6306a79a3f63de3d\"\n"
            + "        },\n"
            + "        \"effects\": {\n"
            + "            \"href\": \"http://127.0.0.1:8000/operations/2224793063425/effects\"\n"
            + "        },\n"
            + "        \"succeeds\": {\n"
            + "            \"href\": \"http://127.0.0.1:8000/effects?order=desc&cursor=2224793063425\"\n"
            + "        },\n"
            + "        \"precedes\": {\n"
            + "            \"href\": \"http://127.0.0.1:8000/effects?order=asc&cursor=2224793063425\"\n"
            + "        }\n"
            + "    },\n"
            + "    \"id\": \"2224793063425\",\n"
            + "    \"paging_token\": \"2224793063425\",\n"
            + "    \"transaction_successful\": true,\n"
            + "    \"source_account\": \"GDAT5HWTGIU4TSSZ4752OUC4SABDLTLZFRPZUJ3D6LKBNEPA7V2CIG54\",\n"
            + "    \"type\": \"invoke_host_function\",\n"
            + "    \"type_i\": 24,\n"
            + "    \"created_at\": \"2023-07-20T10:44:56Z\",\n"
            + "    \"transaction_hash\": \"4ef3d81fba4b7db959080e4894cb8b2575418b8da9aa484f6306a79a3f63de3d\",\n"
            + "    \"function\": \"HostFunctionTypeHostFunctionTypeInvokeContract\",\n"
            + "    \"parameters\": [\n"
            + "        {\n"
            + "            \"value\": \"AAAAEgAAAAGw7oy+G8a9SeTIE5E/EuJYl5JfwF0eZJWk8S7LmE7fwA==\",\n"
            + "            \"type\": \"Address\"\n"
            + "        },\n"
            + "        {\n"
            + "            \"value\": \"AAAADwAAAAh0cmFuc2Zlcg==\",\n"
            + "            \"type\": \"Sym\"\n"
            + "        },\n"
            + "        {\n"
            + "            \"value\": \"AAAAEgAAAAAAAAAAwT6e0zIpycpZ5/unUFyQAjXNeSxfmidj8tQWkeD9dCQ=\",\n"
            + "            \"type\": \"Address\"\n"
            + "        },\n"
            + "        {\n"
            + "            \"value\": \"AAAAEgAAAAAAAAAAWLfEosjyl6qPPSRxKB/fzOyv5I5WYzE+wY4Spz7KmKE=\",\n"
            + "            \"type\": \"Address\"\n"
            + "        },\n"
            + "        {\n"
            + "            \"value\": \"AAAACgAAAAAAAAAAAAAAASoF8gA=\",\n"
            + "            \"type\": \"I128\"\n"
            + "        }\n"
            + "    ],\n"
            + "    \"address\": \"\",\n"
            + "    \"salt\": \"\",\n"
            + "    \"asset_balance_changes\": [\n"
            + "        {\n"
            + "            \"asset_type\": \"credit_alphanum12\",\n"
            + "            \"asset_code\": \"Hello\",\n"
            + "            \"asset_issuer\": \"GDJKBIYIPBE2NC5XIZX6GCFZHVWFUA7ONMQUOOVTLIM3BESTI4BYADAN\",\n"
            + "            \"type\": \"transfer\",\n"
            + "            \"from\": \"GDAT5HWTGIU4TSSZ4752OUC4SABDLTLZFRPZUJ3D6LKBNEPA7V2CIG54\",\n"
            + "            \"to\": \"GBMLPRFCZDZJPKUPHUSHCKA737GOZL7ERZLGGMJ6YGHBFJZ6ZKMKCZTM\",\n"
            + "            \"amount\": \"500.0000000\"\n"
            + "        }\n"
            + "    ]\n"
            + "}";

    InvokeHostFunctionOperationResponse operation =
        (InvokeHostFunctionOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);
    assertEquals(
        operation.getLinks().getSelf().getHref(), "http://127.0.0.1:8000/operations/2224793063425");
    assertEquals(operation.getId().longValue(), 2224793063425L);
    assertEquals(operation.getPagingToken(), "2224793063425");
    // TODO: add transaction_successful field to the response
    // assertEquals(operation.getTransactionSuccessful(), true);
    assertEquals(
        operation.getSourceAccount(), "GDAT5HWTGIU4TSSZ4752OUC4SABDLTLZFRPZUJ3D6LKBNEPA7V2CIG54");
    assertEquals(operation.getType(), "invoke_host_function");
    // TODO: add type_i field to the response
    assertEquals(operation.getCreatedAt(), "2023-07-20T10:44:56Z");
    assertEquals(
        operation.getTransactionHash(),
        "4ef3d81fba4b7db959080e4894cb8b2575418b8da9aa484f6306a79a3f63de3d");
    assertEquals(operation.getFunction(), "HostFunctionTypeHostFunctionTypeInvokeContract");
    assertEquals(operation.getParameters().size(), 5);
    assertEquals(operation.getParameters().get(0).getType(), "Address");
    assertEquals(
        operation.getParameters().get(0).getValue(),
        "AAAAEgAAAAGw7oy+G8a9SeTIE5E/EuJYl5JfwF0eZJWk8S7LmE7fwA==");
    assertEquals(operation.getParameters().get(1).getType(), "Sym");
    assertEquals(operation.getParameters().get(1).getValue(), "AAAADwAAAAh0cmFuc2Zlcg==");
    assertEquals(operation.getParameters().get(2).getType(), "Address");
    assertEquals(
        operation.getParameters().get(2).getValue(),
        "AAAAEgAAAAAAAAAAwT6e0zIpycpZ5/unUFyQAjXNeSxfmidj8tQWkeD9dCQ=");
    assertEquals(operation.getParameters().get(3).getType(), "Address");
    assertEquals(
        operation.getParameters().get(3).getValue(),
        "AAAAEgAAAAAAAAAAWLfEosjyl6qPPSRxKB/fzOyv5I5WYzE+wY4Spz7KmKE=");
    assertEquals(operation.getParameters().get(4).getType(), "I128");
    assertEquals(operation.getParameters().get(4).getValue(), "AAAACgAAAAAAAAAAAAAAASoF8gA=");

    assertEquals(operation.getAddress(), "");
    assertEquals(operation.getSalt(), "");
    assertEquals(operation.getAssetBalanceChanges().size(), 1);
    assertEquals(operation.getAssetBalanceChanges().get(0).getAssetType(), "credit_alphanum12");
    assertEquals(operation.getAssetBalanceChanges().get(0).getAssetCode(), "Hello");
    assertEquals(
        operation.getAssetBalanceChanges().get(0).getAssetIssuer(),
        "GDJKBIYIPBE2NC5XIZX6GCFZHVWFUA7ONMQUOOVTLIM3BESTI4BYADAN");
    assertEquals(operation.getAssetBalanceChanges().get(0).getType(), "transfer");
    assertEquals(
        operation.getAssetBalanceChanges().get(0).getFrom(),
        "GDAT5HWTGIU4TSSZ4752OUC4SABDLTLZFRPZUJ3D6LKBNEPA7V2CIG54");
    assertEquals(
        operation.getAssetBalanceChanges().get(0).getTo(),
        "GBMLPRFCZDZJPKUPHUSHCKA737GOZL7ERZLGGMJ6YGHBFJZ6ZKMKCZTM");
    assertEquals(operation.getAssetBalanceChanges().get(0).getAmount(), "500.0000000");
  }

  @Test
  public void testDeserializeExtendFootprintTTLOperation() {
    String json =
        "{\n"
            + "    \"_links\": {\n"
            + "        \"self\": {\n"
            + "            \"href\": \"http://127.0.0.1:8000/operations/2224793063426\"\n"
            + "        },\n"
            + "        \"transaction\": {\n"
            + "            \"href\": \"http://127.0.0.1:8000/transactions/c452cd9d1ff9692499d0d2aa2f8e898b8c38025300c0f293f4a2adde7295c82f\"\n"
            + "        },\n"
            + "        \"effects\": {\n"
            + "            \"href\": \"http://127.0.0.1:8000/operations/2224793063426/effects\"\n"
            + "        },\n"
            + "        \"succeeds\": {\n"
            + "            \"href\": \"http://127.0.0.1:8000/effects?order=desc&cursor=2224793063426\"\n"
            + "        },\n"
            + "        \"precedes\": {\n"
            + "            \"href\": \"http://127.0.0.1:8000/effects?order=asc&cursor=2224793063426\"\n"
            + "        }\n"
            + "    },\n"
            + "    \"id\": \"2224793063426\",\n"
            + "    \"paging_token\": \"2224793063426\",\n"
            + "    \"transaction_successful\": true,\n"
            + "    \"source_account\": \"GDAT5HWTGIU4TSSZ4752OUC4SABDLTLZFRPZUJ3D6LKBNEPA7V2CIG54\",\n"
            + "    \"type\": \"bump_footprint_expiration\",\n"
            + "    \"type_i\": 25,\n"
            + "    \"created_at\": \"2023-07-20T10:44:56Z\",\n"
            + "    \"transaction_hash\": \"c452cd9d1ff9692499d0d2aa2f8e898b8c38025300c0f293f4a2adde7295c82f\",\n"
            + "    \"extend_to\": \"2343241\"\n"
            + "}";

    ExtendFootprintTTLOperationResponse operation =
        (ExtendFootprintTTLOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);
    assertEquals(
        operation.getLinks().getSelf().getHref(), "http://127.0.0.1:8000/operations/2224793063426");
    assertEquals(operation.getId().longValue(), 2224793063426L);
    assertEquals(operation.getPagingToken(), "2224793063426");
    // assertEquals(operation.getTransactionSuccessful(), true);
    assertEquals(
        operation.getSourceAccount(), "GDAT5HWTGIU4TSSZ4752OUC4SABDLTLZFRPZUJ3D6LKBNEPA7V2CIG54");
    assertEquals(operation.getType(), "bump_footprint_expiration");
    assertEquals(operation.getCreatedAt(), "2023-07-20T10:44:56Z");
    assertEquals(
        operation.getTransactionHash(),
        "c452cd9d1ff9692499d0d2aa2f8e898b8c38025300c0f293f4a2adde7295c82f");
    assertEquals(operation.getExtendTo().longValue(), 2343241);
  }

  @Test
  public void testDeserializeRestoreFootprintOperationResponse() {
    String json =
        "{\n"
            + "    \"_links\": {\n"
            + "        \"self\": {\n"
            + "            \"href\": \"http://127.0.0.1:8000/operations/2224793063427\"\n"
            + "        },\n"
            + "        \"transaction\": {\n"
            + "            \"href\": \"http://127.0.0.1:8000/transactions/b6932dacb25e05ca8e3d006d2a5a119683602f70474cc9f5de9fc53e99f627f8\"\n"
            + "        },\n"
            + "        \"effects\": {\n"
            + "            \"href\": \"http://127.0.0.1:8000/operations/2224793063427/effects\"\n"
            + "        },\n"
            + "        \"succeeds\": {\n"
            + "            \"href\": \"http://127.0.0.1:8000/effects?order=desc&cursor=2224793063427\"\n"
            + "        },\n"
            + "        \"precedes\": {\n"
            + "            \"href\": \"http://127.0.0.1:8000/effects?order=asc&cursor=2224793063427\"\n"
            + "        }\n"
            + "    },\n"
            + "    \"id\": \"2224793063427\",\n"
            + "    \"paging_token\": \"2224793063427\",\n"
            + "    \"transaction_successful\": true,\n"
            + "    \"source_account\": \"GDAT5HWTGIU4TSSZ4752OUC4SABDLTLZFRPZUJ3D6LKBNEPA7V2CIG54\",\n"
            + "    \"type\": \"restore_footprint\",\n"
            + "    \"type_i\": 26,\n"
            + "    \"created_at\": \"2023-07-20T10:44:56Z\",\n"
            + "    \"transaction_hash\": \"b6932dacb25e05ca8e3d006d2a5a119683602f70474cc9f5de9fc53e99f627f8\"\n"
            + "}";

    RestoreFootprintOperationResponse operation =
        (RestoreFootprintOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);
    assertEquals(
        operation.getLinks().getSelf().getHref(), "http://127.0.0.1:8000/operations/2224793063427");
    assertEquals(operation.getId().longValue(), 2224793063427L);
    assertEquals(operation.getPagingToken(), "2224793063427");
    // assertEquals(operation.getTransactionSuccessful(), true);
    assertEquals(
        operation.getSourceAccount(), "GDAT5HWTGIU4TSSZ4752OUC4SABDLTLZFRPZUJ3D6LKBNEPA7V2CIG54");
    assertEquals(operation.getType(), "restore_footprint");
    assertEquals(operation.getCreatedAt(), "2023-07-20T10:44:56Z");
    assertEquals(
        operation.getTransactionHash(),
        "b6932dacb25e05ca8e3d006d2a5a119683602f70474cc9f5de9fc53e99f627f8");
  }
}
