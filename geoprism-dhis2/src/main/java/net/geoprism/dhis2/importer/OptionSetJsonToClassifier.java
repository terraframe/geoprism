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

import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierIsARelationship;

public class OptionSetJsonToClassifier
{
  private JSONObject json;
  
  private Classifier classy;
  
  public OptionSetJsonToClassifier(JSONObject json)
  {
    this.json = json;
  }
  
  public void apply()
  {
    this.classy = new Classifier();
    this.classy.getDisplayLabel().setValue(json.getString("name"));
    this.classy.setClassifierId(json.getString("id"));
    this.classy.setClassifierPackage(json.getString("id"));
    this.classy.setCategory(true);
    this.classy.apply();
    
    Classifier parent = Classifier.getRoot();
    this.classy.addLink(parent, ClassifierIsARelationship.CLASS);
  }
  
  public void applyCategoryRelationships()
  {
    this.classy = Classifier.getByKey(json.getString("id") + Classifier.KEY_CONCATENATOR + json.getString("id"));
    
    JSONArray options = json.getJSONArray("options");
    for (int i = 0; i < options.length(); ++i)
    {
      JSONObject jsonOption = options.getJSONObject(i);
      Classifier option = Classifier.getByKey(jsonOption.getString("id") + Classifier.KEY_CONCATENATOR + jsonOption.getString("id"));
      
      option.addLink(classy, ClassifierIsARelationship.CLASS);
    }
  }
}
