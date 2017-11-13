///
/// Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
///
/// This file is part of Runway SDK(tm).
///
/// Runway SDK(tm) is free software: you can redistribute it and/or modify
/// it under the terms of the GNU Lesser General Public License as
/// published by the Free Software Foundation, either version 3 of the
/// License, or (at your option) any later version.
///
/// Runway SDK(tm) is distributed in the hope that it will be useful, but
/// WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU Lesser General Public License for more details.
///
/// You should have received a copy of the GNU Lesser General Public
/// License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
///

import { Component, EventEmitter, Input, OnInit } from '@angular/core';
import { Location } from '@angular/common';

import { GeoTreeService } from './geotree.service';
import { GeoTree } from './geotree';

declare var Mojo:any;
declare var net:any;

@Component({
  
  selector: 'geotree',
  templateUrl: './geotree.component.html',
  styles: []
})
export class GeoTreeComponent implements OnInit {
  
  constructor(private service:GeoTreeService) {}

  ngOnInit() {
    this.service.getInstance().then(geotree => {
    var request = new Mojo.ClientRequest({
      onSuccess: function(views:any){
            
        var tree = new net.geoprism.ontology.GeoEntityTree({
          termType : geotree.type,
          relationshipTypes : geotree.relationships,
          rootTerms : [ { termId : geotree.rootId} ],
          editable : true,
          crud: {
            create: { // This configuration gets merged into the jquery create dialog.
              height: 320
            },
            update: {
              height: 320
            }
          }
        });
        tree.render("#tree", views);
      },
      onFailure : function(ex:any) {
    	// TODO fix this to use standard error popup
        alert("Error reading entity problems");
      }
    });
      
    net.geoprism.ontology.GeoEntityUtil.getAllProblems(request);
      
      
    });
  }  
}
