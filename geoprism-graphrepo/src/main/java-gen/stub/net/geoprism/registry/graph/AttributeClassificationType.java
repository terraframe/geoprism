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
package net.geoprism.registry.graph;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeClassificationInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeClassificationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeClassificationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.model.Classification;
import net.geoprism.registry.model.ClassificationType;
import net.geoprism.registry.model.GeoObjectMetadata;
import net.geoprism.registry.model.GraphRefNodeValueStrategy;
import net.geoprism.registry.model.ValueStrategy;
import net.geoprism.registry.model.VertexValueStrategy;
import net.geoprism.registry.model.graph.GraphTableUtil;
import net.geoprism.registry.service.business.ClassificationBusinessServiceIF;
import net.geoprism.registry.service.business.ClassificationTypeBusinessServiceIF;
import net.geoprism.registry.service.business.ServiceFactory;

public class AttributeClassificationType extends AttributeClassificationTypeBase
{
  public static final String PREFIX           = "avc_";

  public static final String VALUE            = "value";

  @SuppressWarnings("unused")
  private static final long  serialVersionUID = -352072420;

  public AttributeClassificationType()
  {
    super();
  }

  @Override
  @Transaction
  public void apply()
  {
    if (!this.getIsChangeOverTime())
    {
      MdAttributeClassificationDAO mdAttribute = null;

      // Create the value vertex class
      if (this.isNew() && !this.isAppliedToDb())
      {
        // Create the MdAttribute on the MdVertex
        mdAttribute = MdAttributeClassificationDAO.newInstance();
      }
      else
      {
        // Update the precision and scale of the value attribute
        MdVertexDAOIF mdVertex = MdVertexDAO.get(this.getGeoObjectType().getMdVertexOid());

        mdAttribute = (MdAttributeClassificationDAO) mdVertex.definesAttribute(this.getCode()).getBusinessDAO();
      }

      populate(mdAttribute);

      mdAttribute.apply();
    }
    else
    {
      // Create the value vertex class
      if (this.isNew() && !this.isAppliedToDb())
      {
        String tableName = GraphTableUtil.generateTableName(PREFIX, "_classification");

        MdVertexDAOIF superVertex = MdVertexDAO.getMdVertexDAO(AttributeBasicValue.CLASS);

        MdVertexDAO mdVertex = MdVertexDAO.newInstance();
        mdVertex.setValue(MdVertexInfo.PACKAGE, RegistryConstants.UNIVERSAL_GRAPH_PACKAGE + ".value");
        mdVertex.setValue(MdVertexInfo.NAME, tableName);
        mdVertex.setValue(MdVertexInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
        mdVertex.setValue(MdVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
        mdVertex.setValue(MdVertexInfo.DB_CLASS_NAME, tableName);
        mdVertex.setValue(MdVertexInfo.SUPER_MD_VERTEX, superVertex.getOid());
        mdVertex.apply();

        // Create the value attribute
        MdAttributeClassificationDAO mdAttribute = MdAttributeClassificationDAO.newInstance();
        mdAttribute.setValue(MdAttributeClassificationInfo.DEFINING_MD_CLASS, mdVertex.getOid());
        mdAttribute.setValue(MdAttributeClassificationInfo.NAME, VALUE);
        mdAttribute.setStructValue(MdAttributeClassificationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Value");
        mdAttribute.setValue(MdAttributeClassificationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
        mdAttribute.setValue(MdAttributeClassificationInfo.REFERENCE_MD_CLASSIFICATION, this.getMdClassificationOid());

        if (!StringUtils.isBlank(this.getObjectValue(ROOTTERM)))
        {
          mdAttribute.setValue(MdAttributeClassificationInfo.ROOT, this.getObjectValue(ROOTTERM));
        }

        mdAttribute.apply();

        this.setValueVertexId(mdVertex.getOid());
      }
      else
      {
        // Update the precision and scale of the value attribute
        MdVertexDAOIF mdVertex = MdVertexDAO.get(this.getValueVertexOid());

        MdAttributeClassificationDAO mdAttribute = (MdAttributeClassificationDAO) mdVertex.definesAttribute(VALUE).getBusinessDAO();
        mdAttribute.setValue(MdAttributeClassificationInfo.ROOT, this.getObjectValue(ROOTTERM));
        mdAttribute.apply();
      }
    }

    super.apply();
  }

  @Override
  @Transaction
  public void delete()
  {
    if (!this.getIsChangeOverTime())
    {
      // Update the precision and scale of the value attribute
      MdVertexDAOIF mdVertex = MdVertexDAO.get(this.getGeoObjectType().getMdVertexOid());
      BusinessDAO mdAttribute = mdVertex.definesAttribute(this.getCode()).getBusinessDAO();
      mdAttribute.delete();
    }

    MdVertex mdVertex = this.getValueVertex();

    super.delete();

    if (mdVertex != null)
    {
      mdVertex.delete();
    }
  }

  @Override
  protected void populate(MdAttributeConcreteDAO mdAttribute)
  {
    super.populate(mdAttribute);

    mdAttribute.setValue(MdAttributeClassificationInfo.REFERENCE_MD_CLASSIFICATION, this.getMdClassificationOid());
    mdAttribute.setValue(MdAttributeClassificationInfo.ROOT, this.getRootTerm());
  }

  @Override
  public void fromDTO(AttributeType dto)
  {
    super.fromDTO(dto);

    ClassificationTypeBusinessServiceIF tService = ServiceFactory.getBean(ClassificationTypeBusinessServiceIF.class);
    ClassificationBusinessServiceIF cService = ServiceFactory.getBean(ClassificationBusinessServiceIF.class);

    org.commongeoregistry.adapter.metadata.AttributeClassificationType attributeClassificationType = (org.commongeoregistry.adapter.metadata.AttributeClassificationType) dto;
    String classificationTypeCode = attributeClassificationType.getClassificationType();
    ClassificationType classificationType = tService.getByCode(classificationTypeCode);

    this.setValue(AttributeClassificationType.MDCLASSIFICATION, classificationType.getOid());

    Term root = attributeClassificationType.getRootTerm();

    if (root != null)
    {
      Classification classification = cService.getByCode(classificationType, root.getCode()).orElseThrow(() -> {
        net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
        ex.setTypeLabel(classificationType.getDisplayLabel().getValue());
        ex.setDataIdentifier(root.getCode());
        ex.setAttributeLabel(GeoObjectMetadata.get().getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));
        
        throw ex;
      });

      this.setValue(AttributeClassificationType.ROOTTERM, classification.getOid());
    }
    else
    {
      this.setValue(AttributeClassificationType.ROOTTERM, null);
    }

  }

  @Override
  protected void populate(AttributeType dto)
  {
    super.populate(dto);

    ClassificationType type = this.getClassificationType();
    Classification root = this.getRootClassification();

    org.commongeoregistry.adapter.metadata.AttributeClassificationType attributeType = (org.commongeoregistry.adapter.metadata.AttributeClassificationType) dto;
    attributeType.setClassificationType(type.getCode());

    if (root != null)
    {
      attributeType.setRootTerm(root.toTerm());
    }
  }

  @Override
  public AttributeType toDTO()
  {
    org.commongeoregistry.adapter.metadata.AttributeClassificationType dto = new org.commongeoregistry.adapter.metadata.AttributeClassificationType(this.getCode(), getLocalizedLabel(), getLocalizedDescription(), getIsDefault(), isNew(), getUnique());

    this.populate(dto);

    return dto;
  }

  @Override
  public ValueStrategy getStrategy()
  {
    if (!this.getIsChangeOverTime())
    {
      return new VertexValueStrategy(this);
    }
    else
    {
      return new GraphRefNodeValueStrategy(this, MdVertexDAO.get(this.getValueVertexOid()), VALUE);
    }
  }

  public ClassificationType getClassificationType()
  {
    ClassificationTypeBusinessServiceIF typeService = ServiceFactory.getBean(ClassificationTypeBusinessServiceIF.class);

    return typeService.get(this.getMdClassificationOid());
  }

  public Classification getRootClassification()
  {
    return getRootClassification(getClassificationType());
  }

  protected Classification getRootClassification(ClassificationType type)
  {
    String oid = this.getRootTerm();

    if (!StringUtils.isBlank(oid))
    {
      ClassificationBusinessServiceIF service = ServiceFactory.getBean(ClassificationBusinessServiceIF.class);
      
      return service.getByOid(type, oid).orElseThrow(() -> {
        net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
        ex.setTypeLabel(type.getDisplayLabel().getValue());
        ex.setDataIdentifier(oid);
        ex.setAttributeLabel(GeoObjectMetadata.get().getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));
        
        throw ex;
      });
    }

    return null;
  }

  public MdAttributeClassificationDAOIF getMdAttributeClassification()
  {
    ValueStrategy strategy = this.getStrategy();

    List<MdAttributeDAOIF> attributes = strategy.getValueAttributes();

    return (MdAttributeClassificationDAOIF) attributes.get(0);
  }
}
