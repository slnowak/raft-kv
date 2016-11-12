package pl.edu.agh.raftkv.client;

import pl.edu.agh.raftkv.client.raftserver.NodeAddress;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by novy on 12.11.16.
 */
class Context {
    private final Environment environment;

    Context() {
        environment = new Environment();
    }

    NodeAddress nodeAddress() {
        return environment
                .get("HOST_ADDRESS")
                .map(NodeAddress::new)
                .orElse(new NodeAddress("localhost", 9090));
    }

    Collection<NodeAddress> initialNodes() {
        return environment
                .get("INITIAL_CLUSTER_NODES")
                .map(clusterString -> clusterString.split(";"))
                .map(nodeStrings -> Stream.of(nodeStrings).map(NodeAddress::new).collect(Collectors.toList()))
                .orElse(Arrays.asList(
                        new NodeAddress("node1:9090"),
                        new NodeAddress("node2:9090"),
                        new NodeAddress("node3:9090"))
                );
    }

    private class Environment {
        Optional<String> get(String envKey) {
            return Optional.ofNullable(System.getenv(envKey));
        }
    }
}
