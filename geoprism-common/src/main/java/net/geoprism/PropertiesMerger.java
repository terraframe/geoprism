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
  
  public static void main(String[] args) throws ConfigurationException, ParseException, IOException
  {
    CommandLineParser parser = new DefaultParser();
    Options options = new Options();
    options.addOption(Option.builder("b").hasArg().argName("baseFile").desc("The path to the base properties file.").build());
    options.addOption(Option.builder("o").hasArg().argName("overrideFile").desc("The path to the override properties file.").build());
    options.addOption(Option.builder("e").hasArg().argName("exportFile").desc("The path to the export properties file. A null value defaults to base.").build());
    options.addOption(Option.builder("B").hasArg().argName("baseDir").desc("The path to the base directory.").build());
    options.addOption(Option.builder("O").hasArg().argName("overrideDir").desc("The path to the override directory.").build());
    options.addOption(Option.builder("E").hasArg().argName("exportDir").desc("The path to the export directory. A null value defaults to base.").build());
    CommandLine line = parser.parse( options, args );
    
    String sBase = line.getOptionValue("b");
    String sOverride = line.getOptionValue("o");
    String sExport = line.getOptionValue("e");
    String sBaseDir = line.getOptionValue("B");
    String sOverrideDir = line.getOptionValue("O");
    String sExportDir = line.getOptionValue("E");
    
    if (sBase != null && sOverride != null)
    {
      if (sExport == null)
      {
        sExport = sBase;
      }
      
      PropertiesMerger merger = new PropertiesMerger();
      merger.mergeProperties(new File(sBase), new File(sOverride), new File(sExport));
    }
    else if (sBaseDir != null && sOverrideDir != null)
    {
      if (sExportDir == null)
      {
        sExportDir = sBaseDir;
      }
      
      PropertiesMerger merger = new PropertiesMerger();
      merger.mergeDirectories(new File(sBaseDir), new File(sOverrideDir), new File(sExportDir));
    }
    else
    {
      throw new RuntimeException("Invalid arguments");
    }
  }
  
  public PropertiesMerger()
  {
    
  }
  
  public void mergeDirectories(File base, File override, File export) throws ConfigurationException, IOException
  {
    if (!export.exists())
    {
      export.mkdirs();
    }
    
    for (File child : override.listFiles())
    {
      String name = child.getName();
      
      if (child.isDirectory())
      {
        mergeDirectories(new File(base, name), child, new File(export, name));
      }
      else if (name.endsWith(".properties"))
      {
        mergeProperties(new File(base, name), child, new File(export, name));
      }
    }
  }
  
  public void mergeProperties(File base, File override, File export) throws ConfigurationException, IOException
  {
    if (!export.exists())
    {
      export.createNewFile();
    }
    if (!base.exists())
    {
      base.createNewFile();
    }
    
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
