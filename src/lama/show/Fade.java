package lama.show;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/***
 * Ungenutzte Klasse zur Verwaltung von Animationen
 */
public class Fade {

    private final FadeTransition ft;
    public Fade(Node node)
    {
        this.ft = new FadeTransition(Duration.millis(400), node);
    }

    void fadeFrom()
    {
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setCycleCount(3);
        ft.setAutoReverse(false);
        ft.play();
    }
    void fadeTo()
    {
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setCycleCount(3);
        ft.setAutoReverse(false);
        ft.play();
    }
}

