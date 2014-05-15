package com.runwaysdk.geodashboard.gis.persist;

@com.runwaysdk.business.ClassSignature(hash = 1582870733)
public abstract class AggregationTypeDTOBase extends com.runwaysdk.system.EnumerationMasterDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.gis.persist.AggregationType";
  private static final long serialVersionUID = 1582870733;
  
  protected AggregationTypeDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected AggregationTypeDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.AggregationTypeDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.geodashboard.gis.persist.AggregationTypeDTO) dto;
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
  
  public static com.runwaysdk.geodashboard.gis.persist.AggregationTypeQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.geodashboard.gis.persist.AggregationTypeQueryDTO) clientRequest.getAllInstances(com.runwaysdk.geodashboard.gis.persist.AggregationTypeDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.AggregationTypeDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.AggregationTypeDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.geodashboard.gis.persist.AggregationTypeDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.AggregationTypeDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.AggregationTypeDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.geodashboard.gis.persist.AggregationTypeDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
