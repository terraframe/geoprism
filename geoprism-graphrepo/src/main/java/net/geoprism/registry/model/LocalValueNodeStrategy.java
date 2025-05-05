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

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.localization.LocalizationFacade;

import net.geoprism.registry.graph.AttributeType;

public class LocalValueNodeStrategy extends ValueNodeStrategy implements ValueStrategy
{

  public LocalValueNodeStrategy(AttributeType type, MdVertexDAOIF nodeVertex, String nodeAttribute)
  {
    super(type, nodeVertex, nodeAttribute);
  }

  @Override
  public List<MdAttributeDAOIF> getValueAttributes()
  {
    List<MdAttributeDAOIF> list = new LinkedList<MdAttributeDAOIF>();
    list.add(this.getNodeVertex().definesAttribute(LocalizedValue.DEFAULT_LOCALE));

    Set<Locale> locales = LocalizationFacade.getInstalledLocales();

    for (Locale locale : locales)
    {
      MdAttributeDAOIF mdAttribute = this.getNodeVertex().definesAttribute(locale.toString());

      if (mdAttribute != null)
      {
        list.add(mdAttribute);
      }
    }

    return list;
  }

  @Override
  protected void setNodeValue(VertexObject node, Object value, Boolean validate)
  {
    if (value instanceof LocalizedValue)
    {
      LocalizedValue lValue = (LocalizedValue) value;

      node.setValue(LocalizedValue.DEFAULT_LOCALE, lValue.getValue(LocalizedValue.DEFAULT_LOCALE));

      Set<Locale> locales = LocalizationFacade.getInstalledLocales();

      for (Locale locale : locales)
      {
        String localeName = locale.toString();

        if (lValue.contains(locale) && node.hasAttribute(localeName))
        {
          node.setValue(localeName, lValue.getValue(localeName));
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  protected <T> T getNodeValue(VertexObject node)
  {
    LocalizedValue value = new LocalizedValue(node.getObjectValue(LocalizedValue.DEFAULT_LOCALE));
    value.setValue(LocalizedValue.DEFAULT_LOCALE, node.getObjectValue(LocalizedValue.DEFAULT_LOCALE));

    Set<Locale> locales = LocalizationFacade.getInstalledLocales();

    for (Locale locale : locales)
    {
      String localeName = locale.toString();

      if (node.hasAttribute(localeName))
      {
        value.setValue(locale, node.getObjectValue(localeName));
      }
    }

    return (T) value;
  }

}
