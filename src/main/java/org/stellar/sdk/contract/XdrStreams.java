package org.stellar.sdk.contract;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.stellar.sdk.contract.exception.InvalidWasmException;
import org.stellar.sdk.xdr.SCEnvMetaEntry;
import org.stellar.sdk.xdr.SCMetaEntry;
import org.stellar.sdk.xdr.SCSpecEntry;
import org.stellar.sdk.xdr.XdrDataInputStream;
import org.stellar.sdk.xdr.XdrDataOutputStream;

/**
 * Parses and serializes unframed XDR streams of contract introspection entries (SEP-0046/0047 meta,
 * SEP-0048 spec, and contract environment meta).
 */
final class XdrStreams {
  private XdrStreams() {}

  @FunctionalInterface
  private interface XdrDecoder<T> {
    T decode(XdrDataInputStream stream) throws IOException;
  }

  @FunctionalInterface
  private interface XdrEncoder<T> {
    void encode(T entry, XdrDataOutputStream stream) throws IOException;
  }

  static List<SCMetaEntry> parseScMetaEntries(byte[] data) {
    return parseStream(data, SCMetaEntry::decode, "SCMetaEntry");
  }

  static List<SCSpecEntry> parseScSpecEntries(byte[] data) {
    return parseStream(data, SCSpecEntry::decode, "SCSpecEntry");
  }

  static List<SCEnvMetaEntry> parseScEnvMetaEntries(byte[] data) {
    return parseStream(data, SCEnvMetaEntry::decode, "SCEnvMetaEntry");
  }

  static byte[] serializeScMetaEntries(Iterable<SCMetaEntry> entries) {
    return serializeStream(entries, SCMetaEntry::encode);
  }

  static byte[] serializeScSpecEntries(Iterable<SCSpecEntry> entries) {
    return serializeStream(entries, SCSpecEntry::encode);
  }

  static byte[] serializeScEnvMetaEntries(Iterable<SCEnvMetaEntry> entries) {
    return serializeStream(entries, SCEnvMetaEntry::encode);
  }

  private static <T> List<T> parseStream(byte[] data, XdrDecoder<T> decoder, String entryName) {
    if (data == null) {
      throw new IllegalArgumentException("data must not be null");
    }
    ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
    XdrDataInputStream xdrStream = new XdrDataInputStream(byteStream);
    xdrStream.setMaxInputLen(data.length);

    List<T> entries = new ArrayList<>();
    int previousAvailable = data.length;
    while (previousAvailable > 0) {
      T entry;
      try {
        entry = decoder.decode(xdrStream);
      } catch (IOException | IllegalArgumentException e) {
        throw new InvalidWasmException("Invalid XDR stream for " + entryName + ".", e);
      }
      int currentAvailable = byteStream.available();
      if (currentAvailable >= previousAvailable) {
        throw new InvalidWasmException(
            "Invalid XDR stream for " + entryName + ": decoder made no progress.");
      }
      previousAvailable = currentAvailable;
      entries.add(entry);
    }

    if (byteStream.available() != 0) {
      throw new InvalidWasmException("Invalid XDR stream for " + entryName + ": trailing bytes.");
    }
    return entries;
  }

  private static <T> byte[] serializeStream(Iterable<T> entries, XdrEncoder<T> encoder) {
    if (entries == null) {
      throw new IllegalArgumentException("entries must not be null");
    }
    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrStream = new XdrDataOutputStream(byteStream);
    try {
      for (T entry : entries) {
        if (entry == null) {
          throw new IllegalArgumentException("entries must not contain null elements");
        }
        encoder.encode(entry, xdrStream);
      }
    } catch (IOException e) {
      throw new InvalidWasmException("Failed to serialize XDR stream.", e);
    }
    return byteStream.toByteArray();
  }
}
