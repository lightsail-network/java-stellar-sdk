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
 * TimeBounds's original definition in the XDR file is:
 *
 * <pre>
 * struct TimeBounds
 * {
 *     TimePoint minTime;
 *     TimePoint maxTime; // 0 here means no maxTime
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TimeBounds implements XdrElement {
  private TimePoint minTime;
  private TimePoint maxTime;

  public void encode(XdrDataOutputStream stream) throws IOException {
    minTime.encode(stream);
    maxTime.encode(stream);
  }

  public static TimeBounds decode(XdrDataInputStream stream) throws IOException {
    TimeBounds decodedTimeBounds = new TimeBounds();
    decodedTimeBounds.minTime = TimePoint.decode(stream);
    decodedTimeBounds.maxTime = TimePoint.decode(stream);
    return decodedTimeBounds;
  }

  public static TimeBounds fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TimeBounds fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
