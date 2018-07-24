package net.geoprism.data.etl.excel;

@com.runwaysdk.business.ClassSignature(hash = -845149221)
public abstract class DataUploaderImportJobDTOBase extends com.runwaysdk.system.scheduler.ExecutableJobDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "net.geoprism.data.etl.excel.DataUploaderImportJob";
  private static final long serialVersionUID = -845149221;
  
  protected DataUploaderImportJobDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected DataUploaderImportJobDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static net.geoprism.data.etl.excel.DataUploaderImportJobDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (net.geoprism.data.etl.excel.DataUploaderImportJobDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createBusiness(this);
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
  
  public static net.geoprism.data.etl.excel.DataUploaderImportJobQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (net.geoprism.data.etl.excel.DataUploaderImportJobQueryDTO) clientRequest.getAllInstances(net.geoprism.data.etl.excel.DataUploaderImportJobDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static net.geoprism.data.etl.excel.DataUploaderImportJobDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.data.etl.excel.DataUploaderImportJobDTO.CLASS, "lock", _declaredTypes);
    return (net.geoprism.data.etl.excel.DataUploaderImportJobDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static net.geoprism.data.etl.excel.DataUploaderImportJobDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.data.etl.excel.DataUploaderImportJobDTO.CLASS, "unlock", _declaredTypes);
    return (net.geoprism.data.etl.excel.DataUploaderImportJobDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
