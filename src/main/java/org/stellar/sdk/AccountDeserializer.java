package org.stellar.sdk;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.stellar.base.Keypair;

import java.lang.reflect.Type;

class AccountDeserializer implements JsonDeserializer<Account> {
  @Override
  public Account deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    // Deserialize all primitive types
    Account account = new Gson().fromJson(json, Account.class);

    // Keypair
    JsonObject object = json.getAsJsonObject();
    Keypair keypair = Keypair.fromAddress(object.get("address").getAsString());
    account.setKeypair(keypair);

    return account;
  }
}
