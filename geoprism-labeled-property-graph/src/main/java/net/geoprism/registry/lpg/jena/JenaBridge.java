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
package net.geoprism.registry.lpg.jena;

import net.geoprism.registry.lpg.adapter.exception.BadServerUriException;
import net.geoprism.registry.lpg.adapter.exception.HTTPException;

/**
 * Resources:
 * - https://hub.docker.com/r/secoresearch/fuseki
 * - https://www.w3.org/TR/sparql11-http-rdf-update/#http-put
 */
public class JenaBridge
{
  public static final String  API_PATH = "labeled-property-graph-type";
  
  private JenaConnector connector;
  
  public JenaBridge(JenaConnector connector)
  {
    this.connector = connector;
  }

  public JenaResponse put(String graphName, String file) throws HTTPException, BadServerUriException
  {
    return this.connector.put(graphName, file);
  }
}
