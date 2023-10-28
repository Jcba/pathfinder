# Additional developer documentation

Query the stored geometries for a small area in h2gis:

```SQL
SELECT JSON_OBJECT(
'type': 'FeatureCollection',
'GJFeatures': (
SELECT JSON_ARRAYAGG(
JSON_OBJECT('type': 'Feature', 'geometry': GEOM, 'properties': JSON_OBJECT('id': ID)))
FROM (SELECT * FROM GEOMETRY_KV where geom && 'POLYGON ((5.2742 52.3642 , 5.2746 52.3516 , 5.2960 52.3590 , 5.2742 52.3642 ))') T(ID, GEOM)));
```