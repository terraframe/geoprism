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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.system.metadata.MdView;

public class TargetDefinition implements TargetDefinitionIF
{
  private String                         sourceType;

  private String                         targetType;

  private PersistenceStrategyIF          strategy;

  private Map<String, TargetFieldIF>     fieldMap;

  private HashMap<String, TargetFieldIF> labelMap;

  private boolean                        isNew;

  private boolean                        isApplied;

  public TargetDefinition()
  {
    this.fieldMap = new HashMap<String, TargetFieldIF>();
    this.labelMap = new HashMap<String, TargetFieldIF>();
    this.isNew = true;
    this.isApplied = false;
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

  public void setNew(boolean isNew)
  {
    this.isNew = isNew;
  }

  @Override
  public boolean isNew()
  {
    return this.isNew;
  }

  public boolean isApplied()
  {
    return isApplied;
  }

  public void setApplied(boolean isApplied)
  {
    this.isApplied = isApplied;
  }

  public void addField(TargetFieldIF field)
  {
    if (!this.fieldMap.containsKey(field.getName()))
    {
      this.fieldMap.put(field.getName(), field);
      this.labelMap.put(field.getLabel(), field);
    }
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
    if (this.isNew())
    {
      TargetBinding binding = new TargetBinding();
      binding.setSourceView(MdView.getMdView(this.sourceType));
      binding.setTargetBusiness(MdClass.getMdClass(this.targetType));
      binding.setStrategy((PersistenceStrategy) this.strategy);
      binding.apply();

      Collection<TargetFieldIF> fields = this.fieldMap.values();

      for (TargetFieldIF field : fields)
      {
        field.persist(binding);
      }

      this.setApplied(true);
    }
  }

  public PersistenceStrategyIF getStrategy()
  {
    return strategy;
  }

  public void setStrategy(PersistenceStrategyIF strategy)
  {
    this.strategy = strategy;
  }

}
