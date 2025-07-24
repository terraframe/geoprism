package net.geoprism.registry.service.business;

import java.util.List;

import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdClass;

import net.geoprism.graph.AttributeGeometryTypeSnapshot;
import net.geoprism.graph.AttributeTypeSnapshot;
import net.geoprism.graph.ObjectTypeSnapshot;

public interface ObjectTypeSnapshotBusinessServiceIF<T extends ObjectTypeSnapshot>
{
  public List<AttributeTypeSnapshot> getAttributeTypes(T snapshot);
  
  public AttributeTypeSnapshot createAttributeTypeSnapshot(T type, AttributeType attributeType);
  
  public AttributeGeometryTypeSnapshot createAttributeTypeSnapshot(T snapshot, GeometryType geometryType);

  public void createGeometryAttribute(GeometryType geometryType, MdVertexDAO mdTableDAO);
  
  public MdAttributeConcrete createMdAttributeFromAttributeType(MdClass mdClass, AttributeType attributeType);

}
