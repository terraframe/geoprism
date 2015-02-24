package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class LocationCondition extends LocationConditionBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1502073465;

  public LocationCondition()
  {
    super();
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    // Do nothing
  }

  @Override
  public void restrictQuery(QueryFactory factory, ValueQuery query, Attribute attr)
  {
    if (this.getComparisonValue() != null && this.getComparisonValue().length() > 0)
    {
      AttributeReference attributeReference = (AttributeReference) attr;

      GeoEntity entity = GeoEntity.get(this.getComparisonValue());

      GeoEntityAllPathsTableQuery aptQuery = new GeoEntityAllPathsTableQuery(query);

      query.WHERE(aptQuery.getParentTerm().EQ(entity));
      query.AND(attributeReference.EQ(aptQuery.getChildTerm()));
    }
  }

}
