/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.data.etl;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdView;

import net.geoprism.MappableClass;

public class ExcelSourceBinding extends ExcelSourceBindingBase 
{
  private static final long serialVersionUID = 1190316606;

  public ExcelSourceBinding()
  {
    super();
  }

  @Override
  @Transaction
  public void delete()
  {
    List<ExcelFieldBinding> fields = this.getFields();

    for (ExcelFieldBinding field : fields)
    {
      field.delete();
    }

    super.delete();

    this.getMdView().delete();
  }

  public List<ExcelFieldBinding> getFields()
  {
    List<ExcelFieldBinding> list = new LinkedList<ExcelFieldBinding>();

    ExcelFieldBindingQuery query = new ExcelFieldBindingQuery(new QueryFactory());
    query.WHERE(query.getSourceDefinition().EQ(this));

    OIterator<? extends ExcelFieldBinding> iterator = query.getIterator();

    try
    {
      while (iterator.hasNext())
      {
        list.add(iterator.next());
      }

      return list;
    }
    finally
    {
      iterator.close();
    }
  }

  public static ExcelSourceBinding getBinding(String type)
  {
    ExcelSourceBindingQuery query = new ExcelSourceBindingQuery(new QueryFactory());
    query.WHERE(query.getMdView().EQ(MdView.getMdView(type)));

    OIterator<? extends ExcelSourceBinding> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        ExcelSourceBinding binding = iterator.next();

        return binding;
      }

      return null;
    }
    finally
    {
      iterator.close();
    }
  }

  public SourceDefinitionIF getDefinition(String sheetName)
  {
    MdView mdView = this.getMdView();
    
    MappableClass mClass = MappableClass.getMappableClass(this.getTargetBinding().getTargetBusiness().definesType());
    Universal country = mClass.getCountry();
    
    SourceDefinition definition = new SourceDefinition();
    definition.setType(mdView.definesType());
    definition.setLabel(mdView.getDisplayLabel().getValue());
    definition.setSheetName(sheetName);
    definition.setCountry(country.getOid());
    definition.setNew(false);
    definition.setApplied(true);
    definition.setId(this.getOid());

    List<ExcelFieldBinding> fields = this.getFields();

    for (ExcelFieldBinding field : fields)
    {
      definition.addField(field.getSourceField());
    }
    
    return definition;
  }

  public TargetBinding getTargetBinding()
  {
    return TargetBinding.getBindingForSource(this.getMdView());
  }
}
