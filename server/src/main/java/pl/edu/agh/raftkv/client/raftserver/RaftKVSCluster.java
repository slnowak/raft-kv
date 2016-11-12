package pl.edu.agh.raftkv.client.raftserver;

import io.atomix.catalyst.transport.Address;
import io.atomix.copycat.server.CopycatServer;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by novy on 12.11.16.
 */

public class RaftKVSCluster {

    private final Collection<NodeAddress> initialNodes;

    private RaftKVSCluster(Collection<NodeAddress> initialNodes) {
        if (numberOfInitialNodesEven(initialNodes)) {
            throw new IllegalArgumentException("Number of initial nodes must be odd");
        }
        this.initialNodes = initialNodes;
    }

    public static RaftKVSCluster withInitialNodes(Collection<NodeAddress> initialNodes) {
        return new RaftKVSCluster(initialNodes);
    }

    public static RaftKVSCluster withInitialNodes(NodeAddress... initialNodes) {
        return new RaftKVSCluster(Arrays.asList(initialNodes));
    }

    @SneakyThrows
    void initBy(CopycatServer server) {
        server.bootstrap(initialNodeAddresses()).join();
    }

    private Collection<Address> initialNodeAddresses() {
        return initialNodes.stream().map(NodeAddress::toURIString).map(Address::new).collect(Collectors.toList());
    }

    private static boolean numberOfInitialNodesEven(Collection<NodeAddress> initialNodes) {
        return initialNodes.size() % 2 == 0;
    }
}
