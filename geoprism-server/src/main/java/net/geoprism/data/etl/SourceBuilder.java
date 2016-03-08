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

import net.geoprism.DataUploader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdViewQuery;

public class SourceBuilder
{
  public static final String PACKAGE_NAME = "net.geoprism.data.view";

  private JSONObject         configuration;

  private SourceContext      source;

  public SourceBuilder(JSONObject configuration, SourceContext source)
  {
    this.configuration = configuration;
    this.source = source;
  }

  public void build()
  {
    try
    {
      JSONArray cSheets = configuration.getJSONArray("sheets");

      for (int i = 0; i < cSheets.length(); i++)
      {
        JSONObject sheet = cSheets.getJSONObject(i);

        SourceDefinitionIF definition = this.createMdView(sheet);

        this.source.addSheetDefinition(definition);
      }

      String directory = configuration.getString("directory");
      String filename = configuration.getString("filename");

      this.source.setDirectory(directory);
      this.source.setFilename(filename);
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private SourceDefinitionIF createMdView(JSONObject cSheet) throws JSONException
  {
    String sheetName = cSheet.getString("name");
    String label = cSheet.getString("label");

    String typeName = this.generateViewType(label);

    /*
     * Define the new MdBussiness and Mappable class
     */
    MdViewDAO mdView = MdViewDAO.newInstance();
    mdView.setValue(MdViewInfo.PACKAGE, PACKAGE_NAME);
    mdView.setValue(MdViewInfo.NAME, typeName);
    mdView.setStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
    mdView.setValue(MdViewInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdView.apply();

    JSONArray cFields = cSheet.getJSONArray("fields");

    SourceDefinition definition = new SourceDefinition();
    definition.setType(PACKAGE_NAME + "." + typeName);
    definition.setSheetName(sheetName);

    for (int i = 0; i < cFields.length(); i++)
    {
      JSONObject cField = cFields.getJSONObject(i);

      SourceFieldIF field = this.createMdAttribute(mdView, cField);

      definition.addField(field);
    }

    return definition;
  }

  private SourceFieldIF createMdAttribute(MdClassDAO mdClass, JSONObject cField) throws JSONException
  {
    String columnName = cField.getString("name");
    String label = cField.getString("label");
    String attributeName = this.generateAttributeName(label);
    String type = cField.getString("type");

    MdAttributeTextDAO mdAttribute = MdAttributeTextDAO.newInstance();
    mdAttribute.setValue(MdAttributeTextInfo.NAME, attributeName);
    mdAttribute.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, mdClass.getId());
    mdAttribute.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
    mdAttribute.apply();

    SourceField field = new SourceField();
    field.setColumnName(columnName);
    field.setAttributeName(attributeName);
    field.setLabel(label);
    field.setType(ColumnType.valueOf(type));

    return field;
  }

  private String generateAttributeName(String label)
  {
    return this.generateAttributeName(label, "Attribute");
  }

  private String generateAttributeName(String label, String suffix)
  {
    String name = DataUploader.getSystemName(label, suffix, false);

    return name;
  }

  private String generateViewType(String label)
  {
    // Create a unique name for the view type based upon the name of the sheet
    Integer suffix = 0;

    String typeName = DataUploader.getSystemName(label, "Type", true) + "View";

    while (!this.isUnique(PACKAGE_NAME, typeName, suffix))
    {
      suffix += 1;
    }

    if (suffix > 0)
    {
      return typeName + suffix;
    }

    return typeName;
  }

  private boolean isUnique(String packageName, String typeName, Integer suffix)
  {
    if (suffix > 0)
    {
      typeName = typeName + suffix;
    }

    MdViewQuery query = new MdViewQuery(new QueryFactory());
    query.WHERE(query.getTypeName().EQ(typeName));
    query.AND(query.getPackageName().EQ(packageName));

    long count = query.getCount();

    return ( count == 0 );
  }

}
