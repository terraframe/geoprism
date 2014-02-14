package com.runwaysdk.web;

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
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.generation.loader.Reloadable;

public class SessionFilter implements Filter, Reloadable
{
  private FilterConfig filterConfig;

  public void init(FilterConfig filterConfig) throws ServletException
  {
    this.filterConfig = filterConfig;
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
    // Cookie cookie1[]= httpReq.getCookies();
    // if (cookie1 != null) {
    // for (int i=0; i<cookie1.length; i++) {
    // Cookie cookie = cookie1[i];
    // if (cookie != null && cookie.getName().equals("PrevLoadTime"))
    // {
    // filterConfig.getServletContext().log(cookie.getValue());
    // MdssLog.debug(cookie.getValue());
    // cookie.setValue("");
    // cookie.setMaxAge(-1);
    // httpRes.addCookie(cookie);
    // }
    // }
    // }

    HttpSession session = httpReq.getSession();

    WebClientSession clientSession = (WebClientSession) session.getAttribute(ClientConstants.CLIENTSESSION);

    // let some requests pass through
    if (pathAllowed(httpReq))
    {
      chain.doFilter(req, res);
      return;
    }
    else if (clientSession == null)
    {
      httpRes.sendRedirect(httpReq.getContextPath() + "/login");
    }
    else if (clientSession != null) {
      // Create a request object for this request
      ClientRequestIF clientRequest = clientSession.getRequest();

      if (clientRequest.isLoggedIn())
      {
        req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);
        chain.doFilter(req, res);
        return;
      }
      else {
        httpRes.sendRedirect(httpReq.getContextPath() + "/login");
      }
    }

    // check if the term or geo allpaths are dirty and need to rebuilt. The
    // system is unusable if this is true.

//    if (! ( AllPaths.containsValues() && dss.vector.solutions.ontology.AllPaths.containsValues() ))
//    {
//      filterConfig.getServletContext().getRequestDispatcher("/allpathsRebuild.jsp").forward(httpReq, httpRes);
//    }
//    else
//    {
//      // redirect to the login page because the user is not logged in
//      filterConfig.getServletContext().getRequestDispatcher("/login.jsp").forward(httpReq, httpRes);
//    }
  }

  private boolean pathAllowed(HttpServletRequest req)
  {
    String uri = req.getRequestURI();
    
    // They're allowed to hit the login view page, otherwise its a redirect loop
    if (uri.equals(req.getContextPath() + "/login")) {
      return true;
    }
    
    // They can also invoke the login action on SessionController @ session/login
    if (uri.equals(req.getContextPath() + "/session/login")) {
      return true;
    }
    
    // Allow style files for GIS maps
    if (uri.endsWith(".sld"))
    {
      return true;
    }

    if (uri.endsWith("reload.jsp"))
    {
      return true;
    }

    if (uri.endsWith("status.jsp"))
    {
      return true;
    }

    if (uri.endsWith("allpathsRebuild.jsp"))
    {
      return true;
    }
    
    // The login page requires some JS.
    if (uri.endsWith(".js")) {
      return true;
    }

    // Login/Logout requests
    if (uri.endsWith(SessionController.LOGIN_ACTION) || uri.endsWith(SessionController.LOGOUT_ACTION))
    {
      return true;
    }

    return false;
  }
}
