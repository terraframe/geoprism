package net.geoprism.data.etl.excel;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.system.scheduler.AllJobStatus;
import com.runwaysdk.system.scheduler.ExecutionContext;
import com.runwaysdk.system.scheduler.JobHistory;

import net.geoprism.data.etl.ImportResponseIF;
import net.geoprism.data.etl.ImportRunnable;
import net.geoprism.data.etl.SuccessResponse;

public class DataUploaderImportJob extends DataUploaderImportJobBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -450862498;
  
  public DataUploaderImportJob()
  {
    super();
  }

  private static Map<String, SharedState> sharedStates = new HashMap<String, SharedState>();

  protected SharedState                   sharedState;                                      // This
                                                                                            // state
                                                                                            // is
                                                                                            // shared
                                                                                            // across
                                                                                            // threads

  private ExecutionContext                context;

  public DataUploaderImportJob(String configuration, File file, String fileName)
  {
    super();

    this.sharedState = new SharedState();
    this.sharedState.configuration = configuration;
    this.sharedState.file = file;
    this.sharedState.fileName = fileName;
  }

  private void saveSharedState()
  {
    sharedStates.put(this.getId(), this.sharedState);
  }

  private void loadSharedState()
  {
    if (sharedStates.containsKey(this.getId()))
    {
      this.sharedState = sharedStates.get(this.getId());
    }
    else
    {
      this.sharedState = new SharedState();
    }
  }

  protected class SharedState implements com.runwaysdk.generation.loader.Reloadable
  {
    protected String    configuration;

    protected File      file;

    // protected Semaphore semaphore;

    protected Throwable sharedEx;

    protected String    responseJSON;

    protected String    fileName;
  }

  @Override
  protected JobHistory createNewHistory()
  {
    ExcelImportHistory history = new ExcelImportHistory();
    history.setStartTime(new Date());
    history.addStatus(AllJobStatus.RUNNING);
    history.setFileName(this.sharedState.fileName);
    history.apply();

    return history;
  }

  public String doImport()
  {
    this.saveSharedState();

    this.start();

    return "";
  }

  @Override
  public void execute(ExecutionContext context)
  {
    loadSharedState();
    this.context = context;

    try
    {
      executeAuthenticated();
    }
    catch (Throwable ex)
    {
      this.sharedState.sharedEx = ex;
      throw ex;
    }
    finally
    {
      // this.sharedState.semaphore.release();
      sharedStates.remove(this.getId());
    }
  }

  @Authenticate
  public void executeAuthenticated()
  {
    doInTransaction(context);
  }

  // @Transaction
  public void doInTransaction(ExecutionContext context)
  {
    JobHistoryProgressMonitor monitor = new JobHistoryProgressMonitor((ExcelImportHistory) context.getJobHistory());

    /*
     * This can cause a reload, everything after this line needs to be invoked
     * through reflection
     */
    ImportRunnable run = new ImportRunnable(this.sharedState.fileName, this.sharedState.configuration, this.sharedState.file, monitor);
    ImportResponseIF response = run.run();

    if (!(response instanceof SuccessResponse))
    {
      context.setStatus(AllJobStatus.WARNING);
    }
  }
}
