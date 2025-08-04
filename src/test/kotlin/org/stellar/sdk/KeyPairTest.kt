package org.stellar.sdk

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.util.Base64
import org.stellar.sdk.xdr.PublicKeyType

class KeyPairTest :
  FunSpec({
    val testKeypairs =
      mapOf(
        "SDJHRQF4GCMIIKAAAQ6IHY42X73FQFLHUULAPSKKD4DFDM7UXWWCRHBE" to
          "GCZHXL5HXQX5ABDM26LHYRCQZ5OJFHLOPLZX47WEBP3V2PF5AVFK2A5D",
        "SDTQN6XUC3D2Z6TIG3XLUTJIMHOSON2FMSKCTM2OHKKH2UX56RQ7R5Y4" to
          "GDEAOZWTVHQZGGJY6KG4NAGJQ6DXATXAJO3AMW7C4IXLKMPWWB4FDNFZ",
        "SDIREFASXYQVEI6RWCQW7F37E6YNXECQJ4SPIOFMMMJRU5CMDQVW32L5" to
          "GD2EVR7DGDLNKWEG366FIKXO2KCUAIE3HBUQP4RNY7LEZR5LDKBYHMM6",
        "SDAPE6RHEJ7745VQEKCI2LMYKZB3H6H366I33A42DG7XKV57673XLCC2" to
          "GDLXVH2BTLCLZM53GF7ELZFF4BW4MHH2WXEA4Z5Z3O6DPNZNR44A56UJ",
        "SDYZ5IYOML3LTWJ6WIAC2YWORKVO7GJRTPPGGNJQERH72I6ZCQHDAJZN" to
          "GABXJTV7ELEB2TQZKJYEGXBUIG6QODJULKJDI65KZMIZZG2EACJU5EA7",
      )
    val mainSecret = testKeypairs.keys.first()
    val mainAccount = testKeypairs[mainSecret]!!

    context("fromSecretSeed") {
      val invalidSeeds =
        listOf(
          "",
          "hello",
          "SBWUBZ3SIPLLF5CCXLWUB2Z6UBTYAW34KVXOLRQ5HDAZG4ZY7MHNBWJ1",
          "masterpassphrasemasterpassphrase",
          "gsYRSEQhTffqA9opPepAENCr2WG6z5iBHHubxxbRzWaHf8FBWcu",
        )

      withData(
        nameFn = { "should create keypair from secret seed ${it.first}" },
        testKeypairs.toList(),
      ) { (seed, expectedAccountId) ->
        val keypair = KeyPair.fromSecretSeed(seed)

        keypair.canSign() shouldBe true
        keypair.accountId shouldBe expectedAccountId
        keypair.getSecretSeed() shouldBe seed.toCharArray()
      }

      test("should create keypair from char array seed") {
        val original = KeyPair.fromSecretSeed(mainSecret.toCharArray())
        val seedArray = original.getSecretSeed()!!
        val newPair = KeyPair.fromSecretSeed(seedArray)

        original.getSecretSeed() shouldBe newPair.getSecretSeed()
        newPair.accountId shouldBe original.accountId
      }

      withData(nameFn = { "should throw for invalid seed: $it" }, invalidSeeds) { invalidSeed ->
        shouldThrow<IllegalArgumentException> { KeyPair.fromSecretSeed(invalidSeed) }
      }

      withData(nameFn = { "should throw for invalid seed char array: $it" }, invalidSeeds) {
        invalidSeed ->
        shouldThrow<IllegalArgumentException> { KeyPair.fromSecretSeed(invalidSeed.toCharArray()) }
      }
    }

    context("fromAccountId") {
      val invalidAccountIds =
        listOf(
          "",
          "hello",
          "GAXDYNIBA5E4DXR5TJN522RRYESFQ5UNUXHIPTFGVLLD5O5K552DFBAD",
          "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUACUSI",
          "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZA",
          "GAXDYNIBA5E4DXR5TJN522RRYESFQ5UNUXHIPTFGVLLD5O5K552DF5Z",
          "GAH6H2XPCZS27WMKPTZJPTDN7JMBCDHTLU5WQP7TUI2ORA2M5FY5DHNU",
          "masterpassphrasemasterpassphrase",
          "gsYRSEQhTffqA9opPepAENCr2WG6z5iBHHubxxbRzWaHf8FBWcu",
        )

      withData(nameFn = { "should create keypair from account ID $it" }, testKeypairs.values) {
        accountId ->
        val keypair = KeyPair.fromAccountId(accountId)

        keypair.canSign() shouldBe false
        keypair.accountId shouldBe accountId
      }

      withData(nameFn = { "should throw for invalid account ID: $it" }, invalidAccountIds) {
        invalidAccountId ->
        shouldThrow<IllegalArgumentException> { KeyPair.fromAccountId(invalidAccountId) }
      }
    }

    context("fromPublicKey") {
      test("should create keypair from raw public key bytes") {
        val publicKey = StrKey.decodeEd25519PublicKey(mainAccount)
        val keypair = KeyPair.fromPublicKey(publicKey)

        keypair.canSign() shouldBe false
        keypair.accountId shouldBe mainAccount
      }
    }

    context("random") {
      test("should generate random keypair") {
        val keypair = KeyPair.random()

        keypair.canSign() shouldBe true
        keypair.accountId.length shouldBe 56
        keypair.getSecretSeed()!!.size shouldBe 56
      }

      test("should generate different random keypairs") {
        val keypair1 = KeyPair.random()
        val keypair2 = KeyPair.random()

        keypair1.accountId shouldNotBe keypair2.accountId
      }
    }

    context("signing and verification") {
      test("should sign data with expected signature") {
        val keypair = KeyPair.fromSecretSeed(mainSecret)
        val signature = keypair.sign("hello world".toByteArray())
        signature.toHexString() shouldBe
          "694a7db1f37a1cd0e0ac8b83bb01051fa32ceca6dfd677d6b3ba98bd98b3ba601ebd4d3a0df957f691906f293fa581b13728750ee94dfba3c62d6bd5985f300d"
      }

      test("should verify correct signature") {
        val sig =
          "694a7db1f37a1cd0e0ac8b83bb01051fa32ceca6dfd677d6b3ba98bd98b3ba601ebd4d3a0df957f691906f293fa581b13728750ee94dfba3c62d6bd5985f300d"
        val keypair = KeyPair.fromSecretSeed(mainSecret)

        keypair.verify("hello world".toByteArray(), Util.hexToBytes(sig)) shouldBe true
      }

      test("should fail verification with wrong signature") {
        val badSig =
          "687d4b472eeef7d07aafcd0b049640b0bb3f39784118c2e2b73a04fa2f64c9c538b4b2d0f5335e968a480021fdc23e98c0ddf424cb15d8131df8cb6c4bb58309"
        val keypair = KeyPair.fromSecretSeed(mainSecret)

        keypair.verify("hello world".toByteArray(), Util.hexToBytes(badSig)) shouldBe false
        keypair.verify("hello world".toByteArray(), byteArrayOf(0x00)) shouldBe false
      }

      test("should check canSign capability") {
        val secretKeypair = KeyPair.fromSecretSeed(mainSecret)
        val publicKeypair = KeyPair.fromAccountId(mainAccount)

        secretKeypair.canSign() shouldBe true
        publicKeypair.canSign() shouldBe false
      }

      test("should throw when signing without private key") {
        val keypair = KeyPair.fromAccountId(mainAccount)

        shouldThrow<IllegalStateException> { keypair.sign("hello world".toByteArray()) }
          .message shouldBe
          "KeyPair does not contain secret key. Use KeyPair.fromSecretSeed method to create a new KeyPair with a secret key."
      }
    }

    context("decorated signatures") {
      val testKeypair = KeyPair.fromSecretSeed(mainSecret)

      test("should generate signature hint") {
        val keypair = KeyPair.fromSecretSeed(mainSecret)
        val signatureHint = keypair.getSignatureHint()

        signatureHint.signatureHint.toHexString() shouldBe "bd054aad"
      }

      test("should create decorated signature") {
        val decoratedSignature = testKeypair.signDecorated("hello world".toByteArray())

        decoratedSignature.hint.signatureHint.toHexString() shouldBe "bd054aad"
        decoratedSignature.signature.signature.toHexString() shouldBe
          "694a7db1f37a1cd0e0ac8b83bb01051fa32ceca6dfd677d6b3ba98bd98b3ba601ebd4d3a0df957f691906f293fa581b13728750ee94dfba3c62d6bd5985f300d"
      }
    }

    context("SEP-53 message signing") {
      val messageKeypair =
        KeyPair.fromSecretSeed("SAKICEVQLYWGSOJS4WW7HZJWAHZVEEBS527LHK5V4MLJALYKICQCJXMW")

      data class MessageTestCase(val input: List<Byte>, val expectedSigHex: String)

      withData(
        nameFn = { case -> "should sign and verify message: ${case.input}" },
        listOf(
          MessageTestCase(
            "Hello, World!".toByteArray(Charsets.UTF_8).toList(),
            "7cee5d6d885752104c85eea421dfdcb95abf01f1271d11c4bec3fcbd7874dccd6e2e98b97b8eb23b643cac4073bb77de5d07b0710139180ae9f3cbba78f2ba04",
          ),
          MessageTestCase(
            "こんにちは、世界！".toByteArray(Charsets.UTF_8).toList(),
            "083536eb95ecf32dce59b07fe7a1fd8cf814b2ce46f40d2a16e4ea1f6cecd980e04e6fbef9d21f98011c785a81edb85f3776a6e7d942b435eb0adc07da4d4604",
          ),
          MessageTestCase(
            Base64.getDecoder().decode("2zZDP1sa1BVBfLP7TeeMk3sUbaxAkUhBhDiNdrksaFo=").toList(),
            "540d7eee179f370bf634a49c1fa9fe4a58e3d7990b0207be336c04edfcc539ff8bd0c31bb2c0359b07c9651cb2ae104e4504657b5d17d43c69c7e50e23811b0d",
          ),
        ),
      ) { case ->
        val inputAsByteArray = case.input.toByteArray()
        val expectedSig = Util.hexToBytes(case.expectedSigHex)
        messageKeypair.signMessage(inputAsByteArray) shouldBe expectedSig
        messageKeypair.verifyMessage(inputAsByteArray, expectedSig) shouldBe true
      }

      withData(
        nameFn = { "should fail message verification with invalid signature: $it" },
        listOf(
          "540d7eee179f370bf634a49c1fa9fe4a58e3d7990b0207be336c04edfcc539ff8bd0c31bb2c0359b07c9651cb2ae104e4504657b5d17d43c69c7e50e23811b0d",
          "3132",
        ),
      ) { sigHex ->
        val keypair = KeyPair.fromAccountId(mainAccount)
        keypair.verifyMessage("Hello, World!", Util.hexToBytes(sigHex)) shouldBe false
      }
    }

    context("XDR operations") {
      val testKeypair = KeyPair.fromSecretSeed(mainSecret)

      test("should generate XDR public key") {
        val xdrPublicKey = testKeypair.getXdrPublicKey()

        xdrPublicKey.discriminant shouldBe PublicKeyType.PUBLIC_KEY_TYPE_ED25519
        testKeypair.publicKey shouldBe xdrPublicKey.ed25519.uint256
      }

      test("should generate XDR account ID") {
        val xdrAccountId = testKeypair.getXdrAccountId()
        testKeypair.publicKey shouldBe xdrAccountId.accountID.ed25519.uint256
      }

      test("should create keypair from XDR public key") {
        val xdrPublicKey = testKeypair.getXdrPublicKey()
        val recreatedKeypair = KeyPair.fromXdrPublicKey(xdrPublicKey)

        recreatedKeypair.accountId shouldBe testKeypair.accountId
      }
    }

    context("equality") {
      test("should be equal for same secret keypairs") {
        val keypair1 = KeyPair.fromSecretSeed(mainSecret)
        val keypair2 = KeyPair.fromSecretSeed(mainSecret)

        keypair1 shouldBe keypair2
        keypair1.hashCode() shouldBe keypair2.hashCode()
      }

      test("should be equal for same public-only keypairs") {
        val keypair1 = KeyPair.fromAccountId(mainAccount)
        val keypair2 = KeyPair.fromAccountId(mainAccount)

        keypair1 shouldBe keypair2
        keypair1.hashCode() shouldBe keypair2.hashCode()
      }

      test("should not be equal for public vs private keypairs") {
        val secretKeypair = KeyPair.fromSecretSeed(mainSecret)
        val publicKeypair = KeyPair.fromAccountId(mainAccount)

        secretKeypair shouldNotBe publicKeypair
      }
    }

    context("BIP39 derivation") {
      // kitchen lucky again risk rose master place barrel cancel danger boil want
      val bip39SeedHex =
        "c8a66fe0a9119f3f84d50d1128d568cf78703b9fe8d45f5e0b702e24c7170b0988f9617c952e29b0ce3f4f8e666187893c6ada2119205945f3e0596ce3922598"
      val bip39Accounts =
        listOf(
          "GBQ33BQ3GIOMLDW6I4LXBKURQDR77XGUGZIGG6WWI5EFOQ62DJUOW7J7" to
            "SDXDWQL7CEYVCVJD3K6GW7RHUUR3XKNQCRBQZJSG6K5LFOOXXAXLG3LH",
          "GDXDB4HICXSQWW6JVAI33ZYSE5R4ICOZ47Q4KE6IADP5UEUVKEXOV2CM" to
            "SAXL6344C5Z7OPIXJJE4I4JZPCLPZLF2M3P4HAS6A2LUY4KX3PN25DTK",
          "GCV7AYC3NPXL4ZRVSITZOHWKLCMY6SZ253T66H3PI66O73TUVWEYBMX7" to
            "SBPAJWWJJMPPOY2R726MFC7XHT4WXMJ2YCML6EB46UJZC5SIHCTLETT4",
        )

      withData(
        nameFn = { (index, account, _) ->
          "should create keypair from BIP39 seed for account $index: $account"
        },
        bip39Accounts.mapIndexed { index, (account, secret) -> Triple(index, account, secret) },
      ) { (index, expectedAccount, expectedSecret) ->
        val keypair = KeyPair.fromBip39Seed(Util.hexToBytes(bip39SeedHex), index)

        keypair.canSign() shouldBe true
        keypair.accountId shouldBe expectedAccount
        keypair.getSecretSeed() shouldBe expectedSecret.toCharArray()
      }
    }
  })
