package net.geoprism.data.etl;

@com.runwaysdk.business.ClassSignature(hash = 1808313497)
public abstract class TargetFieldClassifierBindingDTOBase extends net.geoprism.data.etl.TargetFieldBasicBindingDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "net.geoprism.data.etl.TargetFieldClassifierBinding";
  private static final long serialVersionUID = 1808313497;
  
  protected TargetFieldClassifierBindingDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected TargetFieldClassifierBindingDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ISVALIDATE = "isValidate";
  public static java.lang.String PACKAGENAME = "packageName";
  public Boolean getIsValidate()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ISVALIDATE));
  }
  
  public void setIsValidate(Boolean value)
  {
    if(value == null)
    {
      setValue(ISVALIDATE, "");
    }
    else
    {
      setValue(ISVALIDATE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isIsValidateWritable()
  {
    return isWritable(ISVALIDATE);
  }
  
  public boolean isIsValidateReadable()
  {
    return isReadable(ISVALIDATE);
  }
  
  public boolean isIsValidateModified()
  {
    return isModified(ISVALIDATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getIsValidateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(ISVALIDATE).getAttributeMdDTO();
  }
  
  public String getPackageName()
  {
    return getValue(PACKAGENAME);
  }
  
  public void setPackageName(String value)
  {
    if(value == null)
    {
      setValue(PACKAGENAME, "");
    }
    else
    {
      setValue(PACKAGENAME, value);
    }
  }
  
  public boolean isPackageNameWritable()
  {
    return isWritable(PACKAGENAME);
  }
  
  public boolean isPackageNameReadable()
  {
    return isReadable(PACKAGENAME);
  }
  
  public boolean isPackageNameModified()
  {
    return isModified(PACKAGENAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getPackageNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(PACKAGENAME).getAttributeMdDTO();
  }
  
  public static net.geoprism.data.etl.TargetFieldClassifierBindingDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (net.geoprism.data.etl.TargetFieldClassifierBindingDTO) dto;
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
  
  public static net.geoprism.data.etl.TargetFieldClassifierBindingQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (net.geoprism.data.etl.TargetFieldClassifierBindingQueryDTO) clientRequest.getAllInstances(net.geoprism.data.etl.TargetFieldClassifierBindingDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static net.geoprism.data.etl.TargetFieldClassifierBindingDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.data.etl.TargetFieldClassifierBindingDTO.CLASS, "lock", _declaredTypes);
    return (net.geoprism.data.etl.TargetFieldClassifierBindingDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static net.geoprism.data.etl.TargetFieldClassifierBindingDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.data.etl.TargetFieldClassifierBindingDTO.CLASS, "unlock", _declaredTypes);
    return (net.geoprism.data.etl.TargetFieldClassifierBindingDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
