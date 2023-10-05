package net.geoprism.graph.lpg.business;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;

import net.geoprism.graph.LabeledPropertyGraphType;
import net.geoprism.graph.LabeledPropertyGraphTypeEntry;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.graph.LabeledPropertyGraphTypeVersionQuery;
import net.geoprism.graph.LabeledPropertyGraphUtil;
import net.geoprism.registry.DateUtil;

@Repository
public class LabeledPropertyGraphTypeEntryBusinessService implements LabeledPropertyGraphTypeEntryBusinessServiceIF
{
  @Autowired
  private LabeledPropertyGraphTypeVersionBusinessServiceIF versionService;

  @Override
  @Transaction
  public void delete(LabeledPropertyGraphTypeEntry entry)
  {
    this.getVersions(entry).forEach(version -> {
      if (Session.getCurrentSession() != null)
      {
        this.versionService.remove(version);
      }
      else
      {
        this.versionService.delete(version);
      }

    });

    entry.delete();
  }

  @Override
  public JsonObject toJSON(LabeledPropertyGraphTypeEntry entry)
  {
    JsonObject object = entry.toJSON();
    List<LabeledPropertyGraphTypeVersion> versions = this.getVersions(entry);

    JsonArray jVersions = new JsonArray();

    for (LabeledPropertyGraphTypeVersion version : versions)
    {
      jVersions.add(this.versionService.toJSON(version));
    }

    object.add(LabeledPropertyGraphTypeEntry.VERSIONS, jVersions);

    return object;
  }

  @Override
  public List<LabeledPropertyGraphTypeVersion> getVersions(LabeledPropertyGraphTypeEntry entry)
  {
    LabeledPropertyGraphTypeVersionQuery query = new LabeledPropertyGraphTypeVersionQuery(new QueryFactory());
    query.WHERE(query.getEntry().EQ(entry));
    // query.AND(query.getWorking().EQ(false));
    query.ORDER_BY_DESC(query.getVersionNumber());

    try (OIterator<? extends LabeledPropertyGraphTypeVersion> it = query.getIterator())
    {
      return new LinkedList<LabeledPropertyGraphTypeVersion>(it.getAll());
    }
  }

  @Override
  public JsonArray getVersionJson(LabeledPropertyGraphTypeEntry entry)
  {
    List<LabeledPropertyGraphTypeVersion> versions = this.getVersions(entry);

    JsonArray jVersions = new JsonArray();

    for (LabeledPropertyGraphTypeVersion version : versions)
    {
      jVersions.add(this.versionService.toJSON(version));
    }

    return jVersions;
  }

  @Transaction
  public LabeledPropertyGraphTypeVersion createVersion(LabeledPropertyGraphTypeEntry entry)
  {
    List<LabeledPropertyGraphTypeVersion> versions = this.getVersions(entry);

    int versionNumber = versions.size() > 0 ? versions.get(0).getVersionNumber() + 1 : 1;

    return this.versionService.create(entry, false, versionNumber);
  }

  @Override
  public LabeledPropertyGraphTypeVersion publishNoAuth(LabeledPropertyGraphTypeEntry entry)
  {
    LabeledPropertyGraphTypeVersion version = this.createVersion(entry);

    // Refresh the users session
    ( (Session) Session.getCurrentSession() ).reloadPermissions();

    return version;
  }

  @Override
  public LabeledPropertyGraphTypeVersion publish(LabeledPropertyGraphTypeEntry entry)
  {
    return new LabeledPropertyGraphUtil(this).publishEntry(entry);
  }

  @Override
  @Transaction
  public LabeledPropertyGraphTypeEntry create(LabeledPropertyGraphType list, Date forDate)
  {
    LabeledPropertyGraphTypeEntry entry = new LabeledPropertyGraphTypeEntry();
    entry.setGraphType(list);
    entry.setForDate(forDate);
    entry.apply();

    this.versionService.create(entry, true, 0);

    return entry;
  }

  @Override
  @Transaction
  public LabeledPropertyGraphTypeEntry create(LabeledPropertyGraphType list, JsonObject json)
  {
    String forDate = json.get(LabeledPropertyGraphTypeEntry.FORDATE).getAsString();

    LabeledPropertyGraphTypeEntry entry = new LabeledPropertyGraphTypeEntry();
    entry.setGraphType(list);
    entry.setForDate(DateUtil.parseDate(forDate, false));
    entry.apply();

    return entry;
  }

  @Override
  public LabeledPropertyGraphTypeEntry get(String oid)
  {
    return LabeledPropertyGraphTypeEntry.get(oid);
  }

}
