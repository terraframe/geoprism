package com.runwaysdk.geodashboard.gis.model.condition;

import com.runwaysdk.geodashboard.gis.model.Component;
import com.runwaysdk.geodashboard.gis.model.ThematicStyle;

public interface Condition extends Component
{
  public ThematicStyle getThematicStyle();
}
