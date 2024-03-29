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
package net.geoprism.ontology;

/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 *
 * @author Autogenerated by RunwaySDK
 */
@com.runwaysdk.business.ClassSignature(hash = 1408892504)
public enum ClassifierProblemType implements com.runwaysdk.business.BusinessEnumeration
{
  UNMATCHED();
  
  public static final java.lang.String CLASS = "net.geoprism.ontology.ClassifierProblemType";
  private net.geoprism.ontology.ClassifierProblemTypeMaster enumeration;
  
  private synchronized void loadEnumeration()
  {
    net.geoprism.ontology.ClassifierProblemTypeMaster enu = net.geoprism.ontology.ClassifierProblemTypeMaster.getEnumeration(this.name());
    setEnumeration(enu);
  }
  
  private synchronized void setEnumeration(net.geoprism.ontology.ClassifierProblemTypeMaster enumeration)
  {
    this.enumeration = enumeration;
  }
  
  public net.geoprism.ontology.ClassifierProblemTypeMasterDescription getDescription()
  {
    loadEnumeration();
    return enumeration.getDescription();
  }
  
  public java.lang.String getOid()
  {
    loadEnumeration();
    return enumeration.getOid();
  }
  
  public java.lang.String getEnumName()
  {
    loadEnumeration();
    return enumeration.getEnumName();
  }
  
  public java.lang.String getDisplayLabel()
  {
    loadEnumeration();
    return enumeration.getDisplayLabel().getValue(com.runwaysdk.session.Session.getCurrentLocale());
  }
  
  public static ClassifierProblemType get(String oid)
  {
    for (ClassifierProblemType e : ClassifierProblemType.values())
    {
      if (e.getOid().equals(oid))
      {
        return e;
      }
    }
    return null;
  }
  
}
