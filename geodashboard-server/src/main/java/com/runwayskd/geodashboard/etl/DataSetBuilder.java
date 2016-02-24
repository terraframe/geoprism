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
package com.runwayskd.geodashboard.etl;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.AbortIfProblem;
import com.runwaysdk.dataaccess.transaction.Transaction;

public class DataSetBuilder implements DataSetBuilderIF
{
  private JSONObject    configuration;

  private SourceContext source;

  private TargetContext target;

  public DataSetBuilder(String _configuration)
  {
    try
    {
      this.configuration = new JSONObject(_configuration);

      this.source = new SourceContext();
      this.target = new TargetContext();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  @Transaction
  @AbortIfProblem
  public void build()
  {
    new SourceBuilder(this.configuration, this.source).build();

    new TargetBuilder(this.configuration, this.source, this.target).build();

    // TODO Persist the mapping configuration
  }

  @Override
  public SourceContextIF getSourceContext()
  {
    return this.source;
  }

  @Override
  public TargetContextIF getTargetContext()
  {
    return this.target;
  }
}
