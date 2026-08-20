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

import java.util.List;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.GraphTypeDTO;
import org.springframework.stereotype.Service;

import com.runwaysdk.business.graph.GraphQuery;
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

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.DuplicateHierarchyTypeException;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.graph.DataSource;
import net.geoprism.registry.graph.DirectedAcyclicGraphType;
import net.geoprism.registry.graph.GeoVertex;
import net.geoprism.registry.model.EdgeType;

@Service
public class DirectedAcyclicGraphTypeBusinessService extends EdgeClassBusinessService<DirectedAcyclicGraphType, GraphTypeDTO> implements DirectedAcyclicGraphTypeBusinessServiceIF
{
  public DirectedAcyclicGraphTypeBusinessService()
  {
    super(DirectedAcyclicGraphType.CLASS);
  }

  @Override
  protected GraphTypeDTO createDTO()
  {
    return new GraphTypeDTO();
  }

  @Override
  public DirectedAcyclicGraphType create(GraphTypeDTO object)
  {
    String code = object.getCode();
    LocalizedValue label = object.getLabel();
    LocalizedValue description = object.getDescription();
    Long seq = object.getSeq();

    return create(code, label, description, GeoprismProperties.getOrigin(), seq);
  }

  @Override
  @Transaction
  public DirectedAcyclicGraphType create(String code, LocalizedValue label, LocalizedValue description, Long seq)
  {
    return create(code, label, description, GeoprismProperties.getOrigin(), seq);
  }

  @Override
  @Transaction
  public DirectedAcyclicGraphType create(String code, LocalizedValue label, LocalizedValue description, String origin, Long seq)
  {
    try
    {
      MdVertexDAOIF mdBusGeoEntity = MdVertexDAO.getMdVertexDAO(GeoVertex.CLASS);

      MdEdgeDAO mdEdgeDAO = MdEdgeDAO.newInstance();
      mdEdgeDAO.setValue(MdEdgeInfo.PACKAGE, RegistryConstants.DAG_PACKAGE);
      mdEdgeDAO.setValue(MdEdgeInfo.NAME, code);
      mdEdgeDAO.setValue(MdEdgeInfo.PARENT_MD_VERTEX, mdBusGeoEntity.getOid());
      mdEdgeDAO.setValue(MdEdgeInfo.CHILD_MD_VERTEX, mdBusGeoEntity.getOid());
      RegistryLocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DISPLAY_LABEL, label);
      RegistryLocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DESCRIPTION, description);
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

      createPermissions(mdEdgeDAO);

      DirectedAcyclicGraphType graphType = new DirectedAcyclicGraphType();
      graphType.setCode(code);
      graphType.setMdEdgeId(mdEdgeDAO.getOid());
      RegistryLocalizedValueConverter.populate(graphType, DirectedAcyclicGraphType.DISPLAYLABEL, label);
      RegistryLocalizedValueConverter.populate(graphType, DirectedAcyclicGraphType.DESCRIPTION, description);
      graphType.setOrigin(origin);
      graphType.setSequence(seq);
      graphType.apply();

      this.getCache().put(graphType);

      return graphType;
    }
    catch (DuplicateDataException ex)
    {
      DuplicateHierarchyTypeException ex2 = new DuplicateHierarchyTypeException();
      ex2.setDuplicateValue(code);
      throw ex2;
    }
  }

  @Override
  public List<DirectedAcyclicGraphType> getAll(String... typeCodes)
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(DirectedAcyclicGraphType.CLASS);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());

    if (typeCodes != null && typeCodes.length > 0)
    {
      statement.append(" WHERE code IN (:codes)");
    }

    GraphQuery<DirectedAcyclicGraphType> query = new GraphQuery<DirectedAcyclicGraphType>(statement.toString());

    if (typeCodes != null && typeCodes.length > 0)
    {
      query.setParameter("codes", typeCodes);
    }

    return query.getResults();
  }

}
