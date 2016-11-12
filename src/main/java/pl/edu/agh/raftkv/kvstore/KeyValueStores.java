package pl.edu.agh.raftkv.kvstore;

/**
 * Created by novy on 12.11.16.
 */
public class KeyValueStores {

    public static KeyValueStoreClient connectedTo(String host, int port) {
        return new RaftKVStoreClient(host, port);
    }

    public static KeyValueStoreClient defaultClient() {
        return connectedTo("localhost", 9999);
    }
}
