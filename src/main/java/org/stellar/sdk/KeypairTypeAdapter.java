package org.stellar.sdk;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.stellar.base.Keypair;

import java.io.IOException;

class KeypairTypeAdapter extends TypeAdapter<Keypair> {
  @Override
  public void write(JsonWriter out, Keypair value) throws IOException {
    // Don't need this.
  }

  @Override
  public Keypair read(JsonReader in) throws IOException {
    return Keypair.fromAddress(in.nextString());
  }
}
