package net.geoprism;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import com.runwaysdk.controller.RequestManager;
import com.runwaysdk.mvc.ResponseIF;
import com.runwaysdk.request.ServletResponseIF;

public class LogoutResponse implements ResponseIF
{
  private String location;

  private String name;

  private int    maxAge;

  public LogoutResponse(String location, String name, int maxAge)
  {
    this.location = location;
    this.name = name;
    this.maxAge = maxAge;
  }

  @Override
  public void handle(RequestManager manager) throws ServletException, IOException
  {
    Cookie cookie = new Cookie(this.name, "");
    cookie.setMaxAge(this.maxAge);
    cookie.setPath(manager.getReq().getContextPath());

    ServletResponseIF resp = manager.getResp();
    resp.addCookie(cookie);
    resp.sendRedirect(this.location);
  }

}
