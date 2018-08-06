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

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
  
  private Set<ImportProblemIF> problems;

  public Converter(TargetContextIF context, ProgressMonitorIF monitor)
  {
    this.context = context;
    this.monitor = monitor;
    this.problems = new TreeSet<ImportProblemIF>();
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
      List<TargetFieldIF> fields = this.context.getFields(source.getType());

      Mutable business = this.context.newMutable(source.getType());
      boolean hasValues = false;
      boolean valid = true;

      for (TargetFieldIF field : fields)
      {
        String attributeName = field.getName();

        MdAttributeConcreteDAOIF mdAttribute = (MdAttributeConcreteDAOIF) business.getMdAttributeDAO(attributeName);

        // get value can intentionally fail if attempting to get the value of a
        // location that is on the
        // location exclusion list. Note the custom effor after this TRY
        // statement.
        FieldValue fValue = field.getValue(mdAttribute, source);

        if (! ( fValue instanceof ImportProblemIF ))
        {
          Object value = fValue.getValue();

          if (value != null)
          {
            business.setValue(attributeName, value);
          }

          hasValues = hasValues || !fValue.isBlank();
        }
        else
        {
          this.problems.add((ImportProblemIF) fValue);

          valid = false;
        }
      }

      /*
       * Before apply ensure that at least one source field was not blank
       */
      if (hasValues)
      {
        TargetDefinitionIF definition = this.context.getDefinition(source.getType());
        PersistenceStrategyIF strategy = definition.getStrategy();

        strategy.handle(business);

        if (valid)
        {
          this.monitor.entityImported(definition);
        }
      }
    }
    catch (ExclusionException e)
    {
      this.problems.add(e.getProblem());
    }
  }
  
  public Collection<ImportProblemIF> getProblems()
  {
    return problems;
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
