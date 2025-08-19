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

import java.util.Locale;
import java.util.Set;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.spatial4j.shape.Shape;

import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.spatial.shape.OShapeFactory;
import com.runwaysdk.business.graph.EdgeObject;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.MdAttributeClassificationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeGraphRefDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalEmbeddedDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdClassificationDAOIF;
import com.runwaysdk.dataaccess.MdEmbeddedGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.EdgeObjectDAO;
import com.runwaysdk.dataaccess.graph.EdgeObjectDAOIF;
import com.runwaysdk.dataaccess.graph.EmbeddedGraphObjectDAO;
import com.runwaysdk.dataaccess.graph.GraphObjectDAO;
import com.runwaysdk.dataaccess.graph.ResultSetConverterIF;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.Attribute;
import com.runwaysdk.dataaccess.graph.attributes.AttributeEmbedded;
import com.runwaysdk.dataaccess.graph.attributes.AttributeGraphRef;
import com.runwaysdk.dataaccess.graph.attributes.AttributeGraphRef.ID;
import com.runwaysdk.dataaccess.metadata.MdAttributeEmbeddedDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdGraphClassDAO;
import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;
import com.runwaysdk.localization.LocalizationFacade;

import net.geoprism.registry.graph.DataSource;
import net.geoprism.registry.model.ClassificationType;

public abstract class PrefixedResultSetConverter implements ResultSetConverterIF
{
  public static final String                  VERTEX_PREFIX = "v";

  public static final String                  ATTR_PREFIX   = "attr";

  private DataSourceBusinessServiceIF         sourceService;

  private ClassificationBusinessServiceIF     classificationService;

  private ClassificationTypeBusinessServiceIF classificationTypeService;

  public PrefixedResultSetConverter()
  {
    this.sourceService = ServiceFactory.getBean(DataSourceBusinessServiceIF.class);
    this.classificationService = ServiceFactory.getBean(ClassificationBusinessServiceIF.class);
  }

  protected Object build(String prefix, OResult element)
  {
    String className = element.getProperty(prefix + ".@class");
    MdGraphClassDAOIF mdGraph = MdGraphClassDAO.getMdGraphClassByTableName(className);

    if (mdGraph instanceof MdVertexDAOIF)
    {
      VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance((MdVertexDAOIF) mdGraph);

      populateDAO(prefix, element, vertexDAO);

      return this.convertFromDAO(vertexDAO);
    }
    else if (mdGraph instanceof MdEmbeddedGraphClassDAOIF)
    {
      EmbeddedGraphObjectDAO embeddedDAO = EmbeddedGraphObjectDAO.newInstance((MdEmbeddedGraphClassDAOIF) mdGraph);

      populateDAO(prefix, element, embeddedDAO);

      return this.convertFromDAO(embeddedDAO);
    }
    else
    {
      throw new UnsupportedOperationException();
    }
  }

  protected void populateDAO(String prefix, OResult result, GraphObjectDAO vertexDAO)
  {
    Object rid = result.getProperty(prefix + "." + "@rid");

    vertexDAO.setIsNew(false);
    vertexDAO.setAppliedToDB(true);
    vertexDAO.setRID(rid);

    Attribute[] attributes = vertexDAO.getAttributeArray();

    for (Attribute attribute : attributes)
    {
      MdAttributeConcreteDAOIF mdAttribute = attribute.getMdAttribute();

      if (mdAttribute instanceof MdAttributeLocalEmbeddedDAOIF)
      {
        AttributeEmbedded embedded = (AttributeEmbedded) attribute;

        String defaultLocale = result.getProperty(prefix + "." + LocalizedValue.DEFAULT_LOCALE);

        embedded.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, defaultLocale);

        Set<Locale> locales = LocalizationFacade.getInstalledLocales();

        for (Locale locale : locales)
        {
          String columnName = prefix + "." + locale.toString();

          if (result.hasProperty(columnName))
          {
            embedded.setValue(locale.toString(), result.getProperty(columnName));
          }
        }
      }
      else
      {
        String columnName = mdAttribute.getColumnName();
        Object value = result.getProperty(prefix + "." + columnName);

        if (value != null)
        {
          if (mdAttribute instanceof MdAttributeGeometryDAOIF)
          {
            OResult doc = (OResult) value;

            Shape shape = OShapeFactory.INSTANCE.fromObject(doc);
            Geometry geometry = OShapeFactory.INSTANCE.toGeometry(shape);

            attribute.setValueInternal(geometry);
          }
          else if (mdAttribute instanceof MdAttributeClassificationDAOIF)
          {
            if ( ( value instanceof ORecordId ))
            {
              ORecordId ref = (ORecordId) value;

              this.classificationService.getByRid(ref.toString()).ifPresent(source -> {
                attribute.setValueInternal(source.getOid());

                ( (AttributeGraphRef) attribute ).setId(new ID(source.getOid(), source.getRID()));
              });
            }
            else
            {
              throw new UnsupportedOperationException();
            }

          }
          else if (mdAttribute instanceof MdAttributeGraphRefDAOIF)
          {
            MdClassDAOIF mdVertex = ( (MdAttributeGraphRefDAOIF) mdAttribute ).getReferenceMdVertexDAOIF();

            if ( ( value instanceof ORecordId ) && mdVertex.definesType().equals(DataSource.CLASS))
            {
              ORecordId ref = (ORecordId) value;
              this.sourceService.getByRid(ref.toString()).ifPresent(source -> {
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
      }
    }

  }

  protected Object convertFromDAO(Object dao)
  {
    if (dao instanceof VertexObjectDAOIF)
    {
      return VertexObject.instantiate((VertexObjectDAO) dao);
    }
    else if (dao instanceof EdgeObjectDAOIF)
    {
      return EdgeObject.instantiate((EdgeObjectDAO) dao);
    }
    else
    {
      return dao;
    }
  }
}
