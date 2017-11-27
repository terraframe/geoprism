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
/// License along with Runway SDK(tm).  If not, see <ehttp://www.gnu.org/licenses/>.
///
import { Injectable } from '@angular/core';

import { CookieService } from 'angular2-cookie/core';
import { User } from './user';

@Injectable()
export class AuthService {
  private user:User = {
    loggedIn:false,
    username:'',
    roles:[]
  };

  constructor(private service:CookieService) {
    let cookie = service.get('user');
    
    if(cookie != null && cookie.length > 0) {
      this.user = JSON.parse(JSON.parse(cookie)) as User;      
    }        
  }
  
  setUser(user:User):void {
    this.user = user;    
  }
  
  removeUser():void {
    this.user = {
      loggedIn:false,
      username:'',
      roles:[]
    };	  
  }
  
  isLoggedIn():boolean {
    return this.user.loggedIn;
  }
  
  isAdmin():boolean {
    return this.user.roles.indexOf("geoprism.admin.Administrator") !== -1;
  }  
}
