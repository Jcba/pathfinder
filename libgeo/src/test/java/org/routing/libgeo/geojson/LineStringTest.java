package org.routing.libgeo.geojson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineStringTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void convertToGeoJson_shouldBeValid() throws JsonProcessingException {
        List<Point> points = List.of(new Point(1.0, 1.0), new Point(2.0, 2.0));
        LineString lineString = new LineString(points);

        ObjectMapper objectMapper = new ObjectMapper();

        String result = objectMapper.writeValueAsString(lineString);

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

        AbstractGeometry<?> result = objectMapper.readValue(geojsonLineString, AbstractGeometry.class);

        assertThat(result)
                .isInstanceOf(LineString.class)
                .extracting(AbstractGeometry::getCoordinates).isEqualTo(new Double[][]{{1.0, 1.0}, {2.0, 2.0}});
    }
}