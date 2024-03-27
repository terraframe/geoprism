package net.geoprism.registry.lpg.jena;

import org.apache.jena.rdf.model.Model;

import net.geoprism.registry.lpg.adapter.exception.BadServerUriException;
import net.geoprism.registry.lpg.adapter.exception.HTTPException;

/**
 * Resources:
 * - https://hub.docker.com/r/secoresearch/fuseki
 * - https://www.w3.org/TR/sparql11-http-rdf-update/#http-put
 */
public class JenaBridge
{
  public static final String  API_PATH = "labeled-property-graph-type";
  
  private JenaConnector connector;
  
  public JenaBridge(JenaConnector connector)
  {
    this.connector = connector;
  }

  public JenaResponse put(String graphName, Model data) throws HTTPException, BadServerUriException
  {
    return this.connector.put(graphName, data);
  }
}
