package org.stellar.sdk.xdr;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Manual corpus verifier for generated XDR JSON.
 *
 * <p>This class is intentionally opt-in: it is compiled with the normal test sources, but it is not
 * executed by the default `test` or `check` tasks. Run it explicitly through the dedicated Gradle
 * task when you want to validate generated JSON against a corpus and the `stellar` CLI.
 *
 * <p>Examples:
 *
 * <pre>
 * ./gradlew verifyXdrJson --args='/path/to/transactions.csv --limit 1000'
 * ./gradlew verifyXdrJson --args='/path/to/transactions.csv --type transaction-envelope --verbose'
 * ./gradlew verifyXdrJson --args='/path/to/transactions.csv --stellar /path/to/stellar'
 * </pre>
 */
public final class XdrJsonCorpusVerifier {
  private static final int MAX_SAMPLES = 10;

  private XdrJsonCorpusVerifier() {}

  public static void main(String[] args) throws Exception {
    if (args.length == 1 && ("--help".equals(args[0]) || "-h".equals(args[0]))) {
      System.out.println(Options.usage());
      return;
    }

    try {
      Options options = Options.parse(args);
      Stats stats = processCsv(options);
      printReport(stats, options);

      if (stats.hasFailures()) {
        System.exit(1);
      }
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
      System.exit(2);
    }
  }

  private static Stats processCsv(Options options) throws Exception {
    Stats stats = new Stats();
    long startedAt = System.nanoTime();

    try (CsvRowReader csv =
        new CsvRowReader(Files.newBufferedReader(options.csvPath, StandardCharsets.UTF_8))) {
      List<String> headers = csv.nextRow();
      if (headers == null || headers.isEmpty()) {
        throw new IllegalArgumentException("CSV file is empty: " + options.csvPath);
      }
      int columnIndex = headers.indexOf(options.column);
      if (columnIndex < 0) {
        throw new IllegalArgumentException(
            "Column '"
                + options.column
                + "' not found. Available columns: "
                + String.join(", ", headers));
      }

      List<String> row;
      int rowNumber = 1;
      while ((row = csv.nextRow()) != null && stats.total < options.limit) {
        rowNumber++;
        String xdr = columnIndex < row.size() ? row.get(columnIndex).trim() : "";
        if (xdr.isEmpty()) {
          continue;
        }

        stats.total++;

        DecodedValue javaDecoded = decodeWithJava(options.codecs, xdr);
        if (javaDecoded == null) {
          stats.javaErrors++;
          stats.recordJavaError(rowNumber, "Java decode failed for all configured codecs");
          if (options.verbose) {
            System.err.println("[row " + rowNumber + "] ERROR: Java decode failed");
          }
          continue;
        }

        DecodedValue cliDecoded = decodeWithCli(options.codecs, options.stellarCommand, xdr);
        if (cliDecoded == null) {
          stats.cliErrors++;
          stats.recordCliError(rowNumber, "stellar CLI decode failed for all configured codecs");
          if (options.verbose) {
            System.err.println("[row " + rowNumber + "] WARN: stellar CLI decode failed");
          }
          continue;
        }

        if (cliDecoded.codec != javaDecoded.codec) {
          List<String> diffs =
              Collections.singletonList(
                  "$: decoded as "
                      + cliDecoded.codec.cliType
                      + " by CLI but "
                      + javaDecoded.codec.cliType
                      + " by Java");
          stats.recordMismatch(rowNumber, diffs);
          if (options.verbose) {
            System.out.println("[row " + rowNumber + "] JSON MISMATCH (codec mismatch)");
          }
        } else {
          List<String> diffs =
              deepCompare(stripSchemaKeys(cliDecoded.json), stripSchemaKeys(javaDecoded.json), "$");
          if (diffs.isEmpty()) {
            stats.jsonMatched++;
          } else {
            stats.recordMismatch(rowNumber, diffs);
            if (options.verbose) {
              System.out.println(
                  "[row " + rowNumber + "] JSON MISMATCH (" + diffs.size() + " diff(s))");
            }
          }
        }

        String roundTripError = verifyRoundTrip(javaDecoded.codec, javaDecoded.jsonText, xdr);
        if (roundTripError == null) {
          stats.roundTripOk++;
        } else {
          stats.recordRoundTripFailure(rowNumber, roundTripError);
          if (options.verbose) {
            System.out.println("[row " + rowNumber + "] ROUND-TRIP FAIL: " + roundTripError);
          }
        }

        if (options.progressInterval > 0 && stats.total % options.progressInterval == 0) {
          double elapsedSeconds = nanosToSeconds(System.nanoTime() - startedAt);
          double rate = elapsedSeconds > 0 ? stats.total / elapsedSeconds : 0.0d;
          System.err.printf("  ... %d processed (%.0f tx/s)%n", stats.total, rate);
        }
      }
    } catch (CliUnavailableException e) {
      throw new IllegalStateException(
          "Unable to execute stellar CLI command '"
              + options.stellarCommand
              + "'. Install the stellar CLI or pass --stellar with the correct path.",
          e);
    }

    stats.elapsedSeconds = nanosToSeconds(System.nanoTime() - startedAt);
    return stats;
  }

  private static DecodedValue decodeWithJava(List<EnvelopeCodec> codecs, String xdr) {
    for (EnvelopeCodec codec : codecs) {
      try {
        XdrElement value = codec.decodeXdr(xdr);
        String jsonText = value.toJson();
        return new DecodedValue(codec, jsonText, JsonParser.parseString(jsonText));
      } catch (Exception e) {
        // Try the next codec.
      }
    }
    return null;
  }

  private static DecodedValue decodeWithCli(
      List<EnvelopeCodec> codecs, String stellarCommand, String xdr)
      throws CliUnavailableException {
    for (EnvelopeCodec codec : codecs) {
      try {
        ProcessResult result =
            runProcess(
                Arrays.asList(stellarCommand, "xdr", "decode", "--type", codec.cliType, xdr));
        if (result.exitCode == 0) {
          return new DecodedValue(codec, result.stdout, JsonParser.parseString(result.stdout));
        }
      } catch (IOException e) {
        throw new CliUnavailableException(e);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new IllegalStateException("Interrupted while executing stellar CLI", e);
      }
    }
    return null;
  }

  private static String verifyRoundTrip(EnvelopeCodec codec, String jsonText, String originalXdr) {
    try {
      XdrElement reparsed = codec.decodeJson(jsonText);
      String roundTripped = reparsed.toXdrBase64();
      if (!originalXdr.equals(roundTripped)) {
        return "XDR mismatch after JSON round-trip";
      }
      return null;
    } catch (Exception e) {
      String message = e.getMessage();
      if (message == null || message.isEmpty()) {
        message = e.getClass().getSimpleName();
      }
      return message;
    }
  }

  private static void printReport(Stats stats, Options options) {
    String divider = repeat("=", 60);
    String section = repeat("-", 60);

    System.out.println();
    System.out.println(divider);
    System.out.printf("  Total rows checked:   %8d%n", stats.total);
    System.out.printf("  Java decode errors:   %8d%n", stats.javaErrors);
    System.out.printf("  CLI decode errors:    %8d%n", stats.cliErrors);
    System.out.println(section);
    System.out.printf("  JSON matched:         %8d%n", stats.jsonMatched);
    System.out.printf("  JSON mismatched:      %8d%n", stats.jsonMismatched);
    System.out.println(section);
    System.out.printf("  Round-trip OK:        %8d%n", stats.roundTripOk);
    System.out.printf("  Round-trip FAIL:      %8d%n", stats.roundTripFail);
    System.out.println(section);
    System.out.printf("  Elapsed:              %7.1fs%n", stats.elapsedSeconds);
    System.out.println(divider);

    if (!stats.cliErrorSamples.isEmpty()) {
      System.out.println();
      System.out.println("First " + stats.cliErrorSamples.size() + " CLI decode failure(s):");
      for (String sample : stats.cliErrorSamples) {
        System.out.println(sample);
      }
    }

    if (!stats.javaErrorSamples.isEmpty()) {
      System.out.println();
      System.out.println("First " + stats.javaErrorSamples.size() + " Java decode failure(s):");
      for (String sample : stats.javaErrorSamples) {
        System.out.println(sample);
      }
    }

    if (!stats.mismatchSamples.isEmpty()) {
      System.out.println();
      System.out.println("First " + stats.mismatchSamples.size() + " JSON mismatch(es):");
      for (String sample : stats.mismatchSamples) {
        System.out.println(sample);
        System.out.println();
      }
    }

    if (!stats.roundTripSamples.isEmpty()) {
      System.out.println();
      System.out.println("First " + stats.roundTripSamples.size() + " round-trip failure(s):");
      for (String sample : stats.roundTripSamples) {
        System.out.println(sample);
      }
    }
  }

  private static List<String> deepCompare(JsonElement left, JsonElement right, String path) {
    List<String> diffs = new ArrayList<>();
    if (left == null) {
      left = JsonNull.INSTANCE;
    }
    if (right == null) {
      right = JsonNull.INSTANCE;
    }

    if (left.isJsonObject() && right.isJsonObject()) {
      JsonObject leftObject = left.getAsJsonObject();
      JsonObject rightObject = right.getAsJsonObject();
      Set<String> keys = new TreeSet<>();
      keys.addAll(leftObject.keySet());
      keys.addAll(rightObject.keySet());
      for (String key : keys) {
        String childPath = path + "." + key;
        if (!leftObject.has(key)) {
          diffs.add(childPath + ": missing in CLI output");
        } else if (!rightObject.has(key)) {
          diffs.add(childPath + ": missing in Java output");
        } else {
          diffs.addAll(deepCompare(leftObject.get(key), rightObject.get(key), childPath));
        }
      }
      return diffs;
    }

    if (left.isJsonArray() && right.isJsonArray()) {
      JsonArray leftArray = left.getAsJsonArray();
      JsonArray rightArray = right.getAsJsonArray();
      if (leftArray.size() != rightArray.size()) {
        diffs.add(
            path
                + ": array length mismatch - cli="
                + leftArray.size()
                + ", java="
                + rightArray.size());
      }
      int count = Math.min(leftArray.size(), rightArray.size());
      for (int i = 0; i < count; i++) {
        diffs.addAll(deepCompare(leftArray.get(i), rightArray.get(i), path + "[" + i + "]"));
      }
      return diffs;
    }

    if (left.isJsonNull() && right.isJsonNull()) {
      return diffs;
    }

    if (left.isJsonPrimitive() && right.isJsonPrimitive()) {
      JsonPrimitive leftPrimitive = left.getAsJsonPrimitive();
      JsonPrimitive rightPrimitive = right.getAsJsonPrimitive();

      if (leftPrimitive.isNumber() && rightPrimitive.isNumber()) {
        if (leftPrimitive.getAsBigDecimal().compareTo(rightPrimitive.getAsBigDecimal()) != 0) {
          diffs.add(path + ": " + left + " (cli) != " + right + " (java)");
        }
        return diffs;
      }

      if (leftPrimitive.isBoolean() && rightPrimitive.isBoolean()) {
        if (leftPrimitive.getAsBoolean() != rightPrimitive.getAsBoolean()) {
          diffs.add(path + ": " + left + " (cli) != " + right + " (java)");
        }
        return diffs;
      }

      if (leftPrimitive.isString() && rightPrimitive.isString()) {
        if (!leftPrimitive.getAsString().equals(rightPrimitive.getAsString())) {
          diffs.add(path + ": " + left + " (cli) != " + right + " (java)");
        }
        return diffs;
      }
    }

    if (!left.equals(right)) {
      diffs.add(
          path
              + ": type/value mismatch - cli="
              + describeJson(left)
              + ", java="
              + describeJson(right));
    }
    return diffs;
  }

  private static JsonElement stripSchemaKeys(JsonElement json) {
    if (json == null || json.isJsonNull()) {
      return JsonNull.INSTANCE;
    }
    if (json.isJsonArray()) {
      JsonArray array = new JsonArray();
      for (JsonElement element : json.getAsJsonArray()) {
        array.add(stripSchemaKeys(element));
      }
      return array;
    }
    if (json.isJsonObject()) {
      JsonObject object = new JsonObject();
      for (String key : json.getAsJsonObject().keySet()) {
        if ("$schema".equals(key)) {
          continue;
        }
        object.add(key, stripSchemaKeys(json.getAsJsonObject().get(key)));
      }
      return object;
    }
    return json.deepCopy();
  }

  private static String describeJson(JsonElement json) {
    if (json == null || json.isJsonNull()) {
      return "null";
    }
    if (json.isJsonObject()) {
      return "object" + json;
    }
    if (json.isJsonArray()) {
      return "array" + json;
    }
    JsonPrimitive primitive = json.getAsJsonPrimitive();
    if (primitive.isString()) {
      return "string(" + primitive.getAsString() + ")";
    }
    if (primitive.isBoolean()) {
      return "boolean(" + primitive.getAsBoolean() + ")";
    }
    if (primitive.isNumber()) {
      return "number(" + primitive.getAsBigDecimal().toPlainString() + ")";
    }
    return json.toString();
  }

  private static ProcessResult runProcess(List<String> command)
      throws IOException, InterruptedException {
    Process process = new ProcessBuilder(command).start();
    String stdout = readFully(process.getInputStream());
    String stderr = readFully(process.getErrorStream());
    int exitCode = process.waitFor();
    return new ProcessResult(exitCode, stdout.trim(), stderr.trim());
  }

  private static String readFully(InputStream inputStream) throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    byte[] buffer = new byte[4096];
    int read;
    while ((read = inputStream.read(buffer)) != -1) {
      output.write(buffer, 0, read);
    }
    return new String(output.toByteArray(), StandardCharsets.UTF_8);
  }

  private static String repeat(String value, int count) {
    StringBuilder builder = new StringBuilder(value.length() * count);
    for (int i = 0; i < count; i++) {
      builder.append(value);
    }
    return builder.toString();
  }

  private static double nanosToSeconds(long nanos) {
    return nanos / 1_000_000_000.0d;
  }

  private interface XdrDecoder {
    XdrElement decode(String value) throws Exception;
  }

  private enum EnvelopeCodec {
    TRANSACTION_ENVELOPE(
        "TransactionEnvelope", TransactionEnvelope::fromXdrBase64, TransactionEnvelope::fromJson),
    FEE_BUMP_TRANSACTION_ENVELOPE(
        "FeeBumpTransactionEnvelope",
        FeeBumpTransactionEnvelope::fromXdrBase64,
        FeeBumpTransactionEnvelope::fromJson);

    private final String cliType;
    private final XdrDecoder xdrDecoder;
    private final XdrDecoder jsonDecoder;

    EnvelopeCodec(String cliType, XdrDecoder xdrDecoder, XdrDecoder jsonDecoder) {
      this.cliType = cliType;
      this.xdrDecoder = xdrDecoder;
      this.jsonDecoder = jsonDecoder;
    }

    XdrElement decodeXdr(String value) throws Exception {
      return xdrDecoder.decode(value);
    }

    XdrElement decodeJson(String value) throws Exception {
      return jsonDecoder.decode(value);
    }

    static List<EnvelopeCodec> fromOption(String type) {
      if ("auto".equals(type)) {
        return Arrays.asList(values());
      }
      if ("transaction-envelope".equals(type)) {
        return Collections.singletonList(TRANSACTION_ENVELOPE);
      }
      if ("fee-bump-transaction-envelope".equals(type)) {
        return Collections.singletonList(FEE_BUMP_TRANSACTION_ENVELOPE);
      }
      throw new IllegalArgumentException(
          "Unsupported --type value '"
              + type
              + "'. Expected one of: auto, transaction-envelope, fee-bump-transaction-envelope");
    }
  }

  private static final class Options {
    private final Path csvPath;
    private final String column;
    private final long limit;
    private final boolean verbose;
    private final int progressInterval;
    private final String stellarCommand;
    private final List<EnvelopeCodec> codecs;

    private Options(
        Path csvPath,
        String column,
        long limit,
        boolean verbose,
        int progressInterval,
        String stellarCommand,
        List<EnvelopeCodec> codecs) {
      this.csvPath = csvPath;
      this.column = column;
      this.limit = limit;
      this.verbose = verbose;
      this.progressInterval = progressInterval;
      this.stellarCommand = stellarCommand;
      this.codecs = codecs;
    }

    static Options parse(String[] args) {
      if (args.length == 0) {
        throw new IllegalArgumentException(usage());
      }

      Path csvPath = null;
      String column = "tx_envelope";
      long limit = Long.MAX_VALUE;
      boolean verbose = false;
      int progressInterval = 1000;
      String stellarCommand = "stellar";
      String type = "auto";

      for (int i = 0; i < args.length; i++) {
        String arg = args[i];
        if ("--help".equals(arg) || "-h".equals(arg)) {
          throw new IllegalArgumentException(usage());
        }
        if (arg.startsWith("--")) {
          String value;
          if (arg.contains("=")) {
            String[] parts = arg.split("=", 2);
            arg = parts[0];
            value = parts[1];
          } else if ("--verbose".equals(arg)) {
            value = null;
          } else {
            if (i + 1 >= args.length) {
              throw new IllegalArgumentException("Missing value for " + arg);
            }
            value = args[++i];
          }

          if ("--column".equals(arg)) {
            column = value;
          } else if ("--limit".equals(arg)) {
            limit = Long.parseLong(value);
          } else if ("--progress-interval".equals(arg)) {
            progressInterval = Integer.parseInt(value);
          } else if ("--verbose".equals(arg)) {
            verbose = true;
          } else if ("--stellar".equals(arg)) {
            stellarCommand = value;
          } else if ("--type".equals(arg)) {
            type = value;
          } else {
            throw new IllegalArgumentException("Unknown option: " + arg);
          }
        } else if (csvPath == null) {
          csvPath = Paths.get(arg);
        } else {
          throw new IllegalArgumentException("Unexpected positional argument: " + arg);
        }
      }

      if (csvPath == null) {
        throw new IllegalArgumentException("CSV path is required.\n\n" + usage());
      }
      if (limit <= 0) {
        throw new IllegalArgumentException("--limit must be greater than 0");
      }
      if (progressInterval < 0) {
        throw new IllegalArgumentException("--progress-interval must be 0 or greater");
      }

      return new Options(
          csvPath,
          column,
          limit,
          verbose,
          progressInterval,
          stellarCommand,
          EnvelopeCodec.fromOption(type));
    }

    static String usage() {
      return "Usage: ./gradlew verifyXdrJson --args='transactions.csv [options]'\n"
          + "Options:\n"
          + "  --column <name>              CSV column containing base64 XDR (default: tx_envelope)\n"
          + "  --type <value>               auto | transaction-envelope | fee-bump-transaction-envelope\n"
          + "  --limit <n>                  Maximum non-empty rows to check (default: unlimited)\n"
          + "  --progress-interval <n>      Print progress every n rows, 0 disables it (default: 1000)\n"
          + "  --stellar <command>          stellar CLI command name/path (default: stellar)\n"
          + "  --verbose                    Print per-row failures as they happen\n";
    }
  }

  private static final class DecodedValue {
    private final EnvelopeCodec codec;
    private final String jsonText;
    private final JsonElement json;

    private DecodedValue(EnvelopeCodec codec, String jsonText, JsonElement json) {
      this.codec = codec;
      this.jsonText = jsonText;
      this.json = json;
    }
  }

  private static final class ProcessResult {
    private final int exitCode;
    private final String stdout;

    @SuppressWarnings("unused")
    private final String stderr;

    private ProcessResult(int exitCode, String stdout, String stderr) {
      this.exitCode = exitCode;
      this.stdout = stdout;
      this.stderr = stderr;
    }
  }

  private static final class Stats {
    private long total;
    private int cliErrors;
    private int javaErrors;
    private int jsonMatched;
    private int jsonMismatched;
    private int roundTripOk;
    private int roundTripFail;
    private double elapsedSeconds;
    private final List<String> cliErrorSamples = new ArrayList<>();
    private final List<String> javaErrorSamples = new ArrayList<>();
    private final List<String> mismatchSamples = new ArrayList<>();
    private final List<String> roundTripSamples = new ArrayList<>();

    private void recordCliError(int rowNumber, String message) {
      if (cliErrorSamples.size() < MAX_SAMPLES) {
        cliErrorSamples.add("[row " + rowNumber + "] " + message);
      }
    }

    private void recordJavaError(int rowNumber, String message) {
      if (javaErrorSamples.size() < MAX_SAMPLES) {
        javaErrorSamples.add("[row " + rowNumber + "] " + message);
      }
    }

    private void recordMismatch(int rowNumber, List<String> diffs) {
      jsonMismatched++;
      if (mismatchSamples.size() < MAX_SAMPLES) {
        StringBuilder builder = new StringBuilder();
        builder.append("[row ").append(rowNumber).append("]\n");
        int limit = Math.min(diffs.size(), 5);
        for (int i = 0; i < limit; i++) {
          builder.append(diffs.get(i));
          if (i + 1 < limit) {
            builder.append('\n');
          }
        }
        mismatchSamples.add(builder.toString());
      }
    }

    private void recordRoundTripFailure(int rowNumber, String reason) {
      roundTripFail++;
      if (roundTripSamples.size() < MAX_SAMPLES) {
        roundTripSamples.add("[row " + rowNumber + "] " + reason);
      }
    }

    private boolean hasFailures() {
      return cliErrors > 0 || javaErrors > 0 || jsonMismatched > 0 || roundTripFail > 0;
    }
  }

  private static final class CliUnavailableException extends Exception {
    private CliUnavailableException(Throwable cause) {
      super(cause);
    }
  }

  private static final class CsvRowReader implements Closeable {
    private final BufferedReader reader;
    private boolean reachedEof;
    private boolean firstRow = true;

    private CsvRowReader(Reader reader) {
      this.reader = new BufferedReader(reader);
    }

    List<String> nextRow() throws IOException {
      if (reachedEof) {
        return null;
      }

      List<String> row = new ArrayList<>();
      StringBuilder current = new StringBuilder();
      boolean inQuotes = false;
      boolean sawAnyCharacter = false;

      while (true) {
        int read = reader.read();
        if (read == -1) {
          reachedEof = true;
          if (!sawAnyCharacter && current.length() == 0 && row.isEmpty()) {
            return null;
          }
          row.add(current.toString());
          sanitizeFirstRow(row);
          return row;
        }

        sawAnyCharacter = true;
        char ch = (char) read;

        if (inQuotes) {
          if (ch == '"') {
            reader.mark(1);
            int next = reader.read();
            if (next == '"') {
              current.append('"');
            } else {
              inQuotes = false;
              if (next != -1) {
                reader.reset();
              }
            }
          } else {
            current.append(ch);
          }
          continue;
        }

        if (ch == '"') {
          inQuotes = true;
          continue;
        }

        if (ch == ',') {
          row.add(current.toString());
          current.setLength(0);
          continue;
        }

        if (ch == '\n') {
          row.add(current.toString());
          sanitizeFirstRow(row);
          return row;
        }

        if (ch == '\r') {
          reader.mark(1);
          int next = reader.read();
          if (next != '\n' && next != -1) {
            reader.reset();
          }
          row.add(current.toString());
          sanitizeFirstRow(row);
          return row;
        }

        current.append(ch);
      }
    }

    private void sanitizeFirstRow(List<String> row) {
      if (firstRow && !row.isEmpty() && !row.get(0).isEmpty() && row.get(0).charAt(0) == '\uFEFF') {
        row.set(0, row.get(0).substring(1));
      }
      firstRow = false;
    }

    @Override
    public void close() throws IOException {
      reader.close();
    }
  }
}
