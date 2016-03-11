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

import java.util.List;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;

public class Converter implements ConverterIF
{
  private TargetContextIF context;

  public Converter(TargetContextIF context)
  {
    this.context = context;
  }

  @Override
  public void create(Transient source)
  {
    Business business = this.context.newBusiness(source.getType());

    List<TargetFieldIF> fields = this.context.getFields(source.getType());

    for (TargetFieldIF field : fields)
    {
      String attributeName = field.getName();

      MdAttributeConcreteDAOIF mdAttribute = business.getMdAttributeDAO(attributeName);

      Object value = field.getValue(mdAttribute, source);

      if (value != null)
      {
        business.setValue(attributeName, value);
      }
    }

    business.apply();
  }
}
