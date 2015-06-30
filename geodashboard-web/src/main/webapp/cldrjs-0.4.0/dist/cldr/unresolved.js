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
/*!
 * CLDR JavaScript Library v0.4.0 2015-01-20T20:32Z MIT license © Rafael Xavier
 * http://git.io/h4lmVg
 */
(function( factory ) {

	if ( typeof define === "function" && define.amd ) {
		// AMD.
		define( [ "../cldr" ], factory );
	} else if ( typeof module === "object" && typeof module.exports === "object" ) {
		// Node. CommonJS.
		module.exports = factory( require( "cldrjs" ) );
	} else {
		// Global
		factory( Cldr );
	}

}(function( Cldr ) {

	// Build optimization hack to avoid duplicating functions across modules.
	var coreLoad = Cldr._coreLoad;
	var jsonMerge = Cldr._jsonMerge;
	var pathNormalize = Cldr._pathNormalize;
	var resourceGet = Cldr._resourceGet;
	var validatePresence = Cldr._validatePresence;
	var validateTypePath = Cldr._validateTypePath;



	var bundleParentLookup = function( Cldr, locale ) {
		var normalizedPath, parent;

		if ( locale === "root" ) {
			return;
		}

		// First, try to find parent on supplemental data.
		normalizedPath = pathNormalize( [ "supplemental/parentLocales/parentLocale", locale ] );
		parent = resourceGet( Cldr._resolved, normalizedPath ) || resourceGet( Cldr._raw, normalizedPath );
		if ( parent ) {
			return parent;
		}

		// Or truncate locale.
		parent = locale.substr( 0, locale.lastIndexOf( Cldr.localeSep ) );
		if ( !parent ) {
			return "root";
		}

		return parent;
	};




	// @path: normalized path
	var resourceSet = function( data, path, value ) {
		var i,
			node = data,
			length = path.length;

		for ( i = 0; i < length - 1; i++ ) {
			if ( !node[ path[ i ] ] ) {
				node[ path[ i ] ] = {};
			}
			node = node[ path[ i ] ];
		}
		node[ path[ i ] ] = value;
	};


	var itemLookup = (function() {

	var lookup;

	lookup = function( Cldr, locale, path, attributes, childLocale ) {
		var normalizedPath, parent, value;

		// 1: Finish recursion
		// 2: Avoid infinite loop
		if ( typeof locale === "undefined" /* 1 */ || locale === childLocale /* 2 */ ) {
			return;
		}

		// Resolve path
		normalizedPath = pathNormalize( path, attributes );

		// Check resolved (cached) data first
		// 1: Due to #16, never use the cached resolved non-leaf nodes. It may not
		//    represent its leafs in its entirety.
		value = resourceGet( Cldr._resolved, normalizedPath );
		if ( value && typeof value !== "object" /* 1 */ ) {
			return value;
		}

		// Check raw data
		value = resourceGet( Cldr._raw, normalizedPath );

		if ( !value ) {
			// Or, lookup at parent locale
			parent = bundleParentLookup( Cldr, locale );
			value = lookup( Cldr, parent, path, jsonMerge( attributes, { bundle: parent }), locale );
		}

		if ( value ) {
			// Set resolved (cached)
			resourceSet( Cldr._resolved, normalizedPath, value );
		}

		return value;
	};

	return lookup;

}());


	Cldr._raw = {};

	/**
	 * Cldr.load( json [, json, ...] )
	 *
	 * @json [JSON] CLDR data or [Array] Array of @json's.
	 *
	 * Load resolved or unresolved cldr data.
	 * Overwrite Cldr.load().
	 */
	Cldr.load = function() {
		Cldr._raw = coreLoad( Cldr, Cldr._raw, arguments );
	};

	/**
	 * Overwrite Cldr.prototype.get().
	 */
	Cldr.prototype.get = function( path ) {
		validatePresence( path, "path" );
		validateTypePath( path, "path" );

		// 1: use bundle as locale on item lookup for simplification purposes, because no other extended subtag is used anyway on bundle parent lookup.
		// 2: during init(), this method is called, but bundle is yet not defined. Use "" as a workaround in this very specific scenario.
		return itemLookup( Cldr, this.attributes && this.attributes.bundle /* 1 */ || "" /* 2 */, path, this.attributes );
	};

	// In case cldr/unresolved is loaded after cldr/event, we trigger its overloads again. Because, .get is overwritten in here.
	if ( Cldr._eventInit ) {
		Cldr._eventInit();
	}

	return Cldr;




}));
