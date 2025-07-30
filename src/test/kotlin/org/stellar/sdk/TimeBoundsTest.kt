package org.stellar.sdk

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.longs.shouldBeInRange
import io.kotest.matchers.shouldBe

class TimeBoundsTest :
  FunSpec({
    test("should throw IllegalArgumentException for negative minTime") {
      val exception = shouldThrow<IllegalArgumentException> { TimeBounds(-1, 300) }
      exception.message shouldBe "minTime must be between 0 and 2^64-1"
    }

    test("should throw IllegalArgumentException for negative maxTime") {
      val exception = shouldThrow<IllegalArgumentException> { TimeBounds(1, -300) }
      exception.message shouldBe "maxTime must be between 0 and 2^64-1"
    }

    test("should throw IllegalArgumentException when minTime greater than maxTime") {
      val exception = shouldThrow<IllegalArgumentException> { TimeBounds(300, 1) }
      exception.message shouldBe "minTime must be <= maxTime"
    }

    test("should create TimeBounds with infinite timeout (maxTime = 0)") {
      val timeBounds = TimeBounds(300, 0)
      timeBounds.minTime.toLong() shouldBe 300
      timeBounds.maxTime.toLong() shouldBe 0
      TimeBounds.fromXdr(timeBounds.toXdr()) shouldBe timeBounds
    }

    test("should create TimeBounds with both times zero") {
      val timeBounds = TimeBounds(0, 0)
      timeBounds.minTime.toLong() shouldBe 0
      timeBounds.maxTime.toLong() shouldBe 0
      TimeBounds.fromXdr(timeBounds.toXdr()) shouldBe timeBounds
    }

    test("should create TimeBounds with valid range") {
      val timeBounds = TimeBounds(1, 300)
      timeBounds.minTime.toLong() shouldBe 1
      timeBounds.maxTime.toLong() shouldBe 300
      TimeBounds.fromXdr(timeBounds.toXdr()) shouldBe timeBounds
    }

    test("should create TimeBounds with equal min and max time") {
      val timeBounds = TimeBounds(300, 300)
      timeBounds.minTime.toLong() shouldBe 300
      timeBounds.maxTime.toLong() shouldBe 300
      TimeBounds.fromXdr(timeBounds.toXdr()) shouldBe timeBounds
    }

    test("should create TimeBounds with timeout using expiresAfter") {
      val timeout = 300L
      val timeBounds = TimeBounds.expiresAfter(timeout)
      val now = System.currentTimeMillis() / 1000L

      timeBounds.minTime.toLong() shouldBe 0
      val actualMaxTime = timeBounds.maxTime.toLong()
      actualMaxTime shouldBeInRange (now + timeout - 1)..(now + timeout + 1)
    }

    test("should handle large time values") {
      val largeTime = Long.MAX_VALUE - 1
      val timeBounds = TimeBounds(0, largeTime)
      timeBounds.minTime.toLong() shouldBe 0
      timeBounds.maxTime.toLong() shouldBe largeTime
      TimeBounds.fromXdr(timeBounds.toXdr()) shouldBe timeBounds
    }

    test("should handle maximum valid long values") {
      val maxTime = Long.MAX_VALUE
      val timeBounds = TimeBounds(maxTime, maxTime)
      timeBounds.minTime.toLong() shouldBe maxTime
      timeBounds.maxTime.toLong() shouldBe maxTime
      TimeBounds.fromXdr(timeBounds.toXdr()) shouldBe timeBounds
    }
  })
