package net.geoprism.graph;

import java.util.LinkedList;
import java.util.List;

import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.query.OIterator;

import net.geoprism.registry.conversion.AttributeTypeSnapshotConverter;

public abstract class ObjectTypeSnapshot extends ObjectTypeSnapshotBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1791617045;

  public ObjectTypeSnapshot()
  {
    super();
  }

  public abstract String getGraphMdVertexOid();

  public abstract String getCode();

  public List<org.commongeoregistry.adapter.metadata.AttributeType> getAttributeTypes()
  {
    AttributeTypeSnapshotConverter converter = new AttributeTypeSnapshotConverter();

    List<AttributeType> attributes = new LinkedList<>();

    try (OIterator<? extends AttributeTypeSnapshot> it = this.getAllAttribute())
    {
      while (it.hasNext())
      {
        AttributeTypeSnapshot attribute = it.next();

        if (! ( attribute instanceof AttributeGeometryTypeSnapshot ))
        {
          attributes.add(converter.build(attribute));
        }
      }
    }

    return attributes;
  }

}
