/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 * <p>
 * This file is part of Geoprism Registry(tm).
 * <p>
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism Registry(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.build.domain;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.graph.GraphDB;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.graph.orientdb.OrientDBImpl;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

import com.runwaysdk.session.Request;
import net.geoprism.registry.graph.GeoVertex;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class GeoVertexRefactor {


    @Request
    private void doIt() {
        LocalProperties.setSkipCodeGenAndCompile(true);

        // For the change over time value to be false
        MdVertexDAO mdVertexDAO = MdVertexDAO.getMdVertexDAO(GeoVertex.CLASS).getBusinessDAO();

        Attribute attribute = mdVertexDAO.getAttribute(MdVertexInfo.ENABLE_CHANGE_OVER_TIME);
        attribute.setValueNoValidation("0");

        mdVertexDAO.apply();

        // Delete the cot attributes
        GraphDBService service = GraphDBService.getInstance();
        GraphRequest ddlRequest = service.getDDLGraphDBRequest();
        GraphRequest dbRequest = service.getGraphDBRequest();

        String[] columnNames = new String[]{"createDate", "lastUpdateDate", "oid", "seq"};

        for (String columnName : columnNames) {
            service.ddlCommand(dbRequest, ddlRequest, "DROP PROPERTY " + mdVertexDAO.getDBClassName() + "." + columnName + "_cot IF EXISTS", new HashMap<>()).execute();
        }
    }

    public static void main(String[] args) {


        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("net.geoprism.spring.core", "net.geoprism.registry.service.business", "net.geoprism.registry.service.permission")) {
            context.register(GeoVertexRefactor.class);

            GeoVertexRefactor service = context.getBean(GeoVertexRefactor.class);

            new GeoVertexRefactor().doIt();
        }
    }

}
