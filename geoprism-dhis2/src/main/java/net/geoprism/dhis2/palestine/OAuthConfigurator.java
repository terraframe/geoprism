package net.geoprism.dhis2.palestine;

import java.io.File;
import java.sql.Savepoint;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;

import net.geoprism.account.ExternalProfile;
import net.geoprism.account.ExternalProfileQuery;
import net.geoprism.account.OauthServer;
import net.geoprism.configuration.GeoprismConfigurationResolver;
import net.geoprism.dhis2.DHIS2Configuration;
import net.geoprism.dhis2.connector.DHIS2OAuthConnector;

public class OAuthConfigurator
{
  public static void main(String[] args)
  {
    CommandLineParser parser = new DefaultParser();
    Options options = new Options();
    options.addOption(Option.builder("url").hasArg().argName("url").longOpt("url").desc("URL of the DHIS2 server to connect to, including the port. Defaults to: http://127.0.0.1:8085/").optionalArg(true).build());
    options.addOption(Option.builder("username").hasArg().argName("username").longOpt("username").desc("The username of the root (admin) DHIS2 user.").required().build());
    options.addOption(Option.builder("password").hasArg().argName("password").longOpt("password").desc("The password for the root (admin) DHIS2 user.").required().build());
    options.addOption(Option.builder("appcfgPath").hasArg().argName("appcfgPath").longOpt("appcfgPath").desc("An absolute path to the external configuration directory for this geoprism app.").optionalArg(true).build());
    
    try {
      CommandLine line = parser.parse( options, args );
      
      String url = line.getOptionValue("url");
      String username = line.getOptionValue("username");
      String password = line.getOptionValue("password");
      String appcfgPath = line.getOptionValue("appcfgPath");
      
      if (url == null)
      {
        url = "http://127.0.0.1:8085/";
      }
      if (appcfgPath != null)
      {
        GeoprismConfigurationResolver resolver = (GeoprismConfigurationResolver) ConfigurationManager.Singleton.INSTANCE.getConfigResolver();
        resolver.setExternalConfigDir(new File(appcfgPath));
      }
      
      configure(username, password, url);
    }
    catch (ParseException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  @Request
  private static void configure(String user, String pazz, String url)
  {
    DHIS2Configuration config = DHIS2Configuration.getByKey("DEFAULT");
    config.setPazzword(pazz);
    config.setUsername(user);
    config.setUrl(url);
    config.appLock();
    config.apply();
    
    deleteServer();
    
    DHIS2OAuthConnector dhis2 = new DHIS2OAuthConnector();
    dhis2.readConfigFromDB();
    dhis2.initialize();
  }
  
  @Transaction
  private static void deleteServer()
  {
    ExternalProfileQuery pq = new ExternalProfileQuery(new QueryFactory());
    OIterator<? extends ExternalProfile> it = pq.getIterator();
    while (it.hasNext())
    {
      it.next().delete();
    }
    
    Savepoint sp = Database.setSavepoint();
    
    try
    {
      OauthServer server = OauthServer.getByKey(DHIS2OAuthConnector.OAUTH_KEY_NAME);
      server.delete();
    }
    catch (DataNotFoundException ex)
    {
      Database.rollbackSavepoint(sp);
    }
    catch (RuntimeException ex)
    {
      Database.rollbackSavepoint(sp);
      throw ex;
    }
    finally
    {
      Database.releaseSavepoint(sp);
    }
  }
}
