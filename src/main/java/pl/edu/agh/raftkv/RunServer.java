package pl.edu.agh.raftkv;

import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.raftkv.kvstore.KeyValueStoreServer;

/**
 * Created by novy on 12.11.16.
 */

@Slf4j
public class RunServer {

    public static void main(String[] args) {
        new KeyValueStoreServer("localhost", 9999, "/tmp").start();
    }
}
