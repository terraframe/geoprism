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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.geoprism.dhis2.util.DHIS2Util;
import net.geoprism.ontology.Classifier;

public class OptionJsonToClassifier
{
  private final Logger logger = LoggerFactory.getLogger(OptionSetJsonToClassifier.class);
  
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
    else
    {
      // All three of these are required attributes in DHIS2. Apparently their database is allowed to have invalid data.
      
      logger.error("Database integrity problem: missing either code, id or name in option json [" + json.toString() + "].");
    }
  }
}
