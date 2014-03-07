(function(){
  
  var ROOT_PACKAGE = Mojo.ROOT_PACKAGE + 'geodashboard.';
  var ALIAS = 'GDB';
  
  var Constants = Mojo.Meta.newInterface(ROOT_PACKAGE+'Constants', {
    Constants : {
      ROOT_PACKAGE: ROOT_PACKAGE,
      GIS_PACKAGE: ROOT_PACKAGE+"gis."
    }
  });

  // Alias the project's root to GDB
  Mojo.GLOBAL[ALIAS] = Mojo.$.com.runwaysdk.geodashboard;
})();