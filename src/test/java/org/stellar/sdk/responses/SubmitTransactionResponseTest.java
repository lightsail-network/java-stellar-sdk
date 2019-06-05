package org.stellar.sdk.responses;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SubmitTransactionResponseTest {
  @Test
  public void testDeserializeTransactionFailureResponse() {
    String json = "{\n" +
            "  \"type\": \"https://stellar.org/horizon-errors/transaction_failed\",\n" +
            "  \"title\": \"Transaction Failed\",\n" +
            "  \"status\": 400,\n" +
            "  \"detail\": \"The transaction failed when submitted to the stellar network. The `extras.result_codes` field on this response contains further details.  Descriptions of each code can be found at: https://www.stellar.org/developers/learn/concepts/list-of-operations.html\",\n" +
            "  \"instance\": \"horizon-testnet-001.prd.stellar001.internal.stellar-ops.com/4elYz2fHhC-528285\",\n" +
            "  \"extras\": {\n" +
            "    \"envelope_xdr\": \"AAAAAKpmDL6Z4hvZmkTBkYpHftan4ogzTaO4XTB7joLgQnYYAAAAZAAAAAAABeoyAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAABAAAAAD3sEVVGZGi/NoC3ta/8f/YZKMzyi9ZJpOi0H47x7IqYAAAAAAAAAAAF9eEAAAAAAAAAAAA=\",\n" +
            "    \"result_codes\": {\n" +
            "      \"transaction\": \"tx_no_source_account\"\n" +
            "    },\n" +
            "    \"result_xdr\": \"AAAAAAAAAAD////4AAAAAA==\"\n" +
            "  }\n" +
            "}";

    SubmitTransactionResponse submitTransactionResponse = GsonSingleton.getInstance().fromJson(json, SubmitTransactionResponse.class);
    assertEquals(submitTransactionResponse.isSuccess(), false);
    assertEquals(submitTransactionResponse.getEnvelopeXdr(), "AAAAAKpmDL6Z4hvZmkTBkYpHftan4ogzTaO4XTB7joLgQnYYAAAAZAAAAAAABeoyAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAABAAAAAD3sEVVGZGi/NoC3ta/8f/YZKMzyi9ZJpOi0H47x7IqYAAAAAAAAAAAF9eEAAAAAAAAAAAA=");
    assertEquals(submitTransactionResponse.getResultXdr(), "AAAAAAAAAAD////4AAAAAA==");
    assertEquals(submitTransactionResponse.getExtras().getResultCodes().getTransactionResultCode(), "tx_no_source_account");
  }

  @Test
  public void testDeserializeOperationFailureResponse() {
    String json = "{\n" +
            "  \"type\": \"https://stellar.org/horizon-errors/transaction_failed\",\n" +
            "  \"title\": \"Transaction Failed\",\n" +
            "  \"status\": 400,\n" +
            "  \"detail\": \"The transaction failed when submitted to the stellar network. The `extras.result_codes` field on this response contains further details.  Descriptions of each code can be found at: https://www.stellar.org/developers/learn/concepts/list-of-operations.html\",\n" +
            "  \"instance\": \"horizon-testnet-001.prd.stellar001.internal.stellar-ops.com/4elYz2fHhC-528366\",\n" +
            "  \"extras\": {\n" +
            "    \"envelope_xdr\": \"AAAAAF2O0axA67+p2jMunG6G188kDSHIvqQ13d9l29YCSA/uAAAAZAAvvc0AAAABAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAABAAAAAD3sEVVGZGi/NoC3ta/8f/YZKMzyi9ZJpOi0H47x7IqYAAAAAAAAAAAF9eEAAAAAAAAAAAECSA/uAAAAQFuZVAjftHa+JZes1VxSk8naOfjjAz9V86mY1AZf8Ik6PtTsBpDsCfG57EYsq4jWyZcT+vhXyWsw5evF1ELqMw4=\",\n" +
            "    \"result_codes\": {\n" +
            "      \"transaction\": \"tx_failed\",\n" +
            "      \"operations\": [\n" +
            "        \"op_no_destination\"\n" +
            "      ]\n" +
            "    },\n" +
            "    \"result_xdr\": \"AAAAAAAAAGT/////AAAAAQAAAAAAAAAB////+wAAAAA=\"\n" +
            "  }\n" +
            "}";

    SubmitTransactionResponse submitTransactionResponse = GsonSingleton.getInstance().fromJson(json, SubmitTransactionResponse.class);
    assertEquals(submitTransactionResponse.isSuccess(), false);
    assertEquals(submitTransactionResponse.getEnvelopeXdr(), "AAAAAF2O0axA67+p2jMunG6G188kDSHIvqQ13d9l29YCSA/uAAAAZAAvvc0AAAABAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAABAAAAAD3sEVVGZGi/NoC3ta/8f/YZKMzyi9ZJpOi0H47x7IqYAAAAAAAAAAAF9eEAAAAAAAAAAAECSA/uAAAAQFuZVAjftHa+JZes1VxSk8naOfjjAz9V86mY1AZf8Ik6PtTsBpDsCfG57EYsq4jWyZcT+vhXyWsw5evF1ELqMw4=");
    assertEquals(submitTransactionResponse.getResultXdr(), "AAAAAAAAAGT/////AAAAAQAAAAAAAAAB////+wAAAAA=");
    assertEquals(submitTransactionResponse.getExtras().getResultCodes().getTransactionResultCode(), "tx_failed");
    assertEquals(submitTransactionResponse.getExtras().getResultCodes().getOperationsResultCodes().get(0), "op_no_destination");
  }

  @Test
  public void testDeserializeSuccessResponse() {
    String json = "{\n" +
            "  \"_links\": {\n" +
            "    \"transaction\": {\n" +
            "      \"href\": \"https://horizon-testnet.stellar.org/transactions/ee14b93fcd31d4cfe835b941a0a8744e23a6677097db1fafe0552d8657bed940\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"hash\": \"ee14b93fcd31d4cfe835b941a0a8744e23a6677097db1fafe0552d8657bed940\",\n" +
            "  \"ledger\": 3128812,\n" +
            "  \"envelope_xdr\": \"AAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAZAAT3TUAAAAwAAAAAAAAAAAAAAABAAAAAAAAAAMAAAABSU5SAAAAAAA0jDEZkBgx+hCc5IIv+z6CoaYTB8jRkIA6drZUv3YRlwAAAAFVU0QAAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAX14QAAAAAKAAAAAQAAAAAAAAAAAAAAAAAAAAG/dhGXAAAAQLuStfImg0OeeGAQmvLkJSZ1MPSkCzCYNbGqX5oYNuuOqZ5SmWhEsC7uOD9ha4V7KengiwNlc0oMNqBVo22S7gk=\",\n" +
            "  \"result_xdr\": \"AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAADAAAAAAAAAAAAAAAAAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAAAAPEAAAABSU5SAAAAAAA0jDEZkBgx+hCc5IIv+z6CoaYTB8jRkIA6drZUv3YRlwAAAAFVU0QAAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAX14QAAAAAKAAAAAQAAAAAAAAAAAAAAAA==\",\n" +
            "  \"result_meta_xdr\": \"AAAAAAAAAAEAAAACAAAAAAAvoHwAAAACAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAAAAPEAAAABSU5SAAAAAAA0jDEZkBgx+hCc5IIv+z6CoaYTB8jRkIA6drZUv3YRlwAAAAFVU0QAAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAX14QAAAAAKAAAAAQAAAAAAAAAAAAAAAAAAAAEAL6B8AAAAAAAAAAA0jDEZkBgx+hCc5IIv+z6CoaYTB8jRkIA6drZUv3YRlwAAABZ9zvNAABPdNQAAADAAAAAEAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAA==\"\n" +
            "}";

    SubmitTransactionResponse submitTransactionResponse = GsonSingleton.getInstance().fromJson(json, SubmitTransactionResponse.class);
    assertEquals(submitTransactionResponse.isSuccess(), true);
    assertEquals(submitTransactionResponse.getHash(), "ee14b93fcd31d4cfe835b941a0a8744e23a6677097db1fafe0552d8657bed940");
    assertEquals(submitTransactionResponse.getLedger(), new Long(3128812));
    assertEquals(submitTransactionResponse.getEnvelopeXdr(), "AAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAZAAT3TUAAAAwAAAAAAAAAAAAAAABAAAAAAAAAAMAAAABSU5SAAAAAAA0jDEZkBgx+hCc5IIv+z6CoaYTB8jRkIA6drZUv3YRlwAAAAFVU0QAAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAX14QAAAAAKAAAAAQAAAAAAAAAAAAAAAAAAAAG/dhGXAAAAQLuStfImg0OeeGAQmvLkJSZ1MPSkCzCYNbGqX5oYNuuOqZ5SmWhEsC7uOD9ha4V7KengiwNlc0oMNqBVo22S7gk=");
    assertEquals(submitTransactionResponse.getResultXdr(), "AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAADAAAAAAAAAAAAAAAAAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAAAAPEAAAABSU5SAAAAAAA0jDEZkBgx+hCc5IIv+z6CoaYTB8jRkIA6drZUv3YRlwAAAAFVU0QAAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAX14QAAAAAKAAAAAQAAAAAAAAAAAAAAAA==");
    assertEquals(submitTransactionResponse.getOfferIdFromResult(0), new Long(241));
  }

  @Test
  public void testDeserializeNoOfferID() {
    String json = "{\n" +
            "  \"_links\": {\n" +
            "    \"transaction\": {\n" +
            "      \"href\": \"https://horizon-testnet.stellar.org/transactions/ee14b93fcd31d4cfe835b941a0a8744e23a6677097db1fafe0552d8657bed940\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"hash\": \"ee14b93fcd31d4cfe835b941a0a8744e23a6677097db1fafe0552d8657bed940\",\n" +
            "  \"ledger\": 3128812,\n" +
            "  \"envelope_xdr\": \"AAAAAP0uDCJGzWofMAUkG/F3YCPctgLkeP3VTr3P1mHKznAHAAAAZAA5klgAAABLAAAAAAAAAAAAAAABAAAAAQAAAAD9LgwiRs1qHzAFJBvxd2Aj3LYC5Hj91U69z9Zhys5wBwAAAAMAAAABRVVSAAAAAAD9LgwiRs1qHzAFJBvxd2Aj3LYC5Hj91U69z9Zhys5wBwAAAAFVU0QAAAAAAOw4Vbv3zJ23hiG1bjU7M/AOuVNHKnREERoitIr1Zj/ZAAAAAAOThwAAAAACAAAAAQAAAAAAAAAAAAAAAAAAAAHKznAHAAAAQEe1jyNCeK3TckPuuKeWIICf6nvz2zBZ8mbbUamWLnOFMMqvQPTllOe9DIdloNxaixgle9zi2F+yyOhLzpNhkAg=\",\n" +
            "  \"result_xdr\": \"AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAADAAAAAAAAAAEAAAAA7DhVu/fMnbeGIbVuNTsz8A65U0cqdEQRGiK0ivVmP9kAAAAAAAASJQAAAAFVU0QAAAAAAOw4Vbv3zJ23hiG1bjU7M/AOuVNHKnREERoitIr1Zj/ZAAAAAAcnDgAAAAABRVVSAAAAAAD9LgwiRs1qHzAFJBvxd2Aj3LYC5Hj91U69z9Zhys5wBwAAAAADk4cAAAAAAgAAAAA=\",\n" +
            "  \"result_meta_xdr\": \"AAAAAAAAAAEAAAACAAAAAAAvoHwAAAACAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAAAAPEAAAABSU5SAAAAAAA0jDEZkBgx+hCc5IIv+z6CoaYTB8jRkIA6drZUv3YRlwAAAAFVU0QAAAAAADSMMRmQGDH6EJzkgi/7PoKhphMHyNGQgDp2tlS/dhGXAAAAAAX14QAAAAAKAAAAAQAAAAAAAAAAAAAAAAAAAAEAL6B8AAAAAAAAAAA0jDEZkBgx+hCc5IIv+z6CoaYTB8jRkIA6drZUv3YRlwAAABZ9zvNAABPdNQAAADAAAAAEAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAA==\"\n" +
            "}";

    SubmitTransactionResponse submitTransactionResponse = GsonSingleton.getInstance().fromJson(json, SubmitTransactionResponse.class);
    assertEquals(submitTransactionResponse.isSuccess(), true);
    assertNull(submitTransactionResponse.getOfferIdFromResult(0));
  }
}
