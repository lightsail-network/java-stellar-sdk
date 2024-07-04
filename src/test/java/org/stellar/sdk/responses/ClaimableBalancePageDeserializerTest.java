package org.stellar.sdk.responses;

import com.google.gson.reflect.TypeToken;
import java.util.Optional;
import junit.framework.TestCase;
import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.Claimant;
import org.stellar.sdk.Predicate;
import org.stellar.sdk.xdr.ClaimPredicate;

public class ClaimableBalancePageDeserializerTest extends TestCase {

  private void roundTripPredicate(Predicate p) {
    ClaimPredicate xdrPredicate = p.toXdr();
    assertTrue(Predicate.fromXdr(xdrPredicate).equals(p));
  }

  @Test
  public void testDeserialize() {
    Page<ClaimableBalanceResponse> claimableBalancePage =
        GsonSingleton.getInstance()
            .fromJson(json, new TypeToken<Page<ClaimableBalanceResponse>>() {}.getType());
    assertEquals(claimableBalancePage.getRecords().size(), 2);

    assertEquals(
        claimableBalancePage.getRecords().get(0).getId(),
        "00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd2");
    assertEquals(
        claimableBalancePage.getRecords().get(0).getAsset(),
        Asset.create("COP:GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO"));
    assertEquals(
        claimableBalancePage.getRecords().get(0).getAssetString(),
        "COP:GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO");
    assertEquals(claimableBalancePage.getRecords().get(0).getAmount(), "1000.0000000");
    assertEquals(
        claimableBalancePage.getRecords().get(0).getSponsor(),
        Optional.of("GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO"));
    assertEquals(
        claimableBalancePage.getRecords().get(0).getLastModifiedLedger().intValue(), 117663);
    assertFalse(claimableBalancePage.getRecords().get(0).getFlags().getClawbackEnabled());
    assertEquals(
        claimableBalancePage.getRecords().get(0).getPagingToken(),
        "117663-00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd2");
    assertEquals(
        claimableBalancePage.getRecords().get(0).getClaimants().get(0).getDestination(),
        "GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO");
    Predicate.Or or =
        (Predicate.Or)
            claimableBalancePage.getRecords().get(0).getClaimants().get(0).getPredicate();
    Predicate.AbsBefore absBefore = (Predicate.AbsBefore) or.getLeft();
    Predicate.RelBefore relBefore = (Predicate.RelBefore) or.getRight();
    assertEquals(absBefore.getDate().toString(), "2020-09-28T17:57:04Z");
    assertEquals(relBefore.getSecondsSinceClose(), 12L);
    assertEquals(
        claimableBalancePage.getRecords().get(0).getLinks().getSelf().getHref(),
        "https://horizon.stellar.org/claimable_balances/00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd2");
    assertEquals(
        claimableBalancePage.getRecords().get(0).getLinks().getTransactions().getHref(),
        "https://horizon.stellar.org/claimable_balances/00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd2/transactions{?cursor,limit,order}");
    assertEquals(
        claimableBalancePage.getRecords().get(0).getLinks().getOperations().getHref(),
        "https://horizon.stellar.org/claimable_balances/00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd2/operations{?cursor,limit,order}");

    assertEquals(
        claimableBalancePage.getRecords().get(1).getId(),
        "00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd3");
    assertEquals(claimableBalancePage.getRecords().get(1).getAsset(), Asset.create("native"));
    assertEquals(claimableBalancePage.getRecords().get(1).getAssetString(), "native");
    assertEquals(claimableBalancePage.getRecords().get(1).getAmount(), "2000.0000000");
    assertFalse(claimableBalancePage.getRecords().get(1).getSponsor().isPresent());
    assertEquals(
        claimableBalancePage.getRecords().get(1).getLastModifiedLedger().intValue(), 117663);
    assertFalse(claimableBalancePage.getRecords().get(1).getFlags().getClawbackEnabled());
    assertEquals(
        claimableBalancePage.getRecords().get(1).getPagingToken(),
        "117663-00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd3");
    assertEquals(
        claimableBalancePage.getRecords().get(1).getClaimants().get(0).getDestination(),
        "GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO");

    Predicate.And and =
        (Predicate.And)
            claimableBalancePage.getRecords().get(1).getClaimants().get(0).getPredicate();
    absBefore = (Predicate.AbsBefore) and.getLeft();
    assertEquals(absBefore.getDate().toString(), "2020-09-28T17:57:04Z");
    Predicate.Not not = (Predicate.Not) and.getRight();
    assertEquals(new Predicate.Unconditional(), not.getInner());

    for (ClaimableBalanceResponse record : claimableBalancePage.getRecords()) {
      for (Claimant claimant : record.getClaimants()) {
        roundTripPredicate(claimant.getPredicate());
      }
    }
  }

  String json =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"self\": {\n"
          + "      \"href\": \"https://horizon-protocol14.stellar.org/claimable_balances?cursor=&limit=10&order=asc\"\n"
          + "    },\n"
          + "    \"next\": {\n"
          + "      \"href\": \"https://horizon-protocol14.stellar.org/claimable_balances?cursor=117663-00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd2&limit=10&order=asc\"\n"
          + "    },\n"
          + "    \"prev\": {\n"
          + "      \"href\": \"https://horizon-protocol14.stellar.org/claimable_balances?cursor=38888-00000000929b20b72e5890ab51c24f1cc46fa01c4f318d8d33367d24dd614cfdf5491072&limit=10&order=desc\"\n"
          + "    }\n"
          + "  },\n"
          + "  \"_embedded\": {\n"
          + "    \"records\": [\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "    \"self\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/claimable_balances/00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd2\"\n"
          + "    },\n"
          + "    \"transactions\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/claimable_balances/00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd2/transactions{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"operations\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/claimable_balances/00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd2/operations{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    }\n"
          + "  },\n"
          + "        \"id\": \"00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd2\",\n"
          + "        \"asset\": \"COP:GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO\",\n"
          + "        \"amount\": \"1000.0000000\",\n"
          + "        \"sponsor\": \"GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO\",\n"
          + "        \"last_modified_ledger\": 117663,\n"
          + "        \"claimants\": [\n"
          + "          {\n"
          + "            \"destination\": \"GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO\",\n"
          + "            \"predicate\": {\n"
          + "              \"or\": [\n"
          + "                {\n"
          + "                  \"abs_before\": \"2020-09-28T17:57:04Z\"\n"
          + "                },\n"
          + "                {\n"
          + "                    \"rel_before\": \"12\"\n"
          + "                }\n"
          + "              ]\n"
          + "            }\n"
          + "          }\n"
          + "        ],\n"
          + "        \"flags\": {\n"
          + "                \"clawback_enabled\": false\n"
          + "        },"
          + "        \"paging_token\": \"117663-00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd2\"\n"
          + "      },\n"
          + "            {\n"
          + "        \"_links\": {\n"
          + "    \"self\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/claimable_balances/00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd3\"\n"
          + "    },\n"
          + "    \"transactions\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/claimable_balances/00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd3/transactions{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"operations\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/claimable_balances/00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd3/operations{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    }\n"
          + "  },\n"
          + "        \"id\": \"00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd3\",\n"
          + "        \"asset\": \"native\",\n"
          + "        \"amount\": \"2000.0000000\",\n"
          + "        \"last_modified_ledger\": 117663,\n"
          + "        \"claimants\": [\n"
          + "          {\n"
          + "            \"destination\": \"GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO\",\n"
          + "            \"predicate\": {\n"
          + "              \"and\": [\n"
          + "                {\n"
          + "                  \"abs_before\": \"2020-09-28T17:57:04Z\"\n"
          + "                },\n"
          + "                {\n"
          + "                  \"not\": {\n"
          + "                    \"unconditional\": true\n"
          + "                  }\n"
          + "                }\n"
          + "              ]\n"
          + "            }\n"
          + "          }\n"
          + "        ],\n"
          + "        \"flags\": {\n"
          + "                \"clawback_enabled\": false\n"
          + "        },"
          + "        \"paging_token\": \"117663-00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd3\"\n"
          + "      }\n"
          + "    ]\n"
          + "  }\n"
          + "}";
}
