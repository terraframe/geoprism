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

import java.util.Date;
import java.util.List;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.graph.ConceptClassSnapshot;
import net.geoprism.graph.ConceptClassSnapshotQuery;
import net.geoprism.graph.LabeledPropertyGraphTypeSnapshotQuery;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.registry.DateFormatter;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.SnapshotContainer;
import net.geoprism.registry.view.ConceptClassDTO;

@Service
public class ConceptClassSnapshotBusinessService extends ObjectTypeSnapshotBusinessService<ConceptClassSnapshot> implements ConceptClassSnapshotBusinessServiceIF
{
  @Transaction
  @Override
  public ConceptClassSnapshot create(SnapshotContainer<?> version, ConceptClassDTO type)
  {
    String code = type.getCode();
    String orgCode = type.getOrganization();
    String origin = type.hasOrigin() ? type.getOrigin() : GeoprismProperties.getOrigin();
    Long sequence = type.hasSequence() ? type.getSequence() : 0;
    String viewName = getTableName(code);
    LocalizedValue label = type.getDisplayLabel();

    List<AttributeType> attributes = type.getAttributes();

    MdVertex mdTable = createMdVertex(version, viewName, label, attributes);

    ConceptClassSnapshot snapshot = new ConceptClassSnapshot();
    snapshot.setGraphMdVertex(mdTable);
    snapshot.setCode(code);
    snapshot.setOrgCode(orgCode);
    snapshot.setOrigin(origin);
    snapshot.setSequence(sequence);
    LocalizedValueConverter.populate(snapshot.getDisplayLabel(), label);
    snapshot.apply();

    version.addSnapshot(snapshot).apply();

    attributes.forEach(attributeType -> {
      this.createAttributeTypeSnapshot(snapshot, attributeType);
    });

    return snapshot;
  }

  protected MdVertex createMdVertex(SnapshotContainer<?> version, String viewName, LocalizedValue label, List<AttributeType> attributes)
  {
    // Create the MdTable
    if (version.createTablesWithSnapshot())
    {
      MdVertexDAO mdTableDAO = MdVertexDAO.newInstance();
      mdTableDAO.setValue(MdVertexInfo.NAME, viewName);
      mdTableDAO.setValue(MdVertexInfo.PACKAGE, TABLE_PACKAGE);
      LocalizedValueConverter.populate(mdTableDAO, MdVertexInfo.DISPLAY_LABEL, label);
      mdTableDAO.setValue(MdVertexInfo.DB_CLASS_NAME, viewName);
      mdTableDAO.setValue(MdVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdTableDAO.setValue(MdVertexInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
      mdTableDAO.apply();

      MdVertex mdTable = (MdVertex) BusinessFacade.get(mdTableDAO);

      attributes.forEach(attributeType -> {
        this.createMdAttributeFromAttributeType(mdTable, attributeType);
      });

      this.assignPermissions(mdTableDAO);

      return mdTable;
    }

    return null;
  }

  @Override
  public ConceptClassSnapshot get(SnapshotContainer<?> version, String code)
  {
    QueryFactory factory = new QueryFactory();

    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ((LabeledPropertyGraphTypeVersion) version));

    ConceptClassSnapshotQuery query = new ConceptClassSnapshotQuery(factory);
    query.WHERE(query.EQ(vQuery.getChild()));
    query.AND(query.getCode().EQ(code));

    try (OIterator<? extends ConceptClassSnapshot> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
  }

  @Override
  public ConceptClassSnapshot get(SnapshotContainer<?> version, MdVertexDAOIF mdVertex)
  {
    QueryFactory factory = new QueryFactory();

    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ((LabeledPropertyGraphTypeVersion) version));

    ConceptClassSnapshotQuery query = new ConceptClassSnapshotQuery(factory);
    query.WHERE(query.EQ(vQuery.getChild()));
    query.AND(query.getGraphMdVertex().EQ(mdVertex.getOid()));

    try (OIterator<? extends ConceptClassSnapshot> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
  }

  @Override
  public JsonObject toDTO(ConceptClassSnapshot snapshot, VertexObject vertex)
  {
    JsonObject data = new JsonObject();

    List<? extends MdAttributeConcreteDAOIF> mdAttributes = MdVertexDAO.get(snapshot.getGraphMdVertexOid()).definesAttributes();

    for (MdAttributeConcreteDAOIF mdAttribute : mdAttributes)
    {
      String attributeName = mdAttribute.definesAttribute();

      if (!attributeName.equals("code"))
      {

        Object value = vertex.getObjectValue(attributeName);

        if (value != null)
        {
          if (mdAttribute instanceof MdAttributeTermDAOIF)
          {
            // throw new UnsupportedOperationException();
          }
          else if (value instanceof Number)
          {
            data.addProperty(mdAttribute.definesAttribute(), (Number) value);
          }
          else if (value instanceof Boolean)
          {
            data.addProperty(mdAttribute.definesAttribute(), (Boolean) value);
          }
          else if (value instanceof String)
          {
            data.addProperty(mdAttribute.definesAttribute(), (String) value);
          }
          else if (value instanceof Character)
          {
            data.addProperty(mdAttribute.definesAttribute(), (Character) value);
          }
          else if (value instanceof Date)
          {
            data.addProperty(mdAttribute.definesAttribute(), DateFormatter.formatDate((Date) value, false));
          }
        }
      }
    }

    JsonObject json = new JsonObject();
    json.addProperty("code", vertex.getValue("code"));
    json.add("label", LocalizedValueConverter.convert(vertex.getEmbeddedComponent(DefaultAttribute.DISPLAY_LABEL.getName())).toJSON());
    json.add("data", data);

    return json;
  }

  protected void assignPermissions(ComponentIF component)
  {
  }
}
