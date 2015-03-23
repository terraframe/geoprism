package com.runwaysdk.geodashboard.gis.persist.condition;

@com.runwaysdk.business.ClassSignature(hash = 1358228764)
public abstract class LocationConditionDTOBase extends com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.gis.persist.condition.LocationCondition";
  private static final long serialVersionUID = 1358228764;
  
  protected LocationConditionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected LocationConditionDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String COMPARISONVALUE = "comparisonValue";
  public com.runwaysdk.system.gis.geo.GeoEntityDTO getComparisonValue()
  {
    if(getValue(COMPARISONVALUE) == null || getValue(COMPARISONVALUE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.gis.geo.GeoEntityDTO.get(getRequest(), getValue(COMPARISONVALUE));
    }
  }
  
  public String getComparisonValueId()
  {
    return getValue(COMPARISONVALUE);
  }
  
  public void setComparisonValue(com.runwaysdk.system.gis.geo.GeoEntityDTO value)
  {
    if(value == null)
    {
      setValue(COMPARISONVALUE, "");
    }
    else
    {
      setValue(COMPARISONVALUE, value.getId());
    }
  }
  
  public boolean isComparisonValueWritable()
  {
    return isWritable(COMPARISONVALUE);
  }
  
  public boolean isComparisonValueReadable()
  {
    return isReadable(COMPARISONVALUE);
  }
  
  public boolean isComparisonValueModified()
  {
    return isModified(COMPARISONVALUE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getComparisonValueMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(COMPARISONVALUE).getAttributeMdDTO();
  }
  
  public final java.lang.String getComparisonLabel()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.condition.LocationConditionDTO.CLASS, "getComparisonLabel", _declaredTypes);
    return (java.lang.String) getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final java.lang.String getComparisonLabel(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.condition.LocationConditionDTO.CLASS, "getComparisonLabel", _declaredTypes);
    return (java.lang.String) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.condition.LocationConditionDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.geodashboard.gis.persist.condition.LocationConditionDTO) dto;
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
  
  public static com.runwaysdk.geodashboard.gis.persist.condition.LocationConditionQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.geodashboard.gis.persist.condition.LocationConditionQueryDTO) clientRequest.getAllInstances(com.runwaysdk.geodashboard.gis.persist.condition.LocationConditionDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.condition.LocationConditionDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.condition.LocationConditionDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.geodashboard.gis.persist.condition.LocationConditionDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.condition.LocationConditionDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.condition.LocationConditionDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.geodashboard.gis.persist.condition.LocationConditionDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
