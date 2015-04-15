package com.runwaysdk.geodashboard.gis.persist;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.gis.model.AttributeType;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.SecondaryAttributeStyleIF;
import com.runwaysdk.geodashboard.gis.model.ThematicStyle;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;

public class DashboardThematicStyle extends DashboardThematicStyleBase implements com.runwaysdk.generation.loader.Reloadable, ThematicStyle
{
  private static final long serialVersionUID = -1178596850;

  public DashboardThematicStyle()
  {
    super();
  }

  @Override
  public String getAttribute()
  {
    return MdAttributeDAO.get(this.getMdAttributeId()).definesAttribute();
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
      json.put("mdAttribute", this.getMdAttribute().getId());
      json.put("bubbleContinuousSize", this.getBubbleContinuousSize());
      json.put("attributeType", this.getAttributeType());

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
  public AttributeType getAttributeType()
  {
    MdAttributeDAOIF mdAttribute = this.getMdAttributeDAO().getMdAttributeConcrete();

    if (mdAttribute instanceof MdAttributeDateDAOIF)
    {
      return AttributeType.DATE;
    }
    else if (mdAttribute instanceof MdAttributeDateTimeDAOIF)
    {
      return AttributeType.DATETIME;
    }
    else if (mdAttribute instanceof MdAttributeTimeDAOIF)
    {
      return AttributeType.TIME;
    }
    else if (mdAttribute instanceof MdAttributeNumberDAOIF)
    {
      return AttributeType.NUMBER;
    }

    return AttributeType.BASIC;
  }

  public AllAggregationType getSingleAggregationType()
  {
    List<AllAggregationType> allAgg = this.getAggregationType();

    if (allAgg.size() > 0)
    {
      return allAgg.get(0);
    }

    return null;
  }

  @Override
  public SecondaryAttributeStyleIF getSecondaryAttributeStyle()
  {
    return SecondaryAttributeStyle.getSecondaryAttributeStyleIF(this.getId());
  }
}
