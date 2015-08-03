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
package com.runwaysdk.geodashboard.gis.shapefile;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.IInitPage;
import com.runwaysdk.geodashboard.gis.LocalizedException;
import com.runwaysdk.geodashboard.gis.Localizer;
import com.runwaysdk.geodashboard.service.GeoEntityShapefileImporter;
import com.runwaysdk.logging.LogLevel;
import com.runwaysdk.logging.RunwayLogUtil;

public class ShapeFilePage extends WizardPage implements Reloadable
{
  class FileSelection extends MouseAdapter implements Reloadable
  {
    @Override
    public void mouseDown(MouseEvent e)
    {
      openFileDialog();
    }
  }

  class FileListener implements Listener, Reloadable
  {
    @Override
    public void handleEvent(Event arg0)
    {
      openFileDialog();
    }
  }

  public static final String  PAGE_NAME = "ShapeFilePage";

  private Text                fileText;

  private final ShapeFileBean data;

  public ShapeFilePage(ShapeFileBean data)
  {
    super(PAGE_NAME);
    setTitle(Localizer.getMessage("SHAPE_FILE_PAGE"));
    setDescription(Localizer.getMessage("SHAPE_FILE_DESC"));
    setPageComplete(false);

    this.data = data;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
   * .Composite)
   */
  public void createControl(Composite parent)
  {
    Composite composite = new Composite(parent, SWT.NULL);
    composite.setLayout(new FormLayout());

    Label label = new Label(composite, SWT.NULL);
    label.setText(Localizer.getMessage("SHAPE_FILE") + ": ");
    label.setLayoutData(new FormData());

    FormData compositeData = new FormData();
    compositeData.left = new FormAttachment(label, 5);
    compositeData.right = new FormAttachment(100, -5);

    Composite fileComposite = new Composite(composite, SWT.BORDER);
    fileComposite.setLayout(new FormLayout());
    fileComposite.setLayoutData(compositeData);

    FormData fileTextData = new FormData(SWT.DEFAULT, 20);
    fileTextData.left = new FormAttachment(0, 0);
    fileTextData.right = new FormAttachment(70, 0);

    fileText = new Text(fileComposite, SWT.BORDER);
    fileText.setLayoutData(fileTextData);
    fileText.addMouseListener(new FileSelection());

    FormData fileButtonData = new FormData();
    fileButtonData.left = new FormAttachment(fileText);
    fileButtonData.right = new FormAttachment(100, 0);

    Button fileButton = new Button(fileComposite, SWT.BORDER);
    fileButton.setLayoutData(fileButtonData);
    fileButton.setText(Localizer.getMessage("SELECT_FILE"));
    fileButton.addListener(SWT.Selection, new FileListener());

    setControl(composite);
  }

  private void openFileDialog()
  {
    FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
    dialog.setFilterNames(new String[] { Localizer.getMessage("SHAPE_FILES"), Localizer.getMessage("ALL_FILES") });
    dialog.setFilterExtensions(new String[] { "*.shp", "*.*" });

    String location = dialog.open();

    if (location != null && location.length() > 0)
    {
      this.setShapefile(location);
    }
  }

  private void setShapefile(String location)
  {
    fileText.setText(location);

    data.setShapeFile(new File(location));

    if (data.getShapeFile() != null)
    {
      processShapefile();
    }

    readyNextPage();
  }

  private void processShapefile()
  {
    class ProcessShapefileRunner implements IRunnableWithProgress, Reloadable
    {
      public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
      {
        monitor.beginTask(Localizer.getMessage("READ_SHAPE_FILE"), -1);

        try
        {
          GeoEntityShapefileImporter facade = new GeoEntityShapefileImporter(data.getShapeFile());
          List<String> attributes = facade.getAttributes();
          data.setAttributes(attributes);
        }
        catch (Throwable t)
        {
          throw new InvocationTargetException(t);
        }

        monitor.done();
      }
    }

    try
    {
      ShapeFilePage.this.setErrorMessage(null);

      getContainer().run(true, false, new ProcessShapefileRunner());
    }
    catch (Exception e)
    {
      RunwayLogUtil.logToLevel(LogLevel.ERROR, "Error while processing the shapefile", e);

      String msg = this.getExceptionMessage(e);

      ShapeFilePage.this.setErrorMessage(msg);

      setPageComplete(false);
    }
  }

  private String getExceptionMessage(Exception e)
  {
    String msg = null;

    if (e instanceof InvocationTargetException)
    {
      Throwable cause = e.getCause();

      if (cause instanceof LocalizedException)
      {
        msg = cause.getLocalizedMessage();
      }
    }

    if (msg == null)
    {
      msg = Localizer.getMessage("UNABLE_TO_READ_SHAPEFILE");
    }
    return msg;
  }

  private void readyNextPage()
  {
    if (data.getAttributes() != null)
    {
      IWizardPage page = ShapeFilePage.this.getNextPage();

      if (page instanceof IInitPage)
      {
        ( (IInitPage) page ).init();
      }

      setPageComplete(true);
    }
    else
    {
      setPageComplete(false);
    }
  }
}
