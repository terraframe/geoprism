package com.runwaysdk.geodashboard.gis.persist;

@com.runwaysdk.business.ClassSignature(hash = 1713873495)
public abstract class AggregationStrategyViewDTOBase extends com.runwaysdk.business.ViewDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.gis.persist.AggregationStrategyView";
  private static final long serialVersionUID = 1713873495;
  
  protected AggregationStrategyViewDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String AGGREGATIONTYPE = "aggregationType";
  public static java.lang.String AVAILABLEGEOMETRYTYPES = "availableGeometryTypes";
  public static java.lang.String DISPLAYLABEL = "displayLabel";
  public static java.lang.String ID = "id";
  public static java.lang.String VALUE = "value";
  public String getAggregationType()
  {
    return getValue(AGGREGATIONTYPE);
  }
  
  public void setAggregationType(String value)
  {
    if(value == null)
    {
      setValue(AGGREGATIONTYPE, "");
    }
    else
    {
      setValue(AGGREGATIONTYPE, value);
    }
  }
  
  public boolean isAggregationTypeWritable()
  {
    return isWritable(AGGREGATIONTYPE);
  }
  
  public boolean isAggregationTypeReadable()
  {
    return isReadable(AGGREGATIONTYPE);
  }
  
  public boolean isAggregationTypeModified()
  {
    return isModified(AGGREGATIONTYPE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getAggregationTypeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(AGGREGATIONTYPE).getAttributeMdDTO();
  }
  
  public String getAvailableGeometryTypes()
  {
    return getValue(AVAILABLEGEOMETRYTYPES);
  }
  
  public void setAvailableGeometryTypes(String value)
  {
    if(value == null)
    {
      setValue(AVAILABLEGEOMETRYTYPES, "");
    }
    else
    {
      setValue(AVAILABLEGEOMETRYTYPES, value);
    }
  }
  
  public boolean isAvailableGeometryTypesWritable()
  {
    return isWritable(AVAILABLEGEOMETRYTYPES);
  }
  
  public boolean isAvailableGeometryTypesReadable()
  {
    return isReadable(AVAILABLEGEOMETRYTYPES);
  }
  
  public boolean isAvailableGeometryTypesModified()
  {
    return isModified(AVAILABLEGEOMETRYTYPES);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getAvailableGeometryTypesMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(AVAILABLEGEOMETRYTYPES).getAttributeMdDTO();
  }
  
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
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(DISPLAYLABEL).getAttributeMdDTO();
  }
  
  public String getValue()
  {
    return getValue(VALUE);
  }
  
  public void setValue(String value)
  {
    if(value == null)
    {
      setValue(VALUE, "");
    }
    else
    {
      setValue(VALUE, value);
    }
  }
  
  public boolean isValueWritable()
  {
    return isWritable(VALUE);
  }
  
  public boolean isValueReadable()
  {
    return isReadable(VALUE);
  }
  
  public boolean isValueModified()
  {
    return isModified(VALUE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getValueMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(VALUE).getAttributeMdDTO();
  }
  
  public static final com.runwaysdk.geodashboard.gis.persist.AggregationStrategyViewDTO[] getAggregationStrategies(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String nodeId)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{nodeId};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.AggregationStrategyViewDTO.CLASS, "getAggregationStrategies", _declaredTypes);
    return (com.runwaysdk.geodashboard.gis.persist.AggregationStrategyViewDTO[]) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static AggregationStrategyViewDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.ViewDTO dto = (com.runwaysdk.business.ViewDTO)clientRequest.get(id);
    
    return (AggregationStrategyViewDTO) dto;
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
