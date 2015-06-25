package com.runwaysdk.geodashboard.oda.driver.ui.editors;

import java.util.Properties;

import org.eclipse.datatools.connectivity.internal.ui.dialogs.ExceptionHandler;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
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

import com.runwaysdk.geodashboard.oda.driver.Driver;
import com.runwaysdk.geodashboard.oda.driver.ui.GeodashboardPlugin;
import com.runwaysdk.geodashboard.oda.driver.ui.provider.ConnectionException;
import com.runwaysdk.geodashboard.oda.driver.ui.provider.GeodashboardMetaDataProvider;
import com.runwaysdk.geodashboard.oda.driver.ui.provider.LabelValuePair;
import com.runwaysdk.geodashboard.oda.driver.ui.provider.QueryFacadeUtil;

/**
 * The Geodashborad DatasetEditor page which enable user to browse the catalog of the selected data source. The page
 * extends the <code>DataSetWizardPage</code> it could be loaded as a custom page for geodashboard ui.
 */

public class GeodashboardDataSetEditorPage extends DataSetWizardPage
{
  /**
   * Time out in seconds
   */
  private static final int             TIME_OUT_LIMIT = 20;

  /**
   * Combo viewer to select the query
   */
  private ComboViewer                  schemaCombo;

  /**
   * Combo viewer of potential aggregation levels. Specific to a query.
   */
  private ComboViewer                  aggregationCombo;

  /**
   * Combo for potential entities
   */
  private ComboViewer                  entityCombo;

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
    if (this.initialized)
    {
      if (this.aggregationCombo.getControl().getEnabled())
      {
        return ( this.getAggregation() != null && this.getValue() != null && this.getDefaultGeoId() != null );
      }

      return ( this.getValue() != null && this.getDefaultGeoId() != null );
    }

    return false;
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
      LabelValuePair[] input = this.provider.getTypes(TIME_OUT_LIMIT * 1000);
      LabelValuePair[] entities = provider.getEntitySuggestions("", TIME_OUT_LIMIT * 1000);

      GridData gd = new GridData(GridData.FILL_HORIZONTAL);
      gd.horizontalSpan = 2;

      Label schemaLabel = new Label(selectTableGroup, SWT.LEFT);
      schemaLabel.setText(GeodashboardPlugin.getResourceString("dashboardpage.label.selectdashboard"));

      this.schemaCombo = new ComboViewer(selectTableGroup, SWT.READ_ONLY);
      this.schemaCombo.getControl().setLayoutData(gd);
      this.schemaCombo.setContentProvider(new ArrayContentProvider());
      this.schemaCombo.setLabelProvider(new DataSetTypeLabelProvider());
      this.schemaCombo.setInput(input);

      if (input.length > 0)
      {
        this.schemaCombo.setSelection(new StructuredSelection(input[0]));
      }

      Label depthLabel = new Label(selectTableGroup, SWT.LEFT);
      depthLabel.setText(GeodashboardPlugin.getResourceString("dashboardpage.label.aggregation"));

      // Create a single line text field
      this.aggregationCombo = new ComboViewer(selectTableGroup, SWT.READ_ONLY);
      this.aggregationCombo.getControl().setLayoutData(gd);
      this.aggregationCombo.setContentProvider(new ArrayContentProvider());
      this.aggregationCombo.setLabelProvider(new DataSetTypeLabelProvider());

      this.schemaCombo.addSelectionChangedListener(new ISelectionChangedListener()
      {
        @Override
        public void selectionChanged(SelectionChangedEvent event)
        {
          IStructuredSelection selection = (IStructuredSelection) event.getSelection();
          LabelValuePair element = (LabelValuePair) selection.getFirstElement();

          LabelValuePair[] input = provider.getSupportedAggregation(element.getValue(), TIME_OUT_LIMIT * 1000);

          aggregationCombo.setInput(input);

          if (input.length > 0)
          {
            aggregationCombo.setSelection(new StructuredSelection(input[0]));
          }
          else
          {
            aggregationCombo.getControl().setEnabled(true);
          }
        }
      });

      Label entityLabel = new Label(selectTableGroup, SWT.LEFT);
      entityLabel.setText(GeodashboardPlugin.getResourceString("dashboardpage.label.selectentity"));

      // Create a single line text field
      this.entityCombo = new ComboViewer(selectTableGroup, SWT.READ_ONLY);
      this.entityCombo.getControl().setLayoutData(gd);
      this.entityCombo.setContentProvider(new ArrayContentProvider());
      this.entityCombo.setLabelProvider(new DataSetTypeLabelProvider());
      this.entityCombo.setInput(entities);

      if (entities.length > 0)
      {
        this.entityCombo.setSelection(new StructuredSelection(input[0]));
      }

      this.initialized = true;
    }
    catch (ConnectionException ex)
    {
      Shell shell = parent.getShell();
      String label = GeodashboardPlugin.getResourceString("dataset.error");
      String message = GeodashboardPlugin.getResourceString("dashboardpage.label.connectionerror");
//      String message = ex.getLocalizedMessage();

      ExceptionHandler.showException(shell, label, message, ex);

      Text exceptionLabel = new Text(selectTableGroup, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
      exceptionLabel.setLayoutData(new GridData(GridData.FILL_BOTH));
      exceptionLabel.setText(message);

      return null;
    }

    return selectTableGroup;
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
      // instantiate your custom ODA runtime driver class
      IDriver driver = new Driver();

      // obtain and open a live connection
      Properties props = DesignSessionUtil.getEffectiveDataSourceProperties(getInitializationDesign().getDataSourceDesign());
      connection = driver.getConnection(null);
      connection.open(props);

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
    if (this.schemaCombo != null)
    {
      String value = this.getValue();
      String aggregation = this.getAggregation();
      String defaultGeoId = this.getDefaultGeoId();

      return QueryFacadeUtil.getValuesQueryText(value, aggregation, defaultGeoId);
    }

    return null;
  }

  private String getValue()
  {
    StructuredSelection selection = (StructuredSelection) this.schemaCombo.getSelection();
    LabelValuePair datasetType = (LabelValuePair) selection.getFirstElement();

    return datasetType.getValue();
  }

  private String getAggregation()
  {
    if (this.aggregationCombo.getControl().getEnabled())
    {
      StructuredSelection selection = (StructuredSelection) this.aggregationCombo.getSelection();
      LabelValuePair datasetType = (LabelValuePair) selection.getFirstElement();

      return datasetType.getValue();
    }

    return null;
  }

  private String getDefaultGeoId()
  {
    StructuredSelection selection = (StructuredSelection) this.entityCombo.getSelection();
    LabelValuePair datasetType = (LabelValuePair) selection.getFirstElement();

    return datasetType.getValue();
  }

  private void updateFromQueryText(String queryText)
  {
    if (queryText != null && queryText.length() > 0)
    {
      this.updateComboSelection(QueryFacadeUtil.getQueryIdFromQueryText(queryText), this.schemaCombo);

      this.updateComboSelection(QueryFacadeUtil.getAggregationFromQueryText(queryText), this.aggregationCombo);

      this.updateComboSelection(QueryFacadeUtil.getGeoIdFromQueryText(queryText), this.entityCombo);
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