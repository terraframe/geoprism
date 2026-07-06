package net.geoprism.registry.view;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runwaysdk.dataaccess.ProgrammingErrorException;

@JsonTypeName(ConceptClassDTO.TYPE)
public class ConceptClassDTO extends ObjectClassDTO
{
  public static final String TYPE = "concept-class";

  @JsonProperty("type")
  private final String       type = TYPE;

  public static String toJson(ConceptClassDTO dto)
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

  public static String toJson(List<ConceptClassDTO> dtos)
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

  public static ConceptClassDTO parseJson(String json)
  {
    try
    {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(json, ConceptClassDTO.class);
    }
    catch (JsonProcessingException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public static List<ConceptClassDTO> parseList(String json)
  {
    try
    {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readerForListOf(ConceptClassDTO.class).readValue(json);
    }
    catch (JsonProcessingException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }
}
