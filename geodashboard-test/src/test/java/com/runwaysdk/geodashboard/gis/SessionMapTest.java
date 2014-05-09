package com.runwaysdk.geodashboard.gis;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.GeodashboardUser;
import com.runwaysdk.geodashboard.SessionEntry;
import com.runwaysdk.geodashboard.SessionEntryQuery;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverProperties;
import com.runwaysdk.geodashboard.gis.persist.DashboardMap;
import com.runwaysdk.geodashboard.gis.persist.DashboardMapQuery;
import com.runwaysdk.geodashboard.gis.persist.SessionMapQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.system.Roles;

public class SessionMapTest
{
  private static final String     USER = "TestUser";

  private static final String     PASS = "TestPass";

  private static GeodashboardUser user;

  @BeforeClass
  @Request
  @Transaction
  public static void classSetup()
  {
    user = new GeodashboardUser();
    user.setUsername(USER);
    user.setEmail("TestUser@Test.com");
    user.setFirstName("Test");
    user.setLastName("User");
    user.setPassword(PASS);
    user.setSessionLimit(10000);
    user.apply();

    Roles.findRoleByName("Administrator").addSingleActor(user).apply();
  }

  @AfterClass
  @Request
  @Transaction
  public static void classTeardown()
  {
    SessionEntry.deleteAll();
    user.delete();
  }

  @Test
  public void createMapForSession()
  {
    String sessionId = SessionFacade.logIn(USER, PASS, new Locale[] { Locale.ENGLISH });
    try
    {
      createMapForSessionRequest(sessionId);
    }
    finally
    {
      SessionFacade.closeSession(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  private void createMapForSessionRequest(String sessionId)
  {
    GeodashboardUser user = GeodashboardUser.getCurrentUser();
    DashboardMap map = SessionEntry.createMapForSession();
    String mapId = map.getId();

    // Make sure the map exists
    QueryFactory f = new QueryFactory();
    DashboardMapQuery dmq = new DashboardMapQuery(f);

    dmq.WHERE(dmq.getId().EQ(mapId));

    Assert.assertTrue(dmq.getCount() == 1);

    // Make sure the map links to the SessionEntry
    f = new QueryFactory();
    SessionEntryQuery seq = new SessionEntryQuery(f);
    SessionMapQuery smq = new SessionMapQuery(f);

    smq.WHERE(smq.childId().EQ(mapId));
    seq.WHERE(seq.dashboardMap(smq));

    OIterator<? extends SessionEntry> iter = seq.getIterator();

    try
    {
      if (iter.hasNext())
      {
        SessionEntry entry = iter.next();
        Assert.assertEquals(entry.getSessionId(), sessionId);
        Assert.assertEquals(entry.getDashboardUser(), user);

        if (iter.hasNext())
        {
          Assert.fail("More than one instance of SessionEntry associated with a map");
        }
      }
    }
    finally
    {
      iter.close();
    }

    // Delete the map for the session
    SessionEntry.deleteMapForSession(mapId);

    try
    {
      DashboardMap.get(mapId);

      Assert.fail("Map [" + mapId + "] was not deleted.");
    }
    catch (DataNotFoundException e)
    {
      // this is expected
    }
  }

  @Test
  public void createMultipleMapsForSession()
  {
    String sessionId = SessionFacade.logIn(USER, PASS, new Locale[] { Locale.ENGLISH });
    try
    {
      createMultipleMapsForSessionRequest(sessionId);
    }
    finally
    {
      SessionFacade.closeSession(sessionId);
    }
  }

  /**
   * Creates multiple maps for a single session.
   * 
   * @param sessionId
   */
  @Request(RequestType.SESSION)
  private void createMultipleMapsForSessionRequest(String sessionId)
  {
    int numMaps = 10;

    List<DashboardMap> maps = new LinkedList<DashboardMap>();
    List<String> mapIds = new LinkedList<String>();
    for (int i = 0; i < numMaps; i++)
    {
      DashboardMap map = SessionEntry.createMapForSession();
      maps.add(map);
      mapIds.add(map.getId());
    }

    QueryFactory f = new QueryFactory();
    SessionEntryQuery seq = new SessionEntryQuery(f);
    SessionMapQuery q = new SessionMapQuery(f);

    seq.WHERE(seq.getSessionId().EQ(sessionId));

    q.WHERE(q.childId().IN(mapIds.toArray(new String[mapIds.size()])));
    q.AND(q.hasParent(seq));

    Assert.assertEquals(q.getCount(), numMaps);

    SessionEntry.deleteAllMapsForSession();

    // re-run to make sure there's no relationships
    Assert.assertEquals(q.getCount(), 0);
  }

  /**
   * Makes sure an error is thrown if a session exceeds the number of maps.
   */
  @Test
  public void testMaximumSessionMaps()
  {
    String sessionId = SessionFacade.logIn(USER, PASS, new Locale[] { Locale.ENGLISH });
    try
    {
      Integer max = GeoserverProperties.getSessionMapLimit();
      createMultipleMapsRequest(sessionId, max);
      
      Assert.fail("A session was able to create more than the maximum number of maps ["+max+"].");
    }
    catch(SessionMapLimitException ex)
    {
      // this is expected
    }
    finally
    {
      SessionFacade.closeSession(sessionId);
    }
  }


  /**
   * Creates several sessions for one user and creates multiple maps for each session.
   */
  @Test
  public void createMultipleSessionsAndMaps()
  {
    int numSessions = 10;
    int numMaps = 10;
    List<String> sessionIds = new LinkedList<String>();
    try
    {
      for (int i = 0; i < numSessions; i++)
      {
        String sessionId = SessionFacade.logIn(USER, PASS, new Locale[] { Locale.ENGLISH });
        sessionIds.add(sessionId);

        createMultipleMapsRequest(sessionId, numMaps);
      }
      
      // make sure there are numSessions * numMaps DashboardMaps
      QueryFactory f = new QueryFactory();
      SessionEntryQuery seq = new SessionEntryQuery(f);
      DashboardMapQuery dmq = new DashboardMapQuery(f);
      
      seq.WHERE(seq.getDashboardUser().EQ(user));
      dmq.WHERE(dmq.sessionEntry(seq));
      
      long total = numSessions*numMaps;
      Assert.assertEquals(dmq.getCount(), total);
    }
    finally
    {
      deleteWithoutSession();
    }
  }

  @Request(RequestType.SESSION)
  private void createMultipleMapsRequest(String sessionId, int numMaps)
  {
    for(int i=0; i<numMaps; i++)
    {
      SessionEntry.createMapForSession();
    }
  }
  
  /**
   * Because the first parameter isn't sessionId this will execute
   * as system, which is adequate for cleanup.
   */
  @Request
  private void deleteWithoutSession()
  {
    SessionEntry.deleteByUser(user);
    
    // Make sure there's no more entries or maps
    QueryFactory f = new QueryFactory();
    SessionEntryQuery seq = new SessionEntryQuery(f);
    DashboardMapQuery dmq = new DashboardMapQuery(f);
    
    seq.WHERE(seq.getDashboardUser().EQ(user));
    dmq.WHERE(dmq.sessionEntry(seq));
    
    Assert.assertEquals(dmq.getCount(), 0);
  }

  @Test
  public void deleteSessionEntryForUser()
  {

  }

  @Test
  public void deleteSessionEntryForSession()
  {

  }
}
