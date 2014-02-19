package com.test;

import com.runwaysdk.session.Request;
import com.runwaysdk.system.scheduler.ExecutableJob;
import com.runwaysdk.system.scheduler.ExecutableJobIF;
import com.runwaysdk.system.scheduler.ExecutionContext;

public class WaitSecondJob implements ExecutableJobIF
{
  public WaitSecondJob()
  {
    
  }
  
  @Override
  @Request
  public void execute(ExecutionContext executionContext)
  {
    ExecutableJob job = ExecutableJob.get(executionContext.getJob().getId());
    job.lock();
    job.setRunning(true);
    job.setCompleted(false);
    job.setCanceled(false);
    job.setPaused(false);
    job.setWorkProgress(0);
    job.apply();
    job.unlock();
    
    if (job.getJobId().equals("Play With Fido")) {
      executionContext.getJobHistory().getHistoryInformation().setValue("Fido broke the cd.");
    }
    else if (job.getJobId().equals("Bake Cookies")) {
      executionContext.getJobHistory().getHistoryInformation().setValue("4 cookies accidentally got burnt.");
    }
    else if (job.getJobId().equals("Clean House")) {
      executionContext.getJobHistory().getHistoryInformation().setValue("The Roomba fell down the stairs, flipped over, and stopped cleaning.");
    }
    
    try
    {
      while (job.getCompleted() == false && (job.getRunning() == true || job.getPaused() == true)) {
        job = ExecutableJob.get(job.getId());
        synchronized(job) {
          if (job.getCanceled() == true) {
            job.lock();
            job.setRunning(false);
            job.apply();
            job.unlock();
            return;
          }
        }
        
        synchronized(this) {
          this.wait(1000);
        }
        
        job = ExecutableJob.get(job.getId());
        synchronized(job) {
          if (job.getPaused() == false) {
            job.lock();
            job.setWorkProgress(job.getWorkProgress() + 1);
            
            if (job.getWorkProgress() >= job.getWorkTotal()) {
              job.setCompleted(true);
              job.setRunning(false);
              job.setWorkProgress(job.getWorkTotal());
            }
            
            job.apply();
            job.unlock();
          }
        }
      }
    }
    catch (InterruptedException e)
    {
      throw new RuntimeException(e);
    }
  }
}
