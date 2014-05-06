package com.runwaysdk.geodashboard.gis.persist;

import java.util.List;

import com.runwaysdk.business.generation.NameConventionUtil;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.geodashboard.gis.model.Layer;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.Style;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.util.IdParser;

public class DashboardLayer extends DashboardLayerBase implements com.runwaysdk.generation.loader.Reloadable, Layer
{
  private static final long serialVersionUID = 1992575686;
  
  public DashboardLayer()
  {
    super();
  }
  
  @Override
  protected String buildKey()
  {
    String name = this.getName();
    String idRoot = IdParser.parseRootFromId(this.getId());    
    return NameConventionUtil.buildAttribute(name, idRoot+"_");
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);    
  }
  
  @Override
  public void setGeoEntity(MdAttributeReference value)
  {
    if(value.getMdBusiness().definesType().equals(GeoEntity.CLASS))
    {
      super.setGeoEntity(value);
    }
    else
    {
      throw new ProgrammingErrorException("The attribute ["+DashboardLayer.GEOENTITY+"] can only reference an MdAttributeReference to ["+GeoEntity.CLASS+"]");
    }
  }
  
  @Override
  public void delete()
  {
    for(DashboardStyle style : this.getAllHasStyle())
    {
      style.delete();
    }
    
    super.delete();
  }

  @Override
  public List<? extends Style> getStyles()
  {
    return this.getAllHasStyle().getAll();
  }

  @Override
  public FeatureType getFeatureType()
  {
    AllLayerType type = this.getLayerType().get(0);
    if(type == AllLayerType.BUBBLE)
    {
      return FeatureType.POINT;
    }
    else
    {
      return FeatureType.POLYGON;
    }
  }
  
}
