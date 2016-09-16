package thecat.sample.peer2peer.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/peer2peer.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        primaryStage.setTitle("Peer2PeerFx");
        primaryStage.setScene(new Scene(root, 800, 400));
        primaryStage.getScene().getStylesheets().add
                (Main.class.getClassLoader().getResource("css/style.css").toExternalForm());
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if (null != controller) {
            if (null != controller.getAutoDiscovery()) {
                controller.getAutoDiscovery().stop();
            }

            if (null != controller.getSimpleServer()) {
                controller.getSimpleServer().stop();
            }
        }

        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

