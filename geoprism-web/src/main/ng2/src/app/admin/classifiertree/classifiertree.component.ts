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

import { ClassifierTreeService } from './classifiertree.service';
import { ClassifierTree } from './classifiertree';

declare var Mojo:any;
declare var net:any;
declare var $:any;

@Component({
  
  selector: 'classifiertree',
  templateUrl: './classifiertree.component.html',
  styles: []
})
export class ClassifierTreeComponent implements OnInit {
  
  constructor(private service:ClassifierTreeService) {}

  ngOnInit() {
    this.service.getInstance().then(response => {
      let request = new Mojo.ClientRequest({
        onSuccess: function(views:any){
          let tree = new net.geoprism.ontology.OntologyTree({
            termType : response.type,
            relationshipTypes : response.relationships,
            rootTerms : [ { termId : response.rootId } ],
            editable : true,
            /* checkable: true, */
            crud: {
              create: { // This configuration gets merged into the jquery create dialog.
                height: 290
              },
              update: {
                height: 290
              }
            },
            onCreateLi : function(node:any, $li:any) {
              if (!node.phantom) {
                if(node.hasProblem){
                  var msg = "";
                  var msgEls = $("#problems-list").find('[data-classifier="'+node.runwayId+'"]');
              
                  for(var i=0; i<msgEls.length; i++){
                    var msgEl = msgEls[i];
                    msg +=  i+1 + "." + "&nbsp;&nbsp;" + $(msgEl).find(".classifier-problem-msg").text(); // gets the message from the problems panel
                    if(i < msgEls.length - 1){
                      msg += "<br/><br/>";
                    }
                  }
                  $li.find("span").parent().append("<i data-problemid='"+ node.problemId +"' data-classifier='"+ node.runwayId +"' class='fa fa-times-circle classifier-problem-msg-icon classifier-problem-error'></i>");
                  $li.find("i").tooltip({
                    items: "i",
                    content: msg,
                    tooltipClass: "geoentity-problem-tooltip"
                  });
                }
              }
            }
          });
          tree.renderWithProblems("#tree", views);
        },
        onFailure : function(ex:any) {
          // TODO fix this to use standard error popup
          alert("Error reading classifier problems");
        }
      });
      net.geoprism.ontology.Classifier.getAllProblems(request);      
    });
  }  
}
