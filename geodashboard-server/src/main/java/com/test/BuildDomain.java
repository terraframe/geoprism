/**
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
package com.test;

import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.StrategyInitializer;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.scheduler.ExecutableJob;
import com.runwaysdk.system.scheduler.QualifiedTypeJob;

public class BuildDomain
{
  public static final String PACKAGE = "com.test";
  
  @Request
  public static void main(String[] args)
  {
    doIt();
  }
  
  @Transaction
  public static void doIt() {
    StrategyInitializer.startUp();
    
    QualifiedTypeJob job = new QualifiedTypeJob();
    job.setClassName(WaitSecondJob.class.getName());
    job.setJobId("Play With Fido");
    job.setStructValue(ExecutableJob.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Shoots a disc out of the cd tray for fido to go chase. Takes 4 seconds to complete.");
    job.setWorkTotal(4);
    job.setWorkProgress(0);
    job.setPauseable(true);
    job.setCancelable(true);
    job.apply();
    
    job = new QualifiedTypeJob();
    job.setClassName(WaitSecondJob.class.getName());
    job.setJobId("Bake Cookies");
    job.setStructValue(ExecutableJob.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Tells the computer to begin a delicious batch of chocolate chip cookies immediately. Takes 8 seconds to complete.");
    job.setWorkTotal(8);
    job.setWorkProgress(0);
    job.setPauseable(true);
    job.setCancelable(true);
    job.apply();
    
    job = new QualifiedTypeJob();
    job.setClassName(WaitSecondJob.class.getName());
    job.setJobId("Clean House");
    job.setStructValue(ExecutableJob.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Turns on the Roomba and waits for its completion. Takes 16 seconds to complete.");
    job.setWorkTotal(16);
    job.setWorkProgress(0);
    job.setPauseable(true);
    job.setCancelable(true);
    job.apply();
  }
}
