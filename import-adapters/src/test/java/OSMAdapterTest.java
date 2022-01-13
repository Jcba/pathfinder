import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

class OSMAdapterTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void readFlevoland() throws URISyntaxException, IOException {
        Path flevoland = Path.of(getClass().getClassLoader().getResource("flevoland-latest.osm.pbf").toURI());
        OSMAdapter adapter = new OSMAdapter(flevoland);
    }
}