package net.geoprism.graph;

import net.geoprism.graph.LabeledPropertyGraphTypeVersion;

public interface StrategyPublisher
{
  void publish(LabeledPropertyGraphTypeVersion version);
}
