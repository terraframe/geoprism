package net.geoprism.registry;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collector;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class JsonCollectors
{
  public static Collector<JsonElement, ?, JsonArray> toJsonArray()
  {
    return Collector.of(new Supplier<JsonArray>()
    {

      @Override
      public JsonArray get()
      {
        return new JsonArray();
      }

    }, new BiConsumer<JsonArray, JsonElement>()
    {

      @Override
      public void accept(JsonArray array, JsonElement element)
      {
        array.add(element);
      }
    }, new BinaryOperator<JsonArray>()
    {
      @Override
      public JsonArray apply(JsonArray arg0, JsonArray arg1)
      {
        arg0.addAll(arg1);

        return arg0;
      }
    });

  }
}
