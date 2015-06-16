package com.runwaysdk.geodashboard.gis.persist;

@com.runwaysdk.business.ClassSignature(hash = -297675407)
public abstract class DashboardThematicLayerDTOBase extends com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer";
  private static final long serialVersionUID = -297675407;
  
  protected DashboardThematicLayerDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected DashboardThematicLayerDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String AGGREGATIONSTRATEGY = "aggregationStrategy";
  public static java.lang.String AGGREGATIONTYPE = "aggregationType";
  public static java.lang.String GEONODE = "geoNode";
  public static java.lang.String MDATTRIBUTE = "mdAttribute";
  public com.runwaysdk.geodashboard.gis.persist.AggregationStrategyDTO getAggregationStrategy()
  {
    if(getValue(AGGREGATIONSTRATEGY) == null || getValue(AGGREGATIONSTRATEGY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.geodashboard.gis.persist.AggregationStrategyDTO.get(getRequest(), getValue(AGGREGATIONSTRATEGY));
    }
  }
  
  public String getAggregationStrategyId()
  {
    return getValue(AGGREGATIONSTRATEGY);
  }
  
  public void setAggregationStrategy(com.runwaysdk.geodashboard.gis.persist.AggregationStrategyDTO value)
  {
    if(value == null)
    {
      setValue(AGGREGATIONSTRATEGY, "");
    }
    else
    {
      setValue(AGGREGATIONSTRATEGY, value.getId());
    }
  }
  
  public boolean isAggregationStrategyWritable()
  {
    return isWritable(AGGREGATIONSTRATEGY);
  }
  
  public boolean isAggregationStrategyReadable()
  {
    return isReadable(AGGREGATIONSTRATEGY);
  }
  
  public boolean isAggregationStrategyModified()
  {
    return isModified(AGGREGATIONSTRATEGY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getAggregationStrategyMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(AGGREGATIONSTRATEGY).getAttributeMdDTO();
  }
  
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
  
  public com.runwaysdk.system.gis.geo.GeoNodeDTO getGeoNode()
  {
    if(getValue(GEONODE) == null || getValue(GEONODE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.gis.geo.GeoNodeDTO.get(getRequest(), getValue(GEONODE));
    }
  }
  
  public String getGeoNodeId()
  {
    return getValue(GEONODE);
  }
  
  public void setGeoNode(com.runwaysdk.system.gis.geo.GeoNodeDTO value)
  {
    if(value == null)
    {
      setValue(GEONODE, "");
    }
    else
    {
      setValue(GEONODE, value.getId());
    }
  }
  
  public boolean isGeoNodeWritable()
  {
    return isWritable(GEONODE);
  }
  
  public boolean isGeoNodeReadable()
  {
    return isReadable(GEONODE);
  }
  
  public boolean isGeoNodeModified()
  {
    return isModified(GEONODE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getGeoNodeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(GEONODE).getAttributeMdDTO();
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
  
  public final java.lang.String applyWithStyleAndStrategy(com.runwaysdk.geodashboard.gis.persist.DashboardStyleDTO style, java.lang.String mapId, com.runwaysdk.geodashboard.gis.persist.AggregationStrategyDTO strategy, com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionDTO[] conditions)
  {
    String[] _declaredTypes = new String[]{"com.runwaysdk.geodashboard.gis.persist.DashboardStyle", "java.lang.String", "com.runwaysdk.geodashboard.gis.persist.AggregationStrategy", "[Lcom.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;"};
    Object[] _parameters = new Object[]{style, mapId, strategy, conditions};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerDTO.CLASS, "applyWithStyleAndStrategy", _declaredTypes);
    return (java.lang.String) getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final java.lang.String applyWithStyleAndStrategy(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id, com.runwaysdk.geodashboard.gis.persist.DashboardStyleDTO style, java.lang.String mapId, com.runwaysdk.geodashboard.gis.persist.AggregationStrategyDTO strategy, com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionDTO[] conditions)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "com.runwaysdk.geodashboard.gis.persist.DashboardStyle", "java.lang.String", "com.runwaysdk.geodashboard.gis.persist.AggregationStrategy", "[Lcom.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;"};
    Object[] _parameters = new Object[]{id, style, mapId, strategy, conditions};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerDTO.CLASS, "applyWithStyleAndStrategy", _declaredTypes);
    return (java.lang.String) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerDTO) dto;
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
  
  public static com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerQueryDTO) clientRequest.getAllInstances(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
