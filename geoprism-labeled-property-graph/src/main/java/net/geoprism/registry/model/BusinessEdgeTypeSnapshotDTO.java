package net.geoprism.registry.model;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.serialization.LocalizedValueDeserializer;
import org.commongeoregistry.adapter.serialization.LocalizedValueSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class BusinessEdgeTypeSnapshotDTO
{
  private String         code;

  private String         orgCode;

  private String         origin;

  private Long           sequence;

  @JsonSerialize(using = LocalizedValueSerializer.class)
  @JsonDeserialize(using = LocalizedValueDeserializer.class)
  private LocalizedValue description;

  @JsonSerialize(using = LocalizedValueSerializer.class)
  @JsonDeserialize(using = LocalizedValueDeserializer.class)
  private LocalizedValue displayLabel;

  private Boolean        isChildGeoObject;

  private Boolean        isParentGeoObject;

  private String         parentType;

  private String         childType;

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
    this.code = code;
  }

  public String getOrgCode()
  {
    return orgCode;
  }

  public void setOrgCode(String orgCode)
  {
    this.orgCode = orgCode;
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

  public LocalizedValue getDescription()
  {
    return description;
  }

  public void setDescription(LocalizedValue description)
  {
    this.description = description;
  }

  public LocalizedValue getDisplayLabel()
  {
    return displayLabel;
  }

  public void setDisplayLabel(LocalizedValue displayLabel)
  {
    this.displayLabel = displayLabel;
  }

  public Boolean getIsChildGeoObject()
  {
    return isChildGeoObject;
  }

  public void setIsChildGeoObject(Boolean isChildGeoObject)
  {
    this.isChildGeoObject = isChildGeoObject;
  }

  public Boolean getIsParentGeoObject()
  {
    return isParentGeoObject;
  }

  public void setIsParentGeoObject(Boolean isParentGeoObject)
  {
    this.isParentGeoObject = isParentGeoObject;
  }

  public String getParentType()
  {
    return parentType;
  }

  public void setParentType(String parentType)
  {
    this.parentType = parentType;
  }

  public String getChildType()
  {
    return childType;
  }

  public void setChildType(String childType)
  {
    this.childType = childType;
  }

}
