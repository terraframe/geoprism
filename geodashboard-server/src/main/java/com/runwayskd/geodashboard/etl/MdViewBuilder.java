package com.runwayskd.geodashboard.etl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdViewQuery;

public class MdViewBuilder
{
  private JSONObject    configuration;

  private SourceContext source;

  public MdViewBuilder(JSONObject configuration, SourceContext source)
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

    String type = this.generateViewType(label);

    JSONArray cFields = cSheet.getJSONArray("fields");

    SourceDefinition definition = new SourceDefinition();
    definition.setType(type);
    definition.setSheetName(sheetName);

    for (int i = 0; i < cFields.length(); i++)
    {
      JSONObject cField = cFields.getJSONObject(i);

      SourceFieldIF field = this.createMdAttribute(null, cField);

      definition.addField(field);
    }

    return definition;
  }

  private SourceFieldIF createMdAttribute(MdClassDAO mdClass, JSONObject cField) throws JSONException
  {
    String columnName = cField.getString("name");
    String label = cField.getString("label");
    String attributeName = this.generateAttributeName(label);

    SourceField field = new SourceField();
    field.setColumnName(columnName);
    field.setAttributeName(attributeName);
    field.setLabel(label);

    return field;
  }

  private String generateAttributeName(String label)
  {
    // TODO Come up with naming schema
    String name = label.replaceAll("\\s", "");
    name = name.substring(0, 1).toLowerCase() + name.substring(1);

    return name;
  }

  private String generateViewType(String label)
  {
    // Create a unique name for the view type based upon the name of the sheet
    String packageName = "com.runwaysdk.geodashboard.data.view";

    // TODO Come up with naming schema
    String typeName = label.replaceAll("\\s", "");
    typeName = typeName.replaceAll("\\(", "");
    typeName = typeName.replaceAll("\\)", "");
    typeName = typeName.substring(0, 1).toUpperCase() + typeName.substring(1);
    typeName = typeName + "View";

    Integer suffix = 0;

    while (!this.isUnique(packageName, typeName, suffix))
    {
      suffix += 1;
    }

    if (suffix > 0)
    {
      return packageName + "." + typeName + suffix;
    }

    return packageName + "." + typeName;
  }

  private boolean isUnique(String packageName, String typeName, Integer suffix)
  {
    MdViewQuery query = new MdViewQuery(new QueryFactory());
    query.WHERE(query.getTypeName().EQ(typeName + suffix));
    query.AND(query.getPackageName().EQ(packageName));

    return ( query.getCount() == 0 );
  }

}
