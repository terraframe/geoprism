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

import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.data.importer.ExclusionException;

public class Converter implements ConverterIF
{
  private TargetContextIF context;

  private Workbook        errors;

  private ProgressMonitorIF monitor;

  public Converter(TargetContextIF context, ProgressMonitorIF monitor)
  {
    this.context = context;
    this.monitor = monitor;
  }

  @Override
  public TargetContextIF getTargetContext()
  {
    return this.context;
  }

  @Override
  @Transaction
  public void create(Transient source)
  {
    try
    {
      Mutable business = this.context.newMutable(source.getType());
      boolean hasValues = false;

      List<TargetFieldIF> fields = this.context.getFields(source.getType());

      for (TargetFieldIF field : fields)
      {
        String attributeName = field.getName();

        MdAttributeConcreteDAOIF mdAttribute = (MdAttributeConcreteDAOIF) business.getMdAttributeDAO(attributeName);

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
        TargetDefinitionIF definition = this.context.getDefinition(source.getType());
        PersistenceStrategyIF strategy = definition.getStrategy();

        strategy.handle(business);
        
        this.monitor.entityImported(definition);
      }
    }
    catch (ExclusionException e)
    {
      // Do nothing. It's likely that a source value was not found because of location exclusions
    }
  }

  @Override
  public void setErrors(Workbook errors)
  {
    this.errors = errors;
  }

  @Override
  public Workbook getErrors()
  {
    return this.errors;
  }
}
