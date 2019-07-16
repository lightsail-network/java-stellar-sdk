package org.stellar.sdk.responses;

import junit.framework.TestCase;

import org.junit.Test;
import org.stellar.sdk.MemoHash;
import org.stellar.sdk.MemoNone;

public class TransactionDeserializerTest extends TestCase {
  @Test
  public void testDeserialize() {
    TransactionResponse transaction = GsonSingleton.getInstance().fromJson(json, TransactionResponse.class);
    assertEquals(transaction.getHash(), "5c2e4dad596941ef944d72741c8f8f1a4282f8f2f141e81d827f44bf365d626b");
    assertEquals(transaction.getLedger(), new Long(915744));
    assertEquals(transaction.getCreatedAt(), "2015-11-20T17:01:28Z");
    assertEquals(transaction.getPagingToken(), "3933090531512320");
    assertEquals(transaction.isSuccessful(), new Boolean(true));
    assertEquals(transaction.getSourceAccount(), "GCUB7JL4APK7LKJ6MZF7Q2JTLHAGNBIUA7XIXD5SQTG52GQ2DAT6XZMK");
    assertEquals(transaction.getSourceAccountSequence(), new Long(2373051035426646L));
    assertEquals(transaction.getFeePaid(), new Long(100));
    assertEquals(transaction.getOperationCount(), new Integer(1));
    assertEquals(transaction.getEnvelopeXdr(), "AAAAAKgfpXwD1fWpPmZL+GkzWcBmhRQH7ouPsoTN3RoaGCfrAAAAZAAIbkcAAB9WAAAAAAAAAANRBBZE6D1qyGjISUGLY5Ldvp31PwAAAAAAAAAAAAAAAAAAAAEAAAABAAAAAP1qe44j+i4uIT+arbD4QDQBt8ryEeJd7a0jskQ3nwDeAAAAAAAAAADA7RnarSzCwj3OT+M2btCMFpVBdqxJS+Sr00qBjtFv7gAAAABLCs/QAAAAAAAAAAEaGCfrAAAAQG/56Cj2J8W/KCZr+oC4sWND1CTGWfaccHNtuibQH8kZIb+qBSDY94g7hiaAXrlIeg9b7oz/XuP3x9MWYw2jtwM=");
    assertEquals(transaction.getResultXdr(), "AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAA=");
    assertEquals(transaction.getResultMetaXdr(), "AAAAAAAAAAEAAAACAAAAAAAN+SAAAAAAAAAAAMDtGdqtLMLCPc5P4zZu0IwWlUF2rElL5KvTSoGO0W/uAAAAAEsKz9AADfkgAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAQAN+SAAAAAAAAAAAP1qe44j+i4uIT+arbD4QDQBt8ryEeJd7a0jskQ3nwDeAAHp6WMr55YACD1BAAAAHgAAAAoAAAAAAAAAAAAAAAABAAAAAAAACgAAAAARC07BokpLTOF+/vVKBwiAlop7hHGJTNeGGlY4MoPykwAAAAEAAAAAK+Lzfd3yDD+Ov0GbYu1g7SaIBrKZeBUxoCunkLuI7aoAAAABAAAAAERmsKL73CyLV/HvjyQCERDXXpWE70Xhyb6MR5qPO3yQAAAAAQAAAABSORGwAdyuanN3sNOHqNSpACyYdkUM3L8VafUu69EvEgAAAAEAAAAAeCzqJNkMM/jLvyuMIfyFHljBlLCtDyj17RMycPuNtRMAAAABAAAAAIEi4R7juq15ymL00DNlAddunyFT4FyUD4muC4t3bobdAAAAAQAAAACaNpLL5YMfjOTdXVEqrAh99LM12sN6He6pHgCRAa1f1QAAAAEAAAAAqB+lfAPV9ak+Zkv4aTNZwGaFFAfui4+yhM3dGhoYJ+sAAAABAAAAAMNJrEvdMg6M+M+n4BDIdzsVSj/ZI9SvAp7mOOsvAD/WAAAAAQAAAADbHA6xiKB1+G79mVqpsHMOleOqKa5mxDpP5KEp/Xdz9wAAAAEAAAAAAAAAAA==");

    assertTrue(transaction.getMemo() instanceof MemoHash);
    MemoHash memo = (MemoHash) transaction.getMemo();
    assertEquals("51041644e83d6ac868c849418b6392ddbe9df53f000000000000000000000000", memo.getHexValue());

    assertEquals(transaction.getLinks().getAccount().getHref(), "/accounts/GCUB7JL4APK7LKJ6MZF7Q2JTLHAGNBIUA7XIXD5SQTG52GQ2DAT6XZMK");
    assertEquals(transaction.getLinks().getEffects().getHref(), "/transactions/5c2e4dad596941ef944d72741c8f8f1a4282f8f2f141e81d827f44bf365d626b/effects{?cursor,limit,order}");
    assertEquals(transaction.getLinks().getLedger().getHref(), "/ledgers/915744");
    assertEquals(transaction.getLinks().getOperations().getHref(), "/transactions/5c2e4dad596941ef944d72741c8f8f1a4282f8f2f141e81d827f44bf365d626b/operations{?cursor,limit,order}");
    assertEquals(transaction.getLinks().getPrecedes().getHref(), "/transactions?cursor=3933090531512320&order=asc");
    assertEquals(transaction.getLinks().getSelf().getHref(), "/transactions/5c2e4dad596941ef944d72741c8f8f1a4282f8f2f141e81d827f44bf365d626b");
    assertEquals(transaction.getLinks().getSucceeds().getHref(), "/transactions?cursor=3933090531512320&order=desc");
  }

  @Test
  public void testDeserializeWithoutMemo() {
    TransactionResponse transaction = GsonSingleton.getInstance().fromJson(jsonMemoNone, TransactionResponse.class);
    assertTrue(transaction.getMemo() instanceof MemoNone);
    assertEquals(transaction.isSuccessful(), new Boolean(false));
  }

  String json = "{\n" +
          "  \"_links\": {\n" +
          "    \"account\": {\n" +
          "      \"href\": \"/accounts/GCUB7JL4APK7LKJ6MZF7Q2JTLHAGNBIUA7XIXD5SQTG52GQ2DAT6XZMK\"\n" +
          "    },\n" +
          "    \"effects\": {\n" +
          "      \"href\": \"/transactions/5c2e4dad596941ef944d72741c8f8f1a4282f8f2f141e81d827f44bf365d626b/effects{?cursor,limit,order}\",\n" +
          "      \"templated\": true\n" +
          "    },\n" +
          "    \"ledger\": {\n" +
          "      \"href\": \"/ledgers/915744\"\n" +
          "    },\n" +
          "    \"operations\": {\n" +
          "      \"href\": \"/transactions/5c2e4dad596941ef944d72741c8f8f1a4282f8f2f141e81d827f44bf365d626b/operations{?cursor,limit,order}\",\n" +
          "      \"templated\": true\n" +
          "    },\n" +
          "    \"precedes\": {\n" +
          "      \"href\": \"/transactions?cursor=3933090531512320\\u0026order=asc\"\n" +
          "    },\n" +
          "    \"self\": {\n" +
          "      \"href\": \"/transactions/5c2e4dad596941ef944d72741c8f8f1a4282f8f2f141e81d827f44bf365d626b\"\n" +
          "    },\n" +
          "    \"succeeds\": {\n" +
          "      \"href\": \"/transactions?cursor=3933090531512320\\u0026order=desc\"\n" +
          "    }\n" +
          "  },\n" +
          "  \"id\": \"5c2e4dad596941ef944d72741c8f8f1a4282f8f2f141e81d827f44bf365d626b\",\n" +
          "  \"paging_token\": \"3933090531512320\",\n" +
          "  \"successful\": true,\n" +
          "  \"hash\": \"5c2e4dad596941ef944d72741c8f8f1a4282f8f2f141e81d827f44bf365d626b\",\n" +
          "  \"ledger\": 915744,\n" +
          "  \"created_at\": \"2015-11-20T17:01:28Z\",\n" +
          "  \"source_account\": \"GCUB7JL4APK7LKJ6MZF7Q2JTLHAGNBIUA7XIXD5SQTG52GQ2DAT6XZMK\",\n" +
          "  \"source_account_sequence\": 2373051035426646,\n" +
          "  \"fee_paid\": 100,\n" +
          "  \"operation_count\": 1,\n" +
          "  \"envelope_xdr\": \"AAAAAKgfpXwD1fWpPmZL+GkzWcBmhRQH7ouPsoTN3RoaGCfrAAAAZAAIbkcAAB9WAAAAAAAAAANRBBZE6D1qyGjISUGLY5Ldvp31PwAAAAAAAAAAAAAAAAAAAAEAAAABAAAAAP1qe44j+i4uIT+arbD4QDQBt8ryEeJd7a0jskQ3nwDeAAAAAAAAAADA7RnarSzCwj3OT+M2btCMFpVBdqxJS+Sr00qBjtFv7gAAAABLCs/QAAAAAAAAAAEaGCfrAAAAQG/56Cj2J8W/KCZr+oC4sWND1CTGWfaccHNtuibQH8kZIb+qBSDY94g7hiaAXrlIeg9b7oz/XuP3x9MWYw2jtwM=\",\n" +
          "  \"result_xdr\": \"AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAA=\",\n" +
          "  \"result_meta_xdr\": \"AAAAAAAAAAEAAAACAAAAAAAN+SAAAAAAAAAAAMDtGdqtLMLCPc5P4zZu0IwWlUF2rElL5KvTSoGO0W/uAAAAAEsKz9AADfkgAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAQAN+SAAAAAAAAAAAP1qe44j+i4uIT+arbD4QDQBt8ryEeJd7a0jskQ3nwDeAAHp6WMr55YACD1BAAAAHgAAAAoAAAAAAAAAAAAAAAABAAAAAAAACgAAAAARC07BokpLTOF+/vVKBwiAlop7hHGJTNeGGlY4MoPykwAAAAEAAAAAK+Lzfd3yDD+Ov0GbYu1g7SaIBrKZeBUxoCunkLuI7aoAAAABAAAAAERmsKL73CyLV/HvjyQCERDXXpWE70Xhyb6MR5qPO3yQAAAAAQAAAABSORGwAdyuanN3sNOHqNSpACyYdkUM3L8VafUu69EvEgAAAAEAAAAAeCzqJNkMM/jLvyuMIfyFHljBlLCtDyj17RMycPuNtRMAAAABAAAAAIEi4R7juq15ymL00DNlAddunyFT4FyUD4muC4t3bobdAAAAAQAAAACaNpLL5YMfjOTdXVEqrAh99LM12sN6He6pHgCRAa1f1QAAAAEAAAAAqB+lfAPV9ak+Zkv4aTNZwGaFFAfui4+yhM3dGhoYJ+sAAAABAAAAAMNJrEvdMg6M+M+n4BDIdzsVSj/ZI9SvAp7mOOsvAD/WAAAAAQAAAADbHA6xiKB1+G79mVqpsHMOleOqKa5mxDpP5KEp/Xdz9wAAAAEAAAAAAAAAAA==\",\n" +
          "  \"memo_type\": \"hash\",\n" +
          "  \"memo\": \"UQQWROg9ashoyElBi2OS3b6d9T8AAAAAAAAAAAAAAAA=\",\n" +
          "  \"signatures\": [\n" +
          "    \"b/noKPYnxb8oJmv6gLixY0PUJMZZ9pxwc226JtAfyRkhv6oFINj3iDuGJoBeuUh6D1vujP9e4/fH0xZjDaO3Aw==\"\n" +
          "  ]\n" +
          "}";

  String jsonMemoNone = "{\n" +
          "  \"_links\": {\n" +
          "    \"account\": {\n" +
          "      \"href\": \"/accounts/GCUB7JL4APK7LKJ6MZF7Q2JTLHAGNBIUA7XIXD5SQTG52GQ2DAT6XZMK\"\n" +
          "    },\n" +
          "    \"effects\": {\n" +
          "      \"href\": \"/transactions/5c2e4dad596941ef944d72741c8f8f1a4282f8f2f141e81d827f44bf365d626b/effects{?cursor,limit,order}\",\n" +
          "      \"templated\": true\n" +
          "    },\n" +
          "    \"ledger\": {\n" +
          "      \"href\": \"/ledgers/915744\"\n" +
          "    },\n" +
          "    \"operations\": {\n" +
          "      \"href\": \"/transactions/5c2e4dad596941ef944d72741c8f8f1a4282f8f2f141e81d827f44bf365d626b/operations{?cursor,limit,order}\",\n" +
          "      \"templated\": true\n" +
          "    },\n" +
          "    \"precedes\": {\n" +
          "      \"href\": \"/transactions?cursor=3933090531512320\\u0026order=asc\"\n" +
          "    },\n" +
          "    \"self\": {\n" +
          "      \"href\": \"/transactions/5c2e4dad596941ef944d72741c8f8f1a4282f8f2f141e81d827f44bf365d626b\"\n" +
          "    },\n" +
          "    \"succeeds\": {\n" +
          "      \"href\": \"/transactions?cursor=3933090531512320\\u0026order=desc\"\n" +
          "    }\n" +
          "  },\n" +
          "  \"id\": \"5c2e4dad596941ef944d72741c8f8f1a4282f8f2f141e81d827f44bf365d626b\",\n" +
          "  \"paging_token\": \"3933090531512320\",\n" +
          "  \"successful\": false,\n" +
          "  \"hash\": \"5c2e4dad596941ef944d72741c8f8f1a4282f8f2f141e81d827f44bf365d626b\",\n" +
          "  \"ledger\": 915744,\n" +
          "  \"created_at\": \"2015-11-20T17:01:28Z\",\n" +
          "  \"source_account\": \"GCUB7JL4APK7LKJ6MZF7Q2JTLHAGNBIUA7XIXD5SQTG52GQ2DAT6XZMK\",\n" +
          "  \"source_account_sequence\": 2373051035426646,\n" +
          "  \"fee_paid\": 100,\n" +
          "  \"operation_count\": 1,\n" +
          "  \"envelope_xdr\": \"AAAAAKgfpXwD1fWpPmZL+GkzWcBmhRQH7ouPsoTN3RoaGCfrAAAAZAAIbkcAAB9WAAAAAAAAAANRBBZE6D1qyGjISUGLY5Ldvp31PwAAAAAAAAAAAAAAAAAAAAEAAAABAAAAAP1qe44j+i4uIT+arbD4QDQBt8ryEeJd7a0jskQ3nwDeAAAAAAAAAADA7RnarSzCwj3OT+M2btCMFpVBdqxJS+Sr00qBjtFv7gAAAABLCs/QAAAAAAAAAAEaGCfrAAAAQG/56Cj2J8W/KCZr+oC4sWND1CTGWfaccHNtuibQH8kZIb+qBSDY94g7hiaAXrlIeg9b7oz/XuP3x9MWYw2jtwM=\",\n" +
          "  \"result_xdr\": \"AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAA=\",\n" +
          "  \"result_meta_xdr\": \"AAAAAAAAAAEAAAACAAAAAAAN+SAAAAAAAAAAAMDtGdqtLMLCPc5P4zZu0IwWlUF2rElL5KvTSoGO0W/uAAAAAEsKz9AADfkgAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAQAN+SAAAAAAAAAAAP1qe44j+i4uIT+arbD4QDQBt8ryEeJd7a0jskQ3nwDeAAHp6WMr55YACD1BAAAAHgAAAAoAAAAAAAAAAAAAAAABAAAAAAAACgAAAAARC07BokpLTOF+/vVKBwiAlop7hHGJTNeGGlY4MoPykwAAAAEAAAAAK+Lzfd3yDD+Ov0GbYu1g7SaIBrKZeBUxoCunkLuI7aoAAAABAAAAAERmsKL73CyLV/HvjyQCERDXXpWE70Xhyb6MR5qPO3yQAAAAAQAAAABSORGwAdyuanN3sNOHqNSpACyYdkUM3L8VafUu69EvEgAAAAEAAAAAeCzqJNkMM/jLvyuMIfyFHljBlLCtDyj17RMycPuNtRMAAAABAAAAAIEi4R7juq15ymL00DNlAddunyFT4FyUD4muC4t3bobdAAAAAQAAAACaNpLL5YMfjOTdXVEqrAh99LM12sN6He6pHgCRAa1f1QAAAAEAAAAAqB+lfAPV9ak+Zkv4aTNZwGaFFAfui4+yhM3dGhoYJ+sAAAABAAAAAMNJrEvdMg6M+M+n4BDIdzsVSj/ZI9SvAp7mOOsvAD/WAAAAAQAAAADbHA6xiKB1+G79mVqpsHMOleOqKa5mxDpP5KEp/Xdz9wAAAAEAAAAAAAAAAA==\",\n" +
          "  \"memo_type\": \"none\",\n" +
          "  \"signatures\": [\n" +
          "    \"b/noKPYnxb8oJmv6gLixY0PUJMZZ9pxwc226JtAfyRkhv6oFINj3iDuGJoBeuUh6D1vujP9e4/fH0xZjDaO3Aw==\"\n" +
          "  ]\n" +
          "}";
}
