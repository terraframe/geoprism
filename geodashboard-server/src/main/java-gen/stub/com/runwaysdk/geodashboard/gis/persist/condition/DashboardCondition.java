package com.runwaysdk.geodashboard.gis.persist.condition;

import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.geodashboard.Dashboard;
import com.runwaysdk.geodashboard.gis.model.ThematicLayer;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;
import com.runwaysdk.query.Attribute;
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

  protected abstract DashboardCondition newInstance();

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
  public ThematicLayer getThematicLayer()
  {
    String msg = "The condition [" + this.getName() + "] with condition [" + this + "] is not referenced by a layer.";
    throw new ProgrammingErrorException(msg);
  }

  public DashboardCondition clone(Dashboard dashboard)
  {
    DashboardCondition clone = this.newInstance();
    clone.setDashboard(dashboard);
    clone.populate(this);
    clone.apply();

    return clone;
  }

  protected void populate(DashboardCondition source)
  {
    this.setGeodashboardUser(source.getGeodashboardUser());
  }
}
