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
System.config({
    paths: {
        // paths serve as alias
        'root:': acp + '/',
        'npm:': acp + '/node_modules/'
    },
    // map tells the System loader where to look for things
    map: {
        // our app is within the app folder
        'app': 'root:app',
        'main': 'root:app/main.js',
        // angular bundles
        '@angular/core': 'npm:@angular/core/bundles/core.umd.js',
        '@angular/common': 'npm:@angular/common/bundles/common.umd.js',
        '@angular/compiler': 'npm:@angular/compiler/bundles/compiler.umd.js',
        '@angular/platform-browser': 'npm:@angular/platform-browser/bundles/platform-browser.umd.js',
        '@angular/platform-browser-dynamic': 'npm:@angular/platform-browser-dynamic/bundles/platform-browser-dynamic.umd.js',
        '@angular/http': 'npm:@angular/http/bundles/http.umd.js',
        '@angular/router': 'npm:@angular/router/bundles/router.umd.js',
        '@angular/forms': 'npm:@angular/forms/bundles/forms.umd.js',
        '@angular/upgrade': 'npm:@angular/upgrade/bundles/upgrade.umd.js',
        // other libraries
        'rxjs': 'npm:rxjs',
        'ng2-file-upload': 'npm:ng2-file-upload'
    },
    // packages tells the System loader how to load when no filename and/or no extension
    packages: {
        'app': { main: './main.js', defaultExtension: 'js' },
        'api': { defaultExtension: 'js' },
        'rxjs': { defaultExtension: 'js' },
        'ng2-file-upload': { defaultExtension: 'js' },
    }
});
//# sourceMappingURL=system-config.js.map