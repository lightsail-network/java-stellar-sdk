package org.stellar.sdk.xdr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.junit.Test;
import org.stellar.sdk.AbstractTransaction;
import org.stellar.sdk.Base64Factory;
import org.stellar.sdk.Network;

/**
 * {@code fromXdrBase64} and {@code fromXdrByteArray} decode one whole value: bytes left after it
 * are an error, as stellar-core and the other SDKs require (issue #833).
 */
public class TrailingBytesTest {
  // From issue #833: a transaction envelope, and the same envelope followed by de ad be ef.
  private static final String ENVELOPE =
      "AAAAAgAAAAADoQe/884Qvh1w3RjnS8CZZ+TWMJulDV8d3IZkElUxuAAAAGQAAAAAAAAAAgAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAFhAAAAAAAAAQAAAAEAAAAAA6EHv/POEL4dcN0Y50vAmWfk1jCbpQ1fHdyGZBJVMbgAAAABAAAAAAOhB7/zzhC+HXDdGOdLwJln5NYwm6UNXx3chmQSVTG4AAAAAAAAAAAAmJaAAAAAAAAAAAA=";
  private static final String ENVELOPE_WITH_TRAILING_BYTES =
      "AAAAAgAAAAADoQe/884Qvh1w3RjnS8CZZ+TWMJulDV8d3IZkElUxuAAAAGQAAAAAAAAAAgAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAFhAAAAAAAAAQAAAAEAAAAAA6EHv/POEL4dcN0Y50vAmWfk1jCbpQ1fHdyGZBJVMbgAAAABAAAAAAOhB7/zzhC+HXDdGOdLwJln5NYwm6UNXx3chmQSVTG4AAAAAAAAAAAAmJaAAAAAAAAAAADerb7v";

  @Test
  public void memoWithTrailingBytesIsRejected() throws IOException {
    assertEquals(MemoType.MEMO_NONE, Memo.fromXdrBase64("AAAAAA==").getDiscriminant());
    IOException e = assertThrows(IOException.class, () -> Memo.fromXdrBase64("AAAAAN6tvu8="));
    assertEquals("Input has 4 bytes left after the value", e.getMessage());
  }

  @Test
  public void envelopeWithTrailingBytesIsRejected() throws IOException {
    TransactionEnvelope envelope = TransactionEnvelope.fromXdrBase64(ENVELOPE);
    assertEquals(ENVELOPE, envelope.toXdrBase64());
    assertThrows(
        IOException.class, () -> TransactionEnvelope.fromXdrBase64(ENVELOPE_WITH_TRAILING_BYTES));

    AbstractTransaction.fromEnvelopeXdr(ENVELOPE, Network.TESTNET);
    IllegalArgumentException e =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                AbstractTransaction.fromEnvelopeXdr(ENVELOPE_WITH_TRAILING_BYTES, Network.TESTNET));
    assertTrue(e.getCause() instanceof IOException);
  }

  @Test
  public void leafTypesRejectTrailingBytes() throws IOException {
    byte[] uint32 = {0, 0, 0, 7};
    assertEquals(7L, (long) XdrUnsignedInteger.fromXdrByteArray(uint32).getNumber());
    assertThrows(
        IOException.class, () -> XdrUnsignedInteger.fromXdrByteArray(Arrays.copyOf(uint32, 8)));

    byte[] uint64 = {0, 0, 0, 0, 0, 0, 0, 7};
    assertEquals(7L, XdrUnsignedHyperInteger.fromXdrByteArray(uint64).getNumber().longValue());
    assertThrows(
        IOException.class,
        () -> XdrUnsignedHyperInteger.fromXdrByteArray(Arrays.copyOf(uint64, 12)));

    byte[] string = {0, 0, 0, 2, 'h', 'i', 0, 0};
    assertEquals("hi", XdrString.fromXdrByteArray(string).toString());
    assertEquals("hi", XdrString.fromXdrByteArray(string, 2).toString());
    assertThrows(IOException.class, () -> XdrString.fromXdrByteArray(Arrays.copyOf(string, 12)));
    assertThrows(IOException.class, () -> XdrString.fromXdrByteArray(Arrays.copyOf(string, 12), 2));
  }

  /** Trailing bytes that would be valid padding or a whole value are rejected too. */
  @Test
  public void trailingZeroBytesAreRejected() {
    for (int extra = 1; extra <= 8; extra++) {
      byte[] input = new byte[4 + extra]; // MEMO_NONE followed by zero bytes
      assertThrows(IOException.class, () -> Memo.fromXdrByteArray(input));
    }
  }

  /**
   * Reading values one after another from a stream still works: only the byte-array form checks.
   */
  @Test
  public void streamsCanHoldSeveralValues() throws IOException {
    byte[] twoMemos = new byte[8];
    XdrDataInputStream stream = new XdrDataInputStream(new ByteArrayInputStream(twoMemos));
    assertEquals(MemoType.MEMO_NONE, Memo.decode(stream).getDiscriminant());
    assertEquals(MemoType.MEMO_NONE, Memo.decode(stream).getDiscriminant());
  }

  /**
   * Every type: each reference vector decodes, and fails with 1 to 8 bytes appended, whether the
   * bytes are zero or not.
   */
  @Test
  public void everyTypeRejectsTrailingBytes() throws Exception {
    JsonArray values;
    try (InputStream raw = getClass().getResourceAsStream("/xdr_json/xdr_json_values.json.gz");
        Reader reader = new InputStreamReader(new GZIPInputStream(raw), StandardCharsets.UTF_8)) {
      values = JsonParser.parseReader(reader).getAsJsonArray();
    }

    List<String> failures = new ArrayList<>();
    int checked = 0;
    for (JsonElement element : values) {
      JsonObject vector = element.getAsJsonObject();
      String type = vector.get("type").getAsString();
      Method fromXdr =
          XdrJsonVectorsTest.xdrClass(type).getMethod("fromXdrByteArray", byte[].class);
      byte[] xdr = Base64Factory.getInstance().decode(vector.get("xdr").getAsString());
      try {
        fromXdr.invoke(null, (Object) xdr);
      } catch (InvocationTargetException e) {
        failures.add(type + ": the vector itself fails: " + e.getCause());
        continue;
      }
      for (int extra = 1; extra <= 8; extra++) {
        for (byte fill : new byte[] {0, (byte) 0xde}) {
          byte[] input = Arrays.copyOf(xdr, xdr.length + extra);
          Arrays.fill(input, xdr.length, input.length, fill);
          try {
            fromXdr.invoke(null, (Object) input);
            failures.add(type + ": accepted " + extra + " trailing bytes of " + fill);
          } catch (InvocationTargetException e) {
            if (!(e.getCause() instanceof IOException)) {
              failures.add(type + ": " + extra + " trailing bytes threw " + e.getCause());
            }
          }
          checked++;
        }
      }
    }
    if (!failures.isEmpty()) {
      fail(
          failures.size()
              + " failures:\n  "
              + String.join("\n  ", failures.subList(0, Math.min(40, failures.size()))));
    }
    assertTrue("checked " + checked, checked > 50_000);
  }
}
