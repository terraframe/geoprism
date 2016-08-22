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

import java.util.HashMap;
import java.util.List;

import net.geoprism.data.importer.LocationExclusionException;

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
  public void create(Transient source, List<HashMap<String, String>> locationExclusions)
  {
    try
    {
      Business business = this.context.newBusiness(source.getType());
      boolean hasValues = false;
  
      List<TargetFieldIF> fields = this.context.getFields(source.getType());
  
      for (TargetFieldIF field : fields)
      {
        String attributeName = field.getName();
  
        MdAttributeConcreteDAOIF mdAttribute = business.getMdAttributeDAO(attributeName);
  
        // get value can intentionally fail if attempting to get the value of a location that is on the 
        // location exclusion list. Note the custom effor after this TRY statement.
        FieldValue fValue = field.getValue(mdAttribute, source);
        Object value = fValue.getValue();
  
        if (value != null)
        {
          business.setValue(attributeName, value);
        }
  
        hasValues = hasValues || !fValue.isBlank();
      }
  
      /*
       * Before apply ensure that at least one source field was not blank
       */
      if (hasValues)
      {
        business.apply();
      }
    }
    catch(LocationExclusionException e)
    {
      // Do nothing. It's likely that a source value was not found because of location exclusions
    }
  }
}
