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

import java.sql.Savepoint;

import org.json.JSONObject;

import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.system.gis.geo.Universal;

public class OrgUnitLevelJsonToUniversal
{
  private JSONObject json;
  
  private Universal universal;
  
  private DHIS2DataImporter importer;
  
  public OrgUnitLevelJsonToUniversal(DHIS2DataImporter importer, JSONObject json)
  {
    this.json = json;
    
    this.importer = importer;
    
    this.universal = new Universal();
  }
  
  public Universal getUniversal()
  {
    return universal;
  }
  
  public void apply()
  {
    Savepoint sp = Database.setSavepoint();
    
    try
    {
      // Create new
      universal.getDisplayLabel().setValue(json.getString("name"));
      universal.setUniversalId(json.getString("id"));
      universal.apply();
    }
    catch (DuplicateDataException ex)
    {
      // Update existing
      Database.rollbackSavepoint(sp);
      
      universal = Universal.getByKey(json.getString("id"));
      
      if (!universal.getDisplayLabel().getValue().equals(json.getString("name")))
      {
        universal.lock();
        universal.getDisplayLabel().setValue(json.getString("name"));
        universal.apply();
      }
    }
    catch (RuntimeException ex)
    {
      Database.rollbackSavepoint(sp);
      throw ex;
    }
    finally
    {
      Database.releaseSavepoint(sp);
    }
  }
  
  public void applyLocatedIn()
  {
    int level = json.getInt("level")-1;
    
    Universal parent;
    if (level == 0)
    {
      parent = Universal.getRoot();
    }
    else
    {
      parent = importer.getUniversalByLevel(level-1).getUniversal();
    }
    
    
    Savepoint sp = Database.setSavepoint();
    
    try
    {
      universal.addAllowedIn(parent).apply();
    }
    catch (DuplicateDataException ex)
    {
      Database.rollbackSavepoint(sp);
    }
    catch (RuntimeException ex)
    {
      Database.rollbackSavepoint(sp);
      throw ex;
    }
    finally
    {
      Database.releaseSavepoint(sp);
    }
  }
}
