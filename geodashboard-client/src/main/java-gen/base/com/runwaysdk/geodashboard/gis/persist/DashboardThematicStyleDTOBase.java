package com.runwaysdk.geodashboard.gis.persist;

@com.runwaysdk.business.ClassSignature(hash = -2027501386)
public abstract class DashboardThematicStyleDTOBase extends com.runwaysdk.geodashboard.gis.persist.DashboardStyleDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle";
  private static final long serialVersionUID = -2027501386;
  
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
  
  public static java.lang.String AGGREGATIONTYPE = "aggregationType";
  public static java.lang.String MDATTRIBUTE = "mdAttribute";
  public static java.lang.String POINTMAXSIZE = "pointMaxSize";
  public static java.lang.String POINTMINSIZE = "pointMinSize";
  public static java.lang.String POLYGONMAXFILL = "polygonMaxFill";
  public static java.lang.String POLYGONMINFILL = "polygonMinFill";
  public static java.lang.String STYLECONDITION = "styleCondition";
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.geodashboard.gis.persist.AllAggregationTypeDTO> getAggregationType()
  {
    return (java.util.List<com.runwaysdk.geodashboard.gis.persist.AllAggregationTypeDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.geodashboard.gis.persist.AllAggregationTypeDTO.CLASS, getEnumNames(AGGREGATIONTYPE));
  }
  
  public java.util.List<String> getAggregationTypeEnumNames()
  {
    return getEnumNames(AGGREGATIONTYPE);
  }
  
  public void addAggregationType(com.runwaysdk.geodashboard.gis.persist.AllAggregationTypeDTO enumDTO)
  {
    addEnumItem(AGGREGATIONTYPE, enumDTO.toString());
  }
  
  public void removeAggregationType(com.runwaysdk.geodashboard.gis.persist.AllAggregationTypeDTO enumDTO)
  {
    removeEnumItem(AGGREGATIONTYPE, enumDTO.toString());
  }
  
  public void clearAggregationType()
  {
    clearEnum(AGGREGATIONTYPE);
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
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getAggregationTypeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(AGGREGATIONTYPE).getAttributeMdDTO();
  }
  
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
  
  public Integer getPointMaxSize()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(POINTMAXSIZE));
  }
  
  public void setPointMaxSize(Integer value)
  {
    if(value == null)
    {
      setValue(POINTMAXSIZE, "");
    }
    else
    {
      setValue(POINTMAXSIZE, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isPointMaxSizeWritable()
  {
    return isWritable(POINTMAXSIZE);
  }
  
  public boolean isPointMaxSizeReadable()
  {
    return isReadable(POINTMAXSIZE);
  }
  
  public boolean isPointMaxSizeModified()
  {
    return isModified(POINTMAXSIZE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getPointMaxSizeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(POINTMAXSIZE).getAttributeMdDTO();
  }
  
  public Integer getPointMinSize()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(POINTMINSIZE));
  }
  
  public void setPointMinSize(Integer value)
  {
    if(value == null)
    {
      setValue(POINTMINSIZE, "");
    }
    else
    {
      setValue(POINTMINSIZE, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isPointMinSizeWritable()
  {
    return isWritable(POINTMINSIZE);
  }
  
  public boolean isPointMinSizeReadable()
  {
    return isReadable(POINTMINSIZE);
  }
  
  public boolean isPointMinSizeModified()
  {
    return isModified(POINTMINSIZE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getPointMinSizeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(POINTMINSIZE).getAttributeMdDTO();
  }
  
  public String getPolygonMaxFill()
  {
    return getValue(POLYGONMAXFILL);
  }
  
  public void setPolygonMaxFill(String value)
  {
    if(value == null)
    {
      setValue(POLYGONMAXFILL, "");
    }
    else
    {
      setValue(POLYGONMAXFILL, value);
    }
  }
  
  public boolean isPolygonMaxFillWritable()
  {
    return isWritable(POLYGONMAXFILL);
  }
  
  public boolean isPolygonMaxFillReadable()
  {
    return isReadable(POLYGONMAXFILL);
  }
  
  public boolean isPolygonMaxFillModified()
  {
    return isModified(POLYGONMAXFILL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getPolygonMaxFillMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(POLYGONMAXFILL).getAttributeMdDTO();
  }
  
  public String getPolygonMinFill()
  {
    return getValue(POLYGONMINFILL);
  }
  
  public void setPolygonMinFill(String value)
  {
    if(value == null)
    {
      setValue(POLYGONMINFILL, "");
    }
    else
    {
      setValue(POLYGONMINFILL, value);
    }
  }
  
  public boolean isPolygonMinFillWritable()
  {
    return isWritable(POLYGONMINFILL);
  }
  
  public boolean isPolygonMinFillReadable()
  {
    return isReadable(POLYGONMINFILL);
  }
  
  public boolean isPolygonMinFillModified()
  {
    return isModified(POLYGONMINFILL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getPolygonMinFillMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(POLYGONMINFILL).getAttributeMdDTO();
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
