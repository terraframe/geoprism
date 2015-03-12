package com.runwaysdk.geodashboard.gis.persist.condition;

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
  private static final long serialVersionUID = -1287604192;
  
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
    return "["+getName()+"] - "+this.getId();
  }
  
  abstract public void restrictQuery(ValueQuery query, Attribute attr);
  
  @Override
  public void setParentCondition(DashboardCondition value)
  {
    if(this.equals(value))
    {
      throw new ProgrammingErrorException("Condition ["+getName()+"] cannot be its own parent condition.");
    }

    super.setParentCondition(value);
  }
  
  @Override
  public void setRootCondition(DashboardCondition value)
  {
    if(this.equals(value))
    {
      throw new ProgrammingErrorException("Condition ["+getName()+"] cannot be its own root condition.");
    }

    super.setRootCondition(value);
  }
  
  @Override
  public ThematicStyle getThematicStyle()
  {
    QueryFactory f = new QueryFactory();
    DashboardThematicStyleQuery q = new DashboardThematicStyleQuery(f);
    
    DashboardCondition root = this.getRootCondition();
    DashboardCondition cond = root != null ? root : this;
    q.WHERE(q.getStyleCondition().EQ(cond));
    
    OIterator<? extends DashboardThematicStyle> iter = q.getIterator();
    
    try
    {
      // There should be one result
      if(iter.hasNext())
      {
        return iter.next();
      }
      else
      {
        String msg = "The condition ["+this.getName()+"] with root condition ["+root+"] is not referenced by a style.";
        throw new ProgrammingErrorException(msg);
      }
    }
    finally
    {
      iter.close();
    }
  }
}
