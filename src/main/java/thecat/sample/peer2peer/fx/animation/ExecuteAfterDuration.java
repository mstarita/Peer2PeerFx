package thecat.sample.peer2peer.fx.animation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Created by thecat on 18/09/16.
 */
public class ExecuteAfterDuration {

    private final Node node;
    private final Duration sleepDuration;
    private final EventHandler onEndDuration;

    private Timeline timeline;

    public ExecuteAfterDuration(Node node, Duration sleepDuration, EventHandler onEndDuration) {
        this.node = node;
        this.sleepDuration = sleepDuration;
        this.onEndDuration = onEndDuration;
    }

    private void setup() {
        timeline = new Timeline(new KeyFrame(sleepDuration));
        timeline.setOnFinished(onEndDuration);

    }

    public void play() {
        setup();
        timeline.play();
    }

}
