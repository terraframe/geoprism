/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service.business;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.db.record.ORecordElement;
import com.orientechnologies.orient.core.exception.ORecordNotFoundException;
import com.orientechnologies.orient.core.exception.OSerializationException;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.ORecord;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.record.impl.OBlob;
import com.orientechnologies.orient.core.record.impl.ODocumentHelper;
import com.orientechnologies.orient.core.serialization.OSerializableStream;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.storage.OStorage.LOCKING_STRATEGY;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.orientdb.ResultSetConverter;

import net.geoprism.registry.graph.AttributeType;
import net.geoprism.registry.graph.AttributeValue;
import net.geoprism.registry.graph.GeoVertex;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.graph.VertexServerGeoObject;

public class VertexAndEdgeResultSetConverter extends ResultSetConverter
{
  public static final String VERTEX_PREFIX = "v";
  
  public static final String ATTR_PREFIX = "attr";
  
  public VertexAndEdgeResultSetConverter()
  {
    super(VertexObjectDAO.class);
  }
  
  @Override
  public VertexAndEdge convert(GraphRequest request, Object result)
  {
    final OResult oresult = (OResult) result;
    
    final String geoObjectOid = oresult.getProperty(VERTEX_PREFIX + ".oid");
    final String attrOid = oresult.getProperty(ATTR_PREFIX + ".oid");
    final String edgeClass = oresult.getProperty("edgeClass");
    final String edgeOid = oresult.getProperty("edgeOid");
    
    if (edgeClass.equals("search_link_default") || (geoObjectOid == null && attrOid == null)) return null;
    
    ResultPrefixWrapper vwrapper = new ResultPrefixWrapper(oresult, VERTEX_PREFIX + ".");
    final VertexObject goVertex = (VertexObject) super.convert(request, vwrapper);
    
    ResultPrefixWrapper attrWrapper = new ResultPrefixWrapper(oresult, ATTR_PREFIX + ".");
    final VertexObject attrVertex = (VertexObject) super.convert(request, attrWrapper);
    
    return new VertexAndEdge(goVertex, attrVertex, geoObjectOid, edgeClass, edgeOid);
  }
  
  public static class OElementPrefixWrapper implements OElement
  {
    private String propertyPrefix;
    
    private OElement oelement;
    
    public OElementPrefixWrapper(OElement oelement, String propertyPrefix)
    {
      this.propertyPrefix = propertyPrefix;
      this.oelement = oelement;
    }

    @Override
    public boolean detach()
    {
      return oelement.detach();
    }

    @Override
    public <RET extends ORecord> RET reset()
    {
      return oelement.reset();
    }

    @Override
    public <RET extends ORecord> RET unload()
    {
      return oelement.unload();
    }

    @Override
    public <RET extends ORecord> RET clear()
    {
      return oelement.clear();
    }

    @Override
    public <RET extends ORecord> RET copy()
    {
      return oelement.copy();
    }

    @Override
    public ORID getIdentity()
    {
      // If we fetch the RID in this fashion then OrientDB will cache the document and corrupt subsequent requests
//      OVertexDocument doc = (OVertexDocument) getProperty("@rid");
//      ORID ident = doc.getIdentity();
//      doc.unload();
//      doc.setDirty();
//      return ident;
      
      return (ORID) ODocumentHelper.getIdentifiableValue(this, propertyPrefix + "@rid");
    }

    @Override
    public int getVersion()
    {
      return oelement.getVersion();
    }

    @Override
    public ODatabaseDocument getDatabase()
    {
      return oelement.getDatabase();
    }

    @Override
    public boolean isDirty()
    {
      return oelement.isDirty();
    }

    @Override
    public <RET extends ORecord> RET load() throws ORecordNotFoundException
    {
      return oelement.load();
    }

    @Override
    public <RET extends ORecord> RET reload() throws ORecordNotFoundException
    {
      return oelement.reload();
    }

    @Override
    public <RET extends ORecord> RET reload(String fetchPlan, boolean ignoreCache, boolean force) throws ORecordNotFoundException
    {
      return oelement.reload();
    }

    @Override
    public <RET extends ORecord> RET save()
    {
      return oelement.save();
    }

    @Override
    public <RET extends ORecord> RET save(String iCluster)
    {
      return oelement.save(iCluster);
    }

    @Override
    public <RET extends ORecord> RET save(boolean forceCreate)
    {
      return oelement.save(forceCreate);
    }

    @Override
    public <RET extends ORecord> RET save(String iCluster, boolean forceCreate)
    {
      return oelement.save(iCluster, forceCreate);
    }

    @Override
    public <RET extends ORecord> RET delete()
    {
      return oelement.delete();
    }

    @Override
    public <RET extends ORecord> RET fromJSON(String iJson)
    {
      return oelement.fromJSON(iJson);
    }

    @Override
    public String toJSON()
    {
      return oelement.toJSON();
    }

    @Override
    public String toJSON(String iFormat)
    {
      return oelement.toJSON(iFormat);
    }

    @Override
    public int getSize()
    {
      return oelement.getSize();
    }

    @Override
    public STATUS getInternalStatus()
    {
      return oelement.getInternalStatus();
    }

    @Override
    public void setInternalStatus(STATUS iStatus)
    {
      oelement.setInternalStatus(iStatus);
    }

    @Override
    public <RET> RET setDirty()
    {
      return oelement.setDirty();
    }

    @Override
    public void setDirtyNoChanged()
    {
      oelement.setDirtyNoChanged();
    }

    @Override
    public ORecordElement getOwner()
    {
      return oelement.getOwner();
    }

    @Override
    public <T extends ORecord> T getRecord()
    {
      return oelement.getRecord();
    }

    @Override
    public void lock(boolean iExclusive)
    {
      oelement.lock(iExclusive);
    }

    @Override
    public boolean isLocked()
    {
      return oelement.isLocked();
    }

    @Override
    public LOCKING_STRATEGY lockingStrategy()
    {
      return oelement.lockingStrategy();
    }

    @Override
    public void unlock()
    {
      oelement.unlock();
    }

    @Override
    public int compareTo(OIdentifiable o)
    {
      return oelement.compareTo(o);
    }

    @Override
    public int compare(OIdentifiable o1, OIdentifiable o2)
    {
      return oelement.compare(o1, o2);
    }

    @Override
    public byte[] toStream() throws OSerializationException
    {
      return oelement.toStream();
    }

    @Override
    public OSerializableStream fromStream(byte[] iStream) throws OSerializationException
    {
      return oelement.fromStream(iStream);
    }

    @Override
    public Set<String> getPropertyNames()
    {
      return oelement.getPropertyNames().stream().map(n -> n.replace(propertyPrefix, "")).collect(Collectors.toSet());
    }

    @Override
    public <RET> RET getProperty(String name)
    {
      return oelement.getProperty(propertyPrefix + name);
    }

    @Override
    public boolean hasProperty(String propertyName)
    {
      return oelement.hasProperty(propertyPrefix + propertyName);
    }

    @Override
    public void setProperty(String name, Object value)
    {
      oelement.setProperty(propertyPrefix + name, value);
    }

    @Override
    public void setProperty(String name, Object value, OType... fieldType)
    {
      oelement.setProperty(propertyPrefix + name, value, fieldType);
    }

    @Override
    public <RET> RET removeProperty(String name)
    {
      return oelement.removeProperty(propertyPrefix + name);
    }

    @Override
    public Optional<OVertex> asVertex()
    {
      return oelement.asVertex();
    }

    @Override
    public Optional<OEdge> asEdge()
    {
      return oelement.asEdge();
    }

    @Override
    public boolean isVertex()
    {
      return oelement.isVertex();
    }

    @Override
    public boolean isEdge()
    {
      return oelement.isEdge();
    }

    @Override
    public Optional<OClass> getSchemaType()
    {
      return oelement.getSchemaType();
    }
  }
  
  public static class ResultPrefixWrapper implements OResult
  {
    private String propertyPrefix;
    
    private OResult oresult;
    
    public ResultPrefixWrapper(OResult oresult, String propertyPrefix)
    {
      this.propertyPrefix = propertyPrefix;
      this.oresult = oresult;
    }

    @Override
    public <T> T getProperty(String name)
    {
      return oresult.getProperty(propertyPrefix + name);
    }

    @Override
    public OElement getElementProperty(String name)
    {
      return oresult.getElementProperty(name);
    }

    @Override
    public OVertex getVertexProperty(String name)
    {
      return oresult.getVertexProperty(name);
    }

    @Override
    public OEdge getEdgeProperty(String name)
    {
      return oresult.getEdgeProperty(name);
    }

    @Override
    public OBlob getBlobProperty(String name)
    {
      return oresult.getBlobProperty(name);
    }

    @Override
    public Set<String> getPropertyNames()
    {
      return oresult.getPropertyNames().stream().map(n -> n.replace(propertyPrefix, "")).collect(Collectors.toSet());
    }

    @Override
    public Optional<ORID> getIdentity()
    {
      // If we fetch the RID in this fashion then OrientDB will cache the document and corrupt subsequent requests
//      OVertexDocument doc = (OVertexDocument) getProperty("@rid");
//      ORID ident = doc.getIdentity();
//      doc.unload();
//      doc.setDirty();
//      return Optional.of(ident);
      
      return Optional.of((ORID) ODocumentHelper.getIdentifiableValue(this.toElement(), propertyPrefix + "@rid"));
    }

    @Override
    public boolean isElement()
    {
      return oresult.isElement();
    }

    @Override
    public Optional<OElement> getElement()
    {
      Optional<OElement> op = oresult.getElement();
      
      if (op.isPresent())
      {
        return Optional.of(new OElementPrefixWrapper(op.get(), propertyPrefix));
      }
      
      return op;
    }

    @Override
    public OElement toElement()
    {
      return new OElementPrefixWrapper(oresult.toElement(), propertyPrefix);
    }

    @Override
    public boolean isBlob()
    {
      return oresult.isBlob();
    }

    @Override
    public Optional<OBlob> getBlob()
    {
      return oresult.getBlob();
    }

    @Override
    public Optional<ORecord> getRecord()
    {
      return oresult.getRecord();
    }

    @Override
    public boolean isProjection()
    {
      return oresult.isProjection();
    }

    @Override
    public Object getMetadata(String key)
    {
      return oresult.getMetadata(key);
    }

    @Override
    public Set<String> getMetadataKeys()
    {
      return oresult.getMetadataKeys();
    }

    @Override
    public boolean hasProperty(String varName)
    {
      return oresult.hasProperty(propertyPrefix + varName);
    }
  }
  
  public static class VertexAndEdge
  {
    public String edgeOid;
    
    public String edgeClass;
    
    public VertexObject goVertex;
    
    public VertexObject attrVertex;

    public VertexAndEdge(VertexObject goVertex, VertexObject attrVertex, String geoObjectOid, String edgeClass, String edgeOid)
    {
      super();
      this.edgeOid = edgeOid;
      this.edgeClass = edgeClass;
      this.goVertex = goVertex;
      this.attrVertex = attrVertex;
    }
  }
  
  public static class GeoObjectAndEdge
  {
    public String edgeOid;
    
    public String edgeClass;
    
    public ServerGeoObjectIF geoObject;

    public GeoObjectAndEdge(ServerGeoObjectIF geoObject, String edgeClass, String edgeOid)
    {
      super();
      this.edgeOid = edgeOid;
      this.edgeClass = edgeClass;
      this.geoObject = geoObject;
    }
  }
  
  public static List<GeoObjectAndEdge> convertResults(List<VertexAndEdge> results, Date date)
  {
    VertexAndEdge previous = null;
    List<VertexObject> currentAttributes = new LinkedList<>();
    List<GeoObjectAndEdge> list = new LinkedList<GeoObjectAndEdge>();

    for (VertexAndEdge result : results)
    {
      if (previous != null && !(previous.goVertex.getOid().equals(result.goVertex.getOid()) && previous.edgeOid.equals(result.edgeOid)))
      {
        MdVertexDAOIF mdVertex = (MdVertexDAOIF) previous.goVertex.getMdClass();
        ServerGeoObjectType type = ServerGeoObjectType.get(mdVertex);

        Map<String, List<VertexObject>> nodeMap = currentAttributes.stream().collect(Collectors.groupingBy(v -> {
          return (String) v.getObjectValue(AttributeValue.ATTRIBUTENAME);
        }));

        VertexServerGeoObject vsgo = new VertexServerGeoObject(type, previous.goVertex, nodeMap, date);
        list.add(new GeoObjectAndEdge(vsgo, previous.edgeClass, previous.edgeOid));
        currentAttributes = new LinkedList<>();
      }
      
      currentAttributes.add(result.attrVertex);
      
      previous = result;
    }

    if (previous != null)
    {
      MdVertexDAOIF mdVertex = (MdVertexDAOIF) previous.goVertex.getMdClass();
      ServerGeoObjectType type = ServerGeoObjectType.get(mdVertex);

      Map<String, List<VertexObject>> nodeMap = currentAttributes.stream().collect(Collectors.groupingBy(v -> {
        return (String) v.getObjectValue(AttributeValue.ATTRIBUTENAME);
      }));

      VertexServerGeoObject vsgo = new VertexServerGeoObject(type, previous.goVertex, nodeMap, date);
      list.add(new GeoObjectAndEdge(vsgo, previous.edgeClass, previous.edgeOid));
    }

    return list;
  }
  
  public static String geoVertexColumns(MdGraphClassDAOIF mdClass)
  {
    Set<String> columns = new HashSet<String>();
    columns.add(VERTEX_PREFIX + ".@class");
    columns.add(VERTEX_PREFIX + ".@rid");
    
    // GeoVertex
    for (String column : new String[] { GeoVertex.SEQ, GeoVertex.CREATEDATE, GeoVertex.LASTUPDATEDATE, GeoVertex.OID })
    {
      columns.add(VERTEX_PREFIX + "." + column);
    }
    
    List<? extends MdAttributeConcreteDAOIF> mdAttrs = mdClass.definesAttributes();
    for(MdAttributeConcreteDAOIF mdAttr : mdAttrs)
    {
      columns.add(VERTEX_PREFIX + "." + mdAttr.getColumnName());
    }
    
    return StringUtils.join(columns, ", ");
  }
  
  public static String geoVertexAttributeColumns(List<net.geoprism.registry.graph.AttributeType> types)
  {
    Set<String> columns = new HashSet<String>();
    columns.add(ATTR_PREFIX + ".@class");
    columns.add(ATTR_PREFIX + ".@rid");
    
    // AttributeValue
    for (String column : new String[] { AttributeValue.SEQ, AttributeValue.OID, AttributeValue.ATTRIBUTENAME, AttributeValue.STARTDATE, AttributeValue.ENDDATE })
    {
      columns.add(ATTR_PREFIX + "." + column);
    }
    
    for (net.geoprism.registry.graph.AttributeType at : types)
    {
      for (MdAttributeDAOIF mdAttr : at.getStrategy().getValueAttributes())
      {
        columns.add(ATTR_PREFIX + "." + mdAttr.getColumnName());
      }
    }
    
    return StringUtils.join(columns, ", ");
  }
}
