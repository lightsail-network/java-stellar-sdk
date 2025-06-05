package org.stellar.sdk.http.sse;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import jakarta.ws.rs.sse.InboundSseEvent;
import java.net.SocketException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.stellar.sdk.requests.EventListener;
import org.stellar.sdk.requests.RequestBuilder;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.gson.GsonSingleton;

@RunWith(MockitoJUnitRunner.class)
public class CxfSseEventStreamTest {

  @Mock private EventListener<AccountResponse> eventListener;

  @Mock private RequestBuilder requestBuilder;

  @Captor private ArgumentCaptor<AccountResponse> eventListenerCaptor;

  @Captor private ArgumentCaptor<Optional<Throwable>> eventListenerThrowableCaptor;

  @Captor private ArgumentCaptor<Optional<Integer>> eventListenerErrorCodeCaptor;

  @Before
  public void setup() {
    doNothing().when(eventListener).onEvent(eventListenerCaptor.capture());
    doNothing()
        .when(eventListener)
        .onFailure(eventListenerThrowableCaptor.capture(), eventListenerErrorCodeCaptor.capture());
  }

  @Test
  public void testEventStoppedContextDoesNothing() {
    final var context = mockContext();
    final var listenerId = 34567L;
    try (final var mockEventSource = new MockCxfEventSource()) {
      final var stream =
          new CxfSseEventStream<>(
              context,
              mockEventSource,
              AccountResponse.class,
              listenerId,
              eventListener,
              requestBuilder);

      context.getIsStopped().set(true);

      final var event = mock(InboundSseEvent.class);
      mockEventSource.handleEvent(event);

      verifyNoInteractions(event);
      verifyNoInteractions(eventListener);
    }
  }

  @Test
  public void testEventChangedListenerIdDoesNothing() {
    final var context = mockContext();
    final var listenerId = 34567L;
    try (final var mockEventSource = new MockCxfEventSource()) {
      final var stream =
          new CxfSseEventStream<>(
              context,
              mockEventSource,
              AccountResponse.class,
              listenerId,
              eventListener,
              requestBuilder);

      context.getIsStopped().set(false);
      context.getCurrentListenerId().set(45678L);

      final var event = mock(InboundSseEvent.class);
      mockEventSource.handleEvent(event);

      verifyNoInteractions(event);
      verifyNoInteractions(eventListener);
    }
  }

  @Test
  public void testEventDataHelloDoesNothing() {
    final var context = mockContext();
    final var listenerId = 34567L;
    try (final var mockEventSource = new MockCxfEventSource()) {
      final var stream =
          new CxfSseEventStream<>(
              context,
              mockEventSource,
              AccountResponse.class,
              listenerId,
              eventListener,
              requestBuilder);

      context.getIsStopped().set(false);
      context.getCurrentListenerId().set(listenerId);

      final var event = mock(InboundSseEvent.class);
      when(event.readData()).thenReturn("\"hello\"");
      mockEventSource.handleEvent(event);

      verify(event, times(1)).readData();
      verifyNoInteractions(eventListener);
    }
  }

  @Test
  public void testEventDataByeByeDoesNothing() {
    final var context = mockContext();
    final var listenerId = 34567L;
    try (final var mockEventSource = new MockCxfEventSource()) {
      final var stream =
          new CxfSseEventStream<>(
              context,
              mockEventSource,
              AccountResponse.class,
              listenerId,
              eventListener,
              requestBuilder);

      context.getIsStopped().set(false);
      context.getCurrentListenerId().set(listenerId);

      final var event = mock(InboundSseEvent.class);
      when(event.readData()).thenReturn("\"byebye\"");
      mockEventSource.handleEvent(event);

      verify(event, times(1)).readData();
      verifyNoInteractions(eventListener);
    }
  }

  @Test
  public void testEventNormal() {
    final var context = mockContext();
    final var listenerId = 34567L;
    try (final var mockEventSource = new MockCxfEventSource()) {
      final var stream =
          new CxfSseEventStream<>(
              context,
              mockEventSource,
              AccountResponse.class,
              listenerId,
              eventListener,
              requestBuilder);

      context.getIsStopped().set(false);
      context.getCurrentListenerId().set(listenerId);

      final var response = new AccountResponse();

      final var event = mock(InboundSseEvent.class);
      when(event.readData()).thenReturn(toJson(response));
      when(event.getId()).thenReturn("new event ID");
      mockEventSource.handleEvent(event);

      verify(event, times(1)).readData();

      assertNotNull(eventListenerCaptor.getValue());
      assertEquals(response, eventListenerCaptor.getValue());
    }
  }

  @Test
  public void testErrorStoppedContextDoesNothing() {
    final var context = mockContext();
    final var listenerId = 34567L;
    try (final var mockEventSource = new MockCxfEventSource()) {
      final var stream =
          new CxfSseEventStream<>(
              context,
              mockEventSource,
              AccountResponse.class,
              listenerId,
              eventListener,
              requestBuilder);

      context.getIsStopped().set(true);
      mockEventSource.handleError(new Exception("error"));
      verifyNoInteractions(eventListener);
    }
  }

  @Test
  public void testErrorChangedListenerIdDoesNothing() {
    final var context = mockContext();
    final var listenerId = 34567L;
    try (final var mockEventSource = new MockCxfEventSource()) {
      final var stream =
          new CxfSseEventStream<>(
              context,
              mockEventSource,
              AccountResponse.class,
              listenerId,
              eventListener,
              requestBuilder);

      context.getIsStopped().set(false);
      context.getCurrentListenerId().set(45678L);
      mockEventSource.handleError(new Exception("error"));
      verifyNoInteractions(eventListener);
    }
  }

  @Test
  public void testErrorSocketExceptionClosesContext() {
    final var context = mockContext();
    final var listenerId = 34567L;
    try (final var mockEventSource = new MockCxfEventSource()) {
      final var stream =
          new CxfSseEventStream<>(
              context,
              mockEventSource,
              AccountResponse.class,
              listenerId,
              eventListener,
              requestBuilder);

      context.getIsStopped().set(false);
      context.getIsClosed().set(false);
      context.getCurrentListenerId().set(listenerId);
      mockEventSource.handleError(new SocketException());

      assertTrue(context.getIsClosed().get());
      verifyNoInteractions(eventListener);
    }
  }

  @Test
  public void testErrorHandlesThrowable() {
    final var context = mockContext();
    final var listenerId = 34567L;
    try (final var mockEventSource = new MockCxfEventSource()) {
      final var stream =
          new CxfSseEventStream<>(
              context,
              mockEventSource,
              AccountResponse.class,
              listenerId,
              eventListener,
              requestBuilder);

      context.getIsStopped().set(false);
      context.getIsClosed().set(false);
      context.getCurrentListenerId().set(listenerId);
      final var error = new Exception();
      mockEventSource.handleError(error);

      assertFalse(context.getIsClosed().get());
      assertNotNull(eventListenerThrowableCaptor.getValue());
      assertTrue(eventListenerThrowableCaptor.getValue().isPresent());
      assertEquals(error, eventListenerThrowableCaptor.getValue().get());
    }
  }

  @Test
  public void testErrorHandlesNullThrowable() {
    final var context = mockContext();
    final var listenerId = 34567L;
    try (final var mockEventSource = new MockCxfEventSource()) {
      final var stream =
          new CxfSseEventStream<>(
              context,
              mockEventSource,
              AccountResponse.class,
              listenerId,
              eventListener,
              requestBuilder);

      context.getIsStopped().set(false);
      context.getIsClosed().set(false);
      context.getCurrentListenerId().set(listenerId);
      mockEventSource.handleError(null);

      assertFalse(context.getIsClosed().get());
      assertNotNull(eventListenerThrowableCaptor.getValue());
      assertTrue(eventListenerThrowableCaptor.getValue().isEmpty());
    }
  }

  @Test
  public void testCompleteStoppedContextDoesNothing() {
    final var context = mockContext();
    final var listenerId = 34567L;
    try (final var mockEventSource = new MockCxfEventSource()) {
      final var stream =
          new CxfSseEventStream<>(
              context,
              mockEventSource,
              AccountResponse.class,
              listenerId,
              eventListener,
              requestBuilder);

      context.getIsStopped().set(true);
      context.getIsClosed().set(false);
      mockEventSource.complete();
      verifyNoInteractions(eventListener);
      assertFalse(context.getIsClosed().get());
    }
  }

  @Test
  public void testCompleteChangedListenerIdDoesNothing() {
    final var context = mockContext();
    final var listenerId = 34567L;
    try (final var mockEventSource = new MockCxfEventSource()) {
      final var stream =
          new CxfSseEventStream<>(
              context,
              mockEventSource,
              AccountResponse.class,
              listenerId,
              eventListener,
              requestBuilder);

      context.getIsStopped().set(false);
      context.getCurrentListenerId().set(45678L);
      mockEventSource.complete();
      verifyNoInteractions(eventListener);
      assertFalse(context.getIsClosed().get());
    }
  }

  @Test
  public void testCompleteClosesContext() {
    final var context = mockContext();
    final var listenerId = 34567L;
    try (final var mockEventSource = new MockCxfEventSource()) {
      final var stream =
          new CxfSseEventStream<>(
              context,
              mockEventSource,
              AccountResponse.class,
              listenerId,
              eventListener,
              requestBuilder);

      context.getIsStopped().set(false);
      context.getIsClosed().set(false);
      context.getCurrentListenerId().set(listenerId);
      mockEventSource.complete();
      verifyNoInteractions(eventListener);
      assertTrue(context.getIsClosed().get());
    }
  }

  private static SseContext mockContext() {
    return new SseContext(
        new AtomicBoolean(),
        new AtomicBoolean(),
        new AtomicLong(),
        new AtomicLong(),
        new AtomicReference<String>());
  }

  private static String toJson(Object object) {
    return GsonSingleton.getInstance().toJson(object);
  }
}
