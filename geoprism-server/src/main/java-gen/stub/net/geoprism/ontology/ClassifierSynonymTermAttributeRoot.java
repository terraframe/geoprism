/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.ontology;


public class ClassifierSynonymTermAttributeRoot extends ClassifierSynonymTermAttributeRootBase 
{
  private static final long serialVersionUID = -91099530;
  
  public ClassifierSynonymTermAttributeRoot(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public ClassifierSynonymTermAttributeRoot(com.runwaysdk.system.metadata.MdAttributeTerm parent, net.geoprism.ontology.ClassifierSynonym child)
  {
    this(parent.getOid(), child.getOid());
  }
  
}
