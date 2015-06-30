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
L.DeferredLayer = L.LayerGroup.extend({
	options: {
		js: [],
		init: null
	},

	_script_cache: {},

	initialize: function(options) {
		L.Util.setOptions(this, options);
		L.LayerGroup.prototype.initialize.apply(this);
		this._loaded = false;
	},

	onAdd: function(map) {
		L.LayerGroup.prototype.onAdd.apply(this, [map]);
		if (this._loaded) return;
		var loaded = function() {
			this._loaded = true;
			var l = this.options.init();
			if (l)
				this.addLayer(l);
		};
		this._loadScripts(this.options.js.reverse(), L.Util.bind(loaded, this));
	},

	_loadScripts: function(scripts, cb, args) {
		if (!scripts || scripts.length === 0)
			return cb(args);
		var _this = this, s = scripts.pop(), c;
		c = this._script_cache[s];
		if (c === undefined) {
			c = {url: s, wait: []};
			var script = document.createElement('script');
			script.src = s;
			script.type = 'text/javascript';
			script.onload = function () {
				c.e.readyState = 'completed';
				var i = 0;
				for (i = 0; i < c.wait.length; i++)
					c.wait[i]();
			};
			c.e = script;
			document.getElementsByTagName('head')[0].appendChild(script);
		}
		function _cb() { _this._loadScripts(scripts, cb, args); }
		c.wait.push(_cb);
		if (c.e.readyState === 'completed')
			_cb();
		this._script_cache[s] = c;
	}
});
