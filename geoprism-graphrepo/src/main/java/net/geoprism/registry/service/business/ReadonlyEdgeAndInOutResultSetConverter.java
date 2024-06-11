package net.geoprism.registry.service.business;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.Attribute;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.dataaccess.UnknownTermException;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.locationtech.jts.geom.Geometry;

import com.orientechnologies.orient.core.sql.executor.OResult;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.graph.orientdb.ResultSetConverter;
import com.runwaysdk.dataaccess.metadata.graph.MdGraphClassDAO;

import net.geoprism.ontology.Classifier;
import net.geoprism.registry.cache.ClassificationCache;
import net.geoprism.registry.graph.AttributeValue;
import net.geoprism.registry.io.TermValueException;
import net.geoprism.registry.model.Classification;
import net.geoprism.registry.model.ClassificationType;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.service.business.ReadonlyEdgeAndInOutResultSetConverter.ReadonlyPrefixedResultObject;

public class ReadonlyEdgeAndInOutResultSetConverter extends ResultSetConverter
{
public static final String VERTEX_IN_PREFIX = "in";
  
  public static final String VERTEX_OUT_PREFIX = "out";
  
  public static final String IN_ATTR_PREFIX = "inAttr";
  
  public static final String OUT_ATTR_PREFIX = "outAttr";
  
  public ReadonlyEdgeAndInOutResultSetConverter()
  {
    super();
  }
  
  @Override
  public ReadonlyEdgeAndVerticies convert(GraphRequest request, Object result)
  {
    final OResult oresult = (OResult) result;
    
    final String inOid = oresult.getProperty(VERTEX_IN_PREFIX + ".oid");
    final String outOid = oresult.getProperty(VERTEX_OUT_PREFIX + ".oid");
    final String edgeClass = oresult.getProperty("edgeClass");
    final String edgeOid = oresult.getProperty("edgeOid");
    
    if (edgeClass.equals("search_link_default") || (inOid == null && outOid == null)) return null;
    
    Map<String, Object> resultMap = (Map<String, Object>) super.convert(request, oresult);
    
    ReadonlyPrefixedResultObject goInVertex = null;
    ReadonlyPrefixedResultObject inAttrVertex = null;
    if (inOid != null)
    {
      goInVertex = new ReadonlyPrefixedResultObject(resultMap, VERTEX_IN_PREFIX + ".");
      inAttrVertex = new ReadonlyPrefixedResultObject(resultMap, IN_ATTR_PREFIX + ".");
    }
    
    ReadonlyPrefixedResultObject goOutVertex = null;
    ReadonlyPrefixedResultObject outAttrVertex = null;
    if (outOid != null)
    {
      goOutVertex = new ReadonlyPrefixedResultObject(resultMap, VERTEX_OUT_PREFIX + ".");
      outAttrVertex = new ReadonlyPrefixedResultObject(resultMap, OUT_ATTR_PREFIX + ".");
    }
    
    return new ReadonlyEdgeAndVerticies(goInVertex, inAttrVertex, goOutVertex, outAttrVertex, edgeClass, edgeOid);
  }
  
  public static class ReadonlyPrefixedResultObject
  {
    private String propertyPrefix;
    
    private Map<String, Object> resultMap;
    
    public ReadonlyPrefixedResultObject(Map<String, Object> resultMap, String propertyPrefix)
    {
      this.propertyPrefix = propertyPrefix;
      this.resultMap = resultMap;
    }

    public Object getObjectValue(String name)
    {
      return resultMap.get(propertyPrefix + name);
    }

    public boolean hasProperty(String varName)
    {
      return resultMap.containsKey(propertyPrefix + varName);
    }
    
    public String getOid()
    {
      return (String) getObjectValue("oid");
    }
    
    public MdVertexDAOIF getMdClass()
    {
      return (MdVertexDAOIF) MdGraphClassDAO.getMdGraphClassByTableName((String) getObjectValue("@class"));
    }
    
    private boolean between(Date date, Date start, Date end)
    {
      return (date.equals(start) || date.after(start)) && (date.equals(end) || date.before(end));
    }
    
    public GeoObject toGeoObject(List<ReadonlyPrefixedResultObject> attributeValues, Date date, ClassificationCache classiCache, ClassificationBusinessServiceIF cService, ClassificationTypeBusinessServiceIF cTypeService)
    {
      ServerGeoObjectType type = ServerGeoObjectType.get(getMdClass());
      Map<String, ReadonlyPrefixedResultObject> nodeMap = new HashMap<String, ReadonlyPrefixedResultObject>();
      attributeValues.stream().filter(at -> between(date, (Date) at.getObjectValue("startDate"), (Date) at.getObjectValue("endDate"))).forEach(v -> nodeMap.put((String) v.getObjectValue(AttributeValue.ATTRIBUTENAME), v));
      
      GeoObjectType typeDto = type.toDTO();
      Map<String, Attribute> attributeMap = GeoObject.buildAttributeMap(typeDto);

      GeoObject geoObj = new GeoObject(typeDto, type.getGeometryType(), attributeMap);

      Map<String, AttributeType> attributes = typeDto.getAttributeMap();
      attributes.forEach((attributeName, attribute) -> {
        if (attributeName.equals(DefaultAttribute.TYPE.getName()) || attributeName.equals(DefaultAttribute.GEOMETRY.getName()))
        {
          // Ignore
        }
        else if (nodeMap.containsKey(attributeName))
        {
          ReadonlyPrefixedResultObject resultObject = nodeMap.get(attributeName);
          Object value = resultObject.getObjectValue("value");

          if (value != null)
          {
            if (attribute instanceof AttributeTermType)
            {
              Classifier classifier = (Classifier) value;

              try
              {
                geoObj.setValue(attributeName, classifier.getClassifierId());
              }
              catch (UnknownTermException e)
              {
                TermValueException ex = new TermValueException();
                ex.setAttributeLabel(e.getAttribute().getLabel().getValue());
                ex.setCode(e.getCode());

                throw e;
              }
            }
            else if (attribute instanceof AttributeClassificationType)
            {
              final String rid = value.toString();
              String classificationTypeCode = ( (AttributeClassificationType) attribute ).getClassificationType();

              Classification classification = classificationFromRid(rid, classificationTypeCode, classiCache, cService, cTypeService);
              
              try
              {
                geoObj.setValue(attributeName, classification.toTerm());
              }
              catch (UnknownTermException e)
              {
                TermValueException ex = new TermValueException();
                ex.setAttributeLabel(e.getAttribute().getLabel().getValue());
                ex.setCode(e.getCode());

                throw e;
              }
            }
            else
            {
              geoObj.setValue(attributeName, value);
            }
          }
        }
      });

      geoObj.setUid((String) this.getObjectValue(DefaultAttribute.UID.getName()));
      geoObj.setCode((String) this.getObjectValue(DefaultAttribute.CODE.getName()));
      
      if (nodeMap.containsKey(DefaultAttribute.GEOMETRY.getName()))
        geoObj.setGeometry((Geometry) nodeMap.get(DefaultAttribute.GEOMETRY.getName()).getObjectValue("value"));
      
      if (nodeMap.containsKey(DefaultAttribute.DISPLAY_LABEL.getName()))
        geoObj.setDisplayLabel(new LocalizedValue((String) nodeMap.get(DefaultAttribute.DISPLAY_LABEL.getName()).getObjectValue("defaultLocale")));
      
      if (nodeMap.containsKey(DefaultAttribute.EXISTS.getName()))
        geoObj.setExists((Boolean) nodeMap.get(DefaultAttribute.EXISTS.getName()).getObjectValue("value"));
      
      geoObj.setInvalid((Boolean) this.getObjectValue(DefaultAttribute.INVALID.getName()));

      return geoObj;
    }
    
    private Classification classificationFromRid(String rid, String classificationTypeCode, ClassificationCache classiCache, ClassificationBusinessServiceIF cService, ClassificationTypeBusinessServiceIF cTypeService)
    {
      Classification classification = null;
      if (classiCache != null)
      {
        classification = classiCache.getClassification(classificationTypeCode, rid);
      }

      if (classification == null)
      {
        ClassificationType type = cTypeService.getByCode(classificationTypeCode);
        
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT FROM " + type.getMdVertex().getDBClassName());
        builder.append(" WHERE @rid = :rid");

        GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(builder.toString());
        query.setParameter("rid", rid);

        VertexObject result = query.getSingleResult();

        if (result != null)
        {
          classification = new Classification(type, result);
        }

        if (classification != null && classiCache != null)
        {
          classiCache.putClassification(classificationTypeCode, rid, classification);
        }
      }
      
      return classification;
    }
  }
  
  public static class ReadonlyEdgeAndVerticies
  {
    public String edgeOid;
    
    public String edgeClass;
    
    public ReadonlyPrefixedResultObject inVertex;
    
    public ReadonlyPrefixedResultObject inAttrVertex;
    
    public ReadonlyPrefixedResultObject outVertex;
    
    public ReadonlyPrefixedResultObject outAttrVertex;

    public ReadonlyEdgeAndVerticies(ReadonlyPrefixedResultObject inVertex, ReadonlyPrefixedResultObject inAttrVertex, ReadonlyPrefixedResultObject outVertex, ReadonlyPrefixedResultObject outAttrVertex, String edgeClass, String edgeOid)
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
  
  public static class EdgeAndGOInOut
  {
    public String edgeOid;
    
    public String edgeClass;
    
    public GeoObject in;
    
    public GeoObject out;

    public EdgeAndGOInOut(GeoObject in, GeoObject out, String edgeClass, String edgeOid)
    {
      super();
      this.edgeOid = edgeOid;
      this.edgeClass = edgeClass;
      this.in = in;
      this.out = out;
    }
  }
  
  public static List<EdgeAndGOInOut> convertResults(List<ReadonlyEdgeAndVerticies> results, Date date, ClassificationCache classiCache, ClassificationBusinessServiceIF cService, ClassificationTypeBusinessServiceIF cTypeService)
  {
    ReadonlyEdgeAndVerticies previous = null;
    List<ReadonlyPrefixedResultObject> currentInAttributes = new LinkedList<>();
    List<ReadonlyPrefixedResultObject> currentOutAttributes = new LinkedList<>();
    ReadonlyPrefixedResultObject currentInVertex = null;
    ReadonlyPrefixedResultObject currentOutVertex = null;
    List<EdgeAndGOInOut> list = new LinkedList<EdgeAndGOInOut>();

    for (ReadonlyEdgeAndVerticies result : results)
    {
      if (previous != null && !(previous.edgeOid.equals(result.edgeOid)) && currentOutVertex != null && currentInVertex != null)
      {
        GeoObject out = currentOutVertex.toGeoObject(currentOutAttributes, date, classiCache, cService, cTypeService);
        GeoObject in = currentInVertex.toGeoObject(currentInAttributes, date, classiCache, cService, cTypeService);
        
        list.add(new EdgeAndGOInOut(in, out, previous.edgeClass, previous.edgeOid));
        
        currentInAttributes = new LinkedList<>();
        currentOutAttributes = new LinkedList<>();
        currentInVertex = null;
        currentOutVertex = null;
      }
      
      if (result.inAttrVertex != null)
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
      GeoObject out = currentOutVertex.toGeoObject(currentOutAttributes, date, classiCache, cService, cTypeService);
      GeoObject in = currentInVertex.toGeoObject(currentInAttributes, date, classiCache, cService, cTypeService);
      
      list.add(new EdgeAndGOInOut(in, out, previous.edgeClass, previous.edgeOid));
    }
    
    return list;
  }
}
