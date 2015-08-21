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
import java.util.Locale;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.runwaysdk.business.Mutable;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.io.ExcelExportListener;
import com.runwaysdk.dataaccess.io.excel.ExcelAdapter;
import com.runwaysdk.dataaccess.io.excel.ExcelColumn;
import com.runwaysdk.dataaccess.io.excel.ExcelUtil;
import com.runwaysdk.dataaccess.io.excel.ImportListener;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.ontology.Classifier;
import com.runwaysdk.geodashboard.ontology.ClassifierAttributeRootQuery;
import com.runwaysdk.geodashboard.ontology.ClassifierQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;

public class ClassifierColumnListener extends ExcelAdapter implements ExcelExportListener, ImportListener, Reloadable
{
  private MdAttributeTermDAOIF mdAttributeTermDAOIF;

  private String               attributeName;

  private String               classifierPackage;

  public ClassifierColumnListener(MdAttributeTermDAOIF _mdAttributeTermDAOIF)
  {
    this.mdAttributeTermDAOIF = _mdAttributeTermDAOIF;
    this.attributeName = this.mdAttributeTermDAOIF.definesAttribute();
    this.classifierPackage = ClassifierColumnListener.getClassifierPackage(this.mdAttributeTermDAOIF);
  }

  @Override
  public void addColumns(List<ExcelColumn> extraColumns)
  {
    Locale currentLocale = Session.getCurrentLocale();

    String attributeLabel = this.mdAttributeTermDAOIF.getDisplayLabel(currentLocale);
    String headerLabel = attributeLabel;

    extraColumns.add(new ClassifierColumn(attributeName, headerLabel));
  }

  @Override
  public void handleExtraColumns(Mutable instance, List<ExcelColumn> extraColumns, Row row) throws Exception
  {
    ExcelColumn column = this.getColumn(extraColumns);
    Cell cell = row.getCell(column.getIndex());
    String value = ExcelUtil.getString(cell);

    if (value != null && value.length() > 0)
    {
      Classifier classifier = Classifier.findClassifierAddIfNotExist(this.classifierPackage, value, this.mdAttributeTermDAOIF);

      if (classifier != null)
      {
        instance.setValue(attributeName, classifier.getId());
      }
    }
  }

  private ExcelColumn getColumn(List<ExcelColumn> columns)
  {
    for (ExcelColumn column : columns)
    {
      if (this.attributeName.equals(column.getAttributeName()))
      {
        return column;
      }
    }

    throw new ProgrammingErrorException("Unable to find the excel column for attribute [" + this.attributeName + "]");
  }

  public static String getClassifierPackage(MdAttributeTermDAOIF mdAttributeTerm)
  {
    QueryFactory factory = new QueryFactory();

    ClassifierAttributeRootQuery arQuery = new ClassifierAttributeRootQuery(factory);
    arQuery.WHERE(arQuery.parentId().EQ(mdAttributeTerm.getId()));

    ClassifierQuery cQuery = new ClassifierQuery(factory);
    cQuery.WHERE(cQuery.classifierAttributeRoots(arQuery));

    OIterator<? extends Classifier> it = cQuery.getIterator();

    try
    {
      List<? extends Classifier> roots = it.getAll();

      if (roots.size() == 0)
      {
        throw new ProgrammingErrorException("Unable to find attribute roots for [" + mdAttributeTerm.getKey() + "]");
      }

      return roots.get(0).getClassifierPackage();
    }
    finally
    {
      it.close();
    }
  }

}
