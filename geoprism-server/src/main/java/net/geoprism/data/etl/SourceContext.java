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
import java.util.Map;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.Transient;
import com.runwaysdk.business.View;

public class SourceContext implements SourceContextIF
{
  private Map<String, SourceDefinitionIF> sheets;

  public SourceContext()
  {
    this.sheets = new HashMap<String, SourceDefinitionIF>();
  }

  private SourceDefinitionIF getSheet(String sheetName)
  {
    return this.sheets.get(sheetName);
  }

  public void addSheetDefinition(SourceDefinitionIF sheet)
  {
    this.sheets.put(sheet.getName(), sheet);
  }

  @Override
  public Transient newView(String sheetName)
  {
    SourceDefinitionIF sheet = this.getSheet(sheetName);

    if (sheet != null)
    {
      String type = sheet.getType();

      View view = (View) BusinessFacade.newSessionComponent(type);

      return view;
    }

    return null;
  }

  @Override
  public SourceFieldIF getFieldByLabel(String sheetName, String label)
  {
    SourceDefinitionIF sheet = this.getSheet(sheetName);
    return sheet.getFieldByLabel(label);
  }

  @Override
  public SourceFieldIF getFieldByName(String sheetName, String columnName)
  {
    SourceDefinitionIF sheet = this.getSheet(sheetName);
    return sheet.getFieldByName(columnName);
  }

  @Override
  public String getType(String sheetName)
  {
    SourceDefinitionIF sheet = this.getSheet(sheetName);

    return sheet.getType();
  }

  @Override
  public String getOid(String sheetName)
  {
    SourceDefinitionIF sheet = this.getSheet(sheetName);
    
    return sheet.getOid();
  }
  
  public void persist()
  {
    Collection<SourceDefinitionIF> definitions = this.sheets.values();

    for (SourceDefinitionIF definition : definitions)
    {
      definition.persist();
    }
  }

  @Override
  public Collection<SourceDefinitionIF> getDefinitions()
  {
    return this.sheets.values();
  }
}
