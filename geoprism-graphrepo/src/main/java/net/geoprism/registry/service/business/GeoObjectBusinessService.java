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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.Attribute;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.GeoObjectOverTime;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.commongeoregistry.adapter.dataaccess.UnknownTermException;
import org.commongeoregistry.adapter.dataaccess.ValueOverTimeCollectionDTO;
import org.commongeoregistry.adapter.dataaccess.ValueOverTimeDTO;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.commongeoregistry.adapter.metadata.AttributeDataSourceType;
import org.commongeoregistry.adapter.metadata.AttributeLocalType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.locationtech.jts.geom.Geometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.EdgeObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassificationDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTimeCollection;
import com.runwaysdk.dataaccess.metadata.graph.MdGraphClassDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.AbstractClassification;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.dashboard.GeometryUpdateException;
import net.geoprism.ontology.Classifier;
import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.DataNotFoundException;
import net.geoprism.registry.DuplicateGeoObjectCodeException;
import net.geoprism.registry.DuplicateGeoObjectException;
import net.geoprism.registry.DuplicateGeoObjectMultipleException;
import net.geoprism.registry.OriginException;
import net.geoprism.registry.RequiredAttributeException;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.conversion.ServerGeoObjectStrategyIF;
import net.geoprism.registry.conversion.TermConverter;
import net.geoprism.registry.conversion.VertexGeoObjectStrategy;
import net.geoprism.registry.etl.export.GeoObjectExportFormat;
import net.geoprism.registry.etl.export.GeoObjectJsonExporter;
import net.geoprism.registry.etl.upload.ClassifierVertexCache;
import net.geoprism.registry.graph.DataSource;
import net.geoprism.registry.graph.GeoVertex;
import net.geoprism.registry.graph.HierarchicalRelationshipType;
import net.geoprism.registry.graph.InheritedHierarchyAnnotation;
import net.geoprism.registry.io.TermValueException;
import net.geoprism.registry.model.BusinessObject;
import net.geoprism.registry.model.Classification;
import net.geoprism.registry.model.ClassificationType;
import net.geoprism.registry.model.EdgeConstant;
import net.geoprism.registry.model.EdgeDirection;
import net.geoprism.registry.model.EdgeValueOverTime;
import net.geoprism.registry.model.GeoObjectMetadata;
import net.geoprism.registry.model.GeoObjectTypeMetadata;
import net.geoprism.registry.model.GraphType;
import net.geoprism.registry.model.LocationInfo;
import net.geoprism.registry.model.LocationInfoHolder;
import net.geoprism.registry.model.ServerChildTreeNode;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.model.ServerParentTreeNode;
import net.geoprism.registry.model.graph.EdgeVertexType;
import net.geoprism.registry.model.graph.VertexComponent;
import net.geoprism.registry.model.graph.VertexServerGeoObject;
import net.geoprism.registry.model.graph.VertexServerGeoObject.EdgeComparator;
import net.geoprism.registry.query.ServerGeoObjectQuery;
import net.geoprism.registry.query.graph.VertexGeoObjectQuery;
import net.geoprism.registry.service.business.VertexAndEdgeResultSetConverter.GeoObjectAndEdge;
import net.geoprism.registry.service.business.VertexAndEdgeResultSetConverter.VertexAndEdge;
import net.geoprism.registry.service.permission.AllowAllGeoObjectPermissionService;
import net.geoprism.registry.service.permission.GeoObjectPermissionServiceIF;
import net.geoprism.registry.view.GeoObjectSplitView;
import net.geoprism.registry.view.ServerParentTreeNodeOverTime;

@Service
public class GeoObjectBusinessService extends RegistryLocalizedValueConverter implements GeoObjectBusinessServiceIF
{
  private static final Logger                   logger = LoggerFactory.getLogger(GeoObjectBusinessService.class);

  @Autowired
  protected GeoObjectPermissionServiceIF        permissionService;

  @Autowired
  protected GeoObjectTypeBusinessServiceIF      gotService;

  @Autowired
  protected HierarchyTypeBusinessServiceIF      htService;

  @Autowired
  protected BusinessTypeBusinessServiceIF       businessTypeService;

  @Autowired
  protected BusinessEdgeTypeBusinessServiceIF   businessEdgeTypeService;

  @Autowired
  protected BusinessObjectBusinessServiceIF     businessObjectService;

  @Autowired
  protected ClassificationTypeBusinessServiceIF cTypeService;

  @Autowired
  protected ClassificationBusinessServiceIF     cService;

  @Autowired
  protected DataSourceBusinessServiceIF         sourceService;

  public GeoObjectBusinessService()
  {
    this(new AllowAllGeoObjectPermissionService());
  }

  public GeoObjectBusinessService(GeoObjectPermissionServiceIF permissionService)
  {
    this.permissionService = permissionService;
  }

  @Override
  public JsonObject getAll(String gotCode, String hierarchyCode, Date since, Boolean includeLevel, String format, String externalSystemId, Integer pageNumber, Integer pageSize)
  {
    GeoObjectExportFormat goef = null;
    if (format != null && format.length() > 0)
    {
      goef = GeoObjectExportFormat.valueOf(format);
    }

    GeoObjectJsonExporter exporter = new GeoObjectJsonExporter(gotCode, hierarchyCode, since, includeLevel, goef, null, pageSize, pageNumber);

    try
    {
      return exporter.export();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ParentTreeNode addChild(String parentCode, String parentGeoObjectTypeCode, String childCode, String childGeoObjectTypeCode, String hierarchyCode, Date startDate, Date endDate, String uid, DataSource source, boolean validateOrigin)
  {
    ServerGeoObjectIF parent = this.getGeoObjectByCode(parentCode, parentGeoObjectTypeCode, true);
    ServerGeoObjectIF child = this.getGeoObjectByCode(childCode, childGeoObjectTypeCode, true);
    ServerHierarchyType ht = ServerHierarchyType.get(hierarchyCode);

    ServiceFactory.getGeoObjectRelationshipPermissionService().enforceCanAddChild(ht.getOrganization().getCode(), parent.getType(), child.getType());

    return addChild(parent, child, ht, startDate, endDate, uid, source, validateOrigin).toNode(false);
  }

  @Override
  public void removeChild(String parentCode, String parentGeoObjectTypeCode, String childCode, String childGeoObjectTypeCode, String hierarchyCode, Date startDate, Date endDate, boolean validateOrigin)
  {
    ServerGeoObjectIF parent = this.getGeoObjectByCode(parentCode, parentGeoObjectTypeCode, true);
    ServerGeoObjectIF child = this.getGeoObjectByCode(childCode, childGeoObjectTypeCode, true);
    ServerHierarchyType ht = ServerHierarchyType.get(hierarchyCode);

    ServiceFactory.getGeoObjectRelationshipPermissionService().enforceCanRemoveChild(ht.getOrganization().getCode(), parent.getType(), child.getType());

    removeChild(parent, child, hierarchyCode, startDate, endDate, validateOrigin);
  }

  @Override
  @Transaction
  public ServerGeoObjectIF apply(GeoObject dto, Date startDate, Date endDate, boolean isNew, boolean isImport, boolean validateOrigin)
  {
    ServerGeoObjectType type = ServerGeoObjectType.get(dto.getType().getCode());
    ServerGeoObjectStrategyIF strategy = this.getStrategy(type);

    if (isNew)
    {
      permissionService.enforceCanCreate(type.getOrganization().getCode(), type);
    }
    else
    {
      permissionService.enforceCanWrite(type.getOrganization().getCode(), type);
    }

    ServerGeoObjectIF geoObject = strategy.constructFromGeoObject(dto, isNew);
    geoObject.setDate(startDate);

    populate(geoObject, dto, startDate, endDate);

    try
    {
      apply(geoObject, isImport, validateOrigin);

      // Return the refreshed copy of the geoObject
      ServerGeoObjectIF object = strategy.getGeoObjectByUid(geoObject.getUid());
      object.setDate(startDate);
      return object;
    }
    catch (DuplicateDataException e)
    {
      handleDuplicateDataException(type, e);

      throw e;
    }
  }

  @Override
  public void handleDuplicateDataException(ServerGeoObjectType type, DuplicateDataException e)
  {
    if (e.getAttributes().size() == 0)
    {
      throw e;
    }
    else if (e.getAttributes().size() == 1)
    {
      MdAttributeDAOIF attr = e.getAttributes().get(0);

      if (VertexServerGeoObject.isCodeAttribute(attr))
      {
        DuplicateGeoObjectCodeException ex = new DuplicateGeoObjectCodeException();
        ex.setGeoObjectType(type.getLabel().getValue());
        ex.setValue(e.getValues().get(0));
        throw ex;
      }
      else
      {
        DuplicateGeoObjectException ex = new DuplicateGeoObjectException();
        ex.setGeoObjectType(type.getLabel().getValue());
        ex.setValue(e.getValues().get(0));
        ex.setAttributeName(VertexServerGeoObject.getAttributeLabel(type, attr));
        throw ex;
      }
    }
    else
    {
      List<String> attrLabels = new ArrayList<String>();

      for (MdAttributeDAOIF attr : e.getAttributes())
      {
        attrLabels.add(VertexServerGeoObject.getAttributeLabel(type, attr));
      }

      DuplicateGeoObjectMultipleException ex = new DuplicateGeoObjectMultipleException();
      ex.setAttributeLabels(StringUtils.join(attrLabels, ", "));
      throw ex;
    }
  }

  @Override
  public boolean trySetValue(ServerGeoObjectIF sgo, String attributeName, Object value)
  {
    return false;
  }

  @Override
  @Transaction
  public void apply(ServerGeoObjectIF sgo, boolean isImport, boolean validateOrigin)
  {
    if (!isImport && !sgo.isNew() && !sgo.getType().isGeometryEditable() && sgo.isModified(DefaultAttribute.GEOMETRY.getName()))
    {
      throw new GeometryUpdateException();
    }

    if (validateOrigin)
    {
      if (!sgo.getType().getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    final boolean isNew = sgo.isNew() || sgo.getValue(GeoVertex.CREATEDATE) == null;

    if (isNew)
    {
      sgo.setValue(GeoVertex.CREATEDATE, new Date());
    }

    sgo.setValue(GeoVertex.LASTUPDATEDATE, new Date());

    if (sgo.getInvalid() == null)
    {
      sgo.setInvalid(false);
    }

    validate(sgo);

    sgo.apply();
  }

  protected void validate(ServerGeoObjectIF sgo)
  {
    // this.validateCOTAttr(DefaultAttribute.EXISTS.getName());

    // this.validateCOTAttr(DefaultAttribute.DISPLAY_LABEL.getName());

    String code = sgo.getCode();

    if (code == null || code.length() == 0)
    {
      RequiredAttributeException ex = new RequiredAttributeException();
      ex.setAttributeLabel(GeoObjectTypeMetadata.getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));
      throw ex;
    }
  }

  @Override
  @Transaction
  public ServerGeoObjectIF apply(GeoObject object, boolean isNew, boolean isImport, boolean validateOrigin)
  {
    return apply(object, null, null, isNew, isImport, validateOrigin);
  }

  @Override
  public ServerGeoObjectIF fromDTO(GeoObjectOverTime goTime, boolean isNew)
  {
    ServerGeoObjectType type = ServerGeoObjectType.get(goTime.getType().getCode());

    return fromDTO(type, goTime, isNew);
  }

  @Override
  public ServerGeoObjectIF fromDTO(ServerGeoObjectType type, GeoObjectOverTime goTime, boolean isNew)
  {
    ServerGeoObjectStrategyIF strategy = this.getStrategy(type);

    if (isNew)
    {
      permissionService.enforceCanCreate(type.getOrganization().getCode(), type);
    }
    else
    {
      permissionService.enforceCanWrite(type.getOrganization().getCode(), type);
    }

    ServerGeoObjectIF goServer = strategy.constructFromGeoObjectOverTime(goTime, isNew);

    populate(goServer, goTime);

    return goServer;
  }

  @Override
  @Transaction
  public ServerGeoObjectIF apply(GeoObjectOverTime goTime, boolean isNew, boolean isImport, boolean validateOrigin)
  {
    ServerGeoObjectType type = ServerGeoObjectType.get(goTime.getType().getCode());

    ServerGeoObjectIF goServer = this.fromDTO(type, goTime, isNew);

    try
    {
      apply(goServer, isImport, validateOrigin);

      // Return the refreshed copy of the geoObject
      return this.getStrategy(type).getGeoObjectByUid(goServer.getUid());
    }
    catch (DuplicateDataException e)
    {
      handleDuplicateDataException(type, e);

      throw e;
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void populate(ServerGeoObjectIF sgo, GeoObject dto, Date startDate, Date endDate)
  {
    Map<String, AttributeType> attributes = dto.getType().getAttributeMap();
    attributes.forEach((attributeName, attribute) -> {
      if (attributeName.equals(DefaultAttribute.INVALID.getName()) || attributeName.equals(DefaultAttribute.EXISTS.getName()) || attributeName.equals(DefaultAttribute.DISPLAY_LABEL.getName()) || attributeName.equals(DefaultAttribute.CODE.getName()) || attributeName.equals(DefaultAttribute.UID.getName()) || attributeName.equals(GeoVertex.LASTUPDATEDATE) || attributeName.equals(GeoVertex.CREATEDATE) || attributeName.equals(DefaultAttribute.ALT_IDS.getName()))
      {
        // Ignore the attributes
      }
      else if (sgo.hasAttribute(attributeName))
      {
        if (attribute instanceof AttributeTermType)
        {
          Iterator<String> it = (Iterator<String>) dto.getValue(attributeName);

          if (it.hasNext())
          {
            String code = it.next();

            Term root = ( (AttributeTermType) attribute ).getRootTerm();
            String parent = TermConverter.buildClassifierKeyFromTermCode(root.getCode());

            String classifierKey = Classifier.buildKey(parent, code);
            Classifier classifier = Classifier.getByKey(classifierKey);

            sgo.setValue(attributeName, classifier.getOid(), startDate, endDate);
          }
          else
          {
            sgo.setValue(attributeName, (String) null, startDate, endDate);
          }
        }
        else if (attribute instanceof AttributeDataSourceType)
        {
          String value = (String) dto.getValue(attributeName);

          if (!StringUtils.isBlank(value))
          {
            DataSource source = this.sourceService.getByCode(value).orElseThrow(() -> {
              throw new ProgrammingErrorException("Unable to find source with code [" + value + "]");
            });

            sgo.setValue(attributeName, source, startDate, endDate);
          }
          else
          {
            sgo.setValue(attributeName, (String) null, startDate, endDate);
          }
        }
        else if (attribute instanceof AttributeClassificationType)
        {
          String value = (String) dto.getValue(attributeName);

          if (value != null)
          {
            this.cService.get((AttributeClassificationType) attribute, value).ifPresent(classification -> {
              sgo.setValue(attributeName, classification.getVertex(), startDate, endDate);
            });
          }
          else
          {
            sgo.setValue(attributeName, (String) null, startDate, endDate);
          }
        }
        else
        {
          Object value = dto.getValue(attributeName);

          // if (value != null)
          // {
          // sgo.getVertex().setValue(attributeName, value, startDate, endDate);
          // }
          // else
          // {
          // sgo.getVertex().setValue(attributeName, (String) null, startDate,
          // endDate);
          // }

          sgo.setValue(attributeName, value, startDate, endDate);
        }
      }
    });

    sgo.setInvalid(dto.getInvalid());
    sgo.setUid(dto.getUid());
    sgo.setCode(dto.getCode());
    sgo.setExists(dto.getExists(), startDate, endDate);
    sgo.setDisplayLabel(dto.getDisplayLabel(), startDate, endDate);
    sgo.setGeometry(dto.getGeometry(), startDate, endDate);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void populate(ServerGeoObjectIF sgo, GeoObjectOverTime goTime)
  {
    Map<String, AttributeType> attributes = goTime.getType().getAttributeMap();
    attributes.forEach((attributeName, attribute) -> {
      if (!attribute.isChangeOverTime() || attributeName.equals(DefaultAttribute.INVALID.getName()) || attributeName.equals(DefaultAttribute.CODE.getName()) || attributeName.equals(DefaultAttribute.UID.getName()) || attributeName.equals(GeoVertex.LASTUPDATEDATE) || attributeName.equals(GeoVertex.CREATEDATE) || attributeName.equals(DefaultAttribute.ALT_IDS.getName()))
      {
        // Ignore the attributes
      }
      // else if (attributeName.equals(DefaultAttribute.GEOMETRY.getName()))
      // {
      // for (ValueOverTimeDTO votDTO : goTime.getAllValues(attributeName))
      // {
      // Geometry geom = goTime.getGeometry(votDTO.getStartDate());
      //
      // sgo.setGeometry(geom, votDTO.getStartDate(), votDTO.getEndDate());
      // }
      // }
      // else if
      // (attributeName.equals(DefaultAttribute.DISPLAY_LABEL.getName()))
      // {
      // sgo.getValuesOverTime(attributeName).clear();
      // for (ValueOverTimeDTO votDTO : goTime.getAllValues(attributeName))
      // {
      // LocalizedValue label = (LocalizedValue) votDTO.getValue();
      //
      // sgo.setDisplayLabel(label, votDTO.getStartDate(),
      // votDTO.getEndDate());
      // }
      // }
      else if (sgo.hasAttribute(attributeName))
      {
        ValueOverTimeCollectionDTO collection = goTime.getAllValues(attributeName);

        ValueOverTimeCollection c = new ValueOverTimeCollection();

        for (ValueOverTimeDTO votDTO : collection)
        {
          if (attribute instanceof AttributeTermType)
          {
            Iterator<String> it = (Iterator<String>) votDTO.getValue();

            if (it.hasNext())
            {
              String code = it.next();

              Term root = ( (AttributeTermType) attribute ).getRootTerm();
              String parent = TermConverter.buildClassifierKeyFromTermCode(root.getCode());

              String classifierKey = Classifier.buildKey(parent, code);
              Classifier classifier = Classifier.getByKey(classifierKey);

              c.add(new ValueOverTime(votDTO.getStartDate(), votDTO.getEndDate(), classifier.getOid()));
            }
            else
            {
              c.add(new ValueOverTime(votDTO.getStartDate(), votDTO.getEndDate(), (String) null));
            }
          }
          else if (attribute instanceof AttributeDataSourceType)
          {
            String value = (String) votDTO.getValue();

            if (!StringUtils.isBlank(value))
            {
              DataSource source = this.sourceService.getByCode(value).orElseThrow(() -> {
                throw new ProgrammingErrorException("Unable to find source with code [" + value + "]");
              });

              // sgo.setValue(attributeName, source.getOid(),
              // votDTO.getStartDate(), votDTO.getEndDate());
              c.add(new ValueOverTime(votDTO.getStartDate(), votDTO.getEndDate(), source.getOid()));

            }
            else
            {
              c.add(new ValueOverTime(votDTO.getStartDate(), votDTO.getEndDate(), (String) null));
              // sgo.setValue(attributeName, (String) null,
              // votDTO.getStartDate(), votDTO.getEndDate());
            }
          }
          else if (attribute instanceof AttributeClassificationType)
          {
            String value = (String) votDTO.getValue();

            if (value != null)
            {
              this.cService.get((AttributeClassificationType) attribute, value).ifPresent(classification -> {
                sgo.setValue(attributeName, classification.getVertex(), votDTO.getStartDate(), votDTO.getEndDate());

                c.add(new ValueOverTime(votDTO.getStartDate(), votDTO.getEndDate(), classification.getVertex()));
              });

            }
            else
            {
              c.add(new ValueOverTime(votDTO.getStartDate(), votDTO.getEndDate(), (String) null));
            }
          }
          else if (attribute instanceof AttributeClassificationType)
          {
            String value = (String) votDTO.getValue();

            if (value != null)
            {
              this.sourceService.getByCode(value).ifPresent(source -> {
                sgo.setValue(attributeName, source, votDTO.getStartDate(), votDTO.getEndDate());

                c.add(new ValueOverTime(votDTO.getStartDate(), votDTO.getEndDate(), source));
              });
            }
            else
            {
              c.add(new ValueOverTime(votDTO.getStartDate(), votDTO.getEndDate(), (String) null));
            }
          }
          else
          {
            Object value = votDTO.getValue();

            c.add(new ValueOverTime(votDTO.getOid(), votDTO.getStartDate(), votDTO.getEndDate(), value));
          }
        }

        sgo.setValuesOverTime(attributeName, c);
      }
    });

    ValueOverTimeCollection geometries = new ValueOverTimeCollection();

    for (ValueOverTimeDTO votDTO : goTime.getAllValues(DefaultAttribute.GEOMETRY.getName()))
    {
      Geometry geom = goTime.getGeometry(votDTO.getStartDate());

      geometries.add(new ValueOverTime(votDTO.getStartDate(), votDTO.getEndDate(), geom));
    }

    sgo.setValuesOverTime(DefaultAttribute.GEOMETRY.getName(), geometries);

    sgo.setUid(goTime.getUid());
    sgo.setCode(goTime.getCode());
    sgo.setInvalid(goTime.getInvalid());
  }

  @Override
  @Transaction
  public ServerGeoObjectIF split(GeoObjectSplitView view)
  {
    ServerGeoObjectType type = ServerGeoObjectType.get(view.getTypeCode());
    ServerGeoObjectStrategyIF strategy = this.getStrategy(type);

    final ServerGeoObjectIF source = strategy.getGeoObjectByCode(view.getSourceCode());
    source.setDate(view.getDate());

    ServerGeoObjectIF target = strategy.newInstance();
    target.setDate(view.getDate());
    populate(target, toGeoObject(source, view.getDate()), view.getDate(), view.getDate());
    target.setCode(view.getTargetCode());
    target.setDisplayLabel(view.getLabel());

    apply(target, false, true);

    final ServerParentTreeNode sNode = getParentGeoObjects(source, null, null, false, false, view.getDate());

    final List<ServerParentTreeNode> sParents = sNode.getParents();

    for (ServerParentTreeNode sParent : sParents)
    {
      final ServerGeoObjectIF parent = sParent.getGeoObject();
      final ServerHierarchyType hierarchyType = sParent.getHierarchyType();

      addParent(target, parent, hierarchyType, view.getDate(), null, sParent.getUid(), null, true);
    }

    return target;
  }

  @Override
  public ServerGeoObjectIF newInstance(ServerGeoObjectType type)
  {
    ServerGeoObjectStrategyIF strategy = this.getStrategy(type);

    return strategy.newInstance();
  }

  @Override
  public ServerGeoObjectIF getGeoObject(GeoObject go)
  {
    ServerGeoObjectType type = ServerGeoObjectType.get(go.getType().getCode());

    ServerGeoObjectStrategyIF strategy = this.getStrategy(type);

    return strategy.constructFromGeoObject(go, false);
  }

  @Override
  public ServerGeoObjectIF getGeoObject(GeoObjectOverTime timeGO)
  {
    ServerGeoObjectType type = ServerGeoObjectType.get(timeGO.getType().getCode());

    ServerGeoObjectStrategyIF strategy = this.getStrategy(type);

    return strategy.constructFromGeoObjectOverTime(timeGO, false);
  }

  @Override
  public ServerGeoObjectIF getGeoObjectByCode(String code, String typeCode)
  {
    return this.getGeoObjectByCode(code, ServerGeoObjectType.get(typeCode));
  }

  @Override
  public ServerGeoObjectIF getGeoObjectByCode(String code, String typeCode, boolean throwException)
  {
    return this.getGeoObjectByCode(code, ServerGeoObjectType.get(typeCode), throwException);
  }

  @Override
  public ServerGeoObjectIF getGeoObjectByCode(String code, ServerGeoObjectType type)
  {
    return this.getGeoObjectByCode(code, type, true);
  }

  @Override
  public ServerGeoObjectIF getGeoObjectByCode(String code, ServerGeoObjectType type, boolean throwException)
  {
    this.permissionService.enforceCanRead(type.getOrganization().getCode(), type);

    ServerGeoObjectStrategyIF strategy = this.getStrategy(type);

    ServerGeoObjectIF geoObject = strategy.getGeoObjectByCode(code);

    if (geoObject == null && throwException)
    {
      DataNotFoundException ex = new DataNotFoundException("Could not find a GeoObject with code [" + code + "].");
      ex.setTypeLabel(GeoObjectMetadata.get().getClassDisplayLabel());
      ex.setDataIdentifier(code);
      ex.setAttributeLabel(GeoObjectMetadata.get().getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));

      throw ex;
    }

    return geoObject;
  }

  @Override
  public ServerGeoObjectIF getGeoObject(String uid, String typeCode)
  {
    ServerGeoObjectType type = ServerGeoObjectType.get(typeCode);

    this.permissionService.enforceCanRead(type.getOrganization().getCode(), type);

    ServerGeoObjectStrategyIF strategy = this.getStrategy(type);
    ServerGeoObjectIF object = strategy.getGeoObjectByUid(uid);

    if (object == null)
    {
      DataNotFoundException ex = new DataNotFoundException();
      ex.setTypeLabel(type.getLabel().getValue());
      ex.setAttributeLabel(GeoObjectMetadata.get().getAttributeDisplayLabel(DefaultAttribute.UID.getName()));
      ex.setDataIdentifier(uid);
      throw ex;
    }

    return object;
  }

  @Override
  public ServerGeoObjectStrategyIF getStrategy(ServerGeoObjectType type)
  {
    return new VertexGeoObjectStrategy(type);
  }

  @Override
  public ServerGeoObjectQuery createQuery(ServerGeoObjectType type, Date date)
  {
    return new VertexGeoObjectQuery(type, date);
  }

  @Override
  public ServerGeoObjectQuery createQuery(String typeCode)
  {
    ServerGeoObjectType type = ServerGeoObjectType.get(typeCode);

    return this.createQuery(type, null);
  }

  @Override
  public boolean hasData(ServerHierarchyType serverHierarchyType, ServerGeoObjectType childType)
  {
    return VertexServerGeoObject.hasData(serverHierarchyType, childType);
  }

  @Override
  public void removeAllEdges(ServerHierarchyType hierarchyType, ServerGeoObjectType childType, boolean validateOrigin)
  {
    VertexServerGeoObject.removeAllEdges(hierarchyType, childType);
  }

  @Override

  public JsonObject doesGeoObjectExistAtRange(Date startDate, Date endDate, String typeCode, String code)
  {
    VertexServerGeoObject vsgo = (VertexServerGeoObject) new GeoObjectBusinessService().getGeoObjectByCode(code, typeCode);

    JsonObject jo = new JsonObject();

    jo.addProperty("exists", vsgo.existsAtRange(startDate, endDate));
    jo.addProperty("invalid", vsgo.getInvalid());

    return jo;
  }

  @Override
  public List<VertexServerGeoObject> getAncestors(ServerGeoObjectIF sgo, ServerHierarchyType hierarchy)
  {
    return getAncestors(sgo, hierarchy, false, false);
  }

  @Override
  public List<VertexServerGeoObject> getAncestors(ServerGeoObjectIF sgo, ServerHierarchyType hierarchy, boolean includeNonExist, boolean includeInherited)
  {
    if (includeInherited)
    {
      List<ServerGeoObjectType> typeAncestors = gotService.getTypeAncestors(sgo.getType(), hierarchy, true);

      return buildAncestorVertexQueryFast(sgo, hierarchy, typeAncestors);
    }
    else
    {
      return buildAncestorQuery(sgo, hierarchy, includeNonExist);
    }
  }

  protected List<VertexServerGeoObject> buildAncestorQuery(ServerGeoObjectIF sgo, ServerHierarchyType hierarchy, boolean includeNonExist)
  {
    if (sgo.getDate() == null)
    {
      StringBuilder statement = new StringBuilder();
      statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
      statement.append(" SELECT FROM (");
      statement.append("   TRAVERSE in('" + hierarchy.getObjectEdge().getDBClassName() + "') FROM :rid");
      statement.append(" )");
      statement.append(" WHERE invalid = false");

      if (!includeNonExist)
      {
        statement.append(" AND last(out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "')[attributeName = 'exists']).value = true");

      }
      statement.append(")");

      GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
      query.setParameter("rid", sgo.getVertex().getRID());

      return VertexServerGeoObject.processTraverseResults(query.getResults(), sgo.getDate());
    }
    else
    {
      StringBuilder statement = new StringBuilder();
      statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
      statement.append(" SELECT FROM (");
      statement.append("   TRAVERSE in('" + hierarchy.getObjectEdge().getDBClassName() + "')[:date BETWEEN startDate AND endDate] FROM :rid");
      statement.append(" )");
      statement.append(" WHERE invalid = false");

      if (!includeNonExist)
      {
        statement.append(" AND last(out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "')[attributeName = 'exists' AND :date BETWEEN startDate AND endDate]).value = true");

      }
      statement.append(")");

      GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
      query.setParameter("rid", sgo.getVertex().getRID());
      query.setParameter("date", sgo.getDate());

      return VertexServerGeoObject.processTraverseResults(query.getResults(), sgo.getDate());
    }
  }

  /**
   * 
   * @param hierarchy
   * @param parents
   *          The parent types, sorted from the top to the bottom
   * @return
   */
  private List<VertexServerGeoObject> buildAncestorVertexQueryFast(ServerGeoObjectIF sgo, ServerHierarchyType hierarchy, List<ServerGeoObjectType> parents)
  {
    LinkedList<ServerHierarchyType> inheritancePath = new LinkedList<ServerHierarchyType>();
    inheritancePath.add(hierarchy);

    for (int i = parents.size() - 1; i >= 0; --i)
    {
      ServerGeoObjectType parent = parents.get(i);

      if (gotService.isRoot(parent, hierarchy))
      {
        ServerHierarchyType inheritedHierarchy = gotService.getInheritedHierarchy(parent, hierarchy);

        if (inheritedHierarchy != null)
        {
          inheritancePath.addFirst(inheritedHierarchy);
        }
      }
    }

    String dbClassName = sgo.getMdClass().getDBClassName();

    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
    statement.append("SELECT FROM (");

    for (ServerHierarchyType hier : inheritancePath)
    {
      if (sgo.getDate() != null)
      {
        statement.append("TRAVERSE inE('" + hier.getObjectEdge().getDBClassName() + "')[:date between startDate AND endDate].outV() FROM (");
      }
      else
      {
        statement.append("TRAVERSE inE('" + hier.getObjectEdge().getDBClassName() + "').outV() FROM (");
      }
    }

    statement.append("SELECT FROM " + dbClassName + " WHERE @rid=:rid");

    for (ServerHierarchyType hier : inheritancePath)
    {
      statement.append(")");
    }

    if (sgo.getDate() != null)
    {
      statement.append(") WHERE last(out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "')[attributeName = 'exists' AND :date BETWEEN startDate AND endDate]).value = true");
    }
    else
    {
      statement.append(") WHERE last(out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "')[attributeName = 'exists']).value = true");
    }
    statement.append(")");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("rid", sgo.getVertex().getRID());

    if (sgo.getDate() != null)
    {
      query.setParameter("date", sgo.getDate());
    }

    return VertexServerGeoObject.processTraverseResults(query.getResults(), sgo.getDate());
  }

  @Override
  @Transaction
  public void removeChild(ServerGeoObjectIF sgo, ServerGeoObjectIF child, String hierarchyCode, Date startDate, Date endDate, boolean validateOrigin)
  {
    ServerHierarchyType hierarchyType = ServerHierarchyType.get(hierarchyCode);
    removeParent(child, sgo, hierarchyType, startDate, endDate, validateOrigin);
  }

  @Override
  public void removeParent(ServerGeoObjectIF sgo, ServerGeoObjectIF parent, ServerHierarchyType hierarchyType, Date startDate, Date endDate, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      if (!hierarchyType.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    EdgeObject edge = sgo.getEdge(parent, hierarchyType, startDate, endDate);
    edge.delete();
    //
    // this.getVertex().removeParent( ( (VertexComponent) parent ).getVertex(),
    // hierarchyType.getMdEdge());
  }

  @Override
  public GeoObject toGeoObject(ServerGeoObjectIF sgo, Date date)
  {
    return toGeoObject(sgo, date, true);
  }

  @Override
  public GeoObject toGeoObject(ServerGeoObjectIF sgo, Date date, boolean includeExternalIds)
  {
    GeoObjectType dto = sgo.getType().toDTO();
    Map<String, Attribute> attributeMap = GeoObject.buildAttributeMap(dto);

    GeoObject geoObj = new GeoObject(dto, sgo.getType().getGeometryType(), attributeMap);

    Map<String, AttributeType> attributes = dto.getAttributeMap();
    attributes.forEach((attributeName, attribute) -> {
      if (attributeName.equals(DefaultAttribute.TYPE.getName()) || attributeName.equals(DefaultAttribute.GEOMETRY.getName()))
      {
        // Ignore
      }
      else if (sgo.hasAttribute(attributeName))
      {
        Object value = sgo.getValue(attributeName, date);

        if (value != null)
        {
          if (attribute instanceof AttributeTermType)
          {
            Classifier classifier = (Classifier) value;

            try
            {
              geoObj.setValue(attributeName, classifier.getClassifierId());
            }
            catch (UnknownTermException e)
            {
              TermValueException ex = new TermValueException();
              ex.setAttributeLabel(e.getAttribute().getLabel().getValue());
              ex.setCode(e.getCode());

              throw e;
            }
          }
          else if (attribute instanceof AttributeDataSourceType)
          {
//            ID id = (ID) value;
//
//            DataSource source = this.sourceService.getByRid(id.getRid().toString()).orElseThrow();
            
            DataSource source = this.sourceService.get((String) value);
            geoObj.setValue(attributeName, source.getCode());
          }
          else if (attribute instanceof AttributeClassificationType)
          {
//            ID id = (ID) value;
            String classificationTypeCode = ( (AttributeClassificationType) attribute ).getClassificationType();
            ClassificationType classificationType = this.cTypeService.getByCode(classificationTypeCode);

            Classification classification = this.cService.getByOid(classificationType, (String) value).orElseThrow(() -> {
//            Classification classification = this.cService.getByRid(id.getRid().toString()).orElseThrow(() -> {
              TermValueException ex = new TermValueException();
              ex.setAttributeLabel(attribute.getLabel().getValue());
              ex.setCode(value.toString());

              throw ex;
            });

            geoObj.setValue(attributeName, classification.toTerm());
          }
          else
          {
            geoObj.setValue(attributeName, value);
          }
        }
      }
    });

    geoObj.setUid(sgo.getValue(DefaultAttribute.UID.getName()));
    geoObj.setCode(sgo.getValue(DefaultAttribute.CODE.getName()));
    geoObj.setGeometry(sgo.getGeometry(date));
    geoObj.setDisplayLabel(sgo.getDisplayLabel(date));
    geoObj.setExists(sgo.getExists(date));
    geoObj.setInvalid(sgo.getInvalid());

    if (sgo.isNew())// && !vertex.isAppliedToDB())
    {
      geoObj.setUid(UUID.randomUUID().toString());
    }

    return geoObj;
  }

  @Override
  public JsonArray getHierarchiesForGeoObject(ServerGeoObjectIF sgo, Date date)
  {
    ServerGeoObjectType geoObjectType = sgo.getType();

    List<ServerHierarchyType> hierarchyTypes = ServiceFactory.getMetadataCache().getAllHierarchyTypes();
    JsonArray hierarchies = new JsonArray();

    for (ServerHierarchyType sType : hierarchyTypes)
    {
      if (ServiceFactory.getHierarchyPermissionService().canWrite(sType.getOrganization().getCode()))
      {
        // Note: Ordered ancestors always includes self
        List<ServerGeoObjectType> pTypes = sType.getAncestors(geoObjectType);

        ParentTreeNode ptnAncestors = getParentGeoObjects(sgo, sType, null, true, true, date).toNode(true);

        if (pTypes.size() > 1)
        {
          JsonObject object = new JsonObject();
          object.addProperty("code", sType.getCode());
          object.addProperty("label", sType.getLabel().getValue());

          JsonArray pArray = new JsonArray();

          for (ServerGeoObjectType pType : pTypes)
          {

            if (!pType.getCode().equals(geoObjectType.getCode()))
            {
              JsonObject pObject = new JsonObject();
              pObject.addProperty("code", pType.getCode());
              pObject.addProperty("label", pType.getLabel().getValue());

              List<ParentTreeNode> ptns = ptnAncestors.findParentOfType(pType.getCode());
              for (ParentTreeNode ptn : ptns)
              {
                if (ptn.getHierachyType().getCode().equals(sType.getCode()))
                {
                  pObject.add("ptn", ptn.toJSON());
                  break; // TODO Sibling ancestors
                }
              }

              pArray.add(pObject);
            }
          }

          object.add("parents", pArray);

          hierarchies.add(object);
        }
      }
    }

    if (hierarchies.size() == 0)
    {
      /*
       * This is a root type so include all hierarchies
       */

      for (ServerHierarchyType hierarchyType : hierarchyTypes)
      {
        if (ServiceFactory.getHierarchyPermissionService().canWrite(hierarchyType.getOrganization().getCode()))
        {
          JsonObject object = new JsonObject();
          object.addProperty("code", hierarchyType.getCode());
          object.addProperty("label", hierarchyType.getLabel().getValue());
          object.add("parents", new JsonArray());

          hierarchies.add(object);
        }
      }
    }

    return hierarchies;
  }

  @Override
  public GeoObjectOverTime toGeoObjectOverTime(ServerGeoObjectIF sgo)
  {
    return toGeoObjectOverTime(sgo, true);
  }

  @Override
  public GeoObjectOverTime toGeoObjectOverTime(ServerGeoObjectIF sgo, boolean generateUid)
  {
    return toGeoObjectOverTime(sgo, generateUid, null);
  }

  @Override
  public GeoObjectOverTime toGeoObjectOverTime(ServerGeoObjectIF sgo, boolean generateUid, ClassifierVertexCache classifierCache)
  {
    GeoObjectType typeDto = sgo.getType().toDTO();
    Map<String, ValueOverTimeCollectionDTO> votAttributeMap = GeoObjectOverTime.buildVotAttributeMap(typeDto);
    Map<String, Attribute> attributeMap = GeoObjectOverTime.buildAttributeMap(typeDto);

    GeoObjectOverTime geoObj = new GeoObjectOverTime(typeDto, votAttributeMap, attributeMap);

    Map<String, AttributeType> attributes = typeDto.getAttributeMap();
    attributes.forEach((attributeName, attribute) -> {
      if (attribute instanceof AttributeLocalType)
      {
        ValueOverTimeCollection votc = sgo.getValuesOverTime(attributeName);
        ValueOverTimeCollectionDTO votcDTO = new ValueOverTimeCollectionDTO(attribute);

        for (ValueOverTime vot : votc)
        {
          LocalizedValue value = sgo.getValueLocalized(attributeName, vot.getStartDate());

          ValueOverTimeDTO votDTO = new ValueOverTimeDTO(vot.getOid(), vot.getStartDate(), vot.getEndDate(), votcDTO);
          votDTO.setValue(value);
          votcDTO.add(votDTO);
        }

        geoObj.setValueCollection(attributeName, votcDTO);
      }
      // else if (attributeName.equals(DefaultAttribute.GEOMETRY.getName()))
      // {
      // ValueOverTimeCollection votc =
      // this.getValuesOverTime(this.getGeometryAttributeName());
      //
      // for (ValueOverTime vot : votc)
      // {
      // Object value = vot.getValue();
      //
      // geoObj.setValue(attributeName, value, vot.getStartDate(),
      // vot.getEndDate());
      // }
      // }
      else if (sgo.hasAttribute(attributeName))
      {
        if (attribute.isChangeOverTime())
        {
          ValueOverTimeCollection votc = sgo.getValuesOverTime(attributeName);
          ValueOverTimeCollectionDTO votcDTO = new ValueOverTimeCollectionDTO(attribute);

          for (ValueOverTime vot : votc)
          {
            Object value = vot.getValue();

            if (value != null)
            {
              if (attribute instanceof AttributeTermType)
              {
                Classifier classifier = Classifier.get((String) value);

                try
                {
                  ValueOverTimeDTO votDTO = new ValueOverTimeDTO(vot.getOid(), vot.getStartDate(), vot.getEndDate(), votcDTO);
                  votDTO.setValue(classifier.getClassifierId());
                  votcDTO.add(votDTO);
                }
                catch (UnknownTermException e)
                {
                  TermValueException ex = new TermValueException();
                  ex.setAttributeLabel(e.getAttribute().getLabel().getValue());
                  ex.setCode(e.getCode());

                  throw e;
                }
              }
              else if (attribute instanceof AttributeDataSourceType)
              {
//                ID id = (ID) value;
//
//                DataSource source = this.sourceService.getByRid(id.getRid().toString()).orElseThrow();
                DataSource source = this.sourceService.get((String) value);

                ValueOverTimeDTO votDTO = new ValueOverTimeDTO(vot.getOid(), vot.getStartDate(), vot.getEndDate(), votcDTO);
                votDTO.setValue(source.getCode());
                votcDTO.add(votDTO);
              }
              else if (attribute instanceof AttributeClassificationType)
              {
//                ID id = (ID) value;
//
//                Classification classification = this.cService.getByRid(id.getRid().toString()).orElseThrow(() -> {
                
                String classificationTypeCode = ( (AttributeClassificationType) attribute ).getClassificationType();
                ClassificationType classificationType = this.cTypeService.getByCode(classificationTypeCode);

                Classification classification = this.cService.getByOid(classificationType, (String) value).orElseThrow(() -> {                
                  TermValueException ex = new TermValueException();
                  ex.setAttributeLabel(attribute.getLabel().getValue());
                  ex.setCode(value.toString());

                  throw ex;
                });

                ValueOverTimeDTO votDTO = new ValueOverTimeDTO(vot.getOid(), vot.getStartDate(), vot.getEndDate(), votcDTO);
                votDTO.setValue(classification.toTerm());
                votcDTO.add(votDTO);
              }
              else
              {
                ValueOverTimeDTO votDTO = new ValueOverTimeDTO(vot.getOid(), vot.getStartDate(), vot.getEndDate(), votcDTO);
                votDTO.setValue(value);
                votcDTO.add(votDTO);
              }
            }
            // }
          }

          geoObj.setValueCollection(attributeName, votcDTO);
        }
        else
        {
          Object value = sgo.getValue(attributeName);

          if (value != null)
          {
            if (attribute instanceof AttributeTermType)
            {
              Classifier classifier = Classifier.get((String) value);

              try
              {
                geoObj.setValue(attributeName, classifier.getClassifierId());
              }
              catch (UnknownTermException e)
              {
                TermValueException ex = new TermValueException();
                ex.setAttributeLabel(e.getAttribute().getLabel().getValue());
                ex.setCode(e.getCode());

                throw e;
              }
            }
            else if (attribute instanceof AttributeClassificationType)
            {
              String classificationTypeCode = ( (AttributeClassificationType) attribute ).getClassificationType();
              ClassificationType classificationType = this.cTypeService.getByCode(classificationTypeCode);
              MdClassificationDAOIF mdClassificationDAO = classificationType.getMdClassification();

              VertexObject classifier = null;
              if (classifierCache != null)
              {
                classifier = classifierCache.getClassifier(classificationTypeCode, value.toString().trim());
              }

              if (classifier == null)
              {
                MdVertexDAOIF mdVertexDAO = mdClassificationDAO.getReferenceMdVertexDAO();

                classifier = VertexObject.get(mdVertexDAO, (String) value);

                if (classifier != null && classifierCache != null)
                {
                  classifierCache.putClassifier(classificationTypeCode, value.toString().trim(), classifier);
                }
              }

              try
              {
                geoObj.setValue(attributeName, classifier.getObjectValue(AbstractClassification.CODE));
              }
              catch (UnknownTermException e)
              {
                TermValueException ex = new TermValueException();
                ex.setAttributeLabel(e.getAttribute().getLabel().getValue());
                ex.setCode(e.getCode());

                throw e;
              }
            }
            else
            {
              geoObj.setValue(attributeName, value);
            }
          }
        }
      }
    });

    if (generateUid && sgo.isNew())// && !vertex.isAppliedToDB())
    {
      geoObj.setUid(UUID.randomUUID().toString());
    }
    else
    {
      geoObj.setUid(sgo.getValue(DefaultAttribute.UID.getName()));
    }

    ValueOverTimeCollection votc = sgo.getValuesOverTime(DefaultAttribute.GEOMETRY.getName());
    ValueOverTimeCollectionDTO votcDTO = new ValueOverTimeCollectionDTO(geoObj.getGeometryAttributeType());
    for (ValueOverTime vot : votc)
    {
      Object value = vot.getValue();

      ValueOverTimeDTO votDTO = new ValueOverTimeDTO(vot.getOid(), vot.getStartDate(), vot.getEndDate(), votcDTO);
      votDTO.setValue(value);
      votcDTO.add(votDTO);
    }
    geoObj.setValueCollection(DefaultAttribute.GEOMETRY.getName(), votcDTO);

    geoObj.setCode(sgo.getCode());

    return geoObj;
  }
  //
  // @Transaction
  // @Override
  // public ServerParentTreeNode addChild(ServerGeoObjectIF sgo,
  // ServerGeoObjectIF child, ServerHierarchyType hierarchy)
  // {
  // return addParent(child, sgo, hierarchy, uid);
  // }

  @Transaction
  @Override
  public ServerParentTreeNode addChild(ServerGeoObjectIF sgo, ServerGeoObjectIF child, ServerHierarchyType hierarchy, Date startDate, Date endDate, String uid, DataSource source, boolean validateOrigin)
  {
    return addParent(child, sgo, hierarchy, startDate, endDate, uid, source, validateOrigin);
  }

  @Override
  public ServerChildTreeNode getChildGeoObjects(ServerGeoObjectIF sgo, ServerHierarchyType hierarchy, String[] childrenTypes, Boolean recursive, Date date)
  {
    return internalGetChildGeoObjects(sgo, childrenTypes, recursive, hierarchy, date);
  }

  @Override
  public ServerParentTreeNode getParentGeoObjects(ServerGeoObjectIF sgo, ServerHierarchyType hierarchy, String[] parentTypes, Boolean recursive, Boolean includeInherited, Date date)
  {
    return internalGetParentGeoObjects(sgo, parentTypes, recursive, includeInherited, hierarchy, date);
  }

  @Override
  public ServerChildTreeNode getGraphChildGeoObjects(ServerGeoObjectIF sgo, GraphType graphType, Boolean recursive, Date date)
  {
    return sgo.getGraphChildren(graphType, recursive, date);
  }

  @Override
  public ServerParentTreeNode getGraphParentGeoObjects(ServerGeoObjectIF sgo, GraphType graphType, Boolean recursive, Boolean includeInherited, Date date)
  {
    return sgo.getGraphParents(graphType, recursive, date);
  }

  @Override
  public ServerParentTreeNode getParentsForHierarchy(ServerGeoObjectIF sgo, ServerHierarchyType hierarchy, Boolean recursive, Boolean includeInherited, Date date)
  {
    return internalGetParentGeoObjects(sgo, null, recursive, includeInherited, hierarchy, date);
  }

  @Override
  public ServerParentTreeNodeOverTime getParentsOverTime(ServerGeoObjectIF sgo, String[] parentTypes, Boolean recursive, Boolean includeInherited)
  {
    return internalGetParentOverTime(sgo, parentTypes, recursive, includeInherited);
  }

  @Override
  public void setParents(ServerGeoObjectIF sgo, ServerParentTreeNodeOverTime parentsOverTime, boolean validateOrigin)
  {
    parentsOverTime.enforceUserHasPermissionSetParents(sgo.getType().getCode(), false);

    final Collection<ServerHierarchyType> hierarchyTypes = parentsOverTime.getHierarchies();

    for (ServerHierarchyType hierarchyType : hierarchyTypes)
    {
      if (validateOrigin)
      {
        if (!hierarchyType.getOrigin().equals(GeoprismProperties.getOrigin()))
        {
          throw new OriginException();
        }
      }

      final List<ServerParentTreeNode> entries = parentsOverTime.getEntries(hierarchyType);

      this.removeAllEdges(sgo, hierarchyType, validateOrigin);

      final TreeSet<EdgeObject> edges = new TreeSet<EdgeObject>(new EdgeComparator());

      for (ServerParentTreeNode entry : entries)
      {
        final ServerGeoObjectIF parent = entry.getGeoObject();

        EdgeObject newEdge = sgo.getVertex().addParent( ( (VertexComponent) parent ).getVertex(), hierarchyType.getObjectEdge());
        newEdge.setValue(GeoVertex.START_DATE, entry.getStartDate());
        newEdge.setValue(GeoVertex.END_DATE, entry.getEndDate());
        newEdge.setValue(DefaultAttribute.UID.getName(), entry.getUid());
        newEdge.setValue(DefaultAttribute.DATA_SOURCE.getName(), entry.getSource());

        edges.add(newEdge);
      }

      for (EdgeObject e : edges)
      {
        e.apply();
      }
    }
  }

  @Override
  public void removeAllEdges(ServerGeoObjectIF sgo, ServerHierarchyType hierarchyType, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      if (!hierarchyType.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    // Delete the current edges and recreate the new ones
    final SortedSet<EdgeObject> edges = sgo.getEdges(hierarchyType);

    for (EdgeObject edge : edges)
    {
      edge.delete();
    }
  }

  private ServerChildTreeNode internalGetChildGeoObjects(ServerGeoObjectIF parent, String[] childrenTypes, Boolean recursive, ServerHierarchyType htIn, Date date)
  {
    ServerChildTreeNode tnRoot = new ServerChildTreeNode(parent, htIn, date, null, null, null, null);

    Map<String, Object> parameters = new HashedMap<String, Object>();
    parameters.put("rid", parent.getVertex().getRID());

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT " + VertexAndEdgeResultSetConverter.geoVertexColumns(parent.getMdClass()));
    statement.append(", " + VertexAndEdgeResultSetConverter.geoVertexAttributeColumns(parent.getType().definesAttributes()));
    statement.append(", edgeClass, edgeOid, edgeUid, edgeSource" + "\n");
    statement.append(" FROM ( " + "\n");
    statement.append("   SELECT v, v.out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') as attr");
    statement.append(", edgeClass, edgeOid, edgeUid, edgeSource" + "\n");
    statement.append("   FROM ( " + "\n");
    statement.append("     SELECT in as v, @class as edgeClass, oid as edgeOid, uid as edgeUid, dataSource.code as edgeSource" + "\n");
    statement.append("       FROM ( " + "\n");
    statement.append("         SELECT EXPAND( outE( ");

    if (htIn != null)
    {
      statement.append("'" + htIn.getObjectEdge().getDBClassName() + "'");
    }
    statement.append(")");

    if (childrenTypes != null && childrenTypes.length > 0)
    {
      statement.append("[");

      for (int i = 0; i < childrenTypes.length; i++)
      {
        ServerGeoObjectType type = ServerGeoObjectType.get(childrenTypes[i]);
        final String paramName = "p" + Integer.toString(i);

        if (i > 0)
        {
          statement.append(" OR ");
        }

        statement.append("in.@class = :" + paramName);

        parameters.put(paramName, type.getMdVertex().getDBClassName());
      }

      statement.append("]");
    }

    statement.append(" ) FROM :rid" + "\n");
    statement.append(" )) UNWIND attr )" + "\n");

    GraphQuery<VertexAndEdge> query = new GraphQuery<VertexAndEdge>(statement.toString(), parameters, new VertexAndEdgeResultSetConverter());

    List<VertexAndEdge> results = query.getResults();

    List<GeoObjectAndEdge> parents = VertexAndEdgeResultSetConverter.convertResults(results, date);

    for (GeoObjectAndEdge childAndEdge : parents)
    {
      ServerGeoObjectIF child = childAndEdge.geoObject;
      if (child.getRunwayId().equals(parent.getRunwayId()))
        continue;

      ServerChildTreeNode tnChild;

      final ServerHierarchyType rowHierarchy = ServerHierarchyType.get((MdEdgeDAOIF) MdGraphClassDAO.getMdGraphClassByTableName(childAndEdge.edgeClass));

      if (recursive)
      {
        tnChild = internalGetChildGeoObjects(child, childrenTypes, recursive, rowHierarchy, date);
      }
      else
      {
        DataSource source = this.sourceService.getByCode(childAndEdge.edgeSource).orElse(null);

        tnChild = new ServerChildTreeNode(child, rowHierarchy, date, null, childAndEdge.edgeOid, childAndEdge.edgeUid, source);
      }

      tnRoot.addChild(tnChild);
    }

    return tnRoot;
  }

  /*
   * SELECT v.@rid, v.@class, v.oid, v.code, attr.@rid, attr.oid, attr.@class,
   * attr.value, edgeClass, edgeOid FROM ( SELECT v, v.out('has_value',
   * 'has_geometry') as attr, edgeClass, edgeOid FROM ( SELECT out as v,
   * 
   * @class as edgeClass, oid as edgeOid FROM ( SELECT
   * EXPAND(inE('fasta_dmin_code')) FROM #366:1 ) ) UNWIND attr )
   */
  protected ServerParentTreeNode internalGetParentGeoObjects(ServerGeoObjectIF child, String[] parentTypes, boolean recursive, boolean includeInherited, ServerHierarchyType htIn, Date date)
  {
    ServerParentTreeNode tnRoot = new ServerParentTreeNode(child, htIn, date, null, null, null, null);

    Map<String, Object> parameters = new HashedMap<String, Object>();
    parameters.put("rid", child.getVertex().getRID());

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT " + VertexAndEdgeResultSetConverter.geoVertexColumns(child.getMdClass()));
    statement.append(", " + VertexAndEdgeResultSetConverter.geoVertexAttributeColumns(child.getType().definesAttributes()));
    statement.append(", edgeClass, edgeOid, edgeUid, edgeSource" + "\n");
    statement.append(" FROM ( " + "\n");
    statement.append("   SELECT v, v.out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') as attr");
    statement.append(", edgeClass, edgeOid, edgeUid, edgeSource" + "\n");
    statement.append("   FROM ( " + "\n");
    statement.append("     SELECT out as v, @class as edgeClass, oid as edgeOid, uid as edgeUid, dataSource.code as edgeSource" + "\n");
    statement.append("       FROM ( " + "\n");
    statement.append("         SELECT EXPAND( inE( ");

    if (htIn != null)
    {
      statement.append("'" + htIn.getObjectEdge().getDBClassName() + "'");
    }
    statement.append(")");

    if (date != null || ( parentTypes != null && parentTypes.length > 0 ))
    {
      statement.append("[");

      if (date != null)
      {
        statement.append(" :date BETWEEN startDate AND endDate");
        parameters.put("date", date);
      }

      if (parentTypes != null && parentTypes.length > 0)
      {
        if (date != null)
        {
          statement.append(" AND");
        }

        statement.append("(");
        for (int i = 0; i < parentTypes.length; i++)
        {
          ServerGeoObjectType type = ServerGeoObjectType.get(parentTypes[i]);
          final String paramName = "p" + Integer.toString(i);

          if (i > 0)
          {
            statement.append(" OR ");
          }

          statement.append("out.@class = :" + paramName);

          parameters.put(paramName, type.getMdVertex().getDBClassName());
        }
        statement.append(" )");
      }

      statement.append(" ]");
    }

    statement.append(" ) FROM :rid" + "\n");
    statement.append(" )) UNWIND attr )" + "\n");

    GraphQuery<VertexAndEdge> query = new GraphQuery<VertexAndEdge>(statement.toString(), parameters, new VertexAndEdgeResultSetConverter());

    List<VertexAndEdge> results = query.getResults();

    List<GeoObjectAndEdge> parents = VertexAndEdgeResultSetConverter.convertResults(results, date);

    for (GeoObjectAndEdge parentAndEdge : parents)
    {
      ServerGeoObjectIF parent = parentAndEdge.geoObject;

      if (child.getRunwayId().equals(parent.getRunwayId()))
        continue;

      ServerParentTreeNode tnParent;

      final ServerHierarchyType rowHierarchy = ServerHierarchyType.get((MdEdgeDAOIF) MdGraphClassDAO.getMdGraphClassByTableName(parentAndEdge.edgeClass));

      if (recursive)
      {
        if (includeInherited && gotService.isRoot(parent.getType(), rowHierarchy))
        {
          InheritedHierarchyAnnotation anno = InheritedHierarchyAnnotation.getByForHierarchical(rowHierarchy.getObject());

          if (anno != null)
          {
            ServerHierarchyType shtInherited = ServerHierarchyType.get(anno.getInheritedHierarchyCode());

            tnParent = internalGetParentGeoObjects(parent, parentTypes, recursive, includeInherited, shtInherited, date);
          }
          else
          {
            tnParent = internalGetParentGeoObjects(parent, parentTypes, recursive, includeInherited, rowHierarchy, date);
          }
        }
        else
        {
          tnParent = internalGetParentGeoObjects(parent, parentTypes, recursive, includeInherited, rowHierarchy, date);
        }
      }
      else
      {
        DataSource source = this.sourceService.getByCode(parentAndEdge.edgeSource).orElse(null);

        tnParent = new ServerParentTreeNode(parent, rowHierarchy, date, null, parentAndEdge.edgeOid, parentAndEdge.edgeUid, source);
      }

      tnRoot.addParent(tnParent);
    }

    return tnRoot;
  }

  protected ServerParentTreeNodeOverTime internalGetParentOverTime(ServerGeoObjectIF child, String[] parentTypes, boolean recursive, boolean includeInherited)
  {
    final ServerGeoObjectType cType = child.getType();
    final List<ServerHierarchyType> hierarchies = gotService.getHierarchies(cType);

    ServerParentTreeNodeOverTime response = new ServerParentTreeNodeOverTime(cType);

    for (ServerHierarchyType ht : hierarchies)
    {
      response.add(ht);
    }

    Map<String, Object> parameters = new HashedMap<String, Object>();
    parameters.put("rid", child.getVertex().getRID());

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT EXPAND(inE()");

    if (parentTypes != null && parentTypes.length > 0)
    {
      statement.append("[");

      for (int i = 0; i < parentTypes.length; i++)
      {
        ServerGeoObjectType type = ServerGeoObjectType.get(parentTypes[i]);

        if (i > 0)
        {
          statement.append(" OR ");
        }

        statement.append("out.@class = :a" + i);

        parameters.put("a" + Integer.toString(i), type.getMdVertex().getDBClassName());
      }

      statement.append("]");
    }

    statement.append(") FROM :rid");
    statement.append(" ORDER BY startDate ASC");

    GraphQuery<EdgeObject> query = new GraphQuery<EdgeObject>(statement.toString(), parameters);

    List<EdgeObject> edges = query.getResults();

    for (EdgeObject edge : edges)
    {
      MdEdgeDAOIF mdEdge = (MdEdgeDAOIF) edge.getMdClass();

      if (HierarchicalRelationshipType.isEdgeAHierarchyType(mdEdge))
      {
        ServerHierarchyType ht = ServerHierarchyType.get(mdEdge);

        VertexObject parentVertex = edge.getParent();

        MdVertexDAOIF mdVertex = (MdVertexDAOIF) parentVertex.getMdClass();

        ServerGeoObjectType parentType = ServerGeoObjectType.get(mdVertex);

        Date date = edge.getObjectValue(GeoVertex.START_DATE);
        Date endDate = edge.getObjectValue(GeoVertex.END_DATE);
        String oid = edge.getObjectValue(GeoVertex.OID);
        String uid = edge.getObjectValue(DefaultAttribute.UID.getName());
        DataSource source = this.sourceService.get(edge.getObjectValue(DefaultAttribute.DATA_SOURCE.getName()));

        ServerParentTreeNode tnRoot = new ServerParentTreeNode(child, null, date, null, oid, uid, source);
        tnRoot.setEndDate(endDate);
        tnRoot.setOid(oid);

        // TODO: HEADS UP - Refactor query to get edge, parent, and attribute
        // nodes in a single query????
        ServerGeoObjectIF parent = this.getGeoObjectByCode((String) parentVertex.getObjectValue(DefaultAttribute.CODE.getName()), parentType);

        ServerParentTreeNode tnParent;

        if (recursive)
        {
          if (includeInherited && gotService.isRoot(parentType, ht))
          {
            InheritedHierarchyAnnotation anno = InheritedHierarchyAnnotation.getByForHierarchical(ht);

            if (anno != null)
            {
              ServerHierarchyType shtInherited = ServerHierarchyType.get(anno.getInheritedHierarchyCode());

              tnParent = internalGetParentGeoObjects(parent, parentTypes, recursive, includeInherited, shtInherited, date);
            }
            else
            {
              tnParent = internalGetParentGeoObjects(parent, parentTypes, recursive, includeInherited, ht, date);
            }
          }
          else
          {
            tnParent = internalGetParentGeoObjects(parent, parentTypes, recursive, includeInherited, ht, date);
          }
        }
        else
        {
          tnParent = new ServerParentTreeNode(parent, ht, date, null, oid, uid, source);
        }

        tnRoot.addParent(tnParent);

        response.add(ht, tnRoot);
      }
    }

    return response;
  }

  @Override
  public ValueOverTimeCollection getParentCollection(ServerGeoObjectIF sgo, ServerHierarchyType hierarchyType)
  {
    ValueOverTimeCollection votc = new ValueOverTimeCollection();

    SortedSet<EdgeObject> edges = sgo.getEdges(hierarchyType);

    for (EdgeObject edge : edges)
    {
      final Date startDate = edge.getObjectValue(GeoVertex.START_DATE);
      final Date endDate = edge.getObjectValue(GeoVertex.END_DATE);
      final String uid = edge.getObjectValue(DefaultAttribute.UID.getName());

      VertexObject parentVertex = edge.getParent();
      MdVertexDAOIF mdVertex = (MdVertexDAOIF) parentVertex.getMdClass();
      ServerGeoObjectType parentType = ServerGeoObjectType.get(mdVertex);
      VertexServerGeoObject parent = new VertexServerGeoObject(parentType, parentVertex, new TreeMap<>(), startDate);

      votc.add(new EdgeValueOverTime(edge.getOid(), startDate, endDate, parent, uid));
    }

    return votc;
  }

  @Override
  public SortedSet<EdgeObject> setParentCollection(ServerGeoObjectIF sgo, ServerHierarchyType hierarchyType, ValueOverTimeCollection votc, DataSource source, boolean validateOrigin)
  {
    SortedSet<EdgeObject> resultEdges = new TreeSet<EdgeObject>(new EdgeComparator());
    SortedSet<EdgeObject> existingEdges = sgo.getEdges(hierarchyType);

    for (EdgeObject edge : existingEdges)
    {
      final Date startDate = edge.getObjectValue(GeoVertex.START_DATE);
      final Date endDate = edge.getObjectValue(GeoVertex.END_DATE);
      final String uid = edge.getObjectValue(DefaultAttribute.UID.getName());

      VertexObject parentVertex = edge.getParent();
      MdVertexDAOIF mdVertex = (MdVertexDAOIF) parentVertex.getMdClass();
      ServerGeoObjectType parentType = ServerGeoObjectType.get(mdVertex);
      final VertexServerGeoObject edgeGo = new VertexServerGeoObject(parentType, parentVertex, new TreeMap<>(), startDate);

      Optional<EdgeValueOverTime> existing = votc.stream().filter(vot -> vot instanceof EdgeValueOverTime).map(vot -> (EdgeValueOverTime) vot).filter(vot -> vot.getUid().equals(uid)).findFirst();

      existing.ifPresentOrElse(inVot -> {
        VertexServerGeoObject inGo = (VertexServerGeoObject) inVot.getValue();

        boolean hasValueChange = false;

        if ( ( inGo == null && edgeGo != null ) || ( inGo != null && edgeGo == null ))
        {
          hasValueChange = true;
        }
        else if ( ( inGo != null && edgeGo != null ) && !inGo.equals(edgeGo))
        {
          hasValueChange = true;
        }

        if (hasValueChange)
        {
          edge.delete();

          EdgeObject newEdge = addParentRaw(sgo, inGo.getVertex(), hierarchyType.getObjectEdge(), startDate, endDate, uid, source, validateOrigin);

          resultEdges.add(newEdge);
        }
        else
        {
          boolean hasChanges = false;

          Date inStartDate = inVot.getStartDate();
          Date inEndDate = inVot.getEndDate();

          if (!startDate.equals(inStartDate))
          {
            hasChanges = true;
            edge.setValue(GeoVertex.START_DATE, inStartDate);
          }

          if (!endDate.equals(inEndDate))
          {
            hasChanges = true;
            edge.setValue(GeoVertex.END_DATE, inEndDate);
          }

          if (hasChanges)
          {
            edge.apply();
          }

          resultEdges.add(edge);
        }
      }, () -> {
        edge.delete();
      });
    }

    // Handle new edges
    List<String> edgeUids = existingEdges.stream().map(e -> (String) e.getObjectValue(DefaultAttribute.UID.getName())).toList();

    votc.stream().map(vot -> {
      if (vot instanceof EdgeValueOverTime)
      {
        return (EdgeValueOverTime) vot;
      }

      return new EdgeValueOverTime(vot.getStartDate(), vot.getEndDate(), vot.getValue(), UUID.randomUUID().toString());
    }).filter(vot -> !edgeUids.contains(vot.getUid())).forEach(vot -> {
      EdgeObject newEdge = addParentRaw(sgo, ( (VertexServerGeoObject) vot.getValue() ).getVertex(), hierarchyType.getObjectEdge(), vot.getStartDate(), vot.getEndDate(), vot.getUid(), source, validateOrigin);

      resultEdges.add(newEdge);
    });

    return resultEdges;
  }

  @Override
  public ServerParentTreeNode addParent(ServerGeoObjectIF sgo, ServerGeoObjectIF parent, ServerHierarchyType hierarchyType, Date startDate, Date endDate, String uid, DataSource source, boolean validateOrigin)
  {
    htService.validateUniversalRelationship(hierarchyType, sgo.getType(), parent.getType());

    ValueOverTimeCollection votc = getParentCollection(sgo, hierarchyType);
    votc.add(new EdgeValueOverTime(startDate, endDate, parent, uid));

    SortedSet<EdgeObject> newEdges = setParentCollection(sgo, hierarchyType, votc, source, validateOrigin);
    EdgeObject edge = newEdges.first();

    ServerParentTreeNode node = new ServerParentTreeNode(sgo, hierarchyType, startDate, null, null, null, source);
    node.addParent(new ServerParentTreeNode(parent, hierarchyType, startDate, null, edge.getOid(), edge.getObjectValue(DefaultAttribute.UID.getName()), source));

    return node;
  }

  /**
   * Adds an edge, bypassing all validation (for performance reasons). Be
   * careful with this method!! You probably want to call addChild or addParent
   * instead.
   */
  @Override
  public EdgeObject addParentRaw(ServerGeoObjectIF sgo, VertexObject parent, MdEdgeDAOIF mdEdge, Date startDate, Date endDate, String uid, DataSource source, boolean validateOrigin)
  {
    EdgeObject newEdge = sgo.getVertex().addParent(parent, mdEdge);
    newEdge.setValue(GeoVertex.START_DATE, startDate);
    newEdge.setValue(GeoVertex.END_DATE, endDate);
    newEdge.setValue(DefaultAttribute.UID.getName(), uid);
    newEdge.setValue(DefaultAttribute.DATA_SOURCE.getName(), source);
    newEdge.apply();

    return newEdge;
  }

  /**
   * 
   * @param hierarchy
   * @param parents
   *          The parent types, sorted from the top to the bottom
   * @return
   */
  private List<ServerGeoObjectIF> buildAncestorSelectQueryFast(ServerGeoObjectIF sgo, ServerHierarchyType hierarchy, List<ServerGeoObjectType> parents)
  {
    LinkedList<ServerHierarchyType> inheritancePath = new LinkedList<ServerHierarchyType>();
    inheritancePath.add(hierarchy);

    for (int i = parents.size() - 1; i >= 0; --i)
    {
      ServerGeoObjectType parent = parents.get(i);

      if (gotService.isRoot(parent, hierarchy))
      {
        ServerHierarchyType inheritedHierarchy = gotService.getInheritedHierarchy(parent, hierarchy);

        if (inheritedHierarchy != null)
        {
          inheritancePath.addFirst(inheritedHierarchy);
        }
      }
    }

    // We only care about the code and the display label attributes
    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "')[attributeName = 'displayLabel'] FROM (");
    statement.append("SELECT FROM (");

    for (ServerHierarchyType hier : inheritancePath)
    {
      if (sgo.getDate() != null)
      {
        statement.append("TRAVERSE inE('" + hier.getObjectEdge().getDBClassName() + "')[:date between startDate AND endDate].outV() FROM (");
      }
      else
      {
        statement.append("TRAVERSE inE('" + hier.getObjectEdge().getDBClassName() + "').outV() FROM (");
      }
    }

    statement.append("SELECT FROM " + sgo.getType().getDBClassName() + " WHERE @rid=:rid");

    for (ServerHierarchyType hier : inheritancePath)
    {
      statement.append(")");
    }

    if (sgo.getDate() != null)
    {
      statement.append(") WHERE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "')[attributeName = 'exists' AND value = true AND :date BETWEEN startDate AND endDate].size() > 0");
    }
    else
    {
      statement.append(") WHERE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "')[attributeName = 'exists' AND value = true].size() > 0");
    }

    statement.append(")");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("rid", sgo.getVertex().getRID());

    if (sgo.getDate() != null)
    {
      query.setParameter("date", sgo.getDate());
    }

    return VertexServerGeoObject.processTraverseResults(query.getResults(), sgo.getDate());
  }

  @Override
  public Map<String, LocationInfo> getAncestorMap(ServerGeoObjectIF sgo, ServerHierarchyType hierarchy, List<ServerGeoObjectType> parents)
  {
    TreeMap<String, LocationInfo> map = new TreeMap<String, LocationInfo>();

    List<ServerGeoObjectIF> results = buildAncestorSelectQueryFast(sgo, hierarchy, parents);

    if (results.size() <= 1)
    {
      return map;
    }

    results.remove(0); // First result is the child object

    results.forEach(result -> {

      LocalizedValue localized = result.getDisplayLabel();

      ServerGeoObjectType type = null;

      for (ServerGeoObjectType parent : parents)
      {
        if (parent.getMdVertex().getDBClassName().equals(result.getType().getDBClassName()))
        {
          type = parent;
        }
      }

      if (type != null && localized != null)
      {
        LocationInfoHolder holder = new LocationInfoHolder(result.getCode(), localized, type);
        map.put(type.getCode(), holder);
      }
      else
      {
        logger.error("Could not find [" + sgo.getType().getDBClassName() + "] or the localized value was null.");
      }
    });

    return map;
  }

  @Override

  public JsonObject hasDuplicateLabel(Date date, String typeCode, String code, String label)
  {
    boolean inUse = VertexServerGeoObject.hasDuplicateLabel(date, typeCode, code, label);

    JsonObject jo = new JsonObject();
    jo.addProperty("labelInUse", inUse);

    return jo;
  }

  @Override
  public JsonArray getBusinessObjects(String typeCode, String code, String edgeTypeCode, String direction)
  {
    VertexServerGeoObject vsgo = (VertexServerGeoObject) this.getGeoObjectByCode(code, typeCode);

    List<BusinessObject> objects = null;

    BusinessEdgeType edgeType = this.businessEdgeTypeService.getByCodeOrThrow(edgeTypeCode);

    objects = this.getBusinessObjects(vsgo, edgeType, EdgeDirection.valueOf(direction));

    return objects.stream().map(object -> {
      return this.businessObjectService.toJSON(object);
    }).collect(() -> new JsonArray(), (array, element) -> array.add(element), (listA, listB) -> {
    });
  }

  @Override
  public List<BusinessObject> getBusinessObjects(VertexServerGeoObject object, BusinessEdgeType edgeType, EdgeDirection direction)
  {
    List<VertexObject> objects = direction.equals(EdgeDirection.PARENT) ? object.getVertex().getParents(edgeType.getMdEdgeDAO(), VertexObject.class) : object.getVertex().getChildren(edgeType.getMdEdgeDAO(), VertexObject.class);

    EdgeVertexType type = direction.equals(EdgeDirection.PARENT) ? this.businessEdgeTypeService.getParent(edgeType) : this.businessEdgeTypeService.getChild(edgeType);

    return objects.stream().map(o -> new BusinessObject(o, type.toBusinessType())).collect(Collectors.toList());
  }

}
