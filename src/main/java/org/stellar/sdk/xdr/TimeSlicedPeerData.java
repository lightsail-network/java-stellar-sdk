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
 * TimeSlicedPeerData's original definition in the XDR file is:
 *
 * <pre>
 * struct TimeSlicedPeerData
 * {
 *     PeerStats peerStats;
 *     uint32 averageLatencyMs;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TimeSlicedPeerData implements XdrElement {
  private PeerStats peerStats;
  private Uint32 averageLatencyMs;

  public void encode(XdrDataOutputStream stream) throws IOException {
    peerStats.encode(stream);
    averageLatencyMs.encode(stream);
  }

  public static TimeSlicedPeerData decode(XdrDataInputStream stream) throws IOException {
    TimeSlicedPeerData decodedTimeSlicedPeerData = new TimeSlicedPeerData();
    decodedTimeSlicedPeerData.peerStats = PeerStats.decode(stream);
    decodedTimeSlicedPeerData.averageLatencyMs = Uint32.decode(stream);
    return decodedTimeSlicedPeerData;
  }

  public static TimeSlicedPeerData fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TimeSlicedPeerData fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}