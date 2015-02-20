package com.runwaysdk.geodashboard.report;

import com.runwaysdk.geodashboard.localization.LocalizationFacade;

public class PairView extends PairViewBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1900255208;

  public PairView()
  {
    super();
  }

  public static PairView create(String value, String key)
  {
    PairView view = new PairView();
    view.setValue(value);
    view.setLabel(LocalizationFacade.getFromBundles(key));

    return view;
  }

}
