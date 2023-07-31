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