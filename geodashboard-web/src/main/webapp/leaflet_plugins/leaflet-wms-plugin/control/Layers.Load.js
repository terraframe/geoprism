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
 * Add async initialization of layers to L.Control.Layers
 */
L.Control.Layers.include({
	_loadScripts: function(scripts, cb, args) {
		if (!scripts || scripts.length === 0)
			return cb(args);
		var _this = this, s = scripts.pop(), c;
		c = L.Control.Layers._script_cache[s];
		if (c === undefined) {
			c = {url: s, wait: []};
			var script = document.createElement('script');
			script.src = s;
			script.type = 'text/javascript';
			script.onload = function () {
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
		L.Control.Layers._script_cache[s] = c;
	},

	addLayerDef: function(name, def) {
		if (this._layer_defs === undefined)
			this._layer_defs = {};
		this._layer_defs[name] = def;
	},

	addLayerDefs: function(defs) {
		if (this._layer_defs === undefined)
			this._layer_defs = {};
		L.Util.extend(this._layer_defs, defs);
	},

	loadLayer: function(name, deflt) {
		var _this = this, l = this._layer_defs[name];
		l['default'] = deflt;
		this._loadScripts(l.js.reverse(), function(l) {_this._loadLayer(l);}, l);
	},

	_loadLayer: function(l) {
		var x = l.init();
		if (l['default'] && this._map)
			this._map.addLayer(x);
		if (!l.overlay)
			this.addBaseLayer(x, l.name);
		else
			this.addOverlay(x, l.name);
	}
});

L.Control.Layers._script_cache = {};
