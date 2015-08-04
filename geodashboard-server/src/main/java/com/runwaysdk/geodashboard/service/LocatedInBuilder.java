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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.service.LocatedInBean.BuildTypes;
import com.runwaysdk.logging.LogLevel;
import com.runwaysdk.logging.RunwayLogUtil;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.LocatedInQuery;
import com.runwaysdk.system.metadata.MdEntity;

public class LocatedInBuilder implements Reloadable
{
  private BuildTypes          type;

  private int                 percent;

  private volatile boolean    inProgress;

  public static final String  CHILD_ID                   = "child_id";

  public static final String  PARENT_ID                  = "parent_id";

  public static final String  PROCESSED                  = "processed";

  public static final String  TOTAL                      = "total";

  public static final String  FAILED_ENTITY_ID           = "id";

  private static final String UNIVERSALS_TABLE           = "universals";

  private static final String ENTITIES_TABLE             = "entities";

  private static final String FAILED_ENTITIES_TABLE      = "failed_entities";

  private static final String CHILD_PARENT_TYPE          = "child_parent";

  private static final String DERIVE_LOCATED_IN_FUNC     = "derive_located_in";

  private static final String DERIVE_LOCATED_IN_REC_FUNC = "derive_located_in_rec";

  private static final String GEO_TOTAL_SEQ              = "geo_total";

  private static final String GEO_PROGRESS_SEQ           = "geo_progres";

  private static final String POSTGRES_SEQ_LAST_VALUE    = "last_value";

  private static final String PARENT_CLASS               = "parent_class";

  private static final String CHILD_CLASS                = "child_class";

  @Request
  public static void main(String[] args)
  {
    LocatedInBuilder b = new LocatedInBuilder(BuildTypes.REBUILD_ALL, 80);
    b.setup();
    b.deriveLocatedIn();
    b.cleanup();
  }

  public LocatedInBuilder(BuildTypes type, int percent)
  {
    this.type = type;
    this.percent = percent;
    this.inProgress = false;
  }

  private void createTables()
  {
    Connection conn = Database.getConnection();
    Statement stmt = null;

    try
    {
      String id = LocatedInQueryUtil.getIdColumn();

      String geoEntity = MdEntity.getMdEntity(GeoEntity.CLASS).getTableName();
      String geoMultiPolygonAttr = LocatedInQueryUtil.getColumnName(GeoEntity.getGeoMultiPolygonMd());
      String geoDataAttr = LocatedInQueryUtil.getColumnName(GeoEntity.getWktMd());

      String allowedIn = MdEntity.getMdEntity(AllowedIn.CLASS).getTableName();

      stmt = conn.createStatement();
      StringBuffer sql = new StringBuffer();

      // FIXME use Runway call once the transaction bug is fixed
      // sql.append("DELETE FROM " + allPaths + ";\n");

      // dereference all universals for allowed_in lookup
      sql.append("DROP TABLE IF EXISTS " + UNIVERSALS_TABLE + ";\n");
      sql.append("CREATE TABLE " + UNIVERSALS_TABLE + " AS (\n");
      sql.append("  SELECT ai.child_id " + CHILD_CLASS + ",\n");
      sql.append("         ai.parent_id " + PARENT_CLASS + "\n");
      sql.append("  FROM " + allowedIn + " AS ai \n");
      sql.append(");\n");

      sql.append("CREATE INDEX parent_child_ind ON " + UNIVERSALS_TABLE + "(" + CHILD_CLASS + ", " + PARENT_CLASS + ");\n");

      // collect all geometry valid entities
      sql.append("DROP TABLE IF EXISTS " + ENTITIES_TABLE + ";\n");
      sql.append("CREATE TABLE " + ENTITIES_TABLE + " AS (\n");
      sql.append("  SELECT g." + id + ", geom,\n" + "  g.universal AS univ,\n" + "  st_npoints(geom) points,\n" + "  g." + geoDataAttr + " geo_data\n");
      sql.append("  FROM " + geoEntity + " g \n");
      sql.append("  INNER JOIN (\n");
      sql.append("    SELECT " + id + ", st_buffer(" + geoMultiPolygonAttr + ",0) AS geom\n");
      sql.append("    FROM " + geoEntity + " \n");
      sql.append("    WHERE " + geoMultiPolygonAttr + " IS NOT NULL\n");
      sql.append("  ) g2 ON g2." + id + " = g." + id + "\n");
      sql.append("  WHERE st_isvalid(geom) AND st_issimple(geom) AND st_isempty(geom) != true AND st_isclosed(geom)\n");
      sql.append(");\n");

      // Create indexes for the geometry lookup
      sql.append("CREATE UNIQUE INDEX id_ind ON " + ENTITIES_TABLE + "(" + id + ");\n");
      sql.append("CREATE INDEX geom_ind ON " + ENTITIES_TABLE + " USING GIST (geom);\n");

      // child->parent mapping for new located_in relationships
      sql.append("DROP TYPE IF EXISTS " + CHILD_PARENT_TYPE + " CASCADE;\n");
      sql.append("CREATE TYPE " + CHILD_PARENT_TYPE + " AS (");
      sql.append("  " + CHILD_ID + " character(64), " + PARENT_ID + " character(64)");
      sql.append(");\n");

      // failed entities
      sql.append("DROP TABLE IF EXISTS " + FAILED_ENTITIES_TABLE + ";\n");
      sql.append("CREATE TABLE " + FAILED_ENTITIES_TABLE + " AS (");
      sql.append("  SELECT " + id);
      sql.append("  FROM " + geoEntity + " \n");
      sql.append("  EXCEPT (\n");
      sql.append("    SELECT " + id);
      sql.append("    FROM " + ENTITIES_TABLE);
      sql.append("  )\n");
      sql.append(");\n");

      // Create progress sequences
      sql.append("DROP SEQUENCE IF EXISTS " + GEO_TOTAL_SEQ + ";\n");
      sql.append("CREATE SEQUENCE " + GEO_TOTAL_SEQ + ";\n");

      sql.append("DROP SEQUENCE IF EXISTS " + GEO_PROGRESS_SEQ + ";\n");
      sql.append("CREATE SEQUENCE " + GEO_PROGRESS_SEQ + ";\n");
      sql.append("SELECT setval('" + GEO_TOTAL_SEQ + "', (SELECT count(*) + 1 FROM " + ENTITIES_TABLE + "));\n");

      stmt.execute(sql.toString());
      conn.commit();
    }
    catch (SQLException e)
    {
      RunwayLogUtil.logToLevel(LogLevel.ERROR, "Error occured while creating the database tables used for the located in builder", e);
      Database.throwDatabaseException(e);
    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }

      try
      {
        Database.closeConnection(conn);
      }
      catch (SQLException e)
      {
        Database.throwDatabaseException(e);
      }
    }
  }

  public boolean inProgress()
  {
    return this.inProgress;
  }

  public OIterator<ValueObject> getProgress()
  {
    QueryFactory f = new QueryFactory();
    ValueQuery vq = new ValueQuery(f);
    vq.SELECT(vq.aSQLInteger(PROCESSED, "p." + POSTGRES_SEQ_LAST_VALUE + ""), vq.aSQLInteger(TOTAL, "t." + POSTGRES_SEQ_LAST_VALUE + ""));
    vq.FROM("" + GEO_PROGRESS_SEQ + "", "p");
    vq.FROM("" + GEO_TOTAL_SEQ + "", "t");

    return vq.getIterator();
  }

  private void buildFunction()
  {
    String id = LocatedInQueryUtil.getIdColumn();
    String locatedIn = MdEntity.getMdEntity(LocatedIn.CLASS).getTableName();

    StringBuffer sql = new StringBuffer();

    // Define the function which create the derived located in recursive values
    sql.append("CREATE OR REPLACE FUNCTION " + DERIVE_LOCATED_IN_REC_FUNC + "(");
    sql.append("  pctThreshold integer, childId " + ENTITIES_TABLE + "." + id + "%TYPE, univ " + ENTITIES_TABLE + ".univ%TYPE)\n");
    sql.append("RETURNS SETOF " + CHILD_PARENT_TYPE + " AS \n");
    sql.append("$BODY$\n");
    sql.append("DECLARE\n");
    sql.append("  matched " + CHILD_PARENT_TYPE + "%ROWTYPE;\n");
    sql.append("  parentMd " + UNIVERSALS_TABLE + "." + PARENT_CLASS + "%TYPE;\n");
    sql.append("  foundMatch boolean;\n");
    sql.append("BEGIN\n");
    sql.append("  BEGIN\n");
    sql.append("    FOR parentMd IN SELECT " + PARENT_CLASS + " FROM " + UNIVERSALS_TABLE + " WHERE " + CHILD_CLASS + " = univ\n");
    sql.append("    LOOP\n");
    sql.append("      foundMatch := false;\n");
    sql.append("      FOR matched IN SELECT child." + id + " " + CHILD_ID + ", parents." + id + " " + PARENT_ID + "\n");
    sql.append("      FROM " + ENTITIES_TABLE + " parents\n");
    sql.append("      INNER JOIN " + ENTITIES_TABLE + " child ON child.id = childId");
    sql.append("      AND child.geom && parents.geom\n");
    sql.append("      AND parents.univ = parentMd\n");
    sql.append("      AND CASE ");
    sql.append("        WHEN st_dimension(child.geo_data) = 1 THEN st_crosses(st_geomfromtext(child.geo_data, st_srid(parents.geom)), parents.geom)\n");
    sql.append("        WHEN st_dimension(child.geo_data) = 0 THEN st_within(st_centroid(child.geom), parents.geom)\n");
    sql.append("        ELSE st_area(st_intersection(parents.geom, child.geom))/st_area(child.geom)*100.0 >= pctThreshold END\n");
    sql.append("      LOOP\n");
    sql.append("        foundMatch := true;\n");
    sql.append("        RETURN QUERY SELECT matched." + CHILD_ID + ", matched." + PARENT_ID + ";\n");
    sql.append("      END LOOP;\n");
    sql.append("      IF foundMatch = false THEN\n");
    sql.append("        RETURN QUERY SELECT * FROM " + DERIVE_LOCATED_IN_REC_FUNC + "(pctThreshold, childId, parentMd);\n");
    sql.append("      END IF;\n");
    sql.append("    END LOOP;\n");
    sql.append("  EXCEPTION WHEN OTHERS THEN\n");
    sql.append("    INSERT INTO " + FAILED_ENTITIES_TABLE + " (" + id + ") VALUES (childId);" + "    RETURN;\n");
    sql.append("  END;\n");
    sql.append("END\n");
    sql.append("$BODY$\n");
    sql.append("LANGUAGE plpgsql VOLATILE;\n");

    // Define the function which create the derived located in values
    sql.append("CREATE OR REPLACE FUNCTION " + DERIVE_LOCATED_IN_FUNC + "(pctThreshold integer, deriveType int)\n");
    sql.append("RETURNS SETOF " + CHILD_PARENT_TYPE + " AS\n");
    sql.append("$BODY$\n");
    sql.append("DECLARE\n");
    sql.append("  childRow " + ENTITIES_TABLE + "%ROWTYPE;\n");
    sql.append("  pointAvg int;\n");
    sql.append("  standardDev int;\n");
    sql.append("BEGIN\n");
    sql.append("  SELECT round(AVG(points)) FROM " + ENTITIES_TABLE + " INTO pointAvg;\n");
    sql.append("  SELECT round(sqrt(AVG((points-pointAvg)^2))) FROM " + ENTITIES_TABLE + " INTO standardDev;\n");
    sql.append("  UPDATE " + ENTITIES_TABLE + " SET geom = st_envelope(geom) WHERE points > (pointAvg+(standardDev*3));\n");
    sql.append("  CASE deriveType\n");
    sql.append("    WHEN 1, 2 THEN\n");
    sql.append("      FOR childRow IN SELECT * FROM " + ENTITIES_TABLE + "\n");
    sql.append("      LOOP\n");
    sql.append("        RETURN QUERY SELECT * FROM " + DERIVE_LOCATED_IN_REC_FUNC + "(pctThreshold, childRow." + id + ", childRow.univ);\n");
    sql.append("        perform nextval('" + GEO_PROGRESS_SEQ + "');\n");
    sql.append("      END LOOP;\n");
    sql.append("    WHEN 3 THEN\n");
    sql.append("      FOR childRow IN SELECT * FROM " + ENTITIES_TABLE + " e INNER JOIN (\n");
    sql.append("        SELECT " + id + " FROM " + ENTITIES_TABLE + " EXCEPT SELECT " + RelationshipDAOIF.CHILD_ID_COLUMN + " FROM " + locatedIn + ") orphaned \n" + " ON orphaned." + id + " = e." + id + "\n");
    sql.append("      LOOP\n");
    sql.append("        RETURN QUERY SELECT * FROM " + DERIVE_LOCATED_IN_FUNC + "_rec(pctThreshold, childRow." + id + ", childRow.univ);\n");
    sql.append("        perform nextval('" + GEO_PROGRESS_SEQ + "');\n");
    sql.append("      END LOOP;\n");
    sql.append("    ELSE\n");
    sql.append("      RETURN;\n");
    sql.append("  END CASE;\n");
    sql.append("  RETURN;\n");
    sql.append("END\n");
    sql.append("$BODY$\n");
    sql.append("LANGUAGE plpgsql VOLATILE;\n");

    Connection conn = Database.getConnection();
    Statement stmt = null;

    try
    {
      stmt = conn.createStatement();
      stmt.execute(sql.toString());
      conn.commit();
    }
    catch (SQLException e)
    {
      RunwayLogUtil.logToLevel(LogLevel.ERROR, "Error occured while rebuilding the located in all paths", e);

      Database.throwDatabaseException(e);
    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }

      try
      {
        Database.closeConnection(conn);
      }
      catch (SQLException e)
      {
        Database.throwDatabaseException(e);
      }
    }
  }

  public void setup()
  {
    createTables();
    buildFunction();
  }

  public OIterator<ValueObject> getFailedEntities()
  {
    QueryFactory f = new QueryFactory();
    ValueQuery vq = new ValueQuery(f);

    vq.SELECT(vq.aSQLCharacter(FAILED_ENTITY_ID, FAILED_ENTITY_ID));
    vq.FROM("" + FAILED_ENTITIES_TABLE + "", "" + FAILED_ENTITIES_TABLE + "");

    return vq.getIterator();
  }

  public OIterator<ValueObject> deriveLocatedIn()
  {
    QueryFactory f = new QueryFactory();
    ValueQuery minus = new ValueQuery(f);

    ValueQuery vq = new ValueQuery(f);
    vq.SELECT_DISTINCT(vq.aSQLCharacter(CHILD_ID, CHILD_ID), vq.aSQLCharacter(PARENT_ID, PARENT_ID));
    vq.FROM("" + DERIVE_LOCATED_IN_FUNC + "(" + percent + "::integer, " + type.getCode() + "::integer)", "derivation");

    ValueQuery liVQ = new ValueQuery(f);
    LocatedInQuery liQ = new LocatedInQuery(liVQ);

    liVQ.SELECT(liVQ.aSQLCharacter("li_child_id", liQ.childId().getDbQualifiedName()), liVQ.aSQLCharacter("li_parent_id", liQ.parentId().getDbQualifiedName()));
    liVQ.FROM(liQ.getMdClassIF().getTableName(), liQ.getTableAlias());

    minus.MINUS(vq, liVQ);

    this.inProgress = true;

    OIterator<ValueObject> iter = minus.getIterator();

    this.inProgress = false;

    return iter;
  }

  public void cleanup()
  {
    Connection conn = Database.getConnection();
    Statement stmt = null;

    try
    {
      stmt = conn.createStatement();
      String sql = "";
      sql += "drop sequence " + GEO_TOTAL_SEQ + ";";
      sql += "drop sequence " + GEO_PROGRESS_SEQ + ";";
      sql += "drop function " + DERIVE_LOCATED_IN_FUNC + "(integer, integer);";
      sql += "drop function " + DERIVE_LOCATED_IN_REC_FUNC + "(integer, " + ENTITIES_TABLE + ".id%TYPE, " + ENTITIES_TABLE + ".univ%TYPE);";
      sql += "drop type " + CHILD_PARENT_TYPE + ";";
      sql += "drop table " + ENTITIES_TABLE + ";";
      sql += "drop table " + UNIVERSALS_TABLE + ";";
      sql += "drop table " + FAILED_ENTITIES_TABLE + ";";

      stmt.execute(sql);
      conn.commit();
    }
    catch (SQLException e)
    {
      RunwayLogUtil.logToLevel(LogLevel.ERROR, "Error occured while cleaning up the located in builder", e);

      Database.throwDatabaseException(e);
    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }

      try
      {
        Database.closeConnection(conn);
      }
      catch (SQLException e)
      {
        Database.throwDatabaseException(e);
      }
    }
  }
}
