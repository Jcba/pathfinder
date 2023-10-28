package org.routing.libgeo.geojson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class GJGJFeatureCollectionTest {

    @Test
    void convertToGeoJson_shouldBeValidGeoJsonString() throws JsonProcessingException {

        GJFeatureCollection GJFeatureCollection = new GJFeatureCollection(List.of(
                new GJFeature(
                        "0",
                        new GJPoint(0.0, 0.0),
                        new Properties()
                )
        ));

        ObjectMapper objectMapper = new ObjectMapper();

        String result = objectMapper.writeValueAsString(GJFeatureCollection);

        assertThat(result).isEqualToIgnoringWhitespace("""
                      {
                              "type": "FeatureCollection",
                              "GJFeatures": [{
                                  "type": "Feature",
                                  "properties": {},
                                  "geometry": {
                                    "type": "Point",
                                    "coordinates": [
                                        0.0,
                                        0.0
                                      ]
                                    },
                                    "id": "0"
                                }]
                          }
                """);
    }

}