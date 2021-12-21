/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.report;

import java.util.List;

import org.json.JSONException;

import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoNode;

public interface ReportProviderIF 
{
  /**
   * Dashboard name
   */
  public static final String DASHBOARD_NAME = "dashboardName";

  /**
   * Dashboard oid
   */
  public static final String DASHBOARD_ID   = "dashboardId";

  /**
   * A list of oid-label pairing for all of the report queries supported by this provider
   * 
   * @return
   */
  public List<PairView> getSupportedQueryDescriptors();

  /**
   * If the ReportProvider supports the given query
   * 
   * @param queryId
   * @return
   */
  public boolean hasSupport(String queryId);

  /**
   * List of all of the aggregations supported by the given query
   * 
   * @param queryId
   * @return
   */
  public PairView[] getSupportedAggregation(String queryId);

  /**
   * List of all of the geo nodes supported by the given query
   * 
   * @param queryId
   * @return
   */
  public List<GeoNode> getSupportedGeoNodes(String queryId);

  /**
   * Returns a ValueQuery containing all of the results for the given query based upon the context in which the query is
   * run.
   * 
   * @param queryId
   * @param context
   * @return
   * @throws JSONException
   */
  public ValueQuery getReportQuery(String queryId, String context) throws JSONException;

}
