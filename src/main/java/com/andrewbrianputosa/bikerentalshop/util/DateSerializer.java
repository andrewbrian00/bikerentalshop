package com.andrewbrianputosa.bikerentalshop.util;

import java.lang.reflect.Type;
import java.util.Date;
import com.google.gson.*;

public class DateSerializer implements JsonSerializer<Date> {

  @Override
  public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src.getTime());
  }

}
