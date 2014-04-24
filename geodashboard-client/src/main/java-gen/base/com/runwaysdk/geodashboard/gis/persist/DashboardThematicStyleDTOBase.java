package com.runwaysdk.geodashboard.gis.persist;

@com.runwaysdk.business.ClassSignature(hash = -706289423)
public abstract class DashboardThematicStyleDTOBase extends com.runwaysdk.geodashboard.gis.persist.DashboardStyleDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle";
  private static final long serialVersionUID = -706289423;
  
  protected DashboardThematicStyleDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected DashboardThematicStyleDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String MDATTRIBUTE = "mdAttribute";
  public static java.lang.String STYLECONDITION = "styleCondition";
  public com.runwaysdk.system.metadata.MdAttributeDTO getMdAttribute()
  {
    if(getValue(MDATTRIBUTE) == null || getValue(MDATTRIBUTE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdAttributeDTO.get(getRequest(), getValue(MDATTRIBUTE));
    }
  }
  
  public String getMdAttributeId()
  {
    return getValue(MDATTRIBUTE);
  }
  
  public void setMdAttribute(com.runwaysdk.system.metadata.MdAttributeDTO value)
  {
    if(value == null)
    {
      setValue(MDATTRIBUTE, "");
    }
    else
    {
      setValue(MDATTRIBUTE, value.getId());
    }
  }
  
  public boolean isMdAttributeWritable()
  {
    return isWritable(MDATTRIBUTE);
  }
  
  public boolean isMdAttributeReadable()
  {
    return isReadable(MDATTRIBUTE);
  }
  
  public boolean isMdAttributeModified()
  {
    return isModified(MDATTRIBUTE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getMdAttributeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(MDATTRIBUTE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionDTO getStyleCondition()
  {
    if(getValue(STYLECONDITION) == null || getValue(STYLECONDITION).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionDTO.get(getRequest(), getValue(STYLECONDITION));
    }
  }
  
  public String getStyleConditionId()
  {
    return getValue(STYLECONDITION);
  }
  
  public void setStyleCondition(com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionDTO value)
  {
    if(value == null)
    {
      setValue(STYLECONDITION, "");
    }
    else
    {
      setValue(STYLECONDITION, value.getId());
    }
  }
  
  public boolean isStyleConditionWritable()
  {
    return isWritable(STYLECONDITION);
  }
  
  public boolean isStyleConditionReadable()
  {
    return isReadable(STYLECONDITION);
  }
  
  public boolean isStyleConditionModified()
  {
    return isModified(STYLECONDITION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getStyleConditionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(STYLECONDITION).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyleDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyleDTO) dto;
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
  
  public static com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyleQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyleQueryDTO) clientRequest.getAllInstances(com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyleDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyleDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyleDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyleDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyleDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyleDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyleDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
