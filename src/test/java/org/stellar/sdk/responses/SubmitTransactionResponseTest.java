package org.stellar.sdk.responses;

import java.io.IOException;
import junit.framework.TestCase;
import org.junit.Test;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.TransactionResult;
import org.stellar.sdk.xdr.TransactionResultCode;

public class SubmitTransactionResponseTest extends TestCase {
  @Test
  public void testDeserializeTransactionFailureResponse() throws IOException {
    String json =
        "{\n"
            + "  \"type\": \"https://developers.stellar.org/api/errors/http-status-codes/horizon-specific/transaction-failed/\",\n"
            + "  \"title\": \"Transaction Failed\",\n"
            + "  \"status\": 400,\n"
            + "  \"detail\": \"The transaction failed when submitted to the stellar network. The `extras.result_codes` field on this response contains further details.  Descriptions of each code can be found at: https://developers.stellar.org/docs/start/list-of-operations/\",\n"
            + "  \"instance\": \"horizon-testnet-001.prd.stellar001.internal.stellar-ops.com/4elYz2fHhC-528285\",\n"
            + "  \"extras\": {\n"
            + "    \"envelope_xdr\": \"AAAAAKpmDL6Z4hvZmkTBkYpHftan4ogzTaO4XTB7joLgQnYYAAAAZAAAAAAABeoyAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAABAAAAAD3sEVVGZGi/NoC3ta/8f/YZKMzyi9ZJpOi0H47x7IqYAAAAAAAAAAAF9eEAAAAAAAAAAAA=\",\n"
            + "    \"result_codes\": {\n"
            + "      \"transaction\": \"tx_no_source_account\"\n"
            + "    },\n"
            + "    \"result_xdr\": \"AAAAAAAAAAD////4AAAAAA==\"\n"
            + "  }\n"
            + "}";

    SubmitTransactionResponse submitTransactionResponse =
        GsonSingleton.getInstance().fromJson(json, SubmitTransactionResponse.class);
    assertEquals(submitTransactionResponse.isSuccess(), false);
    assertEquals(
        submitTransactionResponse.getEnvelopeXdr().get(),
        "AAAAAKpmDL6Z4hvZmkTBkYpHftan4ogzTaO4XTB7joLgQnYYAAAAZAAAAAAABeoyAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAABAAAAAD3sEVVGZGi/NoC3ta/8f/YZKMzyi9ZJpOi0H47x7IqYAAAAAAAAAAAF9eEAAAAAAAAAAAA=");
    assertEquals(submitTransactionResponse.getResultXdr().get(), "AAAAAAAAAAD////4AAAAAA==");
    assertFalse(submitTransactionResponse.getOfferIdFromResult(0).isPresent());
    assertFalse(submitTransactionResponse.getDecodedTransactionResult().isPresent());
    assertEquals(
        submitTransactionResponse.getExtras().getResultCodes().getTransactionResultCode(),
        "tx_no_source_account");
  }

  @Test
  public void testDeserializeFeeBumpTransactionFailureResponse() throws IOException {
    String json =
        "{\n"
            + "  \"type\": \"https://developers.stellar.org/api/errors/http-status-codes/horizon-specific/transaction-failed/\",\n"
            + "  \"title\": \"Transaction Failed\",\n"
            + "  \"status\": 400,\n"
            + "  \"detail\": \"The transaction failed when submitted to the stellar network. The `extras.result_codes` field on this response contains further details.  Descriptions of each code can be found at: https://developers.stellar.org/docs/start/list-of-operations/\",\n"
            + "  \"instance\": \"horizon-testnet-001.prd.stellar001.internal.stellar-ops.com/4elYz2fHhC-528285\",\n"
            + "  \"extras\": {\n"
            + "    \"envelope_xdr\": \"AAAAAKpmDL6Z4hvZmkTBkYpHftan4ogzTaO4XTB7joLgQnYYAAAAZAAAAAAABeoyAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAABAAAAAD3sEVVGZGi/NoC3ta/8f/YZKMzyi9ZJpOi0H47x7IqYAAAAAAAAAAAF9eEAAAAAAAAAAAA=\",\n"
            + "    \"result_codes\": {\n"
            + "      \"transaction\": \"tx_fee_bump_inner_failed\",\n"
            + "      \"inner_transaction\": \"tx_no_source_account\"\n"
            + "    },\n"
            + "    \"result_xdr\": \"AAAAAAAAAMj////zxZjN9tKJLJ6diZuWBitlMJ/4MXX1Akm9M5CqZt10ogoAAAAAAAAAAP////8AAAABAAAAAAAAAAb/////AAAAAAAAAAA=\"\n"
            + "  }\n"
            + "}";

    SubmitTransactionResponse submitTransactionResponse =
        GsonSingleton.getInstance().fromJson(json, SubmitTransactionResponse.class);
    assertEquals(submitTransactionResponse.isSuccess(), false);
    assertEquals(
        submitTransactionResponse.getEnvelopeXdr().get(),
        "AAAAAKpmDL6Z4hvZmkTBkYpHftan4ogzTaO4XTB7joLgQnYYAAAAZAAAAAAABeoyAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAABAAAAAD3sEVVGZGi/NoC3ta/8f/YZKMzyi9ZJpOi0H47x7IqYAAAAAAAAAAAF9eEAAAAAAAAAAAA=");
    assertEquals(
        submitTransactionResponse.getResultXdr().get(),
        "AAAAAAAAAMj////zxZjN9tKJLJ6diZuWBitlMJ/4MXX1Akm9M5CqZt10ogoAAAAAAAAAAP////8AAAABAAAAAAAAAAb/////AAAAAAAAAAA=");
    assertFalse(submitTransactionResponse.getOfferIdFromResult(0).isPresent());
    assertFalse(submitTransactionResponse.getDecodedTransactionResult().isPresent());
    assertEquals(
        submitTransactionResponse.getExtras().getResultCodes().getTransactionResultCode(),
        "tx_fee_bump_inner_failed");
    assertEquals(
        submitTransactionResponse.getExtras().getResultCodes().getInnerTransactionResultCode(),
        "tx_no_source_account");
  }

  @Test
  public void testDeserializeOperationFailureResponse() throws IOException {
    String json =
        "{\n"
            + "  \"type\": \"https://developers.stellar.org/api/errors/http-status-codes/horizon-specific/transaction-failed/\",\n"
            + "  \"title\": \"Transaction Failed\",\n"
            + "  \"status\": 400,\n"
            + "  \"detail\": \"The transaction failed when submitted to the stellar network. The `extras.result_codes` field on this response contains further details.  Descriptions of each code can be found at: https://developers.stellar.org/docs/start/list-of-operations/\",\n"
            + "  \"instance\": \"horizon-testnet-001.prd.stellar001.internal.stellar-ops.com/4elYz2fHhC-528366\",\n"
            + "  \"extras\": {\n"
            + "    \"envelope_xdr\": \"AAAAAF2O0axA67+p2jMunG6G188kDSHIvqQ13d9l29YCSA/uAAAAZAAvvc0AAAABAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAABAAAAAD3sEVVGZGi/NoC3ta/8f/YZKMzyi9ZJpOi0H47x7IqYAAAAAAAAAAAF9eEAAAAAAAAAAAECSA/uAAAAQFuZVAjftHa+JZes1VxSk8naOfjjAz9V86mY1AZf8Ik6PtTsBpDsCfG57EYsq4jWyZcT+vhXyWsw5evF1ELqMw4=\",\n"
            + "    \"result_codes\": {\n"
            + "      \"transaction\": \"tx_failed\",\n"
            + "      \"operations\": [\n"
            + "        \"op_no_destination\"\n"
            + "      ]\n"
            + "    },\n"
            + "    \"result_xdr\": \"AAAAAAAAAGT/////AAAAAQAAAAAAAAAB////+wAAAAA=\"\n"
            + "  }\n"
            + "}";

    SubmitTransactionResponse submitTransactionResponse =
        GsonSingleton.getInstance().fromJson(json, SubmitTransactionResponse.class);
    assertEquals(submitTransactionResponse.isSuccess(), false);
    assertEquals(
        submitTransactionResponse.getEnvelopeXdr().get(),
        "AAAAAF2O0axA67+p2jMunG6G188kDSHIvqQ13d9l29YCSA/uAAAAZAAvvc0AAAABAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAABAAAAAD3sEVVGZGi/NoC3ta/8f/YZKMzyi9ZJpOi0H47x7IqYAAAAAAAAAAAF9eEAAAAAAAAAAAECSA/uAAAAQFuZVAjftHa+JZes1VxSk8naOfjjAz9V86mY1AZf8Ik6PtTsBpDsCfG57EYsq4jWyZcT+vhXyWsw5evF1ELqMw4=");
    assertEquals(
        submitTransactionResponse.getResultXdr().get(),
        "AAAAAAAAAGT/////AAAAAQAAAAAAAAAB////+wAAAAA=");
    assertFalse(submitTransactionResponse.getOfferIdFromResult(0).isPresent());
    assertFalse(submitTransactionResponse.getDecodedTransactionResult().isPresent());
    assertEquals(
        submitTransactionResponse.getExtras().getResultCodes().getTransactionResultCode(),
        "tx_failed");
    assertEquals(
        submitTransactionResponse.getExtras().getResultCodes().getOperationsResultCodes().get(0),
        "op_no_destination");
  }

  @Test
  public void testDeserializeSuccessResponse() throws IOException {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"https://horizon-testnet.stellar.org/transactions/ee14b93fcd31d4cfe835b941a0a8744e23a6677097db1fafe0552d8657bed940\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"hash\": \"ee14b93fcd31d4cfe835b941a0a8744e23a6677097db1fafe0552d8657bed940\",\n"
            + "  \"ledger\": 3128812,\n"
            + "  \"envelope_xdr\": \"AAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAZAAT3TUAAAAwAAAAAAAAAAAAAAABAAAAAAAAAAMAAAABSU5SAAAAAAA0jDEZkBgx+hCc5IIv+z6CoaYTB8jRkIA6drZUv3YRlwAAAAFVU0QAAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAX14QAAAAAKAAAAAQAAAAAAAAAAAAAAAAAAAAG/dhGXAAAAQLuStfImg0OeeGAQmvLkJSZ1MPSkCzCYNbGqX5oYNuuOqZ5SmWhEsC7uOD9ha4V7KengiwNlc0oMNqBVo22S7gk=\",\n"
            + "  \"result_xdr\": \"AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAADAAAAAAAAAAAAAAAAAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAAAAPEAAAABSU5SAAAAAAA0jDEZkBgx+hCc5IIv+z6CoaYTB8jRkIA6drZUv3YRlwAAAAFVU0QAAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAX14QAAAAAKAAAAAQAAAAAAAAAAAAAAAA==\",\n"
            + "  \"result_meta_xdr\": \"AAAAAAAAAAEAAAACAAAAAAAvoHwAAAACAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAAAAPEAAAABSU5SAAAAAAA0jDEZkBgx+hCc5IIv+z6CoaYTB8jRkIA6drZUv3YRlwAAAAFVU0QAAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAX14QAAAAAKAAAAAQAAAAAAAAAAAAAAAAAAAAEAL6B8AAAAAAAAAAA0jDEZkBgx+hCc5IIv+z6CoaYTB8jRkIA6drZUv3YRlwAAABZ9zvNAABPdNQAAADAAAAAEAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAA==\"\n"
            + "}";

    SubmitTransactionResponse submitTransactionResponse =
        GsonSingleton.getInstance().fromJson(json, SubmitTransactionResponse.class);
    assertEquals(submitTransactionResponse.isSuccess(), true);
    assertEquals(
        submitTransactionResponse.getHash(),
        "ee14b93fcd31d4cfe835b941a0a8744e23a6677097db1fafe0552d8657bed940");
    assertEquals(submitTransactionResponse.getLedger(), new Long(3128812));
    assertEquals(
        submitTransactionResponse.getEnvelopeXdr().get(),
        "AAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAZAAT3TUAAAAwAAAAAAAAAAAAAAABAAAAAAAAAAMAAAABSU5SAAAAAAA0jDEZkBgx+hCc5IIv+z6CoaYTB8jRkIA6drZUv3YRlwAAAAFVU0QAAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAX14QAAAAAKAAAAAQAAAAAAAAAAAAAAAAAAAAG/dhGXAAAAQLuStfImg0OeeGAQmvLkJSZ1MPSkCzCYNbGqX5oYNuuOqZ5SmWhEsC7uOD9ha4V7KengiwNlc0oMNqBVo22S7gk=");
    assertEquals(
        submitTransactionResponse.getResultXdr().get(),
        "AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAADAAAAAAAAAAAAAAAAAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAAAAPEAAAABSU5SAAAAAAA0jDEZkBgx+hCc5IIv+z6CoaYTB8jRkIA6drZUv3YRlwAAAAFVU0QAAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAX14QAAAAAKAAAAAQAAAAAAAAAAAAAAAA==");
    assertEquals(submitTransactionResponse.getOfferIdFromResult(0).get(), new Long(241));
    TransactionResult transactionResult =
        submitTransactionResponse.getDecodedTransactionResult().get();
    assertEquals(transactionResult.getResult().getDiscriminant(), TransactionResultCode.txSUCCESS);
    assertEquals(transactionResult.getResult().getResults().length, 1);
    assertEquals(
        transactionResult.getResult().getResults()[0].getTr().getDiscriminant(),
        OperationType.MANAGE_SELL_OFFER);
    assertNotNull(transactionResult.getResult().getResults()[0].getTr().getManageSellOfferResult());
  }

  @Test
  public void testDeserializeNoOfferID() throws IOException {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"https://horizon-testnet.stellar.org/transactions/ee14b93fcd31d4cfe835b941a0a8744e23a6677097db1fafe0552d8657bed940\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"hash\": \"ee14b93fcd31d4cfe835b941a0a8744e23a6677097db1fafe0552d8657bed940\",\n"
            + "  \"ledger\": 3128812,\n"
            + "  \"envelope_xdr\": \"AAAAAP0uDCJGzWofMAUkG/F3YCPctgLkeP3VTr3P1mHKznAHAAAAZAA5klgAAABLAAAAAAAAAAAAAAABAAAAAQAAAAD9LgwiRs1qHzAFJBvxd2Aj3LYC5Hj91U69z9Zhys5wBwAAAAMAAAABRVVSAAAAAAD9LgwiRs1qHzAFJBvxd2Aj3LYC5Hj91U69z9Zhys5wBwAAAAFVU0QAAAAAAOw4Vbv3zJ23hiG1bjU7M/AOuVNHKnREERoitIr1Zj/ZAAAAAAOThwAAAAACAAAAAQAAAAAAAAAAAAAAAAAAAAHKznAHAAAAQEe1jyNCeK3TckPuuKeWIICf6nvz2zBZ8mbbUamWLnOFMMqvQPTllOe9DIdloNxaixgle9zi2F+yyOhLzpNhkAg=\",\n"
            + "  \"result_xdr\": \"AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAADAAAAAAAAAAEAAAAA7DhVu/fMnbeGIbVuNTsz8A65U0cqdEQRGiK0ivVmP9kAAAAAAAASJQAAAAFVU0QAAAAAAOw4Vbv3zJ23hiG1bjU7M/AOuVNHKnREERoitIr1Zj/ZAAAAAAcnDgAAAAABRVVSAAAAAAD9LgwiRs1qHzAFJBvxd2Aj3LYC5Hj91U69z9Zhys5wBwAAAAADk4cAAAAAAgAAAAA=\",\n"
            + "  \"result_meta_xdr\": \"AAAAAAAAAAEAAAACAAAAAAAvoHwAAAACAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAAAAPEAAAABSU5SAAAAAAA0jDEZkBgx+hCc5IIv+z6CoaYTB8jRkIA6drZUv3YRlwAAAAFVU0QAAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAX14QAAAAAKAAAAAQAAAAAAAAAAAAAAAAAAAAEAL6B8AAAAAAAAAAA0jDEZkBgx+hCc5IIv+z6CoaYTB8jRkIA6drZUv3YRlwAAABZ9zvNAABPdNQAAADAAAAAEAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAA==\"\n"
            + "}";

    SubmitTransactionResponse submitTransactionResponse =
        GsonSingleton.getInstance().fromJson(json, SubmitTransactionResponse.class);
    assertEquals(submitTransactionResponse.isSuccess(), true);
    assertFalse(submitTransactionResponse.getOfferIdFromResult(0).isPresent());
  }
}
