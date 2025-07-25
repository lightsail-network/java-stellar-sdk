package org.stellar.sdk

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class TOIDTest :
  FunSpec({
    val ledgerFirst = 1L shl 32
    val txFirst = 1L shl 12
    val opFirst = 1L

    context("test toInt64 and fromInt64") {
      withData(
        nameFn = { (toid, id) ->
          "TOID(${toid.ledgerSequence}, ${toid.transactionOrder}, ${toid.operationIndex}) should be $id"
        },
        TOID(0, 0, 4095) to 4095L,
        TOID(0, 1048575, 0) to 4294963200L,
        TOID(2147483647, 0, 0) to 9223372032559808512L,
        TOID(1, 1, 1) to (ledgerFirst + txFirst + opFirst),
        TOID(1, 1, 0) to (ledgerFirst + txFirst),
        TOID(1, 0, 1) to (ledgerFirst + opFirst),
        TOID(1, 0, 0) to ledgerFirst,
        TOID(0, 1, 0) to txFirst,
        TOID(0, 0, 1) to opFirst,
        TOID(0, 0, 0) to 0L,
        TOID(2147483647, 1048575, 4095) to 9223372036854775807L,
      ) { (toid, id) ->
        toid.toInt64() shouldBe id
        TOID.fromInt64(id) shouldBe toid
      }
    }

    context("test ledgerRangeInclusive") {
      test("ledger 1 to 1") {
        TOID.ledgerRangeInclusive(1, 1) shouldBe
          TOID.TOIDRange(TOID(0, 0, 0).toInt64(), TOID(2, 0, 0).toInt64())
      }
      test("ledger 1 to 2") {
        TOID.ledgerRangeInclusive(1, 2) shouldBe
          TOID.TOIDRange(TOID(0, 0, 0).toInt64(), TOID(3, 0, 0).toInt64())
      }
      test("ledger 2 to 2") {
        TOID.ledgerRangeInclusive(2, 2) shouldBe
          TOID.TOIDRange(TOID(2, 0, 0).toInt64(), TOID(3, 0, 0).toInt64())
      }
      test("ledger 2 to 3") {
        TOID.ledgerRangeInclusive(2, 3) shouldBe
          TOID.TOIDRange(TOID(2, 0, 0).toInt64(), TOID(4, 0, 0).toInt64())
      }
    }

    context("test incrementOperationIndex") {
      test("increment from 0") {
        val toid = TOID(0, 0, 0)
        toid.incrementOperationIndex()
        toid shouldBe TOID(0, 0, 1)
        toid.incrementOperationIndex()
        toid shouldBe TOID(0, 0, 2)
        toid.incrementOperationIndex()
        toid shouldBe TOID(0, 0, 3)
      }

      test("increment from max operation index") {
        val toid = TOID(0, 0, 0xFFF)
        toid.incrementOperationIndex()
        toid shouldBe TOID(1, 0, 0)
        toid.incrementOperationIndex()
        toid shouldBe TOID(1, 0, 1)
      }
    }

    context("test afterLedger") {
      test("after ledger 0") { TOID.afterLedger(0).toInt64() shouldBe 4294967295L }
      test("after ledger 1") { TOID.afterLedger(1).toInt64() shouldBe 8589934591L }
      test("after ledger 100") { TOID.afterLedger(100).toInt64() shouldBe 433791696895L }
    }

    context("test invalid parameters") {
      test("constructor with invalid params throws") {
        shouldThrow<IllegalArgumentException> { TOID(-1, 0, 0) }
        shouldThrow<IllegalArgumentException> { TOID(0, -1, 0) }
        shouldThrow<IllegalArgumentException> { TOID(0, 0, -1) }
        shouldThrow<IllegalArgumentException> { TOID(0, 0xFFFFF + 1, 0) }
        shouldThrow<IllegalArgumentException> { TOID(0, 0, 0xFFF + 1) }
      }

      test("fromInt64 with invalid params throws") {
        shouldThrow<IllegalArgumentException> { TOID.fromInt64(-1) }
      }

      test("ledgerRangeInclusive with invalid params throws") {
        shouldThrow<IllegalArgumentException> { TOID.ledgerRangeInclusive(2, 1) }
        shouldThrow<IllegalArgumentException> { TOID.ledgerRangeInclusive(0, 1) }
        shouldThrow<IllegalArgumentException> { TOID.ledgerRangeInclusive(-1, 100) }
      }
    }
  })
