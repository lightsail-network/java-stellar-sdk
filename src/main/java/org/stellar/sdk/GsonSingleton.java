package org.stellar.sdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.stellar.sdk.responses.SubmitTransactionResponse;
import org.stellar.sdk.responses.deserializers.SubmitTransactionResponseExtrasDeserializer;

class GsonSingleton {
  private static Gson instance = null;

  protected GsonSingleton() {}

  public static Gson getInstance() {
    if(instance == null) {
      instance = new GsonBuilder()
                      .registerTypeAdapter(SubmitTransactionResponse.Extras.class, new SubmitTransactionResponseExtrasDeserializer())
                      // add more (de)serializers
                      .create();
    }
    return instance;
  }

}
