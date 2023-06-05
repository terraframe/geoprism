package net.geoprism.graph.adapter;

public class HTTPConnectorBuilder implements RegistryConnectorBuilderIF
{
  @Override
  public RegistryConnectorIF build(String url)
  {
    return new HTTPConnector(url);
  }

}
