package by.andd3dfx;

import com.palantir.docker.compose.DockerComposeExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.tarantool.Iterator;
import org.tarantool.TarantoolClient;
import org.tarantool.TarantoolClientConfig;
import org.tarantool.TarantoolClientImpl;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PalantirDockerTarantoolTest {

    @RegisterExtension
    protected static DockerComposeExtension docker = DockerComposeExtension.builder()
            .file("src/test/resources/palantir/docker-compose.yml")
            .build();

    @Test
    public void testDataExtraction() {
        TarantoolClient tarantoolClient = tarantoolClient(tarantoolClientConfig());

        final List<?> tuple = tarantoolClient.syncOps().select("tester", "primary", Collections.singletonList(1), 0, 1, Iterator.EQ);

        checkExtractedBand(tuple, 1, "Roxette", 1986);
    }

    private TarantoolClientConfig tarantoolClientConfig() {
        TarantoolClientConfig config = new TarantoolClientConfig();
        config.username = "api_user";
        config.password = "secret";
        return config;
    }

    private TarantoolClient tarantoolClient(TarantoolClientConfig tarantoolClientConfig) {
        return new TarantoolClientImpl("localhost:3301", tarantoolClientConfig);
    }

    private void checkExtractedBand(List<?> result, int expectedId, String expectedBandName, int expectedYear) {
        assertEquals(1, result.size());

        List extractedBand = (List) result.get(0);
        int id = (int) extractedBand.get(0);
        String bandName = (String) extractedBand.get(1);
        int year = (int) extractedBand.get(2);

        assertEquals(expectedId, id);
        assertEquals(expectedBandName, bandName);
        assertEquals(expectedYear, year);
    }
}
