package pl.edu.agh.raftkv.chaostests;

import com.jayway.awaitility.Awaitility;
import lombok.SneakyThrows;
import lombok.val;
import org.arquillian.cube.q.api.ContainerChaos;
import org.arquillian.cube.q.api.ContainerChaos.ContainersType;
import org.arquillian.cube.q.api.NetworkChaos;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.edu.agh.raftkv.client.raftclient.KeyValueStoreClient;
import pl.edu.agh.raftkv.client.raftclient.KeyValueStores;

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
    NetworkChaos networkChaos;


    @Test
    public void should_write_n_keys_without_any_interruptions() throws Exception {
        // given
        waitForClusterToBeReady();
        val numberOfKeys = 50_000;

        // when
        writeNKeys(numberOfKeys);

        // expect
        assertThatNKeysHaveBeenWritten(numberOfKeys);
    }

    @Test
    public void should_survive_killing_containers() throws Exception {
        // given
        waitForClusterToBeReady();
        val numberOfKeys = 50_000;

        // when
        containerChaos.onCubeDockerHost().killRandomly(
                ContainersType.regularExpression("^node"),
                ContainerChaos.IntervalType.intervalInSeconds(10),
                ContainerChaos.KillSignal.SIGTERM
        ).exec(() -> writeNKeys(numberOfKeys));

        // expect
        assertThatNKeysHaveBeenWritten(numberOfKeys);
    }

    @SneakyThrows
    private void waitForClusterToBeReady() {
        TimeUnit.SECONDS.sleep(15);
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
        Awaitility.await().atMost(15, TimeUnit.MINUTES).until(executorService::isTerminated);
    }

    private KeyValueStoreClient keyValueStoreClient() {
        return KeyValueStores.connectedTo(
                "localhost:9091",
                "localhost:9092",
                "localhost:9093",
                "localhost:9094",
                "localhost:9095",
                "localhost:9096",
                "localhost:9097",
                "localhost:9098",
                "localhost:9099"
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
