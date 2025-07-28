package net.geoprism.registry.view;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.model.EdgeDirection;

public class BusinessGeoEdgeTypeView
{
  private String         organizationCode;

  private String         code;

  private LocalizedValue label;

  private LocalizedValue description;

  private String         typeCode;

  private EdgeDirection  direction;

  private String         origin;

  private Long           seq;

  public BusinessGeoEdgeTypeView()
  {
    this.origin = GeoprismProperties.getOrigin();
  }

  public String getOrganizationCode()
  {
    return organizationCode;
  }

  public void setOrganizationCode(String organizationCode)
  {
    this.organizationCode = organizationCode;
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

  public String getTypeCode()
  {
    return typeCode;
  }

  public void setTypeCode(String typeCode)
  {
    this.typeCode = typeCode;
  }

  public EdgeDirection getDirection()
  {
    return direction;
  }

  public void setDirection(EdgeDirection direction)
  {
    this.direction = direction;
  }

  public String getOrigin()
  {
    return origin;
  }

  public void setOrigin(String origin)
  {
    this.origin = origin;
  }

  public Long getSeq()
  {
    return seq;
  }

  public void setSeq(Long seq)
  {
    this.seq = seq;
  }

  public static BusinessGeoEdgeTypeView build(String organizationCode, String code, LocalizedValue label, LocalizedValue description, String typeCode, EdgeDirection direction)
  {    
    BusinessGeoEdgeTypeView view = new BusinessGeoEdgeTypeView();
    view.setCode(code);
    view.setLabel(label);
    view.setDescription(description);
    view.setOrganizationCode(organizationCode);
    view.setTypeCode(typeCode);
    view.setDirection(direction);
    view.setSeq(0L);

    return view;
  }


}
