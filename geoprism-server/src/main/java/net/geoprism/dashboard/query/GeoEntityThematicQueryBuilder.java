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
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.generated.system.gis.geo.LocatedInAllPathsTableQuery;

import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableSingle;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.GeoNode;
import com.runwaysdk.system.gis.geo.Universal;

import net.geoprism.dashboard.AllAggregationType;
import net.geoprism.dashboard.DashboardStyle;
import net.geoprism.dashboard.condition.DashboardCondition;
import net.geoprism.dashboard.condition.LocationCondition;
import net.geoprism.dashboard.layer.DashboardThematicLayer;

public class GeoEntityThematicQueryBuilder extends ThematicQueryBuilder 
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
    MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(this.geoNode.getDisplayLabelAttribute().getOid());

    SelectableSingle label = this.geoEntityQuery.getDisplayLabel().localize(mdAttribute.definesAttribute());
    label.setColumnAlias(ThematicQueryBuilder.LABEL_ALIAS);
    label.setUserDefinedAlias(ThematicQueryBuilder.LABEL_ALIAS);
    label.setUserDefinedDisplayLabel(mdAttribute.getDisplayLabel(Session.getCurrentLocale()));

    return label;
  }

  @Override
  protected Selectable getOidentifierSelectable(GeneratedComponentQuery query)
  {
    MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(this.geoNode.getIdentifierAttribute().getOid());

    // geo oid (for uniqueness)
    Selectable geoId = this.geoEntityQuery.getGeoId(mdAttribute.definesAttribute());
    geoId.setUserDefinedAlias(ThematicQueryBuilder.LOCATION_ALIAS);
    geoId.setColumnAlias(ThematicQueryBuilder.LOCATION_ALIAS);
    geoId.setUserDefinedDisplayLabel(mdAttribute.getDisplayLabel(Session.getCurrentLocale()));

    return geoId;
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
    Attribute geoAttr = (Attribute) this.getGeoEntityAttribute(componentQuery);

    // the entity's GeoEntity should match the all path's child
    LocatedInAllPathsTableQuery geAllPathsQ = new LocatedInAllPathsTableQuery(vQuery);
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
