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
package net.geoprism.dhis2.export;

import java.io.OutputStream;
import java.util.Stack;

import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.io.dataDefinition.ComponentExporterIF;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXExporter;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.system.ontology.TermUtil;

public class DHIS2DataExporter
{
//  private ComponentExporterIF exporter;
//  
//  public static void main(String[] args)
//  {
//    
//  }
//  
//  public DHIS2DataExporter(ComponentExporterIF exporter)
//  {
//    this.exporter = exporter;
//  }
//
//  public DHIS2DataExporter(OutputStream stream)
//  {
//    this.exporter = new SAXExporter(stream, "classpath:com/runwaysdk/resources/xsd/datatype.xsd");
//  }
//
//  /**
//   * Copied from TermExporter.java
//   */
//  public void exportAll()
//  {
//    exporter.open();
//
//    try
//    {
//      if (includeParent)
//      {
//        // exporter.export(parent.getId());
//        exporter.writeCreate(parent);
//
//        // Loop over relationships with parents
//        String[] prelts = TermUtil.getAllChildRelationships(parent.getId());
//        for (String prelt : prelts)
//        {
//          OIterator<? extends Relationship> rel = parent.getParentRelationships(prelt);
//
//          try
//          {
//            while (rel.hasNext())
//            {
//              exporter.writeCreate(rel.next());
//            }
//          }
//          finally
//          {
//            rel.close();
//          }
//        }
//      }
//
//      /*
//       * This stack contains terms that have been exported, but have children
//       * that have not yet been exported.
//       */
//      Stack<Term> s = new Stack<Term>();
//
//      Term p = parent;
//
//      do
//      {
//        String[] relTypes = TermUtil.getAllParentRelationships(p.getId());
//
//        for (String relType : relTypes)
//        {
//          OIterator<? extends Relationship> rChildren = p.getChildRelationships(relType);
//
//          try
//          {
//            for (Relationship rChild : rChildren)
//            {
//              exporter.writeCreate(rChild.getChild());
//              exporter.writeCreate(rChild);
//
//              s.push((Term) rChild.getChild());
//            }
//          }
//          finally
//          {
//            rChildren.close();
//          }
//        }
//
//        if (!s.empty())
//        {
//          p = s.pop();
//        }
//        else
//        {
//          p = null;
//        }
//      } while (s.size() > 0 || p != null);
//    }
//    finally
//    {
//      exporter.close();
//    }
//  }
}
