package net.geoprism.data.etl;

@com.runwaysdk.business.ClassSignature(hash = 1302801909)
public abstract class TargetFieldGeoEntityBindingDTOBase extends net.geoprism.data.etl.TargetFieldBindingDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "net.geoprism.data.etl.TargetFieldGeoEntityBinding";
  private static final long serialVersionUID = 1302801909;
  
  protected TargetFieldGeoEntityBindingDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected TargetFieldGeoEntityBindingDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String GEOENTITY = "geoEntity";
  public static java.lang.String LATITUDEATTRIBUTENAME = "latitudeAttributeName";
  public static java.lang.String LONGITUDEATTRIBUTENAME = "longitudeAttributeName";
  public static java.lang.String USECOORDINATESFORLOCATIONASSIGNMENT = "useCoordinatesForLocationAssignment";
  public com.runwaysdk.system.gis.geo.GeoEntityDTO getGeoEntity()
  {
    if(getValue(GEOENTITY) == null || getValue(GEOENTITY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.gis.geo.GeoEntityDTO.get(getRequest(), getValue(GEOENTITY));
    }
  }
  
  public String getGeoEntityId()
  {
    return getValue(GEOENTITY);
  }
  
  public void setGeoEntity(com.runwaysdk.system.gis.geo.GeoEntityDTO value)
  {
    if(value == null)
    {
      setValue(GEOENTITY, "");
    }
    else
    {
      setValue(GEOENTITY, value.getId());
    }
  }
  
  public boolean isGeoEntityWritable()
  {
    return isWritable(GEOENTITY);
  }
  
  public boolean isGeoEntityReadable()
  {
    return isReadable(GEOENTITY);
  }
  
  public boolean isGeoEntityModified()
  {
    return isModified(GEOENTITY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getGeoEntityMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(GEOENTITY).getAttributeMdDTO();
  }
  
  public String getLatitudeAttributeName()
  {
    return getValue(LATITUDEATTRIBUTENAME);
  }
  
  public void setLatitudeAttributeName(String value)
  {
    if(value == null)
    {
      setValue(LATITUDEATTRIBUTENAME, "");
    }
    else
    {
      setValue(LATITUDEATTRIBUTENAME, value);
    }
  }
  
  public boolean isLatitudeAttributeNameWritable()
  {
    return isWritable(LATITUDEATTRIBUTENAME);
  }
  
  public boolean isLatitudeAttributeNameReadable()
  {
    return isReadable(LATITUDEATTRIBUTENAME);
  }
  
  public boolean isLatitudeAttributeNameModified()
  {
    return isModified(LATITUDEATTRIBUTENAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getLatitudeAttributeNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(LATITUDEATTRIBUTENAME).getAttributeMdDTO();
  }
  
  public String getLongitudeAttributeName()
  {
    return getValue(LONGITUDEATTRIBUTENAME);
  }
  
  public void setLongitudeAttributeName(String value)
  {
    if(value == null)
    {
      setValue(LONGITUDEATTRIBUTENAME, "");
    }
    else
    {
      setValue(LONGITUDEATTRIBUTENAME, value);
    }
  }
  
  public boolean isLongitudeAttributeNameWritable()
  {
    return isWritable(LONGITUDEATTRIBUTENAME);
  }
  
  public boolean isLongitudeAttributeNameReadable()
  {
    return isReadable(LONGITUDEATTRIBUTENAME);
  }
  
  public boolean isLongitudeAttributeNameModified()
  {
    return isModified(LONGITUDEATTRIBUTENAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getLongitudeAttributeNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(LONGITUDEATTRIBUTENAME).getAttributeMdDTO();
  }
  
  public Boolean getUseCoordinatesForLocationAssignment()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(USECOORDINATESFORLOCATIONASSIGNMENT));
  }
  
  public void setUseCoordinatesForLocationAssignment(Boolean value)
  {
    if(value == null)
    {
      setValue(USECOORDINATESFORLOCATIONASSIGNMENT, "");
    }
    else
    {
      setValue(USECOORDINATESFORLOCATIONASSIGNMENT, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isUseCoordinatesForLocationAssignmentWritable()
  {
    return isWritable(USECOORDINATESFORLOCATIONASSIGNMENT);
  }
  
  public boolean isUseCoordinatesForLocationAssignmentReadable()
  {
    return isReadable(USECOORDINATESFORLOCATIONASSIGNMENT);
  }
  
  public boolean isUseCoordinatesForLocationAssignmentModified()
  {
    return isModified(USECOORDINATESFORLOCATIONASSIGNMENT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getUseCoordinatesForLocationAssignmentMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(USECOORDINATESFORLOCATIONASSIGNMENT).getAttributeMdDTO();
  }
  
  public static net.geoprism.data.etl.TargetFieldGeoEntityBindingDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (net.geoprism.data.etl.TargetFieldGeoEntityBindingDTO) dto;
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
  
  public static net.geoprism.data.etl.TargetFieldGeoEntityBindingQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (net.geoprism.data.etl.TargetFieldGeoEntityBindingQueryDTO) clientRequest.getAllInstances(net.geoprism.data.etl.TargetFieldGeoEntityBindingDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static net.geoprism.data.etl.TargetFieldGeoEntityBindingDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.data.etl.TargetFieldGeoEntityBindingDTO.CLASS, "lock", _declaredTypes);
    return (net.geoprism.data.etl.TargetFieldGeoEntityBindingDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static net.geoprism.data.etl.TargetFieldGeoEntityBindingDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.data.etl.TargetFieldGeoEntityBindingDTO.CLASS, "unlock", _declaredTypes);
    return (net.geoprism.data.etl.TargetFieldGeoEntityBindingDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
