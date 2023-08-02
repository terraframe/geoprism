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
package net.geoprism.session;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.runwaysdk.session.Request;

@Service
public class LoginBruteForceGuardService
{
  public static final int MAX_ATTEMPT = 10;
  private LoadingCache<String, Integer> attemptsCache;

  @Autowired
  private HttpServletRequest servletReq;

  public LoginBruteForceGuardService() {
      super();
      attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
          @Override
          public Integer load(final String key) {
              return 0;
          }
      });
  }

  public void loginFailed(final String key) {
      int attempts;
      try {
          attempts = attemptsCache.get(key);
      } catch (final ExecutionException e) {
          attempts = 0;
      }
      attempts++;
      attemptsCache.put(key, attempts);
  }

  public boolean isBlocked() {
      try {
          return attemptsCache.get(getClientIP()) >= MAX_ATTEMPT;
      } catch (final ExecutionException e) {
          return false;
      }
  }
  
  @Request
  public void guardLogin()
  {
    if (isBlocked())
    {
      throw new LoginBlockedException();
    }
  }

  private String getClientIP() {
      final String xfHeader = servletReq.getHeader("X-Forwarded-For");
      if (xfHeader != null) {
          return xfHeader.split(",")[0];
      }
      return servletReq.getRemoteAddr();
  }
}