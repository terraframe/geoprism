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
L.GPX = L.FeatureGroup.extend({
	initialize: function(gpx, options) {
		L.Util.setOptions(this, options);
		this._gpx = gpx;
		this._layers = {};
		
		if (gpx) {
			this.addGPX(gpx, options, this.options.async);
		}
	},
	
	loadXML: function(url, cb, options, async) {
		if (async === undefined) async = this.options.async;
		if (options === undefined) options = this.options;

		var req = new window.XMLHttpRequest();
		req.open('GET', url, async);
		try {
			req.overrideMimeType('text/xml'); // unsupported by IE
		} catch(e) {}
		req.onreadystatechange = function() {
			if (req.readyState !== 4) return;
			if(req.status === 200) cb(req.responseXML, options);
		};
		req.send(null);
	},

	_humanLen: function(l) {
		if (l < 2000)
			return l.toFixed(0) + ' m';
		else
			return (l/1000).toFixed(1) + ' km';
	},
	
	_polylineLen: function(line)//line is a L.Polyline()
	{
		var ll = line._latlngs;
		var d = 0, p = null;
		for (var i = 0; i < ll.length; i++)
		{
			if(i && p)
				d += p.distanceTo(ll[i]);
			p = ll[i];
		}
		return d;
	},

	addGPX: function(url, options, async) {
		var _this = this;
		var cb = function(gpx, options) { _this._addGPX(gpx, options); };
		this.loadXML(url, cb, options, async);
	},

	_addGPX: function(gpx, options) {
		var layers = this.parseGPX(gpx, options);
		if (!layers) return;
		this.addLayer(layers);
		this.fire('loaded');
	},	

	parseGPX: function(xml, options) {
		var j, i, el, layers = [];
		var named = false, tags = [['rte','rtept'], ['trkseg','trkpt']];

		for (j = 0; j < tags.length; j++) {
			el = xml.getElementsByTagName(tags[j][0]);
			for (i = 0; i < el.length; i++) {
				var l = this.parse_trkseg(el[i], xml, options, tags[j][1]);
				for (var k = 0; k < l.length; k++) {
					if (this.parse_name(el[i], l[k])) named = true;
					layers.push(l[k]);
				}
			}
		}

		el = xml.getElementsByTagName('wpt');
		if (options.display_wpt !== false) {
			for (i = 0; i < el.length; i++) {
				var marker = this.parse_wpt(el[i], xml, options);
				if (!marker) continue;
				if (this.parse_name(el[i], marker)) named = true;
				layers.push(marker);
			}
		}

		if (!layers.length) return;
		var layer = layers[0];
		if (layers.length > 1) 
			layer = new L.FeatureGroup(layers);
		if (!named) this.parse_name(xml, layer);
		return layer;
	},

	parse_name: function(xml, layer) {
		var i, el, txt='', name, descr='', len=0;
		el = xml.getElementsByTagName('name');
		if (el.length)
			name = el[0].childNodes[0].nodeValue;
		el = xml.getElementsByTagName('desc');
		for (i = 0; i < el.length; i++) {
			for (var j = 0; j < el[i].childNodes.length; j++)
				descr = descr + el[i].childNodes[j].nodeValue;
		}

		if(layer instanceof L.Path)
			len = this._polylineLen(layer);

		if (name) txt += '<h2>' + name + '</h2>' + descr;
		if (len) txt += '<p>' + this._humanLen(len) + '</p>';
		
		if (layer && layer._popup === undefined) layer.bindPopup(txt);
		return txt;
	},

	parse_trkseg: function(line, xml, options, tag) {
		var el = line.getElementsByTagName(tag);
		if (!el.length) return [];
		var coords = [];
		for (var i = 0; i < el.length; i++) {
			var ll = new L.LatLng(el[i].getAttribute('lat'),
						el[i].getAttribute('lon'));
			ll.meta = {};
			for (var j in el[i].childNodes) {
				var e = el[i].childNodes[j];
				if (!e.tagName) continue;
				ll.meta[e.tagName] = e.textContent;
			}
			coords.push(ll);
		}
		var l = [new L.Polyline(coords, options)];
		this.fire('addline', {line:l});
		return l;
	},

	parse_wpt: function(e, xml, options) {
		var m = new L.Marker(new L.LatLng(e.getAttribute('lat'),
						e.getAttribute('lon')), options);
		this.fire('addpoint', {point:m});
		return m;
	}
});
