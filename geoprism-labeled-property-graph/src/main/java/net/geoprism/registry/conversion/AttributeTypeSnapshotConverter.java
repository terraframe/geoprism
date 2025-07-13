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
package net.geoprism.registry.conversion;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeBooleanType;
import org.commongeoregistry.adapter.metadata.AttributeCharacterType;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.commongeoregistry.adapter.metadata.AttributeDateType;
import org.commongeoregistry.adapter.metadata.AttributeFloatType;
import org.commongeoregistry.adapter.metadata.AttributeIntegerType;
import org.commongeoregistry.adapter.metadata.AttributeLocalType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.session.Session;

import net.geoprism.graph.AttributeBooleanTypeSnapshot;
import net.geoprism.graph.AttributeCharacterTypeSnapshot;
import net.geoprism.graph.AttributeClassificationTypeSnapshot;
import net.geoprism.graph.AttributeDateTypeSnapshot;
import net.geoprism.graph.AttributeDoubleTypeSnapshot;
import net.geoprism.graph.AttributeLocalTypeSnapshot;
import net.geoprism.graph.AttributeLongTypeSnapshot;
import net.geoprism.graph.AttributeTermTypeSnapshot;
import net.geoprism.graph.AttributeTypeSnapshot;
import net.geoprism.registry.model.Classification;
import net.geoprism.registry.model.ClassificationType;
import net.geoprism.registry.service.business.ClassificationBusinessServiceIF;
import net.geoprism.registry.service.business.ClassificationTypeBusinessServiceIF;
import net.geoprism.spring.core.ApplicationContextHolder;

public class AttributeTypeSnapshotConverter
{
  public AttributeType build(AttributeTypeSnapshot attribute)
  {
    Locale locale = Session.getCurrentLocale();

    String attributeName = attribute.getCode();
    LocalizedValue displayLabel = LocalizedValueConverter.convertNoAutoCoalesce(attribute.getLabel());
    LocalizedValue description = LocalizedValueConverter.convertNoAutoCoalesce(attribute.getDescription());
    boolean required = attribute.getIsRequired();
    boolean unique = attribute.getIsUnique();

    boolean isChangeOverTime = true;
    DefaultAttribute defaultAttr = DefaultAttribute.getByAttributeName(attributeName);
    if (defaultAttr != null)
    {
      isChangeOverTime = defaultAttr.isChangeOverTime();
    }

    if (attribute instanceof AttributeBooleanTypeSnapshot)
    {
      return AttributeType.factory(attributeName, displayLabel, description, AttributeBooleanType.TYPE, required, unique, isChangeOverTime);
    }
    else if (attribute instanceof AttributeLocalTypeSnapshot)
    {
      return AttributeType.factory(attributeName, displayLabel, description, AttributeLocalType.TYPE, required, unique, isChangeOverTime);
    }
    else if (attribute instanceof AttributeCharacterTypeSnapshot)
    {
      return AttributeType.factory(attributeName, displayLabel, description, AttributeCharacterType.TYPE, required, unique, isChangeOverTime);
    }
    else if (attribute instanceof AttributeDateTypeSnapshot)
    {
      return AttributeType.factory(attributeName, displayLabel, description, AttributeDateType.TYPE, required, unique, isChangeOverTime);
    }
    else if (attribute instanceof AttributeDoubleTypeSnapshot)
    {
      AttributeDoubleTypeSnapshot mdAttributeDec = (AttributeDoubleTypeSnapshot) attribute;

      AttributeFloatType attributeType = (AttributeFloatType) AttributeType.factory(attributeName, displayLabel, description, AttributeFloatType.TYPE, required, unique, isChangeOverTime);
      attributeType.setPrecision(mdAttributeDec.getPrecision());
      attributeType.setScale(mdAttributeDec.getScale());

      return attributeType;
    }
    else if (attribute instanceof AttributeLongTypeSnapshot)
    {
      return AttributeType.factory(attributeName, displayLabel, description, AttributeIntegerType.TYPE, required, unique, isChangeOverTime);
    }
    else if (attribute instanceof AttributeClassificationTypeSnapshot)
    {
      AttributeClassificationTypeSnapshot attributeClassification = (AttributeClassificationTypeSnapshot) attribute;

      AttributeClassificationType attributeType = (AttributeClassificationType) AttributeType.factory(attributeName, displayLabel, description, AttributeClassificationType.TYPE, required, unique, isChangeOverTime);
      attributeType.setClassificationType(attributeClassification.getClassificationType());

      if (!StringUtils.isBlank(attributeClassification.getRootTerm()))
      {
        ClassificationTypeBusinessServiceIF typeService = ApplicationContextHolder.getBean(ClassificationTypeBusinessServiceIF.class);
        ClassificationBusinessServiceIF service = ApplicationContextHolder.getBean(ClassificationBusinessServiceIF.class);

        ClassificationType type = typeService.getByCode(attributeClassification.getCode());

        Classification classification = service.get(type, attributeClassification.getRootTerm());

        attributeType.setRootTerm(classification.toTerm());
      }

      return attributeType;
    }
    else if (attribute instanceof AttributeTermTypeSnapshot)
    {
      return (AttributeTermType) AttributeType.factory(attributeName, displayLabel, description, AttributeTermType.TYPE, required, unique, isChangeOverTime);
    }

    throw new UnsupportedOperationException("Unsupported attribute type [" + attribute.getClass().getSimpleName() + "]");
  }

}
