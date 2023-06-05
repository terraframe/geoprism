package net.geoprism.graph.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.session.InvalidLoginException;

import net.geoprism.graph.adapter.exception.BadServerUriException;
import net.geoprism.graph.adapter.exception.HTTPException;
import net.geoprism.graph.adapter.response.RegistryResponse;

public class HTTPConnector implements RegistryConnectorIF
{
  CloseableHttpClient client;

  Logger              logger = LoggerFactory.getLogger(HTTPConnector.class);

  String              serverurl;

  public HTTPConnector(String url)
  {
    this.setServerUrl(url);
  }

  public String getServerUrl()
  {
    return serverurl;
  }

  public final void setServerUrl(String url)
  {
    if (!url.endsWith("/"))
    {
      url = url + "/";
    }

    this.serverurl = url;
  }

  synchronized public void initialize()
  {
    this.client = HttpClients.createDefault(); // TODO : Thread safety ?
  }

  public boolean isInitialized()
  {
    return client != null;
  }

  private URI buildUri(String searchPath, NameValuePair... params) throws URISyntaxException
  {
    if (!searchPath.startsWith("/"))
    {
      searchPath = "/" + searchPath;
    }

    URIBuilder uriBuilder = new URIBuilder(this.getServerUrl());

    List<String> pathSegsBefore = uriBuilder.getPathSegments();
    List<String> additionalPathSegs = uriBuilder.setPath(searchPath).getPathSegments();
    pathSegsBefore.addAll(additionalPathSegs);
    uriBuilder.setPathSegments(pathSegsBefore);

    if (params != null)
    {
      uriBuilder.setParameters(params);
    }

    return uriBuilder.build();
  }

  private RegistryResponse convertResponse(CloseableHttpResponse response) throws InvalidLoginException, UnsupportedOperationException, IOException
  {
    int statusCode = response.getStatusLine().getStatusCode();

    if (statusCode == 401 || ( statusCode == 302 && response.getFirstHeader("Location") != null && response.getFirstHeader("Location").getValue().contains("security/login") ))
    {
      throw new InvalidLoginException("Unable to log in to " + this.getServerUrl());
    }

    if (response.getEntity() != null)
    {
      try (InputStream is = response.getEntity().getContent())
      {
        String resp = IOUtils.toString(is, "UTF-8");

        return new RegistryResponse(resp, response.getStatusLine().getStatusCode());
      }
    }
    else
    {
      return new RegistryResponse(null, response.getStatusLine().getStatusCode());
    }
  }

  public RegistryResponse httpGet(String url, NameValuePair... params) throws InvalidLoginException, HTTPException, BadServerUriException
  {
    try
    {
      if (!isInitialized())
      {
        initialize();
      }

      HttpGet get = new HttpGet(this.buildUri(url, params));

      get.addHeader("Accept", "application/json");

      return this.httpRequest(get);
    }
    catch (URISyntaxException | IOException e)
    {
      throw new HTTPException(e);
    }
  }

  public RegistryResponse httpPost(String url, HttpEntity body, NameValuePair... params) throws InvalidLoginException, HTTPException, BadServerUriException
  {
    try
    {
      if (!isInitialized())
      {
        initialize();
      }

      HttpPost post = new HttpPost(this.buildUri(url, params));

      post.addHeader("Content-Type", "application/json");

      post.setEntity(body);

      return this.httpRequest(post);
    }
    catch (IOException | URISyntaxException e)
    {
      throw new HTTPException(e);
    }
  }

  public RegistryResponse httpRequest(HttpRequestBase method) throws InvalidLoginException, ClientProtocolException, IOException, URISyntaxException, BadServerUriException
  {
    this.logger.debug("Sending request to " + method.getURI());

    // Execute the method.
    try (CloseableHttpResponse response = client.execute(method))
    {
      int statusCode = response.getStatusLine().getStatusCode();

      // Follow Redirects
      if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_TEMPORARY_REDIRECT || statusCode == HttpStatus.SC_SEE_OTHER)
      {
        String location = response.getFirstHeader("Location") != null ? response.getFirstHeader("Location").getValue() : "";

        if (location.contains("security/login"))
        {
          throw new InvalidLoginException("Unable to log in to " + this.getServerUrl());
        }
        else if (!"".equals(location))
        {
          this.logger.debug("Redirected [" + statusCode + "] to [" + location + "].");

          method.setURI(new URI(location));
          method.releaseConnection();
          return httpRequest(method);
        }
      }

      return this.convertResponse(response);
    }
  }

  @Override
  public void close()
  {
    if (this.client != null)
    {
      try
      {
        this.client.close();
      }
      catch (IOException e)
      {
        throw new HTTPException(e);
      }
    }

  }
}