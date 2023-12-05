package net.geoprism.registry.service.business;

import org.commongeoregistry.adapter.metadata.OrganizationDTO;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.geoprism.registry.model.GraphNode;
import net.geoprism.registry.model.OrganizationView;
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

  JsonArray exportToJson();

  public void importJsonTree(JsonArray array);

  public void importJsonTree(ServerOrganization parent, JsonObject object);

  public Page<OrganizationView> getPage(Integer pageSize, Integer pageNumber);
}
