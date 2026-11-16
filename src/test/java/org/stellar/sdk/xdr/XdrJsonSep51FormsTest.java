package org.stellar.sdk.xdr;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import org.junit.Test;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.StrKey;

/**
 * The XDR-JSON forms where the SDK used to differ from SEP-51 (issue #835, item 6). {@link
 * XdrJsonVectorsTest} checks every type against the reference vectors; these tests pin each
 * difference by name, including values the vectors don't hold.
 */
public class XdrJsonSep51FormsTest {
  private static final String ACCOUNT = "GAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWHF";

  private static void assertJsonEquals(String expected, String actual) {
    JsonElement e = JsonParser.parseString(expected);
    JsonElement a = JsonParser.parseString(actual);
    assertEquals(e, a);
  }

  // --- Enum members and union arms ----------------------------------------------------------

  @Test
  public void transactionResultArmsAreSnakeCase() throws IOException {
    String json =
        "{\"fee_charged\":\"100\",\"result\":{\"tx_success\":[{\"op_inner\":{\"payment\":\"success\"}}]},\"ext\":\"v0\"}";
    TransactionResult result = TransactionResult.fromJson(json);
    assertEquals(TransactionResultCode.txSUCCESS, result.getResult().getDiscriminant());
    OperationResult op = result.getResult().getResults()[0];
    assertEquals(OperationResultCode.opINNER, op.getDiscriminant());
    assertEquals(
        PaymentResultCode.PAYMENT_SUCCESS, op.getTr().getPaymentResult().getDiscriminant());

    TransactionResult decoded = TransactionResult.fromXdrBase64(result.toXdrBase64());
    assertJsonEquals(json, decoded.toJson());
  }

  @Test
  public void transactionResultCodeNames() {
    assertEquals("\"tx_success\"", TransactionResultCode.txSUCCESS.toJson());
    assertEquals("\"tx_failed\"", TransactionResultCode.txFAILED.toJson());
    assertEquals(
        "\"tx_fee_bump_inner_success\"", TransactionResultCode.txFEE_BUMP_INNER_SUCCESS.toJson());
    assertEquals(
        "\"tx_fee_bump_inner_failed\"", TransactionResultCode.txFEE_BUMP_INNER_FAILED.toJson());
    assertEquals(
        "\"tx_bad_min_seq_age_or_gap\"", TransactionResultCode.txBAD_MIN_SEQ_AGE_OR_GAP.toJson());
    assertEquals(TransactionResultCode.txSUCCESS, TransactionResultCode.fromJson("\"tx_success\""));
  }

  @Test
  public void operationResultCodeNames() {
    assertEquals("\"op_inner\"", OperationResultCode.opINNER.toJson());
    assertEquals("\"op_bad_auth\"", OperationResultCode.opBAD_AUTH.toJson());
    assertEquals("\"op_no_account\"", OperationResultCode.opNO_ACCOUNT.toJson());
    assertEquals(OperationResultCode.opINNER, OperationResultCode.fromJson("\"op_inner\""));
  }

  @Test
  public void oldArmNamesAreRejected() {
    assertThrows(
        IllegalArgumentException.class, () -> TransactionResultCode.fromJson("\"txsuccess\""));
    assertThrows(IllegalArgumentException.class, () -> OperationResultCode.fromJson("\"opinner\""));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            TransactionResult.fromJson(
                "{\"fee_charged\":\"100\",\"result\":{\"txsuccess\":[]},\"ext\":\"v0\"}"));
  }

  @Test
  public void camelCaseMembersAreSplitIntoWords() {
    assertEquals("\"wasm_insn_exec\"", ContractCostType.WasmInsnExec.toJson());
    assertEquals("\"mem_alloc\"", ContractCostType.MemAlloc.toJson());
    assertEquals("\"compute_sha256_hash\"", ContractCostType.ComputeSha256Hash.toJson());
    assertEquals("\"bls12381_g1_add\"", ContractCostType.Bls12381G1Add.toJson());
    assertEquals("\"cha_cha20_draw_bytes\"", ContractCostType.ChaCha20DrawBytes.toJson());
    assertEquals(ContractCostType.WasmInsnExec, ContractCostType.fromJson("\"wasm_insn_exec\""));
  }

  @Test
  public void ipAddrTypeNames() throws IOException {
    assertEquals("\"i_pv4\"", IPAddrType.IPv4.toJson());
    assertEquals("\"i_pv6\"", IPAddrType.IPv6.toJson());
    String json = "{\"ip\":{\"i_pv4\":\"7f000001\"},\"port\":11625,\"num_failures\":0}";
    PeerAddress address = PeerAddress.fromJson(json);
    assertEquals(IPAddrType.IPv4, address.getIp().getDiscriminant());
    assertJsonEquals(json, PeerAddress.fromXdrBase64(address.toXdrBase64()).toJson());
  }

  // Not in the SEP-51 text: rs-stellar-xdr keeps the letter because a Rust identifier can't
  // start with a digit, and the other implementations follow it. See enum_json_name in
  // xdr-generator/generator/generator.rb.
  @Test
  public void namesStartingWithADigitKeepAPrefixLetter() {
    assertEquals("\"b8_bit\"", BinaryFuseFilterType.BINARY_FUSE_FILTER_8_BIT.toJson());
    assertEquals("\"b16_bit\"", BinaryFuseFilterType.BINARY_FUSE_FILTER_16_BIT.toJson());
    assertEquals("\"b32_bit\"", BinaryFuseFilterType.BINARY_FUSE_FILTER_32_BIT.toJson());
    assertEquals(
        BinaryFuseFilterType.BINARY_FUSE_FILTER_8_BIT, BinaryFuseFilterType.fromJson("\"b8_bit\""));
    assertThrows(IllegalArgumentException.class, () -> BinaryFuseFilterType.fromJson("\"8_bit\""));
  }

  @Test
  public void namesThatAlreadyMatchAreUnchanged() {
    assertEquals("\"credit_alphanum4\"", AssetType.ASSET_TYPE_CREDIT_ALPHANUM4.toJson());
    assertEquals("\"pool_share\"", AssetType.ASSET_TYPE_POOL_SHARE.toJson());
    assertEquals("\"ledger_key_nonce\"", SCValType.SCV_LEDGER_KEY_NONCE.toJson());
    assertEquals("\"bytes_n\"", SCSpecType.SC_SPEC_TYPE_BYTES_N.toJson());
    assertEquals("\"liveentry\"", BucketEntryType.LIVEENTRY.toJson());
  }

  // --- AssetCode ----------------------------------------------------------------------------

  @Test
  public void allowTrustAssetIsABareString() throws IOException {
    String json = "{\"trustor\":\"" + ACCOUNT + "\",\"asset\":\"FIL\",\"authorize\":1}";
    AllowTrustOp op = AllowTrustOp.fromJson(json);
    assertEquals(AssetType.ASSET_TYPE_CREDIT_ALPHANUM4, op.getAsset().getDiscriminant());
    assertArrayEquals(new byte[] {'F', 'I', 'L', 0}, op.getAsset().getAssetCode4().getAssetCode4());
    assertJsonEquals(json, AllowTrustOp.fromXdrBase64(op.toXdrBase64()).toJson());
  }

  @Test
  public void assetCodeLengthChoosesTheArm() throws IOException {
    assertAssetCode("\"\"", AssetType.ASSET_TYPE_CREDIT_ALPHANUM4, new byte[4]);
    assertAssetCode(
        "\"USDC\"", AssetType.ASSET_TYPE_CREDIT_ALPHANUM4, new byte[] {'U', 'S', 'D', 'C'});
    assertAssetCode(
        "\"ASTRO\"",
        AssetType.ASSET_TYPE_CREDIT_ALPHANUM12,
        Arrays.copyOf(new byte[] {'A', 'S', 'T', 'R', 'O'}, 12));
    assertAssetCode(
        "\"ABCDEFGHIJKL\"",
        AssetType.ASSET_TYPE_CREDIT_ALPHANUM12,
        "ABCDEFGHIJKL".getBytes(java.nio.charset.StandardCharsets.US_ASCII));
    // An AssetCode12 shorter than 5 bytes keeps 5, so it doesn't read back as an AssetCode4.
    assertAssetCode(
        "\"AB\\\\0\\\\0\\\\0\"",
        AssetType.ASSET_TYPE_CREDIT_ALPHANUM12,
        Arrays.copyOf(new byte[] {'A', 'B'}, 12));
    assertAssetCode(
        "\"\\\\0\\\\0\\\\0\\\\0\\\\0\"", AssetType.ASSET_TYPE_CREDIT_ALPHANUM12, new byte[12]);
  }

  private static void assertAssetCode(String json, AssetType type, byte[] bytes)
      throws IOException {
    AssetCode code = AssetCode.fromJson(json);
    assertEquals(type, code.getDiscriminant());
    if (type == AssetType.ASSET_TYPE_CREDIT_ALPHANUM4) {
      assertArrayEquals(bytes, code.getAssetCode4().getAssetCode4());
    } else {
      assertArrayEquals(bytes, code.getAssetCode12().getAssetCode12());
    }
    AssetCode decoded = AssetCode.fromXdrBase64(code.toXdrBase64());
    assertEquals(code, decoded);
    assertJsonEquals(json, decoded.toJson());
  }

  @Test
  public void assetCodeRejectsOtherForms() {
    assertThrows(IllegalArgumentException.class, () -> AssetCode.fromJson("\"ABCDEFGHIJKLM\""));
    assertThrows(
        IllegalArgumentException.class, () -> AssetCode.fromJson("{\"credit_alphanum4\":\"FIL\"}"));
    assertThrows(IllegalArgumentException.class, () -> AssetCode.fromJson("1"));
  }

  // --- Strkeys ------------------------------------------------------------------------------

  @Test
  public void signerKeyWithEmptySignedPayloadHasNoJson() {
    SignerKey key = signedPayloadSigner(new byte[0]);
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class, key::toJson);
    assertTrue(e.getMessage(), e.getMessage().contains("no SEP-51 representation"));
    assertThrows(IllegalArgumentException.class, () -> key.getEd25519SignedPayload().toJson());
  }

  @Test
  public void signerKeySignedPayloadRoundTrips() throws IOException {
    for (int length : new int[] {1, 4, 5, 63, 64}) {
      byte[] payload = new byte[length];
      for (int i = 0; i < length; i++) {
        payload[i] = (byte) (i + 1);
      }
      SignerKey key = signedPayloadSigner(payload);
      String json = key.toJson();
      String strKey = JsonParser.parseString(json).getAsString();
      assertTrue(strKey, strKey.startsWith("P"));
      assertEquals(key, SignerKey.fromJson(json));
      assertEquals(
          key.getEd25519SignedPayload(), SignerKey.SignerKeyEd25519SignedPayload.fromJson(json));
      assertEquals(key, SignerKey.fromXdrBase64(SignerKey.fromJson(json).toXdrBase64()));
    }
  }

  private static SignerKey signedPayloadSigner(byte[] payload) {
    byte[] ed25519 = KeyPair.random().getPublicKey();
    return SignerKey.builder()
        .discriminant(SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD)
        .ed25519SignedPayload(
            SignerKey.SignerKeyEd25519SignedPayload.builder()
                .ed25519(new Uint256(ed25519))
                .payload(payload)
                .build())
        .build();
  }

  @Test
  public void scAddressMuxedContractIsAWStrkey() throws IOException {
    byte[] contract = new byte[32];
    for (int i = 0; i < 32; i++) {
      contract[i] = (byte) (0x10 + i);
    }
    MuxedContract muxed =
        MuxedContract.builder()
            .id(new Uint64(new XdrUnsignedHyperInteger(new BigInteger("18446744073709551615"))))
            .contractId(new ContractID(new Hash(contract)))
            .build();
    byte[] raw = Arrays.copyOf(contract, 40);
    Arrays.fill(raw, 32, 40, (byte) 0xff);
    String strKey = StrKey.encodeMuxedContract(raw);

    assertEquals("\"" + strKey + "\"", muxed.toJson());
    assertEquals(muxed, MuxedContract.fromJson("\"" + strKey + "\""));

    SCAddress address =
        SCAddress.builder()
            .discriminant(SCAddressType.SC_ADDRESS_TYPE_MUXED_CONTRACT)
            .muxedContract(muxed)
            .build();
    assertEquals("\"" + strKey + "\"", address.toJson());
    SCAddress read = SCAddress.fromJson("\"" + strKey + "\"");
    assertEquals(address, read);
    assertEquals(address, SCAddress.fromXdrBase64(read.toXdrBase64()));
    assertEquals(
        "{\"address\":\"" + strKey + "\"}",
        SCVal.builder().discriminant(SCValType.SCV_ADDRESS).address(address).build().toJson());
  }

  @Test
  public void muxedAccountMed25519IsAnMStrkey() {
    byte[] ed25519 = KeyPair.random().getPublicKey();
    MuxedAccount.MuxedAccountMed25519 med =
        MuxedAccount.MuxedAccountMed25519.builder()
            .id(new Uint64(new XdrUnsignedHyperInteger(1234L)))
            .ed25519(new Uint256(ed25519))
            .build();
    String json = med.toJson();
    String strKey = JsonParser.parseString(json).getAsString();
    assertTrue(strKey, strKey.startsWith("M"));
    assertEquals(med, MuxedAccount.MuxedAccountMed25519.fromJson(json));

    MuxedAccount account =
        MuxedAccount.builder()
            .discriminant(CryptoKeyType.KEY_TYPE_MUXED_ED25519)
            .med25519(med)
            .build();
    assertEquals(json, account.toJson());
    assertEquals(account, MuxedAccount.fromJson(json));
  }

  // --- Optional union arms ------------------------------------------------------------------

  @Test
  public void scValWithoutVecOrMapIsNull() throws IOException {
    SCVal vec = SCVal.builder().discriminant(SCValType.SCV_VEC).build();
    SCVal map = SCVal.builder().discriminant(SCValType.SCV_MAP).build();
    assertEquals("{\"vec\":null}", vec.toJson());
    assertEquals("{\"map\":null}", map.toJson());

    SCVal readVec = SCVal.fromJson("{\"vec\":null}");
    assertEquals(SCValType.SCV_VEC, readVec.getDiscriminant());
    assertNull(readVec.getVec());
    assertEquals(vec, readVec);
    assertEquals("AAAAEAAAAAA=", readVec.toXdrBase64());
    assertEquals(map, SCVal.fromJson("{\"map\":null}"));
    assertEquals("AAAAEQAAAAA=", SCVal.fromJson("{\"map\":null}").toXdrBase64());

    assertEquals("{\"vec\":[]}", SCVal.fromJson("{\"vec\":[]}").toJson());
  }

  @Test
  public void claimPredicateNotWithoutPredicateIsNull() throws IOException {
    ClaimPredicate not =
        ClaimPredicate.builder().discriminant(ClaimPredicateType.CLAIM_PREDICATE_NOT).build();
    assertEquals("{\"not\":null}", not.toJson());
    ClaimPredicate read = ClaimPredicate.fromJson("{\"not\":null}");
    assertEquals(not, read);
    assertEquals(not, ClaimPredicate.fromXdrBase64(read.toXdrBase64()));

    String nested = "{\"not\":\"unconditional\"}";
    assertEquals(nested, ClaimPredicate.fromJson(nested).toJson());
  }
}
