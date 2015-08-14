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
package com.runwaysdk.geodashboard.io;

import java.util.List;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.io.ExcelExportListener;
import com.runwaysdk.dataaccess.io.ExcelExporter;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;

public class GenericTypeExcelListenerBuilder 
{

  /** 
   * Builds {@link ExcelExportListener} objects and adds them to the given {@link ExcelExporter}. The given
   * parameters are passed on to the listeners if required.
   * 
   * @param excelImporter
   * @param mdClassType
   * @param params
   */
  public static void buildListeners(ExcelExporter excelImporter, String mdClassType, String... params)
  {
    MdClassDAOIF mdClassDAOIF = MdClassDAO.getMdClassDAO(mdClassType);
    
    List<? extends MdAttributeDAOIF> mdAttributes = mdClassDAOIF.getAllDefinedMdAttributes();
    
    for (MdAttributeDAOIF mdAttributeDAOIF : mdAttributes)
    {
      if (mdAttributeDAOIF instanceof MdAttributeTermDAOIF)
      {
        ExcelExportListener listener = new ClassifierColumnListener(mdClassType, (MdAttributeTermDAOIF)mdAttributeDAOIF);
        excelImporter.addListener(listener);
      }
    }
  }

}
