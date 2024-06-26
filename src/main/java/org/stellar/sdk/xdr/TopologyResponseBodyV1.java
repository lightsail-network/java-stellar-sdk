// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.stellar.sdk.Base64Factory;

/**
 * TopologyResponseBodyV1's original definition in the XDR file is:
 *
 * <pre>
 * struct TopologyResponseBodyV1
 * {
 *     PeerStatList inboundPeers;
 *     PeerStatList outboundPeers;
 *
 *     uint32 totalInboundPeerCount;
 *     uint32 totalOutboundPeerCount;
 *
 *     uint32 maxInboundPeerCount;
 *     uint32 maxOutboundPeerCount;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TopologyResponseBodyV1 implements XdrElement {
  private PeerStatList inboundPeers;
  private PeerStatList outboundPeers;
  private Uint32 totalInboundPeerCount;
  private Uint32 totalOutboundPeerCount;
  private Uint32 maxInboundPeerCount;
  private Uint32 maxOutboundPeerCount;

  public void encode(XdrDataOutputStream stream) throws IOException {
    inboundPeers.encode(stream);
    outboundPeers.encode(stream);
    totalInboundPeerCount.encode(stream);
    totalOutboundPeerCount.encode(stream);
    maxInboundPeerCount.encode(stream);
    maxOutboundPeerCount.encode(stream);
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

  public static TopologyResponseBodyV1 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TopologyResponseBodyV1 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
