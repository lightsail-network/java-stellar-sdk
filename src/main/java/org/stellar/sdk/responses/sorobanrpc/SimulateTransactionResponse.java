package org.stellar.sdk.responses.sorobanrpc;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Value;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.DiagnosticEvent;
import org.stellar.sdk.xdr.LedgerEntry;
import org.stellar.sdk.xdr.LedgerKey;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SorobanAuthorizationEntry;
import org.stellar.sdk.xdr.SorobanTransactionData;

/**
 * Response for JSON-RPC method simulateTransaction.
 *
 * <p>Note - The simulation response will have different model representations with different
 * members present or absent depending on type of response that it is conveying. For example, the
 * simulation response for invoke host function, could be one of three types: error, success, or
 * restore operation needed.
 *
 * <p>Please refer to the latest <a
 * href="https://soroban.stellar.org/api/methods/simulateTransaction#returns"
 * target="_blank">Soroban simulateTransaction documentation</a> for details on which members of the
 * simulation response model are keyed to each type of response.
 */
@Value
public class SimulateTransactionResponse {
  String error;

  /** The field can be parsed as {@link org.stellar.sdk.xdr.SorobanTransactionData} object. */
  String transactionData;

  /** The elements inside can be parsed as {@link org.stellar.sdk.xdr.DiagnosticEvent} objects. */
  List<String> events;

  Long minResourceFee;

  // An array of the individual host function call results.
  // This will only contain a single element if present, because only a single
  // invokeHostFunctionOperation
  // is supported per transaction.
  List<SimulateHostFunctionResult> results;

  SimulateTransactionCost cost;

  RestorePreamble restorePreamble;

  List<LedgerEntryChange> stateChanges;

  Long latestLedger;

  public SorobanTransactionData parseTransactionData() {
    return Util.parseXdr(transactionData, SorobanTransactionData::fromXdrBase64);
  }

  public List<DiagnosticEvent> parseEvents() {
    if (events == null) {
      return null;
    }
    return events.stream()
        .map(event -> Util.parseXdr(event, DiagnosticEvent::fromXdrBase64))
        .collect(Collectors.toList());
  }

  @Value
  public static class SimulateHostFunctionResult {

    /**
     * The elements inside can be parsed as {@link org.stellar.sdk.xdr.SorobanAuthorizationEntry}
     * objects.
     */
    List<String> auth;

    /** The field can be parsed as {@link org.stellar.sdk.xdr.SCVal} object. */
    String xdr;

    /**
     * Parses the {@code auth} field from a list of strings to a list of {@link
     * org.stellar.sdk.xdr.SorobanAuthorizationEntry} objects.
     *
     * @return a list of parsed {@link org.stellar.sdk.xdr.SorobanAuthorizationEntry} objects
     */
    public List<SorobanAuthorizationEntry> parseAuth() {
      if (auth == null) {
        return null;
      }
      return auth.stream()
          .map(entry -> Util.parseXdr(entry, SorobanAuthorizationEntry::fromXdrBase64))
          .collect(Collectors.toList());
    }

    /**
     * Parses the {@code xdr} field from a string to an {@link org.stellar.sdk.xdr.SCVal} object.
     *
     * @return the parsed {@link org.stellar.sdk.xdr.SCVal} object
     */
    public SCVal parseXdr() {
      return Util.parseXdr(xdr, SCVal::fromXdrBase64);
    }
  }

  @Value
  public static class SimulateTransactionCost {
    @SerializedName("cpuInsns")
    BigInteger cpuInstructions;

    @SerializedName("memBytes")
    BigInteger memoryBytes;
  }

  @Value
  public static class RestorePreamble {
    /** The field can be parsed as {@link org.stellar.sdk.xdr.SorobanTransactionData} object. */
    String transactionData;

    Long minResourceFee;

    /**
     * Parses the {@code transactionData} field from a string to an {@link
     * org.stellar.sdk.xdr.SorobanTransactionData} object.
     *
     * @return the parsed {@link org.stellar.sdk.xdr.SorobanTransactionData} object
     */
    public SorobanTransactionData parseTransactionData() {
      return Util.parseXdr(transactionData, SorobanTransactionData::fromXdrBase64);
    }
  }

  /**
   * LedgerEntryChange designates a change in a ledger entry. Before and After cannot be omitted at
   * the same time. If Before is omitted, it constitutes a creation, if After is omitted, it
   * constitutes a deletion.
   */
  @Value
  public static class LedgerEntryChange {
    String type;
    String key;
    String before;
    String after;

    /**
     * Parses the {@code key} field from a string to an {@link org.stellar.sdk.xdr.LedgerKey}
     * object.
     *
     * @return the parsed {@link org.stellar.sdk.xdr.LedgerKey} object
     */
    public LedgerKey parseKey() {
      return Util.parseXdr(key, LedgerKey::fromXdrBase64);
    }

    /**
     * Parses the {@code before} field from a string to an {@link org.stellar.sdk.xdr.LedgerEntry}
     * object.
     *
     * @return the parsed {@link org.stellar.sdk.xdr.LedgerEntry} object
     */
    public LedgerEntry parseBefore() {
      return Util.parseXdr(before, LedgerEntry::fromXdrBase64);
    }

    /**
     * Parses the {@code after} field from a string to an {@link org.stellar.sdk.xdr.LedgerEntry}
     * object.
     *
     * @return the parsed {@link org.stellar.sdk.xdr.LedgerEntry} object
     */
    public LedgerEntry parseAfter() {
      return Util.parseXdr(after, LedgerEntry::fromXdrBase64);
    }
  }
}
