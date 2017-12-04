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

import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { enableProdMode } from '@angular/core';

import { AppModule } from './app/app.module';
import { AdminModule } from './app/admin/admin.module';

declare var appname: string;
declare var  acp:string; 
declare var  __webpack_public_path__:string; 

if (process.env.ENV === 'production') {
  __webpack_public_path__ = acp + '/dist/';
	
  enableProdMode();  
}

if(appname !== undefined && appname === 'admin-app' ) {
  platformBrowserDynamic().bootstrapModule(AdminModule)
    .then(success => console.log('Admin Bootstrap success'))
    .catch(error => console.log(error));  
}
else {
  platformBrowserDynamic().bootstrapModule(AppModule)
    .then(success => console.log('App bootstrap success'))
    .catch(error => console.log(error));  
}
