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

import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Router } from '@angular/router';

import { Program } from '../model/dhis2-program';
import { TrackedEntity } from '../model/dhis2-tracked-entity';
import { TrackedEntityAttribute } from '../model/dhis2-tracked-entity-attribute';

import { EventService } from '../service/core.service';
import { LocalizationService } from '../service/localization.service';
import { DHIS2Service } from '../service/dhis2.service';

declare let acp: string;

@Component({
  selector: 'dhis2-id-finder',
  templateUrl: './dhis2-id-finder.component.html',
  styleUrls: ['./dhis2-id-finder.component.css']
})
export class DHIS2IdFinderComponent implements OnInit {
  public programs: Program[];
  public trackedentities: TrackedEntity[];
  public trackedentityattributes: TrackedEntityAttribute[];
  
  constructor(
    private router: Router,
    private dhis2Service: DHIS2Service,
    private localizationService: LocalizationService,
    private eventService: EventService) { }

  ngOnInit(): void {
    this.getPrograms();
    this.getTrackedEntities();
    this.getTrackedEntityAttributes();
  };
  
  getPrograms() : void {
    this.dhis2Service
      .getPrograms()
      .then(programs => {
        this.programs = programs;
      })
  };
  
  getTrackedEntities() : void {
    this.dhis2Service
      .getTrackedEntities()
      .then(trackedentities => {
        this.trackedentities = trackedentities;
      })
  };
  
  getTrackedEntityAttributes() : void {
    this.dhis2Service
      .getTrackedEntityAttributes()
      .then(trackedentityattributes => {
        this.trackedentityattributes = trackedentityattributes;
      })
  };
}