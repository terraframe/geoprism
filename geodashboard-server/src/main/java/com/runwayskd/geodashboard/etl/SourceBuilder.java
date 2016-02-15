package com.runwayskd.geodashboard.etl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.geodashboard.DataUploader;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdViewQuery;

public class SourceBuilder
{
  public static final String PACKAGE_NAME = "com.runwaysdk.geodashboard.data.view";

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
    mdView.apply();

    JSONArray cFields = cSheet.getJSONArray("fields");

    SourceDefinition definition = new SourceDefinition();
    definition.setType(typeName);
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

    MdAttributeTextDAO mdAttribute = MdAttributeTextDAO.newInstance();
    mdAttribute.setValue(MdAttributeTextInfo.NAME, attributeName);
    mdAttribute.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, mdClass.getId());
    mdAttribute.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
    mdAttribute.apply();

    SourceField field = new SourceField();
    field.setColumnName(columnName);
    field.setAttributeName(attributeName);
    field.setLabel(label);

    return field;
  }

  private String generateAttributeName(String label)
  {
    return this.generateAttributeName(label, "");
  }

  private String generateAttributeName(String label, String suffix)
  {
    String name = DataUploader.getSystemName(label, suffix, false);

    return name;
  }

  private String generateViewType(String label)
  {
    // Create a unique name for the view type based upon the name of the sheet
    String typeName = DataUploader.getSystemName(label, "View", true);

    Integer suffix = 0;

    while (!this.isUnique(PACKAGE_NAME, typeName, suffix))
    {
      suffix += 1;
    }

    if (suffix > 0)
    {
      return PACKAGE_NAME + "." + typeName + suffix;
    }

    return PACKAGE_NAME + "." + typeName;
  }

  private boolean isUnique(String packageName, String typeName, Integer suffix)
  {
    MdViewQuery query = new MdViewQuery(new QueryFactory());
    query.WHERE(query.getTypeName().EQ(typeName + suffix));
    query.AND(query.getPackageName().EQ(packageName));

    return ( query.getCount() == 0 );
  }

}
