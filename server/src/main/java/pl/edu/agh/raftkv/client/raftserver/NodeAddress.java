package pl.edu.agh.raftkv.client.raftserver;

/**
 * Created by novy on 12.11.16.
 */
public class NodeAddress {
    private final String address;

    public NodeAddress(String address) {
        this.address = address;
    }

    public NodeAddress(String address, int host) {
        this.address = address + ":" + host;
    }

    String toURIString() {
        return address;
    }
}
