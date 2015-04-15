package com.runwaysdk.geodashboard.gis.model;

import com.runwaysdk.geodashboard.gis.model.condition.And;
import com.runwaysdk.geodashboard.gis.model.condition.Category;
import com.runwaysdk.geodashboard.gis.model.condition.Equal;
import com.runwaysdk.geodashboard.gis.model.condition.Gradient;
import com.runwaysdk.geodashboard.gis.model.condition.GreaterThan;
import com.runwaysdk.geodashboard.gis.model.condition.GreaterThanOrEqual;
import com.runwaysdk.geodashboard.gis.model.condition.IsBetween;
import com.runwaysdk.geodashboard.gis.model.condition.IsLike;
import com.runwaysdk.geodashboard.gis.model.condition.IsNull;
import com.runwaysdk.geodashboard.gis.model.condition.LessThan;
import com.runwaysdk.geodashboard.gis.model.condition.LessThanOrEqual;
import com.runwaysdk.geodashboard.gis.model.condition.NotEqual;
import com.runwaysdk.geodashboard.gis.model.condition.Or;

public interface MapVisitor
{
  public void visit(Map component);

  public void visit(ThematicLayer component);
  
  public void visit(ReferenceLayer component);

  public void visit(Style style);

  public void visit(ThematicStyle component);

  public void visit(Or component);

  public void visit(And component);

  public void visit(Equal component);

  public void visit(NotEqual component);

  public void visit(GreaterThan component);

  public void visit(GreaterThanOrEqual component);

  public void visit(LessThan component);

  public void visit(LessThanOrEqual component);

  public void visit(IsLike component);

  public void visit(IsNull component);

  public void visit(IsBetween component);

  public void visit(Gradient component);

  public void visit(Category component);
}
