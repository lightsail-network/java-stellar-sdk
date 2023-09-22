// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.stellar.sdk.Base64;

// === xdr source ============================================================

//  struct TopologyResponseBodyV1
//  {
//      PeerStatList inboundPeers;
//      PeerStatList outboundPeers;
//
//      uint32 totalInboundPeerCount;
//      uint32 totalOutboundPeerCount;
//
//      uint32 maxInboundPeerCount;
//      uint32 maxOutboundPeerCount;
//  };

//  ===========================================================================
public class TopologyResponseBodyV1 implements XdrElement {
  public TopologyResponseBodyV1() {}

  private PeerStatList inboundPeers;

  public PeerStatList getInboundPeers() {
    return this.inboundPeers;
  }

  public void setInboundPeers(PeerStatList value) {
    this.inboundPeers = value;
  }

  private PeerStatList outboundPeers;

  public PeerStatList getOutboundPeers() {
    return this.outboundPeers;
  }

  public void setOutboundPeers(PeerStatList value) {
    this.outboundPeers = value;
  }

  private Uint32 totalInboundPeerCount;

  public Uint32 getTotalInboundPeerCount() {
    return this.totalInboundPeerCount;
  }

  public void setTotalInboundPeerCount(Uint32 value) {
    this.totalInboundPeerCount = value;
  }

  private Uint32 totalOutboundPeerCount;

  public Uint32 getTotalOutboundPeerCount() {
    return this.totalOutboundPeerCount;
  }

  public void setTotalOutboundPeerCount(Uint32 value) {
    this.totalOutboundPeerCount = value;
  }

  private Uint32 maxInboundPeerCount;

  public Uint32 getMaxInboundPeerCount() {
    return this.maxInboundPeerCount;
  }

  public void setMaxInboundPeerCount(Uint32 value) {
    this.maxInboundPeerCount = value;
  }

  private Uint32 maxOutboundPeerCount;

  public Uint32 getMaxOutboundPeerCount() {
    return this.maxOutboundPeerCount;
  }

  public void setMaxOutboundPeerCount(Uint32 value) {
    this.maxOutboundPeerCount = value;
  }

  public static void encode(
      XdrDataOutputStream stream, TopologyResponseBodyV1 encodedTopologyResponseBodyV1)
      throws IOException {
    PeerStatList.encode(stream, encodedTopologyResponseBodyV1.inboundPeers);
    PeerStatList.encode(stream, encodedTopologyResponseBodyV1.outboundPeers);
    Uint32.encode(stream, encodedTopologyResponseBodyV1.totalInboundPeerCount);
    Uint32.encode(stream, encodedTopologyResponseBodyV1.totalOutboundPeerCount);
    Uint32.encode(stream, encodedTopologyResponseBodyV1.maxInboundPeerCount);
    Uint32.encode(stream, encodedTopologyResponseBodyV1.maxOutboundPeerCount);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static TopologyResponseBodyV1 decode(XdrDataInputStream stream) throws IOException {
    TopologyResponseBodyV1 decodedTopologyResponseBodyV1 = new TopologyResponseBodyV1();
    decodedTopologyResponseBodyV1.inboundPeers = PeerStatList.decode(stream);
    decodedTopologyResponseBodyV1.outboundPeers = PeerStatList.decode(stream);
    decodedTopologyResponseBodyV1.totalInboundPeerCount = Uint32.decode(stream);
    decodedTopologyResponseBodyV1.totalOutboundPeerCount = Uint32.decode(stream);
    decodedTopologyResponseBodyV1.maxInboundPeerCount = Uint32.decode(stream);
    decodedTopologyResponseBodyV1.maxOutboundPeerCount = Uint32.decode(stream);
    return decodedTopologyResponseBodyV1;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.inboundPeers,
        this.outboundPeers,
        this.totalInboundPeerCount,
        this.totalOutboundPeerCount,
        this.maxInboundPeerCount,
        this.maxOutboundPeerCount);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof TopologyResponseBodyV1)) {
      return false;
    }

    TopologyResponseBodyV1 other = (TopologyResponseBodyV1) object;
    return Objects.equals(this.inboundPeers, other.inboundPeers)
        && Objects.equals(this.outboundPeers, other.outboundPeers)
        && Objects.equals(this.totalInboundPeerCount, other.totalInboundPeerCount)
        && Objects.equals(this.totalOutboundPeerCount, other.totalOutboundPeerCount)
        && Objects.equals(this.maxInboundPeerCount, other.maxInboundPeerCount)
        && Objects.equals(this.maxOutboundPeerCount, other.maxOutboundPeerCount);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64.encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static TopologyResponseBodyV1 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TopologyResponseBodyV1 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private PeerStatList inboundPeers;
    private PeerStatList outboundPeers;
    private Uint32 totalInboundPeerCount;
    private Uint32 totalOutboundPeerCount;
    private Uint32 maxInboundPeerCount;
    private Uint32 maxOutboundPeerCount;

    public Builder inboundPeers(PeerStatList inboundPeers) {
      this.inboundPeers = inboundPeers;
      return this;
    }

    public Builder outboundPeers(PeerStatList outboundPeers) {
      this.outboundPeers = outboundPeers;
      return this;
    }

    public Builder totalInboundPeerCount(Uint32 totalInboundPeerCount) {
      this.totalInboundPeerCount = totalInboundPeerCount;
      return this;
    }

    public Builder totalOutboundPeerCount(Uint32 totalOutboundPeerCount) {
      this.totalOutboundPeerCount = totalOutboundPeerCount;
      return this;
    }

    public Builder maxInboundPeerCount(Uint32 maxInboundPeerCount) {
      this.maxInboundPeerCount = maxInboundPeerCount;
      return this;
    }

    public Builder maxOutboundPeerCount(Uint32 maxOutboundPeerCount) {
      this.maxOutboundPeerCount = maxOutboundPeerCount;
      return this;
    }

    public TopologyResponseBodyV1 build() {
      TopologyResponseBodyV1 val = new TopologyResponseBodyV1();
      val.setInboundPeers(this.inboundPeers);
      val.setOutboundPeers(this.outboundPeers);
      val.setTotalInboundPeerCount(this.totalInboundPeerCount);
      val.setTotalOutboundPeerCount(this.totalOutboundPeerCount);
      val.setMaxInboundPeerCount(this.maxInboundPeerCount);
      val.setMaxOutboundPeerCount(this.maxOutboundPeerCount);
      return val;
    }
  }
}
