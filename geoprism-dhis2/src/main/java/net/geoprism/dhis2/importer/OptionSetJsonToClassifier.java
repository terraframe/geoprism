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
package net.geoprism.dhis2.importer;

import org.json.JSONArray;
import org.json.JSONObject;

import net.geoprism.dhis2.util.DHIS2Util;
import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierIsARelationship;

public class OptionSetJsonToClassifier
{
  private JSONObject json;
  
  private Classifier classy;
  
  /**
   * This is a hack we're doing for the beta release. If a classifier package contains this prefix then we know it exists in DHIS2. Otherwise
   *   we know it needs to be exported. In the future we'll likely have a table that maps from runway oid to DHIS2 oid.
   */
  public static final String DHIS2_CLASSIFIER_PACKAGE_PREFIX = "DHIS2-";
  
  public OptionSetJsonToClassifier(JSONObject json)
  {
    this.json = json;
  }
  
  public void apply()
  {
    this.classy = new Classifier();
    this.classy.getDisplayLabel().setValue(json.getString("name"));
    this.classy.setClassifierId(json.getString("oid"));
    this.classy.setClassifierPackage(DHIS2_CLASSIFIER_PACKAGE_PREFIX + json.getString("oid"));
    this.classy.setCategory(true);
    this.classy.apply();
    
    DHIS2Util.mapIds(classy.getOid(), json.getString("oid"));
    
    Classifier parent = Classifier.getRoot();
    this.classy.addLink(parent, ClassifierIsARelationship.CLASS);
  }
  
  public void applyCategoryRelationships()
  {
    this.classy = Classifier.get(DHIS2Util.getRunwayIdFromDhis2Id(json.getString("oid")));
    
    JSONArray options = json.getJSONArray("options");
    for (int i = 0; i < options.length(); ++i)
    {
      JSONObject jsonOption = options.getJSONObject(i);
      Classifier option = Classifier.get(DHIS2Util.getRunwayIdFromDhis2Id(jsonOption.getString("oid")));
      
      option.addLink(classy, ClassifierIsARelationship.CLASS);
    }
  }
}
