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
package net.geoprism.data.importer;

import org.apache.poi.ss.usermodel.Cell;

import com.runwaysdk.dataaccess.io.excel.ExcelColumn;

import net.geoprism.ontology.Classifier;

public class ClassifierColumn extends ExcelColumn
{
  public ClassifierColumn(String attributeName, String displayLabel)
  {
    super(attributeName, displayLabel);
  }

  @Override
  public void setValue(Cell cell, String value)
  {
    if (value != null && value.length() > 0)
    {
      Classifier classifier = Classifier.get(value);

      super.setValue(cell, classifier.getDisplayLabel().getValue());
    }
    else
    {
      super.setValue(cell, value);
    }
  }
}
