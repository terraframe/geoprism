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
package net.geoprism.registry.model;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.google.gson.JsonObject;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.constants.graph.MdClassificationInfo;
import com.runwaysdk.dataaccess.MdClassificationDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdClassificationDAO;
import com.runwaysdk.system.metadata.MdClassification;

import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.view.JsonSerializable;

public class ClassificationType implements JsonSerializable
{
  private MdClassificationDAOIF mdClassification;

  public ClassificationType()
  {
  }

  public ClassificationType(MdClassificationDAOIF mdClassification)
  {
    this.mdClassification = mdClassification;
  }

  public MdClassificationDAOIF getMdClassification()
  {
    return mdClassification;
  }

  public void setMdClassification(MdClassificationDAOIF mdClassification)
  {
    this.mdClassification = mdClassification;
  }

  public MdClassification getMdClassificationObject()
  {
    return (MdClassification) BusinessFacade.get(this.mdClassification);
  }

  public String getCode()
  {
    return this.mdClassification.getValue(MdClassificationInfo.TYPE_NAME);
  }

  public String getType()
  {
    return this.mdClassification.definesType();
  }

  public String getOid()
  {
    return this.mdClassification.getOid();
  }

  public LocalizedValue getDisplayLabel()
  {
    return LocalizedValueConverter.convert(this.mdClassification.getDisplayLabels());
  }

  public LocalizedValue getDescription()
  {
    return LocalizedValueConverter.convert(this.mdClassification.getDescriptions());
  }

  public MdEdgeDAOIF getMdEdge()
  {
    return this.mdClassification.getReferenceMdEdgeDAO();
  }

  public MdVertexDAOIF getMdVertex()
  {
    return this.mdClassification.getReferenceMdVertexDAO();
  }

  public void setRoot(Classification classification)
  {
    MdClassificationDAO mdClassificationDAO = (MdClassificationDAO) this.mdClassification.getBusinessDAO();
    mdClassificationDAO.setValue(MdClassificationInfo.ROOT, classification.getOid());
    mdClassificationDAO.apply();

    this.mdClassification = mdClassificationDAO;
  }

  public Classification getRoot()
  {
    VertexObjectDAOIF root = this.mdClassification.getRoot();

    if (root != null)
    {
      return new Classification(this, VertexObject.instantiate((VertexObjectDAO) root));
    }

    return null;
  }

  @Override
  public JsonObject toJSON()
  {
    JsonObject object = new JsonObject();
    object.addProperty(MdClassificationInfo.OID, this.getOid());
    object.addProperty(MdClassificationInfo.TYPE, this.getType());
    object.addProperty(DefaultAttribute.CODE.getName(), this.getCode());
    object.add(MdClassificationInfo.DISPLAY_LABEL, this.getDisplayLabel().toJSON());
    object.add(MdClassificationInfo.DESCRIPTION, this.getDescription().toJSON());

    Classification root = this.getRoot();

    if (root != null)
    {
      object.add(MdClassificationInfo.ROOT, root.toJSON());
    }

    return object;
  }

}
