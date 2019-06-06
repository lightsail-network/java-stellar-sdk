package org.stellar.sdk.xdr;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaddingTest {
    @Test
    public void testString() throws IOException {
        byte[] bytes = {0, 0, 0, 2, 'a', 'b', 1, 0};

        IOException expectedException = assertThrows(
                IOException.class,
                () -> String32.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)))
        );
        assertEquals("non-zero padding", expectedException.getMessage());
    }

    @Test
    public void testVarOpaque() throws IOException {
        byte[] bytes = {0, 0, 0, 2, 'a', 'b', 1, 0};

        IOException expectedException = assertThrows(
                IOException.class,
                () -> DataValue.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)))
        );
        assertEquals("non-zero padding", expectedException.getMessage());
    }
}
