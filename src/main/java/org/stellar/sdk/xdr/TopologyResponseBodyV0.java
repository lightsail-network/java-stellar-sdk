// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

// === xdr source ============================================================

//  struct TopologyResponseBodyV0
//  {
//      PeerStatList inboundPeers;
//      PeerStatList outboundPeers;
//
//      uint32 totalInboundPeerCount;
//      uint32 totalOutboundPeerCount;
//  };

//  ===========================================================================
public class TopologyResponseBodyV0 implements XdrElement {
  public TopologyResponseBodyV0() {}

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

  public static void encode(
      XdrDataOutputStream stream, TopologyResponseBodyV0 encodedTopologyResponseBodyV0)
      throws IOException {
    PeerStatList.encode(stream, encodedTopologyResponseBodyV0.inboundPeers);
    PeerStatList.encode(stream, encodedTopologyResponseBodyV0.outboundPeers);
    Uint32.encode(stream, encodedTopologyResponseBodyV0.totalInboundPeerCount);
    Uint32.encode(stream, encodedTopologyResponseBodyV0.totalOutboundPeerCount);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static TopologyResponseBodyV0 decode(XdrDataInputStream stream) throws IOException {
    TopologyResponseBodyV0 decodedTopologyResponseBodyV0 = new TopologyResponseBodyV0();
    decodedTopologyResponseBodyV0.inboundPeers = PeerStatList.decode(stream);
    decodedTopologyResponseBodyV0.outboundPeers = PeerStatList.decode(stream);
    decodedTopologyResponseBodyV0.totalInboundPeerCount = Uint32.decode(stream);
    decodedTopologyResponseBodyV0.totalOutboundPeerCount = Uint32.decode(stream);
    return decodedTopologyResponseBodyV0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.inboundPeers,
        this.outboundPeers,
        this.totalInboundPeerCount,
        this.totalOutboundPeerCount);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof TopologyResponseBodyV0)) {
      return false;
    }

    TopologyResponseBodyV0 other = (TopologyResponseBodyV0) object;
    return Objects.equals(this.inboundPeers, other.inboundPeers)
        && Objects.equals(this.outboundPeers, other.outboundPeers)
        && Objects.equals(this.totalInboundPeerCount, other.totalInboundPeerCount)
        && Objects.equals(this.totalOutboundPeerCount, other.totalOutboundPeerCount);
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

  public static TopologyResponseBodyV0 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TopologyResponseBodyV0 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private PeerStatList inboundPeers;
    private PeerStatList outboundPeers;
    private Uint32 totalInboundPeerCount;
    private Uint32 totalOutboundPeerCount;

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

    public TopologyResponseBodyV0 build() {
      TopologyResponseBodyV0 val = new TopologyResponseBodyV0();
      val.setInboundPeers(this.inboundPeers);
      val.setOutboundPeers(this.outboundPeers);
      val.setTotalInboundPeerCount(this.totalInboundPeerCount);
      val.setTotalOutboundPeerCount(this.totalOutboundPeerCount);
      return val;
    }
  }
}
