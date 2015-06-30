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
L.Icon.Text = L.Icon.extend({
	initialize: function (text, options) {
		this._text = text;
		L.Icon.prototype.initialize.apply(this, [options]);
	},

	createIcon: function() {
		var el = document.createElement('div');
		el.appendChild(document.createTextNode(this._text));
		this._setIconStyles(el, 'icon');
		el.style.textShadow = '2px 2px 2px #fff';
		return el;
	},

	createShadow: function() { return null; }

});

L.Marker.Text = L.Marker.extend({
	initialize: function (latlng, text, options) {
        	L.Marker.prototype.initialize.apply(this, [latlng, options]);
		this._fakeicon = new L.Icon.Text(text);
	},

	_initIcon: function() {
        	L.Marker.prototype._initIcon.apply(this);

		var i = this._icon, s = this._shadow, obj = this.options.icon;
		this._icon = this._shadow = null;

		this.options.icon = this._fakeicon;
        	L.Marker.prototype._initIcon.apply(this);
		this.options.icon = obj;

		if (s) {
			s.parentNode.removeChild(s);
			this._icon.appendChild(s);
		}
		
		i.parentNode.removeChild(i);
		this._icon.appendChild(i);

		var w = this._icon.clientWidth, h = this._icon.clientHeight;
		this._icon.style.marginLeft = -w / 2 + 'px';
		//this._icon.style.backgroundColor = "red";
		var off = new L.Point(w/2, 0);
		if (L.Browser.webkit) off.y = -h;
		L.DomUtil.setPosition(i, off);
		if (s) L.DomUtil.setPosition(s, off);
	}
});
