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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Row;

import com.runwaysdk.business.Entity;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.io.ExcelExportListener;
import com.runwaysdk.dataaccess.io.excel.ExcelAdapter;
import com.runwaysdk.dataaccess.io.excel.ExcelColumn;
import com.runwaysdk.dataaccess.io.excel.ImportListener;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.ontology.Classifier;
import com.runwaysdk.session.Session;

public class ClassifierColumnListener extends ExcelAdapter implements ExcelExportListener, ImportListener, Reloadable
{
  private MdAttributeTermDAOIF mdAttributeTermDAOIF;

  private String               excelType;
  
  private String               attributeName;
  
  public ClassifierColumnListener(String _excelType, MdAttributeTermDAOIF _mdAttributeTermDAOIF)
  {
    this.excelType                  = _excelType;
    this.mdAttributeTermDAOIF       = _mdAttributeTermDAOIF;
    this.attributeName              = this.mdAttributeTermDAOIF.definesAttribute();
  }
  
  @Override
  public void addColumns(List<ExcelColumn> extraColumns)
  {
    Locale currentLocale = Session.getCurrentLocale();
    
    List<RelationshipDAOIF> attributeRoots = this.mdAttributeTermDAOIF.getAllAttributeRoots();
    
    if (attributeRoots.size() == 1)
    {
      RelationshipDAOIF relationshipDAOIF = attributeRoots.get(0);
      
      Classifier root = Classifier.get(relationshipDAOIF.getChildId());
      
      String headerName = buildHeaderName(this.attributeName, root);
      
      String attributeLabel = this.mdAttributeTermDAOIF.getDisplayLabel(currentLocale);
      String headerLabel = attributeLabel;
      
      extraColumns.add(new ExcelColumn(headerName, headerLabel));
    }
    else
    {
      for (RelationshipDAOIF relationshipDAOIF : attributeRoots)
      {
        Classifier root = Classifier.get(relationshipDAOIF.getChildId());
      
        String headerName = buildHeaderName(this.attributeName, root);

        String attributeLabel = this.mdAttributeTermDAOIF.getDisplayLabel(currentLocale);
        String termDisplayLabel = root.getDisplayLabel().getValue(currentLocale);
        String headerLabel = buildHeaderLabel(attributeLabel, termDisplayLabel);
   
        extraColumns.add(new ExcelColumn(headerName, headerLabel));
      }
    }
  }
  
  private static String buildHeaderName(String _attributeName, Classifier _root)
  {
    return _attributeName + " - " + _root.getClassifierId();
  }
  
  private String buildHeaderLabel(String _attributeLabel, String _rootLabel)
  {
    return _attributeLabel + " " + _rootLabel;
  }
  
  @Override
  public void handleExtraColumns(Mutable instance, List<ExcelColumn> extraColumns, Row row) throws Exception
  {
  }
  
  @Override
  public void addAdditionalEntities(HashMap<String, List<Entity>> map)
  {
  }
  
  @Override
  public void validate(Mutable instance, HashMap<String, List<Entity>> entities)
  {
  }
  
  @Override
  public void afterApply(Mutable instance)
  {
    
  }
  
//  private List<Universal> buildHierarchy(Universal universal, List<Universal> _heierarchyList)
//  {
//    
//  }
}
