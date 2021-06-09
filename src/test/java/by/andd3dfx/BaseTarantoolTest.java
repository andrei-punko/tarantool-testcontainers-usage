package by.andd3dfx;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.util.List;

public abstract class BaseTarantoolTest {

    @Container
    static GenericContainer<?> container =
            new GenericContainer<>(DockerImageName.parse("tarantool/tarantool:2.6.0"))
                    .withCopyFileToContainer(MountableFile.forClasspathResource("testcontainers/server.lua"), "/opt/tarantool/")
                    .withCommand("tarantool", "/opt/tarantool/server.lua")
                    .withExposedPorts(3301)
                    .withReuse(true);

    static {
        container.setPortBindings(List.of("3301:3301"));
    }
}
