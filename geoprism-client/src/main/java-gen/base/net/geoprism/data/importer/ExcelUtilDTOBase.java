package net.geoprism.data.importer;

@com.runwaysdk.business.ClassSignature(hash = -845000382)
public abstract class ExcelUtilDTOBase extends com.runwaysdk.business.UtilDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "net.geoprism.data.importer.ExcelUtil";
  private static final long serialVersionUID = -845000382;
  
  protected ExcelUtilDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static final void excelImportFromVault(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String vaultId, java.lang.String config)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.lang.String"};
    Object[] _parameters = new Object[]{vaultId, config};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.data.importer.ExcelUtilDTO.CLASS, "excelImportFromVault", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final java.io.InputStream exportExcelFile(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String type, java.lang.String country)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.lang.String"};
    Object[] _parameters = new Object[]{type, country};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.data.importer.ExcelUtilDTO.CLASS, "exportExcelFile", _declaredTypes);
    return (java.io.InputStream) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final java.io.InputStream importExcelFile(com.runwaysdk.constants.ClientRequestIF clientRequest, java.io.InputStream istream, java.lang.String defaultEntity)
  {
    String[] _declaredTypes = new String[]{"java.io.InputStream", "java.lang.String"};
    Object[] _parameters = new Object[]{istream, defaultEntity};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.data.importer.ExcelUtilDTO.CLASS, "importExcelFile", _declaredTypes);
    return (java.io.InputStream) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static ExcelUtilDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.UtilDTO dto = (com.runwaysdk.business.UtilDTO)clientRequest.get(id);
    
    return (ExcelUtilDTO) dto;
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
