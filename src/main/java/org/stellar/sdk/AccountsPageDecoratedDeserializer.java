package org.stellar.sdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

class AccountsPageDecoratedDeserializer implements JsonDeserializer<AccountsPageDecorated> {
  @Override
  public AccountsPageDecorated deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    // Flatten the object so it has two fields `records` and `links`
    JsonObject newJson = new JsonObject();
    newJson.add("records", json.getAsJsonObject().get("_embedded").getAsJsonObject().get("records"));
    newJson.add("links", json.getAsJsonObject().get("_links"));

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Account.class, new AccountDeserializer())
            .create();

    AccountsPage page = gson.fromJson(newJson, AccountsPage.class);
    return new AccountsPageDecorated(page);
  }
}
