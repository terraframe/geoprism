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
"use strict";
var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require("@angular/core");
var http_1 = require("@angular/http");
require("rxjs/add/operator/toPromise");
var core_service_1 = require("./core.service");
var event_http_service_1 = require("./event-http.service");
var UploadService = (function (_super) {
    __extends(UploadService, _super);
    function UploadService(service, ehttp, http) {
        var _this = _super.call(this, service) || this;
        _this.ehttp = ehttp;
        _this.http = http;
        return _this;
    }
    UploadService.prototype.getSavedConfiguration = function (id, sheetName) {
        var headers = new http_1.Headers({
            'Content-Type': 'application/json'
        });
        var data = JSON.stringify({ id: id, sheetName: sheetName });
        return this.ehttp
            .post(acp + '/uploader/getSavedConfiguration', data, { headers: headers })
            .toPromise()
            .then(function (response) {
            return response.json();
        })
            .catch(this.handleError);
    };
    UploadService.prototype.cancelImport = function (workbook) {
        var headers = new http_1.Headers({
            'Content-Type': 'application/json'
        });
        var data = JSON.stringify({ configuration: workbook });
        return this.ehttp
            .post(acp + '/uploader/cancelImport', data, { headers: headers })
            .toPromise()
            .catch(this.handleError);
    };
    UploadService.prototype.importData = function (workbook) {
        var headers = new http_1.Headers({
            'Content-Type': 'application/json'
        });
        var data = JSON.stringify({ configuration: workbook });
        return this.ehttp
            .post(acp + '/uploader/importData', data, { headers: headers })
            .toPromise()
            .then(function (response) {
            return response.json();
        })
            .catch(this.handleError);
    };
    UploadService.prototype.getGeoEntitySuggestions = function (parentId, universalId, text, limit) {
        var params = new http_1.URLSearchParams();
        params.set('parentId', parentId);
        params.set('universalId', universalId);
        params.set('text', text);
        params.set('limit', limit);
        return this.http
            .get(acp + '/uploader/getGeoEntitySuggestions', { search: params })
            .map(function (resp) { return resp.json(); });
        //      .toPromise()
        //      .then((response: any) => {
        //        return response.json() as Pair[];
        //      });
        //      .catch(this.handleError);    
    };
    UploadService.prototype.createGeoEntitySynonym = function (entityId, label) {
        var headers = new http_1.Headers({
            'Content-Type': 'application/json'
        });
        var data = JSON.stringify({ entityId: entityId, label: label });
        return this.ehttp
            .post(acp + '/uploader/createGeoEntitySynonym', data, { headers: headers })
            .toPromise()
            .then(function (response) {
            return response.json();
        })
            .catch(this.handleError);
    };
    UploadService.prototype.createGeoEntity = function (parentId, universalId, label) {
        var headers = new http_1.Headers({
            'Content-Type': 'application/json'
        });
        var data = JSON.stringify({ parentId: parentId, universalId: universalId, label: label });
        return this.ehttp
            .post(acp + '/uploader/createGeoEntity', data, { headers: headers })
            .toPromise()
            .then(function (response) {
            return response.json();
        })
            .catch(this.handleError);
    };
    UploadService.prototype.deleteGeoEntity = function (entityId) {
        var headers = new http_1.Headers({
            'Content-Type': 'application/json'
        });
        var data = JSON.stringify({ entityId: entityId });
        return this.ehttp
            .post(acp + '/uploader/deleteGeoEntity', data, { headers: headers })
            .toPromise()
            .catch(this.handleError);
    };
    UploadService.prototype.deleteGeoEntitySynonym = function (synonymId) {
        var headers = new http_1.Headers({
            'Content-Type': 'application/json'
        });
        var data = JSON.stringify({ synonymId: synonymId });
        return this.ehttp
            .post(acp + '/uploader/deleteGeoEntitySynonym', data, { headers: headers })
            .toPromise()
            .catch(this.handleError);
    };
    return UploadService;
}(core_service_1.BasicService));
UploadService = __decorate([
    core_1.Injectable(),
    __metadata("design:paramtypes", [core_service_1.EventService, event_http_service_1.EventHttpService, http_1.Http])
], UploadService);
exports.UploadService = UploadService;
//# sourceMappingURL=upload.service.js.map