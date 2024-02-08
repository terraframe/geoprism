package net.geoprism.registry;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.locationtech.jts.geom.Envelope;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.orientdb.OrientDBRequest;
import com.runwaysdk.dataaccess.transaction.ThreadTransactionState;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.dataaccess.transaction.TransactionType;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;

import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.registry.tile.PublisherUtil;
import net.geoprism.registry.tile.VersionVectorTileBuilder;

public class LPGTileCache extends LPGTileCacheBase
{
  public static final ExecutorService executor = Executors.newFixedThreadPool(5, new ThreadFactory()
  {    
    @Override
    public Thread newThread(Runnable r)
    {
      Thread t = new Thread(r, "tile-builder");
      t.setDaemon(false);
      return t;
    }
  });

  private static class CacheCallable implements Callable<byte[]>
  {
    private ThreadTransactionState state;

    private String                 versionId;

    private String                 typeCode;

    private int                    x;

    private int                    y;

    private int                    zoom;

    public CacheCallable(ThreadTransactionState state, String versionId, String typeCode, int x, int y, int zoom)
    {
      super();
      this.state = state;
      this.versionId = versionId;
      this.typeCode = typeCode;
      this.x = x;
      this.y = y;
      this.zoom = zoom;
    }

    @Override
    public byte[] call() throws Exception
    {
      return this.call(this.state);
    }

    @Request(RequestType.THREAD)
    public byte[] call(ThreadTransactionState state)
    {
      try
      {
        /*
         * Re-check
         */
        byte[] cached = getCachedTile(versionId, typeCode, x, y, zoom);

        if (cached != null)
        {
          return cached;
        }

        // First ensure that orientdb is active on this thread
        OrientDBRequest request = (OrientDBRequest) GraphDBService.getInstance().getGraphDBRequest();
        ODatabaseSession oDatabaseSession = request.getODatabaseSession();

        if (!oDatabaseSession.isActiveOnCurrentThread())
        {
          oDatabaseSession.activateOnCurrentThread();
        }

        Envelope envelope = PublisherUtil.getEnvelope(x, y, zoom);
        Envelope bounds = PublisherUtil.getTileBounds(envelope);

        LabeledPropertyGraphTypeVersion version = LabeledPropertyGraphTypeVersion.get(this.versionId);

        VersionVectorTileBuilder builder = new VersionVectorTileBuilder(version, this.typeCode);
        byte[] tile = builder.writeVectorTiles(envelope, bounds);

        this.populateTile(state, tile);

        return tile;
      }
      catch (Exception e)
      {
        e.printStackTrace();

        return null;
      }
    }

    @Transaction(TransactionType.THREAD)
    private void populateTile(ThreadTransactionState state, byte[] tile)
    {
      LPGTileCache cache = new LPGTileCache();
      cache.setVersionId(this.versionId);
      cache.setTypeCode(this.typeCode);
      cache.setX(this.x);
      cache.setY(this.y);
      cache.setZ(this.zoom);
      cache.setTile(tile);
      cache.apply();
    }
  }

  @SuppressWarnings("unused")
  private static final long serialVersionUID = -1910869074;

  public LPGTileCache()
  {
    super();
  }

  public static byte[] getTile(JSONObject object) throws JSONException
  {
    String versionId = object.getString("oid");
    String typeCode = object.getString("typeCode");
    int x = object.getInt("x");
    int y = object.getInt("y");
    int zoom = object.getInt("z");

    return getTile(versionId, typeCode, x, y, zoom);
  }

  @Transaction
  private static byte[] getTile(String versionId, String typeCode, int x, int y, int zoom)
  {
    byte[] cached = getCachedTile(versionId, typeCode, x, y, zoom);

    if (cached != null)
    {
      return cached;
    }
    else
    {
      /*
       * Store the tile into the cache for future reads
       */
      SessionIF session = Session.getCurrentSession();

      if (session != null)
      {
        ThreadTransactionState state = ThreadTransactionState.getCurrentThreadTransactionState();

        try
        {
          byte[] result = executor.submit(new CacheCallable(state, versionId, typeCode, x, y, zoom)).get();

          return result;
        }
        catch (InterruptedException | ExecutionException e)
        {
          throw new ProgrammingErrorException(e);
        }
      }

      return null;
    }
  }

  public static byte[] getCachedTile(String versionId, String typeCode, int x, int y, int zoom)
  {
    LPGTileCacheQuery query = new LPGTileCacheQuery(new QueryFactory());
    query.WHERE(query.getVersion().EQ(versionId));
    query.WHERE(query.getTypeCode().EQ(typeCode));
    query.AND(query.getX().EQ(x));
    query.AND(query.getY().EQ(y));
    query.AND(query.getZ().EQ(zoom));

    try (OIterator<? extends LPGTileCache> it = query.getIterator())
    {
      if (it.hasNext())
      {
        LPGTileCache tile = it.next();
        return tile.getTile();
      }

      return null;
    }
  }

  public static void deleteTiles(LabeledPropertyGraphTypeVersion version)
  {
    LPGTileCacheQuery query = new LPGTileCacheQuery(new QueryFactory());
    query.WHERE(query.getVersion().EQ(version));

    try (OIterator<? extends LPGTileCache> it = query.getIterator())
    {
      while (it.hasNext())
      {
        it.next().delete();
      }
    }
  }

}
