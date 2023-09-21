/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism Registry(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.conversion;

import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.AbstractClassification;

import net.geoprism.ontology.Classifier;

/**
 * Responsible for building {@link Term} objects from Runway {@link Classifier}
 * objects.
 * 
 * @author nathan
 *
 */
public class VertexTermConverter
{
  private VertexObject classification;

  public VertexTermConverter(VertexObject classification)
  {
    this.classification = classification;
  }

  @Request
  public Term build()
  {
    return this.buildTermFromClassifier(this.classification);

  }

  private Term buildTermFromClassifier(VertexObject classification)
  {
    LocalizedValue label = RegistryAttributeTypeConverter.convert(classification.getEmbeddedComponent(AbstractClassification.DISPLAYLABEL));

    Term term = new Term(classification.getObjectValue(AbstractClassification.CODE), label, new LocalizedValue(""));

    return term;
  }
}
