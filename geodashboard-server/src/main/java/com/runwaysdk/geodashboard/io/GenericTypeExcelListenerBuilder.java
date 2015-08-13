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
