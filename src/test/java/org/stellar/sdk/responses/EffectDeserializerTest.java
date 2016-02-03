package org.stellar.sdk.responses;

import junit.framework.TestCase;

import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.responses.effects.*;

public class EffectDeserializerTest extends TestCase {
  @Test
  public void testDeserializeAccountCreatedEffect() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/65571265847297\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=65571265847297-1\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=65571265847297-1\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"0000065571265847297-0000000001\",\n" +
            "        \"paging_token\": \"65571265847297-1\",\n" +
            "        \"account\": \"GCBQ6JRBPF3SXQBQ6SO5MRBE7WVV4UCHYOSHQGXSZNPZLFRYVYOWBZRQ\",\n" +
            "        \"type\": \"account_created\",\n" +
            "        \"type_i\": 0,\n" +
            "        \"starting_balance\": \"30.0\"\n" +
            "      }";

    AccountCreatedEffect effect = (AccountCreatedEffect) GsonSingleton.getInstance().fromJson(json, Effect.class);

    assertEquals(effect.getAccount().getAccountId(), "GCBQ6JRBPF3SXQBQ6SO5MRBE7WVV4UCHYOSHQGXSZNPZLFRYVYOWBZRQ");
    assertEquals(effect.getStartingBalance(), "30.0");
    assertEquals(effect.getPagingToken(), "65571265847297-1");

    assertEquals(effect.getLinks().getOperation().getHref(), "http://horizon-testnet.stellar.org/operations/65571265847297");
    assertEquals(effect.getLinks().getSucceeds().getHref(), "http://horizon-testnet.stellar.org/effects?order=desc&cursor=65571265847297-1");
    assertEquals(effect.getLinks().getPrecedes().getHref(), "http://horizon-testnet.stellar.org/effects?order=asc&cursor=65571265847297-1");
  }

  @Test
  public void testDeserializeAccountRemovedEffect() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/65571265847297\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=65571265847297-1\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=65571265847297-1\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"0000065571265847297-0000000001\",\n" +
            "        \"paging_token\": \"65571265847297-1\",\n" +
            "        \"account\": \"GCBQ6JRBPF3SXQBQ6SO5MRBE7WVV4UCHYOSHQGXSZNPZLFRYVYOWBZRQ\",\n" +
            "        \"type\": \"account_removed\",\n" +
            "        \"type_i\": 1\n"+
            "      }";

    AccountRemovedEffect effect = (AccountRemovedEffect) GsonSingleton.getInstance().fromJson(json, Effect.class);

    assertEquals(effect.getAccount().getAccountId(), "GCBQ6JRBPF3SXQBQ6SO5MRBE7WVV4UCHYOSHQGXSZNPZLFRYVYOWBZRQ");

    assertEquals(effect.getLinks().getOperation().getHref(), "http://horizon-testnet.stellar.org/operations/65571265847297");
    assertEquals(effect.getLinks().getSucceeds().getHref(), "http://horizon-testnet.stellar.org/effects?order=desc&cursor=65571265847297-1");
    assertEquals(effect.getLinks().getPrecedes().getHref(), "http://horizon-testnet.stellar.org/effects?order=asc&cursor=65571265847297-1");
  }

  @Test
  public void testDeserializeAccountCreditedEffect() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/13563506724865\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=13563506724865-1\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=13563506724865-1\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"0000013563506724865-0000000001\",\n" +
            "        \"paging_token\": \"13563506724865-1\",\n" +
            "        \"account\": \"GDLGTRIBFH24364GPWPUS45GUFC2GU4ARPGWTXVCPLGTUHX3IOS3ON47\",\n" +
            "        \"type\": \"account_credited\",\n" +
            "        \"type_i\": 2,\n" +
            "        \"asset_type\": \"native\",\n" +
            "        \"amount\": \"1000.0\"\n" +
            "      }";

    AccountCreditedEffect effect = (AccountCreditedEffect) GsonSingleton.getInstance().fromJson(json, Effect.class);

    assertEquals(effect.getAccount().getAccountId(), "GDLGTRIBFH24364GPWPUS45GUFC2GU4ARPGWTXVCPLGTUHX3IOS3ON47");
    TestCase.assertEquals(effect.getAsset(), new AssetTypeNative());
    assertEquals(effect.getAmount(), "1000.0");

    assertEquals(effect.getLinks().getOperation().getHref(), "http://horizon-testnet.stellar.org/operations/13563506724865");
    assertEquals(effect.getLinks().getSucceeds().getHref(), "http://horizon-testnet.stellar.org/effects?order=desc&cursor=13563506724865-1");
    assertEquals(effect.getLinks().getPrecedes().getHref(), "http://horizon-testnet.stellar.org/effects?order=asc&cursor=13563506724865-1");
  }

  @Test
  public void testDeserializeAccountDebitedEffect() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/65571265843201\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=65571265843201-2\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=65571265843201-2\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"0000065571265843201-0000000002\",\n" +
            "        \"paging_token\": \"65571265843201-2\",\n" +
            "        \"account\": \"GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H\",\n" +
            "        \"type\": \"account_debited\",\n" +
            "        \"type_i\": 3,\n" +
            "        \"asset_type\": \"native\",\n" +
            "        \"amount\": \"30.0\"\n" +
            "      }";

    AccountDebitedEffect effect = (AccountDebitedEffect) GsonSingleton.getInstance().fromJson(json, Effect.class);

    assertEquals(effect.getAccount().getAccountId(), "GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H");
    assertEquals(effect.getAsset(), new AssetTypeNative());
    assertEquals(effect.getAmount(), "30.0");

    assertEquals(effect.getLinks().getOperation().getHref(), "http://horizon-testnet.stellar.org/operations/65571265843201");
    assertEquals(effect.getLinks().getSucceeds().getHref(), "http://horizon-testnet.stellar.org/effects?order=desc&cursor=65571265843201-2");
    assertEquals(effect.getLinks().getPrecedes().getHref(), "http://horizon-testnet.stellar.org/effects?order=asc&cursor=65571265843201-2");
  }

  @Test
  public void testDeserializeAccountThresholdsUpdatedEffect() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/18970870550529\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=18970870550529-1\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=18970870550529-1\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"0000018970870550529-0000000001\",\n" +
            "        \"paging_token\": \"18970870550529-1\",\n" +
            "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n" +
            "        \"low_threshold\": 2,\n" +
            "        \"med_threshold\": 3,\n" +
            "        \"high_threshold\": 4,\n" +
            "        \"type\": \"account_thresholds_updated\",\n" +
            "        \"type_i\": 4\n" +
            "      }";

    AccountThresholdsUpdatedEffect effect = (AccountThresholdsUpdatedEffect) GsonSingleton.getInstance().fromJson(json, Effect.class);

    assertEquals(effect.getAccount().getAccountId(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    assertEquals(effect.getLowThreshold(), new Integer(2));
    assertEquals(effect.getMedThreshold(), new Integer(3));
    assertEquals(effect.getHighThreshold(), new Integer(4));

    assertEquals(effect.getLinks().getOperation().getHref(), "http://horizon-testnet.stellar.org/operations/18970870550529");
    assertEquals(effect.getLinks().getSucceeds().getHref(), "http://horizon-testnet.stellar.org/effects?order=desc&cursor=18970870550529-1");
    assertEquals(effect.getLinks().getPrecedes().getHref(), "http://horizon-testnet.stellar.org/effects?order=asc&cursor=18970870550529-1");
  }

  @Test
  public void testDeserializeAccountHomeDomainUpdatedEffect() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/18970870550529\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=18970870550529-1\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=18970870550529-1\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"0000018970870550529-0000000001\",\n" +
            "        \"paging_token\": \"18970870550529-1\",\n" +
            "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n" +
            "        \"type\": \"account_home_domain_updated\",\n" +
            "        \"type_i\": 5,\n" +
            "        \"home_domain\": \"stellar.org\"\n" +
            "      }";

    AccountHomeDomainUpdatedEffect effect = (AccountHomeDomainUpdatedEffect) GsonSingleton.getInstance().fromJson(json, Effect.class);

    assertEquals(effect.getAccount().getAccountId(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    assertEquals(effect.getHomeDomain(), "stellar.org");

    assertEquals(effect.getLinks().getOperation().getHref(), "http://horizon-testnet.stellar.org/operations/18970870550529");
    assertEquals(effect.getLinks().getSucceeds().getHref(), "http://horizon-testnet.stellar.org/effects?order=desc&cursor=18970870550529-1");
    assertEquals(effect.getLinks().getPrecedes().getHref(), "http://horizon-testnet.stellar.org/effects?order=asc&cursor=18970870550529-1");
  }

  @Test
  public void testDeserializeAccountFlagsUpdatedEffect() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/18970870550529\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=18970870550529-1\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=18970870550529-1\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"0000018970870550529-0000000001\",\n" +
            "        \"paging_token\": \"18970870550529-1\",\n" +
            "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n" +
            "        \"type\": \"account_flags_updated\",\n" +
            "        \"type_i\": 6,\n" +
            "        \"auth_required_flag\": false,\n" +
            "        \"auth_revokable_flag\": true\n" +
            "      }";

    AccountFlagsUpdatedEffect effect = (AccountFlagsUpdatedEffect) GsonSingleton.getInstance().fromJson(json, Effect.class);

    assertEquals(effect.getAccount().getAccountId(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    assertEquals(effect.getAuthRequiredFlag(), new Boolean(false));
    assertEquals(effect.getAuthRevokableFlag(), new Boolean(true));

    assertEquals(effect.getLinks().getOperation().getHref(), "http://horizon-testnet.stellar.org/operations/18970870550529");
    assertEquals(effect.getLinks().getSucceeds().getHref(), "http://horizon-testnet.stellar.org/effects?order=desc&cursor=18970870550529-1");
    assertEquals(effect.getLinks().getPrecedes().getHref(), "http://horizon-testnet.stellar.org/effects?order=asc&cursor=18970870550529-1");
  }

  @Test
  public void testDeserializeSignerCreatedEffect() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/65571265859585\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=65571265859585-3\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=65571265859585-3\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"0000065571265859585-0000000003\",\n" +
            "        \"paging_token\": \"65571265859585-3\",\n" +
            "        \"account\": \"GB24LPGAHYTWRYOXIDKXLI55SBRWW42T3TZKDAAW3BOJX4ADVIATFTLU\",\n" +
            "        \"type\": \"signer_created\",\n" +
            "        \"type_i\": 10,\n" +
            "        \"weight\": 1,\n" +
            "        \"public_key\": \"GB24LPGAHYTWRYOXIDKXLI55SBRWW42T3TZKDAAW3BOJX4ADVIATFTLU\"\n" +
            "      }";

    SignerCreatedEffect effect = (SignerCreatedEffect) GsonSingleton.getInstance().fromJson(json, Effect.class);

    assertEquals(effect.getAccount().getAccountId(), "GB24LPGAHYTWRYOXIDKXLI55SBRWW42T3TZKDAAW3BOJX4ADVIATFTLU");
    assertEquals(effect.getWeight(), new Integer(1));
    assertEquals(effect.getPublicKey(), "GB24LPGAHYTWRYOXIDKXLI55SBRWW42T3TZKDAAW3BOJX4ADVIATFTLU");

    assertEquals(effect.getLinks().getOperation().getHref(), "http://horizon-testnet.stellar.org/operations/65571265859585");
    assertEquals(effect.getLinks().getSucceeds().getHref(), "http://horizon-testnet.stellar.org/effects?order=desc&cursor=65571265859585-3");
    assertEquals(effect.getLinks().getPrecedes().getHref(), "http://horizon-testnet.stellar.org/effects?order=asc&cursor=65571265859585-3");
  }

  @Test
  public void testDeserializeSignerRemovedEffect() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/43658342567940\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=43658342567940-2\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=43658342567940-2\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"0000043658342567940-0000000002\",\n" +
            "        \"paging_token\": \"43658342567940-2\",\n" +
            "        \"account\": \"GCFKT6BN2FEASCEVDNHEC4LLFT2KLUUPEMKM4OJPEJ65H2AEZ7IH4RV6\",\n" +
            "        \"type\": \"signer_removed\",\n" +
            "        \"type_i\": 11,\n" +
            "        \"weight\": 0,\n" +
            "        \"public_key\": \"GCFKT6BN2FEASCEVDNHEC4LLFT2KLUUPEMKM4OJPEJ65H2AEZ7IH4RV6\"\n" +
            "      }";

    SignerRemovedEffect effect = (SignerRemovedEffect) GsonSingleton.getInstance().fromJson(json, Effect.class);

    assertEquals(effect.getAccount().getAccountId(), "GCFKT6BN2FEASCEVDNHEC4LLFT2KLUUPEMKM4OJPEJ65H2AEZ7IH4RV6");
    assertEquals(effect.getWeight(), new Integer(0));
    assertEquals(effect.getPublicKey(), "GCFKT6BN2FEASCEVDNHEC4LLFT2KLUUPEMKM4OJPEJ65H2AEZ7IH4RV6");

    assertEquals(effect.getLinks().getOperation().getHref(), "http://horizon-testnet.stellar.org/operations/43658342567940");
    assertEquals(effect.getLinks().getSucceeds().getHref(), "http://horizon-testnet.stellar.org/effects?order=desc&cursor=43658342567940-2");
    assertEquals(effect.getLinks().getPrecedes().getHref(), "http://horizon-testnet.stellar.org/effects?order=asc&cursor=43658342567940-2");
  }

  @Test
  public void testDeserializeSignerUpdatedEffect() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/33788507721730\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=33788507721730-2\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=33788507721730-2\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"0000033788507721730-0000000002\",\n" +
            "        \"paging_token\": \"33788507721730-2\",\n" +
            "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n" +
            "        \"type\": \"signer_updated\",\n" +
            "        \"type_i\": 12,\n" +
            "        \"weight\": 2,\n" +
            "        \"public_key\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\"\n" +
            "      }";

    SignerUpdatedEffect effect = (SignerUpdatedEffect) GsonSingleton.getInstance().fromJson(json, Effect.class);

    assertEquals(effect.getAccount().getAccountId(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    assertEquals(effect.getWeight(), new Integer(2));
    assertEquals(effect.getPublicKey(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");

    assertEquals(effect.getLinks().getOperation().getHref(), "http://horizon-testnet.stellar.org/operations/33788507721730");
    assertEquals(effect.getLinks().getSucceeds().getHref(), "http://horizon-testnet.stellar.org/effects?order=desc&cursor=33788507721730-2");
    assertEquals(effect.getLinks().getPrecedes().getHref(), "http://horizon-testnet.stellar.org/effects?order=asc&cursor=33788507721730-2");
  }

  @Test
  public void testDeserializeTrustlineCreatedEffect() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/33788507721730\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=33788507721730-2\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=33788507721730-2\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"0000033788507721730-0000000002\",\n" +
            "        \"paging_token\": \"33788507721730-2\",\n" +
            "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n" +
            "        \"type\": \"trustline_created\",\n" +
            "        \"type_i\": 20,\n" +
            "        \"asset_type\": \"credit_alphanum4\",\n" +
            "        \"asset_code\": \"EUR\",\n" +
            "        \"asset_issuer\": \"GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA\",\n" +
            "        \"limit\": \"1000.0\"\n" +
            "      }";

    TrustlineCreatedEffect effect = (TrustlineCreatedEffect) GsonSingleton.getInstance().fromJson(json, Effect.class);

    assertEquals(effect.getAccount().getAccountId(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    TestCase.assertEquals(effect.getAsset(), Asset.createNonNativeAsset("EUR", KeyPair.fromAccountId("GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA")));
    assertEquals(effect.getLimit(), "1000.0");

    assertEquals(effect.getLinks().getOperation().getHref(), "http://horizon-testnet.stellar.org/operations/33788507721730");
    assertEquals(effect.getLinks().getSucceeds().getHref(), "http://horizon-testnet.stellar.org/effects?order=desc&cursor=33788507721730-2");
    assertEquals(effect.getLinks().getPrecedes().getHref(), "http://horizon-testnet.stellar.org/effects?order=asc&cursor=33788507721730-2");
  }

  @Test
  public void testDeserializeTrustlineRemovedEffect() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/33788507721730\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=33788507721730-2\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=33788507721730-2\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"0000033788507721730-0000000002\",\n" +
            "        \"paging_token\": \"33788507721730-2\",\n" +
            "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n" +
            "        \"type\": \"trustline_removed\",\n" +
            "        \"type_i\": 21,\n" +
            "        \"asset_type\": \"credit_alphanum4\",\n" +
            "        \"asset_code\": \"EUR\",\n" +
            "        \"asset_issuer\": \"GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA\",\n" +
            "        \"limit\": \"0.0\"\n" +
            "      }";

    TrustlineRemovedEffect effect = (TrustlineRemovedEffect) GsonSingleton.getInstance().fromJson(json, Effect.class);

    assertEquals(effect.getAccount().getAccountId(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    assertEquals(effect.getAsset(), Asset.createNonNativeAsset("EUR", KeyPair.fromAccountId("GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA")));
    assertEquals(effect.getLimit(), "0.0");

    assertEquals(effect.getLinks().getOperation().getHref(), "http://horizon-testnet.stellar.org/operations/33788507721730");
    assertEquals(effect.getLinks().getSucceeds().getHref(), "http://horizon-testnet.stellar.org/effects?order=desc&cursor=33788507721730-2");
    assertEquals(effect.getLinks().getPrecedes().getHref(), "http://horizon-testnet.stellar.org/effects?order=asc&cursor=33788507721730-2");
  }

  @Test
  public void testDeserializeTrustlineUpdatedEffect() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/33788507721730\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=33788507721730-2\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=33788507721730-2\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"0000033788507721730-0000000002\",\n" +
            "        \"paging_token\": \"33788507721730-2\",\n" +
            "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n" +
            "        \"type\": \"trustline_updated\",\n" +
            "        \"type_i\": 22,\n" +
            "        \"asset_type\": \"credit_alphanum12\",\n" +
            "        \"asset_code\": \"TESTTEST\",\n" +
            "        \"asset_issuer\": \"GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA\",\n" +
            "        \"limit\": \"100.0\"\n" +
            "      }";

    TrustlineUpdatedEffect effect = (TrustlineUpdatedEffect) GsonSingleton.getInstance().fromJson(json, Effect.class);

    assertEquals(effect.getAccount().getAccountId(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    assertEquals(effect.getAsset(), Asset.createNonNativeAsset("TESTTEST", KeyPair.fromAccountId("GAZN3PPIDQCSP5JD4ETQQQ2IU2RMFYQTAL4NNQZUGLLO2XJJJ3RDSDGA")));
    assertEquals(effect.getLimit(), "100.0");

    assertEquals(effect.getLinks().getOperation().getHref(), "http://horizon-testnet.stellar.org/operations/33788507721730");
    assertEquals(effect.getLinks().getSucceeds().getHref(), "http://horizon-testnet.stellar.org/effects?order=desc&cursor=33788507721730-2");
    assertEquals(effect.getLinks().getPrecedes().getHref(), "http://horizon-testnet.stellar.org/effects?order=asc&cursor=33788507721730-2");
  }

  @Test
  public void testDeserializeTrustlineAuthorizedEffect() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/33788507721730\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=33788507721730-2\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=33788507721730-2\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"0000033788507721730-0000000002\",\n" +
            "        \"paging_token\": \"33788507721730-2\",\n" +
            "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n" +
            "        \"type\": \"trustline_authorized\",\n" +
            "        \"type_i\": 23,\n" +
            "        \"asset_type\": \"credit_alphanum12\",\n" +
            "        \"asset_code\": \"TESTTEST\",\n" +
            "        \"trustor\": \"GB3E4AB4VWXJDUVN4Z3CPBU5HTMWVEQXONZYVDFMHQD6333KHCOL3UBR\"\n" +
            "      }";

    TrustlineAuthorizedEffect effect = (TrustlineAuthorizedEffect) GsonSingleton.getInstance().fromJson(json, Effect.class);

    assertEquals(effect.getAccount().getAccountId(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    assertEquals(effect.getAssetType(), "credit_alphanum12");
    assertEquals(effect.getAssetCode(), "TESTTEST");
    assertEquals(effect.getTrustor().getAccountId(), "GB3E4AB4VWXJDUVN4Z3CPBU5HTMWVEQXONZYVDFMHQD6333KHCOL3UBR");

    assertEquals(effect.getLinks().getOperation().getHref(), "http://horizon-testnet.stellar.org/operations/33788507721730");
    assertEquals(effect.getLinks().getSucceeds().getHref(), "http://horizon-testnet.stellar.org/effects?order=desc&cursor=33788507721730-2");
    assertEquals(effect.getLinks().getPrecedes().getHref(), "http://horizon-testnet.stellar.org/effects?order=asc&cursor=33788507721730-2");
  }

  @Test
  public void testDeserializeTrustlineDeauthorizedEffect() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/33788507721730\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=33788507721730-2\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=33788507721730-2\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"0000033788507721730-0000000002\",\n" +
            "        \"paging_token\": \"33788507721730-2\",\n" +
            "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n" +
            "        \"type\": \"trustline_deauthorized\",\n" +
            "        \"type_i\": 24,\n" +
            "        \"asset_type\": \"credit_alphanum4\",\n" +
            "        \"asset_code\": \"EUR\",\n" +
            "        \"trustor\": \"GB3E4AB4VWXJDUVN4Z3CPBU5HTMWVEQXONZYVDFMHQD6333KHCOL3UBR\"\n" +
            "      }";

    TrustlineDeauthorizedEffect effect = (TrustlineDeauthorizedEffect) GsonSingleton.getInstance().fromJson(json, Effect.class);

    assertEquals(effect.getAccount().getAccountId(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    assertEquals(effect.getAssetType(), "credit_alphanum4");
    assertEquals(effect.getAssetCode(), "EUR");
    assertEquals(effect.getTrustor().getAccountId(), "GB3E4AB4VWXJDUVN4Z3CPBU5HTMWVEQXONZYVDFMHQD6333KHCOL3UBR");

    assertEquals(effect.getLinks().getOperation().getHref(), "http://horizon-testnet.stellar.org/operations/33788507721730");
    assertEquals(effect.getLinks().getSucceeds().getHref(), "http://horizon-testnet.stellar.org/effects?order=desc&cursor=33788507721730-2");
    assertEquals(effect.getLinks().getPrecedes().getHref(), "http://horizon-testnet.stellar.org/effects?order=asc&cursor=33788507721730-2");
  }

  @Test
  public void testDeserializeTradeEffect() {
    String json = "{\n" +
            "        \"_links\": {\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/33788507721730\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=33788507721730-2\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=33788507721730-2\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"0000033788507721730-0000000002\",\n" +
            "        \"paging_token\": \"33788507721730-2\",\n" +
            "        \"account\": \"GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO\",\n" +
            "        \"type\": \"trade\",\n" +
            "        \"type_i\": 33,\n" +
            "        \"seller\": \"GCVHDLN6EHZBYW2M3BQIY32C23E4GPIRZZDBNF2Q73DAZ5VJDRGSMYRB\",\n" +
            "        \"offer_id\": 1,\n" +
            "        \"sold_amount\": \"1000.0\",\n" +
            "        \"sold_asset_type\": \"credit_alphanum4\",\n" +
            "        \"sold_asset_code\": \"EUR\",\n" +
            "        \"sold_asset_issuer\": \"GCWVFBJ24754I5GXG4JOEB72GJCL3MKWC7VAEYWKGQHPVH3ENPNBSKWS\",\n" +
            "        \"bought_amount\": \"60.0\",\n" +
            "        \"bought_asset_type\": \"credit_alphanum12\",\n" +
            "        \"bought_asset_code\": \"TESTTEST\",\n" +
            "        \"bought_asset_issuer\": \"GAHXPUDP3AK6F2QQM4FIRBGPNGKLRDDSTQCVKEXXKKRHJZUUQ23D5BU7\"\n" +
            "      }";

    TradeEffect effect = (TradeEffect) GsonSingleton.getInstance().fromJson(json, Effect.class);

    assertEquals(effect.getAccount().getAccountId(), "GA6U5X6WOPNKKDKQULBR7IDHDBAQKOWPHYEC7WSXHZBFEYFD3XVZAKOO");
    assertEquals(effect.getSeller().getAccountId(), "GCVHDLN6EHZBYW2M3BQIY32C23E4GPIRZZDBNF2Q73DAZ5VJDRGSMYRB");
    assertEquals(effect.getOfferId(), new Long(1));
    assertEquals(effect.getSoldAmount(), "1000.0");
    assertEquals(effect.getSoldAsset(), Asset.createNonNativeAsset("EUR", KeyPair.fromAccountId("GCWVFBJ24754I5GXG4JOEB72GJCL3MKWC7VAEYWKGQHPVH3ENPNBSKWS")));
    assertEquals(effect.getBoughtAmount(), "60.0");
    assertEquals(effect.getBoughtAsset(), Asset.createNonNativeAsset("TESTTEST", KeyPair.fromAccountId("GAHXPUDP3AK6F2QQM4FIRBGPNGKLRDDSTQCVKEXXKKRHJZUUQ23D5BU7")));

    assertEquals(effect.getLinks().getOperation().getHref(), "http://horizon-testnet.stellar.org/operations/33788507721730");
    assertEquals(effect.getLinks().getSucceeds().getHref(), "http://horizon-testnet.stellar.org/effects?order=desc&cursor=33788507721730-2");
    assertEquals(effect.getLinks().getPrecedes().getHref(), "http://horizon-testnet.stellar.org/effects?order=asc&cursor=33788507721730-2");
  }
}
