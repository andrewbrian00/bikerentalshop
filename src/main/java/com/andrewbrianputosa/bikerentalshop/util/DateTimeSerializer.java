package com.andrewbrianputosa.bikerentalshop.util;

import java.lang.reflect.Type;
import org.joda.time.DateTime;
import com.google.gson.*;

public class DateTimeSerializer implements JsonSerializer<DateTime> {

  @Override
  public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src.getMillis());
  }

}
