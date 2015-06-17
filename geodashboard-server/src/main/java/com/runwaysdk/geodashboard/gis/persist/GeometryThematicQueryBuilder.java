package com.runwaysdk.geodashboard.gis.persist;

import java.util.List;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;
import com.runwaysdk.geodashboard.gis.persist.condition.LocationCondition;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeLocal;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableSingle;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoNode;

public class GeometryThematicQueryBuilder extends ThematicQueryBuilder implements Reloadable
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
    MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(this.geoNode.getDisplayLabelAttribute().getId());
    String attributeName = mdAttribute.definesAttribute();

    Attribute attribute = query.get(attributeName, GeoEntity.DISPLAYLABEL);
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

    label.setUserDefinedAlias(GeoEntity.DISPLAYLABEL);
    label.setColumnAlias(GeoEntity.DISPLAYLABEL);

    return label;
  }

  @Override
  protected Selectable getIdentifierSelectable(GeneratedComponentQuery query)
  {
    MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(this.geoNode.getIdentifierAttribute().getId());
    String attributeName = mdAttribute.definesAttribute();

    Selectable attribute = query.get(attributeName, GeoEntity.GEOID);
    attribute.setUserDefinedAlias(GeoEntity.GEOID);
    attribute.setColumnAlias(GeoEntity.GEOID);

    return attribute;
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
  }

  @Override
  protected void addLocationCondition(ValueQuery vQuery, GeneratedComponentQuery componentQuery, LocationCondition condition)
  {
    condition.restrictQuery(vQuery, this.getGeoEntityAttribute(componentQuery));
  }
}
