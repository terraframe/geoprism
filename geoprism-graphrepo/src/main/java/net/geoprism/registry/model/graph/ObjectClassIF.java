package net.geoprism.registry.model.graph;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.geoprism.registry.graph.AttributeType;

public interface ObjectClassIF
{

  public Optional<AttributeType> getAttribute(String attributeName);

  public List<AttributeType> getAttributes();

  public Map<String, AttributeType> getAttributeMap();

}
