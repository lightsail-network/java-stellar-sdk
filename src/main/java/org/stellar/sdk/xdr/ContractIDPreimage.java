// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.stellar.sdk.Base64Factory;

/**
 * ContractIDPreimage's original definition in the XDR file is:
 *
 * <pre>
 * union ContractIDPreimage switch (ContractIDPreimageType type)
 * {
 * case CONTRACT_ID_PREIMAGE_FROM_ADDRESS:
 *     struct
 *     {
 *         SCAddress address;
 *         uint256 salt;
 *     } fromAddress;
 * case CONTRACT_ID_PREIMAGE_FROM_ASSET:
 *     Asset fromAsset;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ContractIDPreimage implements XdrElement {
  private ContractIDPreimageType discriminant;
  private ContractIDPreimageFromAddress fromAddress;
  private Asset fromAsset;

  public static void encode(
      XdrDataOutputStream stream, ContractIDPreimage encodedContractIDPreimage) throws IOException {
    // Xdrgen::AST::Identifier
    // ContractIDPreimageType
    stream.writeInt(encodedContractIDPreimage.getDiscriminant().getValue());
    switch (encodedContractIDPreimage.getDiscriminant()) {
      case CONTRACT_ID_PREIMAGE_FROM_ADDRESS:
        ContractIDPreimageFromAddress.encode(stream, encodedContractIDPreimage.fromAddress);
        break;
      case CONTRACT_ID_PREIMAGE_FROM_ASSET:
        Asset.encode(stream, encodedContractIDPreimage.fromAsset);
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static ContractIDPreimage decode(XdrDataInputStream stream) throws IOException {
    ContractIDPreimage decodedContractIDPreimage = new ContractIDPreimage();
    ContractIDPreimageType discriminant = ContractIDPreimageType.decode(stream);
    decodedContractIDPreimage.setDiscriminant(discriminant);
    switch (decodedContractIDPreimage.getDiscriminant()) {
      case CONTRACT_ID_PREIMAGE_FROM_ADDRESS:
        decodedContractIDPreimage.fromAddress = ContractIDPreimageFromAddress.decode(stream);
        break;
      case CONTRACT_ID_PREIMAGE_FROM_ASSET:
        decodedContractIDPreimage.fromAsset = Asset.decode(stream);
        break;
    }
    return decodedContractIDPreimage;
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

  public static ContractIDPreimage fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ContractIDPreimage fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  /**
   * ContractIDPreimageFromAddress's original definition in the XDR file is:
   *
   * <pre>
   * struct
   *     {
   *         SCAddress address;
   *         uint256 salt;
   *     }
   * </pre>
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder(toBuilder = true)
  public static class ContractIDPreimageFromAddress implements XdrElement {
    private SCAddress address;
    private Uint256 salt;

    public static void encode(
        XdrDataOutputStream stream,
        ContractIDPreimageFromAddress encodedContractIDPreimageFromAddress)
        throws IOException {
      SCAddress.encode(stream, encodedContractIDPreimageFromAddress.address);
      Uint256.encode(stream, encodedContractIDPreimageFromAddress.salt);
    }

    public void encode(XdrDataOutputStream stream) throws IOException {
      encode(stream, this);
    }

    public static ContractIDPreimageFromAddress decode(XdrDataInputStream stream)
        throws IOException {
      ContractIDPreimageFromAddress decodedContractIDPreimageFromAddress =
          new ContractIDPreimageFromAddress();
      decodedContractIDPreimageFromAddress.address = SCAddress.decode(stream);
      decodedContractIDPreimageFromAddress.salt = Uint256.decode(stream);
      return decodedContractIDPreimageFromAddress;
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

    public static ContractIDPreimageFromAddress fromXdrBase64(String xdr) throws IOException {
      byte[] bytes = Base64Factory.getInstance().decode(xdr);
      return fromXdrByteArray(bytes);
    }

    public static ContractIDPreimageFromAddress fromXdrByteArray(byte[] xdr) throws IOException {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
      XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
      return decode(xdrDataInputStream);
    }
  }
}
