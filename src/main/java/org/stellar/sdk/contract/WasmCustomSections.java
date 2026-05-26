package org.stellar.sdk.contract;

import java.nio.charset.CharacterCodingException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.stellar.sdk.contract.exception.InvalidWasmException;

/**
 * Parses Wasm modules and extracts custom section payloads. Intended for SEP-0046/0047/0048
 * contract introspection.
 *
 * <p>Implements just enough of the Wasm binary format to walk top-level sections, validate the
 * header, and return the raw payload bytes of custom sections by name.
 */
final class WasmCustomSections {
  static final String CONTRACT_META_SECTION_NAME = "contractmetav0";
  static final String CONTRACT_SPEC_SECTION_NAME = "contractspecv0";
  static final String CONTRACT_ENV_META_SECTION_NAME = "contractenvmetav0";

  private static final byte[] WASM_MAGIC = {0x00, 0x61, 0x73, 0x6d};
  private static final byte[] WASM_VERSION_1 = {0x01, 0x00, 0x00, 0x00};
  private static final int CUSTOM_SECTION_ID = 0;
  private static final int MAX_LEB128_U32_BYTES = 5;

  private WasmCustomSections() {}

  /**
   * Returns all custom section name/payload pairs in module order. Each payload is a fresh byte
   * array copied out of the input.
   */
  static List<Map.Entry<String, byte[]>> getCustomSections(byte[] wasm) {
    if (wasm == null) {
      throw new IllegalArgumentException("wasm must not be null");
    }
    if (wasm.length < 8) {
      throw new InvalidWasmException("Invalid Wasm module: header is too short.");
    }
    if (!matches(wasm, 0, WASM_MAGIC)) {
      throw new InvalidWasmException("Invalid Wasm module: bad magic header.");
    }
    if (!matches(wasm, 4, WASM_VERSION_1)) {
      throw new InvalidWasmException("Invalid Wasm module: unsupported version.");
    }

    List<Map.Entry<String, byte[]>> sections = new ArrayList<>();
    int offset = 8;
    int wasmLen = wasm.length;
    while (offset < wasmLen) {
      int sectionId = wasm[offset] & 0xff;
      offset += 1;

      long[] sizeRead = readU32Leb128(wasm, offset, wasmLen);
      long sectionSize = sizeRead[0];
      offset = (int) sizeRead[1];

      long sectionEndLong = (long) offset + sectionSize;
      if (sectionEndLong > wasmLen) {
        throw new InvalidWasmException("Invalid Wasm module: section extends past EOF.");
      }
      int sectionEnd = (int) sectionEndLong;

      if (sectionId == CUSTOM_SECTION_ID) {
        long[] nameLenRead = readU32Leb128(wasm, offset, sectionEnd);
        long nameLen = nameLenRead[0];
        int nameStart = (int) nameLenRead[1];
        long nameEndLong = (long) nameStart + nameLen;
        if (nameEndLong > sectionEnd) {
          throw new InvalidWasmException(
              "Invalid Wasm custom section: name extends past section end.");
        }
        int nameEnd = (int) nameEndLong;
        String name = decodeSectionName(Arrays.copyOfRange(wasm, nameStart, nameEnd));
        byte[] payload = Arrays.copyOfRange(wasm, nameEnd, sectionEnd);
        sections.add(new AbstractMap.SimpleImmutableEntry<>(name, payload));
      }

      offset = sectionEnd;
    }
    return sections;
  }

  /** Returns all payloads for custom sections matching {@code name} in module order. */
  static List<byte[]> getCustomSections(byte[] wasm, String name) {
    if (name == null) {
      throw new IllegalArgumentException("name must not be null");
    }
    List<byte[]> result = new ArrayList<>();
    for (Map.Entry<String, byte[]> entry : getCustomSections(wasm)) {
      if (name.equals(entry.getKey())) {
        result.add(entry.getValue());
      }
    }
    return result;
  }

  private static boolean matches(byte[] data, int offset, byte[] expected) {
    for (int i = 0; i < expected.length; i++) {
      if (data[offset + i] != expected[i]) {
        return false;
      }
    }
    return true;
  }

  /**
   * Reads an unsigned LEB128 u32 from {@code data} starting at {@code offset}. Returns a two-long
   * array {@code [value, nextOffset]}. {@code limit} is exclusive — reads must not pass it.
   */
  private static long[] readU32Leb128(byte[] data, int offset, int limit) {
    long result = 0;
    int shift = 0;
    int pos = offset;
    for (int i = 0; i < MAX_LEB128_U32_BYTES; i++) {
      if (pos >= limit) {
        throw new InvalidWasmException("Invalid Wasm module: truncated LEB128 value.");
      }
      int b = data[pos] & 0xff;
      pos += 1;
      result |= ((long) (b & 0x7f)) << shift;
      if ((b & 0x80) == 0) {
        if (result > 0xffffffffL) {
          throw new InvalidWasmException("Invalid Wasm module: LEB128 value exceeds u32.");
        }
        return new long[] {result, pos};
      }
      shift += 7;
    }
    throw new InvalidWasmException("Invalid Wasm module: LEB128 value is too long.");
  }

  private static String decodeSectionName(byte[] data) {
    try {
      return Utf8.strictDecode(data);
    } catch (CharacterCodingException e) {
      throw new InvalidWasmException("Invalid Wasm custom section: name is not UTF-8.", e);
    }
  }
}
