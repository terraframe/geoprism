/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.data.etl;

import net.geoprism.ontology.Classifier;

import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.system.metadata.MdAttribute;

public class TargetFieldDomain extends TargetFieldBasic implements TargetFieldIF
{
  @Override
  public FieldValue getValue(MdAttributeConcreteDAOIF mdAttribute, Transient source)
  {
    String value = source.getValue(this.getSourceAttributeName());

    if (value != null && value.length() > 0)
    {
      MdAttributeTermDAOIF mdAttributeTerm = (MdAttributeTermDAOIF) mdAttribute;

      Classifier classifier = Classifier.findMatchingTerm(value.trim(), mdAttributeTerm);

      if (classifier == null)
      {
        throw new ProgrammingErrorException("Unable to find matching term with label [" + value + "]");
      }

      return new FieldValue(classifier.getId());
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

}
