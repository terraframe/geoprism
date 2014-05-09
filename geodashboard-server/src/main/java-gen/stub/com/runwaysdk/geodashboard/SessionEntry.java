package com.runwaysdk.geodashboard;

import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.gis.persist.DashboardMap;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;
import com.runwaysdk.util.IdParser;

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
   * Deletes the given map for this session.
   * 
   * @param mapId
   */
  @Transaction
  public static void deleteMapForSession(String mapId)
  {
    SessionIF session = Session.getCurrentSession();
    
    
  }
  
  /**
   * Deletes all maps for the current session.
   * 
   */
  @Transaction
  public static void deleteAllMapsForSession()
  {
    
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
