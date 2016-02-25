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
package com.runwayskd.geodashboard.etl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdView;

public class TargetDefinition implements TargetDefinitionIF
{
  private String                         sourceType;

  private String                         targetType;

  private Map<String, TargetFieldIF>     fieldMap;

  private HashMap<String, TargetFieldIF> labelMap;

  public TargetDefinition()
  {
    this.fieldMap = new HashMap<String, TargetFieldIF>();
    this.labelMap = new HashMap<String, TargetFieldIF>();
  }

  public String getSourceType()
  {
    return sourceType;
  }

  public void setSourceType(String sourceType)
  {
    this.sourceType = sourceType;
  }

  public String getTargetType()
  {
    return targetType;
  }

  public void setTargetType(String targetType)
  {
    this.targetType = targetType;
  }

  public void addField(TargetFieldIF field)
  {
    this.fieldMap.put(field.getName(), field);
    this.labelMap.put(field.getLabel(), field);
  }

  @Override
  public TargetFieldIF getFieldByName(String name)
  {
    return this.fieldMap.get(name);
  }

  @Override
  public TargetFieldIF getFieldByLabel(String label)
  {
    return this.labelMap.get(label);
  }

  @Override
  public List<TargetFieldIF> getFields()
  {
    return new LinkedList<TargetFieldIF>(this.fieldMap.values());
  }

  @Override
  public void persist()
  {
    TargetBinding binding = new TargetBinding();
    binding.setSourceView(MdView.getMdView(this.sourceType));
    binding.setTargetBusiness(MdBusiness.getMdBusiness(this.targetType));
    binding.apply();
    
    Collection<TargetFieldIF> fields = this.fieldMap.values();

    for (TargetFieldIF field : fields)
    {
      field.persist(binding);
    }
  }
}
