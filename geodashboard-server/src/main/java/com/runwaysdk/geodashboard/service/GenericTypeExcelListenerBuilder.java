package com.runwaysdk.geodashboard.service;

import java.util.List;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.io.ExcelExportListener;
import com.runwaysdk.dataaccess.io.ExcelExporter;
import com.runwaysdk.dataaccess.io.ExcelImporter.ImportContext;
import com.runwaysdk.dataaccess.io.excel.ImportListener;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.geodashboard.KeyGeneratorIF;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class GenericTypeExcelListenerBuilder
{
  private GeoEntity      country;

  private KeyGeneratorIF generator;

  public GenericTypeExcelListenerBuilder(GeoEntity country)
  {
    this.country = country;
    this.generator = new SeedKeyGenerator();
  }

  /**
   * Builds {@link ExcelExportListener} objects and adds them to the given {@link ExcelExporter}. The given parameters
   * are passed on to the listeners if required.
   * 
   * @param exporter
   * @param mdClassType
   * @param params
   */
  public void configure(ExcelExporter exporter, String mdClassType, String... params)
  {
    MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(mdClassType);

    List<? extends MdAttributeDAOIF> mdAttributes = mdClass.getAllDefinedMdAttributes();

    for (MdAttributeDAOIF mdAttribute : mdAttributes)
    {
      if (mdAttribute instanceof MdAttributeTermDAOIF)
      {
        ExcelExportListener listener = new ClassifierColumnListener((MdAttributeTermDAOIF) mdAttribute);
        exporter.addListener(listener);
      }
      else if (mdAttribute instanceof MdAttributeReferenceDAOIF)
      {
        MdAttributeReferenceDAOIF mdAttributeReference = (MdAttributeReferenceDAOIF) mdAttribute;
        MdBusinessDAOIF referenceMdBusiness = mdAttributeReference.getReferenceMdBusinessDAO();

        if (referenceMdBusiness.definesType().equals(GeoEntity.CLASS))
        {
          ExcelExportListener listener = new GeoEntityColumnListener(mdAttributeReference, this.country, this.generator);
          exporter.addListener(listener);
        }
      }
    }
  }

  public void configure(ImportContext context)
  {
    MdClassDAOIF mdClass = context.getMdClass();

    List<? extends MdAttributeDAOIF> mdAttributes = mdClass.getAllDefinedMdAttributes();

    for (MdAttributeDAOIF mdAttribute : mdAttributes)
    {
      if (mdAttribute instanceof MdAttributeTermDAOIF)
      {
        ImportListener listener = new ClassifierColumnListener((MdAttributeTermDAOIF) mdAttribute);
        context.addListener(listener);
      }
      else if (mdAttribute instanceof MdAttributeReferenceDAOIF)
      {
        MdAttributeReferenceDAOIF mdAttributeReference = (MdAttributeReferenceDAOIF) mdAttribute;
        MdBusinessDAOIF referenceMdBusiness = mdAttributeReference.getReferenceMdBusinessDAO();

        if (referenceMdBusiness.definesType().equals(GeoEntity.CLASS))
        {
          ImportListener listener = new GeoEntityColumnListener(mdAttributeReference, this.country, generator);
          context.addListener(listener);
        }
      }
    }
  }
}
