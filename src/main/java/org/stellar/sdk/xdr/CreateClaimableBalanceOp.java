// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import org.stellar.sdk.Base64Factory;

/**
 * CreateClaimableBalanceOp's original definition in the XDR file is:
 *
 * <pre>
 * struct CreateClaimableBalanceOp
 * {
 *     Asset asset;
 *     int64 amount;
 *     Claimant claimants&lt;10&gt;;
 * };
 * </pre>
 */
public class CreateClaimableBalanceOp implements XdrElement {
  public CreateClaimableBalanceOp() {}

  private Asset asset;

  public Asset getAsset() {
    return this.asset;
  }

  public void setAsset(Asset value) {
    this.asset = value;
  }

  private Int64 amount;

  public Int64 getAmount() {
    return this.amount;
  }

  public void setAmount(Int64 value) {
    this.amount = value;
  }

  private Claimant[] claimants;

  public Claimant[] getClaimants() {
    return this.claimants;
  }

  public void setClaimants(Claimant[] value) {
    this.claimants = value;
  }

  public static void encode(
      XdrDataOutputStream stream, CreateClaimableBalanceOp encodedCreateClaimableBalanceOp)
      throws IOException {
    Asset.encode(stream, encodedCreateClaimableBalanceOp.asset);
    Int64.encode(stream, encodedCreateClaimableBalanceOp.amount);
    int claimantssize = encodedCreateClaimableBalanceOp.getClaimants().length;
    stream.writeInt(claimantssize);
    for (int i = 0; i < claimantssize; i++) {
      Claimant.encode(stream, encodedCreateClaimableBalanceOp.claimants[i]);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static CreateClaimableBalanceOp decode(XdrDataInputStream stream) throws IOException {
    CreateClaimableBalanceOp decodedCreateClaimableBalanceOp = new CreateClaimableBalanceOp();
    decodedCreateClaimableBalanceOp.asset = Asset.decode(stream);
    decodedCreateClaimableBalanceOp.amount = Int64.decode(stream);
    int claimantssize = stream.readInt();
    decodedCreateClaimableBalanceOp.claimants = new Claimant[claimantssize];
    for (int i = 0; i < claimantssize; i++) {
      decodedCreateClaimableBalanceOp.claimants[i] = Claimant.decode(stream);
    }
    return decodedCreateClaimableBalanceOp;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.asset, this.amount, Arrays.hashCode(this.claimants));
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof CreateClaimableBalanceOp)) {
      return false;
    }

    CreateClaimableBalanceOp other = (CreateClaimableBalanceOp) object;
    return Objects.equals(this.asset, other.asset)
        && Objects.equals(this.amount, other.amount)
        && Arrays.equals(this.claimants, other.claimants);
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

  public static CreateClaimableBalanceOp fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static CreateClaimableBalanceOp fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private Asset asset;
    private Int64 amount;
    private Claimant[] claimants;

    public Builder asset(Asset asset) {
      this.asset = asset;
      return this;
    }

    public Builder amount(Int64 amount) {
      this.amount = amount;
      return this;
    }

    public Builder claimants(Claimant[] claimants) {
      this.claimants = claimants;
      return this;
    }

    public CreateClaimableBalanceOp build() {
      CreateClaimableBalanceOp val = new CreateClaimableBalanceOp();
      val.setAsset(this.asset);
      val.setAmount(this.amount);
      val.setClaimants(this.claimants);
      return val;
    }
  }
}
