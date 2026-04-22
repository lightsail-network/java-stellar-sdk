package org.stellar.sdk.requests

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf
import java.io.Closeable
import java.util.Optional
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okhttp3.mockwebserver.SocketPolicy
import org.stellar.sdk.Server
import org.stellar.sdk.responses.LedgerResponse

class SSEStreamTest :
  FunSpec({
    context("successful streaming") {
      test("parses a ledger event and ignores hello / byebye data frames") {
        withLedgerStream(
          sseResponse(
            controlMessage("hello"),
            ledgerEvent(id = "event-1", cursor = "cursor-1", sequence = 1L),
            controlMessage("byebye"),
          ),
          noResponse(),
        ) {
          takeRequest().shouldBeSseRequest()

          val event = probe.awaitEvent()
          event.pagingToken shouldBe "cursor-1"
          event.sequence shouldBe 1L
          stream.lastPagingToken() shouldBe "event-1"
        }
      }

      test("ignores SSE comment frames") {
        withLedgerStream(
          sseResponse(
            comment("keepalive"),
            comment("another comment line"),
            ledgerEvent(id = "event-2", cursor = "cursor-2", sequence = 2L),
          ),
          noResponse(),
        ) {
          takeRequest().shouldBeSseRequest()

          val event = probe.awaitEvent()
          event.pagingToken shouldBe "cursor-2"
          event.sequence shouldBe 2L
          stream.lastPagingToken() shouldBe "event-2"
        }
      }
    }

    context("resume behavior") {
      test("reconnects with Last-Event-ID and cursor, stops issuing requests after close") {
        withLedgerStream(
          sseResponse(ledgerEvent(id = "event-1", cursor = "cursor-1", sequence = 1L)),
          noResponse(),
          reconnectTimeout = SHORT_RECONNECT_MS,
        ) {
          takeRequest().shouldBeSseRequest()
          probe.awaitEvent()

          takeRequest().shouldBeSseRequest(lastEventId = "event-1", cursor = "cursor-1")

          stream.close()
          expectNoAdditionalRequests(NO_REQUEST_WINDOW_MS)
        }
      }

      test("advances cursor to the latest event when one connection delivers multiple events") {
        withLedgerStream(
          sseResponse(
            ledgerEvent(id = "event-1", cursor = "cursor-1", sequence = 1L),
            ledgerEvent(id = "event-2", cursor = "cursor-2", sequence = 2L),
          ),
          noResponse(),
          reconnectTimeout = SHORT_RECONNECT_MS,
        ) {
          takeRequest().shouldBeSseRequest()

          val events = probe.awaitEvents(count = 2)
          events.last().sequence shouldBe 2L
          stream.lastPagingToken() shouldBe "event-2"

          takeRequest().shouldBeSseRequest(lastEventId = "event-2", cursor = "cursor-2")
        }
      }
    }

    context("failure propagation") {
      test("fails when the response content type is not SSE") {
        withLedgerStream(
          MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/json")
            .setBody("{}")
        ) {
          takeRequest().shouldBeSseRequest()

          val failure = probe.awaitFailure()
          probe.eventsSnapshot().shouldBeEmpty()

          failure.responseCode shouldBe 200
          val error = failure.error.shouldNotBeNull()
          error.shouldBeInstanceOf<IllegalStateException>()
          error.message.shouldNotBeNull() shouldContain "Invalid content-type"
        }
      }

      test("fails on malformed JSON and does not advance resume state") {
        withLedgerStream(
          sseResponse("id: bad-event\ndata: {\"paging_token\":\n\n"),
          noResponse(),
          reconnectTimeout = SHORT_RECONNECT_MS,
        ) {
          takeRequest().shouldBeSseRequest()

          val failure = probe.awaitFailure()
          probe.eventsSnapshot().shouldBeEmpty()

          failure.error.shouldNotBeNull().shouldBeInstanceOf<RuntimeException>()
          failure.responseCode shouldBe 200
          stream.lastPagingToken().shouldBeNull()

          // The reconnect should happen fresh, with no resume tokens.
          takeRequest().shouldBeSseRequest()
        }
      }

      test("surfaces HTTP error codes as failure signals without a throwable") {
        withLedgerStream(MockResponse().setResponseCode(503).setBody("temporarily unavailable")) {
          takeRequest().shouldBeSseRequest()

          val failure = probe.awaitFailure()
          probe.eventsSnapshot().shouldBeEmpty()

          failure.error.shouldBeNull()
          failure.responseCode shouldBe 503
        }
      }
    }
  })

private data class FailureSignal(val error: Throwable?, val responseCode: Int?)

private class StreamProbe : EventListener<LedgerResponse> {
  private val events = LinkedBlockingQueue<LedgerResponse>()
  private val failures = LinkedBlockingQueue<FailureSignal>()

  override fun onEvent(event: LedgerResponse) {
    events.put(event)
  }

  override fun onFailure(error: Optional<Throwable>, responseCode: Optional<Int>) {
    failures.put(FailureSignal(error.orElse(null), responseCode.orElse(null)))
  }

  fun awaitEvent(): LedgerResponse =
    events.poll(AWAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS).shouldNotBeNull()

  fun awaitEvents(count: Int): List<LedgerResponse> = List(count) { awaitEvent() }

  fun awaitFailure(): FailureSignal =
    failures.poll(AWAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS).shouldNotBeNull()

  /** Drains the non-blocking view of received events, for assertions like "nothing leaked". */
  fun eventsSnapshot(): List<LedgerResponse> = events.toList()
}

private class StreamFixture(
  private val mockWebServer: MockWebServer,
  val probe: StreamProbe,
  val stream: SSEStream<LedgerResponse>,
) : Closeable {
  fun takeRequest(): RecordedRequest =
    mockWebServer.takeRequest(AWAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS).shouldNotBeNull()

  fun expectNoAdditionalRequests(windowMs: Long) {
    mockWebServer.takeRequest(windowMs, TimeUnit.MILLISECONDS).shouldBeNull()
  }

  override fun close() {
    stream.close()
  }
}

private inline fun withLedgerStream(
  vararg responses: MockResponse,
  reconnectTimeout: Long = SSEStream.DEFAULT_RECONNECT_TIMEOUT,
  block: StreamFixture.() -> Unit,
) {
  MockWebServer().use { mockWebServer ->
    responses.forEach(mockWebServer::enqueue)
    mockWebServer.start()

    Server(mockWebServer.url("/").toString()).use { server ->
      val probe = StreamProbe()
      val stream = server.ledgers().stream(probe, reconnectTimeout)
      StreamFixture(mockWebServer, probe, stream).use(block)
    }
  }
}

private fun sseResponse(body: String): MockResponse =
  MockResponse().setResponseCode(200).setHeader("Content-Type", EVENT_STREAM).setBody(body)

private fun sseResponse(vararg frames: String): MockResponse =
  sseResponse(frames.joinToString(separator = ""))

private fun noResponse(): MockResponse = MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE)

private fun ledgerEvent(id: String, cursor: String, sequence: Long): String =
  "id: $id\ndata: {\"paging_token\":\"$cursor\",\"sequence\":$sequence}\n\n"

private fun controlMessage(message: String): String = "data: \"$message\"\n\n"

private fun comment(message: String): String = ": $message\n\n"

private fun RecordedRequest.shouldBeSseRequest(
  lastEventId: String? = null,
  cursor: String? = null,
) {
  getHeader("Accept") shouldBe EVENT_STREAM
  getHeader("Cache-Control") shouldBe "no-cache"
  getHeader("Last-Event-ID") shouldBe lastEventId
  getHeader("X-Client-Name") shouldBe SDK_NAME
  getHeader("X-Client-Version").shouldNotBeNull()

  val url = requestUrl.shouldNotBeNull()
  url.queryParameter("X-Client-Name") shouldBe SDK_NAME
  url.queryParameter("X-Client-Version").shouldNotBeNull()
  url.queryParameter("cursor") shouldBe cursor
}

private const val EVENT_STREAM = "text/event-stream"
private const val SDK_NAME = "java-stellar-sdk"
private const val AWAIT_TIMEOUT_SECONDS = 5L
private const val SHORT_RECONNECT_MS = 50L
private const val NO_REQUEST_WINDOW_MS = 750L
