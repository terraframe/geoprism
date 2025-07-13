package net.geoprism.registry.service.business;

import java.util.List;

import net.geoprism.graph.AttributeTypeSnapshot;
import net.geoprism.graph.ObjectTypeSnapshot;

public interface ObjectTypeBusinessServiceIF<T extends ObjectTypeSnapshot>
{
  public List<AttributeTypeSnapshot> getAttributeTypes(T snapshot);
}
