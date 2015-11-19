package org.stellar.sdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class GsonSingleton {
  private static Gson instance = null;

  protected GsonSingleton() {}

  public static Gson getInstance() {
    if (instance == null) {
      instance = new GsonBuilder()
                      .registerTypeAdapter(Account.class, new AccountDeserializer())
                      .registerTypeAdapter(AccountsPageDecorated.class, new AccountsPageDecoratedDeserializer())
                      .create();
    }
    return instance;
  }

}
