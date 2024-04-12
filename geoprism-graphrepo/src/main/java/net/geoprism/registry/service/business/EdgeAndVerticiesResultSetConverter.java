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
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.orientdb.ResultSetConverter;

import net.geoprism.registry.graph.AttributeValue;
import net.geoprism.registry.graph.GeoVertex;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.graph.VertexServerGeoObject;

public class EdgeAndVerticiesResultSetConverter extends ResultSetConverter
{
  public static final String VERTEX_IN_PREFIX = "in";
  
  public static final String VERTEX_OUT_PREFIX = "out";
  
  public static final String IN_ATTR_PREFIX = "inAttr";
  
  public static final String OUT_ATTR_PREFIX = "outAttr";
  
  public EdgeAndVerticiesResultSetConverter()
  {
    super(VertexObjectDAO.class);
  }
  
  @Override
  public EdgeAndVerticies convert(GraphRequest request, Object result)
  {
    final OResult oresult = (OResult) result;
    
    final String inOid = oresult.getProperty(VERTEX_IN_PREFIX + ".oid");
    final String outOid = oresult.getProperty(VERTEX_OUT_PREFIX + ".oid");
    final String edgeClass = oresult.getProperty("edgeClass");
    final String edgeOid = oresult.getProperty("edgeOid");
    
    if (edgeClass.equals("search_link_default") || (inOid == null && outOid == null)) return null;
    
    VertexObject goInVertex = null;
    VertexObject inAttrVertex = null;
    if (inOid != null)
    {
      ResultPrefixWrapper inVertexWrapper = new ResultPrefixWrapper(oresult, VERTEX_IN_PREFIX + ".");
      goInVertex = (VertexObject) super.convert(request, inVertexWrapper);
      
      ResultPrefixWrapper inAttrWrapper = new ResultPrefixWrapper(oresult, IN_ATTR_PREFIX + ".");
      inAttrVertex = (VertexObject) super.convert(request, inAttrWrapper);
    }
    
    VertexObject goOutVertex = null;
    VertexObject outAttrVertex = null;
    if (outOid != null)
    {
      ResultPrefixWrapper outVertexWrapper = new ResultPrefixWrapper(oresult, VERTEX_OUT_PREFIX + ".");
      goOutVertex = (VertexObject) super.convert(request, outVertexWrapper);
      
      ResultPrefixWrapper outAttrWrapper = new ResultPrefixWrapper(oresult, OUT_ATTR_PREFIX + ".");
      outAttrVertex = (VertexObject) super.convert(request, outAttrWrapper);
    }
    
    return new EdgeAndVerticies(goInVertex, inAttrVertex, goOutVertex, outAttrVertex, edgeClass, edgeOid);
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
  
  public static class EdgeAndVerticies
  {
    public String edgeOid;
    
    public String edgeClass;
    
    public VertexObject inVertex;
    
    public VertexObject inAttrVertex;
    
    public VertexObject outVertex;
    
    public VertexObject outAttrVertex;

    public EdgeAndVerticies(VertexObject inVertex, VertexObject inAttrVertex, VertexObject outVertex, VertexObject outAttrVertex, String edgeClass, String edgeOid)
    {
      super();
      this.edgeOid = edgeOid;
      this.edgeClass = edgeClass;
      this.inVertex = inVertex;
      this.inAttrVertex = inAttrVertex;
      this.outVertex = outVertex;
      this.outAttrVertex = outAttrVertex;
    }
  }
  
  public static class EdgeAndInOut
  {
    public String edgeOid;
    
    public String edgeClass;
    
    public ServerGeoObjectIF in;
    
    public ServerGeoObjectIF out;

    public EdgeAndInOut(ServerGeoObjectIF in, ServerGeoObjectIF out, String edgeClass, String edgeOid)
    {
      super();
      this.edgeOid = edgeOid;
      this.edgeClass = edgeClass;
      this.in = in;
      this.out = out;
    }
  }
  
  public static List<EdgeAndInOut> convertResults(List<EdgeAndVerticies> results, Date date)
  {
    EdgeAndVerticies previous = null;
    List<VertexObject> currentInAttributes = new LinkedList<>();
    List<VertexObject> currentOutAttributes = new LinkedList<>();
    VertexObject currentInVertex = null;
    VertexObject currentOutVertex = null;
    List<EdgeAndInOut> list = new LinkedList<EdgeAndInOut>();

    for (EdgeAndVerticies result : results)
    {
      if (previous != null && !(previous.edgeOid.equals(result.edgeOid)))
      {
        MdVertexDAOIF mdVertex = (MdVertexDAOIF) currentOutVertex.getMdClass();
        ServerGeoObjectType type = ServerGeoObjectType.get(mdVertex);
        Map<String, List<VertexObject>> nodeMap = currentOutAttributes.stream().collect(Collectors.groupingBy(v -> {
          return (String) v.getObjectValue(AttributeValue.ATTRIBUTENAME);
        }));
        VertexServerGeoObject out = new VertexServerGeoObject(type, currentOutVertex, nodeMap, date);
        
        MdVertexDAOIF mdVertex2 = (MdVertexDAOIF) currentInVertex.getMdClass();
        ServerGeoObjectType type2 = ServerGeoObjectType.get(mdVertex2);
        Map<String, List<VertexObject>> nodeMap2 = currentInAttributes.stream().collect(Collectors.groupingBy(v -> {
          return (String) v.getObjectValue(AttributeValue.ATTRIBUTENAME);
        }));
        VertexServerGeoObject in = new VertexServerGeoObject(type2, currentInVertex, nodeMap2, date);
        
        list.add(new EdgeAndInOut(in, out, previous.edgeClass, previous.edgeOid));
        
        currentInAttributes = new LinkedList<>();
        currentOutAttributes = new LinkedList<>();
        currentInVertex = null;
        currentOutVertex = null;
      }
      else if (result.inAttrVertex != null)
      {
        currentInAttributes.add(result.inAttrVertex);
        currentInVertex = result.inVertex;
      }
      else if (result.outAttrVertex != null)
      {
        currentOutAttributes.add(result.outAttrVertex);
        currentOutVertex = result.outVertex;
      }
      
      previous = result;
    }

    if (previous != null)
    {
      MdVertexDAOIF mdVertex = (MdVertexDAOIF) currentOutVertex.getMdClass();
      ServerGeoObjectType type = ServerGeoObjectType.get(mdVertex);
      Map<String, List<VertexObject>> nodeMap = currentOutAttributes.stream().collect(Collectors.groupingBy(v -> {
        return (String) v.getObjectValue(AttributeValue.ATTRIBUTENAME);
      }));
      VertexServerGeoObject out = new VertexServerGeoObject(type, currentOutVertex, nodeMap, date);
      
      MdVertexDAOIF mdVertex2 = (MdVertexDAOIF) currentInVertex.getMdClass();
      ServerGeoObjectType type2 = ServerGeoObjectType.get(mdVertex2);
      Map<String, List<VertexObject>> nodeMap2 = currentInAttributes.stream().collect(Collectors.groupingBy(v -> {
        return (String) v.getObjectValue(AttributeValue.ATTRIBUTENAME);
      }));
      VertexServerGeoObject in = new VertexServerGeoObject(type2, currentInVertex, nodeMap2, date);
      
      list.add(new EdgeAndInOut(in, out, previous.edgeClass, previous.edgeOid));
    }

    return list;
  }
  
  public static String vertexColumns(String prefix)
  {
    Set<String> columns = new HashSet<String>();
    columns.add(prefix + ".@class");
    columns.add(prefix + ".@rid");
    
    // GeoVertex
    for (String column : new String[] { GeoVertex.SEQ, GeoVertex.CREATEDATE, GeoVertex.LASTUPDATEDATE, GeoVertex.OID })
    {
      columns.add(prefix + "." + column);
    }
    
    for (ServerGeoObjectType type : ServerGeoObjectType.getAll())
    {
      List<? extends MdAttributeConcreteDAOIF> mdAttrs = type.getMdVertex().getMdClassDAO().definesAttributes();
      for(MdAttributeConcreteDAOIF mdAttr : mdAttrs)
      {
        columns.add(prefix + "." + mdAttr.getColumnName());
      }
    }
    
    return StringUtils.join(columns, ", ");
  }
  
  public static Set<String> allAttributeColumns()
  {
    Set<String> columns = new HashSet<String>();
    columns.add("@class");
    columns.add("@rid");
    
    // AttributeValue
    for (String column : new String[] { AttributeValue.SEQ, AttributeValue.OID, AttributeValue.ATTRIBUTENAME, AttributeValue.STARTDATE, AttributeValue.ENDDATE })
    {
      columns.add(column);
    }
    
    for (ServerGeoObjectType type : ServerGeoObjectType.getAll())
    {
      for (net.geoprism.registry.graph.AttributeType at : type.definesAttributes())
      {
        for (MdAttributeDAOIF mdAttr : at.getStrategy().getValueAttributes())
        {
          columns.add(mdAttr.getColumnName());
        }
      }
    }
    
    return columns;
  }
}
