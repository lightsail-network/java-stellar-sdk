// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import com.google.common.base.Objects;
import com.google.common.io.BaseEncoding;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

// === xdr source ============================================================

//  struct Hello
//  {
//      uint32 ledgerVersion;
//      uint32 overlayVersion;
//      uint32 overlayMinVersion;
//      Hash networkID;
//      string versionStr<100>;
//      int listeningPort;
//      NodeID peerID;
//      AuthCert cert;
//      uint256 nonce;
//  };

//  ===========================================================================
public class Hello implements XdrElement {
  public Hello() {}

  private Uint32 ledgerVersion;

  public Uint32 getLedgerVersion() {
    return this.ledgerVersion;
  }

  public void setLedgerVersion(Uint32 value) {
    this.ledgerVersion = value;
  }

  private Uint32 overlayVersion;

  public Uint32 getOverlayVersion() {
    return this.overlayVersion;
  }

  public void setOverlayVersion(Uint32 value) {
    this.overlayVersion = value;
  }

  private Uint32 overlayMinVersion;

  public Uint32 getOverlayMinVersion() {
    return this.overlayMinVersion;
  }

  public void setOverlayMinVersion(Uint32 value) {
    this.overlayMinVersion = value;
  }

  private Hash networkID;

  public Hash getNetworkID() {
    return this.networkID;
  }

  public void setNetworkID(Hash value) {
    this.networkID = value;
  }

  private XdrString versionStr;

  public XdrString getVersionStr() {
    return this.versionStr;
  }

  public void setVersionStr(XdrString value) {
    this.versionStr = value;
  }

  private Integer listeningPort;

  public Integer getListeningPort() {
    return this.listeningPort;
  }

  public void setListeningPort(Integer value) {
    this.listeningPort = value;
  }

  private NodeID peerID;

  public NodeID getPeerID() {
    return this.peerID;
  }

  public void setPeerID(NodeID value) {
    this.peerID = value;
  }

  private AuthCert cert;

  public AuthCert getCert() {
    return this.cert;
  }

  public void setCert(AuthCert value) {
    this.cert = value;
  }

  private Uint256 nonce;

  public Uint256 getNonce() {
    return this.nonce;
  }

  public void setNonce(Uint256 value) {
    this.nonce = value;
  }

  public static void encode(XdrDataOutputStream stream, Hello encodedHello) throws IOException {
    Uint32.encode(stream, encodedHello.ledgerVersion);
    Uint32.encode(stream, encodedHello.overlayVersion);
    Uint32.encode(stream, encodedHello.overlayMinVersion);
    Hash.encode(stream, encodedHello.networkID);
    encodedHello.versionStr.encode(stream);
    stream.writeInt(encodedHello.listeningPort);
    NodeID.encode(stream, encodedHello.peerID);
    AuthCert.encode(stream, encodedHello.cert);
    Uint256.encode(stream, encodedHello.nonce);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static Hello decode(XdrDataInputStream stream) throws IOException {
    Hello decodedHello = new Hello();
    decodedHello.ledgerVersion = Uint32.decode(stream);
    decodedHello.overlayVersion = Uint32.decode(stream);
    decodedHello.overlayMinVersion = Uint32.decode(stream);
    decodedHello.networkID = Hash.decode(stream);
    decodedHello.versionStr = XdrString.decode(stream, 100);
    decodedHello.listeningPort = stream.readInt();
    decodedHello.peerID = NodeID.decode(stream);
    decodedHello.cert = AuthCert.decode(stream);
    decodedHello.nonce = Uint256.decode(stream);
    return decodedHello;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(
        this.ledgerVersion,
        this.overlayVersion,
        this.overlayMinVersion,
        this.networkID,
        this.versionStr,
        this.listeningPort,
        this.peerID,
        this.cert,
        this.nonce);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Hello)) {
      return false;
    }

    Hello other = (Hello) object;
    return Objects.equal(this.ledgerVersion, other.ledgerVersion)
        && Objects.equal(this.overlayVersion, other.overlayVersion)
        && Objects.equal(this.overlayMinVersion, other.overlayMinVersion)
        && Objects.equal(this.networkID, other.networkID)
        && Objects.equal(this.versionStr, other.versionStr)
        && Objects.equal(this.listeningPort, other.listeningPort)
        && Objects.equal(this.peerID, other.peerID)
        && Objects.equal(this.cert, other.cert)
        && Objects.equal(this.nonce, other.nonce);
  }

  @Override
  public String toXdrBase64() throws IOException {
    BaseEncoding base64Encoding = BaseEncoding.base64();
    return base64Encoding.encode(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static Hello fromXdrBase64(String xdr) throws IOException {
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] bytes = base64Encoding.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static Hello fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private Uint32 ledgerVersion;
    private Uint32 overlayVersion;
    private Uint32 overlayMinVersion;
    private Hash networkID;
    private XdrString versionStr;
    private Integer listeningPort;
    private NodeID peerID;
    private AuthCert cert;
    private Uint256 nonce;

    public Builder ledgerVersion(Uint32 ledgerVersion) {
      this.ledgerVersion = ledgerVersion;
      return this;
    }

    public Builder overlayVersion(Uint32 overlayVersion) {
      this.overlayVersion = overlayVersion;
      return this;
    }

    public Builder overlayMinVersion(Uint32 overlayMinVersion) {
      this.overlayMinVersion = overlayMinVersion;
      return this;
    }

    public Builder networkID(Hash networkID) {
      this.networkID = networkID;
      return this;
    }

    public Builder versionStr(XdrString versionStr) {
      this.versionStr = versionStr;
      return this;
    }

    public Builder listeningPort(Integer listeningPort) {
      this.listeningPort = listeningPort;
      return this;
    }

    public Builder peerID(NodeID peerID) {
      this.peerID = peerID;
      return this;
    }

    public Builder cert(AuthCert cert) {
      this.cert = cert;
      return this;
    }

    public Builder nonce(Uint256 nonce) {
      this.nonce = nonce;
      return this;
    }

    public Hello build() {
      Hello val = new Hello();
      val.setLedgerVersion(this.ledgerVersion);
      val.setOverlayVersion(this.overlayVersion);
      val.setOverlayMinVersion(this.overlayMinVersion);
      val.setNetworkID(this.networkID);
      val.setVersionStr(this.versionStr);
      val.setListeningPort(this.listeningPort);
      val.setPeerID(this.peerID);
      val.setCert(this.cert);
      val.setNonce(this.nonce);
      return val;
    }
  }
}
