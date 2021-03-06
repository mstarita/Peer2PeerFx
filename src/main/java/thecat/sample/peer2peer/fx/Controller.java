package thecat.sample.peer2peer.fx;

import eu.hansolo.enzo.lcd.Lcd;
import eu.hansolo.enzo.lcd.LcdBuilder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import jfxtras.scene.control.gauge.linear.SimpleMetroArcGauge;
import net.gotev.autodiscovery.AutoDiscoveryPeer;
import org.controlsfx.control.PopOver;
import thecat.sample.peer2peer.fx.animation.ExecuteWithDelay;
import thecat.sample.peer2peer.fx.animation.FadeOutIn;
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

    @FXML private VBox leftPanelVBox;
    @FXML private Pane peerListPane;
    @FXML Pane receivedTextPane;

    private boolean joined = false;

    private SimpleAutodiscovery autoDiscovery;
    private SimpleServer simpleServer;

    private SimpleMetroArcGauge peerNumberGauge;
    private Lcd messagesLcd;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        peerNumberGauge = new SimpleMetroArcGauge();
        peerNumberGauge.setValue(0);
        peerNumberGauge.setMaxValue(10);
        peerNumberGauge.setMinValue(0);
        peerListPane.getChildren().add(peerNumberGauge);
        peerNumberGauge.toBack();

        messagesLcd = LcdBuilder
                .create()
                .title("Message received")
                .titleVisible(true)
                .value(0)
                .keepAspect(false)
                .valueFont(Lcd.LcdFont.ELEKTRA)
                .animated(true)
                .build();
        receivedTextPane.getChildren().add(messagesLcd);
        messagesLcd.toBack();

        setupUiForNetworkState(true);

    }

    private void setupUiForNetworkState(boolean init) {

        if (!init) {
            FadeOutIn fadeOutIn = new FadeOutIn(
                    peer_name,
                    Duration.seconds(1),
                    Duration.seconds(2),
                    event -> {
                        if (!joined) {
                            peer_name.setText("Join the network...");
                        } else {
                            peer_name.setText(peerNameField.getText());
                        }
                    });

            fadeOutIn.play();
        }

        joinButton.setVisible(!joined);
        leaveButton.setVisible(joined);

        sendToAllButton.setDisable(!joined);
        sendToPeerButton.setDisable(!joined);

        messagesLcd.setValue(0);
    }

    private void switchJoined() {

        joined = !joined;
        setupUiForNetworkState(false);

    }

    public void onLeave(ActionEvent actionEvent) {

        if (null != autoDiscovery) {

            try {
                autoDiscovery.stop();
                simpleServer.stop();

                peerList.getItems().clear();

                peerNumberGauge.setValue(0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            switchJoined();
        }

    }

    public void onJoin(ActionEvent actionEvent) {

        if (!peerNameField.getText().isEmpty()) {

            autoDiscovery = new SimpleAutodiscovery(peerNameField.getText(), peerList, peerNumberGauge);
            simpleServer = new SimpleServer(autoDiscovery, receivedText, messagesLcd);

            try {
                autoDiscovery.start();
                simpleServer.start();

                switchJoined();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {

            showToolTip("Please specify the peer name.");

        }

    }

    @FXML
    public void sendTo(ActionEvent actionEvent) {

        if (joined && null != autoDiscovery) {

            for (AutoDiscoveryPeer peer : autoDiscovery.getPeers()) {
                try {
                    SimpleClient.send(peer.getIpAddress(), sendText.getText());
                } catch (Exception ex) {
                    ex.printStackTrace();

                    // TODO fill on the app log panel the exception
                }
            }

            Platform.runLater(() -> {
                appendMessage();

                messagesLcd.setBlinking(true);
                ExecuteWithDelay blink = new ExecuteWithDelay(
                        Duration.seconds(2),
                        event -> {
                    messagesLcd.setBlinking(false);
                });
                blink.play();

                sendText.setText("");
            });
        } else {

            showToolTip("Please join the network...");

        }
    }

    @FXML
    public void sendToPeer(ActionEvent actionEvent) {

        if (joined) {
            if (null != peerList.getSelectionModel().getSelectedItem()) {
                try {

                    Peer peer = (Peer) peerList.getSelectionModel().getSelectedItem();
                    SimpleClient.send(peer.getIpAddress(), sendText.getText());

                } catch (Exception ex) {
                    ex.printStackTrace();

                    // TODO fill on the app log panel the exception
                }

                Platform.runLater(() -> {
                    appendMessage();
                    sendText.setText("");
                });

            }
        } else {

            showToolTip("Please join the network...");

        }
    }

    private void appendMessage() {
        StringBuilder sbMsg = new StringBuilder();
        sbMsg.append(autoDiscovery.getPeerName()).append(": ").append(sendText.getText());
        receivedText.setText(receivedText.getText() + '\n' + sbMsg.toString());

        messagesLcd.setValue(messagesLcd.getValue() + 1);

        receivedText.setScrollTop(Double.MAX_VALUE);
    }

    public SimpleAutodiscovery getAutoDiscovery() {
        return autoDiscovery;
    }

    public SimpleServer getSimpleServer() {
        return simpleServer;
    }

    private void showToolTip(String message) {
        Tooltip tooltip = new Tooltip();
        tooltip.setText(message);
        tooltip.setStyle("-fx-background-color: #7f7f7f; -fx-font-size: 24px; -fx-Text-fill: #fbfbfb;");
        tooltip.show(joinButton.getScene().getWindow());

        ExecuteWithDelay executeWithDelay = new ExecuteWithDelay(
                Duration.seconds(2),
                event -> {
                    tooltip.hide();
                }
        );
        executeWithDelay.play();
    }

}
