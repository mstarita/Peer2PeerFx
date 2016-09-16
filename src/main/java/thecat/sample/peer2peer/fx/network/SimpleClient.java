package thecat.sample.peer2peer.fx.network;

import com.esotericsoftware.kryonet.Client;

/**
 * Created by thecat on 15/09/16.
 */
public class SimpleClient {

    public static void send(String peerIp, String data) throws Exception {
        Client client = new Client();

        client.start();

        client.connect(5000, peerIp, SimpleServer.DEFAULT_PORT);

        client.sendTCP(data);

        client.stop();
    }

}
