package com.runwaysdk.geodashboard.oda.driver.ui.profile;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.datatools.connectivity.internal.ui.dialogs.ExceptionHandler;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.runwaysdk.geodashboard.oda.driver.CryptographySingleton;
import com.runwaysdk.geodashboard.oda.driver.ui.GeodashboardPlugin;
import com.runwaysdk.geodashboard.oda.driver.ui.util.Constants;
import com.runwaysdk.geodashboard.oda.driver.ui.util.DriverLoader;

public class GeodashboardSelectionPageHelper implements ModifyListener
{
  // constant string
  final private static String EMPTY_URL          = GeodashboardPlugin.getResourceString("error.emptyURL");     //$NON-NLS-1$

  // constant string
  final private static String EMPTY_USERNAME     = GeodashboardPlugin.getResourceString("error.emptyUsername"); //$NON-NLS-1$

  // constant string
  final private static String EMPTY_PASSWORD     = GeodashboardPlugin.getResourceString("error.emptyPassword"); //$NON-NLS-1$

  private static final String EMPTY_STRING       = "";                                                         //$NON-NLS-1$

  private WizardPage          m_wizardPage;

  private PreferencePage      m_propertyPage;

  // Text of url, name and password
  private Text                url, userName, password;

  private Composite           porpertyGroupComposite;

  // Button of test connection
  private Button              testButton;

  private String              DEFAULT_MESSAGE;

  private Map<String, String> databaseProperties = new HashMap<String, String>();

  public GeodashboardSelectionPageHelper(WizardPage page)
  {
    DEFAULT_MESSAGE = GeodashboardPlugin.getResourceString("wizard.message.createDataSource"); //$NON-NLS-1$
    m_wizardPage = page;
  }

  public GeodashboardSelectionPageHelper(PreferencePage page)
  {
    DEFAULT_MESSAGE = GeodashboardPlugin.getResourceString("wizard.message.createDataSource"); //$NON-NLS-1$
    m_propertyPage = page;
  }

  public Composite createCustomControl(Composite parent)
  {
    ScrolledComposite scrollContent = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);

    scrollContent.setAlwaysShowScrollBars(false);
    scrollContent.setExpandHorizontal(true);

    scrollContent.setLayout(new FillLayout());

    // create the composite to hold the widgets
    Composite content = new Composite(scrollContent, SWT.NONE);

    GridLayout layout = new GridLayout();
    layout.numColumns = 4;
    layout.verticalSpacing = 10;
    layout.marginBottom = 300;
    content.setLayout(layout);

    GridData gridData;

    // initialize Database URL editor
    new Label(content, SWT.RIGHT).setText(GeodashboardPlugin.getResourceString("wizard.label.url"));//$NON-NLS-1$

    url = new Text(content, SWT.BORDER);
    gridData = new GridData();
    gridData.horizontalSpan = 3; // bidi_hcg
    gridData.horizontalAlignment = SWT.FILL;
    gridData.grabExcessHorizontalSpace = true;
    url.setLayoutData(gridData);

    // User Name
    new Label(content, SWT.RIGHT).setText(GeodashboardPlugin.getResourceString("wizard.label.username"));//$NON-NLS-1$
    userName = new Text(content, SWT.BORDER);
    gridData = new GridData();
    gridData.horizontalSpan = 3; // bidi_hcg
    gridData.horizontalAlignment = SWT.FILL;
    userName.setLayoutData(gridData);

    // Password
    new Label(content, SWT.RIGHT).setText(GeodashboardPlugin.getResourceString("wizard.label.password"));//$NON-NLS-1$
    password = new Text(content, SWT.BORDER | SWT.PASSWORD);
    gridData = new GridData();
    gridData.horizontalSpan = 3; // bidi_hcg
    gridData.horizontalAlignment = SWT.FILL;
    password.setLayoutData(gridData);

    createPropertiesComposite(content);

    testButton = new Button(content, SWT.PUSH);
    testButton.setText(GeodashboardPlugin.getResourceString("wizard.label.testConnection"));//$NON-NLS-1$
    testButton.setLayoutData(new GridData(GridData.CENTER));

    Point size = content.computeSize(SWT.DEFAULT, SWT.DEFAULT);
    content.setSize(size.x, size.y);

    scrollContent.setMinWidth(size.x + 10);

    scrollContent.setContent(content);

    addControlListeners();
    updateTestButton();
    verifyConnectionProperties();

    return content;
  }

  private void createPropertiesComposite(Composite content)
  {
    GridData gridData;

    porpertyGroupComposite = new Composite(content, SWT.NONE);

    gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
    gridData.horizontalSpan = 4;
    gridData.horizontalAlignment = SWT.FILL;
    gridData.exclude = true;
    porpertyGroupComposite.setLayoutData(gridData);
    GridLayout layout = new GridLayout();
    // layout.horizontalSpacing = layout.verticalSpacing = 0;
    layout.marginWidth = layout.marginHeight = 0;
    layout.numColumns = 5;
    Layout parentLayout = porpertyGroupComposite.getParent().getLayout();

    if (parentLayout instanceof GridLayout)
    {
      layout.horizontalSpacing = ( (GridLayout) parentLayout ).horizontalSpacing;
    }

    porpertyGroupComposite.setLayout(layout);
  }

  /**
   * populate properties
   * 
   * @param profileProps
   */
  void initCustomControl(Properties profileProps)
  {
    if (profileProps != null && !profileProps.isEmpty())
    {
      String odaUrl = profileProps.getProperty(Constants.ODAURL);

      if (odaUrl == null)
      {
        odaUrl = "//localhost:1199/";
      }

      url.setText(odaUrl);

      String odaUser = profileProps.getProperty(Constants.ODAUser);
      if (odaUser == null)
      {
        odaUrl = "admin";
      }
      userName.setText(odaUser);

      String odaPassword = profileProps.getProperty(Constants.ODAPassword);
      if (odaPassword != null && odaPassword.length() > 0)
      {
        String decrypted = CryptographySingleton.decrypt(odaPassword);

        password.setText(decrypted);
      }
      else
      {
        password.setText(EMPTY_STRING);
      }
    }
    else
    {
      url.setText("//localhost:1199/");
      userName.setText("admin");
      password.setText(EMPTY_STRING);
    }

    updateTestButton();
    verifyConnectionProperties();
  }

  /**
   * collection all custom properties
   * 
   * @param props
   * @return
   */
  Properties collectCustomProperties(Properties props)
  {
    if (props == null)
    {
      props = new Properties();
    }

    // set custom driver specific properties
    props.setProperty(Constants.ODAURL, this.getDriverURL());
    props.setProperty(Constants.ODAUser, this.getODAUser());
    props.setProperty(Constants.ODAPassword, this.getEncryptedODAPassword());
    props.putAll(databaseProperties);

    return props;
  }

  /**
   * get user name
   * 
   * @return
   */
  private String getODAUser()
  {
    if (userName == null)
    {
      return EMPTY_STRING;
    }

    return getTrimedString(userName.getText());
  }

  private String getEncryptedODAPassword()
  {
    String password = this.getODAPassword();
    String encrypted = CryptographySingleton.encrypt(password);

    return encrypted;
  }

  /**
   * get password
   * 
   * @return
   */
  private String getODAPassword()
  {
    if (password == null)
    {
      return EMPTY_STRING;
    }

    return getTrimedString(password.getText());
  }

  /**
   * get driver url
   * 
   * @return
   */
  private String getDriverURL()
  {
    if (url == null)
    {
      return EMPTY_STRING;
    }

    return getTrimedString(url.getText());
  }

  /**
   * 
   * @param tobeTrimed
   * @return
   */
  private String getTrimedString(String tobeTrimed)
  {
    if (tobeTrimed != null)
    {
      tobeTrimed = tobeTrimed.trim();
    }

    return tobeTrimed;
  }

  /**
   * Adds event listeners
   */
  private void addControlListeners()
  {
    userName.addModifyListener(this);
    password.addModifyListener(this);
    url.addModifyListener(this);

    testButton.addSelectionListener(new SelectionAdapter()
    {

      public void widgetSelected(SelectionEvent evt)
      {
        handleTestButtonSelection();
      }

    });
  }

  private Properties collectSpecifiedProperties()
  {
    Properties props = new Properties();

    for (String o : databaseProperties.keySet())
    {
      if (databaseProperties.get(o) != null && databaseProperties.get(o).trim().length() > 0)
      {
        props.setProperty(o, databaseProperties.get(o));
      }
    }
    return props;
  }

  /**
   * Validates the data source and updates the window message accordingly
   * 
   * @return
   */
  private boolean isValidDataSource()
  {
    Text[] elements = { userName, password, url };
    boolean valid = true;

    for (Text element : elements)
    {
      valid = valid && ! ( this.isBlank(element) );
    }

    return valid;
  }

  private boolean isBlank(Text element)
  {
    return element == null || element.getText().trim().length() == 0;
  }

  /**
   * This method should be called in the following occations: 1. The value of inputed URL, username is changed 2. When
   * the control is created.
   */
  private void updateTestButton()
  {
    // Geodashboard Url cannot be blank
    if (this.isBlank(this.url))
    {
      setMessage(EMPTY_URL, IMessageProvider.ERROR);
      testButton.setEnabled(false);
    }
    else if (this.isBlank(this.userName))
    {
      setMessage(EMPTY_USERNAME, IMessageProvider.ERROR);
      testButton.setEnabled(false);
    }
    else if (this.isBlank(this.password))
    {
      setMessage(EMPTY_PASSWORD, IMessageProvider.ERROR);
      testButton.setEnabled(false);
    }
    else
    {
      setMessage(DEFAULT_MESSAGE);

      if (!testButton.isEnabled())
      {
        testButton.setEnabled(true);
      }
    }
  }

  /**
   * Reset the testButton and manageButton to "enabled" status
   */
  protected void resetTestAndMngButton()
  {
    updateTestButton();
  }

  private void verifyConnectionProperties()
  {
    setPageComplete(this.isValidDataSource());
  }

  /**
   * get the Shell from DialogPage
   * 
   * @return
   */
  private Shell getShell()
  {
    if (m_wizardPage != null)
    {
      return m_wizardPage.getShell();
    }
    else if (m_propertyPage != null)
    {
      return m_propertyPage.getShell();
    }
    else
    {
      return null;
    }
  }

  /**
   * set page complete
   * 
   * @param complete
   */
  private void setPageComplete(boolean complete)
  {
    if (m_wizardPage != null)
    {
      m_wizardPage.setPageComplete(complete);
    }
    else if (m_propertyPage != null)
    {
      m_propertyPage.setValid(complete);
    }
  }

  /**
   * set message
   * 
   * @param message
   */
  private void setMessage(String message)
  {
    if (m_wizardPage != null)
    {
      m_wizardPage.setMessage(message);
    }
    else if (m_propertyPage != null)
    {
      m_propertyPage.setMessage(message);
    }
  }

  /**
   * set message
   * 
   * @param message
   * @param type
   */
  private void setMessage(String message, int type)
  {
    if (m_wizardPage != null)
    {
      m_wizardPage.setMessage(message, type);
    }
    else if (m_propertyPage != null)
    {
      m_propertyPage.setMessage(message, type);
    }
  }

  public void setDefaultMessage(String message)
  {
    this.DEFAULT_MESSAGE = message;
  }

  private void handleTestButtonSelection()
  {
    this.testButton.setEnabled(false);

    try
    {
      if (!isValidDataSource())
      {
        throw new OdaException(GeodashboardPlugin.getResourceString("dataset.error"));
      }

      final String connectionUrl = url.getText().trim();
      final String userid = userName.getText().trim();
      final String passwd = CryptographySingleton.encrypt(password.getText());

      new ProgressMonitorDialog(getShell()).run(true, false, new IRunnableWithProgress()
      {

        @Override
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          monitor.beginTask(GeodashboardPlugin.getResourceString("connection.testing"), IProgressMonitor.UNKNOWN);

          try
          {
            DriverLoader.testConnection(connectionUrl, userid, passwd, collectSpecifiedProperties());
          }
          catch (OdaException e)
          {
            ExceptionHandler.showException(getShell(), GeodashboardPlugin.getResourceString("connection.test"),//$NON-NLS-1$
                GeodashboardPlugin.getResourceString("connection.failed"), e);
          }
        }
      });

      MessageDialog.openInformation(getShell(), GeodashboardPlugin.getResourceString("connection.test"),//$NON-NLS-1$
          GeodashboardPlugin.getResourceString("connection.success"));//$NON-NLS-1$

    }
    catch (Exception e)
    {
      OdaException ex = new OdaException(GeodashboardPlugin.getResourceString("connection.failed"));
      ExceptionHandler.showException(getShell(), GeodashboardPlugin.getResourceString("connection.test"),//$NON-NLS-1$
          GeodashboardPlugin.getResourceString("connection.failed"), ex);
    }

    this.testButton.setEnabled(true);
  }

  public void modifyText(ModifyEvent e)
  {
    updateTestButton();
    verifyConnectionProperties();
  }
}
