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
package com.runwaysdk.geodashboard;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.databrowser.DataBrowserUtilDTO;
import com.runwaysdk.geodashboard.databrowser.MetadataTypeDTO;
import com.runwaysdk.geodashboard.gis.ClassifierExportMenuDTO;
import com.runwaysdk.geodashboard.gis.GeoEntityExportMenuDTO;
import com.runwaysdk.geodashboard.gis.UniversalExportMenuDTO;
import com.runwaysdk.geodashboard.gis.persist.AggregationStrategyDTO;
import com.runwaysdk.geodashboard.gis.persist.AggregationStrategyViewDTO;
import com.runwaysdk.geodashboard.gis.persist.DashboardLayerController;
import com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO;
import com.runwaysdk.geodashboard.gis.persist.DashboardLayerViewDTO;
import com.runwaysdk.geodashboard.gis.persist.DashboardMapController;
import com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO;
import com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerController;
import com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerDTO;
import com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerViewDTO;
import com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerController;
import com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerDTO;
import com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategyDTO;
import com.runwaysdk.geodashboard.gis.persist.UniversalAggregationStrategyDTO;
import com.runwaysdk.geodashboard.gis.persist.condition.ClassifierConditionDTO;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqualDTO;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThanDTO;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThanOrEqualDTO;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardLessThanDTO;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardLessThanOrEqualDTO;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardNotEqualDTO;
import com.runwaysdk.geodashboard.gis.persist.condition.LocationConditionDTO;
import com.runwaysdk.geodashboard.ontology.ClassifierController;
import com.runwaysdk.geodashboard.ontology.ClassifierDTO;
import com.runwaysdk.geodashboard.ontology.ClassifierDisplayLabelDTO;
import com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO;
import com.runwaysdk.geodashboard.report.PairViewDTO;
import com.runwaysdk.geodashboard.report.ReportItemController;
import com.runwaysdk.geodashboard.report.ReportItemDTO;
import com.runwaysdk.geodashboard.report.ReportItemViewDTO;
import com.runwaysdk.system.RolesDTO;
import com.runwaysdk.system.gis.geo.AllowedInDTO;
import com.runwaysdk.system.gis.geo.GeoEntityController;
import com.runwaysdk.system.gis.geo.GeoEntityDTO;
import com.runwaysdk.system.gis.geo.GeoEntityDisplayLabelDTO;
import com.runwaysdk.system.gis.geo.GeoEntityProblemDTO;
import com.runwaysdk.system.gis.geo.GeoEntityProblemViewDTO;
import com.runwaysdk.system.gis.geo.GeoEntityViewDTO;
import com.runwaysdk.system.gis.geo.GeoNodeDTO;
import com.runwaysdk.system.gis.geo.GeoNodeEntityDTO;
import com.runwaysdk.system.gis.geo.GeoNodeGeometryDTO;
import com.runwaysdk.system.gis.geo.IsARelationshipDTO;
import com.runwaysdk.system.gis.geo.LocatedInDTO;
import com.runwaysdk.system.gis.geo.SynonymDTO;
import com.runwaysdk.system.gis.geo.SynonymDisplayLabelDTO;
import com.runwaysdk.system.gis.geo.UniversalDTO;
import com.runwaysdk.system.gis.geo.UniversalDisplayLabelDTO;
import com.runwaysdk.system.ontology.TermUtilDTO;
import com.runwaysdk.system.scheduler.ExecutableJobDTO;
import com.runwaysdk.system.scheduler.ExecutableJobDescriptionDTO;
import com.runwaysdk.system.scheduler.JobHistoryDTO;
import com.runwaysdk.system.scheduler.JobHistoryHistoryInformationController;
import com.runwaysdk.system.scheduler.JobHistoryViewDTO;
import com.runwaysdk.system.scheduler.JobSnapshotDTO;
import com.runwaysdk.system.scheduler.QualifiedTypeJobDTO;
import com.runwaysdk.web.json.JSONController;

public class JavascriptUtil implements Reloadable
{
  public static String getJavascript(ClientRequestIF clientRequest, String... types)
  {
    String js = JSONController.importTypes(clientRequest.getSessionId(), types, true);

    return js;
  }

  private static void loadJavascript(ClientRequestIF request, HttpServletRequest req, Set<String> set)
  {
    String[] types = set.toArray(new String[set.size()]);
    String javascript = JavascriptUtil.getJavascript(request, types);

    req.setAttribute("js", javascript);
  }

  public static void loadGeoEntityBundle(ClientRequestIF request, HttpServletRequest req)
  {
    Set<String> types = new HashSet<String>();
    types.add(GeoEntityDTO.CLASS);
    types.add(LocatedInDTO.CLASS);
    types.add(GeoEntityDisplayLabelDTO.CLASS);
    types.add(GeoEntityController.CLASS);
    types.add(UniversalDTO.CLASS);
    types.add(UniversalDisplayLabelDTO.CLASS);
    types.add(TermUtilDTO.CLASS);
    types.add(GeoEntityViewDTO.CLASS);
    types.add(SynonymDTO.CLASS);
    types.add(SynonymDisplayLabelDTO.CLASS);
    types.add(GeoEntityExportMenuDTO.CLASS);
    types.add(GeoEntityUtilDTO.CLASS);
    types.add(GeoEntityProblemViewDTO.CLASS);
    types.add(GeoEntityProblemDTO.CLASS);

    JavascriptUtil.loadJavascript(request, req, types);
  }

  public static void loadSchedulerBundle(ClientRequestIF request, HttpServletRequest req)
  {
    Set<String> types = new HashSet<String>();
    types.add(ExecutableJobDTO.CLASS);
    types.add(ExecutableJobDescriptionDTO.CLASS);
    types.add(QualifiedTypeJobDTO.CLASS);
    types.add(JobHistoryDTO.CLASS);
    types.add(JobSnapshotDTO.CLASS);
    types.add(JobHistoryViewDTO.CLASS);
    types.add(JobHistoryHistoryInformationController.CLASS);

    JavascriptUtil.loadJavascript(request, req, types);
  }

  public static void loadUserBundle(ClientRequestIF request, HttpServletRequest req)
  {
    Set<String> types = new HashSet<String>();
    types.add(RolesDTO.CLASS);
    types.add(RoleViewDTO.CLASS);
    types.add(GeodashboardUserDTO.CLASS);

    JavascriptUtil.loadJavascript(request, req, types);
  }

  public static void loadDatabrowserBundle(ClientRequestIF request, HttpServletRequest req)
  {
    Set<String> types = new HashSet<String>();
    types.add(DataBrowserUtilDTO.CLASS);
    types.add(MetadataTypeDTO.CLASS);
    types.add(PairViewDTO.CLASS);
    types.add(ReportItemDTO.CLASS);

    JavascriptUtil.loadJavascript(request, req, types);
  }

  public static void loadSystemBundle(ClientRequestIF request, HttpServletRequest req)
  {
    Set<String> types = new HashSet<String>();
    types.add(EmailSettingDTO.CLASS);
    types.add(RolesDTO.CLASS);
    types.add(RoleViewDTO.CLASS);
    types.add(GeodashboardUserDTO.CLASS);

    JavascriptUtil.loadJavascript(request, req, types);
  }

  public static void loadUniversalBundle(ClientRequestIF request, HttpServletRequest req)
  {
    Set<String> types = new HashSet<String>();
    types.add(UniversalDTO.CLASS);
    types.add(AllowedInDTO.CLASS);
    types.add(UniversalDisplayLabelDTO.CLASS);
    types.add(GeoEntityDTO.CLASS);
    types.add(IsARelationshipDTO.CLASS);
    types.add(TermUtilDTO.CLASS);
    types.add(UniversalExportMenuDTO.CLASS);

    JavascriptUtil.loadJavascript(request, req, types);
  }

  public static void loadOntologyBundle(ClientRequestIF request, HttpServletRequest req)
  {
    Set<String> types = JavascriptUtil.getOntologyTypes();

    JavascriptUtil.loadJavascript(request, req, types);
  }

  private static Set<String> getOntologyTypes()
  {
    Set<String> types = new HashSet<String>();
    types.add(ClassifierDTO.CLASS);
    types.add(ClassifierIsARelationshipDTO.CLASS);
    types.add(ClassifierDisplayLabelDTO.CLASS);
    types.add(ClassifierController.CLASS);
    types.add(TermUtilDTO.CLASS);
    types.add(ClassifierExportMenuDTO.CLASS);

    return types;
  }

  public static void loadDynamicMapBundle(ClientRequestIF request, HttpServletRequest req)
  {
    Set<String> types = new HashSet<String>();
    types.add(DashboardMapDTO.CLASS);
    types.add(DashboardLayerDTO.CLASS);
    types.add(DashboardLayerViewDTO.CLASS);
    types.add(DashboardLayerController.CLASS);
    types.add(DashboardThematicLayerDTO.CLASS);
    types.add(DashboardThematicLayerController.CLASS);
    types.add(DashboardReferenceLayerDTO.CLASS);
    types.add(DashboardReferenceLayerViewDTO.CLASS);
    types.add(DashboardReferenceLayerController.CLASS);
    types.add(DashboardGreaterThanDTO.CLASS);
    types.add(DashboardGreaterThanOrEqualDTO.CLASS);
    types.add(DashboardLessThanDTO.CLASS);
    types.add(DashboardLessThanOrEqualDTO.CLASS);
    types.add(DashboardEqualDTO.CLASS);
    types.add(DashboardNotEqualDTO.CLASS);
    types.add(ClassifierConditionDTO.CLASS);
    types.add(DashboardController.CLASS);
    types.add(DashboardMapController.CLASS);
    types.add(DashboardDTO.CLASS);
    types.add(LocationConditionDTO.CLASS);
    types.add(ReportItemController.CLASS);
    types.add(ReportItemDTO.CLASS);
    types.add(ReportItemViewDTO.CLASS);
    types.add(GeoNodeDTO.CLASS);
    types.add(GeoNodeEntityDTO.CLASS);
    types.add(GeoNodeGeometryDTO.CLASS);
    types.add(AggregationStrategyDTO.CLASS);
    types.add(UniversalAggregationStrategyDTO.CLASS);
    types.add(GeometryAggregationStrategyDTO.CLASS);
    types.add(AggregationStrategyViewDTO.CLASS);
    types.addAll(JavascriptUtil.getOntologyTypes());
    types.add(GeoNodeDTO.CLASS);
    types.add(AggregationStrategyViewDTO.CLASS);
    types.add(GeoEntityUtilDTO.CLASS);

    JavascriptUtil.loadJavascript(request, req, types);
  }

  public static void loadDashboardBundle(ClientRequestIF request, HttpServletRequest req)
  {
    Set<String> types = new HashSet<String>();
    types.add(DashboardDTO.CLASS);

    JavascriptUtil.loadJavascript(request, req, types);
  }

  public static void loadBuilderBundle(ClientRequestIF request, HttpServletRequest req)
  {
    Set<String> types = new HashSet<String>();
    types.add(DashboardDTO.CLASS);
    types.add(DashboardDisplayLabelDTO.CLASS);
    types.add(MappableClassDTO.CLASS);

    JavascriptUtil.loadJavascript(request, req, types);
  }
}
