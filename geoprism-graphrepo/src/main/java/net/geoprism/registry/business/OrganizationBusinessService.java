package net.geoprism.registry.business;

import java.util.List;

import org.commongeoregistry.adapter.metadata.OrganizationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.Roles;

import net.geoprism.graphrepo.permission.OrganizationPermissionServiceIF;
import net.geoprism.registry.ObjectHasDataException;
import net.geoprism.registry.conversion.OrganizationConverter;
import net.geoprism.registry.model.GraphNode;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.service.ServiceFactory;
import net.geoprism.registry.view.Page;

@Service
public class OrganizationBusinessService implements OrganizationBusinessServiceIF
{
  @Autowired
  private OrganizationPermissionServiceIF permissionService;
  
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

    // Can't delete if there's existing data
    List<ServerHierarchyType> hierarchyTypes = ServiceFactory.getMetadataCache().getAllHierarchyTypes();

    for (ServerHierarchyType ht : hierarchyTypes)
    {
      if (ht.getOrganizationCode().equals(sorg.getCode()))
      {
        throw new ObjectHasDataException();
      }
    }

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

}
