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

import java.util.Properties;

import org.eclipse.datatools.connectivity.internal.ui.dialogs.ExceptionHandler;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.runwaysdk.geodashboard.oda.driver.ui.GeodashboardPlugin;
import com.runwaysdk.geodashboard.oda.driver.ui.provider.ConnectionException;
import com.runwaysdk.geodashboard.oda.driver.ui.provider.GeodashboardMetaDataProvider;
import com.runwaysdk.geodashboard.oda.driver.ui.provider.LabelValuePair;
import com.runwaysdk.geodashboard.oda.driver.ui.provider.QueryFacadeUtil;
import com.runwaysdk.geodashboard.oda.driver.ui.util.DriverLoader;

/**
 * The Geodashborad DatasetEditor page which enable user to browse the catalog of the selected data source. The page
 * extends the <code>DataSetWizardPage</code> it could be loaded as a custom page for geodashboard ui.
 */

public class GeodashboardDataSetEditorPage extends DataSetWizardPage implements ISelectionChangedListener
{
  /**
   * Time out in seconds
   */
  private static final int             TIME_OUT_LIMIT = 20;

  /**
   * Combo viewer to select the query
   */
  private ComboViewer                  queryCombo;

  /**
   * Combo viewer of potential aggregation levels. Specific to a query.
   */
  private ComboViewer                  aggregationCombo;

  /**
   * Combo for potential default entities
   */
  private ComboViewer                  entityCombo;

  /**
   * Combo for geo nodes
   */
  private ComboViewer                  nodeCombo;

  /**
   * Provider used to communicate with the server
   */
  private GeodashboardMetaDataProvider provider;

  private boolean                      initialized;

  /**
   * constructor
   * 
   * @param pageName
   */
  public GeodashboardDataSetEditorPage(String pageName)
  {
    super(pageName);

    this.initialized = false;
  }

  @Override
  public void createPageCustomControl(Composite parent)
  {
    DataSetDesign dataSetDesign = this.getInitializationDesign();

    this.prepareGeodashboardMetaDataProvider(dataSetDesign);

    this.setControl(this.createPageControl(parent));

    this.initializeControl(dataSetDesign);
  }

  private void initializeControl(DataSetDesign dataSetDesign)
  {
    /*
     * Optionally restores the state of a previous design session. Obtains designer state, using
     * getInitializationDesignerState();
     */
    if (dataSetDesign == null)
    {
      return; // nothing to initialize
    }

    String queryText = dataSetDesign.getQueryText();

    this.updateFromQueryText(queryText);
  }

  @Override
  protected boolean canLeave()
  {
    /*
     * Page might not be initialized if the user is looking at a different page of the dataset editor. In this case just
     * assume the user can leave the page.
     */
    if (this.initialized)
    {
      if (this.aggregationCombo.getControl().getEnabled())
      {
        return ( this.getAggregation() != null && this.getQueryId() != null && this.getDefaultGeoId() != null );
      }

      return ( this.getQueryId() != null && this.getDefaultGeoId() != null );
    }

    return true;
  }

  /**
   * Create page for the geodashbaord dataset definition
   * 
   * @param parent
   * @return
   */
  private Control createPageControl(Composite parent)
  {
    // Group for selecting the Tables etc
    // Searching the Tables and Views
    GridLayout groupLayout = new GridLayout();
    groupLayout.numColumns = 3;
    groupLayout.verticalSpacing = 10;

    GridData selectTableData = new GridData(GridData.FILL_HORIZONTAL);

    Group selectTableGroup = new Group(parent, SWT.FILL);
    selectTableGroup.setLayout(groupLayout);
    selectTableGroup.setLayoutData(selectTableData);

    try
    {
      LabelValuePair[] queries = this.provider.getQueries(TIME_OUT_LIMIT * 1000);
      LabelValuePair[] entities = provider.getEntitySuggestions("", TIME_OUT_LIMIT * 1000);

      GridData gd = new GridData(GridData.FILL_HORIZONTAL);
      gd.horizontalSpan = 2;

      /*
       * Create a combo viewer to select the query
       */
      Label schemaLabel = new Label(selectTableGroup, SWT.LEFT);
      schemaLabel.setText(GeodashboardPlugin.getResourceString("dashboardpage.label.selectdashboard")); //$NON-NLS-1$

      this.queryCombo = new ComboViewer(selectTableGroup, SWT.READ_ONLY);
      this.queryCombo.getControl().setLayoutData(gd);
      this.queryCombo.setContentProvider(new ArrayContentProvider());
      this.queryCombo.setLabelProvider(new DataSetTypeLabelProvider());
      this.queryCombo.setInput(queries);
      this.queryCombo.addSelectionChangedListener(this);

      /*
       * Create a combo viewer to select the geo node
       */
      Label geoNodeLabel = new Label(selectTableGroup, SWT.LEFT);
      geoNodeLabel.setText(GeodashboardPlugin.getResourceString("dashboardpage.label.geoNode")); //$NON-NLS-1$

      this.nodeCombo = new ComboViewer(selectTableGroup, SWT.READ_ONLY);
      this.nodeCombo.getControl().setLayoutData(gd);
      this.nodeCombo.setContentProvider(new ArrayContentProvider());
      this.nodeCombo.setLabelProvider(new DataSetTypeLabelProvider());

      /*
       * Create a combo viewer to select the aggregation level
       */
      Label aggregationLabel = new Label(selectTableGroup, SWT.LEFT);
      aggregationLabel.setText(GeodashboardPlugin.getResourceString("dashboardpage.label.aggregation")); //$NON-NLS-1$

      this.aggregationCombo = new ComboViewer(selectTableGroup, SWT.READ_ONLY);
      this.aggregationCombo.getControl().setLayoutData(gd);
      this.aggregationCombo.setContentProvider(new ArrayContentProvider());
      this.aggregationCombo.setLabelProvider(new DataSetTypeLabelProvider());

      /*
       * Create a combo viewer to select the default geo id
       */
      Label entityLabel = new Label(selectTableGroup, SWT.LEFT);
      entityLabel.setText(GeodashboardPlugin.getResourceString("dashboardpage.label.defaultGeoId")); //$NON-NLS-1$

      this.entityCombo = new ComboViewer(selectTableGroup, SWT.READ_ONLY);
      this.entityCombo.getControl().setLayoutData(gd);
      this.entityCombo.setContentProvider(new ArrayContentProvider());
      this.entityCombo.setLabelProvider(new DataSetTypeLabelProvider());
      this.entityCombo.setInput(entities);

      /*
       * Initialize all of the combo views with the first selection. This must be done after the viewers have been
       * created because setting the selection will invoke any selection handler.
       */
      if (queries.length > 0)
      {
        this.queryCombo.setSelection(new StructuredSelection(queries[0]));
      }

      if (entities.length > 0)
      {
        this.entityCombo.setSelection(new StructuredSelection(entities[0]));
      }

      this.initialized = true;
    }
    catch (ConnectionException ex)
    {
      Shell shell = parent.getShell();
      String label = GeodashboardPlugin.getResourceString("dataset.error"); //$NON-NLS-1$
      String message = GeodashboardPlugin.getResourceString("dashboardpage.label.connectionerror"); //$NON-NLS-1$
      // String message = ex.getLocalizedMessage();

      ExceptionHandler.showException(shell, label, message, ex);

      Text exceptionLabel = new Text(selectTableGroup, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
      exceptionLabel.setLayoutData(new GridData(GridData.FILL_BOTH));
      exceptionLabel.setText(message);

      return null;
    }

    return selectTableGroup;
  }

  @Override
  public void selectionChanged(SelectionChangedEvent event)
  {
    IStructuredSelection selection = (IStructuredSelection) event.getSelection();
    LabelValuePair element = (LabelValuePair) selection.getFirstElement();

    String queryId = element.getValue();

    this.setQuerySelection(queryId);
  }

  private void setQuerySelection(String queryId)
  {
    LabelValuePair[] aggregations = provider.getSupportedAggregation(queryId, TIME_OUT_LIMIT * 1000);
    LabelValuePair[] geoNodes = provider.getGeoNodes(queryId, TIME_OUT_LIMIT * 1000);

    this.aggregationCombo.setInput(aggregations);

    if (aggregations.length > 0)
    {
      this.aggregationCombo.setSelection(new StructuredSelection(aggregations[0]));
    }
    else
    {
      this.aggregationCombo.getControl().setEnabled(true);
    }

    this.nodeCombo.setInput(geoNodes);

    if (geoNodes.length > 0)
    {
      this.nodeCombo.setSelection(new StructuredSelection(geoNodes[0]));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage
   * #collectDataSetDesign(org.eclipse.datatools.connectivity.oda.design. DataSetDesign)
   */
  protected DataSetDesign collectDataSetDesign(DataSetDesign design)
  {
    this.savePage(design);

    return design;
  }

  private void prepareGeodashboardMetaDataProvider(DataSetDesign dataSetDesign)
  {
    this.provider = new GeodashboardMetaDataProvider(dataSetDesign);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage #cleanup()
   */
  protected void cleanup()
  {
    try
    {
      if (this.provider != null)
      {
        this.provider.release();
      }
    }
    catch (OdaException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Updates the given dataSetDesign with the query and its metadata defined in this page.
   * 
   * @param dataSetDesign
   */
  private void savePage(DataSetDesign dataSetDesign)
  {
    String queryText = this.getQueryText();

    if (queryText != null)
    {
      dataSetDesign.setQueryText(queryText);
    }

    // obtain query's current runtime metadata, and maps it to the dataSetDesign
    IConnection connection = null;

    try
    {
      // obtain and open a live connection
      Properties props = DesignSessionUtil.getEffectiveDataSourceProperties(getInitializationDesign().getDataSourceDesign());

      // instantiate your custom ODA runtime driver class
      connection = DriverLoader.getConnection(props);

      // update the data set design with the
      // query's current runtime metadata
      updateDesign(dataSetDesign, connection, queryText);
    }
    catch (OdaException e)
    {
      // not able to get current metadata, reset previous derived metadata
      dataSetDesign.setResultSets(null);
      dataSetDesign.setParameters(null);
    }
    finally
    {
      closeConnection(connection);
    }

  }

  /**
   * Gets the query text
   * 
   * @return query text
   */
  private String getQueryText()
  {
    if (this.queryCombo != null)
    {
      String queryId = this.getQueryId();
      String aggregation = this.getAggregation();
      String defaultGeoId = this.getDefaultGeoId();
      String geoNodeId = this.getGeoNodeId();

      return QueryFacadeUtil.buildQueryText(queryId, aggregation, defaultGeoId, geoNodeId);
    }

    return null;
  }

  private String getValue(ComboViewer viewer)
  {
    StructuredSelection selection = (StructuredSelection) viewer.getSelection();
    LabelValuePair datasetType = (LabelValuePair) selection.getFirstElement();

    return datasetType.getValue();
  }

  private String getQueryId()
  {
    return this.getValue(this.queryCombo);
  }

  private String getAggregation()
  {
    if (this.aggregationCombo.getControl().getEnabled())
    {
      return this.getValue(this.aggregationCombo);
    }

    return null;
  }

  private String getDefaultGeoId()
  {
    return this.getValue(this.entityCombo);
  }

  private String getGeoNodeId()
  {
    return this.getValue(this.nodeCombo);
  }

  private void updateFromQueryText(String queryText)
  {
    if (queryText != null && queryText.length() > 0)
    {
      this.updateComboSelection(QueryFacadeUtil.getQueryIdFromQueryText(queryText), this.queryCombo);

      this.updateComboSelection(QueryFacadeUtil.getAggregationFromQueryText(queryText), this.aggregationCombo);

      this.updateComboSelection(QueryFacadeUtil.getDefaultGeoIdFromQueryText(queryText), this.entityCombo);

      this.updateComboSelection(QueryFacadeUtil.getGeoNodeIdFromQueryText(queryText), this.nodeCombo);
    }
  }

  private void updateComboSelection(String value, ComboViewer comboViewer)
  {
    if (comboViewer != null)
    {
      LabelValuePair[] pairs = (LabelValuePair[]) comboViewer.getInput();

      if (pairs != null)
      {
        for (LabelValuePair pair : pairs)
        {
          if (pair.getValue().equals(value))
          {
            comboViewer.setSelection(new StructuredSelection(pair));
          }
        }
      }
    }
  }

  /**
   * Updates the given dataSetDesign with the queryText and its derived metadata obtained from the ODA runtime
   * connection.
   */
  private void updateDesign(DataSetDesign dataSetDesign, IConnection conn, String queryText) throws OdaException
  {
    if (queryText != null)
    {
      MetadataRetriverRunnable runnable = new MetadataRetriverRunnable(conn, queryText);

      try
      {
        new ProgressMonitorDialog(getShell()).run(true, false, runnable);
      }
      catch (Exception e)
      {

      }

      if (runnable.getMetadata() != null && runnable.getThrowable() == null)
      {
        try
        {
          updateResultSetDesign(runnable.getMetadata(), dataSetDesign);
        }
        catch (OdaException e)
        {
          // no result set definition available, reset previous derived metadata
          dataSetDesign.setResultSets(null);
          e.printStackTrace();
        }
      }
      else
      {
        dataSetDesign.setResultSets(null);
      }

      if (runnable.getParameterMetadata() != null && runnable.getThrowable() == null)
      {
        // proceed to get parameter design definition
        try
        {
          updateParameterDesign(runnable.getParameterMetadata(), dataSetDesign);
        }
        catch (OdaException ex)
        {
          // no parameter definition available, reset previous derived metadata
          dataSetDesign.setParameters(null);
          ex.printStackTrace();
        }
      }
      else
      {
        dataSetDesign.setParameters(null);
      }
    }
  }

  /**
   * Updates the specified data set design's result set definition based on the specified runtime metadata.
   * 
   * @param md
   *          runtime result set metadata instance
   * @param dataSetDesign
   *          data set design instance to update
   * @throws OdaException
   */
  private void updateResultSetDesign(IResultSetMetaData md, DataSetDesign dataSetDesign) throws OdaException
  {
    ResultSetColumns columns = DesignSessionUtil.toResultSetColumnsDesign(md);

    ResultSetDefinition resultSetDefn = DesignFactory.eINSTANCE.createResultSetDefinition();
    // resultSetDefn.setName( value ); // result set name
    resultSetDefn.setResultSetColumns(columns);

    // no exception in conversion; go ahead and assign to specified
    // dataSetDesign
    dataSetDesign.setPrimaryResultSet(resultSetDefn);
    dataSetDesign.getResultSets().setDerivedMetaData(true);
  }

  /**
   * Updates the specified data set design's parameter definition based on the specified runtime metadata.
   * 
   * @param paramMd
   *          runtime parameter metadata instance
   * @param dataSetDesign
   *          data set design instance to update
   * @throws OdaException
   */
  private void updateParameterDesign(IParameterMetaData paramMd, DataSetDesign dataSetDesign) throws OdaException
  {
    DataSetParameters paramDesign = DesignSessionUtil.toDataSetParametersDesign(paramMd, DesignSessionUtil.toParameterModeDesign(IParameterMetaData.parameterModeIn));
    dataSetDesign.setParameters(paramDesign);

    if (paramDesign == null)
    {
      return; // no parameter definitions; done with update
    }

    paramDesign.setDerivedMetaData(true);
  }

  /**
   * Attempts to close given ODA connection.
   */
  private void closeConnection(IConnection conn)
  {
    try
    {
      if (conn != null && conn.isOpen())
      {
        conn.close();
      }
    }
    catch (OdaException e)
    {
      // ignore
    }
  }
}
