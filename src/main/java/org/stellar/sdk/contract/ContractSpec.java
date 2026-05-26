package org.stellar.sdk.contract;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.stellar.sdk.contract.exception.InvalidWasmException;
import org.stellar.sdk.xdr.SCSpecEntry;
import org.stellar.sdk.xdr.SCSpecEntryKind;
import org.stellar.sdk.xdr.SCSpecEventV0;
import org.stellar.sdk.xdr.SCSpecFunctionV0;
import org.stellar.sdk.xdr.SCSpecUDTEnumV0;
import org.stellar.sdk.xdr.SCSpecUDTErrorEnumV0;
import org.stellar.sdk.xdr.SCSpecUDTStructV0;
import org.stellar.sdk.xdr.SCSpecUDTUnionV0;
import org.stellar.sdk.xdr.SCSymbol;
import org.stellar.sdk.xdr.XdrString;

/**
 * Represents a SEP-0048 contract interface specification.
 *
 * <p>Entries are stored in module order and exposed as an unmodifiable list. The classified views
 * ({@link #getFunctions()}, {@link #getEvents()}, etc.) are computed once at construction.
 * Construct with raw {@link SCSpecEntry} values, decode from contract Wasm bytes via {@link
 * #fromWasm(byte[])}, or decode from a SEP-0048 XDR stream via {@link #fromXdrBytes(byte[])}.
 *
 * <p>SEP-0048 does not require entry names to be unique, so the {@code getFunction}/{@code
 * getEvent}/{@code getUdt} lookups return the first matching entry in module order.
 *
 * @see <a
 *     href="https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0048.md">SEP-0048</a>
 */
@Getter
@EqualsAndHashCode(of = "entries")
@ToString(of = "entries")
public final class ContractSpec {
  private final List<SCSpecEntry> entries;
  private final List<SCSpecFunctionV0> functions;
  private final List<SCSpecEventV0> events;
  private final List<SCSpecUDTStructV0> structs;
  private final List<SCSpecUDTUnionV0> unions;
  private final List<SCSpecUDTEnumV0> enums;
  private final List<SCSpecUDTErrorEnumV0> errorEnums;

  public ContractSpec() {
    this(Collections.emptyList());
  }

  public ContractSpec(List<SCSpecEntry> entries) {
    if (entries == null) {
      throw new IllegalArgumentException("entries must not be null");
    }
    List<SCSpecEntry> copy = new ArrayList<>(entries.size());
    for (SCSpecEntry entry : entries) {
      if (entry == null) {
        throw new IllegalArgumentException("entries must not contain null elements");
      }
      copy.add(entry);
    }
    this.entries = Collections.unmodifiableList(copy);
    this.functions =
        classify(
            this.entries, SCSpecEntryKind.SC_SPEC_ENTRY_FUNCTION_V0, SCSpecEntry::getFunctionV0);
    this.events =
        classify(this.entries, SCSpecEntryKind.SC_SPEC_ENTRY_EVENT_V0, SCSpecEntry::getEventV0);
    this.structs =
        classify(
            this.entries, SCSpecEntryKind.SC_SPEC_ENTRY_UDT_STRUCT_V0, SCSpecEntry::getUdtStructV0);
    this.unions =
        classify(
            this.entries, SCSpecEntryKind.SC_SPEC_ENTRY_UDT_UNION_V0, SCSpecEntry::getUdtUnionV0);
    this.enums =
        classify(
            this.entries, SCSpecEntryKind.SC_SPEC_ENTRY_UDT_ENUM_V0, SCSpecEntry::getUdtEnumV0);
    this.errorEnums =
        classify(
            this.entries,
            SCSpecEntryKind.SC_SPEC_ENTRY_UDT_ERROR_ENUM_V0,
            SCSpecEntry::getUdtErrorEnumV0);
  }

  private static <T> List<T> classify(
      List<SCSpecEntry> entries, SCSpecEntryKind kind, Function<SCSpecEntry, T> extractor) {
    List<T> result = new ArrayList<>();
    for (SCSpecEntry entry : entries) {
      if (entry.getDiscriminant() == kind) {
        T value = extractor.apply(entry);
        if (value != null) {
          result.add(value);
        }
      }
    }
    return Collections.unmodifiableList(result);
  }

  /**
   * Creates a {@link ContractSpec} from contract Wasm bytes. Returns an empty spec if the {@code
   * contractspecv0} section is absent.
   *
   * @param wasm contract Wasm bytes
   * @throws InvalidWasmException if the Wasm module or specification section cannot be decoded, or
   *     if multiple {@code contractspecv0} sections are present
   */
  public static ContractSpec fromWasm(byte[] wasm) {
    List<byte[]> sections =
        WasmCustomSections.getCustomSections(wasm, WasmCustomSections.CONTRACT_SPEC_SECTION_NAME);
    if (sections.size() > 1) {
      throw new InvalidWasmException(
          "Invalid Wasm module: expected at most one '"
              + WasmCustomSections.CONTRACT_SPEC_SECTION_NAME
              + "' section.");
    }
    if (sections.isEmpty()) {
      return new ContractSpec();
    }
    return new ContractSpec(XdrStreams.parseScSpecEntries(sections.get(0)));
  }

  /**
   * Creates a {@link ContractSpec} from a contract Wasm file.
   *
   * @param path path to the contract Wasm file
   * @throws IOException if the file cannot be read
   * @throws InvalidWasmException if the Wasm module or specification section cannot be decoded
   */
  public static ContractSpec fromWasmFile(Path path) throws IOException {
    return fromWasm(Files.readAllBytes(path));
  }

  /**
   * Creates a {@link ContractSpec} from a SEP-0048 XDR stream of {@link SCSpecEntry} values.
   *
   * @param data XDR stream bytes
   * @throws InvalidWasmException if the XDR stream cannot be decoded
   */
  public static ContractSpec fromXdrBytes(byte[] data) {
    return new ContractSpec(XdrStreams.parseScSpecEntries(data));
  }

  /** Serializes the entries as a SEP-0048 XDR stream. */
  public byte[] toXdrBytes() {
    return XdrStreams.serializeScSpecEntries(entries);
  }

  /**
   * Returns the first function with the given name, if present. SEP-0048 does not require names to
   * be unique.
   *
   * @throws InvalidWasmException if a function name is not valid UTF-8
   */
  public Optional<SCSpecFunctionV0> getFunction(String name) {
    if (name == null) {
      return Optional.empty();
    }
    for (SCSpecFunctionV0 function : getFunctions()) {
      if (name.equals(decodeSymbol(function.getName()))) {
        return Optional.of(function);
      }
    }
    return Optional.empty();
  }

  /**
   * Returns the first event with the given name, if present. SEP-0048 does not require names to be
   * unique.
   *
   * @throws InvalidWasmException if an event name is not valid UTF-8
   */
  public Optional<SCSpecEventV0> getEvent(String name) {
    if (name == null) {
      return Optional.empty();
    }
    for (SCSpecEventV0 event : getEvents()) {
      if (name.equals(decodeSymbol(event.getName()))) {
        return Optional.of(event);
      }
    }
    return Optional.empty();
  }

  /**
   * Returns the first user-defined type entry (struct, union, enum, or error enum) with the given
   * name, if present. SEP-0048 does not require names to be unique.
   *
   * @throws InvalidWasmException if a type name is not valid UTF-8
   */
  public Optional<SCSpecEntry> getUdt(String name) {
    if (name == null) {
      return Optional.empty();
    }
    for (SCSpecEntry entry : entries) {
      String udtName = getUdtName(entry);
      if (udtName != null && udtName.equals(name)) {
        return Optional.of(entry);
      }
    }
    return Optional.empty();
  }

  private static String getUdtName(SCSpecEntry entry) {
    switch (entry.getDiscriminant()) {
      case SC_SPEC_ENTRY_UDT_STRUCT_V0:
        return entry.getUdtStructV0() != null
            ? decodeString(entry.getUdtStructV0().getName())
            : null;
      case SC_SPEC_ENTRY_UDT_UNION_V0:
        return entry.getUdtUnionV0() != null ? decodeString(entry.getUdtUnionV0().getName()) : null;
      case SC_SPEC_ENTRY_UDT_ENUM_V0:
        return entry.getUdtEnumV0() != null ? decodeString(entry.getUdtEnumV0().getName()) : null;
      case SC_SPEC_ENTRY_UDT_ERROR_ENUM_V0:
        return entry.getUdtErrorEnumV0() != null
            ? decodeString(entry.getUdtErrorEnumV0().getName())
            : null;
      default:
        return null;
    }
  }

  private static String decodeSymbol(SCSymbol symbol) {
    if (symbol == null || symbol.getSCSymbol() == null) {
      throw new InvalidWasmException("Contract spec contains a null symbol.");
    }
    return decodeName(symbol.getSCSymbol().getBytes(), "symbol");
  }

  private static String decodeString(XdrString value) {
    if (value == null) {
      throw new InvalidWasmException("Contract spec contains a null string.");
    }
    return decodeName(value.getBytes(), "string");
  }

  private static String decodeName(byte[] data, String kind) {
    try {
      return Utf8.strictDecode(data);
    } catch (CharacterCodingException e) {
      throw new InvalidWasmException("Contract spec contains a non-UTF-8 " + kind + ".", e);
    }
  }
}
