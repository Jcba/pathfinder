package org.routing.libgeo.geojson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class GJPointTest {

    @ParameterizedTest
    @CsvSource(textBlock = """
            52.3568, 5.2492, 52.3566, 5.2486, 45
            52.3566, 5.2486, 52.3568, 5.2492, 45
            52.3570, 5.2496, 52.3566, 5.2496, 45
            52.3572, 5.2561, 52.3662, 5.2474, 1200.0
            """)
    void distance_shouldBeCorrect(double lat1, double lon1, double lat2, double lon2, double distance) {
        double result = new GJPoint(lat1, lon1).distance(new GJPoint(lat2, lon2));
        assertThat(result).isCloseTo(distance, Percentage.withPercentage(5.0));
    }

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