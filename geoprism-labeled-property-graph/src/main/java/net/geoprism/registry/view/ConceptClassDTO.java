package net.geoprism.registry.view;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.serialization.LocalizedValueDeserializer;
import org.commongeoregistry.adapter.serialization.LocalizedValueSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ConceptClassDTO
{
  private String              oid;

  private String              code;

  private String              organization;

  private String              organizationLabel;

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

  public boolean hasSequence()
  {
    return this.sequence != null;
  }
}
