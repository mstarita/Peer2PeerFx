package thecat.sample.peer2peer.fx.animation;

import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 * Created by thecat on 18/09/16.
 */
public class ExecuteWithDelay {

    private final Duration sleepDuration;
    private final EventHandler onEndPause;

    private PauseTransition pauseTransition;

    public ExecuteWithDelay(Duration sleepDuration, EventHandler onEndPause) {
        this.sleepDuration = sleepDuration;
        this.onEndPause = onEndPause;
    }

    private void setup() {
        pauseTransition = new PauseTransition(sleepDuration);
        pauseTransition.setOnFinished(onEndPause);
    }

    public void play() {
        setup();
        pauseTransition.play();
    }

}
