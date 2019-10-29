/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.dashboard;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.GeoNode;

import net.geoprism.dashboard.layer.DashboardThematicLayer;
import net.geoprism.dashboard.query.GeoEntityThematicQueryBuilder;
import net.geoprism.dashboard.query.ThematicQueryBuilder;
import net.geoprism.gis.geoserver.GeoserverFacade;
import net.geoprism.gis.wrapper.FeatureType;

public class UniversalAggregationStrategy extends UniversalAggregationStrategyBase 
{
  public static final Log   log              = LogFactory.getLog(UniversalAggregationStrategy.class);

  private static final long serialVersionUID = 1170847564;

  public UniversalAggregationStrategy()
  {
    super();
  }

  public String getGeometryColumn(DashboardThematicLayer layer)
  {
    if (layer.getFeatureType().equals(FeatureType.POINT))
    {
      return GeoEntity.GEOPOINT;
    }
    else
    {
      return GeoEntity.GEOMULTIPOLYGON;
    }
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

//    if (valueQuery.getCount() == 0)
//    {
//      EmptyLayerInformation info = new EmptyLayerInformation();
//      info.setLayerName(layer.getName());
//      info.apply();
//
//      info.throwIt();
//    }

    // Set the GeoID and the Geometry attribute for the second query
    GeoEntityQuery entityQuery = new GeoEntityQuery(geometryQuery);

    Selectable geoId2 = entityQuery.getGeoId(GeoEntity.GEOID);
    geoId2.setColumnAlias(ThematicQueryBuilder.LOCATION_ALIAS);
    geoId2.setUserDefinedAlias(ThematicQueryBuilder.LOCATION_ALIAS);

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

    // Join the geometry query to the values query through the geo oid
    outerQuery.WHERE(geometryQuery.aCharacter(ThematicQueryBuilder.LOCATION_ALIAS).EQ(valueQuery.aCharacter(ThematicQueryBuilder.LOCATION_ALIAS)));

    return outerQuery;
  }

  private ValueQuery getThematicValueQuery(QueryFactory factory, DashboardThematicLayer layer)
  {
    GeoEntityThematicQueryBuilder builder = new GeoEntityThematicQueryBuilder(factory, layer, this.getUniversal());
    ValueQuery valueQuery = builder.getThematicValueQuery();

    return valueQuery;
  }

  @Override
  public JSONObject getJSON()
  {
    try
    {
      JSONObject object = new JSONObject();
      object.put("type", this.getClass().getSimpleName());
      object.put("value", this.getUniversalId());
      object.put("oid", this.getOid());

      return object;
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public String getCategoryLabel(GeoNode geoNode, String categoryId)
  {
    GeoEntity entity = GeoEntity.getByKey(categoryId);

    return entity.getDisplayLabel().getValue();
  }

  @Override
  public AggregationStrategy clone()
  {
    UniversalAggregationStrategy clone = new UniversalAggregationStrategy();
    clone.setUniversal(this.getUniversal());
    clone.apply();

    return clone;
  }
}
