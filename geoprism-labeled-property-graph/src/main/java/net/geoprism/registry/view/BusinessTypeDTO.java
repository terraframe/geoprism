package net.geoprism.registry.view;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.serialization.LocalizedValueDeserializer;
import org.commongeoregistry.adapter.serialization.LocalizedValueSerializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.runwaysdk.dataaccess.ProgrammingErrorException;

public class BusinessTypeDTO
{
  private String              oid;

  private String              code;

  private String              organization;

  private String              organizationLabel;

  private String              labelAttribute;

  private String              origin;

  private Long                sequence;

  @JsonSerialize(using = LocalizedValueSerializer.class)
  @JsonDeserialize(using = LocalizedValueDeserializer.class)
  private LocalizedValue      displayLabel;

  private List<AttributeType> attributes;

  public String getOid()
  {
    return oid;
  }

  public void setOid(String oid)
  {
    this.oid = oid;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
    this.code = code;
  }

  public String getOrganization()
  {
    return organization;
  }

  public void setOrganization(String organization)
  {
    this.organization = organization;
  }

  public String getOrganizationLabel()
  {
    return organizationLabel;
  }

  public void setOrganizationLabel(String organizationLabel)
  {
    this.organizationLabel = organizationLabel;
  }

  public String getLabelAttribute()
  {
    return labelAttribute;
  }

  public void setLabelAttribute(String labelAttribute)
  {
    this.labelAttribute = labelAttribute;
  }

  public String getOrigin()
  {
    return origin;
  }

  public void setOrigin(String origin)
  {
    this.origin = origin;
  }

  public Long getSequence()
  {
    return sequence;
  }

  public void setSequence(Long sequence)
  {
    this.sequence = sequence;
  }

  public LocalizedValue getDisplayLabel()
  {
    return displayLabel;
  }

  public void setDisplayLabel(LocalizedValue displayLabel)
  {
    this.displayLabel = displayLabel;
  }

  public List<AttributeType> getAttributes()
  {
    return attributes;
  }

  public void setAttributes(List<AttributeType> attributes)
  {
    this.attributes = attributes;
  }

  public boolean hasOrigin()
  {
    return !StringUtils.isBlank(this.getOrigin());
  }

  public boolean hasOid()
  {
    return !StringUtils.isBlank(this.getOid());
  }

  public boolean hasLabelAttribute()
  {
    return !StringUtils.isBlank(this.getLabelAttribute());
  }

  public boolean hasSequence()
  {
    return this.sequence != null;
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
