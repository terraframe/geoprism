package com.runwaysdk.geodashboard.gis.persist;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.ThematicStyle;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;

public class DashboardThematicStyle extends DashboardThematicStyleBase implements Reloadable, ThematicStyle
{
  private static final long serialVersionUID = -1178596850;

  public DashboardThematicStyle()
  {
    super();
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public Condition getCondition()
  {
    return this.getStyleCondition();
  }

  public JSONObject toJSON()
  {
    try
    {
      JSONObject json = new JSONObject();
      json.put("bubbleContinuousSize", this.getBubbleContinuousSize());

      return json;
    }
    catch (JSONException ex)
    {
      String msg = "Could not properly form DashboardStyle [" + this.toString() + "] into valid JSON to send back to the client.";
      throw new ProgrammingErrorException(msg, ex);
    }
  }

  @Override
  @Transaction
  public void delete()
  {
    super.delete();

    DashboardCondition cond = this.getStyleCondition();

    if (cond != null)
    {
      cond.delete();
    }
  }

  @Override
  public AllAggregationType getSecondaryAttributeAggregationMethod()
  {
    List<AllAggregationType> aggregations = this.getSecondaryAggregationType();

    if (aggregations.size() > 0)
    {
      return aggregations.get(0);
    }

    return null;
  }

  @Override
  public MdAttributeDAOIF getSecondaryAttributeDAO()
  {
    String mdAttributeId = this.getSecondaryAttributeId();

    if (mdAttributeId != null && mdAttributeId.length() > 0)
    {
      return MdAttributeDAO.get(mdAttributeId);
    }

    return null;
  }

  @Override
  public JSONArray getSecondaryAttributeCategoriesAsJSON() throws JSONException
  {
    if (this.getSecondaryCategories() != null && this.getSecondaryCategories().length() > 0)
    {
      return new JSONArray(this.getSecondaryCategories());
    }

    return null;
  }

  @Override
  public DashboardStyle clone()
  {
    DashboardThematicStyle clone = new DashboardThematicStyle();
    clone.populate(this);
    clone.apply();

    return clone;
  }

  @Override
  protected void populate(DashboardStyle source)
  {
    super.populate(source);

    if (source instanceof DashboardThematicStyle)
    {
      DashboardThematicStyle tSource = (DashboardThematicStyle) source;

      this.setBubbleContinuousSize(tSource.getBubbleContinuousSize());
      this.setBubbleMaxSize(tSource.getBubbleMaxSize());
      this.setBubbleMinSize(tSource.getBubbleMinSize());
//      this.setPointRadius(tSource.getPointRadius());
      this.setBasicPointSize(tSource.getBasicPointSize());
      this.setGradientPolygonMaxFill(tSource.getGradientPolygonMaxFill());
      this.setGradientPolygonMinFill(tSource.getGradientPolygonMinFill());
      this.addSecondaryAggregationType(tSource.getSecondaryAttributeAggregationMethod());
      this.setSecondaryAttribute(tSource.getSecondaryAttribute());
      this.setSecondaryCategories(tSource.getSecondaryCategories());
      this.setCategoryPolygonStyles(tSource.getCategoryPolygonStyles());
      this.setStyleCondition(tSource.getStyleCondition());
    }
  }
}
