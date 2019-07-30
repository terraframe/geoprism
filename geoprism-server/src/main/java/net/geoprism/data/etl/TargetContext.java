/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.Mutable;

public class TargetContext implements TargetContextIF
{
  private Map<String, TargetDefinitionIF> definitions;

  private Map<String, Set<String>>        locationExclusions;

  private Map<String, Set<String>>        categoryExclusions;

  public TargetContext()
  {
    this.definitions = new HashMap<String, TargetDefinitionIF>();
  }

  public void addDefinition(TargetDefinitionIF definition)
  {
    this.definitions.put(definition.getSourceType(), definition);
  }

  public TargetDefinitionIF getDefinition(String sourceType)
  {
    return this.definitions.get(sourceType);
  }

  public Map<String, Set<String>> getLocationExclusions()
  {
    return locationExclusions;
  }

  public void setLocationExclusions(Map<String, Set<String>> locationExclusions)
  {
    this.locationExclusions = locationExclusions;
  }

  @Override
  public Map<String, Set<String>> getCategoryExclusions()
  {
    return this.categoryExclusions;
  }

  public void setCategoryExclusions(Map<String, Set<String>> categoryExclusions)
  {
    this.categoryExclusions = categoryExclusions;
  }

  @Override
  public String getType(String sourceType)
  {
    TargetDefinitionIF definition = this.getDefinition(sourceType);

    return definition.getTargetType();
  }

  @Override
  public Mutable newMutable(String sourceType)
  {
    TargetDefinitionIF definition = this.getDefinition(sourceType);

    if (definition != null)
    {
      String type = definition.getTargetType();

      Mutable business = BusinessFacade.newMutable(type);

      return business;
    }

    return null;
  }

  @Override
  public List<TargetFieldIF> getFields(String sourceType)
  {
    TargetDefinitionIF definition = this.getDefinition(sourceType);

    return definition.getFields();
  }

  @Override
  public List<TargetDefinitionIF> getDefinitions()
  {
    return new LinkedList<TargetDefinitionIF>(this.definitions.values());
  }

  public void persist()
  {
    List<TargetDefinitionIF> definitions = this.getDefinitions();

    for (TargetDefinitionIF definition : definitions)
    {
      definition.persist();
    }
  }

}
