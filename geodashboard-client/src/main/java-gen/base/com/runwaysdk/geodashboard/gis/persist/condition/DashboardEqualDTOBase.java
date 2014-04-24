package com.runwaysdk.geodashboard.gis.persist.condition;

@com.runwaysdk.business.ClassSignature(hash = -1932211411)
public abstract class DashboardEqualDTOBase extends com.runwaysdk.geodashboard.gis.persist.condition.DashboardPrimitiveDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqual";
  private static final long serialVersionUID = -1932211411;
  
  protected DashboardEqualDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected DashboardEqualDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ATTRIBUTEVALUE = "attributeValue";
  public String getAttributeValue()
  {
    return getValue(ATTRIBUTEVALUE);
  }
  
  public void setAttributeValue(String value)
  {
    if(value == null)
    {
      setValue(ATTRIBUTEVALUE, "");
    }
    else
    {
      setValue(ATTRIBUTEVALUE, value);
    }
  }
  
  public boolean isAttributeValueWritable()
  {
    return isWritable(ATTRIBUTEVALUE);
  }
  
  public boolean isAttributeValueReadable()
  {
    return isReadable(ATTRIBUTEVALUE);
  }
  
  public boolean isAttributeValueModified()
  {
    return isModified(ATTRIBUTEVALUE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getAttributeValueMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(ATTRIBUTEVALUE).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqualDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqualDTO) dto;
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
  
  public static com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqualQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqualQueryDTO) clientRequest.getAllInstances(com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqualDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqualDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqualDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqualDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqualDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqualDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqualDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
