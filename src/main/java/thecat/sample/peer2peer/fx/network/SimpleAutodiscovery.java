package thecat.sample.peer2peer.fx.network;

import javafx.scene.control.ListView;
import jfxtras.scene.control.gauge.linear.SimpleMetroArcGauge;
import net.gotev.autodiscovery.AutoDiscovery;
import net.gotev.autodiscovery.AutoDiscoveryPeer;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.List;

/**
 * Created by thecat on 15/09/16.
 */
public class SimpleAutodiscovery {

    private static final int UDP_PORT = 7777;
    private static final int SEND_BROADCAST_SECONDS = 5;
    private static final boolean DEBUG_ENABLED = false;

    private String peerName = null;
    private ListView peerListView = null;
    private SimpleMetroArcGauge peerNumberGauge = null;

    private AutoDiscovery autoDiscovery = new AutoDiscovery(UDP_PORT);

    public SimpleAutodiscovery(String peerName, ListView peerListView, SimpleMetroArcGauge peerNumberGauge) {
        this.peerName = peerName;
        this.peerListView = peerListView;
        this.peerNumberGauge = peerNumberGauge;
    }

    public void start() throws Exception {
        autoDiscovery = new AutoDiscovery(UDP_PORT);
        autoDiscovery.setDebugEnabled(DEBUG_ENABLED);
        autoDiscovery.setSendBroadcastEvery(SEND_BROADCAST_SECONDS);

        autoDiscovery.addListener(new SimpleAutodiscoveryListener(autoDiscovery, peerListView, peerNumberGauge));

        autoDiscovery.addParameter("peerName", peerName);

        autoDiscovery.start();
    }

    public void stop() throws Exception {
        autoDiscovery.stop();
    }

    public List<AutoDiscoveryPeer> getPeers() {
        return autoDiscovery.getDiscoveredPeers();
    }

    public String getPeerName() {
        return peerName;
    }
}
