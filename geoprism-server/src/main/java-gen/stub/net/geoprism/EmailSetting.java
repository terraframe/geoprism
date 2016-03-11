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
package net.geoprism;

import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public class EmailSetting extends EmailSettingBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1631634656;
  
  public EmailSetting()
  {
    super();
  }
  
  public static net.geoprism.EmailSetting getDefault()
  {
    EmailSettingQuery query = new EmailSettingQuery(new QueryFactory());
    OIterator<? extends EmailSetting> it = query.getIterator();
    
    if (it.hasNext())
    {
      EmailSetting first = it.next();
      
      if (it.hasNext())
      {
        throw new UnsupportedOperationException();
      }
      
      return first;
    }
    else
    {
      EmailSetting setting = new EmailSetting();
      setting.apply();
      return setting;
    }
  }
  
}
