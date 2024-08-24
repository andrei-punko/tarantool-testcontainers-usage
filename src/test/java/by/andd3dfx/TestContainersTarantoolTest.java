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

class TestContainersTarantoolTest {

    private static TarantoolContainer container;

    @BeforeAll
    public static void setup() {
        container = new TarantoolContainer()
                .withDirectoryBinding("testcontainers")
        // Default commands:
        //      .withCopyFileToContainer(MountableFile.forClasspathResource("testcontainers/server.lua"), "/opt/tarantool/")
        //      .withCommand("tarantool", "/opt/tarantool/server.lua")
        //      .withExposedPorts(3301)
        //      .withReuse(true)
        ;
        container.start();
    }

    @AfterAll
    public static void tearDown() {
        container.stop();
    }

    @Test
    public void testExecuteScriptWithCrud() throws Exception {
        container.executeScript("testcontainers/test.lua").get();

        List<?> result = container.executeCommand("return user_function_no_param()").get();
        assertEquals(1, result.size());
        assertEquals(5, result.get(0));

        List<?> result2 = container.executeCommand("return get_band(3)").get();
        checkExtractedBand(result2, 3, "Ace of Base", 1993);

        container.executeCommand("return create_band(4, 'Smokie', 1996)").get();
        List<?> result4 = container.executeCommand("return get_band(4)").get();
        checkExtractedBand(result4, 4, "Smokie", 1996);

        container.executeCommand("return update_band_year(4, 1997)").get();
        List<?> result5 = container.executeCommand("return get_band(4)").get();
        checkExtractedBand(result5, 4, "Smokie", 1997);

        container.executeCommand("return delete_band(4)").get();
        List<?> result6 = container.executeCommand("return get_band(4)").get();
        assertEquals(1, result6.size());
        assertEquals(true, ((List) result6.get(0)).isEmpty());
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
