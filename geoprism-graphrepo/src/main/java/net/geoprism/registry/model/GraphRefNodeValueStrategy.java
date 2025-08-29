package net.geoprism.registry.model;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdVertexDAOIF;

import net.geoprism.registry.graph.AttributeType;

public class GraphRefNodeValueStrategy extends ValueNodeStrategy
{

  public GraphRefNodeValueStrategy(AttributeType type, MdVertexDAOIF nodeVertex, String nodeAttribute)
  {
    super(type, nodeVertex, nodeAttribute);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected <T> T getNodeValue(VertexObject node)
  {
    
    
//    AttributeGraphRef attribute = (AttributeGraphRef) node.getObjectAttribute(this.getNodeAttribute());
//    
//    return (T) attribute.getId();

//    if (!StringUtils.isBlank(oid))
//    {
//      DataSourceBusinessServiceIF service = ServiceFactory.getBean(DataSourceBusinessServiceIF.class);
//
//      return (T) service.get(oid);
//    }
//
    return super.getNodeValue(node);
  }

}
