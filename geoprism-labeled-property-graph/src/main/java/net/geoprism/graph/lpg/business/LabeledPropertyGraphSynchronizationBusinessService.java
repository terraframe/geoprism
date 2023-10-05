/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.graph.lpg.business;

import java.util.LinkedList;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.AttributeLocal;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OrderBy.SortOrder;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableChar;
import com.runwaysdk.session.Session;

import net.geoprism.graph.LabeledPropertyGraphSynchronization;
import net.geoprism.graph.LabeledPropertyGraphSynchronizationQuery;
import net.geoprism.graph.LabeledPropertyGraphType;
import net.geoprism.graph.LabeledPropertyGraphTypeEntry;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.graph.LabeledPropertyGraphUtil;
import net.geoprism.graph.lpg.adapter.RegistryBridge;
import net.geoprism.graph.lpg.adapter.RegistryConnectorFactory;
import net.geoprism.graph.lpg.adapter.RegistryConnectorIF;
import net.geoprism.graph.lpg.service.AbstractGraphVersionPublisherService.State;
import net.geoprism.graph.lpg.service.JsonGraphVersionPublisherService;
import net.geoprism.registry.view.Page;

@Repository
public class LabeledPropertyGraphSynchronizationBusinessService implements LabeledPropertyGraphSynchronizationBusinessServiceIF
{
  @Autowired
  private LabeledPropertyGraphTypeBusinessServiceIF        typeService;

  @Autowired
  private LabeledPropertyGraphTypeEntryBusinessServiceIF   entryService;

  @Autowired
  private LabeledPropertyGraphTypeVersionBusinessServiceIF versionService;

  @Autowired
  private JsonGraphVersionPublisherService                 publisherService;

  @Override
  @Transaction
  public void delete(LabeledPropertyGraphSynchronization synchronization)
  {
    synchronization.delete();

    if (!StringUtils.isEmpty(synchronization.getVersionOid()))
    {
      this.versionService.remove(synchronization.getVersion());
    }

    if (!StringUtils.isEmpty(synchronization.getEntryOid()))
    {
      this.entryService.delete(synchronization.getEntry());
    }

    if (!StringUtils.isEmpty(synchronization.getGraphTypeOid()))
    {
      this.typeService.delete(synchronization.getGraphType());
    }
  }

  @Override
  public void execute(LabeledPropertyGraphSynchronization synchronization)
  {
    new LabeledPropertyGraphUtil(this).execute(synchronization);
  }

  @Override
  public void executeNoAuth(LabeledPropertyGraphSynchronization synchronization)
  {
    try
    {
      this.createTables(synchronization);
    }
    finally
    {
      // Refresh permissions in case new definitions were defined during the
      // synchronization process
      Session session = (Session) Session.getCurrentSession();

      if (session != null)
      {
        session.reloadPermissions();
      }
    }

    LabeledPropertyGraphTypeVersion version = synchronization.getVersion();
    this.versionService.truncate(version);

    State state = this.publisherService.createState(synchronization, version);

    try (RegistryConnectorIF connector = RegistryConnectorFactory.getConnector(synchronization.getUrl()))
    {
      RegistryBridge bridge = new RegistryBridge(connector);

      JsonArray results = new JsonArray();

      // Should synchronization number be configurable to increase speed on
      // larger systems
      final int BLOCK_SIZE = 50;
      long skip = 0;

      // Get all of the geoObjects
      do
      {
        results = bridge.getGeoObjects(synchronization.getRemoteVersion(), skip, BLOCK_SIZE).getJsonArray();

        this.publisherService.publishGeoObjects(state, results);

        skip += BLOCK_SIZE;
      } while (results.size() > 0);

      // Get all of the edges
      skip = 0;
      do
      {
        results = bridge.getEdges(synchronization.getRemoteVersion(), skip, BLOCK_SIZE).getJsonArray();

        this.publisherService.publishEdges(state, results);

        skip += BLOCK_SIZE;
      } while (results.size() > 0);

    }
  }

  @Transaction
  private void createTables(LabeledPropertyGraphSynchronization synchronization)
  {
    try (RegistryConnectorIF connector = RegistryConnectorFactory.getConnector(synchronization.getUrl()))
    {
      RegistryBridge bridge = new RegistryBridge(connector);

      JsonObject typeObject = bridge.getType(synchronization.getRemoteType()).getJsonObject();
      typeObject.remove(LabeledPropertyGraphSynchronization.OID);

      JsonObject entryObject = bridge.getEntry(synchronization.getRemoteEntry()).getJsonObject();
      entryObject.remove(LabeledPropertyGraphSynchronization.OID);

      JsonObject versionObject = bridge.getVersion(synchronization.getRemoteVersion()).getJsonObject();
      versionObject.remove(LabeledPropertyGraphSynchronization.OID);

      synchronization.appLock();

      if (StringUtils.isEmpty(synchronization.getGraphTypeOid()))
      {
        LabeledPropertyGraphType type = this.typeService.apply(typeObject, false);

        synchronization.setGraphType(type);
      }

      if (StringUtils.isEmpty(synchronization.getEntryOid()))
      {
        LabeledPropertyGraphTypeEntry entry = this.entryService.create(synchronization.getGraphType(), entryObject);

        synchronization.setEntry(entry);
      }

      if (StringUtils.isEmpty(synchronization.getVersionOid()))
      {
        LabeledPropertyGraphTypeVersion version = this.versionService.create(synchronization.getEntry(), versionObject);

        synchronization.setVersion(version);

      }

      synchronization.apply();
    }
  }

  // @Transaction
  @Override
  public void updateRemoteVersion(LabeledPropertyGraphSynchronization synchronization, String versionId, Integer versionNumber)
  {
    if (!synchronization.getRemoteVersion().equals(versionId))
    {
      LabeledPropertyGraphTypeVersion version = synchronization.getVersion();

      // Due to memory constraints in the orientdb database we need to truncate
      // the graph first in its own transaction
      if (version != null)
      {
        this.versionService.truncate(version);
      }

      updateRemoteVersion(synchronization, versionId, versionNumber, version);
    }
  }

  @Transaction
  private void updateRemoteVersion(LabeledPropertyGraphSynchronization synchronization, String versionId, Integer versionNumber, LabeledPropertyGraphTypeVersion version)
  {
    synchronization.appLock();

    try
    {

      synchronization.setVersion(null);
      synchronization.setRemoteVersion(versionId);
      synchronization.setVersionNumber(versionNumber);
      synchronization.apply();

      if (version != null)
      {
        this.versionService.remove(version);
      }
    }
    finally
    {
      synchronization.unlock();
    }
  }

  @Override
  public VertexObject getObject(LabeledPropertyGraphSynchronization synchronization, String uid)
  {
    return this.versionService.getObject(synchronization.getVersion(), uid);
  }

  @Override
  public LabeledPropertyGraphSynchronization fromJSON(JsonObject object)
  {
    LabeledPropertyGraphSynchronization list = null;

    if (object.has("oid") && !object.get("oid").isJsonNull())
    {
      String oid = object.get("oid").getAsString();

      list = LabeledPropertyGraphSynchronization.lock(oid);
    }
    else
    {
      list = new LabeledPropertyGraphSynchronization();
    }

    list.parse(object);

    return list;
  }

  @Override
  @Transaction
  public LabeledPropertyGraphSynchronization apply(JsonObject object)
  {
    LabeledPropertyGraphSynchronization list = this.fromJSON(object);
    list.apply();

    return list;
  }

  @Override
  public JsonArray getAll()
  {
    LabeledPropertyGraphSynchronizationQuery query = new LabeledPropertyGraphSynchronizationQuery(new QueryFactory());
    query.ORDER_BY_DESC(query.getRemoteType());

    JsonArray array = new JsonArray();

    try (OIterator<? extends LabeledPropertyGraphSynchronization> iterator = query.getIterator())
    {
      iterator.forEach(i -> array.add(i.toJSON()));
    }

    return array;
  }

  @Override
  public Page<LabeledPropertyGraphSynchronization> page(JsonObject criteria)
  {
    LabeledPropertyGraphSynchronizationQuery query = new LabeledPropertyGraphSynchronizationQuery(new QueryFactory());
    int pageSize = 10;
    int pageNumber = 1;

    if (criteria.has("first") && criteria.has("rows"))
    {
      int first = criteria.get("first").getAsInt();
      pageSize = criteria.get("rows").getAsInt();
      pageNumber = ( first / pageSize ) + 1;

      query.restrictRows(pageSize, pageNumber);
    }

    if (criteria.has("sortField") && criteria.has("sortOrder"))
    {
      String field = criteria.get("sortField").getAsString();
      SortOrder order = criteria.get("sortOrder").getAsInt() == 1 ? SortOrder.ASC : SortOrder.DESC;

      query.ORDER_BY(query.getS(field), order);
    }
    else if (criteria.has("multiSortMeta"))
    {
      JsonArray sorts = criteria.get("multiSortMeta").getAsJsonArray();

      for (int i = 0; i < sorts.size(); i++)
      {
        JsonObject sort = sorts.get(i).getAsJsonObject();

        String field = sort.get("field").getAsString();
        SortOrder order = sort.get("order").getAsInt() == 1 ? SortOrder.ASC : SortOrder.DESC;

        query.ORDER_BY(query.getS(field), order);
      }
    }

    if (criteria.has("filters"))
    {
      JsonObject filters = criteria.get("filters").getAsJsonObject();
      Set<String> keys = filters.keySet();

      for (String attributeName : keys)
      {
        Selectable attribute = query.get(attributeName);

        if (attribute != null)
        {
          JsonObject filter = filters.get(attributeName).getAsJsonObject();

          String value = filter.get("value").getAsString();
          String mode = filter.get("matchMode").getAsString();

          if (mode.equals("contains"))
          {
            if (attribute instanceof AttributeLocal)
            {
              query.WHERE( ( (AttributeLocal) attribute ).localize().LIKEi("%" + value + "%"));
            }
            else
            {
              SelectableChar selectable = (SelectableChar) attribute;
              query.WHERE(selectable.LIKEi("%" + value + "%"));
            }
          }
          else if (mode.equals("equals"))
          {
            if (attribute instanceof AttributeLocal)
            {
              query.WHERE( ( (AttributeLocal) attribute ).localize().EQ(value));
            }
            else
            {
              query.WHERE(attribute.EQ(value));
            }
          }
        }
      }
    }

    long count = query.getCount();

    try (OIterator<? extends LabeledPropertyGraphSynchronization> iterator = query.getIterator())
    {
      return new Page<LabeledPropertyGraphSynchronization>(count, pageNumber, pageSize, new LinkedList<LabeledPropertyGraphSynchronization>(iterator.getAll()));
    }
  }

  @Override
  public LabeledPropertyGraphSynchronization get(String oid)
  {
    return LabeledPropertyGraphSynchronization.get(oid);
  }

}
