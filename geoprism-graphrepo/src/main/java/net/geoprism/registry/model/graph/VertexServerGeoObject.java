/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.model.graph;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.dataaccess.ValueOverTimeDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runwaysdk.Pair;
import com.runwaysdk.business.graph.EdgeObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTimeCollection;
import com.runwaysdk.dataaccess.metadata.graph.MdGraphClassDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.localization.LocalizationFacade;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.metadata.MdVertex;
import com.runwaysdk.system.metadata.MdVertexQuery;

import net.geoprism.ontology.Classifier;
import net.geoprism.registry.DateFormatter;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.geoobject.ValueOutOfRangeException;
import net.geoprism.registry.graph.AttributeLocalType;
import net.geoprism.registry.graph.AttributeTermType;
import net.geoprism.registry.graph.AttributeType;
import net.geoprism.registry.graph.AttributeValue;
import net.geoprism.registry.graph.GeoVertex;
import net.geoprism.registry.graph.GeoVertexSynonym;
import net.geoprism.registry.model.AbstractServerGeoObject;
import net.geoprism.registry.model.AttributeState;
import net.geoprism.registry.model.EdgeConstant;
import net.geoprism.registry.model.GeoObjectMetadata;
import net.geoprism.registry.model.GraphType;
import net.geoprism.registry.model.LocationInfo;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerGraphNode;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.service.request.ServiceFactory;

public class VertexServerGeoObject extends AbstractServerGeoObject implements ServerGeoObjectIF, LocationInfo, VertexComponent
{

  private static final Logger logger = LoggerFactory.getLogger(VertexServerGeoObject.class);

  public static class EdgeComparator implements Comparator<EdgeObject>, Serializable
  {
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(EdgeObject o1, EdgeObject o2)
    {
      Date d1 = o1.getObjectValue(GeoVertex.START_DATE);
      Date d2 = o2.getObjectValue(GeoVertex.START_DATE);

      return d1.compareTo(d2);
    }
  }

  protected ServerGeoObjectType         type;

  protected VertexObject                vertex;

  // Current state of values. May not have been applied to the database.
  protected Map<String, AttributeState> valueNodeMap;

  protected Date                        date;

  public VertexServerGeoObject(ServerGeoObjectType type, VertexObject vertex, Map<String, List<VertexObject>> valueNodeMap)
  {
    this(type, vertex, valueNodeMap, null);
  }

  public VertexServerGeoObject(ServerGeoObjectType type, VertexObject vertex, Map<String, List<VertexObject>> valueNodeMap, Date date)
  {
    MdVertexDAOIF actualVertexType = (MdVertexDAOIF) vertex.getMdClass();
    ServerGeoObjectType actualType = ServerGeoObjectType.get(actualVertexType);

    this.type = actualType;
    this.vertex = vertex;

    this.valueNodeMap = valueNodeMap.entrySet().stream().map(entry -> {

      Optional<AttributeType> attribute = type.getAttribute(entry.getKey());

      if (attribute.isPresent())
      {
        return new AttributeState(attribute.get(), entry.getValue());
      }

      return null;
    }).filter(t -> t != null).collect(Collectors.toMap(s -> s.getAttributeType().getCode(), s -> s));

    this.date = date;
  }

  @Override
  @Transaction
  public void apply()
  {
    // TODO: HEADS UP: Add a version check to ensure this object is current,
    // otherwise the value node map may create duplicates
    this.vertex.apply();

    this.valueNodeMap.forEach((attributeName, state) -> {
      state.persit(this.vertex);

      // TODO: HEADS UP: Handle rollback of object on persist failure
      // Only clear the state after the transaction has passed
      state.clear();
    });

  }

  @Override
  @Transaction
  public void delete()
  {
    this.valueNodeMap.forEach((attributeName, state) -> {
      state.delete();

      // TODO: HEADS UP: Handle rollback of object on persist failure
      // Only clear the state after the transaction has passed
      state.clear();
    });

    this.vertex.delete();
  }

  @Override
  public boolean isNew()
  {
    return this.vertex.isNew();
  }

  public String getDBClassName()
  {
    return ( (MdVertexDAOIF) this.vertex.getMdClass() ).getDBClassName();
  }

  public ServerGeoObjectType getType()
  {
    return type;
  }

  public void setType(ServerGeoObjectType type)
  {
    this.type = type;
  }

  public VertexObject getVertex()
  {
    return vertex;
  }

  public void setVertex(VertexObject vertex)
  {
    this.vertex = vertex;
  }

  @Override
  public Date getCreateDate()
  {
    return this.getValue(GeoVertex.CREATEDATE);
  }

  @Override
  public Date getLastUpdateDate()
  {
    return this.getValue(GeoVertex.LASTUPDATEDATE);
  }

  @Override
  public void setDate(Date date)
  {
    this.date = date;
  }

  public Date getDate()
  {
    return date;
  }

  @Override
  public void setCode(String code)
  {
    if (code != null)
    {
      code = code.trim();
      code = code.replaceAll("\\s+", "");
    }

    final String value = code;

    this.type.getAttribute(DefaultAttribute.CODE.getName()).ifPresent( ( attr -> {
      attr.getStrategy().setValue(this.vertex, this.valueNodeMap, value, null, null);
    } ));
  }

  @Override
  public void setGeometry(Geometry geometry)
  {
    this.setGeometry(geometry, this.date, this.date);
  }

  @Override
  public void setGeometry(Geometry geometry, Date startDate, Date endDate)
  {
    this.type.getAttribute(DefaultAttribute.GEOMETRY.getName()).ifPresent( ( attr -> {
      attr.getStrategy().setValue(this.vertex, this.valueNodeMap, geometry, startDate, endDate);
    } ));
  }

  @Override
  public void setExists(Boolean value)
  {
    this.setExists(value, this.date, this.date);
  }

  @Override
  public void setExists(Boolean value, Date startDate, Date endDate)
  {
    this.type.getAttribute(DefaultAttribute.EXISTS.getName()).ifPresent( ( attr -> {
      attr.getStrategy().setValue(this.vertex, this.valueNodeMap, value == null ? Boolean.FALSE : value, startDate, endDate);
    } ));
  }

  @Override
  public Boolean getExists()
  {
    return this.getExists(this.date);
  }

  public Boolean getExists(Date date)
  {
    Boolean value = this.getValue(DefaultAttribute.EXISTS.getName(), date);

    return value == null ? Boolean.FALSE : value;
  }

  @Override
  public void setUid(String uid)
  {
    this.type.getAttribute(DefaultAttribute.UID.getName()).ifPresent( ( attr -> {
      attr.getStrategy().setValue(this.vertex, this.valueNodeMap, uid, null, null);
    } ));
  }

  @Override
  public void setDisplayLabel(LocalizedValue value)
  {
    this.setDisplayLabel(value, this.date, this.date);
  }

  @Override
  public void setDisplayLabel(LocalizedValue value, Date startDate, Date endDate)
  {
    this.setValue(DefaultAttribute.DISPLAY_LABEL.getName(), value, startDate, endDate);
  }

  public boolean existsAtRange(Date startDate, Date endDate)
  {
    ValueOverTimeCollection votc = this.getValuesOverTime(DefaultAttribute.EXISTS.getName());

    if (startDate == null) // Null is treated as "latest"
    {
      if (votc.size() > 0)
      {
        return (boolean) votc.last().getValue();
      }
      else
      {
        return false;
      }
    }

    if (endDate == null)
    {
      endDate = ValueOverTime.INFINITY_END_DATE;
    }

    for (ValueOverTime vot : votc)
    {
      if (vot.getValue() instanceof Boolean && ( (Boolean) vot.getValue() ))
      {
        if ( ( vot.getStartDate() != null && vot.between(startDate) ) && ( vot.getEndDate() != null && vot.between(endDate) ))
        {
          return true;
        }
      }
    }

    return false;
  }

  public void enforceAttributeSetWithinRange(String geoObjectLabel, String attributeLabel, Date startDate, Date endDate)
  {
    if (!this.existsAtRange(startDate, endDate))
    {
      final SimpleDateFormat format = ValueOverTimeDTO.getTimeFormatter();

      ValueOutOfRangeException ex = new ValueOutOfRangeException();
      ex.setGeoObject(geoObjectLabel);
      ex.setAttribute(attributeLabel);

      if (startDate != null)
      {
        ex.setStartDate(format.format(startDate));
      }
      else
      {
        ex.setStartDate("null");
      }

      if (ValueOverTime.INFINITY_END_DATE.equals(endDate))
      {
        ex.setEndDate(LocalizationFacade.localize("changeovertime.present"));
      }
      else
      {
        ex.setEndDate(format.format(endDate));
      }

      throw ex;
    }
  }

  @Override
  public String getLabel()
  {
    LocalizedValue lv = this.getDisplayLabel();

    return lv.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE);
  }

  @Override
  public void setValue(String attributeName, Object value)
  {
    AttributeType at = this.type.getAttribute(attributeName).orElse(null);

    if (at instanceof AttributeLocalType)
    {
      RegistryLocalizedValueConverter.populate(this.vertex, attributeName, (LocalizedValue) value, this.date, null);
    }
    else
    {
      this.vertex.setValue(attributeName, value, this.date, this.date);
    }
  }

  @Override
  public void setValue(String attributeName, Object value, Date startDate, Date endDate)
  {
    this.type.getAttribute(attributeName).ifPresent( ( attr -> {
      attr.getStrategy().setValue(this.vertex, this.valueNodeMap, value, startDate, endDate);
    } ));

  }

  @Override
  public String getLabel(Locale locale)
  {
    LocalizedValue lv = this.getDisplayLabel();

    return lv.getValue(locale);
  }

  public Map<String, ServerHierarchyType> getHierarchyTypeMap(String[] relationshipTypes)
  {
    throw new UnsupportedOperationException();
  }

  public MdVertexDAOIF getMdClass()
  {
    return (MdVertexDAOIF) this.vertex.getMdClass();
  }

  @Override
  public String getCode()
  {
    return (String) this.getValue(DefaultAttribute.CODE.getName());
  }

  @Override
  public String getUid()
  {
    return (String) this.getValue(DefaultAttribute.UID.getName());
  }

  @Override
  public String getRunwayId()
  {
    return this.vertex.getOid();
  }

  @Override
  public boolean isModified(String attributeName)
  {
    Optional<AttributeType> optional = this.type.getAttribute(attributeName);

    if (optional.isPresent())
    {
      AttributeType attributeType = optional.get();

      return attributeType.getStrategy().isModified(attributeType, valueNodeMap);
    }

    return false;
  }

  @Override
  public boolean hasAttribute(String attributeName)
  {
    return this.type.getAttribute(attributeName).isPresent();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getValue(String attributeName)
  {
    Optional<AttributeType> optional = this.type.getAttribute(attributeName);

    if (optional.isPresent())
    {
      AttributeType attributeType = optional.get();

      Object value = !attributeType.getIsChangeOverTime() ? attributeType.getStrategy().getValue(vertex, valueNodeMap, null) : this.getMostRecentValue(attributeName);

      if (value != null && attributeType instanceof AttributeTermType)
      {
        return (T) Classifier.get((String) value);
      }

      return (T) value;
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getValue(String attributeName, Date date)
  {
    AttributeType at = this.type.getAttribute(attributeName).orElse(null);

    if (at != null)
    {
      Object value = at.getStrategy().getValue(this.vertex, this.valueNodeMap, date);

      if (value != null && at instanceof AttributeTermType)
      {
        return (T) Classifier.get((String) value);
      }

      return (T) value;
    }

    return null;
  }

  @Override
  public ValueOverTimeCollection getValuesOverTime(String attributeName)
  {
    Optional<AttributeType> attribute = this.type.getAttribute(attributeName);

    if (attribute.isPresent())
    {
      return attribute.get().getStrategy().getValueOverTimeCollection(this.vertex, this.valueNodeMap);
    }

    return new ValueOverTimeCollection();
  }

  @Override
  public void setValuesOverTime(String attributeName, ValueOverTimeCollection collection)
  {
    this.type.getAttribute(attributeName).ifPresent( ( attr -> {
      attr.getStrategy().setValuesOverTime(this.vertex, this.valueNodeMap, collection);
    } ));

  }

  public ValueOverTime buildDefaultExists()
  {
    if (this.getValuesOverTime(DefaultAttribute.EXISTS.getName()).size() != 0)
    {
      return null;
    }

    Collection<AttributeType> attributes = type.getAttributeMap().values();

    String[] shouldNotProcessArray = new String[] { DefaultAttribute.UID.getName(), DefaultAttribute.SEQUENCE.getName(), DefaultAttribute.LAST_UPDATE_DATE.getName(), DefaultAttribute.CREATE_DATE.getName(), DefaultAttribute.TYPE.getName(), DefaultAttribute.EXISTS.getName() };

    Date startDate = null;
    Date endDate = null;

    for (AttributeType attribute : attributes)
    {
      boolean shouldProcess = !ArrayUtils.contains(shouldNotProcessArray, attribute.getCode());

      if (shouldProcess && attribute.getIsChangeOverTime())
      {
        ValueOverTimeCollection votc = this.getValuesOverTime(attribute.getCode());

        for (ValueOverTime vot : votc)
        {
          if (startDate == null || startDate.after(vot.getStartDate()))
          {
            startDate = vot.getStartDate();
          }

          if (endDate == null || endDate.before(vot.getEndDate()))
          {
            endDate = vot.getEndDate();
          }
        }
      }
    }

    if (startDate != null && endDate != null)
    {
      return new ValueOverTime(startDate, endDate, Boolean.TRUE);
    }
    else
    {
      return null;
    }
  }

  @Override
  public String bbox(Date date)
  {
    Geometry geometry = this.getGeometry(date);

    if (geometry != null)
    {
      try
      {
        Envelope e = geometry.getEnvelopeInternal();

        JSONArray bboxArr = new JSONArray();
        bboxArr.put(e.getMinX());
        bboxArr.put(e.getMinY());
        bboxArr.put(e.getMaxX());
        bboxArr.put(e.getMaxY());

        return bboxArr.toString();
      }
      catch (JSONException ex)
      {
        throw new ProgrammingErrorException(ex);
      }
    }

    return null;
  }

  protected Date calculateDateMinusOneDay(Date source)
  {
    LocalDate localEnd = source.toInstant().atZone(ZoneId.of("Z")).toLocalDate().minusDays(1);
    Instant instant = localEnd.atStartOfDay().atZone(ZoneId.of("Z")).toInstant();

    return Date.from(instant);
  }

  protected Date calculateDatePlusOneDay(Date source)
  {
    LocalDate localEnd = source.toInstant().atZone(ZoneId.of("Z")).toLocalDate().plusDays(1);
    Instant instant = localEnd.atStartOfDay().atZone(ZoneId.of("Z")).toInstant();

    return Date.from(instant);
  }

  protected Object getMostRecentValue(String attributeName)
  {
    ValueOverTimeCollection votc = this.getValuesOverTime(attributeName);

    if (votc.size() > 0)
    {
      return votc.get(votc.size() - 1).getValue();
    }
    else
    {
      return null;
    }
  }

  public LocalizedValue getDisplayLabel()
  {
    return this.getValueLocalized(DefaultAttribute.DISPLAY_LABEL.getName());
  }

  public LocalizedValue getDisplayLabel(Date date)
  {
    return this.getValueLocalized(DefaultAttribute.DISPLAY_LABEL.getName(), date);
  }

  public LocalizedValue getValueLocalized(String attributeName)
  {
    if (this.date == null)
    {
      return (LocalizedValue) this.getMostRecentValue(attributeName);
    }
    else
    {
      return this.getValue(attributeName, this.date);
    }
  }

  public LocalizedValue getValueLocalized(String attributeName, Date date)
  {
    return this.getValue(attributeName, date);
  }

  public Geometry getGeometry(Date date)
  {
    return this.getValue(DefaultAttribute.GEOMETRY.getName(), date);
  }

  public Geometry getGeometry()
  {
    return this.getGeometry(this.date);
  }

  public boolean exists(ServerGeoObjectIF parent, ServerHierarchyType hierarchyType, Date startDate, Date endDate)
  {
    EdgeObject edge = this.getEdge(parent, hierarchyType, startDate, endDate);

    return ( edge != null );
  }

  public EdgeObject getEdge(ServerGeoObjectIF parent, ServerHierarchyType hierarchyType, Date startDate, Date endDate)
  {
    String statement = "SELECT FROM " + hierarchyType.getObjectEdge().getDBClassName();
    statement += " WHERE out = :parent";
    statement += " AND in = :child";

    if (startDate != null)
    {
      statement += " AND startDate = :startDate";
    }

    if (endDate != null)
    {
      statement += " AND endDate = :endDate";
    }

    GraphQuery<EdgeObject> query = new GraphQuery<EdgeObject>(statement);
    query.setParameter("parent", ( (VertexComponent) parent ).getVertex().getRID());
    query.setParameter("child", this.getVertex().getRID());

    if (startDate != null)
    {
      query.setParameter("startDate", startDate);
    }

    if (endDate != null)
    {
      query.setParameter("endDate", endDate);
    }

    return query.getSingleResult();
  }

  public SortedSet<EdgeObject> getEdges(ServerHierarchyType hierarchyType)
  {
    TreeSet<EdgeObject> set = new TreeSet<EdgeObject>(new EdgeComparator());

    String statement = "SELECT expand(inE('" + hierarchyType.getObjectEdge().getDBClassName() + "'))";
    statement += " FROM :child";

    GraphQuery<EdgeObject> query = new GraphQuery<EdgeObject>(statement);
    query.setParameter("child", this.getVertex().getRID());

    set.addAll(query.getResults());

    return set;
  }

  public String addSynonym(String label)
  {
    GeoVertexSynonym synonym = new GeoVertexSynonym();
    synonym.setValue(GeoVertexSynonym.LABEL, label);
    synonym.apply();

    this.vertex.addChild(synonym, GeoVertex.HAS_SYNONYM).apply();

    return synonym.getOid();
  }

  public static List<VertexObject> getByRID(ServerGeoObjectType type, Object rid)
  {
    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
    statement.append(" SELECT FROM :rid");
    statement.append(")");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("rid", rid);

    return query.getResults();
  }

  public static List<VertexObject> getVertex(ServerGeoObjectType type, String uid)
  {
    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
    statement.append(" SELECT FROM " + type.getDBClassName());
    statement.append(" WHERE uid = :uid");
    statement.append(")");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("uid", uid);

    return query.getResults();
  }

  public static List<VertexObject> getVertexByCode(ServerGeoObjectType type, String code)
  {
    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
    statement.append(" SELECT FROM " + type.getDBClassName());
    statement.append(" WHERE code = :code");
    statement.append(")");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("code", code);

    return query.getResults();
  }

  public static VertexObject newInstance(ServerGeoObjectType type)
  {
    VertexObjectDAO dao = VertexObjectDAO.newInstance(type.getMdVertex());

    return VertexObject.instantiate(dao);
  }

  public static Pair<Date, Date> getDataRange(ServerGeoObjectType type)
  {
    final String dbClassName = type.getMdVertex().getDBClassName();

    final Date startDate = new GraphQuery<Date>("SELECT MIN(exists_cot.startDate) FROM " + dbClassName).getSingleResult();
    final Date endDate = new GraphQuery<Date>("SELECT MAX(exists_cot.startDate) FROM " + dbClassName).getSingleResult();

    Date current = DateFormatter.getCurrentDate();

    if (startDate != null && endDate != null)
    {
      if (endDate.before(current))
      {
        return new Pair<Date, Date>(startDate, current);
      }

      return new Pair<Date, Date>(startDate, endDate);
    }

    return null;
  }

  public static boolean hasData(ServerHierarchyType hierarchyType, ServerGeoObjectType childType)
  {
    StringBuilder statement = new StringBuilder();
    statement.append("SELECT COUNT(*) FROM " + hierarchyType.getObjectEdge().getDBClassName());
    statement.append(" WHERE in.@class = :class");
    statement.append(" OR out.@class = :class");

    GraphQuery<Long> query = new GraphQuery<Long>(statement.toString());
    query.setParameter("class", childType.getMdVertex().getDBClassName());

    Long result = query.getSingleResult();

    return ( result != null && result > 0 );
  }

  public static void removeAllEdges(ServerHierarchyType hierarchyType, ServerGeoObjectType childType)
  {
    StringBuilder statement = new StringBuilder();
    statement.append("DELETE EDGE " + hierarchyType.getObjectEdge().getDBClassName());
    statement.append(" WHERE in.@class = :class");
    statement.append(" OR out.@class = :class");

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("class", childType.getMdVertex().getDBClassName());

    GraphDBService service = GraphDBService.getInstance();
    service.command(service.getGraphDBRequest(), statement.toString(), parameters);
  }

  public static String findTypeLabelFromGeoObjectCode(String code, ServerGeoObjectType rootType)
  {
    ServerGeoObjectType type = null;

    try
    {
      type = findTypeOfGeoObjectCode(code, rootType);
    }
    catch (Throwable t)
    {
      logger.error("Error encountered while finding a geoObject of code [" + code + "].", t);
    }

    if (type == null)
    {
      return "?";
    }
    else
    {
      return type.getLabel().getValue();
    }
  }

  /**
   * Finds the ServerGeoObjectType associated with the particular Geo-Object
   * code.
   * 
   * @param rootType
   *          TODO
   * 
   * @return
   */
  public static ServerGeoObjectType findTypeOfGeoObjectCode(String code, ServerGeoObjectType rootType)
  {
    MdVertexDAOIF rootVertex = rootType.getMdVertex();

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT @class FROM " + rootVertex.getDBClassName() + " WHERE code=:code");

    GraphQuery<String> query = new GraphQuery<String>(statement.toString());
    query.setParameter("code", code);

    String className = query.getSingleResult();

    MdVertexQuery mvq = new MdVertexQuery(new QueryFactory());

    mvq.WHERE(mvq.getDbClassName().EQ(className));

    MdVertex mdVertex = mvq.getIterator().getAll().get(0);

    return ServerGeoObjectType.get(MdVertexDAO.get(mdVertex.getOid()));
  }

  public static boolean isCodeAttribute(MdAttributeDAOIF attr)
  {
    String attributeName = attr.definesAttribute();

    return attributeName.equals(DefaultAttribute.CODE.getName()) || attributeName.equals(ElementInfo.KEY) || attributeName.equals(ElementInfo.OID) || attributeName.equals(GeoEntity.GEOID);
  }

  public static String getAttributeLabel(ServerGeoObjectType type, MdAttributeDAOIF attr)
  {
    if (isCodeAttribute(attr))
    {
      return type.getAttribute(DefaultAttribute.CODE.getName()).get().getLocalizedLabel().getValue();
    }

    if (type.getAttribute(attr.definesAttribute()).isPresent())
    {
      return type.getAttribute(attr.definesAttribute()).get().getLocalizedLabel().getValue();
    }

    return attr.getDisplayLabel(Session.getCurrentLocale());
  }

  @Override
  public String toString()
  {
    return GeoObjectMetadata.get().getClassDisplayLabel() + " : " + this.getCode();
  }

  @Override
  public Boolean getInvalid()
  {
    Boolean value = this.getValue(DefaultAttribute.INVALID.getName());
    return value;
  }

  @Override
  public void setInvalid(Boolean invalid)
  {
    this.setValue(DefaultAttribute.INVALID.getName(), invalid);
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null || ! ( obj instanceof VertexServerGeoObject ))
    {
      return false;
    }

    return this.getUid().equals( ( (VertexServerGeoObject) obj ).getUid());
  }

  @Override
  public int hashCode()
  {
    return this.getCode().hashCode();
  }

  /*
   * DIRECT ACYCLIC/UNDIRECTED GRAPH METODS
   */
  @Override
  @Transaction
  public void removeGraphChild(ServerGeoObjectIF child, GraphType type, Date startDate, Date endDate)
  {
    type.getStrategy().removeParent((VertexServerGeoObject) child, this, startDate, endDate);
  }

  @Override
  @Transaction
  public <T extends ServerGraphNode> T addGraphChild(ServerGeoObjectIF child, GraphType type, Date startDate, Date endDate, boolean validate)
  {
    return type.getStrategy().addChild(this, (VertexServerGeoObject) child, startDate, endDate, validate);
  }

  @Override
  @Transaction
  public <T extends ServerGraphNode> T addGraphParent(ServerGeoObjectIF parent, GraphType type, Date startDate, Date endDate, boolean validate)
  {
    return type.getStrategy().addParent(this, (VertexServerGeoObject) parent, startDate, endDate, validate);
  }

  @Override
  public <T extends ServerGraphNode> T getGraphChildren(GraphType type, Boolean recursive, Date date, String boundsWKT, Long skip, Long limit)
  {
    return type.getStrategy().getChildren(this, recursive, date, boundsWKT, skip, limit);
  }

  @Override
  public <T extends ServerGraphNode> T getGraphParents(GraphType type, Boolean recursive, Date date, String boundsWKT, Long skip, Long limit)
  {
    return type.getStrategy().getParents(this, recursive, date, boundsWKT, skip, limit);
  }

  @Override
  public <T extends ServerGraphNode> T getGraphChildren(GraphType type, Boolean recursive, Date date)
  {
    return this.getGraphChildren(type, recursive, date, null, null, null);
  }

  @Override
  public <T extends ServerGraphNode> T getGraphParents(GraphType type, Boolean recursive, Date date)
  {
    return this.getGraphParents(type, recursive, date, null, null, null);
  }

  public static JsonArray getGeoObjectSuggestions(String text, String typeCode, String parentCode, String parentTypeCode, String hierarchyCode, Date startDate, Date endDate)
  {
    final ServerGeoObjectType type = ServerGeoObjectType.get(typeCode);

    ServerHierarchyType ht = hierarchyCode != null ? ServerHierarchyType.get(hierarchyCode) : null;

    final ServerGeoObjectType parentType = ServerGeoObjectType.get(parentTypeCode);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT @class as clazz, @rid, oid, uid, code");

    if (startDate != null && endDate != null)
    {
      statement.append(", last(out('has_value')[attributeName = 'displayLabel'][(:startDate BETWEEN startDate AND endDate)].defaultLocale) AS label");
      statement.append(", last(out('has_value')[attributeName = 'displayLabel'][(:startDate BETWEEN startDate AND endDate)].startDate) AS labelStartDate");
      statement.append(", last(out('has_value')[attributeName = 'displayLabel'][(:startDate BETWEEN startDate AND endDate)].endDate) AS labelEndDate");
      statement.append(", last(out('has_value')[attributeName = 'exists'][(:startDate BETWEEN startDate AND endDate)].startDate) AS existStartDate");
      statement.append(", last(out('has_value')[attributeName = 'exists'][(:startDate BETWEEN startDate AND endDate)].endDate) AS existEndDate");
      statement.append(", last(out('has_value')[attributeName = 'exists'][(:startDate BETWEEN startDate AND endDate)].value) AS existValue");
    }
    else
    {
      statement.append(", last(out('has_value')[attributeName = 'displayLabel'][startDate = max($current.out('has_value')[attributeName = 'displayLabel'].startDate)].defaultLocale) AS label");
      statement.append(", last(out('has_value')[attributeName = 'exists'][startDate = max($current.out('has_value')[attributeName = 'displayLabel'].startDate)].value) AS existValue");
    }

    statement.append(" FROM " + type.getMdVertex().getDBClassName() + " WHERE ");

    statement.append("invalid=false");

    // Must be a child of parent type
    if (parentTypeCode != null && parentTypeCode.length() > 0)
    {
      StringBuilder parentCondition = new StringBuilder();

      parentCondition.append("(@rid in ( TRAVERSE outE('" + ht.getObjectEdge().getDBClassName() + "')");

      if (startDate != null && endDate != null)
      {
        parentCondition.append("[(:startDate BETWEEN startDate AND endDate) AND (:endDate BETWEEN startDate AND endDate)]");
      }

      parentCondition.append(".inV() FROM (select from " + parentType.getMdVertex().getDBClassName() + " where code='" + parentCode + "') )) ");

      statement.append(" AND " + parentCondition.toString());
    }

    statement = new StringBuilder("SELECT * FROM (" + statement.toString() + ") WHERE ");

    List<String> conditions = new ArrayList<String>();

    conditions.add("existValue=true");
    if (startDate != null && endDate != null)
    {
      conditions.add(":startDate BETWEEN labelStartDate AND labelEndDate");
      conditions.add(":startDate BETWEEN existStartDate AND existEndDate");
      conditions.add(":endDate BETWEEN existStartDate AND existEndDate");
    }

    if (text != null && text.length() > 0)
    {
      // TODO : Coalesce?
      conditions.add("(label.toLowerCase() LIKE '%' + :text + '%' OR code.toLowerCase() LIKE '%' + :text + '%')");
    }

    statement.append(StringUtils.join(conditions, " AND "));

    statement.append(" ORDER BY defaultLocale ASC LIMIT 10");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());

    if (startDate != null && endDate != null)
    {
      query.setParameter("startDate", startDate);
      query.setParameter("endDate", endDate);
    }

    if (text != null)
    {
      query.setParameter("text", text.toLowerCase().trim());
    }
    else
    {
      query.setParameter("text", null);
    }

    @SuppressWarnings("unchecked")
    List<HashMap<String, Object>> results = (List<HashMap<String, Object>>) ( (Object) query.getResults() );

    JsonArray array = new JsonArray();

    for (HashMap<String, Object> row : results)
    {
      ServerGeoObjectType rowType = ServerGeoObjectType.get((MdVertexDAOIF) MdGraphClassDAO.getMdGraphClassByTableName((String) row.get("clazz")));

      if (ServiceFactory.getGeoObjectPermissionService().canRead(rowType.getOrganization().getCode(), rowType))
      {
        JsonObject result = new JsonObject();
        result.addProperty("id", (String) row.get("oid"));
        result.addProperty("name", (String) row.get("label"));
        result.addProperty(GeoObject.CODE, (String) row.get("code"));
        result.addProperty(GeoObject.UID, (String) row.get("uid"));
        result.addProperty("typeCode", rowType.getCode());

        array.add(result);
      }
    }

    return array;
  }

  /*
   * public static JsonArray getGeoObjectSuggestions(String text, String
   * typeCode, String parentCode, String parentTypeCode, String hierarchyCode,
   * Date startDate, Date endDate) { final ServerGeoObjectType type =
   * ServerGeoObjectType.get(typeCode);
   * 
   * ServerHierarchyType ht = hierarchyCode != null ?
   * ServerHierarchyType.get(hierarchyCode) : null;
   * 
   * final ServerGeoObjectType parentType =
   * ServerGeoObjectType.get(parentTypeCode);
   * 
   * List<String> conditions = new ArrayList<String>();
   * 
   * StringBuilder statement = new StringBuilder();
   * statement.append("select $filteredLabel,@class as clazz,* from " +
   * type.getMdVertex().getDBClassName() + " ");
   * 
   * statement.append("let $dateLabel = first(displayLabel_cot"); if (startDate
   * != null && endDate != null) { // Intentionally do not filter on end date as
   * per #913 statement.append("[(:startDate BETWEEN startDate AND endDate)]");
   * } statement.append("), "); statement.append("$filteredLabel = " +
   * AbstractVertexRestriction.localize("$dateLabel.value") + " ");
   * 
   * statement.append("where ");
   * 
   * // Must be a child of parent type if (parentTypeCode != null &&
   * parentTypeCode.length() > 0) { StringBuilder parentCondition = new
   * StringBuilder();
   * 
   * parentCondition.append("(@rid in ( TRAVERSE outE('" +
   * ht.getObjectEdge().getDBClassName() + "')");
   * 
   * if (startDate != null && endDate != null) { parentCondition.
   * append("[(:startDate BETWEEN startDate AND endDate) AND (:endDate BETWEEN startDate AND endDate)]"
   * ); }
   * 
   * parentCondition.append(".inV() FROM (select from " +
   * parentType.getMdVertex().getDBClassName() + " where code='" + parentCode +
   * "') )) ");
   * 
   * conditions.add(parentCondition.toString()); }
   * 
   * // Must have display label we expect if (text != null && text.length() > 0)
   * { StringBuilder textCondition = new StringBuilder();
   * 
   * textCondition.append("(displayLabel_cot CONTAINS (");
   * 
   * if (startDate != null && endDate != null) { // Intentionally do not filter
   * on end date as per #913
   * textCondition.append("  (:startDate BETWEEN startDate AND endDate) AND ");
   * }
   * 
   * textCondition.append(AbstractVertexRestriction.localize("value") +
   * ".toLowerCase() LIKE '%' + :text + '%'"); textCondition.append(")");
   * 
   * textCondition.append(" OR code.toLowerCase() LIKE '%' + :text + '%')");
   * 
   * conditions.add(textCondition.toString()); }
   * 
   * // Must not be invalid conditions.add("invalid=false");
   * 
   * // Must exist at date { StringBuilder existCondition = new StringBuilder();
   * 
   * existCondition.append("(exists_cot CONTAINS (");
   * 
   * if (startDate != null && endDate != null) { existCondition.
   * append("  (:startDate BETWEEN startDate AND endDate) AND (:endDate BETWEEN startDate AND endDate) AND "
   * ); }
   * 
   * existCondition.append("value=true"); existCondition.append("))");
   * 
   * conditions.add(existCondition.toString()); }
   * 
   * statement.append(StringUtils.join(conditions, " AND "));
   * 
   * statement.append(" ORDER BY $filteredLabel ASC LIMIT 10");
   * 
   * GraphQuery<VertexObject> query = new
   * GraphQuery<VertexObject>(statement.toString());
   * 
   * if (startDate != null && endDate != null) { query.setParameter("startDate",
   * startDate); query.setParameter("endDate", endDate); }
   * 
   * if (text != null) { query.setParameter("text", text.toLowerCase().trim());
   * } else { query.setParameter("text", null); }
   * 
   * @SuppressWarnings("unchecked") List<HashMap<String, Object>> results =
   * (List<HashMap<String, Object>>) ( (Object) query.getResults() );
   * 
   * JsonArray array = new JsonArray();
   * 
   * for (HashMap<String, Object> row : results) { ServerGeoObjectType rowType =
   * ServerGeoObjectType.get((MdVertexDAOIF)
   * MdGraphClassDAO.getMdGraphClassByTableName((String) row.get("clazz")));
   * 
   * if (ServiceFactory.getGeoObjectPermissionService().canRead(rowType.
   * getOrganization().getCode(), rowType)) { JsonObject result = new
   * JsonObject(); result.addProperty("id", (String) row.get("oid"));
   * result.addProperty("name", (String) row.get("$filteredLabel"));
   * result.addProperty(GeoObject.CODE, (String) row.get("code"));
   * result.addProperty(GeoObject.UID, (String) row.get("uid"));
   * result.addProperty("typeCode", rowType.getCode());
   * 
   * array.add(result); } }
   * 
   * return array; }
   */

  public static boolean hasDuplicateLabel(Date date, String typeCode, String code, String label)
  {
    LocalizedValue localizedValue = LocalizedValue.fromJSON(JsonParser.parseString(label).getAsJsonObject());

    ServerGeoObjectType type = ServerGeoObjectType.get(typeCode);
    MdVertexDAOIF mdVertex = type.getMdVertex();

    String dbClassName = mdVertex.getDBClassName();

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT COUNT(*) FROM " + dbClassName);
    statement.append(" WHERE displayLabel_cot CONTAINS ((");

    List<Entry<String, String>> entrySet = new ArrayList<Entry<String, String>>(localizedValue.getLocaleMap().entrySet());

    for (int i = 0; i < entrySet.size(); i++)
    {
      Entry<String, String> entry = entrySet.get(i);
      String locale = entry.getKey();

      if (i != 0)
      {
        statement.append(" OR ");
      }

      statement.append("value." + locale + " = :" + locale);
    }

    statement.append(") AND :date BETWEEN startDate AND endDate)");

    if (code != null && code.length() > 0)
    {
      statement.append(" AND code != :code");
    }

    GraphQuery<Long> query = new GraphQuery<Long>(statement.toString());
    query.setParameter("date", date);

    entrySet.forEach(entry -> {
      String value = entry.getValue();
      String locale = entry.getKey();

      query.setParameter(locale, value);
    });

    if (code != null && code.length() > 0)
    {
      query.setParameter("code", code);
    }

    return query.getSingleResult() > 0;
  }

  public static List<ServerGeoObjectIF> processTraverseResults(List<VertexObject> results, Date date)
  {
    VertexObject current = null;
    List<VertexObject> currentAttributes = new LinkedList<>();
    MdVertexDAOIF mdGeoVertex = MdVertexDAO.getMdVertexDAO(GeoVertex.CLASS);
    List<ServerGeoObjectIF> list = new LinkedList<ServerGeoObjectIF>();

    for (VertexObject result : results)
    {
      MdVertexDAOIF mdClass = (MdVertexDAOIF) result.getMdClass();
      List<? extends MdVertexDAOIF> superClasses = mdClass.getSuperClasses();
      if (superClasses.contains(mdGeoVertex))
      {
        if (current != null)
        {
          MdVertexDAOIF mdVertex = (MdVertexDAOIF) current.getMdClass();
          ServerGeoObjectType type = ServerGeoObjectType.get(mdVertex);

          Map<String, List<VertexObject>> nodeMap = currentAttributes.stream().collect(Collectors.groupingBy(v -> {
            return (String) v.getObjectValue(AttributeValue.ATTRIBUTENAME);
          }));

          VertexServerGeoObject vsgo = new VertexServerGeoObject(type, current, nodeMap, date);
          list.add(vsgo);
        }
        current = result;
        currentAttributes = new LinkedList<>();
      }
      else
      {
        currentAttributes.add(result);
      }
    }

    if (current != null)
    {
      MdVertexDAOIF mdVertex = (MdVertexDAOIF) current.getMdClass();
      ServerGeoObjectType type = ServerGeoObjectType.get(mdVertex);

      Map<String, List<VertexObject>> nodeMap = currentAttributes.stream().collect(Collectors.groupingBy(v -> {
        return (String) v.getObjectValue(AttributeValue.ATTRIBUTENAME);
      }));

      VertexServerGeoObject vsgo = new VertexServerGeoObject(type, current, nodeMap, date);
      list.add(vsgo);
    }

    return list;
  }


}
