package com.runwaysdk.geodashboard.gis.persist;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.geodashboard.GeoEntityUtil;
import com.runwaysdk.geodashboard.MetadataGeoNode;
import com.runwaysdk.geodashboard.MetadataGeoNodeQuery;
import com.runwaysdk.geodashboard.MetadataWrapper;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoNode;
import com.runwaysdk.system.gis.geo.GeoNodeGeometry;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdAttribute;

public class AggregationStrategyView extends AggregationStrategyViewBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1241142559;

  public AggregationStrategyView()
  {
    super();
  }

  public static AggregationStrategyView[] getAggregationStrategies(GeoNode node)
  {
    List<AggregationStrategyView> list = new LinkedList<AggregationStrategyView>();

    MetadataGeoNodeQuery query = new MetadataGeoNodeQuery(new QueryFactory());
    query.WHERE(query.getChild().EQ(node));

    OIterator<? extends MetadataGeoNode> iterator = query.getIterator();

    try
    {
      MetadataGeoNode relationship = iterator.next();
      MetadataWrapper wrapper = relationship.getParent();

      Universal lowest = wrapper.getUniversal();
      Universal root = Universal.getRoot();

      Collection<Term> universals = GeoEntityUtil.getOrderedAncestors(root, lowest, AllowedIn.CLASS);

      for (Term universal : universals)
      {
        AggregationStrategyView view = new AggregationStrategyView();
        view.setAggregationType(UniversalAggregationStrategy.CLASS);
        view.setValue(universal.getId());
        view.setDisplayLabel(universal.getDisplayLabel().getValue());

        list.add(view);
      }
    }
    finally
    {
      iterator.close();
    }

    if (node instanceof GeoNodeGeometry)
    {
      String label = getDisplayLabel(node);

      AggregationStrategyView view = new AggregationStrategyView();
      view.setAggregationType(GeometryAggregationStrategy.CLASS);
      view.setValue(GeometryAggregationStrategy.VALUE);
      view.setDisplayLabel(label);

      list.add(view);
    }

    return list.toArray(new AggregationStrategyView[list.size()]);
  }

  public static String getDisplayLabel(GeoNode node)
  {
    MdAttribute attribute = node.getGeometryAttribute();

    if (attribute == null)
    {
      attribute = node.getPointAttribute();
    }

    if (attribute == null)
    {
      attribute = node.getMultiPolygonAttribute();
    }

    if (attribute == null)
    {
      attribute = node.getGeoEntityAttribute();
    }

    return attribute.getDisplayLabel().getValue();
  }
}
