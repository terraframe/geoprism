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
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
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
import net.geoprism.graph.BusinessTypeSnapshot;
import net.geoprism.graph.BusinessTypeSnapshotQuery;
import net.geoprism.graph.LabeledPropertyGraphTypeSnapshotQuery;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.registry.DateFormatter;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.SnapshotContainer;

@Service
public class BusinessTypeSnapshotBusinessService extends ObjectTypeBusinessService<BusinessTypeSnapshot> implements BusinessTypeSnapshotBusinessServiceIF
{
  @Transaction
  @Override
  public BusinessTypeSnapshot create(SnapshotContainer<?> version, JsonObject typeDto)
  {
    String code = typeDto.get(BusinessTypeSnapshot.CODE).getAsString();
    String origin = typeDto.has(BusinessTypeSnapshot.ORIGIN) ? typeDto.get(BusinessTypeSnapshot.ORIGIN).getAsString() : GeoprismProperties.getOrigin();
    String orgCode = typeDto.get(BusinessTypeSnapshot.ORGCODE).getAsString();
    String viewName = getTableName(code);
    LocalizedValue label = LocalizedValue.fromJSON(typeDto.get(BusinessTypeSnapshot.DISPLAYLABEL).getAsJsonObject());

    JsonArray attributes = typeDto.get("attributes").getAsJsonArray();

    MdVertex mdTable = createMdVertex(version, viewName, label, attributes);

    BusinessTypeSnapshot snapshot = new BusinessTypeSnapshot();
    snapshot.setGraphMdVertex(mdTable);
    snapshot.setLabelAttribute(typeDto.get(BusinessTypeSnapshot.LABELATTRIBUTE).getAsString());
    snapshot.setCode(code);
    snapshot.setOrgCode(orgCode);
    snapshot.setOrigin(origin);
    LocalizedValueConverter.populate(snapshot.getDisplayLabel(), label);
    snapshot.apply();

    version.addSnapshot(snapshot).apply();

    attributes.forEach(joAttr -> {
      AttributeType attributeType = AttributeType.parse(joAttr.getAsJsonObject());

      if (! ( attributeType instanceof AttributeTermType ))
      {
        this.createAttributeTypeSnapshot(snapshot, attributeType);
      }
    });

    return snapshot;
  }

  protected MdVertex createMdVertex(SnapshotContainer<?> version, String viewName, LocalizedValue label, JsonArray attributes)
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

      attributes.forEach(joAttr -> {
        AttributeType attributeType = AttributeType.parse(joAttr.getAsJsonObject());

        if (! ( attributeType instanceof AttributeTermType ))
        {
          this.createMdAttributeFromAttributeType(mdTable, attributeType);
        }
      });

      this.assignPermissions(mdTableDAO);

      return mdTable;
    }
    
    return null;
  }

  @Override
  public BusinessTypeSnapshot get(SnapshotContainer<?> version, String code)
  {
    QueryFactory factory = new QueryFactory();

    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ((LabeledPropertyGraphTypeVersion) version));

    BusinessTypeSnapshotQuery query = new BusinessTypeSnapshotQuery(factory);
    query.WHERE(query.EQ(vQuery.getChild()));
    query.AND(query.getCode().EQ(code));

    try (OIterator<? extends BusinessTypeSnapshot> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
  }

  @Override
  public BusinessTypeSnapshot get(SnapshotContainer<?> version, MdVertexDAOIF mdVertex)
  {
    QueryFactory factory = new QueryFactory();

    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ((LabeledPropertyGraphTypeVersion) version));

    BusinessTypeSnapshotQuery query = new BusinessTypeSnapshotQuery(factory);
    query.LEFT_JOIN_EQ(vQuery.getChild());
    query.AND(query.getGraphMdVertex().EQ(mdVertex.getOid()));

    try (OIterator<? extends BusinessTypeSnapshot> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
  }

  @Override
  public JsonObject toDTO(BusinessTypeSnapshot snapshot, VertexObject vertex)
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
