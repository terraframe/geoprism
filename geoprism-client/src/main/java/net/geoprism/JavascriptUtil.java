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
package net.geoprism;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.geoprism.dashboard.DashboardController;
import net.geoprism.dashboard.DashboardDTO;
import net.geoprism.dashboard.DashboardDisplayLabelDTO;
import net.geoprism.DataUploaderController;
import net.geoprism.EmailSettingDTO;
import net.geoprism.GeoEntityUtilDTO;
import net.geoprism.GeoprismUserDTO;
import net.geoprism.MappableClassDTO;
import net.geoprism.RoleViewDTO;
import net.geoprism.dashboard.AggregationStrategyDTO;
import net.geoprism.dashboard.AggregationStrategyViewDTO;
import net.geoprism.dashboard.AggregationTypeDTO;
import net.geoprism.dashboard.AllAggregationTypeDTO;
import net.geoprism.dashboard.DashboardMapController;
import net.geoprism.dashboard.DashboardMapDTO;
import net.geoprism.dashboard.DashboardStyleDTO;
import net.geoprism.dashboard.DashboardThematicStyleDTO;
import net.geoprism.dashboard.GeometryAggregationStrategyDTO;
import net.geoprism.dashboard.UniversalAggregationStrategyDTO;
import net.geoprism.dashboard.layer.DashboardLayerController;
import net.geoprism.dashboard.layer.DashboardLayerDTO;
import net.geoprism.dashboard.layer.DashboardLayerViewDTO;
import net.geoprism.dashboard.layer.DashboardReferenceLayerController;
import net.geoprism.dashboard.layer.DashboardReferenceLayerDTO;
import net.geoprism.dashboard.layer.DashboardReferenceLayerViewDTO;
import net.geoprism.dashboard.layer.DashboardThematicLayerController;
import net.geoprism.dashboard.layer.DashboardThematicLayerDTO;
import net.geoprism.data.browser.DataBrowserUtilDTO;
import net.geoprism.data.browser.MetadataTypeDTO;
import net.geoprism.ontology.ClassifierController;
import net.geoprism.ontology.ClassifierDTO;
import net.geoprism.ontology.ClassifierDisplayLabelDTO;
import net.geoprism.ontology.ClassifierExportMenuDTO;
import net.geoprism.ontology.ClassifierIsARelationshipDTO;
import net.geoprism.ontology.GeoEntityExportMenuDTO;
import net.geoprism.ontology.UniversalExportMenuDTO;
import net.geoprism.report.PairViewDTO;
import net.geoprism.report.ReportItemController;
import net.geoprism.report.ReportItemDTO;
import net.geoprism.report.ReportItemViewDTO;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.generation.loader.Reloadable;
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
    types.add(GeoprismUserDTO.CLASS);
    types.add(DashboardDTO.CLASS);
    types.add(DashboardDisplayLabelDTO.CLASS);   
    types.add(DashboardController.CLASS);
    types.add(DataUploaderController.CLASS);

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
    types.add(GeoprismUserDTO.CLASS);

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
    types.add(DashboardDTO.CLASS);
    types.add(DashboardDisplayLabelDTO.CLASS);
    types.add(DashboardMapDTO.CLASS);
    types.add(DashboardLayerDTO.CLASS);
    types.add(DashboardLayerViewDTO.CLASS);
    types.add(DashboardLayerController.CLASS);
    types.add(DashboardThematicLayerDTO.CLASS);
    types.add(DashboardStyleDTO.CLASS);
    types.add(DashboardThematicStyleDTO.CLASS);
    types.add(DashboardThematicLayerController.CLASS);
    types.add(DashboardReferenceLayerDTO.CLASS);
    types.add(DashboardReferenceLayerViewDTO.CLASS);
    types.add(DashboardReferenceLayerController.CLASS);
    types.add(DashboardController.CLASS);
    types.add(DashboardMapController.CLASS);
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
    types.add(MappableClassDTO.CLASS);
    types.add(AllAggregationTypeDTO.CLASS);
    types.add(AggregationTypeDTO.CLASS);
    types.add(DataUploaderController.CLASS);

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
    types.add(DashboardController.CLASS);
    types.add(DataUploaderController.CLASS);

    JavascriptUtil.loadJavascript(request, req, types);
  }
}
