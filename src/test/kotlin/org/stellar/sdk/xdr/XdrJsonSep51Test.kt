package org.stellar.sdk.xdr

import com.google.gson.JsonParser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import org.stellar.sdk.StrKey

private const val ACCOUNT_ID = "GAQAA5L65LSYH7CQ3VTJ7F3HHLGCL3DSLAR2Y47263D56MNNGHSQSTVY"
private const val CONTRACT_ID = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA"
private const val MUXED_ACCOUNT_ID =
  "MAQAA5L65LSYH7CQ3VTJ7F3HHLGCL3DSLAR2Y47263D56MNNGHSQSAAAAAAAAAAE2LP26"
private const val CLAIMABLE_BALANCE_ID =
  "BAAD6DBUX6J22DMZOHIEZTEQ64CVCHEDRKWZONFEUL5Q26QD7R76RGR4TU"
private const val LIQUIDITY_POOL_ID = "LA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUPJN"
private val TEST_BYTES = ByteArray(32) { it.toByte() }
private val ALT_TEST_BYTES = ByteArray(32) { (31 - it).toByte() }

private fun uint32(value: Long): Uint32 = Uint32(XdrUnsignedInteger(value))

private fun uint64(value: Long): Uint64 = Uint64(XdrUnsignedHyperInteger(value))

private fun uint64(value: BigInteger): Uint64 = Uint64(XdrUnsignedHyperInteger(value))

private fun assetCode4(value: String): AssetCode4 =
  AssetCode4(value.toByteArray(StandardCharsets.US_ASCII).copyOf(4))

private fun assetCode12(value: String): AssetCode12 =
  AssetCode12(value.toByteArray(StandardCharsets.US_ASCII).copyOf(12))

private fun scSymbol(value: String): SCSymbol = SCSymbol(XdrString(value))

private fun scValSymbol(value: String): SCVal =
  SCVal().apply {
    discriminant = SCValType.SCV_SYMBOL
    sym = scSymbol(value)
  }

private fun scValU32(value: Long): SCVal =
  SCVal().apply {
    discriminant = SCValType.SCV_U32
    u32 = uint32(value)
  }

private fun scValBool(value: Boolean): SCVal =
  SCVal().apply {
    discriminant = SCValType.SCV_BOOL
    b = value
  }

private fun scValVoid(): SCVal = SCVal().apply { discriminant = SCValType.SCV_VOID }

private fun scValAddress(strKey: String): SCVal =
  SCVal().apply {
    discriminant = SCValType.SCV_ADDRESS
    address = SCAddress.fromJson("\"$strKey\"")
  }

private fun scValNonceKey(nonce: Long): SCVal =
  SCVal().apply {
    discriminant = SCValType.SCV_LEDGER_KEY_NONCE
    nonce_key = SCNonceKey(Int64(nonce))
  }

class XdrJsonSep51Test :
  FunSpec({
    context("generator-driven scalar semantics") {
      test("enum names drop XDR prefixes in JSON") {
        AssetType.ASSET_TYPE_NATIVE.toJson() shouldBe "\"native\""
        AssetType.ASSET_TYPE_CREDIT_ALPHANUM4.toJson() shouldBe "\"credit_alphanum4\""
        AssetType.fromJson("\"credit_alphanum4\"") shouldBe AssetType.ASSET_TYPE_CREDIT_ALPHANUM4
      }

      test("scalar typedefs and opaque values use the expected JSON forms") {
        uint32(XdrUnsignedInteger.MAX_VALUE).toJson() shouldBe "4294967295"
        Int32(Int.MIN_VALUE).toJson() shouldBe "-2147483648"
        uint64(XdrUnsignedHyperInteger.MAX_VALUE).toJson() shouldBe "\"18446744073709551615\""
        Int64(Long.MIN_VALUE).toJson() shouldBe "\"-9223372036854775808\""
        Duration(uint64(3600)).toJson() shouldBe "\"3600\""
        SequenceNumber(Int64(123456789)).toJson() shouldBe "\"123456789\""
        TimePoint(uint64(1700000000)).toJson() shouldBe "\"1700000000\""

        val zeroHash = Hash(ByteArray(32))
        zeroHash.toJson() shouldBe "\"${"00".repeat(32)}\""
        Hash.fromJson(zeroHash.toJson()) shouldBe zeroHash

        val dataValue = DataValue("hello".toByteArray(StandardCharsets.UTF_8))
        dataValue.toJson() shouldBe "\"68656c6c6f\""
        DataValue.fromJson(dataValue.toJson()) shouldBe dataValue
      }

      test("escaped ascii helpers preserve special characters") {
        val bytes =
          byteArrayOf('A'.code.toByte(), 0, '\\'.code.toByte(), '\n'.code.toByte(), 0xff.toByte())
        val escaped = XdrElement.bytesToEscapedAscii(bytes)

        escaped shouldBe "A\\0\\\\\\n\\xff"
        XdrElement.escapedAsciiToBytes(escaped).contentEquals(bytes) shouldBe true
        XdrString.fromJson("\"A\\\\0\\\\\\\\\\\\n\\\\xff\"") shouldBe XdrString(bytes)
      }
    }

    context("generator-driven struct semantics") {
      test("optional struct fields stay JSON null") {
        val preconditions =
          PreconditionsV2(null, null, null, Duration(uint64(0)), uint32(0), emptyArray())

        val json = JsonParser.parseString(preconditions.toJson()).asJsonObject

        json.get("time_bounds").isJsonNull shouldBe true
        json.get("ledger_bounds").isJsonNull shouldBe true
        json.get("min_seq_num").isJsonNull shouldBe true
        json.get("min_seq_age").asString shouldBe "0"
        json.get("min_seq_ledger_gap").asLong shouldBe 0L
        json.get("extra_signers").asJsonArray.size() shouldBe 0

        PreconditionsV2.fromJson(preconditions.toJson()) shouldBe preconditions
      }

      test("optional struct fields round-trip when populated") {
        val hashXSigner = StrKey.encodeSha256Hash(ALT_TEST_BYTES)
        val preconditions =
          PreconditionsV2(
            TimeBounds(TimePoint(uint64(100)), TimePoint(uint64(200))),
            LedgerBounds(uint32(10), uint32(20)),
            SequenceNumber(Int64(42)),
            Duration(uint64(60)),
            uint32(5),
            arrayOf(SignerKey.fromJson("\"$ACCOUNT_ID\""), SignerKey.fromJson("\"$hashXSigner\"")),
          )

        val json = JsonParser.parseString(preconditions.toJson()).asJsonObject
        val extraSigners = json.get("extra_signers").asJsonArray

        json.get("time_bounds").asJsonObject.get("min_time").asString shouldBe "100"
        json.get("ledger_bounds").asJsonObject.get("max_ledger").asLong shouldBe 20L
        json.get("min_seq_num").asString shouldBe "42"
        extraSigners.size() shouldBe 2
        extraSigners.get(0).asString shouldBe ACCOUNT_ID
        extraSigners.get(1).asString shouldBe hashXSigner

        PreconditionsV2.fromJson(preconditions.toJson()) shouldBe preconditions
      }

      test("wide integer parts use decimal strings") {
        val uint128 = UInt128Parts(uint64(1), uint64(BigInteger.valueOf(2)))
        val uint128Max =
          UInt128Parts(
            uint64(XdrUnsignedHyperInteger.MAX_VALUE),
            uint64(XdrUnsignedHyperInteger.MAX_VALUE),
          )
        val int128Min = Int128Parts(Int64(Long.MIN_VALUE), uint64(0))
        val uint256Large = UInt256Parts(uint64(1), uint64(0), uint64(0), uint64(0))
        val int256 =
          Int256Parts(
            Int64(-1),
            uint64(XdrUnsignedHyperInteger.MAX_VALUE),
            uint64(XdrUnsignedHyperInteger.MAX_VALUE),
            uint64(XdrUnsignedHyperInteger.MAX_VALUE),
          )

        uint128.toJson() shouldBe "\"18446744073709551618\""
        UInt128Parts.fromJson(uint128.toJson()) shouldBe uint128

        uint128Max.toJson() shouldBe "\"340282366920938463463374607431768211455\""
        UInt128Parts.fromJson(uint128Max.toJson()) shouldBe uint128Max

        int128Min.toJson() shouldBe "\"-170141183460469231731687303715884105728\""
        Int128Parts.fromJson(int128Min.toJson()) shouldBe int128Min

        uint256Large.toJson() shouldBe "\"${BigInteger.ONE.shiftLeft(192)}\""
        UInt256Parts.fromJson(uint256Large.toJson()) shouldBe uint256Large

        int256.toJson() shouldBe "\"-1\""
        Int256Parts.fromJson(int256.toJson()) shouldBe int256
      }
    }

    context("generator-driven union semantics") {
      test("void unions use string sentinels rather than null") {
        val memoNone = Memo().apply { discriminant = MemoType.MEMO_NONE }
        val preconditionsNone =
          Preconditions().apply { discriminant = PreconditionType.PRECOND_NONE }

        JsonParser.parseString(memoNone.toJson()).asString shouldBe "none"
        Memo.fromJson(memoNone.toJson()) shouldBe memoNone

        JsonParser.parseString(preconditionsNone.toJson()).asString shouldBe "none"
        Preconditions.fromJson(preconditionsNone.toJson()) shouldBe preconditionsNone
      }

      test("single-key unions ignore schema metadata") {
        val json =
          """
          {
            "${'$'}schema": "https://stellar.org/sep-0051/example",
            "credit_alphanum4": {
              "asset_code": "USD",
              "issuer": "$ACCOUNT_ID"
            }
          }
          """
            .trimIndent()

        val parsed = Asset.fromJson(json)
        val expected =
          Asset().apply {
            discriminant = AssetType.ASSET_TYPE_CREDIT_ALPHANUM4
            alphaNum4 =
              AlphaNum4().apply {
                assetCode = assetCode4("USD")
                issuer = AccountID.fromJson("\"$ACCOUNT_ID\"")
              }
          }

        parsed shouldBe expected
        parsed.toJson() shouldBe
          """{"credit_alphanum4":{"asset_code":"USD","issuer":"$ACCOUNT_ID"}}"""
      }

      test("single-key unions reject invalid string and multi-key shapes") {
        val invalidString =
          shouldThrow<IllegalArgumentException> { Asset.fromJson("\"credit_alphanum4\"") }
        val multiKey =
          shouldThrow<IllegalArgumentException> {
            Asset.fromJson("""{"credit_alphanum4":{},"credit_alphanum12":{}}""")
          }

        invalidString.message!! shouldContain "Unexpected string"
        multiKey.message!! shouldContain "Expected a single-key object"
      }

      test("dict-only and void-only unions round-trip through their JSON forms") {
        val assetCode =
          AssetCode().apply {
            discriminant = AssetType.ASSET_TYPE_CREDIT_ALPHANUM12
            assetCode12 = assetCode12("LONGASSET")
          }
        val extensionPoint = ExtensionPoint(0)

        AssetCode.fromJson(assetCode.toJson()) shouldBe assetCode
        ExtensionPoint.fromJson(extensionPoint.toJson()) shouldBe extensionPoint

        AllowTrustResultCode.values().forEach { code ->
          val result = AllowTrustResult().apply { discriminant = code }
          AllowTrustResult.fromJson(result.toJson()) shouldBe result
        }

        shouldThrow<IllegalArgumentException> {
            AssetCode.fromJson("""{"credit_alphanum4":"USD","credit_alphanum12":"LONGASSET"}""")
          }
          .message!! shouldContain "Expected a single-key object"
      }
    }

    context("stellar specific encodings") {
      test("stellar specific JSON round-trips through strkey forms") {
        val accountId = AccountID.fromJson("\"$ACCOUNT_ID\"")
        val contractAddress = SCAddress.fromJson("\"$CONTRACT_ID\"")
        val claimableBalanceId = ClaimableBalanceID.fromJson("\"$CLAIMABLE_BALANCE_ID\"")

        accountId.toJson() shouldBe "\"$ACCOUNT_ID\""

        contractAddress.discriminant shouldBe SCAddressType.SC_ADDRESS_TYPE_CONTRACT
        contractAddress.toJson() shouldBe "\"$CONTRACT_ID\""

        claimableBalanceId.discriminant shouldBe ClaimableBalanceIDType.CLAIMABLE_BALANCE_ID_TYPE_V0
        claimableBalanceId.toJson() shouldBe "\"$CLAIMABLE_BALANCE_ID\""
      }

      test("additional strkey specializations round-trip through JSON") {
        val preAuthTx = StrKey.encodePreAuthTx(TEST_BYTES)
        val hashX = StrKey.encodeSha256Hash(ALT_TEST_BYTES)

        val publicKey = PublicKey.fromJson("\"$ACCOUNT_ID\"")
        val muxedAccount = MuxedAccount.fromJson("\"$MUXED_ACCOUNT_ID\"")
        val poolId = PoolID.fromJson("\"$LIQUIDITY_POOL_ID\"")
        val preAuthSigner = SignerKey.fromJson("\"$preAuthTx\"")
        val hashXSigner = SignerKey.fromJson("\"$hashX\"")

        publicKey.discriminant shouldBe PublicKeyType.PUBLIC_KEY_TYPE_ED25519
        publicKey.toJson() shouldBe "\"$ACCOUNT_ID\""

        muxedAccount.discriminant shouldBe CryptoKeyType.KEY_TYPE_MUXED_ED25519
        muxedAccount.toJson() shouldBe "\"$MUXED_ACCOUNT_ID\""

        poolId.toJson() shouldBe "\"$LIQUIDITY_POOL_ID\""

        preAuthSigner.discriminant shouldBe SignerKeyType.SIGNER_KEY_TYPE_PRE_AUTH_TX
        preAuthSigner.toJson() shouldBe "\"$preAuthTx\""

        hashXSigner.discriminant shouldBe SignerKeyType.SIGNER_KEY_TYPE_HASH_X
        hashXSigner.toJson() shouldBe "\"$hashX\""
      }

      test("asset codes preserve null padding rules in JSON") {
        val code4 = assetCode4("USD")
        val code12 = assetCode12("ABCDE")

        code4.toJson() shouldBe "\"USD\""
        AssetCode4.fromJson(code4.toJson()) shouldBe code4

        code12.toJson() shouldBe "\"ABCDE\""
        AssetCode12.fromJson(code12.toJson()) shouldBe code12
      }
    }

    context("recursive SCVal JSON") {
      test("representative SCVal arms encode to the expected JSON shapes") {
        val samples =
          listOf(
            scValBool(true) to """{"bool":true}""",
            SCVal().apply {
              discriminant = SCValType.SCV_I32
              i32 = Int32(-1)
            } to """{"i32":-1}""",
            SCVal().apply {
              discriminant = SCValType.SCV_U64
              u64 = uint64(XdrUnsignedHyperInteger.MAX_VALUE)
            } to """{"u64":"18446744073709551615"}""",
            SCVal().apply {
              discriminant = SCValType.SCV_TIMEPOINT
              timepoint = TimePoint(uint64(1700000000))
            } to """{"timepoint":"1700000000"}""",
            SCVal().apply {
              discriminant = SCValType.SCV_BYTES
              bytes = SCBytes(byteArrayOf(0xca.toByte(), 0xfe.toByte()))
            } to """{"bytes":"cafe"}""",
            SCVal().apply {
              discriminant = SCValType.SCV_STRING
              str =
                SCString(
                  XdrString(
                    byteArrayOf(
                      'a'.code.toByte(),
                      0,
                      'b'.code.toByte(),
                      '\n'.code.toByte(),
                      'c'.code.toByte(),
                    )
                  )
                )
            } to """{"string":"a\\0b\\nc"}""",
            SCVal().apply {
              discriminant = SCValType.SCV_SYMBOL
              sym = scSymbol("transfer")
            } to """{"symbol":"transfer"}""",
            scValAddress(CONTRACT_ID) to """{"address":"$CONTRACT_ID"}""",
            scValNonceKey(999) to """{"ledger_key_nonce":{"nonce":"999"}}""",
          )

        samples.forEach { (value, expectedJson) ->
          JsonParser.parseString(value.toJson()) shouldBe JsonParser.parseString(expectedJson)
          SCVal.fromJson(value.toJson()) shouldBe value
        }

        shouldThrow<IllegalArgumentException> { SCVal.fromJson("\"u32\"") }.message!! shouldContain
          "Unexpected string"
        shouldThrow<IllegalArgumentException> { SCVal.fromJson("""{"u32":1,"i32":2}""") }
          .message!! shouldContain "Expected a single-key object"
      }

      test("SCVal round-trips nested containers and special arms") {
        val nested =
          SCVal().apply {
            discriminant = SCValType.SCV_MAP
            map =
              SCMap(
                arrayOf(
                  SCMapEntry().apply {
                    key = scValSymbol("items")
                    setVal(
                      SCVal().apply {
                        discriminant = SCValType.SCV_VEC
                        vec = SCVec(arrayOf(scValU32(1), scValBool(true), scValVoid()))
                      }
                    )
                  },
                  SCMapEntry().apply {
                    key = scValSymbol("owner")
                    setVal(scValAddress(ACCOUNT_ID))
                  },
                  SCMapEntry().apply {
                    key = scValSymbol("nonce")
                    setVal(scValNonceKey(999))
                  },
                )
              )
          }

        val json = JsonParser.parseString(nested.toJson()).asJsonObject

        json.get("map").asJsonArray.size() shouldBe 3
        SCVal.fromJson(nested.toJson()) shouldBe nested

        val ledgerKey = SCVal.fromJson("\"ledger_key_contract_instance\"")
        ledgerKey.discriminant shouldBe SCValType.SCV_LEDGER_KEY_CONTRACT_INSTANCE
        ledgerKey.toJson() shouldBe "\"ledger_key_contract_instance\""
      }
    }

    context("transaction envelopes") {
      test("real transaction envelope round-trips through JSON") {
        val xdr =
          "AAAAAgAAAADmmSZkwY3163TMouB2TY8MljqXw2IxVYTGyvDrR6YtAAAqmmQAABpuAAAAAQAAAAAAAAAAAAAAAQAAAAAAAAAYAAAAAQAAAAEAAAAAAAAAAQAAAAAAAAABAAAAAAAAAAAAAAABAAAABgAAAAHXkotywnA8z+r365/0701QSlWouXn8m0UOoshCtNHOYQAAABQAAAABAAI9fQAAAAAAAAD4AAAAAAAqmgAAAAABR6YtAAAAAEArDtxbqUI+CsdkRmV0lFhVt0wyB7fyrmmkM6Fr35wpPcK8WKcXeKTl4BQ+akE14MZtpaea9LMdhXopaW3pJA0E"
        val expectedJson =
          """
          {
            "tx": {
              "tx": {
                "source_account": "GDTJSJTEYGG7L23UZSROA5SNR4GJMOUXYNRDCVMEY3FPB22HUYWQBZIA",
                "fee": 2792036,
                "seq_num": "29059748724737",
                "cond": "none",
                "memo": "none",
                "operations": [
                  {
                    "source_account": null,
                    "body": {
                      "invoke_host_function": {
                        "host_function": {
                          "create_contract": {
                            "contract_id_preimage": {
                              "asset": "native"
                            },
                            "executable": "stellar_asset"
                          }
                        },
                        "auth": []
                      }
                    }
                  }
                ],
                "ext": {
                  "v1": {
                    "ext": "v0",
                    "resources": {
                      "footprint": {
                        "read_only": [],
                        "read_write": [
                          {
                            "contract_data": {
                              "contract": "CDLZFC3SYJYDZT7K67VZ75HPJVIEUVNIXF47ZG2FB2RMQQVU2HHGCYSC",
                              "key": "ledger_key_contract_instance",
                              "durability": "persistent"
                            }
                          }
                        ]
                      },
                      "instructions": 146813,
                      "disk_read_bytes": 0,
                      "write_bytes": 248
                    },
                    "resource_fee": "2791936"
                  }
                }
              },
              "signatures": [
                {
                  "hint": "47a62d00",
                  "signature": "2b0edc5ba9423e0ac764466574945855b74c3207b7f2ae69a433a16bdf9c293dc2bc58a71778a4e5e0143e6a4135e0c66da5a79af4b31d857a29696de9240d04"
                }
              ]
            }
          }
          """
            .trimIndent()

        val envelope = TransactionEnvelope.fromXdrBase64(xdr)

        JsonParser.parseString(envelope.toJson()) shouldBe JsonParser.parseString(expectedJson)
        TransactionEnvelope.fromJson(envelope.toJson()) shouldBe envelope
      }
    }
  })
