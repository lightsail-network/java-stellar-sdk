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
 * SCSpecTypeDef's original definition in the XDR file is:
 *
 * <pre>
 * union SCSpecTypeDef switch (SCSpecType type)
 * {
 * case SC_SPEC_TYPE_VAL:
 * case SC_SPEC_TYPE_BOOL:
 * case SC_SPEC_TYPE_VOID:
 * case SC_SPEC_TYPE_ERROR:
 * case SC_SPEC_TYPE_U32:
 * case SC_SPEC_TYPE_I32:
 * case SC_SPEC_TYPE_U64:
 * case SC_SPEC_TYPE_I64:
 * case SC_SPEC_TYPE_TIMEPOINT:
 * case SC_SPEC_TYPE_DURATION:
 * case SC_SPEC_TYPE_U128:
 * case SC_SPEC_TYPE_I128:
 * case SC_SPEC_TYPE_U256:
 * case SC_SPEC_TYPE_I256:
 * case SC_SPEC_TYPE_BYTES:
 * case SC_SPEC_TYPE_STRING:
 * case SC_SPEC_TYPE_SYMBOL:
 * case SC_SPEC_TYPE_ADDRESS:
 *     void;
 * case SC_SPEC_TYPE_OPTION:
 *     SCSpecTypeOption option;
 * case SC_SPEC_TYPE_RESULT:
 *     SCSpecTypeResult result;
 * case SC_SPEC_TYPE_VEC:
 *     SCSpecTypeVec vec;
 * case SC_SPEC_TYPE_MAP:
 *     SCSpecTypeMap map;
 * case SC_SPEC_TYPE_TUPLE:
 *     SCSpecTypeTuple tuple;
 * case SC_SPEC_TYPE_BYTES_N:
 *     SCSpecTypeBytesN bytesN;
 * case SC_SPEC_TYPE_UDT:
 *     SCSpecTypeUDT udt;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SCSpecTypeDef implements XdrElement {
  private SCSpecType discriminant;
  private SCSpecTypeOption option;
  private SCSpecTypeResult result;
  private SCSpecTypeVec vec;
  private SCSpecTypeMap map;
  private SCSpecTypeTuple tuple;
  private SCSpecTypeBytesN bytesN;
  private SCSpecTypeUDT udt;

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(discriminant.getValue());
    switch (discriminant) {
      case SC_SPEC_TYPE_VAL:
      case SC_SPEC_TYPE_BOOL:
      case SC_SPEC_TYPE_VOID:
      case SC_SPEC_TYPE_ERROR:
      case SC_SPEC_TYPE_U32:
      case SC_SPEC_TYPE_I32:
      case SC_SPEC_TYPE_U64:
      case SC_SPEC_TYPE_I64:
      case SC_SPEC_TYPE_TIMEPOINT:
      case SC_SPEC_TYPE_DURATION:
      case SC_SPEC_TYPE_U128:
      case SC_SPEC_TYPE_I128:
      case SC_SPEC_TYPE_U256:
      case SC_SPEC_TYPE_I256:
      case SC_SPEC_TYPE_BYTES:
      case SC_SPEC_TYPE_STRING:
      case SC_SPEC_TYPE_SYMBOL:
      case SC_SPEC_TYPE_ADDRESS:
        break;
      case SC_SPEC_TYPE_OPTION:
        option.encode(stream);
        break;
      case SC_SPEC_TYPE_RESULT:
        result.encode(stream);
        break;
      case SC_SPEC_TYPE_VEC:
        vec.encode(stream);
        break;
      case SC_SPEC_TYPE_MAP:
        map.encode(stream);
        break;
      case SC_SPEC_TYPE_TUPLE:
        tuple.encode(stream);
        break;
      case SC_SPEC_TYPE_BYTES_N:
        bytesN.encode(stream);
        break;
      case SC_SPEC_TYPE_UDT:
        udt.encode(stream);
        break;
    }
  }

  public static SCSpecTypeDef decode(XdrDataInputStream stream) throws IOException {
    SCSpecTypeDef decodedSCSpecTypeDef = new SCSpecTypeDef();
    SCSpecType discriminant = SCSpecType.decode(stream);
    decodedSCSpecTypeDef.setDiscriminant(discriminant);
    switch (decodedSCSpecTypeDef.getDiscriminant()) {
      case SC_SPEC_TYPE_VAL:
      case SC_SPEC_TYPE_BOOL:
      case SC_SPEC_TYPE_VOID:
      case SC_SPEC_TYPE_ERROR:
      case SC_SPEC_TYPE_U32:
      case SC_SPEC_TYPE_I32:
      case SC_SPEC_TYPE_U64:
      case SC_SPEC_TYPE_I64:
      case SC_SPEC_TYPE_TIMEPOINT:
      case SC_SPEC_TYPE_DURATION:
      case SC_SPEC_TYPE_U128:
      case SC_SPEC_TYPE_I128:
      case SC_SPEC_TYPE_U256:
      case SC_SPEC_TYPE_I256:
      case SC_SPEC_TYPE_BYTES:
      case SC_SPEC_TYPE_STRING:
      case SC_SPEC_TYPE_SYMBOL:
      case SC_SPEC_TYPE_ADDRESS:
        break;
      case SC_SPEC_TYPE_OPTION:
        decodedSCSpecTypeDef.option = SCSpecTypeOption.decode(stream);
        break;
      case SC_SPEC_TYPE_RESULT:
        decodedSCSpecTypeDef.result = SCSpecTypeResult.decode(stream);
        break;
      case SC_SPEC_TYPE_VEC:
        decodedSCSpecTypeDef.vec = SCSpecTypeVec.decode(stream);
        break;
      case SC_SPEC_TYPE_MAP:
        decodedSCSpecTypeDef.map = SCSpecTypeMap.decode(stream);
        break;
      case SC_SPEC_TYPE_TUPLE:
        decodedSCSpecTypeDef.tuple = SCSpecTypeTuple.decode(stream);
        break;
      case SC_SPEC_TYPE_BYTES_N:
        decodedSCSpecTypeDef.bytesN = SCSpecTypeBytesN.decode(stream);
        break;
      case SC_SPEC_TYPE_UDT:
        decodedSCSpecTypeDef.udt = SCSpecTypeUDT.decode(stream);
        break;
    }
    return decodedSCSpecTypeDef;
  }

  public static SCSpecTypeDef fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SCSpecTypeDef fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
