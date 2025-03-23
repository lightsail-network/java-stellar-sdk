package org.stellar.sdk;

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class TOIDTest {

  private static final long ledgerFirst = 1L << 32;
  private static final long txFirst = 1L << 12;
  private static final long opFirst = 1L;

  @Test
  public void testToInt64AndFromInt64() {
    List<TOID> toids =
        Arrays.asList(
            new TOID(0, 0, 1),
            new TOID(0, 0, 4095),
            new TOID(0, 1, 0),
            new TOID(0, 1048575, 0),
            new TOID(1, 0, 0),
            new TOID(2_147_483_647, 0, 0),
            new TOID(1, 1, 1),
            new TOID(1, 1, 0),
            new TOID(1, 0, 1),
            new TOID(1, 0, 0),
            new TOID(0, 1, 0),
            new TOID(0, 0, 1),
            new TOID(0, 0, 0),
            new TOID(2_147_483_647, 1_048_575, 4_095));
    List<Long> ids =
        Arrays.asList(
            1L,
            4095L,
            4096L,
            4294963200L,
            4294967296L,
            9223372032559808512L,
            ledgerFirst + txFirst + opFirst,
            ledgerFirst + txFirst,
            ledgerFirst + opFirst,
            ledgerFirst,
            txFirst,
            opFirst,
            0L,
            9223372036854775807L);
    for (int i = 0; i < toids.size(); i++) {
      TOID toid = toids.get(i);
      long id = ids.get(i);
      Assert.assertEquals(id, toid.toInt64());
      Assert.assertEquals(toid, TOID.fromInt64(id));
    }
  }

  @Test
  public void testLedgerRangeInclusive() {
    Assert.assertEquals(
        new TOID.TOIDRange(new TOID(0, 0, 0).toInt64(), new TOID(2, 0, 0).toInt64()),
        TOID.ledgerRangeInclusive(1, 1));
    Assert.assertEquals(
        new TOID.TOIDRange(new TOID(0, 0, 0).toInt64(), new TOID(3, 0, 0).toInt64()),
        TOID.ledgerRangeInclusive(1, 2));
    Assert.assertEquals(
        new TOID.TOIDRange(new TOID(2, 0, 0).toInt64(), new TOID(3, 0, 0).toInt64()),
        TOID.ledgerRangeInclusive(2, 2));
    Assert.assertEquals(
        new TOID.TOIDRange(new TOID(2, 0, 0).toInt64(), new TOID(4, 0, 0).toInt64()),
        TOID.ledgerRangeInclusive(2, 3));
  }

  @Test
  public void testIncrementOperationIndex() {
    TOID toid = new TOID(0, 0, 0);
    toid.incrementOperationIndex();
    Assert.assertEquals(new TOID(0, 0, 1), toid);
    toid.incrementOperationIndex();
    Assert.assertEquals(new TOID(0, 0, 2), toid);
    toid.incrementOperationIndex();
    Assert.assertEquals(new TOID(0, 0, 3), toid);

    toid = new TOID(0, 0, 0xFFF);
    toid.incrementOperationIndex();
    Assert.assertEquals(new TOID(1, 0, 0), toid);
    toid.incrementOperationIndex();
    Assert.assertEquals(new TOID(1, 0, 1), toid);
  }

  @Test
  public void testAfterLedger() {
    Assert.assertEquals(4294967295L, TOID.afterLedger(0).toInt64());
    Assert.assertEquals(8589934591L, TOID.afterLedger(1).toInt64());
    Assert.assertEquals(433791696895L, TOID.afterLedger(100).toInt64());
  }

  @Test
  public void initWithInvalidParamsThrows() {
    Assert.assertThrows(IllegalArgumentException.class, () -> new TOID(-1, 0, 0));
    Assert.assertThrows(IllegalArgumentException.class, () -> new TOID(0, -1, 0));
    Assert.assertThrows(IllegalArgumentException.class, () -> new TOID(0, 0, -1));
    Assert.assertThrows(IllegalArgumentException.class, () -> new TOID(0, 0xFFFFF + 1, 0));
    Assert.assertThrows(IllegalArgumentException.class, () -> new TOID(0, 0, 0xFFF + 1));
  }

  @Test
  public void fromInt64WithInvalidParamsThrows() {
    Assert.assertThrows(IllegalArgumentException.class, () -> TOID.fromInt64(-1));
  }

  @Test
  public void testLedgerRangeInclusiveWithInvalidParamsThrows() {
    Assert.assertThrows(IllegalArgumentException.class, () -> TOID.ledgerRangeInclusive(2, 1));
    Assert.assertThrows(IllegalArgumentException.class, () -> TOID.ledgerRangeInclusive(0, 1));
    Assert.assertThrows(IllegalArgumentException.class, () -> TOID.ledgerRangeInclusive(-1, 100));
  }
}
