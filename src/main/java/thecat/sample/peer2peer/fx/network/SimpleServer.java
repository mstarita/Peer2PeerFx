package thecat.sample.peer2peer.fx.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import eu.hansolo.enzo.lcd.Lcd;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.util.Duration;
import net.gotev.autodiscovery.AutoDiscoveryPeer;
import thecat.sample.peer2peer.fx.animation.ExecuteAfterPause;

/**
 * Created by thecat on 15/09/16.
 */
public class SimpleServer {

    public static final int DEFAULT_PORT = 7171;
    private final TextArea receivedText;
    private final Lcd messagesLcd;

    private Server server = null;
    private SimpleAutodiscovery autoDiscovery;

    public SimpleServer(SimpleAutodiscovery autoDiscovery, TextArea receivedText, Lcd messageLcd) {
        this.autoDiscovery = autoDiscovery;
        this.receivedText = receivedText;
        this.messagesLcd = messageLcd;
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
                            break;
                        }
                    }

                    if (sb.length() == 0) {
                        sb.append("???");
                    }
                    sb.append(": ").append(code);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            receivedText.setText(receivedText.getText() + '\n' + sb.toString());
                            messagesLcd.setValue(messagesLcd.getValue() + 1);

                            receivedText.setScrollTop(Double.MAX_VALUE);

                            messagesLcd.setBlinking(true);
                            ExecuteAfterPause blink = new ExecuteAfterPause(
                                    Duration.seconds(2),
                                    event -> {
                                messagesLcd.setBlinking(false);
                            });
                            blink.play();
                        }
                    });

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
