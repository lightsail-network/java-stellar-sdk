package org.stellar.sdk.responses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.Claimant;
import org.stellar.sdk.Predicate;
import org.stellar.sdk.responses.gson.GsonSingleton;
import org.stellar.sdk.xdr.TimePoint;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

public class ClaimableBalanceResponseTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/claimable_balance.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ClaimableBalanceResponse claimableBalanceResponse =
        GsonSingleton.getInstance().fromJson(json, ClaimableBalanceResponse.class);
    assertEquals(
        "000000002a409ac949579731ca1f89d8e9edca661861219bd278b2fccb451bd2643eb795",
        claimableBalanceResponse.getId());
    assertEquals(new AssetTypeNative(), claimableBalanceResponse.getAsset());
    assertEquals("1.2345000", claimableBalanceResponse.getAmount());
    assertEquals(
        "GCS4PXWRDKC5445PEKTJHIOEOVTCIQK2YMGPXDZLDXUIPK4HOHLQVYXL",
        claimableBalanceResponse.getSponsor());
    assertEquals(52442023L, claimableBalanceResponse.getLastModifiedLedger().longValue());
    assertEquals("2024-07-06T02:39:03Z", claimableBalanceResponse.getLastModifiedTime());
    assertFalse(claimableBalanceResponse.getFlags().getClawbackEnabled());
    Claimant claimant1 =
        new Claimant(
            "GDMTVHLWJTHSUDMZVVMXXH6VJHA2ZV3HNG5LYNAZ6RTWB7GISM6PGTUV",
            new Predicate.And(
                new Predicate.Or(
                    new Predicate.AbsBefore(600),
                    new Predicate.Or(
                        new Predicate.Unconditional(),
                        new Predicate.AbsBefore(
                            new TimePoint(new Uint64(new XdrUnsignedHyperInteger(1720233843L)))))),
                new Predicate.Unconditional()));
    Claimant claimant2 =
        new Claimant(
            "GCS4PXWRDKC5445PEKTJHIOEOVTCIQK2YMGPXDZLDXUIPK4HOHLQVYXL",
            new Predicate.Unconditional());

    List<Claimant> claimants = Arrays.asList(claimant1, claimant2);
    assertEquals(claimants, claimableBalanceResponse.getClaimants());
  }
}
