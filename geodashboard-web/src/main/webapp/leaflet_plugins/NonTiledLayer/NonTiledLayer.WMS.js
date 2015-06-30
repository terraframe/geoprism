/*
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * L.NonTiledLayer.WMS is used for putting WMS non tiled layers on the map.
 */
L.NonTiledLayer.WMS = L.NonTiledLayer.extend({

    defaultWmsParams: {
        service: 'WMS',
        request: 'GetMap',
        version: '1.1.1',
        layers: '',
        styles: '',
        format: 'image/jpeg',
        transparent: false
    },

    initialize: function (url, options) { // (String, Object)
        this._wmsUrl = url;

        var wmsParams = L.extend({}, this.defaultWmsParams);
		
		// all keys that are not NonTiledLayer options go to WMS params
		for (var i in options) {
			if (!L.NonTiledLayer.prototype.options.hasOwnProperty(i)) {
				wmsParams[i] = options[i];
			}
		}

        this.wmsParams = wmsParams;

        L.setOptions(this, options);
    },

    onAdd: function (map) {
        var projectionKey = parseFloat(this.wmsParams.version) >= 1.3 ? 'crs' : 'srs';
        this.wmsParams[projectionKey] = map.options.crs.code;

        L.NonTiledLayer.prototype.onAdd.call(this, map);
    },

    getImageUrl: function (world1, world2, width, height) {
        var wmsParams = this.wmsParams;
        wmsParams.width = width;
        wmsParams.height = height;

        var crs = this._map.options.crs;

        var p1 = crs.project(world1);
        var p2 = crs.project(world2);

        var url = this._wmsUrl + L.Util.getParamString(wmsParams, this._wmsUrl) + '&bbox=' + p1.x + ',' + p2.y + ',' + p2.x + ',' + p1.y;
        return url;
    },

    setParams: function (params, noRedraw) {

        L.extend(this.wmsParams, params);

        if (!noRedraw) {
            this.redraw();
        }

        return this;
    }
});

L.nonTiledLayer.wms = function (url, options) {
    return new L.NonTiledLayer.WMS(url, options);
};