package com.runwaysdk.geodashboard.gis.persist;

@com.runwaysdk.business.ClassSignature(hash = -571208042)
public abstract class DashboardThematicStyleDTOBase extends com.runwaysdk.geodashboard.gis.persist.DashboardStyleDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle";
  private static final long serialVersionUID = -571208042;
  
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
  public static java.lang.String STYLECATEGORY1 = "styleCategory1";
  public static java.lang.String STYLECATEGORY2 = "styleCategory2";
  public static java.lang.String STYLECATEGORY3 = "styleCategory3";
  public static java.lang.String STYLECATEGORY4 = "styleCategory4";
  public static java.lang.String STYLECATEGORY5 = "styleCategory5";
  public static java.lang.String STYLECATEGORYFILL1 = "styleCategoryFill1";
  public static java.lang.String STYLECATEGORYFILL2 = "styleCategoryFill2";
  public static java.lang.String STYLECATEGORYFILL3 = "styleCategoryFill3";
  public static java.lang.String STYLECATEGORYFILL4 = "styleCategoryFill4";
  public static java.lang.String STYLECATEGORYFILL5 = "styleCategoryFill5";
  public static java.lang.String STYLECONDITION = "styleCondition";
  public static java.lang.String STYLEONTOLOGYCATEGORYIES = "styleOntologyCategoryies";
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
  
  public String getStyleCategory1()
  {
    return getValue(STYLECATEGORY1);
  }
  
  public void setStyleCategory1(String value)
  {
    if(value == null)
    {
      setValue(STYLECATEGORY1, "");
    }
    else
    {
      setValue(STYLECATEGORY1, value);
    }
  }
  
  public boolean isStyleCategory1Writable()
  {
    return isWritable(STYLECATEGORY1);
  }
  
  public boolean isStyleCategory1Readable()
  {
    return isReadable(STYLECATEGORY1);
  }
  
  public boolean isStyleCategory1Modified()
  {
    return isModified(STYLECATEGORY1);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getStyleCategory1Md()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(STYLECATEGORY1).getAttributeMdDTO();
  }
  
  public String getStyleCategory2()
  {
    return getValue(STYLECATEGORY2);
  }
  
  public void setStyleCategory2(String value)
  {
    if(value == null)
    {
      setValue(STYLECATEGORY2, "");
    }
    else
    {
      setValue(STYLECATEGORY2, value);
    }
  }
  
  public boolean isStyleCategory2Writable()
  {
    return isWritable(STYLECATEGORY2);
  }
  
  public boolean isStyleCategory2Readable()
  {
    return isReadable(STYLECATEGORY2);
  }
  
  public boolean isStyleCategory2Modified()
  {
    return isModified(STYLECATEGORY2);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getStyleCategory2Md()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(STYLECATEGORY2).getAttributeMdDTO();
  }
  
  public String getStyleCategory3()
  {
    return getValue(STYLECATEGORY3);
  }
  
  public void setStyleCategory3(String value)
  {
    if(value == null)
    {
      setValue(STYLECATEGORY3, "");
    }
    else
    {
      setValue(STYLECATEGORY3, value);
    }
  }
  
  public boolean isStyleCategory3Writable()
  {
    return isWritable(STYLECATEGORY3);
  }
  
  public boolean isStyleCategory3Readable()
  {
    return isReadable(STYLECATEGORY3);
  }
  
  public boolean isStyleCategory3Modified()
  {
    return isModified(STYLECATEGORY3);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getStyleCategory3Md()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(STYLECATEGORY3).getAttributeMdDTO();
  }
  
  public String getStyleCategory4()
  {
    return getValue(STYLECATEGORY4);
  }
  
  public void setStyleCategory4(String value)
  {
    if(value == null)
    {
      setValue(STYLECATEGORY4, "");
    }
    else
    {
      setValue(STYLECATEGORY4, value);
    }
  }
  
  public boolean isStyleCategory4Writable()
  {
    return isWritable(STYLECATEGORY4);
  }
  
  public boolean isStyleCategory4Readable()
  {
    return isReadable(STYLECATEGORY4);
  }
  
  public boolean isStyleCategory4Modified()
  {
    return isModified(STYLECATEGORY4);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getStyleCategory4Md()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(STYLECATEGORY4).getAttributeMdDTO();
  }
  
  public String getStyleCategory5()
  {
    return getValue(STYLECATEGORY5);
  }
  
  public void setStyleCategory5(String value)
  {
    if(value == null)
    {
      setValue(STYLECATEGORY5, "");
    }
    else
    {
      setValue(STYLECATEGORY5, value);
    }
  }
  
  public boolean isStyleCategory5Writable()
  {
    return isWritable(STYLECATEGORY5);
  }
  
  public boolean isStyleCategory5Readable()
  {
    return isReadable(STYLECATEGORY5);
  }
  
  public boolean isStyleCategory5Modified()
  {
    return isModified(STYLECATEGORY5);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getStyleCategory5Md()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(STYLECATEGORY5).getAttributeMdDTO();
  }
  
  public String getStyleCategoryFill1()
  {
    return getValue(STYLECATEGORYFILL1);
  }
  
  public void setStyleCategoryFill1(String value)
  {
    if(value == null)
    {
      setValue(STYLECATEGORYFILL1, "");
    }
    else
    {
      setValue(STYLECATEGORYFILL1, value);
    }
  }
  
  public boolean isStyleCategoryFill1Writable()
  {
    return isWritable(STYLECATEGORYFILL1);
  }
  
  public boolean isStyleCategoryFill1Readable()
  {
    return isReadable(STYLECATEGORYFILL1);
  }
  
  public boolean isStyleCategoryFill1Modified()
  {
    return isModified(STYLECATEGORYFILL1);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getStyleCategoryFill1Md()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(STYLECATEGORYFILL1).getAttributeMdDTO();
  }
  
  public String getStyleCategoryFill2()
  {
    return getValue(STYLECATEGORYFILL2);
  }
  
  public void setStyleCategoryFill2(String value)
  {
    if(value == null)
    {
      setValue(STYLECATEGORYFILL2, "");
    }
    else
    {
      setValue(STYLECATEGORYFILL2, value);
    }
  }
  
  public boolean isStyleCategoryFill2Writable()
  {
    return isWritable(STYLECATEGORYFILL2);
  }
  
  public boolean isStyleCategoryFill2Readable()
  {
    return isReadable(STYLECATEGORYFILL2);
  }
  
  public boolean isStyleCategoryFill2Modified()
  {
    return isModified(STYLECATEGORYFILL2);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getStyleCategoryFill2Md()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(STYLECATEGORYFILL2).getAttributeMdDTO();
  }
  
  public String getStyleCategoryFill3()
  {
    return getValue(STYLECATEGORYFILL3);
  }
  
  public void setStyleCategoryFill3(String value)
  {
    if(value == null)
    {
      setValue(STYLECATEGORYFILL3, "");
    }
    else
    {
      setValue(STYLECATEGORYFILL3, value);
    }
  }
  
  public boolean isStyleCategoryFill3Writable()
  {
    return isWritable(STYLECATEGORYFILL3);
  }
  
  public boolean isStyleCategoryFill3Readable()
  {
    return isReadable(STYLECATEGORYFILL3);
  }
  
  public boolean isStyleCategoryFill3Modified()
  {
    return isModified(STYLECATEGORYFILL3);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getStyleCategoryFill3Md()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(STYLECATEGORYFILL3).getAttributeMdDTO();
  }
  
  public String getStyleCategoryFill4()
  {
    return getValue(STYLECATEGORYFILL4);
  }
  
  public void setStyleCategoryFill4(String value)
  {
    if(value == null)
    {
      setValue(STYLECATEGORYFILL4, "");
    }
    else
    {
      setValue(STYLECATEGORYFILL4, value);
    }
  }
  
  public boolean isStyleCategoryFill4Writable()
  {
    return isWritable(STYLECATEGORYFILL4);
  }
  
  public boolean isStyleCategoryFill4Readable()
  {
    return isReadable(STYLECATEGORYFILL4);
  }
  
  public boolean isStyleCategoryFill4Modified()
  {
    return isModified(STYLECATEGORYFILL4);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getStyleCategoryFill4Md()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(STYLECATEGORYFILL4).getAttributeMdDTO();
  }
  
  public String getStyleCategoryFill5()
  {
    return getValue(STYLECATEGORYFILL5);
  }
  
  public void setStyleCategoryFill5(String value)
  {
    if(value == null)
    {
      setValue(STYLECATEGORYFILL5, "");
    }
    else
    {
      setValue(STYLECATEGORYFILL5, value);
    }
  }
  
  public boolean isStyleCategoryFill5Writable()
  {
    return isWritable(STYLECATEGORYFILL5);
  }
  
  public boolean isStyleCategoryFill5Readable()
  {
    return isReadable(STYLECATEGORYFILL5);
  }
  
  public boolean isStyleCategoryFill5Modified()
  {
    return isModified(STYLECATEGORYFILL5);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getStyleCategoryFill5Md()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(STYLECATEGORYFILL5).getAttributeMdDTO();
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
  
  public String getStyleOntologyCategoryies()
  {
    return getValue(STYLEONTOLOGYCATEGORYIES);
  }
  
  public void setStyleOntologyCategoryies(String value)
  {
    if(value == null)
    {
      setValue(STYLEONTOLOGYCATEGORYIES, "");
    }
    else
    {
      setValue(STYLEONTOLOGYCATEGORYIES, value);
    }
  }
  
  public boolean isStyleOntologyCategoryiesWritable()
  {
    return isWritable(STYLEONTOLOGYCATEGORYIES);
  }
  
  public boolean isStyleOntologyCategoryiesReadable()
  {
    return isReadable(STYLEONTOLOGYCATEGORYIES);
  }
  
  public boolean isStyleOntologyCategoryiesModified()
  {
    return isModified(STYLEONTOLOGYCATEGORYIES);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getStyleOntologyCategoryiesMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(STYLEONTOLOGYCATEGORYIES).getAttributeMdDTO();
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
