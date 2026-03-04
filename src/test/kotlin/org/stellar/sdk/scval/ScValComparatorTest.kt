package org.stellar.sdk.scval

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import java.math.BigInteger
import org.stellar.sdk.xdr.AccountID
import org.stellar.sdk.xdr.ClaimableBalanceID
import org.stellar.sdk.xdr.ClaimableBalanceIDType
import org.stellar.sdk.xdr.ContractExecutable
import org.stellar.sdk.xdr.ContractExecutableType
import org.stellar.sdk.xdr.ContractID
import org.stellar.sdk.xdr.Hash
import org.stellar.sdk.xdr.Int64
import org.stellar.sdk.xdr.MuxedEd25519Account
import org.stellar.sdk.xdr.PoolID
import org.stellar.sdk.xdr.PublicKey
import org.stellar.sdk.xdr.PublicKeyType
import org.stellar.sdk.xdr.SCAddress
import org.stellar.sdk.xdr.SCAddressType
import org.stellar.sdk.xdr.SCContractInstance
import org.stellar.sdk.xdr.SCError
import org.stellar.sdk.xdr.SCErrorCode
import org.stellar.sdk.xdr.SCErrorType
import org.stellar.sdk.xdr.SCMap
import org.stellar.sdk.xdr.SCMapEntry
import org.stellar.sdk.xdr.SCNonceKey
import org.stellar.sdk.xdr.SCVal
import org.stellar.sdk.xdr.SCValType
import org.stellar.sdk.xdr.Uint256
import org.stellar.sdk.xdr.Uint32
import org.stellar.sdk.xdr.Uint64
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger
import org.stellar.sdk.xdr.XdrUnsignedInteger

class ScValComparatorTest :
  FunSpec({
    context("cross-type ordering") {
      test("SCV_BOOL < SCV_VOID < SCV_U32 < SCV_SYMBOL") {
        ScValComparator.compareScVal(Scv.toBoolean(true), Scv.toVoid()) shouldBeLessThan 0
        ScValComparator.compareScVal(Scv.toVoid(), Scv.toUint32(0)) shouldBeLessThan 0
        ScValComparator.compareScVal(Scv.toUint32(0), Scv.toSymbol("x")) shouldBeLessThan 0
        ScValComparator.compareScVal(Scv.toSymbol("x"), Scv.toVoid()) shouldBeGreaterThan 0
      }
    }

    context("SCV_BOOL") {
      test("false < true") {
        ScValComparator.compareScVal(Scv.toBoolean(false), Scv.toBoolean(true)) shouldBeLessThan 0
        ScValComparator.compareScVal(Scv.toBoolean(true), Scv.toBoolean(false)) shouldBeGreaterThan
          0
        ScValComparator.compareScVal(Scv.toBoolean(true), Scv.toBoolean(true)) shouldBe 0
      }
    }

    context("SCV_VOID") {
      test("void == void") { ScValComparator.compareScVal(Scv.toVoid(), Scv.toVoid()) shouldBe 0 }
    }

    context("SCV_U32") {
      test("numeric ordering") {
        ScValComparator.compareScVal(Scv.toUint32(1), Scv.toUint32(2)) shouldBeLessThan 0
        ScValComparator.compareScVal(Scv.toUint32(0), Scv.toUint32(0)) shouldBe 0
      }
    }

    context("SCV_I32") {
      test("signed ordering") {
        ScValComparator.compareScVal(Scv.toInt32(-10), Scv.toInt32(-1)) shouldBeLessThan 0
        ScValComparator.compareScVal(Scv.toInt32(-1), Scv.toInt32(0)) shouldBeLessThan 0
        ScValComparator.compareScVal(Scv.toInt32(5), Scv.toInt32(5)) shouldBe 0
      }
    }

    context("SCV_U64") {
      test("unsigned ordering including max") {
        val maxU64 = BigInteger("18446744073709551615")
        ScValComparator.compareScVal(
          Scv.toUint64(BigInteger.ZERO),
          Scv.toUint64(maxU64),
        ) shouldBeLessThan 0
      }
    }

    context("SCV_I64") {
      test("signed ordering") {
        ScValComparator.compareScVal(Scv.toInt64(-1), Scv.toInt64(0)) shouldBeLessThan 0
        ScValComparator.compareScVal(
          Scv.toInt64(Long.MIN_VALUE),
          Scv.toInt64(Long.MAX_VALUE),
        ) shouldBeLessThan 0
      }
    }

    context("SCV_TIMEPOINT") {
      test("numeric ordering") {
        ScValComparator.compareScVal(
          Scv.toTimePoint(BigInteger.ONE),
          Scv.toTimePoint(BigInteger.valueOf(2)),
        ) shouldBeLessThan 0
      }
    }

    context("SCV_DURATION") {
      test("numeric ordering") {
        ScValComparator.compareScVal(
          Scv.toDuration(BigInteger.ONE),
          Scv.toDuration(BigInteger.valueOf(2)),
        ) shouldBeLessThan 0
      }
    }

    context("SCV_U128") {
      test("tuple (hi, lo) ordering - hi differs") {
        val twoTo64 = BigInteger.ONE.shiftLeft(64)
        ScValComparator.compareScVal(
          Scv.toUint128(twoTo64.subtract(BigInteger.ONE)),
          Scv.toUint128(twoTo64),
        ) shouldBeLessThan 0
        ScValComparator.compareScVal(
          Scv.toUint128(BigInteger.ZERO),
          Scv.toUint128(BigInteger.ZERO),
        ) shouldBe 0
      }

      test("tuple (hi, lo) ordering - same hi, lo differs") {
        val twoTo64 = BigInteger.ONE.shiftLeft(64)
        // both have hi=1, but lo differs: 1*2^64+0 vs 1*2^64+1
        ScValComparator.compareScVal(
          Scv.toUint128(twoTo64),
          Scv.toUint128(twoTo64.add(BigInteger.ONE)),
        ) shouldBeLessThan 0
      }
    }

    context("SCV_I128") {
      test("signed ordering including negative") {
        val minI128 = BigInteger.ONE.shiftLeft(127).negate()
        ScValComparator.compareScVal(
          Scv.toInt128(minI128),
          Scv.toInt128(BigInteger.ZERO),
        ) shouldBeLessThan 0
        ScValComparator.compareScVal(
          Scv.toInt128(BigInteger.valueOf(-1)),
          Scv.toInt128(BigInteger.ZERO),
        ) shouldBeLessThan 0
      }

      test("same hi, lo differs") {
        // 1 and 2 both have hi=0, lo differs
        ScValComparator.compareScVal(
          Scv.toInt128(BigInteger.ONE),
          Scv.toInt128(BigInteger.valueOf(2)),
        ) shouldBeLessThan 0
      }
    }

    context("SCV_U256") {
      test("numeric ordering") {
        ScValComparator.compareScVal(
          Scv.toUint256(BigInteger.ZERO),
          Scv.toUint256(BigInteger.ONE),
        ) shouldBeLessThan 0
      }

      test("each component level determines ordering") {
        val shift64 = BigInteger.ONE.shiftLeft(64)
        val shift128 = BigInteger.ONE.shiftLeft(128)
        val shift192 = BigInteger.ONE.shiftLeft(192)
        // hi_hi differs
        ScValComparator.compareScVal(
          Scv.toUint256(shift192),
          Scv.toUint256(shift192.add(shift192)),
        ) shouldBeLessThan 0
        // same hi_hi, hi_lo differs
        ScValComparator.compareScVal(
          Scv.toUint256(shift128),
          Scv.toUint256(shift128.add(shift128)),
        ) shouldBeLessThan 0
        // same hi_hi+hi_lo, lo_hi differs
        ScValComparator.compareScVal(
          Scv.toUint256(shift64),
          Scv.toUint256(shift64.add(shift64)),
        ) shouldBeLessThan 0
        // same hi_hi+hi_lo+lo_hi, lo_lo differs
        ScValComparator.compareScVal(
          Scv.toUint256(BigInteger.ONE),
          Scv.toUint256(BigInteger.valueOf(2)),
        ) shouldBeLessThan 0
      }
    }

    context("SCV_I256") {
      test("signed ordering including negative") {
        val minI256 = BigInteger.ONE.shiftLeft(255).negate()
        ScValComparator.compareScVal(
          Scv.toInt256(minI256),
          Scv.toInt256(BigInteger.ZERO),
        ) shouldBeLessThan 0
        ScValComparator.compareScVal(
          Scv.toInt256(BigInteger.valueOf(-1)),
          Scv.toInt256(BigInteger.ZERO),
        ) shouldBeLessThan 0
      }

      test("each component level determines ordering") {
        val shift64 = BigInteger.ONE.shiftLeft(64)
        val shift128 = BigInteger.ONE.shiftLeft(128)
        // same hi_hi(0), hi_lo differs
        ScValComparator.compareScVal(
          Scv.toInt256(shift128),
          Scv.toInt256(shift128.add(shift128)),
        ) shouldBeLessThan 0
        // same hi_hi+hi_lo(0), lo_hi differs
        ScValComparator.compareScVal(
          Scv.toInt256(shift64),
          Scv.toInt256(shift64.add(shift64)),
        ) shouldBeLessThan 0
        // same hi_hi+hi_lo+lo_hi(0), lo_lo differs
        ScValComparator.compareScVal(
          Scv.toInt256(BigInteger.ONE),
          Scv.toInt256(BigInteger.valueOf(2)),
        ) shouldBeLessThan 0
      }
    }

    context("SCV_BYTES") {
      test("lexicographic ordering and prefix rule") {
        ScValComparator.compareScVal(
          Scv.toBytes(byteArrayOf(0x61, 0x62)),
          Scv.toBytes(byteArrayOf(0x61, 0x62, 0x63)),
        ) shouldBeLessThan 0
        ScValComparator.compareScVal(
          Scv.toBytes(byteArrayOf(0x61)),
          Scv.toBytes(byteArrayOf(0x61)),
        ) shouldBe 0
      }

      test("unsigned byte comparison: 0x80 > 0x7F") {
        ScValComparator.compareScVal(
          Scv.toBytes(byteArrayOf(0x7F)),
          Scv.toBytes(byteArrayOf(0x80.toByte())),
        ) shouldBeLessThan 0
      }
    }

    context("SCV_STRING") {
      test("lexicographic ordering and prefix rule") {
        ScValComparator.compareScVal(Scv.toString("abc"), Scv.toString("abd")) shouldBeLessThan 0
        ScValComparator.compareScVal(Scv.toString("ab"), Scv.toString("abc")) shouldBeLessThan 0
      }
    }

    context("SCV_SYMBOL") {
      test("lexicographic ordering") {
        ScValComparator.compareScVal(Scv.toSymbol("alpha"), Scv.toSymbol("bravo")) shouldBeLessThan
          0
        ScValComparator.compareScVal(Scv.toSymbol("x"), Scv.toSymbol("x")) shouldBe 0
      }
    }

    context("SCV_VEC") {
      test("element-by-element ordering") {
        val a = listOf(Scv.toUint32(1), Scv.toUint32(2))
        val b = listOf(Scv.toUint32(1), Scv.toUint32(3))
        ScValComparator.compareScVal(Scv.toVec(a), Scv.toVec(b)) shouldBeLessThan 0
      }

      test("shorter < longer when prefix matches") {
        ScValComparator.compareScVal(
          Scv.toVec(listOf(Scv.toUint32(1))),
          Scv.toVec(listOf(Scv.toUint32(1), Scv.toUint32(2))),
        ) shouldBeLessThan 0
      }

      test("equal vecs") {
        val v = listOf(Scv.toUint32(1), Scv.toUint32(2))
        ScValComparator.compareScVal(Scv.toVec(v), Scv.toVec(v)) shouldBe 0
      }
    }

    context("SCV_MAP") {
      test("compare by key") {
        val a = makeMap(arrayOf(entry(Scv.toUint32(1), Scv.toVoid())))
        val b = makeMap(arrayOf(entry(Scv.toUint32(2), Scv.toVoid())))
        ScValComparator.compareScVal(a, b) shouldBeLessThan 0
      }

      test("compare by val when keys equal") {
        val c = makeMap(arrayOf(entry(Scv.toUint32(1), Scv.toInt32(10))))
        val d = makeMap(arrayOf(entry(Scv.toUint32(1), Scv.toInt32(20))))
        ScValComparator.compareScVal(c, d) shouldBeLessThan 0
      }

      test("shorter < longer") {
        val a = makeMap(arrayOf(entry(Scv.toUint32(1), Scv.toVoid())))
        val e =
          makeMap(
            arrayOf(entry(Scv.toUint32(1), Scv.toVoid()), entry(Scv.toUint32(2), Scv.toVoid()))
          )
        ScValComparator.compareScVal(a, e) shouldBeLessThan 0
      }

      test("equal maps") {
        val a = makeMap(arrayOf(entry(Scv.toUint32(1), Scv.toInt32(10))))
        val b = makeMap(arrayOf(entry(Scv.toUint32(1), Scv.toInt32(10))))
        ScValComparator.compareScVal(a, b) shouldBe 0
      }
    }

    context("SCV_ERROR") {
      test("SCE_CONTRACT < SCE_WASM_VM") {
        val contractErr = makeErrorContract(1)
        val wasmErr = makeErrorWasm(SCErrorCode.SCEC_ARITH_DOMAIN)
        ScValComparator.compareScVal(contractErr, wasmErr) shouldBeLessThan 0
      }

      test("same type, different code") {
        ScValComparator.compareScVal(makeErrorContract(1), makeErrorContract(2)) shouldBeLessThan 0
        ScValComparator.compareScVal(makeErrorContract(5), makeErrorContract(5)) shouldBe 0
      }

      test("non-contract error: compare by error code") {
        val wasmErr1 = makeErrorWasm(SCErrorCode.SCEC_ARITH_DOMAIN)
        val wasmErr2 = makeErrorWasm(SCErrorCode.SCEC_UNEXPECTED_SIZE)
        ScValComparator.compareScVal(wasmErr1, wasmErr2) shouldBeLessThan 0
        ScValComparator.compareScVal(wasmErr1, wasmErr1) shouldBe 0
      }
    }

    context("SCV_CONTRACT_INSTANCE") {
      test("WASM < STELLAR_ASSET") {
        val wasm = makeWasmInstance(bytes32(0x00), null)
        val asset = makeStellarAssetInstance(null)
        ScValComparator.compareScVal(wasm, asset) shouldBeLessThan 0
      }

      test("different wasm hash") {
        ScValComparator.compareScVal(
          makeWasmInstance(bytes32(0x00), null),
          makeWasmInstance(bytes32Last(0x01), null),
        ) shouldBeLessThan 0
      }

      test("null storage < non-null storage") {
        val asset = makeStellarAssetInstance(null)
        val storage = SCMap(arrayOf(entry(Scv.toUint32(1), Scv.toVoid())))
        ScValComparator.compareScVal(asset, makeStellarAssetInstance(storage)) shouldBeLessThan 0
      }
    }

    context("SCV_LEDGER_KEY_NONCE") {
      test("signed ordering") {
        ScValComparator.compareScVal(makeNonce(-1), makeNonce(0)) shouldBeLessThan 0
        ScValComparator.compareScVal(makeNonce(42), makeNonce(42)) shouldBe 0
      }
    }

    context("SCV_LEDGER_KEY_CONTRACT_INSTANCE") {
      test("always equal") {
        val a = SCVal.builder().discriminant(SCValType.SCV_LEDGER_KEY_CONTRACT_INSTANCE).build()
        val b = SCVal.builder().discriminant(SCValType.SCV_LEDGER_KEY_CONTRACT_INSTANCE).build()
        ScValComparator.compareScVal(a, b) shouldBe 0
      }
    }

    context("compareScAddress") {
      test("address type ordering: account < contract < muxed < claimable < pool") {
        val addrs =
          arrayOf(
            accountAddress(bytes32(0x00)),
            contractAddress(bytes32(0x00)),
            muxedAddress(0, bytes32(0x00)),
            claimableBalanceAddress(bytes32(0x00)),
            liquidityPoolAddress(bytes32(0x00)),
          )
        for (i in 0 until addrs.size - 1) {
          ScValComparator.compareScAddress(addrs[i], addrs[i + 1]) shouldBeLessThan 0
        }
      }

      test("account: compare by ed25519") {
        ScValComparator.compareScAddress(
          accountAddress(bytes32(0x00)),
          accountAddress(bytes32Last(0x01)),
        ) shouldBeLessThan 0
      }

      test("contract: compare by hash") {
        ScValComparator.compareScAddress(
          contractAddress(bytes32(0x00)),
          contractAddress(bytes32(0xFF)),
        ) shouldBeLessThan 0
      }

      test("claimable balance: compare by hash") {
        ScValComparator.compareScAddress(
          claimableBalanceAddress(bytes32(0x00)),
          claimableBalanceAddress(bytes32Last(0x01)),
        ) shouldBeLessThan 0
        ScValComparator.compareScAddress(
          claimableBalanceAddress(bytes32(0xAA)),
          claimableBalanceAddress(bytes32(0xAA)),
        ) shouldBe 0
      }

      test("liquidity pool: compare by hash") {
        ScValComparator.compareScAddress(
          liquidityPoolAddress(bytes32(0x00)),
          liquidityPoolAddress(bytes32Last(0x01)),
        ) shouldBeLessThan 0
        ScValComparator.compareScAddress(
          liquidityPoolAddress(bytes32(0xBB)),
          liquidityPoolAddress(bytes32(0xBB)),
        ) shouldBe 0
      }

      test("muxed: id first, then ed25519") {
        ScValComparator.compareScAddress(
          muxedAddress(1, bytes32(0xFF)),
          muxedAddress(2, bytes32(0x00)),
        ) shouldBeLessThan 0
        ScValComparator.compareScAddress(
          muxedAddress(1, bytes32(0x00)),
          muxedAddress(1, bytes32Last(0x01)),
        ) shouldBeLessThan 0
        ScValComparator.compareScAddress(
          muxedAddress(5, bytes32(0xAB)),
          muxedAddress(5, bytes32(0xAB)),
        ) shouldBe 0
      }
    }

    context("compareContractExecutable") {
      test("WASM < STELLAR_ASSET") {
        val wasm =
          ContractExecutable.builder()
            .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_WASM)
            .wasm_hash(Hash(bytes32(0x00)))
            .build()
        val asset =
          ContractExecutable.builder()
            .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_STELLAR_ASSET)
            .build()
        ScValComparator.compareContractExecutable(wasm, asset) shouldBeLessThan 0
        ScValComparator.compareContractExecutable(asset, asset) shouldBe 0
      }
    }

    context("compareOptionalScMap") {
      test("null handling and ordering") {
        val m1 = SCMap(arrayOf(entry(Scv.toUint32(1), Scv.toVoid())))
        val m2 = SCMap(arrayOf(entry(Scv.toUint32(2), Scv.toVoid())))

        ScValComparator.compareOptionalScMap(null, null) shouldBe 0
        ScValComparator.compareOptionalScMap(null, m1) shouldBeLessThan 0
        ScValComparator.compareOptionalScMap(m1, null) shouldBeGreaterThan 0
        ScValComparator.compareOptionalScMap(m1, m2) shouldBeLessThan 0
        ScValComparator.compareOptionalScMap(m1, m1) shouldBe 0
      }
    }

    context("Comparator interface") {
      test("INSTANCE.compare delegates to compareScVal") {
        ScValComparator.INSTANCE.compare(Scv.toUint32(1), Scv.toUint32(2)) shouldBeLessThan 0
        ScValComparator.INSTANCE.compare(Scv.toUint32(2), Scv.toUint32(1)) shouldBeGreaterThan 0
        ScValComparator.INSTANCE.compare(Scv.toUint32(1), Scv.toUint32(1)) shouldBe 0
      }
    }

    context("antisymmetry") {
      test("compare(a,b) == -compare(b,a)") {
        val pairs =
          arrayOf(
            arrayOf(Scv.toBoolean(false), Scv.toBoolean(true)),
            arrayOf(Scv.toUint32(1), Scv.toUint32(2)),
            arrayOf(Scv.toInt32(-10), Scv.toInt32(10)),
            arrayOf(Scv.toSymbol("a"), Scv.toSymbol("b")),
            arrayOf(Scv.toBoolean(false), Scv.toUint32(0)),
            arrayOf(Scv.toInt32(0), Scv.toSymbol("x")),
          )
        for ((i, pair) in pairs.withIndex()) {
          ScValComparator.compareScVal(pair[0], pair[1]) shouldBe
            -ScValComparator.compareScVal(pair[1], pair[0])
        }
      }
    }

    context("transitivity") {
      test("a < b < c implies a < c") {
        val a = Scv.toBoolean(true)
        val b = Scv.toUint32(0)
        val c = Scv.toSymbol("x")
        ScValComparator.compareScVal(a, b) shouldBeLessThan 0
        ScValComparator.compareScVal(b, c) shouldBeLessThan 0
        ScValComparator.compareScVal(a, c) shouldBeLessThan 0
      }
    }
  }) {
  companion object {
    private fun bytes32(fill: Int): ByteArray {
      val b = ByteArray(32)
      b.fill(fill.toByte())
      return b
    }

    private fun bytes32Last(last: Int): ByteArray {
      val b = ByteArray(32)
      b[31] = last.toByte()
      return b
    }

    private fun entry(key: SCVal, value: SCVal): SCMapEntry =
      SCMapEntry.builder().key(key).`val`(value).build()

    private fun makeMap(entries: Array<SCMapEntry>): SCVal =
      SCVal.builder().discriminant(SCValType.SCV_MAP).map(SCMap(entries)).build()

    private fun makeErrorContract(code: Int): SCVal =
      SCVal.builder()
        .discriminant(SCValType.SCV_ERROR)
        .error(
          SCError.builder()
            .discriminant(SCErrorType.SCE_CONTRACT)
            .contractCode(Uint32(XdrUnsignedInteger(code.toLong())))
            .build()
        )
        .build()

    private fun makeErrorWasm(code: SCErrorCode): SCVal =
      SCVal.builder()
        .discriminant(SCValType.SCV_ERROR)
        .error(SCError.builder().discriminant(SCErrorType.SCE_WASM_VM).code(code).build())
        .build()

    private fun makeWasmInstance(wasmHash: ByteArray, storage: SCMap?): SCVal =
      SCVal.builder()
        .discriminant(SCValType.SCV_CONTRACT_INSTANCE)
        .instance(
          SCContractInstance.builder()
            .executable(
              ContractExecutable.builder()
                .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_WASM)
                .wasm_hash(Hash(wasmHash))
                .build()
            )
            .storage(storage)
            .build()
        )
        .build()

    private fun makeStellarAssetInstance(storage: SCMap?): SCVal =
      SCVal.builder()
        .discriminant(SCValType.SCV_CONTRACT_INSTANCE)
        .instance(
          SCContractInstance.builder()
            .executable(
              ContractExecutable.builder()
                .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_STELLAR_ASSET)
                .build()
            )
            .storage(storage)
            .build()
        )
        .build()

    private fun makeNonce(v: Long): SCVal =
      SCVal.builder()
        .discriminant(SCValType.SCV_LEDGER_KEY_NONCE)
        .nonce_key(SCNonceKey.builder().nonce(Int64(v)).build())
        .build()

    private fun accountAddress(key: ByteArray): SCAddress =
      SCAddress.builder()
        .discriminant(SCAddressType.SC_ADDRESS_TYPE_ACCOUNT)
        .accountId(
          AccountID(
            PublicKey.builder()
              .discriminant(PublicKeyType.PUBLIC_KEY_TYPE_ED25519)
              .ed25519(Uint256(key))
              .build()
          )
        )
        .build()

    private fun contractAddress(hash: ByteArray): SCAddress =
      SCAddress.builder()
        .discriminant(SCAddressType.SC_ADDRESS_TYPE_CONTRACT)
        .contractId(ContractID(Hash(hash)))
        .build()

    private fun muxedAddress(id: Long, key: ByteArray): SCAddress =
      SCAddress.builder()
        .discriminant(SCAddressType.SC_ADDRESS_TYPE_MUXED_ACCOUNT)
        .muxedAccount(
          MuxedEd25519Account.builder()
            .id(Uint64(XdrUnsignedHyperInteger(BigInteger.valueOf(id))))
            .ed25519(Uint256(key))
            .build()
        )
        .build()

    private fun claimableBalanceAddress(hash: ByteArray): SCAddress =
      SCAddress.builder()
        .discriminant(SCAddressType.SC_ADDRESS_TYPE_CLAIMABLE_BALANCE)
        .claimableBalanceId(
          ClaimableBalanceID.builder()
            .discriminant(ClaimableBalanceIDType.CLAIMABLE_BALANCE_ID_TYPE_V0)
            .v0(Hash(hash))
            .build()
        )
        .build()

    private fun liquidityPoolAddress(hash: ByteArray): SCAddress =
      SCAddress.builder()
        .discriminant(SCAddressType.SC_ADDRESS_TYPE_LIQUIDITY_POOL)
        .liquidityPoolId(PoolID(Hash(hash)))
        .build()
  }
}
