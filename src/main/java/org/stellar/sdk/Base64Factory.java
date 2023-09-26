package org.stellar.sdk;

import java.util.Iterator;
import java.util.ServiceLoader;
import lombok.Getter;
import org.stellar.sdk.spi.SdkProvider;

/**
 * Factory class used to create {@link Base64} instances.
 *
 * <p>The class will try to load an implementation of {@link Base64} using the {@link
 * ServiceLoader}, if no implementation is found it will use the default implementation {@link
 * JDKBase64}.
 *
 * <p>Note: The class should be limited to internal use only, and users should not use it directly.
 */
public class Base64Factory {
  @Getter private static Base64 instance = new JDKBase64();

  static {
    ServiceLoader<SdkProvider> load = ServiceLoader.load(SdkProvider.class);
    Iterator<SdkProvider> iterator = load.iterator();
    if (iterator.hasNext()) {
      SdkProvider sdkProvider = iterator.next();
      Base64 newInstance = sdkProvider.createBase64();
      if (newInstance != null) {
        instance = newInstance;
      }
    }
  }
}
