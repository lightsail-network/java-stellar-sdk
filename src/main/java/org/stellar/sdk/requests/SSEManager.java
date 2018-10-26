package org.stellar.sdk.requests;

import okhttp3.OkHttpClient;
import okhttp3.sse.EventSource;

import java.io.Closeable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

interface CloseListener {
  public void closed(EventSource source);
}

public class SSEManager<T extends org.stellar.sdk.responses.Response> implements Closeable {
  private final OkHttpClient okHttpClient;
  private final RequestBuilder requestBuilder;
  private final Class<T> clazz;
  private final EventListener<T> listener;

  private final AtomicBoolean isStopped = new AtomicBoolean(false);
  private final AtomicBoolean serverSideClosed = new AtomicBoolean(true); // make sure we start correctly

  private ExecutorService executorService;

  SSEManager(final OkHttpClient okHttpClient, final RequestBuilder requestBuilder, final Class<T> clazz, final EventListener<T> listener) {
    this.okHttpClient = okHttpClient;
    this.requestBuilder = requestBuilder;
    this.clazz = clazz;
    this.listener = listener;

    executorService = Executors.newSingleThreadExecutor();
    requestBuilder.buildUri(); // call this once to add the segments
  }

  public void start() {
    if(isStopped.get()) {
      throw new IllegalStateException("Already stopped");
    }
    executorService.submit(new Runnable() {
      @Override
      public void run() {
        while(!isStopped.get()) {
          try {
            Thread.sleep(200);
            if(serverSideClosed.get() && !isStopped.get()) {
              serverSideClosed.set(false); // don't restart until true again
              restart();
            }
          } catch (InterruptedException e) {
            throw new IllegalStateException("interrupted",e);
          }
        }
      }
    });

  }

  private void restart() {
    SSEUtils.streamInternal(okHttpClient, requestBuilder, clazz, listener, requestBuilder.uriBuilder.build().toString(), new CloseListener() {
      @Override
      public void closed(EventSource source) {
        serverSideClosed.set(true
        );
      }
    });
  }

  public void stop() {
    isStopped.set(true);
  }

  public void close()  {
    stop();
    executorService.shutdownNow();
  }
}
