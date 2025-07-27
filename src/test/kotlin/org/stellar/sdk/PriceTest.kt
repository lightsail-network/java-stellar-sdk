package org.stellar.sdk

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

data class PriceTestCase(
  val input: String,
  val expectedNumerator: Int,
  val expectedDenominator: Int,
)

class PriceTest :
  FunSpec({
    context("Price.fromString()") {
      withData(
        nameFn = {
          "should parse '${it.input}' to ${it.expectedNumerator}/${it.expectedDenominator}"
        },
        PriceTestCase("0", 0, 1),
        PriceTestCase("0.1", 1, 10),
        PriceTestCase("0.01", 1, 100),
        PriceTestCase("0.001", 1, 1000),
        PriceTestCase("543.01793", 54301793, 100000),
        PriceTestCase("319.69983", 31969983, 100000),
        PriceTestCase("0.93", 93, 100),
        PriceTestCase("0.5", 1, 2),
        PriceTestCase("1.730", 173, 100),
        PriceTestCase("0.85334384", 5333399, 6250000),
        PriceTestCase("5.5", 11, 2),
        PriceTestCase("2.72783", 272783, 100000),
        PriceTestCase("638082.0", 638082, 1),
        PriceTestCase("2.93850088", 36731261, 12500000),
        PriceTestCase("58.04", 1451, 25),
        PriceTestCase("41.265", 8253, 200),
        PriceTestCase("5.1476", 12869, 2500),
        PriceTestCase("95.14", 4757, 50),
        PriceTestCase("0.74580", 3729, 5000),
        PriceTestCase("4119.0", 4119, 1),
        PriceTestCase("1073742464.5", 1073742464, 1),
        PriceTestCase("1635962526.2", 1635962526, 1),
        PriceTestCase("2147483647", 2147483647, 1),
      ) { testCase ->
        val price = Price.fromString(testCase.input)
        price.numerator shouldBe testCase.expectedNumerator
        price.denominator shouldBe testCase.expectedDenominator
        Price.fromXdr(price.toXdr()) shouldBe price
      }
    }

    context("Price constructor") {
      test("should create price with numerator and denominator") {
        val price = Price(3, 4)
        price.numerator shouldBe 3
        price.denominator shouldBe 4
      }
    }

    context("Price.toString()") {
      test("should return decimal representation") {
        Price(1, 2).toString() shouldBe "0.5"
        Price(3, 4).toString() shouldBe "0.75"
        Price(5, 1).toString() shouldBe "5"
        Price(0, 1).toString() shouldBe "0"
      }
    }
  })
