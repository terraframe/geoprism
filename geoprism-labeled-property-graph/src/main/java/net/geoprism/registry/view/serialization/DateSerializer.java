package net.geoprism.registry.view.serialization;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateSerializer extends JsonSerializer<Date>
{
  @Override
  public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException
  {
    if (value != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

      String rawValue = sdf.format(value);

      gen.writeString(rawValue);
    }
  }

}