package org.stellar.sdk

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.stellar.sdk.xdr.AssetType

class TrustLineAssetTest :
  FunSpec({
    context("Classic Asset") {
      test("should create TrustLineAsset with native asset") {
        val asset = AssetTypeNative()
        val trustLineAsset = TrustLineAsset(asset)

        trustLineAsset.assetType shouldBe AssetType.ASSET_TYPE_NATIVE
        trustLineAsset.asset shouldBe asset
        trustLineAsset.liquidityPoolId.shouldBeNull()

        val expectedXdr = "AAAAAA=="
        trustLineAsset.toXdr().toXdrBase64() shouldBe expectedXdr

        val xdr = trustLineAsset.toXdr()
        val parsedTrustLineAsset = TrustLineAsset.fromXdr(xdr)
        parsedTrustLineAsset shouldBe trustLineAsset
      }

      test("should create TrustLineAsset with alphaNum4 asset") {
        val asset = Asset.create("ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO")
        val trustLineAsset = TrustLineAsset(asset)

        trustLineAsset.assetType shouldBe AssetType.ASSET_TYPE_CREDIT_ALPHANUM4
        trustLineAsset.asset shouldBe asset
        trustLineAsset.liquidityPoolId.shouldBeNull()

        val expectedXdr = "AAAAAUFSU1QAAAAAfzBiNMmJ6dZ/ad/ZMRmLYA89bOa2TCJAcB+2KwiDK4c="
        trustLineAsset.toXdr().toXdrBase64() shouldBe expectedXdr

        val xdr = trustLineAsset.toXdr()
        val parsedTrustLineAsset = TrustLineAsset.fromXdr(xdr)
        parsedTrustLineAsset shouldBe trustLineAsset
      }

      test("should create TrustLineAsset with alphaNum12 asset") {
        val asset =
          Asset.create("ARST123123:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO")
        val trustLineAsset = TrustLineAsset(asset)

        trustLineAsset.assetType shouldBe AssetType.ASSET_TYPE_CREDIT_ALPHANUM12
        trustLineAsset.asset shouldBe asset
        trustLineAsset.liquidityPoolId.shouldBeNull()

        val expectedXdr = "AAAAAkFSU1QxMjMxMjMAAAAAAAB/MGI0yYnp1n9p39kxGYtgDz1s5rZMIkBwH7YrCIMrhw=="
        trustLineAsset.toXdr().toXdrBase64() shouldBe expectedXdr

        val xdr = trustLineAsset.toXdr()
        val parsedTrustLineAsset = TrustLineAsset.fromXdr(xdr)
        parsedTrustLineAsset shouldBe trustLineAsset
      }
    }

    context("Liquidity Pool Share") {
      test("should create TrustLineAsset with liquidity pool ID") {
        val poolId = "dd7b1ab831c273310ddbec6f97870aa83c2fbd78ce22aded37ecbf4f3380faca"
        val trustLineAsset = TrustLineAsset(poolId)

        trustLineAsset.assetType shouldBe AssetType.ASSET_TYPE_POOL_SHARE
        trustLineAsset.asset.shouldBeNull()
        trustLineAsset.liquidityPoolId shouldBe poolId

        val expectedXdr = "AAAAA917GrgxwnMxDdvsb5eHCqg8L714ziKt7Tfsv08zgPrK"
        trustLineAsset.toXdr().toXdrBase64() shouldBe expectedXdr

        // Test round-trip XDR conversion
        val xdr = trustLineAsset.toXdr()
        val parsedTrustLineAsset = TrustLineAsset.fromXdr(xdr)
        parsedTrustLineAsset shouldBe trustLineAsset
      }

      test("should normalize liquidity pool ID to lowercase") {
        val poolIdUppercase = "DD7B1AB831C273310DDBEC6F97870AA83C2FBD78CE22ADED37ECBF4F3380FACA"
        val poolIdLowercase = "dd7b1ab831c273310ddbec6f97870aa83c2fbd78ce22aded37ecbf4f3380faca"
        val trustLineAsset = TrustLineAsset(poolIdUppercase)

        trustLineAsset.liquidityPoolId shouldBe poolIdLowercase
      }

      test("should handle mixed case liquidity pool ID") {
        val poolIdMixedCase = "Dd7B1aB831c273310DdBeC6f97870Aa83C2FbD78cE22adeD37ecBf4F3380fAcA"
        val poolIdLowercase = "dd7b1ab831c273310ddbec6f97870aa83c2fbd78ce22aded37ecbf4f3380faca"
        val trustLineAsset = TrustLineAsset(poolIdMixedCase)

        trustLineAsset.liquidityPoolId shouldBe poolIdLowercase
      }

      test("should handle short liquidity pool ID") {
        val shortPoolId = "dd7b1ab831c273310ddbec6f97870aa83c2fbd78ce22aded37ecbf4f3380fac"
        val exception = shouldThrow<Exception> { TrustLineAsset(shortPoolId) }
        exception.message shouldBe "Liquidity pool ID must be a 64-character hexadecimal string"
      }

      test("should handle long liquidity pool ID") {
        val longPoolId = "dd7b1ab831c273310ddbec6f97870aa83c2fbd78ce22aded37ecbf4f3380facaa"
        val exception = shouldThrow<Exception> { TrustLineAsset(longPoolId) }
        exception.message shouldBe "Liquidity pool ID must be a 64-character hexadecimal string"
      }
    }

    context("Equality and HashCode") {
      test("should be equal when same asset") {
        val asset = Asset.create("USD:GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ")
        val trustLineAsset1 = TrustLineAsset(asset)
        val trustLineAsset2 = TrustLineAsset(asset)

        trustLineAsset1 shouldBe trustLineAsset2
        trustLineAsset1.hashCode() shouldBe trustLineAsset2.hashCode()
      }

      test("should be equal when same liquidity pool ID") {
        val poolId = "dd7b1ab831c273310ddbec6f97870aa83c2fbd78ce22aded37ecbf4f3380faca"
        val trustLineAsset1 = TrustLineAsset(poolId)
        val trustLineAsset2 = TrustLineAsset(poolId)

        trustLineAsset1 shouldBe trustLineAsset2
        trustLineAsset1.hashCode() shouldBe trustLineAsset2.hashCode()
      }

      test("should be equal when liquidity pool ID case differs") {
        val poolIdLower = "dd7b1ab831c273310ddbec6f97870aa83c2fbd78ce22aded37ecbf4f3380faca"
        val poolIdUpper = "DD7B1AB831C273310DDBEC6F97870AA83C2FBD78CE22ADED37ECBF4F3380FACA"
        val trustLineAsset1 = TrustLineAsset(poolIdLower)
        val trustLineAsset2 = TrustLineAsset(poolIdUpper)

        trustLineAsset1 shouldBe trustLineAsset2
        trustLineAsset1.hashCode() shouldBe trustLineAsset2.hashCode()
      }

      test("should not be equal when different assets") {
        val asset1 = Asset.create("USD:GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ")
        val asset2 = Asset.create("EUR:GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ")
        val trustLineAsset1 = TrustLineAsset(asset1)
        val trustLineAsset2 = TrustLineAsset(asset2)

        trustLineAsset1 shouldNotBe trustLineAsset2
      }

      test("should not be equal when different liquidity pool IDs") {
        val poolId1 = "dd7b1ab831c273310ddbec6f97870aa83c2fbd78ce22aded37ecbf4f3380faca"
        val poolId2 = "ee8c2bc942d384421eecfd7fa8981bb94d3cce89df33befe48fdc5e4491fb1db"
        val trustLineAsset1 = TrustLineAsset(poolId1)
        val trustLineAsset2 = TrustLineAsset(poolId2)

        trustLineAsset1 shouldNotBe trustLineAsset2
      }

      test("should not be equal when asset vs liquidity pool") {
        val asset = Asset.create("USD:GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ")
        val poolId = "dd7b1ab831c273310ddbec6f97870aa83c2fbd78ce22aded37ecbf4f3380faca"
        val assetTrustLine = TrustLineAsset(asset)
        val poolTrustLine = TrustLineAsset(poolId)

        assetTrustLine shouldNotBe poolTrustLine
      }
    }
  })
