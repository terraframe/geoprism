package net.geoprism.dhis2.palestine;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.scheduler.AllJobStatus;

import net.geoprism.account.ExternalProfile;
import net.geoprism.configuration.GeoprismConfigurationResolver;
import net.geoprism.data.etl.DataImportState;
import net.geoprism.data.etl.excel.ExcelDataFormatter;
import net.geoprism.data.etl.excel.ExcelImportHistory;
import net.geoprism.data.etl.excel.ExcelSheetReader;
import net.geoprism.data.etl.excel.JobHistoryProgressMonitor;
import net.geoprism.dhis2.connector.AbstractDHIS2Connector;
import net.geoprism.dhis2.connector.DHIS2HTTPCredentialConnector;
import net.geoprism.dhis2.connector.DHIS2OAuthConnector;
import net.geoprism.dhis2.response.OAuthLoginRequiredException;

public class PalestineExporter
{
  private AbstractDHIS2Connector dhis2;
  
  @Request
  public static void main(String[] args)
  {
    CommandLineParser parser = new DefaultParser();
    Options options = new Options();
    options.addOption(Option.builder("url").hasArg().argName("url").longOpt("url").desc("URL of the DHIS2 server to connect to, including the port. Defaults to: http://127.0.0.1:8085/").optionalArg(true).build());
    options.addOption(Option.builder("username").hasArg().argName("username").longOpt("username").desc("The username of the root (admin) DHIS2 user.").required().build());
    options.addOption(Option.builder("password").hasArg().argName("password").longOpt("password").desc("The password for the root (admin) DHIS2 user.").required().build());
    options.addOption(Option.builder("appcfgPath").hasArg().argName("appcfgPath").longOpt("appcfgPath").desc("An absolute path to the external configuration directory for this geoprism app.").optionalArg(true).build());
    options.addOption(Option.builder("excelFile").hasArg().argName("excelFile").longOpt("excelFile").desc("The path of the excelFile to export.").required().build());
    
    try {
      CommandLine line = parser.parse( options, args );
      
      String url = line.getOptionValue("url");
      String username = line.getOptionValue("username");
      String password = line.getOptionValue("password");
      String appcfgPath = line.getOptionValue("appcfgPath");
      String excelFile = line.getOptionValue("excelFile");
      
      if (url == null)
      {
        url = "http://127.0.0.1:8085/";
      }
      if (appcfgPath != null)
      {
        GeoprismConfigurationResolver resolver = (GeoprismConfigurationResolver) ConfigurationManager.Singleton.INSTANCE.getConfigResolver();
        resolver.setExternalConfigDir(new File(appcfgPath));
      }
      
      PalestineExporter exporter = new PalestineExporter(url, username, password);
      exporter.xport(excelFile);
    }
    catch (ParseException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  // Our constructor must be 0 arguments because it conforms to Java service loader paradigm.
  public PalestineExporter()
  {
    dhis2 = new DHIS2OAuthConnector();
    dhis2.readConfigFromDB();
  }
  
  public PalestineExporter(String url, String username, String password)
  {
    dhis2 = new DHIS2HTTPCredentialConnector();
    dhis2.setServerUrl(url);
    dhis2.setCredentials(username, password);
  }
  
//  @Override
  public void xport(String filename)
  {
    if (dhis2 instanceof DHIS2OAuthConnector && ExternalProfile.getAccessToken() == null)
    {
      OAuthLoginRequiredException ex = new OAuthLoginRequiredException();
      throw ex;
    }
    
    actuallyDoExport(filename);
  }
  
  private void actuallyDoExport(String filename)
  {
    ExcelImportHistory history = null;
    
    try
    {
      FileInputStream istream = new FileInputStream(filename);
      
      history = new ExcelImportHistory();
      history.setStartTime(new Date());
      history.addStatus(AllJobStatus.RUNNING);
      history.setFileName(filename);
      history.apply();
      
      JobHistoryProgressMonitor monitor = new JobHistoryProgressMonitor(history);
      
      PalestineConverter converter = new PalestineConverter(dhis2);
      
      PalestineContentHandler handler = new PalestineContentHandler(converter, monitor, DataImportState.DATAIMPORT);
      ExcelDataFormatter formatter = new ExcelDataFormatter();
  
      ExcelSheetReader reader = new ExcelSheetReader(handler, formatter);
      reader.process(istream);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
    finally
    {
      if (history != null)
      {
        history.clearStatus();
        history.addStatus(AllJobStatus.SUCCESS);
        history.apply();
      }
    }
  }
}
