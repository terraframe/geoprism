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
 * Based on comments by @runanet and @coomsie 
 * https://github.com/CloudMade/Leaflet/issues/386
 *
 * Wrapping function is needed to preserve L.Marker.update function
 */
(function () {
	var _old__setPos = L.Marker.prototype._setPos;
	L.Marker.include({
		_updateImg: function(i, a, s) {
			a = L.point(s).divideBy(2)._subtract(L.point(a));
			var transform = '';
			transform += ' translate(' + -a.x + 'px, ' + -a.y + 'px)';
			transform += ' rotate(' + this.options.iconAngle + 'deg)';
			transform += ' translate(' + a.x + 'px, ' + a.y + 'px)';
			i.style[L.DomUtil.TRANSFORM] += transform;
		},

		setIconAngle: function (iconAngle) {
			this.options.iconAngle = iconAngle;
			if (this._map)
				this.update();
		},

		_setPos: function (pos) {
			if (this._icon)
				this._icon.style[L.DomUtil.TRANSFORM] = '';
			if (this._shadow)
				this._shadow.style[L.DomUtil.TRANSFORM] = '';

			_old__setPos.apply(this,[pos]);

			if (this.options.iconAngle) {
				var a = this.options.icon.options.iconAnchor;
				var s = this.options.icon.options.iconSize;
				var i;
				if (this._icon) {
					i = this._icon;
					this._updateImg(i, a, s);
				}
				if (this._shadow) {
					if (this.options.icon.options.shadowAnchor)
						a = this.options.icon.options.shadowAnchor;
					s = this.options.icon.options.shadowSize;
					i = this._shadow;
					this._updateImg(i, a, s);
				}
			}
		}
	});
}());
