package org.stellar.sdk.http.sse;

import jakarta.ws.rs.sse.InboundSseEvent;
import jakarta.ws.rs.sse.SseEventSource;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class MockCxfEventSource implements SseEventSource {
  private Consumer<InboundSseEvent> eventConsumer;
  private Consumer<Throwable> errorConsumer;
  private Runnable completionInstruction;
  private boolean open;

  @Override
  public void register(Consumer<InboundSseEvent> consumer) {
    this.eventConsumer = consumer;
  }

  @Override
  public void register(Consumer<InboundSseEvent> consumer, Consumer<Throwable> consumer1) {
    this.eventConsumer = consumer;
    this.errorConsumer = consumer1;
  }

  @Override
  public void register(
      Consumer<InboundSseEvent> consumer, Consumer<Throwable> consumer1, Runnable runnable) {
    this.eventConsumer = consumer;
    this.errorConsumer = consumer1;
    this.completionInstruction = runnable;
  }

  @Override
  public void open() {
    this.open = true;
  }

  @Override
  public boolean isOpen() {
    return open;
  }

  @Override
  public boolean close(long l, TimeUnit timeUnit) {
    this.open = false;
    return false;
  }

  public void handleEvent(InboundSseEvent event) {
    eventConsumer.accept(event);
  }

  public void handleError(Throwable throwable) {
    errorConsumer.accept(throwable);
  }

  public void complete() {
    completionInstruction.run();
  }
}
