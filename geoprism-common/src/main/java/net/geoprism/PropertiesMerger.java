package net.geoprism;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
/**
 * This class is designed to take in 2 properties files and export a merged file as part of the build system. 
 * 
 * @author rrowlands
 */
public class PropertiesMerger
{
  private CompositeConfiguration cconfig;
  
  private File base;
  
  private File override;
  
  private File export;
  
  public static void main(String[] args) throws ConfigurationException, ParseException, IOException
  {
    CommandLineParser parser = new DefaultParser();
    Options options = new Options();
    options.addOption(Option.builder("b").hasArg().argName("base").required().desc("The path to the base properties file.").build());
    options.addOption(Option.builder("o").hasArg().argName("override").required().desc("The path to the override properties file.").build());
    options.addOption(Option.builder("e").hasArg().argName("export").required().desc("The path to the export properties file.").build());
    CommandLine line = parser.parse( options, args );

    File base = new File(line.getOptionValue("b"));
    File override = new File(line.getOptionValue("o"));
    File export = new File(line.getOptionValue("e"));
    
    PropertiesMerger merger = new PropertiesMerger(base, override, export);
    merger.export();
  }
  
  public PropertiesMerger(File base, File override, File export)
  {
    this.base = base;
    this.override = override;
    this.export = export;
  }
  
  public void export() throws ConfigurationException, IOException
  {
    Parameters params = new Parameters();
    FileBasedConfigurationBuilder<FileBasedConfiguration> baseBuilder =
        new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
        .configure(params.fileBased()
            .setFile(base));
    
    FileBasedConfigurationBuilder<FileBasedConfiguration> overrideBuilder =
        new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
        .configure(params.fileBased()
            .setFile(override));
    
    this.cconfig = new CompositeConfiguration();
    this.cconfig.addConfiguration(overrideBuilder.getConfiguration());
    this.cconfig.addConfiguration(baseBuilder.getConfiguration());
    
    if (!export.exists())
    {
      export.createNewFile();
    }
    
    FileBasedConfigurationBuilder<FileBasedConfiguration> exportBuilder =
        new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
        .configure(params.fileBased()
            .setFile(export));
    
    Configuration interpolated = this.cconfig.interpolatedConfiguration();
    Configuration exportConfig = exportBuilder.getConfiguration();
    
    Iterator<String> i = interpolated.getKeys();
    while (i.hasNext())
    {
      String key = i.next();
      String value = interpolated.get(String.class, key);
      exportConfig.setProperty(key,value);
    }
    
    exportBuilder.save();
  }
}
