package net.geoprism.registry.view;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.runwaysdk.dataaccess.ProgrammingErrorException;

import net.geoprism.registry.view.serialization.DateDeserializer;
import net.geoprism.registry.view.serialization.DateSerializer;

public class ObjectAtTimeDTO
{
  private TypeInfo            type;

  private String              code;

  private String              label;

  @JsonSerialize(using = DateSerializer.class)
  @JsonDeserialize(using = DateDeserializer.class)
  private Date                date;

  /**
   * For attributes that do change over time, they will be stored here.
   */
  private Map<String, Object> data = new HashMap<>();

  public TypeInfo getType()
  {
    return type;
  }

  public void setType(TypeInfo type)
  {
    this.type = type;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
    this.code = code;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public Date getDate()
  {
    return date;
  }

  public void setDate(Date date)
  {
    this.date = date;
  }

  public Map<String, Object> getData()
  {
    return data;
  }

  public void setData(Map<String, Object> data)
  {
    this.data = data;
  }

  @JsonIgnore
  public void setValue(String attributeName, Object value)
  {
    this.data.put(attributeName, value);
  }

  @SuppressWarnings("unchecked")
  @JsonIgnore
  public <T> T getValue(String attributeName)
  {
    return (T) this.data.get(attributeName);
  }

  @JsonIgnore
  public boolean has(String attributeName)
  {
    return this.data.containsKey(attributeName);
  }

  public static String toJson(ObjectAtTimeDTO dto)
  {
    try
    {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(dto);
    }
    catch (JsonProcessingException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public static String toJson(List<ObjectAtTimeDTO> dtos)
  {
    try
    {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(dtos);
    }
    catch (JsonProcessingException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public static ObjectAtTimeDTO parseJson(String json)
  {
    try
    {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(json, ObjectAtTimeDTO.class);
    }
    catch (JsonProcessingException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public static List<ObjectAtTimeDTO> parseList(String json)
  {
    try
    {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readerForListOf(ObjectAtTimeDTO.class).readValue(json);
    }
    catch (JsonProcessingException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

}
