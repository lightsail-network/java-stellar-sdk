// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

// === xdr source ============================================================

//  struct SorobanTransactionMeta
//  {
//      ExtensionPoint ext;
//
//      ContractEvent events<>;             // custom events populated by the
//                                          // contracts themselves.
//      SCVal returnValue;                  // return value of the host fn invocation
//
//      // Diagnostics events that are not hashed.
//      // This will contain all contract and diagnostic events. Even ones
//      // that were emitted in a failed contract call.
//      DiagnosticEvent diagnosticEvents<>;
//  };

//  ===========================================================================
public class SorobanTransactionMeta implements XdrElement {
  public SorobanTransactionMeta() {}

  private ExtensionPoint ext;

  public ExtensionPoint getExt() {
    return this.ext;
  }

  public void setExt(ExtensionPoint value) {
    this.ext = value;
  }

  private ContractEvent[] events;

  public ContractEvent[] getEvents() {
    return this.events;
  }

  public void setEvents(ContractEvent[] value) {
    this.events = value;
  }

  private SCVal returnValue;

  public SCVal getReturnValue() {
    return this.returnValue;
  }

  public void setReturnValue(SCVal value) {
    this.returnValue = value;
  }

  private DiagnosticEvent[] diagnosticEvents;

  public DiagnosticEvent[] getDiagnosticEvents() {
    return this.diagnosticEvents;
  }

  public void setDiagnosticEvents(DiagnosticEvent[] value) {
    this.diagnosticEvents = value;
  }

  public static void encode(
      XdrDataOutputStream stream, SorobanTransactionMeta encodedSorobanTransactionMeta)
      throws IOException {
    ExtensionPoint.encode(stream, encodedSorobanTransactionMeta.ext);
    int eventssize = encodedSorobanTransactionMeta.getEvents().length;
    stream.writeInt(eventssize);
    for (int i = 0; i < eventssize; i++) {
      ContractEvent.encode(stream, encodedSorobanTransactionMeta.events[i]);
    }
    SCVal.encode(stream, encodedSorobanTransactionMeta.returnValue);
    int diagnosticEventssize = encodedSorobanTransactionMeta.getDiagnosticEvents().length;
    stream.writeInt(diagnosticEventssize);
    for (int i = 0; i < diagnosticEventssize; i++) {
      DiagnosticEvent.encode(stream, encodedSorobanTransactionMeta.diagnosticEvents[i]);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SorobanTransactionMeta decode(XdrDataInputStream stream) throws IOException {
    SorobanTransactionMeta decodedSorobanTransactionMeta = new SorobanTransactionMeta();
    decodedSorobanTransactionMeta.ext = ExtensionPoint.decode(stream);
    int eventssize = stream.readInt();
    decodedSorobanTransactionMeta.events = new ContractEvent[eventssize];
    for (int i = 0; i < eventssize; i++) {
      decodedSorobanTransactionMeta.events[i] = ContractEvent.decode(stream);
    }
    decodedSorobanTransactionMeta.returnValue = SCVal.decode(stream);
    int diagnosticEventssize = stream.readInt();
    decodedSorobanTransactionMeta.diagnosticEvents = new DiagnosticEvent[diagnosticEventssize];
    for (int i = 0; i < diagnosticEventssize; i++) {
      decodedSorobanTransactionMeta.diagnosticEvents[i] = DiagnosticEvent.decode(stream);
    }
    return decodedSorobanTransactionMeta;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.ext,
        Arrays.hashCode(this.events),
        this.returnValue,
        Arrays.hashCode(this.diagnosticEvents));
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SorobanTransactionMeta)) {
      return false;
    }

    SorobanTransactionMeta other = (SorobanTransactionMeta) object;
    return Objects.equals(this.ext, other.ext)
        && Arrays.equals(this.events, other.events)
        && Objects.equals(this.returnValue, other.returnValue)
        && Arrays.equals(this.diagnosticEvents, other.diagnosticEvents);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64.getEncoder().encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static SorobanTransactionMeta fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SorobanTransactionMeta fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private ExtensionPoint ext;
    private ContractEvent[] events;
    private SCVal returnValue;
    private DiagnosticEvent[] diagnosticEvents;

    public Builder ext(ExtensionPoint ext) {
      this.ext = ext;
      return this;
    }

    public Builder events(ContractEvent[] events) {
      this.events = events;
      return this;
    }

    public Builder returnValue(SCVal returnValue) {
      this.returnValue = returnValue;
      return this;
    }

    public Builder diagnosticEvents(DiagnosticEvent[] diagnosticEvents) {
      this.diagnosticEvents = diagnosticEvents;
      return this;
    }

    public SorobanTransactionMeta build() {
      SorobanTransactionMeta val = new SorobanTransactionMeta();
      val.setExt(this.ext);
      val.setEvents(this.events);
      val.setReturnValue(this.returnValue);
      val.setDiagnosticEvents(this.diagnosticEvents);
      return val;
    }
  }
}
