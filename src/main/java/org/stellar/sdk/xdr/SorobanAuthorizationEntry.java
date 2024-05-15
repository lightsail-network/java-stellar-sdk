// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.stellar.sdk.Base64Factory;

/**
 * SorobanAuthorizationEntry's original definition in the XDR file is:
 *
 * <pre>
 * struct SorobanAuthorizationEntry
 * {
 *     SorobanCredentials credentials;
 *     SorobanAuthorizedInvocation rootInvocation;
 * };
 * </pre>
 */
public class SorobanAuthorizationEntry implements XdrElement {
  public SorobanAuthorizationEntry() {}

  private SorobanCredentials credentials;

  public SorobanCredentials getCredentials() {
    return this.credentials;
  }

  public void setCredentials(SorobanCredentials value) {
    this.credentials = value;
  }

  private SorobanAuthorizedInvocation rootInvocation;

  public SorobanAuthorizedInvocation getRootInvocation() {
    return this.rootInvocation;
  }

  public void setRootInvocation(SorobanAuthorizedInvocation value) {
    this.rootInvocation = value;
  }

  public static void encode(
      XdrDataOutputStream stream, SorobanAuthorizationEntry encodedSorobanAuthorizationEntry)
      throws IOException {
    SorobanCredentials.encode(stream, encodedSorobanAuthorizationEntry.credentials);
    SorobanAuthorizedInvocation.encode(stream, encodedSorobanAuthorizationEntry.rootInvocation);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SorobanAuthorizationEntry decode(XdrDataInputStream stream) throws IOException {
    SorobanAuthorizationEntry decodedSorobanAuthorizationEntry = new SorobanAuthorizationEntry();
    decodedSorobanAuthorizationEntry.credentials = SorobanCredentials.decode(stream);
    decodedSorobanAuthorizationEntry.rootInvocation = SorobanAuthorizedInvocation.decode(stream);
    return decodedSorobanAuthorizationEntry;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.credentials, this.rootInvocation);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SorobanAuthorizationEntry)) {
      return false;
    }

    SorobanAuthorizationEntry other = (SorobanAuthorizationEntry) object;
    return Objects.equals(this.credentials, other.credentials)
        && Objects.equals(this.rootInvocation, other.rootInvocation);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64Factory.getInstance().encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static SorobanAuthorizationEntry fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SorobanAuthorizationEntry fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private SorobanCredentials credentials;
    private SorobanAuthorizedInvocation rootInvocation;

    public Builder credentials(SorobanCredentials credentials) {
      this.credentials = credentials;
      return this;
    }

    public Builder rootInvocation(SorobanAuthorizedInvocation rootInvocation) {
      this.rootInvocation = rootInvocation;
      return this;
    }

    public SorobanAuthorizationEntry build() {
      SorobanAuthorizationEntry val = new SorobanAuthorizationEntry();
      val.setCredentials(this.credentials);
      val.setRootInvocation(this.rootInvocation);
      return val;
    }
  }
}
