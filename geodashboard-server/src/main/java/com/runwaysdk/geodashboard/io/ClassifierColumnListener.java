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
