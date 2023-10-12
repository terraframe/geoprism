package net.geoprism.registry.business;

import java.util.LinkedList;
import java.util.List;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdEdge;

import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.BusinessEdgeTypeQuery;
import net.geoprism.registry.BusinessType;
import net.geoprism.registry.DuplicateHierarchyTypeException;
import net.geoprism.registry.Organization;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;

@Service
public class BusinessEdgeTypeBusinessService implements BusinessEdgeTypeBusinessServiceIF
{
  @Autowired
  private BusinessTypeBusinessServiceIF  typeService;

  @Autowired
  private HierarchyTypeBusinessServiceIF hierarchyService;

  @Override
  public BusinessType getParent(BusinessEdgeType edgeType)
  {
    return this.typeService.getByMdVertex((MdVertexDAOIF) BusinessFacade.getEntityDAO(edgeType.getParentType()));
  }

  @Override
  public BusinessType getChild(BusinessEdgeType edgeType)
  {
    return this.typeService.getByMdVertex((MdVertexDAOIF) BusinessFacade.getEntityDAO(edgeType.getChildType()));
  }

  @Override
  @Transaction
  public void update(BusinessEdgeType edgeType, JsonObject object)
  {
    try
    {
      edgeType.appLock();

      if (object.has(BusinessEdgeType.DISPLAYLABEL))
      {
        LocalizedValue label = LocalizedValue.fromJSON(object.getAsJsonObject(BusinessEdgeType.DISPLAYLABEL));

        RegistryLocalizedValueConverter.populate(edgeType.getDisplayLabel(), label);
      }

      if (object.has(BusinessEdgeType.DESCRIPTION))
      {
        LocalizedValue description = LocalizedValue.fromJSON(object.getAsJsonObject(BusinessEdgeType.DESCRIPTION));

        RegistryLocalizedValueConverter.populate(edgeType.getDescription(), description);
      }

      edgeType.apply();
    }
    finally
    {
      edgeType.unlock();
    }
  }

  @Override
  @Transaction
  public void delete(BusinessEdgeType edgeType)
  {
    MdEdge mdEdge = edgeType.getMdEdge();

    edgeType.delete();

    mdEdge.delete();
  }

  @Override
  public JsonObject toJSON(BusinessEdgeType edgeType)
  {
    JsonObject object = new JsonObject();
    object.addProperty(BusinessEdgeType.OID, edgeType.getOid());
    object.addProperty(BusinessEdgeType.TYPE, "BusinessEdgeType");
    object.addProperty(BusinessEdgeType.CODE, edgeType.getCode());
    object.addProperty(BusinessEdgeType.ORGANIZATION, edgeType.getOrganization().getCode());
    object.addProperty(BusinessEdgeType.PARENTTYPE, edgeType.getParentType().getTypeName());
    object.addProperty(BusinessEdgeType.CHILDTYPE, edgeType.getChildType().getTypeName());
    object.add(BusinessEdgeType.JSON_LABEL, RegistryLocalizedValueConverter.convertNoAutoCoalesce(edgeType.getDisplayLabel()).toJSON());
    object.add(BusinessEdgeType.DESCRIPTION, RegistryLocalizedValueConverter.convertNoAutoCoalesce(edgeType.getDescription()).toJSON());

    return object;
  }

  @Override
  public List<BusinessEdgeType> getAll()
  {
    BusinessEdgeTypeQuery query = new BusinessEdgeTypeQuery(new QueryFactory());

    try (OIterator<? extends BusinessEdgeType> it = query.getIterator())
    {
      return new LinkedList<BusinessEdgeType>(it.getAll());
    }
  }

  @Override
  public BusinessEdgeType getByCode(String code)
  {
    return BusinessEdgeType.getByKey(code);
  }

  @Override
  public BusinessEdgeType getByMdEdge(MdEdge mdEdge)
  {
    BusinessEdgeTypeQuery query = new BusinessEdgeTypeQuery(new QueryFactory());
    query.WHERE(query.getMdEdge().EQ(mdEdge));

    try (OIterator<? extends BusinessEdgeType> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
  }

  @Override
  public BusinessEdgeType create(JsonObject object)
  {
    String code = object.get(BusinessEdgeType.CODE).getAsString();
    String parentTypeCode = object.get(BusinessEdgeType.PARENTTYPE).getAsString();
    String childTypeCode = object.get(BusinessEdgeType.CHILDTYPE).getAsString();
    LocalizedValue label = LocalizedValue.fromJSON(object.getAsJsonObject(BusinessEdgeType.JSON_LABEL));
    LocalizedValue description = LocalizedValue.fromJSON(object.getAsJsonObject(BusinessEdgeType.DESCRIPTION));
    String organizationCode = object.get(BusinessType.ORGANIZATION).getAsString();

    return create(organizationCode, code, label, description, parentTypeCode, childTypeCode);
  }

  @Override
  @Transaction
  public BusinessEdgeType create(String organizationCode, String code, LocalizedValue label, LocalizedValue description, String parentTypeCode, String childTypeCode)
  {
    BusinessType parentType = this.typeService.getByCode(parentTypeCode);
    BusinessType childType = this.typeService.getByCode(childTypeCode);
    Organization organization = Organization.getByCode(organizationCode);

    try
    {
      MdEdgeDAO mdEdgeDAO = MdEdgeDAO.newInstance();
      mdEdgeDAO.setValue(MdEdgeInfo.PACKAGE, RegistryConstants.DAG_PACKAGE);
      mdEdgeDAO.setValue(MdEdgeInfo.NAME, code);
      mdEdgeDAO.setValue(MdEdgeInfo.PARENT_MD_VERTEX, parentType.getMdVertexOid());
      mdEdgeDAO.setValue(MdEdgeInfo.CHILD_MD_VERTEX, childType.getMdVertexOid());
      RegistryLocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DISPLAY_LABEL, label);
      RegistryLocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DESCRIPTION, description);
      mdEdgeDAO.setValue(MdEdgeInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
      mdEdgeDAO.apply();

      hierarchyService.grantWritePermissionsOnMdTermRel(mdEdgeDAO);

      BusinessEdgeType businessEdgeType = new BusinessEdgeType();
      businessEdgeType.setOrganization(organization);
      businessEdgeType.setCode(code);
      businessEdgeType.setMdEdgeId(mdEdgeDAO.getOid());
      businessEdgeType.setParentTypeId(parentType.getMdVertexOid());
      businessEdgeType.setChildTypeId(childType.getMdVertexOid());
      RegistryLocalizedValueConverter.populate(businessEdgeType.getDisplayLabel(), label);
      RegistryLocalizedValueConverter.populate(businessEdgeType.getDescription(), description);
      businessEdgeType.apply();

      return businessEdgeType;
    }
    catch (DuplicateDataException ex)
    {
      DuplicateHierarchyTypeException ex2 = new DuplicateHierarchyTypeException();
      ex2.setDuplicateValue(code);
      throw ex2;
    }
  }

}
