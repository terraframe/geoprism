package net.geoprism.registry.model;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.serialization.LocalizedValueDeserializer;
import org.commongeoregistry.adapter.serialization.LocalizedValueSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class SourceAuthorityDTO
{
  private String         oid;

  private String         code;

  @JsonSerialize(using = LocalizedValueSerializer.class)
  @JsonDeserialize(using = LocalizedValueDeserializer.class)
  private LocalizedValue label;

  @JsonSerialize(using = LocalizedValueSerializer.class)
  @JsonDeserialize(using = LocalizedValueDeserializer.class)
  private LocalizedValue description;

  private AuthorityType  authorityType;

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

  public LocalizedValue getLabel()
  {
    return label;
  }

  public void setLabel(LocalizedValue label)
  {
    this.label = label;
  }

  public LocalizedValue getDescription()
  {
    return description;
  }

  public void setDescription(LocalizedValue description)
  {
    this.description = description;
  }

  public AuthorityType getAuthorityType()
  {
    return authorityType;
  }

  public void setAuthorityType(AuthorityType authorityType)
  {
    this.authorityType = authorityType;
  }
}
