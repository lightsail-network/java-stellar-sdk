package org.stellar.sdk;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TimeBoundsTest {

    @Test(expected = IllegalArgumentException.class)
    public void testMinEqualsMax() {
        new TimeBounds(5000, 5000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinAfterMax() {
        new TimeBounds(5001, 5000);
    }

    @Test
    public void testFromXdr() {
        TimeBounds expected = new TimeBounds(3000, 9000);
        TimeBounds actual = TimeBounds.fromXdr(expected.toXdr());
        assertEquals(expected, actual);
    }

    @Test
    public void testFromXdrWhenNull() {
        assertNull(TimeBounds.fromXdr(null));
    }
}
