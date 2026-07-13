package net.geoprism.registry.view;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runwaysdk.dataaccess.ProgrammingErrorException;

public class ObjectOverTimeDTO
{
  private TypeInfo                     type;

  private String                       code;

  private String                       label;

  /**
   * For attributes that do change over time, they will be stored here.
   */
  private Map<String, AttributeDTO<?>> properties = new HashMap<>();

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

  public Map<String, AttributeDTO<?>> getProperties()
  {
    return properties;
  }

  public void setProperties(Map<String, AttributeDTO<?>> properties)
  {
    this.properties = properties;
  }

  @JsonIgnore
  public void put(String attributeName, AttributeDTO<?> attribute)
  {
    this.properties.put(attributeName, attribute);
  }

  @JsonIgnore
  public void setValue(String attributeName, Object value)
  {
    if (!this.properties.containsKey(attributeName))
    {
      if (value instanceof LocalizedValue)
      {
        this.properties.put(attributeName, new AttributeLocalDTO());
      }
      else if (value instanceof String)
      {
        this.properties.put(attributeName, new AttributeCharacterDTO());
      }
    }

    this.properties.get(attributeName).setObjectValue(value);
  }

  @SuppressWarnings("unchecked")
  @JsonIgnore
  public <T> AttributeDTO<T> get(String attributeName)
  {
    return (AttributeDTO<T>) this.properties.get(attributeName);
  }

  @SuppressWarnings("unchecked")
  @JsonIgnore
  public <T> T getValue(String attributeName)
  {
    AttributeDTO<T> attribute = (AttributeDTO<T>) this.properties.get(attributeName);

    return (T) attribute.getValue();
  }

  @SuppressWarnings("unchecked")
  @JsonIgnore
  public <T> List<ValueOverTimeEntryDTO<T>> getValuesOverTime(String attributeName)
  {
    AttributeDTO<T> attribute = (AttributeDTO<T>) this.properties.get(attributeName);

    return attribute.getValues();
  }

  @SuppressWarnings("unchecked")
  @JsonIgnore
  public <T> Optional<T> getValue(String attributeName, Date date)
  {
    AttributeDTO<T> attribute = (AttributeDTO<T>) this.properties.get(attributeName);

    return (Optional<T>) attribute.getValue(date);
  }

  @JsonIgnore
  public boolean has(String attributeName)
  {
    return this.properties.containsKey(attributeName);
  }

  public ObjectAtTimeDTO toDate(Date date)
  {
    ObjectAtTimeDTO dto = new ObjectAtTimeDTO();
    dto.setCode(this.getCode());
    dto.setLabel(this.getLabel());
    dto.setType(this.getType());

    this.getProperties().entrySet().stream().forEach(entry -> {
      AttributeDTO<?> attribute = entry.getValue();

      if (attribute.getValue() != null)
      {
        dto.setValue(entry.getKey(), attribute.getValue());
      }
      else
      {
        attribute.getValue(date).ifPresent(v -> {
          dto.setValue(entry.getKey(), v);
        });
      }
    });

    return dto;
  }

  public static String toJson(ObjectOverTimeDTO dto)
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

  public static String toJson(List<ObjectOverTimeDTO> dtos)
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

  public static ObjectOverTimeDTO parseJson(String json)
  {
    try
    {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(json, ObjectOverTimeDTO.class);
    }
    catch (JsonProcessingException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public static List<ObjectOverTimeDTO> parseList(String json)
  {
    try
    {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readerForListOf(ObjectOverTimeDTO.class).readValue(json);
    }
    catch (JsonProcessingException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

}
