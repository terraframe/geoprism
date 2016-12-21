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
package net.geoprism.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.transaction.Transaction;

public class DatabaseUtil
{
  private static boolean IS_MATERIALIZED = true;

  /**
   * Creates a view.
   * 
   * @param viewName
   * @param query
   */
  @Transaction
  public static void createView(String viewName, String query)
  {
    if (IS_MATERIALIZED)
    {
      String statement = "CREATE MATERIALIZED VIEW " + viewName + " AS (" + query + ")";

      Database.executeStatement(statement);
    }
    else
    {
      Database.createView(viewName, query);
    }
  }

  /**
   * Drops a view.
   * 
   * @param viewName
   * @param query
   */
  @Transaction
  public static void dropView(String viewName, String query, Boolean dropOnEndOfTransaction)
  {
    if (IS_MATERIALIZED)
    {
      String statement = "DROP MATERIALIZED VIEW IF EXISTS " + viewName;

      Database.executeStatement(statement);
    }
    else
    {
      Database.dropView(viewName, query, dropOnEndOfTransaction);
    }
  }

  @Transaction
  public static void dropViews(List<String> viewNames)
  {
    if (IS_MATERIALIZED)
    {
      for (String viewName : viewNames)
      {
        DatabaseUtil.dropView(viewName, null, false);
      }
    }
    else
    {
      Database.dropViews(viewNames);
    }
  }

  @Transaction
  public static List<String> getViewsByPrefix(String prefix)
  {
    if (IS_MATERIALIZED)
    {
      StringBuilder sql = new StringBuilder();
      sql.append("SELECT oid::regclass::text AS viewName");
      sql.append(" FROM pg_class");
      sql.append(" WHERE  relkind = 'm'");

      ResultSet resultSet = Database.query(sql.toString());
      List<String> list = new LinkedList<String>();

      try
      {
        while (resultSet.next())
        {
          String viewName = resultSet.getString("viewName");

          if (viewName.startsWith(prefix))
          {
            list.add(viewName);
          }
        }
      }
      catch (SQLException sqlEx1)
      {
        Database.throwDatabaseException(sqlEx1);
      }
      finally
      {
        try
        {
          java.sql.Statement statement = resultSet.getStatement();
          resultSet.close();
          statement.close();
        }
        catch (SQLException sqlEx2)
        {
          Database.throwDatabaseException(sqlEx2);
        }
      }
      return list;
    }
    else
    {
      return Database.getViewsByPrefix(prefix);
    }
  }

  @Transaction
  public static void refreshView(String viewName)
  {
    if (IS_MATERIALIZED)
    {
      String statement = "REFRESH MATERIALIZED VIEW " + viewName;

      Database.executeStatement(statement);
    }
    else
    {
      throw new UnsupportedOperationException();
    }
  }

}
