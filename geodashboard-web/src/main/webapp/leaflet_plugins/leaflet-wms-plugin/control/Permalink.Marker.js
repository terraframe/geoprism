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
//#include "Permalink.js

L.Control.Permalink.include({
	/*
	options: {
		useMarker: true,
		markerOptions: {}
	},
	*/

	initialize_marker: function() {
		this.on('update', this._set_marker, this);
	},

	_set_marker: function(e) {
		var p = e.params;
		//if (!this.options.useMarker) return;
		if (this._marker) return;
		if (p.marker !== 1) return;
		if (p.mlat !== undefined && p.mlon !== undefined)
			return this._update({mlat: null, mlon: null,
					lat: p.mlat, lon: p.mlon, marker: 1});
		this._marker = new L.Marker(new L.LatLng(p.lat, p.lon),
						this.options.markerOptions);
		this._marker.bindPopup('<a href="' + this._update_href() + '">' + this.options.text + '</a>');
		this._map.addLayer(this._marker);
		this._update({marker: null});
	}
});
