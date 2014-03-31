package com.runwaysdk.geodashboard.databrowser;

import com.runwaysdk.geodashboard.GeodashboardUser;
import com.runwaysdk.system.Users;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdBusiness;

public class DataBrowserUtil extends DataBrowserUtilBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1821253975;
  
  public static final String ROOT_ID = "ROOT";
  
  public DataBrowserUtil()
  {
    super();
  }
  
  public static com.runwaysdk.geodashboard.databrowser.MetadataType[] getTypes()
  {
    MetadataType[] types = new MetadataType[]{
        new MetadataType(ROOT_ID, MdBusiness.getMdBusiness(Users.CLASS)),
        new MetadataType(MdBusiness.getMdBusiness(Users.CLASS).getId(), MdBusiness.getMdBusiness(GeodashboardUser.CLASS)),
        new MetadataType(ROOT_ID, MdBusiness.getMdBusiness(GeoEntity.CLASS)),
        new MetadataType(ROOT_ID, MdBusiness.getMdBusiness(Universal.CLASS))
    };
    
    return types;
  }
  
}
