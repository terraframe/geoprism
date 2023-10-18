package net.geoprism.registry.service.business;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.GeoObjectOverTime;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.EdgeObject;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTimeCollection;

import net.geoprism.registry.BusinessType;
import net.geoprism.registry.conversion.ServerGeoObjectStrategyIF;
import net.geoprism.registry.etl.upload.ClassifierCache;
import net.geoprism.registry.model.BusinessObject;
import net.geoprism.registry.model.LocationInfo;
import net.geoprism.registry.model.ServerChildTreeNode;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.model.ServerParentTreeNode;
import net.geoprism.registry.model.graph.VertexServerGeoObject;
import net.geoprism.registry.query.ServerGeoObjectQuery;
import net.geoprism.registry.view.GeoObjectSplitView;
import net.geoprism.registry.view.ServerParentTreeNodeOverTime;

@Component
public interface GeoObjectBusinessServiceIF
{
  public void populate(ServerGeoObjectIF sgo, GeoObjectOverTime goTime);

  public void populate(ServerGeoObjectIF sgo, GeoObject geoObject, Date startDate, Date endDate);

  public boolean trySetValue(ServerGeoObjectIF sgo, String attributeName, Object value);

  public JsonObject getAll(String gotCode, String hierarchyCode, Date since, Boolean includeLevel, String format, String externalSystemId, Integer pageNumber, Integer pageSize);

  public ParentTreeNode addChild(String parentCode, String parentGeoObjectTypeCode, String childCode, String childGeoObjectTypeCode, String hierarchyCode, Date startDate, Date endDate);

  public void removeChild(String parentCode, String parentGeoObjectTypeCode, String childCode, String childGeoObjectTypeCode, String hierarchyCode, Date startDate, Date endDate);

  public void apply(ServerGeoObjectIF sgo, boolean isImport);

  public ServerGeoObjectIF apply(GeoObject object, Date startDate, Date endDate, boolean isNew, boolean isImport);

  public void handleDuplicateDataException(ServerGeoObjectType type, DuplicateDataException e);

  public ServerGeoObjectIF apply(GeoObject object, boolean isNew, boolean isImport);

  public ServerGeoObjectIF apply(GeoObjectOverTime goTime, boolean isNew, boolean isImport);

  public ServerGeoObjectIF split(GeoObjectSplitView view);

  public ServerGeoObjectIF newInstance(ServerGeoObjectType type);

  public ServerGeoObjectIF getGeoObject(GeoObject go);

  public ServerGeoObjectIF getGeoObject(GeoObjectOverTime timeGO);

  public ServerGeoObjectIF getGeoObjectByCode(String code, String typeCode);

  public ServerGeoObjectIF getGeoObjectByCode(String code, String typeCode, boolean throwException);

  public ServerGeoObjectIF getGeoObjectByCode(String code, ServerGeoObjectType type);

  public ServerGeoObjectIF getGeoObjectByCode(String code, ServerGeoObjectType type, boolean throwException);

  public ServerGeoObjectIF getGeoObject(String uid, String typeCode);

  public ServerGeoObjectStrategyIF getStrategy(ServerGeoObjectType type);

  public ServerGeoObjectIF build(ServerGeoObjectType type, String runwayId);

  public ServerGeoObjectIF build(ServerGeoObjectType type, Object dbObject);

  public ServerGeoObjectQuery createQuery(ServerGeoObjectType type, Date date);

  public ServerGeoObjectQuery createQuery(String typeCode);

  public boolean hasData(ServerHierarchyType serverHierarchyType, ServerGeoObjectType childType);

  public void removeAllEdges(ServerHierarchyType hierarchyType, ServerGeoObjectType childType);

  public JsonObject doesGeoObjectExistAtRange(Date startDate, Date endDate, String typeCode, String code);

  public List<VertexServerGeoObject> getAncestors(ServerGeoObjectIF sgo, ServerHierarchyType hierarchy);

  public List<VertexServerGeoObject> getAncestors(ServerGeoObjectIF sgo, ServerHierarchyType hierarchy, boolean includeNonExist, boolean includeInherited);

  public void removeChild(ServerGeoObjectIF sgo, ServerGeoObjectIF child, String hierarchyCode, Date startDate, Date endDate);

  public void removeParent(ServerGeoObjectIF sgo, ServerGeoObjectIF parent, ServerHierarchyType hierarchyType, Date startDate, Date endDate);

  public GeoObject toGeoObject(ServerGeoObjectIF sgo, Date date);

  public GeoObject toGeoObject(ServerGeoObjectIF sgo, Date date, boolean includeExternalIds);

  public JsonArray getHierarchiesForGeoObject(ServerGeoObjectIF sgo, Date date);

  public GeoObjectOverTime toGeoObjectOverTime(ServerGeoObjectIF sgo);

  public GeoObjectOverTime toGeoObjectOverTime(ServerGeoObjectIF sgo, boolean generateUid);

  public GeoObjectOverTime toGeoObjectOverTime(ServerGeoObjectIF sgo, boolean generateUid, ClassifierCache classifierCache);

  public ServerParentTreeNode addChild(ServerGeoObjectIF sgo, ServerGeoObjectIF child, ServerHierarchyType hierarchy);

  public ServerParentTreeNode addChild(ServerGeoObjectIF sgo, ServerGeoObjectIF child, ServerHierarchyType hierarchy, Date startDate, Date endDate);

  public ServerChildTreeNode getChildGeoObjects(ServerGeoObjectIF sgo, ServerHierarchyType hierarchy, String[] childrenTypes, Boolean recursive, Date date);

  public ServerParentTreeNode getParentGeoObjects(ServerGeoObjectIF sgo, ServerHierarchyType hierarchy, String[] parentTypes, Boolean recursive, Boolean includeInherited, Date date);

  public ServerParentTreeNode getParentsForHierarchy(ServerGeoObjectIF sgo, ServerHierarchyType hierarchy, Boolean recursive, Boolean includeInherited, Date date);

  public ServerParentTreeNodeOverTime getParentsOverTime(ServerGeoObjectIF sgo, String[] parentTypes, Boolean recursive, Boolean includeInherited);

  public void setParents(ServerGeoObjectIF sgo, ServerParentTreeNodeOverTime parentsOverTime);

  public void removeAllEdges(ServerGeoObjectIF sgo, ServerHierarchyType hierarchyType);

  public ServerParentTreeNode addParent(ServerGeoObjectIF sgo, ServerGeoObjectIF parent, ServerHierarchyType hierarchyType);

  public ValueOverTimeCollection getParentCollection(ServerGeoObjectIF sgo, ServerHierarchyType hierarchyType);

  public SortedSet<EdgeObject> setParentCollection(ServerGeoObjectIF sgo, ServerHierarchyType hierarchyType, ValueOverTimeCollection votc);

  public ServerParentTreeNode addParent(ServerGeoObjectIF sgo, ServerGeoObjectIF parent, ServerHierarchyType hierarchyType, Date startDate, Date endDate);

  /**
   * Adds an edge, bypassing all validation (for performance reasons). Be
   * careful with this method!! You probably want to call addChild or addParent
   * instead.
   */
  public EdgeObject addParentRaw(ServerGeoObjectIF sgo, VertexObject parent, MdEdgeDAOIF mdEdge, Date startDate, Date endDate);

  public Map<String, LocationInfo> getAncestorMap(ServerGeoObjectIF sgo, ServerHierarchyType hierarchy, List<ServerGeoObjectType> parents);

  public JsonObject hasDuplicateLabel(Date date, String typeCode, String code, String label);

  public JsonArray getBusinessObjects(String typeCode, String code, String businessTypeCode);

  public List<BusinessObject> getBusinessObjects(VertexServerGeoObject object);

  public List<BusinessObject> getBusinessObjects(VertexServerGeoObject object, BusinessType type);

}