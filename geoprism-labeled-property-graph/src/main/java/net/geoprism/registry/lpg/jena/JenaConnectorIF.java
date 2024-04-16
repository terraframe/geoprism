package net.geoprism.registry.lpg.jena;

import java.io.Closeable;

import org.apache.jena.rdf.model.Model;

public interface JenaConnectorIF extends Closeable
{
  public String getServerUrl();
  
  public JenaResponse put(String graphName, String file);

//  public JenaResponse httpGet(String string, NameValuePair... params) throws HTTPException, BadServerUriException;
//  
//  public JenaResponse httpPost(String string, HttpEntity body, NameValuePair... params) throws HTTPException, BadServerUriException;
//  
//  public JenaResponse httpPut(String string, HttpEntity body, NameValuePair... params) throws HTTPException, BadServerUriException;

  @Override
  public void close();
}
