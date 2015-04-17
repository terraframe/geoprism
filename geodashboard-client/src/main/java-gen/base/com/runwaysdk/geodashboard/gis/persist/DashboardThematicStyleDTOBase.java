package com.runwaysdk.geodashboard.gis.persist;

@com.runwaysdk.business.ClassSignature(hash = 1686172467)
public abstract class DashboardThematicStyleDTOBase extends com.runwaysdk.geodashboard.gis.persist.DashboardStyleDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle";
  private static final long serialVersionUID = 1686172467;
  
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
  
  public static java.lang.String BUBBLECONTINUOUSSIZE = "bubbleContinuousSize";
  public static java.lang.String POINTFIXED = "pointFixed";
  public static java.lang.String POINTFIXEDSIZE = "pointFixedSize";
  public static java.lang.String POINTMAXSIZE = "pointMaxSize";
  public static java.lang.String POINTMINSIZE = "pointMinSize";
  public static java.lang.String POLYGONMAXFILL = "polygonMaxFill";
  public static java.lang.String POLYGONMINFILL = "polygonMinFill";
  public static java.lang.String STYLECATEGORIES = "styleCategories";
  public static java.lang.String STYLECONDITION = "styleCondition";
  public Boolean getBubbleContinuousSize()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(BUBBLECONTINUOUSSIZE));
  }
  
  public void setBubbleContinuousSize(Boolean value)
  {
    if(value == null)
    {
      setValue(BUBBLECONTINUOUSSIZE, "");
    }
    else
    {
      setValue(BUBBLECONTINUOUSSIZE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isBubbleContinuousSizeWritable()
  {
    return isWritable(BUBBLECONTINUOUSSIZE);
  }
  
  public boolean isBubbleContinuousSizeReadable()
  {
    return isReadable(BUBBLECONTINUOUSSIZE);
  }
  
  public boolean isBubbleContinuousSizeModified()
  {
    return isModified(BUBBLECONTINUOUSSIZE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getBubbleContinuousSizeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(BUBBLECONTINUOUSSIZE).getAttributeMdDTO();
  }
  
  public Boolean getPointFixed()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(POINTFIXED));
  }
  
  public void setPointFixed(Boolean value)
  {
    if(value == null)
    {
      setValue(POINTFIXED, "");
    }
    else
    {
      setValue(POINTFIXED, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isPointFixedWritable()
  {
    return isWritable(POINTFIXED);
  }
  
  public boolean isPointFixedReadable()
  {
    return isReadable(POINTFIXED);
  }
  
  public boolean isPointFixedModified()
  {
    return isModified(POINTFIXED);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getPointFixedMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(POINTFIXED).getAttributeMdDTO();
  }
  
  public Integer getPointFixedSize()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(POINTFIXEDSIZE));
  }
  
  public void setPointFixedSize(Integer value)
  {
    if(value == null)
    {
      setValue(POINTFIXEDSIZE, "");
    }
    else
    {
      setValue(POINTFIXEDSIZE, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isPointFixedSizeWritable()
  {
    return isWritable(POINTFIXEDSIZE);
  }
  
  public boolean isPointFixedSizeReadable()
  {
    return isReadable(POINTFIXEDSIZE);
  }
  
  public boolean isPointFixedSizeModified()
  {
    return isModified(POINTFIXEDSIZE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getPointFixedSizeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(POINTFIXEDSIZE).getAttributeMdDTO();
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
  
  public String getStyleCategories()
  {
    return getValue(STYLECATEGORIES);
  }
  
  public void setStyleCategories(String value)
  {
    if(value == null)
    {
      setValue(STYLECATEGORIES, "");
    }
    else
    {
      setValue(STYLECATEGORIES, value);
    }
  }
  
  public boolean isStyleCategoriesWritable()
  {
    return isWritable(STYLECATEGORIES);
  }
  
  public boolean isStyleCategoriesReadable()
  {
    return isReadable(STYLECATEGORIES);
  }
  
  public boolean isStyleCategoriesModified()
  {
    return isModified(STYLECATEGORIES);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getStyleCategoriesMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(STYLECATEGORIES).getAttributeMdDTO();
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
