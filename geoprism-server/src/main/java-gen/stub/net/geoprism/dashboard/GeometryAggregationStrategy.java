/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeCharacter;
import com.runwaysdk.query.AttributeLocal;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoNode;

import net.geoprism.QueryUtil;
import net.geoprism.dashboard.layer.DashboardThematicLayer;
import net.geoprism.dashboard.layer.EmptyLayerInformation;
import net.geoprism.dashboard.query.GeometryThematicQueryBuilder;
import net.geoprism.dashboard.query.ThematicQueryBuilder;
import net.geoprism.gis.geoserver.GeoserverFacade;
import net.geoprism.gis.wrapper.FeatureType;
import net.geoprism.localization.LocalizationFacade;

public class GeometryAggregationStrategy extends GeometryAggregationStrategyBase 
{
  private static final long  serialVersionUID = 178551989;

  public static final Log    log              = LogFactory.getLog(GeometryAggregationStrategy.class);

  /**
   * Hard-coded magic value representing the use of the Geometry aggregation strategy
   */
  public static final String VALUE            = "GEOMETRY";

  public GeometryAggregationStrategy()
  {
    super();
  }

  public String getGeometryColumn(DashboardThematicLayer layer)
  {
    GeoNode geoNode = layer.getGeoNode();

    if (layer.getFeatureType().equals(FeatureType.POINT))
    {
      return geoNode.getPointAttribute().getAttributeName();
    }
    else
    {
      return geoNode.getMultiPolygonAttribute().getAttributeName();
    }
  }

  @Override
  public ValueQuery getViewQuery(DashboardThematicLayer layer)
  {
    GeoNode node = layer.getGeoNode();

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
    MdClassDAOIF mdClass = layer.getMdAttributeDAO().definedByClass();

    GeneratedComponentQuery entityQuery = QueryUtil.getQuery(mdClass, factory);

    String identifierAttribute = node.getIdentifierAttribute().getAttributeName();

    Selectable geoId2 = entityQuery.get(identifierAttribute, GeoEntity.GEOID);
    geoId2.setColumnAlias(ThematicQueryBuilder.LOCATION_ALIAS);
    geoId2.setUserDefinedAlias(ThematicQueryBuilder.LOCATION_ALIAS);

    // geometry
    String columnName = this.getGeometryColumn(layer);

    Selectable geom = entityQuery.get(columnName, GeoserverFacade.GEOM_COLUMN);
    geom.setColumnAlias(GeoserverFacade.GEOM_COLUMN);
    geom.setUserDefinedAlias(GeoserverFacade.GEOM_COLUMN);
    geom.setUserDefinedDisplayLabel(LocalizationFacade.getFromBundles("column.geometry"));    

    geometryQuery.SELECT_DISTINCT(geoId2, geom);

    // Add all of the selectables from the values query to the outer query
    for (Selectable selectable : valueQuery.getSelectableRefs())
    {
      Attribute attribute = valueQuery.get(selectable.getResultAttributeName());
      attribute.setColumnAlias(selectable.getColumnAlias());
      attribute.setUserDefinedDisplayLabel(selectable.getUserDefinedDisplayLabel());

      outerQuery.SELECT(attribute);
    }

    // Add the geometry selectable from the geometry query to the outer query
    Attribute geomAttribute = geometryQuery.get(GeoserverFacade.GEOM_COLUMN);
    geomAttribute.setColumnAlias(GeoserverFacade.GEOM_COLUMN);
    geomAttribute.setUserDefinedDisplayLabel(LocalizationFacade.getFromBundles("column.geometry"));    

    outerQuery.SELECT(geomAttribute);

    // Join the geometry query to the values query through the geo oid
    AttributeCharacter geometrySelectable = geometryQuery.aCharacter(ThematicQueryBuilder.LOCATION_ALIAS);

    outerQuery.WHERE(geometrySelectable.EQ(valueQuery.aCharacter(ThematicQueryBuilder.LOCATION_ALIAS)));

    return outerQuery;
  }

  private ValueQuery getThematicValueQuery(QueryFactory factory, DashboardThematicLayer layer)
  {
    ThematicQueryBuilder builder = new GeometryThematicQueryBuilder(factory, layer);
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
      object.put("value", VALUE);
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
    MdAttributeDAOIF identifierAttribute = MdAttributeDAO.get(geoNode.getIdentifierAttribute().getOid());
    MdAttributeDAOIF displayLabelAttribute = MdAttributeDAO.get(geoNode.getDisplayLabelAttribute().getOid());
    String attributeName = displayLabelAttribute.definesAttribute();

    MdClassDAOIF mdClass = identifierAttribute.definedByClass();

    QueryFactory factory = new QueryFactory();

    ValueQuery vQuery = new ValueQuery(factory);

    GeneratedComponentQuery query = QueryUtil.getQuery(mdClass, vQuery);
    Selectable labelAttribute = query.get(attributeName);

    if (labelAttribute instanceof AttributeLocal)
    {
      labelAttribute = ( (AttributeLocal) labelAttribute ).localize();
      labelAttribute.setColumnAlias(attributeName);
      labelAttribute.setUserDefinedAlias(attributeName);
    }

    vQuery.SELECT(labelAttribute);
    vQuery.WHERE(query.get(identifierAttribute.definesAttribute()).EQ(categoryId));

    OIterator<ValueObject> iterator = vQuery.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        ValueObject object = iterator.next();
        String label = object.getValue(attributeName);

        return label;
      }

      throw new ProgrammingErrorException("Unable to retrieve a display label for a geometry strategy object.  This should never happen");
    }
    finally
    {
      iterator.close();
    }
  }

  @Override
  public AggregationStrategy clone()
  {
    GeometryAggregationStrategy clone = new GeometryAggregationStrategy();
    clone.apply();

    return clone;
  }
}
