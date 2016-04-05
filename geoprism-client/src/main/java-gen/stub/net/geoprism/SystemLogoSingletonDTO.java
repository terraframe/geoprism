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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.util.FileIO;

public class SystemLogoSingletonDTO extends SystemLogoSingletonDTOBase
 implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1855290440;
  
  private static File bannerCache = null;
  
  private static File miniLogoCache = null;
  
  final static Logger logger = LoggerFactory.getLogger(SystemLogoSingletonDTO.class);
  
  public SystemLogoSingletonDTO(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected SystemLogoSingletonDTO(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  /**
   * Uploads a banner file to the server for persistance. Calling this method will also populate the client-side cache for
   * future calls to getBannerFilePath.
   * 
   * @param clientRequest
   * @param fileStream
   * @param fileName
   */
  public static void uploadBannerCached(com.runwaysdk.constants.ClientRequestIF clientRequest, java.io.InputStream fileStream, java.lang.String fileName)
  {
    bannerCache = null;
    SystemLogoSingletonDTOBase.uploadBanner(clientRequest, fileStream, fileName);
  }
  
  /**
   * Uploads a mini logo file to the server for persistance. Calling this method will also populate the client-side cache for
   * future calls to getMiniLogoFilePath.
   * 
   * @param clientRequest
   * @param fileStream
   * @param fileName
   */
  public static void uploadMiniLogoCached(com.runwaysdk.constants.ClientRequestIF clientRequest, java.io.InputStream fileStream, java.lang.String fileName)
  {
    miniLogoCache = null;
    SystemLogoSingletonDTOBase.uploadMiniLogo(clientRequest, fileStream, fileName);
  }
  
  /**
   * Calling this method will give you a path to a file on the system that will contain the uploaded banner, if it exists.
   * If the file does exist, it will be cached client-side. Subsequent calls will return the client-side cached file. The
   * underlying operating system may delete this cached file at any time, causing the file to be fetched from the server again.
   * If no banner has been uploaded this method will return null.
   * 
   * @param clientRequest
   * @return a file path or null
   */
  public static String getBannerFilePath(ClientRequestIF clientRequest)
  {
    if (bannerCache != null)
    {
      return bannerCache.getAbsolutePath();
    }
    
    InputStream stream = SystemLogoSingletonDTOBase.getBannerFile(clientRequest);
    if (stream == null) { return null; }
    
    String fileName = SystemLogoSingletonDTOBase.getBannerFilename(clientRequest);
    if (fileName == null) { return null; }
    
    File tempFile = genericGetCachedLogo(clientRequest, fileName, stream);
    if (tempFile == null) { return null; }
    
    bannerCache = tempFile;
    
    return bannerCache.getAbsolutePath();
  }
  
  /**
   * Calling this method will give you a path to a file on the system that will contain the uploaded mini logo, if it exists.
   * If the file does exist, it will be cached client-side. Subsequent calls will return the client-side cached file. The
   * underlying operating system may delete this cached file at any time, causing the file to be fetched from the server again.
   * If no mini logo has been uploaded this method will return null.
   * 
   * @param clientRequest
   * @return a file path or null
   */
  public static String getMiniLogoFilePath(ClientRequestIF clientRequest)
  {
    if (miniLogoCache != null)
    {
      return miniLogoCache.getAbsolutePath();
    }
    
    InputStream stream = SystemLogoSingletonDTOBase.getMiniLogoFile(clientRequest);
    if (stream == null) { return null; }
    
    String fileName = SystemLogoSingletonDTOBase.getMiniLogoFilename(clientRequest);
    if (fileName == null) { return null; }
    
    File tempFile = genericGetCachedLogo(clientRequest, fileName, stream);
    if (tempFile == null) { return null; }
    
    miniLogoCache = tempFile;
    
    return miniLogoCache.getAbsolutePath();
  }
  
  private static File genericGetCachedLogo(ClientRequestIF clientRequest, String fileName, InputStream stream)
  {
    String fileNoExt = fileName;
    String extension = "";
    int index = fileName.lastIndexOf('.');
    if (index != -1)
    {
      fileNoExt = fileName.substring(0, index);
      extension = fileName.substring(index + 1);
    }
    
    File tempFile = null;
    try
    {
      tempFile = File.createTempFile(fileNoExt, extension);
      FileIO.write(new FileOutputStream(tempFile), stream);
    }
    catch (IOException e)
    {
      logger.error("Error happened while trying to create a temp file for the logo.", e);
    }
    
    return tempFile;
  }
}