package com.runwaysdk.geodashboard.gis.persist;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.business.generation.NameConventionUtil;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverFacade;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverProperties;
import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.geodashboard.gis.model.Layer;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.Style;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.EntityQuery;
import com.runwaysdk.query.F;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.geo.UniversalQuery;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.util.IDGenerator;
import com.runwaysdk.util.IdParser;

public class DashboardLayer extends DashboardLayerBase implements com.runwaysdk.generation.loader.Reloadable, Layer
{
  private static final long  serialVersionUID = 1992575686;

  public static final String DB_VIEW_PREFIX   = "layer$";

  private static final Log   log              = LogFactory.getLog(DashboardLayer.class);

  public DashboardLayer()
  {
    super();
  }

  @Override
  public void apply()
  {

    if (this.isNew())
    {
      // generate a db view name unique across space and time
      String vn = DB_VIEW_PREFIX + IDGenerator.nextID();
      this.setViewName(vn);
      
      this.setVirtual(true);
    }
    super.apply();
  }
  
  @Override
  @Transaction
  public void applyWithStyle(DashboardStyle style, String mapId)
  {
    boolean isNew = this.isNew();
    if(isNew && style instanceof DashboardThematicStyle)
    {
      // FIXME UI needs to allow for picking of the geo entity attribute
      DashboardThematicStyle tStyle = (DashboardThematicStyle) style;
      MdClass mdClass = tStyle.getMdAttribute().getAllDefiningClass().getAll().get(0);
      MdClassDAO md = (MdClassDAO) MdClassDAO.get(mdClass.getId());
      MdAttributeDAOIF attr = md.definesAttribute("geoentity");
      
      this.setValue(DashboardLayer.GEOENTITY, attr.getId());
    }
    
    this.apply();
    
    style.setName("style_"+IDGenerator.nextID());
    style.apply();
    
    if(isNew)
    {
      QueryFactory f = new QueryFactory();
      DashboardLayerQuery q = new DashboardLayerQuery(f);
      DashboardMapQuery mQ = new DashboardMapQuery(f);
      
      mQ.WHERE(mQ.getId().EQ(mapId));
      q.WHERE(q.containingMap(mQ));
      
      int count = (int) q.getCount();
      count++;
      
      DashboardMap map = DashboardMap.get(mapId);
      HasLayer hasLayer = map.addHasLayer(this);
      hasLayer.setLayerIndex(count);
      hasLayer.apply();
      
      
      HasStyle hasStyle = this.addHasStyle(style);
      hasStyle.apply();
    }
    
  }

  /**
   * For easy reference, the name of the SLD is the same as the db view name.
   * The .sld extension is automatically added
   * 
   * @return
   */
  public String getSLDName()
  {
    return this.getViewName();
  }

  /**
   * Returns the File object associated with the SLD for this view.
   * 
   * @return
   */
  public File getSLDFile()
  {
    String path = GeoserverProperties.getGeoserverSLDDir();
    String sld = path + this.getSLDName() + GeoserverProperties.SLD_EXTENSION;
    return new File(sld);
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
          Attribute thematicAttr = entityQ.get(mdC.getAttributeName());

          // use the basic Selectable if no aggregate is selected
          Selectable thematicSel = thematicAttr;
          List<AllAggregationType> allAgg = tStyle.getAggregationType();
          if (allAgg.size() == 1)
          {
            AllAggregationType agg = allAgg.get(0);
            // String func = null;
            if (agg == AllAggregationType.SUM)
            {
              // func = "SUM";
              thematicSel = F.SUM(thematicAttr);
            }
            else if (agg == AllAggregationType.MIN)
            {
              // func = "MIN";
              thematicSel = F.MIN(thematicAttr);
            }
            else if (agg == AllAggregationType.MAX)
            {
              // func = "MAX";
              thematicSel = F.MAX(thematicAttr);
            }
            else if (agg == AllAggregationType.AVG)
            {
              // func = "AVG";
              thematicSel = F.AVG(thematicAttr);
            }

          }
          
          thematicSel.setColumnAlias(tStyle.getAttribute());

          v.SELECT(thematicSel);

          // geoentity label
          GeoEntityQuery geQ = new GeoEntityQuery(v);
          Selectable label = geQ.getDisplayLabel().localize();
          label.setColumnAlias(GeoEntity.DISPLAYLABEL);
          v.SELECT(label);

          // geometry
          Selectable geom;
          if (this.getFeatureType().equals(FeatureType.POINT))
          {
            geom = geQ.get(GeoEntity.GEOPOINT);
          }
          else
          {
            geom = geQ.get(GeoEntity.GEOMULTIPOLYGON);
          }

          geom.setColumnAlias(GeoserverFacade.GEOM_COLUMN);
          geom.setUserDefinedAlias(GeoserverFacade.GEOM_COLUMN);

          v.SELECT(geom);

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

    if (log.isDebugEnabled())
    {
      // print the SQL if the generated
      log.debug("SLD for Layer [%s], this:\n" + v.getSQL());
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
      throw new ProgrammingErrorException("The attribute [" + DashboardLayer.GEOENTITY + "] can only reference an MdAttributeReference to [" + GeoEntity.CLASS + "]");
    }
  }
  
  @Override
  public void lock()
  {
    for (DashboardStyle style : this.getAllHasStyle())
    {
      style.lock();
    }
    
    super.lock();
  }
  
  @Override
  public void unlock()
  {
    for (DashboardStyle style : this.getAllHasStyle())
    {
      style.unlock();
    }
    
    super.unlock();
  }

  @Override
  @Transaction
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
  
  public static UniversalQuery getSortedUniversals()
  {
    QueryFactory f = new QueryFactory();
    UniversalQuery q = new UniversalQuery(f);
    
    Universal root = Universal.getRoot();
    q.WHERE(q.getId().NE(root.getId()));
    
    q.ORDER_BY_ASC(q.getDisplayLabel().localize());
    
    return q;
  }

  @Override
  public FeatureType getFeatureType()
  {
    // LayerType is required so it's safe to assume access to the object
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
