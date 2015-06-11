package com.runwaysdk.geodashboard.gis.persist;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.persist.condition.LocationCondition;
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

    return label;
  }

  @Override
  protected Selectable getIdentifierSelectable(GeneratedComponentQuery query)
  {
    MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(this.geoNode.getIdentifierAttribute().getId());

    // geo id (for uniqueness)
    Selectable geoId = this.geoEntityQuery.getGeoId(mdAttribute.definesAttribute());
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
