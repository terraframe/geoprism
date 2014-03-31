package com.runwaysdk.geodashboard.databrowser;

@com.runwaysdk.business.ClassSignature(hash = -2034307707)
public abstract class MetadataTypeDTOBase extends com.runwaysdk.business.ViewDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.databrowser.MetadataType";
  private static final long serialVersionUID = -2034307707;
  
  protected MetadataTypeDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DISPLAYLABEL = "displayLabel";
  public static java.lang.String ID = "id";
  public static java.lang.String PARENTID = "parentId";
  public static java.lang.String TYPEID = "typeId";
  public static java.lang.String TYPENAME = "typeName";
  public String getDisplayLabel()
  {
    return getValue(DISPLAYLABEL);
  }
  
  public void setDisplayLabel(String value)
  {
    if(value == null)
    {
      setValue(DISPLAYLABEL, "");
    }
    else
    {
      setValue(DISPLAYLABEL, value);
    }
  }
  
  public boolean isDisplayLabelWritable()
  {
    return isWritable(DISPLAYLABEL);
  }
  
  public boolean isDisplayLabelReadable()
  {
    return isReadable(DISPLAYLABEL);
  }
  
  public boolean isDisplayLabelModified()
  {
    return isModified(DISPLAYLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(DISPLAYLABEL).getAttributeMdDTO();
  }
  
  public String getParentId()
  {
    return getValue(PARENTID);
  }
  
  public void setParentId(String value)
  {
    if(value == null)
    {
      setValue(PARENTID, "");
    }
    else
    {
      setValue(PARENTID, value);
    }
  }
  
  public boolean isParentIdWritable()
  {
    return isWritable(PARENTID);
  }
  
  public boolean isParentIdReadable()
  {
    return isReadable(PARENTID);
  }
  
  public boolean isParentIdModified()
  {
    return isModified(PARENTID);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getParentIdMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(PARENTID).getAttributeMdDTO();
  }
  
  public String getTypeId()
  {
    return getValue(TYPEID);
  }
  
  public void setTypeId(String value)
  {
    if(value == null)
    {
      setValue(TYPEID, "");
    }
    else
    {
      setValue(TYPEID, value);
    }
  }
  
  public boolean isTypeIdWritable()
  {
    return isWritable(TYPEID);
  }
  
  public boolean isTypeIdReadable()
  {
    return isReadable(TYPEID);
  }
  
  public boolean isTypeIdModified()
  {
    return isModified(TYPEID);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getTypeIdMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(TYPEID).getAttributeMdDTO();
  }
  
  public String getTypeName()
  {
    return getValue(TYPENAME);
  }
  
  public void setTypeName(String value)
  {
    if(value == null)
    {
      setValue(TYPENAME, "");
    }
    else
    {
      setValue(TYPENAME, value);
    }
  }
  
  public boolean isTypeNameWritable()
  {
    return isWritable(TYPENAME);
  }
  
  public boolean isTypeNameReadable()
  {
    return isReadable(TYPENAME);
  }
  
  public boolean isTypeNameModified()
  {
    return isModified(TYPENAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getTypeNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(TYPENAME).getAttributeMdDTO();
  }
  
  public static MetadataTypeDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.ViewDTO dto = (com.runwaysdk.business.ViewDTO)clientRequest.get(id);
    
    return (MetadataTypeDTO) dto;
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
