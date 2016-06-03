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
package net.geoprism.dashboard.layer;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.VaultFileInfo;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.VaultFile;
import com.runwaysdk.vault.VaultDAO;
import com.runwaysdk.vault.VaultDAOIF;
import com.runwaysdk.vault.VaultFileDAO;
import com.runwaysdk.vault.VaultFileDAOIF;

public class CategoryIcon extends CategoryIconBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1118361586;

  public CategoryIcon()
  {
    super();
  }

  public JSONObject toJSON()
  {
    try
    {
      JSONObject object = new JSONObject();
      object.put("label", this.getDisplayLabel().getValue());
      object.put("id", this.getId());
      object.put("filePath", this.getFilePath());

      return object;
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }
  
  public String getFilePath()
  {
    VaultFileDAOIF file = VaultFileDAO.get(this.getImageId());
    VaultDAOIF vault = VaultDAO.get(file.getVaultReference());
    
    String rootPath = vault.getVaultPath();
    String filePath = file.getVaultFilePath();
    
    String path = rootPath + filePath + file.getVaultFileName();
    
    return path;
  }

  @Transaction
  public static String create(String path, InputStream fileStream, String label)
  {
    int index = path.lastIndexOf('.');

    if (index != -1)
    {
      String filename = path.substring(0, index);
      String extension = path.substring(index + 1);

      VaultFile entity = new VaultFile();
      VaultFileDAO file = (VaultFileDAO) BusinessFacade.getEntityDAO(entity);

      entity.setValue(VaultFileInfo.FILE_NAME, filename);
      entity.setValue(VaultFileInfo.EXTENSION, extension);

      file.setSize(0);
      entity.apply();
      file.putFile(fileStream);

      CategoryIcon icon = new CategoryIcon();
      icon.getDisplayLabel().setValue(label);
      icon.setImage(entity);
      icon.apply();

      JSONArray icons = new JSONArray();
      icons.put(icon.toJSON());

      return icons.toString();
    }

    throw new ProgrammingErrorException("Bad filename: No extension");
  }

  @Transaction
  public static void remove(String id)
  {
    CategoryIcon icon = CategoryIcon.get(id);
    icon.delete();

    VaultFile image = icon.getImage();
    image.delete();
  }

  public static String getAllAsJSON()
  {
    JSONArray categories = new JSONArray();

    CategoryIconQuery query = new CategoryIconQuery(new QueryFactory());
    query.ORDER_BY_DESC(query.getDisplayLabel().localize());

    OIterator<? extends CategoryIcon> iterator = query.getIterator();

    try
    {
      while (iterator.hasNext())
      {
        CategoryIcon icon = iterator.next();

        categories.put(icon.toJSON());
      }
    }
    finally
    {
      iterator.close();
    }

    return categories.toString();
  }
}
