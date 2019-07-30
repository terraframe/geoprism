/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.shapefile.locatedIn;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.geoprism.data.importer.LocatedInBean;
import net.geoprism.data.importer.PathOption;
import net.geoprism.shapefile.Localizer;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;



public class PathFilterPage extends WizardPage implements PropertyChangeListener
{
  public static String  PAGE_NAME = "LocatedInPage";

  private LocatedInBean bean;

  public PathFilterPage(LocatedInBean bean)
  {
    super(PAGE_NAME);

    setTitle(Localizer.getMessage("PATH_CONFIGURATION_TITLE"));
    setDescription(Localizer.getMessage("PATH_CONFIGURATION_DESCRIPTION"));

    this.bean = bean;
    // this.bean.addPropertyChangeListener("option", this);
    // this.bindingContext = new DataBindingContext(SWTObservables.getRealm(Display.getDefault()));

    this.setPageComplete(true);
  }

  @Override
  public void createControl(Composite parent)
  {
    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new GridLayout(1, false));

    Map<String, List<PathOption>> paths = this.bean.getPaths();

    Set<Entry<String, List<PathOption>>> entries = paths.entrySet();

    for (Entry<String, List<PathOption>> entry : entries)
    {
      List<PathOption> options = entry.getValue();

      if (options.size() > 0)
      {
        PathOption option = options.get(0);

        Group group = new Group(composite, SWT.SHADOW_ETCHED_OUT);
        group.setText(option.getChildLabel());
        group.setLayoutData(new GridData(GridData.FILL_BOTH));
        group.setLayout(new FillLayout());

        PathTreeContentProvider provider = new PathTreeContentProvider(options);

        // Create the tree viewer to display the file tree
        final CheckboxTreeViewer viewer = new CheckboxTreeViewer(group);
        viewer.setContentProvider(provider);
        viewer.setLabelProvider(new PathOptionProvider());
        viewer.setCheckStateProvider(new PathOptionProvider());
        viewer.setInput("root");

        viewer.addCheckStateListener(new ICheckStateListener()
        {
          public void checkStateChanged(CheckStateChangedEvent event)
          {
            PathOption option = (PathOption) event.getElement();
            option.setEnabled(event.getChecked());

            if (!event.getChecked())
            {
              viewer.setSubtreeChecked(option, false);
            }
          }
        });
      }
    }

    this.setControl(composite);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt)
  {
    // TODO Auto-generated method stub

  }
}
