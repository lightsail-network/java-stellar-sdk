package org.stellar.sdk.responses;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.TrustLineAsset;

public class AccountResponseTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/account.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountResponse account = GsonSingleton.getInstance().fromJson(json, AccountResponse.class);
    assertEquals(account.getId(), "GDA2KZG7UB3C3KU4RYRGCQI6JPV2I2FYBWT434MQHPE36FH7CWGVR5X3");
    assertEquals(
        account.getAccountId(), "GDA2KZG7UB3C3KU4RYRGCQI6JPV2I2FYBWT434MQHPE36FH7CWGVR5X3");
    assertEquals(account.getSequenceNumber().longValue(), 160470889244710503L);
    assertEquals(account.getSequenceUpdatedAtLedger().longValue(), 52222212);
    assertEquals(account.getSequenceUpdatedAtTime().longValue(), 1718974099);
    assertEquals(account.getSubentryCount().intValue(), 12);
    assertEquals(
        account.getInflationDestination(),
        "GDZMB4BTSFCRDXWQOG43WGX3Z4FUCEYJRTTQPXDR27PNNLU6RJT5V7LQ");
    assertEquals(account.getHomeDomain(), "example.com");
    assertEquals(account.getLastModifiedLedger().longValue(), 52223288);
    assertEquals(account.getLastModifiedTime(), "2024-06-21T12:47:51Z");
    assertEquals(account.getThresholds().getLowThreshold(), 1);
    assertEquals(account.getThresholds().getMedThreshold(), 20);
    assertEquals(account.getThresholds().getHighThreshold(), 30);
    assertEquals(account.getFlags().getAuthRequired(), true);
    assertEquals(account.getFlags().getAuthRevocable(), true);
    assertEquals(account.getFlags().getAuthImmutable(), false);
    assertEquals(account.getFlags().getAuthClawbackEnabled(), true);
    assertEquals(account.getBalances().length, 7);

    assertEquals(account.getBalances()[0].getBalance(), "2816.6567089");
    assertEquals(
        account.getBalances()[0].getLiquidityPoolID().getPoolId(),
        "59fa1dc57433dcfbd2db7319d26cb3da1f28f2d8095a3ec36ad4ef9cadb0013e");
    assertEquals(account.getBalances()[0].getLimit(), "922337203685.4775807");
    assertEquals(account.getBalances()[0].getLastModifiedLedger().intValue(), 39608341);
    assertEquals(account.getBalances()[0].getIsAuthorized(), false);
    assertEquals(account.getBalances()[0].getIsAuthorizedToMaintainLiabilities(), false);
    assertEquals(account.getBalances()[0].getAssetType(), "liquidity_pool_shares");
    assertEquals(
        account.getBalances()[0].getAsset(),
        new TrustLineAsset(
            new LiquidityPoolID(
                "59fa1dc57433dcfbd2db7319d26cb3da1f28f2d8095a3ec36ad4ef9cadb0013e")));

    assertEquals(account.getBalances()[1].getBalance(), "85729.1030749");
    assertEquals(account.getBalances()[1].getLimit(), "922337203685.4775807");
    assertEquals(account.getBalances()[1].getBuyingLiabilities(), "0.0000000");
    assertEquals(account.getBalances()[1].getSellingLiabilities(), "0.0000000");
    assertEquals(account.getBalances()[1].getLastModifiedLedger().intValue(), 52414854);
    assertEquals(account.getBalances()[1].getIsAuthorized(), true);
    assertEquals(account.getBalances()[1].getIsAuthorizedToMaintainLiabilities(), true);
    assertEquals(account.getBalances()[1].getAssetType(), "credit_alphanum4");
    assertEquals(account.getBalances()[1].getAssetCode(), "AQUA");
    assertEquals(
        account.getBalances()[1].getAssetIssuer(),
        "GBNZILSTVQZ4R7IKQDGHYGY2QXL5QOFJYQMXPKWRRM5PAV7Y4M67AQUA");
    assertEquals(
        account.getBalances()[1].getAsset(),
        new TrustLineAsset(
            Asset.create("AQUA:GBNZILSTVQZ4R7IKQDGHYGY2QXL5QOFJYQMXPKWRRM5PAV7Y4M67AQUA")));

    assertEquals(account.getBalances()[6].getBalance(), "10025.7670792");
    assertEquals(account.getBalances()[6].getBuyingLiabilities(), "10000.0000000");
    assertEquals(account.getBalances()[6].getSellingLiabilities(), "25.1000000");
    assertEquals(account.getBalances()[6].getAssetType(), "native");
    assertEquals(account.getBalances()[6].getAsset(), new TrustLineAsset(new AssetTypeNative()));

    assertEquals(account.getSigners().length, 3);
    assertEquals(
        account.getSigners()[0].getKey(),
        "GDA2KZG7UB3C3KU4RYRGCQI6JPV2I2FYBWT434MQHPE36FH7CWGVR5X3");
    assertEquals(account.getSigners()[0].getType(), "ed25519_public_key");
    assertEquals(account.getSigners()[0].getWeight(), 255);
    assertNull(account.getSigners()[0].getSponsor());
    assertEquals(
        account.getSigners()[1].getKey(),
        "GASAK3DMPMVB4G4447P32EA4PT5UCDLVEHFY2NRS32QXDVPIBUVXUWF4");
    assertEquals(account.getSigners()[1].getType(), "ed25519_public_key");
    assertEquals(account.getSigners()[1].getWeight(), 1);
    assertNull(account.getSigners()[1].getSponsor());
    assertEquals(
        account.getSigners()[2].getKey(),
        "GDZMB4BTSFCRDXWQOG43WGX3Z4FUCEYJRTTQPXDR27PNNLU6RJT5V7LQ");
    assertEquals(account.getSigners()[2].getType(), "ed25519_public_key");
    assertEquals(account.getSigners()[2].getWeight(), 10);
    assertNull(account.getSigners()[2].getSponsor());

    assertEquals(account.getData().keySet().size(), 2);
    assertEquals(account.getData().get("v1"), "djEgdmFsdWU=");
    assertArrayEquals(account.getData().getDecoded("v1"), "v1 value".getBytes());
    assertEquals(account.getData().get("v2"), "djIgdmFsdWU=");
    assertArrayEquals(account.getData().getDecoded("v2"), "v2 value".getBytes());

    assertEquals(account.getNumSponsoring().longValue(), 1);
    assertEquals(account.getNumSponsored().longValue(), 2);
    assertNull(account.getSponsor());
    assertEquals(
        account.getPagingToken(), "GDA2KZG7UB3C3KU4RYRGCQI6JPV2I2FYBWT434MQHPE36FH7CWGVR5X3");
  }

  @Test
  public void testTransactionBuilderAccount() throws IOException {
    String filePath = "src/test/resources/account.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountResponse account = GsonSingleton.getInstance().fromJson(json, AccountResponse.class);

    assertEquals(
        account.getAccountId(), "GDA2KZG7UB3C3KU4RYRGCQI6JPV2I2FYBWT434MQHPE36FH7CWGVR5X3");
    assertEquals(
        account.getKeyPair(),
        KeyPair.fromAccountId("GDA2KZG7UB3C3KU4RYRGCQI6JPV2I2FYBWT434MQHPE36FH7CWGVR5X3"));
    assertEquals(account.getSequenceNumber().longValue(), 160470889244710503L);
    assertEquals(account.getIncrementedSequenceNumber().longValue(), 160470889244710504L);
    assertEquals(account.getSequenceNumber().longValue(), 160470889244710503L);
    account.incrementSequenceNumber();
    assertEquals(account.getSequenceNumber().longValue(), 160470889244710504L);
    account.incrementSequenceNumber();
    assertEquals(account.getSequenceNumber().longValue(), 160470889244710505L);
    account.setSequenceNumber(1L);
    assertEquals(account.getSequenceNumber().longValue(), 1L);
    account.incrementSequenceNumber();
    assertEquals(account.getSequenceNumber().longValue(), 2L);
  }
}
