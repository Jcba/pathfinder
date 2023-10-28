package org.routing.libgeo.geojson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class FeatureCollectionTest {

    @Test
    void convertToGeoJson_shouldBeValidGeoJsonString() throws JsonProcessingException {

        FeatureCollection featureCollection = new FeatureCollection(List.of(
                new Feature(
                        "0",
                        new Point(0.0, 0.0),
                        new Properties()
                )
        ));

        ObjectMapper objectMapper = new ObjectMapper();

        String result = objectMapper.writeValueAsString(featureCollection);

        assertThat(result).isEqualToIgnoringWhitespace("""
                      {
                              "type": "FeatureCollection",
                              "features": [{
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