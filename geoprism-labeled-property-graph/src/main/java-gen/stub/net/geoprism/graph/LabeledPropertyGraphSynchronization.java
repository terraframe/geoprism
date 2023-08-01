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
package net.geoprism.graph;

import java.util.LinkedList;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.AttributeLocal;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OrderBy.SortOrder;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableChar;
import com.runwaysdk.session.Session;

import net.geoprism.graph.adapter.RegistryBridge;
import net.geoprism.graph.adapter.RegistryConnectorFactory;
import net.geoprism.graph.adapter.RegistryConnectorIF;
import net.geoprism.graph.service.LabeledPropertyGraphServiceIF;
import net.geoprism.registry.DateUtil;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.view.JsonSerializable;
import net.geoprism.registry.view.Page;

public class LabeledPropertyGraphSynchronization extends LabeledPropertyGraphSynchronizationBase implements JsonSerializable
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 63302423;

  public LabeledPropertyGraphSynchronization()
  {
    super();
  }

  @Override
  public void delete()
  {
    super.delete();

    if (!StringUtils.isEmpty(this.getVersionOid()))
    {
      this.getVersion().remove();
    }

    if (!StringUtils.isEmpty(this.getEntryOid()))
    {
      this.getEntry().delete();
    }

    if (!StringUtils.isEmpty(this.getGraphTypeOid()))
    {
      this.getGraphType().delete();
    }
  }

  @Override
  @Authenticate
  public void execute()
  {
    try
    {
      this.createTables();
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

    LabeledPropertyGraphTypeVersion version = this.getVersion();
    version.truncate();

    LabeledPropertyGraphServiceIF.getInstance().postTruncate(this);

    JsonGraphVersionPublisher publisher = new JsonGraphVersionPublisher(this, version);

    try (RegistryConnectorIF connector = RegistryConnectorFactory.getConnector(this.getUrl()))
    {
      RegistryBridge bridge = new RegistryBridge(connector);

      JsonArray results = new JsonArray();

      // Should this number be configurable to increase speed on larger systems
      final int BLOCK_SIZE = 50;
      long skip = 0;

      // Get all of the geoObjects
      do
      {
        results = bridge.getGeoObjects(this.getRemoteVersion(), skip, BLOCK_SIZE).getJsonArray();

        publisher.publishGeoObjects(results);

        skip += BLOCK_SIZE;
      } while (results.size() > 0);

      // Get all of the edges
      skip = 0;
      do
      {
        results = bridge.getEdges(this.getRemoteVersion(), skip, BLOCK_SIZE).getJsonArray();

        publisher.publishEdges(results);

        skip += BLOCK_SIZE;
      } while (results.size() > 0);

    }

    LabeledPropertyGraphServiceIF.getInstance().postSynchronization(this, publisher.getCache());
  }

  @Transaction
  private void createTables()
  {
    try (RegistryConnectorIF connector = RegistryConnectorFactory.getConnector(this.getUrl()))
    {
      RegistryBridge bridge = new RegistryBridge(connector);

      JsonObject typeObject = bridge.getType(this.getRemoteType()).getJsonObject();
      typeObject.remove(OID);

      JsonObject entryObject = bridge.getEntry(this.getRemoteEntry()).getJsonObject();
      entryObject.remove(OID);

      JsonObject versionObject = bridge.getVersion(this.getRemoteVersion()).getJsonObject();
      versionObject.remove(OID);

      this.appLock();

      if (StringUtils.isEmpty(this.getGraphTypeOid()))
      {
        LabeledPropertyGraphType type = LabeledPropertyGraphType.apply(typeObject, false);

        this.setGraphType(type);
      }

      if (StringUtils.isEmpty(this.getEntryOid()))
      {
        LabeledPropertyGraphTypeEntry entry = LabeledPropertyGraphTypeEntry.create(this.getGraphType(), entryObject);

        this.setEntry(entry);
      }

      if (StringUtils.isEmpty(this.getVersionOid()))
      {
        LabeledPropertyGraphTypeVersion version = LabeledPropertyGraphTypeVersion.create(this.getEntry(), versionObject);

        this.setVersion(version);

      }

      this.apply();
    }
  }

  // @Transaction
  public void updateRemoteVersion(String versionId, Integer versionNumber)
  {
    if (!this.getRemoteVersion().equals(versionId))
    {
      LabeledPropertyGraphTypeVersion version = this.getVersion();

      // Due to memory constraints in the orientdb database we need to truncate
      // the graph first
      // in its own transaction
      if (version != null)
      {
        version.truncate();

        LabeledPropertyGraphServiceIF.getInstance().postTruncate(this);
      }

      updateRemoteVersion(versionId, versionNumber, version);
    }
  }

  @Transaction
  private void updateRemoteVersion(String versionId, Integer versionNumber, LabeledPropertyGraphTypeVersion version)
  {
    this.appLock();

    try
    {

      this.setVersion(null);
      this.setRemoteVersion(versionId);
      this.setVersionNumber(versionNumber);
      this.apply();

      if (version != null)
      {
        version.remove();
      }
    }
    finally
    {
      this.unlock();
    }
  }

  protected void parse(JsonObject object)
  {
    this.setUrl(object.get(LabeledPropertyGraphSynchronization.URL).getAsString());
    this.setRemoteType(object.get(LabeledPropertyGraphSynchronization.REMOTETYPE).getAsString());
    LocalizedValueConverter.populate(this.getDisplayLabel(), LocalizedValue.fromJSON(object.get(LabeledPropertyGraphType.DISPLAYLABEL).getAsJsonObject()));
    this.setRemoteEntry(object.get(LabeledPropertyGraphSynchronization.REMOTEENTRY).getAsString());
    this.setForDate(DateUtil.parseDate(object.get(LabeledPropertyGraphSynchronization.FORDATE).getAsString()));
    this.setRemoteVersion(object.get(LabeledPropertyGraphSynchronization.REMOTEVERSION).getAsString());
    this.setVersionNumber(object.get(LabeledPropertyGraphSynchronization.VERSIONNUMBER).getAsInt());
  }

  public final JsonObject toJSON()
  {
    JsonObject object = new JsonObject();

    if (this.isAppliedToDB())
    {
      object.addProperty(LabeledPropertyGraphSynchronization.OID, this.getOid());
    }

    object.addProperty(LabeledPropertyGraphSynchronization.URL, this.getUrl());
    object.addProperty(LabeledPropertyGraphSynchronization.REMOTETYPE, this.getRemoteType());
    object.add(LabeledPropertyGraphType.DISPLAYLABEL, LocalizedValueConverter.convertNoAutoCoalesce(this.getDisplayLabel()).toJSON());
    object.addProperty(LabeledPropertyGraphSynchronization.REMOTEENTRY, this.getRemoteEntry());
    object.addProperty(LabeledPropertyGraphSynchronization.FORDATE, DateUtil.formatDate(this.getForDate(), false));
    object.addProperty(LabeledPropertyGraphSynchronization.REMOTEVERSION, this.getRemoteVersion());
    object.addProperty(LabeledPropertyGraphSynchronization.VERSIONNUMBER, this.getVersionNumber());

    return object;
  }

  public VertexObject getObject(String uid)
  {
    return this.getVersion().getObject(uid);
  }

  public static LabeledPropertyGraphSynchronization fromJSON(JsonObject object)
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

  @Transaction
  public static LabeledPropertyGraphSynchronization apply(JsonObject object)
  {
    LabeledPropertyGraphSynchronization list = LabeledPropertyGraphSynchronization.fromJSON(object);
    list.apply();

    return list;
  }

  public static JsonArray getAll()
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

  public static Page<LabeledPropertyGraphSynchronization> page(JsonObject criteria)
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

}
