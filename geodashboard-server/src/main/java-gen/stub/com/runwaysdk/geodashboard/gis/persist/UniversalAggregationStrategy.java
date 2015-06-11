package com.runwaysdk.geodashboard.gis.persist;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.geodashboard.gis.EmptyLayerInformation;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverFacade;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;

public class UniversalAggregationStrategy extends UniversalAggregationStrategyBase implements com.runwaysdk.generation.loader.Reloadable
{
  public static final Log   log              = LogFactory.getLog(UniversalAggregationStrategy.class);

  private static final long serialVersionUID = 1170847564;

  public UniversalAggregationStrategy()
  {
    super();
  }

  @Override
  public ValueQuery getViewQuery(DashboardThematicLayer layer)
  {
    QueryFactory factory = new QueryFactory();

    // Query containing the aggregated values and geo entity ids
    ValueQuery valueQuery = this.getThematicValueQuery(factory, layer);

    // Query to join with the geo entity geometries
    ValueQuery geometryQuery = new ValueQuery(factory);

    // Outer query joining the value queries and the geometry query
    ValueQuery outerQuery = new ValueQuery(factory);

    if (log.isDebugEnabled())
    {
      // print the SQL if the generated
      log.debug("SLD for Layer [%s], this:\n" + valueQuery.getSQL());
    }

    if (valueQuery.getCount() == 0)
    {
      EmptyLayerInformation info = new EmptyLayerInformation();
      info.setLayerName(layer.getName());
      info.apply();

      info.throwIt();
    }

    // Set the GeoID and the Geometry attribute for the second query
    GeoEntityQuery entityQuery = new GeoEntityQuery(geometryQuery);

    Selectable geoId2 = entityQuery.getGeoId(GeoEntity.GEOID);
    geoId2.setColumnAlias(GeoEntity.GEOID);

    // geometry
    String columnName = this.getGeometryColumn(layer);

    Selectable geom = entityQuery.get(columnName);
    geom.setColumnAlias(GeoserverFacade.GEOM_COLUMN);
    geom.setUserDefinedAlias(GeoserverFacade.GEOM_COLUMN);

    geometryQuery.SELECT(geoId2, geom);

    // Add all of the selectables from the values query to the outer query
    for (Selectable selectable : valueQuery.getSelectableRefs())
    {
      Attribute attribute = valueQuery.get(selectable.getResultAttributeName());
      attribute.setColumnAlias(selectable.getColumnAlias());

      outerQuery.SELECT(attribute);
    }

    // Add the geometry selectable from the geometry query to the outer query
    Attribute geomAttribute = geometryQuery.get(GeoserverFacade.GEOM_COLUMN);
    geomAttribute.setColumnAlias(GeoserverFacade.GEOM_COLUMN);

    outerQuery.SELECT(geomAttribute);

    // Join the geometry query to the values query through the geo id
    outerQuery.WHERE(geometryQuery.aCharacter(GeoEntity.GEOID).EQ(valueQuery.aCharacter(GeoEntity.GEOID)));

    return outerQuery;
  }

  private ValueQuery getThematicValueQuery(QueryFactory factory, DashboardThematicLayer layer)
  {
    GeoEntityThematicQueryBuilder builder = new GeoEntityThematicQueryBuilder(factory, layer, this.getUniversal());
    ValueQuery valueQuery = builder.getThematicValueQuery();

    return valueQuery;
  }
}
