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

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.CodeReference;

import com.runwaysdk.constants.MdAttributeGraphReferenceInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeGraphReferenceDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.registry.model.ConceptObject;
import net.geoprism.registry.model.GeoObjectMetadata;
import net.geoprism.registry.model.GraphRefNodeValueStrategy;
import net.geoprism.registry.model.ValueStrategy;
import net.geoprism.registry.model.VertexValueStrategy;
import net.geoprism.registry.service.business.ConceptObjectBusinessServiceIF;
import net.geoprism.registry.service.business.ConceptSetBusinessServiceIF;
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
  protected void populate(MdAttributeConcreteDAO mdAttribute)
  {
    super.populate(mdAttribute);

    MdVertexDAOIF mdVertexDAO = MdVertexDAO.getMdVertexDAO(ConceptVertex.CLASS);

    mdAttribute.setValue(MdAttributeGraphReferenceInfo.REFERENCE_MD_VERTEX, mdVertexDAO.getOid());
  }

  @Override
  @Transaction
  public void apply()
  {
    if (!this.getIsChangeOverTime() && !this.getIsVirtual())
    {
      // Create the MdAttribute on the MdVertex
      MdAttributeGraphReferenceDAO mdAttribute = MdAttributeGraphReferenceDAO.newInstance();

      populate(mdAttribute);

      mdAttribute.apply();

    }

    super.apply();
  }

  @Override
  @Transaction
  public void delete()
  {
    if (!this.getIsChangeOverTime() && !this.getIsVirtual())
    {
      ObjectClass type = this.getObjectType();
      MdVertexDAOIF mdVertex = MdVertexDAO.get(type.getMdVertexOid());
      MdAttributeDAOIF mdAttribute = mdVertex.definesAttribute(this.getCode());

      if (mdAttribute != null)
      {
        mdAttribute.getBusinessDAO().delete();
      }
    }

    super.delete();
  }

  @Override
  public void fromDTO(AttributeType dto)
  {
    super.fromDTO(dto);

    org.commongeoregistry.adapter.metadata.AttributeClassificationType attributeClassificationDTO = (org.commongeoregistry.adapter.metadata.AttributeClassificationType) dto;

    ConceptSetBusinessServiceIF tService = ServiceFactory.getBean(ConceptSetBusinessServiceIF.class);
    ConceptSet conceptSet = tService.getByCodeOrThrow(attributeClassificationDTO.getConceptSet());

    this.setStartDate(attributeClassificationDTO.getStartDate());
    this.setEndDate(attributeClassificationDTO.getEndDate());
    this.setConceptSet(conceptSet.getCode());

    CodeReference rootTerm = attributeClassificationDTO.getRootTerm();

    if (rootTerm != null)
    {
      ConceptObjectBusinessServiceIF cService = ServiceFactory.getBean(ConceptObjectBusinessServiceIF.class);

      ConceptObject classification = cService.getByCode(conceptSet, rootTerm.getCode()).orElseThrow(() -> {
        net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
        ex.setTypeLabel(conceptSet.getLabel().getValue());
        ex.setDataIdentifier(rootTerm.getCode());
        ex.setAttributeLabel(GeoObjectMetadata.get().getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));

        throw ex;
      });

      this.setValue(AttributeClassificationType.ROOTTERM, classification.getVertex());
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

    ConceptObjectBusinessServiceIF cService = ServiceFactory.getBean(ConceptObjectBusinessServiceIF.class);

    org.commongeoregistry.adapter.metadata.AttributeClassificationType attributeType = (org.commongeoregistry.adapter.metadata.AttributeClassificationType) dto;
    attributeType.setConceptSet(this.getConceptSet());
    attributeType.setStartDate(this.getStartDate());
    attributeType.setEndDate(this.getEndDate());

    cService.getByOid(this.getObjectValue(ROOTTERM)).ifPresent(root -> {
      attributeType.setRootTerm(CodeReference.build(root.getCode(), root.getType().getCode()));
    });
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
      return new GraphRefNodeValueStrategy(this, MdVertexDAO.getMdVertexDAO(AttributeClassificationValue.CLASS), AttributeClassificationValue.VALUE);
    }
  }
}
