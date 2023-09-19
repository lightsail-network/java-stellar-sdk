package org.stellar.sdk.responses;

import com.google.common.base.Optional;
import java.util.Arrays;
import junit.framework.TestCase;
import org.junit.Test;
import org.stellar.sdk.AssetTypeCreditAlphaNum4;
import org.stellar.sdk.AssetTypeNative;

public class AccountDeserializerTest extends TestCase {
  @Test
  public void testDeserializeBalanceAuth() {
    AccountResponse account =
        GsonSingleton.getInstance()
            .fromJson(jsonAuthorizedToMaintainLiabilities, AccountResponse.class);

    assertEquals(account.getBalances()[0].getAssetType(), "credit_alphanum4");
    assertEquals(account.getBalances()[0].getAssetCode(), Optional.of("ABC"));
    assertEquals(
        account.getBalances()[0].getAssetIssuer(),
        Optional.of("GCRA6COW27CY5MTKIA7POQ2326C5ABYCXODBN4TFF5VL4FMBRHOT3YHU"));
    assertEquals(
        account.getBalances()[0].getAsset(),
        Optional.of(
            new AssetTypeCreditAlphaNum4(
                "ABC", "GCRA6COW27CY5MTKIA7POQ2326C5ABYCXODBN4TFF5VL4FMBRHOT3YHU")));
    assertEquals(account.getBalances()[0].getBalance(), "1001.0000000");
    assertEquals(account.getBalances()[0].getLimit(), "12000.4775807");
    assertEquals(account.getBalances()[0].getBuyingLiabilities(), Optional.of("100.1234567"));
    assertEquals(account.getBalances()[0].getSellingLiabilities(), Optional.of("100.7654321"));
    assertEquals(account.getBalances()[0].getAuthorized(), Boolean.FALSE);
    assertEquals(account.getBalances()[0].getAuthorizedToMaintainLiabilities(), Boolean.TRUE);

    assertEquals(account.getBalances()[1].getAssetType(), "native");
    assertEquals(account.getBalances()[1].getAsset(), Optional.of(new AssetTypeNative()));
    assertEquals(account.getBalances()[1].getBalance(), "20.0000300");
    assertEquals(account.getBalances()[1].getBuyingLiabilities(), Optional.of("5.1234567"));
    assertEquals(account.getBalances()[1].getSellingLiabilities(), Optional.of("1.7654321"));
    assertEquals(account.getBalances()[1].getLimit(), null);
  }

  @Test
  public void testDeserialize() {
    AccountResponse account = GsonSingleton.getInstance().fromJson(json, AccountResponse.class);
    assertEquals(
        account.getAccountId(), "GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7");
    assertEquals(account.getSequenceNumber(), new Long(2319149195853854L));
    assertEquals(account.getSubentryCount(), new Integer(0));
    assertEquals(account.getSequenceUpdatedAtLedger().longValue(), 1234);
    assertEquals(account.getSequenceUpdatedAtTime().longValue(), 4567);
    assertEquals(
        account.getInflationDestination(),
        "GAGRSA6QNQJN2OQYCBNQGMFLO4QLZFNEHIFXOMTQVSUTWVTWT66TOFSC");
    assertEquals(account.getHomeDomain(), "stellar.org");
    assertFalse(account.getSponsor().isPresent());

    assertEquals(account.getThresholds().getLowThreshold(), 10);
    assertEquals(account.getThresholds().getMedThreshold(), 20);
    assertEquals(account.getThresholds().getHighThreshold(), 30);

    assertEquals(account.getFlags().getAuthRequired(), false);
    assertEquals(account.getFlags().getAuthRevocable(), true);
    assertEquals(account.getFlags().getAuthImmutable(), true);

    assertEquals(account.getBalances()[0].getAssetType(), "credit_alphanum4");
    assertEquals(account.getBalances()[0].getAssetCode(), Optional.of("ABC"));
    assertEquals(
        account.getBalances()[0].getAssetIssuer(),
        Optional.of("GCRA6COW27CY5MTKIA7POQ2326C5ABYCXODBN4TFF5VL4FMBRHOT3YHU"));
    assertEquals(
        account.getBalances()[0].getAsset(),
        Optional.of(
            new AssetTypeCreditAlphaNum4(
                "ABC", "GCRA6COW27CY5MTKIA7POQ2326C5ABYCXODBN4TFF5VL4FMBRHOT3YHU")));
    assertEquals(account.getBalances()[0].getBalance(), "1001.0000000");
    assertEquals(account.getBalances()[0].getLimit(), "12000.4775807");
    assertEquals(account.getBalances()[0].getBuyingLiabilities(), Optional.of("100.1234567"));
    assertEquals(account.getBalances()[0].getSellingLiabilities(), Optional.of("100.7654321"));
    assertFalse(account.getBalances()[0].getSponsor().isPresent());

    assertEquals(account.getBalances()[1].getAssetType(), "native");
    assertEquals(account.getBalances()[1].getAsset(), Optional.of(new AssetTypeNative()));
    assertEquals(account.getBalances()[1].getBalance(), "20.0000300");
    assertEquals(account.getBalances()[1].getBuyingLiabilities(), Optional.of("5.1234567"));
    assertEquals(account.getBalances()[1].getSellingLiabilities(), Optional.of("1.7654321"));
    assertEquals(account.getBalances()[1].getLimit(), null);
    assertFalse(account.getBalances()[1].getSponsor().isPresent());

    assertEquals(
        account.getSigners()[0].getAccountId(),
        "GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7");
    assertEquals(account.getSigners()[0].getWeight(), 0);
    assertEquals(account.getSigners()[0].getType(), "ed25519_public_key");
    assertFalse(account.getSigners()[0].getSponsor().isPresent());

    assertEquals(
        account.getSigners()[1].getKey(),
        "GCR2KBCIU6KQXSQY5F5GZYC4WLNHCHCKW4NEGXNEZRYWLTNZIRJJY7D2");
    assertEquals(account.getSigners()[1].getWeight(), 1);
    assertEquals(account.getSigners()[1].getType(), "ed25519_public_key");
    assertFalse(account.getSigners()[1].getSponsor().isPresent());

    assertEquals(account.getData().size(), 2);
    assertEquals(account.getData().get("entry1"), "dGVzdA==");
    assertTrue(Arrays.equals(account.getData().getDecoded("entry1"), "test".getBytes()));
    assertEquals(account.getData().get("entry2"), "dGVzdDI=");
    assertTrue(Arrays.equals(account.getData().getDecoded("entry2"), "test2".getBytes()));

    assertEquals(
        account.getLinks().getEffects().getHref(),
        "/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/effects{?cursor,limit,order}");
    assertEquals(
        account.getLinks().getOffers().getHref(),
        "/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/offers{?cursor,limit,order}");
    assertEquals(
        account.getLinks().getOperations().getHref(),
        "/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/operations{?cursor,limit,order}");
    assertEquals(
        account.getLinks().getSelf().getHref(),
        "/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7");
    assertEquals(
        account.getLinks().getTransactions().getHref(),
        "/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/transactions{?cursor,limit,order}");
  }

  @Test
  public void testDeserializeSponsor() {
    AccountResponse account =
        GsonSingleton.getInstance().fromJson(withSponsor, AccountResponse.class);
    assertEquals(
        account.getSponsor().get(), "GCA7RXNKN7FGBLJVETJCUUXGXTCR6L2SJQFXDGMQCDET5YUE6KFNHQHO");
    assertEquals(
        account.getBalances()[0].getSponsor().get(),
        "GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7");
    assertEquals(
        account.getSigners()[0].getSponsor().get(),
        "GCR2KBCIU6KQXSQY5F5GZYC4WLNHCHCKW4NEGXNEZRYWLTNZIRJJY7D2");
    assertEquals(account.getNumSponsored().intValue(), 3);
    assertEquals(account.getNumSponsoring().intValue(), 2);
  }

  @Test
  public void testDeserializeV9() {
    AccountResponse account = GsonSingleton.getInstance().fromJson(jsonV9, AccountResponse.class);

    assertEquals(account.getBalances()[0].getAssetType(), "credit_alphanum4");
    assertEquals(account.getBalances()[0].getAssetCode(), Optional.of("ABC"));
    assertEquals(
        account.getBalances()[0].getAssetIssuer(),
        Optional.of("GCRA6COW27CY5MTKIA7POQ2326C5ABYCXODBN4TFF5VL4FMBRHOT3YHU"));
    assertEquals(
        account.getBalances()[0].getAsset(),
        Optional.of(
            new AssetTypeCreditAlphaNum4(
                "ABC", "GCRA6COW27CY5MTKIA7POQ2326C5ABYCXODBN4TFF5VL4FMBRHOT3YHU")));
    assertEquals(account.getBalances()[0].getBalance(), "1001.0000000");
    assertEquals(account.getBalances()[0].getLimit(), "12000.4775807");
    assertEquals(account.getBalances()[0].getBuyingLiabilities(), Optional.absent());
    assertEquals(account.getBalances()[0].getSellingLiabilities(), Optional.absent());

    assertEquals(account.getBalances()[1].getAssetType(), "native");
    assertEquals(account.getBalances()[1].getAsset(), Optional.of(new AssetTypeNative()));
    assertEquals(account.getBalances()[1].getBalance(), "20.0000300");
    assertEquals(account.getBalances()[1].getBuyingLiabilities(), Optional.absent());
    assertEquals(account.getBalances()[1].getSellingLiabilities(), Optional.absent());
    assertEquals(account.getBalances()[1].getLimit(), null);
  }

  @Test
  public void testDeserializeLiquidityPoolBalanc() {
    AccountResponse account =
        GsonSingleton.getInstance().fromJson(jsonLiquidityPoolBalance, AccountResponse.class);

    assertEquals(account.getBalances()[0].getAssetType(), "liquidity_pool_shares");
    assertEquals(account.getBalances()[0].getAssetCode(), Optional.absent());
    assertEquals(account.getBalances()[0].getAssetIssuer(), Optional.<String>absent());
    assertEquals(account.getBalances()[0].getBalance(), "223.6067977");
    assertEquals(account.getBalances()[0].getLimit(), "10000.00000");
    assertEquals(account.getBalances()[0].getBuyingLiabilities(), Optional.absent());
    assertEquals(account.getBalances()[0].getSellingLiabilities(), Optional.absent());
    assertTrue(account.getBalances()[0].getLiquidityPoolID().isPresent());
    assertEquals(
        account.getBalances()[0].getLiquidityPoolID().get().toString(),
        "02449937ed825805b7a945bb6c027b53dfaf140983c1a1a64c42a81edd89b5e0");

    assertEquals(account.getBalances()[1].getAssetType(), "native");
    assertEquals(account.getBalances()[1].getBalance(), "20.0000300");
    assertEquals(account.getBalances()[1].getBuyingLiabilities(), Optional.absent());
    assertEquals(account.getBalances()[1].getSellingLiabilities(), Optional.absent());
    assertEquals(account.getBalances()[1].getLimit(), null);
  }

  String jsonAuthorizedToMaintainLiabilities =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"effects\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/effects{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"offers\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/offers{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"operations\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/operations{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"self\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\"\n"
          + "    },\n"
          + "    \"transactions\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/transactions{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    }\n"
          + "  },"
          + "  \"id\": \"GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\",\n"
          + "  \"paging_token\": \"1\",\n"
          + "  \"account_id\": \"GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\",\n"
          + "  \"sequence\": 2319149195853854,\n"
          + "  \"subentry_count\": 0,\n"
          + "  \"inflation_destination\": \"GAGRSA6QNQJN2OQYCBNQGMFLO4QLZFNEHIFXOMTQVSUTWVTWT66TOFSC\",\n"
          + "  \"home_domain\": \"stellar.org\",\n"
          + "  \"thresholds\": {\n"
          + "    \"low_threshold\": 10,\n"
          + "    \"med_threshold\": 20,\n"
          + "    \"high_threshold\": 30\n"
          + "  },\n"
          + "  \"flags\": {\n"
          + "    \"auth_required\": false,\n"
          + "    \"auth_revocable\": true,\n"
          + "    \"auth_immutable\": true\n"
          + "  },\n"
          + "  \"balances\": [\n"
          + "    {\n"
          + "      \"balance\": \"1001.0000000\",\n"
          + "      \"buying_liabilities\": \"100.1234567\",\n"
          + "      \"selling_liabilities\": \"100.7654321\",\n"
          + "      \"limit\": \"12000.4775807\",\n"
          + "      \"asset_type\": \"credit_alphanum4\",\n"
          + "      \"asset_code\": \"ABC\",\n"
          + "      \"asset_issuer\": \"GCRA6COW27CY5MTKIA7POQ2326C5ABYCXODBN4TFF5VL4FMBRHOT3YHU\",\n"
          + "      \"is_authorized\": false,\n"
          + "      \"is_authorized_to_maintain_liabilities\": true\n"
          + "    },"
          + "    {\n"
          + "      \"asset_type\": \"native\",\n"
          + "      \"balance\": \"20.0000300\",\n"
          + "      \"buying_liabilities\": \"5.1234567\",\n"
          + "      \"selling_liabilities\": \"1.7654321\"\n"
          + "    }\n"
          + "  ],\n"
          + "  \"signers\": [\n"
          + "    {\n"
          + "      \"public_key\": \"GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\",\n"
          + "      \"key\": \"GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\",\n"
          + "      \"weight\": 0,\n"
          + "      \"type\": \"ed25519_public_key\"\n"
          + "    },\n"
          + "    {\n"
          + "      \"public_key\": \"GCR2KBCIU6KQXSQY5F5GZYC4WLNHCHCKW4NEGXNEZRYWLTNZIRJJY7D2\",\n"
          + "      \"key\": \"GCR2KBCIU6KQXSQY5F5GZYC4WLNHCHCKW4NEGXNEZRYWLTNZIRJJY7D2\",\n"
          + "      \"weight\": 1,\n"
          + "      \"type\": \"ed25519_public_key\"\n"
          + "    }\n"
          + "  ],\n"
          + "  \"data\": {\n"
          + "    \"entry1\": \"dGVzdA==\",\n"
          + "    \"entry2\": \"dGVzdDI=\"\n"
          + "  }"
          + "}";

  String json =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"effects\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/effects{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"offers\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/offers{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"operations\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/operations{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"self\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\"\n"
          + "    },\n"
          + "    \"transactions\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/transactions{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    }\n"
          + "  },"
          + "  \"id\": \"GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\",\n"
          + "  \"paging_token\": \"1\",\n"
          + "  \"account_id\": \"GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\",\n"
          + "  \"sequence\": 2319149195853854,\n"
          + "  \"sequence_ledger\": 1234,\n"
          + "  \"sequence_time\": 4567,\n"
          + "  \"subentry_count\": 0,\n"
          + "  \"inflation_destination\": \"GAGRSA6QNQJN2OQYCBNQGMFLO4QLZFNEHIFXOMTQVSUTWVTWT66TOFSC\",\n"
          + "  \"home_domain\": \"stellar.org\",\n"
          + "  \"thresholds\": {\n"
          + "    \"low_threshold\": 10,\n"
          + "    \"med_threshold\": 20,\n"
          + "    \"high_threshold\": 30\n"
          + "  },\n"
          + "  \"flags\": {\n"
          + "    \"auth_required\": false,\n"
          + "    \"auth_revocable\": true,\n"
          + "    \"auth_immutable\": true,\n"
          + "    \"auth_clawback_enabled\": true\n"
          + "  },\n"
          + "  \"balances\": [\n"
          + "    {\n"
          + "      \"balance\": \"1001.0000000\",\n"
          + "      \"buying_liabilities\": \"100.1234567\",\n"
          + "      \"selling_liabilities\": \"100.7654321\",\n"
          + "      \"limit\": \"12000.4775807\",\n"
          + "      \"asset_type\": \"credit_alphanum4\",\n"
          + "      \"asset_code\": \"ABC\",\n"
          + "      \"asset_issuer\": \"GCRA6COW27CY5MTKIA7POQ2326C5ABYCXODBN4TFF5VL4FMBRHOT3YHU\"\n"
          + "    },"
          + "    {\n"
          + "      \"asset_type\": \"native\",\n"
          + "      \"balance\": \"20.0000300\",\n"
          + "      \"buying_liabilities\": \"5.1234567\",\n"
          + "      \"selling_liabilities\": \"1.7654321\"\n"
          + "    }\n"
          + "  ],\n"
          + "  \"signers\": [\n"
          + "    {\n"
          + "      \"public_key\": \"GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\",\n"
          + "      \"key\": \"GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\",\n"
          + "      \"weight\": 0,\n"
          + "      \"type\": \"ed25519_public_key\"\n"
          + "    },\n"
          + "    {\n"
          + "      \"public_key\": \"GCR2KBCIU6KQXSQY5F5GZYC4WLNHCHCKW4NEGXNEZRYWLTNZIRJJY7D2\",\n"
          + "      \"key\": \"GCR2KBCIU6KQXSQY5F5GZYC4WLNHCHCKW4NEGXNEZRYWLTNZIRJJY7D2\",\n"
          + "      \"weight\": 1,\n"
          + "      \"type\": \"ed25519_public_key\"\n"
          + "    }\n"
          + "  ],\n"
          + "  \"data\": {\n"
          + "    \"entry1\": \"dGVzdA==\",\n"
          + "    \"entry2\": \"dGVzdDI=\"\n"
          + "  }"
          + "}";

  String jsonV9 =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"effects\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/effects{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"offers\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/offers{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"operations\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/operations{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"self\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\"\n"
          + "    },\n"
          + "    \"transactions\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/transactions{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    }\n"
          + "  },"
          + "  \"id\": \"GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\",\n"
          + "  \"paging_token\": \"1\",\n"
          + "  \"account_id\": \"GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\",\n"
          + "  \"sequence\": 2319149195853854,\n"
          + "  \"subentry_count\": 0,\n"
          + "  \"inflation_destination\": \"GAGRSA6QNQJN2OQYCBNQGMFLO4QLZFNEHIFXOMTQVSUTWVTWT66TOFSC\",\n"
          + "  \"home_domain\": \"stellar.org\",\n"
          + "  \"thresholds\": {\n"
          + "    \"low_threshold\": 10,\n"
          + "    \"med_threshold\": 20,\n"
          + "    \"high_threshold\": 30\n"
          + "  },\n"
          + "  \"flags\": {\n"
          + "    \"auth_required\": false,\n"
          + "    \"auth_revocable\": true\n"
          + "  },\n"
          + "  \"balances\": [\n"
          + "    {\n"
          + "      \"balance\": \"1001.0000000\",\n"
          + "      \"limit\": \"12000.4775807\",\n"
          + "      \"asset_type\": \"credit_alphanum4\",\n"
          + "      \"asset_code\": \"ABC\",\n"
          + "      \"asset_issuer\": \"GCRA6COW27CY5MTKIA7POQ2326C5ABYCXODBN4TFF5VL4FMBRHOT3YHU\"\n"
          + "    },"
          + "    {\n"
          + "      \"asset_type\": \"native\",\n"
          + "      \"balance\": \"20.0000300\"\n"
          + "    }\n"
          + "  ],\n"
          + "  \"signers\": [\n"
          + "    {\n"
          + "      \"public_key\": \"GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\",\n"
          + "      \"weight\": 0\n"
          + "    },\n"
          + "    {\n"
          + "      \"public_key\": \"GCR2KBCIU6KQXSQY5F5GZYC4WLNHCHCKW4NEGXNEZRYWLTNZIRJJY7D2\",\n"
          + "      \"weight\": 1\n"
          + "    }\n"
          + "  ]\n"
          + "}";

  String jsonLiquidityPoolBalance =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"effects\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/effects{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"offers\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/offers{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"operations\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/operations{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"self\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\"\n"
          + "    },\n"
          + "    \"transactions\": {\n"
          + "      \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7/transactions{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    }\n"
          + "  },"
          + "  \"id\": \"GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\",\n"
          + "  \"paging_token\": \"1\",\n"
          + "  \"account_id\": \"GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\",\n"
          + "  \"sequence\": 2319149195853854,\n"
          + "  \"subentry_count\": 0,\n"
          + "  \"home_domain\": \"stellar.org\",\n"
          + "  \"thresholds\": {\n"
          + "    \"low_threshold\": 0,\n"
          + "    \"med_threshold\": 0,\n"
          + "    \"high_threshold\": 0\n"
          + "  },\n"
          + "  \"flags\": {\n"
          + "    \"auth_required\": false,\n"
          + "    \"auth_revocable\": false\n"
          + "  },\n"
          + "  \"balances\": [\n"
          + " {\n"
          + "      \"balance\": \"223.6067977\",\n"
          + "      \"liquidity_pool_id\": \"02449937ed825805b7a945bb6c027b53dfaf140983c1a1a64c42a81edd89b5e0\",\n"
          + "      \"limit\": \"10000.00000\",\n"
          + "      \"last_modified_ledger\": 799014,\n"
          + "      \"is_authorized\": false,\n"
          + "      \"is_authorized_to_maintain_liabilities\": false,\n"
          + "      \"asset_type\": \"liquidity_pool_shares\"\n"
          + "    },\n"
          + "    {\n"
          + "      \"asset_type\": \"native\",\n"
          + "      \"balance\": \"20.0000300\"\n"
          + "    }\n"
          + "  ],\n"
          + "  \"signers\": [\n"
          + "    {\n"
          + "      \"public_key\": \"GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\",\n"
          + "      \"weight\": 0\n"
          + "    },\n"
          + "    {\n"
          + "      \"public_key\": \"GCR2KBCIU6KQXSQY5F5GZYC4WLNHCHCKW4NEGXNEZRYWLTNZIRJJY7D2\",\n"
          + "      \"weight\": 1\n"
          + "    }\n"
          + "  ]\n"
          + "}";

  String withSponsor =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"self\": {\n"
          + "      \"href\": \"https://horizon-protocol14.stellar.org/accounts/GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO\"\n"
          + "    },\n"
          + "    \"transactions\": {\n"
          + "      \"href\": \"https://horizon-protocol14.stellar.org/accounts/GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO/transactions{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"operations\": {\n"
          + "      \"href\": \"https://horizon-protocol14.stellar.org/accounts/GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO/operations{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"payments\": {\n"
          + "      \"href\": \"https://horizon-protocol14.stellar.org/accounts/GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO/payments{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"effects\": {\n"
          + "      \"href\": \"https://horizon-protocol14.stellar.org/accounts/GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO/effects{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"offers\": {\n"
          + "      \"href\": \"https://horizon-protocol14.stellar.org/accounts/GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO/offers{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"trades\": {\n"
          + "      \"href\": \"https://horizon-protocol14.stellar.org/accounts/GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO/trades{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"data\": {\n"
          + "      \"href\": \"https://horizon-protocol14.stellar.org/accounts/GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO/data/{key}\",\n"
          + "      \"templated\": true\n"
          + "    }\n"
          + "  },\n"
          + "  \"id\": \"GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO\",\n"
          + "  \"account_id\": \"GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO\",\n"
          + "  \"sequence\": \"504211980681218\",\n"
          + "  \"subentry_count\": 1,\n"
          + "  \"last_modified_ledger\": 117663,\n"
          + "  \"last_modified_time\": \"2020-09-28T17:56:04Z\",\n"
          + "  \"thresholds\": {\n"
          + "    \"low_threshold\": 0,\n"
          + "    \"med_threshold\": 0,\n"
          + "    \"high_threshold\": 0\n"
          + "  },\n"
          + "  \"flags\": {\n"
          + "    \"auth_required\": false,\n"
          + "    \"auth_revocable\": false,\n"
          + "    \"auth_immutable\": false\n"
          + "  },\n"
          + "  \"balances\": [\n"
          + "    {\n"
          + "      \"sponsor\": \"GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\",\n"
          + "      \"balance\": \"9999.9999800\",\n"
          + "      \"buying_liabilities\": \"0.0000000\",\n"
          + "      \"selling_liabilities\": \"0.0000000\",\n"
          + "      \"asset_type\": \"native\"\n"
          + "    }\n"
          + "  ],\n"
          + "  \"signers\": [\n"
          + "    {\n"
          + "      \"sponsor\": \"GCR2KBCIU6KQXSQY5F5GZYC4WLNHCHCKW4NEGXNEZRYWLTNZIRJJY7D2\",\n"
          + "      \"weight\": 1,\n"
          + "      \"key\": \"GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO\",\n"
          + "      \"type\": \"ed25519_public_key\"\n"
          + "    }\n"
          + "  ],\n"
          + "  \"data\": {\n"
          + "    \"welcome-friend\": \"aHR0cHM6Ly93d3cueW91dHViZS5jb20vd2F0Y2g/dj1kUXc0dzlXZ1hjUQ==\"\n"
          + "  },\n"
          + "  \"num_sponsoring\": 2,\n"
          + "  \"num_sponsored\": 3,\n"
          + "  \"sponsor\": \"GCA7RXNKN7FGBLJVETJCUUXGXTCR6L2SJQFXDGMQCDET5YUE6KFNHQHO\",\n"
          + "  \"paging_token\": \"GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO\"\n"
          + "}";
}
