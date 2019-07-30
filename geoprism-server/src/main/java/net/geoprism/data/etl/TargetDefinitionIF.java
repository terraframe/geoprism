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

public interface TargetDefinitionIF
{
  public String getTargetType();

  public String getSourceType();

  public TargetFieldIF getFieldByName(String name);

  public TargetFieldIF getFieldByLabel(String label);

  public List<TargetFieldIF> getFields();

  public void persist();
  
  public boolean isNew();
  
  public boolean isApplied();
  
  public PersistenceStrategyIF getStrategy();
}
