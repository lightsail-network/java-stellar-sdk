package org.stellar.sdk;

import java.util.Iterator;
import java.util.ServiceLoader;
import lombok.Getter;
import org.stellar.sdk.spi.SdkProvider;

/**
 * Factory class used to create {@link Base32} instances.
 *
 * <p>The class will try to load an implementation of {@link Base32} using the {@link
 * ServiceLoader}, if no implementation is found it will use the default implementation {@link
 * ApacheBase32}.
 */
class Base32Factory {
  @Getter private static Base32 instance = new ApacheBase32();

  static {
    ServiceLoader<SdkProvider> load = ServiceLoader.load(SdkProvider.class);
    Iterator<SdkProvider> iterator = load.iterator();
    if (iterator.hasNext()) {
      SdkProvider sdkProvider = iterator.next();
      Base32 newInstance = sdkProvider.createBase32();
      if (newInstance != null) {
        instance = newInstance;
      }
    }
  }
}
