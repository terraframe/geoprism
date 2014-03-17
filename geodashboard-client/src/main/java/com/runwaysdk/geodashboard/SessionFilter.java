package com.runwaysdk.geodashboard;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.session.InvalidSessionExceptionDTO;
import com.runwaysdk.web.WebClientSession;
import com.runwaysdk.web.controller.SessionController;

public class SessionFilter implements Filter, Reloadable
{
  public void init(FilterConfig filterConfig) throws ServletException
  {
  }

  public void destroy()
  {
  }

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException
  {
    HttpServletRequest httpReq = (HttpServletRequest) req;
    HttpServletResponse httpRes = (HttpServletResponse) res;

    // response time logging
    req.setAttribute("startTime", (Long) ( new Date().getTime() ));

    HttpSession session = httpReq.getSession();

    WebClientSession clientSession = (WebClientSession) session.getAttribute(ClientConstants.CLIENTSESSION);

    // This isLoggedIn check is not 100% sufficient, it doesn't go to the server
    // and check, it only does it locally, so if the session has expired it'l
    // let it through.
    if (clientSession != null && clientSession.getRequest().isLoggedIn())
    {
      String uri = httpReq.getRequestURI();

      // They're already logged in, but they're trying to login again? Redirect
      // to the index.
      if (uri.equals(httpReq.getContextPath() + "/login") || uri.equals(httpReq.getContextPath() + "/session/login"))
      {
        httpRes.sendRedirect(httpReq.getContextPath());
        return;
      }

      try
      {
        req.setAttribute(ClientConstants.CLIENTREQUEST, clientSession.getRequest());
        chain.doFilter(req, res);
      }
      catch (InvalidSessionExceptionDTO e)
      {
        httpRes.sendRedirect(httpReq.getContextPath() + "/login");
      }

      return;
    }
    else if (pathAllowed(httpReq))
    {
      chain.doFilter(req, res);
      return;
    }
    else
    {
      httpRes.sendRedirect(httpReq.getContextPath() + "/login");
    }
  }

  private boolean pathAllowed(HttpServletRequest req)
  {
    String uri = req.getRequestURI();

    // They're allowed to hit the login view page, otherwise its a redirect loop
    if (uri.equals(req.getContextPath() + "/login"))
    {
      return true;
    }

    // Allow direct hitting of all page resources in login directories.
    if (uri.startsWith(req.getContextPath() + "/com/runwaysdk/geodashboard/login"))
    {
      return true;
    }

    // They can also invoke the login action on SessionController @
    // session/login
    if (uri.equals(req.getContextPath() + "/session/login"))
    {
      return true;
    }

    // Allow style files for GIS maps
    if (uri.endsWith(".sld"))
    {
      return true;
    }

    // Login/Logout requests for mojax/mojo extensions.
    if (uri.endsWith(SessionController.LOGIN_ACTION) || uri.endsWith(SessionController.LOGOUT_ACTION))
    {
      return true;
    }

    return false;
  }
}
