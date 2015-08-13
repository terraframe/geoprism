package com.runwaysdk.geodashboard.io;

import java.util.List;

import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.io.ExcelExportListener;
import com.runwaysdk.dataaccess.io.excel.ExcelAdapter;
import com.runwaysdk.dataaccess.io.excel.ImportListener;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.system.gis.geo.Universal;

public class GeoEntityColumnListener extends ExcelAdapter implements ExcelExportListener, ImportListener, Reloadable
{
  private MdAttributeTermDAOIF mdAttributeTermDAOIF;

  private String             excelType;
  
  private List<Universal>    heierarchyList;
  
//  private List<Universal> buildHierarchy(Universal universal, List<Universal> _heierarchyList)
//  {
//    
//  }
}
