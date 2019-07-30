/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.ontology;


public class ClassifierIsARelationship extends ClassifierIsARelationshipBase 
{
  private static final long serialVersionUID = -985779118;
  
  public ClassifierIsARelationship(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public ClassifierIsARelationship(net.geoprism.ontology.Classifier parent, net.geoprism.ontology.Classifier child)
  {
    this(parent.getOid(), child.getOid());
  }
  
  /**
   * Call this method to properly set the key. 
   * 
   * @param parentKey
   * @param childKey
   */
  public void setKey(String parentKey, String childKey)
  {
	  this.setKeyName(parentKey+"_"+childKey);
  }
}
