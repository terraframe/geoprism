package net.geoprism.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.dataaccess.database.Database;

public class DatabaseUtil
{
  private static boolean IS_MATERIALIZED = true;

  /**
   * Creates a view.
   * 
   * @param viewName
   * @param query
   */
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
          list.add(resultSet.getString("viewName"));
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

}
