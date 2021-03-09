package by.andd3dfx;

import io.tarantool.driver.ClusterTarantoolTupleClient;
import io.tarantool.driver.TarantoolServerAddress;
import io.tarantool.driver.auth.SimpleTarantoolCredentials;
import io.tarantool.driver.auth.TarantoolCredentials;
import io.tarantool.driver.metadata.TarantoolSpaceMetadata;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.TarantoolContainer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TarantoolTest {

    private static TarantoolContainer container;

    @BeforeAll
    public static void setup() {
        container = new TarantoolContainer();
        container.start();
    }

    @AfterAll
    public static void tearDown() {
        container.stop();
    }

    @Test
    public void testExecuteScript() throws Exception {
        container.executeScript("org/testcontainers/containers/test.lua").get();

        List<?> result = container.executeCommand("return user_function_no_param()").get();

        assertEquals(1, result.size());
        assertEquals(5, result.get(0));

        List<?> result2 = container.executeCommand("return get_bands(3)").get();

        checkExtractedBand(result2, 3, "Ace of Base", 1993);
    }

    @Test
    public void testDataExtraction() throws Exception {
        // Use properties provided by the container
        TarantoolCredentials credentials =
                new SimpleTarantoolCredentials(container.getUsername(), container.getPassword());
        TarantoolServerAddress serverAddress =
                new TarantoolServerAddress(container.getHost(), container.getPort());

        // Create TarantoolClient instance and use it in tests
        try (ClusterTarantoolTupleClient client = new ClusterTarantoolTupleClient(credentials, serverAddress)) {
            Optional<TarantoolSpaceMetadata> spaceMetadata = client.metadata().getSpaceByName("tester");
            // ...

            // Select a tuple using the primary index
            List<?> result = container.executeCommand("return box.space.tester:select{3}").get();
            checkExtractedBand(result, 3, "Ace of Base", 1993);

            // Select tuples using the secondary index
            List<?> result2 = container.executeCommand("return box.space.tester.index.secondary:select{'Scorpions'}").get();
            checkExtractedBand(result2, 2, "Scorpions", 2015);
        }
    }

    private void checkExtractedBand(List<?> result, int expectedId, String expectedBandName, int expectedYear) {
        assertEquals(1, result.size());

        List extractedBand = (List) ((List) result.get(0)).get(0);
        int id = (int) extractedBand.get(0);
        String bandName = (String) extractedBand.get(1);
        int year = (int) extractedBand.get(2);

        assertEquals(expectedId, id);
        assertEquals(expectedBandName, bandName);
        assertEquals(expectedYear, year);
    }
}
