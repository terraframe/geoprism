/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
/**
 * This class performs merge operations between files or directories. This is designed for use in the build system for custom resource merging.
 * 
 * @author rrowlands
 */
public class FileMerger
{
  private CompositeConfiguration cconfig;
  
  public static void main(String[] args) throws ConfigurationException, ParseException, IOException
  {
    CommandLineParser parser = new DefaultParser();
    Options options = new Options();
    options.addOption(Option.builder("b").hasArg().argName("baseFile").longOpt("baseFile").desc("The path to the base properties file.").build());
    options.addOption(Option.builder("o").hasArg().argName("overrideFile").longOpt("overrideFile").desc("The path to the override properties file.").build());
    options.addOption(Option.builder("e").hasArg().argName("exportFile").longOpt("exportFile").desc("The path to the export properties file. A null value defaults to base.").build());
    options.addOption(Option.builder("B").hasArg().argName("baseDir").longOpt("baseDir").desc("The path to the base directory.").build());
    options.addOption(Option.builder("O").hasArg().argName("overrideDir").longOpt("overrideDir").desc("The path to the override directory.").build());
    options.addOption(Option.builder("E").hasArg().argName("exportDir").longOpt("exportDir").desc("The path to the export directory. A null value defaults to base.").build());
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
      
      File fBase = new File(sBase);
      File fOverride = new File(sOverride);
      File fExport = new File(sExport);
      if (!fBase.exists() || !fOverride.exists())
      {
        throw new RuntimeException("The base [" + sBase + "] and the override [" + sOverride + "] paths must both exist.");
      }
      
      FileMerger merger = new FileMerger();
      merger.mergeProperties(fBase, fOverride, fExport);
    }
    else if (sBaseDir != null && sOverrideDir != null)
    {
      if (sExportDir == null)
      {
        sExportDir = sBaseDir;
      }
      
      File fBaseDir = new File(sBaseDir);
      File fOverrideDir = new File(sOverrideDir);
      File fExportDir = new File(sExportDir);
      if (!fBaseDir.exists() || !fOverrideDir.exists())
      {
        throw new RuntimeException("The base [" + sBaseDir + "] and the override [" + sOverrideDir + "] paths must both exist.");
      }
      
      FileMerger merger = new FileMerger();
      merger.mergeDirectories(fBaseDir, fOverrideDir, fExportDir);
    }
    else
    {
      throw new RuntimeException("Invalid arguments");
    }
  }
  
  public FileMerger()
  {
    
  }
  
  public void mergeDirectories(File base, File override, File export) throws ConfigurationException, IOException
  {
    if (!export.exists())
    {
      export.mkdirs();
    }
    
    if (!export.equals(override))
    {
      for (File overrideChild : override.listFiles())
      {
        mergeFile(new File(base, overrideChild.getName()), overrideChild, new File(export, overrideChild.getName()));
      }
    }
    if (!export.equals(base))
    {
      for (File baseChild : base.listFiles())
      {
        mergeFile(baseChild, new File(override, baseChild.getName()), new File(export, baseChild.getName()));
      }
    }
  }
  
  public void mergeFile(File base, File override, File export) throws ConfigurationException, IOException
  {
    if (base == null || !base.exists())
    {
      if (!override.equals(export))
      {
        if (override.isDirectory())
        {
          mergeDirectories(base, override, export);
        }
        else
        {
          FileUtils.copyFile(override, export);
        }
      }
    }
    else if (override == null || !override.exists())
    {
      if (!base.equals(export))
      {
        if (base.isDirectory())
        {
          mergeDirectories(base, override, export);
        }
        else
        {
          FileUtils.copyFile(base, export);
        }
      }
    }
    else
    {
      if (override.isDirectory())
      {
        mergeDirectories(base, override, export);
      }
      else if (override.getName().endsWith(".properties"))
      {
        mergeProperties(base, override, export);
      }
      else if (!override.equals(export))
      {
        FileUtils.copyFile(override, export);
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
      if (override != export)
      {
        FileUtils.copyFile(override, export);
      }
      return;
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
    
    Configuration exportConfig = exportBuilder.getConfiguration();
    
    Iterator<String> i = this.cconfig.getKeys();
    while (i.hasNext())
    {
      String key = i.next();
      
      Object value = this.cconfig.getProperty(key);
      if (value instanceof String)
      {
        exportConfig.setProperty(key, value);
      }
      else if (value instanceof ArrayList)
      {
        @SuppressWarnings("rawtypes")
        ArrayList values = (ArrayList) value;
        
        System.out.println("WARNING: Multiple values found in [" + base.getAbsolutePath() + "] for key [" + key + "]. Values = " + value.toString());
        exportConfig.setProperty(key, values.get(values.size()-1));
      }
      else
      {
        throw new RuntimeException("Unknown property type [" + value.getClass().getName() + "] + [" + value.toString() + "].");
      }
    }
    
    exportBuilder.save();
  }
}
