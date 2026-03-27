package org.commongeoregistry.adapter.serialization;

import java.io.IOException;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class LocalizedValueDeserializer extends JsonDeserializer<LocalizedValue>
{
  @Override
  public LocalizedValue deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException
  {
    JsonNode node = jsonParser.readValueAsTree();
    String text = node.toPrettyString();
    
    if (text != null && !text.isEmpty())
    {
      return LocalizedValue.fromJSON(com.google.gson.JsonParser.parseString(text).getAsJsonObject());
    }

    return null;
  }
}