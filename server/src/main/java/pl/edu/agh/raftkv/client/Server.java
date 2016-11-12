package pl.edu.agh.raftkv.client;

import pl.edu.agh.raftkv.client.raftserver.KeyValueStoreServer;

/**
 * Created by novy on 12.11.16.
 */
public class Server {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 9999;
    private static final String DEFAULT_JOURNAL_DIR = "/tmp";

    public static void main(String[] args) {
        final KeyValueStoreServer server = new KeyValueStoreServer(
                host(args),
                port(args),
                journalDir(args)
        );
        server.start();
    }

    private static String host(String[] args) {
        return args.length > 0 ? args[0] : DEFAULT_HOST;
    }

    private static int port(String[] args) {
        return args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_PORT;
    }

    private static String journalDir(String[] args) {
        return args.length > 2 ? args[2] : DEFAULT_JOURNAL_DIR;
    }
}
