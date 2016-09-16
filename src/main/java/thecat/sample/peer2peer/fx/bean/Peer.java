package thecat.sample.peer2peer.fx.bean;

/**
 * Created by thecat on 15/09/16.
 */
public class Peer {

    private String name;
    private String ipAddress;

    public Peer(String name, String ipAddress) {
        this.name = name;
        this.ipAddress = ipAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Peer peer = (Peer) o;

        return ipAddress != null ? ipAddress.equals(peer.ipAddress) : peer.ipAddress == null;

    }

    @Override
    public int hashCode() {
        return ipAddress != null ? ipAddress.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name + " (" + ipAddress + ')';
    }
}
