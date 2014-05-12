package com.runwaysdk.gis.web.session;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.ClientSession;
import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Request;

public class GISHttpSessionListener implements HttpSessionListener
{

  private static final Log log = LogFactory.getLog(GISHttpSessionListener.class);
  
  @Override
  public void sessionCreated(HttpSessionEvent evt)
  {
    
  }

  @Override
  @Request
  public void sessionDestroyed(HttpSessionEvent evt)
  {
    ClientSession session = (ClientSession) evt.getSession().getAttribute(ClientConstants.CLIENTSESSION);
    String sessionId = session.getSessionId();
    
    try
    {
      LoaderDecorator.load("com.runwaysdk.geodashboard.SessionEntry").getMethod("deleteBySession", String.class).invoke(null, sessionId);
    }
    catch(Throwable t)
    {
      log.error("Unable to call SessionEntry.deleteBySession()", t);
      throw new RuntimeException(t);
    }
  }

}
