package org.routing.libgeo.geojson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GJPointTest {

    @Test
    void convertToGeoJson_shouldBeValid() throws JsonProcessingException {
        GJPoint GJPoint = new GJPoint(1.0, 1.0);

        ObjectMapper objectMapper = new ObjectMapper();

        String result = objectMapper.writeValueAsString(GJPoint);

        assertThat(result).isEqualToIgnoringWhitespace("""
                {
                    "type": "Point",
                    "coordinates": [1.0,1.0]
                }
                """);
    }

    @Test
    void geoJsonStringToObject_shouldBeValid() throws JsonProcessingException {
        String geoJsonPoint = """
                {
                    "type": "Point",
                    "coordinates": [1.0,1.0]
                }
                """;

        ObjectMapper objectMapper = new ObjectMapper();

        GJAbstractGeometry<?> result = objectMapper.readValue(geoJsonPoint, GJAbstractGeometry.class);

        assertThat(result)
                .isInstanceOf(GJPoint.class)
                .extracting(GJAbstractGeometry::getCoordinates).isEqualTo(new Double[]{1.0, 1.0});
    }

}