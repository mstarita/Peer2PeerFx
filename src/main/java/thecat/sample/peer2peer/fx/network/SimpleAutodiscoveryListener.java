package thecat.sample.peer2peer.fx.network;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import jfxtras.scene.control.gauge.linear.SimpleMetroArcGauge;
import net.gotev.autodiscovery.AutoDiscovery;
import net.gotev.autodiscovery.AutoDiscoveryListener;
import net.gotev.autodiscovery.AutoDiscoveryPeer;
import thecat.sample.peer2peer.fx.bean.Peer;

/**
 * Created by thecat on 15/09/16.
 */
public class SimpleAutodiscoveryListener implements AutoDiscoveryListener {

    private AutoDiscovery autoDiscovery = null;
    private ListView<Peer> peerListView = null;
    private SimpleMetroArcGauge peerNumberGauge = null;

    public SimpleAutodiscoveryListener(AutoDiscovery autoDiscovery, ListView peerListView, SimpleMetroArcGauge peerNumberGauge) {
        this.autoDiscovery = autoDiscovery;
        this.peerListView = peerListView;
        this.peerNumberGauge = peerNumberGauge;
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

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Peer newPeer = new Peer(peer.getParameter("peerName"), peer.getIpAddress());

                peerListView.getItems().add(newPeer);

                peerNumberGauge.setValue(peerNumberGauge.getValue() + 1);
            }
        });
    }

    @Override
    public void removedPeer(AutoDiscoveryPeer peer) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                peerListView.getItems().clear();

                for (AutoDiscoveryPeer aPeer : autoDiscovery.getDiscoveredPeers()) {
                    Peer newPeer = new Peer(aPeer.getParameter("peerName"), aPeer.getIpAddress());

                    peerListView.getItems().add(newPeer);
                }

                double peerNumber = peerNumberGauge.getValue() - 1;
                if (peerNumber < 0) {
                    peerNumber = 0;
                }
                peerNumberGauge.setValue(peerNumber);
            }
        });
    }

}
