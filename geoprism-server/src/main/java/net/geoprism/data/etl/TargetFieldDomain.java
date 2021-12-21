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

import java.util.Map;
import java.util.Set;

import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.util.Cache;

import net.geoprism.GeoprismProperties;
import net.geoprism.data.importer.ExclusionException;
import net.geoprism.ontology.Classifier;

public class TargetFieldDomain extends TargetFieldBasic implements TargetFieldIF, TargetFieldValidationIF
{
  private Cache<String, Classifier> cache;

  public TargetFieldDomain()
  {
    this.cache = new Cache<String, Classifier>(GeoprismProperties.getClassifierCacheSize());
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

        Classifier classifier = Classifier.findMatchingTerm(value.trim(), mdAttributeTerm);

        if (classifier == null)
        {
          throw new ExclusionException("Unable to find matching term with label [" + value + "]");
        }
        else
        {
          this.cache.put(value, classifier);
        }
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

    TargetFieldDomainBinding field = new TargetFieldDomainBinding();
    field.setTarget(binding);
    field.setTargetAttribute(targetAttribute);
    field.setSourceAttribute(sourceAttribute);
    field.setColumnLabel(this.getLabel());
    field.apply();
  }

  @SuppressWarnings("unchecked")
  @Override
  public ImportProblemIF validate(Transient source, Map<String, Object> parameters)
  {
    String value = source.getValue(this.getSourceAttributeName());

    if (value != null && value.length() > 0)
    {
      Map<String, Set<String>> exclusions = (Map<String, Set<String>>) parameters.get("categoryExclusions");

      MdAttributeTermDAOIF mdAttributeTerm = (MdAttributeTermDAOIF) MdAttributeDAO.getByKey(this.getKey());

      if (!this.isExcluded(exclusions, mdAttributeTerm, value))
      {
        Classifier classifier = Classifier.findMatchingTerm(value.trim(), mdAttributeTerm);

        if (classifier == null)
        {
          Classifier root = Classifier.findClassifierRoot(mdAttributeTerm);
          String attributeLabel = mdAttributeTerm.getDisplayLabel(Session.getCurrentLocale());

          return new CategoryProblem(value.trim(), root.getOid(), mdAttributeTerm.getOid(), attributeLabel);
        }
      }
    }

    return null;
  }

  private boolean isExcluded(Map<String, Set<String>> exclusions, MdAttributeTermDAOIF mdAttributeTerm, String label)
  {
    if (exclusions.containsKey(mdAttributeTerm.getOid()))
    {
      Set<String> labels = exclusions.get(mdAttributeTerm.getOid());

      return labels.contains(label);
    }

    return false;
  }
}
