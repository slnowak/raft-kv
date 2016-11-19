package pl.edu.agh.raftkv.chaostests;

import com.github.dockerjava.api.DockerClient;
import com.jayway.awaitility.Awaitility;
import lombok.SneakyThrows;
import lombok.val;
import org.arquillian.cube.docker.impl.client.config.CubeContainer;
import org.arquillian.cube.docker.impl.docker.compose.DockerComposeConverter;
import org.arquillian.cube.q.api.ContainerChaos;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.edu.agh.raftkv.client.raftclient.KeyValueStoreClient;
import pl.edu.agh.raftkv.client.raftclient.KeyValueStores;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by novy on 13.11.16.
 */

@RunWith(Arquillian.class)
public class ChaosOnDockerContainersTest {

    @ArquillianResource
    ContainerChaos containerChaos;

    @ArquillianResource
    DockerClient dockerClient;

    @Test
    public void should_survive_network_failures() throws Exception {
        // given
//        val cluster = startClusterDescribedIn("/docker-compose.yml");

        // when
        writeNKeys(100_000);

        // expect
        assertThatNKeysHaveBeenWritten(100_000);
    }

    @SneakyThrows
    private Map<String, CubeContainer> startClusterDescribedIn(String dockerComposePath) {
        val dockerComposeFileUrl = this.getClass().getResource(dockerComposePath);
        val dockerComposeRef = new File(dockerComposeFileUrl.getFile()).toPath();
        val compose = DockerComposeConverter.create(dockerComposeRef).convert();
        TimeUnit.SECONDS.sleep(60);
        return compose.getContainers();
    }

    private void writeNKeys(int n) {
        val executorService = Executors.newFixedThreadPool(50);
        val kvsClient = keyValueStoreClient();

        IntStream.range(0, n)
                .forEach(i -> {
                    final String key = "key" + i;
                    final String value = "foo";

                    executorService.submit(() -> kvsClient.put(key, value));
                });

        executorService.shutdown();
        Awaitility.await().until(executorService::isShutdown);
    }

    private KeyValueStoreClient keyValueStoreClient() {
        return KeyValueStores.connectedTo(
                "localhost:9091", "localhost:9092", "localhost:9093", "localhost:9094", "localhost:9095"
        );
    }

    private void assertThatNKeysHaveBeenWritten(int n) {
        val kvsClient = keyValueStoreClient();

        IntStream.range(0, n)
                .forEach(i -> {
                    final String key = "key" + i;
                    assertThat(kvsClient.get(key)).isPresent();
                });
    }
}
