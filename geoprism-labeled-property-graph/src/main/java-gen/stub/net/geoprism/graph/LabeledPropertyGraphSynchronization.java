package net.geoprism.graph;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonObject;
import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Session;

import net.geoprism.graph.adapter.RegistryBridge;
import net.geoprism.graph.adapter.RegistryConnectorFactory;
import net.geoprism.graph.adapter.RegistryConnectorIF;
import net.geoprism.graph.adapter.exception.BadServerUriException;
import net.geoprism.graph.adapter.exception.HTTPException;
import net.geoprism.graph.service.LabeledPropertyGraphServiceIF;

public class LabeledPropertyGraphSynchronization extends LabeledPropertyGraphSynchronizationBase
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
    JsonObject data = this.doIt();

    // Refresh permissions in case new definitions were defined during the
    // synchronization process
    Session session = (Session) Session.getCurrentSession();

    if (session != null)
    {
      session.reloadPermissions();
    }
    
    LabeledPropertyGraphTypeVersion version = this.getVersion();
    version.truncate();

    new JsonGraphVersionPublisher(version).publish(data);

    LabeledPropertyGraphServiceIF.getInstance().postSynchronization(version);
  }

  @Transaction
  private JsonObject doIt()
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

      JsonObject data = bridge.getData(this.getRemoteVersion()).getJsonObject();

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
      
      return data;
    }
    catch (HTTPException | BadServerUriException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

}
