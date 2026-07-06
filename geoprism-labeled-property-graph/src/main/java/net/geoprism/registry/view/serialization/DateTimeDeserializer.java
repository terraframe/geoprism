package net.geoprism.registry.view.serialization;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DateTimeDeserializer extends JsonDeserializer<Date>
{
  @Override
  public Date deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException
  {
    String date = jsonParser.getText();
    if (!StringUtils.isBlank(date))
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mmZ");
      sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

      try
      {
        return sdf.parse(date);
      }
      catch (ParseException e)
      {
        throw new JsonParseException(jsonParser, "Failed to parse date value [" + date + "]", e);
      }

    }
    return null;
  }
}