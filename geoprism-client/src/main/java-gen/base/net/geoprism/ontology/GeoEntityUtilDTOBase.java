package net.geoprism.ontology;

@com.runwaysdk.business.ClassSignature(hash = -2105263295)
public abstract class GeoEntityUtilDTOBase extends com.runwaysdk.business.UtilDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "net.geoprism.ontology.GeoEntityUtil";
  private static final long serialVersionUID = -2105263295;
  
  protected GeoEntityUtilDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static final void applyGeometries(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String featureCollection)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{featureCollection};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.ontology.GeoEntityUtilDTO.CLASS, "applyGeometries", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final void cancelEditingSession(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String config)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{config};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.ontology.GeoEntityUtilDTO.CLASS, "cancelEditingSession", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final void deleteEntityProblem(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String problemId)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{problemId};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.ontology.GeoEntityUtilDTO.CLASS, "deleteEntityProblem", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final void deleteGeoEntity(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.ontology.GeoEntityUtilDTO.CLASS, "deleteGeoEntity", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.system.gis.geo.GeoEntityProblemViewDTO[] getAllProblems(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.ontology.GeoEntityUtilDTO.CLASS, "getAllProblems", _declaredTypes);
    return (com.runwaysdk.system.gis.geo.GeoEntityProblemViewDTO[]) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.business.ValueQueryDTO getChildren(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id, java.lang.String universalId, java.lang.Integer limit)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.lang.String", "java.lang.Integer"};
    Object[] _parameters = new Object[]{id, universalId, limit};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.ontology.GeoEntityUtilDTO.CLASS, "getChildren", _declaredTypes);
    return (com.runwaysdk.business.ValueQueryDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final java.lang.String getChildrenBBOX(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id, java.lang.String universalId)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.lang.String"};
    Object[] _parameters = new Object[]{id, universalId};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.ontology.GeoEntityUtilDTO.CLASS, "getChildrenBBOX", _declaredTypes);
    return (java.lang.String) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final java.io.InputStream getData(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String config)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{config};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.ontology.GeoEntityUtilDTO.CLASS, "getData", _declaredTypes);
    return (java.io.InputStream) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final java.lang.String getEntitiesBBOX(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String[] ids)
  {
    String[] _declaredTypes = new String[]{"[Ljava.lang.String;"};
    Object[] _parameters = new Object[]{ids};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.ontology.GeoEntityUtilDTO.CLASS, "getEntitiesBBOX", _declaredTypes);
    return (java.lang.String) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.system.gis.geo.GeoEntityDTO getEntity(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.ontology.GeoEntityUtilDTO.CLASS, "getEntity", _declaredTypes);
    return (com.runwaysdk.system.gis.geo.GeoEntityDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final java.lang.String getEntityLabel(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String entityId)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{entityId};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.ontology.GeoEntityUtilDTO.CLASS, "getEntityLabel", _declaredTypes);
    return (java.lang.String) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.business.ValueQueryDTO getGeoEntitySuggestions(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String parentId, java.lang.String universalId, java.lang.String text, java.lang.Integer limit)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.lang.String", "java.lang.String", "java.lang.Integer"};
    Object[] _parameters = new Object[]{parentId, universalId, text, limit};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.ontology.GeoEntityUtilDTO.CLASS, "getGeoEntitySuggestions", _declaredTypes);
    return (com.runwaysdk.business.ValueQueryDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final java.lang.String getGeoEntityTree(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String geoEntityId)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{geoEntityId};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.ontology.GeoEntityUtilDTO.CLASS, "getGeoEntityTree", _declaredTypes);
    return (java.lang.String) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.system.gis.geo.GeoEntityDTO[] getOrderedAncestors(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.ontology.GeoEntityUtilDTO.CLASS, "getOrderedAncestors", _declaredTypes);
    return (com.runwaysdk.system.gis.geo.GeoEntityDTO[]) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final java.lang.String[] makeSynonym(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String sourceId, java.lang.String destinationId)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.lang.String"};
    Object[] _parameters = new Object[]{sourceId, destinationId};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.ontology.GeoEntityUtilDTO.CLASS, "makeSynonym", _declaredTypes);
    return (java.lang.String[]) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final java.io.InputStream openEditingSession(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String config)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{config};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.ontology.GeoEntityUtilDTO.CLASS, "openEditingSession", _declaredTypes);
    return (java.io.InputStream) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final java.lang.String publishLayers(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id, java.lang.String universalId, java.lang.String existingLayerNames)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.lang.String", "java.lang.String"};
    Object[] _parameters = new Object[]{id, universalId, existingLayerNames};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.ontology.GeoEntityUtilDTO.CLASS, "publishLayers", _declaredTypes);
    return (java.lang.String) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final void refreshViews(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String existingLayerNames)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{existingLayerNames};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.ontology.GeoEntityUtilDTO.CLASS, "refreshViews", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final java.lang.String[] restoreSynonym(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String synonymId)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{synonymId};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.ontology.GeoEntityUtilDTO.CLASS, "restoreSynonym", _declaredTypes);
    return (java.lang.String[]) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static GeoEntityUtilDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.UtilDTO dto = (com.runwaysdk.business.UtilDTO)clientRequest.get(id);
    
    return (GeoEntityUtilDTO) dto;
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
