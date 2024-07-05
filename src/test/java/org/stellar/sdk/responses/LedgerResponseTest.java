package org.stellar.sdk.responses;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;

public class LedgerResponseTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/ledger.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    LedgerResponse ledgerResponse =
        GsonSingleton.getInstance().fromJson(json, LedgerResponse.class);

    assertEquals(
        "c0e25db186d08e5614018def632a730a6ea30ed6b8d438148c884bd503d08736", ledgerResponse.getId());
    assertEquals("225166924667944960", ledgerResponse.getPagingToken());
    assertEquals(
        "c0e25db186d08e5614018def632a730a6ea30ed6b8d438148c884bd503d08736",
        ledgerResponse.getHash());
    assertEquals(
        "fa3c8170d9ab34e0a7d7ad95a903c412330cd92c0a1fa2e814b0271f37ac7a31",
        ledgerResponse.getPrevHash());
    assertEquals(52425760L, ledgerResponse.getSequence().longValue());
    assertEquals(140, ledgerResponse.getSuccessfulTransactionCount().intValue());
    assertEquals(276, ledgerResponse.getFailedTransactionCount().intValue());
    assertEquals(553, ledgerResponse.getOperationCount().intValue());
    assertEquals(864, ledgerResponse.getTxSetOperationCount().intValue());
    assertEquals("2024-07-05T00:33:24Z", ledgerResponse.getClosedAt());
    assertEquals("105443902087.3472865", ledgerResponse.getTotalCoins());
    assertEquals("4761068.4157944", ledgerResponse.getFeePool());
    assertEquals(100L, ledgerResponse.getBaseFeeInStroops().longValue());
    assertEquals(5000000L, ledgerResponse.getBaseReserveInStroops().longValue());
    assertEquals(1000, ledgerResponse.getMaxTxSetSize().intValue());
    assertEquals(21, ledgerResponse.getProtocolVersion().intValue());
    assertEquals(
        "AAAAFfo8gXDZqzTgp9etlakDxBIzDNksCh+i6BSwJx83rHoxo2USJkelS5sBUL80x6CbI3Q0WE5IHtkRkMGlg4bRDvMAAAAAZoc/VAAAAAAAAAABAAAAAD8yPEEcFI+mC4b5mkYsVzg64Eg1bwhZsrAJCPlB4VggAAAAQEBGUSF33J96dtBNm6wv+2bxqRaTk9cHUS3/s3m8DCK+dH/1T2YvwbQk6vtFPKtN7Z63gDnjRmhxSuxlSoOj9QJwHuCQkysepkQeC0P8tX6fyHA+Fa4lqao+J6PVjP7uFWPnxoSf9IbsJBlRYgKYuJ+ynHoVPIXLeuuKClMSe5aYAx/0IA6iHrPseVthAAArTTnqr/gAAAEWAAAAAF3/z8AAAABkAExLQAAAA+gc3Fmsu3J3TE9b1G+5Ze6DMUnKiDiicHnufJSvPeIzLyH84BFz3wV03irgkh9ALgDdzTHBVYw8W2gdG0YCBezXWo9Ff+l9yuwI+kO3FhlXp9K0E/ihJfYsd3r206TA8JbJei8VfTB2djpxiSuf3UjlaPtJZQ2ais6Yr4LPGckLlQAAAAA=",
        ledgerResponse.getHeaderXdr());

    assertEquals(ledgerResponse.getHeaderXdr(), ledgerResponse.parseHeaderXdr().toXdrBase64());
  }
}
