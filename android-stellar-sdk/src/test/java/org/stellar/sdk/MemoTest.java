package org.stellar.sdk;

import org.apache.commons.android.codec.DecoderException;
import org.junit.Test;
import org.stellar.sdk.xdr.MemoType;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MemoTest {
    @Test
    public void testMemoNone() {
        MemoNone memo = Memo.none();
        assertEquals(MemoType.MEMO_NONE, memo.toXdr().getDiscriminant());
    }

    @Test
    public void testMemoTextSuccess() {
        MemoText memo = Memo.text("test");
        assertEquals(MemoType.MEMO_TEXT, memo.toXdr().getDiscriminant());
        assertEquals("test", memo.getText());
    }

    @Test
    public void testMemoTextUtf8() {
        MemoText memo = Memo.text("三");
        assertEquals(MemoType.MEMO_TEXT, memo.toXdr().getDiscriminant());
        assertEquals("三", memo.getText());
    }

    @Test
    public void testMemoTextTooLong() {
        try {
            Memo.text("12345678901234567890123456789");
            fail();
        } catch (RuntimeException exception) {
            assertTrue(exception.getMessage().contains("text must be <= 28 bytes."));
        }
    }

    @Test
    public void testMemoTextTooLongUtf8() {
        try {
            Memo.text("价值交易的开源协议!!");
            fail();
        } catch (RuntimeException exception) {
            assertTrue(exception.getMessage().contains("text must be <= 28 bytes."));
        }
    }

    @Test
    public void testMemoId() {
        MemoId memo = Memo.id(9223372036854775807L);
        assertEquals(9223372036854775807L, memo.getId());
        assertEquals(MemoType.MEMO_ID, memo.toXdr().getDiscriminant());
        assertEquals(new Long(9223372036854775807L), memo.toXdr().getId().getUint64());
    }

    @Test
    public void testMemoHashSuccess() throws DecoderException {
        MemoHash memo = Memo.hash("4142434445464748494a4b4c");
        assertEquals(MemoType.MEMO_HASH, memo.toXdr().getDiscriminant());
        String test = "ABCDEFGHIJKL";
        assertEquals(test, Util.paddedByteArrayToString(memo.getBytes()));
        assertEquals("4142434445464748494a4b4c", memo.getTrimmedHexValue());
    }

    @Test
    public void testMemoHashBytesSuccess() {
        byte[] bytes = new byte[10];
        Arrays.fill(bytes, (byte) 'A');
        MemoHash memo = Memo.hash(bytes);
        assertEquals(MemoType.MEMO_HASH, memo.toXdr().getDiscriminant());
        assertEquals("AAAAAAAAAA", Util.paddedByteArrayToString(memo.getBytes()));
        assertEquals("4141414141414141414100000000000000000000000000000000000000000000", memo.getHexValue());
        assertEquals("41414141414141414141", memo.getTrimmedHexValue());
    }

    @Test
    public void testMemoHashTooLong() {
        byte[] longer = new byte[33];
        Arrays.fill(longer, (byte) 0);
        try {
            Memo.hash(longer);
            fail();
        } catch (MemoTooLongException exception) {
            assertTrue(exception.getMessage().contains("MEMO_HASH can contain 32 bytes at max."));
        }
    }

    @Test
    public void testMemoHashInvalidHex() {
        try {
            Memo.hash("test");
            fail();
        } catch (DecoderException e) {

        }
    }

    @Test
    public void testMemoReturnHashSuccess() throws DecoderException {
        MemoReturnHash memo = Memo.returnHash("4142434445464748494a4b4c");
        assertEquals(MemoType.MEMO_RETURN, memo.toXdr().getDiscriminant());
        assertEquals("4142434445464748494a4b4c", memo.getTrimmedHexValue());
    }
}
