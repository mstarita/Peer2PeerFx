package thecat.sample.peer2peer.fx.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import javafx.scene.control.TextArea;
import net.gotev.autodiscovery.AutoDiscoveryPeer;

/**
 * Created by thecat on 15/09/16.
 */
public class SimpleServer {

    public static final int DEFAULT_PORT = 7171;
    private final TextArea receivedText;

    private Server server = null;
    private SimpleAutodiscovery autoDiscovery;

    public SimpleServer(SimpleAutodiscovery autoDiscovery, TextArea receivedText) {
        this.autoDiscovery = autoDiscovery;
        this.receivedText = receivedText;
    }

    public void start() throws Exception {
        if (null != server) {
            server.stop();
        }

        server = new Server();
        server.start();
        server.bind(DEFAULT_PORT);

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof String) {
                    String code = (String) object;

                    String peerAddress = connection.getRemoteAddressTCP().getAddress().getHostAddress();

                    StringBuilder sb = new StringBuilder();
                    for (AutoDiscoveryPeer aPeer : autoDiscovery.getPeers()) {
                        if (aPeer.getIpAddress().equals(peerAddress)) {
                            sb.append(aPeer.getParameter("peerName"));
                        }
                    }

                    sb.append(": ").append(code);

                    receivedText.setText(receivedText.getText() + '\n' + sb.toString());
                }
            }
        });
    }

    public void stop() {
        if (null != server) {
            server.stop();
        }
    }

}
