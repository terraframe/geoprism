/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service.business;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
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
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdEdge;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.BusinessType;
import net.geoprism.registry.DirectedAcyclicGraphType;
import net.geoprism.registry.DirectedAcyclicGraphTypeQuery;
import net.geoprism.registry.DuplicateHierarchyTypeException;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.cache.TransactionLRUCache;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.graph.DataSource;
import net.geoprism.registry.graph.GeoVertex;

@Service
public class DirectedAcyclicGraphTypeBusinessService implements DirectedAcyclicGraphTypeBusinessServiceIF
{
  private final TransactionLRUCache<String, DirectedAcyclicGraphType> cache;

  public DirectedAcyclicGraphTypeBusinessService()
  {
    this.cache = new TransactionLRUCache<String, DirectedAcyclicGraphType>("t-undirected-cache", (v) -> {

      return new String[] { v.getCode(), v.getMdEdgeOid() };
    }, 20);
  }

  @Override
  @Transaction
  public void update(DirectedAcyclicGraphType type, JsonObject object)
  {
    try
    {
      type.appLock();

      if (object.has(DirectedAcyclicGraphType.DISPLAYLABEL))
      {
        LocalizedValue label = LocalizedValue.fromJSON(object.getAsJsonObject(DirectedAcyclicGraphType.DISPLAYLABEL));

        RegistryLocalizedValueConverter.populate(type.getDisplayLabel(), label);
      }

      if (object.has(DirectedAcyclicGraphType.DESCRIPTION))
      {
        LocalizedValue description = LocalizedValue.fromJSON(object.getAsJsonObject(DirectedAcyclicGraphType.DESCRIPTION));

        RegistryLocalizedValueConverter.populate(type.getDescription(), description);
      }

      type.setSequence(type.getSequence() + 1);
      type.apply();
    }
    finally
    {
      type.unlock();
    }

    this.cache.put(type);
  }

  @Override
  @Transaction
  public void delete(DirectedAcyclicGraphType dagt)
  {
    MdEdge mdEdge = dagt.getMdEdge();

    dagt.delete();

    mdEdge.delete();

    this.cache.remove(dagt);
  }

  @Override
  public DirectedAcyclicGraphType create(JsonObject object)
  {
    String code = object.get(DirectedAcyclicGraphType.CODE).getAsString();
    LocalizedValue label = LocalizedValue.fromJSON(object.getAsJsonObject(DirectedAcyclicGraphType.JSON_LABEL));
    LocalizedValue description = LocalizedValue.fromJSON(object.getAsJsonObject(DirectedAcyclicGraphType.DESCRIPTION));
    Long seq = object.has(BusinessType.SEQ) ? object.get(BusinessType.SEQ).getAsLong() : 0L;

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
      startDate.setValue(MdAttributeDateTimeInfo.NAME, GeoVertex.START_DATE);
      startDate.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Date");
      startDate.setStructValue(MdAttributeDateTimeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Date");
      startDate.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS, mdEdgeDAO.getOid());
      startDate.apply();

      MdAttributeDateTimeDAO endDate = MdAttributeDateTimeDAO.newInstance();
      endDate.setValue(MdAttributeDateTimeInfo.NAME, GeoVertex.END_DATE);
      endDate.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "End Date");
      endDate.setStructValue(MdAttributeDateTimeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "End Date");
      endDate.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS, mdEdgeDAO.getOid());
      endDate.apply();

      createPermissions(mdEdgeDAO);

      DirectedAcyclicGraphType graphType = new DirectedAcyclicGraphType();
      graphType.setCode(code);
      graphType.setMdEdgeId(mdEdgeDAO.getOid());
      RegistryLocalizedValueConverter.populate(graphType.getDisplayLabel(), label);
      RegistryLocalizedValueConverter.populate(graphType.getDescription(), description);
      graphType.setOrigin(origin);
      graphType.setSequence(seq);
      graphType.apply();

      this.cache.put(graphType);

      return graphType;
    }
    catch (DuplicateDataException ex)
    {
      DuplicateHierarchyTypeException ex2 = new DuplicateHierarchyTypeException();
      ex2.setDuplicateValue(code);
      throw ex2;
    }
  }

  protected void createPermissions(MdEdgeDAO mdEdgeDAO)
  {
    // None in the graph repo
  }

  @Override
  public List<DirectedAcyclicGraphType> getAll(String... typeCodes)
  {
    DirectedAcyclicGraphTypeQuery query = new DirectedAcyclicGraphTypeQuery(new QueryFactory());

    if (typeCodes != null && typeCodes.length > 0)
    {
      query.AND(query.getCode().IN(typeCodes));
    }

    try (OIterator<? extends DirectedAcyclicGraphType> it = query.getIterator())
    {
      return new LinkedList<DirectedAcyclicGraphType>(it.getAll());
    }
  }

  @Override
  public Optional<DirectedAcyclicGraphType> getByCode(String code)
  {
    return this.cache.get(code, () -> {
      DirectedAcyclicGraphTypeQuery query = new DirectedAcyclicGraphTypeQuery(new QueryFactory());
      query.WHERE(query.getCode().EQ(code));

      try (OIterator<? extends DirectedAcyclicGraphType> it = query.getIterator())
      {
        if (it.hasNext())
        {
          return Optional.ofNullable(it.next());
        }
      }

      return Optional.empty();
    });

  }

  @Override
  public DirectedAcyclicGraphType getByMdEdge(MdEdge mdEdge)
  {
    return this.cache.get(mdEdge.getOid(), () -> {
      DirectedAcyclicGraphTypeQuery query = new DirectedAcyclicGraphTypeQuery(new QueryFactory());
      query.WHERE(query.getMdEdge().EQ(mdEdge));

      try (OIterator<? extends DirectedAcyclicGraphType> it = query.getIterator())
      {
        if (it.hasNext())
        {
          return Optional.of(it.next());
        }
      }

      return Optional.empty();
    }).orElse(null);
  }

}
