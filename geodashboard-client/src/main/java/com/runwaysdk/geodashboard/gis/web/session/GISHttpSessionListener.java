package com.runwaysdk.geodashboard.gis.web.session;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.ClientException;
import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.web.WebClientSession;

public class GISHttpSessionListener implements HttpSessionAttributeListener
{

  private static final Log log = LogFactory.getLog(GISHttpSessionListener.class);

  @Override
  public void attributeAdded(HttpSessionBindingEvent evt)
  {
    // TODO Auto-generated method stub
    
    
  }

  /**
   * Removes all SessionEntry objects with the associated session id stored in the HttpSession.
   */
  @Override
  public void attributeRemoved(HttpSessionBindingEvent evt)
  {
    if(ClientConstants.CLIENTSESSION.equals(evt.getName()))
    {
      WebClientSession wcs = (WebClientSession) evt.getValue();
      String sessionId =  wcs.getSessionId();

      String msg = "Removing SessionEntry objects for session id ["+sessionId+"] after unbinding attribute ["+evt.getName()+"].";
      log.debug(msg);
      
      try
      {
        LoaderDecorator.load("com.runwaysdk.geodashboard.SessionEntry").getMethod("deleteBySession", String.class).invoke(null, sessionId);
      }
      catch(Throwable t)
      {
        ClientException ex = new ClientException("Error in the following: "+msg, t);
        
        log.error(ex.getMessage(), t);
        
        throw ex;
      }    
      
    }
  }

  @Override
  public void attributeReplaced(HttpSessionBindingEvent evt)
  {
    // TODO Auto-generated method stub
    
  }

}
