// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

// === xdr source ============================================================

//  union StellarMessage switch (MessageType type)
//  {
//  case ERROR_MSG:
//      Error error;
//  case HELLO:
//      Hello hello;
//  case AUTH:
//      Auth auth;
//  case DONT_HAVE:
//      DontHave dontHave;
//  case GET_PEERS:
//      void;
//  case PEERS:
//      PeerAddress peers<100>;
//
//  case GET_TX_SET:
//      uint256 txSetHash;
//  case TX_SET:
//      TransactionSet txSet;
//  case GENERALIZED_TX_SET:
//      GeneralizedTransactionSet generalizedTxSet;
//
//  case TRANSACTION:
//      TransactionEnvelope transaction;
//
//  case SURVEY_REQUEST:
//      SignedSurveyRequestMessage signedSurveyRequestMessage;
//
//  case SURVEY_RESPONSE:
//      SignedSurveyResponseMessage signedSurveyResponseMessage;
//
//  // SCP
//  case GET_SCP_QUORUMSET:
//      uint256 qSetHash;
//  case SCP_QUORUMSET:
//      SCPQuorumSet qSet;
//  case SCP_MESSAGE:
//      SCPEnvelope envelope;
//  case GET_SCP_STATE:
//      uint32 getSCPLedgerSeq; // ledger seq requested ; if 0, requests the latest
//  case SEND_MORE:
//      SendMore sendMoreMessage;
//  case SEND_MORE_EXTENDED:
//      SendMoreExtended sendMoreExtendedMessage;
//  // Pull mode
//  case FLOOD_ADVERT:
//       FloodAdvert floodAdvert;
//  case FLOOD_DEMAND:
//       FloodDemand floodDemand;
//  };

//  ===========================================================================
public class StellarMessage implements XdrElement {
  public StellarMessage() {}

  MessageType type;

  public MessageType getDiscriminant() {
    return this.type;
  }

  public void setDiscriminant(MessageType value) {
    this.type = value;
  }

  private Error error;

  public Error getError() {
    return this.error;
  }

  public void setError(Error value) {
    this.error = value;
  }

  private Hello hello;

  public Hello getHello() {
    return this.hello;
  }

  public void setHello(Hello value) {
    this.hello = value;
  }

  private Auth auth;

  public Auth getAuth() {
    return this.auth;
  }

  public void setAuth(Auth value) {
    this.auth = value;
  }

  private DontHave dontHave;

  public DontHave getDontHave() {
    return this.dontHave;
  }

  public void setDontHave(DontHave value) {
    this.dontHave = value;
  }

  private PeerAddress[] peers;

  public PeerAddress[] getPeers() {
    return this.peers;
  }

  public void setPeers(PeerAddress[] value) {
    this.peers = value;
  }

  private Uint256 txSetHash;

  public Uint256 getTxSetHash() {
    return this.txSetHash;
  }

  public void setTxSetHash(Uint256 value) {
    this.txSetHash = value;
  }

  private TransactionSet txSet;

  public TransactionSet getTxSet() {
    return this.txSet;
  }

  public void setTxSet(TransactionSet value) {
    this.txSet = value;
  }

  private GeneralizedTransactionSet generalizedTxSet;

  public GeneralizedTransactionSet getGeneralizedTxSet() {
    return this.generalizedTxSet;
  }

  public void setGeneralizedTxSet(GeneralizedTransactionSet value) {
    this.generalizedTxSet = value;
  }

  private TransactionEnvelope transaction;

  public TransactionEnvelope getTransaction() {
    return this.transaction;
  }

  public void setTransaction(TransactionEnvelope value) {
    this.transaction = value;
  }

  private SignedSurveyRequestMessage signedSurveyRequestMessage;

  public SignedSurveyRequestMessage getSignedSurveyRequestMessage() {
    return this.signedSurveyRequestMessage;
  }

  public void setSignedSurveyRequestMessage(SignedSurveyRequestMessage value) {
    this.signedSurveyRequestMessage = value;
  }

  private SignedSurveyResponseMessage signedSurveyResponseMessage;

  public SignedSurveyResponseMessage getSignedSurveyResponseMessage() {
    return this.signedSurveyResponseMessage;
  }

  public void setSignedSurveyResponseMessage(SignedSurveyResponseMessage value) {
    this.signedSurveyResponseMessage = value;
  }

  private Uint256 qSetHash;

  public Uint256 getQSetHash() {
    return this.qSetHash;
  }

  public void setQSetHash(Uint256 value) {
    this.qSetHash = value;
  }

  private SCPQuorumSet qSet;

  public SCPQuorumSet getQSet() {
    return this.qSet;
  }

  public void setQSet(SCPQuorumSet value) {
    this.qSet = value;
  }

  private SCPEnvelope envelope;

  public SCPEnvelope getEnvelope() {
    return this.envelope;
  }

  public void setEnvelope(SCPEnvelope value) {
    this.envelope = value;
  }

  private Uint32 getSCPLedgerSeq;

  public Uint32 getGetSCPLedgerSeq() {
    return this.getSCPLedgerSeq;
  }

  public void setGetSCPLedgerSeq(Uint32 value) {
    this.getSCPLedgerSeq = value;
  }

  private SendMore sendMoreMessage;

  public SendMore getSendMoreMessage() {
    return this.sendMoreMessage;
  }

  public void setSendMoreMessage(SendMore value) {
    this.sendMoreMessage = value;
  }

  private SendMoreExtended sendMoreExtendedMessage;

  public SendMoreExtended getSendMoreExtendedMessage() {
    return this.sendMoreExtendedMessage;
  }

  public void setSendMoreExtendedMessage(SendMoreExtended value) {
    this.sendMoreExtendedMessage = value;
  }

  private FloodAdvert floodAdvert;

  public FloodAdvert getFloodAdvert() {
    return this.floodAdvert;
  }

  public void setFloodAdvert(FloodAdvert value) {
    this.floodAdvert = value;
  }

  private FloodDemand floodDemand;

  public FloodDemand getFloodDemand() {
    return this.floodDemand;
  }

  public void setFloodDemand(FloodDemand value) {
    this.floodDemand = value;
  }

  public static final class Builder {
    private MessageType discriminant;
    private Error error;
    private Hello hello;
    private Auth auth;
    private DontHave dontHave;
    private PeerAddress[] peers;
    private Uint256 txSetHash;
    private TransactionSet txSet;
    private GeneralizedTransactionSet generalizedTxSet;
    private TransactionEnvelope transaction;
    private SignedSurveyRequestMessage signedSurveyRequestMessage;
    private SignedSurveyResponseMessage signedSurveyResponseMessage;
    private Uint256 qSetHash;
    private SCPQuorumSet qSet;
    private SCPEnvelope envelope;
    private Uint32 getSCPLedgerSeq;
    private SendMore sendMoreMessage;
    private SendMoreExtended sendMoreExtendedMessage;
    private FloodAdvert floodAdvert;
    private FloodDemand floodDemand;

    public Builder discriminant(MessageType discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public Builder error(Error error) {
      this.error = error;
      return this;
    }

    public Builder hello(Hello hello) {
      this.hello = hello;
      return this;
    }

    public Builder auth(Auth auth) {
      this.auth = auth;
      return this;
    }

    public Builder dontHave(DontHave dontHave) {
      this.dontHave = dontHave;
      return this;
    }

    public Builder peers(PeerAddress[] peers) {
      this.peers = peers;
      return this;
    }

    public Builder txSetHash(Uint256 txSetHash) {
      this.txSetHash = txSetHash;
      return this;
    }

    public Builder txSet(TransactionSet txSet) {
      this.txSet = txSet;
      return this;
    }

    public Builder generalizedTxSet(GeneralizedTransactionSet generalizedTxSet) {
      this.generalizedTxSet = generalizedTxSet;
      return this;
    }

    public Builder transaction(TransactionEnvelope transaction) {
      this.transaction = transaction;
      return this;
    }

    public Builder signedSurveyRequestMessage(
        SignedSurveyRequestMessage signedSurveyRequestMessage) {
      this.signedSurveyRequestMessage = signedSurveyRequestMessage;
      return this;
    }

    public Builder signedSurveyResponseMessage(
        SignedSurveyResponseMessage signedSurveyResponseMessage) {
      this.signedSurveyResponseMessage = signedSurveyResponseMessage;
      return this;
    }

    public Builder qSetHash(Uint256 qSetHash) {
      this.qSetHash = qSetHash;
      return this;
    }

    public Builder qSet(SCPQuorumSet qSet) {
      this.qSet = qSet;
      return this;
    }

    public Builder envelope(SCPEnvelope envelope) {
      this.envelope = envelope;
      return this;
    }

    public Builder getSCPLedgerSeq(Uint32 getSCPLedgerSeq) {
      this.getSCPLedgerSeq = getSCPLedgerSeq;
      return this;
    }

    public Builder sendMoreMessage(SendMore sendMoreMessage) {
      this.sendMoreMessage = sendMoreMessage;
      return this;
    }

    public Builder sendMoreExtendedMessage(SendMoreExtended sendMoreExtendedMessage) {
      this.sendMoreExtendedMessage = sendMoreExtendedMessage;
      return this;
    }

    public Builder floodAdvert(FloodAdvert floodAdvert) {
      this.floodAdvert = floodAdvert;
      return this;
    }

    public Builder floodDemand(FloodDemand floodDemand) {
      this.floodDemand = floodDemand;
      return this;
    }

    public StellarMessage build() {
      StellarMessage val = new StellarMessage();
      val.setDiscriminant(discriminant);
      val.setError(this.error);
      val.setHello(this.hello);
      val.setAuth(this.auth);
      val.setDontHave(this.dontHave);
      val.setPeers(this.peers);
      val.setTxSetHash(this.txSetHash);
      val.setTxSet(this.txSet);
      val.setGeneralizedTxSet(this.generalizedTxSet);
      val.setTransaction(this.transaction);
      val.setSignedSurveyRequestMessage(this.signedSurveyRequestMessage);
      val.setSignedSurveyResponseMessage(this.signedSurveyResponseMessage);
      val.setQSetHash(this.qSetHash);
      val.setQSet(this.qSet);
      val.setEnvelope(this.envelope);
      val.setGetSCPLedgerSeq(this.getSCPLedgerSeq);
      val.setSendMoreMessage(this.sendMoreMessage);
      val.setSendMoreExtendedMessage(this.sendMoreExtendedMessage);
      val.setFloodAdvert(this.floodAdvert);
      val.setFloodDemand(this.floodDemand);
      return val;
    }
  }

  public static void encode(XdrDataOutputStream stream, StellarMessage encodedStellarMessage)
      throws IOException {
    // Xdrgen::AST::Identifier
    // MessageType
    stream.writeInt(encodedStellarMessage.getDiscriminant().getValue());
    switch (encodedStellarMessage.getDiscriminant()) {
      case ERROR_MSG:
        Error.encode(stream, encodedStellarMessage.error);
        break;
      case HELLO:
        Hello.encode(stream, encodedStellarMessage.hello);
        break;
      case AUTH:
        Auth.encode(stream, encodedStellarMessage.auth);
        break;
      case DONT_HAVE:
        DontHave.encode(stream, encodedStellarMessage.dontHave);
        break;
      case GET_PEERS:
        break;
      case PEERS:
        int peerssize = encodedStellarMessage.getPeers().length;
        stream.writeInt(peerssize);
        for (int i = 0; i < peerssize; i++) {
          PeerAddress.encode(stream, encodedStellarMessage.peers[i]);
        }
        break;
      case GET_TX_SET:
        Uint256.encode(stream, encodedStellarMessage.txSetHash);
        break;
      case TX_SET:
        TransactionSet.encode(stream, encodedStellarMessage.txSet);
        break;
      case GENERALIZED_TX_SET:
        GeneralizedTransactionSet.encode(stream, encodedStellarMessage.generalizedTxSet);
        break;
      case TRANSACTION:
        TransactionEnvelope.encode(stream, encodedStellarMessage.transaction);
        break;
      case SURVEY_REQUEST:
        SignedSurveyRequestMessage.encode(stream, encodedStellarMessage.signedSurveyRequestMessage);
        break;
      case SURVEY_RESPONSE:
        SignedSurveyResponseMessage.encode(
            stream, encodedStellarMessage.signedSurveyResponseMessage);
        break;
      case GET_SCP_QUORUMSET:
        Uint256.encode(stream, encodedStellarMessage.qSetHash);
        break;
      case SCP_QUORUMSET:
        SCPQuorumSet.encode(stream, encodedStellarMessage.qSet);
        break;
      case SCP_MESSAGE:
        SCPEnvelope.encode(stream, encodedStellarMessage.envelope);
        break;
      case GET_SCP_STATE:
        Uint32.encode(stream, encodedStellarMessage.getSCPLedgerSeq);
        break;
      case SEND_MORE:
        SendMore.encode(stream, encodedStellarMessage.sendMoreMessage);
        break;
      case SEND_MORE_EXTENDED:
        SendMoreExtended.encode(stream, encodedStellarMessage.sendMoreExtendedMessage);
        break;
      case FLOOD_ADVERT:
        FloodAdvert.encode(stream, encodedStellarMessage.floodAdvert);
        break;
      case FLOOD_DEMAND:
        FloodDemand.encode(stream, encodedStellarMessage.floodDemand);
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static StellarMessage decode(XdrDataInputStream stream) throws IOException {
    StellarMessage decodedStellarMessage = new StellarMessage();
    MessageType discriminant = MessageType.decode(stream);
    decodedStellarMessage.setDiscriminant(discriminant);
    switch (decodedStellarMessage.getDiscriminant()) {
      case ERROR_MSG:
        decodedStellarMessage.error = Error.decode(stream);
        break;
      case HELLO:
        decodedStellarMessage.hello = Hello.decode(stream);
        break;
      case AUTH:
        decodedStellarMessage.auth = Auth.decode(stream);
        break;
      case DONT_HAVE:
        decodedStellarMessage.dontHave = DontHave.decode(stream);
        break;
      case GET_PEERS:
        break;
      case PEERS:
        int peerssize = stream.readInt();
        decodedStellarMessage.peers = new PeerAddress[peerssize];
        for (int i = 0; i < peerssize; i++) {
          decodedStellarMessage.peers[i] = PeerAddress.decode(stream);
        }
        break;
      case GET_TX_SET:
        decodedStellarMessage.txSetHash = Uint256.decode(stream);
        break;
      case TX_SET:
        decodedStellarMessage.txSet = TransactionSet.decode(stream);
        break;
      case GENERALIZED_TX_SET:
        decodedStellarMessage.generalizedTxSet = GeneralizedTransactionSet.decode(stream);
        break;
      case TRANSACTION:
        decodedStellarMessage.transaction = TransactionEnvelope.decode(stream);
        break;
      case SURVEY_REQUEST:
        decodedStellarMessage.signedSurveyRequestMessage =
            SignedSurveyRequestMessage.decode(stream);
        break;
      case SURVEY_RESPONSE:
        decodedStellarMessage.signedSurveyResponseMessage =
            SignedSurveyResponseMessage.decode(stream);
        break;
      case GET_SCP_QUORUMSET:
        decodedStellarMessage.qSetHash = Uint256.decode(stream);
        break;
      case SCP_QUORUMSET:
        decodedStellarMessage.qSet = SCPQuorumSet.decode(stream);
        break;
      case SCP_MESSAGE:
        decodedStellarMessage.envelope = SCPEnvelope.decode(stream);
        break;
      case GET_SCP_STATE:
        decodedStellarMessage.getSCPLedgerSeq = Uint32.decode(stream);
        break;
      case SEND_MORE:
        decodedStellarMessage.sendMoreMessage = SendMore.decode(stream);
        break;
      case SEND_MORE_EXTENDED:
        decodedStellarMessage.sendMoreExtendedMessage = SendMoreExtended.decode(stream);
        break;
      case FLOOD_ADVERT:
        decodedStellarMessage.floodAdvert = FloodAdvert.decode(stream);
        break;
      case FLOOD_DEMAND:
        decodedStellarMessage.floodDemand = FloodDemand.decode(stream);
        break;
    }
    return decodedStellarMessage;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.error,
        this.hello,
        this.auth,
        this.dontHave,
        Arrays.hashCode(this.peers),
        this.txSetHash,
        this.txSet,
        this.generalizedTxSet,
        this.transaction,
        this.signedSurveyRequestMessage,
        this.signedSurveyResponseMessage,
        this.qSetHash,
        this.qSet,
        this.envelope,
        this.getSCPLedgerSeq,
        this.sendMoreMessage,
        this.sendMoreExtendedMessage,
        this.floodAdvert,
        this.floodDemand,
        this.type);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof StellarMessage)) {
      return false;
    }

    StellarMessage other = (StellarMessage) object;
    return Objects.equals(this.error, other.error)
        && Objects.equals(this.hello, other.hello)
        && Objects.equals(this.auth, other.auth)
        && Objects.equals(this.dontHave, other.dontHave)
        && Arrays.equals(this.peers, other.peers)
        && Objects.equals(this.txSetHash, other.txSetHash)
        && Objects.equals(this.txSet, other.txSet)
        && Objects.equals(this.generalizedTxSet, other.generalizedTxSet)
        && Objects.equals(this.transaction, other.transaction)
        && Objects.equals(this.signedSurveyRequestMessage, other.signedSurveyRequestMessage)
        && Objects.equals(this.signedSurveyResponseMessage, other.signedSurveyResponseMessage)
        && Objects.equals(this.qSetHash, other.qSetHash)
        && Objects.equals(this.qSet, other.qSet)
        && Objects.equals(this.envelope, other.envelope)
        && Objects.equals(this.getSCPLedgerSeq, other.getSCPLedgerSeq)
        && Objects.equals(this.sendMoreMessage, other.sendMoreMessage)
        && Objects.equals(this.sendMoreExtendedMessage, other.sendMoreExtendedMessage)
        && Objects.equals(this.floodAdvert, other.floodAdvert)
        && Objects.equals(this.floodDemand, other.floodDemand)
        && Objects.equals(this.type, other.type);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64.getEncoder().encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static StellarMessage fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static StellarMessage fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
