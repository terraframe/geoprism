package com.runwaysdk.geodashboard.databrowser;

import com.runwaysdk.query.QueryFactory;

public class DataBrowserUtil extends DataBrowserUtilBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 884335891;
  
  public DataBrowserUtil()
  {
    super();
  }
  
  public static com.runwaysdk.geodashboard.databrowser.MetadataTypeQuery getTypes(String[] packages, String[] types)
  {
    QueryFactory f = new QueryFactory();
    
    MetadataTypeQuery query = new MetadataTypeQuery(f, packages, types);
    
    return query;
  }
}
