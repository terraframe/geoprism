package dss.vector.solutions.gis.locatedIn;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.MdEntity;
import com.runwaysdk.system.metadata.MdType;

import dss.vector.solutions.MdssLog;
import dss.vector.solutions.geo.AllPaths;
import dss.vector.solutions.geo.AllowedIn;
import dss.vector.solutions.geo.GeoHierarchy;
import dss.vector.solutions.geo.LocatedIn;
import dss.vector.solutions.geo.LocatedInQuery;
import dss.vector.solutions.geo.generated.Earth;
import dss.vector.solutions.geo.generated.GeoEntity;
import dss.vector.solutions.gis.locatedIn.LocatedInBean.BuildTypes;
import dss.vector.solutions.util.QueryUtil;

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
      String id = QueryUtil.getIdColumn();

      String geoHierarchy = MdEntity.getMdEntity(GeoHierarchy.CLASS).getTableName();
      String allowedIn = MdEntity.getMdEntity(AllowedIn.CLASS).getTableName();
      String geoEntityClass = QueryUtil.getColumnName(GeoHierarchy.getGeoEntityClassMd());

      String geoEntity = MdEntity.getMdEntity(GeoEntity.CLASS).getTableName();
      String geoMultiPolygon = QueryUtil.getColumnName(GeoEntity.getGeoMultiPolygonMd());
      String geoData = QueryUtil.getColumnName(GeoEntity.getGeoDataMd());

      String mdType = MdEntity.getMdEntity(MdType.CLASS).getTableName();
      String type = QueryUtil.getColumnName(MdType.getTypeMd());
      String packageName = QueryUtil.getColumnName(MdType.getPackageNameMd());
      String typeName = QueryUtil.getColumnName(MdType.getTypeNameMd());

      String allPaths = MdEntity.getMdEntity(AllPaths.CLASS).getTableName();

      stmt = conn.createStatement();
      String sql = ""

      // FIXME use Runway call once the transaction bug is fixed
          + "DELETE FROM "
          + allPaths
          + ";\n"

          // dereference all universals for allowed_in lookup
          + "drop table if exists "
          + UNIVERSALS_TABLE
          + ";\n"
          + "create table "
          + UNIVERSALS_TABLE
          + " as (\n"
          + "    select c_gh."
          + geoEntityClass
          + " "
          + CHILD_CLASS
          + ", p_gh."
          + geoEntityClass
          + " "
          + PARENT_CLASS
          + " \n"
          + "    from "
          + allowedIn
          + " ai \n"
          + "    inner join "
          + geoHierarchy
          + " c_gh on c_gh."
          + id
          + " = ai."
          + RelationshipDAOIF.CHILD_ID_COLUMN
          + "\n"
          + "    inner join "
          + geoHierarchy
          + " p_gh on p_gh."
          + id
          + " = ai."
          + RelationshipDAOIF.PARENT_ID_COLUMN
          + "\n"
          + "  );\n"
          + "  create index parent_child_ind on "
          + UNIVERSALS_TABLE
          + "("
          + CHILD_CLASS
          + ", "
          + PARENT_CLASS
          + ");\n"
          // collect all geometry valid entities
          + "drop table if exists "
          + ENTITIES_TABLE
          + ";\n"
          + "create table "
          + ENTITIES_TABLE
          + " as (\n"
          + "  select g."
          + id
          + ", geom,\n"
          + "  md."
          + id
          + " as md,\n"
          + "  st_npoints(geom) points,\n"
          + "  g."+geoData+" geo_data\n"
          + "  from "
          + geoEntity
          + " g \n"
          + "  inner join (SELECT "
          + id
          + ", st_buffer("
          + geoMultiPolygon
          + ",0) geom FROM "
          + geoEntity
          + " \n"
          + "    WHERE "
          + geoMultiPolygon
          + " IS NOT NULL) g2 on g2."
          + id
          + " = g."
          + id
          + "\n"
          + "  inner join "
          + mdType
          + " md on g."
          + type
          + " =  (md."
          + packageName
          + " || '.' || md."
          + typeName
          + ")\n"
          + "  where st_isvalid(geom) AND st_issimple(geom) AND st_isempty(geom) != true AND st_isclosed(geom)\n"
          + ");\n" + "create unique index id_ind on " + ENTITIES_TABLE + "(" + id
          + ");\n"
          + "CREATE INDEX geom_ind ON "
          + ENTITIES_TABLE
          + " USING GIST (geom);\n"

          // child->parent mapping for new located_in relationships
          + "drop type if exists " + CHILD_PARENT_TYPE + " cascade;\n" + "create type "
          + CHILD_PARENT_TYPE + " as (" + CHILD_ID + " character(64), "
          + PARENT_ID
          + " character(64));\n"

          // failed entities
          + "drop table if exists " + FAILED_ENTITIES_TABLE + ";\n" + "create table "
          + FAILED_ENTITIES_TABLE + " as (SELECT " + id + " FROM " + geoEntity + " \n" + "WHERE " + type
          + " != '" + Earth.CLASS + "' EXCEPT SELECT " + id + " FROM " + ENTITIES_TABLE + ");\n";

      sql += "drop sequence if exists " + GEO_TOTAL_SEQ + ";";
      sql += "create sequence " + GEO_TOTAL_SEQ + ";";
      sql += "drop sequence if exists " + GEO_PROGRESS_SEQ + ";";
      sql += "create sequence " + GEO_PROGRESS_SEQ + ";";
      sql += "SELECT setval('" + GEO_TOTAL_SEQ + "', (SELECT count(*) + 1 FROM " + ENTITIES_TABLE
          + "));";

      stmt.execute(sql);
      conn.commit();
    }
    catch (SQLException e)
    {
      MdssLog.error("LocatedInBuilder", e);

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
    vq.SELECT(vq.aSQLInteger(PROCESSED, "p." + POSTGRES_SEQ_LAST_VALUE + ""), vq.aSQLInteger(TOTAL, "t."
        + POSTGRES_SEQ_LAST_VALUE + ""));
    vq.FROM("" + GEO_PROGRESS_SEQ + "", "p");
    vq.FROM("" + GEO_TOTAL_SEQ + "", "t");

    return vq.getIterator();
  }

  private void buildFunction()
  {
    String id = QueryUtil.getIdColumn();
    String locatedIn = MdEntity.getMdEntity(LocatedIn.CLASS).getTableName();

    String sql = "" +

    "CREATE OR REPLACE FUNCTION "
        + DERIVE_LOCATED_IN_REC_FUNC
        + "(pctThreshold integer, childId "
        + ENTITIES_TABLE
        + "."
        + id
        + "%TYPE, \n"
        + "  currentMd "
        + ENTITIES_TABLE
        + ".md%TYPE) \n"
        + "RETURNS SETOF "
        + CHILD_PARENT_TYPE
        + " AS \n"
        + "$$\n"
        + "DECLARE\n"
        + "  matched "
        + CHILD_PARENT_TYPE
        + "%ROWTYPE;\n"
        + "  parentMd "
        + UNIVERSALS_TABLE
        + "."
        + PARENT_CLASS
        + "%TYPE;\n"
        + "  foundMatch boolean;\n"
        + "BEGIN\n"
        + "  BEGIN\n"
        + "    FOR parentMd IN SELECT "
        + PARENT_CLASS
        + " FROM "
        + UNIVERSALS_TABLE
        + " WHERE "
        + CHILD_CLASS
        + " = currentMd LOOP\n"
        + "      foundMatch := false;\n"
        + "      FOR matched IN SELECT child."
        + id
        + " "
        + CHILD_ID
        + ", parents."
        + id
        + " "
        + PARENT_ID
        + "\n"
        + "        FROM "
        + ENTITIES_TABLE
        + " parents\n"
        + "        INNER JOIN "
        + ENTITIES_TABLE
        + " child ON child.id = childId AND child.geom && parents.geom\n"
        + "        AND parents.md = parentMd\n"
        + "        AND CASE WHEN st_dimension(child.geo_data) = 1 THEN st_crosses(st_geomfromtext(child.geo_data, st_srid(parents.geom)), parents.geom)\n"
        + " WHEN st_dimension(child.geo_data) = 0\n"
        + "   THEN st_within(st_centroid(child.geom), parents.geom)\n"
        + "        ELSE st_area(st_intersection(parents.geom, child.geom))/st_area(child.geom)*100.0 >= pctThreshold END\n"
        + "      LOOP\n" + "        foundMatch := true;\n" + "        RETURN QUERY SELECT matched."
        + CHILD_ID + ", matched." + PARENT_ID + ";\n" + "      END LOOP;\n"
        + "      IF foundMatch = false THEN\n" + "        RETURN QUERY SELECT * FROM "
        + DERIVE_LOCATED_IN_REC_FUNC + "(pctThreshold, childId, parentMd);\n" + "      END IF;\n"

        + "    END LOOP;\n" + "  EXCEPTION WHEN OTHERS THEN\n" + "    INSERT INTO "
        + FAILED_ENTITIES_TABLE + " (" + id + ") VALUES (childId);" + "    RETURN;\n" + "  END;\n"
        + "END\n" + "$$\n" + "LANGUAGE plpgsql VOLATILE;\n" + "CREATE OR REPLACE FUNCTION "
        + DERIVE_LOCATED_IN_FUNC + "(pctThreshold integer, deriveType int)\n" + "RETURNS SETOF "
        + CHILD_PARENT_TYPE + " AS\n" + "$$\n" + "DECLARE\n" + "  childRow " + ENTITIES_TABLE
        + "%ROWTYPE;\n" + "  pointAvg int;\n" + "  standardDev int;\n" + "BEGIN\n"
//        + "  SELECT round(AVG(points)) FROM " + ENTITIES_TABLE + " INTO pointAvg;\n"
//        + "  SELECT round(sqrt(AVG((points-pointAvg)^2))) FROM " + ENTITIES_TABLE
//        + " INTO standardDev;\n" + "  UPDATE " + ENTITIES_TABLE
//        + " SET geom = st_envelope(geom) WHERE points > (pointAvg+(standardDev*3));\n"
        + "  CASE deriveType\n" + "    WHEN 1, 2 THEN\n" + "      FOR childRow IN SELECT * FROM "
        + ENTITIES_TABLE + " LOOP\n" + "        RETURN QUERY SELECT * FROM "
        + DERIVE_LOCATED_IN_REC_FUNC + "(pctThreshold, childRow." + id + ", childRow.md);\n"
        + "        perform nextval('" + GEO_PROGRESS_SEQ + "');\n" + "      END LOOP;\n"
        + "    WHEN 3 THEN\n" + "      FOR childRow IN SELECT * FROM " + ENTITIES_TABLE
        + " e INNER JOIN \n" + "        (SELECT " + id + " from " + ENTITIES_TABLE + " except select "
        + RelationshipDAOIF.CHILD_ID_COLUMN + " from " + locatedIn + ") orphaned \n" + " ON orphaned."
        + id + " = e." + id + " LOOP\n" + "        RETURN QUERY SELECT * FROM " + DERIVE_LOCATED_IN_FUNC
        + "_rec(pctThreshold, childRow." + id + ", childRow.md);\n" + "        perform nextval('"
        + GEO_PROGRESS_SEQ + "');\n" + "      END LOOP;\n" + "    ELSE\n" + "      RETURN;\n"
        + "  END CASE;\n" + "  RETURN;\n" + "END\n" + "$$\n" + "LANGUAGE plpgsql VOLATILE;\n";

    Connection conn = Database.getConnection();
    Statement stmt = null;

    try
    {
      stmt = conn.createStatement();
      stmt.execute(sql);
      conn.commit();
    }
    catch (SQLException e)
    {
      MdssLog.error("LocatedInBuilder", e);

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

  // private void vacuumGeom()
  // {
  // Connection conn = Database.getConnection();
  // Statement stmt = null;
  // Boolean originalAutoCommit = null;
  //    
  // try
  // {
  // originalAutoCommit = new Boolean(conn.getAutoCommit());
  // conn.setAutoCommit(false); // cannot vacuum within a transaction
  // conn.createStatement().executeUpdate("VACUUM ANALYZE "+ENTITIES_TABLE+";");
  //      
  // }
  // catch (SQLException e)
  // {
  // Database.throwDatabaseException(e);
  // }
  // finally
  // {
  // try
  // {
  // if (stmt != null)
  // {
  // stmt.close();
  // }
  //        
  // if(originalAutoCommit != null)
  // {
  // conn.setAutoCommit(originalAutoCommit.booleanValue());
  // }
  // }
  // catch (SQLException sqlEx2)
  // {
  // Database.throwDatabaseException(sqlEx2);
  // }
  // }
  // }

  public void setup()
  {
    createTables();
    // vacuumGeom();
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
    vq.FROM("" + DERIVE_LOCATED_IN_FUNC + "(" + percent + "::integer, " + type.getCode() + "::integer)",
        "derivation");

    ValueQuery liVQ = new ValueQuery(f);
    LocatedInQuery liQ = new LocatedInQuery(liVQ);

    liVQ.SELECT(liVQ.aSQLCharacter("li_child_id", liQ.childId().getDbQualifiedName()), liVQ
        .aSQLCharacter("li_parent_id", liQ.parentId().getDbQualifiedName()));
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
      sql += "drop function " + DERIVE_LOCATED_IN_REC_FUNC + "(integer, " + ENTITIES_TABLE
          + ".id%TYPE, " + ENTITIES_TABLE + ".md%TYPE);";
      sql += "drop type " + CHILD_PARENT_TYPE + ";";
      sql += "drop table " + ENTITIES_TABLE + ";";
      sql += "drop table " + UNIVERSALS_TABLE + ";";
      sql += "drop table " + FAILED_ENTITIES_TABLE + ";";

      stmt.execute(sql);
      conn.commit();
    }
    catch (SQLException e)
    {
      MdssLog.error("LocatedInBuilder", e);

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
