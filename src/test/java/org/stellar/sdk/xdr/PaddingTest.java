package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import junit.framework.TestCase;
import org.junit.Test;

public class PaddingTest extends TestCase {
  @Test
  public void testString() throws IOException {
    byte[] bytes = {0, 0, 0, 2, 'a', 'b', 1, 0};

    try {
      String32 xdrObject = String32.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)));
      fail("Didn't throw IOException");
    } catch (IOException expectedException) {
      assertEquals("non-zero padding", expectedException.getMessage());
    }
  }

  @Test
  public void testVarOpaque() throws IOException {
    byte[] bytes = {0, 0, 0, 2, 'a', 'b', 1, 0};

    try {
      DataValue xdrObject =
          DataValue.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)));
      fail("Didn't throw IOException");
    } catch (IOException expectedException) {
      assertEquals("non-zero padding", expectedException.getMessage());
    }
  }
}
