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
package net.geoprism.data.etl;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.system.metadata.MdView;

public class TargetBinding extends TargetBindingBase 
{
  private static final long serialVersionUID = 2043607150;

  public TargetBinding()
  {
    super();
  }

  @Override
  @Transaction
  public void delete()
  {
    List<TargetFieldBinding> fields = this.getFields();

    for (TargetFieldBinding field : fields)
    {
      field.delete();
    }

    super.delete();

    MdView mdView = this.getSourceView();

    ExcelSourceBinding source = ExcelSourceBinding.getBinding(mdView.definesType());
    source.delete();
  }

  public List<TargetFieldBinding> getFields()
  {
    List<TargetFieldBinding> list = new LinkedList<TargetFieldBinding>();

    TargetFieldBindingQuery query = new TargetFieldBindingQuery(new QueryFactory());
    query.WHERE(query.getTarget().EQ(this));

    OIterator<? extends TargetFieldBinding> iterator = query.getIterator();

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

  public TargetDefinitionIF getDefinition()
  {
    MdView sourceView = this.getSourceView();
    MdClass targetBusiness = this.getTargetBusiness();
    PersistenceStrategy strategy = this.getStrategy();

    TargetDefinition definition = new TargetDefinition();
    definition.setSourceType(sourceView.definesType());
    definition.setTargetType(targetBusiness.definesType());
    definition.setStrategy(strategy);
    definition.setNew(false);
    definition.setApplied(true);

    List<TargetFieldBinding> fields = this.getFields();

    for (TargetFieldBinding field : fields)
    {
      definition.addField(field.getTargetField());
    }

    return definition;
  }

  public static TargetBinding getBinding(String type)
  {
    return TargetBinding.getBindingForTarget(MdClass.getMdClass(type));
  }

  public static TargetBinding getBindingForTarget(MdClass mdClass)
  {
    TargetBindingQuery query = new TargetBindingQuery(new QueryFactory());
    query.WHERE(query.getTargetBusiness().EQ(mdClass));

    OIterator<? extends TargetBinding> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        TargetBinding binding = iterator.next();

        return binding;
      }

      return null;
    }
    finally
    {
      iterator.close();
    }
  }

  public static TargetBinding getBindingForSource(MdView mdView)
  {
    TargetBindingQuery query = new TargetBindingQuery(new QueryFactory());
    query.WHERE(query.getSourceView().EQ(mdView));

    OIterator<? extends TargetBinding> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        TargetBinding binding = iterator.next();

        return binding;
      }

      return null;
    }
    finally
    {
      iterator.close();
    }
  }

}
