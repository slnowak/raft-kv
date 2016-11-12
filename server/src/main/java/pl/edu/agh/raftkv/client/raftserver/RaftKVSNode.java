package pl.edu.agh.raftkv.client.raftserver;

import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.copycat.server.CopycatServer;
import io.atomix.copycat.server.storage.Storage;
import pl.edu.agh.raftkv.protocol.DeleteCommand;
import pl.edu.agh.raftkv.protocol.GetQuery;
import pl.edu.agh.raftkv.protocol.PutCommand;

import java.time.Duration;

/**
 * Created by novy on 12.11.16.
 */

public class RaftKVSNode {
    private final NodeAddress address;
    private final String journalDirectory;

    private CopycatServer server;

    public RaftKVSNode(NodeAddress address, String journalDirectory) {
        this.address = address;
        this.journalDirectory = journalDirectory;
    }

    public RaftKVSNode(NodeAddress address) {
        this(address, "/tmp");
    }

    public void bootstrap(RaftKVSCluster cluster) {
        final CopycatServer node = initializedCopycatNode();
        cluster.initBy(node);
    }

    private CopycatServer initializedCopycatNode() {
        final CopycatServer server = createCopycatNode();
        registerCommands(server);
        return server;
    }

    private CopycatServer createCopycatNode() {
        return CopycatServer.builder(new Address(address.toURIString()))
                .withStateMachine(RaftKVStateMachine::new)
                .withTransport(new NettyTransport())
                .withStorage(Storage.builder()
                        .withDirectory(journalDirectory)
                        .withMaxSegmentSize(1024 * 1024 * 32)
                        .withMinorCompactionInterval(Duration.ofMinutes(1))
                        .withMajorCompactionInterval(Duration.ofMinutes(15))
                        .build())
                .build();
    }

    private void registerCommands(CopycatServer server) {
        server.serializer().register(PutCommand.class, 1);
        server.serializer().register(GetQuery.class, 2);
        server.serializer().register(DeleteCommand.class, 3);
    }

    public void stop() {
        server.shutdown();
    }

}
