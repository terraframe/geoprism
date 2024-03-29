/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.data.importer;

import org.opengis.feature.simple.SimpleFeature;

public class SimpleFeatureRow implements FeatureRow
{
  private SimpleFeature feature;
  
  private Long rowNumber;

  public SimpleFeatureRow(SimpleFeature feature, Long rowNumber)
  {
    this.feature = feature;
    this.rowNumber = rowNumber;
  }

  public SimpleFeature getFeature()
  {
    return feature;
  }

  @Override
  public Object getValue(String attributeName)
  {
    return this.feature.getAttribute(attributeName);
  }
  
  @Override
  public Long getRowNumber()
  {
    return this.rowNumber;
  }
}
