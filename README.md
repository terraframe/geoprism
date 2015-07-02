# Runway GeoDashboard

Runway GeoDashboard is an integrated platform for data management and visualization for use as an App layer on top of RunwaySDK. Together RunwaySDK and Runway GeoDashboard provide an advanced toolset to help maintain data integrity while allowing users to generate interactive maps and dashboards on the fly.  

### An Ontological Approach to GIS Development
By utilizing an ontological approach to software development Runway GeoDashboard enables the utilization of almost any data set even if no geometries exist for data points.  This is one of the great strengths of Runway GeoDashboard as it allows for analysing and visuzlizing data in a geographic context even when geometry information is lacking.  Additionally, ontologies and geo-ontologies allow for navigating relationship hierarchies (including spatial) where such relationships would typically be computationally derived from geometric relationships or manually assigned by an individual.  

### Important Concepts of Runway GeoDashboard
In GeoDashboard a Universal represents a feature set type to which data points can be related (EX: Countries, States, or Cities).  A GeoEntity represents the actual geographic feature for a Universal (EX: USA, Colorado, or Denver).  In essence, a Universal defines a geographic heirarchy to which GeoEntities are assigned.  Relationships are then defined between data injested into the system and one or more GeoNodes which in turn have a relationship to one or more Universals.


### Visualization Features Overview
* Dynamically map and style layers based on data attributes.
* Dynamically aggregate layers by spatial boundaries.
* Enable/disable reference boundary layers for gegraphic context and cartography.
* Filter data by date, text, ontology term, number, and/or location.
* Save data filters for a map or dashboard.
* Add custom BIRT charts and graphs that update based on layer feature click.
* Export maps to .png file format.
* Create and clone new dashboards.
* Switch between different saved dashboards and maps.


### Data Management Features Overview
* Visualize Universal relationship heirarchy. 
* Edit Universals and Universal relationship heirarchy.
* Create Universal subtypes.
* Export Universal definitions.
* Visualize GeoEntity relationship heirarchy. 
* Edit GeoEntities (including their geometry) and GeoEntity relationship heirarchy.
* Create GeoEntity synonyms.
* Export GeoEntity definitions.
* Identify system detected GeoEntity data issues.
* Fix system identified GeoEntity data issues.
* Edit ontology term and term heirarchy.
* Export ontology term definitions.
* Visualize data in a tabular form.
* Create and edit scheduled data upload jobs. 
* View scheduled data upload job execution history.


### System Administration Features Overview
* Create and administer new users to the system. 
* Assing users to system roles.
* Assign users privileges to dashboards.


### General Software Requirements
* Java 6 or 7 (Oracle jre recommended)
* Tomcat 6 
* GeoServer 2.5 +
* PostgreSQL 9.0 +
* PostGIS 2.0 +

