package org.stellar.sdk

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class StrKeyTest :
  FunSpec({
    context("ACCOUNT_ID") {
      test("encode and decode") {
        val rawKey =
          Util.hexToBytes("5223d15964cb25b98d17dfc9cb954a4331617bbaa4e5dc144c87df0b8b3b47d9")
        val encoded = "GBJCHUKZMTFSLOMNC7P4TS4VJJBTCYL3XKSOLXAUJSD56C4LHND5TWUC"
        StrKey.encodeEd25519PublicKey(rawKey) shouldBe encoded
        StrKey.decodeEd25519PublicKey(encoded) shouldBe rawKey
      }

      context("is valid") {
        withData(
          nameFn = { "should be true for $it" },
          "GBBM6BKZPEHWYO3E3YKREDPQXMS4VK35YLNU7NFBRI26RAN7GI5POFBB",
          "GB7KKHHVYLDIZEKYJPAJUOTBE5E3NJAXPSDZK7O6O44WR3EBRO5HRPVT",
          "GD6WVYRVID442Y4JVWFWKWCZKB45UGHJAABBJRS22TUSTWGJYXIUR7N2",
          "GBCG42WTVWPO4Q6OZCYI3D6ZSTFSJIXIS6INCIUF23L6VN3ADE4337AP",
          "GDFX463YPLCO2EY7NGFMI7SXWWDQAMASGYZXCG2LATOF3PP5NQIUKBPT",
          "GBXEODUMM3SJ3QSX2VYUWFU3NRP7BQRC2ERWS7E2LZXDJXL2N66ZQ5PT",
          "GAJHORKJKDDEPYCD6URDFODV7CVLJ5AAOJKR6PG2VQOLWFQOF3X7XLOG",
          "GACXQEAXYBEZLBMQ2XETOBRO4P66FZAJENDHOQRYPUIXZIIXLKMZEXBJ",
          "GDD3XRXU3G4DXHVRUDH7LJM4CD4PDZTVP4QHOO4Q6DELKXUATR657OZV",
          "GDTYVCTAUQVPKEDZIBWEJGKBQHB4UGGXI2SXXUEW7LXMD4B7MK37CWLJ",
        ) { publicKey ->
          StrKey.isValidEd25519PublicKey(publicKey) shouldBe true
        }

        withData(
          nameFn = { "should be false for $it" },
          "GA3D5KRYM6CB7OWOOOORR3Z4T7GNZLKERYNZGGA5SOAOPIFY6YQHES5", // corrupted payload
          "GBPXX0A5N4JYPESHAADMQKBPWZWQDQ64ZV6ZL2S3LAGW4SY7NTCMWIVL", // wrong checksum
          "GCFZB6L25D26RQFDWSSBDEYQ32JHLRMTT44ZYE3DZQUTYOL7WY43PLBG++", // invalid chars
          "GADE5QJ2TY7S5ZB65Q43DFGWYWCPHIYDJ2326KZGAGBN7AE5UY6JVDRRA", // wrong length
          "GB6OWYST45X57HCJY5XWOHDEBULB6XUROWPIKW77L5DSNANBEQGUPADT2", // wrong length
          "GB6OWYST45X57HCJY5XWOHDEBULB6XUROWPIKW77L5DSNANBEQGUPADT2T", // too long
          "GDXIIZTKTLVYCBHURXL2UPMTYXOVNI7BRAEFQCP6EZCY4JLKY4VKFNLT", // wrong checksum
          "SB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZV4E", // wrong version byte
          "gWRYUerEKuz53tstxEuR3NCkiQDcV4wzFHmvLnZmj7PUqxW2wt", // old format
          "test",
          "",
          "g4VPBPrHZkfE8CsjuG2S4yBQNd455UWmk", // Old network key
        ) { publicKey ->
          StrKey.isValidEd25519PublicKey(publicKey) shouldBe false
        }
      }
    }

    context("SEED") {
      test("encode and decode") {
        val rawKey =
          Util.hexToBytes("7dd61ca0d677cd0efc2b5473e9426c7a98b3e64567682d46ee7cbd8007e532bc")
        val encoded = "SB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZV4E"
        StrKey.encodeEd25519SecretSeed(rawKey) shouldBe encoded.toCharArray()
        StrKey.decodeEd25519SecretSeed(encoded.toCharArray()) shouldBe rawKey
      }

      context("should validate valid seeds") {
        withData(
          nameFn = { "should be true for $it" },
          "SAB5556L5AN5KSR5WF7UOEFDCIODEWEO7H2UR4S5R62DFTQOGLKOVZDY",
          "SCZTUEKSEH2VYZQC6VLOTOM4ZDLMAGV4LUMH4AASZ4ORF27V2X64F2S2",
          "SCGNLQKTZ4XCDUGVIADRVOD4DEVNYZ5A7PGLIIZQGH7QEHK6DYODTFEH",
          "SDH6R7PMU4WIUEXSM66LFE4JCUHGYRTLTOXVUV5GUEPITQEO3INRLHER",
          "SC2RDTRNSHXJNCWEUVO7VGUSPNRAWFCQDPP6BGN4JFMWDSEZBRAPANYW",
          "SCEMFYOSFZ5MUXDKTLZ2GC5RTOJO6FGTAJCF3CCPZXSLXA2GX6QUYOA7",
        ) { seed ->
          StrKey.isValidEd25519SecretSeed(seed.toCharArray()) shouldBe true
        }
      }

      context("should reject invalid seeds") {
        withData(
          nameFn = { "should be false for $it" },
          "GBJCHUKZMTFSLOMNC7P4TS4VJJBTCYL3XKSOLXAUJSD56C4LHND5TWUC", // wrong version
          "SAB5556L5AN5KSR5WF7UOEFDCIODEWEO7H2UR4S5R62DFTQOGLKOVZDYT", // too long
          "SAFGAMN5Z6IHVI3IVEPIILS7ITZDYSCEPLN4FN5Z3IY63DRH4CIYEV", // too short
          "SAFGAMN5Z6IHVI3IVEPIILS7ITZDYSCEPLN4FN5Z3IY63DRH4CIYEVIT", // wrong checksum
          "test",
          "",
        ) { seed ->
          StrKey.isValidEd25519SecretSeed(seed.toCharArray()) shouldBe false
        }
      }
    }

    context("PRE_AUTH_TX") {
      test("encode and decode") {
        val rawKey =
          Util.hexToBytes("7dd61ca0d677cd0efc2b5473e9426c7a98b3e64567682d46ee7cbd8007e532bc")
        val encoded = "TB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZM5K"
        StrKey.encodePreAuthTx(rawKey) shouldBe encoded
        StrKey.decodePreAuthTx(encoded) shouldBe rawKey
      }

      test("is valid") {
        StrKey.isValidPreAuthTx("TB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZM5K") shouldBe
          true
        StrKey.isValidPreAuthTx("SAB5556L5AN5KSR5WF7UOEFDCIODEWEO7H2UR4S5R62DFTQOGLKOVZDY") shouldBe
          false
        StrKey.isValidPreAuthTx("TB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZM5") shouldBe
          false
      }
    }

    context("SHA256_HASH") {
      test("encode and decode") {
        val rawKey =
          Util.hexToBytes("7dd61ca0d677cd0efc2b5473e9426c7a98b3e64567682d46ee7cbd8007e532bc")
        val encoded = "XB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLYIYT"
        StrKey.encodeSha256Hash(rawKey) shouldBe encoded
        StrKey.decodeSha256Hash(encoded) shouldBe rawKey
      }

      test("is valid") {
        StrKey.isValidSha256Hash(
          "XB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLYIYT"
        ) shouldBe true
        StrKey.isValidSha256Hash(
          "SAB5556L5AN5KSR5WF7UOEFDCIODEWEO7H2UR4S5R62DFTQOGLKOVZDY"
        ) shouldBe false
        StrKey.isValidSha256Hash("XB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLYIY") shouldBe
          false
      }
    }

    context("CONTRACT") {
      test("encode and decode") {
        val rawKey =
          Util.hexToBytes("3f0c34bf93ad0d9971d04ccc90f705511c838aad9734a4a2fb0d7a03fc7fe89a")
        val encoded = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA"
        StrKey.decodeContract(encoded) shouldBe rawKey
        StrKey.encodeContract(rawKey) shouldBe encoded
      }

      test("is valid") {
        StrKey.isValidContract("CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA") shouldBe
          true
        StrKey.isValidContract("SAB5556L5AN5KSR5WF7UOEFDCIODEWEO7H2UR4S5R62DFTQOGLKOVZDY") shouldBe
          false
        StrKey.isValidContract("CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWD") shouldBe
          false
      }
    }

    context("LIQUIDITY_POOL") {
      test("encode and decode") {
        val rawKey =
          Util.hexToBytes("3f0c34bf93ad0d9971d04ccc90f705511c838aad9734a4a2fb0d7a03fc7fe89a")
        val encoded = "LA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUPJN"
        StrKey.encodeLiquidityPool(rawKey) shouldBe encoded
        StrKey.decodeLiquidityPool(encoded) shouldBe rawKey
      }

      test("is valid") {
        StrKey.isValidLiquidityPool(
          "LA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUPJN"
        ) shouldBe true
        StrKey.isValidLiquidityPool(
          "SAB5556L5AN5KSR5WF7UOEFDCIODEWEO7H2UR4S5R62DFTQOGLKOVZDY"
        ) shouldBe false
        StrKey.isValidLiquidityPool(
          "LA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUPJ"
        ) shouldBe false
      }
    }

    context("CLAIMABLE_BALANCE") {
      test("encode and decode") {
        val rawKey =
          Util.hexToBytes("003f0c34bf93ad0d9971d04ccc90f705511c838aad9734a4a2fb0d7a03fc7fe89a")
        val encoded = "BAAD6DBUX6J22DMZOHIEZTEQ64CVCHEDRKWZONFEUL5Q26QD7R76RGR4TU"
        StrKey.encodeClaimableBalance(rawKey) shouldBe encoded
        StrKey.decodeClaimableBalance(encoded) shouldBe rawKey
      }

      test("is valid") {
        StrKey.isValidClaimableBalance(
          "BAAD6DBUX6J22DMZOHIEZTEQ64CVCHEDRKWZONFEUL5Q26QD7R76RGR4TU"
        ) shouldBe true
        StrKey.isValidClaimableBalance(
          "SAB5556L5AN5KSR5WF7UOEFDCIODEWEO7H2UR4S5R62DFTQOGLKOVZDY"
        ) shouldBe false
        StrKey.isValidClaimableBalance(
          "BAT7RRVQBTQ3TEDLDICV7GNX6GE3KVQFJCNOESUI4QNA4TYYR44P2FS5PZYQ"
        ) shouldBe false
      }
    }

    context("MED25519_PUBLIC_KEY") {
      test("encode and decode") {
        val rawKey =
          Util.hexToBytes(
            "2000757eeae583fc50dd669f97673acc25ec725823ac73faf6c7df31ad31e50900000000000004d2"
          )
        val encoded = "MAQAA5L65LSYH7CQ3VTJ7F3HHLGCL3DSLAR2Y47263D56MNNGHSQSAAAAAAAAAAE2LP26"
        StrKey.encodeMed25519PublicKey(rawKey) shouldBe encoded
        StrKey.decodeMed25519PublicKey(encoded) shouldBe rawKey
      }

      test("is valid") {
        StrKey.isValidMed25519PublicKey(
          "MAQAA5L65LSYH7CQ3VTJ7F3HHLGCL3DSLAR2Y47263D56MNNGHSQSAAAAAAAAAAE2LP26"
        ) shouldBe true
        StrKey.isValidMed25519PublicKey(
          "SAB5556L5AN5KSR5WF7UOEFDCIODEWEO7H2UR4S5R62DFTQOGLKOVZDY"
        ) shouldBe false
        StrKey.isValidMed25519PublicKey(
          "GBJCHUKZMTFSLOMNC7P4TS4VJJBTCYL3XKSOLXAUJSD56C4LHND5TWUC"
        ) shouldBe false
        StrKey.isValidMed25519PublicKey(
          "MAQAA5L65LSYH7CQ3VTJ7F3HHLGCL3DSLAR2Y47263D56MNNGHSQSAAAAAAAAAAE2LP2"
        ) shouldBe false
      }
    }

    context("SIGNED_PAYLOAD") {
      test("encode and decode with 64-byte payload") {
        val rawKey =
          Util.hexToBytes(
            "5223d15964cb25b98d17dfc9cb954a4331617bbaa4e5dc144c87df0b8b3b47d9000000409641ff797553b36084e91ba38e53a2c184e72d1b18a8a7f2c36aa6cb66e473f6396b87b8e305feeaf98a3c3630392632dcf0fa6c10398dbe5352ff662316cf9c"
          )
        val encoded =
          "PBJCHUKZMTFSLOMNC7P4TS4VJJBTCYL3XKSOLXAUJSD56C4LHND5SAAAABAJMQP7PF2VHM3AQTURXI4OKORMDBHHFUNRRKFH6LBWVJWLM3SHH5RZNOD3RYYF73VPTCR4GYYDSJRS3TYPU3AQHGG34U2S75TCGFWPTRYJS"
        StrKey.encodeSignedPayload(rawKey) shouldBe encoded
        StrKey.decodeSignedPayload(encoded) shouldBe rawKey
      }

      test("encode and decode with 1-byte payload") {
        val rawKey =
          Util.hexToBytes(
            "5223d15964cb25b98d17dfc9cb954a4331617bbaa4e5dc144c87df0b8b3b47d90000000106000000"
          )
        val encoded = "PBJCHUKZMTFSLOMNC7P4TS4VJJBTCYL3XKSOLXAUJSD56C4LHND5SAAAAAAQMAAAADDCO"
        StrKey.encodeSignedPayload(rawKey) shouldBe encoded
        StrKey.decodeSignedPayload(encoded) shouldBe rawKey
      }

      context("is valid") {
        test("should be true for valid signed payload") {
          StrKey.isValidSignedPayload(
            "PBJCHUKZMTFSLOMNC7P4TS4VJJBTCYL3XKSOLXAUJSD56C4LHND5SAAAAAAQMAAAADDCO"
          ) shouldBe true
        }
        withData(
          nameFn = { "should be false for $it" },
          "PA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAQACAQDAQCQMBYIBEFAWDANBYHRAEISCMKBKFQXDAMRUGY4DUPB6IAAAAAAAAPM",
          "PA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAOQCAQDAQCQMBYIBEFAWDANBYHRAEISCMKBKFQXDAMRUGY4Z2PQ",
          "PA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAOQCAQDAQCQMBYIBEFAWDANBYHRAEISCMKBKFQXDAMRUGY4DXFH6",
          "PDPYP7E6NEYZSVOTV6M23OFM2XRIMPDUJABHGHHH2Y67X7JL25GW6AAAAAAAAAAAAAAJE",
          "GBJCHUKZMTFSLOMNC7P4TS4VJJBTCYL3XKSOLXAUJSD56C4LHND5TWUC",
        ) { key ->
          StrKey.isValidSignedPayload(key) shouldBe false
        }
      }

      test("decode with invalid payload length - too long") {
        val encoded =
          "PBJCHUKZMTFSLOMNC7P4TS4VJJBTCYL3XKSOLXAUJSD56C4LHND5SAAAABA5P7IKKHQ7JVFVSMJM7EWEBTDTL5XMXMFVVTMG65GUYEHAQCX6JHGGBLBW7EHIJJD7FH7HQL5FLDFBC22QJGTGQEHXVY4GEKCRHGGD7IBAAAAAS6LA"
        shouldThrow<IllegalArgumentException> { StrKey.decodeSignedPayload(encoded) }
          .message shouldBe
          "Invalid data length, the length should be between 40 and 100 bytes, got 104"
      }

      test("decode with invalid payload length - too short") {
        val encoded = "PACLVBTZGFKZBKERFMUDQQRJODWJLFAAQYF7XH6I6SXDYAPXP7LXYM37QTTGBGQI4LCA"
        shouldThrow<IllegalArgumentException> { StrKey.decodeSignedPayload(encoded) }
          .message shouldBe
          "Invalid data length, the length should be between 40 and 100 bytes, got 39"
      }
      // TODO: add encode check
    }

    context("should reject all invalid StrKey cases") {
      // https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0023.md
      withData(
        nameFn = { "should throw IllegalArgumentException for $it" },
        "GAAAAAAAACGC6",
        "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUR",
        "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZA",
        "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUACUSI",
        "G47QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVP2I",
        "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLKA",
        "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAAV75I",
        "M47QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUQ",
        "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUK",
        "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUO",
        "PA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAQACAQDAQCQMBYIBEFAWDANBYHRAEISCMKBKFQXDAMRUGY4DUPB6IAAAAAAAAPM",
        "PA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAOQCAQDAQCQMBYIBEFAWDANBYHRAEISCMKBKFQXDAMRUGY4Z2PQ",
        "PA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAOQCAQDAQCQMBYIBEFAWDANBYHRAEISCMKBKFQXDAMRUGY4DXFH6",
        "BAAD6DBUX6J22DMZOHIEZTEQ64CVCHEDRKWZONFEUL5Q26QD7R76RGR4TV",
        // We should consider this valid
        // "BAAT6DBUX6J22DMZOHIEZTEQ64CVCHEDRKWZONFEUL5Q26QD7R76RGXACA" // Invalid claimable
        // balance type (first byte of binary key is not 0),
        "",
      ) { key ->
        shouldThrow<IllegalArgumentException> { StrKey.decodeEd25519PublicKey(key) }
        shouldThrow<IllegalArgumentException> { StrKey.decodeMed25519PublicKey(key) }
        shouldThrow<IllegalArgumentException> { StrKey.decodeEd25519SecretSeed(key.toCharArray()) }
        shouldThrow<IllegalArgumentException> { StrKey.decodePreAuthTx(key) }
        shouldThrow<IllegalArgumentException> { StrKey.decodeSha256Hash(key) }
        shouldThrow<IllegalArgumentException> { StrKey.decodeSignedPayload(key) }
        shouldThrow<IllegalArgumentException> { StrKey.decodeContract(key) }
        shouldThrow<IllegalArgumentException> { StrKey.decodeLiquidityPool(key) }
        shouldThrow<IllegalArgumentException> { StrKey.decodeClaimableBalance(key) }
      }
    }

    test("decode version byte") {
      StrKey.decodeVersionByte("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR") shouldBe
        StrKey.VersionByte.ACCOUNT_ID
      StrKey.decodeVersionByte(
        "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK"
      ) shouldBe StrKey.VersionByte.MED25519_PUBLIC_KEY
      StrKey.decodeVersionByte("SDJHRQF4GCMIIKAAAQ6IHY42X73FQFLHUULAPSKKD4DFDM7UXWWCRHBE") shouldBe
        StrKey.VersionByte.SEED
      StrKey.decodeVersionByte("TAQCSRX2RIDJNHFIFHWD63X7D7D6TRT5Y2S6E3TEMXTG5W3OECHZ2OG4") shouldBe
        StrKey.VersionByte.PRE_AUTH_TX
      StrKey.decodeVersionByte("XDRPF6NZRR7EEVO7ESIWUDXHAOMM2QSKIQQBJK6I2FB7YKDZES5UCLWD") shouldBe
        StrKey.VersionByte.SHA256_HASH
      StrKey.decodeVersionByte(
        "PDPYP7E6NEYZSVOTV6M23OFM2XRIMPDUJABHGHHH2Y67X7JL25GW6AAAAAAAAAAAAAAJEVA"
      ) shouldBe StrKey.VersionByte.SIGNED_PAYLOAD
      StrKey.decodeVersionByte("CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA") shouldBe
        StrKey.VersionByte.CONTRACT
      StrKey.decodeVersionByte("LA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUPJN") shouldBe
        StrKey.VersionByte.LIQUIDITY_POOL
      StrKey.decodeVersionByte(
        "BAAD6DBUX6J22DMZOHIEZTEQ64CVCHEDRKWZONFEUL5Q26QD7R76RGR4TU"
      ) shouldBe StrKey.VersionByte.CLAIMABLE_BALANCE
    }

    test("decode version byte throws exception for invalid input") {
      val exception =
        shouldThrow<IllegalArgumentException> {
          StrKey.decodeVersionByte("INVALIDBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6INVALID")
        }
      exception.message shouldBe "Version byte is invalid"
    }
  })
