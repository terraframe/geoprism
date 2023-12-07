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

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;

import com.runwaysdk.localization.LocalizationFacade;
import com.runwaysdk.localization.LocalizedValueIF;

import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;

public class GeoObjectTypeMetadata extends GeoObjectTypeMetadataBase
{
  private static final long  serialVersionUID = -427820585;

  public static final String TYPE_LABEL       = "geoObjectType.label";

  public GeoObjectTypeMetadata()
  {
    super();
  }
  
  public void injectDisplayLabels(GeoObjectType type)
  {
    for (DefaultAttribute defaultAttr : DefaultAttribute.values())
    {
      if (type.getAttribute(defaultAttr.getName()).isPresent())
      {
        AttributeType attr = type.getAttribute(defaultAttr.getName()).get();
        
        LocalizedValueIF val = LocalizationFacade.localizeAll("geoObjectType.attr."  + attr.getName());
        
        if (val != null)
        {
          attr.setLabel(RegistryLocalizedValueConverter.convert(val));
        }
      }
    }
  }

  public ServerGeoObjectType getServerType()
  {
    return ServerGeoObjectType.get(this.getUniversal());
  }

  @Override
  protected String buildKey()
  {
    return this.getUniversal().getKey();
  }

  public String getClassDisplayLabel()
  {
    return sGetClassDisplayLabel();
  }

  public static String sGetClassDisplayLabel()
  {
    return LocalizationFacade.localize(TYPE_LABEL);
  }

  public static String getAttributeDisplayLabel(String attributeName)
  {
    return LocalizationFacade.localize("geoObjectType.attr." + attributeName);
  }

}
