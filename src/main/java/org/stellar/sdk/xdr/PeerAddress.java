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
 * PeerAddress's original definition in the XDR file is:
 *
 * <pre>
 * struct PeerAddress
 * {
 *     union switch (IPAddrType type)
 *     {
 *     case IPv4:
 *         opaque ipv4[4];
 *     case IPv6:
 *         opaque ipv6[16];
 *     }
 *     ip;
 *     uint32 port;
 *     uint32 numFailures;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PeerAddress implements XdrElement {
  private PeerAddressIp ip;
  private Uint32 port;
  private Uint32 numFailures;

  public void encode(XdrDataOutputStream stream) throws IOException {
    ip.encode(stream);
    port.encode(stream);
    numFailures.encode(stream);
  }

  public static PeerAddress decode(XdrDataInputStream stream) throws IOException {
    PeerAddress decodedPeerAddress = new PeerAddress();
    decodedPeerAddress.ip = PeerAddressIp.decode(stream);
    decodedPeerAddress.port = Uint32.decode(stream);
    decodedPeerAddress.numFailures = Uint32.decode(stream);
    return decodedPeerAddress;
  }

  public static PeerAddress fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static PeerAddress fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  /**
   * PeerAddressIp's original definition in the XDR file is:
   *
   * <pre>
   * union switch (IPAddrType type)
   *     {
   *     case IPv4:
   *         opaque ipv4[4];
   *     case IPv6:
   *         opaque ipv6[16];
   *     }
   * </pre>
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder(toBuilder = true)
  public static class PeerAddressIp implements XdrElement {
    private IPAddrType discriminant;
    private byte[] ipv4;
    private byte[] ipv6;

    public void encode(XdrDataOutputStream stream) throws IOException {
      stream.writeInt(discriminant.getValue());
      switch (discriminant) {
        case IPv4:
          int ipv4Size = ipv4.length;
          stream.write(getIpv4(), 0, ipv4Size);
          break;
        case IPv6:
          int ipv6Size = ipv6.length;
          stream.write(getIpv6(), 0, ipv6Size);
          break;
      }
    }

    public static PeerAddressIp decode(XdrDataInputStream stream) throws IOException {
      PeerAddressIp decodedPeerAddressIp = new PeerAddressIp();
      IPAddrType discriminant = IPAddrType.decode(stream);
      decodedPeerAddressIp.setDiscriminant(discriminant);
      switch (decodedPeerAddressIp.getDiscriminant()) {
        case IPv4:
          int ipv4Size = 4;
          decodedPeerAddressIp.ipv4 = new byte[ipv4Size];
          stream.read(decodedPeerAddressIp.ipv4, 0, ipv4Size);
          break;
        case IPv6:
          int ipv6Size = 16;
          decodedPeerAddressIp.ipv6 = new byte[ipv6Size];
          stream.read(decodedPeerAddressIp.ipv6, 0, ipv6Size);
          break;
      }
      return decodedPeerAddressIp;
    }

    public static PeerAddressIp fromXdrBase64(String xdr) throws IOException {
      byte[] bytes = Base64Factory.getInstance().decode(xdr);
      return fromXdrByteArray(bytes);
    }

    public static PeerAddressIp fromXdrByteArray(byte[] xdr) throws IOException {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
      XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
      return decode(xdrDataInputStream);
    }
  }
}
