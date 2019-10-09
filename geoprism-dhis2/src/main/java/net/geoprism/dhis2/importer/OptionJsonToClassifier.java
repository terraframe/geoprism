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

import org.json.JSONObject;

import net.geoprism.dhis2.util.DHIS2Util;
import net.geoprism.ontology.Classifier;

public class OptionJsonToClassifier
{
  private JSONObject json;
  
  public OptionJsonToClassifier(JSONObject json)
  {
    this.json = json;
  }
  
  public void apply()
  {
    if (json.has("code") && json.has("id") && json.has("name"))
    {
      Classifier classy = new Classifier();
      classy.getDisplayLabel().setValue(json.getString("name"));
      classy.setClassifierId(json.getString("id"));
      classy.setClassifierPackage(OptionSetJsonToClassifier.DHIS2_CLASSIFIER_PACKAGE_PREFIX + json.getString("id"));
      classy.setCategory(false);
      classy.apply();
      
      DHIS2Util.mapIds(classy.getId(), json.getString("id"));
      DHIS2Util.mapOptionCode(classy.getId(), json.getString("code"));
    }
  }
}
