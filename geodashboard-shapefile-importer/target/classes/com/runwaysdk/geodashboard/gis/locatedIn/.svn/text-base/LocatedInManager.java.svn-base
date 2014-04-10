package dss.vector.solutions.gis.locatedIn;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.runwaysdk.ProblemException;
import com.runwaysdk.ProblemIF;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.SelectableSQLCharacter;
import com.runwaysdk.query.SelectableSQLInteger;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestState;
import com.runwaysdk.system.metadata.MdBusiness;
import com.sun.tools.javac.util.Pair;

import dss.vector.solutions.geo.AllPaths;
import dss.vector.solutions.geo.GeoHierarchy;
import dss.vector.solutions.geo.LocatedIn;
import dss.vector.solutions.geo.LocatedInQuery;
import dss.vector.solutions.geo.generated.Earth;
import dss.vector.solutions.geo.generated.EarthQuery;
import dss.vector.solutions.geo.generated.GeoEntity;
import dss.vector.solutions.geo.generated.GeoEntityQuery;
import dss.vector.solutions.gis.Localizer;
import dss.vector.solutions.gis.GISImportLoggerIF;
import dss.vector.solutions.gis.TaskObservable;
import dss.vector.solutions.gis.locatedIn.LocatedInBean.BuildTypes;

public class LocatedInManager extends TaskObservable implements UncaughtExceptionHandler, Reloadable
{
  private LocatedInBean bean;

  private Throwable     throwable;

  public LocatedInManager(LocatedInBean bean)
  {
    this.bean = bean;
    this.throwable = null;
  }

  @Request
  public void run(GISImportLoggerIF logger) throws InvocationTargetException
  {
    try
    {
      this.fireStart();

      MdBusiness mdBusiness = MdBusiness.getMdBusiness(AllPaths.CLASS);
      mdBusiness.deleteAllTableRecords();

      if (bean.getOption().equals(BuildTypes.REBUILD_ALL))
      {
        this.deleteExisting();
      }
      else if(bean.getOption().equals(BuildTypes.ADDITIVE))
      {
        this.deleteEarthChildren();
      }

      List<Pair<String, String>> pairs = this.computeLocatedInRelationship(logger);

      this.build(pairs, logger);

      this.handleOrphanedEntities();

      this.rebuildAllPaths();

      this.fireTaskDone(true);
    }
    catch (RuntimeException e)
    {
      this.fireTaskDone(false);

      throw e;
    }
    catch (Exception e)
    {
      this.fireTaskDone(false);

      throw new InvocationTargetException(e);
    }

  }
  
  /**
   * Deletes all children under Earth whose universal is not directly allowed under Earth.
   * For example, Country entities will most likely remain but not Village entities.
   */
  @Transaction
  protected void deleteEarthChildren()
  {
    // First query for all valid child universal types that are not directly beneath earth.
    QueryFactory f = new QueryFactory();
    ValueQuery v = new ValueQuery(f);

    v.setSqlPrefix(GeoHierarchy.getAllPathsSQL());
    
    SelectableSQLCharacter childType = v.aSQLCharacter(GeoHierarchy.ALLPATHS_CHILD_TYPE, GeoHierarchy.ALLPATHS_CHILD_TYPE);
    SelectableSQLInteger depth = v.aSQLAggregateInteger(GeoHierarchy.ALLPATHS_DEPTH, "MAX(" + GeoHierarchy.ALLPATHS_DEPTH + ")");
    
    v.SELECT(childType);
    
    v.FROM(GeoHierarchy.ALLPATHS_ROLLUP, GeoHierarchy.ALLPATHS_ROLLUP);
    
    v.GROUP_BY(childType);
    v.HAVING(depth.GT(0));
    
    List<ValueObject> objs = v.getIterator().getAll();
    String[] types = new String[objs.size()];
    for(int i=0; i<objs.size(); i++)
    {
      types[i] = objs.get(i).getValue(GeoHierarchy.ALLPATHS_CHILD_TYPE);
    }
    
    
    // now query all located in relationships whose child entities contain the allowed types
    QueryFactory f2 = new QueryFactory();
    LocatedInQuery toDelete = new LocatedInQuery(f2);
    GeoEntityQuery children = new GeoEntityQuery(f2);
    EarthQuery parent = new EarthQuery(f2);
    
    children.WHERE(children.getType().IN(types));
    toDelete.WHERE(toDelete.hasChild(children));
    toDelete.WHERE(toDelete.hasParent(parent));
    
    toDelete.ORDER_BY_ASC(toDelete.getType());
    
    int count = (int) toDelete.getCount();

    this.fireStartTask(Localizer.getMessage("DELETE_EXISTING"), count);
    
    OIterator<? extends LocatedIn> validChildren = toDelete.getIterator();

    try
    {
      while (validChildren.hasNext())
      {
        LocatedIn relationship = validChildren.next();
        relationship.delete();
        
        this.fireTaskProgress(1);
      }
    }
    finally
    {
      validChildren.close();
    }
  }

  @Transaction
  protected void deleteExisting()
  {
    LocatedInQuery query = new LocatedInQuery(new QueryFactory());
    int count = (int) query.getCount();

    this.fireStartTask(Localizer.getMessage("DELETE_EXISTING"), count);

    OIterator<? extends LocatedIn> it = query.getIterator();

    try
    {
      while (it.hasNext())
      {
        LocatedIn relationship = it.next();
        relationship.delete();

        this.fireTaskProgress(1);
      }
    }
    finally
    {
      it.close();
    }
  }

  @Transaction
  protected void handleOrphanedEntities()
  {
    Earth earth = Earth.getEarthInstance();

    GeoEntityQuery query = this.getOrphanedChildren();

    int count = (int) query.getCount();

    this.fireStartTask(Localizer.getMessage("FIXING_ORPHANED_CHILDREN"), count);

    OIterator<? extends GeoEntity> it = query.getIterator();

    try
    {
      while (it.hasNext())
      {
        GeoEntity entity = it.next();

        LocatedIn locatedIn = entity.addLocatedInGeoEntity(earth);
        locatedIn.applyWithoutCreatingAllPaths();

        this.fireTaskProgress(1);
      }
    }
    finally
    {
      it.close();
    }
  }

  protected GeoEntityQuery getOrphanedChildren()
  {
    String earthId = Earth.getEarthInstance().getId();

    QueryFactory factory = new QueryFactory();

    LocatedInQuery locatedInQuery = new LocatedInQuery(factory);

    GeoEntityQuery query = new GeoEntityQuery(factory);
    query.WHERE(query.getId().NE(locatedInQuery.childId()));
    query.AND(query.getId().NE(earthId));

    return query;
  }

  private void rebuildAllPaths()
  {
    this.fireStartTask(Localizer.getMessage("REBUILD_ALL_PATHS"), -1);

    // Rebuild the all paths table
    GeoEntity.buildAllPathsFast();
  }

  private void build(List<Pair<String, String>> pairs, GISImportLoggerIF logger)
  {
    this.fireStartTask(Localizer.getMessage("BUILD_LOCATED_IN"), pairs.size());

    for (Pair<String, String> pair : pairs)
    {
      try
      {
        buildRelationship(pair.fst, pair.snd);
      }
      catch (Exception e)
      {
        try
        {
          GeoEntity entity = GeoEntity.get(pair.snd);
          logger.log(entity.getGeoId(), e);
        }
        catch (Exception ex)
        {
          logger.log(pair.fst, e);
        }
      }

      this.fireTaskProgress(1);
    }
  }

  @Transaction
  private void buildRelationship(String parentId, String childId)
  {
    GeoEntity parent = GeoEntity.get(parentId);
    GeoEntity child = GeoEntity.get(childId);

    LocatedIn locatedIn = child.addLocatedInGeoEntity(parent);
    locatedIn.applyWithoutCreatingAllPaths();

    // We must ensure that any problems created during the transaction are
    // logged now instead of when the request returns. As such, if any problems
    // exist immediately throw a ProblemException so that normal exception
    // handling can occur.
    List<ProblemIF> problems = RequestState.getProblemsInCurrentRequest();

    if (problems.size() != 0)
    {
      throw new ProblemException(null, problems);
    }
  }

  private List<Pair<String, String>> computeLocatedInRelationship(GISImportLoggerIF logger) throws InvocationTargetException
  {
    this.fireStartTask(Localizer.getMessage("PREPARE_COMPUTATION"), -1);

    LocatedInBuilder builder = new LocatedInBuilder(bean.getOption(), bean.getOverlapPercent());
    ComputeLocatedInRunner runner = new ComputeLocatedInRunner(builder);
    builder.setup();

    try
    {
      Thread thread = new Thread(runner);
      thread.setDaemon(true);
      thread.setUncaughtExceptionHandler(this);
      thread.start();

      this.setProgressTotal(builder);

      int previous = 1;

      while (thread.isAlive())
      {
        OIterator<ValueObject> progress = builder.getProgress();

        try
        {
          ValueObject progressObject = progress.next();
          String processed = progressObject.getValue(LocatedInBuilder.PROCESSED);
          int current = Integer.parseInt(processed);

          if (previous != current)
          {
            this.fireTaskProgress(current - previous);

            previous = current;
          }
        }
        finally
        {
          progress.close();
        }

        try
        {
          // Try to have this thread sleep to avoid spaming postgres with
          // progress requests
          Thread.sleep(100);
        }
        catch (InterruptedException e)
        {
          // Do nothing, let this thread continue as normal
        }
      }

      // Ensure that any exception thrown in the computation thread is
      // propigated to the manager.
      if (this.throwable != null)
      {
        if (this.throwable instanceof RuntimeException)
        {
          throw (RuntimeException) this.throwable;
        }

        throw new InvocationTargetException(this.throwable);
      }

      this.processInvalidGeometries(builder, logger);
    }
    finally
    {
      this.fireStartTask(Localizer.getMessage("CLEANUP_COMPUTATION"), -1);

      builder.cleanup();
    }

    return runner.getList();
  }

  private void processInvalidGeometries(LocatedInBuilder builder, GISImportLoggerIF logger)
  {
    this.fireStartTask(Localizer.getMessage("PROCESS_INVALID_GEOMETRIES"), -1);

    OIterator<ValueObject> it = builder.getFailedEntities();

    try
    {
      while (it.hasNext())
      {
        ValueObject valueObject = it.next();
        String id = valueObject.getValue(LocatedInBuilder.FAILED_ENTITY_ID);

        try
        {
          GeoEntity entity = GeoEntity.get(id);

          logger.log(entity.getGeoId(), Localizer.getMessage("UNABLE_TO_COMPUTE_GEOMETRY"));
        }
        catch (Exception e)
        {
          logger.log(id, Localizer.getMessage("UNABLE_TO_COMPUTE_GEOMETRY"));
        }
      }
    }
    finally
    {
      it.close();
    }
  }

  private void setProgressTotal(final LocatedInBuilder builder)
  {
    OIterator<ValueObject> progress = builder.getProgress();

    try
    {
      ValueObject progressObject = progress.next();
      String total = progressObject.getValue(LocatedInBuilder.TOTAL);

      this.fireStartTask(Localizer.getMessage("COMPUTE_LOCATED_IN"), Integer.parseInt(total));
    }
    finally
    {
      progress.close();
    }
  }

  public void uncaughtException(Thread t, Throwable e)
  {
    throwable = e;
  }
}
