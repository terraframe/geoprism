package com.runwaysdk.geodashboard.gis;

import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.GeodashboardUser;
import com.runwaysdk.geodashboard.SessionEntry;
import com.runwaysdk.geodashboard.SessionEntryQuery;
import com.runwaysdk.geodashboard.gis.persist.DashboardMap;
import com.runwaysdk.geodashboard.gis.persist.DashboardMapQuery;
import com.runwaysdk.geodashboard.gis.persist.SessionMapQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.SessionFacade;

public class SessionMapTest
{
  private static final String USER = "TestUser";
  
  private static final String PASS = "TestPass";
  
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
    user.apply();
  }
  
  @AfterClass
  @Request
  @Transaction
  public static void classTeardown()
  {
    user.delete();
  }
  
  @Test
  public void createMapForSession()
  {
    String sessionId = SessionFacade.logIn(USER, PASS, new Locale[]{Locale.ENGLISH});
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
      if(iter.hasNext())
      {
        SessionEntry entry = iter.next();
        Assert.assertEquals(entry.getSessionId(), sessionId);
        Assert.assertEquals(entry.getDashboardUser(), user);
        
        if(iter.hasNext())
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
      
      Assert.fail("Map ["+mapId+"] was not deleted.");
    }
    catch(DataNotFoundException e)
    {
      // this is expected
    }
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
