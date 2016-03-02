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

import net.geoprism.dashboard.condition.DashboardCondition;
import net.geoprism.dashboard.condition.LocationCondition;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.persist.AllAggregationType;
import com.runwaysdk.geodashboard.gis.persist.DashboardStyle;
import com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableSingle;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.GeoNode;
import com.runwaysdk.system.gis.geo.Universal;

public class GeoEntityThematicQueryBuilder extends ThematicQueryBuilder implements Reloadable
{
  private GeoEntityQuery geoEntityQuery;

  private Universal      universal;

  private GeoNode        geoNode;

  public GeoEntityThematicQueryBuilder(QueryFactory factory, DashboardThematicLayer layer, Universal universal)
  {
    super(factory, layer);

    this.universal = universal;
    this.geoNode = layer.getGeoNode();
  }

  public GeoEntityThematicQueryBuilder(QueryFactory factory, MdAttributeDAOIF thematicMdAttribute, DashboardStyle style, AllAggregationType aggregation, List<DashboardCondition> conditions, Universal universal, GeoNode geoNode)
  {
    super(factory, thematicMdAttribute, style, aggregation, conditions);

    this.universal = universal;
    this.geoNode = geoNode;
  }

  @Override
  protected void initialize(ValueQuery vQuery)
  {
    this.geoEntityQuery = new GeoEntityQuery(vQuery);
  }

  @Override
  protected SelectableSingle getLabelSelectable(GeneratedComponentQuery query)
  {
    MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(this.geoNode.getDisplayLabelAttribute().getId());

    SelectableSingle label = this.geoEntityQuery.getDisplayLabel().localize(mdAttribute.definesAttribute());
    label.setColumnAlias(GeoEntity.DISPLAYLABEL);
    label.setUserDefinedAlias(GeoEntity.DISPLAYLABEL);

    return label;
  }

  @Override
  protected Selectable getIdentifierSelectable(GeneratedComponentQuery query)
  {
    MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(this.geoNode.getIdentifierAttribute().getId());

    // geo id (for uniqueness)
    Selectable geoId = this.geoEntityQuery.getGeoId(mdAttribute.definesAttribute());
    geoId.setUserDefinedAlias(GeoEntity.GEOID);
    geoId.setColumnAlias(GeoEntity.GEOID);

    return geoId;
  }

  private Attribute getGeoEntityAttribute(GeneratedComponentQuery componentQuery)
  {
    MdAttributeReferenceDAOIF geoRef = MdAttributeReferenceDAO.get(this.geoNode.getGeoEntityAttributeId());

    // Join the entity's GeoEntity reference with the all paths table
    Attribute geoAttr = componentQuery.get(geoRef.definesAttribute());

    return geoAttr;
  }

  @Override
  protected void addLocationCriteria(ValueQuery vQuery, GeneratedComponentQuery componentQuery)
  {
    Attribute geoAttr = this.getGeoEntityAttribute(componentQuery);

    // the entity's GeoEntity should match the all path's child
    GeoEntityAllPathsTableQuery geAllPathsQ = new GeoEntityAllPathsTableQuery(vQuery);
    vQuery.WHERE(geoAttr.LEFT_JOIN_EQ(geAllPathsQ.getChildTerm()));

    // the displayed GeoEntity should match the all path's parent
    vQuery.AND(geAllPathsQ.getParentTerm().EQ(this.geoEntityQuery));

    // make sure the parent GeoEntity is of the proper Universal
    vQuery.AND(this.geoEntityQuery.getUniversal().EQ(this.universal));
  }

  @Override
  protected void addLocationCondition(ValueQuery vQuery, GeneratedComponentQuery componentQuery, LocationCondition condition)
  {
    condition.restrictQuery(vQuery, this.getGeoEntityAttribute(componentQuery));
  }
}
