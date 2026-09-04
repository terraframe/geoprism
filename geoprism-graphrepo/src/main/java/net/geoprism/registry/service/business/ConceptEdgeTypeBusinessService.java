/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service.business;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeGraphReferenceInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeGraphReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeUUIDDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.registry.DuplicateHierarchyTypeException;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.graph.BaseGeoObjectType;
import net.geoprism.registry.graph.ConceptClass;
import net.geoprism.registry.graph.ConceptEdgeType;
import net.geoprism.registry.graph.DataSource;
import net.geoprism.registry.graph.GeoObjectType;
import net.geoprism.registry.graph.GeoVertex;
import net.geoprism.registry.graph.ObjectClass;
import net.geoprism.registry.model.EdgeType;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.view.ConceptEdgeTypeDTO;
import net.geoprism.registry.view.DiscreteType;

@Service
public class ConceptEdgeTypeBusinessService extends EdgeClassBusinessService<ConceptEdgeType, ConceptEdgeTypeDTO> implements ConceptEdgeTypeBusinessServiceIF
{
  @Autowired
  private ConceptClassBusinessServiceIF  typeService;

  @Autowired
  private HierarchyTypeBusinessServiceIF hierarchyService;

  public ConceptEdgeTypeBusinessService()
  {
    super(ConceptEdgeType.CLASS);
  }

  @Override
  public ObjectClass getParent(ConceptEdgeType edgeType)
  {
    return getObjectClass((MdVertexDAOIF) BusinessFacade.getEntityDAO(edgeType.getParentType()));
  }

  @Override
  public ObjectClass getChild(ConceptEdgeType edgeType)
  {
    return getObjectClass((MdVertexDAOIF) BusinessFacade.getEntityDAO(edgeType.getChildType()));
  }

  protected ObjectClass getObjectClass(MdVertexDAOIF mdVertex)
  {
    MdVertexDAOIF mdBusGeoEntity = MdVertexDAO.getMdVertexDAO(GeoVertex.CLASS);

    if (mdVertex.equals(mdBusGeoEntity))
    {
      return BaseGeoObjectType.getByCode(GeoObjectType.ROOT);
    }

    return this.typeService.getByMdVertex(mdVertex);
  }

  @Override
  protected ConceptEdgeTypeDTO createDTO()
  {
    return new ConceptEdgeTypeDTO();
  }

  @Override
  protected void populate(ConceptEdgeType edgeType, ConceptEdgeTypeDTO dto)
  {
    dto.setParentType(edgeType.getParentType().getTypeName());
    dto.setChildType(edgeType.getChildType().getTypeName());
    dto.setDiscreteType(DiscreteType.valueOf(edgeType.getDiscreteType()));
    dto.setOrganizationCode(edgeType.getOrganization().getCode());

    super.populate(edgeType, dto);
  }

  @Override
  @Transaction
  public ConceptEdgeType create(ConceptEdgeTypeDTO dto)
  {
    ConceptClass parentType = this.typeService.getByCodeOrThrow(dto.getParentType());
    ConceptClass childType = this.typeService.getByCodeOrThrow(dto.getChildType());
    ServerOrganization organization = ServerOrganization.getByCode(dto.getOrganizationCode());

    try
    {
      MdEdgeDAO mdEdgeDAO = MdEdgeDAO.newInstance();
      mdEdgeDAO.setValue(MdEdgeInfo.PACKAGE, RegistryConstants.DAG_PACKAGE);
      mdEdgeDAO.setValue(MdEdgeInfo.NAME, dto.getCode());
      mdEdgeDAO.setValue(MdEdgeInfo.PARENT_MD_VERTEX, parentType.getMdVertexOid());
      mdEdgeDAO.setValue(MdEdgeInfo.CHILD_MD_VERTEX, childType.getMdVertexOid());
      RegistryLocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DISPLAY_LABEL, dto.getLabel());
      RegistryLocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DESCRIPTION, dto.getDescription());
      mdEdgeDAO.setValue(MdEdgeInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
      mdEdgeDAO.apply();

      MdAttributeUUIDDAO uidAttr = MdAttributeUUIDDAO.newInstance();
      uidAttr.setValue(MdAttributeConcreteInfo.NAME, DefaultAttribute.UID.getName());
      uidAttr.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.UID.getDefaultLocalizedName());
      uidAttr.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.UID.getDefaultDescription());
      uidAttr.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdEdgeDAO.getOid());
      uidAttr.setValue(MdAttributeConcreteInfo.REQUIRED, true);
      uidAttr.apply();

      MdAttributeGraphReferenceDAO sourceAttr = MdAttributeGraphReferenceDAO.newInstance();
      sourceAttr.setValue(MdAttributeConcreteInfo.NAME, DefaultAttribute.DATA_SOURCE.getName());
      sourceAttr.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.DATA_SOURCE.getDefaultLocalizedName());
      sourceAttr.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.DATA_SOURCE.getDefaultDescription());
      sourceAttr.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdEdgeDAO.getOid());
      sourceAttr.setValue(MdAttributeGraphReferenceInfo.REFERENCE_MD_VERTEX, MdVertexDAO.getMdVertexDAO(DataSource.CLASS).getOid());
      sourceAttr.setValue(MdAttributeConcreteInfo.REQUIRED, false);
      sourceAttr.apply();

      MdAttributeDateTimeDAO startDate = MdAttributeDateTimeDAO.newInstance();
      startDate.setValue(MdAttributeDateTimeInfo.NAME, EdgeType.START_DATE);
      startDate.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Date");
      startDate.setStructValue(MdAttributeDateTimeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Date");
      startDate.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS, mdEdgeDAO.getOid());
      startDate.apply();

      MdAttributeDateTimeDAO endDate = MdAttributeDateTimeDAO.newInstance();
      endDate.setValue(MdAttributeDateTimeInfo.NAME, EdgeType.END_DATE);
      endDate.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "End Date");
      endDate.setStructValue(MdAttributeDateTimeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "End Date");
      endDate.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS, mdEdgeDAO.getOid());
      endDate.apply();

      this.createPermissions(mdEdgeDAO);

      ConceptEdgeType edgeType = new ConceptEdgeType();
      edgeType.setOrganization(organization.getGraphOrganization());
      edgeType.setCode(dto.getCode());
      edgeType.setMdEdgeId(mdEdgeDAO.getOid());
      edgeType.setParentTypeId(parentType.getMdVertexOid());
      edgeType.setChildTypeId(childType.getMdVertexOid());
      edgeType.setOrigin(dto.getOrigin());
      RegistryLocalizedValueConverter.populate(edgeType, ConceptEdgeType.DISPLAYLABEL, dto.getLabel());
      RegistryLocalizedValueConverter.populate(edgeType, ConceptEdgeType.DESCRIPTION, dto.getDescription());
      edgeType.setSequence(dto.getSeq());
      edgeType.setDiscreteType(dto.getDiscreteType().name());
      edgeType.apply();

      this.getCache().put(edgeType);

      return edgeType;
    }
    catch (DuplicateDataException ex)
    {
      DuplicateHierarchyTypeException ex2 = new DuplicateHierarchyTypeException();
      ex2.setDuplicateValue(dto.getCode());
      throw ex2;
    }
  }

  @Override
  protected void createPermissions(MdEdgeDAO mdEdgeDAO)
  {
    hierarchyService.grantWritePermissionsOnMdTermRel(mdEdgeDAO);
  }
}
