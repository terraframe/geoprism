package com.runwaysdk.geodashboard.gis.persist;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.impl.SecondaryAttributeStyleImpl;
import com.runwaysdk.geodashboard.gis.model.SecondaryAttributeStyleIF;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public class SecondaryAttributeStyle extends SecondaryAttributeStyleBase implements Reloadable, SecondaryAttributeStyleIF
{
  private static final long serialVersionUID = -287065018;

  public SecondaryAttributeStyle()
  {
    super();
  }

  public AllAggregationType getAggregationMethod()
  {
    List<AllAggregationType> aggregations = this.getAggregationType();

    if (aggregations.size() > 0)
    {
      return aggregations.get(0);
    }

    return null;
  }

  public MdAttributeDAOIF getMdAttributeDAO()
  {
    String mdAttributeId = this.getMdAttributeId();

    if (mdAttributeId != null && mdAttributeId.length() > 0)
    {
      return MdAttributeDAO.get(mdAttributeId);
    }

    return null;
  }

  @Override
  public JSONArray getCategoriesAsJSON() throws JSONException
  {
    if (this.getCategories() != null && this.getCategories().length() > 0)
    {
      return new JSONArray(this.getCategories());
    }

    return null;
  }

  public static SecondaryAttributeStyle getSecondaryAttributeStyle(String thematicLayerId)
  {
    SecondaryAttributeStyleQuery query = new SecondaryAttributeStyleQuery(new QueryFactory());
    query.WHERE(query.getThematicLayer().EQ(thematicLayerId));

    OIterator<? extends SecondaryAttributeStyle> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        return iterator.next();
      }
    }
    finally
    {
      iterator.close();
    }

    return null;
  }

  public static SecondaryAttributeStyleIF getSecondaryAttributeStyleIF(String thematicLayerId)
  {
    // return getSecondaryAttributeStyles(thematicStyleId);

    /*
     * Spoof SecondaryAttributeStyle
     */
    MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO("org.ideorg.iq.cambodia.KhDeliverySummary");

    JSONArray categories = new JSONArray();
    categories.put(SecondaryAttributeStyleImpl.createCategory("Active", "#F4A460"));
    categories.put(SecondaryAttributeStyleImpl.createCategory("Inactive", "#A4A460"));
    categories.put(SecondaryAttributeStyleImpl.createCategory("Active, Inactive", "#C4A460"));

    SecondaryAttributeStyleImpl style = new SecondaryAttributeStyleImpl(mdClass, "sanitationTeacherStatus", AllAggregationType.MAJORITY, categories);

    return style;
  }
}
