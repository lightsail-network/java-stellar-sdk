package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import org.junit.Test;
import org.stellar.sdk.responses.GsonSingleton;

public class GetTransactionDeserializerTest {
  @Test
  public void testDeserializeSuccess() throws IOException {
    SorobanRpcResponse<GetTransactionResponse> getTransactionResponse =
        GsonSingleton.getInstance()
            .fromJson(
                jsonSuccess,
                new TypeToken<SorobanRpcResponse<GetTransactionResponse>>() {}.getType());
    assertEquals(
        getTransactionResponse.getResult().getStatus(),
        GetTransactionResponse.GetTransactionStatus.SUCCESS);
    assertEquals(getTransactionResponse.getResult().getLatestLedger().longValue(), 79289L);
    assertEquals(
        getTransactionResponse.getResult().getLatestLedgerCloseTime().longValue(), 1690387240L);
    assertEquals(getTransactionResponse.getResult().getOldestLedger().longValue(), 77850L);
    assertEquals(
        getTransactionResponse.getResult().getOldestLedgerCloseTime().longValue(), 1690379694L);
    assertEquals(getTransactionResponse.getResult().getApplicationOrder().intValue(), 1);
    assertEquals(
        getTransactionResponse.getResult().getEnvelopeXdr(),
        "AAAAAgAAAADGFY14/R1KD0VGtTbi5Yp4d7LuMW0iQbLM/AUiGKj5owCpsoQAJY3OAAAjqgAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAGAAAAAAAAAABhhOwI+RL18Zpk7cqI5pRRf0L96jE8i+0x3ekhuBh2cUAAAARc2V0X2N1cnJlbmN5X3JhdGUAAAAAAAACAAAADwAAAANldXIAAAAACQAAAAAAAAAAAAAAAAARCz4AAAABAAAAAAAAAAAAAAABhhOwI+RL18Zpk7cqI5pRRf0L96jE8i+0x3ekhuBh2cUAAAARc2V0X2N1cnJlbmN5X3JhdGUAAAAAAAACAAAADwAAAANldXIAAAAACQAAAAAAAAAAAAAAAAARCz4AAAAAAAAAAQAAAAAAAAABAAAAB4408vVXuLU3mry897TfPpYjjsSN7n42REos241RddYdAAAAAQAAAAYAAAABhhOwI+RL18Zpk7cqI5pRRf0L96jE8i+0x3ekhuBh2cUAAAAUAAAAAQFvcYAAAImAAAAHxAAAAAAAAAACAAAAARio+aMAAABATbFMyom/TUz87wHex0LoYZA8jbNJkXbaDSgmOdk+wSBFJuMuta+/vSlro0e0vK2+1FqD/zWHZeYig4pKmM3rDA==");
    assertEquals(
        getTransactionResponse.getResult().getResultXdr(),
        "AAAAAAABNCwAAAAAAAAAAQAAAAAAAAAYAAAAAJhEDjNV0Jj46jrxCj87qJ6JaYKJN4c+p5tvapkLwrn8AAAAAA==");
    assertEquals(
        getTransactionResponse.getResult().getResultMetaXdr(),
        "AAAAAwAAAAAAAAACAAAAAwABNbYAAAAAAAAAANNgojN9rS5sqnuqM5wX5k7yyNC63OjWIe4OHx6yL2r7AAAAF0h1s9QAATW1AAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAQABNbYAAAAAAAAAANNgojN9rS5sqnuqM5wX5k7yyNC63OjWIe4OHx6yL2r7AAAAF0h1s9QAATW1AAAAAQAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAAAAAAAAMAAAAAAAE1tgAAAABkwUMZAAAAAAAAAAEAAAAAAAAAAgAAAAMAATW2AAAAAAAAAADTYKIzfa0ubKp7qjOcF+ZO8sjQutzo1iHuDh8esi9q+wAAABdIdbPUAAE1tQAAAAEAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAADAAAAAAABNbYAAAAAZMFDGQAAAAAAAAABAAE1tgAAAAAAAAAA02CiM32tLmyqe6oznBfmTvLI0Lrc6NYh7g4fHrIvavsAAAAXSHWz/AABNbUAAAABAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAACAAAAAAAAAAAAAAAAAAAAAwAAAAAAATW2AAAAAGTBQxkAAAAAAAAAAQAAAAAAAAAAAAAADgAAAAZUb2tlbkEAAAAAAAIAAAABAAAAAAAAAAAAAAACAAAAAAAAAAMAAAAPAAAAB2ZuX2NhbGwAAAAADQAAACC9x/ldchaDQwaY3uK3IcXmke/4UTUtggE9g4egPM+sawAAAA8AAAAEbmFtZQAAAAEAAAABAAAAAAAAAAG9x/ldchaDQwaY3uK3IcXmke/4UTUtggE9g4egPM+sawAAAAIAAAAAAAAAAgAAAA8AAAAJZm5fcmV0dXJuAAAAAAAADwAAAARuYW1lAAAADgAAAAZUb2tlbkEAAA==");
    assertEquals(getTransactionResponse.getResult().getLedger().longValue(), 79286L);
    assertEquals(getTransactionResponse.getResult().getCreatedAt().longValue(), 1690387225L);
    assertNull(getTransactionResponse.getResult().getFeeBump());
    assertEquals(
        getTransactionResponse.getResult().parseEnvelopeXdr().toXdrBase64(),
        getTransactionResponse.getResult().getEnvelopeXdr());
    assertEquals(
        getTransactionResponse.getResult().parseResultXdr().toXdrBase64(),
        getTransactionResponse.getResult().getResultXdr());
    assertEquals(
        getTransactionResponse.getResult().parseResultMetaXdr().toXdrBase64(),
        getTransactionResponse.getResult().getResultMetaXdr());
  }

  @Test
  public void testDeserializeFailed() {
    SorobanRpcResponse<GetTransactionResponse> getTransactionResponse =
        GsonSingleton.getInstance()
            .fromJson(
                jsonFailed,
                new TypeToken<SorobanRpcResponse<GetTransactionResponse>>() {}.getType());
    assertEquals(
        getTransactionResponse.getResult().getStatus(),
        GetTransactionResponse.GetTransactionStatus.FAILED);
    assertEquals(getTransactionResponse.getResult().getLatestLedger().longValue(), 79289L);
    assertEquals(
        getTransactionResponse.getResult().getLatestLedgerCloseTime().longValue(), 1690387240L);
    assertEquals(getTransactionResponse.getResult().getOldestLedger().longValue(), 77850L);
    assertEquals(
        getTransactionResponse.getResult().getOldestLedgerCloseTime().longValue(), 1690379694L);
    assertEquals(getTransactionResponse.getResult().getApplicationOrder().intValue(), 1);
    assertEquals(
        getTransactionResponse.getResult().getEnvelopeXdr(),
        "AAAAAgAAAADGFY14/R1KD0VGtTbi5Yp4d7LuMW0iQbLM/AUiGKj5owCpsoQAJY3OAAAjqgAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAGAAAAAAAAAABhhOwI+RL18Zpk7cqI5pRRf0L96jE8i+0x3ekhuBh2cUAAAARc2V0X2N1cnJlbmN5X3JhdGUAAAAAAAACAAAADwAAAANldXIAAAAACQAAAAAAAAAAAAAAAAARCz4AAAABAAAAAAAAAAAAAAABhhOwI+RL18Zpk7cqI5pRRf0L96jE8i+0x3ekhuBh2cUAAAARc2V0X2N1cnJlbmN5X3JhdGUAAAAAAAACAAAADwAAAANldXIAAAAACQAAAAAAAAAAAAAAAAARCz4AAAAAAAAAAQAAAAAAAAABAAAAB4408vVXuLU3mry897TfPpYjjsSN7n42REos241RddYdAAAAAQAAAAYAAAABhhOwI+RL18Zpk7cqI5pRRf0L96jE8i+0x3ekhuBh2cUAAAAUAAAAAQFvcYAAAImAAAAHxAAAAAAAAAACAAAAARio+aMAAABATbFMyom/TUz87wHex0LoYZA8jbNJkXbaDSgmOdk+wSBFJuMuta+/vSlro0e0vK2+1FqD/zWHZeYig4pKmM3rDA==");
    assertEquals(
        getTransactionResponse.getResult().getResultXdr(),
        "AAAAAAABNCwAAAAAAAAAAQAAAAAAAAAYAAAAAJhEDjNV0Jj46jrxCj87qJ6JaYKJN4c+p5tvapkLwrn8AAAAAA==");
    assertEquals(
        getTransactionResponse.getResult().getResultMetaXdr(),
        "AAAAAwAAAAAAAAACAAAAAwABNbYAAAAAAAAAANNgojN9rS5sqnuqM5wX5k7yyNC63OjWIe4OHx6yL2r7AAAAF0h1s9QAATW1AAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAQABNbYAAAAAAAAAANNgojN9rS5sqnuqM5wX5k7yyNC63OjWIe4OHx6yL2r7AAAAF0h1s9QAATW1AAAAAQAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAAAAAAAAMAAAAAAAE1tgAAAABkwUMZAAAAAAAAAAEAAAAAAAAAAgAAAAMAATW2AAAAAAAAAADTYKIzfa0ubKp7qjOcF+ZO8sjQutzo1iHuDh8esi9q+wAAABdIdbPUAAE1tQAAAAEAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAADAAAAAAABNbYAAAAAZMFDGQAAAAAAAAABAAE1tgAAAAAAAAAA02CiM32tLmyqe6oznBfmTvLI0Lrc6NYh7g4fHrIvavsAAAAXSHWz/AABNbUAAAABAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAACAAAAAAAAAAAAAAAAAAAAAwAAAAAAATW2AAAAAGTBQxkAAAAAAAAAAQAAAAAAAAAAAAAADgAAAAZUb2tlbkEAAAAAAAIAAAABAAAAAAAAAAAAAAACAAAAAAAAAAMAAAAPAAAAB2ZuX2NhbGwAAAAADQAAACC9x/ldchaDQwaY3uK3IcXmke/4UTUtggE9g4egPM+sawAAAA8AAAAEbmFtZQAAAAEAAAABAAAAAAAAAAG9x/ldchaDQwaY3uK3IcXmke/4UTUtggE9g4egPM+sawAAAAIAAAAAAAAAAgAAAA8AAAAJZm5fcmV0dXJuAAAAAAAADwAAAARuYW1lAAAADgAAAAZUb2tlbkEAAA==");
    assertEquals(getTransactionResponse.getResult().getLedger().longValue(), 79286L);
    assertEquals(getTransactionResponse.getResult().getCreatedAt().longValue(), 1690387225L);
    assertNull(getTransactionResponse.getResult().getFeeBump());
    assertNotNull(getTransactionResponse.getResult().parseEnvelopeXdr());
    assertNotNull(getTransactionResponse.getResult().parseResultXdr());
    assertNotNull(getTransactionResponse.getResult().parseResultMetaXdr());
  }

  @Test
  public void testDeserializeNotFound() {
    SorobanRpcResponse<GetTransactionResponse> getTransactionResponse =
        GsonSingleton.getInstance()
            .fromJson(
                jsonNotFound,
                new TypeToken<SorobanRpcResponse<GetTransactionResponse>>() {}.getType());
    assertEquals(
        getTransactionResponse.getResult().getStatus(),
        GetTransactionResponse.GetTransactionStatus.NOT_FOUND);
    assertEquals(getTransactionResponse.getResult().getLatestLedger().longValue(), 79285L);
    assertEquals(
        getTransactionResponse.getResult().getLatestLedgerCloseTime().longValue(), 1690387220L);
    assertEquals(getTransactionResponse.getResult().getOldestLedger().longValue(), 77846L);
    assertEquals(
        getTransactionResponse.getResult().getOldestLedgerCloseTime().longValue(), 1690379672L);

    assertNull(getTransactionResponse.getResult().getApplicationOrder());
    assertNull(getTransactionResponse.getResult().getEnvelopeXdr());
    assertNull(getTransactionResponse.getResult().getResultXdr());
    assertNull(getTransactionResponse.getResult().getResultMetaXdr());
    assertNull(getTransactionResponse.getResult().getLedger());
    assertNull(getTransactionResponse.getResult().getCreatedAt());
    assertNull(getTransactionResponse.getResult().getFeeBump());
    assertNull(getTransactionResponse.getResult().parseEnvelopeXdr());
    assertNull(getTransactionResponse.getResult().parseResultXdr());
    assertNull(getTransactionResponse.getResult().parseResultMetaXdr());
  }

  String jsonSuccess =
      "{\n"
          + "    \"jsonrpc\": \"2.0\",\n"
          + "    \"id\": \"198cb1a8-9104-4446-a269-88bf000c2721\",\n"
          + "    \"result\": {\n"
          + "        \"status\": \"SUCCESS\",\n"
          + "        \"latestLedger\": \"79289\",\n"
          + "        \"latestLedgerCloseTime\": \"1690387240\",\n"
          + "        \"oldestLedger\": \"77850\",\n"
          + "        \"oldestLedgerCloseTime\": \"1690379694\",\n"
          + "        \"applicationOrder\": 1,\n"
          + "        \"envelopeXdr\": \"AAAAAgAAAADGFY14/R1KD0VGtTbi5Yp4d7LuMW0iQbLM/AUiGKj5owCpsoQAJY3OAAAjqgAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAGAAAAAAAAAABhhOwI+RL18Zpk7cqI5pRRf0L96jE8i+0x3ekhuBh2cUAAAARc2V0X2N1cnJlbmN5X3JhdGUAAAAAAAACAAAADwAAAANldXIAAAAACQAAAAAAAAAAAAAAAAARCz4AAAABAAAAAAAAAAAAAAABhhOwI+RL18Zpk7cqI5pRRf0L96jE8i+0x3ekhuBh2cUAAAARc2V0X2N1cnJlbmN5X3JhdGUAAAAAAAACAAAADwAAAANldXIAAAAACQAAAAAAAAAAAAAAAAARCz4AAAAAAAAAAQAAAAAAAAABAAAAB4408vVXuLU3mry897TfPpYjjsSN7n42REos241RddYdAAAAAQAAAAYAAAABhhOwI+RL18Zpk7cqI5pRRf0L96jE8i+0x3ekhuBh2cUAAAAUAAAAAQFvcYAAAImAAAAHxAAAAAAAAAACAAAAARio+aMAAABATbFMyom/TUz87wHex0LoYZA8jbNJkXbaDSgmOdk+wSBFJuMuta+/vSlro0e0vK2+1FqD/zWHZeYig4pKmM3rDA==\",\n"
          + "        \"resultXdr\": \"AAAAAAABNCwAAAAAAAAAAQAAAAAAAAAYAAAAAJhEDjNV0Jj46jrxCj87qJ6JaYKJN4c+p5tvapkLwrn8AAAAAA==\",\n"
          + "        \"resultMetaXdr\": \"AAAAAwAAAAAAAAACAAAAAwABNbYAAAAAAAAAANNgojN9rS5sqnuqM5wX5k7yyNC63OjWIe4OHx6yL2r7AAAAF0h1s9QAATW1AAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAQABNbYAAAAAAAAAANNgojN9rS5sqnuqM5wX5k7yyNC63OjWIe4OHx6yL2r7AAAAF0h1s9QAATW1AAAAAQAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAAAAAAAAMAAAAAAAE1tgAAAABkwUMZAAAAAAAAAAEAAAAAAAAAAgAAAAMAATW2AAAAAAAAAADTYKIzfa0ubKp7qjOcF+ZO8sjQutzo1iHuDh8esi9q+wAAABdIdbPUAAE1tQAAAAEAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAADAAAAAAABNbYAAAAAZMFDGQAAAAAAAAABAAE1tgAAAAAAAAAA02CiM32tLmyqe6oznBfmTvLI0Lrc6NYh7g4fHrIvavsAAAAXSHWz/AABNbUAAAABAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAACAAAAAAAAAAAAAAAAAAAAAwAAAAAAATW2AAAAAGTBQxkAAAAAAAAAAQAAAAAAAAAAAAAADgAAAAZUb2tlbkEAAAAAAAIAAAABAAAAAAAAAAAAAAACAAAAAAAAAAMAAAAPAAAAB2ZuX2NhbGwAAAAADQAAACC9x/ldchaDQwaY3uK3IcXmke/4UTUtggE9g4egPM+sawAAAA8AAAAEbmFtZQAAAAEAAAABAAAAAAAAAAG9x/ldchaDQwaY3uK3IcXmke/4UTUtggE9g4egPM+sawAAAAIAAAAAAAAAAgAAAA8AAAAJZm5fcmV0dXJuAAAAAAAADwAAAARuYW1lAAAADgAAAAZUb2tlbkEAAA==\",\n"
          + "        \"ledger\": \"79286\",\n"
          + "        \"createdAt\": \"1690387225\"\n"
          + "    }\n"
          + "}";

  String jsonNotFound =
      "{\n"
          + "    \"jsonrpc\": \"2.0\",\n"
          + "    \"id\": \"198cb1a8-9104-4446-a269-88bf000c2721\",\n"
          + "    \"result\": {\n"
          + "        \"status\": \"NOT_FOUND\",\n"
          + "        \"latestLedger\": \"79285\",\n"
          + "        \"latestLedgerCloseTime\": \"1690387220\",\n"
          + "        \"oldestLedger\": \"77846\",\n"
          + "        \"oldestLedgerCloseTime\": \"1690379672\"\n"
          + "    }\n"
          + "}";

  String jsonFailed =
      "{\n"
          + "    \"jsonrpc\": \"2.0\",\n"
          + "    \"id\": \"198cb1a8-9104-4446-a269-88bf000c2721\",\n"
          + "    \"result\": {\n"
          + "        \"status\": \"FAILED\",\n"
          + "        \"latestLedger\": \"79289\",\n"
          + "        \"latestLedgerCloseTime\": \"1690387240\",\n"
          + "        \"oldestLedger\": \"77850\",\n"
          + "        \"oldestLedgerCloseTime\": \"1690379694\",\n"
          + "        \"applicationOrder\": 1,\n"
          + "        \"envelopeXdr\": \"AAAAAgAAAADGFY14/R1KD0VGtTbi5Yp4d7LuMW0iQbLM/AUiGKj5owCpsoQAJY3OAAAjqgAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAGAAAAAAAAAABhhOwI+RL18Zpk7cqI5pRRf0L96jE8i+0x3ekhuBh2cUAAAARc2V0X2N1cnJlbmN5X3JhdGUAAAAAAAACAAAADwAAAANldXIAAAAACQAAAAAAAAAAAAAAAAARCz4AAAABAAAAAAAAAAAAAAABhhOwI+RL18Zpk7cqI5pRRf0L96jE8i+0x3ekhuBh2cUAAAARc2V0X2N1cnJlbmN5X3JhdGUAAAAAAAACAAAADwAAAANldXIAAAAACQAAAAAAAAAAAAAAAAARCz4AAAAAAAAAAQAAAAAAAAABAAAAB4408vVXuLU3mry897TfPpYjjsSN7n42REos241RddYdAAAAAQAAAAYAAAABhhOwI+RL18Zpk7cqI5pRRf0L96jE8i+0x3ekhuBh2cUAAAAUAAAAAQFvcYAAAImAAAAHxAAAAAAAAAACAAAAARio+aMAAABATbFMyom/TUz87wHex0LoYZA8jbNJkXbaDSgmOdk+wSBFJuMuta+/vSlro0e0vK2+1FqD/zWHZeYig4pKmM3rDA==\",\n"
          + "        \"resultXdr\": \"AAAAAAABNCwAAAAAAAAAAQAAAAAAAAAYAAAAAJhEDjNV0Jj46jrxCj87qJ6JaYKJN4c+p5tvapkLwrn8AAAAAA==\",\n"
          + "        \"resultMetaXdr\": \"AAAAAwAAAAAAAAACAAAAAwABNbYAAAAAAAAAANNgojN9rS5sqnuqM5wX5k7yyNC63OjWIe4OHx6yL2r7AAAAF0h1s9QAATW1AAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAQABNbYAAAAAAAAAANNgojN9rS5sqnuqM5wX5k7yyNC63OjWIe4OHx6yL2r7AAAAF0h1s9QAATW1AAAAAQAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAAAAAAAAMAAAAAAAE1tgAAAABkwUMZAAAAAAAAAAEAAAAAAAAAAgAAAAMAATW2AAAAAAAAAADTYKIzfa0ubKp7qjOcF+ZO8sjQutzo1iHuDh8esi9q+wAAABdIdbPUAAE1tQAAAAEAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAADAAAAAAABNbYAAAAAZMFDGQAAAAAAAAABAAE1tgAAAAAAAAAA02CiM32tLmyqe6oznBfmTvLI0Lrc6NYh7g4fHrIvavsAAAAXSHWz/AABNbUAAAABAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAACAAAAAAAAAAAAAAAAAAAAAwAAAAAAATW2AAAAAGTBQxkAAAAAAAAAAQAAAAAAAAAAAAAADgAAAAZUb2tlbkEAAAAAAAIAAAABAAAAAAAAAAAAAAACAAAAAAAAAAMAAAAPAAAAB2ZuX2NhbGwAAAAADQAAACC9x/ldchaDQwaY3uK3IcXmke/4UTUtggE9g4egPM+sawAAAA8AAAAEbmFtZQAAAAEAAAABAAAAAAAAAAG9x/ldchaDQwaY3uK3IcXmke/4UTUtggE9g4egPM+sawAAAAIAAAAAAAAAAgAAAA8AAAAJZm5fcmV0dXJuAAAAAAAADwAAAARuYW1lAAAADgAAAAZUb2tlbkEAAA==\",\n"
          + "        \"ledger\": \"79286\",\n"
          + "        \"createdAt\": \"1690387225\"\n"
          + "    }\n"
          + "}";
}
