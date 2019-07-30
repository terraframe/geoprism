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

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.business.Mutable;

public interface TargetContextIF
{
  public Mutable newMutable(String sourceType);

  public String getType(String sourceType);

  public List<TargetFieldIF> getFields(String sourceType);

  public List<TargetDefinitionIF> getDefinitions();

  public TargetDefinitionIF getDefinition(String sourceType);

  public Map<String, Set<String>> getLocationExclusions();

  public Map<String, Set<String>> getCategoryExclusions();
}
