package pl.edu.agh.raftkv.kvstore;

import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.copycat.client.ConnectionStrategies;
import io.atomix.copycat.client.CopycatClient;
import io.atomix.copycat.client.RecoveryStrategies;
import io.atomix.copycat.client.ServerSelectionStrategies;
import lombok.SneakyThrows;

import java.util.Optional;

/**
 * Created by novy on 12.11.16.
 */
class RaftKVStoreClient implements KeyValueStoreClient {

    private final CopycatClient client;

    public RaftKVStoreClient(String serverHost, int serverPort) {
        client = CopycatClient.builder(new Address(serverHost, serverPort))
                .withTransport(new NettyTransport())
                .withConnectionStrategy(ConnectionStrategies.FIBONACCI_BACKOFF)
                .withRecoveryStrategy(RecoveryStrategies.RECOVER)
                .withServerSelectionStrategy(ServerSelectionStrategies.LEADER)
                .build();

        client.serializer().register(PutCommand.class, 1);
        client.serializer().register(GetQuery.class, 2);
        client.serializer().register(DeleteCommand.class, 3);

        client.connect().join();
    }

    @Override
    @SneakyThrows
    public void put(String key, Object value) {
        // todo fixme
        client.submit(new PutCommand(key, value)).get();
    }

    @Override
    @SneakyThrows
    public void delete(String key) {
        // todo fixme
        client.submit(new DeleteCommand(key)).get();
    }

    @Override
    public Optional<Object> get(String key) {
        return Optional.ofNullable(nullOrValue(key));
    }

    @SneakyThrows
    private Object nullOrValue(String key) {
        // todo fixme
        return client.submit(new GetQuery(key)).get();
    }
}
