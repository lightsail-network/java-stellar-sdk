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
 * TopologyResponseBodyV2's original definition in the XDR file is:
 *
 * <pre>
 * struct TopologyResponseBodyV2
 * {
 *     TimeSlicedPeerDataList inboundPeers;
 *     TimeSlicedPeerDataList outboundPeers;
 *     TimeSlicedNodeData nodeData;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TopologyResponseBodyV2 implements XdrElement {
  private TimeSlicedPeerDataList inboundPeers;
  private TimeSlicedPeerDataList outboundPeers;
  private TimeSlicedNodeData nodeData;

  public void encode(XdrDataOutputStream stream) throws IOException {
    inboundPeers.encode(stream);
    outboundPeers.encode(stream);
    nodeData.encode(stream);
  }

  public static TopologyResponseBodyV2 decode(XdrDataInputStream stream) throws IOException {
    TopologyResponseBodyV2 decodedTopologyResponseBodyV2 = new TopologyResponseBodyV2();
    decodedTopologyResponseBodyV2.inboundPeers = TimeSlicedPeerDataList.decode(stream);
    decodedTopologyResponseBodyV2.outboundPeers = TimeSlicedPeerDataList.decode(stream);
    decodedTopologyResponseBodyV2.nodeData = TimeSlicedNodeData.decode(stream);
    return decodedTopologyResponseBodyV2;
  }

  public static TopologyResponseBodyV2 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TopologyResponseBodyV2 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
