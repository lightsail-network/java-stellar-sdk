package org.stellar.sdk.responses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.Memo;

public class TransactionResponseTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/transaction.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TransactionResponse transactionResponse =
        GsonSingleton.getInstance().fromJson(json, TransactionResponse.class);

    // Assert _links
    assertEquals(
        "https://horizon.stellar.org/transactions/a434302ea03b42dd00614e258e6b7cdce5dc8a9d7381b1cba8844b75df4f1486",
        transactionResponse.getLinks().getSelf().getHref());
    assertEquals(
        "https://horizon.stellar.org/accounts/GANGI6CEX7L52QPPH5MK2SDZE5WDESSO24HIMVHO5FCIGKQWKLAF5E7O",
        transactionResponse.getLinks().getAccount().getHref());
    assertEquals(
        "https://horizon.stellar.org/ledgers/52429011",
        transactionResponse.getLinks().getLedger().getHref());
    assertEquals(
        "https://horizon.stellar.org/transactions/a434302ea03b42dd00614e258e6b7cdce5dc8a9d7381b1cba8844b75df4f1486/operations{?cursor,limit,order}",
        transactionResponse.getLinks().getOperations().getHref());
    assertTrue(transactionResponse.getLinks().getOperations().isTemplated());
    assertEquals(
        "https://horizon.stellar.org/transactions/a434302ea03b42dd00614e258e6b7cdce5dc8a9d7381b1cba8844b75df4f1486/effects{?cursor,limit,order}",
        transactionResponse.getLinks().getEffects().getHref());
    assertTrue(transactionResponse.getLinks().getEffects().isTemplated());
    assertEquals(
        "https://horizon.stellar.org/transactions?order=asc&cursor=225180887607500800",
        transactionResponse.getLinks().getPrecedes().getHref());
    assertEquals(
        "https://horizon.stellar.org/transactions?order=desc&cursor=225180887607500800",
        transactionResponse.getLinks().getSucceeds().getHref());
    assertEquals(
        "https://horizon.stellar.org/transactions/a434302ea03b42dd00614e258e6b7cdce5dc8a9d7381b1cba8844b75df4f1486",
        transactionResponse.getLinks().getSelf().getHref());

    assertEquals(
        "a434302ea03b42dd00614e258e6b7cdce5dc8a9d7381b1cba8844b75df4f1486",
        transactionResponse.getId());
    assertEquals("225180887607500800", transactionResponse.getPagingToken());
    assertTrue(transactionResponse.getSuccessful());
    assertEquals(
        "a434302ea03b42dd00614e258e6b7cdce5dc8a9d7381b1cba8844b75df4f1486",
        transactionResponse.getHash());
    assertEquals(52429011L, transactionResponse.getLedger().longValue());
    assertEquals("2024-07-05T05:51:31Z", transactionResponse.getCreatedAt());
    assertEquals(
        "GANGI6CEX7L52QPPH5MK2SDZE5WDESSO24HIMVHO5FCIGKQWKLAF5E7O",
        transactionResponse.getSourceAccount());
    assertEquals(224884019467125027L, transactionResponse.getSourceAccountSequence().longValue());
    assertEquals(
        "GANGI6CEX7L52QPPH5MK2SDZE5WDESSO24HIMVHO5FCIGKQWKLAF5E7O",
        transactionResponse.getFeeAccount());
    assertEquals(100, transactionResponse.getFeeCharged().longValue());
    assertEquals(100, transactionResponse.getMaxFee().longValue());
    assertEquals(1, transactionResponse.getOperationCount().longValue());
    assertEquals(
        transactionResponse.parseEnvelopeXdr().toXdrBase64(), transactionResponse.getEnvelopeXdr());
    assertEquals(
        transactionResponse.parseResultXdr().toXdrBase64(), transactionResponse.getResultXdr());
    assertEquals(
        transactionResponse.parseResultMetaXdr().toXdrBase64(),
        transactionResponse.getResultMetaXdr());
    assertEquals(
        transactionResponse.parseFeeMetaXdr().toXdrBase64(), transactionResponse.getFeeMetaXdr());
    assertEquals("0,075% Daily for Holders", transactionResponse.getMemoValue());
    assertEquals("MCwwNzUlIERhaWx5IGZvciBIb2xkZXJz", transactionResponse.getMemoBytes());
    assertEquals("text", transactionResponse.getMemoType());
    assertEquals(2, transactionResponse.getSignatures().size());
    assertEquals(
        "ua2Hf0WOjxMLTnCflk4DDiWi7auTEBorqTXqUdheURyxgdn936BrlsKA70pX+xsppkBQIL0q8UimVeRd98HFDQ==",
        transactionResponse.getSignatures().get(0));
    assertEquals(
        "LbDjlPdLbiyGQnu7JGX8/nEPzpd+LupNjGZvy6Hw8RzCVvmipuclxOVRXa4mzb6DMyWKM20BusKbrA77ViwlDg==",
        transactionResponse.getSignatures().get(1));
    assertEquals(0, transactionResponse.getPreconditions().getTimeBounds().getMinTime());
    assertEquals(1720158787, transactionResponse.getPreconditions().getTimeBounds().getMaxTime());
    assertEquals(Memo.text("0,075% Daily for Holders"), transactionResponse.getMemo());
  }

  @Test
  public void testDeserializeWithTransactionFailed() throws IOException {
    String filePath = "src/test/resources/responses/transaction_failed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TransactionResponse transactionResponse =
        GsonSingleton.getInstance().fromJson(json, TransactionResponse.class);

    // Assert transaction response fields
    assertEquals(
        "ced549af061dc39758ce222f78f027e82b5077176a4e2efbeb4dc04086150b7d",
        transactionResponse.getId());
    assertEquals("225181329989492736", transactionResponse.getPagingToken());
    assertFalse(transactionResponse.getSuccessful());
    assertEquals(
        "ced549af061dc39758ce222f78f027e82b5077176a4e2efbeb4dc04086150b7d",
        transactionResponse.getHash());
    assertEquals(52429114, transactionResponse.getLedger().longValue());
    assertEquals("2024-07-05T06:01:20Z", transactionResponse.getCreatedAt());
    assertEquals(
        "GAC6MRNVZNVFKAQRFGVJBNZ734T3HPJH3OVTKY433STDPZVRDI75UDLD",
        transactionResponse.getSourceAccount());
    assertEquals(169774989149474823L, transactionResponse.getSourceAccountSequence().longValue());
    assertEquals(
        "GAC6MRNVZNVFKAQRFGVJBNZ734T3HPJH3OVTKY433STDPZVRDI75UDLD",
        transactionResponse.getFeeAccount());
    assertEquals(100, transactionResponse.getFeeCharged().intValue());
    assertEquals(101, transactionResponse.getMaxFee().intValue());
    assertEquals(1, transactionResponse.getOperationCount().intValue());
    assertEquals(
        transactionResponse.parseEnvelopeXdr().toXdrBase64(), transactionResponse.getEnvelopeXdr());
    assertEquals(
        transactionResponse.parseResultXdr().toXdrBase64(), transactionResponse.getResultXdr());
    assertEquals(
        transactionResponse.parseResultMetaXdr().toXdrBase64(),
        transactionResponse.getResultMetaXdr());
    assertEquals(
        transactionResponse.parseFeeMetaXdr().toXdrBase64(), transactionResponse.getFeeMetaXdr());
    assertEquals("none", transactionResponse.getMemoType());
    assertEquals(2, transactionResponse.getSignatures().size());
    assertEquals(
        "KO5WWF+Xob1yQmwTPgbBs4lazMfG0bsumNrrIaOELxbjTJ/9TJzYAWXIw/Fje6M/KpzXIDth5ql4acY0ws78Bw==",
        transactionResponse.getSignatures().get(0));
    assertEquals(
        "rWzMRzkbVOTXv6rUONbf343/TUK2BD/nzhS8qNRJuiCqnwkefo/EwraJkLT32nkZVw8TnWQEqhuFeXjrIo9YCQ==",
        transactionResponse.getSignatures().get(1));
    assertEquals(0, transactionResponse.getPreconditions().getTimeBounds().getMinTime());
    assertEquals(1720159290, transactionResponse.getPreconditions().getTimeBounds().getMaxTime());
    assertEquals(Memo.none(), transactionResponse.getMemo());
  }
}
