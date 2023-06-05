package net.geoprism.graph.adapter;

public class RegistryConnectorFactory
{
  private static RegistryConnectorBuilderIF builder = new HTTPConnectorBuilder();

  public static void setBuilder(RegistryConnectorBuilderIF builder)
  {
    RegistryConnectorFactory.builder = builder;
  }

  public static RegistryConnectorIF getConnector(String url)
  {
    return builder.build(url);
  }

}
