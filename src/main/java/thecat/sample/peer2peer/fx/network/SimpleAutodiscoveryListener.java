package thecat.sample.peer2peer.fx.network;

import javafx.scene.control.ListView;
import net.gotev.autodiscovery.AutoDiscovery;
import net.gotev.autodiscovery.AutoDiscoveryListener;
import net.gotev.autodiscovery.AutoDiscoveryPeer;
import thecat.sample.peer2peer.fx.bean.Peer;

/**
 * Created by thecat on 15/09/16.
 */
public class SimpleAutodiscoveryListener implements AutoDiscoveryListener {

    private AutoDiscovery autoDiscovery = null;
    private ListView<Peer> peerListView;

    public SimpleAutodiscoveryListener(AutoDiscovery autoDiscovery, ListView peerListView) {
        this.autoDiscovery = autoDiscovery;
        this.peerListView = peerListView;
    }

    @Override
    public void debug(String debug) {
        System.out.println("debug: " + debug);
    }

    @Override
    public void errorOccurred(AutoDiscovery arg0, Throwable arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void newPeer(AutoDiscoveryPeer peer) {

        Peer newPeer = new Peer(peer.getParameter("peerName"), peer.getIpAddress());

        peerListView.getItems().add(newPeer);

    }

    @Override
    public void removedPeer(AutoDiscoveryPeer peer) {
        peerListView.getItems().clear();

        for (AutoDiscoveryPeer aPeer : autoDiscovery.getDiscoveredPeers()) {
            Peer newPeer = new Peer(aPeer.getParameter("peerName"), aPeer.getIpAddress());

            peerListView.getItems().add(newPeer);
        }

    }

}
