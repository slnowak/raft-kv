package pl.edu.agh.raftkv.chaostests;

import com.jayway.awaitility.Awaitility;
import lombok.SneakyThrows;
import lombok.val;
import pl.edu.agh.raftkv.client.raftclient.KeyValueStoreClient;
import pl.edu.agh.raftkv.client.raftclient.KeyValueStores;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by novy on 20.11.16.
 */
class KeyValueStoreTests {

    @SneakyThrows
    static void waitForClusterToBeReady() {
        TimeUnit.SECONDS.sleep(15);
    }

    static void writeNKeys(int n) {
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

    static KeyValueStoreClient keyValueStoreClient() {
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

    static void assertThatNKeysHaveBeenWritten(int n) {
        val kvsClient = keyValueStoreClient();

        IntStream.range(0, n)
                .forEach(i -> {
                    final String key = "key" + i;
                    assertThat(kvsClient.get(key)).isPresent();
                });
    }

}
