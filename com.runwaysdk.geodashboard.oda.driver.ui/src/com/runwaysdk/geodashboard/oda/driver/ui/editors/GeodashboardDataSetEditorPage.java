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
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import com.runwaysdk.geodashboard.oda.driver.Driver;
import com.runwaysdk.geodashboard.oda.driver.ui.GeodashboardPlugin;
import com.runwaysdk.geodashboard.oda.driver.ui.provider.DataSetType;
import com.runwaysdk.geodashboard.oda.driver.ui.provider.GeodashboardMetaDataProvider;
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
   * Combo viewer to select the query type
   */
  private ComboViewer                  schemaCombo;

  /**
   * Max depth text input. Only enabled when applicable
   */
  private ComboViewer                  depthInput;

  /**
   * Max possible depth
   */
  private int                          maxDepth;

  /**
   * Provider used to communicate with the server
   */
  private GeodashboardMetaDataProvider provider;

  /**
   * constructor
   * 
   * @param pageName
   */
  public GeodashboardDataSetEditorPage(String pageName)
  {
    super(pageName);
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

  /**
   * create page control for sql edit page
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
      Label schemaLabel = new Label(selectTableGroup, SWT.LEFT);
      schemaLabel.setText(GeodashboardPlugin.getResourceString("dashboardpage.label.selectdashboard"));

      GridData gd = new GridData(GridData.FILL_HORIZONTAL);
      gd.horizontalSpan = 2;

      this.schemaCombo = new ComboViewer(selectTableGroup, SWT.READ_ONLY);
      this.schemaCombo.getControl().setLayoutData(gd);

      Label depthLabel = new Label(selectTableGroup, SWT.LEFT);
      depthLabel.setText(GeodashboardPlugin.getResourceString("dashboardpage.label.depth"));

      // Create a single line text field
      this.depthInput = new ComboViewer(selectTableGroup, SWT.READ_ONLY);
      this.depthInput.getControl().setLayoutData(gd);
      this.depthInput.getControl().setEnabled(false);
      this.depthInput.setContentProvider(new ArrayContentProvider());
      this.depthInput.setLabelProvider(new LabelProvider()
      {
        @Override
        public String getText(Object element)
        {
          if (element != null)
          {
            return ( (Integer) element ).toString();
          }

          return "";
        }
      });

      this.schemaCombo.addSelectionChangedListener(new ISelectionChangedListener()
      {
        @Override
        public void selectionChanged(SelectionChangedEvent event)
        {
          IStructuredSelection selection = (IStructuredSelection) event.getSelection();
          DataSetType element = (DataSetType) selection.getFirstElement();

          maxDepth = element.getMaxDepth();

          if (maxDepth != 0)
          {
            Integer[] content = new Integer[maxDepth];

            for (int i = 0; i < maxDepth; i++)
            {
              content[i] = ( i + 1 );
            }

            depthInput.getControl().setEnabled(true);
            depthInput.setInput(content);
            depthInput.setSelection(new StructuredSelection(content[0]));
          }
          else
          {
            depthInput.getControl().setEnabled(false);
            depthInput.setInput(new Integer[] {});
            depthInput.setSelection(null);
          }
        }
      });

      DataSetType[] input = this.provider.getTypes(TIME_OUT_LIMIT * 1000);

      schemaCombo.setContentProvider(new ArrayContentProvider());
      schemaCombo.setLabelProvider(new DataSetTypeLabelProvider());
      schemaCombo.setInput(input);

      if (input.length > 0)
      {
        schemaCombo.setSelection(new StructuredSelection(input[0]));
      }
    }
    catch (Exception ex)
    {
      ExceptionHandler.showException(getShell(), GeodashboardPlugin.getResourceString("dataset.error"), ex.getLocalizedMessage(), ex);

      this.setPageComplete(false);
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
      String depth = this.getDepth();

      return QueryFacadeUtil.getValuesQueryText(value, depth);
    }

    return null;
  }

  private String getValue()
  {
    StructuredSelection selection = (StructuredSelection) this.schemaCombo.getSelection();
    DataSetType datasetType = (DataSetType) selection.getFirstElement();

    String value = datasetType.getId();
    return value;
  }

  private String getDepth()
  {
    if (this.depthInput.getControl().getEnabled())
    {
      StructuredSelection sel = (StructuredSelection) this.depthInput.getSelection();
      String depth = ( (Integer) sel.getFirstElement() ).toString();
      return depth;
    }

    return null;
  }

  private void updateFromQueryText(String queryText)
  {
    if (queryText != null && queryText.length() > 0)
    {
      String type = QueryFacadeUtil.getTypeFromQueryText(queryText);

      DataSetType[] values = (DataSetType[]) this.schemaCombo.getInput();

      for (DataSetType value : values)
      {
        if (value.getId().equals(type))
        {
          this.schemaCombo.setSelection(new StructuredSelection(value));
        }
      }

      Integer depth = QueryFacadeUtil.getDepthFromQueryText(queryText);

      if (depth != null)
      {
        this.depthInput.setSelection(new StructuredSelection(depth));
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