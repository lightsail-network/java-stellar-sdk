package org.stellar.sdk.responses;

import junit.framework.TestCase;

import org.junit.Test;
import org.stellar.sdk.AssetTypeCreditAlphaNum4;
import org.stellar.sdk.CreateAccountOperation;
import org.stellar.sdk.LedgerEntryChange;
import org.stellar.sdk.LedgerEntryChanges;
import org.stellar.sdk.MemoHash;
import org.stellar.sdk.MemoNone;
import org.stellar.sdk.Operation;
import org.stellar.sdk.TrustLineLedgerEntryChange;

import java.util.List;

public class TransactionDeserializerTest extends TestCase {
  @Test
  public void testDeserialize() {
    TransactionResponse transaction = GsonSingleton.getInstance().fromJson(json, TransactionResponse.class);
    assertEquals(transaction.getHash(), "5c2e4dad596941ef944d72741c8f8f1a4282f8f2f141e81d827f44bf365d626b");
    assertEquals(transaction.getLedger(), new Long(915744));
    assertEquals(transaction.getCreatedAt(), "2015-11-20T17:01:28Z");
    assertEquals(transaction.getPagingToken(), "3933090531512320");
    assertEquals(transaction.getSourceAccount().getAccountId(), "GCUB7JL4APK7LKJ6MZF7Q2JTLHAGNBIUA7XIXD5SQTG52GQ2DAT6XZMK");
    assertEquals(transaction.getSourceAccountSequence(), new Long(2373051035426646L));
    assertEquals(transaction.getFeePaid(), new Long(100));
    assertEquals(transaction.getOperationCount(), new Integer(1));
    assertEquals(transaction.getEnvelopeXdr(), "AAAAAKgfpXwD1fWpPmZL+GkzWcBmhRQH7ouPsoTN3RoaGCfrAAAAZAAIbkcAAB9WAAAAAAAAAANRBBZE6D1qyGjISUGLY5Ldvp31PwAAAAAAAAAAAAAAAAAAAAEAAAABAAAAAP1qe44j+i4uIT+arbD4QDQBt8ryEeJd7a0jskQ3nwDeAAAAAAAAAADA7RnarSzCwj3OT+M2btCMFpVBdqxJS+Sr00qBjtFv7gAAAABLCs/QAAAAAAAAAAEaGCfrAAAAQG/56Cj2J8W/KCZr+oC4sWND1CTGWfaccHNtuibQH8kZIb+qBSDY94g7hiaAXrlIeg9b7oz/XuP3x9MWYw2jtwM=");
    assertEquals(transaction.getResultXdr(), "AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAA=");
    assertEquals(transaction.getResultMetaXdr(), "AAAAAAAAAAEAAAACAAAAAAAN+SAAAAAAAAAAAMDtGdqtLMLCPc5P4zZu0IwWlUF2rElL5KvTSoGO0W/uAAAAAEsKz9AADfkgAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAQAN+SAAAAAAAAAAAP1qe44j+i4uIT+arbD4QDQBt8ryEeJd7a0jskQ3nwDeAAHp6WMr55YACD1BAAAAHgAAAAoAAAAAAAAAAAAAAAABAAAAAAAACgAAAAARC07BokpLTOF+/vVKBwiAlop7hHGJTNeGGlY4MoPykwAAAAEAAAAAK+Lzfd3yDD+Ov0GbYu1g7SaIBrKZeBUxoCunkLuI7aoAAAABAAAAAERmsKL73CyLV/HvjyQCERDXXpWE70Xhyb6MR5qPO3yQAAAAAQAAAABSORGwAdyuanN3sNOHqNSpACyYdkUM3L8VafUu69EvEgAAAAEAAAAAeCzqJNkMM/jLvyuMIfyFHljBlLCtDyj17RMycPuNtRMAAAABAAAAAIEi4R7juq15ymL00DNlAddunyFT4FyUD4muC4t3bobdAAAAAQAAAACaNpLL5YMfjOTdXVEqrAh99LM12sN6He6pHgCRAa1f1QAAAAEAAAAAqB+lfAPV9ak+Zkv4aTNZwGaFFAfui4+yhM3dGhoYJ+sAAAABAAAAAMNJrEvdMg6M+M+n4BDIdzsVSj/ZI9SvAp7mOOsvAD/WAAAAAQAAAADbHA6xiKB1+G79mVqpsHMOleOqKa5mxDpP5KEp/Xdz9wAAAAEAAAAAAAAAAA==");
    List<Operation> operations = transaction.getOperations();
    assertNotNull(operations);
    assertEquals(operations.size(), 1);
    assertTrue(operations.get(0) instanceof CreateAccountOperation);
    CreateAccountOperation createAccountOperation = (CreateAccountOperation) operations.get(0);
    assertEquals(createAccountOperation.getSourceAccount().getAccountId(), "GD6WU64OEP5C4LRBH6NK3MHYIA2ADN6K6II6EXPNVUR3ERBXT4AN4ACD");
    assertEquals(createAccountOperation.getDestination().getAccountId(), "GDAO2GO2VUWMFQR5ZZH6GNTO2CGBNFKBO2WESS7EVPJUVAMO2FX64V4B");

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
  public void testGetLedgerChanges() {
    TransactionResponse transaction = GsonSingleton.getInstance().fromJson(jsonLedgerChanges, TransactionResponse.class);
    List<LedgerEntryChanges> ledgerChangesPerOperation = transaction.getLedgerChanges();

    assertEquals(1, ledgerChangesPerOperation.size());
    LedgerEntryChanges ledgerEntryChanges = ledgerChangesPerOperation.get(0);
    assertEquals(2, ledgerEntryChanges.getLedgerEntryUpdates().length);
    assertEquals(2, ledgerEntryChanges.getLedgerEntryStates().length);
    LedgerEntryChange sentAccountState = ledgerEntryChanges.getLedgerEntryStates()[0];
    assertTrue(sentAccountState instanceof TrustLineLedgerEntryChange);
    TrustLineLedgerEntryChange sentAccountTrustLineState = (TrustLineLedgerEntryChange) sentAccountState;
    assertEquals("GA62NHJCFCAEOZXTBA7DG6PTGSHYJP2UTMYFCZV2OQKDC3CMXBFMIICL", sentAccountTrustLineState.getAccountID().getAccountId());
    assertEquals("922337203685.4775807", sentAccountTrustLineState.getLimit());
    assertEquals("3982", sentAccountTrustLineState.getBalance());
    assertTrue(sentAccountTrustLineState.getAsset() instanceof AssetTypeCreditAlphaNum4);
    assertEquals("KIN", ((AssetTypeCreditAlphaNum4) sentAccountTrustLineState.getAsset()).getCode());
    assertEquals("GCKG5WGBIJP74UDNRIRDFGENNIH5Y3KBI5IHREFAJKV4MQXLELT7EX6V", ((AssetTypeCreditAlphaNum4) sentAccountTrustLineState.getAsset()).getIssuer().getAccountId());

    LedgerEntryChange receivedAccountState = ledgerEntryChanges.getLedgerEntryStates()[1];
    assertTrue(receivedAccountState instanceof TrustLineLedgerEntryChange);
    TrustLineLedgerEntryChange recievedAccountTrustLineState = (TrustLineLedgerEntryChange) receivedAccountState;
    assertEquals("GDHPNRNU5PCP46DPW3MHK74XQU3BOMVBJKCTW3DV4WZNBOJB6JTJTFXC", recievedAccountTrustLineState.getAccountID().getAccountId());
    assertEquals("922337203685.4775807", recievedAccountTrustLineState.getLimit());
    assertEquals("8039", recievedAccountTrustLineState.getBalance());
    assertTrue(recievedAccountTrustLineState.getAsset() instanceof AssetTypeCreditAlphaNum4);
    assertEquals("KIN", ((AssetTypeCreditAlphaNum4) recievedAccountTrustLineState.getAsset()).getCode());
    assertEquals("GCKG5WGBIJP74UDNRIRDFGENNIH5Y3KBI5IHREFAJKV4MQXLELT7EX6V", ((AssetTypeCreditAlphaNum4) recievedAccountTrustLineState.getAsset()).getIssuer().getAccountId());

    LedgerEntryChange sentAccountUpdate = ledgerEntryChanges.getLedgerEntryUpdates()[0];
    assertTrue(sentAccountUpdate instanceof TrustLineLedgerEntryChange);
    TrustLineLedgerEntryChange sentAccountTrustLineUpdate = (TrustLineLedgerEntryChange) sentAccountUpdate;
    assertEquals("GA62NHJCFCAEOZXTBA7DG6PTGSHYJP2UTMYFCZV2OQKDC3CMXBFMIICL", sentAccountTrustLineUpdate.getAccountID().getAccountId());
    assertEquals("922337203685.4775807", sentAccountTrustLineUpdate.getLimit());
    assertEquals("3961", sentAccountTrustLineUpdate.getBalance());
    assertTrue(sentAccountTrustLineUpdate.getAsset() instanceof AssetTypeCreditAlphaNum4);
    assertEquals("KIN", ((AssetTypeCreditAlphaNum4) sentAccountTrustLineUpdate.getAsset()).getCode());
    assertEquals("GCKG5WGBIJP74UDNRIRDFGENNIH5Y3KBI5IHREFAJKV4MQXLELT7EX6V", ((AssetTypeCreditAlphaNum4) sentAccountTrustLineUpdate.getAsset()).getIssuer().getAccountId());


    LedgerEntryChange receivedAccountUpdate = ledgerEntryChanges.getLedgerEntryUpdates()[1];
    assertTrue(receivedAccountUpdate instanceof TrustLineLedgerEntryChange);
    TrustLineLedgerEntryChange receivedAccountTrustLineUpdate = (TrustLineLedgerEntryChange) receivedAccountUpdate;
    assertEquals("GDHPNRNU5PCP46DPW3MHK74XQU3BOMVBJKCTW3DV4WZNBOJB6JTJTFXC", receivedAccountTrustLineUpdate.getAccountID().getAccountId());
    assertEquals("922337203685.4775807", receivedAccountTrustLineUpdate.getLimit());
    assertEquals("8060", receivedAccountTrustLineUpdate.getBalance());
    assertTrue(receivedAccountTrustLineUpdate.getAsset() instanceof AssetTypeCreditAlphaNum4);
    assertEquals("KIN", ((AssetTypeCreditAlphaNum4) receivedAccountTrustLineUpdate.getAsset()).getCode());
    assertEquals("GCKG5WGBIJP74UDNRIRDFGENNIH5Y3KBI5IHREFAJKV4MQXLELT7EX6V", ((AssetTypeCreditAlphaNum4) receivedAccountTrustLineUpdate.getAsset()).getIssuer().getAccountId());
  }

  @Test
  public void testDeserializeWithoutMemo() {
    TransactionResponse transaction = GsonSingleton.getInstance().fromJson(jsonMemoNone, TransactionResponse.class);
    assertTrue(transaction.getMemo() instanceof MemoNone);
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
  String jsonLedgerChanges = "{\n" +
      "        \"_links\": {\n" +
      "          \"self\": {\n" +
      "            \"href\": \"https://horizon-testnet.stellar.org/transactions/5d1f0d3ddcc04532bcb9d8796fc6beded444e366d2d5b5fc0aa90a2d63457125\"\n" +
      "          },\n" +
      "          \"account\": {\n" +
      "            \"href\": \"https://horizon-testnet.stellar.org/accounts/GA62NHJCFCAEOZXTBA7DG6PTGSHYJP2UTMYFCZV2OQKDC3CMXBFMIICL\"\n" +
      "          },\n" +
      "          \"ledger\": {\n" +
      "            \"href\": \"https://horizon-testnet.stellar.org/ledgers/7502647\"\n" +
      "          },\n" +
      "          \"operations\": {\n" +
      "            \"href\": \"https://horizon-testnet.stellar.org/transactions/5d1f0d3ddcc04532bcb9d8796fc6beded444e366d2d5b5fc0aa90a2d63457125/operations{?cursor,limit,order}\",\n" +
      "            \"templated\": true\n" +
      "          },\n" +
      "          \"effects\": {\n" +
      "            \"href\": \"https://horizon-testnet.stellar.org/transactions/5d1f0d3ddcc04532bcb9d8796fc6beded444e366d2d5b5fc0aa90a2d63457125/effects{?cursor,limit,order}\",\n" +
      "            \"templated\": true\n" +
      "          },\n" +
      "          \"precedes\": {\n" +
      "            \"href\": \"https://horizon-testnet.stellar.org/transactions?order=asc&cursor=32223623498436608\"\n" +
      "          },\n" +
      "          \"succeeds\": {\n" +
      "            \"href\": \"https://horizon-testnet.stellar.org/transactions?order=desc&cursor=32223623498436608\"\n" +
      "          }\n" +
      "        },\n" +
      "        \"id\": \"5d1f0d3ddcc04532bcb9d8796fc6beded444e366d2d5b5fc0aa90a2d63457125\",\n" +
      "        \"paging_token\": \"32223623498436608\",\n" +
      "        \"hash\": \"5d1f0d3ddcc04532bcb9d8796fc6beded444e366d2d5b5fc0aa90a2d63457125\",\n" +
      "        \"ledger\": 7502647,\n" +
      "        \"created_at\": \"2018-02-21T12:36:54Z\",\n" +
      "        \"source_account\": \"GA62NHJCFCAEOZXTBA7DG6PTGSHYJP2UTMYFCZV2OQKDC3CMXBFMIICL\",\n" +
      "        \"source_account_sequence\": \"31945910913073158\",\n" +
      "        \"fee_paid\": 100,\n" +
      "        \"operation_count\": 1,\n" +
      "        \"envelope_xdr\": \"AAAAAD2mnSIogEdm8wg+M3nzNI+Ev1SbMFFmunQUMWxMuErEAAAAZABxfqMAAAAGAAAAAAAAAAEAAAAJc29tZSBtZW1vAAAAAAAAAQAAAAAAAAABAAAAAM72xbTrxP54b7bYdX+XhTYXMqFKhTtsdeWy0Lkh8maZAAAAAUtJTgAAAAAAlG7YwUJf/lBtiiIymI1qD9xtQUdQeJCgSqvGQusi5/IAAAAADIRYgAAAAAAAAAABTLhKxAAAAEC4yCf0EqZ+Id34dRiQ64xhffQsqDiRw5ZAeo6Q2MV/bqj9UTPt5hQljYHxClqbObQzgy0VvWs+9yI0/xfjLmcL\",\n" +
      "        \"result_xdr\": \"AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAABAAAAAAAAAAA=\",\n" +
      "        \"result_meta_xdr\": \"AAAAAAAAAAEAAAAEAAAAAwByexUAAAABAAAAAD2mnSIogEdm8wg+M3nzNI+Ev1SbMFFmunQUMWxMuErEAAAAAUtJTgAAAAAAlG7YwUJf/lBtiiIymI1qD9xtQUdQeJCgSqvGQusi5/IAAAAJRXT7AH//////////AAAAAQAAAAAAAAAAAAAAAQByezcAAAABAAAAAD2mnSIogEdm8wg+M3nzNI+Ev1SbMFFmunQUMWxMuErEAAAAAUtJTgAAAAAAlG7YwUJf/lBtiiIymI1qD9xtQUdQeJCgSqvGQusi5/IAAAAJOPCigH//////////AAAAAQAAAAAAAAAAAAAAAwByexUAAAABAAAAAM72xbTrxP54b7bYdX+XhTYXMqFKhTtsdeWy0Lkh8maZAAAAAUtJTgAAAAAAlG7YwUJf/lBtiiIymI1qD9xtQUdQeJCgSqvGQusi5/IAAAASt54NgH//////////AAAAAQAAAAAAAAAAAAAAAQByezcAAAABAAAAAM72xbTrxP54b7bYdX+XhTYXMqFKhTtsdeWy0Lkh8maZAAAAAUtJTgAAAAAAlG7YwUJf/lBtiiIymI1qD9xtQUdQeJCgSqvGQusi5/IAAAASxCJmAH//////////AAAAAQAAAAAAAAAA\",\n" +
      "        \"fee_meta_xdr\": \"AAAAAgAAAAMAcnsVAAAAAAAAAAA9pp0iKIBHZvMIPjN58zSPhL9UmzBRZrp0FDFsTLhKxAAAAAABMSsMAHF+owAAAAUAAAABAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAEAcns3AAAAAAAAAAA9pp0iKIBHZvMIPjN58zSPhL9UmzBRZrp0FDFsTLhKxAAAAAABMSqoAHF+owAAAAYAAAABAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAA==\",\n" +
      "        \"memo_type\": \"text\",\n" +
      "        \"memo\": \"some memo\",\n" +
      "        \"signatures\": [\n" +
      "          \"uMgn9BKmfiHd+HUYkOuMYX30LKg4kcOWQHqOkNjFf26o/VEz7eYUJY2B8Qpamzm0M4MtFb1rPvciNP8X4y5nCw==\"\n" +
      "        ]\n" +
      "      }";
}
