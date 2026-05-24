package org.stellar.sdk.contract;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
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
 * <p>Entries are stored in module order and exposed as an unmodifiable list. Construct with raw
 * {@link SCSpecEntry} values, decode from contract Wasm bytes via {@link #fromWasm(byte[])}, or
 * decode from a SEP-0048 XDR stream via {@link #fromXdrBytes(byte[])}.
 *
 * @see <a
 *     href="https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0048.md">SEP-0048</a>
 */
@Getter
@EqualsAndHashCode
@ToString
public final class ContractSpec implements Iterable<SCSpecEntry> {
  private final List<SCSpecEntry> entries;

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

  public List<SCSpecFunctionV0> getFunctions() {
    List<SCSpecFunctionV0> functions = new ArrayList<>();
    for (SCSpecEntry entry : entries) {
      if (entry.getDiscriminant() == SCSpecEntryKind.SC_SPEC_ENTRY_FUNCTION_V0
          && entry.getFunctionV0() != null) {
        functions.add(entry.getFunctionV0());
      }
    }
    return Collections.unmodifiableList(functions);
  }

  public List<SCSpecEventV0> getEvents() {
    List<SCSpecEventV0> events = new ArrayList<>();
    for (SCSpecEntry entry : entries) {
      if (entry.getDiscriminant() == SCSpecEntryKind.SC_SPEC_ENTRY_EVENT_V0
          && entry.getEventV0() != null) {
        events.add(entry.getEventV0());
      }
    }
    return Collections.unmodifiableList(events);
  }

  public List<SCSpecUDTStructV0> getStructs() {
    List<SCSpecUDTStructV0> structs = new ArrayList<>();
    for (SCSpecEntry entry : entries) {
      if (entry.getDiscriminant() == SCSpecEntryKind.SC_SPEC_ENTRY_UDT_STRUCT_V0
          && entry.getUdtStructV0() != null) {
        structs.add(entry.getUdtStructV0());
      }
    }
    return Collections.unmodifiableList(structs);
  }

  public List<SCSpecUDTUnionV0> getUnions() {
    List<SCSpecUDTUnionV0> unions = new ArrayList<>();
    for (SCSpecEntry entry : entries) {
      if (entry.getDiscriminant() == SCSpecEntryKind.SC_SPEC_ENTRY_UDT_UNION_V0
          && entry.getUdtUnionV0() != null) {
        unions.add(entry.getUdtUnionV0());
      }
    }
    return Collections.unmodifiableList(unions);
  }

  public List<SCSpecUDTEnumV0> getEnums() {
    List<SCSpecUDTEnumV0> enums = new ArrayList<>();
    for (SCSpecEntry entry : entries) {
      if (entry.getDiscriminant() == SCSpecEntryKind.SC_SPEC_ENTRY_UDT_ENUM_V0
          && entry.getUdtEnumV0() != null) {
        enums.add(entry.getUdtEnumV0());
      }
    }
    return Collections.unmodifiableList(enums);
  }

  public List<SCSpecUDTErrorEnumV0> getErrorEnums() {
    List<SCSpecUDTErrorEnumV0> errorEnums = new ArrayList<>();
    for (SCSpecEntry entry : entries) {
      if (entry.getDiscriminant() == SCSpecEntryKind.SC_SPEC_ENTRY_UDT_ERROR_ENUM_V0
          && entry.getUdtErrorEnumV0() != null) {
        errorEnums.add(entry.getUdtErrorEnumV0());
      }
    }
    return Collections.unmodifiableList(errorEnums);
  }

  /**
   * Returns the function with the given name, if present.
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
   * Returns the event with the given name, if present.
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
   * Returns the user-defined type entry (struct, union, enum, or error enum) with the given name,
   * if present.
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

  @Override
  public Iterator<SCSpecEntry> iterator() {
    return entries.iterator();
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
    return decodeUtf8Strict(symbol.getSCSymbol().getBytes(), "symbol");
  }

  private static String decodeString(XdrString value) {
    if (value == null) {
      throw new InvalidWasmException("Contract spec contains a null string.");
    }
    return decodeUtf8Strict(value.getBytes(), "string");
  }

  private static String decodeUtf8Strict(byte[] data, String kind) {
    CharsetDecoder decoder =
        StandardCharsets.UTF_8
            .newDecoder()
            .onMalformedInput(CodingErrorAction.REPORT)
            .onUnmappableCharacter(CodingErrorAction.REPORT);
    try {
      return decoder.decode(java.nio.ByteBuffer.wrap(data)).toString();
    } catch (CharacterCodingException e) {
      throw new InvalidWasmException("Contract spec contains a non-UTF-8 " + kind + ".", e);
    }
  }
}
