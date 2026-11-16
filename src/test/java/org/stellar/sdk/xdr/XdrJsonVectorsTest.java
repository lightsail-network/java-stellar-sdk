package org.stellar.sdk.xdr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;
import org.junit.Test;

/**
 * Checks the XDR-JSON of every generated type against SEP-51.
 *
 * <p>{@code xdr_json_values.json.gz} holds about 3,900 values, each as XDR and as the XDR-JSON that
 * SEP-51 specifies: values sampled from mainnet and testnet, and a few random values of every type.
 * The JSON was written by the reference implementation, rs-stellar-xdr. {@code xdr_json_names.json}
 * holds the SEP-51 name of every enum member, union arm and struct field, read from the reference
 * implementation's JSON schemas.
 *
 * <p>Both files are copied verbatim from <a
 * href="https://github.com/lightsail-network/py-stellar-xdr/tree/main/tests/vectors">py-stellar-xdr</a>
 * at commit 88e0506e55e976fb379849ba6d24f59330576045, which made them with rs-stellar-xdr at commit
 * 3c2aa43d2ab54d429ae44aa5143d805bf5dba7e2 from the same stellar-xdr commit as this SDK's {@code
 * .x} files (see {@code XDR_COMMIT} in the Makefile). After updating the {@code .x} files, copy
 * them again from a py-stellar-xdr built from the same commit.
 */
public class XdrJsonVectorsTest {
  private static final String PACKAGE = "org.stellar.sdk.xdr.";

  private static JsonArray loadValues() throws IOException {
    try (InputStream raw = resource("xdr_json_values.json.gz");
        Reader reader = new InputStreamReader(new GZIPInputStream(raw), StandardCharsets.UTF_8)) {
      return JsonParser.parseReader(reader).getAsJsonArray();
    }
  }

  private static JsonObject loadNames() throws IOException {
    try (InputStream raw = resource("xdr_json_names.json");
        Reader reader = new InputStreamReader(raw, StandardCharsets.UTF_8)) {
      return JsonParser.parseReader(reader).getAsJsonObject();
    }
  }

  /**
   * The generated classes, keyed by their simple names in lower case.
   *
   * <p>The vectors name types as rs-stellar-xdr does: a nested type by its own name ({@code
   * AccountEntryExt}, which is {@code AccountEntry.AccountEntryExt} here), and some acronyms in
   * other capitals ({@code LedgerKeyTtl} for {@code LedgerKeyTTL}). Lower case is unique in both.
   */
  private static Map<String, Class<?>> classes;

  private static synchronized Class<?> xdrClass(String name) throws Exception {
    if (classes == null) {
      Map<String, Class<?>> found = new LinkedHashMap<>();
      java.net.URL location = Hash.class.getProtectionDomain().getCodeSource().getLocation();
      java.nio.file.Path root =
          java.nio.file.Paths.get(location.toURI()).resolve(PACKAGE.replace('.', '/'));
      try (java.util.stream.Stream<java.nio.file.Path> files = java.nio.file.Files.list(root)) {
        for (java.nio.file.Path file : (Iterable<java.nio.file.Path>) files::iterator) {
          String fileName = file.getFileName().toString();
          if (!fileName.endsWith(".class")) {
            continue;
          }
          String binary = fileName.substring(0, fileName.length() - ".class".length());
          Class<?> cls = Class.forName(PACKAGE + binary, false, Hash.class.getClassLoader());
          if (cls.isAnonymousClass()
              || cls.isSynthetic()
              || !XdrElement.class.isAssignableFrom(cls)) {
            continue;
          }
          Class<?> previous =
              found.put(cls.getSimpleName().toLowerCase(java.util.Locale.ROOT), cls);
          if (previous != null) {
            throw new IllegalStateException(previous + " and " + cls + " differ only in case");
          }
        }
      }
      classes = found;
    }
    // rs-stellar-xdr renames the overlay's `Error` struct, which would clash with Rust's.
    if (name.equals("SError")) {
      name = "Error";
    }
    Class<?> cls = classes.get(name.toLowerCase(java.util.Locale.ROOT));
    if (cls == null) {
      throw new ClassNotFoundException(name);
    }
    return cls;
  }

  private static InputStream resource(String name) {
    InputStream in = XdrJsonVectorsTest.class.getResourceAsStream("/xdr_json/" + name);
    if (in == null) {
      throw new IllegalStateException("missing test resource xdr_json/" + name);
    }
    return in;
  }

  private static Object invoke(Method method, Object target, Object... args) throws Throwable {
    try {
      return method.invoke(target, args);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  @Test
  public void everyTypeHasVectors() throws Exception {
    TreeSet<String> missing = new TreeSet<>();
    for (JsonElement v : loadValues()) {
      String type = v.getAsJsonObject().get("type").getAsString();
      try {
        xdrClass(type);
      } catch (ClassNotFoundException e) {
        missing.add(type);
      }
    }
    assertEquals("vector types without a generated class", new TreeSet<String>(), missing);
  }

  /**
   * For every vector: the XDR decodes to a value whose JSON is the SEP-51 JSON, the SEP-51 JSON
   * reads back as the same value, and that value encodes back to the same XDR.
   */
  @Test
  public void jsonMatchesSep51() throws Exception {
    JsonArray values = loadValues();
    assertTrue("expected thousands of vectors, got " + values.size(), values.size() > 3000);

    Map<String, List<String>> failures = new LinkedHashMap<>();
    int index = 0;
    for (JsonElement element : values) {
      JsonObject vector = element.getAsJsonObject();
      String type = vector.get("type").getAsString();
      String xdr = vector.get("xdr").getAsString();
      JsonElement expected = vector.get("json");
      String where = type + "[" + index++ + "]";

      Class<?> cls = xdrClass(type);
      Method fromXdr = cls.getMethod("fromXdrBase64", String.class);
      Method toXdr = cls.getMethod("toXdrBase64");
      Method toJson = cls.getMethod("toJson");
      Method fromJson = cls.getMethod("fromJson", String.class);

      String problem;
      try {
        Object value = invoke(fromXdr, null, xdr);
        JsonElement actual = JsonParser.parseString((String) invoke(toJson, value));
        if (!actual.equals(expected)) {
          problem = "toJson:\n    expected " + expected + "\n    actual   " + actual;
        } else {
          Object read = invoke(fromJson, null, expected.toString());
          if (!read.equals(value)) {
            problem = "fromJson read a different value";
          } else if (!xdr.equals(invoke(toXdr, read))) {
            problem = "fromJson value encodes to different XDR";
          } else {
            problem = null;
          }
        }
      } catch (Throwable t) {
        problem = t.getClass().getSimpleName() + ": " + t.getMessage();
      }
      if (problem != null) {
        failures.computeIfAbsent(type, k -> new ArrayList<>()).add(where + " " + problem);
      }
    }

    if (!failures.isEmpty()) {
      StringBuilder message = new StringBuilder();
      int total = 0;
      for (List<String> list : failures.values()) {
        total += list.size();
      }
      message
          .append(total)
          .append(" of ")
          .append(values.size())
          .append(" vectors differ from SEP-51, in ")
          .append(failures.size())
          .append(" types:\n");
      for (Map.Entry<String, List<String>> e : failures.entrySet()) {
        message
            .append("  ")
            .append(e.getKey())
            .append(" (")
            .append(e.getValue().size())
            .append("): ")
            .append(e.getValue().get(0))
            .append('\n');
      }
      fail(message.toString());
    }
  }

  /**
   * Every SEP-51 name of an enum member is what the generated enum writes and reads. The vectors
   * hold values, so they can miss members that no sampled value uses; this checks all of them.
   */
  @Test
  public void enumMemberNamesMatchSep51() throws Exception {
    JsonObject names = loadNames();
    List<String> failures = new ArrayList<>();
    for (Map.Entry<String, JsonElement> entry : names.entrySet()) {
      Class<?> cls;
      try {
        cls = xdrClass(entry.getKey());
      } catch (ClassNotFoundException e) {
        failures.add(entry.getKey() + ": no generated class");
        continue;
      }
      if (!cls.isEnum()) {
        continue;
      }
      TreeSet<String> expected = new TreeSet<>();
      for (JsonElement name : entry.getValue().getAsJsonArray()) {
        expected.add(name.getAsString());
      }
      TreeSet<String> actual = new TreeSet<>();
      Method toJson = cls.getMethod("toJson");
      Method fromJson = cls.getMethod("fromJson", String.class);
      for (Object member : cls.getEnumConstants()) {
        String json = (String) toJson.invoke(member);
        String name = JsonParser.parseString(json).getAsString();
        actual.add(name);
        Object read = fromJson.invoke(null, json);
        if (read != member) {
          failures.add(entry.getKey() + ": " + json + " reads back as " + read);
        }
      }
      if (!expected.equals(actual)) {
        TreeSet<String> missing = new TreeSet<>(expected);
        missing.removeAll(actual);
        TreeSet<String> extra = new TreeSet<>(actual);
        extra.removeAll(expected);
        failures.add(entry.getKey() + ": missing " + missing + ", unexpected " + extra);
      }
    }
    if (!failures.isEmpty()) {
      fail(failures.size() + " enums differ from SEP-51:\n  " + String.join("\n  ", failures));
    }
  }
}
