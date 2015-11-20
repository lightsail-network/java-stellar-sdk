package org.stellar.sdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.stellar.base.Keypair;

class GsonSingleton {
  private static Gson instance = null;

  protected GsonSingleton() {}

  public static Gson getInstance() {
    if (instance == null) {
      TypeToken accountPageType = new TypeToken<Page<Account>>() {};
      TypeToken ledgerPageType = new TypeToken<Page<Ledger>>() {};
      TypeToken trasactionPageType = new TypeToken<Page<Transaction>>() {};

      instance = new GsonBuilder()
                      .registerTypeAdapter(Keypair.class, new KeypairTypeAdapter().nullSafe())
                      .registerTypeAdapter(accountPageType.getType(), new PageDeserializer<Account>(accountPageType))
                      .registerTypeAdapter(ledgerPageType.getType(), new PageDeserializer<Ledger>(ledgerPageType))
                      .registerTypeAdapter(trasactionPageType.getType(), new PageDeserializer<Transaction>(trasactionPageType))
                      .create();
    }
    return instance;
  }

}
