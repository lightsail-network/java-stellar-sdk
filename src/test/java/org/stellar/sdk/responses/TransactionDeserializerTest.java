package org.stellar.sdk.responses;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import junit.framework.TestCase;

import org.junit.Test;
import org.stellar.sdk.MemoHash;
import org.stellar.sdk.MemoNone;

public class TransactionDeserializerTest extends TestCase {
  @Test
  public void testDeserializeFeeBump() {
    TransactionResponse transaction = GsonSingleton.getInstance().fromJson(jsonFeeBump, TransactionResponse.class);
    assertEquals(transaction.getHash(), "3dfef7d7226995b504f2827cc63d45ad41e9687bb0a8abcf08ba755fedca0352");
    assertEquals(transaction.getLedger(), Long.valueOf(123));
    assertEquals(transaction.isSuccessful(), Boolean.TRUE);
    assertEquals(transaction.getSourceAccount(), "GABQGAYAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB2MX");
    assertEquals(transaction.getFeeAccount(), "GABAEAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABGKJ");
    assertEquals(transaction.getSourceAccountSequence(), Long.valueOf(97));
    assertEquals(transaction.getMaxFee(), Long.valueOf(776));
    assertEquals(transaction.getFeeCharged(), Long.valueOf(123));
    assertEquals(transaction.getOperationCount(), Integer.valueOf(1));
    assertEquals(transaction.getSignatures(), ImmutableList.of("Hh4e"));

    TransactionResponse.FeeBumpTransaction feeBumpTransaction = transaction.getFeeBump().get();
    assertEquals(feeBumpTransaction.getHash(), "3dfef7d7226995b504f2827cc63d45ad41e9687bb0a8abcf08ba755fedca0352");
    assertEquals(feeBumpTransaction.getSignatures(), ImmutableList.of("Hh4e"));

    TransactionResponse.InnerTransaction innerTransaction = transaction.getInner().get();
    assertEquals(innerTransaction.getHash(), "e98869bba8bce08c10b78406202127f3888c25454cd37b02600862452751f526");
    assertEquals(innerTransaction.getMaxFee(), Long.valueOf(99));
    assertEquals(innerTransaction.getSignatures(), ImmutableList.of("FBQU"));

    assertFalse(transaction.getSourceAccountMuxed().isPresent());
    assertFalse(transaction.getFeeAccountMuxed().isPresent());
  }

  @Test
  public void testDeserialize() {
    TransactionResponse transaction = GsonSingleton.getInstance().fromJson(json, TransactionResponse.class);
    assertEquals(transaction.getHash(), "5c2e4dad596941ef944d72741c8f8f1a4282f8f2f141e81d827f44bf365d626b");
    assertEquals(transaction.getLedger(), new Long(915744));
    assertEquals(transaction.getCreatedAt(), "2015-11-20T17:01:28Z");
    assertEquals(transaction.getPagingToken(), "3933090531512320");
    assertEquals(transaction.isSuccessful(), new Boolean(true));
    assertEquals(transaction.getSourceAccount(), "GCUB7JL4APK7LKJ6MZF7Q2JTLHAGNBIUA7XIXD5SQTG52GQ2DAT6XZMK");
    assertFalse(transaction.getSourceAccountMuxed().isPresent());
    assertFalse(transaction.getFeeAccountMuxed().isPresent());
    assertEquals(transaction.getSourceAccountSequence(), new Long(2373051035426646L));
    assertEquals(transaction.getMaxFee(), new Long(200));
    assertEquals(transaction.getFeeCharged(), new Long(100));
    assertEquals(transaction.getOperationCount(), new Integer(1));
    assertEquals(transaction.getEnvelopeXdr(), "AAAAAKgfpXwD1fWpPmZL+GkzWcBmhRQH7ouPsoTN3RoaGCfrAAAAZAAIbkcAAB9WAAAAAAAAAANRBBZE6D1qyGjISUGLY5Ldvp31PwAAAAAAAAAAAAAAAAAAAAEAAAABAAAAAP1qe44j+i4uIT+arbD4QDQBt8ryEeJd7a0jskQ3nwDeAAAAAAAAAADA7RnarSzCwj3OT+M2btCMFpVBdqxJS+Sr00qBjtFv7gAAAABLCs/QAAAAAAAAAAEaGCfrAAAAQG/56Cj2J8W/KCZr+oC4sWND1CTGWfaccHNtuibQH8kZIb+qBSDY94g7hiaAXrlIeg9b7oz/XuP3x9MWYw2jtwM=");
    assertEquals(transaction.getResultXdr(), "AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAA=");
    assertEquals(transaction.getResultMetaXdr(), "AAAAAAAAAAEAAAACAAAAAAAN+SAAAAAAAAAAAMDtGdqtLMLCPc5P4zZu0IwWlUF2rElL5KvTSoGO0W/uAAAAAEsKz9AADfkgAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAQAN+SAAAAAAAAAAAP1qe44j+i4uIT+arbD4QDQBt8ryEeJd7a0jskQ3nwDeAAHp6WMr55YACD1BAAAAHgAAAAoAAAAAAAAAAAAAAAABAAAAAAAACgAAAAARC07BokpLTOF+/vVKBwiAlop7hHGJTNeGGlY4MoPykwAAAAEAAAAAK+Lzfd3yDD+Ov0GbYu1g7SaIBrKZeBUxoCunkLuI7aoAAAABAAAAAERmsKL73CyLV/HvjyQCERDXXpWE70Xhyb6MR5qPO3yQAAAAAQAAAABSORGwAdyuanN3sNOHqNSpACyYdkUM3L8VafUu69EvEgAAAAEAAAAAeCzqJNkMM/jLvyuMIfyFHljBlLCtDyj17RMycPuNtRMAAAABAAAAAIEi4R7juq15ymL00DNlAddunyFT4FyUD4muC4t3bobdAAAAAQAAAACaNpLL5YMfjOTdXVEqrAh99LM12sN6He6pHgCRAa1f1QAAAAEAAAAAqB+lfAPV9ak+Zkv4aTNZwGaFFAfui4+yhM3dGhoYJ+sAAAABAAAAAMNJrEvdMg6M+M+n4BDIdzsVSj/ZI9SvAp7mOOsvAD/WAAAAAQAAAADbHA6xiKB1+G79mVqpsHMOleOqKa5mxDpP5KEp/Xdz9wAAAAEAAAAAAAAAAA==");
    assertEquals(transaction.getSignatures(), ImmutableList.of("b/noKPYnxb8oJmv6gLixY0PUJMZZ9pxwc226JtAfyRkhv6oFINj3iDuGJoBeuUh6D1vujP9e4/fH0xZjDaO3Aw=="));
    assertEquals(transaction.getFeeBump(), Optional.<TransactionResponse.FeeBumpTransaction>absent());
    assertEquals(transaction.getInner(), Optional.<TransactionResponse.InnerTransaction>absent());
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
  public void testDeserializeMuxed() {
    TransactionResponse transaction = GsonSingleton.getInstance().fromJson(muxed, TransactionResponse.class);
    assertEquals(transaction.getSourceAccountMuxed().get(), new MuxedAccount(
        "MBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA6AAAAAAAAAAAPN7BA",
        "GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L",
        123l
    ));
    assertEquals(transaction.getFeeAccountMuxed().get().getUnmuxedAddress(), "GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G");
    assertEquals(transaction.getFeeAccountMuxed().get().toString(), "MAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472YAAAAAAAAAABUSON4");
    assertEquals(transaction.getFeeAccountMuxed().get().getId().longValue(), 420l);
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
          "  \"max_fee\": 200,\n" +
          "  \"fee_charged\": 100,\n" +
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
          "  \"max_fee\": 200,\n" +
          "  \"fee_charged\": 100,\n" +
          "  \"operation_count\": 1,\n" +
          "  \"envelope_xdr\": \"AAAAAKgfpXwD1fWpPmZL+GkzWcBmhRQH7ouPsoTN3RoaGCfrAAAAZAAIbkcAAB9WAAAAAAAAAANRBBZE6D1qyGjISUGLY5Ldvp31PwAAAAAAAAAAAAAAAAAAAAEAAAABAAAAAP1qe44j+i4uIT+arbD4QDQBt8ryEeJd7a0jskQ3nwDeAAAAAAAAAADA7RnarSzCwj3OT+M2btCMFpVBdqxJS+Sr00qBjtFv7gAAAABLCs/QAAAAAAAAAAEaGCfrAAAAQG/56Cj2J8W/KCZr+oC4sWND1CTGWfaccHNtuibQH8kZIb+qBSDY94g7hiaAXrlIeg9b7oz/XuP3x9MWYw2jtwM=\",\n" +
          "  \"result_xdr\": \"AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAA=\",\n" +
          "  \"result_meta_xdr\": \"AAAAAAAAAAEAAAACAAAAAAAN+SAAAAAAAAAAAMDtGdqtLMLCPc5P4zZu0IwWlUF2rElL5KvTSoGO0W/uAAAAAEsKz9AADfkgAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAQAN+SAAAAAAAAAAAP1qe44j+i4uIT+arbD4QDQBt8ryEeJd7a0jskQ3nwDeAAHp6WMr55YACD1BAAAAHgAAAAoAAAAAAAAAAAAAAAABAAAAAAAACgAAAAARC07BokpLTOF+/vVKBwiAlop7hHGJTNeGGlY4MoPykwAAAAEAAAAAK+Lzfd3yDD+Ov0GbYu1g7SaIBrKZeBUxoCunkLuI7aoAAAABAAAAAERmsKL73CyLV/HvjyQCERDXXpWE70Xhyb6MR5qPO3yQAAAAAQAAAABSORGwAdyuanN3sNOHqNSpACyYdkUM3L8VafUu69EvEgAAAAEAAAAAeCzqJNkMM/jLvyuMIfyFHljBlLCtDyj17RMycPuNtRMAAAABAAAAAIEi4R7juq15ymL00DNlAddunyFT4FyUD4muC4t3bobdAAAAAQAAAACaNpLL5YMfjOTdXVEqrAh99LM12sN6He6pHgCRAa1f1QAAAAEAAAAAqB+lfAPV9ak+Zkv4aTNZwGaFFAfui4+yhM3dGhoYJ+sAAAABAAAAAMNJrEvdMg6M+M+n4BDIdzsVSj/ZI9SvAp7mOOsvAD/WAAAAAQAAAADbHA6xiKB1+G79mVqpsHMOleOqKa5mxDpP5KEp/Xdz9wAAAAEAAAAAAAAAAA==\",\n" +
          "  \"memo_type\": \"none\",\n" +
          "  \"signatures\": [\n" +
          "    \"b/noKPYnxb8oJmv6gLixY0PUJMZZ9pxwc226JtAfyRkhv6oFINj3iDuGJoBeuUh6D1vujP9e4/fH0xZjDaO3Aw==\"\n" +
          "  ]\n" +
          "}";

  String jsonFeeBump = "{\n" +
      "  \"_links\": {\n" +
      "    \"self\": {\n" +
      "      \"href\": \"http://localhost/transactions/3dfef7d7226995b504f2827cc63d45ad41e9687bb0a8abcf08ba755fedca0352\"\n" +
      "    },\n" +
      "    \"account\": {\n" +
      "      \"href\": \"http://localhost/accounts/GABQGAYAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB2MX\"\n" +
      "    },\n" +
      "    \"ledger\": {\n" +
      "      \"href\": \"http://localhost/ledgers/123\"\n" +
      "    },\n" +
      "    \"operations\": {\n" +
      "      \"href\": \"http://localhost/transactions/3dfef7d7226995b504f2827cc63d45ad41e9687bb0a8abcf08ba755fedca0352/operations{?cursor,limit,order}\",\n" +
      "      \"templated\": true\n" +
      "    },\n" +
      "    \"effects\": {\n" +
      "      \"href\": \"http://localhost/transactions/3dfef7d7226995b504f2827cc63d45ad41e9687bb0a8abcf08ba755fedca0352/effects{?cursor,limit,order}\",\n" +
      "      \"templated\": true\n" +
      "    },\n" +
      "    \"precedes\": {\n" +
      "      \"href\": \"http://localhost/transactions?order=asc\\u0026cursor=528280981504\"\n" +
      "    },\n" +
      "    \"succeeds\": {\n" +
      "      \"href\": \"http://localhost/transactions?order=desc\\u0026cursor=528280981504\"\n" +
      "    },\n" +
      "    \"transaction\": {\n" +
      "      \"href\": \"http://localhost/transactions/3dfef7d7226995b504f2827cc63d45ad41e9687bb0a8abcf08ba755fedca0352\"\n" +
      "    }\n" +
      "  },\n" +
      "  \"id\": \"3dfef7d7226995b504f2827cc63d45ad41e9687bb0a8abcf08ba755fedca0352\",\n" +
      "  \"paging_token\": \"528280981504\",\n" +
      "  \"successful\": true,\n" +
      "  \"hash\": \"3dfef7d7226995b504f2827cc63d45ad41e9687bb0a8abcf08ba755fedca0352\",\n" +
      "  \"ledger\": 123,\n" +
      "  \"created_at\": \"2020-04-21T10:21:26Z\",\n" +
      "  \"source_account\": \"GABQGAYAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB2MX\",\n" +
      "  \"source_account_sequence\": \"97\",\n" +
      "  \"fee_account\": \"GABAEAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABGKJ\",\n" +
      "  \"fee_charged\": 123,\n" +
      "  \"max_fee\": 776,\n" +
      "  \"operation_count\": 1,\n" +
      "  \"envelope_xdr\": \"AAAABQAAAAACAgIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMIAAAAAgAAAAADAwMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGMAAAAAAAAAYQAAAAEAAAAAAAAAAgAAAAAAAAAEAAAAAAAAAAEAAAAAAAAACwAAAAAAAABiAAAAAAAAAAECAgICAAAAAxQUFAAAAAAAAAAAAQMDAwMAAAADHh4eAA==\",\n" +
      "  \"result_xdr\": \"AAAAAAAAAHsAAAAB6Yhpu6i84IwQt4QGICEn84iMJUVM03sCYAhiRSdR9SYAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAsAAAAAAAAAAAAAAAA=\",\n" +
      "  \"result_meta_xdr\": \"AAAAAQAAAAAAAAAA\",\n" +
      "  \"fee_meta_xdr\": \"AAAAAA==\",\n" +
      "  \"memo_type\": \"none\",\n" +
      "  \"signatures\": [\n" +
      "    \"Hh4e\"\n" +
      "  ],\n" +
      "  \"valid_after\": \"1970-01-01T00:00:02Z\",\n" +
      "  \"valid_before\": \"1970-01-01T00:00:04Z\",\n" +
      "  \"fee_bump_transaction\": {\n" +
      "    \"hash\": \"3dfef7d7226995b504f2827cc63d45ad41e9687bb0a8abcf08ba755fedca0352\",\n" +
      "    \"signatures\": [\n" +
      "      \"Hh4e\"\n" +
      "    ]\n" +
      "  },\n" +
      "  \"inner_transaction\": {\n" +
      "    \"hash\": \"e98869bba8bce08c10b78406202127f3888c25454cd37b02600862452751f526\",\n" +
      "    \"signatures\": [\n" +
      "      \"FBQU\"\n" +
      "    ],\n" +
      "    \"max_fee\": \"99\"\n" +
      "  }\n" +
      "}";

  String muxed = "{\n" +
      "  \"_links\": {\n" +
      "    \"self\": {\n" +
      "      \"href\": \"http://localhost/transactions/3dfef7d7226995b504f2827cc63d45ad41e9687bb0a8abcf08ba755fedca0352\"\n" +
      "    },\n" +
      "    \"account\": {\n" +
      "      \"href\": \"http://localhost/accounts/GABQGAYAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB2MX\"\n" +
      "    },\n" +
      "    \"ledger\": {\n" +
      "      \"href\": \"http://localhost/ledgers/123\"\n" +
      "    },\n" +
      "    \"operations\": {\n" +
      "      \"href\": \"http://localhost/transactions/3dfef7d7226995b504f2827cc63d45ad41e9687bb0a8abcf08ba755fedca0352/operations{?cursor,limit,order}\",\n" +
      "      \"templated\": true\n" +
      "    },\n" +
      "    \"effects\": {\n" +
      "      \"href\": \"http://localhost/transactions/3dfef7d7226995b504f2827cc63d45ad41e9687bb0a8abcf08ba755fedca0352/effects{?cursor,limit,order}\",\n" +
      "      \"templated\": true\n" +
      "    },\n" +
      "    \"precedes\": {\n" +
      "      \"href\": \"http://localhost/transactions?order=asc\\u0026cursor=528280981504\"\n" +
      "    },\n" +
      "    \"succeeds\": {\n" +
      "      \"href\": \"http://localhost/transactions?order=desc\\u0026cursor=528280981504\"\n" +
      "    },\n" +
      "    \"transaction\": {\n" +
      "      \"href\": \"http://localhost/transactions/3dfef7d7226995b504f2827cc63d45ad41e9687bb0a8abcf08ba755fedca0352\"\n" +
      "    }\n" +
      "  },\n" +
      "  \"id\": \"3dfef7d7226995b504f2827cc63d45ad41e9687bb0a8abcf08ba755fedca0352\",\n" +
      "  \"paging_token\": \"528280981504\",\n" +
      "  \"successful\": true,\n" +
      "  \"hash\": \"3dfef7d7226995b504f2827cc63d45ad41e9687bb0a8abcf08ba755fedca0352\",\n" +
      "  \"ledger\": 123,\n" +
      "  \"created_at\": \"2020-04-21T10:21:26Z\",\n" +
      "  \"source_account\": \"GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\",\n" +
      "  \"account_muxed\": \"MBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA6AAAAAAAAAAAPN7BA\",\n" +
      "  \"account_muxed_id\": \"123\",\n" +
      "  \"source_account_sequence\": \"97\",\n" +
      "  \"fee_account\": \"GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\",\n" +
      "  \"fee_account_muxed\": \"MAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472YAAAAAAAAAABUSON4\",\n" +
      "  \"fee_account_muxed_id\": \"420\",\n" +
      "  \"fee_charged\": 123,\n" +
      "  \"max_fee\": 776,\n" +
      "  \"operation_count\": 1,\n" +
      "  \"envelope_xdr\": \"AAAABQAAAAACAgIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMIAAAAAgAAAAADAwMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGMAAAAAAAAAYQAAAAEAAAAAAAAAAgAAAAAAAAAEAAAAAAAAAAEAAAAAAAAACwAAAAAAAABiAAAAAAAAAAECAgICAAAAAxQUFAAAAAAAAAAAAQMDAwMAAAADHh4eAA==\",\n" +
      "  \"result_xdr\": \"AAAAAAAAAHsAAAAB6Yhpu6i84IwQt4QGICEn84iMJUVM03sCYAhiRSdR9SYAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAsAAAAAAAAAAAAAAAA=\",\n" +
      "  \"result_meta_xdr\": \"AAAAAQAAAAAAAAAA\",\n" +
      "  \"fee_meta_xdr\": \"AAAAAA==\",\n" +
      "  \"memo_type\": \"none\",\n" +
      "  \"signatures\": [\n" +
      "    \"Hh4e\"\n" +
      "  ],\n" +
      "  \"valid_after\": \"1970-01-01T00:00:02Z\",\n" +
      "  \"valid_before\": \"1970-01-01T00:00:04Z\",\n" +
      "  \"fee_bump_transaction\": {\n" +
      "    \"hash\": \"3dfef7d7226995b504f2827cc63d45ad41e9687bb0a8abcf08ba755fedca0352\",\n" +
      "    \"signatures\": [\n" +
      "      \"Hh4e\"\n" +
      "    ]\n" +
      "  },\n" +
      "  \"inner_transaction\": {\n" +
      "    \"hash\": \"e98869bba8bce08c10b78406202127f3888c25454cd37b02600862452751f526\",\n" +
      "    \"signatures\": [\n" +
      "      \"FBQU\"\n" +
      "    ],\n" +
      "    \"max_fee\": \"99\"\n" +
      "  }\n" +
      "}";
}
