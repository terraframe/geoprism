package com.runwaysdk.geodashboard.gis.persist;

import java.util.List;

import org.json.JSONObject;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.geodashboard.QueryUtil;
import com.runwaysdk.geodashboard.gis.EmptyLayerInformation;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverFacade;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverProperties;
import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.ReferenceLayer;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardAttributeCondition;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;
import com.runwaysdk.geodashboard.gis.persist.condition.LocationCondition;
import com.runwaysdk.geodashboard.ontology.Classifier;
import com.runwaysdk.geodashboard.ontology.ClassifierQuery;
import com.runwaysdk.query.AggregateFunction;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.F;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OrderBy;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableMoment;
import com.runwaysdk.query.SelectableNumber;
import com.runwaysdk.query.SelectableSingle;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdAttribute;

public class DashboardReferenceLayer extends DashboardReferenceLayerBase implements com.runwaysdk.generation.loader.Reloadable, ReferenceLayer
{
  private static final long serialVersionUID = -1393835330;
  
  public DashboardReferenceLayer()
  {
    super();
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    // TODO Auto-generated method stub
    
  }
  
//  @Transaction
//  protected void applyWithStyleInTransaction(DashboardStyle style, String mapId, DashboardCondition[] conditions)
//  {
//
//    boolean isNew = this.isNew();
//
//    super.applyWithStyleInTransaction(style, mapId, conditions);
//
//    if (isNew && style instanceof DashboardThematicStyle)
//    {
//      DashboardThematicStyle tStyle = (DashboardThematicStyle) style;
//
//      MdAttribute md = MdAttribute.get(tStyle.getMdAttributeId());
//      this.setMdAttribute(md);
//    }
//  }

  @Override
  public JSONObject toJSON()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  /**
   * @prerequisite conditions is populated with any DashboardConditions
   *               necessary for restricting the view dataset.
   * 
   * @return A ValueQuery for use in creating/dropping the database view which
   *         will be used with GeoServer.
   */
  @Override
  public ValueQuery getViewQuery()
  {
    QueryFactory factory = new QueryFactory();
    ValueQuery query = new ValueQuery(factory);

    ValueQuery outerQuery = new ValueQuery(factory);

    OIterator<? extends DashboardStyle> iter = this.getAllHasStyle();
    try
    {
      while (iter.hasNext())
      {
        DashboardStyle style = iter.next();
        if (style instanceof DashboardStyle)
        {
          
          Integer length = GeoserverProperties.getDecimalLength();
          Integer precision = GeoserverProperties.getDecimalPrecision();
          String sql;

          // geoentity label
          GeoEntityQuery geQ1 = new GeoEntityQuery(query);
          SelectableSingle label = geQ1.getDisplayLabel().localize(GeoEntity.DISPLAYLABEL);
          label.setColumnAlias(GeoEntity.DISPLAYLABEL);

          // geo id (for uniqueness)
          Selectable geoId1 = geQ1.getGeoId(GeoEntity.GEOID);
          geoId1.setColumnAlias(GeoEntity.GEOID);

          // make sure the parent GeoEntity is of the proper Universal
          Universal universal = this.getUniversal();
          query.AND(geQ1.getUniversal().EQ(universal));
        
          query.SELECT(label);
          query.SELECT(geoId1);
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
      log.debug("SLD for Layer [%s], this:\n" + query.getSQL());
    }

    this.viewHasData = true;
    if (query.getCount() == 0)
    {
      EmptyLayerInformation info = new EmptyLayerInformation();
      info.setLayerName(this.getName());
      info.apply();

      info.throwIt();

      this.viewHasData = false;
    }

    // Set the GeoID and the Geometry attribute for the second query
    GeoEntityQuery geQ2 = new GeoEntityQuery(query);
    Selectable geoId2 = geQ2.getGeoId(GeoEntity.GEOID);
    geoId2.setColumnAlias(GeoEntity.GEOID);
    query.SELECT(geoId2);
    // geometry
    Selectable geom;
    if (this.getFeatureType().equals(FeatureType.POINT))
    {
      geom = geQ2.get(GeoEntity.GEOPOINT);
    }
    else
    {
      geom = geQ2.get(GeoEntity.GEOMULTIPOLYGON);
    }

    geom.setColumnAlias(GeoserverFacade.GEOM_COLUMN);
    geom.setUserDefinedAlias(GeoserverFacade.GEOM_COLUMN);
    query.SELECT(geom);

//    for (Selectable selectable : innerQuery.getSelectableRefs())
//    {
//      Attribute attribute = innerQuery.get(selectable.getResultAttributeName());
//      attribute.setColumnAlias(selectable.getColumnAlias());
//
//      outerQuery.SELECT(attribute);
//    }

//    Attribute geomAttribute = innerQuery.get(GeoserverFacade.GEOM_COLUMN);
//    geomAttribute.setColumnAlias(GeoserverFacade.GEOM_COLUMN);
//    outerQuery.SELECT(geomAttribute);
//    outerQuery
//        .WHERE(innerQuery2.aCharacter(GeoEntity.GEOID).EQ(innerQuery.aCharacter(GeoEntity.GEOID)));

    return query;
  }

}



