package com.runwaysdk.geodashboard;

import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.gis.SessionMapLimitException;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverProperties;
import com.runwaysdk.geodashboard.gis.persist.DashboardMap;
import com.runwaysdk.geodashboard.gis.persist.DashboardMapQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;

/**
 * This class should be treated as a facade and calling code should not directly manipulate or call SessionEntry
 * objects.
 * 
 * @author justin
 *
 */
public class SessionEntry extends SessionEntryBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1326763274;
  
  public SessionEntry()
  {
    super();
  }
  
  /**
   * The key is a combination of this object's unique id, user key, and session.
   */
  @Override
  protected String buildKey()
  {
    GeodashboardUser user = this.getDashboardUser();
    String key = user.getId()+"_"+this.getSessionId();
    return key;
  }
  
  /**
   * Deletes this object and associated artifacts, such as session (temporary) maps.
   */
  @Override
  public void delete()
  {
    for(DashboardMap map : this.getAllDashboardMap())
    {
      map.delete();
    }
    
    super.delete();
  }
  
  /**
   * Deletes all SessionEntry objects and their relationships.
   */
  @Transaction
  public static void deleteAll()
  {
    SessionEntryQuery q = new SessionEntryQuery(new QueryFactory());
    
    OIterator<? extends SessionEntry> iter = q.getIterator();
    try
    {
      while(iter.hasNext())
      {
        iter.next().delete();
      }
    }
    finally
    {
      iter.close();
    }
  }
  
  /**
   * Deletes the given map for this session.
   * 
   * @param mapId
   */
  @Transaction
  public static void deleteMapForSession(String mapId)
  {
//    SessionIF session = Session.getCurrentSession();
    
    DashboardMap.get(mapId).delete();
  }
  
  /**
   * Deletes all maps for the current session and the entry itself.
   */
  @Transaction
  public static void deleteAllMapsForSession()
  {
    SessionEntry entry = get();
    for(DashboardMap map : entry.getAllDashboardMap())
    {
      map.delete();
    }
  }
  
  /**
   * Deletes all SessionEntries with the given user.
   * @param user
   */
  @Transaction
  public static void deleteByUser(GeodashboardUser user)
  {
    QueryFactory f = new QueryFactory();
    SessionEntryQuery q = new SessionEntryQuery(f);
    
    q.WHERE(q.getDashboardUser().EQ(user));
    
    OIterator<? extends SessionEntry> iter = q.getIterator();
    
    try
    {
      while(iter.hasNext())
      {
        iter.next().delete();
      }
    }
    finally
    {
      iter.close();
    }
  }
  
  /**
   * Returns the SessionEntry based on the current user and session.
   * @return
   */
  public static SessionEntry get()
  {
    return get(GeodashboardUser.getCurrentUser(), Session.getCurrentSession().getId());
  }
  
  /**
   * Returns the SessionEntry with the given information or null if it does not exist.
   * 
   * @param user
   * @param sessionId
   * @return
   */
  public static SessionEntry get(GeodashboardUser user, String sessionId)
  {
    QueryFactory f = new QueryFactory();
    SessionEntryQuery q = new SessionEntryQuery(f);
    
    q.WHERE(q.getDashboardUser().EQ(user));
    q.WHERE(q.getSessionId().EQ(sessionId));
    
    OIterator<? extends SessionEntry> iter = q.getIterator();
    
    try
    {
      if(iter.hasNext())
      {
        return iter.next();
      }
      else
      {
        return null;
      }
    }
    finally
    {
      iter.close();
    }
  }
  
  /**
   * Creates a DashboardMap and links it to a SessionEntry.
   * @return
   */
  @Transaction
  public static DashboardMap createMapForSession()
  {
    SessionIF session = Session.getCurrentSession();
    String sessionId = session.getId();
    
    GeodashboardUser user = GeodashboardUser.getCurrentUser();
    
    // Create a SessionEntry and link it to the map.
    SessionEntry entry = get(user, sessionId);
    if(entry == null)
    {
      entry = new SessionEntry();
      entry.setDashboardUser(user);
      entry.setSessionId(sessionId);
      entry.apply();
    }
    else
    {
      // SessionEntry already exists. Make sure the user hasn't exceeded the map limit
      Integer max = GeoserverProperties.getSessionMapLimit();
      QueryFactory f = new QueryFactory();
      SessionEntryQuery seq = new SessionEntryQuery(f);
      DashboardMapQuery dmq = new DashboardMapQuery(f);
      
      seq.WHERE(seq.getDashboardUser().EQ(user));
      seq.AND(seq.getSessionId().EQ(sessionId));
      dmq.WHERE(dmq.sessionEntry(seq));
      
      if(dmq.getCount() >= max)
      {
        String msg = "User ["+user+"] cannot create more than ["+max+"] maps per session.";
        SessionMapLimitException ex = new SessionMapLimitException(msg);
        ex.setMapLimit(max);
        throw ex;
      }
    }
    
    
    // Create the temporary (session-based) map.
    DashboardMap map = new DashboardMap();
    
    // Give the map an auto-generated name since it's a temporary map
    // and won't be referenced directly by name.
    map.setName(sessionId+"_"+System.currentTimeMillis());
    map.apply();
    
    entry.addDashboardMap(map).apply();
    
    return map;
  }
}
