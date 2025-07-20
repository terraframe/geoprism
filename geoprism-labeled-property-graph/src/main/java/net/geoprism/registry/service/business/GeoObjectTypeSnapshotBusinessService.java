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

import java.util.Map;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.Attribute;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.dataaccess.UnknownTermException;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.graph.GeoObjectTypeSnapshot;
import net.geoprism.graph.GeoObjectTypeSnapshotQuery;
import net.geoprism.graph.LabeledPropertyGraphTypeSnapshotQuery;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.Classification;
import net.geoprism.registry.model.ClassificationType;
import net.geoprism.registry.model.SnapshotContainer;

@Service
public class GeoObjectTypeSnapshotBusinessService extends ObjectTypeBusinessService<GeoObjectTypeSnapshot> implements GeoObjectTypeSnapshotBusinessServiceIF
{
  public final String                         ROOT = "__ROOT__";

  @Autowired
  private ClassificationBusinessServiceIF     classificationService;

  @Autowired
  private ClassificationTypeBusinessServiceIF typeService;

  @Transaction
  @Override
  public GeoObjectTypeSnapshot createRoot(SnapshotContainer<?> version)
  {
    MdVertex graphMdVertex = null;

    if (version.createTablesWithSnapshot())
    {
      String viewName = getTableName("root_vertex");

      // Create the MdTable
      MdVertexDAO rootMdVertexDAO = MdVertexDAO.newInstance();
      rootMdVertexDAO.setValue(MdVertexInfo.NAME, viewName);
      rootMdVertexDAO.setValue(MdVertexInfo.PACKAGE, TABLE_PACKAGE);
      rootMdVertexDAO.setStructValue(MdVertexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Root Type");
      rootMdVertexDAO.setValue(MdVertexInfo.DB_CLASS_NAME, viewName);
      rootMdVertexDAO.setValue(MdVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      rootMdVertexDAO.setValue(MdVertexInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
      rootMdVertexDAO.setValue(MdVertexInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
      rootMdVertexDAO.apply();

      graphMdVertex = (MdVertex) BusinessFacade.get(rootMdVertexDAO);
    }

    GeoObjectTypeSnapshot snapshot = new GeoObjectTypeSnapshot();
    snapshot.setGraphMdVertex(graphMdVertex);
    snapshot.setCode(ROOT);
    snapshot.setIsAbstract(true);
    snapshot.setIsRoot(true);
    snapshot.setIsPrivate(true);
    LocalizedValueConverter.populate(snapshot.getDisplayLabel(), new LocalizedValue("Root Type"));
    LocalizedValueConverter.populate(snapshot.getDescription(), new LocalizedValue("Root Type"));
    snapshot.apply();

    version.addSnapshot(snapshot).apply();

    return snapshot;
  }

  @Transaction
  @Override
  public GeoObjectTypeSnapshot create(SnapshotContainer<?> version, JsonObject type)
  {
    GeoObjectTypeSnapshot parent = this.get(version, type.get(GeoObjectTypeSnapshot.PARENT).getAsString());

    String code = type.get(GeoObjectTypeSnapshot.CODE).getAsString();
    String orgCode = type.has(GeoObjectTypeSnapshot.ORGCODE) ? type.get(GeoObjectTypeSnapshot.ORGCODE).getAsString() : null;
    String origin = type.has(GeoObjectTypeSnapshot.ORIGIN) ? type.get(GeoObjectTypeSnapshot.ORIGIN).getAsString() : GeoprismProperties.getOrigin();
    String viewName = getTableName(code);
    boolean isAbstract = type.get(GeoObjectTypeSnapshot.ISABSTRACT).getAsBoolean();
    boolean isRoot = type.get(GeoObjectTypeSnapshot.ISROOT).getAsBoolean();
    boolean isPrivate = type.get(GeoObjectTypeSnapshot.ISPRIVATE).getAsBoolean();
    GeometryType geometryType = GeometryType.valueOf(type.get(GeoObjectTypeSnapshot.GEOMETRYTYPE).getAsString());
    LocalizedValue label = LocalizedValue.fromJSON(type.get(GeoObjectTypeSnapshot.DISPLAYLABEL).getAsJsonObject());
    LocalizedValue description = LocalizedValue.fromJSON(type.get(GeoObjectTypeSnapshot.DESCRIPTION).getAsJsonObject());
    JsonArray attributes = type.get("attributes").getAsJsonArray();

    MdVertex mdTable = createMdVertex(version, parent, viewName, isAbstract, geometryType, label, description, attributes);

    GeoObjectTypeSnapshot snapshot = new GeoObjectTypeSnapshot();
    snapshot.setGraphMdVertex(mdTable);
    snapshot.setCode(code);
    snapshot.setOrgCode(orgCode);
    snapshot.setOrigin(origin);
    snapshot.setGeometryType(geometryType.name());
    snapshot.setIsAbstract(isAbstract);
    snapshot.setIsRoot(isRoot);
    snapshot.setIsPrivate(isPrivate);
    LocalizedValueConverter.populate(snapshot.getDisplayLabel(), label);
    LocalizedValueConverter.populate(snapshot.getDescription(), description);
    snapshot.setParent(parent);
    snapshot.apply();

    version.addSnapshot(snapshot).apply();

    if (!isAbstract)
    {
      this.createAttributeTypeSnapshot(snapshot, geometryType);
    }

    attributes.forEach(joAttr -> {
      AttributeType attributeType = AttributeType.parse(joAttr.getAsJsonObject());

      if (! ( attributeType instanceof AttributeTermType ))
      {
        this.createMdAttributeFromAttributeType(mdTable, attributeType);

        this.createAttributeTypeSnapshot(snapshot, attributeType);
      }
    });

    return snapshot;
  }

  protected MdVertex createMdVertex(SnapshotContainer<?> version, GeoObjectTypeSnapshot parent, String viewName, boolean isAbstract, GeometryType geometryType, LocalizedValue label, LocalizedValue description, JsonArray attributes)
  {
    if (version.createTablesWithSnapshot())
    {
      // Create the MdTable
      MdVertexDAO mdTableDAO = MdVertexDAO.newInstance();
      mdTableDAO.setValue(MdVertexInfo.NAME, viewName);
      mdTableDAO.setValue(MdVertexInfo.PACKAGE, TABLE_PACKAGE);
      LocalizedValueConverter.populate(mdTableDAO, MdVertexInfo.DISPLAY_LABEL, label);
      LocalizedValueConverter.populate(mdTableDAO, MdVertexInfo.DESCRIPTION, description);
      mdTableDAO.setValue(MdVertexInfo.DB_CLASS_NAME, viewName);
      mdTableDAO.setValue(MdVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdTableDAO.setValue(MdVertexInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
      mdTableDAO.setValue(MdVertexInfo.SUPER_MD_VERTEX, parent.getGraphMdVertexOid());
      mdTableDAO.apply();

      final MdVertex mdTable = (MdVertex) BusinessFacade.get(mdTableDAO);

      if (!isAbstract)
      {
        createGeometryAttribute(geometryType, mdTableDAO);
      }

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
  public GeoObjectTypeSnapshot get(SnapshotContainer<?> version, String code)
  {
    QueryFactory factory = new QueryFactory();

    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ((LabeledPropertyGraphTypeVersion) version));

    GeoObjectTypeSnapshotQuery query = new GeoObjectTypeSnapshotQuery(factory);
    query.LEFT_JOIN_EQ(vQuery.getChild());
    query.AND(query.getCode().EQ(code));

    try (OIterator<? extends GeoObjectTypeSnapshot> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
  }

  @Override
  public GeoObjectTypeSnapshot get(SnapshotContainer<?> version, MdVertexDAOIF mdVertex)
  {
    QueryFactory factory = new QueryFactory();

    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ((LabeledPropertyGraphTypeVersion) version));

    GeoObjectTypeSnapshotQuery query = new GeoObjectTypeSnapshotQuery(factory);
    query.LEFT_JOIN_EQ(vQuery.getChild());
    query.AND(query.getGraphMdVertex().EQ(mdVertex.getOid()));

    try (OIterator<? extends GeoObjectTypeSnapshot> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
  }

  @Override
  public GeoObjectTypeSnapshot getRoot(SnapshotContainer<?> version)
  {
    QueryFactory factory = new QueryFactory();

    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ((LabeledPropertyGraphTypeVersion) version));

    GeoObjectTypeSnapshotQuery query = new GeoObjectTypeSnapshotQuery(factory);
    query.LEFT_JOIN_EQ(vQuery.getChild());
    query.AND(query.getIsRoot().EQ(true));

    try (OIterator<? extends GeoObjectTypeSnapshot> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
  }

  @Override
  public GeoObject toGeoObject(GeoObjectTypeSnapshot snapshot, VertexObject vertex)
  {
    return toGeoObject(vertex, snapshot.toGeoObjectType());
  }

  @Override
  public GeoObject toGeoObject(VertexObject vertex, GeoObjectType type)
  {
    Map<String, Attribute> attributeMap = GeoObject.buildAttributeMap(type);

    GeoObject geoObj = new GeoObject(type, type.getGeometryType(), attributeMap);

    Map<String, AttributeType> attributes = type.getAttributeMap();
    attributes.forEach((attributeName, attribute) -> {
      if (attributeName.equals(DefaultAttribute.TYPE.getName()))
      {
        // Ignore
      }
      else if (vertex.hasAttribute(attributeName))
      {
        Object value = vertex.getObjectValue(attributeName);

        if (value != null)
        {
          if (attribute instanceof AttributeTermType)
          {
            // Classifier classifier = Classifier.get((String) value);
            //
            // try
            // {
            // geoObj.setValue(attributeName, classifier.getClassifierId());
            // }
            // catch (UnknownTermException e)
            // {
            // TermValueException ex = new TermValueException();
            // ex.setAttributeLabel(e.getAttribute().getLabel().getValue());
            // ex.setCode(e.getCode());
            //
            // throw e;
            // }
          }
          else if (attribute instanceof AttributeClassificationType)
          {
            String classificationTypeCode = ( (AttributeClassificationType) attribute ).getClassificationType();
            ClassificationType classificationType = this.typeService.getByCode(classificationTypeCode);
            Classification classification = this.classificationService.getByOid(classificationType, (String) value);

            try
            {
              geoObj.setValue(attributeName, classification.toTerm());
            }
            catch (UnknownTermException e)
            {
              // TermValueException ex = new TermValueException();
              // ex.setAttributeLabel(e.getAttribute().getLabel().getValue());
              // ex.setCode(e.getCode());
              //
              // throw e;

              // TODO Change exception type

              throw new RuntimeException("Unable to find a classification with the code [" + e.getCode() + "] and attribute [" + e.getAttribute().getLabel().getValue() + "]");
            }
          }
          else
          {
            geoObj.setValue(attributeName, value);
          }
        }
      }
    });

    geoObj.setUid(vertex.getObjectValue(DefaultAttribute.UID.getName()));
    geoObj.setCode(vertex.getObjectValue(DefaultAttribute.CODE.getName()));
    geoObj.setGeometry(vertex.getObjectValue(DefaultAttribute.GEOMETRY.getName()));
    geoObj.setDisplayLabel(LocalizedValueConverter.convert(vertex.getEmbeddedComponent(DefaultAttribute.DISPLAY_LABEL.getName())));
    geoObj.setExists(true);
    geoObj.setInvalid(false);

    return geoObj;
  }

  protected void assignPermissions(ComponentIF component)
  {
  }
}
