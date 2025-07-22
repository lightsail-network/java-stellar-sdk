package org.stellar.sdk

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.io.ByteArrayInputStream
import java.util.Base64
import org.stellar.sdk.xdr.SCAddress
import org.stellar.sdk.xdr.SCVal
import org.stellar.sdk.xdr.XdrDataInputStream

class AddressTest :
  FunSpec({
    val accountId = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ"
    val contractId = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA"
    val muxedAccountId = "MAQAA5L65LSYH7CQ3VTJ7F3HHLGCL3DSLAR2Y47263D56MNNGHSQSAAAAAAAAAAE2LP26"
    val claimableBalanceId = "BAAD6DBUX6J22DMZOHIEZTEQ64CVCHEDRKWZONFEUL5Q26QD7R76RGR4TU"
    val liquidityPoolId = "LA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUPJN"
    val secretKey = "SB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZV4E"
    val invalidAddress = "GINVALID"

    context("Address constructor") {
      test("should create address from account ID") {
        val address = Address(accountId)
        address.toString() shouldBe accountId
        address.addressType shouldBe Address.AddressType.ACCOUNT
      }

      test("should create address from contract ID") {
        val address = Address(contractId)
        address.toString() shouldBe contractId
        address.addressType shouldBe Address.AddressType.CONTRACT
      }

      test("should create address from muxed account ID") {
        val address = Address(muxedAccountId)
        address.toString() shouldBe muxedAccountId
        address.addressType shouldBe Address.AddressType.MUXED_ACCOUNT
      }

      test("should create address from claimable balance ID") {
        val address = Address(claimableBalanceId)
        address.toString() shouldBe claimableBalanceId
        address.addressType shouldBe Address.AddressType.CLAIMABLE_BALANCE
      }

      test("should create address from liquidity pool ID") {
        val address = Address(liquidityPoolId)
        address.toString() shouldBe liquidityPoolId
        address.addressType shouldBe Address.AddressType.LIQUIDITY_POOL
      }

      test("should throw exception for invalid address") {
        val exception = shouldThrow<IllegalArgumentException> { Address(invalidAddress) }
        exception.message shouldBe "Unsupported address type"
      }

      test("should throw exception for secret key") {
        val exception = shouldThrow<IllegalArgumentException> { Address(secretKey) }
        exception.message shouldBe "Unsupported address type"
      }
    }

    context("Address factory methods with byte arrays") {
      test("should create address from account bytes") {
        val accountIdBytes = StrKey.decodeEd25519PublicKey(accountId)
        val address = Address.fromAccount(accountIdBytes)
        address.toString() shouldBe accountId
        address.addressType shouldBe Address.AddressType.ACCOUNT
      }

      test("should create address from contract bytes") {
        val contractIdBytes = StrKey.decodeContract(contractId)
        val address = Address.fromContract(contractIdBytes)
        address.toString() shouldBe contractId
        address.addressType shouldBe Address.AddressType.CONTRACT
      }

      test("should create address from muxed account bytes") {
        val muxedAccountIdBytes = StrKey.decodeMed25519PublicKey(muxedAccountId)
        val address = Address.fromMuxedAccount(muxedAccountIdBytes)
        address.toString() shouldBe muxedAccountId
        address.addressType shouldBe Address.AddressType.MUXED_ACCOUNT
      }

      test("should create address from claimable balance bytes") {
        val claimableBalanceIdBytes = StrKey.decodeClaimableBalance(claimableBalanceId)
        val address = Address.fromClaimableBalance(claimableBalanceIdBytes)
        address.toString() shouldBe claimableBalanceId
        address.addressType shouldBe Address.AddressType.CLAIMABLE_BALANCE
      }

      test("should create address from liquidity pool bytes") {
        val liquidityPoolIdBytes = StrKey.decodeLiquidityPool(liquidityPoolId)
        val address = Address.fromLiquidityPool(liquidityPoolIdBytes)
        address.toString() shouldBe liquidityPoolId
        address.addressType shouldBe Address.AddressType.LIQUIDITY_POOL
      }
    }

    context("SCAddress conversion") {
      test("should convert account to SCAddress") {
        val address = Address(accountId)
        val scAddress = address.toSCAddress()

        val expectedXdr = "AAAAAAAAAAA/DDS/k60NmXHQTMyQ9wVRHIOKrZc0pKL7DXoD/H/omg=="
        val expectedBytes = Base64.getDecoder().decode(expectedXdr)
        val expectedScAddress =
          SCAddress.decode(XdrDataInputStream(ByteArrayInputStream(expectedBytes)))

        scAddress shouldBe expectedScAddress
      }

      test("should convert contract to SCAddress") {
        val address = Address(contractId)
        val scAddress = address.toSCAddress()

        val expectedXdr = "AAAAAT8MNL+TrQ2ZcdBMzJD3BVEcg4qtlzSkovsNegP8f+ia"
        val expectedBytes = Base64.getDecoder().decode(expectedXdr)
        val expectedScAddress =
          SCAddress.decode(XdrDataInputStream(ByteArrayInputStream(expectedBytes)))

        scAddress shouldBe expectedScAddress
      }

      test("should convert muxed account to SCAddress") {
        val address = Address(muxedAccountId)
        val scAddress = address.toSCAddress()

        val expectedXdr = "AAAAAiAAdX7q5YP8UN1mn5dnOswl7HJYI6xz+vbH3zGtMeUJAAAAAAAABNI="
        val expectedBytes = Base64.getDecoder().decode(expectedXdr)
        val expectedScAddress =
          SCAddress.decode(XdrDataInputStream(ByteArrayInputStream(expectedBytes)))

        scAddress shouldBe expectedScAddress
      }

      test("should convert claimable balance to SCAddress") {
        val address = Address(claimableBalanceId)
        val scAddress = address.toSCAddress()

        val expectedXdr = "AAAAAwAAAAA/DDS/k60NmXHQTMyQ9wVRHIOKrZc0pKL7DXoD/H/omg=="
        val expectedBytes = Base64.getDecoder().decode(expectedXdr)
        val expectedScAddress =
          SCAddress.decode(XdrDataInputStream(ByteArrayInputStream(expectedBytes)))

        scAddress shouldBe expectedScAddress
      }

      test("should convert liquidity pool to SCAddress") {
        val address = Address(liquidityPoolId)
        val scAddress = address.toSCAddress()

        val expectedXdr = "AAAABD8MNL+TrQ2ZcdBMzJD3BVEcg4qtlzSkovsNegP8f+ia"
        val expectedBytes = Base64.getDecoder().decode(expectedXdr)
        val expectedScAddress =
          SCAddress.decode(XdrDataInputStream(ByteArrayInputStream(expectedBytes)))

        scAddress shouldBe expectedScAddress
      }
    }

    context("SCAddress to Address conversion") {
      test("should create address from account SCAddress") {
        val xdr = "AAAAAAAAAAA/DDS/k60NmXHQTMyQ9wVRHIOKrZc0pKL7DXoD/H/omg=="
        val bytes = Base64.getDecoder().decode(xdr)
        val scAddress = SCAddress.decode(XdrDataInputStream(ByteArrayInputStream(bytes)))

        val address = Address.fromSCAddress(scAddress)
        address.toString() shouldBe accountId
        address.addressType shouldBe Address.AddressType.ACCOUNT
      }

      test("should create address from contract SCAddress") {
        val xdr = "AAAAAT8MNL+TrQ2ZcdBMzJD3BVEcg4qtlzSkovsNegP8f+ia"
        val bytes = Base64.getDecoder().decode(xdr)
        val scAddress = SCAddress.decode(XdrDataInputStream(ByteArrayInputStream(bytes)))

        val address = Address.fromSCAddress(scAddress)
        address.toString() shouldBe contractId
        address.addressType shouldBe Address.AddressType.CONTRACT
      }

      test("should create address from muxed account SCAddress") {
        val xdr = "AAAAAiAAdX7q5YP8UN1mn5dnOswl7HJYI6xz+vbH3zGtMeUJAAAAAAAABNI="
        val bytes = Base64.getDecoder().decode(xdr)
        val scAddress = SCAddress.decode(XdrDataInputStream(ByteArrayInputStream(bytes)))

        val address = Address.fromSCAddress(scAddress)
        address.toString() shouldBe muxedAccountId
        address.addressType shouldBe Address.AddressType.MUXED_ACCOUNT
      }

      test("should create address from claimable balance SCAddress") {
        val xdr = "AAAAAwAAAAA/DDS/k60NmXHQTMyQ9wVRHIOKrZc0pKL7DXoD/H/omg=="
        val bytes = Base64.getDecoder().decode(xdr)
        val scAddress = SCAddress.decode(XdrDataInputStream(ByteArrayInputStream(bytes)))

        val address = Address.fromSCAddress(scAddress)
        address.toString() shouldBe claimableBalanceId
        address.addressType shouldBe Address.AddressType.CLAIMABLE_BALANCE
      }

      test("should create address from liquidity pool SCAddress") {
        val xdr = "AAAABD8MNL+TrQ2ZcdBMzJD3BVEcg4qtlzSkovsNegP8f+ia"
        val bytes = Base64.getDecoder().decode(xdr)
        val scAddress = SCAddress.decode(XdrDataInputStream(ByteArrayInputStream(bytes)))

        val address = Address.fromSCAddress(scAddress)
        address.toString() shouldBe liquidityPoolId
        address.addressType shouldBe Address.AddressType.LIQUIDITY_POOL
      }
    }

    context("SCVal conversion") {
      test("should convert address to SCVal") {
        val address = Address(contractId)
        val scVal = address.toSCVal()

        val expectedXdr = "AAAAEgAAAAE/DDS/k60NmXHQTMyQ9wVRHIOKrZc0pKL7DXoD/H/omg=="
        val expectedBytes = Base64.getDecoder().decode(expectedXdr)
        val expectedSCVal = SCVal.decode(XdrDataInputStream(ByteArrayInputStream(expectedBytes)))

        scVal shouldBe expectedSCVal
      }

      test("should create address from SCVal") {
        val xdr = "AAAAEgAAAAE/DDS/k60NmXHQTMyQ9wVRHIOKrZc0pKL7DXoD/H/omg=="
        val bytes = Base64.getDecoder().decode(xdr)
        val scVal = SCVal.decode(XdrDataInputStream(ByteArrayInputStream(bytes)))

        val address = Address.fromSCVal(scVal)
        address.toString() shouldBe contractId
        address.addressType shouldBe Address.AddressType.CONTRACT
      }
    }
  })
