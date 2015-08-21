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
import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class GenericTypeExcelListenerBuilder
{
  private GeoEntity      country;

  private KeyGeneratorIF generator;

  public GenericTypeExcelListenerBuilder(GeoEntity _country)
  {
    this.country = _country;
    this.generator = new SeedKeyGenerator();
  }

  /**
   * Builds {@link ExcelExportListener} objects and adds them to the given {@link ExcelExporter}. The given parameters
   * are passed on to the listeners if required.
   * 
   * @param _exporter
   * @param _mdClassType
   * @param _params
   */
  public void configure(ExcelExporter _exporter, String _mdClassType, String... _params)
  {
    MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(_mdClassType);

    List<? extends MdAttributeDAOIF> mdAttributes = mdClass.getAllDefinedMdAttributes();

    for (MdAttributeDAOIF mdAttribute : mdAttributes)
    {
      if (mdAttribute instanceof MdAttributeTermDAOIF)
      {
        ExcelExportListener listener = new ClassifierColumnListener((MdAttributeTermDAOIF) mdAttribute);
        _exporter.addListener(listener);
      }
      else if (mdAttribute instanceof MdAttributeReferenceDAOIF)
      {
        MdAttributeReferenceDAOIF mdAttributeReference = (MdAttributeReferenceDAOIF) mdAttribute;
        MdBusinessDAOIF referenceMdBusiness = mdAttributeReference.getReferenceMdBusinessDAO();

        if (referenceMdBusiness.definesType().equals(GeoEntity.CLASS))
        {
          ExcelExportListener listener = new GeoEntityColumnListener(mdAttributeReference, this.country, this.generator);
          _exporter.addListener(listener);
        }
      }
      else if (mdAttribute instanceof MdAttributeGeometryDAOIF)
      {
        MdAttributeGeometryDAOIF mdAttributeGeometry = (MdAttributeGeometryDAOIF) mdAttribute;

        ExcelExportListener listener = new GeometryColumnListener(mdAttributeGeometry);
        _exporter.addListener(listener);
      }
    }
  }

  public void configure(ImportContext _context)
  {
    MdClassDAOIF mdClass = _context.getMdClass();

    List<? extends MdAttributeDAOIF> mdAttributes = mdClass.getAllDefinedMdAttributes();

    for (MdAttributeDAOIF mdAttribute : mdAttributes)
    {
      if (mdAttribute instanceof MdAttributeTermDAOIF)
      {
        ImportListener listener = new ClassifierColumnListener((MdAttributeTermDAOIF) mdAttribute);
        _context.addListener(listener);
      }
      else if (mdAttribute instanceof MdAttributeReferenceDAOIF)
      {
        MdAttributeReferenceDAOIF mdAttributeReference = (MdAttributeReferenceDAOIF) mdAttribute;
        MdBusinessDAOIF referenceMdBusiness = mdAttributeReference.getReferenceMdBusinessDAO();

        if (referenceMdBusiness.definesType().equals(GeoEntity.CLASS))
        {
          ImportListener listener = new GeoEntityColumnListener(mdAttributeReference, this.country, generator);
          _context.addListener(listener);
        }
      }
      else if (mdAttribute instanceof MdAttributeGeometryDAOIF)
      {
        MdAttributeGeometryDAOIF mdAttributeGeometry = (MdAttributeGeometryDAOIF) mdAttribute;

        ImportListener listener = new GeometryColumnListener(mdAttributeGeometry);
        _context.addListener(listener);
      }
    }
  }
}
