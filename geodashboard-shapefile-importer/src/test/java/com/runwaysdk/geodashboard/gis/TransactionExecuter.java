package com.runwaysdk.geodashboard.gis;

import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

public abstract class TransactionExecuter
{
  protected abstract void executeMethod() throws Exception;

  @Request
  public void execute() throws Exception
  {
    this.executeInTransaction();
  }

  @Transaction
  private void executeInTransaction() throws Exception
  {
    this.executeMethod();
  }
}
