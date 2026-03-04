package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import org.junit.Test;
import org.stellar.sdk.xdr.AccountID;
import org.stellar.sdk.xdr.ClaimableBalanceID;
import org.stellar.sdk.xdr.ClaimableBalanceIDType;
import org.stellar.sdk.xdr.ContractExecutable;
import org.stellar.sdk.xdr.ContractExecutableType;
import org.stellar.sdk.xdr.ContractID;
import org.stellar.sdk.xdr.Hash;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.MuxedEd25519Account;
import org.stellar.sdk.xdr.PoolID;
import org.stellar.sdk.xdr.PublicKey;
import org.stellar.sdk.xdr.PublicKeyType;
import org.stellar.sdk.xdr.SCAddress;
import org.stellar.sdk.xdr.SCAddressType;
import org.stellar.sdk.xdr.SCContractInstance;
import org.stellar.sdk.xdr.SCError;
import org.stellar.sdk.xdr.SCErrorCode;
import org.stellar.sdk.xdr.SCErrorType;
import org.stellar.sdk.xdr.SCMap;
import org.stellar.sdk.xdr.SCMapEntry;
import org.stellar.sdk.xdr.SCNonceKey;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.Uint256;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

public class ScValComparatorTest {

  private static byte[] bytes32(int fill) {
    byte[] b = new byte[32];
    Arrays.fill(b, (byte) fill);
    return b;
  }

  private static byte[] bytes32Last(int last) {
    byte[] b = new byte[32];
    b[31] = (byte) last;
    return b;
  }

  @Test
  public void testCrossTypeOrdering() {
    // SCV_BOOL(0) < SCV_VOID(1) < SCV_U32(3) < SCV_SYMBOL(15)
    assertTrue(ScValComparator.compareScVal(Scv.toBoolean(true), Scv.toVoid()) < 0);
    assertTrue(ScValComparator.compareScVal(Scv.toVoid(), Scv.toUint32(0)) < 0);
    assertTrue(ScValComparator.compareScVal(Scv.toUint32(0), Scv.toSymbol("x")) < 0);
    assertTrue(ScValComparator.compareScVal(Scv.toSymbol("x"), Scv.toVoid()) > 0);
  }

  @Test
  public void testBool() {
    assertTrue(ScValComparator.compareScVal(Scv.toBoolean(false), Scv.toBoolean(true)) < 0);
    assertTrue(ScValComparator.compareScVal(Scv.toBoolean(true), Scv.toBoolean(false)) > 0);
    assertEquals(0, ScValComparator.compareScVal(Scv.toBoolean(true), Scv.toBoolean(true)));
  }

  @Test
  public void testVoid() {
    assertEquals(0, ScValComparator.compareScVal(Scv.toVoid(), Scv.toVoid()));
  }

  @Test
  public void testU32() {
    assertTrue(ScValComparator.compareScVal(Scv.toUint32(1), Scv.toUint32(2)) < 0);
    assertEquals(0, ScValComparator.compareScVal(Scv.toUint32(0), Scv.toUint32(0)));
  }

  @Test
  public void testI32SignedOrder() {
    assertTrue(ScValComparator.compareScVal(Scv.toInt32(-10), Scv.toInt32(-1)) < 0);
    assertTrue(ScValComparator.compareScVal(Scv.toInt32(-1), Scv.toInt32(0)) < 0);
    assertEquals(0, ScValComparator.compareScVal(Scv.toInt32(5), Scv.toInt32(5)));
  }

  @Test
  public void testU64() {
    BigInteger maxU64 = new BigInteger("18446744073709551615");
    assertTrue(
        ScValComparator.compareScVal(Scv.toUint64(BigInteger.ZERO), Scv.toUint64(maxU64)) < 0);
  }

  @Test
  public void testI64() {
    assertTrue(ScValComparator.compareScVal(Scv.toInt64(-1), Scv.toInt64(0)) < 0);
    assertTrue(
        ScValComparator.compareScVal(Scv.toInt64(Long.MIN_VALUE), Scv.toInt64(Long.MAX_VALUE)) < 0);
  }

  @Test
  public void testTimepoint() {
    assertTrue(
        ScValComparator.compareScVal(
                Scv.toTimePoint(BigInteger.ONE), Scv.toTimePoint(BigInteger.valueOf(2)))
            < 0);
  }

  @Test
  public void testDuration() {
    assertTrue(
        ScValComparator.compareScVal(
                Scv.toDuration(BigInteger.ONE), Scv.toDuration(BigInteger.valueOf(2)))
            < 0);
  }

  @Test
  public void testU128() {
    BigInteger twoTo64 = BigInteger.ONE.shiftLeft(64);
    // hi differs
    assertTrue(
        ScValComparator.compareScVal(
                Scv.toUint128(twoTo64.subtract(BigInteger.ONE)), Scv.toUint128(twoTo64))
            < 0);
    assertEquals(
        0,
        ScValComparator.compareScVal(
            Scv.toUint128(BigInteger.ZERO), Scv.toUint128(BigInteger.ZERO)));
  }

  @Test
  public void testI128SignedOrder() {
    BigInteger minI128 = BigInteger.ONE.shiftLeft(127).negate();
    assertTrue(
        ScValComparator.compareScVal(Scv.toInt128(minI128), Scv.toInt128(BigInteger.ZERO)) < 0);
    assertTrue(
        ScValComparator.compareScVal(
                Scv.toInt128(BigInteger.valueOf(-1)), Scv.toInt128(BigInteger.ZERO))
            < 0);
  }

  @Test
  public void testU256() {
    assertTrue(
        ScValComparator.compareScVal(Scv.toUint256(BigInteger.ZERO), Scv.toUint256(BigInteger.ONE))
            < 0);
  }

  @Test
  public void testI256SignedOrder() {
    BigInteger minI256 = BigInteger.ONE.shiftLeft(255).negate();
    assertTrue(
        ScValComparator.compareScVal(Scv.toInt256(minI256), Scv.toInt256(BigInteger.ZERO)) < 0);
    assertTrue(
        ScValComparator.compareScVal(
                Scv.toInt256(BigInteger.valueOf(-1)), Scv.toInt256(BigInteger.ZERO))
            < 0);
  }

  @Test
  public void testBytes() {
    assertTrue(
        ScValComparator.compareScVal(
                Scv.toBytes(new byte[] {0x61, 0x62}), Scv.toBytes(new byte[] {0x61, 0x62, 0x63}))
            < 0);
    assertEquals(
        0,
        ScValComparator.compareScVal(
            Scv.toBytes(new byte[] {0x61}), Scv.toBytes(new byte[] {0x61})));
  }

  @Test
  public void testString() {
    assertTrue(ScValComparator.compareScVal(Scv.toString("abc"), Scv.toString("abd")) < 0);
    assertTrue(ScValComparator.compareScVal(Scv.toString("ab"), Scv.toString("abc")) < 0);
  }

  @Test
  public void testSymbol() {
    assertTrue(ScValComparator.compareScVal(Scv.toSymbol("alpha"), Scv.toSymbol("bravo")) < 0);
    assertEquals(0, ScValComparator.compareScVal(Scv.toSymbol("x"), Scv.toSymbol("x")));
  }

  @Test
  public void testVec() {
    java.util.List<SCVal> a = Arrays.asList(Scv.toUint32(1), Scv.toUint32(2));
    java.util.List<SCVal> b = Arrays.asList(Scv.toUint32(1), Scv.toUint32(3));
    assertTrue(ScValComparator.compareScVal(Scv.toVec(a), Scv.toVec(b)) < 0);
    // shorter < longer when prefix matches
    assertTrue(
        ScValComparator.compareScVal(
                Scv.toVec(Arrays.asList(Scv.toUint32(1))),
                Scv.toVec(Arrays.asList(Scv.toUint32(1), Scv.toUint32(2))))
            < 0);
  }

  @Test
  public void testMap() {
    SCVal a = makeMap(new SCMapEntry[] {entry(Scv.toUint32(1), Scv.toVoid())});
    SCVal b = makeMap(new SCMapEntry[] {entry(Scv.toUint32(2), Scv.toVoid())});
    assertTrue(ScValComparator.compareScVal(a, b) < 0);
    // compare by val when keys equal
    SCVal c = makeMap(new SCMapEntry[] {entry(Scv.toUint32(1), Scv.toInt32(10))});
    SCVal d = makeMap(new SCMapEntry[] {entry(Scv.toUint32(1), Scv.toInt32(20))});
    assertTrue(ScValComparator.compareScVal(c, d) < 0);
    // shorter < longer
    SCVal e =
        makeMap(
            new SCMapEntry[] {
              entry(Scv.toUint32(1), Scv.toVoid()), entry(Scv.toUint32(2), Scv.toVoid())
            });
    assertTrue(ScValComparator.compareScVal(a, e) < 0);
  }

  @Test
  public void testError() {
    SCVal contractErr = makeErrorContract(1);
    SCVal wasmErr = makeErrorWasm(SCErrorCode.SCEC_ARITH_DOMAIN);
    // SCE_CONTRACT(0) < SCE_WASM_VM(1)
    assertTrue(ScValComparator.compareScVal(contractErr, wasmErr) < 0);
    // same type, different code
    assertTrue(ScValComparator.compareScVal(makeErrorContract(1), makeErrorContract(2)) < 0);
    assertEquals(0, ScValComparator.compareScVal(makeErrorContract(5), makeErrorContract(5)));
  }

  @Test
  public void testContractInstance() {
    SCVal wasm = makeWasmInstance(bytes32(0x00), null);
    SCVal asset = makeStellarAssetInstance(null);
    // WASM(0) < STELLAR_ASSET(1)
    assertTrue(ScValComparator.compareScVal(wasm, asset) < 0);
    // different wasm hash
    assertTrue(
        ScValComparator.compareScVal(
                makeWasmInstance(bytes32(0x00), null), makeWasmInstance(bytes32Last(0x01), null))
            < 0);
    // null storage < non-null storage
    SCMap storage = new SCMap(new SCMapEntry[] {entry(Scv.toUint32(1), Scv.toVoid())});
    assertTrue(ScValComparator.compareScVal(asset, makeStellarAssetInstance(storage)) < 0);
  }

  @Test
  public void testLedgerKeyNonce() {
    assertTrue(ScValComparator.compareScVal(makeNonce(-1), makeNonce(0)) < 0);
    assertEquals(0, ScValComparator.compareScVal(makeNonce(42), makeNonce(42)));
  }

  @Test
  public void testLedgerKeyContractInstance() {
    SCVal a = SCVal.builder().discriminant(SCValType.SCV_LEDGER_KEY_CONTRACT_INSTANCE).build();
    SCVal b = SCVal.builder().discriminant(SCValType.SCV_LEDGER_KEY_CONTRACT_INSTANCE).build();
    assertEquals(0, ScValComparator.compareScVal(a, b));
  }

  @Test
  public void testAddressTypeOrdering() {
    // account(0) < contract(1) < muxed(2) < claimable(3) < pool(4)
    SCAddress[] addrs = {
      accountAddress(bytes32(0x00)),
      contractAddress(bytes32(0x00)),
      muxedAddress(0, bytes32(0x00)),
      claimableBalanceAddress(bytes32(0x00)),
      liquidityPoolAddress(bytes32(0x00)),
    };
    for (int i = 0; i < addrs.length - 1; i++) {
      assertTrue(ScValComparator.compareScAddress(addrs[i], addrs[i + 1]) < 0);
    }
  }

  @Test
  public void testAddressSameType() {
    // account: compare by ed25519
    assertTrue(
        ScValComparator.compareScAddress(
                accountAddress(bytes32(0x00)), accountAddress(bytes32Last(0x01)))
            < 0);
    // contract: compare by hash
    assertTrue(
        ScValComparator.compareScAddress(
                contractAddress(bytes32(0x00)), contractAddress(bytes32(0xFF)))
            < 0);
    // muxed: id first, then ed25519
    assertTrue(
        ScValComparator.compareScAddress(
                muxedAddress(1, bytes32(0xFF)), muxedAddress(2, bytes32(0x00)))
            < 0);
    assertTrue(
        ScValComparator.compareScAddress(
                muxedAddress(1, bytes32(0x00)), muxedAddress(1, bytes32Last(0x01)))
            < 0);
    assertEquals(
        0,
        ScValComparator.compareScAddress(
            muxedAddress(5, bytes32(0xAB)), muxedAddress(5, bytes32(0xAB))));
  }

  @Test
  public void testContractExecutable() {
    ContractExecutable wasm =
        ContractExecutable.builder()
            .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_WASM)
            .wasm_hash(new Hash(bytes32(0x00)))
            .build();
    ContractExecutable asset =
        ContractExecutable.builder()
            .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_STELLAR_ASSET)
            .build();
    assertTrue(ScValComparator.compareContractExecutable(wasm, asset) < 0);
    assertEquals(0, ScValComparator.compareContractExecutable(asset, asset));
  }

  @Test
  public void testOptionalScMap() {
    SCMap m1 = new SCMap(new SCMapEntry[] {entry(Scv.toUint32(1), Scv.toVoid())});
    SCMap m2 = new SCMap(new SCMapEntry[] {entry(Scv.toUint32(2), Scv.toVoid())});

    assertEquals(0, ScValComparator.compareOptionalScMap(null, null));
    assertTrue(ScValComparator.compareOptionalScMap(null, m1) < 0);
    assertTrue(ScValComparator.compareOptionalScMap(m1, null) > 0);
    assertTrue(ScValComparator.compareOptionalScMap(m1, m2) < 0);
    assertEquals(0, ScValComparator.compareOptionalScMap(m1, m1));
  }

  @Test
  public void testAntisymmetry() {
    SCVal[][] pairs = {
      {Scv.toBoolean(false), Scv.toBoolean(true)},
      {Scv.toUint32(1), Scv.toUint32(2)},
      {Scv.toInt32(-10), Scv.toInt32(10)},
      {Scv.toSymbol("a"), Scv.toSymbol("b")},
      // cross-type
      {Scv.toBoolean(false), Scv.toUint32(0)},
      {Scv.toInt32(0), Scv.toSymbol("x")},
    };
    for (int i = 0; i < pairs.length; i++) {
      assertEquals(
          "Antisymmetry failed for pair " + i,
          ScValComparator.compareScVal(pairs[i][0], pairs[i][1]),
          -ScValComparator.compareScVal(pairs[i][1], pairs[i][0]));
    }
  }

  @Test
  public void testTransitivity() {
    // SCV_BOOL(0) < SCV_U32(3) < SCV_SYMBOL(15)
    SCVal a = Scv.toBoolean(true);
    SCVal b = Scv.toUint32(0);
    SCVal c = Scv.toSymbol("x");
    assertTrue(ScValComparator.compareScVal(a, b) < 0);
    assertTrue(ScValComparator.compareScVal(b, c) < 0);
    assertTrue(ScValComparator.compareScVal(a, c) < 0);
  }

  private static SCMapEntry entry(SCVal key, SCVal val) {
    return SCMapEntry.builder().key(key).val(val).build();
  }

  private static SCVal makeMap(SCMapEntry[] entries) {
    return SCVal.builder().discriminant(SCValType.SCV_MAP).map(new SCMap(entries)).build();
  }

  private static SCVal makeErrorContract(int code) {
    return SCVal.builder()
        .discriminant(SCValType.SCV_ERROR)
        .error(
            SCError.builder()
                .discriminant(SCErrorType.SCE_CONTRACT)
                .contractCode(new Uint32(new XdrUnsignedInteger((long) code)))
                .build())
        .build();
  }

  private static SCVal makeErrorWasm(SCErrorCode code) {
    return SCVal.builder()
        .discriminant(SCValType.SCV_ERROR)
        .error(SCError.builder().discriminant(SCErrorType.SCE_WASM_VM).code(code).build())
        .build();
  }

  private static SCVal makeWasmInstance(byte[] wasmHash, SCMap storage) {
    return SCVal.builder()
        .discriminant(SCValType.SCV_CONTRACT_INSTANCE)
        .instance(
            SCContractInstance.builder()
                .executable(
                    ContractExecutable.builder()
                        .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_WASM)
                        .wasm_hash(new Hash(wasmHash))
                        .build())
                .storage(storage)
                .build())
        .build();
  }

  private static SCVal makeStellarAssetInstance(SCMap storage) {
    return SCVal.builder()
        .discriminant(SCValType.SCV_CONTRACT_INSTANCE)
        .instance(
            SCContractInstance.builder()
                .executable(
                    ContractExecutable.builder()
                        .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_STELLAR_ASSET)
                        .build())
                .storage(storage)
                .build())
        .build();
  }

  private static SCVal makeNonce(long v) {
    return SCVal.builder()
        .discriminant(SCValType.SCV_LEDGER_KEY_NONCE)
        .nonce_key(SCNonceKey.builder().nonce(new Int64(v)).build())
        .build();
  }

  private static SCAddress accountAddress(byte[] key) {
    return SCAddress.builder()
        .discriminant(SCAddressType.SC_ADDRESS_TYPE_ACCOUNT)
        .accountId(
            new AccountID(
                PublicKey.builder()
                    .discriminant(PublicKeyType.PUBLIC_KEY_TYPE_ED25519)
                    .ed25519(new Uint256(key))
                    .build()))
        .build();
  }

  private static SCAddress contractAddress(byte[] hash) {
    return SCAddress.builder()
        .discriminant(SCAddressType.SC_ADDRESS_TYPE_CONTRACT)
        .contractId(new ContractID(new Hash(hash)))
        .build();
  }

  private static SCAddress muxedAddress(long id, byte[] key) {
    return SCAddress.builder()
        .discriminant(SCAddressType.SC_ADDRESS_TYPE_MUXED_ACCOUNT)
        .muxedAccount(
            MuxedEd25519Account.builder()
                .id(new Uint64(new XdrUnsignedHyperInteger(BigInteger.valueOf(id))))
                .ed25519(new Uint256(key))
                .build())
        .build();
  }

  private static SCAddress claimableBalanceAddress(byte[] hash) {
    return SCAddress.builder()
        .discriminant(SCAddressType.SC_ADDRESS_TYPE_CLAIMABLE_BALANCE)
        .claimableBalanceId(
            ClaimableBalanceID.builder()
                .discriminant(ClaimableBalanceIDType.CLAIMABLE_BALANCE_ID_TYPE_V0)
                .v0(new Hash(hash))
                .build())
        .build();
  }

  private static SCAddress liquidityPoolAddress(byte[] hash) {
    return SCAddress.builder()
        .discriminant(SCAddressType.SC_ADDRESS_TYPE_LIQUIDITY_POOL)
        .liquidityPoolId(new PoolID(new Hash(hash)))
        .build();
  }
}
