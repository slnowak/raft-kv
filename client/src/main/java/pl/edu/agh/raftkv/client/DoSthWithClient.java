package pl.edu.agh.raftkv.client;

import pl.edu.agh.raftkv.client.raftclient.KeyValueStoreClient;
import pl.edu.agh.raftkv.client.raftclient.KeyValueStores;

/**
 * Created by novy on 12.11.16.
 */
public class DoSthWithClient {

    public static void main(String[] args) {
        final KeyValueStoreClient kvStore = KeyValueStores.connectedTo(
                "localhost:9091", "localhost:9092", "localhost:9093", "localhost:9094", "localhost:9095"
        );

        for (int i = 0; i < 100_000; ++i) {
            kvStore.put("key" + 1, "value");

        }
    }
}
