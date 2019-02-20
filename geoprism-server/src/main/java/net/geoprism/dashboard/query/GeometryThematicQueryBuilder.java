/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.dashboard.query;

import java.util.List;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;

import com.runwaysdk.query.AttributeLocal;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableSingle;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoNode;

import net.geoprism.dashboard.AllAggregationType;
import net.geoprism.dashboard.DashboardStyle;
import net.geoprism.dashboard.condition.DashboardCondition;
import net.geoprism.dashboard.condition.LocationCondition;
import net.geoprism.dashboard.layer.DashboardThematicLayer;

public class GeometryThematicQueryBuilder extends ThematicQueryBuilder 
{
  private GeoNode geoNode;

  public GeometryThematicQueryBuilder(QueryFactory factory, DashboardThematicLayer layer)
  {
    super(factory, layer);

    this.geoNode = layer.getGeoNode();
  }

  public GeometryThematicQueryBuilder(QueryFactory factory, MdAttributeDAOIF thematicMdAttribute, DashboardStyle style, AllAggregationType aggregation, List<DashboardCondition> conditions, GeoNode geoNode)
  {
    super(factory, thematicMdAttribute, style, aggregation, conditions);

    this.geoNode = geoNode;
  }

  @Override
  protected void initialize(ValueQuery vQuery)
  {
  }

  @Override
  protected SelectableSingle getLabelSelectable(GeneratedComponentQuery query)
  {
    MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(this.geoNode.getDisplayLabelAttribute().getOid());
    String attributeName = mdAttribute.definesAttribute();

    Selectable attribute = query.get(attributeName, GeoEntity.DISPLAYLABEL);
    SelectableSingle label = null;

    if (attribute instanceof AttributeLocal)
    {
      label = ( (AttributeLocal) attribute ).localize(attributeName);
    }
    else if (attribute instanceof SelectableSingle)
    {
      label = (SelectableSingle) attribute;
    }
    else
    {
      throw new ProgrammingErrorException("Error exception");
    }

    label.setUserDefinedAlias(ThematicQueryBuilder.LABEL_ALIAS);
    label.setColumnAlias(ThematicQueryBuilder.LABEL_ALIAS);

    return label;
  }

  @Override
  protected Selectable getOidentifierSelectable(GeneratedComponentQuery query)
  {
    MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(this.geoNode.getIdentifierAttribute().getOid());
    String attributeName = mdAttribute.definesAttribute();

    Selectable attribute = query.get(attributeName, GeoEntity.GEOID);
    attribute.setUserDefinedAlias(ThematicQueryBuilder.LOCATION_ALIAS);
    attribute.setColumnAlias(ThematicQueryBuilder.LOCATION_ALIAS);

    return attribute;
  }

  private Selectable getGeoEntityAttribute(GeneratedComponentQuery componentQuery)
  {
    MdAttributeReferenceDAOIF geoRef = MdAttributeReferenceDAO.get(this.geoNode.getGeoEntityAttributeId());

    // Join the entity's GeoEntity reference with the all paths table
    Selectable geoAttr = componentQuery.get(geoRef.definesAttribute());

    return geoAttr;
  }

  @Override
  protected void addLocationCriteria(ValueQuery vQuery, GeneratedComponentQuery componentQuery)
  {
  }

  @Override
  protected void addLocationCondition(ValueQuery vQuery, GeneratedComponentQuery componentQuery, LocationCondition condition)
  {
    condition.restrictQuery(vQuery, this.getGeoEntityAttribute(componentQuery));
  }
}
