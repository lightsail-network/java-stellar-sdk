package org.stellar.sdk.responses;

import com.google.common.collect.Lists;
import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.List;
import org.stellar.sdk.Predicate;
import org.stellar.sdk.xdr.Duration;
import org.stellar.sdk.xdr.TimePoint;
import org.stellar.sdk.xdr.Uint64;
import org.threeten.bp.Instant;

public class PredicateDeserializer implements JsonDeserializer<Predicate> {
  @Override
  public Predicate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject obj = json.getAsJsonObject();
    if (obj.has("unconditional")) {
      return new Predicate.Unconditional();
    }

    if (obj.has("not")) {
      return new Predicate.Not(deserialize(obj.get("not"), typeOfT, context));
    }

    if (obj.has("and")) {
      List<Predicate> inner = Lists.newArrayList();
      for (JsonElement elt : obj.get("and").getAsJsonArray()) {
        inner.add(deserialize(elt, typeOfT, context));
      }
      return new Predicate.And(inner);
    }

    if (obj.has("or")) {
      List<Predicate> inner = Lists.newArrayList();
      for (JsonElement elt : obj.get("or").getAsJsonArray()) {
        inner.add(deserialize(elt, typeOfT, context));
      }
      return new Predicate.Or(inner);
    }

    // backwards compatible for abs before, uses the newer abs_before_epoch if available from server
    // otherwise if abs_before_epoch is absent, it will fall back to original attempt to parse
    // abs_before date string
    if (obj.has("abs_before_epoch")) {
      return new Predicate.AbsBefore(
          new TimePoint(new Uint64(obj.get("abs_before_epoch").getAsLong())));
    } else if (obj.has("abs_before")) {
      String formattedDate = obj.get("abs_before").getAsString();
      return new Predicate.AbsBefore(
          new TimePoint(new Uint64(Instant.parse(formattedDate).getEpochSecond())));
    }

    if (obj.has("rel_before")) {
      return new Predicate.RelBefore(new Duration(new Uint64(obj.get("rel_before").getAsLong())));
    }

    throw new IllegalArgumentException("Unsupported predicate: " + json.toString());
  }
}
