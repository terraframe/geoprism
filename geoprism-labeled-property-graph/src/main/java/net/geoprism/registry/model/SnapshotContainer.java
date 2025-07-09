package net.geoprism.registry.model;

import com.runwaysdk.business.Relationship;

import net.geoprism.graph.MetadataSnapshot;

public interface SnapshotContainer<T extends Relationship>
{
  T addSnapshot(MetadataSnapshot snapshot);

  boolean createTablesWithSnapshot();
}
