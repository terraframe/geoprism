package net.geoprism.registry.view;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runwaysdk.dataaccess.ProgrammingErrorException;

@JsonTypeName(BusinessTypeDTO.TYPE)
public class BusinessTypeDTO extends ObjectClassDTO
{
  public static final String TYPE = "business-type";

  @JsonProperty("type")
  private final String       type = TYPE;

  private String             labelAttribute;

  public String getLabelAttribute()
  {
    return labelAttribute;
  }

  public void setLabelAttribute(String labelAttribute)
  {
    this.labelAttribute = labelAttribute;
  }

  public boolean hasLabelAttribute()
  {
    return !StringUtils.isBlank(this.getLabelAttribute());
  }

  public static String toJson(BusinessTypeDTO dto)
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

  public static String toJson(List<BusinessTypeDTO> dtos)
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

  public static BusinessTypeDTO parseJson(String json)
  {
    try
    {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(json, BusinessTypeDTO.class);
    }
    catch (JsonProcessingException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public static List<BusinessTypeDTO> parseList(String json)
  {
    try
    {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readerForListOf(BusinessTypeDTO.class).readValue(json);
    }
    catch (JsonProcessingException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }
}
