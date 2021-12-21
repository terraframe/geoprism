/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.data.importer;

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
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;

import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierQuery;
import net.geoprism.ontology.ClassifierTermAttributeRootQuery;

public class ClassifierColumnListener extends ExcelAdapter implements ExcelExportListener, ImportListener
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

    if (column != null)
    {
      Cell cell = row.getCell(column.getIndex());
      String value = ExcelUtil.getString(cell);

      if (value != null && value.length() > 0)
      {
        Classifier classifier = Classifier.findClassifierAddIfNotExist(this.classifierPackage, value, this.mdAttributeTermDAOIF);

        if (classifier != null)
        {
          instance.setValue(attributeName, classifier.getOid());
        }
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

    // This might happen if the user column isn't included in the file
    return null;
  }

  public static String getClassifierPackage(MdAttributeTermDAOIF mdAttributeTerm)
  {
    QueryFactory factory = new QueryFactory();

    ClassifierTermAttributeRootQuery arQuery = new ClassifierTermAttributeRootQuery(factory);
    arQuery.WHERE(arQuery.parentOid().EQ(mdAttributeTerm.getOid()));

    ClassifierQuery cQuery = new ClassifierQuery(factory);
    cQuery.WHERE(cQuery.classifierTermAttributeRoots(arQuery));

    OIterator<? extends Classifier> it = cQuery.getIterator();

    try
    {
      List<? extends Classifier> roots = it.getAll();

      if (roots.size() == 0)
      {
        throw new ProgrammingErrorException("No attribute roots have been defined for [" + mdAttributeTerm.getKey() + "]");
      }

      return roots.get(0).getClassifierPackage();
    }
    finally
    {
      it.close();
    }
  }

}
