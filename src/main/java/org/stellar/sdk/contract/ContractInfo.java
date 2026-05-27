package org.stellar.sdk.contract;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.stellar.sdk.contract.exception.InvalidWasmException;
import org.stellar.sdk.xdr.SCEnvMetaEntry;
import org.stellar.sdk.xdr.SCMetaEntry;
import org.stellar.sdk.xdr.SCSpecEntry;

/**
 * Aggregates SEP-0046 contract metadata, SEP-0048 interface specification, and contract environment
 * metadata read from a single Wasm module.
 */
@Getter
@EqualsAndHashCode
@ToString
public final class ContractInfo {
  private final ContractMeta meta;
  private final ContractSpec spec;
  private final List<SCEnvMetaEntry> envMeta;

  public ContractInfo(ContractMeta meta, ContractSpec spec) {
    this(meta, spec, Collections.emptyList());
  }

  public ContractInfo(ContractMeta meta, ContractSpec spec, List<SCEnvMetaEntry> envMeta) {
    if (meta == null) {
      throw new IllegalArgumentException("meta must not be null");
    }
    if (spec == null) {
      throw new IllegalArgumentException("spec must not be null");
    }
    if (envMeta == null) {
      throw new IllegalArgumentException("envMeta must not be null");
    }
    List<SCEnvMetaEntry> copy = new ArrayList<>(envMeta.size());
    for (SCEnvMetaEntry entry : envMeta) {
      if (entry == null) {
        throw new IllegalArgumentException("envMeta must not contain null elements");
      }
      copy.add(entry);
    }
    this.meta = meta;
    this.spec = spec;
    this.envMeta = Collections.unmodifiableList(copy);
  }

  /**
   * Creates a {@link ContractInfo} from contract Wasm bytes. Scans the module once and reads {@code
   * contractmetav0}, {@code contractspecv0}, and {@code contractenvmetav0} sections.
   *
   * @param wasm contract Wasm bytes
   * @throws InvalidWasmException if the Wasm module or any introspection section cannot be decoded,
   *     or if multiple {@code contractspecv0} sections are present
   */
  public static ContractInfo fromWasm(byte[] wasm) {
    List<SCMetaEntry> metaEntries = new ArrayList<>();
    List<byte[]> specSections = new ArrayList<>();
    List<SCEnvMetaEntry> envMetaEntries = new ArrayList<>();

    for (Map.Entry<String, byte[]> section : WasmCustomSections.getCustomSections(wasm)) {
      String name = section.getKey();
      byte[] payload = section.getValue();
      if (WasmCustomSections.CONTRACT_META_SECTION_NAME.equals(name)) {
        metaEntries.addAll(XdrStreams.parseScMetaEntries(payload));
      } else if (WasmCustomSections.CONTRACT_SPEC_SECTION_NAME.equals(name)) {
        specSections.add(payload);
      } else if (WasmCustomSections.CONTRACT_ENV_META_SECTION_NAME.equals(name)) {
        envMetaEntries.addAll(XdrStreams.parseScEnvMetaEntries(payload));
      }
    }

    if (specSections.size() > 1) {
      throw new InvalidWasmException(
          "Invalid Wasm module: expected at most one '"
              + WasmCustomSections.CONTRACT_SPEC_SECTION_NAME
              + "' section.");
    }

    List<SCSpecEntry> specEntries =
        specSections.isEmpty()
            ? Collections.<SCSpecEntry>emptyList()
            : XdrStreams.parseScSpecEntries(specSections.get(0));

    return new ContractInfo(
        new ContractMeta(metaEntries), new ContractSpec(specEntries), envMetaEntries);
  }

  /**
   * Creates a {@link ContractInfo} from a contract Wasm file.
   *
   * @param path path to the contract Wasm file
   * @throws IOException if the file cannot be read
   * @throws InvalidWasmException if the Wasm module or any introspection section cannot be decoded
   */
  public static ContractInfo fromWasmFile(Path path) throws IOException {
    return fromWasm(Files.readAllBytes(path));
  }
}
