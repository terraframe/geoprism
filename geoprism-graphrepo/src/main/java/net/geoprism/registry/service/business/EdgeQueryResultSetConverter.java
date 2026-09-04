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

import java.util.Date;
import java.util.Locale;
import java.util.Set;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.spatial4j.shape.Shape;

import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.spatial.shape.OShapeFactory;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeGraphRefDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalEmbeddedDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.GraphObjectDAO;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.graph.ResultSetConverterIF;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.attributes.Attribute;
import com.runwaysdk.dataaccess.graph.attributes.AttributeEmbedded;
import com.runwaysdk.dataaccess.graph.attributes.AttributeGraphRef;
import com.runwaysdk.dataaccess.graph.attributes.AttributeGraphRef.ID;
import com.runwaysdk.dataaccess.metadata.MdAttributeEmbeddedDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdGraphClassDAO;
import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;
import com.runwaysdk.localization.LocalizationFacade;

import net.geoprism.registry.graph.ConceptVertex;
import net.geoprism.registry.graph.DataSource;

public class EdgeQueryResultSetConverter implements ResultSetConverterIF
{
  private final DataSourceBusinessServiceIF    sourceService;

  private final ConceptObjectBusinessServiceIF cObjectService;

  public EdgeQueryResultSetConverter()
  {
    this.sourceService = ServiceFactory.getBean(DataSourceBusinessServiceIF.class);
    this.cObjectService = ServiceFactory.getBean(ConceptObjectBusinessServiceIF.class);    
  }

  @Override
  public EdgeQueryResult convert(GraphRequest request, Object result)
  {
    OResult oresult = (OResult) result;

    VertexObject vertex = this.buildVertex(oresult);
    ORecordId originVertex = oresult.getProperty("originVertexRid");
    String edgeOid = oresult.getProperty("edgeOid");
    String edgeUid = oresult.getProperty("edgeUid");
    String edgeSource = oresult.getProperty("edgeSource");
    Date startDate = oresult.getProperty("startDate");
    Date endDate = oresult.getProperty("endDate");
    String edgeClass = oresult.getProperty("edgeClass");

    return new EdgeQueryResult(vertex, originVertex, edgeClass, edgeOid, edgeUid, edgeSource, startDate, endDate);
  }

  private VertexObject buildVertex(OResult result)
  {
    String className = result.getProperty("@class");

    if (className == null)
    {
      throw new UnsupportedOperationException("Unable to determine vertex class from result");
    }

    MdGraphClassDAOIF mdGraph = MdGraphClassDAO.getMdGraphClassByTableName(className);

    if (! ( mdGraph instanceof MdVertexDAOIF ))
    {
      throw new UnsupportedOperationException("Result class [" + className + "] is not a vertex class");
    }

    VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance((MdVertexDAOIF) mdGraph);

    this.populateDAO(result, vertexDAO);

    return VertexObject.instantiate(vertexDAO);
  }

  private void populateDAO(OResult result, GraphObjectDAO vertexDAO)
  {
    Object rid = result.getProperty("@rid");

    vertexDAO.setIsNew(false);
    vertexDAO.setAppliedToDB(true);
    vertexDAO.setRID(rid);

    Attribute[] attributes = vertexDAO.getAttributeArray();

    for (Attribute attribute : attributes)
    {
      MdAttributeConcreteDAOIF mdAttribute = attribute.getMdAttribute();

      if (mdAttribute instanceof MdAttributeLocalEmbeddedDAOIF)
      {
        this.populateLocalizedAttribute(result, (AttributeEmbedded) attribute);
      }
      else
      {
        this.populateAttribute(result, attribute, mdAttribute);
      }
    }
  }

  private void populateLocalizedAttribute(OResult result, AttributeEmbedded embedded)
  {
    String defaultLocale = result.getProperty(LocalizedValue.DEFAULT_LOCALE);

    embedded.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, defaultLocale);

    Set<Locale> locales = LocalizationFacade.getInstalledLocales();

    for (Locale locale : locales)
    {
      String columnName = locale.toString();

      if (result.hasProperty(columnName))
      {
        embedded.setValue(columnName, result.getProperty(columnName));
      }
    }
  }

  private void populateAttribute(OResult result, Attribute attribute, MdAttributeConcreteDAOIF mdAttribute)
  {
    String columnName = mdAttribute.getColumnName();

    Object value = result.getProperty(columnName);

    if (value == null)
    {
      return;
    }

    if (mdAttribute instanceof MdAttributeGeometryDAOIF)
    {
      OResult doc = (OResult) value;

      Shape shape = OShapeFactory.INSTANCE.fromObject(doc);

      Geometry geometry = OShapeFactory.INSTANCE.toGeometry(shape);

      attribute.setValueInternal(geometry);
    }
    else if (mdAttribute instanceof MdAttributeGraphRefDAOIF)
    {
      MdClassDAOIF referencedClass = ( (MdAttributeGraphRefDAOIF) mdAttribute ).getReferenceMdVertexDAOIF();

      if (value instanceof ORecordId && referencedClass.definesType().equals(DataSource.CLASS))
      {
        ORecordId ref = (ORecordId) value;

        this.sourceService.getByRid(ref.toString()).ifPresent(source -> {

          attribute.setValueInternal(source.getOid());

          ( (AttributeGraphRef) attribute ).setId(new ID(source.getOid(), source.getRID()));
        });
      }
      else if ( ( value instanceof ORecordId ) && referencedClass.definesType().equals(ConceptVertex.CLASS))
      {
        ORecordId ref = (ORecordId) value;

        this.cObjectService.getByRid(ref.toString()).ifPresent(source -> {
          attribute.setValueInternal(source.getOid());

          ( (AttributeGraphRef) attribute ).setId(new ID(source.getOid(), source.getRID()));
        });
      }      
      else
      {
        throw new UnsupportedOperationException();
      }
    }
    else if (mdAttribute instanceof MdAttributeEmbeddedDAO)
    {
      throw new UnsupportedOperationException();
    }
    else
    {
      attribute.setValueInternal(value);
    }
  }

  public static class EdgeQueryResult
  {
    private final VertexObject vertex;

    private final ORecordId    originVertex;

    private final String       edgeClass;

    private final String       edgeOid;

    private final String       edgeUid;

    private final String       edgeSource;

    private final Date         startDate;

    private final Date         endDate;

    public EdgeQueryResult(VertexObject vertex, ORecordId originVertex, String edgeClass, String edgeOid, String edgeUid, String edgeSource, Date startDate, Date endDate)
    {
      this.vertex = vertex;
      this.originVertex = originVertex;
      this.edgeClass = edgeClass;
      this.edgeOid = edgeOid;
      this.edgeUid = edgeUid;
      this.edgeSource = edgeSource;
      this.startDate = startDate;
      this.endDate = endDate;
    }

    public VertexObject getVertex()
    {
      return this.vertex;
    }

    public ORecordId getOriginVertex()
    {
      return this.originVertex;
    }

    public String getOriginVertexRid()
    {
      return this.originVertex != null ? this.originVertex.toString() : null;
    }

    public String getEdgeClass()
    {
      return this.edgeClass;
    }

    public String getEdgeOid()
    {
      return this.edgeOid;
    }

    public String getEdgeUid()
    {
      return this.edgeUid;
    }

    public String getEdgeSource()
    {
      return this.edgeSource;
    }

    public Date getStartDate()
    {
      return this.startDate;
    }

    public Date getEndDate()
    {
      return this.endDate;
    }
  }
}