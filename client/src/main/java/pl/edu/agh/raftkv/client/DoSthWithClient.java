package pl.edu.agh.raftkv.client;

import pl.edu.agh.raftkv.client.raftclient.KeyValueStoreClient;
import pl.edu.agh.raftkv.client.raftclient.KeyValueStores;

import java.util.Optional;

/**
 * Created by novy on 12.11.16.
 */
public class DoSthWithClient {

    public static void main(String[] args) {
        final KeyValueStoreClient kvStore = KeyValueStores.defaultClient();

        kvStore.put("ala", "ma kota");
        final Optional<Object> value = kvStore.get("ala");
        System.out.println(value);
    }
}
