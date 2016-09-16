package thecat.sample.peer2peer.fx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import net.gotev.autodiscovery.AutoDiscoveryPeer;
import org.controlsfx.control.PopOver;
import thecat.sample.peer2peer.fx.bean.Peer;
import thecat.sample.peer2peer.fx.network.SimpleAutodiscovery;
import thecat.sample.peer2peer.fx.network.SimpleClient;
import thecat.sample.peer2peer.fx.network.SimpleServer;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private Label peer_name;

    @FXML private TextField peerNameField;

    @FXML private Button joinButton;
    @FXML private Button leaveButton;

    @FXML private ListView peerList;

    @FXML private TextArea receivedText;
    @FXML private TextArea sendText;

    @FXML private Button sendToAllButton;
    @FXML private Button sendToPeerButton;

    private boolean joined = false;

    private SimpleAutodiscovery autoDiscovery;
    private SimpleServer simpleServer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setupUiForNetworkState();

    }

    private void setupUiForNetworkState() {

        if (!joined) {
            peer_name.setText("Join the network...");
        } else {
            peer_name.setText(peerNameField.getText());
        }

        joinButton.setVisible(!joined);
        leaveButton.setVisible(joined);

        sendToAllButton.setDisable(!joined);
        sendToPeerButton.setDisable(!joined);

    }

    private void switchJoined() {

        joined = !joined;
        setupUiForNetworkState();

    }

    public void onLeave(ActionEvent actionEvent) {

        if (null != autoDiscovery) {

            try {
                autoDiscovery.stop();
                simpleServer.stop();

                peerList.getItems().clear();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            switchJoined();
        }

    }

    public void onJoin(ActionEvent actionEvent) {

        if (!peerNameField.getText().isEmpty()) {

            autoDiscovery = new SimpleAutodiscovery(peerNameField.getText(), peerList);
            simpleServer = new SimpleServer(autoDiscovery, receivedText);

            try {
                autoDiscovery.start();
                simpleServer.start();

                switchJoined();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            PopOver popOver = createPopOver("Please specify the peer name");
            popOver.show(peerNameField);
        }

    }

    public void sendTo(ActionEvent actionEvent) {

        if (joined && null != autoDiscovery) {

            try {
                for (AutoDiscoveryPeer peer : autoDiscovery.getPeers()) {
                    SimpleClient.send(peer.getIpAddress(), sendText.getText());
                }

                appendMessage();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            PopOver popOver = createPopOver("Please join the network...");
            popOver.show(sendToAllButton);
        }
    }

    public void sendToPeer(ActionEvent actionEvent) {

        if (joined) {
            if (null != peerList.getSelectionModel().getSelectedItem()) {
                try {

                    Peer peer = (Peer) peerList.getSelectionModel().getSelectedItem();
                    SimpleClient.send(peer.getIpAddress(), sendText.getText());

                    appendMessage();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            PopOver popOver = createPopOver("Please join the network...");
            popOver.show(sendToPeerButton);
        }
    }

    private void appendMessage() {
        StringBuilder sbMsg = new StringBuilder();
        sbMsg.append(autoDiscovery.getPeerName()).append(": ").append(sendText.getText());
        receivedText.setText(receivedText.getText() + '\n' + sbMsg);
    }

    public SimpleAutodiscovery getAutoDiscovery() {
        return autoDiscovery;
    }

    public SimpleServer getSimpleServer() {
        return simpleServer;
    }

    private PopOver createPopOver(String message) {
        PopOver popOver = new PopOver();
        popOver.setAutoHide(true);
        popOver.setAnimated(true);

        popOver.setContentNode(new Text(message));
        return popOver;
    }

}
