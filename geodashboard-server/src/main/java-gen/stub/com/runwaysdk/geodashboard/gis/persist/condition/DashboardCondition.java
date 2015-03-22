package com.runwaysdk.geodashboard.gis.persist.condition;

import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.geodashboard.gis.model.ThematicStyle;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;
import com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle;
import com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyleQuery;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;

public abstract class DashboardCondition extends DashboardConditionBase implements com.runwaysdk.generation.loader.Reloadable, Condition
{
  private static final long  serialVersionUID = -1287604192;

  /**
   * Magic value for the json attribute name which specifies the operation type
   */
  public static final String OPERATION_KEY    = "operation";

  /**
   * Magic value for the json attribute name which specifies the value
   */
  public static final String VALUE_KEY        = "value";

  /**
   * Magic value for the json attribute name which specifies the value
   */
  public static final String TYPE_KEY         = "type";

  public abstract void restrictQuery(ValueQuery query, Attribute attr);

  public abstract JSONObject getJSON();

  public abstract String getJSONKey();

  public DashboardCondition()
  {
    super();
  }

  @Override
  public String getName()
  {
    return this.getClass().getSimpleName();
  }

  @Override
  public String toString()
  {
    return "[" + getName() + "] - " + this.getId();
  }

  @Override
  public ThematicStyle getThematicStyle()
  {
    QueryFactory f = new QueryFactory();
    DashboardThematicStyleQuery q = new DashboardThematicStyleQuery(f);

    q.WHERE(q.getStyleCondition().EQ(this));

    OIterator<? extends DashboardThematicStyle> iter = q.getIterator();

    try
    {
      // There should be one result
      if (iter.hasNext())
      {
        return iter.next();
      }
      else
      {
        String msg = "The condition [" + this.getName() + "] with condition [" + this + "] is not referenced by a style.";
        throw new ProgrammingErrorException(msg);
      }
    }
    finally
    {
      iter.close();
    }
  }

}
