package org.stellar.sdk.responses;

import com.google.gson.reflect.TypeToken;

import junit.framework.TestCase;

import org.junit.Test;
import org.stellar.sdk.MemoText;

public class TransactionPageDeserializerTest extends TestCase {
  @Test
  public void testDeserialize() {
    Page<TransactionResponse> transactionsPage = GsonSingleton.getInstance().fromJson(json, new TypeToken<Page<TransactionResponse>>() {}.getType());

    assertEquals(transactionsPage.getRecords().get(0).getSourceAccount().getAccountId(), "GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7");
    assertEquals(transactionsPage.getRecords().get(0).getPagingToken(), "12884905984");
    assertTrue(transactionsPage.getRecords().get(0).getMemo() instanceof MemoText);
    MemoText memoText = (MemoText) transactionsPage.getRecords().get(0).getMemo();
    assertEquals(memoText.getText(), "hello world");
    assertEquals(transactionsPage.getRecords().get(0).getLinks().getAccount().getHref(), "/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7");
    assertEquals(transactionsPage.getRecords().get(9).getSourceAccount().getAccountId(), "GAENIE5LBJIXLMJIAJ7225IUPA6CX7EGHUXRX5FLCZFFAQSG2ZUYSWFK");

    // Empty memo_text
    assertTrue(transactionsPage.getRecords().get(2).getMemo() instanceof MemoText);
    memoText = (MemoText) transactionsPage.getRecords().get(2).getMemo();
    assertEquals(memoText.getText(), "");

    assertEquals(transactionsPage.getLinks().getNext().getHref(), "/transactions?order=asc&limit=10&cursor=81058917781504");
    assertEquals(transactionsPage.getLinks().getPrev().getHref(), "/transactions?order=desc&limit=10&cursor=12884905984");
    assertEquals(transactionsPage.getLinks().getSelf().getHref(), "/transactions?order=asc&limit=10&cursor=");
  }

  String json = "{\n" +
          "  \"_embedded\": {\n" +
          "    \"records\": [\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"account\": {\n" +
          "            \"href\": \"/accounts/GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/transactions/3389e9f0f1a65f19736cacf544c2e825313e8447f569233bb8db39aa607c8889/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"ledger\": {\n" +
          "            \"href\": \"/ledgers/3\"\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/transactions/3389e9f0f1a65f19736cacf544c2e825313e8447f569233bb8db39aa607c8889/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"/transactions?cursor=12884905984\\u0026order=asc\"\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/transactions/3389e9f0f1a65f19736cacf544c2e825313e8447f569233bb8db39aa607c8889\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"/transactions?cursor=12884905984\\u0026order=desc\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"3389e9f0f1a65f19736cacf544c2e825313e8447f569233bb8db39aa607c8889\",\n" +
          "        \"paging_token\": \"12884905984\",\n" +
          "        \"hash\": \"3389e9f0f1a65f19736cacf544c2e825313e8447f569233bb8db39aa607c8889\",\n" +
          "        \"ledger\": 3,\n" +
          "        \"created_at\": \"2015-09-30T17:15:54Z\",\n" +
          "        \"source_account\": \"GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\",\n" +
          "        \"source_account_sequence\": 1,\n" +
          "        \"fee_paid\": 300,\n" +
          "        \"operation_count\": 3,\n" +
          "        \"envelope_xdr\": \"AAAAAAGUcmKO5465JxTSLQOQljwk2SfqAJmZSG6JH6wtqpwhAAABLAAAAAAAAAABAAAAAAAAAAEAAAALaGVsbG8gd29ybGQAAAAAAwAAAAAAAAAAAAAAABbxCy3mLg3hiTqX4VUEEp60pFOrJNxYM1JtxXTwXhY2AAAAAAvrwgAAAAAAAAAAAQAAAAAW8Qst5i4N4Yk6l+FVBBKetKRTqyTcWDNSbcV08F4WNgAAAAAN4Lazj4x61AAAAAAAAAAFAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABLaqcIQAAAEBKwqWy3TaOxoGnfm9eUjfTRBvPf34dvDA0Nf+B8z4zBob90UXtuCqmQqwMCyH+okOI3c05br3khkH0yP4kCwcE\",\n" +
          "        \"result_xdr\": \"AAAAAAAAASwAAAAAAAAAAwAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAFAAAAAAAAAAA=\",\n" +
          "        \"result_meta_xdr\": \"AAAAAAAAAAMAAAACAAAAAAAAAAMAAAAAAAAAABbxCy3mLg3hiTqX4VUEEp60pFOrJNxYM1JtxXTwXhY2AAAAAAvrwgAAAAADAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAMAAAAAAAAAAAGUcmKO5465JxTSLQOQljwk2SfqAJmZSG6JH6wtqpwhDeC2s5t4PNQAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAEAAAADAAAAAAAAAAABlHJijueOuScU0i0DkJY8JNkn6gCZmUhuiR+sLaqcIQAAAAAL68IAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAEAAAADAAAAAAAAAAAW8Qst5i4N4Yk6l+FVBBKetKRTqyTcWDNSbcV08F4WNg3gtrObeDzUAAAAAwAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAEAAAABAAAAAwAAAAAAAAAAAZRyYo7njrknFNItA5CWPCTZJ+oAmZlIbokfrC2qnCEAAAAAC+vCAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=\",\n" +
          "        \"memo_type\": \"text\",\n" +
          "        \"memo\": \"hello world\",\n" +
          "        \"signatures\": [\n" +
          "          \"SsKlst02jsaBp35vXlI300Qbz39+HbwwNDX/gfM+MwaG/dFF7bgqpkKsDAsh/qJDiN3NOW695IZB9Mj+JAsHBA==\"\n" +
          "        ]\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"account\": {\n" +
          "            \"href\": \"/accounts/GALPCCZN4YXA3YMJHKL6CVIECKPLJJCTVMSNYWBTKJW4K5HQLYLDMZTB\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/transactions/2db4b22ca018119c5027a80578813ffcf582cda4aa9e31cd92b43cf1bda4fc5a/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"ledger\": {\n" +
          "            \"href\": \"/ledgers/7841\"\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/transactions/2db4b22ca018119c5027a80578813ffcf582cda4aa9e31cd92b43cf1bda4fc5a/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"/transactions?cursor=33676838572032\\u0026order=asc\"\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/transactions/2db4b22ca018119c5027a80578813ffcf582cda4aa9e31cd92b43cf1bda4fc5a\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"/transactions?cursor=33676838572032\\u0026order=desc\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"2db4b22ca018119c5027a80578813ffcf582cda4aa9e31cd92b43cf1bda4fc5a\",\n" +
          "        \"paging_token\": \"33676838572032\",\n" +
          "        \"hash\": \"2db4b22ca018119c5027a80578813ffcf582cda4aa9e31cd92b43cf1bda4fc5a\",\n" +
          "        \"ledger\": 7841,\n" +
          "        \"created_at\": \"2015-10-01T04:15:01Z\",\n" +
          "        \"source_account\": \"GALPCCZN4YXA3YMJHKL6CVIECKPLJJCTVMSNYWBTKJW4K5HQLYLDMZTB\",\n" +
          "        \"source_account_sequence\": 12884901890,\n" +
          "        \"fee_paid\": 300,\n" +
          "        \"operation_count\": 3,\n" +
          "        \"envelope_xdr\": \"AAAAABbxCy3mLg3hiTqX4VUEEp60pFOrJNxYM1JtxXTwXhY2AAABLAAAAAMAAAACAAAAAAAAAAEAAAATdGVzdHBvb2wsZmF1Y2V0LHNkZgAAAAADAAAAAAAAAAAAAAAAH6Ue1GOPj6Hb/ROPyIFCJpQPMujihEIvJSfK0UfMDIgAAAAAC+vCAAAAAAAAAAAAAAAAALMw4P7yJTyqj6ptNh7BPyXEoT+zVwTcU4JVbGyonvgbAAAAAAvrwgAAAAAAAAAAAAAAAABJlwu05Op/5x1uyrweYsyR6pTTos33hRNZe5IF6blnzwAAAAAL68IAAAAAAAAAAAHwXhY2AAAAQDSBB5eNEKkWIoQbZ1YQabJuE5mW/AKhrHTxw9H3m/sai90YcaZlsAe3ueO9jExjSZF289ZcR4vc0wFw1p/WyAc=\",\n" +
          "        \"result_xdr\": \"AAAAAAAAASwAAAAAAAAAAwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=\",\n" +
          "        \"result_meta_xdr\": \"AAAAAAAAAAMAAAACAAAAAAAAHqEAAAAAAAAAAB+lHtRjj4+h2/0Tj8iBQiaUDzLo4oRCLyUnytFHzAyIAAAAAAvrwgAAAB6hAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAQAAHqEAAAAAAAAAABbxCy3mLg3hiTqX4VUEEp60pFOrJNxYM1JtxXTwXhY2DeC2s4+MeHwAAAADAAAAAgAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAB6hAAAAAAAAAACzMOD+8iU8qo+qbTYewT8lxKE/s1cE3FOCVWxsqJ74GwAAAAAL68IAAAAeoQAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAEAAB6hAAAAAAAAAAAW8Qst5i4N4Yk6l+FVBBKetKRTqyTcWDNSbcV08F4WNg3gtrODoLZ8AAAAAwAAAAIAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAeoQAAAAAAAAAASZcLtOTqf+cdbsq8HmLMkeqU06LN94UTWXuSBem5Z88AAAAAC+vCAAAAHqEAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAABAAAeoQAAAAAAAAAAFvELLeYuDeGJOpfhVQQSnrSkU6sk3FgzUm3FdPBeFjYN4Lazd7T0fAAAAAMAAAACAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAA=\",\n" +
          "        \"memo_type\": \"text\",\n" +
          "        \"memo\": \"testpool,faucet,sdf\",\n" +
          "        \"signatures\": [\n" +
          "          \"NIEHl40QqRYihBtnVhBpsm4TmZb8AqGsdPHD0feb+xqL3RhxpmWwB7e5472MTGNJkXbz1lxHi9zTAXDWn9bIBw==\"\n" +
          "        ]\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"account\": {\n" +
          "            \"href\": \"/accounts/GALPCCZN4YXA3YMJHKL6CVIECKPLJJCTVMSNYWBTKJW4K5HQLYLDMZTB\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/transactions/3ce2aca2fed36da2faea31352c76c5e412348887a4c119b1e90de8d1b937396a/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"ledger\": {\n" +
          "            \"href\": \"/ledgers/7855\"\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/transactions/3ce2aca2fed36da2faea31352c76c5e412348887a4c119b1e90de8d1b937396a/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"/transactions?cursor=33736968114176\\u0026order=asc\"\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/transactions/3ce2aca2fed36da2faea31352c76c5e412348887a4c119b1e90de8d1b937396a\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"/transactions?cursor=33736968114176\\u0026order=desc\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"3ce2aca2fed36da2faea31352c76c5e412348887a4c119b1e90de8d1b937396a\",\n" +
          "        \"paging_token\": \"33736968114176\",\n" +
          "        \"hash\": \"3ce2aca2fed36da2faea31352c76c5e412348887a4c119b1e90de8d1b937396a\",\n" +
          "        \"ledger\": 7855,\n" +
          "        \"created_at\": \"2015-10-01T04:16:11Z\",\n" +
          "        \"source_account\": \"GALPCCZN4YXA3YMJHKL6CVIECKPLJJCTVMSNYWBTKJW4K5HQLYLDMZTB\",\n" +
          "        \"source_account_sequence\": 12884901891,\n" +
          "        \"fee_paid\": 100,\n" +
          "        \"operation_count\": 1,\n" +
          "        \"envelope_xdr\": \"AAAAABbxCy3mLg3hiTqX4VUEEp60pFOrJNxYM1JtxXTwXhY2AAAAZAAAAAMAAAADAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAAFAAAAAQAAAAAfpR7UY4+Podv9E4/IgUImlA8y6OKEQi8lJ8rRR8wMiAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHwXhY2AAAAQNbDcWsR3s3z8Qzqatcdc/k2L4LXWJMA6eXac8dbXkAdc4ppH25isGC5OwvG06Vwvc3Ce3/r2rYcBP3vxhx18A8=\",\n" +
          "        \"result_xdr\": \"AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAAFAAAAAAAAAAA=\",\n" +
          "        \"result_meta_xdr\": \"AAAAAAAAAAEAAAABAAAAAQAAHq8AAAAAAAAAABbxCy3mLg3hiTqX4VUEEp60pFOrJNxYM1JtxXTwXhY2DeC2s3e09BgAAAADAAAAAwAAAAAAAAABAAAAAB+lHtRjj4+h2/0Tj8iBQiaUDzLo4oRCLyUnytFHzAyIAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAA\",\n" +
          "        \"memo_type\": \"text\",\n" +
          "        \"signatures\": [\n" +
          "          \"1sNxaxHezfPxDOpq1x1z+TYvgtdYkwDp5dpzx1teQB1zimkfbmKwYLk7C8bTpXC9zcJ7f+vathwE/e/GHHXwDw==\"\n" +
          "        ]\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"account\": {\n" +
          "            \"href\": \"/accounts/GALPCCZN4YXA3YMJHKL6CVIECKPLJJCTVMSNYWBTKJW4K5HQLYLDMZTB\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/transactions/e2126485295ecf645cc6615267a83eb4daf8896289caea91038b4c0e6e1471c8/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"ledger\": {\n" +
          "            \"href\": \"/ledgers/7863\"\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/transactions/e2126485295ecf645cc6615267a83eb4daf8896289caea91038b4c0e6e1471c8/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"/transactions?cursor=33771327852544\\u0026order=asc\"\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/transactions/e2126485295ecf645cc6615267a83eb4daf8896289caea91038b4c0e6e1471c8\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"/transactions?cursor=33771327852544\\u0026order=desc\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"e2126485295ecf645cc6615267a83eb4daf8896289caea91038b4c0e6e1471c8\",\n" +
          "        \"paging_token\": \"33771327852544\",\n" +
          "        \"hash\": \"e2126485295ecf645cc6615267a83eb4daf8896289caea91038b4c0e6e1471c8\",\n" +
          "        \"ledger\": 7863,\n" +
          "        \"created_at\": \"2015-10-01T04:16:50Z\",\n" +
          "        \"source_account\": \"GALPCCZN4YXA3YMJHKL6CVIECKPLJJCTVMSNYWBTKJW4K5HQLYLDMZTB\",\n" +
          "        \"source_account_sequence\": 12884901892,\n" +
          "        \"fee_paid\": 100,\n" +
          "        \"operation_count\": 1,\n" +
          "        \"envelope_xdr\": \"AAAAABbxCy3mLg3hiTqX4VUEEp60pFOrJNxYM1JtxXTwXhY2AAAAZAAAAAMAAAAEAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAAJAAAAAAAAAAHwXhY2AAAAQFEHN8h4ZRAOQ21rfMbvEfRKroxx9rkrx3hK2XX1j6mN7qKANKmzwPMlcRcb5yzskPqoWCsuqX/MkUYLfDZe7QY=\",\n" +
          "        \"result_xdr\": \"AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAAJAAAAAAAAAAEAAAAAH6Ue1GOPj6Hb/ROPyIFCJpQPMujihEIvJSfK0UfMDIgAAK11sXJ6SgAAAAA=\",\n" +
          "        \"result_meta_xdr\": \"AAAAAAAAAAEAAAABAAAAAQAAHrcAAAAAAAAAAB+lHtRjj4+h2/0Tj8iBQiaUDzLo4oRCLyUnytFHzAyIAACtdb1ePEoAAB6hAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAA\",\n" +
          "        \"memo_type\": \"text\",\n" +
          "        \"signatures\": [\n" +
          "          \"UQc3yHhlEA5DbWt8xu8R9EqujHH2uSvHeErZdfWPqY3uooA0qbPA8yVxFxvnLOyQ+qhYKy6pf8yRRgt8Nl7tBg==\"\n" +
          "        ]\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"account\": {\n" +
          "            \"href\": \"/accounts/GALPCCZN4YXA3YMJHKL6CVIECKPLJJCTVMSNYWBTKJW4K5HQLYLDMZTB\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/transactions/1113f23c225495534b2fd589a037798155ea73ee68a418e74364c1a3be4a20d8/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"ledger\": {\n" +
          "            \"href\": \"/ledgers/7871\"\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/transactions/1113f23c225495534b2fd589a037798155ea73ee68a418e74364c1a3be4a20d8/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"/transactions?cursor=33805687590912\\u0026order=asc\"\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/transactions/1113f23c225495534b2fd589a037798155ea73ee68a418e74364c1a3be4a20d8\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"/transactions?cursor=33805687590912\\u0026order=desc\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"1113f23c225495534b2fd589a037798155ea73ee68a418e74364c1a3be4a20d8\",\n" +
          "        \"paging_token\": \"33805687590912\",\n" +
          "        \"hash\": \"1113f23c225495534b2fd589a037798155ea73ee68a418e74364c1a3be4a20d8\",\n" +
          "        \"ledger\": 7871,\n" +
          "        \"created_at\": \"2015-10-01T04:17:31Z\",\n" +
          "        \"source_account\": \"GALPCCZN4YXA3YMJHKL6CVIECKPLJJCTVMSNYWBTKJW4K5HQLYLDMZTB\",\n" +
          "        \"source_account_sequence\": 12884901893,\n" +
          "        \"fee_paid\": 100,\n" +
          "        \"operation_count\": 1,\n" +
          "        \"envelope_xdr\": \"AAAAABbxCy3mLg3hiTqX4VUEEp60pFOrJNxYM1JtxXTwXhY2AAAAZAAAAAMAAAAFAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAAJAAAAAAAAAAHwXhY2AAAAQGCy99saUm6alXIRyp0NNh2OFSCybp1JGPDN2pb/+Fw07/X7y4lPEp/B6WIV130a+2eY+5T3ujbiKa6TIcUaNwQ=\",\n" +
          "        \"result_xdr\": \"AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAAJAAAAAAAAAAEAAAAAH6Ue1GOPj6Hb/ROPyIFCJpQPMujihEIvJSfK0UfMDIgAAK11sXTKRwAAAAA=\",\n" +
          "        \"result_meta_xdr\": \"AAAAAAAAAAEAAAABAAAAAQAAHr8AAAAAAAAAAB+lHtRjj4+h2/0Tj8iBQiaUDzLo4oRCLyUnytFHzAyIAAFa627TBpEAAB6hAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAA\",\n" +
          "        \"memo_type\": \"text\",\n" +
          "        \"signatures\": [\n" +
          "          \"YLL32xpSbpqVchHKnQ02HY4VILJunUkY8M3alv/4XDTv9fvLiU8Sn8HpYhXXfRr7Z5j7lPe6NuIprpMhxRo3BA==\"\n" +
          "        ]\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"account\": {\n" +
          "            \"href\": \"/accounts/GALPCCZN4YXA3YMJHKL6CVIECKPLJJCTVMSNYWBTKJW4K5HQLYLDMZTB\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/transactions/f0222a5421ccfc4e612f11d9ff95755fbb6300df7c61442d990d498a4cd01c92/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"ledger\": {\n" +
          "            \"href\": \"/ledgers/7874\"\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/transactions/f0222a5421ccfc4e612f11d9ff95755fbb6300df7c61442d990d498a4cd01c92/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"/transactions?cursor=33818572492800\\u0026order=asc\"\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/transactions/f0222a5421ccfc4e612f11d9ff95755fbb6300df7c61442d990d498a4cd01c92\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"/transactions?cursor=33818572492800\\u0026order=desc\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"f0222a5421ccfc4e612f11d9ff95755fbb6300df7c61442d990d498a4cd01c92\",\n" +
          "        \"paging_token\": \"33818572492800\",\n" +
          "        \"hash\": \"f0222a5421ccfc4e612f11d9ff95755fbb6300df7c61442d990d498a4cd01c92\",\n" +
          "        \"ledger\": 7874,\n" +
          "        \"created_at\": \"2015-10-01T04:17:46Z\",\n" +
          "        \"source_account\": \"GALPCCZN4YXA3YMJHKL6CVIECKPLJJCTVMSNYWBTKJW4K5HQLYLDMZTB\",\n" +
          "        \"source_account_sequence\": 12884901894,\n" +
          "        \"fee_paid\": 100,\n" +
          "        \"operation_count\": 1,\n" +
          "        \"envelope_xdr\": \"AAAAABbxCy3mLg3hiTqX4VUEEp60pFOrJNxYM1JtxXTwXhY2AAAAZAAAAAMAAAAGAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAAJAAAAAAAAAAHwXhY2AAAAQCPAo8QwsZe9FA0sz/deMdhlu6/zrk7SgkBG22ApvtpETBhnGkX4trSFDz8sVlKqvweqGUVgvjUyM0AcHxyXZQw=\",\n" +
          "        \"result_xdr\": \"AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAAJAAAAAAAAAAEAAAAAH6Ue1GOPj6Hb/ROPyIFCJpQPMujihEIvJSfK0UfMDIgAAK1+KLf6bgAAAAA=\",\n" +
          "        \"result_meta_xdr\": \"AAAAAAAAAAEAAAABAAAAAQAAHsIAAAAAAAAAAB+lHtRjj4+h2/0Tj8iBQiaUDzLo4oRCLyUnytFHzAyIAAIIaZeLAP8AAB6hAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAA\",\n" +
          "        \"memo_type\": \"text\",\n" +
          "        \"signatures\": [\n" +
          "          \"I8CjxDCxl70UDSzP914x2GW7r/OuTtKCQEbbYCm+2kRMGGcaRfi2tIUPPyxWUqq/B6oZRWC+NTIzQBwfHJdlDA==\"\n" +
          "        ]\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"account\": {\n" +
          "            \"href\": \"/accounts/GALPCCZN4YXA3YMJHKL6CVIECKPLJJCTVMSNYWBTKJW4K5HQLYLDMZTB\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/transactions/2ead393e776b5a374b478ada47ad7240d004ac1b52468f4ec58aeeb7a9c369e3/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"ledger\": {\n" +
          "            \"href\": \"/ledgers/7990\"\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/transactions/2ead393e776b5a374b478ada47ad7240d004ac1b52468f4ec58aeeb7a9c369e3/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"/transactions?cursor=34316788699136\\u0026order=asc\"\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/transactions/2ead393e776b5a374b478ada47ad7240d004ac1b52468f4ec58aeeb7a9c369e3\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"/transactions?cursor=34316788699136\\u0026order=desc\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"2ead393e776b5a374b478ada47ad7240d004ac1b52468f4ec58aeeb7a9c369e3\",\n" +
          "        \"paging_token\": \"34316788699136\",\n" +
          "        \"hash\": \"2ead393e776b5a374b478ada47ad7240d004ac1b52468f4ec58aeeb7a9c369e3\",\n" +
          "        \"ledger\": 7990,\n" +
          "        \"created_at\": \"2015-10-01T04:27:26Z\",\n" +
          "        \"source_account\": \"GALPCCZN4YXA3YMJHKL6CVIECKPLJJCTVMSNYWBTKJW4K5HQLYLDMZTB\",\n" +
          "        \"source_account_sequence\": 12884901895,\n" +
          "        \"fee_paid\": 100,\n" +
          "        \"operation_count\": 1,\n" +
          "        \"envelope_xdr\": \"AAAAABbxCy3mLg3hiTqX4VUEEp60pFOrJNxYM1JtxXTwXhY2AAAAZAAAAAMAAAAHAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAABAAAAAEmXC7Tk6n/nHW7KvB5izJHqlNOizfeFE1l7kgXpuWfPAAAAAA3gtq7biOyIAAAAAAAAAAHwXhY2AAAAQDZFfOd/26OprF+/0yi9ZtfuHXuL4Tu36eAouGcAS7iHq6l+aMy2z39Ipd/yRAQUHdR7ackuWTB4b26hEEy1xwA=\",\n" +
          "        \"result_xdr\": \"AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAABAAAAAAAAAAA=\",\n" +
          "        \"result_meta_xdr\": \"AAAAAAAAAAEAAAACAAAAAQAAHzYAAAAAAAAAABbxCy3mLg3hiTqX4VUEEp60pFOrJNxYM1JtxXTwXhY2AAAABJwsBgAAAAADAAAABwAAAAAAAAABAAAAAB+lHtRjj4+h2/0Tj8iBQiaUDzLo4oRCLyUnytFHzAyIAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAQAAHzYAAAAAAAAAAEmXC7Tk6n/nHW7KvB5izJHqlNOizfeFE1l7kgXpuWfPDeC2rud0rogAAB6hAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAA\",\n" +
          "        \"memo_type\": \"text\",\n" +
          "        \"signatures\": [\n" +
          "          \"NkV853/bo6msX7/TKL1m1+4de4vhO7fp4Ci4ZwBLuIerqX5ozLbPf0il3/JEBBQd1HtpyS5ZMHhvbqEQTLXHAA==\"\n" +
          "        ]\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"account\": {\n" +
          "            \"href\": \"/accounts/GAP2KHWUMOHY7IO37UJY7SEBIITJIDZS5DRIIQRPEUT4VUKHZQGIRWS4\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/transactions/5068a4acda55fddc9db6a5b3113c4520d22a4158978a33ed6399d39f940d5018/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"ledger\": {\n" +
          "            \"href\": \"/ledgers/8682\"\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/transactions/5068a4acda55fddc9db6a5b3113c4520d22a4158978a33ed6399d39f940d5018/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"/transactions?cursor=37288906067968\\u0026order=asc\"\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/transactions/5068a4acda55fddc9db6a5b3113c4520d22a4158978a33ed6399d39f940d5018\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"/transactions?cursor=37288906067968\\u0026order=desc\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"5068a4acda55fddc9db6a5b3113c4520d22a4158978a33ed6399d39f940d5018\",\n" +
          "        \"paging_token\": \"37288906067968\",\n" +
          "        \"hash\": \"5068a4acda55fddc9db6a5b3113c4520d22a4158978a33ed6399d39f940d5018\",\n" +
          "        \"ledger\": 8682,\n" +
          "        \"created_at\": \"2015-10-01T05:25:06Z\",\n" +
          "        \"source_account\": \"GAP2KHWUMOHY7IO37UJY7SEBIITJIDZS5DRIIQRPEUT4VUKHZQGIRWS4\",\n" +
          "        \"source_account_sequence\": 33676838567937,\n" +
          "        \"fee_paid\": 200,\n" +
          "        \"operation_count\": 2,\n" +
          "        \"envelope_xdr\": \"AAAAAB+lHtRjj4+h2/0Tj8iBQiaUDzLo4oRCLyUnytFHzAyIAAAAyAAAHqEAAAABAAAAAAAAAAEAAAAHaGVscHNkZgAAAAACAAAAAAAAAAAAAAAACNQTqwpRdbEoAn+tdRR4PCv8hj0vG/SrFkpQQkbWaYkAAAAAC+vCAAAAAAAAAAABAAAAAAjUE6sKUXWxKAJ/rXUUeDwr/IY9Lxv0qxZKUEJG1mmJAAAAAAACCGTvczj/AAAAAAAAAAFHzAyIAAAAQM5E3lSZ8mBzppHShs5Vy5B+MsUVCXNg5qaVMC7GyVTJOYBhcAyNlF4X03rnGZddR3i6fUfFLym77Lryh41pAQg=\",\n" +
          "        \"result_xdr\": \"AAAAAAAAAMgAAAAAAAAAAgAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAA=\",\n" +
          "        \"result_meta_xdr\": \"AAAAAAAAAAIAAAACAAAAAAAAIeoAAAAAAAAAAAjUE6sKUXWxKAJ/rXUUeDwr/IY9Lxv0qxZKUEJG1mmJAAAAAAvrwgAAACHqAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAQAAIeoAAAAAAAAAAB+lHtRjj4+h2/0Tj8iBQiaUDzLo4oRCLyUnytFHzAyIAAIIaYufPjcAAB6hAAAAAQAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAEAACHqAAAAAAAAAAAI1BOrClF1sSgCf611FHg8K/yGPS8b9KsWSlBCRtZpiQACCGT7Xvr/AAAh6gAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAEAACHqAAAAAAAAAAAfpR7UY4+Podv9E4/IgUImlA8y6OKEQi8lJ8rRR8wMiAAAAAScLAU4AAAeoQAAAAEAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAA==\",\n" +
          "        \"memo_type\": \"text\",\n" +
          "        \"memo\": \"helpsdf\",\n" +
          "        \"signatures\": [\n" +
          "          \"zkTeVJnyYHOmkdKGzlXLkH4yxRUJc2DmppUwLsbJVMk5gGFwDI2UXhfTeucZl11HeLp9R8UvKbvsuvKHjWkBCA==\"\n" +
          "        ]\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"account\": {\n" +
          "            \"href\": \"/accounts/GBEZOC5U4TVH7ZY5N3FLYHTCZSI6VFGTULG7PBITLF5ZEBPJXFT46YZM\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/transactions/388561da0b439741fd27177b924dc4f6a0705bdfd8126a83432be642b2889415/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"ledger\": {\n" +
          "            \"href\": \"/ledgers/18768\"\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/transactions/388561da0b439741fd27177b924dc4f6a0705bdfd8126a83432be642b2889415/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"/transactions?cursor=80607946215424\\u0026order=asc\"\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/transactions/388561da0b439741fd27177b924dc4f6a0705bdfd8126a83432be642b2889415\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"/transactions?cursor=80607946215424\\u0026order=desc\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"388561da0b439741fd27177b924dc4f6a0705bdfd8126a83432be642b2889415\",\n" +
          "        \"paging_token\": \"80607946215424\",\n" +
          "        \"hash\": \"388561da0b439741fd27177b924dc4f6a0705bdfd8126a83432be642b2889415\",\n" +
          "        \"ledger\": 18768,\n" +
          "        \"created_at\": \"2015-10-01T23:07:25Z\",\n" +
          "        \"source_account\": \"GBEZOC5U4TVH7ZY5N3FLYHTCZSI6VFGTULG7PBITLF5ZEBPJXFT46YZM\",\n" +
          "        \"source_account_sequence\": 33676838567937,\n" +
          "        \"fee_paid\": 1000,\n" +
          "        \"operation_count\": 1,\n" +
          "        \"envelope_xdr\": \"AAAAAEmXC7Tk6n/nHW7KvB5izJHqlNOizfeFE1l7kgXpuWfPAAAD6AAAHqEAAAABAAAAAAAAAAAAAAABAAAAAAAAAAUAAAABAAAAAAjUE6sKUXWxKAJ/rXUUeDwr/IY9Lxv0qxZKUEJG1mmJAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAem5Z88AAABAo/o2gLEdY1kkfAYTOjBsTa24WYtWkv0wCyngS6xQU86fgIwD1zF+SP2Joz9x2njQj9B4yzLzJ5jU82X59w6eBg==\",\n" +
          "        \"result_xdr\": \"AAAAAAAAA+gAAAAAAAAAAQAAAAAAAAAFAAAAAAAAAAA=\",\n" +
          "        \"result_meta_xdr\": \"AAAAAAAAAAEAAAABAAAAAQAASVAAAAAAAAAAAEmXC7Tk6n/nHW7KvB5izJHqlNOizfeFE1l7kgXpuWfPDeC2rud0qqAAAB6hAAAAAQAAAAAAAAABAAAAAAjUE6sKUXWxKAJ/rXUUeDwr/IY9Lxv0qxZKUEJG1mmJAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAA\",\n" +
          "        \"memo_type\": \"none\",\n" +
          "        \"signatures\": [\n" +
          "          \"o/o2gLEdY1kkfAYTOjBsTa24WYtWkv0wCyngS6xQU86fgIwD1zF+SP2Joz9x2njQj9B4yzLzJ5jU82X59w6eBg==\"\n" +
          "        ]\n" +
          "      },\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"account\": {\n" +
          "            \"href\": \"/accounts/GAENIE5LBJIXLMJIAJ7225IUPA6CX7EGHUXRX5FLCZFFAQSG2ZUYSWFK\"\n" +
          "          },\n" +
          "          \"effects\": {\n" +
          "            \"href\": \"/transactions/eff9b8993a0cb705b18aa19af228672e026da65c4063bfd2318640c67302831f/effects{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"ledger\": {\n" +
          "            \"href\": \"/ledgers/18873\"\n" +
          "          },\n" +
          "          \"operations\": {\n" +
          "            \"href\": \"/transactions/eff9b8993a0cb705b18aa19af228672e026da65c4063bfd2318640c67302831f/operations{?cursor,limit,order}\",\n" +
          "            \"templated\": true\n" +
          "          },\n" +
          "          \"precedes\": {\n" +
          "            \"href\": \"/transactions?cursor=81058917781504\\u0026order=asc\"\n" +
          "          },\n" +
          "          \"self\": {\n" +
          "            \"href\": \"/transactions/eff9b8993a0cb705b18aa19af228672e026da65c4063bfd2318640c67302831f\"\n" +
          "          },\n" +
          "          \"succeeds\": {\n" +
          "            \"href\": \"/transactions?cursor=81058917781504\\u0026order=desc\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": \"eff9b8993a0cb705b18aa19af228672e026da65c4063bfd2318640c67302831f\",\n" +
          "        \"paging_token\": \"81058917781504\",\n" +
          "        \"hash\": \"eff9b8993a0cb705b18aa19af228672e026da65c4063bfd2318640c67302831f\",\n" +
          "        \"ledger\": 18873,\n" +
          "        \"created_at\": \"2015-10-01T23:17:09Z\",\n" +
          "        \"source_account\": \"GAENIE5LBJIXLMJIAJ7225IUPA6CX7EGHUXRX5FLCZFFAQSG2ZUYSWFK\",\n" +
          "        \"source_account_sequence\": 37288906063873,\n" +
          "        \"fee_paid\": 1000,\n" +
          "        \"operation_count\": 1,\n" +
          "        \"envelope_xdr\": \"AAAAAAjUE6sKUXWxKAJ/rXUUeDwr/IY9Lxv0qxZKUEJG1mmJAAAD6AAAIeoAAAABAAAAAAAAAAAAAAABAAAAAAAAAAUAAAABAAAAAAjUE6sKUXWxKAJ/rXUUeDwr/IY9Lxv0qxZKUEJG1mmJAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAUbWaYkAAABA5Xa8K46SvYshVBom+lxHh4BdK22z7mce1GViab6ikceRxmKM/2+YHaDLoZ6TxKcnl1+/0hSPFTsR7uUGf12UAw==\",\n" +
          "        \"result_xdr\": \"AAAAAAAAA+gAAAAAAAAAAQAAAAAAAAAFAAAAAAAAAAA=\",\n" +
          "        \"result_meta_xdr\": \"AAAAAAAAAAEAAAABAAAAAQAASbkAAAAAAAAAAAjUE6sKUXWxKAJ/rXUUeDwr/IY9Lxv0qxZKUEJG1mmJAAIIZPte9xcAACHqAAAAAQAAAAAAAAABAAAAAAjUE6sKUXWxKAJ/rXUUeDwr/IY9Lxv0qxZKUEJG1mmJAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAA\",\n" +
          "        \"memo_type\": \"none\",\n" +
          "        \"signatures\": [\n" +
          "          \"5Xa8K46SvYshVBom+lxHh4BdK22z7mce1GViab6ikceRxmKM/2+YHaDLoZ6TxKcnl1+/0hSPFTsR7uUGf12UAw==\"\n" +
          "        ]\n" +
          "      }\n" +
          "    ]\n" +
          "  },\n" +
          "  \"_links\": {\n" +
          "    \"next\": {\n" +
          "      \"href\": \"/transactions?order=asc\\u0026limit=10\\u0026cursor=81058917781504\"\n" +
          "    },\n" +
          "    \"prev\": {\n" +
          "      \"href\": \"/transactions?order=desc\\u0026limit=10\\u0026cursor=12884905984\"\n" +
          "    },\n" +
          "    \"self\": {\n" +
          "      \"href\": \"/transactions?order=asc\\u0026limit=10\\u0026cursor=\"\n" +
          "    }\n" +
          "  }\n" +
          "}";
}
