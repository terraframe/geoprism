/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.registry.lpg.adapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import net.geoprism.registry.lpg.adapter.exception.BadServerUriException;
import net.geoprism.registry.lpg.adapter.exception.HTTPException;
import net.geoprism.registry.lpg.adapter.exception.IncompatibleServerVersionException;
import net.geoprism.registry.lpg.adapter.exception.UnexpectedResponseException;
import net.geoprism.registry.lpg.adapter.response.RegistryResponse;

public class RegistryBridge
{
  public static final String  ORG_API_PATH = "organization";

  public static final String  LPG_API_PATH = "labeled-property-graph-type";

  private String              versionRemoteServer;

  private Integer             versionRemoteServerApi;

  private Integer             versionApiCompat;

  private RegistryConnectorIF connector;

  public RegistryBridge(RegistryConnectorIF connector)
  {
    this.connector = connector;
    this.versionApiCompat = null;
  }

  public RegistryBridge(RegistryConnectorIF connector, Integer apiVersion)
  {
    this.connector = connector;
    this.versionApiCompat = apiVersion;
  }

  public void initialize() throws UnexpectedResponseException, HTTPException, IncompatibleServerVersionException, BadServerUriException
  {
    // fetchVersionRemoteServer();
    //
    // validateApiCompatVersion(this.versionApiCompat);
  }

  public RegistryResponse getOrganizations() throws HTTPException, BadServerUriException
  {
    return this.apiGet(ORG_API_PATH + "/export-tree");
  }
  
  public RegistryResponse getTypes() throws HTTPException, BadServerUriException
  {
    return this.apiGet(LPG_API_PATH + "/get-all");
  }

  public RegistryResponse getType(String oid) throws HTTPException, BadServerUriException
  {
    return this.apiGet(LPG_API_PATH + "/get", new BasicNameValuePair("oid", oid));
  }

  public RegistryResponse getEntry(String oid) throws HTTPException, BadServerUriException
  {
    return this.apiGet(LPG_API_PATH + "/entry", new BasicNameValuePair("oid", oid));
  }

  public RegistryResponse getEntries(String oid) throws HTTPException, BadServerUriException
  {
    return this.apiGet(LPG_API_PATH + "/entries", new BasicNameValuePair("oid", oid));
  }

  public RegistryResponse getVersions(String oid) throws HTTPException, BadServerUriException
  {
    return this.apiGet(LPG_API_PATH + "/versions", new BasicNameValuePair("oid", oid));
  }

  public RegistryResponse getVersion(String oid) throws HTTPException, BadServerUriException
  {
    return this.apiGet(LPG_API_PATH + "/version", new BasicNameValuePair("oid", oid), new BasicNameValuePair("includeTableDefinitions", "true"));
  }

  public RegistryResponse getData(String oid) throws HTTPException, BadServerUriException
  {
    return this.apiGet(LPG_API_PATH + "/data", new BasicNameValuePair("oid", oid), new BasicNameValuePair("includeTableDefinitions", "true"));
  }

  public RegistryResponse getGeoObjects(String oid, Long skip, Integer blockSize) throws HTTPException, BadServerUriException
  {
    return this.apiGet(LPG_API_PATH + "/geo-objects", new BasicNameValuePair("oid", oid), new BasicNameValuePair("skip", skip.toString()), new BasicNameValuePair("blockSize", blockSize.toString()));
  }

  public RegistryResponse getEdges(String oid, Long skip, Integer blockSize) throws HTTPException, BadServerUriException
  {
    return this.apiGet(LPG_API_PATH + "/edges", new BasicNameValuePair("oid", oid), new BasicNameValuePair("skip", skip.toString()), new BasicNameValuePair("blockSize", blockSize.toString()));
  }

  public String getRemoteServerUrl()
  {
    return this.connector.getServerUrl();
  }

  private String buildApiEndpoint()
  {
    return "api/";
  }

  public String getVersionRemoteServer()
  {
    return versionRemoteServer;
  }

  public void setVersionRemoteServer(String versionRemoteServer)
  {
    this.versionRemoteServer = versionRemoteServer;
  }

  public Integer getVersionRemoteServerApi()
  {
    return versionRemoteServerApi;
  }

  public void setVersionRemoteServerApi(Integer versionRemoteServerApi)
  {
    this.versionRemoteServerApi = versionRemoteServerApi;
  }

  public Integer getVersionApiCompat()
  {
    return versionApiCompat;
  }

  public void setVersionApiCompat(Integer versionApiCompat) throws IncompatibleServerVersionException
  {
    // validateApiCompatVersion(versionApiCompat);

    this.versionApiCompat = versionApiCompat;
  }

  public RegistryResponse apiGet(String url, NameValuePair... params) throws HTTPException, BadServerUriException
  {
    return connector.httpGet(this.buildApiEndpoint() + url, params);
  }

}
