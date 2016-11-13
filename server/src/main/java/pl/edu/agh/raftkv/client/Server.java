package pl.edu.agh.raftkv.client;

import pl.edu.agh.raftkv.client.raftserver.RaftKVSCluster;
import pl.edu.agh.raftkv.client.raftserver.RaftKVSNode;

/**
 * Created by novy on 12.11.16.
 */
public class Server {

    public static void main(String[] args) {
        final Context ctx = new Context();

        final RaftKVSCluster cluster = RaftKVSCluster.withInitialNodes(ctx.initialNodes());
        final RaftKVSNode node = new RaftKVSNode(ctx.clientBinding(), ctx.clusterBinding());
        node.bootstrap(cluster);
    }
}
