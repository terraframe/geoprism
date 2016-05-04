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
package net.geoprism.data.aws;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import net.geoprism.data.XMLEndpoint;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.util.IDGenerator;

public class AmazonEndpoint implements XMLEndpoint
{
  private static Logger logger = LoggerFactory.getLogger(AmazonEndpoint.class);

  private String getPrefix(String country, String version, String type)
  {
    return "deployable_countries/" + country + "/xml/" + version + "/" + type + "/";
  }

  @Override
  public List<String> listUniversalKeys(String country, String version)
  {
    String prefix = this.getPrefix(country, version, "universals");

    return this.listFiles(prefix);
  }

  @Override
  public List<String> listGeoEntityKeys(String country, String version)
  {
    String prefix = this.getPrefix(country, version, "geoentities");

    return this.listFiles(prefix);
  }

  @Override
  public File copyFiles(List<String> keys)
  {
    File directory = new File(FileUtils.getTempDirectory(), IDGenerator.nextID());
    directory.mkdirs();

    this.copyFiles(directory, keys, false);

    return directory;
  }

  @Override
  public void copyFiles(File directory, List<String> keys, boolean preserveDirectories)
  {
    try
    {
      List<File> files = new LinkedList<File>();

      AmazonS3 client = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());

      for (String key : keys)
      {
        GetObjectRequest request = new GetObjectRequest("geodashboarddata", key);

        S3Object object = client.getObject(request);

        InputStream istream = object.getObjectContent();

        try
        {
          String targetPath = this.getTargetPath(preserveDirectories, key);

          File file = new File(directory, targetPath);

          FileUtils.copyInputStreamToFile(istream, file);

          files.add(file);
        }
        finally
        {
          // Process the objectData stream.
          istream.close();
        }
      }
    }
    catch (IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private String getTargetPath(boolean preserveDirectories, String key)
  {
    if (preserveDirectories)
    {
      return key;
    }
    else
    {
      return new File(key).getName();
    }
  }

  private List<String> listFiles(String prefix)
  {
    List<String> files = new LinkedList<String>();

    try
    {
      AmazonS3 s3Client = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());

      ListObjectsRequest request = new ListObjectsRequest();
      request = request.withBucketName("geodashboarddata");
      request = request.withPrefix(prefix);

      ObjectListing listing;

      do
      {
        listing = s3Client.listObjects(request);

        List<S3ObjectSummary> summaries = listing.getObjectSummaries();

        for (S3ObjectSummary summary : summaries)
        {
          String key = summary.getKey();

          if (key.endsWith(".xml.gz"))
          {
            files.add(key);
          }
        }

        request.setMarker(listing.getNextMarker());
      } while (listing != null && listing.isTruncated());
    }
    catch (Exception e)
    {
      logger.error("Unable to retrieve files", e);
    }

    return files;
  }
}
