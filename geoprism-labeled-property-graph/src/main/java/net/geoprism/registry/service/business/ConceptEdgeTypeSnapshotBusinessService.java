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

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.dataaccess.metadata.MdAttributeUUIDDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdGraphClassDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdEdge;
import com.runwaysdk.system.metadata.MdGraphClassQuery;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.graph.ConceptClassSnapshot;
import net.geoprism.graph.ConceptEdgeTypeSnapshot;
import net.geoprism.graph.ConceptEdgeTypeSnapshotQuery;
import net.geoprism.graph.LabeledPropertyGraphTypeSnapshotQuery;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.graph.ObjectTypeSnapshot;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.SnapshotContainer;
import net.geoprism.registry.view.ConceptEdgeTypeDTO;
import net.geoprism.registry.view.DiscreteType;

@Service
public class ConceptEdgeTypeSnapshotBusinessService implements ConceptEdgeTypeSnapshotBusinessServiceIF
{
  public static final String                    PREFIX = "g_";

  public static final String                    SPLIT  = "__";

  @Autowired
  private ConceptClassSnapshotBusinessServiceIF typeService;

  @Override
  @Transaction
  public void delete(ConceptEdgeTypeSnapshot snapshot)
  {
    String mdEdgeOid = snapshot.getGraphMdEdgeOid();

    snapshot.delete();

    if (!StringUtils.isBlank(mdEdgeOid))
    {
      MdGraphClassDAO.get(mdEdgeOid).getBusinessDAO().delete();
    }
  }

  @Override
  public String getTableName(String className)
  {
    int count = 0;

    String name = PREFIX + count + SPLIT + className;

    if (name.length() > 25)
    {
      name = name.substring(0, 25);
    }

    while (isTableNameInUse(name))
    {
      count++;

      name = PREFIX + count + className;

      if (name.length() > 25)
      {
        name = name.substring(0, 25);
      }
    }

    return name;
  }

  private boolean isTableNameInUse(String name)
  {
    MdGraphClassQuery query = new MdGraphClassQuery(new QueryFactory());
    query.WHERE(query.getDbClassName().EQ(name));

    return query.getCount() > 0;
  }

  @Override
  public ConceptEdgeTypeSnapshot create(SnapshotContainer<?> version, ConceptEdgeTypeDTO dto)
  {
    ConceptClassSnapshot parent = this.typeService.get(version, dto.getParentType());
    ConceptClassSnapshot child = this.typeService.get(version, dto.getChildType());

    return create(version, dto, parent, child);
  }

  @Override
  public ConceptEdgeTypeSnapshot create(SnapshotContainer<?> version, ConceptEdgeTypeDTO dto, ConceptClassSnapshot parent, ConceptClassSnapshot child)
  {
    String code = dto.getCode();
    String orgCode = dto.getOrganizationCode();
    String origin = StringUtils.isNotBlank(dto.getOrigin()) ? dto.getOrigin() : GeoprismProperties.getOrigin();
    Long sequence = dto.getSeq() != null ? dto.getSeq() : 0;
    String viewName = getTableName(code);
    LocalizedValue label = dto.getLabel();
    LocalizedValue description = dto.getDescription();

    MdEdge mdEdge = createMdEdge(version, parent, child, viewName, label, description);

    ConceptEdgeTypeSnapshot snapshot = new ConceptEdgeTypeSnapshot();
    snapshot.setGraphMdEdge(mdEdge);
    snapshot.setCode(code);
    snapshot.setOrigin(origin);
    snapshot.setSequence(sequence);
    snapshot.setOrgCode(orgCode);
    snapshot.setParentType(parent);
    snapshot.setChildType(child);
    snapshot.setDiscreteType(dto.getDiscreteType().name());
    LocalizedValueConverter.populate(snapshot.getDisplayLabel(), label);
    LocalizedValueConverter.populate(snapshot.getDescription(), description);
    snapshot.apply();

    version.addSnapshot(snapshot).apply();

    return snapshot;
  }

  protected MdEdge createMdEdge(SnapshotContainer<?> version, ObjectTypeSnapshot parent, ObjectTypeSnapshot child, String viewName, LocalizedValue label, LocalizedValue description)
  {
    if (version.createTablesWithSnapshot())
    {
      MdEdgeDAO mdEdgeDAO = MdEdgeDAO.newInstance();
      mdEdgeDAO.setValue(MdEdgeInfo.PACKAGE, RegistryConstants.UNIVERSAL_GRAPH_PACKAGE);
      mdEdgeDAO.setValue(MdEdgeInfo.NAME, viewName);
      mdEdgeDAO.setValue(MdEdgeInfo.DB_CLASS_NAME, viewName);
      mdEdgeDAO.setValue(MdEdgeInfo.PARENT_MD_VERTEX, parent.getGraphMdVertexOid());
      mdEdgeDAO.setValue(MdEdgeInfo.CHILD_MD_VERTEX, child.getGraphMdVertexOid());
      LocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DISPLAY_LABEL, label);
      LocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DESCRIPTION, description);
      mdEdgeDAO.setValue(MdEdgeInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
      mdEdgeDAO.apply();

      MdAttributeUUIDDAO uidAttr = MdAttributeUUIDDAO.newInstance();
      uidAttr.setValue(MdAttributeConcreteInfo.NAME, DefaultAttribute.UID.getName());
      uidAttr.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.UID.getDefaultLocalizedName());
      uidAttr.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.UID.getDefaultDescription());
      uidAttr.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdEdgeDAO.getOid());
      uidAttr.setValue(MdAttributeConcreteInfo.REQUIRED, true);
      uidAttr.apply();

      MdEdge mdEdge = (MdEdge) BusinessFacade.get(mdEdgeDAO);

      this.assignPermissions(mdEdge);

      return mdEdge;
    }

    return null;
  }

  @Override
  public ConceptEdgeTypeSnapshot get(SnapshotContainer<?> version, String code)
  {
    QueryFactory factory = new QueryFactory();

    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ((LabeledPropertyGraphTypeVersion) version));

    ConceptEdgeTypeSnapshotQuery query = new ConceptEdgeTypeSnapshotQuery(factory);
    query.WHERE(query.EQ(vQuery.getChild()));
    query.AND(query.getCode().EQ(code));

    try (OIterator<? extends ConceptEdgeTypeSnapshot> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
  }

  protected void assignPermissions(ComponentIF component)
  {
  }

  @Override
  public ConceptEdgeTypeDTO toDTO(ConceptEdgeTypeSnapshot snapshot)
  {
    ConceptEdgeTypeDTO dto = new ConceptEdgeTypeDTO();
    dto.setCode(snapshot.getCode());
    dto.setOrganizationCode(snapshot.getOrgCode());
    dto.setOrigin(snapshot.getOrigin());
    dto.setSeq(snapshot.getSequence());
    dto.setLabel(LocalizedValueConverter.convertNoAutoCoalesce(snapshot.getDisplayLabel()));
    dto.setDescription(LocalizedValueConverter.convertNoAutoCoalesce(snapshot.getDescription()));
    dto.setParentType(snapshot.getParentType().getCode());
    dto.setChildType(snapshot.getChildType().getCode());
    dto.setDiscreteType(DiscreteType.valueOf(snapshot.getDiscreteType()));

    return dto;
  }

  public LabeledPropertyGraphTypeVersion getVersion(ConceptEdgeTypeSnapshot snapshot)
  {
    try (OIterator<? extends LabeledPropertyGraphTypeVersion> iterator = snapshot.getAllVersion())
    {
      if (iterator.hasNext())
      {
        return iterator.next();
      }

    }
    return null;
  }
}
