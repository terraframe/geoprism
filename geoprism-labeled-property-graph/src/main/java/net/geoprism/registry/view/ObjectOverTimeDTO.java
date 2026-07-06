package net.geoprism.registry.view;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runwaysdk.dataaccess.ProgrammingErrorException;

public class ObjectOverTimeDTO
{
  private TypeInfo                       type;

  private String                         code;

  private String                         label;

  /**
   * For attributes that do change over time, they will be stored here.
   */
  private Map<String, AttributeValueDTO> data = new HashMap<>();

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

  public Map<String, AttributeValueDTO> getData()
  {
    return data;
  }

  public void setData(Map<String, AttributeValueDTO> data)
  {
    this.data = data;
  }

  @JsonIgnore
  public void setValue(String attributeName, Object value)
  {
    this.data.put(attributeName, new SingleValueDTO(value));
  }

  @JsonIgnore
  public void put(String attributeName, AttributeValueDTO value)
  {
    this.data.put(attributeName, value);
  }

  @SuppressWarnings("unchecked")
  @JsonIgnore
  public <T> T getValue(String attributeName)
  {
    SingleValueDTO attribute = (SingleValueDTO) this.data.get(attributeName);

    return (T) attribute.getValue();
  }

  @JsonIgnore
  public List<ValueOverTimeEntryDTO> getValuesOverTime(String attributeName)
  {
    MultiValueDTO attribute = (MultiValueDTO) this.data.get(attributeName);

    return attribute.getValues();
  }

  @JsonIgnore
  public boolean has(String attributeName)
  {
    return this.data.containsKey(attributeName);
  }

  public ObjectAtTimeDTO toDate(Date date)
  {
    ObjectAtTimeDTO dto = new ObjectAtTimeDTO();
    dto.setCode(this.getCode());
    dto.setLabel(this.getLabel());
    dto.setType(this.getType());

    this.getData().entrySet().stream().forEach(entry -> {
      Object value = entry.getValue();

      if (value instanceof SingleValueDTO)
      {
        dto.setValue(entry.getKey(), ( (SingleValueDTO) value ).getValue());
      }
      else if (value instanceof MultiValueDTO)
      {
        ( (MultiValueDTO) value ).getValue(date).ifPresent(v -> {
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
