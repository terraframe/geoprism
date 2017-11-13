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

import { UniversalTreeService } from './universaltree.service';
import { UniversalTree } from './universaltree';

declare var Mojo:any;
declare var net:any;

@Component({
  
  selector: 'universaltree',
  templateUrl: './universaltree.component.html',
  styles: []
})
export class UniversalTreeComponent implements OnInit {
  
  constructor(private service:UniversalTreeService) {}

  ngOnInit() {
    this.service.getInstance().then(tree => {
      let universaltree = new net.geoprism.ontology.UniversalTree({
  	    termType : tree.type,
	    relationshipTypes : tree.relationships,
        rootTerms : [ { termId : tree.rootId } ],
        editable : true,
        /* checkable: true, */
        crud: {
          create: { // This configuration gets merged into the jquery create dialog.
            height: 290
          },
          update: {
            height: 290
          }
        }
      });
      universaltree.render("#tree");
    });
  }  
}
