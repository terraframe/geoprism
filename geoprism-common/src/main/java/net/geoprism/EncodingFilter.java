/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class EncodingFilter implements Filter
{

  public static final String ENCODING = "UTF-8";
  
  @Override
  public void init(FilterConfig config) throws ServletException
  {
    // Do nothing
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException
  {
    try
    {
      request.setCharacterEncoding(ENCODING);
      response.setCharacterEncoding(ENCODING);

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
