package com.runwaysdk.geodashboard.gis.persist.condition;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.geodashboard.gis.model.condition.Primitive;
import com.runwaysdk.geodashboard.report.ReportProviderUtil;

public abstract class DashboardPrimitive extends DashboardPrimitiveBase implements com.runwaysdk.generation.loader.Reloadable, Primitive
{
  private static final long serialVersionUID = -1224425442;

  public abstract String getOperation();

  public DashboardPrimitive()
  {
    super();
  }

  @Override
  public Object getValue()
  {
    return this.getComparisonValue();
  }

  public Date getComparisonValueAsDate()
  {
    return ReportProviderUtil.parseDate(this.getComparisonValue());
  }

  public Boolean getComparisonValueAsBoolean()
  {
    return new Boolean(this.getComparisonValue());
  }

  @Override
  public JSONObject getJSON()
  {
    try
    {
      JSONObject object = new JSONObject();
      object.put(TYPE_KEY, CONDITION_TYPE);
      object.put(MD_ATTRIBUTE_KEY, this.getDefiningMdAttributeId());
      object.put(OPERATION_KEY, this.getOperation());
      object.put(VALUE_KEY, this.getComparisonValue());

      return object;
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  protected void populate(DashboardCondition source)
  {
    super.populate(source);

    DashboardPrimitive dSource = (DashboardPrimitive) source;

    this.setComparisonValue(dSource.getComparisonValue());
  }

}
