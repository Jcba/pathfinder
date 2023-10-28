package org.routing.libgeo.geojson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GJLineStringTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void convertToGeoJson_shouldBeValid() throws JsonProcessingException {
        List<GJPoint> GJPoints = List.of(new GJPoint(1.0, 1.0), new GJPoint(2.0, 2.0));
        GJLineString GJLineString = new GJLineString(GJPoints);

        ObjectMapper objectMapper = new ObjectMapper();

        String result = objectMapper.writeValueAsString(GJLineString);

        assertThat(result).isEqualToIgnoringWhitespace("""
                {
                    "type": "LineString",
                    "coordinates": [
                        [1.0,1.0],
                        [2.0,2.0]
                     ]
                }
                """);
    }

    @Test
    void geoJsonStringToObject_shouldBeValid() throws JsonProcessingException {
        String geojsonLineString = """
                {
                    "type": "LineString",
                    "coordinates": [
                        [1.0,1.0],
                        [2.0,2.0]
                     ]
                }
                """;

        ObjectMapper objectMapper = new ObjectMapper();

        GJAbstractGeometry<?> result = objectMapper.readValue(geojsonLineString, GJAbstractGeometry.class);

        assertThat(result)
                .isInstanceOf(GJLineString.class)
                .extracting(GJAbstractGeometry::getCoordinates).isEqualTo(new Double[][]{{1.0, 1.0}, {2.0, 2.0}});
    }
}