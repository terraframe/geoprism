/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.report;

import net.geoprism.localization.LocalizationFacade;

public class PairView extends PairViewBase 
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

  public static PairView createWithLabel(String value, String label)
  {
    PairView view = new PairView();
    view.setValue(value);
    view.setLabel(label);

    return view;
  }

}
