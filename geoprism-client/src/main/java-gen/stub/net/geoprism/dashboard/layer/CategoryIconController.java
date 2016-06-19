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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import net.geoprism.JSONControllerUtil;
import net.geoprism.dashboard.DashboardDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.MultipartFileParameter;
import com.runwaysdk.system.VaultFileDTO;
import com.runwaysdk.util.FileIO;

public class CategoryIconController extends CategoryIconControllerBase implements com.runwaysdk.generation.loader.Reloadable
{
  public CategoryIconController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous);
  }

  @Override
  public void create(MultipartFileParameter file, String label) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      String filename = file.getFilename();
      InputStream stream = file.getInputStream();

      try
      {
        String result = CategoryIconDTO.create(request, filename, stream, label);

        JSONControllerUtil.writeReponse(this.resp, new JSONArray(result));
      }
      finally
      {
        /*
         * Just in case the stream isn't closed by the server method
         */
        stream.close();
      }
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, this.getClientRequest());
    }
  }

  @Override
  public void getAll() throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      String icons = CategoryIconDTO.getAllAsJSON(request);

      JSONObject object = new JSONObject();
      object.put("icons", new JSONArray(icons));

      JSONControllerUtil.writeReponse(this.resp, object);
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, this.getClientRequest());
    }
  }

  @Override
  public void remove(String id) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();

    try
    {
      CategoryIconDTO.remove(request, id);

      JSONControllerUtil.writeReponse(this.resp, "");
    }
    catch (Throwable t)
    {
      JSONControllerUtil.handleException(this.resp, t, this.getClientRequest());
    }
  }
  
  @Override
  public void getCategoryIconImage(String iconId) throws IOException, ServletException
  {
    ClientRequestIF request = this.getClientRequest();
    
    InputStream iconStream = null;
    CategoryIconDTO icon = CategoryIconDTO.get(request, iconId);
    VaultFileDTO iconImage = icon.getImage();
    String rootPath = iconImage.getVaultPath();
    String fileName = iconImage.getFileName();
    String vaultFileName = iconImage.getVaultName();
    String fileExtension = iconImage.getFileExtension();
    
    //TODO: obviously dynamically set the root vault directory
    String fullImagePath = "/home/jlewis/git/geoprism/geoprism-server/target/vault/" + rootPath + "/" + vaultFileName;
    
    try
    {
//      InputStream istream = iconImage.get  getThumbnailStream();
      iconStream = new FileInputStream(new File(fullImagePath));
      try
      {
        resp.setContentType("image/png");

        ServletOutputStream ostream = resp.getOutputStream();

        try
        {
          FileIO.write(ostream, iconStream);

          ostream.flush();
        }
        finally
        {
          ostream.close();
        }
      }
      finally
      {
        iconStream.close();
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
}
