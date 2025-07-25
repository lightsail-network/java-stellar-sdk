package org.stellar.sdk

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.stellar.sdk.xdr.AssetType

class AssetTest :
  FunSpec({
    val validIssuer = "GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ"
    val anotherValidIssuer = "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO"
    val invalidIssuer = "GCEZWKCA5"

    context("Native") {
      test("should create native asset") {
        val asset = AssetTypeNative()
        asset.type shouldBe AssetType.ASSET_TYPE_NATIVE
        asset.toString() shouldBe "native"
      }

      test("should create native asset using factory method") {
        val asset = Asset.createNativeAsset()
        (asset is AssetTypeNative) shouldBe true
        asset.type shouldBe AssetType.ASSET_TYPE_NATIVE
      }

      test("should create native asset from canonical form") {
        val asset = Asset.create("native")
        (asset is AssetTypeNative) shouldBe true
        asset.type shouldBe AssetType.ASSET_TYPE_NATIVE
      }

      test("should convert to XDR and back") {
        val asset = AssetTypeNative()
        val xdr = asset.toXdr()
        val parsedAsset = Asset.fromXdr(xdr)
        (parsedAsset is AssetTypeNative) shouldBe true
        parsedAsset shouldBe asset
      }
    }

    context("AssetTypeCreditAlphaNum4") {
      context("constructor validation") {
        test("should create valid 1-character asset") {
          val asset = AssetTypeCreditAlphaNum4("A", validIssuer)
          asset.code shouldBe "A"
          asset.issuer shouldBe validIssuer
          asset.type shouldBe AssetType.ASSET_TYPE_CREDIT_ALPHANUM4
        }

        test("should create valid 4-character asset") {
          val asset = AssetTypeCreditAlphaNum4("ABCD", validIssuer)
          asset.code shouldBe "ABCD"
          asset.issuer shouldBe validIssuer
          asset.type shouldBe AssetType.ASSET_TYPE_CREDIT_ALPHANUM4
        }

        test("should throw exception for empty code") {
          val exception =
            shouldThrow<IllegalArgumentException> { AssetTypeCreditAlphaNum4("", validIssuer) }
          exception.message shouldBe "The length of code must be between 1 and 4 characters."
        }

        test("should throw exception for code longer than 4 characters") {
          val exception =
            shouldThrow<IllegalArgumentException> {
              AssetTypeCreditAlphaNum4("TOOLONG", validIssuer)
            }
          exception.message shouldBe "The length of code must be between 1 and 4 characters."
        }

        test("should throw exception for invalid issuer") {
          val exception =
            shouldThrow<IllegalArgumentException> { AssetTypeCreditAlphaNum4("USD", invalidIssuer) }
          exception.message shouldBe "Invalid issuer: $invalidIssuer"
        }
      }

      test("should create from factory method") {
        val asset = Asset.createNonNativeAsset("USD", validIssuer)
        (asset is AssetTypeCreditAlphaNum4) shouldBe true
        val alphaNum4 = asset as AssetTypeCreditAlphaNum4
        alphaNum4.code shouldBe "USD"
        alphaNum4.issuer shouldBe validIssuer
      }

      test("should create from canonical form") {
        val asset = Asset.create("USD:$validIssuer")
        (asset is AssetTypeCreditAlphaNum4) shouldBe true
        val alphaNum4 = asset as AssetTypeCreditAlphaNum4
        alphaNum4.code shouldBe "USD"
        alphaNum4.issuer shouldBe validIssuer
      }

      test("should convert to XDR and back") {
        val asset = AssetTypeCreditAlphaNum4("USD", validIssuer)
        val xdr = asset.toXdr()
        val parsedAsset = Asset.fromXdr(xdr)
        (parsedAsset is AssetTypeCreditAlphaNum4) shouldBe true
        val parsedAlphaNum4 = parsedAsset as AssetTypeCreditAlphaNum4
        parsedAlphaNum4.code shouldBe "USD"
        parsedAlphaNum4.issuer shouldBe validIssuer
        parsedAsset shouldBe asset
      }

      test("should have correct toString format") {
        val asset = AssetTypeCreditAlphaNum4("USD", validIssuer)
        asset.toString() shouldBe "USD:$validIssuer"
      }
    }

    context("AssetTypeCreditAlphaNum12") {
      context("constructor validation") {
        test("should create valid 5-character asset") {
          val asset = AssetTypeCreditAlphaNum12("ABCDE", validIssuer)
          asset.code shouldBe "ABCDE"
          asset.issuer shouldBe validIssuer
          asset.type shouldBe AssetType.ASSET_TYPE_CREDIT_ALPHANUM12
        }

        test("should create valid 12-character asset") {
          val asset = AssetTypeCreditAlphaNum12("ABCDEFGHIJKL", validIssuer)
          asset.code shouldBe "ABCDEFGHIJKL"
          asset.issuer shouldBe validIssuer
          asset.type shouldBe AssetType.ASSET_TYPE_CREDIT_ALPHANUM12
        }

        test("should throw exception for code shorter than 5 characters") {
          val exception =
            shouldThrow<IllegalArgumentException> { AssetTypeCreditAlphaNum12("ABCD", validIssuer) }
          exception.message shouldBe "The length of code must be between 5 and 12 characters."
        }

        test("should throw exception for code longer than 12 characters") {
          val exception =
            shouldThrow<IllegalArgumentException> {
              AssetTypeCreditAlphaNum12("TOOLONGASSETCODE", validIssuer)
            }
          exception.message shouldBe "The length of code must be between 5 and 12 characters."
        }

        test("should throw exception for invalid issuer") {
          val exception =
            shouldThrow<IllegalArgumentException> {
              AssetTypeCreditAlphaNum12("LONGCODE", invalidIssuer)
            }
          exception.message shouldBe "Invalid issuer: $invalidIssuer"
        }
      }

      test("should create from factory method") {
        val asset = Asset.createNonNativeAsset("TESTASSET", validIssuer)
        (asset is AssetTypeCreditAlphaNum12) shouldBe true
        val alphaNum12 = asset as AssetTypeCreditAlphaNum12
        alphaNum12.code shouldBe "TESTASSET"
        alphaNum12.issuer shouldBe validIssuer
      }

      test("should create from canonical form") {
        val asset = Asset.create("TESTASSET:$validIssuer")
        (asset is AssetTypeCreditAlphaNum12) shouldBe true
        val alphaNum12 = asset as AssetTypeCreditAlphaNum12
        alphaNum12.code shouldBe "TESTASSET"
        alphaNum12.issuer shouldBe validIssuer
      }

      test("should convert to XDR and back") {
        val asset = AssetTypeCreditAlphaNum12("TESTASSET", validIssuer)
        val xdr = asset.toXdr()
        val parsedAsset = Asset.fromXdr(xdr)
        (parsedAsset is AssetTypeCreditAlphaNum12) shouldBe true
        val parsedAlphaNum12 = parsedAsset as AssetTypeCreditAlphaNum12
        parsedAlphaNum12.code shouldBe "TESTASSET"
        parsedAlphaNum12.issuer shouldBe validIssuer
        parsedAsset shouldBe asset
      }

      test("should have correct toString format") {
        val asset = AssetTypeCreditAlphaNum12("TESTASSET", validIssuer)
        asset.toString() shouldBe "TESTASSET:$validIssuer"
      }
    }

    context("Asset equality and hashCode") {
      test("should be equal when same type and properties") {
        val native1 = AssetTypeNative()
        val native2 = AssetTypeNative()
        native1 shouldBe native2
        native1.hashCode() shouldBe native2.hashCode()

        val alphanum4A = AssetTypeCreditAlphaNum4("USD", validIssuer)
        val alphanum4B = AssetTypeCreditAlphaNum4("USD", validIssuer)
        alphanum4A shouldBe alphanum4B
        alphanum4A.hashCode() shouldBe alphanum4B.hashCode()

        val alphanum12A = AssetTypeCreditAlphaNum12("TESTASSET", validIssuer)
        val alphanum12B = AssetTypeCreditAlphaNum12("TESTASSET", validIssuer)
        alphanum12A shouldBe alphanum12B
        alphanum12A.hashCode() shouldBe alphanum12B.hashCode()
      }

      test("should not be equal when different types") {
        val native = AssetTypeNative()
        val alphanum4 = AssetTypeCreditAlphaNum4("USD", validIssuer)
        val alphanum12 = AssetTypeCreditAlphaNum12("TESTASSET", validIssuer)

        native shouldNotBe alphanum4
        native shouldNotBe alphanum12
        alphanum4 shouldNotBe alphanum12
        native.hashCode() shouldNotBe alphanum4.hashCode()
        native.hashCode() shouldNotBe alphanum12.hashCode()
        alphanum4.hashCode() shouldNotBe alphanum12.hashCode()
      }

      test("should not be equal when different codes") {
        val asset1 = AssetTypeCreditAlphaNum4("USD", validIssuer)
        val asset2 = AssetTypeCreditAlphaNum4("EUR", validIssuer)
        asset1 shouldNotBe asset2
        asset1.hashCode() shouldNotBe asset2.hashCode()

        val asset3 = AssetTypeCreditAlphaNum12("TESTASSET", validIssuer)
        val asset4 = AssetTypeCreditAlphaNum12("ANOTHERASSET", validIssuer)
        asset3 shouldNotBe asset4
        asset3.hashCode() shouldNotBe asset4.hashCode()
      }

      test("should not be equal when different issuers") {
        val asset1 = AssetTypeCreditAlphaNum4("USD", validIssuer)
        val asset2 = AssetTypeCreditAlphaNum4("USD", anotherValidIssuer)
        asset1 shouldNotBe asset2
        asset1.hashCode() shouldNotBe asset2.hashCode()

        val asset3 = AssetTypeCreditAlphaNum12("TESTASSET", validIssuer)
        val asset4 = AssetTypeCreditAlphaNum12("TESTASSET", anotherValidIssuer)
        asset3 shouldNotBe asset4
        asset3.hashCode() shouldNotBe asset4.hashCode()
      }
    }

    context("Asset comparison and sorting") {
      test("should compare to 0 if assets are equal") {
        val native1 = AssetTypeNative()
        val native2 = AssetTypeNative()
        native1.compareTo(native2) shouldBe 0

        val alphanum4A = AssetTypeCreditAlphaNum4("USD", validIssuer)
        val alphanum4B = AssetTypeCreditAlphaNum4("USD", validIssuer)
        alphanum4A.compareTo(alphanum4B) shouldBe 0

        val alphanum12A = AssetTypeCreditAlphaNum12("TESTASSET", validIssuer)
        val alphanum12B = AssetTypeCreditAlphaNum12("TESTASSET", validIssuer)
        alphanum12A.compareTo(alphanum12B) shouldBe 0
      }

      test("should order by asset type: native < alphanum4 < alphanum12") {
        val native = AssetTypeNative()
        val alphanum4 = AssetTypeCreditAlphaNum4("USD", validIssuer)
        val alphanum12 = AssetTypeCreditAlphaNum12("TESTASSET", validIssuer)

        // native < alphanum4
        native.compareTo(alphanum4) shouldBeLessThan 0
        alphanum4.compareTo(native) shouldBeGreaterThan 0

        // native < alphanum12
        native.compareTo(alphanum12) shouldBeLessThan 0
        alphanum12.compareTo(native) shouldBeGreaterThan 0

        // alphanum4 < alphanum12
        alphanum4.compareTo(alphanum12) shouldBeLessThan 0
        alphanum12.compareTo(alphanum4) shouldBeGreaterThan 0
      }

      test("should order by asset code when same type") {
        val assetA = AssetTypeCreditAlphaNum4("ARST", validIssuer)
        val assetB = AssetTypeCreditAlphaNum4("USD", validIssuer)

        assetA.compareTo(assetB) shouldBeLessThan 0
        assetB.compareTo(assetA) shouldBeGreaterThan 0

        val assetC = AssetTypeCreditAlphaNum12("ABCDFG", validIssuer)
        val assetD = AssetTypeCreditAlphaNum12("HIJKLM", validIssuer)
        assetC.compareTo(assetD) shouldBeLessThan 0
        assetD.compareTo(assetC) shouldBeGreaterThan 0
      }

      test("should order uppercase before lowercase") {
        val assetUpper = AssetTypeCreditAlphaNum4("ARST", validIssuer)
        val assetLower = AssetTypeCreditAlphaNum4("arst", validIssuer)

        assetUpper.compareTo(assetLower) shouldBeLessThan 0
        assetLower.compareTo(assetUpper) shouldBeGreaterThan 0

        val assetUpper12 = AssetTypeCreditAlphaNum12("ABCDE", validIssuer)
        val assetLower12 = AssetTypeCreditAlphaNum12("abcde", validIssuer)
        assetUpper12.compareTo(assetLower12) shouldBeLessThan 0
        assetLower12.compareTo(assetUpper12) shouldBeGreaterThan 0
      }

      test("should order by issuer when same code") {
        val asset1 =
          AssetTypeCreditAlphaNum4(
            "USD",
            "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO",
          )
        val asset2 =
          AssetTypeCreditAlphaNum4(
            "USD",
            "GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ",
          )

        asset1.compareTo(asset2) shouldBeLessThan 0
        asset2.compareTo(asset1) shouldBeGreaterThan 0

        val asset3 =
          AssetTypeCreditAlphaNum12(
            "TESTASSET",
            "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO",
          )
        val asset4 =
          AssetTypeCreditAlphaNum12(
            "TESTASSET",
            "GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ",
          )
        asset3.compareTo(asset4) shouldBeLessThan 0
        asset4.compareTo(asset3) shouldBeGreaterThan 0
      }

      test("should sort assets correctly") {
        val native = AssetTypeNative()
        val alphanum4First =
          AssetTypeCreditAlphaNum4(
            "ABCD",
            "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO",
          )
        val alphanum4Second =
          AssetTypeCreditAlphaNum4(
            "BCDE",
            "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO",
          )
        val alphanum12 =
          AssetTypeCreditAlphaNum12(
            "ABCDEFGH",
            "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO",
          )

        val assets = listOf(alphanum12, alphanum4Second, native, alphanum4First)
        val sortedAssets = assets.sorted()

        sortedAssets shouldBe listOf(native, alphanum4First, alphanum4Second, alphanum12)
      }
    }

    context("Asset creation methods") {
      test("should create asset from canonical form with invalid format") {
        val exception1 = shouldThrow<IllegalArgumentException> { Asset.create("invalidformat") }
        exception1.message shouldBe "invalid asset invalidformat"

        val exception2 = shouldThrow<IllegalArgumentException> { Asset.create("USD:ISSUER:EXTRA") }
        exception2.message shouldBe "invalid asset USD:ISSUER:EXTRA"
      }

      test("should create asset with create method using type parameter") {
        val nativeAsset = Asset.create("native", null, null)
        (nativeAsset is AssetTypeNative) shouldBe true

        val alphaNum4Asset = Asset.create(null, "USD", validIssuer)
        (alphaNum4Asset is AssetTypeCreditAlphaNum4) shouldBe true

        val explicitAlphaNum4Asset = Asset.create("credit_alphanum4", "USD", validIssuer)
        (explicitAlphaNum4Asset is AssetTypeCreditAlphaNum4) shouldBe true
      }

      test("should throw exception for invalid asset code length") {
        val exception =
          shouldThrow<IllegalArgumentException> { Asset.createNonNativeAsset("", validIssuer) }
        exception.message shouldBe "The length of code must be between 1 and 12 characters."

        val exception2 =
          shouldThrow<IllegalArgumentException> {
            Asset.createNonNativeAsset("TOOLONGASSETCODE123", validIssuer)
          }
        exception2.message shouldBe "The length of code must be between 1 and 12 characters."
      }
    }

    context("Contract ID generation") {
      test("should generate correct contract ID for native asset") {
        val asset = AssetTypeNative()
        val testnetContractId = asset.getContractId(Network.TESTNET)
        testnetContractId shouldBe "CDLZFC3SYJYDZT7K67VZ75HPJVIEUVNIXF47ZG2FB2RMQQVU2HHGCYSC"
        val publicContractId = asset.getContractId(Network.PUBLIC)
        publicContractId shouldBe "CAS3J7GYLGXMF6TDJBBYYSE3HQ6BBSMLNUQ34T6TZMYMW2EVH34XOWMA"
      }

      test("should generate correct contract ID for alphanum4 asset") {
        val asset =
          AssetTypeCreditAlphaNum4(
            "USD",
            "GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ",
          )
        val testnetContractId = asset.getContractId(Network.TESTNET)
        testnetContractId shouldBe "CBIMQ4GRFDK27OR6MUIK2VO2ZXRFPWATPNCAHPTUS5MSJCUB3G2MBHGV"
        val publicContractId = asset.getContractId(Network.PUBLIC)
        publicContractId shouldBe "CBPDHP5DT6HQ6KPGHMULFSU2NOHEPAACU44B6B5ISCNVRDM62M4B7INK"
      }

      test("should generate correct contract ID for alphanum12 asset") {
        val asset =
          AssetTypeCreditAlphaNum12(
            "TESTASSET",
            "GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ",
          )
        val testnetContractId = asset.getContractId(Network.TESTNET)
        testnetContractId shouldBe "CAQHXOK2TFOQ6OQFAJ5HL5VG54OGSQJNGGSBQ3SHBPXJONWTSJXRL6P5"
        val publicContractId = asset.getContractId(Network.PUBLIC)
        publicContractId shouldBe "CAYBCFNYUBURFRBK2WL52QXDHV42WK33ZQHZZI3SQ5KWEXB55OLLYL6F"
      }
    }
  })
