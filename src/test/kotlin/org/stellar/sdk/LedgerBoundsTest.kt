package org.stellar.sdk

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class LedgerBoundsTest :
  FunSpec({
    test("should throw IllegalArgumentException for negative minLedger") {
      val exception = shouldThrow<IllegalArgumentException> { LedgerBounds(-1, 300) }
      exception.message shouldBe "minLedger must be between 0 and 2^32-1"
    }

    test("should throw IllegalArgumentException for negative maxLedger") {
      val exception = shouldThrow<IllegalArgumentException> { LedgerBounds(1, -300) }
      exception.message shouldBe "maxLedger must be between 0 and 2^32-1"
    }

    test("should throw IllegalArgumentException for minLedger greater than uint32 max") {
      val exception = shouldThrow<IllegalArgumentException> { LedgerBounds(4294967296L, 300) }
      exception.message shouldBe "minLedger must be between 0 and 2^32-1"
    }

    test("should throw IllegalArgumentException for maxLedger greater than uint32 max") {
      val exception = shouldThrow<IllegalArgumentException> { LedgerBounds(1, 4294967296L) }
      exception.message shouldBe "maxLedger must be between 0 and 2^32-1"
    }

    test("should throw IllegalArgumentException when minLedger greater than maxLedger") {
      val exception = shouldThrow<IllegalArgumentException> { LedgerBounds(300, 1) }
      exception.message shouldBe "minLedger can not be greater than maxLedger"
    }

    test("should allow minLedger greater than maxLedger when maxLedger is zero") {
      val ledgerBounds = LedgerBounds(300, 0)
      ledgerBounds.minLedger shouldBe 300
      ledgerBounds.maxLedger shouldBe 0
      LedgerBounds.fromXdr(ledgerBounds.toXdr()) shouldBe ledgerBounds
    }

    test("should create valid LedgerBounds") {
      val ledgerBounds = LedgerBounds(300, 400)
      ledgerBounds.minLedger shouldBe 300
      ledgerBounds.maxLedger shouldBe 400
      LedgerBounds.fromXdr(ledgerBounds.toXdr()) shouldBe ledgerBounds
    }

    test("should handle zero values") {
      val ledgerBounds = LedgerBounds(0, 0)
      ledgerBounds.minLedger shouldBe 0
      ledgerBounds.maxLedger shouldBe 0
      LedgerBounds.fromXdr(ledgerBounds.toXdr()) shouldBe ledgerBounds
    }

    test("should handle uint32 max values") {
      val maxUint32 = 4294967295L
      val ledgerBounds = LedgerBounds(maxUint32, maxUint32)
      ledgerBounds.minLedger shouldBe maxUint32
      ledgerBounds.maxLedger shouldBe maxUint32
      LedgerBounds.fromXdr(ledgerBounds.toXdr()) shouldBe ledgerBounds
    }

    test("should handle min equal to max") {
      val ledgerBounds = LedgerBounds(100, 100)
      ledgerBounds.minLedger shouldBe 100
      ledgerBounds.maxLedger shouldBe 100
      LedgerBounds.fromXdr(ledgerBounds.toXdr()) shouldBe ledgerBounds
    }
  })
