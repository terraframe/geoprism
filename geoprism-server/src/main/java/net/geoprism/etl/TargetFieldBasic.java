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
package net.geoprism.etl;

import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwayskd.geodashboard.etl.TargetBinding;
import com.runwayskd.geodashboard.etl.TargetFieldBasicBinding;

public class TargetFieldBasic extends TargetField implements TargetFieldIF
{
  private String sourceAttributeName;

  public String getSourceAttributeName()
  {
    return sourceAttributeName;
  }

  public void setSourceAttributeName(String sourceAttributeName)
  {
    this.sourceAttributeName = sourceAttributeName;
  }

  @Override
  public Object getValue(MdAttributeConcreteDAOIF mdAttribute, Transient source)
  {
    String value = source.getValue(this.getSourceAttributeName());

    return value;
  }

  @Override
  public void persist(TargetBinding binding)
  {
    MdAttribute sourceAttribute = MdAttribute.getByKey(binding.getSourceView().definesType() + "." + this.getSourceAttributeName());
    MdAttribute targetAttribute = MdAttribute.getByKey(this.getKey());

    TargetFieldBasicBinding field = new TargetFieldBasicBinding();
    field.setTarget(binding);
    field.setTargetAttribute(targetAttribute);
    field.setSourceAttribute(sourceAttribute);
    field.setColumnLabel(this.getLabel());
    field.apply();
  }
}
