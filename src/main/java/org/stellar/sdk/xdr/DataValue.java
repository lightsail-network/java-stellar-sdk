// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import org.stellar.sdk.Base64Factory;

// === xdr source ============================================================

//  typedef opaque DataValue<64>;

//  ===========================================================================
public class DataValue implements XdrElement {
  private byte[] DataValue;

  public DataValue() {}

  public DataValue(byte[] DataValue) {
    this.DataValue = DataValue;
  }

  public byte[] getDataValue() {
    return this.DataValue;
  }

  public void setDataValue(byte[] value) {
    this.DataValue = value;
  }

  public static void encode(XdrDataOutputStream stream, DataValue encodedDataValue)
      throws IOException {
    int DataValuesize = encodedDataValue.DataValue.length;
    stream.writeInt(DataValuesize);
    stream.write(encodedDataValue.getDataValue(), 0, DataValuesize);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static DataValue decode(XdrDataInputStream stream) throws IOException {
    DataValue decodedDataValue = new DataValue();
    int DataValuesize = stream.readInt();
    decodedDataValue.DataValue = new byte[DataValuesize];
    stream.read(decodedDataValue.DataValue, 0, DataValuesize);
    return decodedDataValue;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.DataValue);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof DataValue)) {
      return false;
    }

    DataValue other = (DataValue) object;
    return Arrays.equals(this.DataValue, other.DataValue);
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

  public static DataValue fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static DataValue fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
