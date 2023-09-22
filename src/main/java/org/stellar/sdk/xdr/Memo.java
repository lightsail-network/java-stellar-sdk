// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.stellar.sdk.Base64;

// === xdr source ============================================================

//  union Memo switch (MemoType type)
//  {
//  case MEMO_NONE:
//      void;
//  case MEMO_TEXT:
//      string text<28>;
//  case MEMO_ID:
//      uint64 id;
//  case MEMO_HASH:
//      Hash hash; // the hash of what to pull from the content server
//  case MEMO_RETURN:
//      Hash retHash; // the hash of the tx you are rejecting
//  };

//  ===========================================================================
public class Memo implements XdrElement {
  public Memo() {}

  MemoType type;

  public MemoType getDiscriminant() {
    return this.type;
  }

  public void setDiscriminant(MemoType value) {
    this.type = value;
  }

  private XdrString text;

  public XdrString getText() {
    return this.text;
  }

  public void setText(XdrString value) {
    this.text = value;
  }

  private Uint64 id;

  public Uint64 getId() {
    return this.id;
  }

  public void setId(Uint64 value) {
    this.id = value;
  }

  private Hash hash;

  public Hash getHash() {
    return this.hash;
  }

  public void setHash(Hash value) {
    this.hash = value;
  }

  private Hash retHash;

  public Hash getRetHash() {
    return this.retHash;
  }

  public void setRetHash(Hash value) {
    this.retHash = value;
  }

  public static final class Builder {
    private MemoType discriminant;
    private XdrString text;
    private Uint64 id;
    private Hash hash;
    private Hash retHash;

    public Builder discriminant(MemoType discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public Builder text(XdrString text) {
      this.text = text;
      return this;
    }

    public Builder id(Uint64 id) {
      this.id = id;
      return this;
    }

    public Builder hash(Hash hash) {
      this.hash = hash;
      return this;
    }

    public Builder retHash(Hash retHash) {
      this.retHash = retHash;
      return this;
    }

    public Memo build() {
      Memo val = new Memo();
      val.setDiscriminant(discriminant);
      val.setText(this.text);
      val.setId(this.id);
      val.setHash(this.hash);
      val.setRetHash(this.retHash);
      return val;
    }
  }

  public static void encode(XdrDataOutputStream stream, Memo encodedMemo) throws IOException {
    // Xdrgen::AST::Identifier
    // MemoType
    stream.writeInt(encodedMemo.getDiscriminant().getValue());
    switch (encodedMemo.getDiscriminant()) {
      case MEMO_NONE:
        break;
      case MEMO_TEXT:
        encodedMemo.text.encode(stream);
        break;
      case MEMO_ID:
        Uint64.encode(stream, encodedMemo.id);
        break;
      case MEMO_HASH:
        Hash.encode(stream, encodedMemo.hash);
        break;
      case MEMO_RETURN:
        Hash.encode(stream, encodedMemo.retHash);
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static Memo decode(XdrDataInputStream stream) throws IOException {
    Memo decodedMemo = new Memo();
    MemoType discriminant = MemoType.decode(stream);
    decodedMemo.setDiscriminant(discriminant);
    switch (decodedMemo.getDiscriminant()) {
      case MEMO_NONE:
        break;
      case MEMO_TEXT:
        decodedMemo.text = XdrString.decode(stream, 28);
        break;
      case MEMO_ID:
        decodedMemo.id = Uint64.decode(stream);
        break;
      case MEMO_HASH:
        decodedMemo.hash = Hash.decode(stream);
        break;
      case MEMO_RETURN:
        decodedMemo.retHash = Hash.decode(stream);
        break;
    }
    return decodedMemo;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.text, this.id, this.hash, this.retHash, this.type);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Memo)) {
      return false;
    }

    Memo other = (Memo) object;
    return Objects.equals(this.text, other.text)
        && Objects.equals(this.id, other.id)
        && Objects.equals(this.hash, other.hash)
        && Objects.equals(this.retHash, other.retHash)
        && Objects.equals(this.type, other.type);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64.encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static Memo fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static Memo fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
