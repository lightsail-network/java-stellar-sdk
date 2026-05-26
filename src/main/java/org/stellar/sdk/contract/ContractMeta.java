package org.stellar.sdk.contract;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.stellar.sdk.contract.exception.InvalidWasmException;
import org.stellar.sdk.xdr.SCMetaEntry;
import org.stellar.sdk.xdr.SCMetaKind;
import org.stellar.sdk.xdr.SCMetaV0;

/**
 * Represents SEP-0046 contract metadata and SEP-0047 contract interface discovery data.
 *
 * <p>Entries are stored in module order and exposed as an unmodifiable list. Construct with raw
 * {@link SCMetaEntry} values, decode from contract Wasm bytes via {@link #fromWasm(byte[])}, or
 * decode from a SEP-0046 XDR stream via {@link #fromXdrBytes(byte[])}.
 *
 * @see <a
 *     href="https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0046.md">SEP-0046</a>
 * @see <a
 *     href="https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0047.md">SEP-0047</a>
 */
@Getter
@EqualsAndHashCode
@ToString
public final class ContractMeta {
  private final List<SCMetaEntry> entries;

  public ContractMeta() {
    this(Collections.emptyList());
  }

  public ContractMeta(List<SCMetaEntry> entries) {
    if (entries == null) {
      throw new IllegalArgumentException("entries must not be null");
    }
    List<SCMetaEntry> copy = new ArrayList<>(entries.size());
    for (SCMetaEntry entry : entries) {
      if (entry == null) {
        throw new IllegalArgumentException("entries must not contain null elements");
      }
      copy.add(entry);
    }
    this.entries = Collections.unmodifiableList(copy);
  }

  /**
   * Creates a {@link ContractMeta} from contract Wasm bytes by concatenating all {@code
   * contractmetav0} custom sections in module order.
   *
   * @param wasm contract Wasm bytes
   * @throws InvalidWasmException if the Wasm module or metadata section cannot be decoded
   */
  public static ContractMeta fromWasm(byte[] wasm) {
    List<SCMetaEntry> entries = new ArrayList<>();
    for (byte[] section :
        WasmCustomSections.getCustomSections(wasm, WasmCustomSections.CONTRACT_META_SECTION_NAME)) {
      entries.addAll(XdrStreams.parseScMetaEntries(section));
    }
    return new ContractMeta(entries);
  }

  /**
   * Creates a {@link ContractMeta} from a contract Wasm file.
   *
   * @param path path to the contract Wasm file
   * @throws IOException if the file cannot be read
   * @throws InvalidWasmException if the Wasm module or metadata section cannot be decoded
   */
  public static ContractMeta fromWasmFile(Path path) throws IOException {
    return fromWasm(Files.readAllBytes(path));
  }

  /**
   * Creates a {@link ContractMeta} from a SEP-0046 XDR stream of {@link SCMetaEntry} values.
   *
   * @param data XDR stream bytes
   * @throws InvalidWasmException if the XDR stream cannot be decoded
   */
  public static ContractMeta fromXdrBytes(byte[] data) {
    return new ContractMeta(XdrStreams.parseScMetaEntries(data));
  }

  /** Serializes the entries as a SEP-0046 XDR stream. */
  public byte[] toXdrBytes() {
    return XdrStreams.serializeScMetaEntries(entries);
  }

  /**
   * Returns {@code SC_META_V0} key/value pairs decoded as UTF-8, in entry order.
   *
   * @throws InvalidWasmException if a key or value is not valid UTF-8
   */
  public List<Map.Entry<String, String>> items() {
    List<Map.Entry<String, String>> items = new ArrayList<>();
    for (SCMetaEntry entry : entries) {
      if (entry.getDiscriminant() != SCMetaKind.SC_META_V0 || entry.getV0() == null) {
        continue;
      }
      SCMetaV0 v0 = entry.getV0();
      items.add(
          new AbstractMap.SimpleImmutableEntry<>(
              decodeMetaString(v0.getKey().getBytes()), decodeMetaString(v0.getVal().getBytes())));
    }
    return Collections.unmodifiableList(items);
  }

  /**
   * Returns the first {@code SC_META_V0} value for {@code key}.
   *
   * @throws InvalidWasmException if a key or value is not valid UTF-8
   */
  public Optional<String> get(String key) {
    if (key == null) {
      return Optional.empty();
    }
    for (Map.Entry<String, String> item : items()) {
      if (key.equals(item.getKey())) {
        return Optional.of(item.getValue());
      }
    }
    return Optional.empty();
  }

  /**
   * Returns all {@code SC_META_V0} values for {@code key} in entry order.
   *
   * @throws InvalidWasmException if a key or value is not valid UTF-8
   */
  public List<String> getAll(String key) {
    if (key == null) {
      return Collections.emptyList();
    }
    List<String> values = new ArrayList<>();
    for (Map.Entry<String, String> item : items()) {
      if (key.equals(item.getKey())) {
        values.add(item.getValue());
      }
    }
    return Collections.unmodifiableList(values);
  }

  /**
   * Returns SEP-0047 SEP identifiers declared via {@code sep} metadata entries. Invalid identifiers
   * are skipped.
   *
   * <p>SEP-0047 treats the declared identifiers as an unordered set: the value "may be in any
   * order" and the same identifier may be repeated across entries. The returned set is
   * de-duplicated; its iteration order is the first-seen order purely for deterministic output and
   * carries no meaning.
   */
  public Set<Integer> supportedSeps() {
    return supportedSeps(false);
  }

  /**
   * Returns SEP-0047 SEP identifiers declared via {@code sep} metadata entries.
   *
   * @param strict when true, invalid identifiers cause an {@link IllegalArgumentException} rather
   *     than being skipped
   * @throws IllegalArgumentException if {@code strict} is true and an identifier is invalid
   * @throws InvalidWasmException if a key or value is not valid UTF-8
   */
  public Set<Integer> supportedSeps(boolean strict) {
    Set<Integer> supported = new LinkedHashSet<>();
    for (String value : getAll("sep")) {
      for (String rawPart : value.split(",", -1)) {
        String sep = rawPart.trim();
        if (sep.isEmpty()) {
          if (strict) {
            throw new IllegalArgumentException("Invalid SEP identifier: empty value.");
          }
          continue;
        }
        if (!isAsciiDecimal(sep)) {
          if (strict) {
            throw new IllegalArgumentException("Invalid SEP identifier: '" + sep + "'.");
          }
          continue;
        }
        try {
          supported.add(Integer.parseInt(sep));
        } catch (NumberFormatException e) {
          if (strict) {
            throw new IllegalArgumentException("Invalid SEP identifier: '" + sep + "'.", e);
          }
        }
      }
    }
    return Collections.unmodifiableSet(supported);
  }

  /** Returns whether the contract declares support for {@code sep} via SEP-0047. */
  public boolean implementsSep(int sep) {
    return supportedSeps().contains(sep);
  }

  private static String decodeMetaString(byte[] data) {
    try {
      return Utf8.strictDecode(data);
    } catch (CharacterCodingException e) {
      throw new InvalidWasmException("Contract meta contains a non-UTF-8 string.", e);
    }
  }

  private static boolean isAsciiDecimal(String value) {
    if (value.isEmpty()) {
      return false;
    }
    for (int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);
      if (c < '0' || c > '9') {
        return false;
      }
    }
    return true;
  }
}
