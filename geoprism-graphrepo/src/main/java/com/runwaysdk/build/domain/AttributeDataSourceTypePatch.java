/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.build.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import com.runwaysdk.session.Request;

import net.geoprism.registry.graph.AttributeBooleanType;
import net.geoprism.registry.graph.AttributeDataSourceType;
import net.geoprism.registry.graph.AttributeUUIDType;
import net.geoprism.registry.model.ServerGeoObjectType;

@Service
public class AttributeDataSourceTypePatch
{

  @Request
  private void doIt()
  {
    List<ServerGeoObjectType> types = ServerGeoObjectType.getAll().stream().filter(type -> {      
      return !type.getIsAbstract() && !type.getAttributeMap().containsKey(DefaultAttribute.DATA_SOURCE.getName());
    }).collect(Collectors.toList());
    
    types.forEach(type -> {
      // Create the default source attribute
      
      AttributeDataSourceType sourceAttr = new AttributeDataSourceType();
      sourceAttr.setCode(DefaultAttribute.DATA_SOURCE.getName());
      sourceAttr.setEmbeddedValue(AttributeUUIDType.LABEL, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.DATA_SOURCE.getDefaultLocalizedName());
      sourceAttr.setEmbeddedValue(AttributeUUIDType.DESCRIPTION, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.DATA_SOURCE.getDefaultDescription());
      sourceAttr.setValue(AttributeBooleanType.GEOOBJECTTYPE, type.getOid());
      sourceAttr.setRequired(false);
      sourceAttr.setUnique(false);
      sourceAttr.setIsChangeOverTime(true);
      sourceAttr.setIsDefault(true);
      sourceAttr.apply();

    });
  }

  public static void main(String[] args)
  {

    try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("net.geoprism.spring.core", "net.geoprism.registry.service.business", "net.geoprism.registry.service.permission"))
    {
      context.register(AttributeDataSourceTypePatch.class);

      AttributeDataSourceTypePatch service = context.getBean(AttributeDataSourceTypePatch.class);

      new AttributeDataSourceTypePatch().doIt();
    }
  }

}
