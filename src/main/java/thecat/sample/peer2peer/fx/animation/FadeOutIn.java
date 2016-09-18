package thecat.sample.peer2peer.fx.animation;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.event.EventHandler;
import javafx.scene.Node;

import javafx.util.Duration;

/**
 * Created by thecat on 18/09/16.
 */
public class FadeOutIn {

    private Node node;
    private Duration fadeOut;
    private Duration fadeIn;
    private EventHandler onFadeOutFinished;

    private SequentialTransition seq;

    public FadeOutIn(Node node, Duration fadeOut, Duration fadeIn) {
        this.node = node;
        this.fadeOut = fadeOut;
        this.fadeIn = fadeIn;
    }

    public FadeOutIn(Node node, Duration fadeOut, Duration fadeIn, EventHandler onFadeOutFinished) {
        this.node = node;
        this.fadeOut = fadeOut;
        this.fadeIn = fadeIn;
        this.onFadeOutFinished = onFadeOutFinished;
    }

    private void setup() {
        FadeTransition fadeOutTransition = new FadeTransition(javafx.util.Duration.seconds(1));
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);
        fadeOutTransition.setInterpolator(Interpolator.EASE_OUT);
        fadeOutTransition.setNode(node);

        if (null != onFadeOutFinished) {
            fadeOutTransition.setOnFinished(onFadeOutFinished);
        }

        FadeTransition fadeInTransition = new FadeTransition(javafx.util.Duration.seconds(2));
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);
        fadeOutTransition.setInterpolator(Interpolator.EASE_IN);
        fadeInTransition.setNode(node);

        seq = new SequentialTransition();
        seq.getChildren().add(fadeOutTransition);
        seq.getChildren().add(fadeInTransition);
    }

    public void play() {
        setup();
        seq.play();
    }
}
