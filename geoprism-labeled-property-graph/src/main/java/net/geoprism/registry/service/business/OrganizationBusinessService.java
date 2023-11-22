package net.geoprism.registry.service.business;

import java.util.List;

import org.commongeoregistry.adapter.metadata.OrganizationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.Roles;

import net.geoprism.registry.conversion.OrganizationConverter;
import net.geoprism.registry.model.GraphNode;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.service.permission.OrganizationPermissionServiceIF;
import net.geoprism.registry.view.Page;

@Service
public class OrganizationBusinessService implements OrganizationBusinessServiceIF
{
  @Autowired
  protected OrganizationPermissionServiceIF permissionService;

  @Override
  @Transaction
  public void apply(ServerOrganization organization, ServerOrganization parent)
  {
    organization.apply();

    if (parent != null)
    {
      parent.addChild(organization);
    }
  }

  @Override
  @Transaction
  public ServerOrganization create(OrganizationDTO organizationDTO)
  {
    permissionService.enforceActorCanCreate();

    final ServerOrganization organization = new OrganizationConverter().fromDTO(organizationDTO);
    organization.apply();

    return organization;
  }

  @Override
  @Transaction
  public ServerOrganization update(OrganizationDTO organizationDTO)
  {
    this.permissionService.enforceActorCanUpdate();

    ServerOrganization organization = ServerOrganization.getByCode(organizationDTO.getCode());

    try
    {
      organization.lock();

      organization.setCode(organizationDTO.getCode());
      organization.setDisplayLabel(organizationDTO.getLabel());
      organization.setContactInfo(organizationDTO.getContactInfo());
      organization.setEnabled(organizationDTO.getEnabled());
      organization.apply();
    }
    finally
    {
      organization.unlock();
    }

    return organization;
  }

  @Override
  @Transaction
  public void delete(ServerOrganization sorg)
  {
    this.permissionService.enforceActorCanDelete();

    this.deleteRoles(sorg);

    sorg.delete();
  }

  protected void deleteRoles(ServerOrganization sorg)
  {
    try
    {
      Roles orgRole = sorg.getRole();
      orgRole.delete();
    }
    catch (com.runwaysdk.dataaccess.cache.DataNotFoundException e)
    {
    }
  }

  @Override
  @Transaction
  public void addChild(ServerOrganization parent, ServerOrganization child)
  {
    this.permissionService.enforceActorCanUpdate();

    parent.addChild(child);
  }

  @Override
  @Transaction
  public void removeChild(ServerOrganization parent, ServerOrganization child)
  {
    this.permissionService.enforceActorCanUpdate();

    parent.removeChild(child);
  }

  @Override
  public Page<ServerOrganization> getChildren(ServerOrganization parent, Integer pageSize, Integer pageNumber)
  {
    if (parent != null)
    {
      return parent.getChildren(pageSize, pageNumber);
    }

    List<ServerOrganization> roots = ServerOrganization.getRoots();

    return new Page<ServerOrganization>(roots.size(), pageNumber, pageSize, roots);
  }

  @Transaction
  @Override
  public GraphNode<ServerOrganization> getAncestorTree(ServerOrganization child, String rootCode, Integer pageSize)
  {
    return child.getAncestorTree(rootCode, pageSize);
  }

  @Transaction
  @Override
  public void move(ServerOrganization organization, ServerOrganization newParent)
  {
    this.permissionService.enforceActorCanUpdate();

    organization.move(newParent);
  }

  @Transaction
  @Override
  public void removeAllParents(ServerOrganization organization)
  {
    this.permissionService.enforceActorCanUpdate();

    organization.removeAllParents();
  }

  @Override
  public JsonArray exportToJson()
  {
    JsonArray children = new JsonArray();

    List<ServerOrganization> roots = ServerOrganization.getRoots();

    for (ServerOrganization root : roots)
    {
      children.add(exportToJson(root));
    }

    return children;
  }

  private JsonObject exportToJson(ServerOrganization organization)
  {
    JsonArray children = new JsonArray();

    Page<ServerOrganization> page = organization.getChildren();

    for (ServerOrganization child : page)
    {
      children.add(exportToJson(child));
    }

    JsonObject json = organization.toJSON();
    json.remove("parentCode");
    json.remove("parentLabel");
    json.add("children", children);

    return json;
  }

  @Override
  @Transaction
  public void importJsonTree(JsonArray array)
  {
    for (int i = 0; i < array.size(); i++)
    {
      importJsonTree(null, array.get(i).getAsJsonObject());
    }
  }

  @Override
  @Transaction
  public void importJsonTree(ServerOrganization parent, JsonObject object)
  {
    OrganizationDTO dto = OrganizationDTO.fromJSON(object.toString());

    // Get and update OR create a new organization
    ServerOrganization organization = ServerOrganization.getByCode(dto.getCode(), false);

    if (organization == null)
    {
      organization = new OrganizationConverter().fromDTO(dto);
    }
    else
    {
      // If the organization already exists we need to remove the current parent
      // assignment in order to prevent duplicate edges. This makes the
      // assumption that an organization can only have a single parent
      ServerOrganization currentParent = organization.getParent();

      if (currentParent != null)
      {
        organization.removeParent(currentParent);
      }
    }

    this.apply(organization, parent);

    // Create/assign the child organizations
    JsonArray children = object.get("children").getAsJsonArray();

    for (int i = 0; i < children.size(); i++)
    {
      JsonObject child = children.get(i).getAsJsonObject();

      importJsonTree(organization, child);
    }
  }

}
