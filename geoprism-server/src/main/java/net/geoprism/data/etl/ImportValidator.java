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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;

public class ImportValidator implements ConverterIF
{
  public static class DecimalAttribute
  {
    private int precision;

    private int scale;

    public DecimalAttribute()
    {
      this.precision = 0;
      this.scale = 0;
    }

    public void setPrecision(Integer precision)
    {
      this.precision = Math.max(this.precision, precision);
    }

    public void setScale(Integer scale)
    {
      this.scale = Math.max(this.scale, scale);
    }

    public int getPrecision()
    {
      return precision;
    }

    public int getScale()
    {
      return scale;
    }
  }

  private TargetContextIF               context;

  private Set<ImportProblemIF>          problems;

  private Map<String, DecimalAttribute> attributes;

  public ImportValidator(TargetContextIF context)
  {
    this.context = context;
    this.problems = new TreeSet<ImportProblemIF>();
    this.attributes = new HashMap<String, DecimalAttribute>();

  }
  
  @Override
  public TargetContextIF getTargetContext()
  {
    return this.context;
  }

  @Override
  public void create(Transient source)
  {
    List<TargetFieldIF> fields = this.context.getFields(source.getType());

    for (TargetFieldIF field : fields)
    {
      if (field instanceof TargetFieldValidationIF)
      {
        TargetFieldValidationIF entity = (TargetFieldValidationIF) field;

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("locationExclusions", this.context.getLocationExclusions());
        parameters.put("categoryExclusions", this.context.getCategoryExclusions());

        ImportProblemIF problem = entity.validate(source, parameters);

        if (problem != null)
        {
          this.problems.add(problem);
        }
      }
      else if (field instanceof TargetFieldBasic)
      {
        MdAttributeConcreteDAOIF mdAttribute = MdAttributeConcreteDAO.getByKey(field.getKey());

        if (mdAttribute instanceof MdAttributeDecDAOIF)
        {
          FieldValue fValue = field.getValue(mdAttribute, source);
          String value = (String) fValue.getValue();

          if (value != null && value.length() > 0)
          {
            try
            {
              BigDecimal decimal = new BigDecimal(value).stripTrailingZeros();

              this.attributes.putIfAbsent(mdAttribute.getId(), new DecimalAttribute());

              /*
               * Precision is the total number of digits. Scale is the number of digits after the decimal place.
               */
              DecimalAttribute attribute = this.attributes.get(mdAttribute.getId());

              int precision = decimal.precision();
              int scale = decimal.scale();

              attribute.setPrecision(precision - scale);
              attribute.setScale(scale);
            }
            catch (Exception e)
            {
              // Skip this error
            }
          }
        }
      }
    }
  }

  public Map<String, DecimalAttribute> getAttributes()
  {
    return attributes;
  }

  public Collection<ImportProblemIF> getProblems()
  {
    return problems;
  }
}
