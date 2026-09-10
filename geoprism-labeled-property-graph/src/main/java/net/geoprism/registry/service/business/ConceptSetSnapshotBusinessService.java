/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service.business;

import java.util.LinkedList;
import java.util.List;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.graph.ConceptClassSnapshot;
import net.geoprism.graph.ConceptEdgeTypeSnapshot;
import net.geoprism.graph.ConceptSetSnapshot;
import net.geoprism.graph.ConceptSetSnapshotHasClass;
import net.geoprism.graph.ConceptSetSnapshotHasEdge;
import net.geoprism.graph.ConceptSetSnapshotQuery;
import net.geoprism.graph.LabeledPropertyGraphTypeSnapshotQuery;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.SnapshotContainer;
import net.geoprism.registry.view.ConceptSetDTO;
import net.geoprism.registry.view.DiscreteType;

@Service
public class ConceptSetSnapshotBusinessService implements ConceptSetSnapshotBusinessServiceIF
{
  @Autowired
  private ConceptClassSnapshotBusinessServiceIF    cClassService;

  @Autowired
  private ConceptEdgeTypeSnapshotBusinessServiceIF cEdgeService;

  @Override
  @Transaction
  public void delete(ConceptSetSnapshot snapshot)
  {
    snapshot.delete();
  }

  @Override
  public ConceptSetSnapshot create(SnapshotContainer<?> version, ConceptSetDTO dto)
  {
    String origin = dto.hasOrigin() ? dto.getOrigin() : GeoprismProperties.getOrigin();
    Long sequence = dto.hasSequence() ? dto.getSequence() : 0;
    LocalizedValue label = dto.getDisplayLabel();
    LocalizedValue description = dto.getDescription();
    String discreteType = dto.getDiscreteType().name();
    String rootTerm = dto.getRootTerm();

    ConceptSetSnapshot snapshot = new ConceptSetSnapshot();
    snapshot.setCode(dto.getCode());
    snapshot.setOrigin(origin);
    snapshot.setSequence(sequence);
    snapshot.setDiscreteType(discreteType);
    LocalizedValueConverter.populate(snapshot.getDisplayLabel(), label);
    LocalizedValueConverter.populate(snapshot.getDescription(), description);
    snapshot.setRootTerm(rootTerm);
    snapshot.apply();

    version.addSnapshot(snapshot).apply();

    dto.getConceptClasses().forEach(code -> {
      ConceptClassSnapshot cClass = this.cClassService.get(version, code);

      snapshot.addClassSnapshot(cClass).apply();
    });

    dto.getConceptEdgeTypes().forEach(code -> {
      ConceptEdgeTypeSnapshot cClass = this.cEdgeService.get(version, code);

      snapshot.addEdgeSnapshot(cClass).apply();
    });

    return snapshot;
  }

  @Override
  public ConceptSetSnapshot get(SnapshotContainer<?> version, String code)
  {
    QueryFactory factory = new QueryFactory();

    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ((LabeledPropertyGraphTypeVersion) version));

    ConceptSetSnapshotQuery query = new ConceptSetSnapshotQuery(factory);
    query.WHERE(query.EQ(vQuery.getChild()));
    query.AND(query.getCode().EQ(code));

    try (OIterator<? extends ConceptSetSnapshot> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
  }

  protected void assignPermissions(ComponentIF component)
  {
  }

  @Override
  public ConceptSetDTO toDTO(ConceptSetSnapshot snapshot)
  {
    ConceptSetDTO dto = new ConceptSetDTO();
    dto.setCode(snapshot.getCode());
    dto.setOrigin(snapshot.getOrigin());
    dto.setSequence(snapshot.getSequence());
    dto.setDisplayLabel(LocalizedValueConverter.convertNoAutoCoalesce(snapshot.getDisplayLabel()));
    dto.setDescription(LocalizedValueConverter.convertNoAutoCoalesce(snapshot.getDescription()));
    dto.setRootTerm(snapshot.getRootTerm());
    dto.setDiscreteType(DiscreteType.valueOf(snapshot.getDiscreteType()));
    dto.getConceptClasses().addAll(this.getConceptClasses(snapshot).stream().map(c -> c.getCode()).toList());
    dto.getConceptEdgeTypes().addAll(this.getConceptEdgeTypes(snapshot).stream().map(c -> c.getCode()).toList());

    return dto;
  }

  public LabeledPropertyGraphTypeVersion getVersion(ConceptSetSnapshot snapshot)
  {
    try (OIterator<? extends LabeledPropertyGraphTypeVersion> iterator = snapshot.getAllVersion())
    {
      if (iterator.hasNext())
      {
        return iterator.next();
      }

    }
    return null;
  }

  @Override
  public ConceptSetSnapshotHasClass addConceptClass(ConceptSetSnapshot set, ConceptClassSnapshot conceptClass)
  {
    ConceptSetSnapshotHasClass edge = set.addClassSnapshot(conceptClass);
    edge.apply();

    return edge;
  }

  @Override
  public ConceptSetSnapshotHasEdge addConceptEdgeType(ConceptSetSnapshot set, ConceptEdgeTypeSnapshot conceptEdgeType)
  {
    ConceptSetSnapshotHasEdge edge = set.addEdgeSnapshot(conceptEdgeType);
    edge.apply();

    return edge;
  }

  @Override
  public List<ConceptClassSnapshot> getConceptClasses(ConceptSetSnapshot set)
  {
    try (OIterator<? extends ConceptClassSnapshot> it = set.getAllClassSnapshot())
    {
      return new LinkedList<>(it.getAll());
    }
  }

  @Override
  public List<ConceptEdgeTypeSnapshot> getConceptEdgeTypes(ConceptSetSnapshot set)
  {
    try (OIterator<? extends ConceptEdgeTypeSnapshot> it = set.getAllEdgeSnapshot())
    {
      return new LinkedList<>(it.getAll());
    }
  }
}
