package com.runwaysdk.geodashboard.gis.persist;

import java.util.List;

import com.runwaysdk.business.generation.NameConventionUtil;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.geodashboard.gis.model.Layer;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.Style;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.EntityQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.util.IdParser;

public class DashboardLayer extends DashboardLayerBase implements
    com.runwaysdk.generation.loader.Reloadable, Layer
{
  private static final long serialVersionUID = 1992575686;

  public DashboardLayer()
  {
    super();
  }

  @Override
  public void apply()
  {
    // CHANGE THIS: Lewis
    if (this.isNew())
    {
      long vn = System.currentTimeMillis();
      this.setViewName(Long.toString(vn));
    }

    super.apply();
  }

  public ValueQuery asValueQuery()
  {
    QueryFactory f = new QueryFactory();
    ValueQuery v = new ValueQuery(f);

    OIterator<? extends DashboardStyle> iter = this.getAllHasStyle();
    try
    {
      while (iter.hasNext())
      {
        DashboardStyle style = iter.next();

        if (style instanceof DashboardThematicStyle)
        {
          DashboardThematicStyle tStyle = (DashboardThematicStyle) style;

          MdAttributeConcrete mdAttr = (MdAttributeConcrete) tStyle.getMdAttribute();
          MdAttributeConcrete mdC = (MdAttributeConcrete) mdAttr;
          MdClass mdClass = mdC.getDefiningMdClass();
          EntityQuery entityQ = f.businessQuery(mdClass.definesType());

          // thematic attribute
          Attribute thematic = entityQ.get(mdC.getAttributeName());
          v.SELECT(thematic);

          // geoentity label
          GeoEntityQuery geQ = new GeoEntityQuery(v);
          v.SELECT(geQ.getDisplayLabel().localize());

          // geometry
          if (this.getFeatureType().equals(FeatureType.POINT))
          {
            v.SELECT(geQ.get(GeoEntity.GEOPOINT));
          }
          else
          {
            v.SELECT(geQ.get(GeoEntity.GEOMULTIPOLYGON));
          }

          // Join the entity's GeoEntity reference with the all paths table
          MdAttributeReference geoRef = this.getGeoEntity();
          Attribute geoAttr = entityQ.get(geoRef.getAttributeName());

          // the entity's GeoEntity should match the all path's child
          GeoEntityAllPathsTableQuery geAllPathsQ = new GeoEntityAllPathsTableQuery(v);
          v.WHERE(geoAttr.LEFT_JOIN_EQ(geAllPathsQ.getChildTerm()));

          // the displayed GeoEntity should match the all path's parent
          v.AND(geAllPathsQ.getParentTerm().EQ(geQ));

          // make sure the parent GeoEntity is of the proper Universal
          Universal universal = this.getUniversal();
          v.AND(geQ.getUniversal().EQ(universal));
        }
      }
    }
    finally
    {
      iter.close();
    }

    return v;
  }

  @Override
  protected String buildKey()
  {
    String name = this.getName();
    String idRoot = IdParser.parseRootFromId(this.getId());
    return NameConventionUtil.buildAttribute(name, idRoot + "_");
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public void setGeoEntity(MdAttributeReference value)
  {
    if (value.getMdBusiness().definesType().equals(GeoEntity.CLASS))
    {
      super.setGeoEntity(value);
    }
    else
    {
      throw new ProgrammingErrorException("The attribute [" + DashboardLayer.GEOENTITY
          + "] can only reference an MdAttributeReference to [" + GeoEntity.CLASS + "]");
    }
  }

  @Override
  public void delete()
  {
    for (DashboardStyle style : this.getAllHasStyle())
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
    if (type == AllLayerType.BUBBLE)
    {
      return FeatureType.POINT;
    }
    else
    {
      return FeatureType.POLYGON;
    }
  }

}
