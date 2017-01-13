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

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.constants.VaultFileInfo;
import com.runwaysdk.dataaccess.io.FileReadException;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.CreatePermissionException;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.session.SessionIF;
import com.runwaysdk.system.VaultFile;
import com.runwaysdk.vault.VaultFileDAO;
import com.runwaysdk.vault.VaultFileDAOIF;

/**
 * This class is responsible for allowing the user to upload a custom banner or logo.
 * 
 * @author rrowlands
 */
public class SystemLogoSingleton extends SystemLogoSingletonBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static Logger logger = LoggerFactory.getLogger(SystemLogoSingleton.class);

  private static SystemLogoSingleton instance         = null;

  private static final long          serialVersionUID = 1806816568;

  public SystemLogoSingleton()
  {
    super();
  }

  public static synchronized SystemLogoSingleton getInstance()
  {
    if (instance == null)
    {
      SystemLogoSingletonQuery query = new SystemLogoSingletonQuery(new QueryFactory());
      OIterator<? extends SystemLogoSingleton> it = query.getIterator();
      if (it.hasNext())
      {
        instance = it.next();
      }
      else
      {
        instance = new SystemLogoSingleton();
        instance.apply();
      }
    }
    return instance;
  }

  /**
   * MdMethod Called to fetch the current banner file.
   */
  public static java.io.InputStream getBannerFile()
  {
    SystemLogoSingleton instance = getInstance();

    if (instance == null || instance.getBannerVaultId().equals(""))
    {
      return null;
    }

    try
    {
      return VaultFileDAO.get(instance.getBannerVaultId()).getFileStream();
    }
    catch (FileReadException e)
    {
      logger.error("Unable to retrieve banner file", e);
      
      return null;
    }
  }

  /**
   * MdMethod Called to fetch the current mini logo file.
   */
  public static java.io.InputStream getMiniLogoFile()
  {
    if (getInstance().getMiniLogoVaultId().equals(""))
    {
      return null;
    }

    try
    {
      return VaultFileDAO.get(getInstance().getMiniLogoVaultId()).getFileStream();
    }
    catch (FileReadException e)
    {
      logger.error("Unable to retrieve mini logo file", e);
      
      return null;
    }
  }

  private void checkVaultPermissions(VaultFile entity, Operation operation)
  {
    SessionIF session = Session.getCurrentSession();

    if (session != null)
    {
      String sessionId = session.getId();
      boolean access = SessionFacade.checkAccess(sessionId, operation, entity);

      if (!access)
      {
        SingleActorDAOIF user = SessionFacade.getUser(sessionId);
        String errorMsg = "User [" + user.getSingleActorName() + "] does not have permission to upload a system logo.";
        throw new CreatePermissionException(errorMsg, entity, user);
      }
    }
  }

  /**
   * MdMethod Called to get the filename of the persisted banner. Kind of a hack to get around the fact that we can't
   * return both an input stream and also the name of the file.
   */
  public static java.lang.String getBannerFilename()
  {
    VaultFileDAOIF vfile = VaultFileDAO.get(getInstance().getBannerVaultId());

    return vfile.getFileName() + "." + vfile.getExtension();
  }

  /**
   * MdMethod Called to persist a new banner logo.
   */
  public static void uploadBanner(InputStream fileStream, String fileName)
  {
    SystemLogoSingleton instance = getInstance();
    String vaultFileId = instance.genericLogoUpload(fileStream, fileName, instance.getBannerVaultId());

    instance.lock();
    instance.setBannerVaultId(vaultFileId);
    instance.apply();
  }

  /**
   * MdMethod Called to persist a new mini logo.
   */
  public static void uploadMiniLogo(InputStream fileStream, String fileName)
  {
    SystemLogoSingleton instance = getInstance();
    String vaultFileId = instance.genericLogoUpload(fileStream, fileName, instance.getMiniLogoVaultId());

    instance.lock();
    instance.setMiniLogoVaultId(vaultFileId);
    instance.apply();
  }

  /**
   * MdMethod Called to get the filename of the persisted mini logo. Kind of a hack to get around the fact that we can't
   * return both an input stream and also the name of the file.
   */
  public static java.lang.String getMiniLogoFilename()
  {
    VaultFileDAOIF vfile = VaultFileDAO.get(getInstance().getMiniLogoVaultId());

    return vfile.getFileName() + "." + vfile.getExtension();
  }

  private String genericLogoUpload(InputStream fileStream, String fileName, String vaultId)
  {
    if (fileStream == null)
    {
      return null;
    }

    String fileNoExt = fileName;
    String extension = "";
    int index = fileName.lastIndexOf('.');
    if (index != -1)
    {
      fileNoExt = fileName.substring(0, index);
      extension = fileName.substring(index + 1);
    }

    VaultFile vaultFile;
    VaultFileDAO vaultFileDAO;

    if (vaultId.equals(""))
    {
      /*
       * Create a new vault file
       */
      vaultFile = new VaultFile();
      vaultFileDAO = (VaultFileDAO) BusinessFacade.getEntityDAO(vaultFile);

      this.checkVaultPermissions(vaultFile, Operation.CREATE);
    }
    else
    {
      /*
       * Update the existing vault file
       */
      vaultFile = VaultFile.lock(vaultId);
      vaultFileDAO = (VaultFileDAO) BusinessFacade.getEntityDAO(vaultFile);

      this.checkVaultPermissions(vaultFile, Operation.WRITE);
    }

    vaultFile.setValue(VaultFileInfo.FILE_NAME, fileNoExt);
    vaultFile.setValue(VaultFileInfo.EXTENSION, extension);
    vaultFileDAO.setSize(0);
    vaultFile.apply();
    vaultFileDAO.putFile(fileStream); // putFile will call apply for us

    return vaultFile.getId();
  }

}
