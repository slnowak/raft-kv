package pl.edu.agh.raftkv.client.raftclient;

import java.util.Optional;

/**
 * Created by novy on 12.11.16.
 */
public interface KeyValueStoreClient {

    void put(String key, Object value);

    void delete(String key);

    Optional<Object> get(String key);
}
