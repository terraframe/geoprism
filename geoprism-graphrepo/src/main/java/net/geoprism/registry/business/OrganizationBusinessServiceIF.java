package net.geoprism.registry.business;

import org.commongeoregistry.adapter.metadata.OrganizationDTO;
import org.springframework.stereotype.Component;

import net.geoprism.registry.model.GraphNode;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.view.Page;

@Component
public interface OrganizationBusinessServiceIF
{

  public ServerOrganization create(OrganizationDTO organizationDTO);

  public ServerOrganization update(OrganizationDTO organizationDTO);

  public void delete(ServerOrganization sorg);

  void move(ServerOrganization organization, ServerOrganization newParent);

  void removeAllParents(ServerOrganization organization);

  GraphNode<ServerOrganization> getAncestorTree(ServerOrganization child, String rootCode, Integer pageSize);

  Page<ServerOrganization> getChildren(ServerOrganization parent, Integer pageSize, Integer pageNumber);

  void removeChild(ServerOrganization parent, ServerOrganization child);

  void addChild(ServerOrganization parent, ServerOrganization child);

  void apply(ServerOrganization organization, ServerOrganization parent);

}
