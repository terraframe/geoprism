package com.runwaysdk.geodashboard;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class EncodingFilter implements Filter
{

  @Override
  public void init(FilterConfig config) throws ServletException
  {
    // Do nothing
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException
  {
    try
    {
      request.setCharacterEncoding(EncodingConstants.ENCODING);
      response.setCharacterEncoding(EncodingConstants.ENCODING);

      chain.doFilter(request, response);
    }
    catch (UnsupportedEncodingException e)
    {
      throw new ServletException(e);
    }
    catch (IOException e)
    {
      throw new ServletException(e);
    }
  }

  @Override
  public void destroy()
  {
    // Do nothing
  }
}
