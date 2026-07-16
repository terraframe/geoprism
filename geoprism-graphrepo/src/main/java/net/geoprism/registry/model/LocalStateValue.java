/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.registry.model;

import java.util.Locale;
import java.util.Set;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.localization.LocalizationFacade;

public class LocalStateValue extends StateValue
{

  public LocalStateValue(VertexObject node)
  {
    super(node);
  }

  @Override
  public void setValue(Object value)
  {
    if (value instanceof LocalizedValue)
    {
      LocalizedValue lValue = (LocalizedValue) value;

      this.setValue(LocalizedValue.DEFAULT_LOCALE, lValue.getValue(LocalizedValue.DEFAULT_LOCALE));

      Set<Locale> locales = LocalizationFacade.getInstalledLocales();

      for (Locale locale : locales)
      {
        String localeName = locale.toString();

        if (lValue.contains(locale) && this.hasAttribute(localeName))
        {
          this.setValue(localeName, lValue.getValue(localeName));
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getValue()
  {
    LocalizedValue value = new LocalizedValue(this.getValue(LocalizedValue.DEFAULT_LOCALE));
    value.setValue(LocalizedValue.DEFAULT_LOCALE, this.getValue(LocalizedValue.DEFAULT_LOCALE));

    Set<Locale> locales = LocalizationFacade.getInstalledLocales();

    for (Locale locale : locales)
    {
      String localeName = locale.toString();

      if (this.hasAttribute(localeName))
      {
        value.setValue(locale, this.getValue(localeName));
      }
    }

    return (T) value;
  }

}
