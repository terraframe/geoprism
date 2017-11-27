package net.geoprism;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import com.runwaysdk.controller.RequestManager;
import com.runwaysdk.mvc.ResponseIF;
import com.runwaysdk.mvc.RestResponse;
import com.runwaysdk.request.ServletResponseIF;

public class CookieResponse extends RestResponse implements ResponseIF
{
  private String name;

  private int    maxAge;

  public CookieResponse(String name, int maxAge)
  {
    this.name = name;
    this.maxAge = maxAge;
  }

  @Override
  public void handle(RequestManager manager) throws ServletException, IOException
  {
    Object serialize = this.serialize();

    Cookie cookie = new Cookie(this.name, serialize.toString());
    cookie.setMaxAge(this.maxAge);
    cookie.setPath(manager.getReq().getContextPath());

    ServletResponseIF resp = manager.getResp();
    resp.addCookie(cookie);

    super.handle(manager);
  }

}
