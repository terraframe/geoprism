package net.geoprism.registry.model.graph;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.runwaysdk.dataaccess.MdVertexDAOIF;

import net.geoprism.registry.graph.AttributeType;
import net.geoprism.registry.model.ServerOrganization;

public interface ObjectClassIF
{

  public Optional<AttributeType> getAttribute(String attributeName);

  public List<AttributeType> getAttributes();

  public Map<String, AttributeType> getAttributeMap();

  public ServerOrganization getServerOrganization();

  public MdVertexDAOIF getMdVertexDAO();

  public String getOrigin();

}
