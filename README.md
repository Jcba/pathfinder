# Pathfinder 

Pathfinder finds the shortest route between two GJPoints. It reads OSM data and transforms this to a routable graph. 
The project aims to be simple and extendable.

## Usage

You will need an OSM region extract in PBF format. 

1. Load the project into a code editor
2. Get an OSM region extract in PBF format (for example [using Geofabrik](https://download.geofabrik.de/europe/netherlands/flevoland.html))
3. Store the PBF in the resources folder in the importer module
4. Change application.properties: set network.name to the name of the downloaded extract
5. Run the api module
6. The PBF will be loaded and your routing service will be available at port 8080
7. Run a test request: http://localhost:8080/route?fromLat=52.3565&fromLon=5.2812&toLon=5.2892&toLat=52.3576

## Why 

Just because I can. It's written for fun. 
