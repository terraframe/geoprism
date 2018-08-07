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
package net.geoprism.report;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IParameterGroupDefn;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.LocalProperties;


public class ReportParameterUtil 
{
  public static final String TEXT_BOX     = "Text Box";

  public static final String LIST_BOX     = "List Box";

  public static final String RADIO_BUTTON = "Radio Button";

  public static final String CHECK_BOX    = "Check Box";

  @SuppressWarnings("unchecked")
  public Map<String, Object> convertParameters(IReportRunnable design, Map<String, String> map) throws BirtException
  {
    IReportEngine engine = BirtEngine.getBirtEngine(LocalProperties.getLogDirectory());

    IGetParameterDefinitionTask task = engine.createGetParameterDefinitionTask(design);

    try
    {
      Map<String, Object> parameters = new HashMap<String, Object>();

      for (Entry<String, String> entry : map.entrySet())
      {
        Collection<IParameterDefnBase> params = (Collection<IParameterDefnBase>) task.getParameterDefns(true);

        Iterator<IParameterDefnBase> iter = params.iterator();

        while (iter.hasNext())
        {
          IParameterDefnBase param = iter.next();

          if (entry.getKey().equals(param.getName()))
          {
            IScalarParameterDefn definition = (IScalarParameterDefn) param;
            boolean multivalue = definition.getScalarParameterType().equals("multi-value");

            parameters.put(entry.getKey(), this.parse(definition, entry.getValue(), multivalue));
          }
        }
      }

      return parameters;
    }
    finally
    {
      task.close();
    }
  }

  private Object parse(IScalarParameterDefn param, String value, boolean multivalue)
  {
    try
    {
      if (multivalue)
      {
        List<Object> values = new LinkedList<Object>();
        JSONArray array = new JSONArray(value);

        for (int i = 0; i < array.length(); i++)
        {
          String sub = array.getString(i);
          values.add(this.parse(param, sub, false));
        }

        return values.toArray(new Object[values.size()]);
      }
      else
      {
        if (param.getDataType() == IScalarParameterDefn.TYPE_BOOLEAN)
        {
          return new Boolean(value);
        }
        else if (param.getDataType() == IScalarParameterDefn.TYPE_DATE)
        {
          DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
          Date date = df.parse(value);

          return new java.sql.Date(date.getTime());
        }
        else if (param.getDataType() == IScalarParameterDefn.TYPE_DATE_TIME)
        {
          DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
          return df.parse(value);
        }
        else if (param.getDataType() == IScalarParameterDefn.TYPE_DECIMAL)
        {
          return new Double(value);
        }
        else if (param.getDataType() == IScalarParameterDefn.TYPE_FLOAT)
        {
          return new Float(value);
        }
        else if (param.getDataType() == IScalarParameterDefn.TYPE_INTEGER)
        {
          return new Integer(value);
        }
        else if (param.getDataType() == IScalarParameterDefn.TYPE_TIME)
        {
          DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
          return df.parse(value);
        }

        return value;
      }
    }
    catch (Exception e)
    {
      // TODO change exception type
      throw new RuntimeException("Unable to parse value: [" + value + "]");
    }
  }

  @SuppressWarnings("unchecked")
  public JSONArray getParameterDefinitions(InputStream stream) throws BirtException, JSONException
  {
    IReportEngine engine = BirtEngine.getBirtEngine(LocalProperties.getLogDirectory());

    // Open a report design
    IReportRunnable design = engine.openReportDesign(stream);

    IGetParameterDefinitionTask task = engine.createGetParameterDefinitionTask(design);

    try
    {
      JSONArray array = new JSONArray();

      Collection<IParameterDefnBase> params = (Collection<IParameterDefnBase>) task.getParameterDefns(true);

      Iterator<IParameterDefnBase> iter = params.iterator();
      while (iter.hasNext())
      {
        IParameterDefnBase param = iter.next();

        if (param instanceof IParameterGroupDefn)
        {
          IParameterGroupDefn group = (IParameterGroupDefn) param;

          ArrayList<IScalarParameterDefn> contents = group.getContents();

          Iterator<IScalarParameterDefn> i2 = contents.iterator();

          while (i2.hasNext())
          {
            IScalarParameterDefn scalar = i2.next();

            array.put(this.getParameterDetails(task, scalar, design, group));
          }
        }
        else
        {
          IScalarParameterDefn scalar = (IScalarParameterDefn) param;
          array.put(this.getParameterDetails(task, scalar, design, null));
        }
      }

      return array;
    }
    finally
    {
      task.close();
    }
  }

  @SuppressWarnings("unchecked")
  private JSONObject getParameterDetails(IGetParameterDefinitionTask task, IScalarParameterDefn scalar, IReportRunnable report, IParameterGroupDefn group) throws JSONException
  {
    JSONObject parameter = new JSONObject();

    parameter.put("parameterGroup", ( group == null ? "none" : group.getName() ));
    parameter.put("name", scalar.getName());
    parameter.put("helpText", scalar.getHelpText());
    parameter.put("displayName", scalar.getDisplayName());
    parameter.put("displayFormat", scalar.getDisplayFormat());
    parameter.put("isHidden", scalar.isHidden());
    parameter.put("isRequired", scalar.isRequired());
    parameter.put("isValueConcealed", scalar.isValueConcealed());
    parameter.put("type", scalar.getControlType());
    parameter.put("dataType", scalar.getDataType());
    parameter.put("defaultValue", scalar.getDefaultValue());
    parameter.put("promptText", scalar.getPromptText());
    parameter.put("scalarParameterType", scalar.getScalarParameterType());
    parameter.put("allowNewValues", scalar.allowNewValues());

    ScalarParameterHandle parameterHandle = (ScalarParameterHandle) scalar.getHandle();

    parameter.put("valueExpr", parameterHandle.getValueExpr());

    if (scalar.getControlType() != IScalarParameterDefn.TEXT_BOX)
    {
      DesignElementHandle handle = parameterHandle.getContainer();

      if (handle instanceof CascadingParameterGroupHandle)
      {
        String groupName = handle.getName();
        Object[] values = new Object[] {};

        parameter.put("options", getSelectionListForCascadingGroup(task, groupName, values));
      }
      else
      {
        Collection<IParameterSelectionChoice> collection = task.getSelectionList(scalar.getName());

        if (collection != null)
        {
          JSONArray options = new JSONArray();

          for (IParameterSelectionChoice selectionItem : collection)
          {
            String label = selectionItem.getLabel();
            Object value = selectionItem.getValue();

            JSONObject option = new JSONObject();
            option.put("label", label);
            option.put("value", value);

            options.put(option);
          }

          parameter.put("options", options);
        }
      }

    }

    return parameter;
  }

  @SuppressWarnings("unchecked")
  public JSONArray getSelectionListForCascadingGroup(IGetParameterDefinitionTask task, String groupName, Object[] values) throws JSONException
  {
    Collection<IParameterSelectionChoice> sList = task.getSelectionListForCascadingGroup(groupName, values);

    JSONArray array = new JSONArray();

    for (IParameterSelectionChoice sI : sList)
    {
      String label = sI.getLabel();
      Object value = sI.getValue();

      Object[] selections = Arrays.copyOf(values, ( values.length + 1 ));

      JSONObject object = new JSONObject();
      object.put("label", label);
      object.put("value", value);
      object.put("children", this.getSelectionListForCascadingGroup(task, groupName, selections));

      array.put(object);
    }

    return array;
  }

}
