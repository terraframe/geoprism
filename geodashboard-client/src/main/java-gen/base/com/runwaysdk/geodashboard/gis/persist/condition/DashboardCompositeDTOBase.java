package com.runwaysdk.geodashboard.gis.persist.condition;

@com.runwaysdk.business.ClassSignature(hash = -469648748)
public abstract class DashboardCompositeDTOBase extends com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.gis.persist.condition.DashboardComposite";
  private static final long serialVersionUID = -469648748;
  
  protected DashboardCompositeDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected DashboardCompositeDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String LEFTCONDITION = "leftCondition";
  public static java.lang.String RIGHTCONDITION = "rightCondition";
  public com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionDTO getLeftCondition()
  {
    if(getValue(LEFTCONDITION) == null || getValue(LEFTCONDITION).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionDTO.get(getRequest(), getValue(LEFTCONDITION));
    }
  }
  
  public String getLeftConditionId()
  {
    return getValue(LEFTCONDITION);
  }
  
  public void setLeftCondition(com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionDTO value)
  {
    if(value == null)
    {
      setValue(LEFTCONDITION, "");
    }
    else
    {
      setValue(LEFTCONDITION, value.getId());
    }
  }
  
  public boolean isLeftConditionWritable()
  {
    return isWritable(LEFTCONDITION);
  }
  
  public boolean isLeftConditionReadable()
  {
    return isReadable(LEFTCONDITION);
  }
  
  public boolean isLeftConditionModified()
  {
    return isModified(LEFTCONDITION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getLeftConditionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(LEFTCONDITION).getAttributeMdDTO();
  }
  
  public com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionDTO getRightCondition()
  {
    if(getValue(RIGHTCONDITION) == null || getValue(RIGHTCONDITION).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionDTO.get(getRequest(), getValue(RIGHTCONDITION));
    }
  }
  
  public String getRightConditionId()
  {
    return getValue(RIGHTCONDITION);
  }
  
  public void setRightCondition(com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionDTO value)
  {
    if(value == null)
    {
      setValue(RIGHTCONDITION, "");
    }
    else
    {
      setValue(RIGHTCONDITION, value.getId());
    }
  }
  
  public boolean isRightConditionWritable()
  {
    return isWritable(RIGHTCONDITION);
  }
  
  public boolean isRightConditionReadable()
  {
    return isReadable(RIGHTCONDITION);
  }
  
  public boolean isRightConditionModified()
  {
    return isModified(RIGHTCONDITION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getRightConditionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(RIGHTCONDITION).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.condition.DashboardCompositeDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.geodashboard.gis.persist.condition.DashboardCompositeDTO) dto;
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
  
  public static com.runwaysdk.geodashboard.gis.persist.condition.DashboardCompositeQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.geodashboard.gis.persist.condition.DashboardCompositeQueryDTO) clientRequest.getAllInstances(com.runwaysdk.geodashboard.gis.persist.condition.DashboardCompositeDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.condition.DashboardCompositeDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.condition.DashboardCompositeDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.geodashboard.gis.persist.condition.DashboardCompositeDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.condition.DashboardCompositeDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.condition.DashboardCompositeDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.geodashboard.gis.persist.condition.DashboardCompositeDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
