/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.data.etl;

import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.util.Cache;

import net.geoprism.GeoprismProperties;
import net.geoprism.ontology.Classifier;

public class TargetFieldClassifier extends TargetFieldBasic implements TargetFieldIF
{
  private String                    packageName;

  private Cache<String, Classifier> cache;

  public TargetFieldClassifier()
  {
    this.cache = new Cache<String, Classifier>(GeoprismProperties.getClassifierCacheSize());
  }

  public void setPackageName(String packageName)
  {
    this.packageName = packageName;
  }

  public String getPackageName()
  {
    return packageName;
  }

  @Override
  public FieldValue getValue(MdAttributeConcreteDAOIF mdAttribute, Transient source)
  {
    String value = source.getValue(this.getSourceAttributeName());

    if (value != null && value.length() > 0)
    {
      if (!this.cache.containsKey(value))
      {
        MdAttributeTermDAOIF mdAttributeTerm = (MdAttributeTermDAOIF) mdAttribute;

        Classifier classifier = Classifier.findClassifierAddIfNotExist(this.packageName, value.trim(), mdAttributeTerm, true);

        this.cache.put(value, classifier);
      }

      return new FieldValue(this.cache.get(value).getOid());
    }

    return new FieldValue();
  }

  @Override
  public void persist(TargetBinding binding)
  {
    MdAttribute sourceAttribute = MdAttribute.getByKey(binding.getSourceView().definesType() + "." + this.getSourceAttributeName());
    MdAttribute targetAttribute = MdAttribute.getByKey(this.getKey());

    TargetFieldClassifierBinding field = new TargetFieldClassifierBinding();
    field.setTarget(binding);
    field.setTargetAttribute(targetAttribute);
    field.setSourceAttribute(sourceAttribute);
    field.setColumnLabel(this.getLabel());
    field.setPackageName(this.getPackageName());
    field.apply();
  }
}
