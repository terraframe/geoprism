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
package com.runwaysdk.geodashboard.oda.driver.ui.editors;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.runwaysdk.geodashboard.oda.driver.ui.GeodashboardPlugin;
import com.runwaysdk.geodashboard.oda.driver.ui.ssl.DuplicateCertificateException;
import com.runwaysdk.geodashboard.oda.driver.ui.ssl.SecureKeystoreManager;

public class ConfigureSSHFormDialog extends TitleAreaDialog
{
  private static int DELETE_ID = 1000;

  private Text       location;

  private Button     locationButton;

  private Text       alias;

  public ConfigureSSHFormDialog(Shell parentShell)
  {
    super(parentShell);

    this.setHelpAvailable(false);
  }

  @Override
  public void create()
  {
    super.create();

    this.setTitle(GeodashboardPlugin.getResourceString("wizard.ssh.title")); //$NON-NLS-1$
    this.setMessage(GeodashboardPlugin.getResourceString("wizard.ssh.message"), IMessageProvider.INFORMATION); //$NON-NLS-1$
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    Composite area = (Composite) super.createDialogArea(parent);

    // create the composite to hold the widgets
    GridLayout layout = new GridLayout();
    layout.numColumns = 4;
    layout.verticalSpacing = 10;
    // layout.marginBottom = 300;

    Composite content = new Composite(area, SWT.NONE);
    content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    content.setLayout(layout);

    GridData gridData;

    // Trust location editor
    new Label(content, SWT.RIGHT).setText(GeodashboardPlugin.getResourceString("wizard.ssh.label.location"));//$NON-NLS-1$

    // Editor
    gridData = new GridData();
    gridData.horizontalSpan = 2; // bidi_hcg
    gridData.horizontalAlignment = SWT.FILL;
    gridData.grabExcessHorizontalSpace = true;

    this.location = new Text(content, SWT.BORDER);
    this.location.setLayoutData(gridData);

    // File browser button
    this.locationButton = new Button(content, SWT.PUSH);
    this.locationButton.setText(GeodashboardPlugin.getResourceString("wizard.ssh.label.lbutton"));//$NON-NLS-1$
    this.locationButton.setLayoutData(new GridData(GridData.END));
    this.locationButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent evt)
      {
        handleLocationButtonSelection();
      }
    });

    // Password editor
    new Label(content, SWT.RIGHT).setText(GeodashboardPlugin.getResourceString("wizard.ssh.label.alias"));//$NON-NLS-1$

    gridData = new GridData();
    gridData.horizontalSpan = 3; // bidi_hcg
    gridData.horizontalAlignment = SWT.FILL;

    this.alias = new Text(content, SWT.BORDER);
    this.alias.setLayoutData(gridData);
    this.alias.setText("geodashboard"); //$NON-NLS-1$

    return content;
  }

  private void handleLocationButtonSelection()
  {
    FileDialog dialog = new FileDialog(getShell());
    dialog.setFilterExtensions(new String[] { "*.cer" });

    String path = this.location.getText();

    if (path != null && path.length() > 0)
    {
      dialog.setFileName(path);
    }

    String result = dialog.open();

    if (result != null)
    {
      this.location.setText(result);
    }
  }

  @Override
  protected Point getInitialSize()
  {
    return new Point(450, 300);
  }

  protected void createButtonsForButtonBar(Composite parent)
  {
    // Add the delete button
    createButton(parent, DELETE_ID, "Delete", false);

    super.createButtonsForButtonBar(parent);
  }

  @Override
  protected void buttonPressed(int buttonId)
  {
    if (buttonId == DELETE_ID)
    {
      handleDelete();
    }
    else
    {
      super.buttonPressed(buttonId);
    }
  }

  private void handleDelete()
  {
    try
    {
      SecureKeystoreManager.getInstance().delete();
    }
    catch (Exception e)
    {
      // Set the message
      this.setMessage(e.getMessage(), IMessageProvider.ERROR);
    }
  }

  @Override
  protected void okPressed()
  {
    this.setMessage(null);

    if (this.hasRequiredValues())
    {
      try
      {
        String alias = this.alias.getText();

        // Validate the cer
        FileInputStream stream = new FileInputStream(this.location.getText());

        try
        {
          SecureKeystoreManager.getInstance().addCertificate(stream, alias);
        }
        finally
        {
          stream.close();
        }

        super.okPressed();
      }
      catch (DuplicateCertificateException e)
      {
        String message = GeodashboardPlugin.getResourceString("wizard.ssh.label.duplicatealias"); //$NON-NLS-1$
        this.setMessage(message, IMessageProvider.ERROR);
      }
      catch (FileNotFoundException e)
      {
        String message = GeodashboardPlugin.getResourceString("wizard.ssh.label.filenotfound"); //$NON-NLS-1$
        this.setMessage(message, IMessageProvider.ERROR);
      }
      catch (Exception e)
      {
        // Set the message
        this.setMessage(e.getMessage(), IMessageProvider.ERROR);
      }
    }
    else
    {
      String message = GeodashboardPlugin.getResourceString("wizard.ssh.label.validation"); //$NON-NLS-1$
      this.setMessage(message, IMessageProvider.ERROR);
    }
  }

  private boolean hasRequiredValues()
  {
    return ( this.hasValue(this.location) && this.hasValue(this.alias) );
  }

  private boolean hasValue(Text text)
  {
    return ( text.getText() != null && text.getText().length() > 0 );
  }
}
