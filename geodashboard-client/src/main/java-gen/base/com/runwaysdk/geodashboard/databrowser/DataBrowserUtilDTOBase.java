package com.runwaysdk.geodashboard.databrowser;

@com.runwaysdk.business.ClassSignature(hash = -1686140528)
public abstract class DataBrowserUtilDTOBase extends com.runwaysdk.business.UtilDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.databrowser.DataBrowserUtil";
  private static final long serialVersionUID = -1686140528;
  
  protected DataBrowserUtilDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static final com.runwaysdk.geodashboard.databrowser.MetadataTypeQueryDTO getTypes(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String queryPackage)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{queryPackage};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.databrowser.DataBrowserUtilDTO.CLASS, "getTypes", _declaredTypes);
    return (com.runwaysdk.geodashboard.databrowser.MetadataTypeQueryDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static DataBrowserUtilDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.UtilDTO dto = (com.runwaysdk.business.UtilDTO)clientRequest.get(id);
    
    return (DataBrowserUtilDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createSessionComponent(this);
    }
    else
    {
      getRequest().update(this);
    }
  }
  public void delete()
  {
    getRequest().delete(this.getId());
  }
  
}
