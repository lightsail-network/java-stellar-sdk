package org.stellar.sdk.http.sse;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SseContext {
  private final AtomicBoolean isClosed;
  private final AtomicBoolean isStopped;
  private final AtomicLong currentListenerId;
  private final AtomicLong lastEventTime;
  private final AtomicReference<String> lastEventId;
}
