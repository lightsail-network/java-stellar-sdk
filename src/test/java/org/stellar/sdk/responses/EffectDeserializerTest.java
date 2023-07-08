package org.stellar.sdk.responses;

import static org.stellar.sdk.Asset.create;

import java.util.Arrays;
import junit.framework.TestCase;
import org.junit.Test;
import org.stellar.sdk.AssetAmount;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.Predicate;
import org.stellar.sdk.responses.effects.AccountCreatedEffectResponse;
import org.stellar.sdk.responses.effects.AccountCreditedEffectResponse;
import org.stellar.sdk.responses.effects.AccountDebitedEffectResponse;
import org.stellar.sdk.responses.effects.AccountFlagsUpdatedEffectResponse;
import org.stellar.sdk.responses.effects.AccountHomeDomainUpdatedEffectResponse;
import org.stellar.sdk.responses.effects.AccountInflationDestinationUpdatedEffectResponse;
import org.stellar.sdk.responses.effects.AccountRemovedEffectResponse;
import org.stellar.sdk.responses.effects.AccountThresholdsUpdatedEffectResponse;
import org.stellar.sdk.responses.effects.ClaimableBalanceClaimantCreatedEffectResponse;
import org.stellar.sdk.responses.effects.ClaimableBalanceClawedBackEffectResponse;
import org.stellar.sdk.responses.effects.DataCreatedEffectResponse;
import org.stellar.sdk.responses.effects.DataRemovedEffectResponse;
import org.stellar.sdk.responses.effects.DataUpdatedEffectResponse;
import org.stellar.sdk.responses.effects.EffectResponse;
import org.stellar.sdk.responses.effects.LiquidityPoolTradeEffectResponse;
import org.stellar.sdk.responses.effects.SequenceBumpedEffectResponse;
import org.stellar.sdk.responses.effects.SignerCreatedEffectResponse;
import org.stellar.sdk.responses.effects.SignerRemovedEffectResponse;
import org.stellar.sdk.responses.effects.SignerUpdatedEffectResponse;
import org.stellar.sdk.responses.effects.TradeEffectResponse;
import org.stellar.sdk.responses.effects.TrustlineAuthorizedEffectResponse;
import org.stellar.sdk.responses.effects.TrustlineAuthorizedToMaintainLiabilitiesEffectResponse;
import org.stellar.sdk.responses.effects.TrustlineCreatedEffectResponse;
import org.stellar.sdk.responses.effects.TrustlineDeauthorizedEffectResponse;
import org.stellar.sdk.responses.effects.TrustlineFlagsUpdatedEffectResponse;
import org.stellar.sdk.responses.effects.TrustlineRemovedEffectResponse;
import org.stellar.sdk.responses.effects.TrustlineUpdatedEffectResponse;
import org.stellar.sdk.xdr.LiquidityPoolType;

public class EffectDeserializerTest extends TestCase {
  @Test
  public void testDeserializeAccountCreatedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/65571265847297\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=65571265847297-1\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=65571265847297-1\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000065571265847297-0000000001\",\n"
            + "        \"paging_token\": \"65571265847297-1\",\n"
            + "        \"account\": \"GCBQ6JRBPF3SXQBQ6SO5MRBE7WVV4UCHYOSHQGXSZNPZLFRYVYOWBZRQ\",\n"
            + "        \"type\": \"account_created\",\n"
            + "        \"type_i\": 0,\n"
            + "        \"starting_balance\": \"30.0\"\n"
            + "      }";

    AccountCreatedEffectResponse effect =
        (AccountCreatedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GCBQ6JRBPF3SXQBQ6SO5MRBE7WVV4UCHYOSHQGXSZNPZLFRYVYOWBZRQ");
    assertEquals(effect.getStartingBalance(), "30.0");
    assertEquals(effect.getPagingToken(), "65571265847297-1");

    assertEquals(
        effect.getLinks().getOperation().getHref(),
        "http://horizon-testnet.stellar.org/operations/65571265847297");
    assertEquals(
        effect.getLinks().getSucceeds().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=desc&cursor=65571265847297-1");
    assertEquals(
        effect.getLinks().getPrecedes().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=asc&cursor=65571265847297-1");
  }

  @Test
  public void testDeserializeAccountRemovedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/65571265847297\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=65571265847297-1\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=65571265847297-1\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000065571265847297-0000000001\",\n"
            + "        \"paging_token\": \"65571265847297-1\",\n"
            + "        \"account\": \"GCBQ6JRBPF3SXQBQ6SO5MRBE7WVV4UCHYOSHQGXSZNPZLFRYVYOWBZRQ\",\n"
            + "        \"type\": \"account_removed\",\n"
            + "        \"type_i\": 1\n"
            + "      }";

    AccountRemovedEffectResponse effect =
        (AccountRemovedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GCBQ6JRBPF3SXQBQ6SO5MRBE7WVV4UCHYOSHQGXSZNPZLFRYVYOWBZRQ");

    assertEquals(
        effect.getLinks().getOperation().getHref(),
        "http://horizon-testnet.stellar.org/operations/65571265847297");
    assertEquals(
        effect.getLinks().getSucceeds().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=desc&cursor=65571265847297-1");
    assertEquals(
        effect.getLinks().getPrecedes().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=asc&cursor=65571265847297-1");
  }

  @Test
  public void testDeserializeAccountCreditedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/13563506724865\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=13563506724865-1\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=13563506724865-1\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000013563506724865-0000000001\",\n"
            + "        \"paging_token\": \"13563506724865-1\",\n"
            + "        \"account\": \"GDLGTRIBFH24364GPWPUS45GUFC2GU4ARPGWTXVCPLGTUHX3IOS3ON47\",\n"
            + "        \"type\": \"account_credited\",\n"
            + "        \"type_i\": 2,\n"
            + "        \"asset_type\": \"native\",\n"
            + "        \"amount\": \"1000.0\"\n"
            + "      }";

    AccountCreditedEffectResponse effect =
        (AccountCreditedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GDLGTRIBFH24364GPWPUS45GUFC2GU4ARPGWTXVCPLGTUHX3IOS3ON47");
    TestCase.assertEquals(effect.getAsset(), new AssetTypeNative());
    assertEquals(effect.getAmount(), "1000.0");

    assertEquals(
        effect.getLinks().getOperation().getHref(),
        "http://horizon-testnet.stellar.org/operations/13563506724865");
    assertEquals(
        effect.getLinks().getSucceeds().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=desc&cursor=13563506724865-1");
    assertEquals(
        effect.getLinks().getPrecedes().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=asc&cursor=13563506724865-1");
  }

  @Test
  public void testDeserializeAccountDebitedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/65571265843201\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=65571265843201-2\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=65571265843201-2\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000065571265843201-0000000002\",\n"
            + "        \"paging_token\": \"65571265843201-2\",\n"
            + "        \"account\": \"GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H\",\n"
            + "        \"type\": \"account_debited\",\n"
            + "        \"type_i\": 3,\n"
            + "        \"asset_type\": \"native\",\n"
            + "        \"amount\": \"30.0\"\n"
            + "      }";

    AccountDebitedEffectResponse effect =
        (AccountDebitedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H");
    assertEquals(effect.getAsset(), new AssetTypeNative());
    assertEquals(effect.getAmount(), "30.0");

    assertEquals(
        effect.getLinks().getOperation().getHref(),
        "http://horizon-testnet.stellar.org/operations/65571265843201");
    assertEquals(
        effect.getLinks().getSucceeds().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=desc&cursor=65571265843201-2");
    assertEquals(
        effect.getLinks().getPrecedes().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=asc&cursor=65571265843201-2");
  }

  @Test
  public void testDeserializeAccountThresholdsUpdatedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/18970870550529\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=18970870550529-1\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=18970870550529-1\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000018970870550529-0000000001\",\n"
            + "        \"paging_token\": \"18970870550529-1\",\n"
            + "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n"
            + "        \"low_threshold\": 2,\n"
            + "        \"med_threshold\": 3,\n"
            + "        \"high_threshold\": 4,\n"
            + "        \"type\": \"account_thresholds_updated\",\n"
            + "        \"type_i\": 4\n"
            + "      }";

    AccountThresholdsUpdatedEffectResponse effect =
        (AccountThresholdsUpdatedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    assertEquals(effect.getLowThreshold(), new Integer(2));
    assertEquals(effect.getMedThreshold(), new Integer(3));
    assertEquals(effect.getHighThreshold(), new Integer(4));

    assertEquals(
        effect.getLinks().getOperation().getHref(),
        "http://horizon-testnet.stellar.org/operations/18970870550529");
    assertEquals(
        effect.getLinks().getSucceeds().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=desc&cursor=18970870550529-1");
    assertEquals(
        effect.getLinks().getPrecedes().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=asc&cursor=18970870550529-1");
  }

  @Test
  public void testDeserializeAccountHomeDomainUpdatedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/18970870550529\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=18970870550529-1\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=18970870550529-1\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000018970870550529-0000000001\",\n"
            + "        \"paging_token\": \"18970870550529-1\",\n"
            + "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n"
            + "        \"type\": \"account_home_domain_updated\",\n"
            + "        \"type_i\": 5,\n"
            + "        \"home_domain\": \"stellar.org\"\n"
            + "      }";

    AccountHomeDomainUpdatedEffectResponse effect =
        (AccountHomeDomainUpdatedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    assertEquals(effect.getHomeDomain(), "stellar.org");

    assertEquals(
        effect.getLinks().getOperation().getHref(),
        "http://horizon-testnet.stellar.org/operations/18970870550529");
    assertEquals(
        effect.getLinks().getSucceeds().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=desc&cursor=18970870550529-1");
    assertEquals(
        effect.getLinks().getPrecedes().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=asc&cursor=18970870550529-1");
  }

  @Test
  public void testDeserializeAccountFlagsUpdatedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/18970870550529\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=18970870550529-1\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=18970870550529-1\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000018970870550529-0000000001\",\n"
            + "        \"paging_token\": \"18970870550529-1\",\n"
            + "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n"
            + "        \"type\": \"account_flags_updated\",\n"
            + "        \"type_i\": 6,\n"
            + "        \"auth_required_flag\": false,\n"
            + "        \"auth_revokable_flag\": true\n"
            + "      }";

    AccountFlagsUpdatedEffectResponse effect =
        (AccountFlagsUpdatedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    assertEquals(effect.getAuthRequiredFlag(), new Boolean(false));
    assertEquals(effect.getAuthRevokableFlag(), new Boolean(true));

    assertEquals(
        effect.getLinks().getOperation().getHref(),
        "http://horizon-testnet.stellar.org/operations/18970870550529");
    assertEquals(
        effect.getLinks().getSucceeds().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=desc&cursor=18970870550529-1");
    assertEquals(
        effect.getLinks().getPrecedes().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=asc&cursor=18970870550529-1");
  }

  @Test
  public void testDeserializeClaimableBalanceClaimantCreatedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"https://horizon.stellar.org/operations/158104892991700993\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"https://horizon.stellar.org/effects?order=desc&cursor=158104892991700993-2\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"https://horizon.stellar.org/effects?order=asc&cursor=158104892991700993-2\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0158104892991700993-0000000002\",\n"
            + "        \"paging_token\": \"158104892991700993-2\",\n"
            + "        \"account\": \"GBQECQVAS2FJ7DLCUXDASZAJQLWPXNTCR2DGBPKQDO3QS66TJXLRHFIK\",\n"
            + "        \"type\": \"claimable_balance_claimant_created\",\n"
            + "        \"type_i\": 51,\n"
            + "        \"created_at\": \"2021-08-11T16:16:32Z\",\n"
            + "        \"asset\": \"KES:GA2MSSZKJOU6RNL3EJKH3S5TB5CDYTFQFWRYFGUJVIN5I6AOIRTLUHTO\",\n"
            + "        \"balance_id\": \"0000000071d3336fa6b6cf81fcbeda85a503ccfabc786ab1066594716f3f9551ea4b89ca\",\n"
            + "        \"amount\": \"0.0012200\",\n"
            + "        \"predicate\": {\n"
            + "          \"abs_before\": \"+39121901036-03-29T15:30:22Z\",\n"
            + "          \"abs_before_epoch\": \"1234567890982222222\"\n"
            + "        }\n"
            + "      }\n";

    ClaimableBalanceClaimantCreatedEffectResponse effect =
        (ClaimableBalanceClaimantCreatedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GBQECQVAS2FJ7DLCUXDASZAJQLWPXNTCR2DGBPKQDO3QS66TJXLRHFIK");
    assertEquals(effect.getCreatedAt(), "2021-08-11T16:16:32Z");
    assertEquals(
        effect.getBalanceId(),
        "0000000071d3336fa6b6cf81fcbeda85a503ccfabc786ab1066594716f3f9551ea4b89ca");
    assertEquals(effect.getType(), "claimable_balance_claimant_created");
    assertSame(effect.getPredicate().getClass(), Predicate.AbsBefore.class);
    assertEquals(
        ((Predicate.AbsBefore) effect.getPredicate()).getTimestampSeconds(), 1234567890982222222L);
  }

  @Test
  public void testBackwardsCompatAbsBeforeEpoch() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"https://horizon.stellar.org/operations/158104892991700993\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"https://horizon.stellar.org/effects?order=desc&cursor=158104892991700993-2\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"https://horizon.stellar.org/effects?order=asc&cursor=158104892991700993-2\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0158104892991700993-0000000002\",\n"
            + "        \"paging_token\": \"158104892991700993-2\",\n"
            + "        \"account\": \"GBQECQVAS2FJ7DLCUXDASZAJQLWPXNTCR2DGBPKQDO3QS66TJXLRHFIK\",\n"
            + "        \"type\": \"claimable_balance_claimant_created\",\n"
            + "        \"type_i\": 51,\n"
            + "        \"created_at\": \"2021-08-11T16:16:32Z\",\n"
            + "        \"asset\": \"KES:GA2MSSZKJOU6RNL3EJKH3S5TB5CDYTFQFWRYFGUJVIN5I6AOIRTLUHTO\",\n"
            + "        \"balance_id\": \"0000000071d3336fa6b6cf81fcbeda85a503ccfabc786ab1066594716f3f9551ea4b89ca\",\n"
            + "        \"amount\": \"0.0012200\",\n"
            + "        \"predicate\": {\n"
            + "          \"abs_before\": \"2021-11-21T07:24:10Z\"\n"
            + "        }\n"
            + "      }\n";

    ClaimableBalanceClaimantCreatedEffectResponse effect =
        (ClaimableBalanceClaimantCreatedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GBQECQVAS2FJ7DLCUXDASZAJQLWPXNTCR2DGBPKQDO3QS66TJXLRHFIK");
    assertEquals(effect.getCreatedAt(), "2021-08-11T16:16:32Z");
    assertEquals(
        effect.getBalanceId(),
        "0000000071d3336fa6b6cf81fcbeda85a503ccfabc786ab1066594716f3f9551ea4b89ca");
    assertEquals(effect.getType(), "claimable_balance_claimant_created");
    assertSame(effect.getPredicate().getClass(), Predicate.AbsBefore.class);
    assertEquals(((Predicate.AbsBefore) effect.getPredicate()).getTimestampSeconds(), 1637479450L);
  }

  @Test
  public void testDeserializeAccountInflationDestinationUpdatedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/operations/40181321724596225\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=desc&cursor=40181321724596225-1\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=asc&cursor=40181321724596225-1\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0040181321724596225-0000000001\",\n"
            + "        \"paging_token\": \"40181321724596225-1\",\n"
            + "        \"account\": \"GDPFGP4IPE5DXG6XRXC4ZBUI43PAGRQ5VVNJ3LJTBXDBZ4ITO6HBHNSF\",\n"
            + "        \"type\": \"account_inflation_destination_updated\",\n"
            + "        \"type_i\": 7,\n"
            + "        \"created_at\": \"2018-06-06T10:20:50Z\"\n"
            + "      }";

    AccountInflationDestinationUpdatedEffectResponse effect =
        (AccountInflationDestinationUpdatedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GDPFGP4IPE5DXG6XRXC4ZBUI43PAGRQ5VVNJ3LJTBXDBZ4ITO6HBHNSF");
    assertEquals(effect.getCreatedAt(), "2018-06-06T10:20:50Z");
  }

  @Test
  public void testDeserializeSignerCreatedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/65571265859585\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=65571265859585-3\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=65571265859585-3\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000065571265859585-0000000003\",\n"
            + "        \"paging_token\": \"65571265859585-3\",\n"
            + "        \"account\": \"GB24LPGAHYTWRYOXIDKXLI55SBRWW42T3TZKDAAW3BOJX4ADVIATFTLU\",\n"
            + "        \"type\": \"signer_created\",\n"
            + "        \"type_i\": 10,\n"
            + "        \"weight\": 1,\n"
            + "        \"public_key\": \"GB24LPGAHYTWRYOXIDKXLI55SBRWW42T3TZKDAAW3BOJX4ADVIATFTLU\"\n"
            + "      }";

    SignerCreatedEffectResponse effect =
        (SignerCreatedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GB24LPGAHYTWRYOXIDKXLI55SBRWW42T3TZKDAAW3BOJX4ADVIATFTLU");
    assertEquals(effect.getWeight(), new Integer(1));
    assertEquals(effect.getPublicKey(), "GB24LPGAHYTWRYOXIDKXLI55SBRWW42T3TZKDAAW3BOJX4ADVIATFTLU");

    assertEquals(
        effect.getLinks().getOperation().getHref(),
        "http://horizon-testnet.stellar.org/operations/65571265859585");
    assertEquals(
        effect.getLinks().getSucceeds().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=desc&cursor=65571265859585-3");
    assertEquals(
        effect.getLinks().getPrecedes().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=asc&cursor=65571265859585-3");
  }

  @Test
  public void testDeserializeSignerRemovedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/43658342567940\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=43658342567940-2\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=43658342567940-2\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000043658342567940-0000000002\",\n"
            + "        \"paging_token\": \"43658342567940-2\",\n"
            + "        \"account\": \"GCFKT6BN2FEASCEVDNHEC4LLFT2KLUUPEMKM4OJPEJ65H2AEZ7IH4RV6\",\n"
            + "        \"type\": \"signer_removed\",\n"
            + "        \"type_i\": 11,\n"
            + "        \"weight\": 0,\n"
            + "        \"public_key\": \"GCFKT6BN2FEASCEVDNHEC4LLFT2KLUUPEMKM4OJPEJ65H2AEZ7IH4RV6\"\n"
            + "      }";

    SignerRemovedEffectResponse effect =
        (SignerRemovedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GCFKT6BN2FEASCEVDNHEC4LLFT2KLUUPEMKM4OJPEJ65H2AEZ7IH4RV6");
    assertEquals(effect.getWeight(), new Integer(0));
    assertEquals(effect.getPublicKey(), "GCFKT6BN2FEASCEVDNHEC4LLFT2KLUUPEMKM4OJPEJ65H2AEZ7IH4RV6");

    assertEquals(
        effect.getLinks().getOperation().getHref(),
        "http://horizon-testnet.stellar.org/operations/43658342567940");
    assertEquals(
        effect.getLinks().getSucceeds().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=desc&cursor=43658342567940-2");
    assertEquals(
        effect.getLinks().getPrecedes().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=asc&cursor=43658342567940-2");
  }

  @Test
  public void testDeserializeSignerUpdatedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/33788507721730\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=33788507721730-2\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=33788507721730-2\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000033788507721730-0000000002\",\n"
            + "        \"paging_token\": \"33788507721730-2\",\n"
            + "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n"
            + "        \"type\": \"signer_updated\",\n"
            + "        \"type_i\": 12,\n"
            + "        \"weight\": 2,\n"
            + "        \"public_key\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\"\n"
            + "      }";

    SignerUpdatedEffectResponse effect =
        (SignerUpdatedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    assertEquals(effect.getWeight(), new Integer(2));
    assertEquals(effect.getPublicKey(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");

    assertEquals(
        effect.getLinks().getOperation().getHref(),
        "http://horizon-testnet.stellar.org/operations/33788507721730");
    assertEquals(
        effect.getLinks().getSucceeds().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=desc&cursor=33788507721730-2");
    assertEquals(
        effect.getLinks().getPrecedes().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=asc&cursor=33788507721730-2");
  }

  @Test
  public void testDeserializeTrustlineCreatedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/33788507721730\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=33788507721730-2\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=33788507721730-2\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000033788507721730-0000000002\",\n"
            + "        \"paging_token\": \"33788507721730-2\",\n"
            + "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n"
            + "        \"type\": \"trustline_created\",\n"
            + "        \"type_i\": 20,\n"
            + "        \"asset_type\": \"credit_alphanum4\",\n"
            + "        \"asset_code\": \"EUR\",\n"
            + "        \"asset_issuer\": \"GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA\",\n"
            + "        \"limit\": \"1000.0\"\n"
            + "      }";

    TrustlineCreatedEffectResponse effect =
        (TrustlineCreatedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    TestCase.assertEquals(
        effect.getAsset(),
        create(null, "EUR", "GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA"));
    assertEquals(effect.getLimit(), "1000.0");

    assertEquals(
        effect.getLinks().getOperation().getHref(),
        "http://horizon-testnet.stellar.org/operations/33788507721730");
    assertEquals(
        effect.getLinks().getSucceeds().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=desc&cursor=33788507721730-2");
    assertEquals(
        effect.getLinks().getPrecedes().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=asc&cursor=33788507721730-2");
  }

  @Test
  public void testDeserializeLiquidityPoolTrustlineCreatedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/33788507721730\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=33788507721730-2\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=33788507721730-2\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000033788507721730-0000000002\",\n"
            + "        \"paging_token\": \"33788507721730-2\",\n"
            + "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n"
            + "        \"type\": \"trustline_created\",\n"
            + "        \"type_i\": 20,\n"
            + "        \"asset_type\": \"liquidity_pool_shares\",\n"
            + "        \"liquidity_pool_id\": \"02449937ed825805b7a945bb6c027b53dfaf140983c1a1a64c42a81edd89b5e0\",\n"
            + "        \"limit\": \"1000.0\"\n"
            + "      }";

    TrustlineCreatedEffectResponse effect =
        (TrustlineCreatedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);
    TestCase.assertEquals(
        effect.getAsset(),
        create(
            "liquidity_pool_shares",
            null,
            null,
            "02449937ed825805b7a945bb6c027b53dfaf140983c1a1a64c42a81edd89b5e0"));
  }

  @Test
  public void testDeserializeTrustlineRemovedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/33788507721730\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=33788507721730-2\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=33788507721730-2\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000033788507721730-0000000002\",\n"
            + "        \"paging_token\": \"33788507721730-2\",\n"
            + "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n"
            + "        \"type\": \"trustline_removed\",\n"
            + "        \"type_i\": 21,\n"
            + "        \"asset_type\": \"credit_alphanum4\",\n"
            + "        \"asset_code\": \"EUR\",\n"
            + "        \"asset_issuer\": \"GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA\",\n"
            + "        \"limit\": \"0.0\"\n"
            + "      }";

    TrustlineRemovedEffectResponse effect =
        (TrustlineRemovedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    assertEquals(
        effect.getAsset(),
        create(null, "EUR", "GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA"));
    assertEquals(effect.getLimit(), "0.0");

    assertEquals(
        effect.getLinks().getOperation().getHref(),
        "http://horizon-testnet.stellar.org/operations/33788507721730");
    assertEquals(
        effect.getLinks().getSucceeds().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=desc&cursor=33788507721730-2");
    assertEquals(
        effect.getLinks().getPrecedes().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=asc&cursor=33788507721730-2");
  }

  @Test
  public void testDeserializeLiquidityPoolTrustlineRemovedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/33788507721730\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=33788507721730-2\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=33788507721730-2\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000033788507721730-0000000002\",\n"
            + "        \"paging_token\": \"33788507721730-2\",\n"
            + "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n"
            + "        \"type\": \"trustline_removed\",\n"
            + "        \"type_i\": 21,\n"
            + "        \"asset_type\": \"liquidity_pool_shares\",\n"
            + "        \"liquidity_pool_id\": \"02449937ed825805b7a945bb6c027b53dfaf140983c1a1a64c42a81edd89b5e0\",\n"
            + "        \"limit\": \"0.0\"\n"
            + "      }";

    TrustlineRemovedEffectResponse effect =
        (TrustlineRemovedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);
    TestCase.assertEquals(
        effect.getAsset(),
        create(
            "liquidity_pool_shares",
            null,
            null,
            "02449937ed825805b7a945bb6c027b53dfaf140983c1a1a64c42a81edd89b5e0"));
  }

  @Test
  public void testDeserializeTrustlineUpdatedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/33788507721730\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=33788507721730-2\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=33788507721730-2\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000033788507721730-0000000002\",\n"
            + "        \"paging_token\": \"33788507721730-2\",\n"
            + "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n"
            + "        \"type\": \"trustline_updated\",\n"
            + "        \"type_i\": 22,\n"
            + "        \"asset_type\": \"credit_alphanum12\",\n"
            + "        \"asset_code\": \"TESTTEST\",\n"
            + "        \"asset_issuer\": \"GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA\",\n"
            + "        \"limit\": \"100.0\"\n"
            + "      }";

    TrustlineUpdatedEffectResponse effect =
        (TrustlineUpdatedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    assertEquals(
        effect.getAsset(),
        create(null, "TESTTEST", "GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA"));
    assertEquals(effect.getLimit(), "100.0");

    assertEquals(
        effect.getLinks().getOperation().getHref(),
        "http://horizon-testnet.stellar.org/operations/33788507721730");
    assertEquals(
        effect.getLinks().getSucceeds().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=desc&cursor=33788507721730-2");
    assertEquals(
        effect.getLinks().getPrecedes().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=asc&cursor=33788507721730-2");
  }

  @Test
  public void testDeserializeLiquidityPoolTrustlineUpdatedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/33788507721730\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=33788507721730-2\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=33788507721730-2\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000033788507721730-0000000002\",\n"
            + "        \"paging_token\": \"33788507721730-2\",\n"
            + "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n"
            + "        \"type\": \"trustline_updated\",\n"
            + "        \"type_i\": 22,\n"
            + "        \"asset_type\": \"liquidity_pool_shares\",\n"
            + "        \"liquidity_pool_id\": \"02449937ed825805b7a945bb6c027b53dfaf140983c1a1a64c42a81edd89b5e0\",\n"
            + "        \"limit\": \"100.0\"\n"
            + "      }";

    TrustlineUpdatedEffectResponse effect =
        (TrustlineUpdatedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);
    TestCase.assertEquals(
        effect.getAsset(),
        create(
            "liquidity_pool_shares",
            null,
            null,
            "02449937ed825805b7a945bb6c027b53dfaf140983c1a1a64c42a81edd89b5e0"));
  }

  @Test
  public void testDeserializeTrustlineAuthorizedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/33788507721730\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=33788507721730-2\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=33788507721730-2\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000033788507721730-0000000002\",\n"
            + "        \"paging_token\": \"33788507721730-2\",\n"
            + "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n"
            + "        \"type\": \"trustline_authorized\",\n"
            + "        \"type_i\": 23,\n"
            + "        \"asset_type\": \"credit_alphanum12\",\n"
            + "        \"asset_code\": \"TESTTEST\",\n"
            + "        \"trustor\": \"GB3E4AB4VWXJDUVN4Z3CPBU5HTMWVEQXONZYVDFMHQD6333KHCOL3UBR\"\n"
            + "      }";

    TrustlineAuthorizedEffectResponse effect =
        (TrustlineAuthorizedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    assertEquals(effect.getAssetType(), "credit_alphanum12");
    assertEquals(effect.getAssetCode(), "TESTTEST");
    assertEquals(effect.getTrustor(), "GB3E4AB4VWXJDUVN4Z3CPBU5HTMWVEQXONZYVDFMHQD6333KHCOL3UBR");

    assertEquals(
        effect.getLinks().getOperation().getHref(),
        "http://horizon-testnet.stellar.org/operations/33788507721730");
    assertEquals(
        effect.getLinks().getSucceeds().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=desc&cursor=33788507721730-2");
    assertEquals(
        effect.getLinks().getPrecedes().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=asc&cursor=33788507721730-2");
  }

  @Test
  public void testDeserializeTrustlineAuthorizedToMaintainLiabilitiesEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/33788507721730\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=33788507721730-2\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=33788507721730-2\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000033788507721730-0000000002\",\n"
            + "        \"paging_token\": \"33788507721730-2\",\n"
            + "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n"
            + "        \"type\": \"trustline_authorized_to_maintain_liabilities\",\n"
            + "        \"type_i\": 25,\n"
            + "        \"asset_type\": \"credit_alphanum12\",\n"
            + "        \"asset_code\": \"TESTTEST\",\n"
            + "        \"trustor\": \"GB3E4AB4VWXJDUVN4Z3CPBU5HTMWVEQXONZYVDFMHQD6333KHCOL3UBR\"\n"
            + "      }";

    TrustlineAuthorizedToMaintainLiabilitiesEffectResponse effect =
        (TrustlineAuthorizedToMaintainLiabilitiesEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    assertEquals(effect.getAssetType(), "credit_alphanum12");
    assertEquals(effect.getAssetCode(), "TESTTEST");
    assertEquals(effect.getTrustor(), "GB3E4AB4VWXJDUVN4Z3CPBU5HTMWVEQXONZYVDFMHQD6333KHCOL3UBR");

    assertEquals(
        effect.getLinks().getOperation().getHref(),
        "http://horizon-testnet.stellar.org/operations/33788507721730");
    assertEquals(
        effect.getLinks().getSucceeds().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=desc&cursor=33788507721730-2");
    assertEquals(
        effect.getLinks().getPrecedes().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=asc&cursor=33788507721730-2");
  }

  @Test
  public void testDeserializeTrustlineDeauthorizedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/33788507721730\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=33788507721730-2\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=33788507721730-2\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000033788507721730-0000000002\",\n"
            + "        \"paging_token\": \"33788507721730-2\",\n"
            + "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n"
            + "        \"type\": \"trustline_deauthorized\",\n"
            + "        \"type_i\": 24,\n"
            + "        \"asset_type\": \"credit_alphanum4\",\n"
            + "        \"asset_code\": \"EUR\",\n"
            + "        \"trustor\": \"GB3E4AB4VWXJDUVN4Z3CPBU5HTMWVEQXONZYVDFMHQD6333KHCOL3UBR\"\n"
            + "      }";

    TrustlineDeauthorizedEffectResponse effect =
        (TrustlineDeauthorizedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    assertEquals(effect.getAssetType(), "credit_alphanum4");
    assertEquals(effect.getAssetCode(), "EUR");
    assertEquals(effect.getTrustor(), "GB3E4AB4VWXJDUVN4Z3CPBU5HTMWVEQXONZYVDFMHQD6333KHCOL3UBR");

    assertEquals(
        effect.getLinks().getOperation().getHref(),
        "http://horizon-testnet.stellar.org/operations/33788507721730");
    assertEquals(
        effect.getLinks().getSucceeds().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=desc&cursor=33788507721730-2");
    assertEquals(
        effect.getLinks().getPrecedes().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=asc&cursor=33788507721730-2");
  }

  @Test
  public void testDeserializeTradeEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/operations/33788507721730\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=33788507721730-2\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=33788507721730-2\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0000033788507721730-0000000002\",\n"
            + "        \"paging_token\": \"33788507721730-2\",\n"
            + "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n"
            + "        \"type\": \"trade\",\n"
            + "        \"type_i\": 33,\n"
            + "        \"seller\": \"GCVHDLN6EHZBYW2M3BQIY32C23E4GPIRZZDBNF2Q73DAZ5VJDRGSMYRB\",\n"
            + "        \"offer_id\": 1,\n"
            + "        \"sold_amount\": \"1000.0\",\n"
            + "        \"sold_asset_type\": \"credit_alphanum4\",\n"
            + "        \"sold_asset_code\": \"EUR\",\n"
            + "        \"sold_asset_issuer\": \"GCWVFBJ24754I5GXG4JOEB72GJCL3MKWC7VAEYWKGQHPVH3ENPNBSKWS\",\n"
            + "        \"bought_amount\": \"60.0\",\n"
            + "        \"bought_asset_type\": \"credit_alphanum12\",\n"
            + "        \"bought_asset_code\": \"TESTTEST\",\n"
            + "        \"bought_asset_issuer\": \"GAHXPUDP3AK6F2QQM4FIRBGPNGKLRDDSTQCVKEXXKKRHJZUUQ23D5BU7\"\n"
            + "      }";

    TradeEffectResponse effect =
        (TradeEffectResponse) GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    assertEquals(effect.getSeller(), "GCVHDLN6EHZBYW2M3BQIY32C23E4GPIRZZDBNF2Q73DAZ5VJDRGSMYRB");
    assertEquals(effect.getOfferId(), new Long(1));
    assertEquals(effect.getSoldAmount(), "1000.0");
    assertEquals(
        effect.getSoldAsset(),
        create(null, "EUR", "GCWVFBJ24754I5GXG4JOEB72GJCL3MKWC7VAEYWKGQHPVH3ENPNBSKWS"));
    assertEquals(effect.getBoughtAmount(), "60.0");
    assertEquals(
        effect.getBoughtAsset(),
        create(null, "TESTTEST", "GAHXPUDP3AK6F2QQM4FIRBGPNGKLRDDSTQCVKEXXKKRHJZUUQ23D5BU7"));

    assertEquals(
        effect.getLinks().getOperation().getHref(),
        "http://horizon-testnet.stellar.org/operations/33788507721730");
    assertEquals(
        effect.getLinks().getSucceeds().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=desc&cursor=33788507721730-2");
    assertEquals(
        effect.getLinks().getPrecedes().getHref(),
        "http://horizon-testnet.stellar.org/effects?order=asc&cursor=33788507721730-2");
  }

  @Test
  public void testDeserializeDataCreatedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/operations/40181480638386177\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=desc&cursor=40181480638386177-1\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=asc&cursor=40181480638386177-1\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0040181480638386177-0000000001\",\n"
            + "        \"paging_token\": \"40181480638386177-1\",\n"
            + "        \"account\": \"GDPFGP4IPE5DXG6XRXC4ZBUI43PAGRQ5VVNJ3LJTBXDBZ4ITO6HBHNSF\",\n"
            + "        \"type\": \"data_created\",\n"
            + "        \"type_i\": 40,\n"
            + "        \"created_at\": \"2018-06-06T10:23:57Z\"\n"
            + "      }";

    DataCreatedEffectResponse effect =
        (DataCreatedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GDPFGP4IPE5DXG6XRXC4ZBUI43PAGRQ5VVNJ3LJTBXDBZ4ITO6HBHNSF");
    assertEquals(effect.getCreatedAt(), "2018-06-06T10:23:57Z");
  }

  @Test
  public void testDeserializeDataRemovedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/operations/40181480638386177\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=desc&cursor=40181480638386177-1\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=asc&cursor=40181480638386177-1\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0040181480638386177-0000000001\",\n"
            + "        \"paging_token\": \"40181480638386177-1\",\n"
            + "        \"account\": \"GDPFGP4IPE5DXG6XRXC4ZBUI43PAGRQ5VVNJ3LJTBXDBZ4ITO6HBHNSF\",\n"
            + "        \"type\": \"data_removed\",\n"
            + "        \"type_i\": 41,\n"
            + "        \"created_at\": \"2018-06-06T10:23:57Z\"\n"
            + "      }";

    DataRemovedEffectResponse effect =
        (DataRemovedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GDPFGP4IPE5DXG6XRXC4ZBUI43PAGRQ5VVNJ3LJTBXDBZ4ITO6HBHNSF");
    assertEquals(effect.getCreatedAt(), "2018-06-06T10:23:57Z");
  }

  @Test
  public void testDeserializeDataUpdatedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/operations/40181480638386177\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=desc&cursor=40181480638386177-1\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=asc&cursor=40181480638386177-1\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0040181480638386177-0000000001\",\n"
            + "        \"paging_token\": \"40181480638386177-1\",\n"
            + "        \"account\": \"GDPFGP4IPE5DXG6XRXC4ZBUI43PAGRQ5VVNJ3LJTBXDBZ4ITO6HBHNSF\",\n"
            + "        \"type\": \"data_updated\",\n"
            + "        \"type_i\": 42,\n"
            + "        \"created_at\": \"2018-06-06T10:23:57Z\"\n"
            + "      }";

    DataUpdatedEffectResponse effect =
        (DataUpdatedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GDPFGP4IPE5DXG6XRXC4ZBUI43PAGRQ5VVNJ3LJTBXDBZ4ITO6HBHNSF");
    assertEquals(effect.getCreatedAt(), "2018-06-06T10:23:57Z");
  }

  @Test
  public void testDeserializeSequenceBumpedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/operations/40181480638386177\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=desc&cursor=40181480638386177-1\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=asc&cursor=40181480638386177-1\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0040181480638386177-0000000001\",\n"
            + "        \"paging_token\": \"40181480638386177-1\",\n"
            + "        \"account\": \"GDPFGP4IPE5DXG6XRXC4ZBUI43PAGRQ5VVNJ3LJTBXDBZ4ITO6HBHNSF\",\n"
            + "        \"type\": \"sequence_bumped\",\n"
            + "        \"type_i\": 43,\n"
            + "        \"new_seq\": \"79473726952833048\",\n"
            + "        \"created_at\": \"2018-06-06T10:23:57Z\"\n"
            + "      }";

    SequenceBumpedEffectResponse effect =
        (SequenceBumpedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GDPFGP4IPE5DXG6XRXC4ZBUI43PAGRQ5VVNJ3LJTBXDBZ4ITO6HBHNSF");
    assertEquals(effect.getCreatedAt(), "2018-06-06T10:23:57Z");
    assertEquals(effect.getNewSequence(), new Long(79473726952833048L));
  }

  @Test
  public void testDeserializeClaimableBalanceClawedbackEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/operations/40181480638386177\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=desc&cursor=40181480638386177-1\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=asc&cursor=40181480638386177-1\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0040181480638386177-0000000001\",\n"
            + "        \"paging_token\": \"40181480638386177-1\",\n"
            + "        \"account\": \"GDPFGP4IPE5DXG6XRXC4ZBUI43PAGRQ5VVNJ3LJTBXDBZ4ITO6HBHNSF\",\n"
            + "        \"type\": \"claimable_balance_clawed_back\",\n"
            + "        \"type_i\": 80,\n"
            + "        \"balance_id\": \"00000000178826fbfe339e1f5c53417c6fedfe2c05e8bec14303143ec46b38981b09c3f9\",\n"
            + "        \"created_at\": \"2018-06-06T10:23:57Z\"\n"
            + "      }";

    ClaimableBalanceClawedBackEffectResponse effect =
        (ClaimableBalanceClawedBackEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getAccount(), "GDPFGP4IPE5DXG6XRXC4ZBUI43PAGRQ5VVNJ3LJTBXDBZ4ITO6HBHNSF");
    assertEquals(effect.getCreatedAt(), "2018-06-06T10:23:57Z");
    assertEquals(
        effect.getBalanceId(),
        "00000000178826fbfe339e1f5c53417c6fedfe2c05e8bec14303143ec46b38981b09c3f9");
    assertEquals(effect.getType(), "claimable_balance_clawed_back");
  }

  @Test
  public void testDeserializeTrustlineFlagsUpdatedEffect() {
    String json =
        "{\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/operations/40181480638386177\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=desc&cursor=40181480638386177-1\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=asc&cursor=40181480638386177-1\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0040181480638386177-0000000001\",\n"
            + "        \"paging_token\": \"40181480638386177-1\",\n"
            + "        \"account\": \"GDPFGP4IPE5DXG6XRXC4ZBUI43PAGRQ5VVNJ3LJTBXDBZ4ITO6HBHNSF\",\n"
            + "        \"type\": \"trustline_flags_updated\",\n"
            + "        \"type_i\": 26,\n"
            + "        \"trustor\": \"GCVHDLN6EHZBYW2M3BQIY32C23E4GPIRZZDBNF2Q73DAZ5VJDRGSMYRB\",\n"
            + "        \"asset_type\": \"credit_alphanum4\",\n"
            + "        \"asset_code\": \"EUR\",\n"
            + "        \"asset_issuer\": \"GCWVFBJ24754I5GXG4JOEB72GJCL3MKWC7VAEYWKGQHPVH3ENPNBSKWS\",\n"
            + "        \"authorized_flag\": true,\n"
            + "        \"clawback_enabled_flag\": true,\n"
            + "        \"created_at\": \"2018-06-06T10:23:57Z\"\n"
            + "      }";

    TrustlineFlagsUpdatedEffectResponse effect =
        (TrustlineFlagsUpdatedEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getType(), "trustline_flags_updated");

    assertEquals(effect.getAccount(), "GDPFGP4IPE5DXG6XRXC4ZBUI43PAGRQ5VVNJ3LJTBXDBZ4ITO6HBHNSF");
    assertEquals(effect.getCreatedAt(), "2018-06-06T10:23:57Z");
    assertEquals(effect.getTrustor(), "GCVHDLN6EHZBYW2M3BQIY32C23E4GPIRZZDBNF2Q73DAZ5VJDRGSMYRB");
    assertTrue(effect.getAuthorized());
    assertTrue(effect.getClawbackEnabled());
    assertFalse(effect.getAuthorizedToMaintainLiabilities());

    assertEquals(
        effect.getAsset(),
        create(null, "EUR", "GCWVFBJ24754I5GXG4JOEB72GJCL3MKWC7VAEYWKGQHPVH3ENPNBSKWS"));
    assertEquals(
        effect.getAssetIssuer(), "GCWVFBJ24754I5GXG4JOEB72GJCL3MKWC7VAEYWKGQHPVH3ENPNBSKWS");
    assertEquals(effect.getAssetCode(), "EUR");
    assertEquals(effect.getAssetType(), "credit_alphanum4");
  }

  @Test
  public void testDeserializeLiquidityPoolTradeEffect() {
    String json =
        "      {\n"
            + "        \"_links\": {\n"
            + "          \"operation\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/operations/2091275411030017\"\n"
            + "          },\n"
            + "          \"succeeds\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=2091275411030017-3\"\n"
            + "          },\n"
            + "          \"precedes\": {\n"
            + "            \"href\": \"https://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=2091275411030017-3\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"id\": \"0002091275411030017-0000000003\",\n"
            + "        \"paging_token\": \"2091275411030017-3\",\n"
            + "        \"account\": \"GDUZE3MB2TJVVCGM7LLYUXGZTZNRQS6OEESTQDO4RGI7QZPI2VYOIC4G\",\n"
            + "        \"type\": \"liquidity_pool_trade\",\n"
            + "        \"type_i\": 92,\n"
            + "        \"created_at\": \"2021-10-15T00:15:07Z\",\n"
            + "        \"liquidity_pool\": {\n"
            + "          \"id\": \"af961c246cc51e6eda2441482f09cf6b1478e30b34e47daf86c860f753d8f04c\",\n"
            + "          \"fee_bp\": 30,\n"
            + "          \"type\": \"constant_product\",\n"
            + "          \"total_trustlines\": \"3\",\n"
            + "          \"total_shares\": \"18560.5392046\",\n"
            + "          \"reserves\": [\n"
            + "            {\n"
            + "              \"asset\": \"native\",\n"
            + "              \"amount\": \"6080.9091224\"\n"
            + "            },\n"
            + "            {\n"
            + "              \"asset\": \"ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO\",\n"
            + "              \"amount\": \"56817.1796745\"\n"
            + "            }\n"
            + "          ]\n"
            + "        },\n"
            + "        \"sold\": {\n"
            + "          \"asset\": \"native\",\n"
            + "          \"amount\": \"0.1067066\"\n"
            + "        },\n"
            + "        \"bought\": {\n"
            + "          \"asset\": \"ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO\",\n"
            + "          \"amount\": \"1.0000000\"\n"
            + "        }\n"
            + "      }";
    LiquidityPoolTradeEffectResponse effect =
        (LiquidityPoolTradeEffectResponse)
            GsonSingleton.getInstance().fromJson(json, EffectResponse.class);

    assertEquals(effect.getType(), "liquidity_pool_trade");

    assertEquals(effect.getAccount(), "GDUZE3MB2TJVVCGM7LLYUXGZTZNRQS6OEESTQDO4RGI7QZPI2VYOIC4G");
    assertEquals(effect.getCreatedAt(), "2021-10-15T00:15:07Z");
    assertEquals(
        effect.getLiquidityPool().getID().toString(),
        "af961c246cc51e6eda2441482f09cf6b1478e30b34e47daf86c860f753d8f04c");
    assertEquals(effect.getLiquidityPool().getFeeBP(), Integer.valueOf(30));
    assertEquals(
        effect.getLiquidityPool().getType(), LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT);
    assertEquals(effect.getLiquidityPool().getTotalTrustlines(), Long.valueOf(3));
    assertEquals(effect.getLiquidityPool().getTotalShares(), "18560.5392046");
    assertTrue(
        Arrays.equals(
            effect.getLiquidityPool().getReserves(),
            new AssetAmount[] {
              new AssetAmount(create("native"), "6080.9091224"),
              new AssetAmount(
                  create("ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO"),
                  "56817.1796745")
            }));
    assertEquals(effect.getSold(), new AssetAmount(create("native"), "0.1067066"));
    assertEquals(
        effect.getBought(),
        new AssetAmount(
            create("ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO"), "1.0000000"));
  }
}
