package net.geoprism.registry.lpg.jena;

import org.apache.jena.atlas.web.HttpException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdfconnection.RDFConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.geoprism.registry.lpg.adapter.HTTPConnector;

public class JenaConnector implements JenaConnectorIF
{
  private RDFConnection connection;

  Logger              logger = LoggerFactory.getLogger(HTTPConnector.class);

  String              serverurl;

  public JenaConnector(String url)
  {
    this.setServerUrl(url);
  }

  public String getServerUrl()
  {
    return serverurl;
  }

  public final void setServerUrl(String url)
  {
    if (url.endsWith("/"))
    {
      url = url.substring(0, url.length()-1);
    }

    this.serverurl = url;
  }
  
  protected void setConnector(RDFConnection conn)
  {
    this.connection = conn;
  }

  synchronized public void initialize()
  {
    this.setConnector(RDFConnection.connect(this.serverurl));
  }

  public boolean isInitialized()
  {
    return connection != null;
  }

//  private URI buildUri(String searchPath, NameValuePair... params) throws URISyntaxException
//  {
//    if (!searchPath.startsWith("/"))
//    {
//      searchPath = "/" + searchPath;
//    }
//
//    URIBuilder uriBuilder = new URIBuilder(this.getServerUrl());
//
//    List<String> pathSegsBefore = uriBuilder.getPathSegments();
//    List<String> additionalPathSegs = uriBuilder.setPath(searchPath).getPathSegments();
//    pathSegsBefore.addAll(additionalPathSegs);
//    uriBuilder.setPathSegments(pathSegsBefore);
//
//    if (params != null)
//    {
//      uriBuilder.setParameters(params);
//    }
//
//    return uriBuilder.build();
//  }
//
//  private JenaResponse convertResponse(CloseableHttpResponse response) throws InvalidLoginException, UnsupportedOperationException, IOException
//  {
//    int statusCode = response.getStatusLine().getStatusCode();
//
//    if (statusCode == 401 || ( statusCode == 302 && response.getFirstHeader("Location") != null && response.getFirstHeader("Location").getValue().contains("security/login") ))
//    {
//      throw new InvalidLoginException("Unable to log in to " + this.getServerUrl());
//    }
//
//    if (response.getEntity() != null)
//    {
//      try (InputStream is = response.getEntity().getContent())
//      {
//        String resp = IOUtils.toString(is, "UTF-8");
//
//        return new JenaResponse(resp, response.getStatusLine().getStatusCode());
//      }
//    }
//    else
//    {
//      return new JenaResponse(null, response.getStatusLine().getStatusCode());
//    }
//  }
  
  @Override
  public JenaResponse put(String graphName, String file)
  {
      if (!isInitialized())
      {
        initialize();
      }
      
      try
      {
        this.connection.put(graphName, file);
      }
      catch (HttpException e)
      {
        throw e; // TODO
      }
      
      return new JenaResponse(null, 200);
  }

  @Override
  public void close()
  {
    if (this.connection != null)
    {
      this.connection.close();
    }

  }
}
