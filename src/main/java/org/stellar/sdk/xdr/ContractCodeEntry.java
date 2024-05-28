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
 * ContractCodeEntry's original definition in the XDR file is:
 *
 * <pre>
 * struct ContractCodeEntry {
 *     union switch (int v)
 *     {
 *         case 0:
 *             void;
 *         case 1:
 *             struct
 *             {
 *                 ExtensionPoint ext;
 *                 ContractCodeCostInputs costInputs;
 *             } v1;
 *     } ext;
 *
 *     Hash hash;
 *     opaque code&lt;&gt;;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ContractCodeEntry implements XdrElement {
  private ContractCodeEntryExt ext;
  private Hash hash;
  private byte[] code;

  public static void encode(XdrDataOutputStream stream, ContractCodeEntry encodedContractCodeEntry)
      throws IOException {
    ContractCodeEntryExt.encode(stream, encodedContractCodeEntry.ext);
    Hash.encode(stream, encodedContractCodeEntry.hash);
    int codeSize = encodedContractCodeEntry.code.length;
    stream.writeInt(codeSize);
    stream.write(encodedContractCodeEntry.getCode(), 0, codeSize);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static ContractCodeEntry decode(XdrDataInputStream stream) throws IOException {
    ContractCodeEntry decodedContractCodeEntry = new ContractCodeEntry();
    decodedContractCodeEntry.ext = ContractCodeEntryExt.decode(stream);
    decodedContractCodeEntry.hash = Hash.decode(stream);
    int codeSize = stream.readInt();
    decodedContractCodeEntry.code = new byte[codeSize];
    stream.read(decodedContractCodeEntry.code, 0, codeSize);
    return decodedContractCodeEntry;
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

  public static ContractCodeEntry fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ContractCodeEntry fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  /**
   * ContractCodeEntryExt's original definition in the XDR file is:
   *
   * <pre>
   * union switch (int v)
   *     {
   *         case 0:
   *             void;
   *         case 1:
   *             struct
   *             {
   *                 ExtensionPoint ext;
   *                 ContractCodeCostInputs costInputs;
   *             } v1;
   *     }
   * </pre>
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder(toBuilder = true)
  public static class ContractCodeEntryExt implements XdrElement {
    private Integer discriminant;
    private ContractCodeEntryV1 v1;

    public static void encode(
        XdrDataOutputStream stream, ContractCodeEntryExt encodedContractCodeEntryExt)
        throws IOException {
      // Xdrgen::AST::Typespecs::Int
      // Integer
      stream.writeInt(encodedContractCodeEntryExt.getDiscriminant().intValue());
      switch (encodedContractCodeEntryExt.getDiscriminant()) {
        case 0:
          break;
        case 1:
          ContractCodeEntryV1.encode(stream, encodedContractCodeEntryExt.v1);
          break;
      }
    }

    public void encode(XdrDataOutputStream stream) throws IOException {
      encode(stream, this);
    }

    public static ContractCodeEntryExt decode(XdrDataInputStream stream) throws IOException {
      ContractCodeEntryExt decodedContractCodeEntryExt = new ContractCodeEntryExt();
      Integer discriminant = stream.readInt();
      decodedContractCodeEntryExt.setDiscriminant(discriminant);
      switch (decodedContractCodeEntryExt.getDiscriminant()) {
        case 0:
          break;
        case 1:
          decodedContractCodeEntryExt.v1 = ContractCodeEntryV1.decode(stream);
          break;
      }
      return decodedContractCodeEntryExt;
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

    public static ContractCodeEntryExt fromXdrBase64(String xdr) throws IOException {
      byte[] bytes = Base64Factory.getInstance().decode(xdr);
      return fromXdrByteArray(bytes);
    }

    public static ContractCodeEntryExt fromXdrByteArray(byte[] xdr) throws IOException {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
      XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
      return decode(xdrDataInputStream);
    }

    /**
     * ContractCodeEntryV1's original definition in the XDR file is:
     *
     * <pre>
     * struct
     *             {
     *                 ExtensionPoint ext;
     *                 ContractCodeCostInputs costInputs;
     *             }
     * </pre>
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class ContractCodeEntryV1 implements XdrElement {
      private ExtensionPoint ext;
      private ContractCodeCostInputs costInputs;

      public static void encode(
          XdrDataOutputStream stream, ContractCodeEntryV1 encodedContractCodeEntryV1)
          throws IOException {
        ExtensionPoint.encode(stream, encodedContractCodeEntryV1.ext);
        ContractCodeCostInputs.encode(stream, encodedContractCodeEntryV1.costInputs);
      }

      public void encode(XdrDataOutputStream stream) throws IOException {
        encode(stream, this);
      }

      public static ContractCodeEntryV1 decode(XdrDataInputStream stream) throws IOException {
        ContractCodeEntryV1 decodedContractCodeEntryV1 = new ContractCodeEntryV1();
        decodedContractCodeEntryV1.ext = ExtensionPoint.decode(stream);
        decodedContractCodeEntryV1.costInputs = ContractCodeCostInputs.decode(stream);
        return decodedContractCodeEntryV1;
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

      public static ContractCodeEntryV1 fromXdrBase64(String xdr) throws IOException {
        byte[] bytes = Base64Factory.getInstance().decode(xdr);
        return fromXdrByteArray(bytes);
      }

      public static ContractCodeEntryV1 fromXdrByteArray(byte[] xdr) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
        XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
        return decode(xdrDataInputStream);
      }
    }
  }
}
